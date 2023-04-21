package javango.contrib.admin.tests;

import java.util.ArrayList;
import java.util.List;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javango.contrib.admin.Urls;
import javango.core.SubappUrl;
import javango.api.Url;

public class AdminTestUrls implements javango.api.Urls {

    List<Url> l = new ArrayList<Url>();

    @Inject
    public AdminTestUrls(Injector injector) {
        l.add(new SubappUrl(injector, "^admin/", Urls.class));
    }

    public List<Url> getUrlPatterns() {
        return l;
    }
}
