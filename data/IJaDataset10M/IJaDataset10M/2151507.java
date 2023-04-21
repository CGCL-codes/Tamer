package org.designerator.common.theme;

import java.util.ArrayList;
import java.util.List;
import org.designerator.common.interfaces.IActionAdapter;
import org.designerator.common.interfaces.IColorTheme;
import org.designerator.common.interfaces.IThemePainter;
import org.eclipse.core.commands.IHandler;

public class ThemeDelegate {

    private static ThemeDelegate instance;

    public static ThemeDelegate getInstance() {
        if (instance == null) {
            instance = new ThemeDelegate();
        }
        return instance;
    }

    private IColorTheme currentTheme;

    private List<IThemeListener> listeners = new ArrayList<IThemeListener>();

    private List<IActionAdapter> fullScreenActions = new ArrayList<IActionAdapter>();

    public List<IActionAdapter> getFullScreenActions() {
        return fullScreenActions;
    }

    private IThemePainter themePainter;

    private IHandler fullScreenHandler;

    public static final String THEME = "designerator.presentations.designtheme";

    private ThemeDelegate() {
    }

    public void addThemeListener(IThemeListener themeListener) {
        listeners.add(themeListener);
    }

    public void registerFullScreenAction(IActionAdapter action) {
        fullScreenActions.add(action);
    }

    public IColorTheme getTheme() {
        if (currentTheme == null) {
            return SystemTheme.createTheme();
        }
        return currentTheme;
    }

    public void removeThemeListener(IThemeListener themeListener) {
        listeners.remove(themeListener);
    }

    public void setTheme(IColorTheme theme) {
        currentTheme = theme;
        for (IThemeListener l : listeners) {
            l.themeChanged(currentTheme);
        }
    }

    public boolean isSystemTheme(IColorTheme theme) {
        return SystemTheme.ID.equals(theme.getId());
    }

    public void setThemePainter(IThemePainter themePainter) {
        this.themePainter = themePainter;
    }

    public IThemePainter getThemePainter() {
        return themePainter;
    }

    public void setFullScreenHandler(IHandler fullScreenHandler) {
        this.fullScreenHandler = fullScreenHandler;
    }

    public IHandler getFullScreenHandler() {
        return fullScreenHandler;
    }
}
