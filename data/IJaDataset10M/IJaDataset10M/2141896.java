package ces.coffice.meetmanage;

import ces.coffice.common.OaException;

/**
*
* <p>Title: �������ģ���е��쳣��</p>
*
* <p>Description: �������ģ���еĳ����쳣�� </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: CES</p>
*
* @author ����԰
* @version 3.0
*/
public class MeetException extends OaException {

    /**
     * ���캯��
     * @param str    ��ʾ�ַ���Ϣ
     */
    public MeetException(String str) {
        super(str);
    }

    /**
     * ȱʡ���캯��
     */
    public MeetException() {
        super();
    }
}
