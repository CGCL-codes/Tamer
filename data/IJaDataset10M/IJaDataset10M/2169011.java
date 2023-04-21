package bank.cnaps2.beps.pageview;

import gneit.topbase.util.date.TopDateUtils;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.event.ReferenceBizOkEvent;
import gneit.topface.api.ui.event.ReferenceBizOkListener;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.command.DatasetCommand;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopCustomReference;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopDateEditor;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopNotificationManager;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.view.TopPageView;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import bank.cnaps2.common.tools.Tools;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@PageViewConfiguration(path = "classpath:bank/cnaps2/beps/pageview/Page5504.view")
public class Page5504 extends TopPageView {

    private static final Log logger = LogFactory.getLog(Page5504.class);

    private TopTextEditor idSeqNo = null;

    private TopDateEditor idDate = null;

    private TopForm queryForm = null;

    private TopButton detailBtn;

    private TopButton queryBtn;

    private TopDataTable resultTb;

    public Page5504(ViewConfig viewConfig) {
        super(viewConfig);
    }

    public void alertWindows(String caption) {
        Window window = this.getApplication().getMainWindow();
        TopNotificationManager.alert(window, null, caption);
    }

    protected void afterInitControls() {
        super.afterInitControls();
        detailBtn = this.getControl("ModfyBtn", TopButton.class);
        detailBtn.setEnabled(false);
        queryBtn = (TopButton) this.getControl("SearchBtn", TopButton.class);
        resultTb = (TopDataTable) this.getControl("ResultDatasetTable", TopDataTable.class);
        queryForm = (TopForm) this.getControl("queryDatasetForm", TopForm.class);
        idDate = (TopDateEditor) queryForm.getField("ID_DATE");
        idSeqNo = (TopTextEditor) queryForm.getField("ID_SeqNo");
    }

    protected void bindEvent() {
        final DatasetCommand commandshow5504_2 = this.getCommand("showPage5504_2");
        final DatasetCommand query = this.getCommand("query");
        TopCustomReference ref1 = (TopCustomReference) this.getControl("Page5504_2", TopCustomReference.class);
        ref1.addListener(new ReferenceBizOkListener() {

            private static final long serialVersionUID = 1L;

            public void handle(ReferenceBizOkEvent event) {
                query.setShowMessageOnScreen(false);
                query.execute();
                query.setShowMessageOnScreen(true);
                if (getDataset("ResultDataset").getTotalCount() != 0) {
                    detailBtn.setEnabled(true);
                } else detailBtn.setEnabled(false);
            }
        });
        detailBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                commandshow5504_2.execute();
            }
        });
        resultTb.addListener(new ItemClickEvent.ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    commandshow5504_2.execute();
                }
            }
        });
        Date id_date = TopDateUtils.stringToDate(Tools.getCurrentStringDate());
        getDataset("queryDataset").getCurrent().getItemProperty("ID_DATE").setValue(id_date);
        String strArray[] = { "ID_DATE", "ID_SeqNo", "ID_BEPS,BEPS" };
        idSeqNo.addListener(Tools.addIDEvent(this, strArray, "queryDataset"));
        idDate.addListener(Tools.addIDEvent(this, strArray, "queryDataset"));
        queryBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (Tools.checkWrokdate(Page5504.this, "WORKDATE1", "WORKDATE2", "queryDataset") == -1) return;
                if (Tools.checkAmount(Page5504.this, "AMOUNT1", "AMOUNT2", "queryDataset") == -1) return;
                query.execute();
                if (getDataset("ResultDataset").getTotalCount() != 0) {
                    detailBtn.setEnabled(true);
                } else {
                    detailBtn.setEnabled(false);
                }
            }
        });
    }
}
