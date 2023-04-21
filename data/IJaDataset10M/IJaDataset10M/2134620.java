package net.sourceforge.cridmanager.options;

import org.apache.log4j.Logger;
import java.awt.Component;
import javax.swing.JTextField;

/**
 * Eine Option, die als Textfeld dargestellt wird.
 */
public class TextOption extends Option {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(TextOption.class);

    private JTextField ctrl;

    /**
	 * Erstellt eine neue Textbox-Option. Die Arbeit erledigt die abstrakte Basisklasse, da die
	 * TextOption keine Besonderheiten hat.
	 * 
	 * @param name Der Name der Option.
	 */
    public TextOption(String name) {
        super(name);
    }

    /**
	 * Setzt den Wert.
	 */
    public void setzeWert(Object value) {
        if (logger.isDebugEnabled()) {
            logger.debug("setzeWert(Object) - start");
        }
        super.setzeWert(value);
        if (ctrl != null) ctrl.setText(value.toString());
        if (logger.isDebugEnabled()) {
            logger.debug("setzeWert(Object) - end");
        }
    }

    /**
	 * Liefert das Control zu dieser Option.
	 * 
	 * @returns Ein <code>JTextfield</code> -Control.
	 */
    public Component gibControl() {
        if (logger.isDebugEnabled()) {
            logger.debug("gibControl() - start");
        }
        if (ctrl == null) {
            ctrl = new JTextField();
            setzeTooltip(ctrl);
            ctrl.setColumns(12);
            ctrl.setText(gibWert().toString());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("gibControl() - end");
        }
        return ctrl;
    }

    public void speichern() {
        if (logger.isDebugEnabled()) {
            logger.debug("speichern() - start");
        }
        setzeWert(ctrl.getText());
        super.speichern();
        if (logger.isDebugEnabled()) {
            logger.debug("speichern() - end");
        }
    }
}
