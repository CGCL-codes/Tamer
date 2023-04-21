package ces.platform.infoplat.ui.workflow.collect;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import ces.coral.formdefine.errorreport.IErrorReport;

/**
 * <p>Title: ������Ϣƽ̨</p>
 * <p>Description:�?�Զ��������Ϣ��</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: �Ϻ�������Ϣ��չ���޹�˾</p>
 * @author ����԰
 * @version 3.0
 */
public class DevPlatErrorReport implements IErrorReport {

    /**
     * ��׽������ͨ�ô���ӿ�Ĭ��ʵ����
     * @param e Exception ��׽���쳣
     * @param req ServletRequest ����
     * @param resp ServletResponse ��Ӧ
     * @return boolean �Ƿ��жϳ���
     */
    public boolean catchError(Exception e, ServletRequest req, ServletResponse resp) {
        try {
            e.printStackTrace();
            resp.getWriter().println("<script>alert('�����ˣ�������Ϣ:" + e.getMessage().replace('\n', ' ') + "');</script>");
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
