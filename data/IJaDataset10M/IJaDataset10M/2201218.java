package ces.coffice.docmanage.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import ces.coffice.common.affix.dao.AffixResource;
import ces.coffice.common.base.DbBase;
import ces.coffice.docmanage.DocException;
import ces.coffice.docmanage.dao.imp.ConsultDaoImp;
import ces.coffice.docmanage.vo.DocConsult;
import ces.coral.log.Logger;
import ces.platform.system.dbaccess.CodeData;
import ces.platform.system.facade.CodeInterface;

public class DocFac extends DbBase {

    private static ConsultDaoImp dao = new ConsultDaoImp();

    static Logger logger = new Logger(DocFac.class);

    /**
     * ���ӹ��Ĳ��ļ�¼�������������ļ�¼ID
     */
    public int addConsult(DocConsult docConsult) throws DocException {
        try {
            dao.doNew(docConsult);
        } catch (Exception ex) {
            logger.error("�������Ĳ�ѯ���?");
            throw new DocException(ex.getMessage());
        }
        return docConsult.getId();
    }

    /**
     * �ֱ����ces.coffice.docmanage.facade.ReceiveFac.getDocList()��ces.coffice.docmanage.facade.SendFac.getDocList()�õ��Ѱ��Ĺ���
     */
    public List getDocList(String revCondition, String sendCondition, String type) throws DocException {
        List rs = new Vector();
        List recList = new Vector();
        List sendList = new Vector();
        try {
            if (type == null || "".equals(type)) {
                recList = new ReceiveFac().getDocList(null, "-1", null, revCondition);
                sendList = new SendFac().getDocList(null, "-1", null, sendCondition);
            } else if (AffixResource.RES_TYPE_DOCRECEIVE.equals(type)) {
                recList = new ReceiveFac().getDocList(null, "-1", null, revCondition);
            } else if (AffixResource.RES_TYPE_DOCSEND.equals(type)) {
                sendList = new SendFac().getDocList(null, "-1", null, sendCondition);
            }
            rs.addAll(recList);
            rs.addAll(sendList);
        } catch (Exception ex) {
            logger.error("�õ��Ѱ�ṫ�ĳ��?");
            throw new DocException(ex.getMessage());
        }
        return rs;
    }

    /**
     *
     * @param arg String
     * @return List
     */
    public static List getTransOpinion(String arg) {
        List list = new ArrayList();
        CodeData code = null;
        Vector codes = CodeInterface.getInstance().getChildCodeDates(arg);
        for (int i = 0; i < codes.size(); i++) {
            code = (CodeData) codes.get(i);
            list.add(code.getDataKey());
        }
        return list;
    }

    /**
     * ��ݱ���?�������������ѡ��
     * @param arg String ����ά���еı�������磺��gw001��
     * @return String
     */
    public static String getDropdownValues(String arg) {
        CodeData code = null;
        String returnValue = "";
        Vector codes = CodeInterface.getInstance().getChildCodeDates(arg);
        for (int i = 0; i < codes.size(); i++) {
            code = (CodeData) codes.get(i);
            returnValue += code.getDataKey() + ";";
        }
        return returnValue.length() == 0 ? returnValue : returnValue.substring(0, returnValue.length() - 1);
    }
}
