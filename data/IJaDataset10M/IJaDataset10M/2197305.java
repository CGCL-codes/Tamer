package com.soramaki.fna.commands;

import static com.soramaki.fna.commands.Constants.DATEFORMAT;
import static com.soramaki.fna.commands.Constants.DATENAME;
import static com.soramaki.fna.commands.Constants.DECIMALSEPARATOR;
import static com.soramaki.fna.commands.Constants.FIELDSEPARATOR;
import static com.soramaki.fna.commands.Constants.FILE;
import static com.soramaki.fna.commands.Constants.FROMNAME;
import static com.soramaki.fna.commands.Constants.LOWERMARGIN;
import static com.soramaki.fna.commands.Constants.MAXDURATION;
import static com.soramaki.fna.commands.Constants.MAXRATE;
import static com.soramaki.fna.commands.Constants.PASS;
import static com.soramaki.fna.commands.Constants.RATESFILE;
import static com.soramaki.fna.commands.Constants.RATETICK;
import static com.soramaki.fna.commands.Constants.RECORDIDNAME;
import static com.soramaki.fna.commands.Constants.SKIPLINES;
import static com.soramaki.fna.commands.Constants.TICKMARGIN;
import static com.soramaki.fna.commands.Constants.TIMEFORMAT;
import static com.soramaki.fna.commands.Constants.TIMENAME;
import static com.soramaki.fna.commands.Constants.TONAME;
import static com.soramaki.fna.commands.Constants.UPPERMARGIN;
import static com.soramaki.fna.commands.Constants.VALUENAME;
import static com.soramaki.fna.commands.Constants.VALUETICK;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import com.soramaki.fna.commands.parameters.DateTimeFormatParameter;
import com.soramaki.fna.commands.parameters.DoubleParameter;
import com.soramaki.fna.commands.parameters.FileParameter;
import com.soramaki.fna.commands.parameters.LongParameter;
import com.soramaki.fna.commands.parameters.Parameter;
import com.soramaki.fna.commands.parameters.StringParameter;
import com.soramaki.fna.utils.CustomFileReader;

/**
 * Matcher command 
 * Example: 
 * match -file .\data\testpayments.txt -fieldseparator , -decimalseparator . -dateformat dd/MM/yyyy -timeformat HHmmss -pass 3 -ratesfile g:\data\saromaki\furfine3.2\data\test-rates.txt -maxduration 10 -valuetick 5 -lowermargin 0.01 -uppermargin 0.01 -ratetick 0.0010 -tickmargin 0.0003 -maxrate 0.10 -recordidname id -datename date -timename time -fromname sender -toname receiver -valuename value
 */
public class MatchCommand extends Command {

    private FileParameter datafile = new FileParameter(FILE, "");

    private StringParameter idname = new StringParameter(RECORDIDNAME, "id", "");

    private StringParameter toname = new StringParameter(TONAME, "receiver", "");

    private StringParameter fromname = new StringParameter(FROMNAME, "sender", "");

    private StringParameter datename = new StringParameter(DATENAME, "date", "");

    private StringParameter timename = new StringParameter(TIMENAME, "time", "");

    private StringParameter valuename = new StringParameter(VALUENAME, "value", "");

    private DateTimeFormatParameter timeformat = new DateTimeFormatParameter(TIMEFORMAT, Constants.defaulttimeformat, "");

    private DateTimeFormatParameter dateformat = new DateTimeFormatParameter(DATEFORMAT, Constants.defaultdateformat, "");

    private StringParameter fieldseparator = new StringParameter(FIELDSEPARATOR, ",", "");

    private StringParameter decimalseparator = new StringParameter(DECIMALSEPARATOR, ".", "");

    private LongParameter maxpass = new LongParameter(PASS, 3, "") {

        {
            setRange(1L, 3L);
        }
    };

    private FileParameter ratesfile = new FileParameter(RATESFILE, null, "");

