package saadadb.admin.popup;

import java.awt.Frame;
import javax.swing.JMenuItem;
import saadadb.admin.dmmapper.MapperDemo;
import saadadb.collection.Category;
import saadadb.database.Database;
import saadadb.exceptions.FatalException;

public class PopupClassNode extends PopupNode {

    /**
	 *  * @version $Id: PopupClassNode.java 118 2012-01-06 14:33:51Z laurent.mistahl $

	 */
    private static final long serialVersionUID = 1L;

    public PopupClassNode(Frame frame, Object[] tree_path_components, String title) {
        super(frame, tree_path_components, title);
        JMenuItem item;
        item = new JMenuItem(SHOW_CLASS);
        item.addActionListener(this);
        this.add(item);
        item = new JMenuItem(SHOW_CONTENT);
        item.addActionListener(this);
        this.add(item);
        this.addSeparator();
        boolean rel_found = false;
        try {
            for (String rel : Database.getCachemeta().getRelationNamesStartingFromColl(tree_path_components[1].toString(), Category.getCategory(tree_path_components[2].toString()))) {
                item = new JMenuItem(STARTING_RELATIONSHIP + ": " + rel);
                item.addActionListener(this);
                this.add(item);
                rel_found = true;
            }
            for (String rel : Database.getCachemeta().getRelationNamesEndingOnColl(tree_path_components[1].toString(), Category.getCategory(tree_path_components[2].toString()))) {
                item = new JMenuItem(ENDING_RELATIONSHIP + ": " + rel);
                item.addActionListener(this);
                rel_found = true;
                this.add(item);
            }
        } catch (FatalException e) {
        }
        if (!rel_found) {
            item = new JMenuItem("No Relationship");
            item.setEnabled(false);
            this.add(item);
        }
        this.addSeparator();
        item = new JMenuItem(BUILD_INDEX);
        item.addActionListener(this);
        this.add(item);
        item = new JMenuItem(DELETE_CLASS);
        this.add(item);
        item.addActionListener(this);
        if (tree_path_components[2].toString().equals("ENTRY")) {
            item.setText(item.getText() + " (use TABLE category)");
            item.setEnabled(false);
        }
        if (frame instanceof MapperDemo) {
            this.addSeparator();
            item = new JMenuItem(MAPP_DM);
            item.addActionListener(this);
            this.add(item);
        }
    }
}
