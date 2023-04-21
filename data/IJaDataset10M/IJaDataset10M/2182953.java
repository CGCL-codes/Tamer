package net.sourceforge.fraglets.aotools.model;

import java.util.*;
import java.io.*;
import net.sourceforge.fraglets.aotools.codec.NanoListDecoder;

/**
 *
 * @author  sas
 * @version 
 */
public class BaseNanoList {

    private static BaseNanoList instance = new BaseNanoList();

    List allNano;

    List selectedNano;

    BaseNanoListModel listModel;

    /** Creates new BaseNanoList */
    private BaseNanoList() {
        allNano = new ArrayList();
        selectedNano = allNano;
        try {
            new NanoListDecoder(this).decode(getClass().getResource("DefaultBaseNanoList.xml"));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("loading default base nano list failed: " + ex);
        }
        listModel = new BaseNanoListModel();
    }

    public static BaseNanoList getBaseNanoList() {
        return instance;
    }

    public void save() {
        try {
            File f = new File("BaseNanoList.ser");
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(allNano);
        } catch (Exception ex) {
            System.out.println("Error in saving BaseNanoList.ser");
            ex.printStackTrace();
        }
    }

    public void load() {
        try {
            File f = new File("BaseNanoList.ser");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
            allNano = (List) in.readObject();
            listModel.update();
        } catch (Exception ex) {
            allNano = new ArrayList();
            System.out.println("Error in loading BaseNanoList.ser");
            ex.printStackTrace();
        }
    }

    public void print() {
        BaseNanoCluster b = null, next = null;
        String[] type = new String[3];
        try {
            sort();
            File f = new File("BaseNanoList.txt");
            PrintStream out = new PrintStream(new FileOutputStream(f));
            out.println(formatString("Nano Cluster", 15) + formatString("Skill", 15) + formatString("Faded", 10) + formatString("Bright", 10) + formatString("Shining", 10));
            out.println("-----------------------------------------------------------");
            Iterator i = getAllNanos();
            while (i.hasNext()) {
                if (next == null) b = (BaseNanoCluster) i.next(); else b = next;
                type[0] = type[1] = type[2] = "  ";
                for (int j = 0; j < 3; j++) {
                    int t = b.getType();
                    if (t == 3) {
                        next = null;
                        break;
                    }
                    type[t] = Body.SLOT_NAMES[b.getBodyLoc()];
                    if (!i.hasNext()) break;
                    next = (BaseNanoCluster) i.next();
                    if (!(b.getName().equals(next.getName()))) {
                        break;
                    }
                    b = next;
                }
                out.print(formatString(b.getName(), 14) + " " + formatString(b.getSkill(), 14) + " ");
                out.println(formatString(type[0], 9) + " " + formatString(type[1], 9) + " " + formatString(type[2], 9));
            }
        } catch (Exception ex) {
            System.out.println("Error in prining BaseNanoList.txt");
            ex.printStackTrace();
        }
    }

    private String formatString(String name, int len) {
        String space = "                    ";
        if (name.length() > len) {
            return name.substring(0, len);
        } else {
            return name.concat(space.substring(0, len - name.length()));
        }
    }

    public void addNano(BaseNanoCluster b) {
        if (!allNano.contains(b)) {
            allNano.add(b);
            if (selectedNano != allNano) {
                selectedNano.add(b);
            }
            if (listModel != null) {
                listModel.update();
            }
        }
    }

    public void sort() {
        Object[] ob = allNano.toArray();
        Arrays.sort(ob, 0, ob.length, new Comparator() {

            public int compare(Object o1, Object o2) {
                if (!(o1 instanceof BaseNanoCluster) && (o2 instanceof BaseNanoCluster)) {
                    System.err.println("sort: Wrong elements in BaseNanoList!");
                    return 0;
                }
                BaseNanoCluster b1 = (BaseNanoCluster) o1;
                BaseNanoCluster b2 = (BaseNanoCluster) o2;
                return b1.getName().compareTo(b2.getName());
            }
        });
        allNano = selectedNano = new ArrayList();
        for (int i = 0; i < ob.length; i++) {
            allNano.add(ob[i]);
        }
        System.err.println("sort: done");
        listModel.update();
    }

    public void removeNano(BaseNanoCluster b) {
        int i = allNano.indexOf(b);
        if (i != -1) {
            allNano.remove(i);
            if (allNano == selectedNano) {
                listModel.update();
                return;
            }
        }
        i = selectedNano.indexOf(b);
        if (i != -1) {
            selectedNano.remove(i);
            listModel.update();
        }
    }

    public Iterator getAllNanos() {
        return allNano.iterator();
    }

    public BaseNanoCluster get(int i) {
        return (BaseNanoCluster) selectedNano.get(i);
    }

    public void showType(int type) {
        selectedNano = new ArrayList();
        Iterator i = getAllNanos();
        while (i.hasNext()) {
            BaseNanoCluster b = (BaseNanoCluster) i.next();
            if (b.getType() == type) {
                selectedNano.add(b);
            }
        }
        listModel.update();
    }

    public void showLoc(int loc) {
        selectedNano = new ArrayList();
        Iterator i = getAllNanos();
        while (i.hasNext()) {
            BaseNanoCluster b = (BaseNanoCluster) i.next();
            if (b.getBodyLoc() == loc) {
                selectedNano.add(b);
            }
        }
        listModel.update();
    }

    public void showAll() {
        selectedNano = allNano;
        listModel.update();
    }

    public int size() {
        return selectedNano.size();
    }

    public BaseNanoListModel getListModel() {
        return listModel;
    }
}
