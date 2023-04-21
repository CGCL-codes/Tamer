package org.sulweb.infumon.common.session;

/**
 * <p>Title: InfuGraph</p>
 * <p>Description: Frontend for Infumon database log display</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Elaborazione Dati Pinerolo srl</p>
 * @author unascribed
 * @version 1.0
 */
public interface ManualDrugConcentrationModeCapabilities extends SyringeModel {

    boolean isManualDrugConcentrationModeEnabled(long time);

    float getTotalDelivered(long time);

    float getDeliveryRate(long time);

    float getBodyWeight(long time);

    MeasureUnit getTotalDeliveredMeasureUnit(long time);

    MeasureUnit getDeliveryRateMeasureUnit(long time);

    float getDrugConcentration(long time);

    MeasureUnit getDrugConcentrationMeasureUnit(long time);
}
