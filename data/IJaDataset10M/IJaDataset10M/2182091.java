package diet.task.collabMinitaskProceduralComms;

import diet.utils.StringOperations;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class ProcLangDetector {

    Random r = new Random();

    Vector proclang = new Vector();

    public ProcLangDetector() {
        proclang.addElement("now");
        proclang.addElement("then");
        proclang.addElement("after");
        proclang.addElement("before");
        proclang.addElement("while");
        proclang.addElement("during");
        proclang.addElement("whilst");
    }

    public String procLangMatch(String s) {
        String[] haystack = StringOperations.splitOnlyText(s);
        Vector needle = (Vector) proclang.clone();
        while (needle.size() > 0) {
            String sn = (String) proclang.elementAt(r.nextInt(proclang.size()));
            for (int j = 0; j < haystack.length; j++) {
                if (sn.equalsIgnoreCase(haystack[j])) return sn;
            }
            needle.remove(sn);
        }
        return null;
    }
}
