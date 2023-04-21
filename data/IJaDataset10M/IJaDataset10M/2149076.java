package ch.odi.justblog.gui;

import ch.odi.justblog.gui.swing.SwingUi;

/**
 * Creates Ui instances.
 *
 * @author oglueck
 */
public class UiFactory {

    public static final int SWING = 1;

    public static final int TEST = 2;

    public static final int CLI = 3;

    /**
     * 
     */
    private UiFactory() {
    }

    /**
     * Valid Ui names are
     * 
     * @param name
     * @return
     */
    public static Ui getUi(int ui) {
        switch(ui) {
            case SWING:
                return new SwingUi();
            case TEST:
                Class clazz;
                try {
                    clazz = Class.forName("ch.odi.justblog.gui.test.TestUi");
                    return (Ui) clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Can not find Test UI");
                }
            default:
                return null;
        }
    }
}
