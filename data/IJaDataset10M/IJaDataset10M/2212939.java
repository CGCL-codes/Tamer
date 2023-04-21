package au.com.uptick.serendipity.client.widgets;

import com.allen_sauer.gwt.log.client.Log;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;

public class Masthead extends HLayout {

    private static final int MASTHEAD_HEIGHT = 58;

    private static final int IMAGE_SIZE = 48;

    private static final String WEST_WIDTH = "50%";

    private static final String EAST_WIDTH = "50%";

    private static final String LOGO = "logo.png";

    private static final String NAME_LABEL = "Serendipity";

    private static final String SIGNED_IN_USER_LABEL = "<b>Rob Ferguson</b><br />upTick";

    public Masthead() {
        super();
        Log.debug("Masthead()");
        this.setStyleName("crm-Masthead");
        this.setHeight(MASTHEAD_HEIGHT);
        Img logo = new Img(LOGO, IMAGE_SIZE, IMAGE_SIZE);
        logo.setStyleName("crm-Masthead-Logo");
        Label name = new Label();
        name.setStyleName("crm-Masthead-Name");
        name.setContents(NAME_LABEL);
        HLayout westLayout = new HLayout();
        westLayout.setHeight(MASTHEAD_HEIGHT);
        westLayout.setWidth(WEST_WIDTH);
        westLayout.addMember(logo);
        westLayout.addMember(name);
        Label signedInUser = new Label();
        signedInUser.setStyleName("crm-Masthead-SignedInUser");
        signedInUser.setContents(SIGNED_IN_USER_LABEL);
        HLayout eastLayout = new HLayout();
        eastLayout.setAlign(Alignment.RIGHT);
        eastLayout.setHeight(MASTHEAD_HEIGHT);
        eastLayout.setWidth(EAST_WIDTH);
        eastLayout.addMember(signedInUser);
        this.addMember(westLayout);
        this.addMember(eastLayout);
    }
}
