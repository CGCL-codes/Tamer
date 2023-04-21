package com.xys.ecg.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import android.media.MediaPlayer;
import com.xys.ecg.activity.ECGApplication;
import com.xys.ecg.activity.R;
import com.xys.ecg.activity.ECG_Android.MainEventHandler;
import com.xys.ecg.bean.EcgDataEntity;
import com.xys.ecg.dataproc.EcgDataAnalysis;
import com.xys.ecg.dataproc.EcgDataSave;
import com.xys.ecg.file.EcgDataFileOperate;
import com.xys.ecg.log.Logger;
import com.xys.ecg.utils.TypeConversion;

/**
 * �ĵ���ݹ���
 * @author Administrator
 *
 */
public class EcgBusiness {

    public static Logger logger = Logger.getLogger(EcgBusiness.class);

    private static EcgBusiness instance = new EcgBusiness();

    private LinkedList<EcgDataEntity> ecgBusinessDataQueue = new LinkedList<EcgDataEntity>();

    private static UploadState soonUploadState = UploadState.NORMAL;

    private EcgDataAnalysis ecgDataAnalysis = EcgDataAnalysis.getInstance();

    private EcgDataSave ecgDataSave = EcgDataSave.getInstance();

    public Thread ecgBusinessTask = null;

    public boolean stopFlag = false;

    public static int sleepTime = 960;

    public static enum UploadState {

        NORMAL, UPLOADEVENT, UPLOADING, EVENT_IN_ENENT
    }

    private EcgBusiness() {
    }

    public static EcgBusiness getInstance() {
        return instance;
    }

