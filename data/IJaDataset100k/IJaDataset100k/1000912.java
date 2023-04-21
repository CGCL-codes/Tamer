package org.lindenb.tool.pivot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.lindenb.util.Compilation;

/**
 * Pivot
 * www: http://plindenbaum.blogspot.com
 * mail: plindenbaum yahoo fr
 * @author Pierre Lindenbaum PhD
 * $LastChangedDate$
 * $LastChangedRevision$
 * $LastChangedBy$
 * 
 *
 */
public class Pivot {

    /**
* ColumnModel
* a vector of indexes of the table columns
*/
    private class ColumnModel implements Iterable<Integer> {

        private Vector<Integer> indexes = new Vector<Integer>(10, 1);

        private int maxCol = -1;

        ColumnModel() {
        }

        public void add(int columnIndex) {
            if (columnIndex < 0) throw new IllegalArgumentException("Bad index :" + columnIndex);
            if (this.indexes.contains(columnIndex)) throw new IllegalArgumentException("Index defined twice :" + (columnIndex + 1));
            this.indexes.addElement(columnIndex);
            this.maxCol = Math.max(this.maxCol, columnIndex);
        }

        public int size() {
            return this.indexes.size();
        }

        public boolean isEmpty() {
            return this.indexes.isEmpty();
        }

        public int at(int index) {
            return this.indexes.elementAt(index);
        }

        @Override
        public Iterator<Integer> iterator() {
            return indexes.iterator();
        }
    }

    private enum Choice {

        DEFAULT, MIN, MAX, SUM, MEAN, COUNT_DISTINCT, STDEV, COUNT, HW_FREQ_A1, HW_FREQ_A2, HW_CHI2
    }

    /***********************************
*
*  class Data
*
*/
    private class Data {

        TreeMap<DataScalarList, Integer> data2count = new TreeMap<DataScalarList, Integer>();

        Data() {
        }

        void add(DataScalarList val) {
            Integer i = data2count.get(val);
            if (i == null) i = 0;
            data2count.put(val, i + 1);
        }

