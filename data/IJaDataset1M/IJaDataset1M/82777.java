package model.gl.control;

import model.gl.GLDrawer;

public interface IViewManagerFactory {

    GLViewManager createProjectViewManager(GLDrawer drawer);

    GLViewManager createFactoryViewManager(GLDrawer drawer);

    GLViewManager createTowerViewManager(GLDrawer drawer);
}
