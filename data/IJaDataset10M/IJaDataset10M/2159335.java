package org.sulweb.infureports;

import org.sulweb.infumon.common.session.MLHModeCapabilities;
import org.sulweb.infumon.common.session.MeasureUnit;
import org.sulweb.infumon.common.session.MeasuredFloat;
import org.sulweb.infumon.common.session.MeasuredString;
import org.sulweb.infumon.common.session.MeasuredValue;
import org.sulweb.infumon.common.session.Session;
import java.util.*;
import java.text.*;

public class MLHModeTextView extends TextView {

    private static DecimalFormat df = new DecimalFormat("####0.0###");

    private static DecimalFormat df1 = new DecimalFormat("####0.0");

    private boolean deliveryRateFirst;

    public MLHModeTextView(Session s) {
        this(s, true);
    }

    public MLHModeTextView(Session s, boolean deliveryRateFirst) {
        super(s);
        this.deliveryRateFirst = deliveryRateFirst;
    }

    protected int renderData(long time, int previousPropertyOrdinal) {
        MLHModeCapabilities tc = (MLHModeCapabilities) getSession();
        if (!tc.isMLHModeActive(time)) return previousPropertyOrdinal;
        Map<SortableString, MeasuredValue> m = getLastProperties();
        if (deliveryRateFirst) {
            previousPropertyOrdinal = drate(tc, m, previousPropertyOrdinal, time);
            previousPropertyOrdinal = total(tc, m, previousPropertyOrdinal, time);
            previousPropertyOrdinal = limit(tc, m, previousPropertyOrdinal, time);
        } else {
            previousPropertyOrdinal = limit(tc, m, previousPropertyOrdinal, time);
            previousPropertyOrdinal = total(tc, m, previousPropertyOrdinal, time);
            previousPropertyOrdinal = drate(tc, m, previousPropertyOrdinal, time);
        }
        return previousPropertyOrdinal;
    }

    private int drate(MLHModeCapabilities mc, Map m, int order, long time) {
        String drtitle = "Velocita' infusione";
        SortableString ssdrtitle = new SortableString(drtitle, order);
        float delrate = mc.getDeliveryRate(time);
        MeasuredFloat mfdelrate = new MeasuredFloat(delrate, MeasureUnit.mL_h, df);
        m.put(ssdrtitle, mfdelrate);
        return ssdrtitle.getOrder();
    }

    private int total(MLHModeCapabilities mc, Map m, int order, long time) {
        String tititle = "Totale infuso";
        SortableString sstititle = new SortableString(tititle, order);
        float tdev = mc.getTotalDelivered(time);
        MeasuredFloat mftdev = new MeasuredFloat(tdev, MeasureUnit.mL, df1);
        sstititle.setTotalDeliveredValue(true);
        m.put(sstititle, mftdev);
        return sstititle.getOrder();
    }

    private int limit(MLHModeCapabilities mc, Map m, int order, long time) {
        String dltitle = "Limite infusione";
        SortableString ssdltitle = new SortableString(dltitle, order);
        boolean nolimit = false;
        if (mc.isDeliveryLimitModeSet(time)) {
            float dlim = mc.getDeliveryLimit(time);
            if (dlim == 0.0F) nolimit = true; else {
                MeasuredFloat mfdlim = new MeasuredFloat(dlim, MeasureUnit.mL, df);
                m.put(ssdltitle, mfdlim);
            }
        } else nolimit = true;
        if (nolimit) {
            MeasuredString msnolimit = new MeasuredString("- - -", null);
            m.put(ssdltitle, msnolimit);
        }
        return ssdltitle.getOrder();
    }
}