        public String getValue(Choice choice) {
            switch(choice) {
                case HW_CHI2:
                case HW_FREQ_A1:
                case HW_FREQ_A2:
                    {
                        if (Pivot.this.dataColumnIndex.size() != 1) {
                            return Pivot.this.valueForNull();
                        }
                        HashMap<String, Integer> genotype2count = new HashMap<String, Integer>();
                        TreeSet<String> alleleSet = new TreeSet<String>();
                        int total = 0;
                        for (DataScalarList d : this.data2count.keySet()) {
                            int n = this.data2count.get(d);
                            String genotype = d.at(0);
                            if (genotype.length() == 0 || genotype.equals(EMPTY_VALUE)) continue;
                            genotype = genotype.toUpperCase().replaceAll("[\\[\\]]", "").trim();
                            String genotypes[] = genotype.split("[ ,\\-\t]+");
                            if (genotypes.length == 1 && genotype.length() == 2) {
                                genotypes = new String[] { "" + genotype.charAt(0), "" + genotype.charAt(1) };
                            }
                            if (genotypes.length != 2) continue;
                            if (genotypes[0].compareTo(genotypes[1]) > 0) {
                                String stock = genotypes[0];
                                genotypes[0] = genotypes[1];
                                genotypes[1] = stock;
                            }
                            genotype = genotypes[0] + genotypes[1];
                            alleleSet.add(genotypes[0]);
                            alleleSet.add(genotypes[1]);
                            if (alleleSet.size() > 2) return Pivot.this.valueForNull();
                            Integer count = genotype2count.get(genotype);
                            if (count == null) count = 0;
                            genotype2count.put(genotype, count + n);
                            total += n;
                        }
                        if (total == 0) return Pivot.this.valueForNull();
                        if (alleleSet.size() == 1) alleleSet.add("");
                        String alleles[] = alleleSet.toArray(new String[alleleSet.size()]);
                        Integer oAA = genotype2count.get(alleles[0] + alleles[0]);
                        if (oAA == null) oAA = 0;
                        Integer oAB = genotype2count.get(alleles[0] + alleles[1]);
                        if (oAB == null) oAB = 0;
                        Integer oBB = genotype2count.get(alleles[1] + alleles[1]);
                        if (oBB == null) oBB = 0;
                        double freqA = (2.0 * oAA + oAB) / (2.0 * total);
                        switch(choice) {
                            case HW_FREQ_A1:
                                {
                                    return String.valueOf(alleles[0] + " : " + freqA);
                                }
                            case HW_FREQ_A2:
                                {
                                    return String.valueOf(alleles[1] + " : " + (1.0 - freqA));
                                }
                            default:
                            case HW_CHI2:
                                {
                                    if (freqA == 1.0) return valueForNull();
                                    double eAA = Math.pow(freqA, 2) * total;
                                    double eAB = 2.0 * freqA * (1.0 - freqA) * total;
                                    double eBB = Math.pow((1.0 - freqA), 2) * total;
                                    return String.valueOf(Math.sqrt(Math.pow((oAA - eAA), 2) / eAA + Math.pow((oAB - eAB), 2) / eAB + Math.pow((oBB - eBB), 2) / eBB));
                                }
                        }
                    }
                case COUNT:
                    {
                        int n = 0;
                        for (DataScalarList d : this.data2count.keySet()) n += this.data2count.get(d);
                        return String.valueOf(n);
                    }
                case COUNT_DISTINCT:
                    {
                        return String.valueOf(this.data2count.size());
                    }
                case STDEV:
                case MIN:
                case MAX:
                case SUM:
                case MEAN:
                    {
                        if (Pivot.this.dataColumnIndex.size() != 1) {
                            return Pivot.this.valueForNull();
                        }
                        double min = Double.MAX_VALUE;
                        double max = -Double.MAX_VALUE;
                        double total = 0.0;
                        int count = 0;
                        for (DataScalarList d : this.data2count.keySet()) {
                            try {
                                Double val = new Double(d.at(0));
                                min = Math.min(min, val);
                                max = Math.max(max, val);
                                int c = data2count.get(d);
                                total += val.doubleValue() * c;
                                count += c;
                            } catch (Exception e) {
                            }
                        }
                        if (count == 0) return valueForNull();
                        double mean = total / count;
                        switch(choice) {
                            case MIN:
                                return String.valueOf(min);
                            case MAX:
                                return String.valueOf(max);
                            case SUM:
                                return String.valueOf(total);
                            case MEAN:
                                return String.valueOf(mean);
                            case STDEV:
                                {
                                    if (count == 1) return valueForNull();
                                    double stdev = 0;
                                    for (DataScalarList d : this.data2count.keySet()) {
                                        try {
                                            Double val = new Double(d.at(0));
                                            int c = data2count.get(d);
                                            stdev += (c * Math.pow(mean - val, 2));
                                        } catch (Exception e) {
                                        }
                                    }
                                    return String.valueOf(Math.sqrt(stdev / (count - 1)));
                                }
                        }
                        return valueForNull();
                    }
                case DEFAULT:
                    {
                        int i = data2count.size();
                        if (i == 0) {
                            return valueForNull().toString();
                        }
                        StringBuilder b = new StringBuilder();
                        if (i > 1) b.append("{");
                        boolean found = false;
                        for (DataScalarList d : this.data2count.keySet()) {
                            if (found) b.append(";");
                            found = true;
                            int c = data2count.get(d);
                            b.append(d.toString());
                            if (!(i == 1 && c == 1)) b.append(":" + c);
                        }
                        if (i > 1) b.append("}");
                        return b.toString();
                    }
                default:
                    return valueForNull();
            }
        }

        @Override
        public String toString() {
            return getValue(Choice.DEFAULT);
        }
    }

    private abstract class ScalarList implements Comparable<ScalarList> {

        private int row;

        public ScalarList(int row) {
            this.row = row;
        }

        public abstract ColumnModel getColumnModel();

        public int compareTo(ScalarList o) {
            if (o == this) return 0;
            if (this.getSize() != o.getSize()) throw new RuntimeException();
            for (int j = 0; j < getSize(); ++j) {
                int i = (Pivot.this.casesensible ? at(j).compareTo(o.at(j)) : at(j).compareToIgnoreCase(o.at(j)));
                if (i != 0) return i;
            }
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null) return false;
            return compareTo(ScalarList.class.cast(obj)) == 0;
        }

