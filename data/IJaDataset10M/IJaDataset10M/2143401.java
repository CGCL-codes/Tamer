package bank.cnaps2.beps.pageview;

import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.ref.TopCustomComponent;
import java.util.Map;
import com.vaadin.ui.Button.ClickEvent;

@PageViewConfiguration(path = "classpath:bank/cnaps2/beps/pageview/RefOfPage5222.view")
public class RefOfPage5222 extends TopCustomComponent {

    private TopButton closeBtn = null;

    public RefOfPage5222(ViewConfig viewConfig) {
        super(viewConfig);
    }

    protected void afterInitControls() {
        super.afterInitControls();
        closeBtn = (TopButton) this.getControl("closeBtn", TopButton.class);
    }

    protected void bindEvent() {
        closeBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                RefOfPage5222.this.getParentWindow().close();
            }
        });
    }

    @Override
    protected void onLoad(Map<String, Object> paramSet) {
        this.getDataset("replyDetailDs").setCurrentValue("ID", paramSet.get("RET_ID"));
        this.getCommand("queryForReplyDetailDsCmd").execute();
        this.getCommand("queryForPayerInfoDsCmd").execute();
        super.onLoad(paramSet);
    }

    @Override
    public Map<String, Object> getCallbackData() {
        return null;
    }
}
