package com.melloware.jukes.gui.view.node;

import java.awt.Color;
import java.awt.Font;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jgoodies.uif.application.Application;
import com.melloware.jukes.db.HibernateDao;
import com.melloware.jukes.db.HibernateUtil;
import com.melloware.jukes.db.orm.AbstractJukesObject;
import com.melloware.jukes.db.orm.Artist;
import com.melloware.jukes.db.orm.Disc;
import com.melloware.jukes.db.orm.Track;
import com.melloware.jukes.file.image.ImageBlender;
import com.melloware.jukes.gui.tool.MainModule;
import com.melloware.jukes.gui.tool.Resources;
import com.melloware.jukes.gui.tool.Settings;
import com.melloware.jukes.gui.view.MainFrame;
import com.melloware.jukes.util.GuiUtil;
import com.melloware.jukes.util.MessageUtil;

/**
 * Abstract tree node class that all tree nodes must extend from.  Supports
 * lazy loading of children to conserve memory and better performance.
 * <p>
 * Copyright (c) 1999-2007 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 4.0
 *
 * @see javax.swing.tree.DefaultMutableTreeNode
 * @see NavigationNode
 */
@SuppressWarnings("PMD")
public abstract class AbstractTreeNode extends DefaultMutableTreeNode implements NavigationNode {

    private static final Log LOG = LogFactory.getLog(AbstractTreeNode.class);

    /**
     * Comparator used to sort the tree nodes by name constantly.
     */
    private static Comparator nodeComparator = new Comparator() {

        public int compare(Object aObject1, Object aObject2) {
            final AbstractTreeNode item1 = (AbstractTreeNode) aObject1;
            final AbstractTreeNode item2 = (AbstractTreeNode) aObject2;
            final CompareToBuilder builder = new CompareToBuilder();
            builder.append(item1.getName().toUpperCase(), item2.getName().toUpperCase());
            return builder.toComparison();
        }
    };

    /**
     * Base font used for all children to derive font from.
     */
    protected static final Font BASEFONT = new JList().getFont();

    protected final AbstractJukesObject model;

    protected boolean childrenLoaded = false;

    protected boolean loadingChildren = false;

    protected Color fontColor;

    protected Font font;

    protected final NavigationNode parent;

    protected Settings settings;

    /**
     * Constructor that takes the parent node and the domain model to contain.
     * <p>
     * @param aParent the parent node
     * @param aModel the domain model to contain in this node
     */
    public AbstractTreeNode(NavigationNode aParent, AbstractJukesObject aModel) {
        super();
        parent = aParent;
        model = aModel;
        loadingChildren = false;
        childrenLoaded = false;
    }

    /**
     * Returns this node's name. Subclasses typically implement this method
     * by returning the model's name or identifier.
     *
     * @return this node's name
     */
    public abstract String getName();

    /**
     * Loads the node's child objects.  This is used for lazy loading and each
     * subclass should implement this to retrieve its children.
     */
    public abstract void loadChildren();

    public TreeNode getChildAt(int index) {
        loadChildren();
        return super.getChildAt(index);
    }

    public int getChildCount() {
        if (childrenLoaded || loadingChildren) {
            return super.getChildCount();
        } else {
            if (model == null) {
                return 0;
            } else {
                return model.getChildCount();
            }
        }
    }

    public Font getFont() {
        this.font = null;
        if (getModel().isNotValid()) {
            font = BASEFONT.deriveFont(BASEFONT.getStyle() ^ Font.ITALIC);
        } else if (getModel().isNewFile(this.settings.getNewFileInDays())) {
            font = BASEFONT.deriveFont(BASEFONT.getStyle() ^ Font.BOLD);
        }
        return font;
    }

    /**
     * Gets the fontColor.
     * <p>
     * @return Returns the fontColor.
     */
    public Color getFontColor() {
        this.fontColor = null;
        if (model.isNotValid()) {
            this.fontColor = Color.RED;
        } else if ((model instanceof Track) && (getMainFrame().getPlaylist().containsNext(model))) {
            this.fontColor = Color.BLUE;
        }
        return this.fontColor;
    }

