package com.google.code.burija.normal.dto;

import java.sql.Timestamp;

/**
 * 宴会一覧Dto。
 * @author imaitakafumi
 *
 */
public class PartyInfoDto {

    /** 宴会ID */
    private long partyId;

    /** 宴会名 */
    private String partyName;

    /** 開催予定日/開催時間 */
    private Timestamp partyDate;

    /** 幹事のID */
    private long managerId;

    /** 幹事 */
    private String managerName;

    /** 参加状況（現在人数） */
    private int nowMemberCount;

    /** 参加状況（上限人数） */
    private int memberLimit;

    /** 編集可否 */
    private int isModify;

    /** 中止可否 */
    private int isStop;

    /** 参加表明可否 */
    private int isEntry;

    /** 中止済・未済 */
    private int isStoped;

    /** 参加表明日時 */
    private Timestamp entryDate;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("paryId:").append(this.partyId).append(" partyName:").append(this.partyName).append(" partyDate:").append(this.partyDate).append(" managerId:").append(this.managerId).append(" managerName:").append(this.managerName).append(" nowMemberCount:").append(this.nowMemberCount).append(" memberLimit:").append(this.memberLimit).append(" isModify:").append(this.isModify).append(" isStop:").append(this.isStop).append(" isEntry:").append(this.isEntry).append(" isStoped:").append(this.isStoped).append(" entryDate:").append(this.entryDate);
        return sb.toString();
    }

    public long getPartyId() {
        return partyId;
    }

    public void setPartyId(long partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public Timestamp getPartyDate() {
        return partyDate;
    }

    public void setPartyDate(Timestamp partyDate) {
        this.partyDate = partyDate;
    }

    public long getManagerId() {
        return managerId;
    }

    public void setManagerId(long managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public int getNowMemberCount() {
        return nowMemberCount;
    }

    public void setNowMemberCount(int nowMemberCount) {
        this.nowMemberCount = nowMemberCount;
    }

    public int getMemberLimit() {
        return memberLimit;
    }

    public void setMemberLimit(int memberLimit) {
        this.memberLimit = memberLimit;
    }

    public int getIsModify() {
        return isModify;
    }

    public void setIsModify(int isModify) {
        this.isModify = isModify;
    }

    public int getIsStop() {
        return isStop;
    }

    public void setIsStop(int isStop) {
        this.isStop = isStop;
    }

    public int getIsEntry() {
        return isEntry;
    }

    public void setIsEntry(int isEntry) {
        this.isEntry = isEntry;
    }

    public int getIsStoped() {
        return isStoped;
    }

    public void setIsStoped(int isStoped) {
        this.isStoped = isStoped;
    }

    public Timestamp getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Timestamp entryDate) {
        this.entryDate = entryDate;
    }
}
