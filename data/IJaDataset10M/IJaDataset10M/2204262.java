package net.sf.warpcore.cms.webfrontend.gui.renderer;

import net.sf.wedgetarian.*;
import net.sf.wedgetarian.tree.*;
import net.sf.wedgetarian.event.*;
import net.sf.warpcore.cms.webfrontend.*;
import net.sf.warpcore.cms.webfrontend.gui.*;
import net.sf.warpcore.cms.webfrontend.gui.sorting.*;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;

public class ComplexTreeTypeRenderer implements ComplexTreeCellRenderer {

    private static Category category = Category.getInstance(ComplexTreeTypeRenderer.class.getName());

    Object rendereringModule;

    MimetypeKit mimetypeKit;

    private static final String resourcePath = "classpath:///net/sf/warpcore/cms/webfrontend/gui/resources/browser/";

    private static final Image ascendingImage = new Image(resourcePath + "asc.gif");

    private static final Image ascendingDisabledImage = new Image(resourcePath + "asc_disabled.gif");

    private static final Image descendingImage = new Image(resourcePath + "desc.gif");

    private static final Image descendingDisabledImage = new Image(resourcePath + "desc_disabled.gif");

    private static final Image sortImage = new Image(resourcePath + "type.gif");

    private static final Image sortDisabledImage = new Image(resourcePath + "type_disabled.gif");

    /**
     * Anonymer Konstruktor: Das Modul kann keine Spezialrenderingoptions nutzen.
     */
    public ComplexTreeTypeRenderer() {
        this(null);
    }

    /**
     * Das Modul ist hat sich angemeldet und kann dann auch besonders behandelt werden.
     * @param rendereringModule Refernz auf das zu rendernde Objekt
     */
    public ComplexTreeTypeRenderer(Object rendereringModule) {
        this.rendereringModule = rendereringModule;
        try {
            mimetypeKit = ((Cockpit) Application.getCurrentApplication()).mimetypeKit;
        } catch (ClassCastException exception) {
            if (category.isEnabledFor(Priority.ERROR)) category.error(exception);
            mimetypeKit = new MimetypeKit(Application.getCurrentApplication());
        }
    }

    /**
     * �bergibt die Referenz auf das zu Rendernde-Modul.
     * Wenn dieses nicht gesetzt wurde, kommt <code>null</code> zur�ck.
     */
    public Object getRenderingModule() {
        return rendereringModule;
    }

    /**
     * Im Interface 'ComplexTreeCellRenderer' vorgeschriebene 'normale' Render-Methoden.
     * Unterscheidet anhand des gesetzten <code>rendereringModule</code>s wie gerendert werden soll.
     */
    public Wedget getTreeCellWedget(ComplexTree cTree, Object object, int row, int column, boolean expaned, boolean leaf) {
        if (row == 0) {
            Panel p = new Panel();
            Button sortByTypeButton = new Button(sortImage, sortDisabledImage);
            sortByTypeButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ContentBrowser cb = ((Cockpit) Application.getCurrentApplication()).getNavigator().getActiveContentBrowser();
                    cb.getComparator().setComparatorField(DOMPerignonTreeNodeComparator.TYPE);
                    cb.reload();
                }
            });
            p.add(sortByTypeButton);
            Button sortAscButton = new Button(ascendingImage, ascendingDisabledImage);
            sortAscButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ContentBrowser cb = ((Cockpit) Application.getCurrentApplication()).getNavigator().getActiveContentBrowser();
                    cb.getComparator().setOrderDirection(DOMPerignonTreeNodeComparator.ASCENDING);
                    cb.reload();
                }
            });
            p.add(sortAscButton);
            Button sortDescButton = new Button(descendingImage, descendingDisabledImage);
            sortDescButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ContentBrowser cb = ((Cockpit) Application.getCurrentApplication()).getNavigator().getActiveContentBrowser();
                    cb.getComparator().setOrderDirection(DOMPerignonTreeNodeComparator.DESCENDING);
                    cb.reload();
                }
            });
            p.add(sortDescButton);
            return p;
        }
        try {
            DOMPerignonTreeNode node = (DOMPerignonTreeNode) object;
            if (mimetypeKit != null) {
                if (node.getType() == MimetypeKit.FOLDER) {
                    return new Label(mimetypeKit.getNameFor(node.getType()));
                } else if (node.getType() == MimetypeKit.FILE || node.getType() == MimetypeKit.EXTERNAL_FILE) {
                    String mt = "null";
                    if ((mimetypeKit.getMimetype(node.getName())) != null) {
                        mt = mimetypeKit.getMimetype(node.getName());
                        return new Label(mimetypeKit.getNameFor(mt));
                    } else {
                        return new Label(mimetypeKit.getNameFor(node.getType()));
                    }
                } else {
                    return new Label(mimetypeKit.getNameFor(node.getType()));
                }
            }
        } catch (ClassCastException exception) {
            if (category.isEnabledFor(Priority.ERROR)) category.error(exception);
        }
        return new Label("&nbsp;");
    }

    public Image getTreeRowIcon(ComplexTree cTree, Object object, int row, boolean expaned, boolean leaf) {
        try {
            DOMPerignonTreeNode node = (DOMPerignonTreeNode) object;
            return mimetypeKit.getImageFor(node.getType());
        } catch (ClassCastException exception) {
            if (category.isEnabledFor(Priority.ERROR)) category.error(exception);
            if (leaf) {
                return mimetypeKit.getImageFor(mimetypeKit.FOLDER);
            } else {
                return mimetypeKit.getImageFor(mimetypeKit.UNDEFINED);
            }
        }
    }
}
