package gemini.castor.ui.client.page.content.office.news;

import gemini.castor.ui.client.mvp.container.CastorPlaceContainer;
import gemini.castor.ui.client.mvp.container.CastorPlaceContainer.PresenterGetter;
import gemini.castor.ui.client.mvp.container.CastorWidgetContainerDisplay;
import gemini.castor.ui.client.mvp.container.CastorWidgetContainerPresenter;
import gemini.castor.ui.client.mvp.gin.CastorGinjector;
import gemini.castor.ui.client.utils.PlaceConstants;
import ch.elca.gwt.mvp.client.EventBus;
import ch.elca.gwt.mvp.client.place.Place;
import ch.elca.gwt.mvp.client.place.PlaceRequest;
import com.google.inject.Inject;

public class NewsPresenter extends CastorWidgetContainerPresenter<NewsPresenter.Display> {

    public interface Display extends CastorWidgetContainerDisplay {
    }

    public static final Place PLACE = new CastorPlaceContainer<NewsPresenter>(PlaceConstants.PLACE_NEWS, new PresenterGetter<NewsPresenter>() {

        public NewsPresenter getPresenter() {
            return CastorGinjector.INSTANCE.getNewsPresenter();
        }
    });

    @Inject
    public NewsPresenter(Display display, EventBus eventBus) {
        super(display, eventBus);
        addPresenter(CastorGinjector.INSTANCE.getNewsFormPresenter());
    }

    @Override
    protected void onBind() {
        super.onBind();
        showDefaultPresenter();
    }

    @Override
    public Place getPlace() {
        return PLACE;
    }

    @Override
    protected void onPlaceRequest(PlaceRequest request) {
    }
}
