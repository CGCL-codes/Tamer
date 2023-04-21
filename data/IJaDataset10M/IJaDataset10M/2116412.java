package com.pavco.caribbeanvisit.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface MyResources extends ClientBundle {

    public static final MyResources INSTANCE = GWT.create(MyResources.class);

    @Source("css/CaribbeanVisit.css")
    CaribbeanVisitCssResource caribbeanVisitCss();

    @Source("images/header-bkgd.png")
    ImageResource headerBkgd();

    @Source("images/loading.gif")
    ImageResource loading();

    @Source("images/star_blank.gif")
    ImageResource star_blank();

    @Source("images/star_y.gif")
    ImageResource star_y();

    @Source("images/imageNA_sm.gif")
    ImageResource naImageSm();
}
