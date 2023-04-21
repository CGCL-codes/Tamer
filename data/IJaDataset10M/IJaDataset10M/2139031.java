package booksandfilms.client.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface GlobalResources extends ClientBundle {

    public static final GlobalResources RESOURCE = GWT.create(GlobalResources.class);

    @Source("propertyButton.png")
    ImageResource propertyButton();

    @Source("googlelogo.gif")
    ImageResource googleLogo();

    @Source("GlobalStyles.css")
    GlobalStyles css();
}
