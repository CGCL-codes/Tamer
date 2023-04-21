package bank.cnaps2.beps.pageview;

import java.util.Date;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import bank.cnaps2.common.tools.Tools;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import gneit.topbase.api.core.serivce.callback.Callback;
import gneit.topbase.security.context.UserContextHolder;
import gneit.topbase.util.date.TopDateUtils;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.event.ReferenceBizOkEvent;
import gneit.topface.api.ui.event.ReferenceBizOkListener;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.ViewDataset;
import gneit.topface.core.data.command.DatasetCommand;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.ref.TopCustomComponent;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopDateEditor;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopCustomReference;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.component.TopNotificationManager;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.view.TopPageView;

@PageViewConfiguration(path = "classpath:bank/cnaps2/beps/pageview/Page5607.view")
public class Page5607 extends TopPageView {

    private static final Log logger = LogFactory.getLog(Page5607.class);

    /**
	  * 定义变量
	  *
	  * */
    private TopTextEditor idSeqNo = null;

    private TopDateEditor idDate = null;

    private TopForm queryForm = null;

    private TopButton modfyBtn;

    private DatasetCommand commandshow5607_2;

    private DatasetCommand commandshow5607_3;

    private DatasetCommand query;

    private TopCustomReference ref1;

    private TopButton searchBtn;

    private TopDataTable topDataTable;

    public Page5607(ViewConfig viewConfig) {
        super(viewConfig);
    }

    public void alertWindows(String caption) {
        Window window = this.getApplication().getMainWindow();
        TopNotificationManager.alert(window, null, caption);
    }

    protected void afterInitControls() {
        modfyBtn = this.getControl("ModfyBtn", TopButton.class);
        ref1 = (TopCustomReference) this.getControl("Page5607_2", TopCustomReference.class);
        modfyBtn = (TopButton) this.getControl("ModfyBtn", TopButton.class);
        searchBtn = (TopButton) this.getControl("SearchBtn", TopButton.class);
        topDataTable = (TopDataTable) this.getControl("ResultDsTb", TopDataTable.class);
        queryForm = (TopForm) this.getControl("queryDsForm", TopForm.class);
        commandshow5607_2 = this.getCommand("showPage5607_2");
        commandshow5607_3 = this.getCommand("showPage5607_3");
        query = this.getCommand("query");
        modfyBtn.setEnabled(false);
        super.afterInitControls();
    }

    @Override
    protected void afterLoadData() {
        super.afterLoadData();
        Item current = this.getDataset("queryDs").getCurrent();
        current.getItemProperty("PKG_NO").setValue("123");
    }

    protected void bindEvent() {
        ref1.addListener(new ReferenceBizOkListener() {

            public void handle(ReferenceBizOkEvent event) {
                query.setShowMessageOnScreen(false);
                query.execute();
                query.setShowMessageOnScreen(true);
                if (getDataset("ResultDs").getTotalCount() != 0) {
                    modfyBtn.setEnabled(true);
                } else {
                    modfyBtn.setEnabled(false);
                }
            }
        });
        modfyBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                ViewDataset inputDataset_1 = Page5607.this.getDataset("ResultDs");
                String pkg_no = (String) inputDataset_1.getCurrent().getItemProperty("PKG_NO").getValue();
                if ("123".equals(pkg_no)) {
                    commandshow5607_2.execute();
                } else if ("131".equals(pkg_no)) {
                    commandshow5607_3.execute();
                }
            }
        });
        topDataTable.addListener(new ItemClickEvent.ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                ViewDataset inputDataset_1 = Page5607.this.getDataset("ResultDs");
                String pkg_no = (String) inputDataset_1.getCurrent().getItemProperty("PKG_NO").getValue();
                if (event.isDoubleClick()) {
                    if ("123".equals(pkg_no)) {
                        commandshow5607_2.execute();
                    } else if ("131".equals(pkg_no)) {
                        commandshow5607_3.execute();
                    }
                }
            }
        });
        searchBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (Tools.checkWrokdate(Page5607.this, "WORKDATE1", "WORKDATE2", "queryDs") == -1) return;
                if (Tools.checkAmount(Page5607.this, "AMOUNT1", "AMOUNT2", "queryDs") == -1) return;
                query.execute();
                if (getDataset("ResultDs").getTotalCount() != 0) {
                    modfyBtn.setEnabled(true);
                } else {
                    modfyBtn.setEnabled(false);
                }
            }
        });
        idDate = (TopDateEditor) queryForm.getField("ID_DATE");
        Date IdDate = TopDateUtils.stringToDate(Tools.getCurrentStringDate());
        getDataset("queryDs").getCurrent().getItemProperty("ID_DATE").setValue(IdDate);
        idSeqNo = (TopTextEditor) queryForm.getField("ID_SeqNo");
        String strArray[] = { "ID_DATE", "ID_SeqNo", "ID_BEPS,BEPS" };
        idSeqNo.addListener(Tools.addIDEvent(this, strArray, "queryDs"));
    }
}
