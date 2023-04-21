package com.bluebrim.paint.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.gemstone.client.*;
import com.bluebrim.gemstone.client.command.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.menus.client.*;
import com.bluebrim.observable.*;
import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.swing.client.*;

public class CoColorCollectionUI extends CoDomainUserInterface {

    private CoPopupMenu popupMenu;

    private AbstractAction removeAction;

    private AbstractAction addSpotColorAction;

    private AbstractAction addMultiInkColorAction;

    private AbstractAction addDefaultColorsAction;

    public static final String ADD = "add";

    public static final String REMOVE = "remove";

    public static final String ADD_RGB_COLORS = "add_rgb_colors";

    public static final String SPOT_COLOR = CoSpotColorIF.SPOT_COLOR;

    public static final String WHITE_COLOR = CoWhiteColorIF.WHITE_COLOR;

    public static final String PROCESS_COLOR = CoProcessColorIF.PROCESS_COLOR;

    public static final String MULTI_INK_COLOR = CoMultiInkColorIF.MULTI_INK_COLOR;

    public static final String REGISTRATION_COLOR = CoRegistrationColorIF.REGISTRATION_COLOR;

    public static final String COLOR_CANVAS = "color_canvas";

    public static final String COLORS = "colors";

    private Hashtable m_colorUIs;

    private int m_startOfMutableColors;

    private class addColorTransaction extends CoBasicTransaction {

        private CoColorIF color;

        public addColorTransaction(String name, CoObjectIF target, CoColorCollectionUI source, CoColorIF color) {
            super(name, target, source);
            this.color = color;
        }

        protected String createErrorMessage(Exception e) {
            MessageFormat tFormat = new MessageFormat(CoGsUIStringResources.getName(CoGemStoneUIConstants.ADD_LIST_ELEMENT_ERROR));
            return tFormat.format(new String[] { e.getMessage() != null ? e.getMessage() : new String() });
        }

        protected void doTransaction() {
            addColor(color);
        }

        protected void finish() {
            super.finish();
            getNamedValueModel(COLORS).valueHasChanged();
            getColorList().setSelectedValue(color, true);
        }
    }

    private class addDefaultColorsTransaction extends CoBasicTransaction {

        public addDefaultColorsTransaction(String name, CoObjectIF target, CoColorCollectionUI source) {
            super(name, target, source);
        }

        protected String createErrorMessage(Exception e) {
            MessageFormat tFormat = new MessageFormat(CoGsUIStringResources.getName(CoGemStoneUIConstants.ADD_LIST_ELEMENT_ERROR));
            return tFormat.format(new String[] { e.getMessage() != null ? e.getMessage() : new String() });
        }

        protected void doTransaction() {
            addRGBColors();
        }

        protected void finish() {
            super.finish();
            getNamedValueModel(COLORS).valueHasChanged();
        }
    }

    private class removeColorTransaction extends CoBasicTransaction {

        public removeColorTransaction(String name, CoObjectIF target, CoColorCollectionUI source) {
            super(name, target, source);
        }

        protected String createErrorMessage(Exception e) {
            MessageFormat tFormat = new MessageFormat(CoGsUIStringResources.getName(CoGemStoneUIConstants.REMOVE_LIST_ELEMENTS_ERROR));
            return tFormat.format(new String[] { e.getMessage() != null ? e.getMessage() : new String() });
        }

        protected void doTransaction() {
            removeColor();
        }

        protected void finish() {
            super.finish();
            getNamedValueModel(COLORS).valueHasChanged();
            getColorList().clearSelection();
        }
    }

    public CoColorCollectionUI() {
        this(null);
    }

    public CoColorCollectionUI(CoColorCollectionIF aDomainObject) {
        super(aDomainObject);
        initialize();
        createColorUIs();
    }

    private void createColorUIs() {
        m_colorUIs = new Hashtable(5);
        m_colorUIs.put(SPOT_COLOR, new CoSpotColorUI() {

            protected void handleChangedServerObject(Object serverObject) {
                updateColorList(serverObject);
            }
        });
        m_colorUIs.put(WHITE_COLOR, new CoWhiteColorUI() {

            protected void handleChangedServerObject(Object serverObject) {
                updateColorList(serverObject);
            }
        });
        m_colorUIs.put(PROCESS_COLOR, new CoProcessColorUI() {

            protected void handleChangedServerObject(Object serverObject) {
                updateColorList(serverObject);
            }
        });
        m_colorUIs.put(MULTI_INK_COLOR, new CoMultiInkColorUI() {

            protected CoColorCollectionIF getColorCollection() {
                return CoColorCollectionUI.this.getColorCollection();
            }

            protected void handleChangedServerObject(Object serverObject) {
                updateColorList(serverObject);
            }
        });
        m_colorUIs.put(REGISTRATION_COLOR, new CoRegistrationColorUI() {

            protected void handleChangedServerObject(Object serverObject) {
                updateColorList(serverObject);
            }
        });
    }

