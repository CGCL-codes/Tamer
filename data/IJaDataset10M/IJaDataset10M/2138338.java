package rtjdds.rtps.messages.elements;

import org.omg.CORBA.portable.OutputStream;
import rtjdds.rtps.messages.MalformedSubmessageElementException;
import rtjdds.rtps.portable.InputPacket;
import rtjdds.rtps.types.FragmentNumber_t;
import rtjdds.rtps.types.FragmentNumber_tHelper;

/**
 * @author kerush
 *
 */
public class FragmentNumberSet extends SubmessageElement {

    protected FragmentNumber_t base;

    protected int nBits;

    protected int[] set;

    /**
	 * TODO: constructor weak: it can't set the size of the CDR encoded element...
	 * @param base
	 * @param set
	 */
    public FragmentNumberSet(FragmentNumber_t base, int nBits, int[] set) {
        super(0);
        this.base = base;
        this.nBits = nBits;
        this.set = set;
        super.size = SubmessageElement.FRAGMENT_NUMBER_SIZE + 4 + set.length * 4;
    }

    public static FragmentNumberSet read(InputPacket p) throws MalformedSubmessageElementException {
        FragmentNumber_t base = FragmentNumber_tHelper.read(p);
        int numBits = p.read_long();
        int M = (numBits + 31) / 32;
        int[] set = new int[M];
        for (int i = 0; i < M; i++) {
            set[i] = p.read_long();
        }
        return new FragmentNumberSet(base, numBits, set);
    }

    public void write(OutputStream os) {
        FragmentNumber_tHelper.write(os, this.base);
        os.write_long(this.nBits);
        int M = (this.nBits + 31) / 32;
        for (int i = 0; i < M; i++) {
            os.write_long(this.set[i]);
        }
    }
}
