package com.jeecms.cms.action;

import java.util.List;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import com.jeecms.cms.entity.ChnlModel;
import com.jeecms.cms.entity.ChnlModelItem;
import com.jeecms.cms.manager.ChnlModelItemMng;
import com.jeecms.cms.manager.ChnlModelMng;
import com.jeecms.common.hibernate3.Updater;

@SuppressWarnings("serial")
@Scope("prototype")
@Controller("cms.chnlModelAct")
public class ChnlModelAct extends com.jeecms.cms.CmsSysAction {

    private static final Logger log = LoggerFactory.getLogger(ChnlModelAct.class);

    public String list() {
        this.list = chnlModelMng.getModels(getRootWeb().getId(), true);
        return LIST;
    }

    public String add() {
        return ADD;
    }

    public String save() {
        if (modelId != null) {
            ChnlModel origModel = chnlModelMng.findById(modelId);
            Set<ChnlModelItem> origItems = origModel.getItems();
            ChnlModelItem item;
            for (ChnlModelItem copyItem : origItems) {
                item = new ChnlModelItem();
                try {
                    PropertyUtils.copyProperties(item, copyItem);
                } catch (Exception e) {
                    log.error("����ģ��ʧ��", e);
                }
                item.setId(null);
                bean.addModelItem(item);
            }
        }
        chnlModelMng.save(bean);
        log.info("���� ģ�� �ɹ�:{}", bean.getName());
        return list();
    }

    public String edit() {
        this.bean = chnlModelMng.findById(id);
        return EDIT;
    }

    public String copy() {
        isCopy = true;
        this.bean = chnlModelMng.findById(id);
        return ADD;
    }

    public String update() {
        chnlModelMng.updateModel(bean, modelItems);
        log.info("�޸� ģ�� �ɹ�:{}", bean.getName());
        return list();
    }

    public String delete() {
        try {
            if (id != null) {
                bean = chnlModelMng.deleteById(id);
                log.info("ɾ�� ģ�� �ɹ�:{}", bean.getName());
            } else {
                for (ChnlModel b : chnlModelMng.deleteById(ids)) {
                    log.info("ɾ�� ģ�� �ɹ�:{}", b.getName());
                }
            }
        } catch (DataIntegrityViolationException e) {
            addActionError("��¼�ѱ����ã�����ɾ��");
            return SHOW_ERROR;
        }
        return list();
    }

    public String priorityUpdate() {
        for (int i = 0; i < wids.length; i++) {
            ChnlModel f = chnlModelMng.findById(wids[i]);
            f.setPriority(prioritys[i]);
            chnlModelMng.update(f);
        }
        return list();
    }

    public String addItem() {
        return "addItem";
    }

    public String saveItem() {
        ChnlModel model = chnlModelMng.findById(modelId);
        String checkResult = checkModelItem(model);
        if (checkResult != null) {
            return checkResult;
        }
        modelItem.setModel(model);
        chnlModelItemMng.save(modelItem);
        model.getItems().add(modelItem);
        log.info("��� ģ���� �ɹ�:{}", modelItem.getName());
        id = modelId;
        return edit();
    }

    public String editItem() {
        modelItem = chnlModelItemMng.findById(id);
        return "editItem";
    }

    public String updateItem() {
        ChnlModel model = chnlModelMng.findById(modelId);
        String checkResult = checkModelItem(model);
        if (checkResult != null) {
            return checkResult;
        }
        modelItem.setModel(model);
        Updater updater = Updater.create(modelItem);
        updater.include(ChnlModelItem.PROP_INPUT_SIZE);
        updater.include(ChnlModelItem.PROP_INPUT_WIDTH);
        updater.include(ChnlModelItem.PROP_TEXTAREA_COLS);
        updater.include(ChnlModelItem.PROP_TEXTAREA_ROWS);
        chnlModelItemMng.updateByUpdater(updater);
        log.info("�޸� ģ���� �ɹ�:{}", modelItem.getName());
        id = modelId;
        return edit();
    }

    private String checkModelItem(ChnlModel model) {
        if (!model.getWebsite().getId().equals(getRootWebId())) {
            addActionError("���ܿ�վ�����ģ����");
            log.warn("��վ�����ģ����website��{}��admin��{}��", getRootWeb().getDomain(), getAdmin().getLoginName());
            return SHOW_ERROR;
        } else {
            return null;
        }
    }

    public boolean validateSave() {
        if (hasErrors()) {
            return true;
        }
        bean.setWebsite(getRootWeb());
        return false;
    }

    public boolean validateEdit() {
        if (hasErrors()) {
            return true;
        }
        if (vldExist(id)) {
            return true;
        }
        if (vldWebsite(id, null)) {
            return true;
        }
        return false;
    }

    public boolean validateUpdate() {
        if (hasErrors()) {
            return true;
        }
        if (vldExist(bean.getId())) {
            return true;
        }
        if (vldWebsite(bean.getId(), null)) {
            return true;
        }
        bean.setWebsite(getRootWeb());
        return false;
    }

    public boolean validateDelete() {
        if (hasErrors()) {
            return true;
        }
        ChnlModel po;
        if (id == null && (ids == null || ids.length <= 0)) {
            addActionError("ID����Ϊ��");
            return true;
        } else {
            if (id != null) {
                ids = new Long[] { id };
            }
            for (Long id : ids) {
                po = chnlModelMng.findById(id);
                if (!po.getWebsite().getId().equals(getRootWebId())) {
                    addActionError("����ɾ������վ�����");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean vldExist(Long id) {
        ChnlModel entity = chnlModelMng.findById(id);
        if (entity == null) {
            addActionError("ģ�Ͳ����ڣ�" + id);
            return true;
        }
        return false;
    }

    private boolean vldWebsite(Long id, ChnlModel bean) {
        ChnlModel entity = chnlModelMng.findById(id);
        if (!entity.getWebsite().getId().equals(getRootWebId())) {
            addActionError("ֻ���޸ı�վ����ݣ�" + id);
            return true;
        }
        if (bean != null) {
            bean.setWebsite(getRootWeb());
        }
        return false;
    }

    @Autowired
    protected ChnlModelMng chnlModelMng;

    private ChnlModel bean;

    private List<ChnlModelItem> modelItems;

    private boolean isCopy = false;

    @Autowired
    private ChnlModelItemMng chnlModelItemMng;

    private ChnlModelItem modelItem;

    private Long modelId;

    private Long itemType;

    private Long[] wids;

    private int[] prioritys;

    public ChnlModel getBean() {
        return bean;
    }

    public void setBean(ChnlModel bean) {
        this.bean = bean;
    }

    public List<ChnlModelItem> getModelItems() {
        return modelItems;
    }

    public void setModelItems(List<ChnlModelItem> modelItems) {
        this.modelItems = modelItems;
    }

    public boolean isCopy() {
        return isCopy;
    }

    public void setCopy(boolean isCopy) {
        this.isCopy = isCopy;
    }

    public ChnlModelItem getModelItem() {
        return modelItem;
    }

    public void setModelItem(ChnlModelItem modelItem) {
        this.modelItem = modelItem;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public Long getItemType() {
        return itemType;
    }

    public void setItemType(Long itemType) {
        this.itemType = itemType;
    }

    public Long[] getWids() {
        return wids;
    }

    public void setWids(Long[] wids) {
        this.wids = wids;
    }

    public int[] getPrioritys() {
        return prioritys;
    }

    public void setPrioritys(int[] prioritys) {
        this.prioritys = prioritys;
    }
}
