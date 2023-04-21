package gogo.rzgw.client.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

class Photo extends Composite {

    private VerticalPanel panel = new VerticalPanel();

    Image image = new Image();

    public Photo() {
        panel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        panel.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
        image.setUrl("http://10.1.9.23/rzgw/showImage?id=7&size=normal");
        panel.add(image);
        panel.setStyleName("panel-photo");
        initWidget(panel);
    }

    public void setURL(String url) {
        image.setUrl(url);
    }
}
