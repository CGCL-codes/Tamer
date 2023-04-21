package com.ibm.oti.pim;

import java.util.Date;
import java.util.Enumeration;
import javax.microedition.pim.Event;
import javax.microedition.pim.FieldEmptyException;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.RepeatRule;

class EventImpl extends PIMItemImpl implements Event {

    private RepeatRule rule = null;

    private boolean fromOS = true;

    private boolean loaded = false;

    /**
	 * Internal field ID for repeat rule exception dates.
	 */
    private static final int EXCEPTION_DATE = 154681500;

    /**
	 * Constructor for EventImpl.
	 */
    EventImpl(PIMListImpl owner, int rechandle) {
        super(owner, rechandle);
    }

    /**
	 * Answers the repeat fields set for this event repeat rule.
	 * The method MUST answer the fields' internal indexes and
	 * not their API field IDs, see {@link javax.microedition.pim.RepeatRule#fields}.
	 * @param handle The list handle.
	 * @param rechandle The record handle.
	 * @return int[] An int array of fields that have data currently stored for them. 
	 * 		If no fields contain data, a zero length array is returned.
	 * @throws RuntimeException if the given record has been deleted or
	 * 		the list is not accessible.
	 */
    private native int[] getRepeatFieldsN(int handle, int rechandle);

    /**
	 * Answers the exception dates for this event repeat rule.
	 * @param handle The list handle.
	 * @param rechandle The record handle.
	 * @return long[] A long array of exception dates . 
	 * 		If no exception dates, a zero length array is returned.
	 * @throws RuntimeException if the given record has been deleted or
	 * 		the list is not accessible.
	 */
    private native long[] getRepeatExceptDatesN(int handle, int rechandle);

    /** 
	 * Gets a date value from the given repeat field.
	 * @param handle The list handle.
	 * @param rechandle The record handle.
	 * @param field the internal index of the repeat field END, 
	 * 		see {@link javax.microedition.pim.RepeatRule#fields}.
	 * @return long Number of milliseconds since Jan 1, 1970 GMT.
	 * @throws RuntimeException if the given record has been deleted or
	 * 		the list is not accessible.
	 */
    private native long getRepeatDateN(int handle, int rechandle, int field);

    /** 
	 * Gets an int value from the given repeat field.
	 * @param handle The list handle.
	 * @param rechandle The record handle.
	 * @param field One of the internal indexes of repeat fields except END, 
	 * 		see {@link javax.microedition.pim.RepeatRule#fields}.
	 * @return int The field data.
	 * @throws RuntimeException if the given record has been deleted or
	 * 		the list is not accessible.
	 */
    private native int getRepeatIntN(int handle, int rechandle, int field);

    /**
	 * @see javax.microedition.pim.Event#getRepeat()
	 */
    public RepeatRule getRepeat() {
        synchronized (i2) {
            checkItemDeleted();
            if (fromOS && !loaded) loadRepeatRuleFromOS();
            if (rule != null) {
                RepeatRule copy = new RepeatRule();
                int[] fieldSet = rule.getFields();
                for (int i = 0; i < fieldSet.length; i++) {
                    if (fieldSet[i] != RepeatRule.END) copy.setInt(fieldSet[i], rule.getInt(fieldSet[i])); else copy.setDate(fieldSet[i], rule.getDate(fieldSet[i]));
                }
                Enumeration en = rule.getExceptDates();
                while (en.hasMoreElements()) {
                    copy.addExceptDate(((Date) en.nextElement()).getTime());
                }
                return copy;
            } else return null;
        }
    }

    /**
	 * @see javax.microedition.pim.Event#setRepeat(RepeatRule)
	 */
    public void setRepeat(RepeatRule value) {
        checkItemDeleted();
        fromOS = false;
        loadPIMStruct();
        modified = true;
        this.rule = value;
    }

