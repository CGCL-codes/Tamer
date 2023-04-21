package gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import data.controls.Game;
import ogv.OGV;

public class ViewBroadcast extends JPanel implements ViewInterface {

    private final Game game;

    private final JList jList;

    private final List<String> list;

    public ViewBroadcast() {
        game = OGV.getGame();
        list = game.getBroadcast();
        ListModel listModel = new AbstractListModel() {

            @Override
            public int getSize() {
                return list.size();
            }

            @Override
            public Object getElementAt(int i) {
                return list.get(i);
            }
        };
        jList = new JList(listModel);
        initGUI();
    }

    @Override
    public String getTitle() {
        return "Broadcast";
    }

    @Override
    public String getIconName() {
        return OGV.getConfig().getString("Actions." + CommandsGlobal.VIEW_BROADCAST + ".Icon", null);
    }

    @Override
    public void setFocus() {
        jList.requestFocusInWindow();
    }

    @Override
    public void update() {
    }

    private void initGUI() {
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(jList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
    }
}
