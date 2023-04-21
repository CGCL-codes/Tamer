package corina.formats;

import corina.Sample;
import corina.Element;
import corina.ui.I18n;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
   A Tucson file containing multiple samples.

   WRITEME

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id: PackedTucson.java,v 1.4 2006/08/13 04:36:37 lucasmo Exp $
*/
public class PackedTucson extends Tucson implements PackedFileType {

    public String toString() {
        return I18n.getText("format.packed_tucson");
    }

    public String getDefaultExtension() {
        return ".TUC";
    }

    public Sample load(BufferedReader r) throws IOException {
        throw new WrongFiletypeException();
    }

    private static String commonPrefix(String s1, String s2) {
        int i;
        for (i = 0; i < s1.length() && i < s2.length(); i++) if (s1.charAt(i) != s2.charAt(i)) break;
        return s1.substring(0, i);
    }

    public void saveSamples(List sl, BufferedWriter w) throws IOException {
        List outsamples = new ArrayList(sl.size());
        String prefix = null;
        for (int i = 0; i < sl.size(); i++) {
            Sample s = (Sample) sl.get(i);
            if (s.elements != null) {
                for (int j = 0; j < s.elements.size(); j++) {
                    Sample tmp = ((Element) s.elements.get(j)).load();
                    try {
                        if (prefix == null) prefix = tmp.meta.get("id").toString(); else prefix = commonPrefix(prefix, tmp.meta.get("id").toString());
                    } catch (NullPointerException npe) {
                        throw new IOException("Invalid META ID in file " + tmp.meta.get("filename"));
                    }
                    outsamples.add(tmp);
                }
            } else {
                try {
                    if (prefix == null) prefix = s.meta.get("id").toString(); else prefix = commonPrefix(prefix, s.meta.get("id").toString());
                } catch (NullPointerException npe) {
                    throw new IOException("Invalid META ID in file " + s.meta.get("filename"));
                }
                outsamples.add(s);
            }
        }
        save3LineHeader(w, prefix);
        for (int i = 0; i < outsamples.size(); i++) saveData((Sample) outsamples.get(i), w);
    }

    public void save(Sample s, BufferedWriter w) throws IOException {
        if (s.elements == null) throw new IOException("Packed Tucson format is only available " + "for summed samples with Elements");
        int n = s.elements.size();
        Sample buf[] = new Sample[n];
        for (int i = 0; i < n; i++) buf[i] = ((Element) s.elements.get(i)).load();
        String prefix = buf[0].meta.get("id").toString();
        for (int i = 1; i < n; i++) prefix = commonPrefix(prefix, buf[i].meta.get("id").toString());
        save3LineHeader(w, prefix);
        for (int i = 0; i < n; i++) {
            s = buf[i];
            saveData(s, w);
        }
    }

    private void save3LineHeader(BufferedWriter w, String id) throws IOException {
        if (id.length() > 6) id = id.substring(0, 6); else while (id.length() < 6) id += " ";
        w.write(id + " 1 Untitled");
        w.newLine();
        w.write(id + " 2 ");
        w.newLine();
        w.write(id + " 3 ");
        w.newLine();
    }
}
