package ca.ericslandry.client.mvp.presenter;

import net.customware.gwt.presenter.client.EventBus;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class MainPresenter extends BasePresenter<MainPresenter.Display> {

    public interface Display extends BaseDisplay {

        void setHeader(Widget widget);

        void setContent(Widget widget);
    }

    private HeaderPresenter headerPresenter;

    private ContentPresenter contentPresenter;

    @SuppressWarnings("unused")
    private IssueListPresenter issueListPresenter;

    @Inject
    public MainPresenter(final Display display, final EventBus eventBus, HeaderPresenter headerPresenter, ContentPresenter contentPresenter, IssueListPresenter issueListPresenter) {
        super(display, eventBus);
        this.headerPresenter = headerPresenter;
        this.contentPresenter = contentPresenter;
        this.issueListPresenter = issueListPresenter;
    }

    @Override
    protected void onBind() {
        Log.debug("onBind: main");
        display.setHeader(headerPresenter.getDisplay().asWidget());
        display.setContent(contentPresenter.getDisplay().asWidget());
    }
}
