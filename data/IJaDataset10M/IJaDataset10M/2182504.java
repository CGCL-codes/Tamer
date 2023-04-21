package ces.common.workflow.entity;

import java.sql.Timestamp;

/**
 * <b>�� �� ��: </b>Task.java <br>
 * <b>��������: </b> ������� <br>
 * id: ��� <br>
 * businessId ҵ��������� <br>
 * prosInstId ����ʵ���� <br>
 * prosDefId ���������� <br>
 * prosDefName ����������� <br>
 * functionId: ���ܱ�� <br>
 * functionId: ������� <br>
 * jsp ҵ����? <br>
 * tarskName: ������� <br>
 * sendTime: �������ʱ�� <br>
 * recieveTime: �������ʱ�� <br>
 * finishTime: �������ʱ�� <br>
 * ownerId: ���������� <br>
 * status: ����״̬(0:δ�򿪣�1���Ѵ򿪣�2�������) <br>
 * prosInstStartTime: ���̿�ʼʱ�� <br>
 * actionDesc: ��������/��� <br>
 * summary ����ʵ����ϢժҪ <br>
 * 
 * <b>��Ȩ����: </b>�Ϻ�������Ϣ��չ���޹�˾(CES)2005
 * 
 * @author ������
 * @version 1.0
 */
public class Task {

    /**
	 * @param userId
	 * @param prosInstId
	 * @param function
	 */
    public Task(String userId, long prosInstId, int functionId) {
        this.ownerId = userId;
        this.prosInstId = prosInstId;
        this.functionId = functionId;
        this.sendTime = new Timestamp(System.currentTimeMillis());
        this.status = NOT_PENED;
    }

    /**
	 *  
	 */
    public Task() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public long getProsInstId() {
        return prosInstId;
    }

    public void setProsInstId(long prosInstId) {
        this.prosInstId = prosInstId;
    }

    public int getProsDefId() {
        return prosDefId;
    }

    public void setProsDefId(int prosDefId) {
        this.prosDefId = prosDefId;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    public String getJsp() {
        return jsp;
    }

    public void setJsp(String jsp) {
        this.jsp = jsp;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public Timestamp getRecieveTime() {
        return recieveTime;
    }

    public void setRecieveTime(Timestamp recieveTime) {
        this.recieveTime = recieveTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
	 * @return Returns the prosDefName.
	 */
    public String getProsDefName() {
        return prosDefName;
    }

    /**
	 * @param prosDefName
	 *            The prosDefName to set.
	 */
    public void setProsDefName(String prosDefName) {
        this.prosDefName = prosDefName;
    }

    /**
	 * @return Returns the prosInstStartTime.
	 */
    public Timestamp getProsInstStartTime() {
        return prosInstStartTime;
    }

    /**
	 * @param prosInstStartTime
	 *            The prosInstStartTime to set.
	 */
    public void setProsInstStartTime(Timestamp prosInstStartTime) {
        this.prosInstStartTime = prosInstStartTime;
    }

    /**
	 * @return Returns the functionName.
	 */
    public String getFunctionName() {
        return functionName;
    }

    /**
	 * @param functionName
	 *            The functionName to set.
	 */
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    /**
	 * @return Returns the summary.
	 */
    public String getSummary() {
        return summary;
    }

    /**
	 * @param summary
	 *            The summary to set.
	 */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return Returns the fromTaskId.
     */
    public long getFromTaskId() {
        return fromTaskId;
    }

    /**
     * @param fromTaskId The fromTaskId to set.
     */
    public void setFromTaskId(long fromTaskId) {
        this.fromTaskId = fromTaskId;
    }

    private long id;

    private String businessId;

    private long prosInstId;

    private int prosDefId;

    private String prosDefName;

    private int functionId;

    private String jsp;

    private Timestamp sendTime;

    private Timestamp recieveTime;

    private Timestamp finishTime;

    private String ownerId;

    private int status;

    private Timestamp prosInstStartTime;

    private String summary;

    private String functionName;

    private long fromTaskId;

    /**
	 * Comment for <code>SENDED</code> δ��
	 */
    public static int NOT_PENED = 0;

    /**
	 * Comment for <code>SENDED</code> �Ѵ�
	 */
    public static int PENED = 1;

    /**
	 * Comment for <code>SENDED</code> �����
	 */
    public static int SENDED = 2;

    private static String[] statusDes = { "δ��", "�Ѵ�", "�����" };

    public static String getStatusDes(int status) {
        if (validateStatus(status)) {
            return statusDes[status];
        } else {
            throw new IllegalArgumentException("״ֵ̬����");
        }
    }

    public static boolean validateStatus(int status) {
        return status > -1 && status < 3;
    }
}
