package bank.cnaps2.draft.pageview;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button.ClickEvent;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.ViewDataset;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopComboBox;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.view.TopPageView;

@PageViewConfiguration(path = "classpath:bank/cnaps2/draft/pageview/Page8205.view")
public class Page8205 extends TopPageView {

    private static final long serialVersionUID = -1113943238104724529L;

    private TopButton modfyBtn;

    private TopButton searchBtn;

    private TopButton checkBtn;

    private TopButton refuseBtn;

    private TopButton closeBtn;

    private TopForm detailForm;

    private ViewDataset detailDs;

    private TopDataTable topDataTb;

    private ViewDataset inputDs;

    private TopTextEditor payeeBrnoEditor;

    public Page8205(ViewConfig viewConfig) {
        super(viewConfig);
    }

    @Override
    protected void afterInitControls() {
        super.afterInitControls();
        modfyBtn = (TopButton) this.getControl("modfyBtn", TopButton.class);
        searchBtn = (TopButton) this.getControl("searchBtn", TopButton.class);
        checkBtn = (TopButton) this.getControl("checkBtn", TopButton.class);
        closeBtn = (TopButton) this.getControl("closeBtn", TopButton.class);
        refuseBtn = (TopButton) this.getControl("refuseBtn", TopButton.class);
        detailForm = (TopForm) this.getControl("detailForm", TopForm.class);
        detailDs = this.getDataset("detailDs");
        topDataTb = (TopDataTable) this.getControl("resultTb", TopDataTable.class);
        inputDs = this.getDataset("inputDs");
        payeeBrnoEditor = (TopTextEditor) this.getControl("queryForm", TopForm.class).getField("ISSUED_BRNO");
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        searchBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = -4113943038154724529L;

            public void buttonClick(ClickEvent clickevent) {
                getCommand("queryCmd").execute();
                if (getDataset("listDs").getTotalCount() != 0) {
                    modfyBtn.setEnabled(true);
                } else {
                    modfyBtn.setEnabled(false);
                }
            }
        });
        payeeBrnoEditor.addListener(new TopTextEditor.ClickListener() {

            private static final long serialVersionUID = 2939706857440078609L;

            public void onClick(gneit.topface.ui.component.TopTextEditor.ClickEvent event) {
                if (event.isDoubleClick()) getCommand("refCommand2").execute();
            }
        });
        modfyBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = -421294138154724528L;

            public void buttonClick(ClickEvent event) {
                Page8205.this.getDataset("queryForDetailDs").getCurrent().getItemProperty("ID").setValue((String) Page8205.this.getDataset("listDs").getCurrent().getItemProperty("ID").getValue());
                Page8205.this.getCommand("modfyCmd").execute();
                if ("1".equals((String) detailDs.getCurrent().getItemProperty("DRAFT_TYPE").getValue())) {
                    detailForm.setFieldVisible("AGENT_BRNO", false);
                    detailForm.setFieldVisible("AGENT_BRNAME", false);
                }
                Page8205.this.getControl("subWindow", TopWindow.class).show(Page8205.this.getMainWindow());
            }
        });
        topDataTb.addListener(new ItemClickEvent.ItemClickListener() {

            private static final long serialVersionUID = -4212943238154721526L;

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) modfyBtn.click();
            }
        });
        checkBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = -421200138154724528L;

            public void buttonClick(ClickEvent event) {
                inputDs.getCurrent().getItemProperty("ID").setValue(detailDs.getCurrent().getItemProperty("ID").getValue());
                inputDs.getCurrent().getItemProperty("OP_TYPE").setValue("00");
                if (Page8205.this.getCommand("checkCmd").execute().get("checkCmdItem").hasException() == false) {
                    closeBtn.click();
                    Page8205.this.getCommand("queryCmd").setShowMessageOnScreen(false);
                    searchBtn.click();
                    Page8205.this.getCommand("queryCmd").setShowMessageOnScreen(true);
                }
            }
        });
        refuseBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = -421200138154724528L;

            public void buttonClick(ClickEvent event) {
                inputDs.getCurrent().getItemProperty("ID").setValue(detailDs.getCurrent().getItemProperty("ID").getValue());
                inputDs.getCurrent().getItemProperty("OP_TYPE").setValue("01");
                if (Page8205.this.getCommand("checkCmd").execute().get("checkCmdItem").hasException() == false) {
                    closeBtn.click();
                    Page8205.this.getCommand("queryCmd").setShowMessageOnScreen(false);
                    searchBtn.click();
                    Page8205.this.getCommand("queryCmd").setShowMessageOnScreen(true);
                }
            }
        });
        closeBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = -4212223238154724526L;

            public void buttonClick(ClickEvent clickevent) {
                inputDs.clearCurrentRecordData();
                getControl("subWindow", TopWindow.class).close();
            }
        });
    }
}
