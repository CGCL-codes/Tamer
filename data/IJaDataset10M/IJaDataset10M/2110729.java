package bank.cnaps2.manager.pageview;

import gneit.topbase.security.context.UserContextHolder;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.ControlBind;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.ViewDataset;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopConfirmWindow;
import gneit.topface.ui.component.TopDataTable;
import gneit.topface.ui.component.TopForm;
import gneit.topface.ui.view.TopPageView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button.ClickEvent;

@PageViewConfiguration(path = "classpath:bank/cnaps2/manager/pageview/Page0015.view")
public class Page0015 extends TopPageView {

    private static final Log logger = LogFactory.getLog(Page0015.class);

    private TopButton queryBtn;

    @ControlBind
    private TopButton logoutBtn;

    private ViewDataset queryDs;

    private ViewDataset resultDs;

    private TopDataTable resultTable;

    public Page0015(ViewConfig viewConfig) {
        super(viewConfig);
    }

    @Override
    protected void afterInitControls() {
        super.afterInitControls();
        queryBtn = this.getControl("queryBtn", TopButton.class);
        logoutBtn = this.getControl("logoutBtn", TopButton.class);
        queryDs = this.getDataset("queryDs");
        resultDs = this.getDataset("resultDs");
        resultTable = this.getControl("resultTable", TopDataTable.class);
    }

    @Override
    protected void bindEvent() {
        queryBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent clickevent) {
                String user = (String) queryDs.getCurrentValue("TLRNO");
                if (user != null) {
                    String low_user = user.toLowerCase();
                    queryDs.setCurrentValue("TLRNO", low_user);
                    System.out.println("tolower:" + (String) queryDs.getCurrentValue("TLRNO"));
                }
                String cp2_brno = (String) UserContextHolder.getContext().getSys("cp2_brno");
                queryDs.setCurrentValue("BRNO", cp2_brno);
                if (getCommand("queryCmd").execute().get("queryCmdItem").hasException() == false) {
                    if (getDataset("resultDs").getTotalCount() > 0) {
                        String STATUS = (String) resultDs.getCurrentValue("STATUS");
                        String USER_NAME = (String) resultDs.getCurrentValue("USER_NAME");
                        getDataset("logoutDs").setCurrentValue("TLRNO", USER_NAME);
                        if ("1".equals(STATUS)) {
                            logoutBtn.setEnabled(true);
                        } else {
                            logoutBtn.setEnabled(false);
                        }
                    }
                }
            }
        });
        resultTable.addListener(new ItemClickEvent.ItemClickListener() {

            private static final long serialVersionUID = 1L;

            public void itemClick(ItemClickEvent event) {
                String STATUS = (String) resultDs.getCurrentValue("STATUS");
                String USER_NAME = (String) resultDs.getCurrentValue("USER_NAME");
                getDataset("logoutDs").setCurrentValue("TLRNO", USER_NAME);
                if ("1".equals(STATUS)) {
                    logoutBtn.setEnabled(true);
                } else {
                    logoutBtn.setEnabled(false);
                }
            }
        });
        logoutBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent event) {
                TopConfirmWindow logoutfirm = new TopConfirmWindow("", "<p>是否强制签退该操作员</p><br/>", new TopConfirmWindow.Callback() {

                    private static final long serialVersionUID = 1L;

                    public boolean onCancel() {
                        return true;
                    }

                    public boolean onOk() {
                        if (getCommand("logoutCmd").execute().get("logoutCmdItem").hasException() == false) {
                            getCommand("queryCmd").setShowMessageOnScreen(false);
                            queryBtn.click();
                            getCommand("queryCmd").setShowMessageOnScreen(true);
                        }
                        return true;
                    }
                });
                logoutfirm.show(getMainWindow());
            }
        });
    }
}
