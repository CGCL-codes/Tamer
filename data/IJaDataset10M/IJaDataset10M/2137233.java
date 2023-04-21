package app.ui;

import app.drawing.actions.IDrawingManager;
import java.net.URLClassLoader;

/**
 *
 * @author Administrator
 */
public interface IAppContext {

    public IGUIManager getGUIManager();

    public IDrawingManager getDrawingManager();

    public URLClassLoader getClassLoader();

    public void setClassLoader(URLClassLoader classLoader);
}
