package com.giews.report.component;

import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.JRAlignment;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: ?
 * Date: ?
 * Time: ?
 * To change this template use File | Settings | File Templates.
 */
public class Title extends JRDesignStaticText {

    public Title() {
        setBackcolor(Color.white);
        byte b = 1;
        setMode(b);
        setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_CENTER);
        setVerticalAlignment(JRAlignment.VERTICAL_ALIGN_MIDDLE);
    }
}
