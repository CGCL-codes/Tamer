package de.moonflower.jfritz.utils.reverselookup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.moonflower.jfritz.struct.Person;
import de.moonflower.jfritz.struct.PhoneNumber;
import de.moonflower.jfritz.struct.ReverseLookupSite;
import de.moonflower.jfritz.utils.Debug;
import de.moonflower.jfritz.utils.HTMLUtil;
import de.moonflower.jfritz.utils.JFritzUtils;

/**
	 * 
	 * @author Brian
	 * 
	 */
public class LookupThread extends Thread {

    private static final String userAgent = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.1.13) Gecko/20080208 Mandriva/2.0.0.13-1mdv2008.1 (2008.1) Firefox/2.0.0.13";

    private static final int dataLength = 30000;

    private LookupRequest currentRequest;

    private boolean threadSuspended, quit, terminate, terminated;

    private Person result;

    private static String nummer, urlstr, header, charSet, str, prefix, firstname, company, lastname, street, zipcode, city;

    private static String[] data;

    private static Vector<ReverseLookupSite> rls_list;

    private static ReverseLookupSite rls;

    private static String[] patterns;

    private static URL url;

    private static URLConnection con;

    private static int ac_length;

    /**
	 * sets the default thread state to active
	 *
	 */
    public LookupThread(boolean quitOnDone) {
        threadSuspended = false;
        terminate = false;
        terminated = false;
        quit = quitOnDone;
    }

    /**
	 * This method iterates over all lookup requests and stops once
	 * no more are present, in which case it notifies ReverseLookup
	 * that it is done. If threadSuspended is true the thread while wait
	 * it is reactivated to continue doing lookups.
	 * 
	 */
    public void run() {
        while (!terminate) {
            try {
                if (ReverseLookup.getRequestsCount() == 0 && quit) break;
                while ((!terminate) && (threadSuspended || ReverseLookup.getRequestsCount() == 0)) {
                    ReverseLookup.lookupDone();
                    synchronized (this) {
                        wait();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (!terminate) {
                currentRequest = ReverseLookup.getNextRequest();
                result = lookup(currentRequest.number, currentRequest.lookupSite);
                ReverseLookup.personFound(result);
                try {
                    sleep(2000);
                } catch (Exception e) {
                    break;
                }
            }
        }
        Debug.info("Lookup thread  has quit");
        terminated = true;
    }

    public synchronized void terminate() {
        terminate = true;
        resumeLookup();
    }

    public synchronized boolean isTerminated() {
        return terminated;
    }

    public synchronized void suspendLookup() {
        Debug.info("suspending lookup thread");
        threadSuspended = true;
    }

    public synchronized void resumeLookup() {
        threadSuspended = false;
        notify();
        Debug.info("resuming lookup thread again");
    }

    public synchronized boolean isSuspended() {
        return threadSuspended;
    }

    /**
	 * This function handles the actual "lookup" part. It uses the data loaded
	 * from number/international/revserlookup.xml to iterate over a series of
	 * websites and over series of patterns for processing each website
	 * 
	 * @see de.moonflower.jfritz.struct.ReverseLookupSite
	 * 
	 * 
	 * @param number number to be looked up
	 * @return a new person object containing the results of the search
	 */
    static synchronized Person lookup(PhoneNumber number, String siteName) {
        Vector<Person> foundPersons = new Vector<Person>(5);
        if (number.isFreeCall()) {
            Person p = new Person("", "FreeCall");
            p.addNumber(number);
            foundPersons.add(p);
        } else if (number.isSIPNumber() || number.isQuickDial()) {
            Person p = new Person();
            p.addNumber(number);
            foundPersons.add(p);
        } else if (ReverseLookup.rlsMap.containsKey(number.getCountryCode())) {
            nummer = number.getAreaNumber();
            rls_list = ReverseLookup.rlsMap.get(number.getCountryCode());
            Debug.info("Begin reverselookup for: " + nummer);
            if (nummer.startsWith(number.getCountryCode())) nummer = nummer.substring(number.getCountryCode().length());
            city = "";
            for (int i = 0; i < rls_list.size(); i++) {
                yield();
                rls = rls_list.get(i);
                if (!siteName.equals("") && !siteName.equals(rls.getName())) {
                    Debug.warning("This lookup should be done using a specific site, skipping");
                    continue;
                }
                prefix = rls.getPrefix();
                ac_length = rls.getAreaCodeLength();
                if (!nummer.startsWith(prefix)) nummer = prefix + nummer;
                urlstr = rls.getURL();
                if (urlstr.contains("$AREACODE")) {
                    urlstr = urlstr.replaceAll("\\$AREACODE", nummer.substring(prefix.length(), ac_length + prefix.length()));
                    urlstr = urlstr.replaceAll("\\$NUMBER", nummer.substring(prefix.length() + ac_length));
                } else if (urlstr.contains("$PFXAREACODE")) {
                    urlstr = urlstr.replaceAll("\\$PFXAREACODE", nummer.substring(0, prefix.length() + ac_length));
                    urlstr = urlstr.replaceAll("\\$NUMBER", nummer.substring(prefix.length() + ac_length));
                } else urlstr = urlstr.replaceAll("\\$NUMBER", nummer);
                Debug.info("Reverse lookup using: " + urlstr);
                url = null;
                data = new String[dataLength];
                try {
                    url = new URL(urlstr);
                    if (url != null) {
                        try {
                            con = url.openConnection();
                            con.setConnectTimeout(5000);
                            con.setReadTimeout(15000);
                            con.addRequestProperty("User-Agent", userAgent);
                            con.connect();
                            header = "";
                            charSet = "";
                            for (int j = 0; ; j++) {
                                String headerName = con.getHeaderFieldKey(j);
                                String headerValue = con.getHeaderField(j);
                                if (headerName == null && headerValue == null) {
                                    break;
                                }
                                if ("content-type".equalsIgnoreCase(headerName)) {
                                    String[] split = headerValue.split(";", 2);
                                    for (int k = 0; k < split.length; k++) {
                                        if (split[k].trim().toLowerCase().startsWith("charset=")) {
                                            String[] charsetSplit = split[k].split("=");
                                            charSet = charsetSplit[1].trim();
                                        }
                                    }
                                }
                                header += headerName + ": " + headerValue + " | ";
                            }
                            Debug.debug("Header of " + rls.getName() + ":" + header);
                            Debug.debug("CHARSET : " + charSet);
                            BufferedReader d;
                            if (charSet.equals("")) {
                                d = new BufferedReader(new InputStreamReader(con.getInputStream(), "ISO-8859-1"));
                            } else {
                                d = new BufferedReader(new InputStreamReader(con.getInputStream(), charSet));
                            }
                            int lines = 0;
                            while (null != ((str = d.readLine()))) {
                                data[lines] = str;
                                yield();
                                if (lines >= dataLength) {
                                    System.err.println("Result > " + dataLength + " Lines");
                                    break;
                                }
                                lines++;
                            }
                            d.close();
                            Debug.info("Begin processing response from " + rls.getName());
                            for (int j = 0; j < rls.size(); j++) {
                                yield();
                                firstname = "";
                                lastname = "";
                                company = "";
                                street = "";
                                zipcode = "";
                                city = "";
                                Person p = null;
                                patterns = rls.getEntry(j);
                                Pattern namePattern = null;
                                Pattern streetPattern = null;
                                Pattern cityPattern = null;
                                Pattern zipcodePattern = null;
                                Pattern firstnamePattern = null;
                                Pattern lastnamePattern = null;
                                Matcher nameMatcher = null;
                                Matcher streetMatcher = null;
                                Matcher cityMatcher = null;
                                Matcher zipcodeMatcher = null;
                                Matcher firstnameMatcher = null;
                                Matcher lastnameMatcher = null;
                                if (!patterns[ReverseLookupSite.NAME].equals("") && (patterns[ReverseLookupSite.FIRSTNAME].equals("") && patterns[ReverseLookupSite.LASTNAME].equals(""))) {
                                    namePattern = Pattern.compile(patterns[ReverseLookupSite.NAME]);
                                }
                                if (!patterns[ReverseLookupSite.STREET].equals("")) {
                                    streetPattern = Pattern.compile(patterns[ReverseLookupSite.STREET]);
                                }
                                if (!patterns[ReverseLookupSite.CITY].equals("")) {
                                    cityPattern = Pattern.compile(patterns[ReverseLookupSite.CITY]);
                                }
                                if (!patterns[ReverseLookupSite.ZIPCODE].equals("")) {
                                    zipcodePattern = Pattern.compile(patterns[ReverseLookupSite.ZIPCODE]);
                                }
                                if (!patterns[ReverseLookupSite.FIRSTNAME].equals("")) {
                                    firstnamePattern = Pattern.compile(patterns[ReverseLookupSite.FIRSTNAME]);
                                }
                                if (!patterns[ReverseLookupSite.LASTNAME].equals("")) {
                                    lastnamePattern = Pattern.compile(patterns[ReverseLookupSite.LASTNAME]);
                                }
                                for (int line = 0; line < dataLength; line++) {
                                    if (data[line] != null) {
                                        int spaceAlternative = 160;
                                        data[line] = data[line].replaceAll(new Character((char) spaceAlternative).toString(), " ");
                                        if (lastnamePattern != null) {
                                            lastnameMatcher = lastnamePattern.matcher(data[line]);
                                            if (lastnameMatcher.find()) {
                                                str = "";
                                                for (int k = 1; k <= lastnameMatcher.groupCount(); k++) {
                                                    if (lastnameMatcher.group(k) != null) str = str + lastnameMatcher.group(k).trim() + " ";
                                                }
                                                lastname = JFritzUtils.removeLeadingSpaces(HTMLUtil.stripEntities(str));
                                                lastname = lastname.trim();
                                                lastname = lastname.replaceAll(",", "");
                                                lastname = lastname.replaceAll("%20", " ");
                                                lastname = JFritzUtils.replaceSpecialCharsUTF(lastname);
                                                lastname = JFritzUtils.removeLeadingSpaces(HTMLUtil.stripEntities(lastname));
                                                lastname = JFritzUtils.removeDuplicateWhitespace(lastname);
                                                if ("lastname".equals(patterns[ReverseLookupSite.FIRSTOCCURANCE])) {
                                                    p = new Person();
                                                    p.addNumber(number.getIntNumber(), "home");
                                                    foundPersons.add(p);
                                                }
                                                if (p != null) {
                                                    p.setLastName(lastname);
                                                }
                                            }
                                        }
                                        yield();
                                        if (firstnamePattern != null) {
                                            firstnameMatcher = firstnamePattern.matcher(data[line]);
                                            if (firstnameMatcher.find()) {
                                                str = "";
                                                for (int k = 1; k <= firstnameMatcher.groupCount(); k++) {
                                                    if (firstnameMatcher.group(k) != null) str = str + firstnameMatcher.group(k).trim() + " ";
                                                }
                                                firstname = JFritzUtils.removeLeadingSpaces(HTMLUtil.stripEntities(str));
                                                firstname = firstname.trim();
                                                firstname = firstname.replaceAll(",", "");
                                                firstname = firstname.replaceAll("%20", " ");
                                                firstname = JFritzUtils.replaceSpecialCharsUTF(firstname);
                                                firstname = JFritzUtils.removeLeadingSpaces(HTMLUtil.stripEntities(firstname));
                                                firstname = JFritzUtils.removeDuplicateWhitespace(firstname);
                                                if ("firstname".equals(patterns[ReverseLookupSite.FIRSTOCCURANCE])) {
                                                    p = new Person();
                                                    p.addNumber(number.getIntNumber(), "home");
                                                    foundPersons.add(p);
                                                }
                                                if (p != null) {
                                                    p.setFirstName(firstname);
                                                }
                                            }
                                        }
                                        yield();
                                        if (namePattern != null) {
                                            nameMatcher = namePattern.matcher(data[line]);
                                            if (nameMatcher.find()) {
                                                str = "";
                                                for (int k = 1; k <= nameMatcher.groupCount(); k++) {
                                                    if (nameMatcher.group(k) != null) str = str + nameMatcher.group(k).trim() + " ";
                                                }
                                                String[] split;
                                                split = str.split(" ", 2);
                                                lastname = JFritzUtils.removeLeadingSpaces(HTMLUtil.stripEntities(split[0]));
                                                lastname = lastname.trim();
                                                lastname = lastname.replaceAll(",", "");
                                                lastname = lastname.replaceAll("%20", " ");
                                                lastname = JFritzUtils.replaceSpecialCharsUTF(lastname);
                                                lastname = JFritzUtils.removeLeadingSpaces(HTMLUtil.stripEntities(lastname));
                                                lastname = JFritzUtils.removeDuplicateWhitespace(lastname);
                                                if (split[1].length() > 0) {
                                                    firstname = HTMLUtil.stripEntities(split[1]);
                                                    if ((firstname.indexOf("  ") > -1) && (firstname.indexOf("  u.") == -1)) {
                                                        company = JFritzUtils.removeLeadingSpaces(firstname.substring(firstname.indexOf("  ")).trim());
                                                        firstname = JFritzUtils.removeLeadingSpaces(firstname.substring(0, firstname.indexOf("  ")).trim());
                                                    } else {
                                                        firstname = JFritzUtils.removeLeadingSpaces(firstname.replaceAll("  u. ", " und "));
                                                    }
                                                }
                                                firstname = firstname.replaceAll("%20", " ");
                                                firstname = JFritzUtils.replaceSpecialCharsUTF(firstname);
                                                firstname = JFritzUtils.removeLeadingSpaces(HTMLUtil.stripEntities(firstname));
                                                firstname = JFritzUtils.removeDuplicateWhitespace(firstname);
                                                firstname = firstname.trim();
                                                company = company.replaceAll("%20", " ");
                                                company = JFritzUtils.replaceSpecialCharsUTF(company);
                                                company = JFritzUtils.removeLeadingSpaces(HTMLUtil.stripEntities(company));
                                                company = JFritzUtils.removeDuplicateWhitespace(company);
                                                company = company.trim();
                                                if ("name".equals(patterns[ReverseLookupSite.FIRSTOCCURANCE])) {
                                                    p = new Person();
                                                    if (company.length() > 0) {
                                                        p.addNumber(number.getIntNumber(), "business");
                                                    } else {
                                                        p.addNumber(number.getIntNumber(), "home");
                                                    }
                                                    foundPersons.add(p);
                                                }
                                                if (p != null) {
                                                    p.setFirstName(firstname);
                                                    p.setLastName(lastname);
                                                    p.setCompany(company);
                                                }
                                            }
                                        }
                                        yield();
                                        if (streetPattern != null) {
                                            streetMatcher = streetPattern.matcher(data[line]);
                                            if (streetMatcher.find()) {
                                                str = "";
                                                for (int k = 1; k <= streetMatcher.groupCount(); k++) {
                                                    if (streetMatcher.group(k) != null) str = str + streetMatcher.group(k).trim() + " ";
                                                }
                                                street = str.replaceAll("%20", " ");
                                                street = JFritzUtils.replaceSpecialCharsUTF(street);
                                                street = JFritzUtils.removeLeadingSpaces(HTMLUtil.stripEntities(street));
                                                street = JFritzUtils.removeDuplicateWhitespace(street);
                                                street = street.trim();
                                                if ("street".equals(patterns[ReverseLookupSite.FIRSTOCCURANCE])) {
                                                    p = new Person();
                                                    p.addNumber(number.getIntNumber(), "home");
                                                    foundPersons.add(p);
                                                }
                                                if (p != null) {
                                                    p.setStreet(street);
                                                }
                                            }
                                        }
                                        yield();
                                        if (cityPattern != null) {
                                            cityMatcher = cityPattern.matcher(data[line]);
                                            if (cityMatcher.find()) {
                                                str = "";
                                                for (int k = 1; k <= cityMatcher.groupCount(); k++) {
                                                    if (cityMatcher.group(k) != null) str = str + cityMatcher.group(k).trim() + " ";
                                                }
                                                city = str.replaceAll("%20", " ");
                                                city = JFritzUtils.replaceSpecialCharsUTF(city);
                                                city = JFritzUtils.removeLeadingSpaces(HTMLUtil.stripEntities(city));
                                                city = JFritzUtils.removeDuplicateWhitespace(city);
                                                city = city.trim();
                                                if ("city".equals(patterns[ReverseLookupSite.FIRSTOCCURANCE])) {
                                                    p = new Person();
                                                    p.addNumber(number.getIntNumber(), "home");
                                                    foundPersons.add(p);
                                                }
                                                if (p != null) {
                                                    p.setCity(city);
                                                }
                                            }
                                        }
                                        yield();
                                        if (zipcodePattern != null) {
                                            zipcodeMatcher = zipcodePattern.matcher(data[line]);
                                            if (zipcodeMatcher.find()) {
                                                str = "";
                                                for (int k = 1; k <= zipcodeMatcher.groupCount(); k++) {
                                                    if (zipcodeMatcher.group(k) != null) str = str + zipcodeMatcher.group(k).trim() + " ";
                                                }
                                                zipcode = str.replaceAll("%20", " ");
                                                zipcode = JFritzUtils.replaceSpecialCharsUTF(zipcode);
                                                zipcode = JFritzUtils.removeLeadingSpaces(HTMLUtil.stripEntities(zipcode));
                                                zipcode = JFritzUtils.removeDuplicateWhitespace(zipcode);
                                                zipcode = zipcode.trim();
                                                if ("zipcode".equals(patterns[ReverseLookupSite.FIRSTOCCURANCE])) {
                                                    p = new Person();
                                                    p.addNumber(number.getIntNumber(), "home");
                                                    foundPersons.add(p);
                                                }
                                                if (p != null) {
                                                    p.setPostalCode(zipcode);
                                                }
                                            }
                                        }
                                    }
                                }
                                if (!firstname.equals("") || !lastname.equals("") || !company.equals("")) break;
                            }
                            yield();
                            if (!firstname.equals("") || !lastname.equals("") || !company.equals("")) {
                                if (city.equals("")) {
                                    if (number.getCountryCode().equals(ReverseLookup.GERMANY_CODE)) city = ReverseLookupGermany.getCity(nummer); else if (number.getCountryCode().equals(ReverseLookup.AUSTRIA_CODE)) city = ReverseLookupAustria.getCity(nummer); else if (number.getCountryCode().startsWith(ReverseLookup.USA_CODE)) city = ReverseLookupUnitedStates.getCity(nummer); else if (number.getCountryCode().startsWith(ReverseLookup.TURKEY_CODE)) city = ReverseLookupTurkey.getCity(nummer);
                                }
                                return foundPersons.get(0);
                            }
                        } catch (IOException e1) {
                            Debug.error("Error while retrieving " + urlstr);
                        }
                    }
                } catch (MalformedURLException e) {
                    Debug.error("URL invalid: " + urlstr);
                }
            }
            yield();
            Debug.warning("No match for " + nummer + " found");
            if (city.equals("")) {
                if (number.getCountryCode().equals(ReverseLookup.GERMANY_CODE)) city = ReverseLookupGermany.getCity(nummer); else if (number.getCountryCode().equals(ReverseLookup.AUSTRIA_CODE)) city = ReverseLookupAustria.getCity(nummer); else if (number.getCountryCode().startsWith(ReverseLookup.USA_CODE)) city = ReverseLookupUnitedStates.getCity(nummer); else if (number.getCountryCode().startsWith(ReverseLookup.TURKEY_CODE)) city = ReverseLookupTurkey.getCity(nummer);
            }
            Person p = new Person("", "", "", "", "", city, "", "");
            p.addNumber(number.getAreaNumber(), "home");
            return p;
        } else {
            Debug.warning("No reverse lookup sites for: " + number.getCountryCode());
            Person p = new Person();
            p.addNumber(number.getAreaNumber(), "home");
            if (number.getCountryCode().equals(ReverseLookup.GERMANY_CODE)) city = ReverseLookupGermany.getCity(number.getIntNumber()); else if (number.getCountryCode().equals(ReverseLookup.AUSTRIA_CODE)) city = ReverseLookupAustria.getCity(number.getIntNumber()); else if (number.getCountryCode().startsWith(ReverseLookup.USA_CODE)) city = ReverseLookupUnitedStates.getCity(number.getIntNumber()); else if (number.getCountryCode().startsWith(ReverseLookup.TURKEY_CODE)) city = ReverseLookupTurkey.getCity(number.getIntNumber());
            p.setCity(city);
            return p;
        }
        return new Person("not found", "Person");
    }
}
