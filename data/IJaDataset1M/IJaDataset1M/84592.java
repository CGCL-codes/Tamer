package freestyleLearningGroup.independent.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.JButton;

public class FLGAbstractImageToggleButton extends AbstractButton {

    protected Image button_normal;

    protected Image button_pressed;

    protected Image button_rollover;

    protected Image button_disabled;

    protected Image image_selected_normal;

    protected Image image_pressed;

    protected Image image_selected_rollover;

    protected Image image_selected_disabled;

    protected Image image_notSelected_normal;

    protected Image image_notSelected_rollover;

    protected Image image_notSelected_disabled;

    protected int buttonWidth;

    protected int buttonHeight;

    protected int imageWidth;

    protected int imageHeight;

    private Dimension preferredSize;

    private boolean rollover;

    private boolean pressed;

    private boolean selected;

    private String actionCommand;

    private Vector actionListeners;

    public FLGAbstractImageToggleButton() {
        model = (new JButton()).getModel();
    }

    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    public void setActionCommand(String actionCommand) {
        this.actionCommand = actionCommand;
    }

    public String getActionCommand() {
        return actionCommand;
    }

    public Dimension getPreferredSize() {
        return preferredSize;
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (pressed) {
            g.drawImage(button_pressed, 0, 0, this);
            g.drawImage(image_pressed, (buttonWidth - imageWidth) / 2 + 1, (buttonHeight - imageHeight) / 2 + 1, this);
        } else if (rollover) {
            g.drawImage(button_rollover, 0, 0, this);
            if (selected) g.drawImage(image_selected_rollover, (buttonWidth - imageWidth) / 2, (buttonHeight - imageHeight) / 2, this); else g.drawImage(image_notSelected_rollover, (buttonWidth - imageWidth) / 2, (buttonHeight - imageHeight) / 2, this);
        } else if (!isEnabled()) {
            g.drawImage(button_disabled, 0, 0, this);
            if (selected) g.drawImage(image_selected_disabled, (buttonWidth - imageWidth) / 2, (buttonHeight - imageHeight) / 2, this); else g.drawImage(image_notSelected_disabled, (buttonWidth - imageWidth) / 2, (buttonHeight - imageHeight) / 2, this);
        } else {
            g.drawImage(button_normal, 0, 0, this);
            if (selected) g.drawImage(image_selected_normal, (buttonWidth - imageWidth) / 2, (buttonHeight - imageHeight) / 2, this); else g.drawImage(image_notSelected_normal, (buttonWidth - imageWidth) / 2, (buttonHeight - imageHeight) / 2, this);
        }
    }

    public void setEnabled(boolean enabled) {
        if (!enabled) {
            this.rollover = false;
            this.pressed = false;
        }
        super.setEnabled(enabled);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    public boolean isSelected() {
        return selected;
    }

    protected Image loadImage(String imageFileName) {
        return FLGImageUtility.loadImageAndWait(getClass().getResource(imageFileName));
    }

    protected Image createButtonImage(String filename) {
        return FLGImageUtility.createAntiAliasedImage(loadImage(filename), buttonWidth, buttonHeight);
    }

    protected void init(Image notSelectedImage, Image selectedImage) {
        image_selected_normal = FLGImageUtility.createAntiAliasedImage(selectedImage, imageWidth, imageHeight);
        image_pressed = FLGImageUtility.createAntiAliasedImage(FLGImageUtility.createImageWithDifferentBrightness(selectedImage, 0.75), imageWidth, imageHeight);
        image_selected_rollover = FLGImageUtility.createAntiAliasedImage(FLGImageUtility.createImageWithDifferentBrightness(selectedImage, 1.5), imageWidth, imageHeight);
        image_selected_disabled = FLGImageUtility.createAntiAliasedImage(FLGImageUtility.createGrayImage(selectedImage), imageWidth, imageHeight);
        image_notSelected_normal = FLGImageUtility.createAntiAliasedImage(notSelectedImage, imageWidth, imageHeight);
        image_notSelected_rollover = FLGImageUtility.createAntiAliasedImage(FLGImageUtility.createImageWithDifferentBrightness(notSelectedImage, 1.5), imageWidth, imageHeight);
        image_notSelected_disabled = FLGImageUtility.createAntiAliasedImage(FLGImageUtility.createGrayImage(notSelectedImage), imageWidth, imageHeight);
        initAllButImages();
    }

    protected void initAllButImages() {
        preferredSize = new Dimension(buttonWidth, buttonHeight);
        actionListeners = new Vector();
        setRequestFocusEnabled(false);
        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    pressed = true;
                    selected = !selected;
                    repaint();
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) {
                    fireActionEvent();
                    pressed = false;
                    repaint();
                }
            }

            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    rollover = true;
                    repaint();
                }
            }

            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    rollover = false;
                    repaint();
                }
            }
        });
    }

    private void fireActionEvent() {
        ActionEvent event = new ActionEvent(this, 0, getActionCommand());
        for (int i = 0; i < actionListeners.size(); i++) {
            ActionListener listener = (ActionListener) actionListeners.get(i);
            listener.actionPerformed(event);
        }
    }
}
