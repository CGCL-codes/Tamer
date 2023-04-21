package javango.contrib.compat.jquery;

import javango.api.Settings;
import javango.contrib.compat.Static;

public class Helper extends javango.contrib.jquery.Helper {

    public Helper(String basePath) {
        super(Static.getInjector().getInstance(Settings.class), basePath);
    }
}
