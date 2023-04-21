package ui.components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import presentation.PresentationComponent;
import slides.ImageSlide;
import slides.VideoSlide;
import ui.components.ImageSlideGroupEditor.ImageList.ImageElement;

public class VideoSlideEditor extends JPanel {

    VideoSlide vs = null;

    final JFileChooser fc = new JFileChooser();

    JLabel source_info = new JLabel("no source");

    JLabel res_info = new JLabel("no res info");

    JCheckBox audio_en = new JCheckBox("Audio enabled");

    protected boolean setSource() {
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            VideoSlideEditor.this.vs.setSource(file);
        } else {
            return false;
        }
        return true;
    }

    void setSourceInfo() {
        source_info.setText("Source: " + vs.getSource().getName());
        Dimension res = vs.getResolution();
        long duration = vs.getDuration();
        res_info.setText("Native res: " + res.width + "x" + res.height + " " + (duration / 60) + "m " + (duration % 60) + "s");
    }

    public VideoSlideEditor(PresentationComponent current_selection) {
        vs = (VideoSlide) current_selection;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel song_panel_label = new JLabel("Video slide editor");
        Font f = new Font("Dialog", Font.PLAIN, 24);
        song_panel_label.setFont(f);
        add(song_panel_label, Box.CENTER_ALIGNMENT);
        add(new PresentationComponentNameEditor(current_selection));
        add(source_info);
        add(res_info);
        add(audio_en);
        audio_en.setSelected(vs.isAudioEnabled());
        JButton change_source = new JButton("Change source...");
        setSourceInfo();
        change_source.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setSource();
            }
        });
    }
}
