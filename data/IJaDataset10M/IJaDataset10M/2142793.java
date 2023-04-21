package org.spantus.math.services;

import org.spantus.math.cluster.ClusterService;
import org.spantus.math.cluster.KNNServiceImpl;
import org.spantus.math.dtw.DtwService;
import org.spantus.math.dtw.DtwServiceJavaMLImpl;
import org.spantus.math.dtw.DtwServiceJavaMLImpl.JavaMLSearchWindow;
import org.spantus.math.services.impl.ConvexHullServiceImpl;
import edu.cmu.sphinx.frontend.frequencywarp.PLPCepstrumProducer;

/**
 * 
 * @author Mindaugas Greibus
 * 
 * @since 0.0.1
 * 
 *        Created 2008.09.28
 * 
 */
public abstract class MathServicesFactory {

    static FFTService fftService;

    static MFCCService mfccService;

    static LPCService lpcService;

    static DtwService dtwService;

    static ClusterService knnService;

    static ConvexHullService convexHullService;

    public static FFTService createFFTService() {
        if (fftService == null) {
            fftService = new FFTServiceSphinxImpl();
        }
        return fftService;
    }

    public static MFCCService createMFCCService() {
        return new MFCCServiceSphinxImpl();
    }

    public static PLPService createPLPService() {
        int numbersOfFilters = 32;
        PLPServiceSphinxImpl plpServiceSphinxImpl = new PLPServiceSphinxImpl(numbersOfFilters);
        plpServiceSphinxImpl.setPlpCepstrumProducer(new PLPCepstrumProducer(numbersOfFilters, 13, 14));
        return new PLPServiceSphinxImpl();
    }

    public static DtwService createDtwService() {
        if (dtwService == null) {
            DtwServiceJavaMLImpl dtwServiceImpl = new DtwServiceJavaMLImpl();
            dtwService = dtwServiceImpl;
        }
        return dtwService;
    }

    public static DtwService createDtwService(Integer searchRadius, JavaMLSearchWindow javaMLSearchWindow) {
        DtwServiceJavaMLImpl dtwServiceImpl = new DtwServiceJavaMLImpl();
        dtwServiceImpl.setSearchRadius(searchRadius);
        dtwServiceImpl.setSearchWindow(javaMLSearchWindow);
        return dtwServiceImpl;
    }

    public static LPCService createLPCService() {
        if (lpcService == null) {
            lpcService = new LPCServiceImpl();
        }
        return lpcService;
    }

    public static ClusterService createKnnService() {
        if (knnService == null) {
            knnService = new KNNServiceImpl();
        }
        return knnService;
    }

    public static ConvexHullService createConvexHullService() {
        if (convexHullService == null) {
            convexHullService = new ConvexHullServiceImpl();
        }
        return convexHullService;
    }
}
