package bank.cnaps2.ccms.pageview;

import java.util.Date;
import gneit.topbase.security.context.UserContextHolder;
import gneit.topbase.util.date.TopDateUtils;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopDateEditor;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopNotificationManager;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.view.TopPageView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import bank.cnaps2.common.tools.Tools;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button.ClickEvent;

@PageViewConfiguration(path = "classpath:bank/cnaps2/ccms/pageview/Page7123.view")
public class Page7123 extends TopPageView {

    private static final Log logger = LogFactory.getLog(Page7123.class);

    private TopButton quaryButton;

    private TopButton detailButton;

    private TopForm resultFrom;

    private TopDataTable resultTable;

    private TopButton closeButton;

    private TopButton refuseButton;

    private TopButton reviewButton;

    public Page7123(ViewConfig viewConfig) {
        super(viewConfig);
    }

    protected void afterInitControls() {
        super.afterInitControls();
        detailButton = (TopButton) this.getControl("detailButton", TopButton.class);
        resultFrom = (TopForm) this.getControl("resultFrom", TopForm.class);
        quaryButton = (TopButton) this.getControl("quaryButton", TopButton.class);
        resultTable = (TopDataTable) this.getControl("resultTable", TopDataTable.class);
        closeButton = (TopButton) this.getControl("closeButton", TopButton.class);
        refuseButton = (TopButton) this.getControl("refuseButton", TopButton.class);
        reviewButton = (TopButton) this.getControl("reviewbutton", TopButton.class);
    }

    protected void bindEvent() {
        detailButton.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                getControl("subWindows", TopWindow.class).show(getMainWindow());
            }
        });
        resultTable.addListener(new ItemClickEvent.ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    getControl("subWindows", TopWindow.class).show(getMainWindow());
                }
            }
        });
        quaryButton.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (getDataset("resultDataset").getTotalCount() != 0) {
                    detailButton.setEnabled(true);
                } else {
                    detailButton.setEnabled(false);
                }
            }
        });
        reviewButton.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (getCommand("reviewCommand").execute().get("reviewCommandItem").hasException() == false) {
                    getControl("subWindows", TopWindow.class).close();
                    getCommand("autoQuaryCommand").setShowMessageOnScreen(false);
                    getCommand("autoQuaryCommand").execute();
                    getCommand("autoQuaryCommand").setShowMessageOnScreen(true);
                    if (getDataset("resultDataset").getTotalCount() != 0) {
                        detailButton.setEnabled(true);
                    } else {
                        detailButton.setEnabled(false);
                    }
                }
            }
        });
        refuseButton.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (getCommand("refuseCommand").execute().get("refuseCommandItem").hasException() == false) {
                    getControl("subWindows", TopWindow.class).close();
                    getCommand("autoQuaryCommand").setShowMessageOnScreen(false);
                    getCommand("autoQuaryCommand").execute();
                    getCommand("autoQuaryCommand").setShowMessageOnScreen(true);
                    if (getDataset("resultDataset").getTotalCount() != 0) {
                        detailButton.setEnabled(true);
                    } else {
                        detailButton.setEnabled(false);
                    }
                }
            }
        });
        closeButton.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent event) {
                getControl("subWindows", TopWindow.class).close();
            }
        });
    }
}
