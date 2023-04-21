package bank.cnaps2.beps.pageview;

import java.util.Date;
import bank.cnaps2.common.tools.Tools;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import gneit.topbase.security.context.UserContextHolder;
import gneit.topbase.util.date.TopDateUtils;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.command.DatasetCommand;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopDateEditor;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopNotificationManager;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.view.TopPageView;

@PageViewConfiguration(path = "classpath:bank/cnaps2/beps/pageview/Page5219.view")
public class Page5219 extends TopPageView {

    private TopTextEditor idSeqEditor;

    private TopDateEditor idateEditor;

    private TopForm queryForm = null;

    private DatasetCommand queryForlistDsCmd;

    private TopButton detailBtn;

    private TopButton SearchBtn;

    private TopButton closeBtn;

    private TopDataTable topTable;

    public Page5219(ViewConfig viewConfig) {
        super(viewConfig);
    }

    public void alertWindows(String caption) {
        Window window = this.getApplication().getMainWindow();
        TopNotificationManager.alert(window, null, caption);
    }

    @Override
    protected void afterInitControls() {
        super.afterInitControls();
        queryForm = (TopForm) this.getControl("queryForm", TopForm.class);
        idSeqEditor = (TopTextEditor) queryForm.getField("ID_SeqNo");
        idateEditor = (TopDateEditor) queryForm.getField("ID_DATE");
        queryForlistDsCmd = this.getCommand("queryForlistDsCmd");
        detailBtn = (TopButton) this.getControl("ModfyBtn", TopButton.class);
        SearchBtn = (TopButton) this.getControl("SearchBtn", TopButton.class);
        topTable = (TopDataTable) this.getControl("resultTable", TopDataTable.class);
        closeBtn = (TopButton) this.getControl("CloseBtn", TopButton.class);
    }

    @Override
    protected void bindEvent() {
        SearchBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                if (Tools.checkWrokdate(Page5219.this, "WORKDATE", "WORKDATE2", "queryDs") == -1) return;
                queryForlistDsCmd.execute();
                if (getDataset("listDs").getTotalCount() > 0) {
                    detailBtn.setEnabled(true);
                } else {
                    detailBtn.setEnabled(false);
                }
            }
        });
        detailBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent event) {
                getControl("detailWindow", TopWindow.class).show(getMainWindow());
            }
        });
        topTable.addListener(new ItemClickEvent.ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    getControl("detailWindow", TopWindow.class).show(getMainWindow());
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
        String s[] = new String[] { "ID_DATE", "ID_SeqNo", "ID_BEPS,BEPS" };
        idSeqEditor.addListener(Tools.addIDEvent(this, s, "queryDs"));
        idateEditor.addListener(Tools.addIDEvent(this, s, "queryDs"));
    }
}