    protected void createListeners() {
        super.createListeners();
        ((CoAbstractListAspectAdaptor) getNamedValueModel(COLORS)).addSelectionListener(new CoSelectionListener() {

            public void selectionChange(CoSelectionEvent e) {
                if (singleSelection()) {
                    Object key = ((CoColorIF) getColorList().getSelectedValue()).getType();
                    CoColorUI currentUI = (CoColorUI) m_colorUIs.get(key);
                    CoSubcanvas canvas = (CoSubcanvas) getNamedWidget(COLOR_CANVAS);
                    canvas.setUserInterface(currentUI);
                    CoColorIF selectedValue = (CoColorIF) getColorList().getSelectedValue();
                    currentUI.setDomainFrom(selectedValue);
                    boolean mutable = getColorList().getSelectedIndex() >= m_startOfMutableColors;
                    currentUI.setColorEditable(mutable);
                    currentUI.setEnabled(mutable);
                    canvas.repaint();
                } else {
                    ((CoSubcanvas) getNamedWidget(COLOR_CANVAS)).setUserInterface(null);
                }
                if (!e.getValueIsAdjusting()) enableDisableMenus(true);
            }
        });
        (new CoServerObjectListener(this) {

            protected Object getServerObjectFrom(CoObjectIF domain) {
                return getColorCollection();
            }

            public void changedServerObject(CoChangedObjectEvent e) {
                getNamedValueModel(COLORS).valueHasChanged();
            }
        }).initialize();
        addValueListener(new CoValueListener() {

            public void valueChange(CoValueChangeEvent anEvent) {
                getColorList().clearSelection();
                if (anEvent.getNewValue() == null) {
                    ((CoSubcanvas) getNamedWidget(COLOR_CANVAS)).setUserInterface(null);
                }
            }
        });
        getUIBuilder().addEnableDisableListener(new CoEnableDisableListener() {

            public void enableDisable(CoEnableDisableEvent e) {
                enableDisableMenus(e.enable());
            }
        });
    }

    protected void createPopupMenus() {
        super.createPopupMenus();
        final JList tList = ((CoListBox) getNamedWidget(COLORS)).getList();
        CoMenuBuilder tMenuBuilder = getMenuBuilder();
        this.popupMenu = tMenuBuilder.createPopupMenu(tList);
        CoMenu colorMenu = tMenuBuilder.addPopupSubMenu(this.popupMenu, CoPaintUIResources.getName(ADD));
        tMenuBuilder.addMenuItem(colorMenu, this.addSpotColorAction);
        tMenuBuilder.addMenuItem(colorMenu, this.addMultiInkColorAction);
        tMenuBuilder.addPopupMenuItem(this.popupMenu, this.removeAction);
        tMenuBuilder.addSeparator(this.popupMenu);
        tMenuBuilder.addPopupMenuItem(this.popupMenu, this.addDefaultColorsAction);
        tList.addMouseListener(new CoPopupGestureListener(popupMenu));
    }

    public void createValueModels(CoUserInterfaceBuilder builder) {
        super.createValueModels(builder);
        builder.createListBoxAdaptor(builder.addListAspectAdaptor(new CoAbstractListAspectAdaptor.Default(this, COLORS) {

            protected Object get(CoObjectIF subject) {
                return ((CoColorCollectionIF) subject).getColors();
            }
        }), (CoListBox) getNamedWidget(COLORS));
    }

