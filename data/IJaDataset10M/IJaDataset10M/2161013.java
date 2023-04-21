package gemini.pollux.ui.client.page.content.product.producttype;

import gemini.basic.model.Level;
import gemini.pollux.ui.client.mvp.container.PolluxPlaceContainer;
import gemini.pollux.ui.client.mvp.container.PolluxPlaceContainer.PresenterGetter;
import gemini.pollux.ui.client.mvp.container.PolluxWidgetContainerDisplay;
import gemini.pollux.ui.client.mvp.container.PolluxWidgetContainerPresenter;
import gemini.pollux.ui.client.mvp.gin.PolluxGinjector;
import gemini.pollux.ui.client.utils.UIConstants;
import java.util.ArrayList;
import java.util.List;
import ch.elca.gwt.mvp.client.EventBus;
import ch.elca.gwt.mvp.client.place.Place;
import ch.elca.gwt.mvp.client.place.PlaceRequest;
import com.google.inject.Inject;

public class ProductTypePresenter extends PolluxWidgetContainerPresenter<ProductTypePresenter.Display> {

    public interface Display extends PolluxWidgetContainerDisplay {
    }

    public static final Place PLACE = new PolluxPlaceContainer<ProductTypePresenter>(UIConstants.PLACE_PRODUCT_TYPE, new PresenterGetter<ProductTypePresenter>() {

        public ProductTypePresenter getPresenter() {
            return PolluxGinjector.INSTANCE.getProductTypePresenter();
        }
    });

    @Inject
    public ProductTypePresenter(Display display, EventBus eventBus) {
        super(display, eventBus);
        addPresenter(PolluxGinjector.INSTANCE.getProductTypeListPresenter());
        addPresenter(PolluxGinjector.INSTANCE.getProductTypeEditPresenter());
    }

    @Override
    protected void onBind() {
        super.onBind();
        registerHandlerRegister();
        showDefaultPresenter();
    }

    private void registerHandlerRegister() {
        registerHandler(getLocalEventBus().addHandler(ProductTypeEvent.getType(), new ProductTypeHandler() {

            @Override
            public void onBackToList(ProductTypeEvent homeEvent) {
                showDefaultPresenter();
            }

            @Override
            public void onEdit(ProductTypeEvent homeEvent) {
                showPresenter(PolluxGinjector.INSTANCE.getProductTypeEditPresenter(), getCurrentPresenter().getOutputObject());
            }

            @Override
            public void onNew(ProductTypeEvent levelEvent) {
                showPresenter(PolluxGinjector.INSTANCE.getProductTypeEditPresenter());
            }
        }));
    }

    @Override
    public Place getPlace() {
        return PLACE;
    }

    @Override
    protected void onPlaceRequest(PlaceRequest request) {
    }

    public List<Level> sortLevels(List<Level> levels) {
        List<Level> sortedLevels = new ArrayList<Level>(levels.size());
        for (Level lv : levels) {
            if (lv.getLevel() == null) {
                sortedLevels.add(lv);
                break;
            }
        }
        boolean fulled = false;
        boolean found = false;
        if (sortedLevels.isEmpty()) {
            fulled = true;
        }
        while (!fulled) {
            for (Level lv : levels) {
                found = false;
                if (lv.getLevel() != null && lv.getLevel().getId() == sortedLevels.get(sortedLevels.size() - 1).getId()) {
                    sortedLevels.add(lv);
                    found = true;
                    break;
                }
            }
            if (!found) {
                fulled = true;
            }
        }
        return sortedLevels;
    }
}
