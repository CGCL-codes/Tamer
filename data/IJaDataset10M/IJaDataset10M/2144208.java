package org.form4j.form.field.data;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolTip;
import org.apache.log4j.Logger;
import org.form4j.form.event.RefocusWhenInvisibleListener;
import org.form4j.form.main.Form;
import org.form4j.form.util.PropertyUtil;
import org.form4j.form.util.StringUtil;
import org.form4j.form.util.tip.ToolTipUtil;
import org.w3c.dom.Element;

/**
 * Radio Button Form control.
 * <p>
 * (see: Form definition reference and examples for <a
 * href="../../../../../../manual/choiceDataFields.html#radio"
 * target="_top">Radio </a> in the <a href="../../../../../../manual/index.html"
 * target="_top">form4j Manual </a>!)
 * </p>
 *
 * @author $Author: cjuon $
 * @version 0.2 $Revision: 1.51 $ $Date: 2011/11/12 04:30:43 $
 */
public class Radio extends AbstractChoices implements ItemListener {

    private static final Logger LOG = Logger.getLogger(Radio.class.getName());

    private static boolean extraSpace = false;

    private static Insets insets = new Insets(-2, 1, 0, -2);

    static {
        try {
            boolean OSX_AND_JDK1_6_29 = (System.getProperty("os.name").startsWith("Mac") && System.getProperty("java.version").compareTo("1.6.0_29") >= 0);
            extraSpace = OSX_AND_JDK1_6_29;
            insets = OSX_AND_JDK1_6_29 ? new Insets(2, 1, 2, 2) : new Insets(-2, 1, 0, -2);
            LOG.debug("extraSpace=" + extraSpace + " insets=" + insets);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    private static final int RADIO_BUTTON_WIDTH_GAP = 20;

    /**
     * Construct Radio button form4j field control.
     * @param form the parent form4j
     * @param desc the XML field descriptor
     * @throws Exception Exception*
     */
    public Radio(final Form form, final Element desc) throws Exception {
        super(form, desc);
        LOG.debug("");
        LOG.debug("LAYOUT for " + desc.getAttribute("constraints") + "  orientation " + desc.getAttribute("orientation") + " rows " + desc.getAttribute("rows") + " columns " + desc.getAttribute("columns") + " ITEMS " + getEnumFacade().getItems().size());
        LayoutManager layout = null;
        if (desc.getAttribute("orientation").equals("horizontal")) layout = new FlowLayout(FlowLayout.LEFT); else if (desc.getAttribute("rows").trim().length() > 0) {
            int rows = Integer.parseInt(desc.getAttribute("rows").trim());
            layout = new GridLayout(rows, 0);
        } else if (desc.getAttribute("columns").trim().length() > 0) {
            int columns = Integer.parseInt(desc.getAttribute("columns").trim());
            layout = new GridLayout(0, columns);
        } else if (desc.getAttribute("orientation").equals("vertical")) layout = new GridLayout(getEnumFacade().getItems().size(), 1, 0, 0);
        panel = new JPanel(layout) {

            public Dimension getPreferredSize() {
                if (pref == null) pref = super.getPreferredSize();
                return (isVisible() ? pref : new Dimension(0, 0));
            }

            public void setEnabled(final boolean enabled) {
                enableDisableButtons(enabled);
            }

            Dimension pref = null;
        };
        setComponent(panel);
        init();
        int cnt = 0;
        for (Enumeration en = buttonGroup.getElements(); en.hasMoreElements(); ) {
            JComponent comp = (JComponent) en.nextElement();
            applyStandardComponentAttributes(comp, getFieldDescriptor());
            if (getFieldDescriptor().getAttribute("refocusWhenInvisible").equals("true")) {
                try {
                    LOG.debug("adding RefocusWhenInvisibleListener to " + comp);
                    comp.addFocusListener(new RefocusWhenInvisibleListener("B" + (cnt++) + ":", getForm()));
                } catch (Exception e) {
                    LOG.error("cannot add RefocusWhenInvisibleListener: " + e);
                }
            }
        }
        Enumeration buttonGroupElements = buttonGroup.getElements();
        if (buttonGroupElements.hasMoreElements()) {
            setFocusComponent((JComponent) buttonGroupElements.nextElement());
            Vector v = new Vector();
            buttonGroupElements = buttonGroup.getElements();
            while (buttonGroupElements.hasMoreElements()) {
                Object el = buttonGroupElements.nextElement();
                if (el != invisibleButton) {
                    v.addElement(el);
                }
            }
            setSubComponents(v);
        }
        LOG.debug("SUB COMPONENTS " + getSubComponents().size());
        if (getFieldDescriptor().getAttribute("border").equals("true")) panel.setBorder(this.createFieldBorder()); else panel.setBorder(this.createInvisibleFieldBorder(getComponent().getBackground()));
    }

    private int round(float f) {
        if (f - (int) f > 0.0) return (int) f + 1; else return (int) f;
    }

    /**
     * set the data for this field.
     * @param dataObject the data for this field
     */
    public void setData(final Object dataObject) {
        if (!initialized) initializeRadioButtons();
        String sData = null;
        if (getEnumFacade().getItems().size() > 0) sData = handleEmpty(dataObject); else LOG.info("Cannot handle empty data for empty radio box.");
        this.data = sData;
        LOG.debug(sData);
        if (sData != null && sData.trim().length() > 0) {
            sData = StringUtil.normalizeString(sData);
            LOG.debug("data '" + this.data + "' initialized " + initialized);
            if (getEnumFacade().hasLabels()) sData = getEnumFacade().itemToLabel(sData);
            for (Enumeration en = buttonGroup.getElements(); en.hasMoreElements(); ) {
                JRadioButton button = (JRadioButton) en.nextElement();
                if (button.getText().equals(sData)) button.setSelected(true);
            }
        } else {
            if (getEnumFacade().getItems().size() > 0) {
                if (getWhenEmpty() == USE_FIRST_WHEN_EMPTY) {
                    buttonGroup.getElements().nextElement().setSelected(true);
                }
            } else invisibleButton.setSelected(true);
        }
    }

    /**
     * get the data for this field.
     *
     * @return the data for this field
     */
    public Object getData() {
        LOG.debug("");
        for (Enumeration en = buttonGroup.getElements(); en.hasMoreElements(); ) {
            JRadioButton button = (JRadioButton) en.nextElement();
            if (button.isSelected()) {
                if (getEnumFacade().hasLabels()) return getEnumFacade().labelToItem(button.getText()); else return button.getText();
            }
        }
        return null;
    }

    /**
     * Get the button group for the associated radio buttons.
     * @return the button group
     */
    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    /**
     * Associate a focus listener with the field.
     *
     * @param listener the listener to associate with the field
     */
    public void addFocusListener(final FocusListener listener) {
        for (Enumeration en = buttonGroup.getElements(); en.hasMoreElements(); ) {
            JRadioButton button = (JRadioButton) en.nextElement();
            button.addFocusListener(listener);
        }
    }

    /**
     * Remove some associated focus listener.
     *
     * @param listener the listener to remove
     */
    public void removeFocusListener(final FocusListener listener) {
        for (Enumeration en = buttonGroup.getElements(); en.hasMoreElements(); ) {
            JRadioButton button = (JRadioButton) en.nextElement();
            button.removeFocusListener(listener);
        }
    }

    /**
     * called when the group of radio buttons has changed.
     * @param evt the item change event
     */
    public void itemStateChanged(final ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            try {
                if (LOG.isDebugEnabled()) LOG.debug("Data '" + getData() + "' active " + getEnumFacade().itemToActive((String) getData()));
                if (!getEnumFacade().itemToActive((String) getData())) setData(getForm().getModel().getData(getXPath())); else triggerModelChange();
            } catch (Exception e) {
                LOG.error(e);
            }
        }
    }

    private void triggerModelChange() {
        try {
            String key = getXPath();
            String newData = (String) getData();
            fireFieldChangeListeners(this, key, newData);
            processBeforeOps();
            getForm().getModel().setData(getComponent(), key, newData);
            handleLabelData();
            processAfterOps();
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

    private void handleLabelData() {
        if (getEnumFacade().hasLabels() && getLabelKey() != null) {
            String labelData = null;
            for (Enumeration en = buttonGroup.getElements(); labelData == null && en.hasMoreElements(); ) {
                JRadioButton button = (JRadioButton) en.nextElement();
                if (button.isSelected()) {
                    if (getEnumFacade().hasLabels()) labelData = button.getText();
                }
            }
            if (labelData != null) processLabelKey(labelData);
        }
    }

    protected void addActionListener(final String actionListenerClassName, String actionCommand) throws Exception {
        ActionListener actionListener = (ActionListener) Class.forName(actionListenerClassName).newInstance();
        for (Enumeration en = buttonGroup.getElements(); en.hasMoreElements(); ) {
            JRadioButton button = (JRadioButton) en.nextElement();
            addActionListener(button, actionCommand, actionListener);
        }
    }

    public void resetVisualFieldAttributes(Element alternateFormDefinition) {
        Element alternateFldDesc = obtainAlternateFieldDescriptor(alternateFormDefinition);
        String altBackground = alternateFldDesc.getAttribute("background");
        String background = PropertyUtil.stringFromColor(getComponent().getBackground());
        Color altBackgroundColor = new Color(defaultPanelBG.getRed(), defaultPanelBG.getGreen(), defaultPanelBG.getBlue());
        if (!altBackground.equals(background)) {
            LOG.debug(getFieldDescriptor().getAttribute("key") + "   --> bg " + background + ", alt bg " + altBackground);
            if (altBackground.trim().length() > 0) altBackgroundColor = PropertyUtil.colorFromString(altBackground);
            LOG.debug(getFieldDescriptor().getAttribute("key") + "   --> bg color " + altBackgroundColor);
            getFocusComponent().setBackground(altBackgroundColor);
            getComponent().setBackground(altBackgroundColor);
            Vector subComponents = getSubComponents();
            for (int i = 0; i < subComponents.size(); i++) {
                Component sc = (Component) subComponents.elementAt(i);
                sc.setBackground(altBackgroundColor);
            }
        }
        String altForeground = alternateFldDesc.getAttribute("foreground");
        String foreground = PropertyUtil.stringFromColor(getComponent().getForeground());
        Color altForegroundColor = defaultRadioFG;
        if (!altForeground.equals(foreground)) {
            LOG.debug(getFieldDescriptor().getAttribute("key") + "   --> bg " + foreground + ", alt bg " + altForeground);
            if (altForeground.trim().length() > 0) altForegroundColor = PropertyUtil.colorFromString(altForeground);
            LOG.debug(getFieldDescriptor().getAttribute("key") + "   --> bg color " + altForegroundColor);
            getComponent().setForeground(altForegroundColor);
            Vector subComponents = getSubComponents();
            for (int i = 0; i < subComponents.size(); i++) {
                Component sc = (Component) subComponents.elementAt(i);
                sc.setForeground(altForegroundColor);
            }
        }
        if (getFieldDescriptor().getAttribute("border").equals("true")) {
            getComponent().setBorder(BorderFactory.createEtchedBorder(altBackgroundColor.brighter(), altBackgroundColor.darker()));
        } else {
            getComponent().setBorder(null);
        }
    }

    private synchronized void initializeRadioButtons() {
        Vector elementTips = null;
        if (getFieldDescriptor().getAttribute("elementTips").trim().length() > 0) {
            elementTips = new Vector();
            StringTokenizer st = new StringTokenizer(getFieldDescriptor().getAttribute("elementTips"), getFieldDescriptor().getAttribute("separator"));
            while (st.hasMoreTokens()) elementTips.addElement(st.nextToken());
        }
        int dx = 0;
        if (getEnumFacade().hasLabels()) {
            for (int i = 0; i < getEnumFacade().size(); i++) {
                LOG.debug("adding item A " + getEnumFacade().getLabel(i));
                boolean shown = true;
                boolean enabled = true;
                if (getEnumFacade().isActiveOnly() && !getEnumFacade().getActive(i)) shown = false; else if (!getEnumFacade().getActive(i)) enabled = false;
                if (shown) {
                    JComponent comp = createRadioButton(getEnumFacade().getLabel(i), getEnumFacade().getItem(i), getElementTip(elementTips, i), getEnumFacade().itemToActive(getEnumFacade().getItem(i)));
                    dx += comp.getPreferredSize().width + RADIO_BUTTON_WIDTH_GAP;
                    if (!enabled) comp.setEnabled(false);
                }
            }
        } else {
            for (int i = 0; i < getEnumFacade().size(); i++) {
                LOG.debug("adding item B " + getEnumFacade().getItem(i));
                boolean shown = true;
                boolean enabled = true;
                if (getEnumFacade().isActiveOnly() && !getEnumFacade().getActive(i)) shown = false; else if (!getEnumFacade().getActive(i)) enabled = false;
                if (shown) {
                    JComponent comp = createRadioButton(getEnumFacade().getItem(i), getEnumFacade().getItem(i), getElementTip(elementTips, i), getEnumFacade().itemToActive(getEnumFacade().getItem(i)));
                    dx += comp.getPreferredSize().width + DEFAULT_ICON_DX;
                    if (!enabled) comp.setEnabled(false);
                }
            }
        }
        buttonGroup.add(invisibleButton);
        initialized = true;
    }

    public boolean isLastButton(Component button) {
        List<AbstractButton> l = obtainButtonList();
        return l.get(l.size() - 1) == button;
    }

    public AbstractButton getNextButton(Component button) {
        List<AbstractButton> l = obtainButtonList();
        int index = l.indexOf(button);
        if (index < l.size() - 1) return l.get(index + 1);
        return null;
    }

    public AbstractButton getPreviousButton(Component button) {
        List<AbstractButton> l = obtainButtonList();
        int index = l.indexOf(button);
        if (index > 0) return l.get(index - 1);
        return null;
    }

    public boolean isFirstButton(Component button) {
        List<AbstractButton> l = obtainButtonList();
        return l.get(0) == button;
    }

    private List<AbstractButton> obtainButtonList() {
        List<AbstractButton> l = new ArrayList<AbstractButton>();
        for (Enumeration<AbstractButton> en = buttonGroup.getElements(); en.hasMoreElements(); ) {
            AbstractButton b = en.nextElement();
            if (b.isVisible() && b.isEnabled() && b.getText() != null && b.getText().trim().length() > 0) l.add(b);
        }
        return l;
    }

    /**
     * @param elementTips
     * @return
     */
    private String getElementTip(final Vector elementTips, int index) {
        String elementTip = null;
        if (elementTips != null) {
            elementTip = (String) elementTips.elementAt(index);
            if (elementTip != null) elementTip = StringUtil.resolve(elementTip, "\\n", "\n");
        }
        LOG.debug("ELEMENT TIP " + elementTip);
        return elementTip;
    }

    private JComponent createRadioButton(final String label, final String name, final String elementTip, final boolean active) {
        JRadioButton radioCheckBox = new JRadioButton(label) {

            private int toolTipModifiers = 0;

            public Insets getInsets() {
                return insets;
            }

            public Point getToolTipLocation(final MouseEvent event) {
                toolTipModifiers = event.getModifiers();
                return super.getToolTipLocation(event);
            }

            public JToolTip createToolTip() {
                return ToolTipUtil.createToolTip(Radio.this, toolTipModifiers);
            }

            public Dimension getPreferredSize() {
                Dimension pSize = super.getPreferredSize();
                if (extraSpace) return new Dimension(pSize.width + 5, pSize.height);
                return pSize;
            }
        };
        if (ToolTipUtil.isToolTipDebugActive()) {
            radioCheckBox.setToolTipText(ToolTipUtil.TOOLTIP_PLACEHOLDER);
        }
        radioCheckBox.setEnabled(active);
        radioCheckBox.addItemListener(this);
        buttonGroup.add(radioCheckBox);
        panel.add(radioCheckBox);
        if (elementTip != null) radioCheckBox.setToolTipText(elementTip);
        return radioCheckBox;
    }

    private void enableDisableButtons(final boolean enabled) {
        LOG.debug(enabled + "");
        for (Enumeration en = buttonGroup.getElements(); en.hasMoreElements(); ) {
            JRadioButton button = (JRadioButton) en.nextElement();
            button.setEnabled(enabled);
        }
    }

    static Color defaultRadioBG = null;

    static Color defaultRadioFG = null;

    static Color defaultPanelBG = null;

    static {
        JRadioButton dfltFld = new JRadioButton();
        defaultRadioBG = dfltFld.getBackground();
        defaultRadioFG = dfltFld.getForeground();
        JPanel dfltPanel = new JPanel();
        defaultPanelBG = dfltPanel.getBackground();
    }

    private static final int DEFAULT_ICON_DX = 12;

    private boolean initialized = false;

    private String data = null;

    private ButtonGroup buttonGroup = new ButtonGroup();

    private JRadioButton invisibleButton = new JRadioButton("");

    private JPanel panel = null;
}
