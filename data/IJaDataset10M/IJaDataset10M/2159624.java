package src.projects.findPeaks.objects;

import java.util.Vector;
import src.lib.ioInterfaces.Log_Buffer;

/**
 * A library for storing Sequences.
 * @author afejes
 * @version $Revision: 2879 $
 */
public class SeqStore {

    private Vector<String> sequences;

    private static boolean display_version = true;

    private Log_Buffer LB;

    public SeqStore(Log_Buffer logbuffer) {
        this.LB = logbuffer;
        if (display_version) {
            LB.Version("SeqStore", "$Revision: 2879 $");
            display_version = false;
        }
        sequences = new Vector<String>();
    }

    /**
	 * 
	 * @param a
	 * @return  index number to access that string;
	 */
    public int add(String a) {
        sequences.add(a);
        return (sequences.size() - 1);
    }

    public String get(int a) {
        if (a < 0 || sequences.size() <= a) {
            return "";
        } else {
            return sequences.get(a);
        }
    }

    public void reset() {
        sequences = new Vector<String>();
    }

    public void clear() {
        sequences = null;
    }
}
