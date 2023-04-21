package q.zik.main.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import q.zik.main.view.SongFrame;
import q.zik.util.ResourceProvider;

public class NewAction extends AbstractAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected final SongFrame song;

    public NewAction(final SongFrame _song) {
        super(ResourceProvider.getLabel("fileMenu.new"), ResourceProvider.getImageIcon("fileMenu.icon.new", 16, 16));
        song = _song;
    }

    @Override
    public void actionPerformed(final ActionEvent _arg0) {
        if (song.showSaveConfirmationDialog()) {
            song.getSong().reset();
        }
    }
}
