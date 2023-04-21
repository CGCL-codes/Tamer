package uit.upis.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import uit.upis.dao.UrbanDAO;
import uit.upis.manager.UrbanManager;
import uit.upis.model.DefEntity;
import uit.upis.model.Gosi;
import uit.upis.model.UrbanResult;

public class UrbanManagerImpl implements UrbanManager {

    /**
	 * Logger for this class
	 */
    private final Logger log = Logger.getLogger(getClass());

    private UrbanDAO dao;

    /**
	 * @param dao The dao to set.
	 */
    public void setUrbanDAO(UrbanDAO dao) {
        this.dao = dao;
    }

    /**
	 * ���ð�ȹ�ü� �˻�
	 * @param defEntity
	 * @return
	 */
    public HashMap getSearch(DefEntity defEntity) {
        return dao.getSearch(defEntity);
    }

    /**
	 * ������� ��ȸ
	 * @param objectId
	 * @return
	 */
    public Gosi getGosi(long objectId) {
        return dao.getGosi(objectId);
    }

    /**
	 * �������� �� ��ȸ
	 * @param defEntity
	 * @return
	 */
    public UrbanResult getJoseo(DefEntity defEntity) {
        return dao.getJoseo(defEntity);
    }

    public List getHistList(String tblName, String grKey) {
        return dao.getHistList(tblName, grKey);
    }

    public List getHistDetail() {
        return dao.getHistDetail();
    }

    /**
	 * ������������ ��ȸ
	 * @param code
	 * @param dcnJId
	 * @return
	 */
    public List getEntryParcel(String code, String dcnJId) {
        return dao.getEntryParcel(code, dcnJId);
    }

    /**
	 * �������� ����Ʈ ��ȸ
	 * @param code
	 * @param dcnJId
	 * @return
	 */
    public List getEntryParcelList(String code, String dcnJId) {
        return dao.getEntryParcelList(code, dcnJId);
    }
}
