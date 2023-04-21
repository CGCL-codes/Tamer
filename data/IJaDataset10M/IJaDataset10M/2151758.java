package ru.newton.pokertrainer.businesslogic.statistics.parameters;

import ru.newton.pokertrainer.businesslogic.databaseservices.dictionary.constant.StepType.StepTypeId;
import ru.newton.pokertrainer.businesslogic.databaseservices.handhistory.holders.HandHistoryStepHolder;
import ru.newton.pokertrainer.businesslogic.databaseservices.handhistory.pool.HHPool;
import ru.newton.pokertrainer.businesslogic.databaseservices.handhistory.pool.HandPool;
import ru.newton.pokertrainer.businesslogic.databaseservices.handhistory.pool.HeaderHHPool;
import ru.newton.pokertrainer.businesslogic.importmodule.ImportConstants;
import java.util.List;

/**
 * @author newton
 *         Date: Feb 27, 2011
 */
public class ParameterWTSD_OLD {

    private HHPool hhPool = null;

    private List<Integer> selectedHandsList = null;

    public ParameterWTSD_OLD(final HHPool hhPool, final List<Integer> selectedHandsList) {
        this.hhPool = hhPool;
        this.selectedHandsList = selectedHandsList;
        int sizeHandList = hhPool.getSizeHandList();
    }

    public double getWTSD() {
        double valueWTSD = 0;
        int wTSDCount = 0;
        for (int i = 0; i < hhPool.getSizeHandList(); i++) {
            HandPool hand = hhPool.getHand(i);
            if (WTSDOnHand(hand)) {
                wTSDCount++;
            }
        }
        ParameterHandsCount parameterHandsCount = new ParameterHandsCount(hhPool, selectedHandsList);
        int handsCount = parameterHandsCount.getHandsCount();
        valueWTSD = (double) wTSDCount / (double) handsCount;
        return valueWTSD;
    }

    protected boolean WTSDOnHand(final HandPool hand) {
        HeaderHHPool headerHHPool = hhPool.getHeaderHHPool();
        int userId = headerHHPool.getIdMainPartner();
        boolean flagFlop = false;
        boolean flagShowDown = false;
        int flopIndex = 0;
        if (flopIndex >= 0) {
            int showDownIndex = getShowDownIndex(hand);
            if (showDownIndex > flopIndex) {
                for (int i = flopIndex; i < showDownIndex; i++) {
                    HandHistoryStepHolder handStep = hand.getHandHistoryStep(i);
                    if (userId == handStep.getPartnerId()) {
                        int typeStep = handStep.getPartnerPlayTypeId();
                        if (typeStep == StepTypeId.STEP_ID_FOLDS) {
                            flagFlop = true;
                        }
                    }
                }
                if (!flagFlop) {
                    for (int i = showDownIndex; i < hand.getListHandHistoryStepSize(); i++) {
                        HandHistoryStepHolder handStep = hand.getHandHistoryStep(i);
                        if (userId == handStep.getPartnerId()) {
                            flagShowDown = true;
                        }
                    }
                }
            }
        }
        return flagShowDown;
    }

    private int getShowDownIndex(final HandPool hand) {
        int resultIndex = ImportConstants.ERROR_ID;
        for (int i = 0; i < hand.getListHandHistoryStepSize(); i++) {
            HandHistoryStepHolder handStep = hand.getHandHistoryStep(i);
            if (handStep.getPartnerPlayTypeId() == StepTypeId.STEP_ID_SHOW_DOWN) {
                resultIndex = i;
            }
        }
        return resultIndex;
    }

    private int getSummeryIndex(final HandPool hand) {
        int resultIndex = ImportConstants.ERROR_ID;
        for (int i = 0; i < hand.getListHandHistoryStepSize(); i++) {
            HandHistoryStepHolder handStep = hand.getHandHistoryStep(i);
            if (handStep.getPartnerPlayTypeId() == StepTypeId.STEP_ID_SUMMARY) {
                resultIndex = i;
            }
        }
        return resultIndex;
    }

    private int getFlopIndex(final HandPool hand) {
        int resultIndex = ImportConstants.ERROR_ID;
        for (int i = 0; i < hand.getListHandHistoryStepSize(); i++) {
            HandHistoryStepHolder handStep = hand.getHandHistoryStep(i);
            if (handStep.getPartnerPlayTypeId() == StepTypeId.STEP_ID_FLOP) {
                resultIndex = i;
            }
        }
        return resultIndex;
    }
}
