package tufts.vue;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JPanel;
import tufts.Util;
import tufts.vue.LWComponent.ChildKind;
import tufts.vue.NodeTool.SubTool;
import tufts.vue.NodeTool.SubTool.ShapeIcon;
import tufts.vue.VueTool.ToolIcon;
import tufts.vue.ibisimage.IBISImage;

public class IBISNodeTool extends VueTool implements VueConstants {

    private static IBISNodeTool singleton = null;

    /** the contextual tool panel **/
    private static IBISNodeToolPanel ibisNodeToolPanel;

    /** this constructed called via VueResources.properties init */
    public IBISNodeTool() {
        super();
        if (singleton != null) new Throwable("Warning: mulitple instances of " + this).printStackTrace();
        singleton = this;
        VueToolUtils.setToolProperties(this, "IBISNodeTool");
    }

    /** return the singleton instance of this class */
    public static IBISNodeTool getTool() {
        if (singleton == null) {
            new IBISNodeTool();
        }
        return singleton;
    }

    private static final Object LOCK = new Object();

    static IBISNodeToolPanel getIBISNodeToolPanel() {
        if (DEBUG.Enabled) tufts.Util.printStackTrace("deprecated");
        synchronized (LOCK) {
            if (ibisNodeToolPanel == null) {
                ibisNodeToolPanel = new IBISNodeToolPanel();
            }
        }
        return ibisNodeToolPanel;
    }

    public void setSelectedSubTool(VueTool tool) {
        super.setSelectedSubTool(tool);
        if (VUE.getSelection().size() > 0) {
            IBISSubTool imageTool = (IBISSubTool) tool;
            imageTool.getImageSetterAction().fire(this);
        }
    }

    public JPanel getContextualPanel() {
        return getIBISNodeToolPanel();
    }

    public static IBISNodeTool.IBISSubTool getActiveSubTool() {
        return (IBISSubTool) getTool().getSelectedSubTool();
    }

    @Override
    public Class getSelectionType() {
        return LWIBISNode.class;
    }

    public boolean supportsSelection() {
        return true;
    }

    private ImageIcon currentIcon;

    public void drawSelector(java.awt.Graphics2D g, ImageIcon i) {
        currentIcon = getActiveSubTool().getImageIcon();
    }

    /** @return an array of actions, with icon set, that will set the shape of selected
     * LWIBISNodes */
    public Action[] getIconSetterActions() {
        Action[] actions = new Action[getSubToolIDs().size()];
        Enumeration e = getSubToolIDs().elements();
        int i = 0;
        while (e.hasMoreElements()) {
            String id = (String) e.nextElement();
            IBISNodeTool.IBISSubTool ist = (IBISNodeTool.IBISSubTool) getSubTool(id);
            actions[i++] = ist.getImageSetterAction();
        }
        return actions;
    }

    /** @return an array of standard supported IBIS icons for nodes */
    public Object[] getAllIconValues() {
        Object[] values = new Object[getSubToolIDs().size()];
        Enumeration e = getSubToolIDs().elements();
        int i = 0;
        while (e.hasMoreElements()) {
            String id = (String) e.nextElement();
            IBISNodeTool.IBISSubTool ibnt = (IBISNodeTool.IBISSubTool) getSubTool(id);
            values[i++] = ibnt.getImageIcon();
        }
        return values;
    }

    public static class IBISNodeModeTool extends VueTool {

        private LWIBISNode creationNode = new LWIBISNode("", new tufts.vue.ibisimage.IBISIssueImage());

        private IBISImage[][] creationImages = initIBISImages();

        public IBISNodeModeTool() {
            super();
            creationNode.setAutoSized(false);
            setActiveWhileDownKeyCode(KeyEvent.VK_X);
        }

