package net.dadajax.gui.swing;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import net.dadajax.download.Link;
import net.dadajax.gui.LinkList;

/**
 * @author dadajax
 *
 */
public class SwingLinkList implements LinkList {

    JList list;

    SwingLinkListModel model;

    public SwingLinkList() {
        model = new SwingLinkListModel();
        list = new JList(model);
    }

    @Override
    public void add(String link) {
        model.add(link);
    }

    @Override
    public void add(List<String> links) {
        model.addAll(links);
    }

    @Override
    public List<Link> getLinks() {
        return new ArrayList<Link>();
    }

    @Override
    public void removeAll() {
        model.clearAll();
    }

    @Override
    public void removeSelected() {
    }

    public JList getJList() {
        return list;
    }
}