    public AbstractJukesObject getModel() {
        return model;
    }

    /**
     * Returns the icon to represent this node and any overlays that should be
     * applied.
     * <p>
     * @param selected true if this node is currently selected
     * @return the Icon to display
     */
    public Icon getNodeIcon(boolean selected) {
        Icon icon = this.getIcon(true);
        if (icon == null) {
            icon = UIManager.getIcon(selected ? "Tree.openIcon" : "Tree.closedIcon");
        } else if (getModel().isNewFile(this.settings.getNewFileInDays())) {
            icon = ImageBlender.blendIcons(Resources.NODE_NEW_OVERLAY_ICON, icon, ImageBlender.BLEND_OPAQUE, null);
        } else if (!getModel().isValid()) {
            icon = ImageBlender.blendIcons(Resources.NODE_INVALID_OVERLAY_ICON, icon, ImageBlender.BLEND_OPAQUE, null);
        }
        return icon;
    }

    /**
     * Gets the presentation settings.
     * <p>
     * @return Returns the settings.
     */
    public Settings getSettings() {
        return this.settings;
    }

    public Object getUserObject() {
        return model;
    }

    /**
     * Sets the font for this tree node.
     * <p>
     * @param aFont The font to set.
     */
    public void setFont(Font aFont) {
        this.font = aFont;
    }

    /**
     * Sets the fontColor.
     * <p>
     * @param aFontColor The fontColor to set.
     */
    public void setFontColor(Color aFontColor) {
        this.fontColor = aFontColor;
    }

    /**
     * Sets the Settings into this node.
     * <p>
     * @param aSettings The settings to set.
     */
    public void setSettings(Settings aSettings) {
        this.settings = aSettings;
    }

    public void add(MutableTreeNode aNewChild) {
        super.add(aNewChild);
        Collections.sort(this.children, nodeComparator);
    }

    public Enumeration children() {
        loadChildren();
        return super.children();
    }

    /**
     * Delete this domain object and it's tree node.
     */
    public void delete() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting tree node: " + this.getName());
        }
        try {
            if (!MessageUtil.confirmDelete(this.getMainFrame())) {
                return;
            }
            GuiUtil.setBusyCursor(this.getMainFrame(), true);
            HibernateUtil.beginTransaction();
            if (getModel() instanceof Artist) {
                LOG.debug("Do nothing with the Artist");
            } else if (getModel() instanceof Disc) {
                final Disc disc = (Disc) getModel();
                final Artist artist = disc.getArtist();
                artist.getDiscs().remove(disc);
            } else if (getModel() instanceof Track) {
                final Track track = (Track) getModel();
                final Disc disc = track.getDisc();
                disc.getTracks().remove(track);
            } else {
                throw new IllegalArgumentException("Not a valid tree node type.");
            }
            HibernateDao.delete(getModel());
            HibernateUtil.commitTransaction();
            this.getMainModule().refreshSelection(null, Resources.NODE_DELETED);
        } catch (Exception ex) {
            LOG.error(Resources.getString("messages.ErrorDeletingArtist"), ex);
            final MainFrame mainFrame = (MainFrame) Application.getDefaultParentFrame();
            MessageUtil.showError(mainFrame, Resources.getString("messages.ErrorDeletingArtist"));
        } finally {
            GuiUtil.setBusyCursor(this.getMainFrame(), false);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractTreeNode)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        final AbstractTreeNode rhs = (AbstractTreeNode) obj;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(parent, rhs.parent);
        builder.append(model, rhs.model);
        return builder.isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(parent).append(model).toHashCode();
    }

    public String toString() {
        return model.getName();
    }

    /**
     * Gets the MainFrame for the application.
     * <p>
     * @return the MainFrame object
     */
    protected MainFrame getMainFrame() {
        return (MainFrame) Application.getDefaultParentFrame();
    }

    /**
     * Gets the MainModule for the application.
     * <p>
     * @return the MainModule object
     */
    protected MainModule getMainModule() {
        return getMainFrame().getMainModule();
    }
}
