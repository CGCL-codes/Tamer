package ces.platform.system.facade;

import java.util.Vector;
import ces.coral.dbo.*;
import ces.coral.log.*;

/**
 * <p>����:
 * <font class=titlefont>
 * ����̬�û���ӿڡ���
 * </font>
 * <p>����:
 * <font class=descriptionfont>
 * <br>����һ����̬�û���ӿںͷ���
 * </font>
 * <p>�汾��:
 * <font class=versionfont>
 * Copyright (c) 2.50.2003.0925
 * </font>
 * <p>��˾:
 * <font class=companyfont>
 * �Ϻ�������Ϣ��չ���޹�˾
 * </font>
 * @author ����
 * @version 2.50.2003.0925
 */
public interface UserGroupInterface {

    /**
     * ��ȡ��̬�û��������е��û�
     * @return    �û����󼯺�
     */
    public Vector getUsers() throws Exception;
}