    public boolean insertEcgData(EcgDataEntity ecgDataEntity) {
        try {
            if (1 == ecgDataEntity.getIsEventData()) {
                if (soonUploadState == UploadState.NORMAL) {
                    soonUploadState = UploadState.UPLOADEVENT;
                } else if (soonUploadState == UploadState.UPLOADING) {
                    soonUploadState = UploadState.EVENT_IN_ENENT;
                }
                MediaPlayer play = MediaPlayer.create(ECGApplication.getInstance(), R.raw.exceptionevent);
                play.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        synchronized (ecgBusinessDataQueue) {
            logger.info("inserEcgData ");
            return ecgBusinessDataQueue.add(ecgDataEntity);
        }
    }

    /**
	 * ��ȡ������еĳ���
	 * @return
	 */
    public int getEcgBusinessDataQueueLength() {
        if (ecgBusinessDataQueue != null) {
            return ecgBusinessDataQueue.size();
        } else {
            return 0;
        }
    }

    public EcgDataEntity getEcgBusinessDataQueue() {
        synchronized (ecgBusinessDataQueue) {
            return ecgBusinessDataQueue.poll();
        }
    }

    private void clearEcgBusinessDataQueue() {
        synchronized (ecgBusinessDataQueue) {
            ecgBusinessDataQueue.clear();
        }
    }

    public UploadState getSoonUploadState() {
        return this.soonUploadState;
    }

    public void setSoonUploadState(UploadState soonUploadState) {
        this.soonUploadState = soonUploadState;
    }

    /**
	 * ��ʼ�ĵ�ҵ���߳�
	 * @param mainEventHandler ���߳���Ϣ�������
	 */
    public synchronized void startTask(MainEventHandler mainEventHandler) {
        stopFlag = false;
        if (ecgBusinessTask != null) {
            ecgBusinessTask.notify();
        } else {
            ecgBusinessTask = new Thread(new EcgBusinessTask(mainEventHandler));
            ecgBusinessTask.start();
        }
    }

    /**
	 * ֹͣ�ĵ�ҵ���߳�
	 */
    public synchronized boolean stopTask(MainEventHandler mainEventHandler) {
        stopFlag = true;
        if (ecgBusinessTask != null) {
            ecgDataSave.beginEcgDataSave(new LinkedList(ecgBusinessDataQueue), mainEventHandler);
            clearEcgBusinessDataQueue();
            synchronized (ecgBusinessTask) {
                ecgBusinessTask.notify();
            }
            ecgBusinessTask = null;
        }
        ecgDataSave.stopTask();
        ecgDataAnalysis.stopTask();
        return true;
    }

    /**
	 * ֹͣ�ɼ����
	 * @return
	 */
    public synchronized boolean suspendTask() {
        if (ecgBusinessTask != null) {
            try {
                synchronized (ecgBusinessTask) {
                    ecgBusinessTask.wait();
                }
            } catch (InterruptedException e) {
            }
        }
        return true;
    }

    /**
	 * ��ʱ����������ݸ�����̺߳ͱ����̣߳���֪ͨ������Ӧ�Ĳ���
	 * @author Administrator
	 *
	 */
    private class EcgBusinessTask implements Runnable {

        private MainEventHandler mainEventHandler = null;

        public EcgBusinessTask(MainEventHandler mainEventHandler) {
            this.mainEventHandler = mainEventHandler;
        }

        public void run() {
            while (mainEventHandler != null && !stopFlag) {
                logger.info("ecgBusinessDataQueue:" + ecgBusinessDataQueue.size());
                if (ecgBusinessDataQueue.size() > 180) {
                    EcgBusiness.sleepTime = 800;
                }
                if (ecgBusinessDataQueue.size() <= 120 && EcgBusiness.sleepTime == 800) {
                    EcgBusiness.sleepTime = 960;
                }
                if (ecgBusinessDataQueue.size() > 0) {
                    EcgDataEntity ecgDataEntity = getEcgBusinessDataQueue();
                    ecgDataSave.beginEcgDataSave(ecgDataEntity, mainEventHandler);
                    if (!ecgDataEntity.getIsHisDate()) {
                        ecgDataAnalysis.beginAnalysis(ecgDataEntity, mainEventHandler);
                    }
                }
                try {
                    Thread.sleep(EcgBusiness.sleepTime);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
	 * ����ģ�����ݰ�
	 * @param i
	 * @return
	 */
    public static EcgDataEntity getEcgDataEntity(int i) {
        byte[] packetHead = new byte[10];
        packetHead[0] = (short) 0x06;
        System.arraycopy(TypeConversion.shortToBytes((short) 10), 0, packetHead, 1, 2);
        System.arraycopy(TypeConversion.intToBytes(i), 0, packetHead, 3, 4);
        byte status1 = 56;
        status1 = (byte) (status1 | (30 << 4));
        status1 = (byte) (status1 | (1 << 7));
        packetHead[7] = status1;
        byte status2 = 1;
        if (i % 70 == 0) {
            status2 = 0;
        }
        if (i % 50 == 0) {
            status2 = (byte) (status2 | (1 << 1));
        } else {
            status2 = (byte) (status2 | (0 << 1));
        }
        if (i % 100 == 0) {
            status2 = (byte) (status2 | (1 << 2));
        } else {
            status2 = (byte) (status2 | (0 << 2));
        }
        packetHead[8] = status2;
        byte[] ecgPackage = new byte[296];
        System.arraycopy(TypeConversion.shortToBytes((short) 300), 0, ecgPackage, 0, 2);
        ecgPackage[2] = ((byte) 1) << 4;
        ecgPackage[3] = (byte) 0;
        ecgPackage[4] = (byte) 0;
        System.arraycopy(TypeConversion.shortToBytes((short) 296), 0, ecgPackage, 5, 2);
        ecgPackage[7] = (byte) 0;
        System.arraycopy(TypeConversion.longToBytes(2960322321341321L), 0, ecgPackage, 8, 8);
        System.arraycopy(TypeConversion.longToBytes(23425234254324523L), 0, ecgPackage, 16, 8);
        byte[] accPackage = new byte[222];
        System.arraycopy(TypeConversion.shortToBytes((short) 75), 0, accPackage, 0, 2);
        accPackage[2] = (byte) 7;
        System.arraycopy(TypeConversion.shortToBytes((short) 222), 0, accPackage, 3, 2);
        accPackage[5] = (byte) 0;
        System.arraycopy(TypeConversion.longToBytes(46345343535232323L), 0, accPackage, 6, 8);
        System.arraycopy(TypeConversion.longToBytes(46345343535232323L), 0, accPackage, 78, 8);
        System.arraycopy(TypeConversion.longToBytes(46345343535232323L), 0, accPackage, 150, 8);
        byte[] ecgDataEntitybt = new byte[528];
        System.arraycopy(packetHead, 0, ecgDataEntitybt, 0, 10);
        System.arraycopy(ecgPackage, 0, ecgDataEntitybt, 10, 296);
        System.arraycopy(accPackage, 0, ecgDataEntitybt, 306, 222);
        boolean isfirst = false;
        boolean isHisData = false;
        if (i % 100 == 0) {
            isfirst = true;
        }
        if (i % 2 != 0) {
            isHisData = true;
        }
        byte[] nowTime = new byte[8];
        System.arraycopy(TypeConversion.longToBytes(new Date().getTime() / 1000), 0, nowTime, 0, 8);
        EcgDataEntity ecgDataEntity = new EcgDataEntity(ecgDataEntitybt, isfirst, nowTime, isHisData);
        return ecgDataEntity;
    }
}
