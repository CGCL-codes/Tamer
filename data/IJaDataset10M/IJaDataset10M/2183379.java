package de.flingelli.scrum.gui.options;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import de.flingelli.scrum.language.JastTranslation;
import de.flingelli.scrum.observer.ProductPropertyChangeSupport;

/**
 * 
 * @author Markus Flingelli
 * 
 */
@SuppressWarnings("serial")
public class ButtonPanel extends JPanel implements PropertyChangeListener {

    private JButton jOkButton = null;

    private JButton jApplyButton = null;

    private JButton jCancelButton = null;

    public ButtonPanel() {
        super();
        ProductPropertyChangeSupport.getInstance().addPropertyChangeListener(this);
        initialize();
        changeLanguage();
    }

    private void changeLanguage() {
        JastTranslation trans = JastTranslation.getInstance();
        String prefix = "de.flingelli.scrum.gui.options.ButtonPanel-";
        jOkButton.setText(trans.getValue(prefix + "Ok"));
        jApplyButton.setText(trans.getValue(prefix + "Apply"));
        jCancelButton.setText(trans.getValue(prefix + "Cancel"));
    }

    private void initialize() {
        this.setLayout(null);
        this.setBounds(new Rectangle(0, 0, 330, 30));
        this.add(getJOkButton());
        this.add(getApplyButton());
        this.add(getJCancelButton());
    }

    private JButton getJOkButton() {
        if (jOkButton == null) {
            jOkButton = new JButton();
            jOkButton.setName("options_ok_button");
            jOkButton.setBounds(new Rectangle(10, 5, 100, 20));
            jOkButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    firePropertyChange("options_ok", 0, 1);
                }
            });
        }
        return jOkButton;
    }

    private JButton getApplyButton() {
        if (jApplyButton == null) {
            jApplyButton = new JButton();
            jApplyButton.setName("options_apply_button");
            jApplyButton.setBounds(new Rectangle(115, 5, 100, 20));
            jApplyButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    firePropertyChange("options_apply", 0, 1);
                }
            });
        }
        return jApplyButton;
    }

    private JButton getJCancelButton() {
        if (jCancelButton == null) {
            jCancelButton = new JButton();
            jCancelButton.setName("options_cancel_button");
            jCancelButton.setBounds(new Rectangle(220, 5, 100, 20));
            jCancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    firePropertyChange("options_cancel", 0, 1);
                }
            });
        }
        return jCancelButton;
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("language_changed")) {
            changeLanguage();
        }
    }
}
