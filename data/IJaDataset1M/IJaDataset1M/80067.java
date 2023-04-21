package ru.newton.pokertrainer.businesslogic.databaseservices.old.handhistory.lists.listrow;

import ru.newton.pokertrainer.businesslogic.databaseservices.old.constant.DatabaseServiceConstant;
import ru.newton.pokertrainer.businesslogic.databaseservices.old.handhistory.constant.HHConstants;
import ru.newton.pokertrainer.businesslogic.databaseservices.old.handhistory.constant.SeatConstants;
import ru.newton.pokertrainer.businesslogic.databaseservices.old.handhistory.constant.SeatType;

/**
 * @author newton
 *         Date: Feb 20, 2011
 */
public class SeatRow {

    private int id = DatabaseServiceConstant.ERROR_ID;

    private int handId = DatabaseServiceConstant.ERROR_ID;

    private int seatStage = SeatConstants.STAGE_NONE_FLAG;

    private int seatIndex = SeatConstants.ERROR_INDEX;

    private int nickId = HHConstants.NICK_ERROR_ID;

    private int seatType = SeatType.SEAT_NO_TYPE;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNickId() {
        return nickId;
    }

    public void setNickId(int nickId) {
        this.nickId = nickId;
    }

    public int getSeatType() {
        return seatType;
    }

    public void setSeatType(int seatType) {
        this.seatType = seatType;
    }

    public int getSeatStage() {
        return seatStage;
    }

    public void setSeatStage(int seatStage) {
        this.seatStage = seatStage;
    }

    public int getHandId() {
        return handId;
    }

    public void setHandId(int handId) {
        this.handId = handId;
    }

    public int getSeatIndex() {
        return seatIndex;
    }

    public void setSeatIndex(int seatIndex) {
        this.seatIndex = seatIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeatRow)) return false;
        SeatRow seatRow = (SeatRow) o;
        if (id != seatRow.id) return false;
        if (handId != seatRow.handId) return false;
        if (nickId != seatRow.nickId) return false;
        if (seatIndex != seatRow.seatIndex) return false;
        if (seatStage != seatRow.seatStage) return false;
        if (seatType != seatRow.seatType) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + handId;
        result = 31 * result + nickId;
        result = 31 * result + seatStage;
        result = 31 * result + seatIndex;
        result = 31 * result + seatType;
        return result;
    }
}
