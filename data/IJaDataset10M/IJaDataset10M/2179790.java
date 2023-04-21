package org.vmasterdiff.gui.filediff.api;

import java.util.logging.Logger;

public class FileDiffFactory {

    private static final Logger log = Logger.getLogger(FileDiffFactory.class.getName());

    private static String pack = "org.vmasterdiff.gui.filediff.";

    private static FileDiffFactory singleton = new FileDiffFactory();

    private FileDiffFactory() {
    }

    public static FileDiffFactory singleton() {
        return singleton;
    }

    /**
     * FILL IN JAVADOC HERE
     * @return
     */
    public FileDiffController createFileDiffController() {
        String className = System.getProperty(pack + "api.FileDiffInterface");
        if (className == null) className = pack + "impl.DefaultFileDiffController";
        FileDiffController controller;
        try {
            Class theClass = Class.forName(className);
            controller = (FileDiffController) theClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("bug", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("bug", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("bug", e);
        }
        return controller;
    }

    /**
     * FILL IN JAVADOC HERE
     * @return
     */
    public DiffBarController createDiffBarController() {
        String className = System.getProperty(pack + "api.DiffBarInterface");
        if (className == null) className = pack + "toolbar.DefaultDiffBarController";
        DiffBarController controller;
        try {
            Class theClass = Class.forName(className);
            controller = (DiffBarController) theClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("bug", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("bug", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("bug", e);
        }
        return controller;
    }
}
