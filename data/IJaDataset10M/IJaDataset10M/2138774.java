package neon.core.modules;

import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import neon.core.*;
import java.awt.event.*;
import neon.util.fsm.Event;
import neon.util.fsm.State;
import neon.entities.creatures.Player;
import neon.entities.items.*;

public class RepairModule extends State implements KeyListener {

    private Player player;

    private JList items;

    private ArrayList<Item> listData;

    private JPanel panel;

    private JScrollPane scroller;

    public RepairModule(Engine engine, JScrollPane scroller, JPanel panel) {
        super(engine);
        this.panel = panel;
        this.scroller = scroller;
    }

    public void enter(Event t) {
        post(new Event("show", "text", "Use arrow keys to select item, press enter to repair, esc to exit."));
        items = new JList();
        items.setFocusable(false);
        scroller.setViewportView(items);
        items.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scroller.setBorder(new TitledBorder("Items in need of repair"));
        panel.addKeyListener(this);
        player = Engine.getPlayer();
        initItems();
    }

    public void exit(Event t) {
        panel.removeKeyListener(this);
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                parent.post(new Event("back"));
                break;
            case KeyEvent.VK_UP:
                if (items.getSelectedIndex() > 0) {
                    items.setSelectedIndex(items.getSelectedIndex() - 1);
                }
                break;
            case KeyEvent.VK_DOWN:
                items.setSelectedIndex(items.getSelectedIndex() + 1);
                break;
            case KeyEvent.VK_ENTER:
                try {
                    Object item = items.getSelectedValue();
                    if (item instanceof Clothing) {
                        ((Clothing) item).setState(100);
                    } else if (item instanceof Weapon) {
                        ((Weapon) item).setState(100);
                    }
                    initItems();
                    Engine.getUI().showMessage("Item repaired.", 2);
                } catch (ArrayIndexOutOfBoundsException f) {
                    Engine.getUI().showMessage("No item selected.", 2);
                }
                break;
        }
    }

    private void initItems() {
        listData = new ArrayList<Item>();
        for (Item item : player.inventory.getItems()) {
            if (item instanceof Weapon && ((Weapon) item).getState() < 100) {
                listData.add(item);
            } else if (item instanceof Clothing && ((Clothing) item).getState() < 100) {
                listData.add(item);
            }
        }
        items.setListData(listData.toArray());
        items.setSelectedIndex(0);
    }
}
