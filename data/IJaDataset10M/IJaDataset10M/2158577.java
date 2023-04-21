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
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.view.TopPageView;

@PageViewConfiguration(path = "classpath:bank/cnaps2/beps/pageview/UpdatePage.view")
public class UpdatePage extends TopPageView {

    private static final Log logger = LogFactory.getLog(UpdatePage.class);

    private TopTextEditor idSeqNo = null;

    private TopDateEditor idDate = null;

    private TopForm queryForm = null;

    private TopButton topbutton;

    private TopButton topbutton1;

    private TopDataTable topDataTable;

    private DatasetCommand commandshow5005;

    private DatasetCommand commandshow5006;

    private DatasetCommand queryForlistDsCmd;

    private TopCustomReference Page5005;

    private TopCustomReference Page5006;

    public UpdatePage(ViewConfig viewConfig) {
        super(viewConfig);
    }

    public void alertWindows(String caption) {
        Window window = this.getApplication().getMainWindow();
        TopNotificationManager.alert(window, caption);
    }

    @Override
    protected void afterInitControls() {
        super.afterInitControls();
        queryForm = (TopForm) this.getControl("queryForm", TopForm.class);
        idDate = (TopDateEditor) queryForm.getField("ID_DATE");
        idSeqNo = (TopTextEditor) queryForm.getField("ID_SeqNo");
        topbutton = (TopButton) this.getControl("ModfyBtn", TopButton.class);
        topbutton1 = (TopButton) this.getControl("SearchBtn", TopButton.class);
        topDataTable = (TopDataTable) this.getControl("resultTable", TopDataTable.class);
        Page5005 = (TopCustomReference) this.getControl("Page5005", TopCustomReference.class);
        Page5006 = (TopCustomReference) this.getControl("Page5006", TopCustomReference.class);
    }

    @Override
    protected void afterInitCommands() {
        super.afterInitCommands();
        commandshow5005 = this.getCommand("showPage5005");
        commandshow5006 = this.getCommand("showPage5006");
        queryForlistDsCmd = this.getCommand("queryForlistDsCmd");
    }

    protected void bindEvent() {
        topbutton.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                String PKG_ID = (String) getDataset("listDs").getCurrent().getItemProperty("PKG_ID").getValue();
                if ("beps.127.001.01".equals(PKG_ID)) {
                    commandshow5005.execute();
                } else if ("beps.133.001.01".equals(PKG_ID)) {
                    commandshow5006.execute();
                }
            }
        });
        topbutton1.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (Tools.checkWrokdate(UpdatePage.this, "CPGDATE", "CPGDATE2", "queryDs") == -1) return;
                if (Tools.checkAmount(UpdatePage.this, "AMOUNT", "AMOUNT2", "queryDs") == -1) return;
                queryForlistDsCmd.execute();
                if (getDataset("listDs").getTotalCount() != 0) {
                    topbutton.setEnabled(true);
                } else {
                    topbutton.setEnabled(false);
                }
            }
        });
        topDataTable.addListener(new ItemClickEvent.ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    String PKG_ID = (String) getDataset("listDs").getCurrent().getItemProperty("PKG_ID").getValue();
                    if ("beps.127.001.01".equals(PKG_ID)) {
                        commandshow5005.execute();
                    } else if ("beps.133.001.01".equals(PKG_ID)) {
                        commandshow5006.execute();
                    }
                }
            }
        });
        Page5005.addListener(new ReferenceBizOkListener() {

            public void handle(ReferenceBizOkEvent event) {
                queryForlistDsCmd.setShowMessageOnScreen(false);
                topbutton1.click();
                queryForlistDsCmd.setShowMessageOnScreen(true);
            }
        });
        Page5006.addListener(new ReferenceBizOkListener() {

            public void handle(ReferenceBizOkEvent event) {
                queryForlistDsCmd.setShowMessageOnScreen(false);
                topbutton1.click();
                queryForlistDsCmd.setShowMessageOnScreen(true);
            }
        });
        Date IdDate = TopDateUtils.stringToDate(Tools.getCurrentStringDate());
        getDataset("queryDs").getCurrent().getItemProperty("ID_DATE").setValue(IdDate);
        String strArray[] = { "ID_DATE", "ID_SeqNo", "ID_HVPS,HVPS", "ID_BEPS,BEPS" };
        idSeqNo.addListener(Tools.addIDEvent(this, strArray, "queryDs"));
        idDate.addListener(Tools.addIDEvent(this, strArray, "queryDs"));
    }
}
