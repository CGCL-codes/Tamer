package com.bluebrim.gui.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import com.bluebrim.base.shared.CoObjectIF;
import com.bluebrim.base.shared.debug.CoAssertion;
import com.bluebrim.swing.client.CoPanel;
import com.bluebrim.swing.client.CoTabbedPane;

/**
 * Abstract superclass for ui models that displays a serverbased object 
 * in a tab ui. This ui is optimized in the respect that it doesn't create 
 * the ui models for the separate tabs until they are asked for.
 * <p>
 * This means that the ui will open up faster but that it will take longer the
 * the first time a new tab is selected. A further optimization for speed would
 * be to create the other ui's in a separate thread while the first tab is activated.
 * <p>
 * Creation date: (2000-03-13 21:51:58)
 * @author: Lasse Svad�ngs	
 */
public abstract class CoAbstractTabUI extends CoDomainUserInterface implements CoTabUI {

    private CoTabbedPane m_tabPane;

    private List m_tabData;

    private List m_visibleTabData;

    private CoServerTabData m_serverTabData;

    private boolean m_isInitialized = false;

    public abstract static class TabData extends CoAbstractUserInterfaceData {

        public TabData(Object key, Icon icon, String name) {
            this(key, icon, name, false);
        }

        public TabData(Object key, Icon icon, String name, boolean prebuild) {
            super(key, icon, name, prebuild);
        }
    }

    ;

    public CoAbstractTabUI() {
        this(null);
    }

    public CoAbstractTabUI(CoObjectIF aDomainObject) {
        super(aDomainObject);
        m_tabData = new ArrayList();
        m_visibleTabData = new ArrayList();
    }

    protected final void addTabData(CoAbstractUserInterfaceData tabData) {
        m_tabData.add(tabData);
        m_visibleTabData.add(tabData);
    }

    protected CoTabbedPane createTabbedPane(CoUserInterfaceBuilder builder) {
        m_tabPane = new CoTabbedPane() {

            public void setSelectedIndex(int index) {
                if (isInitialized()) {
                    int oldIndex = getModel().getSelectedIndex();
                    if (oldIndex != index) {
                        handleTabSelectionChange(oldIndex, index);
                    }
                    super.setSelectedIndex(index);
                }
            }

            public Dimension getPreferredSize() {
                Dimension prefSize = super.getPreferredSize();
                if (prefSize.width < 800) prefSize.width = 800;
                if (prefSize.height < 600) prefSize.height = 600;
                return prefSize;
            }
        };
        builder.prepareTabbedPane(m_tabPane);
        createTabs(m_tabPane, builder);
        return m_tabPane;
    }

    protected void createTabs(CoTabbedPane tabPane, CoUserInterfaceBuilder builder) {
        int tabCount = m_tabData.size();
        for (int i = 0; i < tabCount; i++) {
            CoAbstractUserInterfaceData iTabData = getTabDataAt(i);
            tabPane.addTab(iTabData.getName(), iTabData.getIcon(), builder.createSubcanvas(iTabData.getKey()));
        }
        m_isInitialized = true;
    }

    protected void createValueModels(CoUserInterfaceBuilder builder) {
        super.createValueModels(builder);
        int tabCount = m_tabData.size();
        for (int i = 0; i < tabCount; i++) {
            final CoAbstractUserInterfaceData iTabData = getTabDataAt(i);
            builder.createSubcanvasAdaptor(builder.addAspectAdaptor(new CoReadOnlyAspectAdaptor(this, iTabData.getKey().toString()) {

                public Object get(CoObjectIF subject) {
                    return iTabData.getValueFor(subject);
                }
            }), (CoSubcanvas) getNamedWidget(iTabData.getKey()));
        }
    }

    protected void createWidgets(CoPanel panel, CoUserInterfaceBuilder builder) {
        super.createWidgets(panel, builder);
        panel.add(createTabbedPane(builder), BorderLayout.CENTER);
    }

    protected void doAfterCreateUserInterface() {
        super.doAfterCreateUserInterface();
        int index = getTabPane().getSelectedIndex();
        if (index != -1) getTabModelAt(index).valueHasChanged(); else {
            getTabPane().setSelectedIndex(0);
        }
        new CoFloatingTabsManager(this, getMenuBuilder(), true);
    }

    protected void doBeforeCreateUserInterface() {
        super.doBeforeCreateUserInterface();
        initializeTabData();
    }

    public int[] getHiddenTabs() {
        int noOfHiddenTabs = m_tabData.size() - m_visibleTabData.size();
        int hidden[] = new int[noOfHiddenTabs];
        int count = 0;
        for (int i = 0; i < m_tabData.size(); i++) {
            if (m_visibleTabData.contains(m_tabData.get(i))) continue; else hidden[count++] = i;
        }
        return hidden;
    }

    protected final CoSubcanvas getSubcanvasAt(int index) {
        return (CoSubcanvas) getNamedWidget(getTabDataAt(index).getKey());
    }

