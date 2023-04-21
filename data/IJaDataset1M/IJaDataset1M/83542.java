package ces.coffice.common.mesEx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import ces.coffice.calplan.CalPlan;
import ces.coffice.common.OaException;
import ces.coral.log.Logger;
import ces.platform.system.common.IdGenerator;
import ces.platform.system.common.XmlConstant;
import ces.platform.system.common.XmlInfo;

/**
*
* <p>Title: ��Ϣ����</p>
* <p>Description: ��Ϣ���?��ʱ��������е���Ϣ</p>
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: CES</p>
* @author ����԰
* @version 3.0
*/
public class MessCycle {

    static Logger logger = new Logger(MessCycle.class);

    private static MessCycle instance = null;

    private HashMap waitMess = null;

    private Date date = null;

    private MyTime myTime = null;

    private int messageId;

    /**
     * �õ�ʵ��
     */
    public static synchronized MessCycle getInstance() {
        logger.debug("getInstance begin!");
        if (instance == null) {
            instance = new MessCycle();
            instance.run();
        }
        logger.debug("getInstance end!");
        return instance;
    }

    /**
     * ���캯��
     */
    public MessCycle() {
        waitMess = new HashMap();
        date = new Date();
        myTime = new MyTime();
        loadMessageData();
    }

    /**
     * ������Ϣ����Ϣ������
     * @param baseMess	���ӵ���Ϣ����
     * @return messageId	��ϢID
     * @throws OaException
     */
    public int addMessage(BaseMess baseMess) throws OaException {
        messageId = -1;
        try {
            messageId = (int) (IdGenerator.getInstance().getId(IdGenerator.GEN_ID_COFFICE_MESSAGE));
            baseMess.setMessageId(messageId);
            synchronized (waitMess) {
                logger.debug("������Ϣ:=" + baseMess.getMessageId() + " " + new Date(baseMess.getRunTime()));
                waitMess.put("" + messageId, baseMess);
            }
        } catch (Exception e) {
            logger.error("������Ϣ����!" + e.getMessage());
            throw new OaException("������Ϣ����!" + e.getMessage());
        }
        return messageId;
    }

    /**
     * �޸���Ϣ�����е���Ϣ
     * @param baseMess	�޸ĵ���Ϣ����
     * @return boolean	�ɹ���
     * @throws OaException
     */
    public boolean editMessage(BaseMess baseMess) throws OaException {
        boolean bReturn = false;
        try {
            messageId = baseMess.getMessageId();
            if (messageId == -1) {
                logger.error("��ϢID=-1!");
                throw new OaException("û�еõ��޸ĵ���ϢID!");
            }
            synchronized (waitMess) {
                logger.debug("�޸���Ϣ:=" + baseMess.getMessageId() + " " + baseMess.getRunTime());
                waitMess.put("" + messageId, baseMess);
            }
            bReturn = true;
        } catch (Exception e) {
            logger.error("�޸���Ϣ�����е���Ϣ����!" + e.getMessage());
            throw new OaException("�޸���Ϣ�����е���Ϣ����!" + e.getMessage());
        }
        return bReturn;
    }

    /**
     * ɾ����Ϣ�����е���Ϣ
     * @param baseMess	ɾ�����Ϣ����
     * @return boolean	�ɹ���
     * @throws OaException
     */
    public boolean removeMessage(BaseMess baseMess) throws OaException {
        boolean bReturn = false;
        try {
            messageId = baseMess.getMessageId();
            synchronized (waitMess) {
                logger.debug("ɾ����Ϣ:=" + baseMess.getMessageId() + " " + baseMess.getRunTime());
                waitMess.remove("" + messageId);
            }
            bReturn = true;
        } catch (Exception e) {
            logger.error("ɾ����Ϣ�����е���Ϣ����!" + e.getMessage());
            throw new OaException("ɾ����Ϣ�����е���Ϣ����!" + e.getMessage());
        }
        return bReturn;
    }

    /**
     * ���ж�ʱɨ������
     */
    public void run() {
        logger.debug("run begin!");
        long lSecond = 100;
        try {
            String strSecond = XmlInfo.getInstance().getConfigValue(XmlConstant.OA_CALPLAN_MESSAGE_TIME);
            lSecond = Long.parseLong(strSecond);
        } catch (Exception e) {
        }
        logger.debug("ʱ����! lSecond:=" + lSecond);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(myTime, date, 1000 * lSecond);
    }

    class MyTime extends TimerTask {

        public void run() {
            logger.debug("��Ϣ���п�ʼѭ��! currTime:=" + (new Date()));
            try {
                synchronized (waitMess) {
                    Iterator it = waitMess.keySet().iterator();
                    BaseMess baseMess;
                    String sMessId;
                    long runTime;
                    long curTime = new Date().getTime();
                    ArrayList al = new ArrayList();
                    while (it.hasNext()) {
                        sMessId = it.next().toString();
                        baseMess = (BaseMess) waitMess.get(sMessId);
                        logger.debug("��Ϣ:=" + baseMess.getMessageId() + " " + (new Date(baseMess.getRunTime())));
                        runTime = baseMess.getRunTime();
                        if (runTime <= curTime) {
                            new Thread(baseMess).start();
                            al.add(sMessId);
                        }
                    }
                    for (int i = 0; i < al.size(); i++) waitMess.remove(al.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadMessageData() {
        logger.debug("run loadMessageData!");
        try {
            logger.debug("run loadMessageData!");
            BaseMess[] mes = CalPlan.loadMessages();
            for (int i = 0; i < mes.length; i++) {
                addMessage(mes[i]);
            }
            logger.debug("end loadMessageData! length:= " + mes.length);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.debug("end loadMessageData!");
    }
}
