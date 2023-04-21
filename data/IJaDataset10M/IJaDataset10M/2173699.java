package org.spnt.recognition.services;

import java.io.File;
import org.spantus.core.FrameVectorValues;
import org.spantus.core.extractor.IExtractor;
import org.spantus.core.extractor.IExtractorInputReader;
import org.spantus.core.extractor.IExtractorVector;
import org.spantus.core.extractor.IGeneralExtractor;
import org.spantus.logger.Logger;
import org.spantus.math.dtw.DtwService;
import org.spantus.math.services.MathServicesFactory;
import org.spantus.work.services.FeatureExtractor;
import org.spantus.work.services.FeatureExtractorImpl;
import org.spantus.work.services.ReaderDao;
import org.spantus.work.services.WorkServiceFactory;

public class CompareFeatures {

    private String workingExtractor = "LPC";

    private DtwService dtwService;

    private ReaderDao readerDao;

    private FeatureExtractor featureExtractor;

    @SuppressWarnings("unused")
    private Logger log = Logger.getLogger(getClass());

    public Float compareValues(FrameVectorValues targetValues, File sampleFile) {
        IExtractorInputReader sampleReader = getExtractorInputReader(sampleFile);
        IGeneralExtractor sampleExtractor = getFeatureExtractor().findExtractorByName(getWorkingExtractor(), sampleReader);
        Float distance = calculateDistance(targetValues, sampleExtractor);
        return distance;
    }

    /**
	 * 
	 * @param targetFile
	 * @param sampleFile
	 * @return
	 */
    public Float compareValues(File targetFile, File sampleFile) {
        IExtractorInputReader sampleReader = getExtractorInputReader(sampleFile);
        IExtractorInputReader targetReader = getExtractorInputReader(targetFile);
        IGeneralExtractor sampleExtractor = getFeatureExtractor().findExtractorByName(getWorkingExtractor(), sampleReader);
        IGeneralExtractor targetExtractor = getFeatureExtractor().findExtractorByName(getWorkingExtractor(), targetReader);
        ;
        Float distance = calculateDistance(targetExtractor, sampleExtractor);
        return distance;
    }

    /**
	 * 
	 * @param readerFile
	 * @return
	 */
    protected IExtractorInputReader getExtractorInputReader(File readerFile) {
        return getReaderDao().read(readerFile);
    }

    /**
	 * 
	 * @param targetExtractor
	 * @param sampleExtractor
	 * @return
	 */
    public Float calculateDistance(IGeneralExtractor targetExtractor, IGeneralExtractor sampleExtractor) {
        if (targetExtractor instanceof IExtractor) {
            return getDtwService().calculateDistance(((IExtractor) targetExtractor).getOutputValues(), ((IExtractor) sampleExtractor).getOutputValues());
        }
        if (targetExtractor instanceof IExtractorVector) {
            return getDtwService().calculateDistanceVector(((IExtractorVector) targetExtractor).getOutputValues(), ((IExtractorVector) sampleExtractor).getOutputValues());
        }
        return null;
    }

    public Float calculateDistance(FrameVectorValues targetValues, IGeneralExtractor sampleExtractor) {
        return getDtwService().calculateDistanceVector(targetValues, ((IExtractorVector) sampleExtractor).getOutputValues());
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String samplePath = "./test/du.sspnt.xml";
        String targetPath = "./test/vienas.sspnt.xml";
        File sampleFile = new File(samplePath);
        File targetFile = new File(targetPath);
        CompareFeatures compareFeatures = new CompareFeatures();
        Float f = compareFeatures.compareValues(targetFile, sampleFile);
        System.out.print("[main] comparition result: " + f);
    }

    public FeatureExtractor getFeatureExtractor() {
        if (featureExtractor == null) {
            featureExtractor = new FeatureExtractorImpl();
        }
        return featureExtractor;
    }

    public DtwService getDtwService() {
        if (dtwService == null) {
            dtwService = MathServicesFactory.createDtwService();
        }
        return dtwService;
    }

    public ReaderDao getReaderDao() {
        if (readerDao == null) {
            readerDao = WorkServiceFactory.createReaderDao();
        }
        return readerDao;
    }

    public String getWorkingExtractor() {
        return workingExtractor;
    }
}
