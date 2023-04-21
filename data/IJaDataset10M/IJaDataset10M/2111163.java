package bank.cnaps2.beps.pageview;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import bank.cnaps2.common.tools.Tools;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import gneit.topbase.security.context.UserContextHolder;
import gneit.topbase.util.date.TopDateUtils;
import gneit.topface.api.ui.config.ViewConfig;
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

@PageViewConfiguration(path = "classpath:bank/cnaps2/beps/pageview/Page6308.view")
public class Page6308 extends TopPageView {

    private static final Log logger = LogFactory.getLog(Page6308.class);

    private TopTextEditor ID_SeqNo = null;

    private TopDateEditor ID_DATE = null;

    private TopForm queryForm = null;

    private DatasetCommand commandshow6308_2;

    private DatasetCommand query;

    private TopCustomReference Page6308_2;

    private TopButton modfyBtn;

    private TopButton searchBtn;

    private TopDataTable resultDatasetTable;

    public Page6308(ViewConfig viewConfig) {
        super(viewConfig);
    }

    public void alertWindows(String caption) {
        Window window = this.getApplication().getMainWindow();
        TopNotificationManager.alert(window, null, caption);
    }

    protected void afterInitControls() {
        super.afterInitControls();
        modfyBtn = this.getControl("ModfyBtn", TopButton.class);
        commandshow6308_2 = this.getCommand("showPage6308_2");
        query = this.getCommand("query");
        Page6308_2 = (TopCustomReference) this.getControl("Page6308_2", TopCustomReference.class);
        searchBtn = (TopButton) this.getControl("SearchBtn", TopButton.class);
        resultDatasetTable = (TopDataTable) this.getControl("ResultDatasetTable", TopDataTable.class);
        modfyBtn.setEnabled(false);
    }

    protected void bindEvent() {
        commandshow6308_2 = this.getCommand("showPage6308_2");
        query = this.getCommand("query");
        Page6308_2 = (TopCustomReference) this.getControl("Page6308_2", TopCustomReference.class);
        searchBtn = (TopButton) this.getControl("SearchBtn", TopButton.class);
        resultDatasetTable = (TopDataTable) this.getControl("ResultDatasetTable", TopDataTable.class);
        Page6308_2.addListener(new TopWindow.CloseListener() {

            public void windowClose(CloseEvent e) {
                query.setShowMessageOnScreen(false);
                query.execute();
                query.setShowMessageOnScreen(true);
                if (getDataset("ResultDataset").getTotalCount() != 0) {
                    modfyBtn.setEnabled(true);
                } else modfyBtn.setEnabled(false);
            }
        });
        modfyBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                commandshow6308_2.execute();
            }
        });
        resultDatasetTable.addListener(new ItemClickEvent.ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    commandshow6308_2.execute();
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
                if (getDataset("queryDataset").getCurrent().getItemProperty("WORKDATE1").getValue() != null && getDataset("queryDataset").getCurrent().getItemProperty("WORKDATE2").getValue() != null) if (getDataset("queryDataset").getCurrent().getItemProperty("WORKDATE1").getValue().toString().compareTo(getDataset("queryDataset").getCurrent().getItemProperty("WORKDATE2").getValue().toString()) > 0) {
                    alertWindows("截止日期不能小于开始日期！");
                    return;
                }
                if (getDataset("queryDataset").getCurrent().getItemProperty("AMOUNT1").getValue() != null && getDataset("queryDataset").getCurrent().getItemProperty("AMOUNT2").getValue() != null) if (getDataset("queryDataset").getCurrent().getItemProperty("AMOUNT1").getValue().toString().compareTo(getDataset("queryDataset").getCurrent().getItemProperty("AMOUNT2").getValue().toString()) > 0) {
                    alertWindows("截止交易金額不能小于开始交易金額！");
                    return;
                }
                query.execute();
                if (getDataset("ResultDataset").getTotalCount() != 0) {
                    modfyBtn.setEnabled(true);
                } else {
                    modfyBtn.setEnabled(false);
                }
            }
        });
    }
}
