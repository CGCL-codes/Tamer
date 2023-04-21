package net.cygeek.tech.client.ui.form;

import net.cygeek.tech.client.ui.form.AbstractWindow;
import net.cygeek.tech.client.ui.form.AbstractForm;

/**
 * Author: Thilina Hasantha
 */
public class EmpBasicsalaryWindow extends AbstractWindow {

    public static int WIDTH = 370;

    public static int HEIGHT = 310;

    public static int MIN_WIDTH = 370;

    public static int MIN_HEIGHT = 310;

    public EmpBasicsalaryWindow(AbstractForm viewForm) {
        super("", WIDTH, HEIGHT, MIN_WIDTH, MIN_HEIGHT, viewForm);
    }

    public void show(int mode) {
        super.showSuper(mode);
        doLayout();
    }
}
