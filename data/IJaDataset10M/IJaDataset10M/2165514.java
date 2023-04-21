package bank.cnaps2.hvps.pageview;

import java.util.Date;
import gneit.topbase.security.context.UserContextHolder;
import gneit.topbase.util.date.TopDateUtils;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopComboBox;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopDateEditor;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopMoneyEditor;
import gneit.topface.ui.component.TopNotificationManager;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.component.layout.TopVFlowLayout;
import gneit.topface.ui.view.TopPageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bank.cnaps2.beps.pageview.Page5111;
import bank.cnaps2.common.tools.Tools;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button.ClickEvent;

@PageViewConfiguration(path = "classpath:bank/cnaps2/hvps/pageview/Page5704.view")
public class Page5704 extends TopPageView {

    private static final Logger logger = LoggerFactory.getLogger(Page5704.class);

    /**
	  * 定义变量
	  *
	  * */
    private TopButton agreeBtn;

    private TopButton rejectBtn;

    private TopForm inputForm;

    private TopTextEditor id_SeqNo = null;

    private TopDateEditor id_DATE = null;

    private TopForm queryform;

    private TopButton topbutton1;

    private TopDataTable topDataTable;

    private TopButton modfyBtn;

    private TopButton closeBtn;

    private TopMoneyEditor topMoneyEditor;

    public Page5704(ViewConfig viewConfig) {
        super(viewConfig);
    }

    protected void afterInitControls() {
        super.afterInitControls();
        agreeBtn = this.getControl("agreeBtn", TopButton.class);
        rejectBtn = this.getControl("rejectBtn", TopButton.class);
        inputForm = this.getControl("listDsForm", TopForm.class);
        queryform = this.getControl("queryForm", TopForm.class);
        topbutton1 = (TopButton) this.getControl("SearchBtn", TopButton.class);
        topDataTable = this.getControl("resultTable", TopDataTable.class);
        modfyBtn = (TopButton) this.getControl("ModfyBtn", TopButton.class);
        closeBtn = (TopButton) this.getControl("CloseBtn", TopButton.class);
    }

    protected void afterLoadData() {
        super.afterLoadData();
        getDataset("queryDs").getCurrent().getItemProperty("ID_DATE").setValue(TopDateUtils.stringToDate(Tools.getCurrentStringDate()));
    }

    protected void bindEvent() {
        topbutton1.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (Tools.checkWrokdate(Page5704.this, "WORKDATE", "WORKDATE2", "queryDs") == -1) return;
                Page5704.this.getCommand("queryForlistDsCmd").execute();
                if (getDataset("listDs").getTotalCount() != 0) Page5704.this.getControl("ModfyBtn", TopButton.class).setEnabled(true); else {
                    Page5704.this.getControl("ModfyBtn", TopButton.class).setEnabled(false);
                }
            }
        });
        topDataTable.addListener(new ItemClickEvent.ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    inputForm.setReadOnly(true);
                    inputForm.getField("AUD_REMARKS").setReadOnly(false);
                    agreeBtn.setEnabled(true);
                    rejectBtn.setEnabled(true);
                    getControl("detailWindow", TopWindow.class).show(getMainWindow());
                }
            }
        });
        modfyBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                inputForm.setReadOnly(true);
                inputForm.getField("AUD_REMARKS").setReadOnly(false);
                agreeBtn.setEnabled(true);
                rejectBtn.setEnabled(true);
                getControl("detailWindow", TopWindow.class).show(getMainWindow());
            }
        });
        agreeBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (getCommand("agreeCommand").execute().get("agreeCommandItem").hasException() == false) {
                    agreeBtn.setEnabled(false);
                    rejectBtn.setEnabled(false);
                    getControl("detailWindow", TopWindow.class).close();
                    getCommand("queryForlistDsCmd").setShowMessageOnScreen(false);
                    topbutton1.click();
                    getCommand("queryForlistDsCmd").setShowMessageOnScreen(true);
                }
            }
        });
        rejectBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (getCommand("rejectCommand").execute().get("rejectCommandItem").hasException() == false) {
                    agreeBtn.setEnabled(false);
                    rejectBtn.setEnabled(false);
                    getControl("detailWindow", TopWindow.class).close();
                    getCommand("queryForlistDsCmd").setShowMessageOnScreen(false);
                    topbutton1.click();
                    getCommand("queryForlistDsCmd").setShowMessageOnScreen(true);
                }
            }
        });
        closeBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                getControl("detailWindow", TopWindow.class).close();
            }
        });
        id_DATE = (TopDateEditor) queryform.getField("ID_DATE");
        Date idDate = TopDateUtils.stringToDate(Tools.getCurrentStringDate());
        getDataset("queryDs").getCurrent().getItemProperty("ID_DATE").setValue(idDate);
        id_SeqNo = (TopTextEditor) queryform.getField("ID_SeqNo");
        String strArray[] = { "ID_DATE", "ID_SeqNo", "ID_HVPS,HVPS", "ID_BEPS,BEPS" };
        id_SeqNo.addListener(Tools.addIDEvent(this, strArray, "queryDs"));
        id_DATE.addListener(Tools.addIDEvent(this, strArray, "queryDs"));
    }
}
