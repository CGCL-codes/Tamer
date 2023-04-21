package ra.supsimmol;

import java.util.Comparator;
import org.openscience.cdk.interfaces.IAtom;

public class LengthContainerComparator implements Comparator<LengthContainer> {

    @Override
    public int compare(LengthContainer o1, LengthContainer o2) {
        double result = o1.getDistance() - o2.getDistance();
        if (result < 0) {
            return -1;
        } else if (result > 0) {
            return 1;
        } else return 0;
    }
}
