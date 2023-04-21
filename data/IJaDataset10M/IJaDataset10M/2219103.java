package org.slasoi.gslam.monitoring.manager.demos.features;

import java.util.LinkedList;
import org.slasoi.monitoring.common.features.ComponentMonitoringFeatures;
import org.slasoi.monitoring.common.features.MonitoringFeature;
import org.slasoi.monitoring.common.features.impl.FeaturesFactoryImpl;

/**
 * SLA Component Monitoring Features Test for the B4 example.
 **/
public class ORCBCoverageFeatures {

    /** everest Features. **/
    private EverestTestMonitoringFeatures everestFeatures = null;

    /**
     * Constructor.
     */
    public ORCBCoverageFeatures() {
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
            type = "ORCInventoryService/getProductDetails";
            mflist.add(EverestTestMonitoringFeatures.buildSensor(type, type, "RESPONSE"));
            type = "ORCInventoryService/bookSale";
            mflist.add(EverestTestMonitoringFeatures.buildSensor(type, type, "RESPONSE"));
            cmfeatures[2].setMonitoringFeatures(EverestTestMonitoringFeatures.mfListToArray(mflist));
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return cmfeatures;
    }
}
