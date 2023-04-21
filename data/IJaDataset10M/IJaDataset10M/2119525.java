package edu.dimacs.mms.borj.rcv;

import edu.dimacs.mms.boxer.*;
import java.util.*;
import java.io.*;

/** This program prepares XML files with labels of the examples ("the
    label files"), based on the labels found in the TREC-style QREL
    files that come with the original RCV1-v2's corpus. These XML
    files can later be given to BORJ to be used together with the
    feature-only XML files produced by RcvToXML.

    The output XML file produced by QrelToXML may not contain all the
    classes found in the input QREL files. There are several ways for
    the user to specify what labels he wants to appear in the output
    files:

    <ul> <li>You can have QrelToXML choose a few "most frequent"
    categories (regions, industries, and topics). When selecting "most
    frequent" topics, regions, etc. QrelToXML will use the frequency
    of labels in the entire original QREL files (rather than in any
    subset). In this case the program will also produce a suite
    definition file describing the categories it has selected.

    <li>Instead of letting QrelToXML choose categories based on its
    own rules, you can simply supply it with an XML file describing
    the categories. This file is similar in syntax to the suite
    defininition file given to BORJ (in fact, it probably should be
    the same file), but its "discrimination" elements contain an
    additional attribute not used by BORJ/BOXER (see below).

    </ul>

    Usage: 
<pre>
 java [-Dd=/home/rcv] [-Dout=/home/username/rcv-out] [-Dfrom=1] [-Dto=100] borj.rcv.Driver docids.txt [suite.xml]
</pre>

    In the command line above, "docids.txt" is a list of document
    IDs. The content of this file, together with the values of the
    options -Dto and -Dfrom (which can be used to restrict processing
    to only section of the file), determine the list of documents
    whose labels will be included in the output file.

    The output XML label file will be deposited in the directory
    specified by the "out" option; its name will be based on the name
    of the input doc ID file, and the "from" and "to" values (if any).

    The input suite definition file (see sample in <a
    href="../../boxer/doc-files/rcv-even-smaller-suite.xml">sample-data/rcv-even-smaller-suite.xml</a>)
    describes all discriminations you want to generate. Note the use
    of the "defaultclass" and "leftoversclass" attribute. The latter is
    particularly significant: if it's present (it normally should),
    all class labels other than those explicitly listed in the
    discrimination definition will be converted to this "leftover
    class" label. However, to save space, it won't be actually printed
    if it's the same as the "defaultclass". There is also the "qrel"
    attribute in each discrimination; it specifies the (relative) name
    of the QREL file on labels from which this discrimination will be
    based.

    if the input suite definition file is omitted from the command
    line, the program will revert to the behavior from its early
    versions (prior to 0.5.007), and will create a suite itself,
    based on the most frequent categories found in the QREL files.

    Options: 

    <ul>
    <li>-Dd=... specifies the directory where to look for the
    original QREL files (whose relative names are supplied via the
    "qrel" atributes in the input suite definition file, or [in the
    highest-freq mode] are hard-coded into this program)

    <li>-Dd=... specifies the directory where the output files (the
    XML label files, and [when applicable] also the auto-generated
    suite definition file) will be deposited.

    </ul>

    See the scripts src/borj/rcv/qrelToXml.sh and
    src/borj/rcv/qrelToXmlCustom.sh for more usage examples.

 */
public class QrelToXML {

    static void usage() {
        usage(null);
    }

    static void usage(String m) {
        System.out.println("Usage: java [-Dd=/home/rcv] [-Dout=/home/username/rcv-out] [-Dfrom=1] [-Dto=100] [-Dout=output-directory | -Doutsuite=suite-out.xml -Doutlabels=labels-out.xml] borj.rcv.Driver docids.txt [suite.xml]");
        if (m != null) {
            System.out.println(m);
        }
        System.exit(1);
    }

    static ParseConfig ht = new ParseConfig();

