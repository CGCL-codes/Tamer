package de.matthiasmann.twl.theme;

import de.matthiasmann.twl.DebugHook;
import de.matthiasmann.twl.ThemeInfo;
import java.util.HashMap;

/**
 * The ThemeInfo implementation
 *
 * @author Matthias Mann
 */
class ThemeInfoImpl extends ParameterMapImpl implements ThemeInfo {

    private final String name;

    final HashMap<String, ThemeInfoImpl> children;

    boolean maybeUsedFromWildcard;

    String wildcardImportPath;

    public ThemeInfoImpl(ThemeManager manager, String name, ThemeInfoImpl parent) {
        super(manager, parent);
        this.name = name;
        this.children = new HashMap<String, ThemeInfoImpl>();
    }

    void copy(ThemeInfoImpl src) {
        children.putAll(src.children);
        params.putAll(src.params);
        wildcardImportPath = src.wildcardImportPath;
    }

    public String getName() {
        return name;
    }

    public ThemeInfo getChildTheme(String theme) {
        ThemeInfo info = children.get(theme);
        if (info == null) {
            if (wildcardImportPath != null) {
                info = manager.resolveWildcard(wildcardImportPath, theme);
            }
            if (info == null) {
                DebugHook.getDebugHook().missingChildTheme(this, theme);
            }
        }
        return info;
    }

    public String getThemePath() {
        return getThemePath(0).toString();
    }

    private StringBuilder getThemePath(int length) {
        StringBuilder sb;
        length += getName().length();
        if (parent != null) {
            sb = parent.getThemePath(length + 1);
            sb.append('.');
        } else {
            sb = new StringBuilder(length);
        }
        sb.append(getName());
        return sb;
    }
}
