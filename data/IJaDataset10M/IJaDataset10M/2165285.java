package com.petersoft.advancedswing.resizabletextfield;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.text.Document;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class JResizableTextField extends JTextField implements FocusListener {

    public JResizableTextField() {
        super();
        initGUI();
    }

    public JResizableTextField(String text) {
        super(text);
        initGUI();
    }

    public JResizableTextField(int columns) {
        super(columns);
        initGUI();
    }

    public JResizableTextField(String text, int columns) {
        super(text, columns);
        initGUI();
    }

    public JResizableTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        initGUI();
    }

    private void initGUI() {
        try {
            {
                this.addFocusListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int originalHeight;

    int originalWidth;

    @Override
    public void focusGained(FocusEvent e) {
        System.out.println("focusGained");
        System.out.println(originalHeight);
        this.originalHeight = this.getMinimumSize().height;
        this.originalWidth = this.getWidth();
        this.setSize(new Dimension(300, 100));
        this.repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        System.out.println("focusLost");
        System.out.println(originalHeight);
        this.setSize(new Dimension(300, 25));
    }
}
