package pikes.peak.test;

import java.util.HashMap;
import java.util.Map;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;

public class MockThemeSource implements ThemeSource {

    private Map<String, Theme> themeMap = new HashMap<String, Theme>();

    public void addTheme(Theme theme) {
        themeMap.put(theme.getName(), theme);
    }

    public Theme getTheme(String name) {
        return themeMap.get(name);
    }
}
