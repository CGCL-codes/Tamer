package examples.page;

import org.t2framework.t2.annotation.composite.GET;
import org.t2framework.t2.annotation.core.Ajax;
import org.t2framework.t2.annotation.core.Default;
import org.t2framework.t2.annotation.core.Page;
import org.t2framework.t2.annotation.core.RequestParam;
import org.t2framework.t2.navigation.Forward;
import org.t2framework.t2.navigation.Json;
import org.t2framework.t2.spi.Navigation;

@Page("location")
public class LocationPage {

    @Default
    public Navigation index() {
        return Forward.to("/jsp/location.jsp");
    }

    @GET
    @Ajax
    public Navigation execute(@RequestParam("lat") String latitude, @RequestParam("long") String longtitude, @RequestParam("accuracy") String accuracy) {
        return Json.convert(new String[] { latitude, longtitude, accuracy });
    }
}
