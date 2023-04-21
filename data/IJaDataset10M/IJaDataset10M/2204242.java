package com.datas.component;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import com.datas.bean.model.system.SysTabpane;
import com.datas.component.common.CommonTabbedPane;
import com.datas.component.panel.FieldPanel;
import com.datas.form.modul.manager.DetailModulManager;
import com.datas.form.modul.manager.ModulManager;

/**
 * @author kimi
 * 
 */
@SuppressWarnings("serial")
public class TabbedPane extends CommonTabbedPane {

    private ModulManager modul;

    private DetailModulManager detailModul;

    private SortedMap<String, SysTabpane> tabpaneMap = new TreeMap<String, SysTabpane>();

    private SortedMap<String, FieldPanel> tabPanelMap = new TreeMap<String, FieldPanel>();

    public TabbedPane(ModulManager modul) {
        this.modul = modul;
        SysTabpane tabpane = new SysTabpane();
        tabpane.setIdModul(modul.getModel().getSysModul().getIdModul());
        tabpane.setIdTable("0");
        loadTabbedPanes(tabpane);
        init();
    }

    public TabbedPane(DetailModulManager detailModul) {
        this.detailModul = detailModul;
        this.modul = (ModulManager) detailModul.getModul();
        SysTabpane tabpane = new SysTabpane();
        tabpane.setIdModul(this.detailModul.getModul().getModel().getSysModul().getIdModul());
        tabpane.setIdTable(this.detailModul.getIdTable());
        loadTabbedPanes(tabpane);
        init();
    }

    @Override
    protected void init() {
        setLayout();
        setBehavior();
        setLookAndFeel();
    }

    @Override
    protected void setBehavior() {
    }

    @Override
    protected void setLayout() {
        for (Iterator<String> it = tabpaneMap.keySet().iterator(); it.hasNext(); ) {
            SysTabpane sysTabpane = tabpaneMap.get(it.next());
            FieldPanel fieldPanel = new FieldPanel(modul.getModel().getLayoutItems());
            fieldPanel.setName(sysTabpane.getIdTabpane() + sysTabpane.getNumTabpane());
            tabPanelMap.put(sysTabpane.getIdTabpane() + sysTabpane.getNumTabpane(), fieldPanel);
            this.addTab(sysTabpane.getCaption(), fieldPanel);
        }
    }

    @Override
    protected void setLookAndFeel() {
    }

    private void loadTabbedPanes(SysTabpane tabpane) {
        List<SysTabpane> items = getServiceContainer().getSystemService().selectList(tabpane);
        for (SysTabpane item : items) {
            tabpaneMap.put(item.getIdTabpane() + item.getNumTabpane(), item);
        }
    }

    public SortedMap<String, SysTabpane> getTabpaneConfigMap() {
        return tabpaneMap;
    }

    public SortedMap<String, FieldPanel> getTabpanePanelMap() {
        return tabPanelMap;
    }
}
