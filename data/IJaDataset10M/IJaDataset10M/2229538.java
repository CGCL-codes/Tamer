package pl.pyrkon.cm.client.utils.ui;

import static pl.pyrkon.cm.client.i18n.I18N.getCaptions;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationAcceleration;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;

public class LoadingPane extends Window {

    public LoadingPane() {
        super();
        setAnimateAcceleration(AnimationAcceleration.SMOOTH_START_END);
        setAutoCenter(true);
        setIsModal(true);
        setShowModalMask(false);
        setShowCloseButton(false);
        setShowMinimizeButton(false);
        String loadingText = getCaptions().loading();
        setTitle(loadingText);
        addItem(createLayout(loadingText));
        setSize("200px", "60px");
    }

    private Canvas createLayout(String loadingText) {
        Layout outer = new Layout();
        outer.setWidth100();
        outer.setHeight100();
        outer.setAlign(Alignment.CENTER);
        outer.setAlign(VerticalAlignment.CENTER);
        Layout layout = new HStack();
        layout.addMember(getImage());
        layout.addMember(new Label(loadingText));
        layout.setAlign(VerticalAlignment.CENTER);
        layout.setWidth100();
        layout.setHeight100();
        layout.setMembersMargin(10);
        outer.addMember(layout);
        return outer;
    }

    private Canvas getImage() {
        Img img = new Img("loading.gif");
        img.setSize("16px", "16px");
        img.setPosition(Positioning.RELATIVE);
        return img;
    }
}
