package org.shopformat.controller.admin.marketing.content;

import java.util.List;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.shopformat.controller.admin.crud.list.SimpleCrudListViewImpl;
import org.shopformat.domain.marketing.content.ContentPage;

public class ContentPageList extends SimpleCrudListViewImpl<Long, ContentPage> {

    @Override
    protected DataModel loadDataModel() {
        DataModel dataModel = new ListDataModel();
        List<ContentPage> pages = getShopService().findAll(ContentPage.class);
        if (!getShopService().getShopSettings().isCustomStyleSheet()) {
            for (ContentPage page : pages) {
                if (page.getDisplayId().equals("global")) {
                    pages.remove(page);
                    break;
                }
            }
        }
        dataModel.setWrappedData(pages);
        return dataModel;
    }
}
