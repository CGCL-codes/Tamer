package musicsequencer.composer;

import musicsequencer.util.CustomFileFilter;
import musicsequencer.util.ToolBarUtilities;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Header toolbar of the main Composer window.
 * This toolbar is displayed at the top of the main Compser window.  It has
 * buttons for playback and saving/loading.  It also has a slider that allows
 * the user to change the playback tempo.
 *
 * @author Music Sequencer Group
 */
public class ComposerToolBar extends JToolBar implements ActionListener {

    /**
     * Toolbar's Play and Stop button.
     * This button is the main control for the composerEngine.  Its text
     * changes from "Play" to "Stop" and a click on it will take the correct
     * action.
     */
    private JButton playButton;

    /**
     * Toolbar's Save button.
     * Clicking this button will let the user save the current Composer song
     * to a SNG file on the hard disk or other media.
     */
    private JButton saveButton;

    /**
     * Toolbar's Load button.
     * This button allows the user to load a previously saved SNG file from the
     * hard disk or other media.
     */
    private JButton loadButton;

    /**
     * Toolbar's tempo slider.
     * Changing the value of this slider will impact how long a 16th note lasts
     * and therefore how long a bar lasts.  This updates in real time per bar.
     */
    private JSlider tempoSlider;

    /**
     * Engine instance being used in Composer.
     * This is a reference to the main composerEngine that is being used for
     * all audio functionality in the Composer application.
     */
    private ComposerEngine composerEngine;

    /**
     * Directory in which to start save or load file dialogs.
     * This is saved every time a file chooser is opened.  It is used as an
     * initial directory the next time a file chooser is opened.
     */
    private File defaultDirectory;

    /**
     * Main Composer window.
     * The main window holds the main refresh commands.  This reference is
     * needed to call <code>refresh()</code> which will update the entire
     * Composer application once a file load is performed.
     */
    private ComposerFrame guiManager;

    /**
     * Constructor for the Composer's toolbar.
     * Creates all the buttons and the slider and adds them all to the layout.
     *
     * @param guiMan    the main Composer window
     * @param engine    the main composerEngine
     */
    public ComposerToolBar(ComposerFrame guiMan, ComposerEngine engine) {
        composerEngine = engine;
        guiManager = guiMan;
        setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
        setFloatable(false);
        playButton = ToolBarUtilities.createToolBarButton("Play", "images/toolbarButtonGraphics/media/Play24.gif", this);
        add(playButton);
        loadButton = ToolBarUtilities.createToolBarButton("Load", "images/toolbarButtonGraphics/general/Open24.gif", this);
        add(loadButton);
        saveButton = ToolBarUtilities.createToolBarButton("Save", "images/toolbarButtonGraphics/general/Save24.gif", this);
        add(saveButton);
        initializeTempoSlider();
    }

    private void initializeTempoSlider() {
        tempoSlider = ToolBarUtilities.createToolBarSlider("Tempo", 60, 200, composerEngine.getTempo(), new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                composerEngine.setTempo(tempoSlider.getValue());
            }
        });
        add(tempoSlider);
    }

    /**
     * Implements the Play/Stop button clicking functionality.
     * This method is called when Play/Stop button is clicked.  It changes the
     * text of the button and starts or stops the composerEngine as needed.
     */
    private void playButtonClicked() {
        if (playButton.getText().equals("Play")) {
            playButton.setIcon(ToolBarUtilities.createImageIcon("images/toolbarButtonGraphics/media/Stop24.gif"));
            playButton.setText("Stop");
            composerEngine.startPlayback();
        } else {
            playButton.setIcon(ToolBarUtilities.createImageIcon("images/toolbarButtonGraphics/media/Play24.gif"));
            playButton.setText("Play");
            composerEngine.stopPlayback();
        }
    }

    /**
     * Implements the Save button clicking functionality.
     * This method is called when the Save Button is clicked.  It opens a file
     * chooser and makes the call to <code>ComposerEngine.save</code>.
     */
    private void saveButtonClicked() {
        JFileChooser chooser = new JFileChooser();
        if (defaultDirectory != null) chooser.setCurrentDirectory(defaultDirectory);
        chooser.setFileFilter(new CustomFileFilter("SNG"));
        int returnVal = chooser.showSaveDialog(getParent());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            defaultDirectory = new File(chooser.getSelectedFile().getParent());
            String fileName = chooser.getSelectedFile().getPath();
            String shortFileName = chooser.getSelectedFile().getName();
            if (fileName.toLowerCase().endsWith(".sng")) shortFileName = shortFileName.substring(0, shortFileName.toLowerCase().lastIndexOf(".sng")); else fileName += ".sng";
            if (composerEngine.save(fileName)) guiManager.setTitle(shortFileName + " - Music Composer");
        }
    }

    /**
     * Implements the Load button clicking functionality.
     * This method is called when the Load button is clicked.  It pops up a
     * file chooser.  Then it loads the file into the Composer windows,
     * stopping playback of the current file if necessary.
     */
    private void loadButtonClicked() {
        JFileChooser chooser = new JFileChooser();
        if (defaultDirectory != null) chooser.setCurrentDirectory(defaultDirectory);
        chooser.setFileFilter(new CustomFileFilter("SNG"));
        int returnVal = chooser.showOpenDialog(getParent());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            defaultDirectory = new File(chooser.getSelectedFile().getParent());
            String fileName = chooser.getSelectedFile().getPath();
            String shortFileName = chooser.getSelectedFile().getName();
            if (shortFileName.toLowerCase().endsWith(".sng")) shortFileName = shortFileName.substring(0, shortFileName.toLowerCase().lastIndexOf(".sng"));
            if (composerEngine.load(fileName)) guiManager.setTitle(shortFileName + " - Music Composer");
            guiManager.refreshAll();
            playButton.setIcon(ToolBarUtilities.createImageIcon("images/toolbarButtonGraphics/media/Play24.gif"));
            playButton.setText("Play");
        }
    }

    /**
     * Refreshes needed parts of the toolbar.
     * The tempo slider is the only component that needs refreshing.  This
     * happens when a new SNG file is loaded and the tempo needs to be set to
     * the value recorded in the file.
     */
    public void refresh() {
        tempoSlider.setValue(composerEngine.getTempo());
    }

    /**
     * Listens for button clicks in the composer toolbar.
     * This method is called whenever one of the buttons on the toolbar is
     * clicked.  It distinguishes between the buttons then calls the
     * appropriate button-specific <code>xxxxButtonClicked()</code> method.
     *
     * @param evt    an event detailing which button was clicked
     */
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == playButton) playButtonClicked(); else if (evt.getSource() == saveButton) saveButtonClicked(); else if (evt.getSource() == loadButton) loadButtonClicked();
    }
}
