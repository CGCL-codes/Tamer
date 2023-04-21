package org.jdna.bmt.web.client.ui.prefs;

import org.jdna.bmt.web.client.Application;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RefreshPanel extends Composite {

    private final PreferencesServiceAsync preferencesService = GWT.create(PreferencesService.class);

    private static RefreshPanelUiBinder uiBinder = GWT.create(RefreshPanelUiBinder.class);

    interface RefreshPanelUiBinder extends UiBinder<Widget, RefreshPanel> {
    }

    @UiField
    VerticalPanel panel;

    public RefreshPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("refreshVFS")
    public void onRefreshVFS(ClickEvent evt) {
        reload(PreferencesService.REFRESH_VFS);
    }

    @UiHandler("refreshMenus")
    public void onRefreshMenus(ClickEvent evt) {
        reload(PreferencesService.REFRESH_MENUS);
    }

    @UiHandler("refreshMediaTitles")
    public void onRefreshMediaTitles(ClickEvent evt) {
        reload(PreferencesService.REFRESH_MEDIA_TITLES);
    }

    @UiHandler("refreshImageCache")
    public void onRefreshImageCache(ClickEvent evt) {
        reload(PreferencesService.REFRESH_IMAGE_CACHE);
    }

    private void reload(final String id) {
        preferencesService.refreshConfiguration(id, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                Application.fireNotification(Application.messages().configurationReloaded(id));
            }

            @Override
            public void onFailure(Throwable caught) {
                Application.fireErrorEvent(Application.messages().configurationFailedToReload(id));
            }
        });
    }
}
