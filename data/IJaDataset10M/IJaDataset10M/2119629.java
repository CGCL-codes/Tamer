package com.google.code.sagetvaddons.sagealert.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author dbattams
 *
 */
public interface SageAlertClientBundle extends ClientBundle {

    public static final SageAlertClientBundle INSTANCE = GWT.create(SageAlertClientBundle.class);

    @Source("com/google/code/sagetvaddons/sagealert/client/resources/heart.png")
    public ImageResource getDonateImg();

    @Source("com/google/code/sagetvaddons/sagealert/client/resources/lock_open.png")
    public ImageResource getRegisterImg();

    @Source("com/google/code/sagetvaddons/sagealert/client/resources/help.png")
    public ImageResource getHelpImg();

    @Source("com/google/code/sagetvaddons/sagealert/client/resources/server_edit.png")
    public ImageResource getSettingsImg();

    @Source("com/google/code/sagetvaddons/sagealert/client/resources/email_edit.png")
    public ImageResource getEmailImg();

    @Source("com/google/code/sagetvaddons/sagealert/client/resources/cog_edit.png")
    public ImageResource getPrefsImg();

    @Source("com/google/code/sagetvaddons/sagealert/client/resources/font.png")
    public ImageResource getAboutImg();

    @Source("com/google/code/sagetvaddons/sagealert/client/resources/book_go.png")
    public ImageResource getDocsImg();

    @Source("com/google/code/sagetvaddons/sagealert/client/resources/group.png")
    public ImageResource getSupportImg();

    @Source("com/google/code/sagetvaddons/sagealert/client/resources/page_add.png")
    public ImageResource getTixImg();

    @Source("com/google/code/sagetvaddons/sagealert/client/resources/house_go.png")
    public ImageResource getHomeImg();

    @Source("com/google/code/sagetvaddons/sagealert/client/resources/information.png")
    public ImageResource getFaqImg();
}
