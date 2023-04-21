package com.gever.util.sysinfo;

/**
 * <p>Title: ����ʱ������Ϣ</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: gever</p>
 * @author Hu.Walker
 * @version 1.0
 */
public class RunTime {

    public RunTime() {
    }

    /**
     * ��ȡ�ڴ�����
     * @return
     */
    public long getTotalMemory() {
        long ret = 0;
        Runtime rt = Runtime.getRuntime();
        ret = rt.totalMemory();
        return ret;
    }

    /**
     * ��ȡ�����ڴ���
     * @return
     */
    public long getFreeMemory() {
        long ret = 0;
        Runtime rt = Runtime.getRuntime();
        rt.gc();
        ret = rt.freeMemory();
        return ret;
    }

    /**
     * ��ȡ����ϵͳ���
     * @return
     */
    public String getOsName() {
        String ret = "";
        ret = System.getProperty("os.name");
        return ret;
    }

    /**
     * ��ȡ����ϵͳ�汾
     * @return
     */
    public String getOsVersion() {
        String ret = "";
        ret = System.getProperty("os.version");
        return ret;
    }
}
