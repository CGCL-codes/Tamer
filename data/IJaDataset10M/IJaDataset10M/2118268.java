package ru.sgnhp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sgnhp.dao.IOutgoingFileDao;
import ru.sgnhp.entity.OutgoingFileBean;
import ru.sgnhp.entity.OutgoingMailBean;
import ru.sgnhp.entity.OutgoingMailBean;

/*****
 *
 * @author Alexey Khudyakov
 * @company "Salavatgazoneftehimproekt" Ltd
 *
 *****
 */
public class OutgoingFileDaoImpl extends GenericDaoHibernate<OutgoingFileBean, Long> implements IOutgoingFileDao {

    public OutgoingFileDaoImpl() {
        super(OutgoingFileBean.class);
    }

    @Override
    public List<OutgoingFileBean> getFilesByOutgoingMail(OutgoingMailBean outgoingMailBean) {
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("outgoingMailBean", outgoingMailBean);
        List<OutgoingFileBean> list = this.findByNamedQuery("OutgoingFileBean.findByOutgoingMail", value);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list;
    }
}
