package org.palo.api.ext.favoriteviews.impl;

import java.io.ByteArrayInputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.palo.api.Connection;
import org.palo.api.Cube;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.Hierarchy;
import org.palo.api.PaloAPIException;
import org.palo.api.PaloConstants;
import org.palo.api.ext.favoriteviews.FavoriteViewTreeNode;
import com.tensegrity.palo.xmla.ext.views.SQLConnection;

/**
 * The <code>FavoriteViewModel</code> class provides internal methods to load
 * and save the favorite view tree. Namely, this class checks if a cube to
 * store bookmarks is available and creates one if that is not the case. The
 * class will also generate the xml code for a favorite view tree and save that
 * in the correct position in the database.
 * 
 * @author PhilippBouillon
 * @version $Id: FavoriteViewModel.java,v 1.12 2008/08/18 10:26:07 PhilippBouillon Exp $
 */
public class FavoriteViewModel {

    /**
     * Takes the XML representation of a favorite view tree structure and
     * transforms it into a tree.
     * 
     * @param input the xml representation of the favorite views.
     * @return the root of the newly generated tree.
     */
    protected final FavoriteViewTreeNode fromXML(String input, Connection con) {
        FavoriteViewXMLHandler defaultHandler = new FavoriteViewXMLHandler(con);
        SAXParserFactory sF = SAXParserFactory.newInstance();
        SAXParser parser = null;
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(input.getBytes("UTF-8"));
            parser = sF.newSAXParser();
            parser.parse(bin, defaultHandler);
            return defaultHandler.getRoot();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * If a connection is found that does not (yet) have an
     * <code>AdvancedSystem</code> database, the database is created in this
     * method, then dimensions (#user and #bookmarkedViews) are added along
     * with the entries (the current user name and "Bookmarks"). Finally a
     * cube is created using these two dimensions. The cube is then returned.
     * 
     * @param con the connection in which the new AdvancedSystem database is to
     * be added.
     * @return the bookmark cube of the AdvancedSystem database consisting of
     * the user and bookmark dimensions. 
     */
    public Cube createBookmarkCubeInNewDatabase(Connection con) {
        Cube cube = null;
        try {
            Database paloDb = con.addDatabase(PaloConstants.PALO_CLIENT_SYSTEM_DATABASE);
            Dimension userDim = paloDb.addDimension("#user");
            Hierarchy userHier = userDim.getDefaultHierarchy();
            userHier.addElement(con.getUsername(), Element.ELEMENTTYPE_STRING);
            Dimension bookmarkDim = paloDb.addDimension("#bookmarkedViews");
            Hierarchy bookmarkHier = bookmarkDim.getDefaultHierarchy();
            bookmarkHier.addElement("Bookmarks", Element.ELEMENTTYPE_STRING);
            cube = paloDb.addCube("#userBookmarks", new Dimension[] { userDim, bookmarkDim });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cube;
    }

    /**
     * If a connection is found that _does_ have an <code>AdvancedSystem</code>
     * database but which does not include the bookmarks cube, this method
     * creates the new bookmarks cube.
     * 
     * Dimensions will be created if necessary.
     * 
     * @param db the AdvancedSystem database of the current connection.
     * @return the newly created bookmark cube.
     */
    public Cube createBookmarkCubeInExistingDatabase(Database db) {
        Dimension user;
        Dimension bookmarks;
        user = db.getDimensionByName("#user");
        if (user == null) {
            user = db.addDimension("#user");
            user.getDefaultHierarchy().addElement(db.getConnection().getUsername(), Element.ELEMENTTYPE_STRING);
        } else {
            if (user.getDefaultHierarchy().getElementByName(db.getConnection().getUsername()) == null) {
                user.getDefaultHierarchy().addElement(db.getConnection().getUsername(), Element.ELEMENTTYPE_STRING);
            }
        }
        bookmarks = db.getDimensionByName("#bookmarkedViews");
        if (bookmarks == null) {
            bookmarks = db.addDimension("#bookmarkedViews");
            bookmarks.getDefaultHierarchy().addElement("Bookmarks", Element.ELEMENTTYPE_STRING);
        } else {
            if (bookmarks.getDefaultHierarchy().getElementByName("Bookmarks") == null) {
                bookmarks.getDefaultHierarchy().addElement("Bookmarks", Element.ELEMENTTYPE_STRING);
            }
        }
        Cube cube = db.addCube("#userBookmarks", new Dimension[] { user, bookmarks });
        return cube;
    }

    /**
     * Finds the bookmark cube for a given connection. If it is not present
     * yet, the program will try to create it.
     * 
     * @param con the connection from which the bookmark cube is to be found.
     * @return the bookmark cube for the connection or null if it could not
     * be created.
     */
    private final Cube findBookmarksCube(Connection con) {
        Database db = null;
        db = con.getDatabaseByName(PaloConstants.PALO_CLIENT_SYSTEM_DATABASE);
        Cube cube = null;
        if (db == null) {
            cube = createBookmarkCubeInNewDatabase(con);
        } else {
            cube = db.getCubeByName("#userBookmarks");
            if (cube == null) {
                cube = createBookmarkCubeInExistingDatabase(db);
            }
        }
        return cube;
    }

    /**
     * The method retrieves the AdvancedSystem database of the connection (or
     * creates it, if it is not yet in the connection) and also gets the
     * bookmark-cube from the AdvancedSystem database (or creates it, if it had
     * not been present (for this user)).
     * Then, this methods loads all stored favorite views and transforms the
     * xml structure to a new favorite views tree. The root of which is
     * returned.
     * 
     * @param con the connection which is being added to the bookmark model.
     * @return the root of the favorite views tree or null if no bookmarks
     * exist.
     */
    public FavoriteViewTreeNode loadFavoriteViews(Connection con) {
        if (con.getType() == Connection.TYPE_XMLA) {
            SQLConnection sql = new SQLConnection();
            String xmlData = "";
            try {
                xmlData = sql.loadFavoriteView(con.getServer(), con.getService(), con.getUsername());
            } finally {
                sql.close();
            }
            if (xmlData.trim().length() > 0) {
                return fromXML(xmlData, con);
            }
            return null;
        }
        if (con.getType() == Connection.TYPE_WSS) {
            return null;
        }
        Cube cube = findBookmarksCube(con);
        if (cube == null) {
            return null;
        }
        Dimension userDim = cube.getDimensionByName("#user");
        if (userDim == null) {
            return null;
        }
        if (userDim.getDefaultHierarchy().getElementByName(con.getUsername()) == null) {
            try {
                userDim.getDefaultHierarchy().addElement(con.getUsername(), Element.ELEMENTTYPE_STRING);
            } catch (Exception e) {
                throw new PaloAPIException(e.getLocalizedMessage());
            }
        }
        try {
            String xmlData = (String) cube.getData(new String[] { con.getUsername(), "Bookmarks" });
            if (xmlData != null) {
                if (xmlData.length() > 0) {
                    return fromXML(xmlData, con);
                }
            }
        } catch (PaloAPIException e) {
            System.err.println("Failed to load favorite view!\nReason: " + e.getMessage());
        }
        return null;
    }

    /**
     * Stores a favorite views tree in the given connection.
     * 
     * @param con the connection which is to store the favorite views.
     * @param root the root of the favorite views tree that is to be stored.
     */
    public synchronized void storeFavoriteViews(Connection con, FavoriteViewTreeNode root) {
        if (con == null) {
            throw new NullPointerException("Connection to store the bookmark must not be null.");
        }
        if (!con.isConnected()) {
            return;
        }
        FavoriteViewXMLBuilder builder = new FavoriteViewXMLBuilder(con);
        builder.preOrderTraversal(root);
        String xmlBookmarkText = builder.getResult();
        if (con.getType() == Connection.TYPE_XMLA) {
            SQLConnection sql = new SQLConnection();
            try {
                sql.writeFavoriteViews(con.getServer(), con.getService(), con.getUsername(), xmlBookmarkText);
            } finally {
                sql.close();
            }
            return;
        }
        if (con.getType() == Connection.TYPE_WSS) {
            return;
        }
        Cube cube = findBookmarksCube(con);
        if (cube == null) {
            throw new PaloAPIException("Insufficient rights to store favorite views.");
        }
        cube.setData(new String[] { con.getUsername(), "Bookmarks" }, xmlBookmarkText);
    }
}
