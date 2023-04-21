package org.devyant.magicbeans.ui.swing;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import org.devyant.magicbeans.conf.MagicConfiguration;
import org.devyant.magicbeans.ui.AbstractMagicContainer;

/**
 * SwingContainer is a <b>cool</b> class.
 * 
 * @author Filipe Tavares
 * @version $Revision: 1.6 $ ($Author: ftavares $)
 * @since 18/Abr/2005 20:01:28
 * @todo maybe remove status bar....
 * replace by a simple -> title + "*" -> only at nested containers...:(
 */
public class SwingContainer extends AbstractMagicContainer<JComponent> {

    /**
     * @see org.devyant.magicbeans.ui.AbstractMagicContainer#finalizeNestedComponent()
     */
    @Override
    protected void finalizeNestedComponent() {
        this.component.setBorder(new TitledBorder(null, this.getName(), TitledBorder.LEFT, TitledBorder.TOP));
    }

    /**
     * @see org.devyant.magicbeans.ui.AbstractMagicContainer#showMessage(java.lang.String)
     */
    @Override
    public void showMessage(final String name) {
        ((JLabel) this.status).setText(MagicConfiguration.resources.getMessage(name));
    }
}
