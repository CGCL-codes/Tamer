package hszt.tbd.pimp.gui.comp;

import java.awt.Rectangle;

public abstract class TextFieldDecorator implements TextFieldIfc {

    protected TextFieldIfc tf;

    public TextFieldDecorator(TextFieldIfc aTF) {
        tf = aTF;
    }

    public Rectangle getBounds() {
        return tf.getBounds();
    }

    public String getText() {
        return tf.getText();
    }

    public boolean isEditable() {
        return tf.isEditable();
    }

    public boolean isEnabled() {
        return tf.isEnabled();
    }

    public boolean isVisible() {
        return tf.isVisible();
    }

    public void setBounds(int aX, int aY, int aWidth, int aHeight) {
        tf.setBounds(aX, aY, aWidth, aHeight);
    }

    public void setBounds(Rectangle aR) {
        tf.setBounds(aR);
    }

    public void setEditable(boolean aB) {
        tf.setEditable(aB);
    }

    public void setEnabled(boolean aEnabled) {
        tf.setEnabled(aEnabled);
    }

    public void setText(String aT) {
        tf.setText(aT);
    }

    public void setToolTipText(String aText) {
        tf.setToolTipText(aText);
    }

    public void setVisible(boolean aFlag) {
        tf.setVisible(aFlag);
    }

    public TextField getTextField() {
        return tf.getTextField();
    }
}
