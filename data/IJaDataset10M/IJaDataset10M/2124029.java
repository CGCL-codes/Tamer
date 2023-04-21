package jphotoshop.action;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import jphotoshop.filter.PicFilter;
import jphotoshop.ui.EditFrameManager;
import jphotoshop.ui.UICommands;
import jphotoshop.util.LocalBundle;
import jphotoshop.util.ResourceBundleUtil;
import org.jdesktop.application.AbstractBean;

/**
 * @author liuke
 * @email: soulnew@gmail.com
 */
public class FilterManager extends AbstractBean implements PropertyChangeListener, ActionManager {

    static JMenu filterMenu;

    public static void main(String[] arg) {
    }

    PicFilter currentFilter;

    EditFrameManager editFrameManager;

    ArrayList<PicFilter> filterList = new ArrayList<PicFilter>();

    ResourceBundleUtil labels;

    public FilterManager(EditFrameManager editFrameManager) {
        this.editFrameManager = editFrameManager;
        editFrameManager.addPropertyChangeListener(this);
        labels = ResourceBundleUtil.getLAFBundle("resource.LocalMenuBar");
        iniMenu();
    }

    public JMenu createMenu() {
        if (filterMenu == null) {
            iniMenu();
        }
        return filterMenu;
    }

    public JMenuItem getPreItem() {
        return null;
    }

    public void iniMenu() {
        filterMenu = new JMenu();
        labels.configureMenu(filterMenu, "Filter");
        initFilters();
    }

    private void initFilters() {
        LocalBundle lb = new LocalBundle("resource.filters");
        String kinds = lb.getString("filters.Kinds");
        StringTokenizer kindsName = new StringTokenizer(kinds, ",");
        PicFilter filter = null;
        JMenu inMenu;
        while (kindsName.hasMoreTokens()) {
            String kindName = kindsName.nextToken();
            inMenu = new JMenu(lb.getString(kindName + ".Name"));
            String names = lb.getString(kindName + ".Kinds");
            StringTokenizer stringName = new StringTokenizer(names, ",");
            while (stringName.hasMoreTokens()) {
                String cname = null;
                try {
                    cname = stringName.nextToken();
                    String temp = cname;
                    temp = "jphotoshop.filter." + lb.getString(cname);
                    java.lang.Class toolClass = java.lang.Class.forName(temp);
                    filter = (PicFilter) toolClass.newInstance();
                    filter.setEditFrameManager(editFrameManager);
                    filterList.add(filter);
                    inMenu.add(labels.createMenuItem(filter, lb.getString(cname + ".Name")));
                    inMenu.addSeparator();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(cname);
                }
            }
            filterMenu.add(inMenu);
        }
        this.currentFilter = filter;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String cmd = evt.getPropertyName();
        if (UICommands.imageOpened.equals(cmd)) {
            for (PicFilter filter : filterList) {
                filter.setEnabled(true);
            }
        } else if (UICommands.allImageClose.equals(cmd)) {
            for (PicFilter filter : filterList) {
                filter.setEnabled(false);
            }
        }
    }
}
