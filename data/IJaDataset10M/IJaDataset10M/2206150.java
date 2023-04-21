package com.sin.client.ui.central;

import java.util.ArrayList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.sin.client.domains.Videos;
import com.sin.client.timers.CentralVideosTimer;
import com.sin.client.ui.central.CentralVideosView.Presenter;

public class VideoBoxImpl extends Composite implements VideoBox {

    private static VideoBoxUiBinder uiBinder = GWT.create(VideoBoxUiBinder.class);

    private Presenter presenter;

    private FlowPanel flowPanel;

    private ArrayList<Videos> videosarr = new ArrayList<Videos>();

    private String link;

    private String imglink;

    private String description;

    interface VideoBoxUiBinder extends UiBinder<Widget, VideoBoxImpl> {
    }

    @UiField
    DecoratorPanel decoratorPanel;

    @UiField
    VerticalPanel verticalPanel;

    @UiField
    Image image;

    @UiField
    HTML html;

    public VideoBoxImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        image.setHeight("90px");
        image.setWidth("120px");
        verticalPanel.add(image);
        verticalPanel.add(html);
        decoratorPanel.add(verticalPanel);
    }

    @UiHandler("image")
    void onClick(ClickEvent e) {
        Videos videos = new Videos(link, imglink, description);
        videosarr.add(videos);
        presenter.setVideoOnPlayer(videosarr);
    }

    @Override
    public void setVideoBox(String link, String imglink, String description, Presenter presenter, FlowPanel flowPanel) {
        this.link = link;
        this.imglink = imglink;
        this.description = description;
        this.presenter = presenter;
        this.flowPanel = flowPanel;
        image.setUrl(imglink);
        html.setText(description);
    }
}
