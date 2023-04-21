package com.mystictri.neotextureedit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import engine.parameters.ImageParam;

public class ImageParameterEditor extends AbstractParameterEditor implements ActionListener {

    private static final long serialVersionUID = -4192786178673946778L;

    ImageParam param;

    JButton loadButton;

    JButton reloadButton;

    public ImageParameterEditor(ImageParam p) {
        super();
        param = p;
        int x = 0;
        int y = 0;
        JLabel nameLabel = new JLabel(p.getName() + ":");
        nameLabel.setBounds(x, y, NAME_WIDTH, h);
        x += NAME_WIDTH;
        add(nameLabel);
        loadButton = new JButton();
        loadButton.setBounds(x, y, BUTTON_WIDTH + TEXTFIELD_WIDTH, h);
        x += BUTTON_WIDTH + TEXTFIELD_WIDTH;
        loadButton.addActionListener(this);
        add(loadButton);
        reloadButton = new JButton("R");
        reloadButton.setBounds(x, y, BUTTON_WIDTH, h);
        x += BUTTON_WIDTH;
        reloadButton.addActionListener(this);
        reloadButton.setToolTipText("Reload Image");
        add(reloadButton);
        setFileName(p.getImageFilename());
    }

    String getOnlyFilename(String filename) {
        if (filename.lastIndexOf('/') > 0) return filename.substring(filename.lastIndexOf('/') + 1); else if (filename.lastIndexOf('\\') > 0) return filename.substring(filename.lastIndexOf('\\') + 1); else return filename;
    }

    void setFileName(String filename) {
        if (param.loadImage(filename)) {
            loadButton.setText(getOnlyFilename(filename));
            loadButton.setToolTipText(filename);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadButton) {
            JFileChooser c = TextureEditor.INSTANCE.m_TextureFileChooser_SaveLoadImage;
            c.setDialogTitle("Open image ...");
            if (c.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String name = c.getSelectedFile().getAbsolutePath();
                setFileName(name);
            }
        } else if (e.getSource() == reloadButton) {
            param.reloadeImage();
        }
    }
}
