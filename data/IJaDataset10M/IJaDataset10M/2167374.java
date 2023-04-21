package ces.platform.infoplat.ui.workflow.approve.defaultvalue;

import ces.platform.infoplat.ui.common.Translator;

/**
 * <p>
 * Title: ������Ϣƽ̨
 * </p>
 * <p>
 * Description:�������(�Զ������)�е�doc_id��Ĭ��ֵ��
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: �Ϻ�������Ϣ��չ���޹�˾
 * </p>
 * 
 * @author ����
 * @version 2.5
 */
public class DocID extends Translator {

    public String getDefaultValue() throws Exception {
        String docID = request.getParameter("docID");
        if (docID == null) {
            throw new Exception("���������docID�Ҳ���!");
        }
        return docID;
    }
}
