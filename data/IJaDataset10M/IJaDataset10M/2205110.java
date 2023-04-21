package org.isqlviewer.bookmarks;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import org.isqlviewer.sql.embedded.EmbeddedDatabase;
import org.isqlviewer.util.BasicUtilities;

/**
 * TODO Add BookmarkReference JavaDoc inforamation
 * <p>
 * 
 * @author Mark A. Kobold &lt;mkobold at isqlviewer dot com&gt;
 * @version 1.0
 */
public class BookmarkReference implements Transferable, Serializable {

    /**
     * Standard data-transfer flavor for DnD operations.
     */
    public static final DataFlavor BOOKMARK_REFERENCE_FLAVOR = new DataFlavor(BookmarkReference.class, "iSQL-Viewer Bookmark Reference");

    private static final long serialVersionUID = 3377871201547716160L;

    private long id = -1;

    private String name = null;

    private BookmarkFolder folder = null;

    private boolean favorite = false;

    private ColorLabel colorLabel = null;

    @Override
    public boolean equals(Object other) {
        if (other instanceof BookmarkReference) {
            BookmarkReference reference = (BookmarkReference) other;
            return id == reference.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Determine if this flag is a favourite.
     * <p>
     * If a bookmark has the favourite flag set to true, then the bookmark will also be made available into a special
     * favourites folder.
     * 
     * @return if this bookmark is a favourite bookmark.
     */
    public boolean isFavorite() {
        return favorite;
    }

    /**
     * Modifies the favourite flag for this bookmark instance.
     * <p>
     * 
     * @param favorite setting to enable/disable the favourite status of this bookmark.
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /**
     * @param folder the folder to set
     */
    public void setFolder(BookmarkFolder folder) {
        this.folder = folder;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the user defined name for this bookmark.
     * <p>
     * 
     * @return the conical name for this bookmark as defined by the user.
     */
    public String getName() {
        return name == null ? "" : name;
    }

    /**
     * Sets the user defined name for this bookmark.
     * <p>
     * If a <tt>null</tt> name is provided, then a empty string will be inferred as the intended value of the new
     * name.
     * 
     * @param name of the bookmark to be identified by the user.
     */
    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    /**
     * Gets the path of this folder.
     * <p>
     * 
     * @return UNIX like path of the folder that contains this bookmark.
     */
    public String getPath() {
        return folder == null ? "/" : folder.getPath();
    }

    /**
     * Gets the label color for this bookmark.
     * <p>
     * Allows specific client-side coloring for particular bookmarks; inspired from Finder.
     * 
     * @return the colorLabel to color the label; can be null;
     */
    public ColorLabel getColorLabel() {
        return colorLabel;
    }

    /**
     * Sets the label color color for displaying this bookmark.
     * <p>
     * 
     * @param colorLabel the colorLabel to set; use null to turn of
     */
    public void setColorLabel(ColorLabel labelColor) {
        this.colorLabel = labelColor;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor != null) {
            if (BOOKMARK_REFERENCE_FLAVOR.equals(flavor)) {
                return this;
            }
            Bookmark bookmark = null;
            EmbeddedDatabase database = EmbeddedDatabase.getSharedInstance();
            try {
                bookmark = database.getBookmark(this);
                bookmark.setFolder(getFolder());
            } catch (SQLException sourceError) {
                IOException wrappedError = new IOException(sourceError.getMessage());
                BasicUtilities.wrapThrowable(sourceError, wrappedError);
                throw wrappedError;
            }
            if (Bookmark.BOOKMARK_FLAVOR.equals(flavor)) {
                return bookmark;
            } else if (flavor.isFlavorTextType()) {
                return bookmark.getCommandText();
            }
        }
        throw new UnsupportedFlavorException(flavor);
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { BOOKMARK_REFERENCE_FLAVOR, Bookmark.BOOKMARK_FLAVOR, DataFlavor.stringFlavor };
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (flavor != null) {
            if (BOOKMARK_REFERENCE_FLAVOR.equals(flavor)) {
                return true;
            } else if (Bookmark.BOOKMARK_FLAVOR.equals(flavor)) {
                return true;
            } else if (flavor.isFlavorTextType()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Protected call for getting a reference to this bookmarks parent folder.
     * <p>
     * Bookmarks publicly should be always be manipulated from the folder object that owns it.
     * 
     * @return reference to the owning folder.
     */
    public BookmarkFolder getFolder() {
        return folder;
    }
}
