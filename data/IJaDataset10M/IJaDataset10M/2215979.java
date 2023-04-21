package br.unisinos.cs.gp.parallax;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ParallaxEditorThumb extends JPanel {

    private static final long serialVersionUID = -347277337911199681L;

    private ParallaxEditorThumbnailer thumbnailer;

    private ImagePanel imagePanel;

    private Layer image;

    private JButton buttonUp;

    private JButton buttonDown;

    private JButton buttonSelect;

    public ParallaxEditorThumb(ParallaxEditorThumbnailer thumbnailer) {
        this.imagePanel = new ImagePanel();
        this.thumbnailer = thumbnailer;
        this.setPreferredSize(new Dimension(120, 90));
        this.setBackground(Color.WHITE);
        this.setMinimumSize(this.getPreferredSize());
        this.setMaximumSize(this.getPreferredSize());
        imagePanel.setPreferredSize(new Dimension(50, 50));
        this.add(imagePanel, BorderLayout.WEST);
        buttonUp = new JButton("^");
        buttonSelect = new JButton("X");
        buttonDown = new JButton("V");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(buttonUp);
        buttonPanel.add(buttonSelect);
        buttonPanel.add(buttonDown);
        this.add(buttonPanel, BorderLayout.EAST);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public ParallaxEditorThumb setImage(Layer image) {
        this.image = image;
        imagePanel.repaint();
        return this;
    }

    private class ImagePanel extends JPanel {

        private static final long serialVersionUID = 5137447960489698283L;

        public ImagePanel() {
            this.addMouseListener(new SelectPanelAction());
        }

        public void paint(Graphics g) {
            super.paint(g);
            Dimension dim = this.getPreferredSize();
            g.drawImage(image.getScaledInstance(dim.width, dim.height, 0), 0, 0, this);
        }

        private class SelectPanelAction extends MouseAdapter {

            public void mouseClicked(MouseEvent event) {
                ParallaxEditor editor = ParallaxEditor.getInstance();
                int index = editor.getViewPort().getLayerSet().indexOf(image);
                editor.getViewPort().setCurrentIndex(index);
                thumbnailer.updateSpeed(image.getSpeed());
            }
        }
    }
}
