package ndsapp.service;

import java.util.ArrayList;
import ndsapp.domain.Case;
import ndsapp.repository.CaseDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NearestNeighborRetrieveSimilarCases implements CaseEngine {

    private static final long serialVersionUID = 1L;

    private CaseDao cd;

    private static final int totalNumberOfCasesToCheck = 200;

    private int numberOfSimilarCasesToBeReturned = 2;

    private int cns[][] = new int[totalNumberOfCasesToCheck][2];

    private Case nc;

    private int totalsimilarity = 0;

    protected final Log logger = LogFactory.getLog(getClass());

    public ArrayList<Case> getCases(int caseId) {
        return getCases(cd.getCase(caseId));
    }

    public CaseDao getDbaccess() {
        return cd;
    }

    public void setDbaccess(CaseDao dbaccess) {
        this.cd = dbaccess;
    }

    public ArrayList<Case> getCases(Case nc) {
        this.nc = nc;
        int i = 0;
        totalsimilarity = 0;
        Case rc = cd.getCasesSequentially(0);
        while (rc != null) {
            totalsimilarity = calctotalsimilarity(nc, rc);
            cns[i][0] = rc.getCaseId();
            cns[i++][1] = totalsimilarity;
            rc = cd.getCasesSequentially(rc.getCaseId());
        }
        printSimilarityTable();
        for (int k = cd.getMaxCaseId() - 1; k > 0; k--) {
            int currentMin = cns[0][1];
            int currentMinIndex = 0;
            for (int j = 1; j <= k; j++) {
                if (currentMin > cns[j][1]) {
                    currentMin = cns[j][1];
                    currentMinIndex = j;
                }
            }
            if (currentMinIndex != k) {
                int[] temp = cns[k];
                cns[k] = cns[currentMinIndex];
                cns[currentMinIndex] = temp;
            }
        }
        printSimilarityTable();
        ArrayList<Case> finalList = new ArrayList<Case>();
        for (int l = 0; l < numberOfSimilarCasesToBeReturned; l++) {
            Case temp = cd.getCase(cns[l][0]);
            if (temp != null) {
                temp.setSimilarity(((float) cns[l][1]) / 100F);
                finalList.add(temp);
            }
        }
        return finalList;
    }

    private void printSimilarityTable() {
        System.out.println("---- Case ---- Similarity ----");
        for (int i = 0; i < cd.getMaxCaseId(); i++) {
            System.out.println("      " + cns[i][0] + "     " + cns[i][1]);
        }
    }

    private int simForString(String ncatt, String rcatt) {
        if (ncatt != null && rcatt != null) {
            if (ncatt.equals(rcatt)) return 1;
        }
        return 0;
    }

    private int calctotalsimilarity(Case nc, Case rc) {
        if (nc == null || rc == null) {
            return 0;
        }
        int tnn = 0;
        float totalsimilarity = 0.0F;
        if (nc.getLevelOfConscious() != null || rc.getLevelOfConscious() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getLevelOfConscious(), rc.getLevelOfConscious()));
        }
        if (nc.getBehaviorState() != null || rc.getBehaviorState() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getBehaviorState(), rc.getBehaviorState()));
        }
        if (nc.getEmotionalState() != null || rc.getEmotionalState() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getEmotionalState(), rc.getEmotionalState()));
        }
        if (nc.getDysarthia() != null || rc.getDysarthia() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getDysarthia(), rc.getDysarthia()));
        }
        if (nc.getFluencyAndProsody() != null || rc.getFluencyAndProsody() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getFluencyAndProsody(), rc.getFluencyAndProsody()));
        }
        if (nc.getRepetition() != null || rc.getRepetition() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getRepetition(), rc.getRepetition()));
        }
        if (nc.getNaming() != null || rc.getNaming() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getNaming(), rc.getNaming()));
        }
        if (nc.getReading() != null || rc.getReading() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getReading(), rc.getReading()));
        }
        if (nc.getWriting() != null || rc.getWriting() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getWriting(), rc.getWriting()));
        }
        if (nc.getMemory() != null || rc.getMemory() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getMemory(), rc.getMemory()));
        }
        if (nc.getAstereognosis() != null || rc.getAstereognosis() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getAstereognosis(), rc.getAstereognosis()));
        }
        if (nc.getAgraphaesthesia() != null || rc.getAgraphaesthesia() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getAgraphaesthesia(), rc.getAgraphaesthesia()));
        }
        if (nc.getVisualAnosognosia() != null || rc.getVisualAnosognosia() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getVisualAnosognosia(), rc.getVisualAnosognosia()));
        }
        if (nc.getVisualIllusionsAndHallucinations() != null || rc.getVisualIllusionsAndHallucinations() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getVisualIllusionsAndHallucinations(), rc.getVisualIllusionsAndHallucinations()));
        }
        if (nc.getProsopagnosia() != null || rc.getProsopagnosia() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getProsopagnosia(), rc.getProsopagnosia()));
        }
        if (nc.getColourAgnosia() != null || rc.getColourAgnosia() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getColourAgnosia(), rc.getColourAgnosia()));
        }
        if (nc.getBalintsSyndrome() != null || rc.getBalintsSyndrome() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getBalintsSyndrome(), rc.getBalintsSyndrome()));
        }
        if (nc.getSmell() != null || rc.getSmell() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getSmell(), rc.getSmell()));
        }
        if (nc.getVisualAcuity() != null || rc.getVisualAcuity() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getVisualAcuity(), rc.getVisualAcuity()));
        }
        if (nc.getMovingFingerTest() != null || rc.getMovingFingerTest() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getMovingFingerTest(), rc.getMovingFingerTest()));
        }
        if (nc.getRedPinConfrontation() != null || rc.getRedPinConfrontation() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getRedPinConfrontation(), rc.getRedPinConfrontation()));
        }
        if (nc.getBinocularTesting() != null || rc.getBinocularTesting() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getBinocularTesting(), rc.getBinocularTesting()));
        }
        if (nc.getColourVision() != null || rc.getColourVision() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getColourVision(), rc.getColourVision()));
        }
        if (nc.getFunduscopy() != null || rc.getFunduscopy() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getFunduscopy(), rc.getFunduscopy()));
        }
        if (nc.getOcularMotilityTest() != null || rc.getOcularMotilityTest() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getOcularMotilityTest(), rc.getOcularMotilityTest()));
        }
        if (nc.getDiplopia() != null || rc.getDiplopia() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getDiplopia(), rc.getDiplopia()));
        }
        if (nc.getPupilaryLightReaction() != null || rc.getPupilaryLightReaction() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getPupilaryLightReaction(), rc.getPupilaryLightReaction()));
        }
        if (nc.getOphthalmic() != null || rc.getOphthalmic() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getOphthalmic(), rc.getOphthalmic()));
        }
        if (nc.getMaxillary() != null || rc.getMaxillary() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getMaxillary(), rc.getMaxillary()));
        }
        if (nc.getMandibular() != null || rc.getMandibular() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getMandibular(), rc.getMandibular()));
        }
        if (nc.getCornealReflexes() != null || rc.getCornealReflexes() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getCornealReflexes(), rc.getCornealReflexes()));
        }
        if (nc.getClenchingOfTeeth() != null || rc.getClenchingOfTeeth() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getClenchingOfTeeth(), rc.getClenchingOfTeeth()));
        }
        if (nc.getJewJerk() != null || rc.getJewJerk() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getJewJerk(), rc.getJewJerk()));
        }
        if (nc.getSensory() != null || rc.getSensory() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getSensory(), rc.getSensory()));
        }
        if (nc.getMotor() != null || rc.getMotor() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getMotor(), rc.getMotor()));
        }
        if (nc.getTaste() != null || rc.getTaste() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getTaste(), rc.getTaste()));
        }
        if (nc.getJewJerk() != null || rc.getJewJerk() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getJewJerk(), rc.getJewJerk()));
        }
        if (nc.getJewJerk() != null || rc.getJewJerk() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getJewJerk(), rc.getJewJerk()));
        }
        if (nc.getPharyngealReflex() != null || rc.getPharyngealReflex() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getPharyngealReflex(), rc.getPharyngealReflex()));
        }
        if (nc.getNasalRegurgitation() != null || rc.getNasalRegurgitation() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getNasalRegurgitation(), rc.getNasalRegurgitation()));
        }
        if (nc.getPatalElevation() != null || rc.getPatalElevation() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getPatalElevation(), rc.getPatalElevation()));
        }
        if (nc.getAssessmentOfLarynx() != null || rc.getAssessmentOfLarynx() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getAssessmentOfLarynx(), rc.getAssessmentOfLarynx()));
        }
        if (nc.getTongue() != null || rc.getTongue() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getTongue(), rc.getTongue()));
        }
        if (nc.getWasting() != null || rc.getWasting() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getWasting(), rc.getWasting()));
        }
        if (nc.getFasciculation() != null || rc.getFasciculation() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getFasciculation(), rc.getFasciculation()));
        }
        if (nc.getSkinLesions() != null || rc.getSkinLesions() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getSkinLesions(), rc.getSkinLesions()));
        }
        if (nc.getToneAtWrist() != null || rc.getToneAtWrist() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getToneAtWrist(), rc.getToneAtWrist()));
        }
        if (nc.getToneAtWrist() != null || rc.getToneAtWrist() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getToneAtWrist(), rc.getToneAtWrist()));
        }
        if (nc.getToneAtElbows() != null || rc.getToneAtElbows() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getToneAtElbows(), rc.getToneAtElbows()));
        }
        if (nc.getToneAtKness() != null || rc.getToneAtKness() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getToneAtKness(), rc.getToneAtKness()));
        }
        if (nc.getToneAtAnkles() != null || rc.getToneAtAnkles() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getToneAtAnkles(), rc.getToneAtAnkles()));
        }
        if (nc.getAnkleClonus() != null || rc.getAnkleClonus() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getAnkleClonus(), rc.getAnkleClonus()));
        }
        if (nc.getPower() != null || rc.getPower() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getPower(), rc.getPower()));
        }
        if (nc.getJawJerk() != null || rc.getJawJerk() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getJawJerk(), rc.getJawJerk()));
        }
        if (nc.getBicepsJerk() != null || rc.getBicepsJerk() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getBicepsJerk(), rc.getBicepsJerk()));
        }
        if (nc.getSupinatorJerk() != null || rc.getSupinatorJerk() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getSupinatorJerk(), rc.getSupinatorJerk()));
        }
        if (nc.getTricepsJerk() != null || rc.getTricepsJerk() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getTricepsJerk(), rc.getTricepsJerk()));
        }
        if (nc.getKneeJerk() != null || rc.getKneeJerk() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getKneeJerk(), rc.getKneeJerk()));
        }
        if (nc.getAnkleJerk() != null || rc.getAnkleJerk() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getAnkleJerk(), rc.getAnkleJerk()));
        }
        if (nc.getGornealAndPalatalReflex() != null || rc.getGornealAndPalatalReflex() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getGornealAndPalatalReflex(), rc.getGornealAndPalatalReflex()));
        }
        if (nc.getCremastericReflex() != null || rc.getCremastericReflex() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getCremastericReflex(), rc.getCremastericReflex()));
        }
        if (nc.getPlantarReflex() != null || rc.getPlantarReflex() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getPlantarReflex(), rc.getPlantarReflex()));
        }
        if (nc.getSphinctericReflex() != null || rc.getSphinctericReflex() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getSphinctericReflex(), rc.getSphinctericReflex()));
        }
        if (nc.getSwallowing() != null || rc.getSwallowing() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getSwallowing(), rc.getSwallowing()));
        }
        if (nc.getMicturition() != null || rc.getMicturition() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getMicturition(), rc.getMicturition()));
        }
        if (nc.getDefecation() != null || rc.getDefecation() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getDefecation(), rc.getDefecation()));
        }
        if (nc.getFingerNoseTest() != null || rc.getFingerNoseTest() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getFingerNoseTest(), rc.getFingerNoseTest()));
        }
        if (nc.getDysdiadochokinesis() != null || rc.getDysdiadochokinesis() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getDysdiadochokinesis(), rc.getDysdiadochokinesis()));
        }
        if (nc.getFineMovements() != null || rc.getFineMovements() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getFineMovements(), rc.getFineMovements()));
        }
        if (nc.getHeelShinTest() != null || rc.getHeelShinTest() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getHeelShinTest(), rc.getHeelShinTest()));
        }
        if (nc.getPinprick() != null || rc.getPinprick() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getPinprick(), rc.getPinprick()));
        }
        if (nc.getTemperature() != null || rc.getTemperature() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getTemperature(), rc.getTemperature()));
        }
        if (nc.getLightTouch() != null || rc.getLightTouch() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getLightTouch(), rc.getLightTouch()));
        }
        if (nc.getVibrationSense() != null || rc.getVibrationSense() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getVibrationSense(), rc.getVibrationSense()));
        }
        if (nc.getJointPositionSense() != null || rc.getJointPositionSense() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getJointPositionSense(), rc.getJointPositionSense()));
        }
        if (nc.getTwoPointDiscrimination() != null || rc.getTwoPointDiscrimination() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getTwoPointDiscrimination(), rc.getTwoPointDiscrimination()));
        }
        if (nc.getWalkingPattern() != null || rc.getWalkingPattern() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getWalkingPattern(), rc.getWalkingPattern()));
        }
        if (nc.getStandingWFTAndEyesClosed() != null || rc.getStandingWFTAndEyesClosed() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getStandingWFTAndEyesClosed(), rc.getStandingWFTAndEyesClosed()));
        }
        if (nc.getWalkingHeelToToe() != null || rc.getWalkingHeelToToe() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getWalkingHeelToToe(), rc.getWalkingHeelToToe()));
        }
        if (nc.getJumpingAndHopping() != null || rc.getJumpingAndHopping() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getJumpingAndHopping(), rc.getJumpingAndHopping()));
        }
        if (nc.getBloodPressure() != null || rc.getBloodPressure() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getBloodPressure(), rc.getBloodPressure()));
        }
        if (nc.getPulse() != null || rc.getPulse() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getPulse(), rc.getPulse()));
        }
        if (nc.getDeepBreathsTest() != null || rc.getDeepBreathsTest() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getDeepBreathsTest(), rc.getDeepBreathsTest()));
        }
        if (nc.getHandgripTest() != null || rc.getHandgripTest() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getHandgripTest(), rc.getHandgripTest()));
        }
        if (nc.getValsalvaTest() != null || rc.getValsalvaTest() != null) {
            tnn++;
            totalsimilarity = totalsimilarity + (simForString(nc.getValsalvaTest(), rc.getValsalvaTest()));
        }
        return new Integer((int) (10000 * totalsimilarity / (float) tnn));
    }

    public CaseDao getCd() {
        return cd;
    }

    public void setCd(CaseDao cd) {
        this.cd = cd;
    }

    public int getNumberOfSimilarCasesToBeReturned() {
        return numberOfSimilarCasesToBeReturned;
    }

    public void setNumberOfSimilarCasesToBeReturned(int numberOfSimilarCasesToBeReturned) {
        this.numberOfSimilarCasesToBeReturned = numberOfSimilarCasesToBeReturned;
    }

    public Case getNc() {
        return nc;
    }

    public void setNc(Case nc) {
        this.nc = nc;
    }
}
