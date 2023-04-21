package org.quantumleaphealth.transform;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Iterator;
import org.quantumleaphealth.model.trial.Contact;
import org.quantumleaphealth.model.trial.Geocoding;
import org.quantumleaphealth.model.trial.Site;
import org.quantumleaphealth.model.trial.Trial;
import org.quantumleaphealth.model.trial.TrialSite;

/**
 * Writes trial data to delimited UTF8-encoded text files.
 * @author Tom Bechtold
 * @version 2008-09-12
 */
public class TrialDataFileReporter implements TrialDataReporter {

    /**
     * Destination of trial data, guaranteed to be non-<code>null</code>
     */
    private PrintStream trialStream = System.out;

    /**
     * Destination of trial-site data, guaranteed to be non-<code>null</code>
     */
    private PrintStream trialSiteStream = System.out;

    /**
     * Destination of site data, guaranteed to be non-<code>null</code>
     */
    private PrintStream siteStream = System.out;

    /**
     * Destination of contact data, guaranteed to be non-<code>null</code>
     */
    private PrintStream contactStream = System.out;

    /**
     * UTF-8 character encoding
     */
    private static final String UTF8 = "UTF-8";

    /**
     * Separates fields
     */
    private static final char SEPARATOR = '|';

    /**
     * Represents a <code>null</code> field
     */
    private static final String NULL_FIELD = "\\N";

    /**
     * Format date fields using default date formatter
     */
    private static final DateFormat DATEFORMATTER = DateFormat.getDateInstance();

    /**
     * Abbreviation for United States
     */
    private static final String UNITED_STATES = "US";

    /**
     * Sets the file name for the trial data
     * @param trialFileName the file name for the trial data
     * @throws IllegalArgumentException if file name is not writable
     */
    public void setTrialFileName(String trialFileName) throws IllegalArgumentException {
        trialStream = getPrintStream(trialFileName);
    }

    /**
     * Sets the file name for the trial site data
     * @param trialSiteFileName the file name for the trial site data
     * @throws IllegalArgumentException if file name is not writable
     */
    public void setTrialSiteFileName(String trialSiteFileName) throws IllegalArgumentException {
        trialSiteStream = getPrintStream(trialSiteFileName);
    }

    /**
     * Sets the file name for the site data
     * @param siteFileName the file name for the site data
     * @throws IllegalArgumentException if file name is not writable
     */
    public void setSiteFileName(String siteFileName) throws IllegalArgumentException {
        siteStream = getPrintStream(siteFileName);
    }

    /**
     * Sets the file name for the contact data
     * @param contactFileName the file name for the contact data
     * @throws IllegalArgumentException if file name is not writable
     */
    public void setContactFileName(String contactFileName) throws IllegalArgumentException {
        contactStream = getPrintStream(contactFileName);
    }

    /**
     * Close output streams
     * @see org.quantumleaphealth.transform.TrialDataReporter#close()
     */
    public void close() {
        try {
            if (trialStream != null) trialStream.close();
        } catch (Throwable throwable) {
        }
        try {
            if (trialSiteStream != null) trialSiteStream.close();
        } catch (Throwable throwable) {
        }
        try {
            if (siteStream != null) siteStream.close();
        } catch (Throwable throwable) {
        }
        try {
            if (contactStream != null) contactStream.close();
        } catch (Throwable throwable) {
        }
    }

    /**
     * Close output streams
     * @see java.lang.Object#finalize()
     * @see #close()
     */
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    /**
     * @param fileName the file to write to
     * @return the print stream that writes to the file
     * @throws IllegalArgumentException if the specified file cannot be written to 
     *         or no print stream specified
     */
    private static PrintStream getPrintStream(String fileName) throws IllegalArgumentException {
        if ((fileName == null) || (fileName.trim().length() == 0)) throw new IllegalArgumentException("Must specify file name");
        try {
            return new PrintStream(fileName, UTF8);
        } catch (FileNotFoundException fileNotFoundException) {
            throw new IllegalArgumentException(fileNotFoundException);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new IllegalStateException(unsupportedEncodingException);
        }
    }

