package org.richfaces.tarkus.samples.gui.contry;

import java.util.List;
import org.richfaces.tarkus.fmk.gui.bean.AbstractListBean;
import org.richfaces.tarkus.samples.model.ContryBean;
import org.richfaces.tarkus.samples.service.IContryService;

/**
 * TODO Tarkus : Add A comment
 * 
 * @version $Revision$
 * @author TARAK, last modified by $Author$
 * 
 */
public class ListContryBean extends AbstractListBean<ContryBean> {

    private IContryService contryService;

    public ListContryBean() {
        super("listContry", "id", false);
    }

    /**
   * TODO TARAK Add a comment
   * 
   * @see org.richfaces.tarkus.fmk.gui.bean.AbstractListBean#addColumnDefinition()
   */
    @Override
    protected void addColumnDefinition() {
        table.addTextColumn("id", getElMsg("ListContry_ID"), true);
        table.addTextColumn("code", getElMsg("ListContry_CODE"), true);
        addDeleteColumn(table);
        addEditColumn(table, "#{listContry.edit}");
    }

    /**
   * TODO TARAK Add a comment
   * 
   * @see org.richfaces.tarkus.fmk.gui.bean.AbstractListBean#delete(java.lang.Object)
   */
    @Override
    protected void delete(ContryBean t) {
        contryService.delete(t.getId());
    }

    /**
   * TODO TARAK Add a comment
   * 
   * @see org.richfaces.tarkus.fmk.gui.bean.AbstractListBean#getDataList()
   */
    @Override
    public List<ContryBean> getDataList() {
        List<ContryBean> itemList = null;
        if (filterComponent != null && ((filterComponent.getFilterList().size() > 0) || (filterComponent.getMaxResults() > 0))) {
            itemList = dao.findByCriteria(ContryBean.class, filterComponent.getFilterList(), filterComponent.getMaxResults());
        } else {
            itemList = dao.findAll(ContryBean.class);
        }
        return itemList;
    }

    /**
   * TODO TARAK Add a comment
   * 
   * @see org.richfaces.tarkus.fmk.gui.bean.AbstractListBean#getEditKey(java.lang.Object)
   */
    @Override
    protected Object getEditKey(ContryBean t) {
        return t.getId();
    }

    public IContryService getContryService() {
        return contryService;
    }

    public void setContryService(IContryService contryService) {
        this.contryService = contryService;
    }
}
