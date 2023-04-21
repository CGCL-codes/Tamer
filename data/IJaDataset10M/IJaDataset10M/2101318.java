package phex.gui.comparator;

import java.util.*;
import phex.common.*;

public class TransferRateComparator implements Comparator<TransferDataProvider> {

    public int compare(TransferDataProvider p1, TransferDataProvider p2) {
        int p1ltr = p1.getLongTermTransferRate();
        int p2ltr = p2.getLongTermTransferRate();
        if (p1ltr < p2ltr) {
            return -1;
        } else if (p1ltr == p2ltr && p1 == p2) {
            return 0;
        } else {
            return 1;
        }
    }
}
