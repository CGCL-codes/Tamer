package com.sptci.cms.admin.view.tree;

import com.sptci.echo.tree.TreeCellRenderer;
import com.sptci.echo.style.Font;
import static com.sptci.echo.Application.getApplication;
import static com.sptci.ReflectionUtility.execute;
import nextapp.echo.extras.app.tree.TreePath;
import nextapp.echo.extras.app.tree.TreeLayoutData;
import nextapp.echo.extras.app.tree.DefaultTreeCellRenderer;
import nextapp.echo.extras.app.Tree;
import nextapp.echo.app.Component;
import nextapp.echo.app.Label;
import nextapp.echo.app.Insets;
import static nextapp.echo.app.Alignment.ALIGN_CENTER;
import static nextapp.echo.app.Color.WHITE;
import static echopoint.util.ColorKit.makeColor;
import java.lang.reflect.InvocationTargetException;

/**
 * The renderer used to render the header of the node tree table.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-12-07
 * @version $Id: HeaderCellRenderer.java 28 2010-02-02 20:08:42Z spt $
 */
public class HeaderCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getTreeCellRendererComponent(final Tree tree, final TreePath treePath, final Object value, final int column, final int row, final boolean leaf) {
        final Component component = super.getTreeCellRendererComponent(tree, treePath, value, column, row, leaf);
        setStyle(component);
        setLayoutData(component);
        return component;
    }

    protected void setStyle(final Component component) {
        component.setForeground(WHITE);
        component.setFont(Font.getInstance(Font.BOLD));
    }

    protected void setLayoutData(final Component component) {
        final TreeLayoutData layout = new TreeLayoutData();
        layout.setAlignment(ALIGN_CENTER);
        layout.setBackground(makeColor("#2d2d2d"));
        layout.setInsets(new Insets(5, 0, 5, 0));
        component.setLayoutData(layout);
    }
}
