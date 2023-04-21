package bank.cnaps2.ccms.pageview;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bank.cnaps2.beps.pageview.Page5108;
import bank.cnaps2.common.tools.Tools;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button.ClickEvent;
import gneit.topbase.security.context.UserContextHolder;
import gneit.topbase.util.date.TopDateUtils;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.ViewDataset;
import gneit.topface.core.data.command.DatasetCommand;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopDateEditor;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopNotificationManager;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.view.TopPageView;

@PageViewConfiguration(path = "classpath:bank/cnaps2/ccms/pageview/Page7148_1.view")
public class Page7148_1 extends TopPageView {

    private static final Logger logger = LoggerFactory.getLogger(Page7148_1.class);

    private TopForm queryForm;

    private TopDateEditor idDateEditor;

    private TopTextEditor idSeqEditor;

    private ViewDataset queryDs;

    private TopButton searchBtn;

    private TopButton modifyBtn;

    private TopDataTable topTable;

    private DatasetCommand queryCmd;

    private DatasetCommand show7148_2Cmd;

    public Page7148_1(ViewConfig viewConfig) {
        super(viewConfig);
    }

    @Override
    protected void afterInitControls() {
        super.afterInitControls();
        queryForm = (TopForm) this.getControl("queryForm", TopForm.class);
        idDateEditor = (TopDateEditor) queryForm.getField("ID_DATE");
        idSeqEditor = (TopTextEditor) queryForm.getField("ID_SeqNo");
        queryDs = Page7148_1.this.getDataset("queryDs");
        topTable = (TopDataTable) this.getControl("resultTable", TopDataTable.class);
        modifyBtn = (TopButton) this.getControl("ModifyBtn", TopButton.class);
        searchBtn = (TopButton) this.getControl("SearchBtn", TopButton.class);
    }

    protected void bindEvent() {
        queryCmd = this.getCommand("searchCmd");
        show7148_2Cmd = this.getCommand("showPage7148_2");
        modifyBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                show7148_2Cmd.execute();
            }
        });
        topTable.addListener(new ItemClickEvent.ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    show7148_2Cmd.execute();
                }
            }
        });
        searchBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (Tools.checkWrokdate(Page7148_1.this, "WORKDATE", "WORKDATE2", "queryDs") == -1) return;
                queryCmd.execute();
                if (getDataset("listDs").getTotalCount() != 0) {
                    modifyBtn.setEnabled(true);
                } else {
                    modifyBtn.setEnabled(false);
                }
            }
        });
        Date idDate = TopDateUtils.stringToDate(Tools.getCurrentStringDate());
        queryDs.getCurrent().getItemProperty("ID_DATE").setValue(idDate);
        String strArray[] = { "ID_DATE", "ID_SeqNo", "ID_CCMS,CCMS" };
        idSeqEditor.addListener(Tools.addIDEvent(this, strArray, "queryDs"));
    }
}
