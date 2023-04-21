package com.codename1.designer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PreviewPane extends JPanel implements PropertyChangeListener {

    private JLabel label;

    private JLabel previewDetails;

    private int maxImgWidth;

    public PreviewPane(JFileChooser chooser) {
        chooser.setAccessory(this);
        chooser.addPropertyChangeListener(this);
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        previewDetails = new JLabel("Preview:");
        add(previewDetails, BorderLayout.NORTH);
        label = new JLabel();
        label.setBackground(Color.WHITE);
        label.setPreferredSize(new Dimension(200, 200));
        maxImgWidth = 195;
        label.setOpaque(false);
        label.setBorder(BorderFactory.createEtchedBorder());
        add(label, BorderLayout.CENTER);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        Icon icon = null;
        if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
            File newFile = (File) evt.getNewValue();
            if (newFile != null) {
                previewDetails.setText("" + (newFile.length() / 1024) + "kb");
                String path = newFile.getAbsolutePath();
                if (path.endsWith(".gif") || path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".bmp")) {
                    try {
                        BufferedImage img = ImageIO.read(newFile);
                        previewDetails.setText(previewDetails.getText() + " " + img.getWidth() + "x" + img.getHeight());
                        float width = img.getWidth();
                        float height = img.getHeight();
                        float scale = height / width;
                        width = maxImgWidth;
                        height = (width * scale);
                        icon = new ImageIcon(img.getScaledInstance(Math.max(1, (int) width), Math.max(1, (int) height), Image.SCALE_SMOOTH));
                    } catch (IOException e) {
                    }
                } else {
                    if (path.endsWith(".svg")) {
                        InputStream input = null;
                        try {
                            input = new FileInputStream(newFile);
                            org.apache.batik.transcoder.image.PNGTranscoder t = new org.apache.batik.transcoder.image.PNGTranscoder();
                            org.apache.batik.transcoder.TranscoderInput i = new org.apache.batik.transcoder.TranscoderInput(input);
                            ByteArrayOutputStream bo = new ByteArrayOutputStream();
                            org.apache.batik.transcoder.TranscoderOutput o = new org.apache.batik.transcoder.TranscoderOutput(bo);
                            t.transcode(i, o);
                            input.close();
                            bo.close();
                            icon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(bo.toByteArray())));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            try {
                                input.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
            label.setIcon(icon);
            repaint();
        }
    }
}
