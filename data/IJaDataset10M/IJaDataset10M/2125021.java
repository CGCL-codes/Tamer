package pogvue.gui;

import pogvue.analysis.AAFrequency;
import pogvue.datamodel.Alignment;
import pogvue.datamodel.AlignmentI;
import pogvue.datamodel.SequenceI;
import pogvue.io.FileParse;
import pogvue.io.FastaFile;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class FindMotifs {

    private static Hashtable conservedKmers(AlignmentI align, SequenceI[] kmers) {
        Hashtable conshash = new Hashtable();
        for (SequenceI kmer2 : kmers) {
            conshash.put(kmer2.getSequence(), new Vector());
        }
        Vector sel = new Vector();
        for (int i = 0; i < align.getHeight(); i++) {
            if (align.getSequenceAt(i).getName().indexOf("Ensembl") >= 0) {
                sel.addElement(align.getSequenceAt(i));
            }
        }
        Hashtable blocks = AAFrequency.findBlockStarts(align.getSequences(), 1, align.getWidth(), sel);
        SequenceI ref = align.getSequenceAt(0);
        for (SequenceI kmer1 : kmers) {
            String kmer = kmer1.getSequence();
            Enumeration en = blocks.keys();
            while (en.hasMoreElements()) {
                Integer pos = (Integer) en.nextElement();
                int len = (Integer) blocks.get(pos);
                if (len >= kmer.length() && ref.getLength() > (pos + len)) {
                    int start = pos;
                    String bstr = ref.getSequence().substring(pos, pos + len + 1);
                    if (bstr.indexOf(kmer) >= 0) {
                        System.out.println("Bstr " + start + " " + kmer + " " + bstr + " " + (pos + bstr.indexOf(kmer)));
                        Vector v = (Vector) conshash.get(kmer);
                        v.addElement(pos.intValue() + bstr.indexOf(kmer));
                    }
                }
            }
        }
        return conshash;
    }

    public static void main(String[] args) {
        try {
            FileParse fp = new FileParse(args[0], "File");
            FastaFile ff = new FastaFile(args[1], "File");
            SequenceI[] kmer = ff.getSeqsAsArray();
            System.out.println("Kmer " + kmer.length);
            String line;
            while ((line = fp.nextLine()) != null) {
                FastaFile tmpf = new FastaFile("/Users/mclamp/cvs/mut_scripts/tss3/tot" + line, "File");
                SequenceI[] s = tmpf.getSeqsAsArray();
                Hashtable conshash = conservedKmers(new Alignment(s), kmer);
                Enumeration en = conshash.keys();
                System.out.print("File " + line);
                while (en.hasMoreElements()) {
                    String key = (String) en.nextElement();
                    Vector v = (Vector) conshash.get(key);
                    System.out.print(key + " " + v.size() + " " + v + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("Can't read file");
        }
    }
}
