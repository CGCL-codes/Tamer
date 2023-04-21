package org.spantus.exp.segment.exec.classification;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.spantus.core.threshold.ClassifierEnum;
import org.spantus.exp.segment.beans.ComparisionResult;
import org.spantus.exp.segment.services.impl.ExperimentHsqlDao;
import org.spantus.extractor.impl.ExtractorEnum;
import org.spantus.logger.Logger;
import org.spantus.utils.FileUtils;
import org.spantus.utils.StringUtils;

public class ExpDBSegmentation extends ExpSegmentation {

    /**
     * n=6 features, k=1,2,3 possible bins
     *
     * (combination n features and k bins) = n! / k!*(n-k)!
     * total(6 feat k=1,2,3)=( 6! / 1!*(6-1)!)+( 6! / 2!*(6-2)!)+( 6! / 3!*(6-3)!)=41
     */
    public static ExtractorEnum[] extractorEnums = new ExtractorEnum[] { ExtractorEnum.SPECTRAL_FLUX_EXTRACTOR, ExtractorEnum.LOUDNESS_EXTRACTOR, ExtractorEnum.LPC_RESIDUAL_EXTRACTOR, ExtractorEnum.SIGNAL_ENTROPY_EXTRACTOR, ExtractorEnum.ENERGY_EXTRACTOR, ExtractorEnum.ENVELOPE_EXTRACTOR };

    ExperimentHsqlDao experimentDao = new ExperimentHsqlDao();

    public ExpDBSegmentation() {
        experimentDao.init();
    }

    @Override
    public void generateExpName(ComparisionResult result, List<String> signals) {
        super.generateExpName(result, signals);
        result.setClassifier(getComarisionFacade().getClassifier().name());
        StringBuilder features = new StringBuilder();
        String iseperator = "";
        for (ExtractorEnum enum1 : getExtractors()) {
            features.append(iseperator).append(enum1.getDisplayName());
            iseperator = "-";
        }
        for (String signal : signals) {
            if (StringUtils.hasText(signal)) {
                String fileName = FileUtils.truncateDir(signal).replaceAll(".wav", "");
                if (!StringUtils.hasText(result.getNoiseLevel())) {
                    String[] parts = fileName.split("_");
                    if (parts.length > 0) {
                        result.setName(parts[0]);
                    }
                    if (parts.length > 1) {
                        result.setNoiseType(parts[1]);
                    }
                    if (parts.length > 2) {
                        String level = parts[2];
                        level = level.replace("sn5", "05");
                        level = level.replace("sn", "");
                        result.setNoiseLevel(level);
                    }
                    break;
                }
            }
        }
        experimentDao.save(result, features.toString(), null, result.getName());
    }

    /**
     *
     * @return
     */
    public static List<ComparisionResult> calcResultForAllNoizeus(ExtractorEnum[] expExtractorEnums) {
        List<ComparisionResult> results = new ArrayList<ComparisionResult>();
        List<ExpCriteria> criterias = null;
        ClassifierEnum[] enums = new ClassifierEnum[] { ClassifierEnum.dynamic, ClassifierEnum.offline, ClassifierEnum.rules };
        String[] noizes = new String[] { ExpSegmentationUtil.NOIZEUS_01, ExpSegmentationUtil.NOIZEUS_02, ExpSegmentationUtil.NOIZEUS_04, ExpSegmentationUtil.NOIZEUS_07, ExpSegmentationUtil.NOIZEUS_10, ExpSegmentationUtil.NOIZEUS_21 };
        for (ClassifierEnum classifierEnum : enums) {
            ExpSegmentation expSegmentation = ExpSegmentationFactory.createWavExpSegmentation(new ExpDBSegmentation(), classifierEnum);
            expSegmentation.setExtractors(expExtractorEnums);
            criterias = ExpSegmentationFactory.createNoizeusExpCriterias(noizes);
            results.addAll(expSegmentation.multipleMixtureExperiments(criterias));
        }
        return results;
    }

    public static void main(String[] args) {
        Toolkit.getDefaultToolkit().beep();
        Toolkit.getDefaultToolkit().beep();
        Logger log = Logger.getLogger(ExpDBSegmentation.class);
        Set<Set<ExtractorEnum>> enums = new HashSet<Set<ExtractorEnum>>();
        for (ExtractorEnum extractorEntry : extractorEnums) {
            for (ExtractorEnum extractorEntry2 : extractorEnums) {
                for (ExtractorEnum extractorEntry3 : extractorEnums) {
                    Set<ExtractorEnum> testEnums = new HashSet<ExtractorEnum>();
                    testEnums.add(extractorEntry);
                    testEnums.add(extractorEntry2);
                    testEnums.add(extractorEntry3);
                    if (testEnums.size() > 0 && enums.add(testEnums)) {
                        calcResultForAllNoizeus((ExtractorEnum[]) testEnums.toArray(new ExtractorEnum[testEnums.size()]));
                        log.error("DUN" + testEnums);
                    }
                }
            }
        }
        log.error("DUN");
        Toolkit.getDefaultToolkit().beep();
    }
}
