package hu.rsc.svnAdmin.console;

import hu.rsc.svnAdmin.engine.Engine;
import java.util.HashMap;

/**
 *
 * @author arbuckle
 */
public class SubMenu {

    private MenuAction currentAction;

    private String label;

    private HashMap<Integer, MenuAction> items = new HashMap<Integer, MenuAction>();

    public SubMenu(String label) {
        this.label = label;
        items.put(Menu.exit.getId(), Menu.exit);
        items.put(Menu.back.getId(), Menu.back);
    }

    public void addItem(MenuAction action) {
        items.put(action.getId(), action);
    }

    public void activate() {
        System.out.println(label + Engine.NL);
        for (Integer id : items.keySet()) {
            System.out.println(id + ". " + items.get(id).getLabel());
        }
        System.out.println("Select an option:" + Engine.NL);
    }

    public void actionPerformed(String line) {
        if (currentAction == null) {
            try {
                currentAction = items.get(Integer.parseInt(line));
            } catch (NumberFormatException e) {
                System.out.println("Wrong value" + Engine.NL);
            }
        }
        currentAction.actionPerformed(line);
    }
}
