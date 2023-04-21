package com.loribel.tools.editor.decorator;

import java.io.File;
import javax.swing.JButton;
import javax.swing.JComponent;
import com.loribel.commons.abstraction.GB_StringAction;
import com.loribel.commons.abstraction.GB_TextOwnerSet;
import com.loribel.tools.sa.action.GB_SAXmlFormat;
import com.loribel.tools.sa.button.GB_TButtonSA;

/**
 * Decorator of a TextComponent.
 *
 * @author Gr�gory Borelli
 */
public class GB_DecoratorLinkToFileXml extends GB_DecoratorLinkToFile {

    public GB_DecoratorLinkToFileXml(JComponent a_component, GB_TextOwnerSet a_textOwner, File a_file) {
        super(a_component, a_textOwner, a_file);
    }

    protected JComponent buildButtonsBar() {
        JButton l_button;
        JComponent retour = super.buildButtonsBar();
        GB_StringAction l_sa = new GB_SAXmlFormat();
        l_button = new GB_TButtonSA(l_sa, textOwner);
        retour.add(l_button);
        return retour;
    }
}
