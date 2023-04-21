package org.netbeans.modules.flexbean.palette.components;

import javax.swing.text.JTextComponent;
import org.netbeans.modules.flexbean.palette.AbstractFlexPaletteComponent;

/**
 *
 * @author davisal
 */
public class Label extends AbstractFlexPaletteComponent {

    @Override
    public boolean isInLineTag() {
        return true;
    }

    @Override
    public String getName() {
        return "Label";
    }

    @Override
    protected boolean showCustomizer(JTextComponent targetComponent) {
        return true;
    }
}
