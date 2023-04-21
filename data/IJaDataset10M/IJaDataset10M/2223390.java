package auo.cms.hsv.value.experiment;

import java.awt.*;
import javax.swing.*;
import shu.ui.GUIUtils;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class ValueVerifyFrame extends JFrame {

    protected BorderLayout borderLayout1 = new BorderLayout();

    public ValueVerifyFrame() {
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(800, 800);
        getContentPane().setLayout(borderLayout1);
    }

    public static void main(String[] args) {
        GUIUtils.runAsApplication(new ValueVerifyFrame(), false);
    }
}