    /**
     * Writes a trial's fields to one line of a delimited text file and those of
     * its <code>TrialSite</code>s to multiple lines of a delimited text file.
     * This method prints <b>only</b> the following <code>Trial</code> fields to
     * <code>trialStream</code> with the specified maximum widths:
     * <ul>
     * <li><code>id</code></li>
     * <li><code>registry</code></li>
     * <li><code>primaryId</code> (22)</li>
     * <li><code>phase</code></li>
     * <li><code>sponsor</code> (50)</li>
     * <li><code>name</code> (63)</li>
     * <li><code>title</code> (255)</li>
     * <li><code>purpose</code> (255)</li>
     * <li><code>treatmentPlan</code></li>
     * <li><code>lastRegistered</code></li>
     * </ul>
     * This method prints the following <code>TrialSite</code> fields to
     * <code>trialSiteStream</code>:
     * <ul>
     * <li><code>trial.id</code></li>
     * <li><code>site.id</code></li>
     * <li><code>contact.id</code></li>
     * <li><code>principalInvestigator.id</code></li>
     * </ul>
     * 
     * @param trial the trial
     * @see org.quantumleaphealth.transform.TrialDataReporter#outputTrial(org.quantumleaphealth.model.trial.Trial)
     * @throws IllegalArgumentException if <code>trial</code> or its sites are not available
     */
    public void outputTrial(Trial trial) throws IllegalArgumentException {
        if (trial == null) throw new IllegalArgumentException("must specify trial");
        Long trialId = trial.getId();
        if (trial.getTrialSite() == null) throw new IllegalArgumentException("could not dump trial's site list for " + trialId);
        trialStream.print(trialId);
        printField(trial.getRegistry(), trialStream);
        printField(trial.getPrimaryID(), trialStream, 22);
        printField(trial.getPhase(), trialStream);
        printField(trial.getSponsor(), trialStream, 50);
        printField(trial.getName(), trialStream, 63);
        printField(trial.getTitle(), trialStream, 255);
        printField(trial.getPurpose(), trialStream, 255);
        printField(trial.getTreatmentPlan(), trialStream);
        printField(trial.getLastRegistered() == null ? null : DATEFORMATTER.format(trial.getLastRegistered()), trialStream);
        trialStream.println();
        Iterator<TrialSite> trialSiteIterator = trial.getTrialSite().iterator();
        while (trialSiteIterator.hasNext()) {
            TrialSite trialSite = trialSiteIterator.next();
            if ((trialSite == null) || (trialSite.getSite() == null)) continue;
            trialSiteStream.print(trialId);
            printField(trialSite.getSite() == null ? null : trialSite.getSite().getId(), trialSiteStream);
            printField(trialSite.getContact() == null ? null : trialSite.getContact().getId(), trialSiteStream);
            printField(trialSite.getPrincipalInvestigator() == null ? null : trialSite.getPrincipalInvestigator().getId(), trialSiteStream);
            trialSiteStream.println();
        }
    }

    /**
     * Prints each site in a single delimited row to the <code>siteStream</code>. 
     * This method truncates zip code to five characters. It assumes that the
     * geocoding is stored in the site. 
     * This method prints the following <code>Site</code> fields to
     * <code>siteStream</code> using specified maximum widths:
     * <ul>
     * <li><code>id</code></li>
     * <li><code>name</code> (127)</li>
     * <li><code>city</code> (20)</li>
     * <li><code>politicalSubUnitName</code> (2)</li>
     * <li><code>"US"</code></li>
     * <li><code>postalCode</code></li>
     * <li><code>latitude</code></li>
     * <li><code>latitude-sine</code></li>
     * <li><code>latitude-cosine</code></li>
     * <li><code>longitude</code></li>
     * <li><code>longitude-radians</code></li>
     * </ul>
     * @param sites the sites
     * @see org.quantumleaphealth.transform.TrialDataReporter#outputSites(java.util.Iterator)
     * @throws IllegalArgumentException if the parameter is <code>null</code>
     */
    public void outputSites(Iterator<Site> sites) throws IllegalArgumentException {
        if (sites == null) throw new IllegalArgumentException("must specify sites");
        while (sites.hasNext()) {
            Site site = sites.next();
            if (site == null) continue;
            siteStream.print(site.getId());
            printField(site.getName(), siteStream, 127);
            printField(site.getCity(), siteStream, 20);
            printField(site.getPoliticalSubUnitName(), siteStream, 2);
            printField(UNITED_STATES, siteStream);
            String postalCode = site.getPostalCode();
            if ((postalCode != null) && (postalCode.trim().length() > 5)) postalCode = postalCode.trim().substring(0, 5);
            printField(postalCode, siteStream);
            Geocoding geocoding = site.getGeocoding();
            if (geocoding != null) {
                printField(geocoding.getLatitude(), siteStream);
                printField(geocoding.getLatitudeSine(), siteStream);
                printField(geocoding.getLatitudeCosine(), siteStream);
                printField(geocoding.getLongitude(), siteStream);
                printField(geocoding.getLongitudeRadians(), siteStream);
            } else {
                printField(null, siteStream);
                printField(null, siteStream);
                printField(null, siteStream);
                printField(null, siteStream);
                printField(null, siteStream);
            }
            siteStream.println();
        }
    }