        @Override
        public int hashCode() {
            int i = 0;
            for (int j = 0; j < getSize(); ++j) {
                i += (Pivot.this.casesensible ? at(j).hashCode() : at(j).toLowerCase().hashCode());
            }
            return i;
        }

        public String at(int i) {
            return Pivot.this.table.elementAt(this.row)[getColumnModel().at(i)];
        }

        public int getSize() {
            return getColumnModel().size();
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < getSize(); ++i) {
                if (i != 0) b.append("-");
                b.append(at(i));
            }
            return b.toString();
        }
    }

    private class LeftScalarList extends ScalarList {

        LeftScalarList(int row) {
            super(row);
        }

        @Override
        public ColumnModel getColumnModel() {
            return Pivot.this.leftColumnIndex;
        }
    }

    private class TopScalarList extends ScalarList {

        TopScalarList(int row) {
            super(row);
        }

        @Override
        public ColumnModel getColumnModel() {
            return Pivot.this.topColumnIndex;
        }
    }

    private class DataScalarList extends ScalarList {

        DataScalarList(int row) {
            super(row);
        }

        @Override
        public ColumnModel getColumnModel() {
            return Pivot.this.dataColumnIndex;
        }
    }

    private String NULL_VALUE = "NULL";

    private String EMPTY_VALUE = "";

    private Pattern delimiterIn = Pattern.compile("[\t]");

    private String header[] = null;

    private Vector<String[]> table = new Vector<String[]>(10000, 1000);

    private boolean casesensible = true;

    private boolean firstLineIsHeader = true;

    private boolean trimTokens = false;

    private ColumnModel leftColumnIndex = new ColumnModel();

    private ColumnModel topColumnIndex = new ColumnModel();

    private ColumnModel dataColumnIndex = new ColumnModel();

    private HashMap<Choice, Boolean> choices = new HashMap<Choice, Boolean>();

    private AbstractPrinter printer = new PlainPrinter(System.out);

    private boolean print_horizontal_total = true;

    private boolean print_vertical_total = true;

    private Pivot() {
        for (Choice c : Choice.values()) {
            this.choices.put(c, Boolean.FALSE);
        }
    }

    private abstract class AbstractPrinter {

        protected PrintStream out;

        protected AbstractPrinter(PrintStream out) {
            this.out = out;
        }

        protected abstract void oTable();

        protected abstract void cTable();

        protected abstract void oTR();

        protected abstract void cTR();

        protected abstract void TH(String s);

        protected abstract void TD(String s);

        protected Pivot getPivot() {
            return Pivot.this;
        }

        private void print() {
            if (getPivot().header == null) return;
            TreeMap<LeftScalarList, TreeSet<Integer>> leftValues = new TreeMap<LeftScalarList, TreeSet<Integer>>();
            TreeMap<TopScalarList, TreeSet<Integer>> topValues = new TreeMap<TopScalarList, TreeSet<Integer>>();
            Vector<Choice> display = new Vector<Choice>();
            for (Choice c : Choice.values()) {
                if (getPivot().choices.get(c)) {
                    display.addElement(c);
                }
            }
            for (int row = 0; row < getPivot().table.size(); ++row) {
                LeftScalarList L = new LeftScalarList(row);
                TreeSet<Integer> set = leftValues.get(L);
                if (set == null) {
                    set = new TreeSet<Integer>();
                    leftValues.put(L, set);
                }
                set.add(row);
                if (!Pivot.this.topColumnIndex.isEmpty()) {
                    TopScalarList T = new TopScalarList(row);
                    set = topValues.get(T);
                    if (set == null) {
                        set = new TreeSet<Integer>();
                        topValues.put(T, set);
                    }
                    set.add(row);
                }
            }
            oTable();
            for (int x = 0; x < Pivot.this.topColumnIndex.size(); ++x) {
                oTR();
                for (int i = 0; i < leftColumnIndex.size(); ++i) {
                    TH("");
                }
                TH(Pivot.this.header[Pivot.this.topColumnIndex.at(x)]);
                for (TopScalarList rtop : topValues.keySet()) {
                    TH(rtop.at(x));
                }
                TH("");
                cTR();
            }
            oTR();
            for (Integer j : getPivot().leftColumnIndex) {
                TH(getPivot().header[j]);
            }
            TH("Data");
            for (int i = 0; i < topValues.size(); ++i) {
                TH(String.valueOf(i + 1));
            }
            if (Pivot.this.print_horizontal_total) {
                TH("Total");
            }
            cTR();
            for (LeftScalarList rleft : leftValues.keySet()) {
                for (int displayIndex = 0; displayIndex < display.size(); ++displayIndex) {
                    oTR();
                    for (int i = 0; i < rleft.getSize(); ++i) {
                        TH(rleft.at(i));
                    }
                    TH(getLabel(display.elementAt(displayIndex)));
                    for (TopScalarList rtop : topValues.keySet()) {
                        Data data = getData(leftValues.get(rleft), topValues.get(rtop));
                        TD(data.getValue(display.elementAt(displayIndex)));
                    }
                    if (Pivot.this.print_horizontal_total) {
                        Data dataTotal = getData(leftValues.get(rleft), null);
                        TD(dataTotal.getValue(display.elementAt(displayIndex)));
                    }
                    cTR();
                }
            }
            if (Pivot.this.print_vertical_total) {
                for (int displayIndex = 0; displayIndex < display.size(); ++displayIndex) {
                    oTR();
                    for (int i = 0; i < leftColumnIndex.size(); ++i) {
                        TH("");
                    }
                    TH(getLabel(display.elementAt(displayIndex)));
                    for (TopScalarList rtop : topValues.keySet()) {
                        Data data = getData(null, topValues.get(rtop));
                        TD(data.getValue(display.elementAt(displayIndex)));
                    }
                    Data dataTotal = getData(null, null);
                    TD(dataTotal.getValue(display.elementAt(displayIndex)));
                    cTR();
                }
            }
            cTable();
        }
    }

    private class HTMLPrinter extends AbstractPrinter {

        private int trIndex = 0;

        protected HTMLPrinter(PrintStream out) {
            super(out);
        }

        @Override
        protected void oTable() {
            out.print("<table style=\"border-width:1px;border-style: solid;border-collapse: collapse;border-color: black;\">\n");
        }

        @Override
        protected void cTable() {
            out.print("</table>\n");
        }

        @Override
        protected void oTR() {
            out.print("<tr class=\"r" + ((++trIndex) % 2) + "\">");
        }

        @Override
        protected void cTR() {
            out.print("</tr>\n");
        }

        @Override
        protected void TH(String s) {
            out.print("<th>");
            out.print(escape(s));
            out.print("</th>");
        }

        @Override
        protected void TD(String s) {
            out.print("<td>");
            out.print(escape(s));
            out.print("</td>");
        }
    }

    private class PlainPrinter extends AbstractPrinter {

        private boolean atBegin = true;

        protected PlainPrinter(PrintStream out) {
            super(out);
        }

        @Override
        protected void oTable() {
            atBegin = true;
        }

        @Override
        protected void cTable() {
        }

        @Override
        protected void oTR() {
            atBegin = true;
        }

        @Override
        protected void cTR() {
            out.println();
            atBegin = true;
        }

        @Override
        protected void TH(String s) {
            TD(s);
        }

        @Override
        protected void TD(String s) {
            if (!atBegin) out.print("\t");
            out.print(s);
            atBegin = false;
        }
    }

    private Data getData(Set<Integer> left, Set<Integer> top) {
        Data data = new Data();
        HashSet<Integer> set = null;
        if (left != null) {
            set = new HashSet<Integer>(left);
            if (top != null) set.retainAll(top);
        } else if (top != null) {
            set = new HashSet<Integer>(top);
        } else {
            set = new HashSet<Integer>(this.table.size());
            for (int i = 0; i < this.table.size(); ++i) set.add(i);
        }
        for (int row : set) {
            if (!this.dataColumnIndex.isEmpty()) {
                data.add(new DataScalarList(row));
            } else {
                data.add(null);
            }
        }
        return data;
    }

    private String valueForNull() {
        return NULL_VALUE;
    }

    private HTMLPrinter newHTMLPrinter(PrintStream out) {
        return new HTMLPrinter(out);
    }

    private void read(BufferedReader in) throws IOException {
        this.header = null;
        this.table.clear();
        int nLine = 0;
        String line;
        String tokens[];
        HashSet<Integer> ignoreColumns = new HashSet<Integer>();
        while ((line = in.readLine()) != null) {
            ++nLine;
            tokens = this.delimiterIn.split(line);
            if (this.trimTokens) {
                for (int i = 0; i < tokens.length; ++i) {
                    tokens[i] = tokens[i].trim();
                }
            }
            for (int i = 0; i < tokens.length; ++i) {
                if (tokens[i].length() == 0) tokens[i] = EMPTY_VALUE;
            }
            if (nLine == 1) {
                for (int i = 0; i < tokens.length; ++i) {
                    ignoreColumns.add(i);
                }
                for (int i : this.topColumnIndex) ignoreColumns.remove(i);
                for (int i : this.leftColumnIndex) ignoreColumns.remove(i);
                for (int i : this.dataColumnIndex) ignoreColumns.remove(i);
                if (this.firstLineIsHeader) {
                    this.header = tokens;
                } else {
                    this.header = new String[tokens.length];
                    for (int i = 0; i < this.header.length; ++i) this.header[i] = "$" + (i + 1);
                }
                if (this.leftColumnIndex.maxCol >= this.header.length) {
                    throw new IOException("Found " + this.header.length + " columns : out of range with left indexes");
                }
                if (this.topColumnIndex.maxCol >= this.header.length) {
                    throw new IOException("Found " + this.header.length + " columns : out of range with top indexes");
                }
                if (this.dataColumnIndex.maxCol >= this.header.length) {
                    throw new IOException("Found " + this.header.length + " columns : out of range with data indexes");
                }
                if (this.firstLineIsHeader) continue;
            }
            if (tokens.length != this.header.length) {
                throw new IOException("Expected " + header.length + " columns but found " + tokens.length + " in " + line);
            }
            for (int i : ignoreColumns) {
                tokens[i] = null;
            }
            this.table.addElement(tokens);
        }
        if (this.header == null) throw new IOException("No Input");
    }

    /** escape the XML of a given string */
    public static String escape(CharSequence s) {
        if (s == null) throw new NullPointerException();
        int needed = -1;
        for (int i = 0; i < s.length(); ++i) {
            switch(s.charAt(i)) {
                case '\'':
                case '\"':
                case '&':
                case '<':
                case '>':
                    needed = i;
                    break;
                default:
                    break;
            }
            if (needed != -1) break;
        }
        if (needed == -1) return s.toString();
        StringBuffer buffer = new StringBuffer(s.subSequence(0, needed));
        for (int i = needed; i < s.length(); ++i) {
            switch(s.charAt(i)) {
                case '\'':
                    buffer.append("&apos;");
                    break;
                case '\"':
                    buffer.append("&quot;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                default:
                    buffer.append(s.charAt(i));
                    break;
            }
        }
        return buffer.toString();
    }

    private static void assignColumnModel(String arg, ColumnModel columnModel, String option) {
        if (columnModel.size() != 0) {
            throw new IllegalArgumentException("-" + option + " defined twice");
        }
        String tokens[] = arg.split("[,]");
        for (String s : tokens) {
            s = s.trim();
            if (s.length() == 0) continue;
            try {
                Integer i = new Integer(s);
                if (i < 1) throw new IllegalArgumentException("in option -" + option + " bad index" + arg + " <1 ");
                columnModel.add(i - 1);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Bad Column in " + arg, e);
            }
        }
    }

    private static String getLabel(Choice c) {
        switch(c) {
            default:
                return c.toString().toLowerCase().replace('_', '-');
        }
    }

    public static void main(String[] args) {
        try {
            int optind = 0;
            Pivot pivot = new Pivot();
            while (optind < args.length) {
                if (args[optind].equals("-h")) {
                    System.out.println("Pivot [options] (<File>|<file.gz>|<url>|stdin)");
                    System.out.println("Author: Pierre Lindenbaum PhD. 2007");
                    System.out.println("Compiled on " + Compilation.getDate() + " by " + Compilation.getUser());
                    System.out.println("$LastChangedRevision$");
                    System.out.println(" -h help (this screen)");
                    System.out.println(" -L \'column1,column2,column3,...\' columns for left. (required)");
                    System.out.println(" -T \'column1,column2,column3,...\' columns for top. (optional)");
                    System.out.println(" -D \'column1,column3,column3,...\' columns for data.(required)");
                    System.out.println(" -p <regex> pattern used to break the input into tokens default:TAB");
                    System.out.println(" -i case insensitive");
                    System.out.println(" -t trim each column");
                    System.out.println(" -null <string> value for null");
                    System.out.println(" -e <string> value for empty string");
                    System.out.println(" -f first line is NOT the header");
                    System.out.println(" -html html output");
                    System.out.println(" -no-vt disable vertical summary");
                    System.out.println(" -no-ht disable horizontal summary");
                    System.out.println(" -hw (Hardy Weinberg display option)");
                    for (Choice c : Choice.values()) {
                        System.out.println(" -" + getLabel(c) + " (display option)");
                    }
                    return;
                } else if (args[optind].equals("-i")) {
                    pivot.casesensible = false;
                } else if (args[optind].equals("-no-vt")) {
                    pivot.print_vertical_total = false;
                } else if (args[optind].equals("-no-ht")) {
                    pivot.print_horizontal_total = false;
                } else if (args[optind].equals("-f")) {
                    pivot.firstLineIsHeader = false;
                } else if (args[optind].equals("-null")) {
                    pivot.NULL_VALUE = args[++optind];
                } else if (args[optind].equals("-e")) {
                    pivot.EMPTY_VALUE = args[++optind];
                } else if (args[optind].equals("-t")) {
                    pivot.trimTokens = true;
                } else if (args[optind].equals("-html")) {
                    pivot.printer = pivot.newHTMLPrinter(System.out);
                } else if (args[optind].equals("-p")) {
                    pivot.delimiterIn = Pattern.compile(args[++optind]);
                } else if (args[optind].equals("-L")) {
                    assignColumnModel(args[++optind], pivot.leftColumnIndex, "L");
                } else if (args[optind].equals("-T")) {
                    assignColumnModel(args[++optind], pivot.topColumnIndex, "T");
                } else if (args[optind].equals("-D")) {
                    assignColumnModel(args[++optind], pivot.dataColumnIndex, "D");
                } else if (args[optind].equals("-hw")) {
                    pivot.choices.put(Choice.HW_FREQ_A1, true);
                    pivot.choices.put(Choice.HW_FREQ_A2, true);
                    pivot.choices.put(Choice.HW_CHI2, true);
                } else if (args[optind].equals("--")) {
                    ++optind;
                    break;
                } else if (args[optind].startsWith("-")) {
                    boolean found = false;
                    for (Choice c : Choice.values()) {
                        if (args[optind].substring(1).equals(getLabel(c))) {
                            pivot.choices.put(c, true);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        throw new IllegalArgumentException("Unknown option " + args[optind]);
                    }
                } else {
                    break;
                }
                ++optind;
            }
            if (pivot.leftColumnIndex.isEmpty()) {
                throw new IllegalArgumentException("-L undefined");
            }
            if (pivot.dataColumnIndex.isEmpty()) {
                throw new IllegalArgumentException("-D undefined");
            }
            boolean foundChoice = false;
            for (Choice c : Choice.values()) {
                if (pivot.choices.get(c)) {
                    foundChoice = true;
                    break;
                }
            }
            if (!foundChoice) pivot.choices.put(Choice.DEFAULT, true);
            if (optind == args.length) {
                pivot.read(new BufferedReader(new InputStreamReader(System.in)));
            } else if (optind + 1 == args.length) {
                BufferedReader in = null;
                if (args[optind].startsWith("http://") || args[optind].startsWith("https://") || args[optind].startsWith("file://") || args[optind].startsWith("ftp://")) {
                    URL url = new URL(args[optind]);
                    if (args[optind].endsWith(".gz")) {
                        in = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));
                    } else {
                        in = new BufferedReader(new InputStreamReader(url.openStream()));
                    }
                } else {
                    File fin = new File(args[optind]);
                    if (fin.getName().endsWith(".gz")) {
                        in = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(fin))));
                    } else {
                        in = new BufferedReader(new FileReader(fin));
                    }
                }
                pivot.read(in);
                in.close();
            } else {
                throw new IllegalArgumentException("Too many arguments");
            }
            pivot.printer.print();
        } catch (Throwable e) {
            StackTraceElement t[] = e.getStackTrace();
            System.err.println("Error Pivot:" + e.getClass().getName() + ":[" + t[t.length - 1].getLineNumber() + "]: " + e.getMessage());
            System.exit(-1);
        }
    }
}