    private LongParameter duration = new LongParameter(MAXDURATION, 1, "");

    private LongParameter valuetick = new LongParameter(VALUETICK, 1000, "");

    private DoubleParameter uppermargin = new DoubleParameter(UPPERMARGIN, 0.01, "");

    private DoubleParameter lowermargin = new DoubleParameter(LOWERMARGIN, 0.01, "");

    private DoubleParameter ratetick = new DoubleParameter(RATETICK, 25.0 / 10000, "");

    private DoubleParameter ratetickmargin = new DoubleParameter(TICKMARGIN, 3.0 / 10000, "");

    private DoubleParameter maxrate = new DoubleParameter(MAXRATE, 0.1, "");

    private StringParameter skiplines = new StringParameter(SKIPLINES, "", "");

    private static String NL = System.getProperty("line.separator");

    private String[] fieldnames = null;

    private String candidatesfile;

    private String matchesfile;

    private int dbsize;

    private Hashtable<String, Vector<Record>> recorddatabase;

    private Hashtable<Short, Integer> daycountmap;

    private Hashtable<String, Double> ratestable;

    public static String errormsg;

    private final long dayinmillis = 24 * 60 * 60 * 1000;

    private final long halfday = dayinmillis / 3;

    private DecimalFormat decimalformat;

    private Hashtable<String, Vector<Match>> matchdatabase;

    private HashSet<Integer> matchedrecords;

    private Hashtable<Integer, Integer> countertable;

    @Override
    public Parameter[] getParameters() {
        return new Parameter[] { datafile, idname, datename, timename, fromname, toname, valuename, fieldseparator, decimalseparator, dateformat, timeformat, maxpass, ratesfile, duration, valuetick, lowermargin, uppermargin, ratetick, ratetickmargin, maxrate, skiplines };
    }

    public MatchCommand() {
        super();
    }

    @Override
    public void init(String params) {
        super.init(params);
        if (!ok()) return;
        infostring = "";
        decimalformat = new DecimalFormat("#.###");
        DecimalFormatSymbols dfs = decimalformat.getDecimalFormatSymbols();
        dfs.setDecimalSeparator(decimalseparator.getValue().charAt(0));
        decimalformat.setDecimalFormatSymbols(dfs);
    }

    @Override
    public boolean execute() {
        return pass1() && (maxpass.getValue() < 2 || pass2()) && (maxpass.getValue() < 3 || pass3());
    }

    private String getOutputFileName(String fullname, String prefix) {
        File dir = new File(fullname).getParentFile();
        File f = new File(dir, prefix + ".txt");
        return f.getAbsolutePath();
    }

    private String getCandidatesFormat() {
        String header = "advance_date,return_date,sender,receiver,advance_value," + "return_value,advance_id,return_id,advance_time,return_time," + "loan_term,loan_term_business_days";
        if (!fieldseparator.equals(",")) {
            header = header.replace(",", fieldseparator.getValue());
        }
        return header;
    }

