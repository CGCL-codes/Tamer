package org.gnf.trace;

import org.biojava.bio.program.abi.*;
import java.util.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: At each index position set Y or N
 * for whether the highest or second highest peaks overlap with
 * the previous or next peak.</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: GNF</p>
 *
 * @author Keith Ching
 * @version $GNF: projects/gi/seqtracs/src/org/gnf/trace/OverlappingPeakFeature.java,v 1.2 2005/07/29 22:52:25 kching Exp $
 */
public class OverlappingPeakFeature extends TraceFeatureProducer {

    public OverlappingPeakFeature(ABITrace traceobj) {
        super.setTrace(traceobj);
    }

    protected List calculate(ABITrace traceobj) {
        List data = new ArrayList();
        String highbase = "";
        String secondbase = "";
        HashMap orderMap = new HashMap();
        int[] Aop = getOverlappingPeakArray(A);
        int[] Top = getOverlappingPeakArray(T);
        int[] Gop = getOverlappingPeakArray(G);
        int[] Cop = getOverlappingPeakArray(C);
        orderMap.put("A", Aop);
        orderMap.put("G", Gop);
        orderMap.put("C", Cop);
        orderMap.put("T", Top);
        int[] high;
        int[] second;
        int highvalue = 0;
        int secondvalue = 0;
        List order;
        for (int baseidx = 0; baseidx < basecall.length; baseidx++) {
            order = orderBaseCalls(baseidx);
            highbase = (String) order.get(0);
            secondbase = (String) order.get(1);
            if (orderMap.containsKey(highbase)) {
                high = (int[]) orderMap.get(highbase);
                highvalue = high[baseidx];
            } else {
                highvalue = 1;
            }
            if (orderMap.containsKey(secondbase)) {
                second = (int[]) orderMap.get(secondbase);
                secondvalue = second[baseidx];
            } else {
                secondvalue = 1;
            }
            if (highvalue == 0 || secondvalue == 0) {
                data.add("Y");
            } else {
                data.add("N");
            }
        }
        return (data);
    }

    private int[] getOverlappingPeakArray(int[] bases) {
        int[] sep = new int[basecall.length];
        int peak = 0;
        int width = 0;
        int minWidth = 5;
        double high = 0.0;
        double peakhigh = 0.0;
        for (int i = 0; i < basecall.length; i++) {
            peak = basecall[i];
            if (i + 1 < basecall.length) {
                width = (basecall[i + 1] - peak) / 3;
            } else {
                width = minWidth;
            }
            peakhigh = bases[peak];
            high = peakhigh;
            for (int j = peak - width; j <= peak + width; j++) {
                if (j >= 0 && j <= basecall[basecall.length - 1]) {
                    if (bases[j] > high) {
                        high = bases[j];
                    }
                }
            }
            if ((peakhigh >= high * 0.9) && (peakhigh > 0)) {
                sep[i] = 1;
            } else {
                sep[i] = 0;
            }
        }
        return (sep);
    }
}
