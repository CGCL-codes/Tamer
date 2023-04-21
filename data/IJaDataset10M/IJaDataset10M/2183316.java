package de.kopis.jusenet.ui.models;

import javax.swing.DefaultListModel;
import de.kopis.jusenet.nntp.Group;
import de.kopis.jusenet.nntp.listeners.GroupListener;

public class GroupListModel extends DefaultListModel implements GroupListener {

    public void groupLoaded(Group g) {
        addElement(g);
    }

    public void articlesLoaded(Group g) {
    }
}
