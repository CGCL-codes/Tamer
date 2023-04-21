package org.slasoi.studio.smart.features;

import java.util.LinkedList;
import org.slasoi.monitoring.common.features.ComponentMonitoringFeatures;
import org.slasoi.monitoring.common.features.MonitoringFeature;
import org.slasoi.monitoring.common.features.impl.FeaturesFactoryImpl;

/**
 * SLA Component Monitoring Features Test for the B4 example.
 **/
public class SLACoverageFeatures {

    /** everest Features. **/
    private EverestTestMonitoringFeatures everestFeatures = null;

    /**
     * Constructor.
     */
    public SLACoverageFeatures() {
        everestFeatures = new EverestTestMonitoringFeatures();
    }

    /**
     * Constructs a set of ComponentMonitoringFeatures.
     * @return org.slasoi.monitoring.common.features.
     * ComponentMonitoringFeatures[]
     @see org.slasoi.monitoring.common.features.ComponentMonitoringFeatures
     **/
    public final ComponentMonitoringFeatures[] buildTest() {
        FeaturesFactoryImpl ffi = new FeaturesFactoryImpl();
        ComponentMonitoringFeatures[] cmfeatures = new ComponentMonitoringFeatures[2];
        cmfeatures = everestFeatures.buildTest();
        try {
            cmfeatures[2] = ffi.createComponentMonitoringFeatures();
            cmfeatures[2].setUuid("777e8400-sss2-41d4-a716-406075043333");
            cmfeatures[2].setType("REASONER");
            LinkedList<MonitoringFeature> mflist = new LinkedList<MonitoringFeature>();
            String type = "";
            type = "BookingEvaluation/getSatisfactionLevels/MedicalTreatmentQuality";
            mflist.add(EverestTestMonitoringFeatures.buildSensor(type, type, "noevent"));
            type = "BookingEvaluation/getSatisfactionLevels/MobilityQuality";
            mflist.add(EverestTestMonitoringFeatures.buildSensor(type, type, "noevent"));
            type = "BookingEvaluation/getSatisfactionLevels/OverallQuality";
            mflist.add(EverestTestMonitoringFeatures.buildSensor(type, type, "noevent"));
            type = "BookingEvaluation/getSatisfactionLevels/CallCenterQuality";
            mflist.add(EverestTestMonitoringFeatures.buildSensor(type, type, "noevent"));
            type = "BookingEvaluation/getSatisfactionLevels";
            mflist.add(EverestTestMonitoringFeatures.buildSensor(type, type, "noevent"));
            type = "Booking/phoneCall/returnValue";
            type = "Mobility/transport";
            mflist.add(EverestTestMonitoringFeatures.buildSensor(type, type, "noevent"));
            mflist.add(EverestTestMonitoringFeatures.buildSensor(type, type, "noevent"));
            type = "http://www.slaatsoi.org/commonTerms#VM_Access_Point";
            mflist.add(EverestTestMonitoringFeatures.buildSensor(type, type, "RESPONSE"));
            cmfeatures[2].setMonitoringFeatures(EverestTestMonitoringFeatures.mfListToArray(mflist));
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return cmfeatures;
    }
}