    protected abstract CoServerTabData getTabData();

    protected final CoAbstractUserInterfaceData getTabData(Object key) {
        int I = m_tabData.size();
        for (int i = 0; i < I; i++) {
            CoAbstractUserInterfaceData tabData = (CoAbstractUserInterfaceData) m_tabData.get(i);
            if (tabData.getKey().equals(key)) {
                return tabData;
            }
        }
        return null;
    }

    public final CoAbstractUserInterfaceData getTabDataAt(int index) {
        return (CoAbstractUserInterfaceData) m_visibleTabData.get(index);
    }

    protected final CoValueable getTabModelAt(int index) {
        return getNamedValueModel(getTabDataAt(index).getKey());
    }

    protected abstract Object[] getTabOrder();

    protected final CoTabbedPane getTabPane() {
        return m_tabPane;
    }

    protected final CoDomainUserInterface getUserInterfaceAt(int index) {
        if (CoAssertion.TRACE) System.out.println("--->getUserInterfaceAt(" + index + ")");
        return getTabDataAt(index).getUserInterface();
    }

    protected void handleTabSelectionChange(int oldIndex, int index) {
        setBusy(true);
        try {
            fireTabSelectionChangeEvent(new CoTabSelectionChangeEvent(this, index, oldIndex, true));
            if (oldIndex != -1) {
                CoDomainUserInterface oldUI = getUserInterfaceAt(oldIndex);
                CoSubcanvas oldSubcanvas = getSubcanvasAt(oldIndex);
                oldSubcanvas.setUserInterface(null);
                oldUI.setDomain(null);
            }
            CoDomainUserInterface newUI = getUserInterfaceAt(index);
            CoSubcanvas newSubcanvas = getSubcanvasAt(index);
            newSubcanvas.setUserInterface(newUI);
            CoValueable tabModel = getTabModelAt(index);
            if (tabModel != null) tabModel.valueHasChanged();
            fireTabSelectionChangeEvent(new CoTabSelectionChangeEvent(this, index, oldIndex, false));
        } finally {
            setBusy(false);
        }
    }

    protected void hideTab(int index) {
        CoDomainUserInterface oldUI = getUserInterfaceAt(index);
        CoSubcanvas oldSubcanvas = getSubcanvasAt(index);
        oldSubcanvas.setUserInterface(null);
        oldUI.setDomain(null);
        m_tabPane.removeTabAt(index);
        m_visibleTabData.remove(index);
        if (m_tabPane.getSelectedIndex() == index) {
            CoDomainUserInterface newUI = getUserInterfaceAt(index);
            CoSubcanvas newSubcanvas = getSubcanvasAt(index);
            newSubcanvas.setUserInterface(newUI);
        }
    }

    protected int indexOfTabData(CoAbstractUserInterfaceData tabData) {
        return m_visibleTabData.indexOf(tabData);
    }

    protected void initializeTabData() {
        CoServerTabData tabData = tabData();
        Object tabOrder[] = getTabOrder();
        for (int i = 0; i < tabOrder.length; i++) addTabData(tabData.getTabDataFor(tabOrder[i]));
    }

    public final boolean isInitialized() {
        return m_isInitialized;
    }

    protected void showTab(CoAbstractUserInterfaceData tabData) {
        if (!m_visibleTabData.contains(tabData)) {
            m_visibleTabData.add(tabData);
            m_tabPane.addTab(tabData.getName(), tabData.getIcon(), getNamedWidget(tabData.getKey()));
        }
    }

    protected CoServerTabData tabData() {
        if (m_serverTabData == null) m_serverTabData = getTabData();
        return m_serverTabData;
    }

    public void addTabSelectionChangeListener(CoTabSelectionChangeListener listener) {
        m_listenerList.add(CoTabSelectionChangeListener.class, listener);
    }

    private void fireTabSelectionChangeEvent(CoTabSelectionChangeEvent event) {
        Object listeners[] = m_listenerList.getListenerList();
        Class listenerClass = CoTabSelectionChangeListener.class;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == listenerClass) {
                ((CoTabSelectionChangeListener) listeners[i + 1]).tabSelectionChange(event);
            }
        }
    }

    public void removeTabSelectionChangeListener(CoTabSelectionChangeListener listener) {
        m_listenerList.remove(CoTabSelectionChangeListener.class, listener);
    }

    protected void hideTab(CoAbstractUserInterfaceData tabData) {
        int visiblePos = m_visibleTabData.indexOf(tabData);
        if (visiblePos >= 0) {
            hideTab(visiblePos);
        }
    }

    protected void setTabVisible(String tabKey, boolean visible) {
        setTabVisible(tabData().getTabDataFor(tabKey), visible);
    }

    protected void setTabVisible(CoAbstractUserInterfaceData tabData, boolean visible) {
        if (visible) {
            showTab(tabData);
        } else {
            hideTab(tabData);
        }
    }
}
