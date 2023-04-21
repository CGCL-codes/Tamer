package model.gl.control;

import model.gl.GLDrawer;

public class IViewManagerFactoryImpl implements IViewManagerFactory {

    private static IViewManagerFactoryImpl _instance = null;

    protected IViewManagerFactoryImpl() {
    }

    /**
     * @return The unique instance of this class.
     */
    public static IViewManagerFactoryImpl getInstance() {
        if (null == _instance) {
            _instance = new IViewManagerFactoryImpl();
        }
        return _instance;
    }

    public GLViewManager createProjectViewManager(GLDrawer drawer) {
        GLViewManager res = new GLProjectViewManager(drawer, true);
        res.setShadowSupport(true);
        return res;
    }

    public GLViewManager createFactoryViewManager(GLDrawer drawer) {
        GLViewManager res = new GLFactoryViewManager(drawer, true);
        res.setShadowSupport(true);
        return res;
    }

    public GLViewManager createTowerViewManager(GLDrawer drawer) {
        GLViewManager res = new GLTowerViewManager(drawer, true);
        res.setShadowSupport(true);
        return res;
    }
}