    /**
     * Print each contact on a single delimited row to the <code>contactStream</code>.
     * This method prints the following <code>Contact</code> fields to
     * <code>contactStream</code> using specified maximum widths:
     * <ul>
     * <li><code>id</code></li>
     * <li><code>title</code> (127)</li>
     * <li><code>givenName</code> (22)</li>
     * <li><code>middleInitial</code> (12)</li>
     * <li><code>surName</code> (30)</li>
     * <li><code>prefix</code> (6)</li>
     * <li><code>generationSuffix</code> (6)</li>
     * <li><code>professionalSuffix</code> (25)</li>
     * <li><code>phone</code> (26)</li>
     * <li><code>email</code> (50)</li>
     * </ul>
     * @param contacts the contacts
     * @see org.quantumleaphealth.transform.TrialDataReporter#outputContacts(java.util.Iterator)
     * @throws IllegalArgumentException if the parameter is <code>null</code>
     */
    public void outputContacts(Iterator<Contact> contacts) throws IllegalArgumentException {
        if (contacts == null) throw new IllegalArgumentException("Could not dump contact");
        while (contacts.hasNext()) {
            Contact contact = contacts.next();
            if (contact == null) continue;
            contactStream.print(contact.getId());
            printField(contact.getTitle(), contactStream, 127);
            printField(contact.getGivenName(), contactStream, 22);
            printField(contact.getMiddleInitial(), contactStream, 12);
            printField(contact.getSurName(), contactStream, 30);
            printField(contact.getPrefix(), contactStream, 6);
            printField(contact.getGenerationSuffix(), contactStream, 6);
            printField(contact.getProfessionalSuffix(), contactStream, 25);
            printField(contact.getPhone(), contactStream, 26);
            printField(contact.getEmail(), contactStream, 50);
            contactStream.println();
        }
    }

    /**
     * Prints the string representation of a field prefixed by a separator. 
     * This method prints an enumeration's ordinal value.
     * This method does not print <code>null</code> or empty fields 
     * and has no limit for field length.
     * @param field the field to print
     * @param printStream the output destination
     * @throws IllegalArgumentException if <code>field</code> has the 
     *         <code>SEPARATOR</code> character in it
     */
    private static void printField(Object field, PrintStream printStream) throws IllegalArgumentException {
        printField(field, printStream, 0);
    }

    /**
     * Prints the string representation of a field prefixed by a separator. 
     * This method prints an enumeration's ordinal value.
     * This method does not print <code>null</code> or empty fields 
     * and can limit the length of the output text.
     * 
     * @param field the field to print
     * @param printStream the output destination
     * @param limit the maximum length of the string or <code>0</code> for no maximum
     * @throws IllegalArgumentException if <code>field</code> has the <code>SEPARATOR</code> character in it
     */
    private static void printField(Object field, PrintStream printStream, int limit) throws IllegalArgumentException {
        printStream.print(SEPARATOR);
        String string = (field == null) ? NULL_FIELD : ((field instanceof Enum) ? Integer.toString(((Enum) (field)).ordinal()) : field.toString().trim());
        if ((string != null) && (string.length() > 0)) {
            if ((limit > 3) && (string.length() > limit)) string = string.substring(0, limit - 3) + "...";
            if (string.indexOf(SEPARATOR) >= 0) throw new IllegalArgumentException("trial data must not have a " + SEPARATOR + " in it: " + string);
            printStream.print(string);
        }
    }

    /**
     * Prints the string representation of a double field prefixed by a separator.
     * @param field the field to print
     * @param printStream the output destination
     */
    private static void printField(double field, PrintStream printStream) {
        printStream.print(SEPARATOR);
        printStream.print(field);
    }
}
