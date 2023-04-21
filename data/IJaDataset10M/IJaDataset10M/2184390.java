package theweb.i18n;

import java.util.ArrayList;
import java.util.List;

class Bundles {

    private List<Bundle> bundles = new ArrayList<Bundle>();

    public void addBundle(Bundle bundle) {
        bundles.add(bundle);
    }

    public String getProperty(String key) {
        for (Bundle bundle : bundles) {
            String value = bundle.getProperty(key);
            if (value != null) return value;
        }
        return null;
    }
}
