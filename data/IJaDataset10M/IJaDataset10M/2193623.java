package staticdata;

import io.NanoDropData;

/**
 *
 * @author EddyM
 */
public class StaticNanoDropData {

    private static NanoDropData nanoDropData1;

    /** Creates a new instance of StaticNanoDropData */
    public StaticNanoDropData() {
    }

    public static NanoDropData getNanoDropData() {
        if (nanoDropData1 == null) setNanoDropData(new NanoDropData());
        return nanoDropData1;
    }

    public static void setNanoDropData(NanoDropData nanoDropData) {
        nanoDropData1 = nanoDropData;
    }

    public static void sort() {
        getNanoDropData().sortByDate();
    }
}
