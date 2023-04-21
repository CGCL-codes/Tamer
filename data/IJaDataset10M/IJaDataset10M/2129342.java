package gemini.pollux.ui.client.page.content.marketingplan.level;

import java.util.ArrayList;
import java.util.List;
import gemini.basic.model.Level;
import gemini.pollux.ui.client.ServiceCatalog;
import gemini.pollux.ui.client.mvp.callback.AbstractAsyncCallback;
import gemini.pollux.ui.client.mvp.container.PolluxPlaceContainer;
import gemini.pollux.ui.client.mvp.container.PolluxPlaceContainer.PresenterGetter;
import gemini.pollux.ui.client.mvp.container.PolluxWidgetContainerDisplay;
import gemini.pollux.ui.client.mvp.container.PolluxWidgetContainerPresenter;
import gemini.pollux.ui.client.mvp.gin.PolluxGinjector;
import gemini.pollux.ui.client.page.RootEvent;
import gemini.pollux.ui.client.utils.UIConstants;
import ch.elca.gwt.mvp.client.EventBus;
import ch.elca.gwt.mvp.client.place.Place;
import ch.elca.gwt.mvp.client.place.PlaceRequest;
import com.google.inject.Inject;

public class LevelPresenter extends PolluxWidgetContainerPresenter<LevelPresenter.Display> {

    public interface Display extends PolluxWidgetContainerDisplay {
    }

    public static final Place PLACE = new PolluxPlaceContainer<LevelPresenter>(UIConstants.PLACE_LEVEL, new PresenterGetter<LevelPresenter>() {

        public LevelPresenter getPresenter() {
            return PolluxGinjector.INSTANCE.getLevelPresenter();
        }
    });

    @Inject
    public LevelPresenter(Display display, EventBus eventBus) {
        super(display, eventBus);
        addPresenter(PolluxGinjector.INSTANCE.getLevelListPresenter());
        addPresenter(PolluxGinjector.INSTANCE.getLevelEditPresenter());
    }

    @Override
    protected void onBind() {
        super.onBind();
        registerHandlerRegister();
        loadLevels(null);
    }

    private List<Level> levels;

    private void loadLevels(final Boolean edit) {
        eventBus.fireEvent(new RootEvent(RootEvent.ForwardType.MASK));
        ServiceCatalog.getLevelService().getAllLevels(new AbstractAsyncCallback<ArrayList<Level>>() {

            @Override
            public void onSuccess(ArrayList<Level> result) {
                eventBus.fireEvent(new RootEvent(RootEvent.ForwardType.UN_MASK));
                levels = sortLevels(result);
                LevelObject lvObject = new LevelObject();
                lvObject.setLevels(levels);
                if (edit == null) {
                    showPresenter(PolluxGinjector.INSTANCE.getLevelListPresenter(), lvObject);
                } else if (edit) {
                    lvObject.setLevel((Level) getCurrentPresenter().getOutputObject());
                    showPresenter(PolluxGinjector.INSTANCE.getLevelEditPresenter(), lvObject);
                } else {
                    showPresenter(PolluxGinjector.INSTANCE.getLevelEditPresenter(), lvObject);
                }
            }
        });
    }

    private void registerHandlerRegister() {
        registerHandler(getLocalEventBus().addHandler(LevelEvent.getType(), new LevelHandler() {

            @Override
            public void onBackToList(LevelEvent homeEvent) {
                loadLevels(null);
            }

            @Override
            public void onEdit(LevelEvent homeEvent) {
                loadLevels(true);
            }

            @Override
            public void onNew(LevelEvent levelEvent) {
                loadLevels(false);
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

    @Override
    public void onUnbind() {
        levels = null;
    }
}
