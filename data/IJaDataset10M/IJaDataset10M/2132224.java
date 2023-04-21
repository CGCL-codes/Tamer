package nts.noad;

import nts.io.Log;
import nts.io.Loggable;
import nts.io.CharCode;

public class Delimiter implements Loggable {

    public static final Delimiter NULL = null;

    public static final Delimiter VOID = new Delimiter((byte) 0, CharCode.NULL, (byte) 0, CharCode.NULL);

    private final byte smallFam;

    private final CharCode smallCode;

    private final byte largeFam;

    private final CharCode largeCode;

    public Delimiter(byte smallFam, CharCode smallCode, byte largeFam, CharCode largeCode) {
        this.smallFam = smallFam;
        this.smallCode = smallCode;
        this.largeFam = largeFam;
        this.largeCode = largeCode;
    }

    public byte getSmallFam() {
        return smallFam;
    }

    public CharCode getSmallCode() {
        return smallCode;
    }

    public byte getLargeFam() {
        return largeFam;
    }

    public CharCode getLargeCode() {
        return largeCode;
    }

    public boolean isVoid() {
        return (smallFam == 0 && smallCode == CharCode.NULL && largeFam == 0 && largeCode == CharCode.NULL);
    }

    public void addOn(Log log) {
        int n = (famCharNum(smallFam, smallCode) << 12) + famCharNum(largeFam, largeCode);
        log.add('"').add(Integer.toHexString(n).toUpperCase());
    }

    private int famCharNum(byte fam, CharCode code) {
        int n = fam << 8;
        if (code != CharCode.NULL) n += code.numValue();
        return n;
    }
}
