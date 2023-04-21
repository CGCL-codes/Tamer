package org.shopformat.controller.admin.marketing.survey;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.shopformat.controller.admin.crud.list.OrderedCrudListViewImpl;
import org.shopformat.domain.marketing.survey.Reason;

public class ReasonList extends OrderedCrudListViewImpl<Long, Reason> {

    protected DataModel loadDataModel() {
        DataModel dataModel = new ListDataModel();
        dataModel.setWrappedData(getShopService().findAll(Reason.class));
        return dataModel;
    }
}
