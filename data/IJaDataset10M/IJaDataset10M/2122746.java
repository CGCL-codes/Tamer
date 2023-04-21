package bank.cnaps2.fee.pageview;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.ViewDataset;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopMoneyEditor;
import gneit.topface.ui.component.TopNotificationManager;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.view.TopPageView;
import gneit.topface.ui.component.TopComboBox;

/**
 * @author chaihaifeng
 * 标准手续费维护审核
 */
@PageViewConfiguration(path = "classpath:bank/cnaps2/fee/pageview/Page8102.view")
public class Page8102 extends TopPageView {

    private static final long serialVersionUID = 1324234234245L;

    private TopButton detailBtn;

    private TopButton searchBtn;

    private TopButton closeBtn;

    private TopButton CheckBtn;

    private TopButton RefuseBtn;

    private ViewDataset resultDs;

    private TopForm resultForm_subWindow1;

    private TopComboBox segmt_flg;

    private TopDataTable segmtTb;

    private TopComboBox feeType0;

    private TopMoneyEditor fixAmtEditor0;

    private TopTextEditor rateEditor0;

    private TopMoneyEditor lowAmtEditor0;

    private TopMoneyEditor highAmtEditor0;

    public Page8102(ViewConfig viewConfig) {
        super(viewConfig);
    }

    @Override
    protected void afterInitControls() {
        super.afterInitControls();
        detailBtn = (TopButton) this.getControl("detailBtn", TopButton.class);
        searchBtn = (TopButton) this.getControl("searchBtn", TopButton.class);
        closeBtn = (TopButton) this.getControl("CloseBtn", TopButton.class);
        CheckBtn = (TopButton) this.getControl("CheckBtn", TopButton.class);
        RefuseBtn = (TopButton) this.getControl("RefuseBtn", TopButton.class);
        resultDs = (ViewDataset) this.getDataset("resultDs");
        resultForm_subWindow1 = (TopForm) this.getControl("resultForm_subWindow1", TopForm.class);
        segmt_flg = (TopComboBox) resultForm_subWindow1.getField("SEGMT_FLG");
        segmtTb = (TopDataTable) this.getControl("SEGMTTb", TopDataTable.class);
        feeType0 = (TopComboBox) resultForm_subWindow1.getField("FEE_TYPE");
        fixAmtEditor0 = (TopMoneyEditor) resultForm_subWindow1.getField("FIX_AMT");
        rateEditor0 = (TopTextEditor) resultForm_subWindow1.getField("RATE");
        lowAmtEditor0 = (TopMoneyEditor) resultForm_subWindow1.getField("FEE_MIN");
        highAmtEditor0 = (TopMoneyEditor) resultForm_subWindow1.getField("FEE_MAX");
    }

    protected void bindEvent() {
        searchBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent event) {
                getCommand("queryCmd").execute();
                if (resultDs.getTotalCount() > 0) {
                    detailBtn.setEnabled(true);
                } else {
                    detailBtn.setEnabled(false);
                }
            }
        });
        CheckBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1783588082819362165L;

            public void buttonClick(ClickEvent event) {
                if (getCommand("checkCmd").execute().get("checkCmdItem").hasException() == false) {
                    getControl("subWindow", TopWindow.class).close();
                    getCommand("queryCmd").setShowMessageOnScreen(false);
                    searchBtn.click();
                    getCommand("queryCmd").setShowMessageOnScreen(true);
                }
            }
        });
        RefuseBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1783588082819362165L;

            public void buttonClick(ClickEvent event) {
                if (getCommand("refuseCmd").execute().get("refuseCmdItem").hasException() == false) {
                    getControl("subWindow", TopWindow.class).close();
                    getCommand("queryCmd").setShowMessageOnScreen(false);
                    searchBtn.click();
                    getCommand("queryCmd").setShowMessageOnScreen(true);
                }
            }
        });
        closeBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1783588082819362165L;

            public void buttonClick(ClickEvent event) {
                getControl("subWindow", TopWindow.class).close();
            }
        });
        detailBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1783588082819362165L;

            public void buttonClick(ClickEvent event) {
                getCommand("detailCmd").execute();
                getControl("subWindow", TopWindow.class).show(getMainWindow());
                resultForm_subWindow1.setReadOnly(true);
                String feeType = (String) resultDs.getCurrentValue("FEE_TYPE");
                String value_segmt_flg = (String) resultDs.getCurrentValue("SEGMT_FLG");
                if ("2".equals(value_segmt_flg)) {
                    resultForm_subWindow1.setFieldVisible("FEE_TYPE", false);
                    resultForm_subWindow1.setFieldVisible("FIX_AMT", false);
                    resultForm_subWindow1.setFieldVisible("RATE", false);
                    resultForm_subWindow1.setFieldVisible("FEE_MIN", false);
                    resultForm_subWindow1.setFieldVisible("FEE_MAX", false);
                } else {
                    resultForm_subWindow1.setFieldVisible("FEE_TYPE", true);
                    resultForm_subWindow1.setFieldVisible("FIX_AMT", true);
                    resultForm_subWindow1.setFieldVisible("RATE", true);
                    resultForm_subWindow1.setFieldVisible("FEE_MIN", true);
                    resultForm_subWindow1.setFieldVisible("FEE_MAX", true);
                }
                if ("1".equals(feeType)) {
                    if (!"2".equals(value_segmt_flg)) {
                        resultForm_subWindow1.setFieldVisible("FIX_AMT", true);
                        resultForm_subWindow1.setFieldVisible("FEE_MIN", false);
                        resultForm_subWindow1.setFieldVisible("FEE_MAX", false);
                        resultForm_subWindow1.setFieldVisible("RATE", false);
                    } else {
                        resultForm_subWindow1.setFieldVisible("FIX_AMT", false);
                        resultForm_subWindow1.setFieldVisible("FEE_MIN", false);
                        resultForm_subWindow1.setFieldVisible("FEE_MAX", false);
                        resultForm_subWindow1.setFieldVisible("RATE", false);
                    }
                } else {
                    if (!"2".equals(value_segmt_flg)) {
                        resultForm_subWindow1.setFieldVisible("FEE_MIN", true);
                        resultForm_subWindow1.setFieldVisible("FEE_MAX", true);
                        resultForm_subWindow1.setFieldVisible("RATE", true);
                        resultForm_subWindow1.setFieldVisible("FIX_AMT", false);
                    } else {
                        resultForm_subWindow1.setFieldVisible("FIX_AMT", false);
                        resultForm_subWindow1.setFieldVisible("FEE_MIN", false);
                        resultForm_subWindow1.setFieldVisible("FEE_MAX", false);
                        resultForm_subWindow1.setFieldVisible("RATE", false);
                    }
                }
            }
        });
        segmt_flg.addListener(new TopComboBox.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            public void valueChange(ValueChangeEvent event) {
                String segmtFlgTmp = (String) event.getProperty().getValue();
                if ("2".equals(segmtFlgTmp)) {
                    segmtTb.setVisible(true);
                } else {
                    segmtTb.setVisible(false);
                }
            }
        });
    }
}
