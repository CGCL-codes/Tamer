package de.marcelcarle.se.gruppe10.tagger.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.id3.ID3v24Tag;

/**
 *
 * @author mict
 */
public class JImagePanel extends JMyPanel {

    JList imageList;

    JPanel rightSide;

    boolean multipleFiles;

    public JImagePanel() {
        super(new BorderLayout());
        initialize();
    }

    private void initialize() {
        imageList = new JList();
        imageList.setModel(new DefaultListModel());
        imageList.setVisible(true);
        imageList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        this.add(imageList, BorderLayout.WEST);
        rightSide = new JPanel(new GridLayout(0, 1, 50, 50));
        rightSide.setVisible(true);
        this.add(rightSide, BorderLayout.CENTER);
        setVisible(true);
    }

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2 && imageList.getSelectedValues().length > 0) {
            int fs = imageList.getSelectedIndex();
            TagImageElement tim = (TagImageElement) ((DefaultListModel) imageList.getModel()).get(fs);
            rightSide.removeAll();
            ViewComponent vc = new ViewComponent(100, 100);
            vc.setImage(tim.getImage());
            rightSide.add(vc, BorderLayout.CENTER);
            vc.setVisible(true);
            vc.repaint();
            JLabel lDesc = new JLabel("Beschreibung: ");
            JTextArea tfDesc = new JTextArea(tim.getDescription(), 5, 20);
            JTagDisplay tDDisc = new JTagDisplay(lDesc, tfDesc);
            rightSide.add(tDDisc, BorderLayout.EAST);
            rightSide.setVisible(true);
            rightSide.repaint();
            rightSide.setDoubleBuffered(true);
            this.repaint();
            this.getParent().repaint();
        }
    }

    @Override
    Tag getTag(Tag tag) {
        return tag;
    }

    @Override
    void setTags(AudioFile[] fArray, String fileExtension, Tag tmpTag) {
    }

    @Override
    void setTags(AudioFile audioFile, String fileExtension) {
        multipleFiles = false;
        Tag tag = audioFile.getTag();
        List<Artwork> li = tag.getArtworkList();
        ((DefaultListModel) this.imageList.getModel()).clear();
        try {
            for (Artwork a : li) {
                TagImageElement tim = new TagImageElement(a.getImage(), a.getPictureType(), a.getDescription());
                ((DefaultListModel) imageList.getModel()).addElement(tim);
            }
        } catch (Exception e) {
        }
    }

    class ViewComponent extends JComponent {

        private Image image;

        private int height;

        private int width;

        public ViewComponent(int width, int height) {
            this.setMinimumSize(new Dimension(100, 100));
            this.setMaximumSize(new Dimension(100, 100));
            this.height = height;
            this.width = width;
        }

        public void setImage(BufferedImage file) {
            image = file;
            if (image != null) {
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (image != null) {
                g.drawImage(image, 0, 0, width, height, this);
            }
        }
    }
}
