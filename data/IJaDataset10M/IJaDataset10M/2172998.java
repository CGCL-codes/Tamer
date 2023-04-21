package bank.cnaps2.beps.pageview;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import bank.cnaps2.common.tools.Tools;
import bank.cnaps2.saps.pageview.Page7235;
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

@PageViewConfiguration(path = "classpath:bank/cnaps2/beps/pageview/Page5509.view")
public class Page5509 extends TopPageView {

    private static final Log logger = LogFactory.getLog(Page5509.class);

    private TopForm queryForm = null;

    private TopForm detailForm = null;

    private TopForm detailForm2 = null;

    private TopButton detailBtn;

    private TopButton closeBtn;

    private TopDataTable resultTable;

    private TopTextEditor idSeqNo;

    private TopButton queryBtn;

    private TopDateEditor ID_DATE = null;

    private TopForm addForm = null;

    private TopForm addForm2 = null;

    private TopForm in3Form = null;

    private TopForm in31Form = null;

    private TopForm in32Form = null;

    public Page5509(ViewConfig viewConfig) {
        super(viewConfig);
    }

    public void alertWindows(String caption) {
        Window window = this.getApplication().getMainWindow();
        TopNotificationManager.alert(window, null, caption);
    }

    protected void afterInitControls() {
        super.afterInitControls();
        queryForm = this.getControl("queryDatasetForm", TopForm.class);
        detailForm = this.getControl("inputDatasetForm", TopForm.class);
        detailForm2 = this.getControl("inputDatasetForm2", TopForm.class);
        detailBtn = (TopButton) this.getControl("ModfyBtn", TopButton.class);
        closeBtn = (TopButton) this.getControl("CloseBtn", TopButton.class);
        queryBtn = (TopButton) this.getControl("SearchBtn", TopButton.class);
        resultTable = (TopDataTable) this.getControl("ResultDatasetTable", TopDataTable.class);
        idSeqNo = (TopTextEditor) queryForm.getField("ID_SeqNo");
        ID_DATE = (TopDateEditor) queryForm.getField("ID_DATE");
        addForm = this.getControl("addDataset2Form", TopForm.class);
        addForm2 = this.getControl("addDataset3Form", TopForm.class);
        in3Form = this.getControl("inputDatasetForm3", TopForm.class);
        in31Form = this.getControl("inputDatasetForm31", TopForm.class);
        in32Form = this.getControl("inputDatasetForm32", TopForm.class);
    }

    protected void bindEvent() {
        detailBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                String biz_type_flag = (String) Page5509.this.getDataset("listDs").getCurrent().getItemProperty("BIZ_TYPE_CODE").getValue();
                String biz_ctgy_flag = (String) Page5509.this.getDataset("listDs").getCurrent().getItemProperty("BIZ_CTGY_CODE").getValue();
                if ("D102".equals(biz_type_flag)) {
                    addForm2.setVisible(false);
                    addForm.setVisible(true);
                    in31Form.setVisible(false);
                    in32Form.setVisible(false);
                    detailForm.setFieldVisible("PROXY_CURCD", true);
                    detailForm.setFieldVisible("PROXY_FEE", true);
                } else if ("D203".equals(biz_type_flag)) {
                    if ("03402".equals(biz_ctgy_flag)) {
                        addForm2.setVisible(true);
                        addForm.setVisible(false);
                        detailForm.setFieldVisible("PROXY_CURCD", false);
                        detailForm.setFieldVisible("PROXY_FEE", false);
                        in3Form.setVisible(false);
                        in31Form.setVisible(true);
                        in32Form.setVisible(false);
                    } else if ("03406".equals(biz_ctgy_flag)) {
                        addForm2.setVisible(true);
                        addForm.setVisible(false);
                        detailForm.setFieldVisible("PROXY_CURCD", false);
                        detailForm.setFieldVisible("PROXY_FEE", false);
                        in3Form.setVisible(false);
                        in31Form.setVisible(false);
                        in32Form.setVisible(true);
                    } else {
                        addForm2.setVisible(true);
                        addForm.setVisible(false);
                        detailForm.setFieldVisible("PROXY_CURCD", false);
                        detailForm.setFieldVisible("PROXY_FEE", false);
                    }
                } else {
                    addForm.setVisible(false);
                    addForm2.setVisible(false);
                    in31Form.setVisible(false);
                    in32Form.setVisible(false);
                    detailForm.setFieldVisible("PROXY_CURCD", false);
                    detailForm.setFieldVisible("PROXY_FEE", false);
                }
                detailForm.setReadOnly(true);
                detailForm2.setReadOnly(true);
                in31Form.setReadOnly(true);
                in32Form.setReadOnly(true);
                getControl("detailWindow", TopWindow.class).show(getMainWindow());
            }
        });
        resultTable.addListener(new ItemClickEvent.ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    String biz_type_flag = (String) Page5509.this.getDataset("listDs").getCurrent().getItemProperty("BIZ_TYPE_CODE").getValue();
                    if ("D102".equals(biz_type_flag)) {
                        addForm2.setVisible(false);
                        addForm.setVisible(true);
                        detailForm.setFieldVisible("PROXY_CURCD", true);
                        detailForm.setFieldVisible("PROXY_FEE", true);
                    } else if ("D203".equals(biz_type_flag)) {
                        addForm2.setVisible(true);
                        addForm.setVisible(false);
                        detailForm.setFieldVisible("PROXY_CURCD", false);
                        detailForm.setFieldVisible("PROXY_FEE", false);
                    } else {
                        addForm.setVisible(false);
                        addForm2.setVisible(false);
                        detailForm.setFieldVisible("PROXY_CURCD", false);
                        detailForm.setFieldVisible("PROXY_FEE", false);
                    }
                    detailForm.setReadOnly(true);
                    detailForm2.setReadOnly(true);
                    getControl("detailWindow", TopWindow.class).show(getMainWindow());
                    ;
                }
            }
        });
        closeBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                getControl("detailWindow", TopWindow.class).close();
            }
        });
        Date idDate = TopDateUtils.stringToDate(Tools.getCurrentStringDate());
        getDataset("queryDs").getCurrent().getItemProperty("ID_DATE").setValue(idDate);
        String strArray[] = { "ID_DATE", "ID_SeqNo", "ID_BEPS,BEPS" };
        idSeqNo.addListener(Tools.addIDEvent(this, strArray, "queryDs"));
        ID_DATE.addListener(Tools.addIDEvent(this, strArray, "queryDs"));
        queryBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (Tools.checkWrokdate(Page5509.this, "WORKDATE1", "WORKDATE2", "queryDs") == -1) return;
                if (Tools.checkAmount(Page5509.this, "AMOUNT1", "AMOUNT2", "queryDs") == -1) return;
                Page5509.this.getCommand("queryForlistDsCmd").execute();
                if (getDataset("listDs").getTotalCount() != 0) detailBtn.setEnabled(true); else {
                    detailBtn.setEnabled(false);
                }
            }
        });
    }
}
