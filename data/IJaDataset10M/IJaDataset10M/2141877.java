package org.sulweb.infureports;

import org.sulweb.infumon.common.session.ManualDrugConcentrationModeCapabilities;
import org.sulweb.infumon.common.session.MeasureUnit;
import org.sulweb.infumon.common.session.MeasuredDouble;
import org.sulweb.infumon.common.session.MeasuredFloat;
import org.sulweb.infumon.common.session.MeasuredValue;
import org.sulweb.infumon.common.session.Session;
import java.util.*;
import java.text.*;

public class ManualDrugConcentrationTextView extends TextView {

    private static DecimalFormat df = new DecimalFormat("####0.0###");

    private static DecimalFormat calcdf = new DecimalFormat("####0.0");

    private boolean delratefirst;

    public ManualDrugConcentrationTextView(Session s, boolean delratefirst) {
        super(s);
        this.delratefirst = delratefirst;
    }

    public ManualDrugConcentrationTextView(Session s) {
        this(s, false);
    }

    protected int renderData(long time, int previousPropertyOrdinal) {
        ManualDrugConcentrationModeCapabilities mdcmc = (ManualDrugConcentrationModeCapabilities) getSession();
        if (!mdcmc.isManualDrugConcentrationModeEnabled(time)) return previousPropertyOrdinal;
        Map<SortableString, MeasuredValue> props = getLastProperties();
        if (delratefirst) {
            previousPropertyOrdinal = rate(mdcmc, props, previousPropertyOrdinal, time);
            previousPropertyOrdinal = computedRate(mdcmc, props, previousPropertyOrdinal, time);
            previousPropertyOrdinal = total(mdcmc, props, previousPropertyOrdinal, time);
        } else {
            previousPropertyOrdinal = total(mdcmc, props, previousPropertyOrdinal, time);
            previousPropertyOrdinal = rate(mdcmc, props, previousPropertyOrdinal, time);
            previousPropertyOrdinal = computedRate(mdcmc, props, previousPropertyOrdinal, time);
        }
        previousPropertyOrdinal = weight(mdcmc, props, previousPropertyOrdinal, time);
        return previousPropertyOrdinal;
    }

    private int weight(ManualDrugConcentrationModeCapabilities mc, Map m, int order, long time) {
        String patwei = "Peso";
        SortableString sspatwei = new SortableString(patwei, order);
        float wei = mc.getBodyWeight(time);
        MeasuredFloat mfwei = new MeasuredFloat(wei, MeasureUnit.kg, df);
        m.put(sspatwei, mfwei);
        return sspatwei.getOrder();
    }

    private int total(ManualDrugConcentrationModeCapabilities mc, Map m, int order, long time) {
        String titletotdev = "Totale infuso";
        SortableString sstotdev = new SortableString(titletotdev, order);
        float totdev = mc.getTotalDelivered(time);
        sstotdev.setTotalDeliveredValue(true);
        MeasuredFloat mftotdev = new MeasuredFloat(totdev, mc.getTotalDeliveredMeasureUnit(time), df);
        m.put(sstotdev, mftotdev);
        return sstotdev.getOrder();
    }

    private int rate(ManualDrugConcentrationModeCapabilities mc, Map m, int order, long time) {
        String titledelrate = "Velocità infusione";
        SortableString ssdelrate = new SortableString(titledelrate, order);
        float delrate = mc.getDeliveryRate(time);
        MeasuredFloat mfdelrate = new MeasuredFloat(delrate, mc.getDeliveryRateMeasureUnit(time), df);
        m.put(ssdelrate, mfdelrate);
        return ssdelrate.getOrder();
    }

    private int computedRate(ManualDrugConcentrationModeCapabilities mc, Map m, int order, long time) {
        String titlecompdrate = "Vel. infusione calc.";
        SortableString sscompdrate = new SortableString(titlecompdrate, order);
        MeasureUnit drateMu = mc.getDeliveryRateMeasureUnit(time);
        if (drateMu == MeasureUnit.mL_h) return rate(mc, m, order, time);
        double compdrate = mc.getDeliveryRate(time) / mc.getDrugConcentration(time);
        if (drateMu.equals(MeasureUnit.mg_kg_h) || drateMu.equals(MeasureUnit.mg_kg_min) || drateMu.equals(MeasureUnit.ug_kg_h) || drateMu.equals(MeasureUnit.ug_kg_min)) {
            compdrate = compdrate * mc.getBodyWeight(time);
            if (drateMu.equals(MeasureUnit.mg_kg_min) || drateMu.equals(MeasureUnit.ug_kg_min)) compdrate = compdrate * 60.0;
        }
        MeasureUnit concMu = mc.getDrugConcentrationMeasureUnit(time);
        if (concMu.equals(MeasureUnit.ug_mL) && (drateMu.equals(MeasureUnit.mg_kg_h) || drateMu.equals(MeasureUnit.mg_kg_min) || drateMu.equals(MeasureUnit.mg_h))) compdrate = compdrate * 1000.0; else if (concMu.equals(MeasureUnit.mg_mL) && (drateMu.equals(MeasureUnit.ug_kg_h) || drateMu.equals(MeasureUnit.ug_kg_min) || drateMu.equals(MeasureUnit.ug_h))) compdrate = compdrate / 1000.0;
        MeasuredDouble mdcompdrate = new MeasuredDouble(compdrate, MeasureUnit.mL_h, calcdf);
        m.put(sscompdrate, mdcompdrate);
        return sscompdrate.getOrder();
    }
}
