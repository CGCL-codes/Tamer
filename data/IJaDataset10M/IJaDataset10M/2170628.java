package q;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QxQ {

    private final List<RxR> QxQN;

    private QxQ(int n) {
        this.QxQN = new ArrayList<RxR>(n);
        Iterator<R> ity = Q.Q(n).liste().iterator();
        Iterator<R> itx = Q.Q(n).liste().iterator();
        while (ity.hasNext()) {
            R y = ity.next();
            while (itx.hasNext()) {
                QxQN.add(RxR.RxR(itx.next(), y));
            }
            itx = Q.Q(n).liste().iterator();
        }
    }

    public List<RxR> liste() {
        return QxQN;
    }

    public static QxQ QxQ(int n) {
        return new QxQ(n);
    }

    @Override
    public String toString() {
        super.toString();
        return QxQN.toString();
    }
}