    public static void main(String argv[]) throws IOException, org.xml.sax.SAXException, BoxerXMLException {
        String d = null;
        d = ht.getOption("d", d);
        if (d == null) usage("Option -Dd=... must be set");
        String outdir = ht.getOption("out", null);
        if (outdir == null) usage("Option -Dout=... must be set");
        int from = ht.getOption("from", 0), to = ht.getOption("to", 0);
        if (argv.length < 1 || argv.length > 2) usage();
        String idFile = argv[0];
        System.out.println("Reading doc IDs from " + idFile + (from == 0 ? " beginning" : " line " + from) + " to" + (to == 0 ? " end" : " line " + to));
        int a[] = RcvUtil.readIds(idFile, from, to);
        IntSet docids = new IntSet(a);
        String suitein = (argv.length > 1) ? argv[1] : null;
        Suite suite = (suitein != null) ? new Suite(new File(suitein)) : buildHiFreqSuite(d, outdir);
        FeatureDictionary dic = new FeatureDictionary();
        HashMap<Integer, DataPoint> docs = new HashMap<Integer, DataPoint>();
        Vector<DataPoint.FVPair> empty = new Vector<DataPoint.FVPair>();
        for (int docid : a) {
            docs.put(new Integer(docid), new DataPoint(empty, dic, "" + docid));
        }
        HashMap<String, Vector<Discrimination>> qrelFile2disList = new HashMap<String, Vector<Discrimination>>();
        for (int did = 0; did < suite.disCnt(); did++) {
            Discrimination dis = suite.getDisc(did);
            String qrel = dis.getQrelFileName();
            if (dis.getName().equals(Suite.SYSDEFAULTS)) continue;
            if (qrel == null) throw new IllegalArgumentException("No source QREL file indicated for discirmination " + dis.getName() + " !");
            qrel = d + File.separator + qrel;
            Vector<Discrimination> v = qrelFile2disList.get(qrel);
            if (v == null) qrelFile2disList.put(qrel, v = new Vector<Discrimination>());
            v.addElement(dis);
        }
        for (String qrel : qrelFile2disList.keySet()) {
            Vector<Discrimination> disList = qrelFile2disList.get(qrel);
            System.out.println("Generating class labels for " + disList.size() + " discirminations based on data from QREL file " + qrel);
            convertQrel(qrel, disList, suite, docs);
        }
        Vector<DataPoint> v = RcvToXML.setToVector(a, docs, suite);
        String setName = RcvToXML.fnameToSetname(idFile, from, to);
        String outqrel = ht.getOption("outlabels", outdir + File.separator + setName + "-qrel.xml");
        System.out.println("Writing data set " + setName + " with labels for " + v.size() + " documents to file " + outqrel);
        DataPoint.saveAsXML(v, setName, outqrel);
    }

    /** Builds a suite including a few highest-frequency categories
      from all three QREL files, and saves to an XML file.

BOXER_Small_Topics
     Training Set: Prefixes starting at RandIDa 000001
     Test Set: Last 1000 RandIDa
     Categories: The 10 most frequent Topic categories, treated as 10 binary discriminations

BOXER_Small_Industries
     Training Set: Prefixes starting at RandIDa 000001
     Test Set: Last 1000 RandIDa
     Categories: A 10-way polytomous classification.  The categories should be NoIndustry, the 8 most frequent original Industry categories, and Other where Other corresponds to any other Industry category.

BOXER_Small_Regions
     Training Set: Prefixes starting at RandIDa 000001
     Test Set: Last 1000 RandIDa
     Categories: A 10-way polytomous classification.  The categories should be the 9 most frequent Region categories, and Other where Other corresponds to any other Region category.
	*/
    private static Suite buildHiFreqSuite(String d, String outdir) throws java.io.IOException {
        Suite suite = new Suite("RCV_Small_20081206");
        int ntop = 10;
        String qrelTopic = "rcv1-v2.topics.qrels";
        SiPair[] topTopics = topCat(null, d + File.separator + qrelTopic, ntop);
        System.out.println("Top " + topTopics.length + " topics:");
        for (int i = 0; i < topTopics.length; i++) {
            System.out.println("(" + i + ") " + topTopics[i].cat + ": " + topTopics[i].cnt + " docs");
            String disName = RcvToXML.TOPIC + "_" + topTopics[i].cat;
            String c[] = { topTopics[i].cat, RcvToXML.OTHER };
            Discrimination dis = suite.addDiscrimination(disName, c);
            dis.setDefaultClass(RcvToXML.OTHER);
            dis.setLeftoversClass(RcvToXML.OTHER);
            dis.setQrelFileName(qrelTopic);
        }
        String qrelInd = "rcv1-v2.industries.qrels";
        SiPair[] topInd = topCat(null, d + File.separator + qrelInd, ntop = 8);
        System.out.println("Top " + topInd.length + " industries:");
        String disName = RcvToXML.INDUSTRY;
        Discrimination dis = suite.addDiscrimination(disName);
        for (int i = 0; i < topInd.length; i++) {
            System.out.println("(" + i + ") " + topInd[i].cat + ": " + topInd[i].cnt + " docs");
            dis.addClass(topInd[i].cat);
        }
        dis.addClass(RcvToXML.OTHER);
        dis.addClass(RcvToXML.NONE);
        dis.setDefaultClass(RcvToXML.NONE);
        dis.setLeftoversClass(RcvToXML.OTHER);
        dis.setQrelFileName(qrelInd);
        dis.commitStructure(Suite.DCS.Fixed);
        String qrelReg = "rcv1-v2.regions.qrels";
        SiPair[] topReg = topCat(null, d + File.separator + qrelReg, ntop = 9);
        System.out.println("Top " + topReg.length + " regions:");
        disName = RcvToXML.REGION;
        dis = suite.addDiscrimination(disName);
        for (int i = 0; i < topReg.length; i++) {
            System.out.println("(" + i + ") " + topReg[i].cat + ": " + topReg[i].cnt + " docs");
            dis.addClass(topReg[i].cat);
        }
        dis.addClass(RcvToXML.OTHER);
        dis.setDefaultClass(RcvToXML.OTHER);
        dis.setLeftoversClass(RcvToXML.OTHER);
        dis.setQrelFileName(qrelReg);
        String outsuite = ht.getOption("outsuite", outdir + File.separator + "rcv-small-cat-suite.xml");
        System.out.println("Saving suite to " + outsuite);
        suite.saveAsXML(outsuite);
        return suite;
    }

