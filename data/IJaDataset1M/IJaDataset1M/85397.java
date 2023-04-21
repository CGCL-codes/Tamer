package bank.cnaps2.nets.pageview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bank.cnaps2.common.tools.Tools;
import com.vaadin.event.ItemClickEvent;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.ViewDataset;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.view.TopPageView;

@PageViewConfiguration(path = "classpath:bank/cnaps2/qingsuan/pageview/Page7210.view")
public class Page7210 extends TopPageView {

    private static final Logger logger = LoggerFactory.getLogger(Page7210.class);

    private ViewDataset listDs = null;

    private TopDataTable topTable;

    public Page7210(ViewConfig viewConfig) {
        super(viewConfig);
    }

    protected void afterInitControls() {
        super.afterInitControls();
        topTable = (TopDataTable) this.getControl("tableResult", TopDataTable.class);
    }

    protected void bindEvent() {
        topTable.addListener(new ItemClickEvent.ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                getControl("detailWindow", TopWindow.class).show(getMainWindow());
            }
        });
    }
}
