package net.sourceforge.chimeralibrary.ui.ioc.factorybean;

import javax.swing.JLabel;
import net.sourceforge.chimeralibrary.oxm.annotation.Name;

/**
 * JLabel factory bean.
 * 
 * @author Christian Cruz
 * @version 1.0.000
 * @since 1.0.000
 */
@Name("jlabel")
public class JLabelFactoryBean extends JComponentFactoryBean<JLabel> {

    private String text;

    public JLabelFactoryBean() {
        super(new JLabel());
    }

    @Override
    public void createObject() {
        if (text != null) {
            object.setText(text);
        }
        super.createObject();
    }

    /**
	 * Returns the text for the label.
	 * 
	 * @return the text for the label
	 */
    public String getText() {
        return text;
    }

    /**
	 * Sets the text for the label.
	 * 
	 * @param text the text for the label
	 */
    public void setText(final String text) {
        this.text = text;
    }
}
