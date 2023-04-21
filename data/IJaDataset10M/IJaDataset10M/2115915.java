package it.unimib.disco.itis.polimar.discovery.nfpEvaluation;

import java.util.Iterator;
import java.util.Set;

public class IntervalEqualEvaluation extends QuantitativeEvaluation {

    @Override
    public double calculateDom(Set<String> reqSet, Set<String> offSet) {
        Iterator<String> itReq = reqSet.iterator();
        Iterator<String> itOff = offSet.iterator();
        double ar = Double.parseDouble(itReq.next());
        double br = Double.parseDouble(itReq.next());
        double off = Double.parseDouble(itOff.next());
        if (ar < br) {
            if ((ar <= off) && (off <= br)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if ((br <= off) && (off <= ar)) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
