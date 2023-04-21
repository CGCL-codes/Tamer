package bank.cnaps2.beps.pageview;

import java.util.Date;
import bank.cnaps2.common.tools.Tools;
import com.vaadin.ui.Button.ClickEvent;
import gneit.topbase.security.context.UserContextHolder;
import gneit.topbase.util.date.TopDateUtils;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.ViewDataset;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopDateEditor;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopNotificationManager;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.view.TopPageView;

@PageViewConfiguration(path = "classpath:bank/cnaps2/beps/pageview/Page5222.view")
public class Page5222 extends TopPageView {

    private TopButton modifyBtn;

    private TopButton oriDetailBtn;

    private TopButton searchBtn;

    private TopForm queryForm;

    private TopDateEditor idateEditor;

    private TopTextEditor idSeqEditor;

    private ViewDataset queryDataset;

    public Page5222(ViewConfig viewConfig) {
        super(viewConfig);
    }

    protected void afterInitControls() {
        super.afterInitControls();
        searchBtn = this.getControl("searchBtn", TopButton.class);
        modifyBtn = this.getControl("modifyBtn", TopButton.class);
        oriDetailBtn = this.getControl("oriDetailBtn", TopButton.class);
        queryForm = (TopForm) this.getControl("queryForm", TopForm.class);
        idateEditor = (TopDateEditor) queryForm.getField("ID_DATE");
        idSeqEditor = (TopTextEditor) queryForm.getField("ID_SEQNO");
        queryDataset = Page5222.this.getDataset("queryDs");
        Date idDate = TopDateUtils.stringToDate(Tools.getCurrentStringDate());
        queryDataset.getCurrent().getItemProperty("ID_DATE").setValue(idDate);
    }

    @Override
    protected void bindEvent() {
        searchBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (Tools.checkWrokdate(Page5222.this, "STARTWORKDATE", "ENDWORKDATE", "queryDs") == -1) return;
                getCommand("queryForListDsCmd").execute();
                if (getDataset("listDs").getTotalCount() != 0) {
                    modifyBtn.setEnabled(true);
                    oriDetailBtn.setEnabled(true);
                } else {
                    modifyBtn.setEnabled(false);
                    oriDetailBtn.setEnabled(false);
                }
            }
        });
        modifyBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                getCommand("showReplyDetailRefCmd").execute();
            }
        });
        oriDetailBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (getDataset("listDs").getCurrentValue("DRCT").equals("1")) {
                    getCommand("showDetailRefCmd").execute();
                } else {
                    Tools.alertWindows(Page5222.this, "该笔交易没有原交易");
                }
            }
        });
        String strArray[] = { "ID_DATE", "ID_SEQNO", "ID_BEPS,BEPS" };
        idSeqEditor.addListener(Tools.addIDEvent(this, strArray, "queryDs"));
        idateEditor.addListener(Tools.addIDEvent(this, strArray, "queryDs"));
    }
}
