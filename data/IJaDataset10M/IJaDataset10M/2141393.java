package com.global360.sketchpadbpmn.graphic;

import java.awt.Component;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import com.global360.sketchpadbpmn.Utility;
import com.global360.sketchpadbpmn.documents.IconEnumeration;
import com.global360.sketchpadbpmn.graphic.symbol.Symbol;
import com.global360.sketchpadbpmn.i18n.Messages;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class BPMNEventTriggerType extends IconEnumeration {

    public static final int NONE = 0;

    public static final int MESSAGE = 1;

    public static final int TIMER = 2;

    public static final int ERROR = 3;

    public static final int ESCALATION = 4;

    public static final int CANCEL = 5;

    public static final int COMPENSATION = 6;

    public static final int CONDITIONAL = 7;

    public static final int LINK = 8;

    public static final int SIGNAL = 9;

    public static final int TERMINATE = 10;

    public static final int MULTIPLE = 11;

    public static final int PARALLEL_MULTIPLE = 12;

    public static final String S_NONE = Messages.getString("BPMNEventTriggerType.None");

    public static final String S_MESSAGE = Messages.getString("BPMNEventTriggerType.Message");

    public static final String S_TIMER = Messages.getString("BPMNEventTriggerType.Timer");

    public static final String S_ERROR = Messages.getString("BPMNEventTriggerType.Error");

    public static final String S_ESCALATION = Messages.getString("BPMNEventTriggerType.Escalation");

    public static final String S_CANCEL = Messages.getString("BPMNEventTriggerType.Cancel");

    public static final String S_COMPENSATION = Messages.getString("BPMNEventTriggerType.Compensation");

    public static final String S_CONDITIONAL = Messages.getString("BPMNEventTriggerType.Conditional");

    public static final String S_LINK = Messages.getString("BPMNEventTriggerType.Link");

    public static final String S_SIGNAL = Messages.getString("BPMNEventTriggerType.Signal");

    public static final String S_TERMINATE = Messages.getString("BPMNEventTriggerType.Terminate");

    public static final String S_MULTIPLE = Messages.getString("BPMNEventTriggerType.Multiple");

    public static final String S_PARALLEL_MULTIPLE = Messages.getString("BPMNEventTriggerType.ParallelMultiple");

    private static final String startNames[] = { S_NONE, S_MESSAGE, S_TIMER, S_ERROR, S_ESCALATION, S_COMPENSATION, S_CONDITIONAL, S_SIGNAL, S_MULTIPLE, S_PARALLEL_MULTIPLE };

    private static final int[] startTypes = { NONE, MESSAGE, TIMER, ERROR, ESCALATION, COMPENSATION, CONDITIONAL, SIGNAL, MULTIPLE, PARALLEL_MULTIPLE };

    private static final String intermediateNames[] = { S_NONE, S_MESSAGE, S_TIMER, S_ERROR, S_ESCALATION, S_CANCEL, S_COMPENSATION, S_CONDITIONAL, S_LINK, S_SIGNAL, S_MULTIPLE, S_PARALLEL_MULTIPLE };

    private static final int[] intermediateTypes = { NONE, MESSAGE, TIMER, ERROR, ESCALATION, CANCEL, COMPENSATION, CONDITIONAL, LINK, SIGNAL, MULTIPLE, PARALLEL_MULTIPLE };

    private static final String endNames[] = { S_NONE, S_MESSAGE, S_ERROR, S_ESCALATION, S_CANCEL, S_COMPENSATION, S_SIGNAL, S_TERMINATE, S_MULTIPLE };

    private static final int[] endTypes = { NONE, MESSAGE, ERROR, ESCALATION, CANCEL, COMPENSATION, SIGNAL, TERMINATE, MULTIPLE };

    private static final int[] symbolTypes = { Symbol.NONE, Symbol.MESSAGE, Symbol.TIMER, Symbol.ERROR, Symbol.STARFLEET, Symbol.CANCEL, Symbol.COMPENSATION, Symbol.CONDITIONAL, Symbol.LINK, Symbol.SIGNAL, Symbol.TERMINATE, Symbol.MULTIPLE, Symbol.PLUS };

    private static final int[] filledSymbolTypes = { Symbol.NONE, Symbol.FILLED_MESSAGE, Symbol.TIMER, Symbol.FILLED_ERROR, Symbol.FILLED_STARFLEET, Symbol.FILLED_CANCEL, Symbol.FILLED_COMPENSATION, Symbol.CONDITIONAL, Symbol.FILLED_LINK, Symbol.FILLED_SIGNAL, Symbol.FILLED_TERMINATE, Symbol.FILLED_MULTIPLE, Symbol.PLUS };

    private BPMNEventType eventType = null;

    private CatchThrow catchThrow = new CatchThrow(CatchThrow.ID_NULL);

    public static boolean isLegalType(BPMNEventType eventType, BPMNEventTriggerType triggerType, CatchThrow catchThrow) {
        String triggerTypeValue = null;
        if (triggerType != null) {
            triggerTypeValue = triggerType.getValue();
        }
        String catchThrowValue = null;
        if (catchThrow != null) {
            catchThrowValue = catchThrow.getValue();
        }
        return isLegalType(eventType, triggerTypeValue, catchThrowValue);
    }

    public static boolean isLegalType(BPMNEventType eventType, String triggerTypeValue, String catchThrow) {
        String[] names = getExternalValues(eventType);
        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(triggerTypeValue)) {
                return true;
            }
        }
        return false;
    }

    public static BPMNEventTriggerType make() {
        BPMNEventTriggerType result = null;
        result = new BPMNEventTriggerType();
        return result;
    }

    public static BPMNEventTriggerType make(BPMNEventType eventType) {
        BPMNEventTriggerType result = null;
        result = new BPMNEventTriggerType(eventType);
        return result;
    }

    public static BPMNEventTriggerType make(int value) {
        BPMNEventTriggerType result = new BPMNEventTriggerType();
        result.setValue(value);
        return result;
    }

    public static BPMNEventTriggerType make(BPMNEventTriggerType source) {
        if (source == null) {
            return BPMNEventTriggerType.make();
        }
        return BPMNEventTriggerType.make(source.getEventType(), source, source.getCatchThrow());
    }

    public static BPMNEventTriggerType make(BPMNEventType eventType, BPMNEventTriggerType eventTriggerType, CatchThrow catchThrow) {
        return BPMNEventTriggerType.make(eventType, (eventTriggerType == null ? null : eventTriggerType.getValue()), ((catchThrow == null) ? CatchThrow.S_NULL : catchThrow.getValue()));
    }

    public static BPMNEventTriggerType make(BPMNEventType eventType, String eventTriggerTypeString, CatchThrow catchThrow) {
        return BPMNEventTriggerType.make(eventType, eventTriggerTypeString, ((catchThrow == null) ? CatchThrow.S_NULL : catchThrow.getValue()));
    }

    public static BPMNEventTriggerType make(BPMNEventType eventType, String eventTriggerTypeString, String catchThrowString) {
        BPMNEventTriggerType result = null;
        if (BPMNEventTriggerType.isLegalType(eventType, eventTriggerTypeString, catchThrowString)) {
            result = new BPMNEventTriggerType(eventType, eventTriggerTypeString, catchThrowString);
        } else {
            result = new BPMNEventTriggerType(eventType, null, catchThrowString);
        }
        return result;
    }

    private BPMNEventTriggerType() {
        super();
        setEventType(new BPMNEventType(BPMNEventType.START));
        setValue(BPMNEventTriggerType.NONE);
    }

    private BPMNEventTriggerType(BPMNEventType eventType) {
        super();
        setEventType(eventType);
        setValue(BPMNEventTriggerType.NONE);
    }

    public BPMNEventTriggerType(BPMNEventType eventType, int triggerType, CatchThrow catchThrow) {
        super();
        setEventType(eventType);
        setValue(triggerType);
        setCatchThrow(catchThrow);
    }

    public BPMNEventTriggerType(BPMNEventType eventType, String triggerTypeString, String catchThrowString) {
        super();
        setEventType(eventType);
        this.setValue(triggerTypeString);
        this.setCatchThrow(catchThrowString);
    }

    public BPMNEventTriggerType clone() throws CloneNotSupportedException {
        BPMNEventTriggerType result = new BPMNEventTriggerType(this.getEventType(), this.getIntegerValue(), this.catchThrow);
        return result;
    }

    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof BPMNEventTriggerType) {
            result = this.equals((BPMNEventTriggerType) other);
        }
        return result;
    }

    public boolean equals(BPMNEventTriggerType other) {
        if (this == other) return true;
        if (!super.equals(other)) return false;
        if (!Utility.areEqual(this.getEventType(), other.getEventType())) return false;
        if (!Utility.areEqual(this.getCatchThrow(), other.getCatchThrow())) return false;
        return true;
    }

    public boolean isLegalType(String triggerTypeString) {
        if (triggerTypeString != null) {
            String[] names = this.getExternalValues();
            for (int i = 0; i < names.length; i++) {
                if (names[i].equalsIgnoreCase(triggerTypeString)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean supportsCatchThrow() {
        boolean result = false;
        if (this.eventType.equals(BPMNEventType.INTERMEDIATE)) {
            result = (this.equals(BPMNEventTriggerType.MESSAGE) || this.equals(BPMNEventTriggerType.SIGNAL) || this.equals(BPMNEventTriggerType.MULTIPLE) || this.equals(BPMNEventTriggerType.ESCALATION) || this.equals(BPMNEventTriggerType.COMPENSATION) || this.equals(BPMNEventTriggerType.LINK));
        }
        return result;
    }

    public boolean isThrow() {
        if (this.eventType != null) {
            if (this.eventType.equals(BPMNEventType.START)) {
                return false;
            } else if (this.eventType.equals(BPMNEventType.END)) {
                return true;
            }
        }
        if (this.catchThrow != null) {
            if (this.catchThrow.equals(CatchThrow.ID_THROW)) {
                return true;
            }
        }
        return false;
    }

    public CatchThrow getCatchThrow() {
        if (this.catchThrow != null) {
            try {
                return this.catchThrow.clone();
            } catch (CloneNotSupportedException e) {
            }
        }
        return new CatchThrow(CatchThrow.ID_NULL);
    }

    public void setCatchThrow(CatchThrow value) {
        if (value == null) {
            setCatchThrow(CatchThrow.S_NULL);
        } else {
            setCatchThrow(value.getValue());
        }
    }

    public void setCatchThrow(String value) {
        this.catchThrow = new CatchThrow(value);
    }

    public Symbol getSymbol() {
        int value = this.getIntegerValue();
        if ((value >= 0) && (value < BPMNEventTriggerType.symbolTypes.length)) {
            if (this.isThrow()) {
                return Symbol.makeSymbol(filledSymbolTypes[value]);
            } else {
                return Symbol.makeSymbol(symbolTypes[value]);
            }
        }
        return Symbol.makeSymbol(Symbol.NONE);
    }

    public static int[] getTypesArray(BPMNEventType eventType) {
        int[] typesArray = null;
        if (eventType != null) {
            if (eventType.equals(BPMNEventType.START)) {
                typesArray = startTypes;
            } else if (eventType.equals(BPMNEventType.INTERMEDIATE)) {
                typesArray = intermediateTypes;
            } else if (eventType.equals(BPMNEventType.END)) {
                typesArray = endTypes;
            }
        }
        return typesArray;
    }

    public static void addComboBoxItems(JComboBox comboBox, BPMNEventType eventType) {
        int[] typesArray = getTypesArray(eventType);
        for (int i = 0; i < typesArray.length; i++) {
            BPMNEventTriggerType triggerType = new BPMNEventTriggerType(eventType, typesArray[i], null);
            comboBox.addItem(triggerType);
            comboBox.setRenderer(BPMNEventTriggerType.getRenderer());
        }
    }

    private static IconEnumerationComboBoxRenderer renderer = null;

    public static IconEnumerationComboBoxRenderer getRenderer() {
        if (renderer == null) {
            BPMNEventTriggerType factory = new BPMNEventTriggerType();
            renderer = factory.makeRenderer();
        }
        return renderer;
    }

    public IconEnumerationComboBoxRenderer makeRenderer() {
        return new IconEnumerationComboBoxRenderer();
    }

    private void setEventType(BPMNEventType eventType) {
        this.eventType = eventType;
        this.initialize();
    }

    public BPMNEventType getEventType() {
        return this.eventType;
    }

    public DefaultComboBoxModel makeComboBoxModel() {
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        String[] names = getExternalValues();
        BPMNEventTriggerType currentItem = null;
        ;
        for (int i = 0; i < names.length; i++) {
            BPMNEventTriggerType trigger = new BPMNEventTriggerType(this.getEventType(), names[i], null);
            result.addElement(trigger);
            if (names[i].equalsIgnoreCase(this.getExternalValue())) {
                currentItem = trigger;
            }
        }
        if (currentItem != null) {
            result.setSelectedItem(currentItem);
        }
        return result;
    }

    /**
   * getExternalValues
   *
   * @return String[]
   */
    protected String[] getExternalValues() {
        return getExternalValues(this.getEventType());
    }

    /**
   * getExternalValues
   *
   * @param type BPMNEventType
   * @return String[]
   */
    protected static String[] getExternalValues(BPMNEventType type) {
        String[] result = null;
        if (type != null) {
            if (type.equals(BPMNEventType.START)) {
                result = startNames;
            } else if (type.equals(BPMNEventType.INTERMEDIATE)) {
                result = intermediateNames;
            } else if (type.equals(BPMNEventType.END)) {
                result = endNames;
            }
        }
        return result;
    }

    /**
   * getIntegerValues
   *
   * @return int[]
   */
    protected int[] getIntegerValues() {
        return BPMNEventTriggerType.getTypesArray(this.getEventType());
    }

    /**
   * getInternalValues
   *
   * @return String[]
   */
    protected String[] getInternalValues() {
        return getExternalValues();
    }

    class EventTriggerComboBoxRenderer extends JLabel implements ListCellRenderer {

        /**
     * 
     */
        private static final long serialVersionUID = 5083528279400692798L;

        public final int ICON_SIZE = 24;

        public EventTriggerComboBoxRenderer() {
        }

        /**
     * getListCellRendererComponent
     *
     * @param jList JList
     * @param object Object
     * @param int2 int
     * @param boolean3 boolean
     * @param boolean4 boolean
     * @return Component
     */
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value != null) {
                BPMNEventTriggerType trigger = (BPMNEventTriggerType) value;
                setText(trigger.toString());
                Symbol symbol = trigger.getSymbol();
                if (symbol != null) {
                    setIcon(symbol.toIcon(ICON_SIZE, 1));
                }
            }
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }

    @Override
    protected int[] getSymbolTypes() {
        return BPMNEventTriggerType.symbolTypes;
    }
}
