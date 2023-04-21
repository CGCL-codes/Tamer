package ca.ericslandry.client.mvp.presenter;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.Place;
import net.customware.gwt.presenter.client.place.PlaceRequest;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

public abstract class BasePresenter<D extends BasePresenter.BaseDisplay> extends WidgetPresenter<D> {

    public interface BaseDisplay extends WidgetDisplay {
    }

    public static final String ID = "id";

    public static final String ACTION = "action";

    public static final String CREATE = "create";

    public static final String VIEW = "view";

    public static final String UPDATE = "update";

    public static final String SEARCH = "search";

    public static final String MY = "my";

    public static final String BROWSE = "browse";

    public static final String RATING = "rating";

    public static final String USER = "user";

    public BasePresenter(final D display, final EventBus eventBus) {
        super(display, eventBus);
    }

    @Override
    protected void onBind() {
    }

    @Override
    protected void onPlaceRequest(PlaceRequest request) {
    }

    @Override
    protected void onUnbind() {
    }

    @Override
    public void refreshDisplay() {
    }

    @Override
    public Place getPlace() {
        return null;
    }
}