    /** Puts in the classes already listed in the Suite, or "other" 	
	@param replaceName If this parameter is not null, classes absent from the current suite will be replaced with the class of this name. A class of this name must already exist in the suite,
     */
    static void convertQrel(String fname, Vector<Discrimination> disList, Suite suite, HashMap<Integer, DataPoint> docs) throws IOException {
        for (QrelIterator it = new QrelIterator(fname); it.hasNext(); ) {
            QrelEntry q = it.next();
            Integer key = new Integer(q.docid);
            DataPoint doc = docs.get(key);
            if (doc == null) continue;
            for (Discrimination dis : disList) {
                Discrimination.Cla cla = dis.getCla(q.cat);
                if (cla == null) cla = dis.getLeftoversCla();
                if (cla != null && cla != dis.getDefaultCla()) {
                    doc.addClass(cla, false);
                }
            }
        }
    }

    /** More efficient for fairly dense sets than HashSet<Integer> */
    static class IntSet {

        BitSet b;

        /** Creates a set that will be able hold elements with values
	 * from 0 thru n-1 */
        IntSet(int n) {
            b = new BitSet(n);
        }

        void add(int x) {
            b.set(x);
        }

        boolean contains(int x) {
            return b.get(x);
        }

        IntSet(int a[]) {
            b = new BitSet();
            for (int x : a) b.set(x);
        }
    }

    /** Returns a set of all categories that occur in the QREL file
     *  in association with document ids from a specified set
     @param fname QREL File to read
     @docs List of doc ids. The function only looks at the lines in the QREL file where the doc id is in this list
     */
    static HashSet<String> listCat(IntSet docs, String fname) throws IOException {
        HashSet<String> cats = new HashSet<String>();
        for (QrelIterator it = new QrelIterator(fname); it.hasNext(); ) {
            QrelEntry q = it.next();
            if (docs.contains(new Integer(q.docid))) cats.add(q.cat);
        }
        return cats;
    }

    /** Returns a HashMap that maps category name to the number of docs
	from the specified set that have that category assigned to it in the
	QREL file
	@param docs The set of documents we want to look at. If null, look at all docs in the Qrel file
    */
    static HashMap<String, Integer> listCatCnt(IntSet docs, String fname) throws IOException {
        HashMap<String, Integer> cats = new HashMap<String, Integer>();
        for (QrelIterator it = new QrelIterator(fname); it.hasNext(); ) {
            QrelEntry q = it.next();
            if (docs == null || docs.contains(new Integer(q.docid))) {
                Integer z = cats.get(q.cat);
                int cnt = (z == null) ? 0 : z.intValue();
                cats.put(q.cat, new Integer(cnt + 1));
            }
        }
        return cats;
    }

    private static class SiPair implements Comparable {

        String cat;

        int cnt;

        SiPair(Map.Entry<String, Integer> x) {
            cat = x.getKey();
            cnt = x.getValue().intValue();
        }

        /** For reverse (descending) order */
        public int compareTo(Object o) throws ClassCastException {
            return ((SiPair) o).cnt - cnt;
        }
    }

    /** Gets most populated categories, based on the number of their
        occurrences in a specified QREL file (or its subset).

	@param docs The set of documents we want to look at. If null, look at all docs in the Qrel file
     */
    static SiPair[] topCat(IntSet docs, String fname, int ntop) throws IOException {
        HashMap<String, Integer> h = listCatCnt(docs, fname);
        int n = h.size();
        SiPair a[] = new SiPair[n];
        int pos = 0;
        for (Map.Entry<String, Integer> x : h.entrySet()) {
            a[pos++] = new SiPair(x);
        }
        Arrays.sort(a);
        return (ntop < n) ? Arrays.copyOf(a, ntop) : a;
    }
}
