package bank.cnaps2.manager.pageview;

import gneit.topbase.api.core.param.Param;
import gneit.topbase.api.core.param.ParamSet;
import gneit.topbase.security.context.UserContextHolder;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.ViewDataset;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.view.TopPageView;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button.ClickEvent;

@PageViewConfiguration(path = "classpath:bank/cnaps2/manager/pageview/Page8402.view")
public class Page8402 extends TopPageView {

    private static final long serialVersionUID = 1L;

    private TopButton detailBtn;

    private TopButton checkBtn;

    private TopButton refuseBtn;

    private TopButton closeBtn;

    private TopButton queryBtn;

    private ViewDataset resultDs;

    private TopDataTable resultTable;

    public Page8402(ViewConfig viewConfig) {
        super(viewConfig);
    }

    @Override
    protected void beforeLoadData() {
        super.beforeLoadData();
        ParamSet paramset = new ParamSet();
        paramset.add(new Param("USER_ID", UserContextHolder.getContext().getSys("tlrno")));
        this.getDataset("selectDs").setParamSet(paramset);
    }

    @Override
    protected void afterInitControls() {
        super.afterInitControls();
        detailBtn = this.getControl("Detail", TopButton.class);
        checkBtn = this.getControl("CheckBtn", TopButton.class);
        refuseBtn = this.getControl("RefuseBtn", TopButton.class);
        closeBtn = this.getControl("CloseBtn", TopButton.class);
        queryBtn = this.getControl("Query", TopButton.class);
        resultDs = (ViewDataset) this.getDataset("resultDs");
        resultTable = (TopDataTable) this.getControl("resultTable", TopDataTable.class);
    }

    @Override
    protected void bindEvent() {
        queryBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent clickevent) {
                getCommand("queryCmd").execute();
                if (resultDs.getTotalCount() > 0) {
                    detailBtn.setEnabled(true);
                } else {
                    detailBtn.setEnabled(false);
                }
            }
        });
        detailBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent clickevent) {
                getControl("subWindow", TopWindow.class).show(getMainWindow());
            }
        });
        resultTable.addListener(new ItemClickEvent.ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    getControl("subWindow", TopWindow.class).show(getMainWindow());
                }
            }
        });
        closeBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent clickevent) {
                getControl("subWindow", TopWindow.class).close();
            }
        });
        checkBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent clickevent) {
                if (getCommand("checkCmd").execute().get("checkCmdItem").hasException() == false) {
                    getControl("subWindow", TopWindow.class).close();
                    getCommand("queryCmd").setShowMessageOnScreen(false);
                    getCommand("queryCmd").execute();
                    getCommand("queryCmd").setShowMessageOnScreen(true);
                    if (resultDs.getTotalCount() > 0) {
                        detailBtn.setEnabled(true);
                    } else {
                        detailBtn.setEnabled(false);
                    }
                }
            }
        });
        refuseBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent clickevent) {
                if (getCommand("rejectCmd").execute().get("rejectCmdItem").hasException() == false) {
                    getControl("subWindow", TopWindow.class).close();
                    getCommand("queryCmd").setShowMessageOnScreen(false);
                    getCommand("queryCmd").execute();
                    getCommand("queryCmd").setShowMessageOnScreen(true);
                    if (resultDs.getTotalCount() > 0) {
                        detailBtn.setEnabled(true);
                    } else {
                        detailBtn.setEnabled(false);
                    }
                }
            }
        });
    }
}
