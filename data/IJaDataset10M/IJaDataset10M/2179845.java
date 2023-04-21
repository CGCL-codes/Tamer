package htroot;

import java.net.URL;
import rjws.annotations.RJWSEntry;
import tests.utils.Utils;

public class RedirectPage {

    @RJWSEntry
    public Object entry() throws Exception {
        URL redirectUrl = new URL("http://" + Utils.HOST + ":" + Utils.PORT + "/pics/gentoo.png");
        return redirectUrl.openConnection().getInputStream();
    }
}