    /**
	 * Loads the repeat rule from the OS.
	 */
    private void loadRepeatRuleFromOS() {
        if (!valuesFromOS) return;
        checkListClosed();
        int[] fields = getRepeatFieldsN(owner.handle, rechandle);
        if (fields.length == 0) return;
        for (int i = 0; i < fields.length; i++) fields[i] = PIMUtil.getRepeatFieldIDFromIndex(fields[i]);
        long value = 0;
        RepeatRule repeatRule = new RepeatRule();
        for (int i = 0; i < fields.length; i++) {
            int field = fields[i];
            if (field == RepeatRule.END) {
                value = getRepeatDateN(owner.handle, rechandle, PIMUtil.getRepeatFieldIndexFromID(field));
                repeatRule.setDate(field, value);
            } else {
                value = getRepeatIntN(owner.handle, rechandle, PIMUtil.getRepeatFieldIndexFromID(field));
                repeatRule.setInt(field, (int) value);
            }
        }
        long[] exceptDates = getRepeatExceptDatesN(owner.handle, rechandle);
        for (int i = 0; i < exceptDates.length; i++) {
            repeatRule.addExceptDate(exceptDates[i]);
        }
        rule = repeatRule;
        loaded = true;
        fromOS = true;
    }

    /**
	 * Adds a value to the cached data.
	 * @param field
	 * @param index
	 * @param value
	 */
    private void addLongValue(int field, int index, long value) {
        synchronized (i2) {
            int ind = (int) longvalues[0] * 4 + 1;
            int longvlen = longvalues.length;
            if (ind + 3 >= longvlen) longvalues = adjustLongArray(longvalues, longvlen + 4);
            longvalues[ind] = field;
            longvalues[ind + 1] = index;
            longvalues[ind + 2] = 0;
            longvalues[ind + 3] = value;
            longvalues[0]++;
            modified = true;
        }
    }

    /**
	 * Answers if the given repeat rule is supported.
	 * e.g. the frequency is supported and all the fields
	 * required are set.
	 * @param repeatRule
	 * @return boolean
	 */
    private boolean isSupportedRepeatRule(RepeatRule repeatRule) {
        if (repeatRule == null) return false;
        int freq;
        try {
            freq = repeatRule.getInt(RepeatRule.FREQUENCY);
        } catch (FieldEmptyException e) {
            return false;
        }
        if (!PIMUtil.contains(EventListImpl.getSupportedRepeatRuleFrequenciesN(), freq)) return false;
        int[] fields = repeatRule.getFields();
        int[] supportedFields = EventListImpl.getSupportedRepeatRuleFieldsN(freq);
        int mask = 0;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] != RepeatRule.FREQUENCY && fields[i] != RepeatRule.INTERVAL && fields[i] != RepeatRule.COUNT && fields[i] != RepeatRule.END) mask |= fields[i];
        }
        if (supportedFields.length == 0 && mask == 0) return true;
        if (!PIMUtil.contains(supportedFields, mask)) return false;
        return true;
    }

    /**
	 * Loads the repeat rule into the pim structure arrays.
	 * @throws PIMException
	 */
    protected void loadRepeatRule() throws PIMException {
        if (!loaded && fromOS) loadRepeatRuleFromOS();
        synchronized (i2) {
            if (isSupportedRepeatRule(rule)) {
                int[] fields = rule.getFields();
                for (int i = 0; i < fields.length; i++) {
                    int field = fields[i];
                    if (field == RepeatRule.END) addLongValue(PIMUtil.getRepeatFieldIndexFromID(field), 0, rule.getDate(field)); else addLongValue(PIMUtil.getRepeatFieldIndexFromID(field), 0, rule.getInt(field));
                }
                Enumeration exceptDates = rule.getExceptDates();
                int i = 0;
                while (exceptDates.hasMoreElements()) {
                    Date date = (Date) exceptDates.nextElement();
                    addLongValue(EXCEPTION_DATE, i, date.getTime());
                    i++;
                }
            }
            fromOS = true;
            loaded = false;
            rule = null;
        }
    }
}
