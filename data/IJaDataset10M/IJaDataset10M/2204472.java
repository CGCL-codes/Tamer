package bank.cnaps2.saps.pageview;

import gneit.topbase.security.context.UserContextHolder;
import gneit.topbase.util.date.TopDateUtils;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopDateEditor;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.component.TopNotificationManager;
import gneit.topface.ui.component.TopTextEditor;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.view.TopPageView;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bank.cnaps2.common.tools.Tools;
import bank.cnaps2.nets.pageview.Page7215;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button.ClickEvent;

@PageViewConfiguration(path = "classpath:bank/cnaps2/saps/pageview/Page7237.view")
public class Page7237 extends TopPageView {

    private static final Logger logger = LoggerFactory.getLogger(Page7237.class);

    private TopForm inputForm = null;

    private TopForm inputForm2 = null;

    private TopForm detailForm = null;

    private TopButton searchBtn = null;

    private TopButton closeBtn = null;

    private TopButton closeBtnForDetailListWindow = null;

    private TopDataTable topDataTable = null;

    private TopButton detailBtn = null;

    private TopDataTable detailTable = null;

    private TopButton detailListBtn = null;

    public Page7237(ViewConfig viewConfig) {
        super(viewConfig);
    }

    /**
	 * 为页面中声明的变量赋值
	 */
    protected void afterInitControls() {
        super.afterInitControls();
        inputForm = this.getControl("listDsForm", TopForm.class);
        inputForm2 = this.getControl("listDsForm2", TopForm.class);
        detailForm = this.getControl("listdetailDsForm", TopForm.class);
        searchBtn = (TopButton) this.getControl("SearchBtn", TopButton.class);
        closeBtn = (TopButton) this.getControl("CloseBtn", TopButton.class);
        topDataTable = this.getControl("resultTable", TopDataTable.class);
        detailBtn = (TopButton) this.getControl("DetailBtn", TopButton.class);
        detailTable = this.getControl("detailTable", TopDataTable.class);
        detailListBtn = (TopButton) this.getControl("detailListBtn", TopButton.class);
        closeBtnForDetailListWindow = (TopButton) this.getControl("CloseBtn2", TopButton.class);
    }

    protected void bindEvent() {
        searchBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 7728005894672941623L;

            public void buttonClick(ClickEvent clickevent) {
                if (Tools.checkWrokdate(Page7237.this, "CUST_WORKDATE", "CUST_WORKDATE2", "queryDs") == -1) return;
                Page7237.this.getCommand("queryForlistDsCmd").execute();
                if (getDataset("listDs").getTotalCount() != 0) Page7237.this.getControl("DetailBtn", TopButton.class).setEnabled(true); else {
                    Page7237.this.getControl("DetailBtn", TopButton.class).setEnabled(false);
                }
            }
        });
        topDataTable.addListener(new ItemClickEvent.ItemClickListener() {

            private static final long serialVersionUID = -5363232813107176744L;

            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    inputForm.setReadOnly(true);
                    inputForm2.setReadOnly(true);
                    Page7237.this.getCommand("queryFordetaillistDsCmd").execute();
                    getControl("detailWindow", TopWindow.class).show(getMainWindow());
                    if (getDataset("listdetailDs").getTotalCount() > 0) {
                        detailListBtn.setEnabled(true);
                    } else {
                        detailListBtn.setEnabled(false);
                    }
                }
            }
        });
        detailBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 7546231730602907531L;

            public void buttonClick(ClickEvent clickevent) {
                inputForm.setReadOnly(true);
                inputForm2.setReadOnly(true);
                Page7237.this.getCommand("queryFordetaillistDsCmd").execute();
                getControl("detailWindow", TopWindow.class).show(getMainWindow());
                if (getDataset("listdetailDs").getTotalCount() > 0) {
                    detailListBtn.setEnabled(true);
                } else {
                    detailListBtn.setEnabled(false);
                }
            }
        });
        detailListBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = -9116863721993031214L;

            public void buttonClick(ClickEvent clickevent) {
                Page7237.this.getCommand("queryFordetaillist2DsCmd").execute();
                getControl("detaillistWindow", TopWindow.class).show(getMainWindow());
            }
        });
        closeBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 399209529651712142L;

            public void buttonClick(ClickEvent clickevent) {
                getControl("detailWindow", TopWindow.class).close();
            }
        });
        closeBtnForDetailListWindow.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 8153097728164776324L;

            public void buttonClick(ClickEvent clickevent) {
                getControl("detaillistWindow", TopWindow.class).close();
            }
        });
        TopForm queryForm = (TopForm) this.getControl("queryForm", TopForm.class);
        Date IdDate = TopDateUtils.stringToDate(Tools.getCurrentStringDate());
        getDataset("queryDs").getCurrent().getItemProperty("ID_DATE").setValue(IdDate);
        TopTextEditor ID_SeqNo = (TopTextEditor) queryForm.getField("ID_SeqNo");
        String strArray[] = { "ID_DATE", "ID_SeqNo", "ID_SAPS,SAPS" };
        ID_SeqNo.addListener(Tools.addIDEvent(this, strArray, "queryDs"));
        TopDateEditor id_date = (TopDateEditor) queryForm.getField("ID_DATE");
        id_date.addListener(Tools.addIDEvent(this, strArray, "queryDs"));
        getDataset("queryDs").getCurrent().getItemProperty("WORKDATE").setValue(TopDateUtils.stringToDate((String) UserContextHolder.getContext().getSys("CPG_DATE")));
        getDataset("queryDs").getCurrent().getItemProperty("WORKDATE2").setValue(TopDateUtils.stringToDate((String) UserContextHolder.getContext().getSys("CPG_DATE")));
    }
}
