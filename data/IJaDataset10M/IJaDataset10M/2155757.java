package com.pavco.caribbeanvisit.client.view;

import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.pavco.caribbeanvisit.client.CaribbeanVisit;
import com.pavco.caribbeanvisit.client.CaribbeanvisitConstants;
import com.pavco.caribbeanvisit.client.MyResources;
import com.pavco.caribbeanvisit.client.ui.CountryChooser;
import com.pavco.caribbeanvisit.client.ui.GfcSignedInWidget;
import com.pavco.caribbeanvisit.client.ui.PavcoMap;
import com.pavco.caribbeanvisit.client.ui.ShowOptionsWidget;
import com.pavco.caribbeanvisit.client.presenter.MainPresenter;

public class MainView extends Composite implements MainPresenter.Display {

    HandlerManager eventBus;

    final CaribbeanvisitConstants constants = GWT.create(CaribbeanvisitConstants.class);

    private static MainViewUiBinder uiBinder = GWT.create(MainViewUiBinder.class);

    interface MainViewUiBinder extends UiBinder<Widget, MainView> {
    }

    @UiField
    Image headerImg;

    @UiField
    HTML footerText;

    @UiField
    public FlowPanel viewerFriendsPanel;

    @UiField
    public FlowPanel siteFriendsPanel;

    @UiField
    PavcoMap map;

    @UiField
    VerticalPanel searchResults;

    @UiField
    CountryChooser cc;

    @UiField
    ShowOptionsWidget so;

    @UiFactory
    CountryChooser makeCountryChooserWidget() {
        return new CountryChooser();
    }

    @UiFactory
    ShowOptionsWidget makeShowOptionsWidget() {
        return new ShowOptionsWidget(map, searchResults);
    }

    @UiFactory
    HTML createHtml() {
        return new HTML(constants.footerText());
    }

    @UiFactory
    VerticalPanel createPanel() {
        return new VerticalPanel();
    }

    @UiFactory
    HorizontalPanel createHorizontalPanel() {
        HorizontalPanel hp = new HorizontalPanel();
        hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        return hp;
    }

    @UiFactory
    PavcoMap createPavcoMap() {
        return map;
    }

    @UiFactory
    GfcSignedInWidget makeGfcSignedInWidget() {
        return new GfcSignedInWidget(this);
    }

    @UiFactory
    public static MyResources getMyResources() {
        return CaribbeanVisit.resources;
    }

    public MainView() {
        this.searchResults = new VerticalPanel();
        this.map = new PavcoMap();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public HasChangeHandlers getCountryListBox() {
        return cc.getListBox();
    }

    @Override
    public int getSelectedIndex(ChangeEvent event) {
        return cc.getListBox().getSelectedIndex();
    }

    @Override
    public HasClickHandlers getShowButton() {
        return this.so.getShowButton();
    }

    @Override
    public HasClickHandlers getClearButton() {
        return this.so.getClearButton();
    }

    @Override
    public HasValue<Boolean> getDispCountryMarkerCheckBox() {
        return this.so.getDispCountryMarkerCheckBox();
    }

    @Override
    public void setMapCentre(float longitude, float latitude) {
        map.setCenter(LatLng.newInstance(latitude, longitude));
    }

    @Override
    public void setCountries(String[] countryNames) {
        cc.setCountries(countryNames);
    }

    @Override
    public List<String> getSelectedCountries() {
        return this.so.getSelectedCountries();
    }

    @Override
    public List<String> getSelectedTags() {
        return this.so.getSelectedTags();
    }

    @Override
    public void setMapOverlays(Overlay[] countries) {
        for (int i = 0; i < countries.length; ++i) {
            this.map.addOverlay(countries[i]);
        }
    }

    public void setMapInfoWindow(InfoWindowContent iwc) {
    }

    public void setMapWidgetsVisibility(Overlay[] widgets, boolean on) {
        for (int i = 0; i < widgets.length; ++i) {
            if (on) {
                this.map.showOverlay(widgets[i]);
            } else {
                this.map.hideOverlay(widgets[i]);
            }
        }
    }

    @Override
    public PavcoMap getMap() {
        return this.map;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}