    protected void createWidgets(CoPanel aPanel, CoUserInterfaceBuilder aBuilder) {
        super.createWidgets(aPanel, aBuilder);
        CoPanel boxPanel = aBuilder.createBoxPanel(BoxLayout.Y_AXIS);
        boxPanel.add(aBuilder.createHeadlineLabel(CoColorUIResources.getName(COLORS)));
        CoPanel tPanel = aBuilder.createBoxPanel(BoxLayout.Y_AXIS);
        CoListBox lb = aBuilder.createListBox(new CoColorListCellRenderer() {

            protected boolean isMutable(CoColorIF c, int index) {
                return (index >= m_startOfMutableColors) && super.isMutable(c, index);
            }
        }, COLORS);
        tPanel.add(lb);
        CoSubcanvas colorCanvas = aBuilder.createSubcanvas(COLOR_CANVAS);
        boxPanel.add(aBuilder.createSplitPane(true, tPanel, colorCanvas));
        aPanel.add(boxPanel, BorderLayout.CENTER);
    }

    protected void doAfterCreateUserInterface() {
        super.doAfterCreateUserInterface();
        enableDisableMenus(getDomain() != null);
    }

    private void enableDisableMenus(boolean state) {
        boolean tState = state && isEnabled();
        this.addSpotColorAction.setEnabled(tState);
        this.addMultiInkColorAction.setEnabled(tState);
        Object[] selectedValues = getSelectedValues();
        int tSelectionCount = selectedValues.length;
        removeAction.setEnabled(tState && areColorsRemoveable(selectedValues, getColorList().getSelectedIndices()) && tSelectionCount > 0);
    }

    private JList getColorList() {
        return ((CoListBox) getNamedWidget(COLORS)).getList();
    }

    private Object[] getSelectedValues() {
        return getColorList().getSelectedValues();
    }

    private void initialize() {
        this.addSpotColorAction = new AbstractAction(CoPaintUIResources.getName(SPOT_COLOR)) {

            public void actionPerformed(ActionEvent e) {
                new addColorTransaction(CoPaintUIResources.getName(SPOT_COLOR), null, CoColorCollectionUI.this, getColorCollection().createSpotColor()).execute();
            }
        };
        addSpotColorAction.setEnabled(false);
        this.addMultiInkColorAction = new AbstractAction(CoPaintUIResources.getName(MULTI_INK_COLOR)) {

            public void actionPerformed(ActionEvent e) {
                new addColorTransaction(CoPaintUIResources.getName(SPOT_COLOR), null, CoColorCollectionUI.this, getColorCollection().createMultiInkColor()).execute();
            }
        };
        addMultiInkColorAction.setEnabled(false);
        this.removeAction = new AbstractAction(CoPaintUIResources.getName(REMOVE)) {

            public void actionPerformed(ActionEvent e) {
                new removeColorTransaction(CoPaintUIResources.getName(REMOVE), null, CoColorCollectionUI.this).execute();
            }
        };
        removeAction.setEnabled(false);
        this.addDefaultColorsAction = new AbstractAction(CoPaintUIResources.getName(ADD_RGB_COLORS)) {

            public void actionPerformed(ActionEvent e) {
                new addDefaultColorsTransaction(CoPaintUIResources.getName(ADD_RGB_COLORS), null, CoColorCollectionUI.this).execute();
            }
        };
    }

    private boolean singleSelection() {
        JList l = getColorList();
        return (l.getMinSelectionIndex() == l.getMaxSelectionIndex()) && (l.getMinSelectionIndex() != -1);
    }

    private void updateColorList(Object color) {
        ((CoAbstractListAspectAdaptor) getNamedValueModel(COLORS)).elementHasChanged(this, color);
    }

    private boolean areColorsRemoveable(Object[] colors, int[] index) {
        if (colors == null) return false;
        for (int i = 0; i < colors.length; i++) {
            CoColorIF c = (CoColorIF) colors[i];
            if (!c.canBeDeleted()) return false;
            if (index[i] < m_startOfMutableColors) return false;
        }
        return true;
    }

    public void postDomainChange(CoObjectIF d) {
        super.postDomainChange(d);
        m_startOfMutableColors = (getColorCollection() == null) ? 0 : getColorCollection().getImmutableColorCount();
    }

    public void valueHasChanged() {
        super.valueHasChanged();
        m_startOfMutableColors = getColorCollection().getImmutableColorCount();
    }

    private void addColor(CoColorIF color) {
        getColorCollection().addColor(color);
    }

    private void addRGBColors() {
        getColorCollection().addRGBColors();
    }

    private CoColorCollectionIF getColorCollection() {
        return (CoColorCollectionIF) getDomain();
    }

    private void removeColor() {
        getColorCollection().removeColor(getSelectedValues());
    }
}
