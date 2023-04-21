package com.xy.sframe.frame.gc;

import javax.servlet.http.*;

/**
 * ����ͬsession��ص���ݡ���Ҫ��com.resoft.reframe.frame.config����ע��ʵ�ָýӿڵ���,
 * �������������¶��ַ�ʽ��ȥ����
 * <ul>
 *  <il>1)ǰ���û���������logiout���¼������</il>
 *  <il>2)session�¼�destory����ʱ�����ȥ���</il>
 * </ul>
 * ע�⣬gcֻ�ǽ���������ù�����ϵ����û����������jvm��gc������޹��������á�
 * Created on 2005-12-15
 * @author chengang
 * @version 1.0
 */
public interface GarbageClear {

    public void clearAllGarbage(HttpSession HttpSession);
}
