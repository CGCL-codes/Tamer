package jmediaplayer;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;

/**
 *
 * @author Duy Nguyen
 */
public class JTagEditor extends JDialog implements ActionListener {

    public static String CMD_OK = "OKCommand";

    public static String CMD_CANCEL = "CancelCommand";

    private AudioFile m_AudioFile;

    private JTextField m_TrackNumber;

    private JTextField m_TrackTitle;

    private JTextField m_Album;

    private JTextField m_Artist;

    private JTextField m_Comment;

    private JTextField m_Composer;

    private JTextField m_Copyright;

    private JTextField m_Encoded;

    private JTextField m_Original;

    private JTextField m_Lyric;

    private JTextField m_Year;

    private JComboBox m_Genre;

    private String[] m_Genres = { "", "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "General Fiction", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall" };

    public JTagEditor(Frame owner, AudioFile audioFile) {
        super(owner, "Tags Editor", true);
        m_AudioFile = audioFile;
        Initialize();
        initValues();
        pack();
    }

    /**
     * Process task when OK or Cancel
     * @param e
     * @author PHUOCVIET
     */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command != null) {
            if (command.equals(JTagEditor.CMD_OK)) {
                Tag tag = m_AudioFile.getTag();
                if (tag != null) {
                    try {
                        tag.setField(FieldKey.TRACK, m_TrackNumber.getText());
                        tag.setField(FieldKey.TITLE, m_TrackTitle.getText());
                        tag.setField(FieldKey.ALBUM, m_Album.getText());
                        tag.setField(FieldKey.ARTIST, m_Artist.getText());
                        tag.setField(FieldKey.COMMENT, m_Comment.getText());
                        tag.setField(FieldKey.COMPOSER, m_Composer.getText());
                        tag.setField(FieldKey.ENCODER, m_Encoded.getText());
                        tag.setField(FieldKey.LYRICS, m_Lyric.getText());
                        tag.setField(FieldKey.YEAR, m_Year.getText());
                        tag.setField(FieldKey.GENRE, m_Genre.getSelectedItem().toString());
                    } catch (KeyNotFoundException ex) {
                        Logger.getLogger(JTagEditor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (FieldDataInvalidException ex) {
                        Logger.getLogger(JTagEditor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                    }
                    try {
                        m_AudioFile.commit();
                    } catch (CannotWriteException ex) {
                        JOptionPane.showMessageDialog(this, "Error writing tag to file", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
                this.setVisible(false);
            } else if (command.equals(JTagEditor.CMD_CANCEL)) {
                this.setVisible(false);
            }
        }
    }

    private void Initialize() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(0, 1, 1, 1);
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        getContentPane().add(new JLabel("Track Number: ", SwingConstants.RIGHT), constraints);
        constraints.gridx = GridBagConstraints.RELATIVE;
        m_TrackNumber = new JTextField(3);
        getContentPane().add(m_TrackNumber, constraints);
        constraints.gridy = 1;
        constraints.gridx = 0;
        getContentPane().add(new JLabel("Track Title: ", SwingConstants.RIGHT), constraints);
        constraints.gridx = GridBagConstraints.RELATIVE;
        m_TrackTitle = new JTextField(30);
        getContentPane().add(m_TrackTitle, constraints);
        constraints.gridy = 2;
        constraints.gridx = 0;
        getContentPane().add(new JLabel("Album: ", SwingConstants.RIGHT), constraints);
        constraints.gridx = GridBagConstraints.RELATIVE;
        m_Album = new JTextField(30);
        getContentPane().add(m_Album, constraints);
        constraints.gridy = 3;
        constraints.gridx = 0;
        getContentPane().add(new JLabel("Artist: ", SwingConstants.RIGHT), constraints);
        constraints.gridx = GridBagConstraints.RELATIVE;
        m_Artist = new JTextField(30);
        getContentPane().add(m_Artist, constraints);
        constraints.gridy = 4;
        constraints.gridx = 0;
        getContentPane().add(new JLabel("Genre: ", SwingConstants.RIGHT), constraints);
        constraints.gridx = GridBagConstraints.RELATIVE;
        m_Genre = new JComboBox(m_Genres);
        m_Genre.setSelectedIndex(0);
        getContentPane().add(m_Genre, constraints);
        constraints.gridy = 5;
        constraints.gridx = 0;
        getContentPane().add(new JLabel("Comment: ", SwingConstants.RIGHT), constraints);
        constraints.gridx = GridBagConstraints.RELATIVE;
        m_Comment = new JTextField(30);
        getContentPane().add(m_Comment, constraints);
        constraints.gridy = 6;
        constraints.gridx = 0;
        getContentPane().add(new JLabel("Composer: ", SwingConstants.RIGHT), constraints);
        constraints.gridx = GridBagConstraints.RELATIVE;
        m_Composer = new JTextField(30);
        getContentPane().add(m_Composer, constraints);
        constraints.gridy = 8;
        constraints.gridx = 0;
        getContentPane().add(new JLabel("Encoded By: ", SwingConstants.RIGHT), constraints);
        constraints.gridx = GridBagConstraints.RELATIVE;
        m_Encoded = new JTextField(30);
        getContentPane().add(m_Encoded, constraints);
        constraints.gridy = 10;
        constraints.gridx = 0;
        getContentPane().add(new JLabel("Lyric: ", SwingConstants.RIGHT), constraints);
        constraints.gridx = GridBagConstraints.RELATIVE;
        m_Lyric = new JTextField(30);
        m_Lyric.setBounds(new Rectangle(-1, 30));
        getContentPane().add(m_Lyric, constraints);
        constraints.gridy = 11;
        constraints.gridx = 0;
        getContentPane().add(new JLabel("Year: ", SwingConstants.RIGHT), constraints);
        constraints.gridx = GridBagConstraints.RELATIVE;
        m_Year = new JTextField(4);
        getContentPane().add(m_Year, constraints);
        constraints.gridy = 12;
        constraints.gridx = 1;
        JPanel buttonPanel = new JPanel();
        JButton button = new JButton("OK");
        button.setActionCommand(JTagEditor.CMD_OK);
        button.addActionListener(this);
        buttonPanel.add(button);
        button = new JButton("Cancel");
        button.setActionCommand(JTagEditor.CMD_CANCEL);
        button.addActionListener(this);
        buttonPanel.add(button);
        getContentPane().add(buttonPanel, constraints);
    }

    /**
     * Init all values diplayed when window opened
     * @author PHUOCVIET
     */
    private void initValues() {
        if (m_AudioFile != null) {
            Tag tag = m_AudioFile.getTag();
            if (tag != null) {
                try {
                    m_TrackNumber.setText(tag.getFirst(FieldKey.TRACK));
                } catch (Exception ex) {
                }
                try {
                    m_TrackTitle.setText(tag.getFirst(FieldKey.TITLE));
                } catch (Exception ex) {
                }
                try {
                    m_Album.setText(tag.getFirst(FieldKey.ALBUM));
                } catch (Exception exception) {
                }
                try {
                    m_Artist.setText(tag.getFirst(FieldKey.ARTIST));
                } catch (Exception exception) {
                }
                try {
                    m_Comment.setText(tag.getFirst(FieldKey.COMMENT));
                } catch (Exception exception) {
                }
                try {
                    m_Composer.setText(tag.getFirst(FieldKey.COMPOSER));
                } catch (Exception exception) {
                }
                try {
                    m_Encoded.setText(tag.getFirst(FieldKey.ENCODER));
                } catch (Exception exception) {
                }
                try {
                    m_Lyric.setText(tag.getFirst(FieldKey.LYRICS));
                } catch (Exception exception) {
                }
                try {
                    m_Year.setText(tag.getFirst(FieldKey.YEAR));
                } catch (Exception exception) {
                }
                try {
                    m_Genre.setSelectedItem(tag.getFirst(FieldKey.GENRE));
                } catch (Exception exception) {
                }
            } else {
                tag = m_AudioFile.createDefaultTag();
                m_AudioFile.setTag(tag);
            }
        }
    }
}
