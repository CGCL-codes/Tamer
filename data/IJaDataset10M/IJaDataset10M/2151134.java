package org.gnf.trace;

import org.biojava.bio.program.abi.*;
import java.util.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: GNF</p>
 *
 * @author Keith Ching
 * @version $GNF: projects/gi/seqtracs/src/org/gnf/trace/SeparatedPeakFeature.java,v 1.3 2005/08/11 04:46:51 kching Exp $
 */
public class SeparatedPeakFeature extends TraceFeatureProducer {

    public SeparatedPeakFeature(ABITrace traceobj) {
        super.setTrace(traceobj);
    }

    protected List calculate(ABITrace traceobj) {
        List data = new ArrayList();
        String highbase = "";
        String secondbase = "";
        HashMap orderMap = new HashMap();
        int[] Aop = getSeparatedPeakArray(A);
        int[] Top = getSeparatedPeakArray(T);
        int[] Gop = getSeparatedPeakArray(G);
        int[] Cop = getSeparatedPeakArray(C);
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
                data.add("N");
            } else {
                data.add("Y");
            }
        }
        return (data);
    }

    private int[] getSeparatedPeakArray(int[] bases) {
        int[] sep = new int[basecall.length];
        int index = 0;
        int previousIndex = 0;
        double low = 0.0;
        sep[0] = 1;
        for (int i = 1; i < basecall.length; i++) {
            previousIndex = basecall[i - 1];
            index = basecall[i];
            low = bases[previousIndex];
            for (int j = previousIndex; j <= index; j++) {
                if (bases[j] < low) {
                    low = bases[j];
                }
            }
            if ((bases[index] * 0.75) > low) {
                sep[i] = 1;
            } else {
                sep[i] = 0;
            }
        }
        return (sep);
    }
}
