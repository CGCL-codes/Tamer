package bank.cnaps2.beps.pageview;

import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.api.ui.view.PageViewConfiguration;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.ref.TopCustomComponent;
import java.util.Map;
import com.vaadin.ui.Button.ClickEvent;

@PageViewConfiguration(path = "classpath:bank/cnaps2/beps/pageview/Page6405.view")
public class Page6405 extends TopCustomComponent {

    private static final long serialVersionUID = 1L;

    private TopButton closeBtn;

    public Page6405(ViewConfig viewConfig) {
        super(viewConfig);
    }

    /**
	 * 初始化控件
	 */
    protected void afterInitControls() {
        super.afterInitControls();
        closeBtn = (TopButton) this.getControl("closeBtn", TopButton.class);
    }

    protected void bindEvent() {
        closeBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent clickevent) {
                Page6405.this.getParentWindow().close();
            }
        });
    }

    @Override
    protected void onLoad(Map<String, Object> paramSet) {
        this.getDataset("resultDataset").getCurrent().getItemProperty("ID").setValue(paramSet.get("ID"));
        getCommand("Query").execute();
        super.onLoad(paramSet);
    }

    @Override
    public Map<String, Object> getCallbackData() {
        return null;
    }
}