        private IBISImage[][] initIBISImages() {
            int maxPossStatuses = 5;
            String IbisTypes[] = VueResources.getStringArray("IBISNodeTool.subtools");
            IBISImage[][] IBIS_IMAGES = new IBISImage[IbisTypes.length][maxPossStatuses];
            int i = 0;
            for (int x = 0; x < IbisTypes.length; x++) {
                String IbisSubTypes[] = VueResources.getStringArray("IBISNodeTool." + IbisTypes[x] + ".subtypes");
                for (int y = 0; y < IbisSubTypes.length; y++) {
                    try {
                        if (IbisSubTypes[y] != null) IBIS_IMAGES[x][y] = (IBISImage) Class.forName(VueResources.getString("IBISNodeTool." + IbisSubTypes[y] + ".imageClass")).newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            return IBIS_IMAGES;
        }

        @Override
        public Class getSelectionType() {
            return LWIBISNode.class;
        }

        @Override
        public boolean handleMousePressed(MapMouseEvent e) {
            EditorManager.applyCurrentProperties(creationNode);
            return false;
        }

        @Override
        public boolean handleSelectorRelease(MapMouseEvent e) {
            final LWIBISNode node = (LWIBISNode) creationNode.duplicate();
            node.setAutoSized(false);
            node.setFrame(e.getMapSelectorBox());
            node.setLabel(VueResources.getString("newibisnode.html"));
            MapViewer viewer = e.getViewer();
            viewer.getFocal().addChild(node);
            VUE.getUndoManager().mark("New IBIS Node");
            VUE.getSelection().setTo(node);
            viewer.activateLabelEdit(node);
            return true;
        }

        @Override
        public void drawSelector(DrawContext dc, java.awt.Rectangle r) {
            dc.g.draw(r);
            creationNode.setFrame(r);
            creationNode.draw(dc);
        }

        /** @return a new node with the default VUE new-node label, initialized with a style from the current editor property states */
        public static LWIBISNode createNewNode() {
            return createNewNode(VueResources.getString("newibisnode.html"));
        }

        /** @return a new node with the given label, initialized with a style from the current editor property states */
        public static LWIBISNode createNewNode(String label) {
            LWIBISNode node = createDefaultNode(label);
            EditorManager.targetAndApplyCurrentProperties(node);
            return node;
        }

        /** @return a new node initialized to internal VUE defaults -- ignore tool states */
        public static LWIBISNode createDefaultNode(String label) {
            return new LWIBISNode(label);
        }

        public static LWText createRichTextNode(String text) {
            LWText node = buildRichTextNode(text);
            try {
                node.getRichLabelBox(true).overrideTextColor(FontEditorPanel.mTextColorButton.getColor());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            node.setAutoSized(false);
            node.setSize(150, 5);
            return node;
        }

        public static LWIBISNode createTextNode(String text) {
            LWIBISNode node = buildTextNode(text);
            EditorManager.targetAndApplyCurrentProperties(node);
            return node;
        }

        private static LWIBISNode initAsTextNode(LWIBISNode node) {
            if (node != null) node.setAsTextNode(true);
            return node;
        }

        public static LWIBISNode createDefaultTextNode(String text) {
            LWIBISNode node = new LWIBISNode();
            node.setLabel(text);
            initAsTextNode(node);
            return node;
        }

        public static LWIBISNode buildTextNode(String text) {
            return createDefaultTextNode(text);
        }

        public static LWText buildRichTextNode(String s) {
            LWText text = new LWText();
            text.setLabel(s);
            return text;
        }
    }

    public Class<? extends ImageIcon>[] getAllIconClasses() {
        Class<? extends ImageIcon>[] classes = new Class[getSubToolIDs().size()];
        int i = 0;
        for (Object o : getAllIconValues()) classes[i++] = ((ImageIcon) o).getClass();
        return classes;
    }

    public Class<? extends IBISImage>[] getAllImageClasses() {
        Class<? extends IBISImage>[] classes = new Class[getSubToolIDs().size()];
        int i = 0;
        for (Object o : getAllImageValues()) classes[i++] = ((IBISImage) o).getClass();
        return classes;
    }

    /** @return an array of standard supported IBIS images for nodes */
    public Object[] getAllImageValues() {
        Object[] values = new Object[getSubToolIDs().size()];
        Enumeration e = getSubToolIDs().elements();
        int i = 0;
        while (e.hasMoreElements()) {
            String id = (String) e.nextElement();
            IBISNodeTool.IBISSubTool ibnt = (IBISNodeTool.IBISSubTool) getSubTool(id);
            values[i++] = ibnt.getImage();
        }
        return values;
    }

    public IBISImage getNamedImage(String name) {
        if (mSubToolMap.isEmpty()) throw new Error("uninitialized sub-tools");
        for (VueTool t : mSubToolMap.values()) {
            if (name.equalsIgnoreCase(t.getAttribute("cssName"))) {
                return (IBISImage) ((IBISSubTool) t).getImage();
            }
        }
        return null;
    }

    /**
     * VueTool class for each of the specific IBIS node images.  Knows how to generate
     * an action for image setting, and creates a dynamic image based on the node type.
     */
    public static class IBISSubTool extends VueSimpleTool {

        private Class iconClass = null;

        private Class imageClass = null;

        private ImageIcon cachedIcon = null;

        private LWImage cachedImage = null;

        private VueAction imageSetterAction = null;

        public IBISSubTool() {
        }

        public void setID(String pID) {
            super.setID(pID);
            setGeneratedIcons(new IBISNodeIcon(getIconInstance()));
        }

        /** @return an action, with icon set, that will set the shape of selected
         * LWIBISNodes to the current image for this SubTool */
        public VueAction getImageSetterAction() {
            if (imageSetterAction == null) {
                imageSetterAction = new Actions.LWCAction(getToolName(), new IBISNodeIcon(getIconInstance())) {

                    void act(LWIBISNode n) {
                        n.setImageInstance((IBISImage) getImageInstance());
                    }
                };
                imageSetterAction.putValue("property.value", getImage());
            }
            return imageSetterAction;
        }

        public ImageIcon getIconInstance() {
            if (iconClass == null) {
                String iconClassName = getAttribute("iconClass");
                try {
                    this.iconClass = getClass().getClassLoader().loadClass(iconClassName);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            ImageIcon imgIcon = null;
            try {
                imgIcon = (ImageIcon) iconClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imgIcon;
        }

        public LWImage getImageInstance() {
            if (imageClass == null) {
                String imageClassName = getAttribute("imageClass");
                try {
                    this.imageClass = getClass().getClassLoader().loadClass(imageClassName);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            LWImage lwImg = null;
            try {
                lwImg = (LWImage) imageClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return lwImg;
        }

        /** @return true if given shape is of same type as us */
        public boolean isIcon(ImageIcon icon) {
            return icon != null && getImageIcon().getClass().equals(icon.getClass());
        }

        public ImageIcon getImageIcon() {
            if (cachedIcon == null) cachedIcon = getIconInstance();
            return cachedIcon;
        }

        public LWImage getImage() {
            if (cachedImage == null) cachedImage = getImageInstance();
            return cachedImage;
        }

        static final int nearestEven(double d) {
            if (Math.floor(d) == d && d % 2 == 1) return (int) d + 1;
            if (Math.floor(d) % 2 == 0) return (int) Math.floor(d); else return (int) Math.ceil(d);
        }

        static final int nearestOdd(double d) {
            if (Math.floor(d) == d && d % 2 == 0) return (int) d + 1;
            if (Math.floor(d) % 2 == 1) return (int) Math.floor(d); else return (int) Math.ceil(d);
        }

        private static int sWidth;

        private static int sHeight;

        static {
            if (ToolIcon.Width % 2 == 0) sWidth = nearestOdd(ToolIcon.Width / 2); else sWidth = nearestEven(ToolIcon.Width / 2);
            if (ToolIcon.Height % 2 == 0) sHeight = nearestOdd(ToolIcon.Height / 2); else sHeight = nearestEven(ToolIcon.Height / 2);
            sHeight--;
        }

        public static class IBISNodeIcon extends ImageIcon {

            public int getIconWidth() {
                return sWidth;
            }

            public int getIconHeight() {
                return sHeight;
            }

            private ImageIcon mIcon;

            public IBISNodeIcon(ImageIcon pIcon) {
                mIcon = pIcon;
            }

            public IBISNodeIcon(IBISImage pImage) {
                mIcon = pImage.getIcon();
            }

            public void setIcon(ImageIcon pIcon) {
                mIcon = pIcon;
            }

            public ImageIcon getIcon() {
                return mIcon;
            }

            public void paintIcon(Component c, Graphics g, int x, int y) {
                super.paintIcon(c, g, x, y);
            }

            public String toString() {
                return "IBISNodeIcon[" + sWidth + "x" + sHeight + " " + mIcon + "]";
            }
        }
    }
}
