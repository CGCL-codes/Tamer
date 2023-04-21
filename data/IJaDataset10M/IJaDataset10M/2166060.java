package org.jimcat.gui.imagepopup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import org.jimcat.gui.AlbumControl;
import org.jimcat.gui.ImageControl;
import org.jimcat.gui.SwingClient;
import org.jimcat.model.Album;

/**
 * The submenu used to add the currently selected images to a selectable album.
 * 
 * $Id$
 * 
 * @author Herbert
 */
public class AddToAlbumMenu extends ImagePopupAlbumMenu {

    /**
	 * the creator used to create new tag and add images to new tag
	 */
    private AlbumCreator creator = new AlbumCreator();

    /**
	 * a reference to the installed ImageControl
	 */
    private ImageControl control = SwingClient.getInstance().getImageControl();

    /**
	 * a reference to the album control
	 */
    private AlbumControl albumControl = SwingClient.getInstance().getAlbumControl();

    /**
	 * do the magic - add images to an album
	 * 
	 * @see org.jimcat.gui.imagepopup.ImagePopupAlbumMenu#elementSelected(org.jimcat.model.Album)
	 */
    @Override
    public void elementSelected(Album album) {
        control.addSelectionToAlbum(album);
    }

    /**
	 * use basic implementation + new item
	 * 
	 * @see org.jimcat.gui.imagepopup.ImagePopupAlbumMenu#buildNewList()
	 */
    @Override
    protected void buildNewList() {
        super.buildNewList();
        addSeparator();
        JMenuItem item = new JMenuItem("<html><i>New Album...");
        item.addActionListener(getCreator());
        add(item);
    }

    /**
	 * get tag creation listener
	 * 
	 * @return the AlbumCreator
	 */
    private AlbumCreator getCreator() {
        if (creator == null) {
            creator = new AlbumCreator();
        }
        return creator;
    }

    /**
	 * used to generate and assign new tags
	 */
    private class AlbumCreator implements ActionListener {

        /**
		 * react on a click
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
        @SuppressWarnings("unused")
        public void actionPerformed(ActionEvent e) {
            Album album = albumControl.createNewAlbum();
            if (album == null) {
                return;
            }
            elementSelected(album);
        }
    }
}