    private boolean pass1() {
        FileOutputStream out = null;
        String outname = getOutputFileName(datafile.getPath(), "pass1_");
        candidatesfile = outname;
        short currentdate = -1;
        dbsize = 0;
        try {
            CustomFileReader lnr = new CustomFileReader(datafile.getValue(), skiplines.getValue());
            out = new FileOutputStream(outname);
            String header = "# " + getCandidatesFormat() + "\n";
            out.write(header.getBytes());
            recorddatabase = new Hashtable<String, Vector<Record>>();
            daycountmap = new Hashtable<Short, Integer>();
            int daycount = 0;
            String line = null;
            while ((line = lnr.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, fieldseparator.getValue());
                int n = st.countTokens();
                fieldnames = new String[n];
                int i = 0;
                while (st.hasMoreTokens()) {
                    String field = st.nextToken().trim();
                    fieldnames[i] = field;
                    i++;
                }
                break;
            }
            if (fieldnames == null) {
                errorstring = "No field names found in input file " + datafile;
                return false;
            }
            map(idname, "id");
            map(toname, "receiver");
            map(fromname, "sender");
            map(datename, "date");
            map(timename, "time");
            map(valuename, "value");
            if (!checkPresent("receiver")) {
                errorstring = "No '" + toname.getValue() + "' field in input file " + datafile;
                return false;
            }
            if (!checkPresent("sender")) {
                errorstring = "No '" + fromname.getValue() + "' field in input file " + datafile;
                return false;
            }
            if (!checkPresent("value")) {
                errorstring = "No '" + valuename + "' field in input file " + datafile;
                return false;
            }
            if (!checkPresent("date")) {
                errorstring = "No '" + datename + "' field in input file " + datafile;
                return false;
            }
            if (!checkPresent("time")) {
                errorstring = "No '" + timename + "' field in input file " + datafile;
                return false;
            }
            while ((line = lnr.readLine()) != null) {
                if (line.startsWith("#") || line.trim().length() == 0) continue;
                Record r = new Record(line);
                if (r.date == -1) {
                    errorstring = "Parsing error in payments file. " + errormsg;
                    return false;
                }
                if (r.sender == r.receiver) continue;
                if (currentdate != r.date) {
                    log("Processing day " + shortToDateString(r.date) + " (" + dbsize + ")");
                    currentdate = r.date;
                    daycount++;
                    daycountmap.put(currentdate, daycount);
                    purgeDb(currentdate);
                }
                String key = r.receiver + r.sender.charAt(r.sender.length() - 1);
                Vector<Record> records = recorddatabase.get(key);
                if (records != null) {
                    for (Record rpast : records) {
                        if (r.sender != rpast.receiver) {
                            continue;
                        }
                        if (r.value <= rpast.value) {
                            continue;
                        }
                        int days = r.date - rpast.date;
                        if (days <= 0) {
                            continue;
                        }
                        if (days > duration.getValue()) {
                            continue;
                        }
                        if ((r.value / rpast.value - 1) * 365 / days > maxrate.getValue()) {
                            continue;
                        }
                        StringBuffer outline = new StringBuffer();
                        outline.append(shortToDateString(rpast.date)).append(fieldseparator);
                        outline.append(shortToDateString(r.date)).append(fieldseparator);
                        outline.append(rpast.sender).append(fieldseparator);
                        outline.append(rpast.receiver).append(fieldseparator);
                        outline.append(rpast.value).append(fieldseparator);
                        outline.append(r.value).append(fieldseparator);
                        outline.append(rpast.id).append(fieldseparator);
                        outline.append(r.id).append(fieldseparator);
                        outline.append(timeToTimeString(rpast.time)).append(fieldseparator);
                        outline.append(timeToTimeString(r.time)).append(fieldseparator);
                        outline.append(r.date - rpast.date).append(fieldseparator);
                        outline.append(getBusinessDays(rpast.date, r.date)).append("\n");
                        out.write(outline.toString().getBytes());
                    }
                }
                double dv = (double) r.value;
                if (Math.floor(dv) == dv) {
                    if (((int) dv) % valuetick.getValue() == 0) {
                        key = r.receiver + r.sender.charAt(r.sender.length() - 1);
                        Vector<Record> outrecords = recorddatabase.get(key);
                        if (outrecords == null) {
                            outrecords = new Vector<Record>();
                            recorddatabase.put(key, outrecords);
                        }
                        outrecords.add(r);
                        dbsize++;
                    }
                }
            }
            out.close();
            log("Pass 1 Done. " + outname + " saved.");
            return true;
        } catch (FileNotFoundException fe) {
            errorstring = ("Error: input file not found " + fe.getMessage());
            return false;
        } catch (Exception e) {
            errorstring = ("Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void map(StringParameter fieldkey, String defaultname) {
        if (fieldkey.isDefaultUsed()) return;
        for (int i = 0; i < fieldnames.length; i++) {
            if (fieldnames[i].equalsIgnoreCase(fieldkey.getValue())) {
                fieldnames[i] = defaultname;
                break;
            }
        }
    }

    private boolean checkPresent(String name) {
        for (String s : fieldnames) {
            if (s.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private void purgeDb(short currentdate) {
        short old = (short) (currentdate - duration.getValue());
        if (old < 0) {
            return;
        }
        Enumeration<Vector<Record>> e = recorddatabase.elements();
        while (e.hasMoreElements()) {
            Vector<Record> records = e.nextElement();
            int size = records.size();
            int i = 0;
            while (i < size && records.get(i).date < old) {
                i++;
            }
            for (int j = 0; j < i; j++) {
                records.remove(0);
                dbsize--;
            }
        }
    }

    private boolean loadRates() {
        LineNumberReader lnrrates = null;
        String line = null;
        try {
            lnrrates = new LineNumberReader(new FileReader(ratesfile.getValue()));
            while ((line = lnrrates.readLine()) != null) {
                if (line.startsWith("#") || line.trim().length() == 0) continue;
                StringTokenizer st = new StringTokenizer(line, fieldseparator.getValue());
                String date = st.nextToken();
                String rate = st.nextToken();
                if (rate.endsWith("%")) {
                    rate = rate.substring(0, rate.length() - 1);
                }
                rate = rate.trim();
                if (!decimalseparator.equals(".")) {
                    rate = rate.replace(decimalseparator.getValue(), ".");
                }
                double rd = Double.parseDouble(rate) / 100;
                ratestable.put(date, rd);
            }
        } catch (Exception e) {
            errormsg = "Error parsing rates file, line: " + line;
            return false;
        } finally {
            try {
                lnrrates.close();
            } catch (Exception e) {
            }
        }
        return true;
    }

    private boolean pass2() {
        LineNumberReader lnr = null;
        FileOutputStream out = null;
        String outname = getOutputFileName(candidatesfile, "pass2_");
        File f = new File(outname);
        matchesfile = outname;
        try {
            out = new FileOutputStream(outname);
            String header = "# " + getCandidatesFormat() + fieldseparator + "rate\n";
            out.write(header.getBytes());
            lnr = new LineNumberReader(new FileReader(candidatesfile));
            ratestable = new Hashtable<String, Double>();
            if (!ratesfile.isDefaultUsed()) {
                if (!loadRates()) {
                    log(errormsg);
                    return false;
                }
            }
            String line = null;
            while ((line = lnr.readLine()) != null) {
                if (line.startsWith("#") || line.trim().length() == 0) continue;
                StringTokenizer st = new StringTokenizer(line, fieldseparator.getValue());
                String outdate = st.nextToken();
                String indate = st.nextToken();
                st.nextToken();
                st.nextToken();
                String v1 = st.nextToken();
                String v2 = st.nextToken();
                if (!decimalseparator.equals(".")) {
                    v1 = v1.replace(decimalseparator.getValue(), ".");
                    v2 = v2.replace(decimalseparator.getValue(), ".");
                }
                double value1 = Double.parseDouble(v1);
                double value2 = Double.parseDouble(v2);
                int days = dateStringToShort(indate) - dateStringToShort(outdate);
                double rate = (value2 / value1 - 1) * 365 / days;
                double lower = 0;
                double upper = maxrate.getValue();
                if (!ratesfile.isDefaultUsed()) {
                    double depositrate = 0.05;
                    if (ratestable.containsKey(outdate)) {
                        depositrate = ratestable.get(outdate);
                    }
                    lower = depositrate - lowermargin.getValue();
                    upper = depositrate + uppermargin.getValue();
                }
                if (rate < lower || rate > upper) {
                    continue;
                }
                double rateinticks = rate / ratetick.getValue();
                double diff1 = Math.abs(rateinticks - Math.floor(rateinticks));
                double diff2 = Math.abs(rateinticks - Math.ceil(rateinticks));
                double diff = Math.min(diff1, diff2);
                double alloweddiff = ratetickmargin.getValue() / ratetick.getValue();
                if (diff > alloweddiff) {
                    continue;
                }
                rate *= 100;
                line += fieldseparator + decimalformat.format(rate) + "\n";
                out.write(line.getBytes());
            }
            lnr.close();
            out.close();
            log("Pass 2 Done. " + outname + " saved.");
            return true;
        } catch (FileNotFoundException fe) {
            log("Error: input file not found " + fe.getMessage());
            return false;
        } catch (Exception e) {
            log("Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean pass3() {
        LineNumberReader lnr = null;
        FileOutputStream out = null;
        String outname = getOutputFileName(matchesfile, "pass3_");
        File f = new File(outname);
        try {
            out = new FileOutputStream(outname);
            String header = "# " + getCandidatesFormat() + fieldseparator + "rate\n";
            out.write(header.getBytes());
            lnr = new LineNumberReader(new FileReader(matchesfile));
            ratestable = new Hashtable<String, Double>();
            if (!ratesfile.isDefaultUsed()) {
                if (!loadRates()) {
                    log(errormsg);
                    return false;
                }
            }
            matchdatabase = new Hashtable<String, Vector<Match>>();
            matchedrecords = new HashSet<Integer>();
            String line = null;
            while ((line = lnr.readLine()) != null) {
                if (line.startsWith("#") || line.trim().length() == 0) continue;
                Match m = new Match(line);
                if (m.date == -1) {
                    log("Parsing error in match line: " + line);
                    return false;
                }
                String key = m.sender + "-" + m.receiver;
                Vector<Match> loans = matchdatabase.get(key);
                if (loans == null) {
                    loans = new Vector<Match>();
                    matchdatabase.put(key, loans);
                }
                loans.add(m);
            }
            MatchComparator mcomp = new MatchComparator();
            Enumeration<String> keys = matchdatabase.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                Vector<Match> loans = matchdatabase.get(key);
                countertable = new Hashtable<Integer, Integer>();
                for (Match m : loans) {
                    countRecord(m.id1);
                    countRecord(m.id2);
                }
                for (Match m : loans) {
                    if (countertable.get(m.id1) == 1 && countertable.get(m.id2) == 1 && !matchedrecords.contains(m.id1) && !matchedrecords.contains(m.id2)) {
                        out.write((m.line + "\n").getBytes());
                        markMatched(m);
                    }
                }
                if (loans.size() > 0) {
                    Match[] loanarr = loans.toArray(new Match[1]);
                    Arrays.sort(loanarr, mcomp);
                    for (Match m : loanarr) {
                        if (m.matched) {
                            break;
                        }
                        if (!matchedrecords.contains(m.id1) && !matchedrecords.contains(m.id2)) {
                            out.write((m.line + "\n").getBytes());
                            markMatched(m);
                        }
                    }
                }
            }
            lnr.close();
            out.close();
            log("Pass 3 Done. " + outname + " saved.");
            return true;
        } catch (FileNotFoundException fe) {
            log("Error: input file not found " + fe.getMessage());
            return false;
        } catch (Exception e) {
            log("Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void countRecord(int id) {
        if (countertable.containsKey(id)) {
            int count = countertable.get(id) + 1;
            countertable.put(id, count);
        } else {
            countertable.put(id, 1);
        }
    }

    private void markMatched(Match m) {
        m.matched = true;
        matchedrecords.add(m.id1);
        matchedrecords.add(m.id2);
    }

    private short dateStringToShort(String datestring) {
        try {
            Date date = dateformat.getFormatter().parse(datestring);
            long millis = date.getTime() + halfday;
            return (short) (millis / dayinmillis);
        } catch (Exception e) {
            return -1;
        }
    }

    private String shortToDateString(short d) {
        Date date = new Date((long) d * dayinmillis + halfday);
        return dateformat.getFormatter().format(date);
    }

    private String timeToTimeString(long time) {
        String ts = timeformat.getFormatter().format(new Date(time));
        return ts;
    }

    private int getBusinessDays(short d1, short d2) {
        int bd1 = daycountmap.get(d1);
        int bd2 = daycountmap.get(d2);
        return Math.abs(bd1 - bd2);
    }

    private void log(String s) {
        infostring += NL;
        infostring += s;
    }

    class Record {

        short date;

        long time;

        String sender;

        String receiver;

        float value;

        int id;

        public Record(String line) {
            String field = "";
            String name = "";
            Hashtable<String, String> properties = new Hashtable<String, String>();
            {
                StringTokenizer st = new StringTokenizer(line, fieldseparator.getValue());
                for (int i = 0; i < fieldnames.length; i++) {
                    properties.put(fieldnames[i], st.nextToken());
                }
            }
            try {
                name = "id";
                field = properties.get("id");
                id = Integer.parseInt(field);
                name = "date";
                field = properties.get("date");
                date = dateStringToShort(field);
                if (date == -1) throw new Exception();
                name = "time";
                field = properties.get("time");
                if (timeformat.getFormat().length() == field.length() + 1) {
                    field = "0" + field;
                }
                Date d = timeformat.getFormatter().parse(field);
                time = d.getTime();
                name = "value";
                field = properties.get("value");
                if (!decimalseparator.equals(".")) {
                    field = field.replace(decimalseparator.getValue(), ".");
                }
                value = Float.parseFloat(field);
                name = "sender id";
                field = properties.get("sender");
                sender = field;
                name = "receiver id";
                field = properties.get("receiver");
                receiver = field;
            } catch (Exception e) {
                date = -1;
                errormsg = "Line: '" + line + "' Field: '" + name + "' Value: '" + field + "'";
            }
        }
    }

    class Match {

        String line;

        int sender;

        int receiver;

        int id1;

        int id2;

        double rate;

        int baseratedifference;

        short date;

        long time1;

        long time2;

        int delay;

        boolean matched;

        public Match(String s) {
            line = s;
            matched = false;
            try {
                StringTokenizer st = new StringTokenizer(line, fieldseparator.getValue());
                String outdate = st.nextToken();
                date = dateStringToShort(outdate);
                String indate = st.nextToken();
                sender = Integer.parseInt(st.nextToken());
                receiver = Integer.parseInt(st.nextToken());
                st.nextToken();
                st.nextToken();
                id1 = Integer.parseInt(st.nextToken());
                id2 = Integer.parseInt(st.nextToken());
                String t = st.nextToken();
                Date d = timeformat.getFormatter().parse(t);
                time1 = d.getTime() + dayinmillis * date;
                t = st.nextToken();
                d = timeformat.getFormatter().parse(t);
                time2 = d.getTime() + dayinmillis * dateStringToShort(indate);
                delay = (int) ((time2 - time1) / 1000);
                st.nextToken();
                st.nextToken();
                rate = Double.parseDouble(st.nextToken()) / 100;
                if (!ratesfile.isDefaultUsed()) {
                    double baserate = 0.05;
                    if (ratestable.containsKey(outdate)) {
                        baserate = ratestable.get(outdate);
                    }
                    double rateinticks = (rate - baserate) / ratetick.getValue();
                    baseratedifference = (int) (Math.abs(rateinticks));
                } else {
                    baseratedifference = 0;
                }
            } catch (Exception e) {
                date = -1;
            }
        }
    }

    class MatchComparator implements Comparator<Match> {

        public int compare(Match a, Match b) {
            if (!a.matched && b.matched) return -1;
            if (a.matched && !b.matched) return +1;
            if (a.baseratedifference < b.baseratedifference) return -1;
            if (a.baseratedifference > b.baseratedifference) return +1;
            if (a.delay < b.delay) return -1;
            if (a.delay > b.delay) return +1;
            return 0;
        }
    }
}
