package bank.cnaps2.beps.pageview;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import bank.cnaps2.common.tools.Tools;
import bank.cnaps2.saps.pageview.Page7231;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import gneit.topbase.security.context.UserContextHolder;
import gneit.topbase.util.date.TopDateUtils;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.event.ReferenceBizOkEvent;
import gneit.topface.api.ui.event.ReferenceBizOkListener;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.command.DatasetCommand;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopDateEditor;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopCustomReference;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.component.TopNotificationManager;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.view.TopPageView;

@PageViewConfiguration(path = "classpath:bank/cnaps2/beps/pageview/Page5203.view")
public class Page5203 extends TopPageView {

    private static final Log logger = LogFactory.getLog(Page5203.class);

    private TopTextEditor ID_SeqNo = null;

    private TopDateEditor ID_DATE = null;

    private TopForm queryForm = null;

    private DatasetCommand query;

    private DatasetCommand query4detail;

    private TopButton modfyBtn;

    private TopButton searchBtn;

    private TopButton auditBtn;

    private TopButton rejectBtn;

    private TopButton closeBtn;

    private TopDataTable resultDatasetTable;

    private TopWindow SubWindow;

    public Page5203(ViewConfig viewConfig) {
        super(viewConfig);
    }

    public void alertWindows(String caption) {
        Window window = this.getApplication().getMainWindow();
        TopNotificationManager.alert(window, null, caption);
    }

    protected void afterInitControls() {
        super.afterInitControls();
        modfyBtn = this.getControl("ModfyBtn", TopButton.class);
        auditBtn = this.getControl("AuditBtn", TopButton.class);
        rejectBtn = this.getControl("RejectBtn", TopButton.class);
        closeBtn = this.getControl("CloseBtn", TopButton.class);
        query = this.getCommand("query");
        query4detail = this.getCommand("query2");
        searchBtn = (TopButton) this.getControl("SearchBtn", TopButton.class);
        resultDatasetTable = (TopDataTable) this.getControl("ResultDatasetTable", TopDataTable.class);
        SubWindow = (TopWindow) this.getControl("SubWindows", TopWindow.class);
        modfyBtn.setEnabled(false);
    }

    protected void bindEvent() {
        resultDatasetTable = (TopDataTable) this.getControl("ResultDatasetTable", TopDataTable.class);
        auditBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (Page5203.this.getCommand("submitCmd").execute().get("submitCmdItm").hasException() == false) {
                    SubWindow.close();
                    closeRef();
                }
            }
        });
        rejectBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (Page5203.this.getCommand("rejmitCmd").execute().get("rejmitCmdItm").hasException() == false) {
                    SubWindow.close();
                    closeRef();
                }
            }
        });
        closeBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                SubWindow.close();
            }
        });
        modfyBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                query4detail.execute();
                SubWindow.show(getMainWindow());
            }
        });
        resultDatasetTable.addListener(new ItemClickEvent.ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    query4detail.execute();
                    SubWindow.show(getMainWindow());
                }
            }
        });
        queryForm = (TopForm) this.getControl("queryDatasetForm", TopForm.class);
        ID_DATE = (TopDateEditor) queryForm.getField("ID_DATE");
        Date IdDate = TopDateUtils.stringToDate(Tools.getCurrentStringDate());
        getDataset("queryDataset").getCurrent().getItemProperty("ID_DATE").setValue(IdDate);
        ID_SeqNo = (TopTextEditor) queryForm.getField("ID_SeqNo");
        String strArray[] = { "ID_DATE", "ID_SeqNo", "ID_BEPS,BEPS" };
        ID_SeqNo.addListener(Tools.addIDEvent(this, strArray, "queryDataset"));
        ID_DATE.addListener(Tools.addIDEvent(this, strArray, "queryDataset"));
        searchBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                query.execute();
                if (getDataset("ResultDataset").getTotalCount() != 0) {
                    modfyBtn.setEnabled(true);
                } else {
                    modfyBtn.setEnabled(false);
                }
            }
        });
    }

    private void closeRef() {
        query.setShowMessageOnScreen(false);
        query.execute();
        query.setShowMessageOnScreen(true);
        if (getDataset("ResultDataset").getTotalCount() != 0) {
            modfyBtn.setEnabled(true);
        } else {
            modfyBtn.setEnabled(false);
        }
    }
}
