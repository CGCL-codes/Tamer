package pms.whq.data;

import java.util.*;
import org.w3c.dom.*;
import pms.whq.Settings;
import pms.whq.util.XMLUtil;

/**
 *
 * @author psiegel
 */
public class Table implements EventList {

    protected String mName;

    protected List<MonsterContainer> mMonsters;

    protected List<EventEntry> mEvents;

    protected boolean mActive;

    public Table() {
        mName = "Table";
        mMonsters = new ArrayList();
        mEvents = new ArrayList();
        mActive = true;
    }

    /** Creates a new instance of Table */
    public Table(Node node) {
        this();
        mName = XMLUtil.getAttribute(node, "name");
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            addNodeEntry(children.item(i));
        }
    }

    public void addEntry(Object o) {
        if (o instanceof MonsterContainer) {
            mMonsters.add((MonsterContainer) o);
        } else if (o instanceof EventEntry) {
            mEvents.add((EventEntry) o);
        }
    }

    public void addEntries(Collection c) {
        Iterator i = c.iterator();
        while (i.hasNext()) {
            addEntry(i.next());
        }
    }

    public List getEntries() {
        List list = new ArrayList();
        list.addAll(mMonsters);
        list.addAll(mEvents);
        return list;
    }

    protected void addNodeEntry(Node node) {
        String type = node.getNodeName();
        String diceRoll = "";
        try {
            diceRoll = XMLUtil.getAttribute(node, "diceRoll");
        } catch (Exception e) {
        }
        if (type.equals("group")) {
            List entryList = new ArrayList();
            NodeList entryNodes = node.getChildNodes();
            for (int k = 0; k < entryNodes.getLength(); k++) {
                Object entry = nodeToEntry(entryNodes.item(k));
                if (entry != null) {
                    entryList.add(entry);
                }
            }
            MonsterContainer container = new MonsterContainer(diceRoll, entryList);
            mMonsters.add(container);
        } else if (type.equals("monster")) {
            mMonsters.add(new MonsterContainer(diceRoll, (MonsterEntry) nodeToEntry(node)));
        } else if (type.equals("event")) {
            mEvents.add((EventEntry) nodeToEntry(node));
        }
    }

    protected Object nodeToEntry(Node node) {
        String type = node.getNodeName();
        if (type == "monster") {
            return new MonsterEntry(node);
        } else if (type == "event") {
            return new EventEntry(node);
        }
        return null;
    }

    public int size() {
        return mMonsters.size() + mEvents.size();
    }

    public String getName() {
        return mName;
    }

    public Object getEntry() {
        Object entry = null;
        if (shouldGetMonster()) {
            entry = mMonsters.get((int) (Math.random() * (mMonsters.size() - 1))).getMonsters();
        } else if (mEvents.size() > 0) {
            entry = mEvents.get((int) (Math.random() * (mEvents.size() - 1)));
        }
        return entry;
    }

    private boolean shouldGetMonster() {
        boolean getMonster = false;
        if (mMonsters.size() > 0) {
            if (mEvents.size() > 0) {
                int eventProbablity = Settings.getSettingAsInt(Settings.EVENT_PROBABILITY);
                if ((Math.random() * 100) > eventProbablity) {
                    getMonster = true;
                }
            } else {
                getMonster = true;
            }
        }
        return getMonster;
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        mActive = active;
    }

    public Object getEntryForDiceRoll(String diceRoll) {
        if (shouldGetMonster()) {
            for (MonsterContainer monster : mMonsters) {
                if (diceRoll.equals(monster.getDiceRoll())) return monster.getMonsters();
            }
        } else {
            for (EventEntry event : mEvents) {
                if (diceRoll.equals(event.diceRoll)) return event;
            }
        }
        return null;
    }
}
