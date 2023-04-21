package bank.cnaps2.sysm.pageview;

import gneit.topbase.security.context.UserContextHolder;
import gneit.topbase.util.date.TopDateUtils;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.core.data.command.DatasetCommand;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.view.TopPageView;
import java.util.Date;
import com.vaadin.ui.Button.ClickEvent;

@PageViewConfiguration(path = "classpath:bank/cnaps2/sysm/pageview/Page0624.view")
public class Page0624 extends TopPageView {

    private static final long serialVersionUID = 1L;

    private TopButton queryBtn = null;

    private DatasetCommand queryForlistDsCmd;

    public Page0624(ViewConfig viewConfig) {
        super(viewConfig);
    }

    protected void afterInitControls() {
        super.afterInitControls();
        queryBtn = (TopButton) this.getControl("queryBtn", TopButton.class);
        queryForlistDsCmd = this.getCommand("queryForlistDsCmd");
    }

    protected void bindEvent() {
        queryBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 4614112061487744914L;

            public void buttonClick(ClickEvent event) {
                queryForlistDsCmd.execute();
            }
        });
    }

    @Override
    protected void afterBindApplication() {
        super.afterBindApplication();
        String date = (String) UserContextHolder.getContext().getSys("CPG_DATE");
        Date cpgDate = TopDateUtils.stringToDate(date);
        getDataset("dateDs").getCurrent().getItemProperty("SYSDATE").setValue(cpgDate);
        queryForlistDsCmd.execute();
    }
}
