package fr.itris.glips.extension.jwidget.radiobuttonbar.runtime;

import org.w3c.dom.*;
import fr.itris.glips.extension.jwidget.base.runtime.ButtonGroupRuntime;
import fr.itris.glips.extension.jwidget.radiobuttonbar.edition.*;
import fr.itris.glips.rtda.components.picture.*;

/**
 * the class handling a button jwidget for the runtime
 * @author ITRIS, Jordi SUC
 */
public class RadioButtonBarRuntime extends ButtonGroupRuntime {

    /**
	 * the jwidget id
	 */
    public static String jwidgetId = "RadioButtonBarWidget";

    /**
     * the constructor of the class
     * @param picture the svg picture
     * @param element the svg element defining the jwidget
     */
    public RadioButtonBarRuntime(SVGPicture picture, Element element) {
        super(picture, element);
    }

    /**
     * @return the jwidget edition class linked to this jwidget runtime class
     */
    public static Class<?> getEditionClass() {
        return RadioButtonBarEdition.class;
    }
}
