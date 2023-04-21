package bank.cnaps2.beps.pageview;

import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.ViewDataset;
import gneit.topface.core.data.command.DatasetCommand;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopComboBox;
import gneit.topface.ui.component.TopConfirmWindow;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopNotificationManager;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.component.layout.TopHFlowLayout;
import gneit.topface.ui.component.ref.TopCustomComponent;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bank.cnaps2.common.tools.Tools;
import bank.cnaps2.hvps.pageview.Page5702;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@PageViewConfiguration(path = "classpath:bank/cnaps2/beps/pageview/Page6906.view")
public class Page6906 extends TopCustomComponent {

    private static final Logger logger = LoggerFactory.getLogger(Page6906.class);

    private ViewDataset inputDs;

    private TopButton closeBtn = null;

    private TopButton submitBtn = null;

    private TopButton updateBtn;

    private TopButton deleteBtn;

    private TopTextEditor payeeBrnoEditor;

    private DatasetCommand submitCommand = null;

    private TopForm detailForm = null;

    private TopComboBox form_Operate_TYP = null;

    String op_type = "";

    private TopForm addform1 = null;

    private TopHFlowLayout adddataBtnlay1 = null;

    private TopDataTable addtable1 = null;

    private TopForm addform2 = null;

    private TopButton addDataCloseBtn = null;

    private TopButton addDataDetailBtn = null;

    private TopWindow subWindow1 = null;

    public Page6906(ViewConfig viewConfig) {
        super(viewConfig);
    }

    /**
	 * 初始化控件
	 */
    protected void afterInitControls() {
        super.afterInitControls();
        inputDs = this.getDataset("inputDataset");
        detailForm = (TopForm) this.getControl("inputDatasetForm", TopForm.class);
        addform1 = this.getControl("addform1", TopForm.class);
        adddataBtnlay1 = this.getControl("adddataBtnlay1", TopHFlowLayout.class);
        addtable1 = this.getControl("addtable1", TopDataTable.class);
        addform2 = this.getControl("addform2", TopForm.class);
        form_Operate_TYP = (TopComboBox) detailForm.getField("Operate_TYP");
        submitBtn = (TopButton) this.getControl("submitBtn", TopButton.class);
        closeBtn = (TopButton) this.getControl("CloseBtn", TopButton.class);
        updateBtn = (TopButton) this.getControl("updateBtn", TopButton.class);
        deleteBtn = (TopButton) this.getControl("deleteBtn", TopButton.class);
        addDataCloseBtn = (TopButton) this.getControl("addDataCloseBtn", TopButton.class);
        addDataDetailBtn = (TopButton) this.getControl("addDataDetailBtn", TopButton.class);
        subWindow1 = (TopWindow) getControl("SubWindow1", TopWindow.class);
        payeeBrnoEditor = (TopTextEditor) detailForm.getField("PAYEE_BRNO");
    }

    /**
	 * 页面初始化
	 */
    protected void onLoad(Map<String, Object> paramSet) {
        inputDs.getCurrent().getItemProperty("ID").setValue(paramSet.get("ID"));
        this.getCommand("QueryforBasic").execute();
        addform1.setVisible(false);
        adddataBtnlay1.setVisible(false);
        addtable1.setVisible(false);
        addform2.setVisible(false);
        detailForm.getField("PAYEE_BRNO").setReadOnly(true);
        setButtonIsEnable(true);
        String biz_type_flag = (String) paramSet.get("BIZ_TYPE_CODE");
        if ("A307".equals(biz_type_flag)) {
            this.getCommand("QueryforAddData1").execute();
            addform1.setVisible(true);
            adddataBtnlay1.setVisible(true);
            addtable1.setVisible(true);
            getDataset("inputDataset").setCurrentValue("DTL_CURCD", "CNY");
        } else if ("A301".equals(biz_type_flag)) {
            this.getCommand("QueryforAddData2").execute();
            addform2.setVisible(true);
        }
        super.onLoad(paramSet);
    }

    private void setButtonIsEnable(boolean flag) {
        updateBtn.setEnabled(flag);
        submitBtn.setEnabled(!flag);
    }

    /**
	 * 事件绑定
	 */
    protected void bindEvent() {
        payeeBrnoEditor.addListener(new TopTextEditor.ClickListener() {

            private static final long serialVersionUID = -989183034584273914L;

            public void onClick(TopTextEditor.ClickEvent event) {
                if (event.isDoubleClick()) {
                    if (!payeeBrnoEditor.isReadOnly()) {
                        getCommand("showBankRefCmd").execute();
                    }
                }
            }
        });
        payeeBrnoEditor.addListener(new FieldEvents.BlurListener() {

            public void blur(BlurEvent event) {
                String temp = (String) inputDs.getCurrentValue("PAYEE_BRNO");
                if (temp == null || "".equals(temp)) {
                    inputDs.setCurrentValue("PAYEE_BRNAME", "");
                } else {
                    if (getCommand("queryBankCmd").execute().get("queryBankItem").getCallbackCount() == 0) {
                        Tools.alertWindows(Page6906.this, "收款行不存在， 请重新输入");
                        inputDs.setCurrentValue("PAYEE_BRNO", "");
                        inputDs.setCurrentValue("PAYEE_BRNAME", "");
                    }
                }
            }
        });
        updateBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                detailForm.getField("PAYEE_BRNO").setReadOnly(false);
                setButtonIsEnable(false);
            }
        });
        submitBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (!getCommand("submitCommand1").execute().get("subcommand1").hasException()) {
                    Page6906.this.getParentWindow().closeOnBizOk();
                }
            }
        });
        deleteBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                TopConfirmWindow deleteBtnconfirm = new TopConfirmWindow("", "${bank.cnaps2.IFDELETE}", new TopConfirmWindow.Callback() {

                    public boolean onCancel() {
                        return true;
                    }

                    public boolean onOk() {
                        if (getCommand("deleteCmd").execute().get("deleteCmdItem").hasException() == false) {
                            getParentWindow().close();
                        }
                        return true;
                    }
                });
                deleteBtnconfirm.show(Page6906.this.getMainWindow());
            }
        });
        closeBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                Page6906.this.getParentWindow().close();
            }
        });
        addDataCloseBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                subWindow1.close();
            }
        });
        addDataDetailBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                subWindow1.show(getApplication().getMainWindow());
            }
        });
    }

    public void alertWindows(String caption) {
        Window window = this.getApplication().getMainWindow();
        TopNotificationManager.alert(window, caption);
    }

    @Override
    public Map<String, Object> getCallbackData() {
        return null;
    }
}
