package au.com.uptick.serendipity.client.settings.presenter;

import au.com.uptick.serendipity.client.NameTokens;
import au.com.uptick.serendipity.client.presenter.MainPagePresenter;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class AdministrationPresenter extends Presenter<AdministrationPresenter.MyView, AdministrationPresenter.MyProxy> {

    private final PlaceManager placeManager;

    @ProxyCodeSplit
    @NameToken(NameTokens.administration)
    public interface MyProxy extends Proxy<AdministrationPresenter>, Place {
    }

    public interface MyView extends View {
    }

    @Inject
    public AdministrationPresenter(EventBus eventBus, MyView view, MyProxy proxy, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        Log.debug("onReveal() - " + NameTokens.administration);
        MainPagePresenter.getNavigationPaneHeader().setContextAreaHeaderLabelContents(NameTokens.administration);
        MainPagePresenter.getNavigationPane().selectRecord(NameTokens.administration);
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }
}
