package utils;

import jade.content.Predicate;

public class MessageInfo implements Predicate {

    protected int mainSenderId;

    protected int mainReceiverId;

    protected int senderPosX;

    protected int senderPosY;

    protected float senderRange;

    protected int deadline;

    protected int taskCompletion;

    protected int acomplishment;

    protected Fact f = null;

    protected LocateTask lt = null;

    protected CheckLocationTask clt = null;

    public MessageInfo() {
        super();
    }

    public MessageInfo(int mainSenderId, int mainReceiverId, int senderPosX, int senderPosY, float senderRange) {
        this.mainSenderId = mainSenderId;
        this.mainReceiverId = mainReceiverId;
        this.senderPosX = senderPosX;
        this.senderPosY = senderPosY;
        this.senderRange = senderRange;
    }

    public int getMainSenderId() {
        return mainSenderId;
    }

    public void setMainSenderId(int mainSenderId) {
        this.mainSenderId = mainSenderId;
    }

    public int getMainReceiverId() {
        return mainReceiverId;
    }

    public void setMainReceiverId(int mainReceiverId) {
        this.mainReceiverId = mainReceiverId;
    }

    public int getSenderPosX() {
        return senderPosX;
    }

    public void setSenderPosX(int senderPosX) {
        this.senderPosX = senderPosX;
    }

    public int getSenderPosY() {
        return senderPosY;
    }

    public void setSenderPosY(int senderPosY) {
        this.senderPosY = senderPosY;
    }

    public float getSenderRange() {
        return senderRange;
    }

    public void setSenderRange(float senderRange) {
        this.senderRange = senderRange;
    }

    public Fact getF() {
        return f;
    }

    public void setF(Fact f) {
        this.f = f;
    }

    public LocateTask getLt() {
        return lt;
    }

    public void setLt(LocateTask lt) {
        this.lt = lt;
    }

    public CheckLocationTask getClt() {
        return clt;
    }

    public void setClt(CheckLocationTask clt) {
        this.clt = clt;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public int getTaskCompletion() {
        return taskCompletion;
    }

    public void setTaskCompletion(int taskCompletion) {
        this.taskCompletion = taskCompletion;
    }

    public int getAcomplishment() {
        return acomplishment;
    }

    public void setAcomplishment(int acomplishment) {
        this.acomplishment = acomplishment;
    }
}
