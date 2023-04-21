package fr.itris.glips.extension.jwidget.checkbox.edition;

import java.awt.*;
import fr.itris.glips.extension.jwidget.base.edition.AbstractButtonEdition;
import fr.itris.glips.rtdaeditor.jwidget.*;

/**
 * the class of the widget of a button 
 * @author ITRIS, Jordi SUC
 */
public class CheckBoxEdition extends AbstractButtonEdition {

    /**
     * the constructor of the class
     * @param jwidgetManager the jwidget manager
     * @param mainFrame the main frame
     */
    public CheckBoxEdition(JWidgetManager jwidgetManager, Frame mainFrame) {
        super(jwidgetManager, mainFrame, CHECK_BOX, "CheckBoxWidget", 1);
    }
}
