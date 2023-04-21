package org.jdna.bmt.web.client.ui.browser;

import java.util.ArrayList;
import java.util.HashMap;
import org.jdna.bmt.web.client.Application;
import org.jdna.bmt.web.client.media.GWTMediaArt;
import org.jdna.bmt.web.client.media.GWTMediaFile;
import sagex.phoenix.metadata.MediaArtifactType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class FanartPanel extends Composite {

    private static FanartPanelUiBinder uiBinder = GWT.create(FanartPanelUiBinder.class);

    @UiField
    Image mainImage;

    @UiField
    Button btnMakeDefault;

    @UiField
    Button btnDelete;

    @UiField
    ScrollPanel scrollerImages;

    @UiField
    Label header;

    HorizontalPanel imagePanel = new HorizontalPanel();

    private FanartManagerPanel manager;

    private BrowsePanel controller;

    private GWTMediaFile mediaFile;

    private MediaArtifactType type;

    private FanartImage curImage;

    interface FanartPanelUiBinder extends UiBinder<Widget, FanartPanel> {
    }

    public FanartPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void init(FanartManagerPanel fanartManagerPanel, BrowsePanel controller, GWTMediaFile mf, MediaArtifactType type) {
        this.manager = fanartManagerPanel;
        this.controller = controller;
        this.mediaFile = mf;
        this.type = type;
        if (this.type == MediaArtifactType.POSTER) {
            header.setText("Posters");
        } else if (this.type == MediaArtifactType.BACKGROUND) {
            header.setText("Backgrounds");
        } else if (this.type == MediaArtifactType.BANNER) {
            header.setText("Banners");
        }
        imagePanel.setSpacing(5);
        scrollerImages.setWidget(imagePanel);
        scrollerImages.setWidth("500px");
        loadFanart(type);
    }

    public void loadFanart(final MediaArtifactType type) {
        GWT.log("Loading fanart for " + type);
        controller.loadFanart(mediaFile, type, new AsyncCallback<ArrayList<GWTMediaArt>>() {

            public void onSuccess(ArrayList<GWTMediaArt> result) {
                if (result == null || result.size() == 0) {
                    setVisible(false);
                    return;
                }
                updateDisplay(result);
            }

            public void onFailure(Throwable caught) {
                setVisible(false);
            }
        });
    }

    protected void updateDisplay(ArrayList<GWTMediaArt> result) {
        imagePanel.clear();
        for (GWTMediaArt ma : result) {
            imagePanel.add(new FanartImage(ma, this));
        }
        if (result.size() > 0) {
            FanartImage img = (FanartImage) imagePanel.getWidget(0);
            img.onClick(null);
        }
    }

    public void setImageUrl(String url) {
        mainImage.setUrl(url);
        if (type == MediaArtifactType.POSTER) {
            mainImage.setWidth("200px");
        } else {
            mainImage.setWidth("300px");
        }
    }

    /**
	 * @param curImage
	 *            the curImage to set
	 */
    public void setCurImage(FanartImage curImage) {
        this.curImage = curImage;
        setImageUrl(curImage.getMediaArt().getDisplayUrl());
    }

    @UiHandler("btnDelete")
    public void deleteImage(ClickEvent evt) {
        if (curImage != null) {
            if (Window.confirm("Press OK to delete this image.  This cannot be undone.")) {
                controller.getServices().deleteFanart(curImage.getMediaArt(), new AsyncCallback<Boolean>() {

                    public void onFailure(Throwable caught) {
                        Application.fireErrorEvent("Unable to delete image", caught);
                    }

                    public void onSuccess(Boolean result) {
                        if (Boolean.TRUE.equals(result)) {
                            removeCurrentImage();
                        } else {
                            Application.fireErrorEvent("Unable to delete image");
                        }
                    }
                });
            }
        }
    }

    protected void removeCurrentImage() {
        loadFanart(type);
        manager.updateFiles();
    }

    @UiHandler("btnMakeDefault")
    public void makeDefaultImage(ClickEvent evt) {
        if (curImage != null) {
            controller.getServices().makeDefaultFanart(mediaFile, type, curImage.getMediaArt(), new AsyncCallback<Void>() {

                public void onFailure(Throwable caught) {
                    Application.fireErrorEvent("Unable to make this the default iamge", caught);
                }

                public void onSuccess(Void result) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("file", mediaFile);
                    map.put("poster", curImage.getMediaArt());
                    controller.getMessageBus().postMessage(BrowsePanel.MSG_POSTER_UPDATED, map);
                }
            });
        }
    }

    /**
	 * @return the curImage
	 */
    public FanartImage getCurImage() {
        return curImage;
    }
}
