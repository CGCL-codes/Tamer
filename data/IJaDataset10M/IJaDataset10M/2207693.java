package bank.cnaps2.common.pageview;

import gneit.topbase.api.core.serivce.callback.Callback;
import gneit.topface.api.ui.config.ViewConfig;
import gneit.topface.core.data.ViewDataset;
import gneit.topface.core.data.command.DatasetCommand;
import gneit.topface.ui.component.TopButton;
import gneit.topface.ui.component.TopNotificationManager;
import gneit.topface.ui.component.TopWindow;
import gneit.topface.ui.component.ref.TopCustomComponent;
import java.util.Map;
import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;

public class OriTxnMesg_beps extends TopCustomComponent {

    public OriTxnMesg_beps(ViewConfig viewConfig) {
        super(viewConfig);
    }

    @Override
    public Map<String, Object> getCallbackData() {
        return null;
    }

    @Override
    protected void onLoad(Map<String, Object> paramSet) {
        ViewDataset resultDs = this.getDataset("resultDataset");
        Item resultDsCurrent = resultDs.getCurrent();
        resultDsCurrent.getItemProperty("ID").setValue(paramSet.get("ORI_ID"));
        getCommand("queryBeps").execute();
        super.onLoad(paramSet);
    }

    protected void bindEvent() {
        final DatasetCommand commandshow = this.getCommand("oriTxnMesgRefCommand");
        TopButton closeBtn = (TopButton) this.getControl("CloseBtn", TopButton.class);
        closeBtn.addListener(new TopButton.ClickListener() {

            public void buttonClick(ClickEvent clickevent) {
                OriTxnMesg_beps.this.getParentWindow().close();
            }
        });
        TopButton oriBizButton = (TopButton) this.getControl("oriBizButton", TopButton.class);
        oriBizButton.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent clickevent) {
                String ID = (String) getDataset("resultDataset").getCurrent().getItemProperty("ID").getValue();
                if (ID != null) {
                    commandshow.execute();
                    getControl("detailWindow", TopWindow.class).show(getApplication().getMainWindow());
                }
            }
        });
        TopButton CloseBtn = (TopButton) this.getControl("close", TopButton.class);
        CloseBtn.addListener(new TopButton.ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent clickevent) {
                getControl("detailWindow", TopWindow.class).close();
            }
        });
    }
}
