package org.webcastellum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ParameterAndFormProtection implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean hiddenFormFieldProtection;

    private boolean filledButStillAllowingRenames;

    private final Set allParameterNames = new HashSet();

    private final Set requiredParameterNames = new HashSet();

    private final Set readwriteParameterNames = new HashSet();

    private final Map readonlyFieldsName2ExpectedValues = new HashMap();

    private final Map hiddenFieldsName2RemovedValues = new HashMap();

    private final Map selectboxFieldsName2AllowedValues = new HashMap();

    private final Map radiobuttonFieldsName2AllowedValues = new HashMap();

    private final Map checkboxFieldsName2AllowedValues = new HashMap();

    private final Map requestParamName2MinimumValueCount = new HashMap();

    private final Map requestParamName2MaximumValueCount = new HashMap();

    public int hashCode() {
        int hash = 31;
        hash = 7 * hash + this.allParameterNames.hashCode();
        hash = 7 * hash + this.requiredParameterNames.hashCode();
        hash = 7 * hash + this.readwriteParameterNames.hashCode();
        hash = 7 * hash + this.readonlyFieldsName2ExpectedValues.hashCode();
        hash = 7 * hash + this.hiddenFieldsName2RemovedValues.hashCode();
        hash = 7 * hash + this.selectboxFieldsName2AllowedValues.hashCode();
        hash = 7 * hash + this.radiobuttonFieldsName2AllowedValues.hashCode();
        hash = 7 * hash + this.checkboxFieldsName2AllowedValues.hashCode();
        hash = 7 * hash + this.requestParamName2MinimumValueCount.hashCode();
        hash = 7 * hash + this.requestParamName2MaximumValueCount.hashCode();
        return hash;
    }

    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if ((obj == null) || (obj.getClass() != this.getClass())) return false;
        final ParameterAndFormProtection other = (ParameterAndFormProtection) obj;
        return this.allParameterNames.equals(other.allParameterNames) && this.requiredParameterNames.equals(other.requiredParameterNames) && this.readwriteParameterNames.equals(other.readwriteParameterNames) && this.readonlyFieldsName2ExpectedValues.equals(other.readonlyFieldsName2ExpectedValues) && this.hiddenFieldsName2RemovedValues.equals(other.hiddenFieldsName2RemovedValues) && this.selectboxFieldsName2AllowedValues.equals(other.selectboxFieldsName2AllowedValues) && this.radiobuttonFieldsName2AllowedValues.equals(other.radiobuttonFieldsName2AllowedValues) && this.checkboxFieldsName2AllowedValues.equals(other.checkboxFieldsName2AllowedValues) && this.requestParamName2MinimumValueCount.equals(other.requestParamName2MinimumValueCount) && this.requestParamName2MaximumValueCount.equals(other.requestParamName2MaximumValueCount);
    }

    public void renameSecretTokenParameterName(final String oldParameterName, final String newParameterName) {
        renameParameterNameWithinSet(this.allParameterNames, oldParameterName, newParameterName);
        renameParameterNameWithinSet(this.requiredParameterNames, oldParameterName, newParameterName);
        renameParameterNameWithinSet(this.readwriteParameterNames, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.readonlyFieldsName2ExpectedValues, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.hiddenFieldsName2RemovedValues, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.selectboxFieldsName2AllowedValues, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.radiobuttonFieldsName2AllowedValues, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.checkboxFieldsName2AllowedValues, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.requestParamName2MinimumValueCount, oldParameterName, newParameterName);
        renameParameterNameWithinMap(this.requestParamName2MaximumValueCount, oldParameterName, newParameterName);
    }

    public void markAsFilled() {
        this.filledButStillAllowingRenames = true;
    }

    public ParameterAndFormProtection(final boolean hiddenFormFieldProtection) {
        this.hiddenFormFieldProtection = hiddenFormFieldProtection;
    }

    public void addParameterName(final String name, final boolean required) {
        if (this.filledButStillAllowingRenames) throw new IllegalStateException("This ParameterAndFormProtection object is already filled");
        if (name == null || name.length() == 0) return;
        this.allParameterNames.add(name);
        if (required && (this.hiddenFormFieldProtection || !WebCastellumFilter.CAPTCHA_FORM.equals(name))) this.requiredParameterNames.add(name);
    }

    public void addHiddenFieldRemovedValue(final String name, final String value) {
        if (this.filledButStillAllowingRenames) throw new IllegalStateException("This ParameterAndFormProtection object is already filled");
        List values = (List) this.hiddenFieldsName2RemovedValues.get(name);
        if (values == null) {
            values = new ArrayList();
            this.hiddenFieldsName2RemovedValues.put(name, values);
        }
        assert values != null;
        values.add(value);
    }

    public int getIndexOfNextSelectboxFieldAllowedValue(final String name) {
        List values = (List) this.selectboxFieldsName2AllowedValues.get(name);
        if (values == null) return 0;
        return values.size();
    }

    public void addSelectboxFieldAllowedValue(final String name, final String value) {
        if (this.filledButStillAllowingRenames) throw new IllegalStateException("This ParameterAndFormProtection object is already filled");
        List values = (List) this.selectboxFieldsName2AllowedValues.get(name);
        if (values == null) {
            values = new ArrayList();
            this.selectboxFieldsName2AllowedValues.put(name, values);
        }
        assert values != null;
        values.add(value);
    }

    public int getIndexOfNextRadiobuttonFieldAllowedValue(final String name) {
        List values = (List) this.radiobuttonFieldsName2AllowedValues.get(name);
        if (values == null) return 0;
        return values.size();
    }

    public void addRadiobuttonFieldAllowedValue(final String name, final String value) {
        if (this.filledButStillAllowingRenames) throw new IllegalStateException("This ParameterAndFormProtection object is already filled");
        List values = (List) this.radiobuttonFieldsName2AllowedValues.get(name);
        if (values == null) {
            values = new ArrayList();
            this.radiobuttonFieldsName2AllowedValues.put(name, values);
        }
        assert values != null;
        values.add(value);
    }

    public int getIndexOfNextCheckboxFieldAllowedValue(final String name) {
        List values = (List) this.checkboxFieldsName2AllowedValues.get(name);
        if (values == null) return 0;
        return values.size();
    }

    public void addCheckboxFieldAllowedValue(final String name, final String value) {
        if (this.filledButStillAllowingRenames) throw new IllegalStateException("This ParameterAndFormProtection object is already filled");
        List values = (List) this.checkboxFieldsName2AllowedValues.get(name);
        if (values == null) {
            values = new ArrayList();
            this.checkboxFieldsName2AllowedValues.put(name, values);
        }
        assert values != null;
        values.add(value);
    }

    public void addReadonlyFieldExpectedValue(final String name, final String value) {
        if (this.filledButStillAllowingRenames) throw new IllegalStateException("This ParameterAndFormProtection object is already filled");
        List values = (List) this.readonlyFieldsName2ExpectedValues.get(name);
        if (values == null) {
            values = new ArrayList();
            this.readonlyFieldsName2ExpectedValues.put(name, values);
        }
        assert values != null;
        values.add(value);
    }

    public void addReadwriteFieldName(final String name) {
        if (this.filledButStillAllowingRenames) throw new IllegalStateException("This ParameterAndFormProtection object is already filled");
        this.readwriteParameterNames.add(name);
    }

    public void incrementMinimumValueCountForParameterName(final String name, final int increment) {
        if (this.filledButStillAllowingRenames) throw new IllegalStateException("This ParameterAndFormProtection object is already filled");
        Counter counter = (Counter) this.requestParamName2MinimumValueCount.get(name);
        if (counter == null) {
            counter = new Counter();
            this.requestParamName2MinimumValueCount.put(name, counter);
        }
        assert counter != null;
        counter.increment(increment);
    }

    public void incrementMaximumValueCountForParameterName(final String name, final int increment) {
        if (this.filledButStillAllowingRenames) throw new IllegalStateException("This ParameterAndFormProtection object is already filled");
        Counter counter = (Counter) this.requestParamName2MaximumValueCount.get(name);
        if (counter == null) {
            counter = new Counter();
            this.requestParamName2MaximumValueCount.put(name, counter);
        }
        assert counter != null;
        counter.increment(increment);
    }

    private void renameParameterNameWithinSet(final Set set, final String oldParameterName, final String newParameterName) {
        if (set.contains(oldParameterName)) {
            set.remove(oldParameterName);
            set.add(newParameterName);
        }
    }

    private void renameParameterNameWithinMap(final Map map, final String oldParameterName, final String newParameterName) {
        if (map.containsKey(oldParameterName)) {
            final Object value = map.remove(oldParameterName);
            map.put(newParameterName, value);
        }
    }

    public Set getAllParameterNames() {
        return new HashSet(this.allParameterNames);
    }

    public Set getRequiredParameterNames() {
        return new HashSet(this.requiredParameterNames);
    }

    public Map getHiddenFieldsName2RemovedValues() {
        return new HashMap(this.hiddenFieldsName2RemovedValues);
    }

    public Map getSelectboxFieldsName2AllowedValues() {
        return new HashMap(this.selectboxFieldsName2AllowedValues);
    }

    public Map getRadiobuttonFieldsName2AllowedValues() {
        return new HashMap(this.radiobuttonFieldsName2AllowedValues);
    }

    public Map getCheckboxFieldsName2AllowedValues() {
        return new HashMap(this.checkboxFieldsName2AllowedValues);
    }

    public Map getReadonlyFieldsName2ExpectedValues() {
        return new HashMap(this.readonlyFieldsName2ExpectedValues);
    }

    public boolean isAlsoReadwriteField(final String name) {
        return this.readwriteParameterNames.contains(name);
    }

    public int getMinimumValueCountForParameterName(final String name) {
        final Counter counter = (Counter) this.requestParamName2MinimumValueCount.get(name);
        if (counter == null) return Integer.MIN_VALUE;
        return counter.value();
    }

    public int getMaximumValueCountForParameterName(final String name) {
        final Counter counter = (Counter) this.requestParamName2MaximumValueCount.get(name);
        if (counter == null) return Integer.MAX_VALUE;
        return counter.value();
    }

    public String toString() {
        return "PAF";
    }

    private static final class Counter implements Serializable {

        private static final long serialVersionUID = 1L;

        private int value;

        public Counter() {
        }

        public Counter(int value) {
            this();
            this.value = value;
        }

        public void increment(int increment) {
            this.value += increment;
        }

        public int value() {
            return this.value;
        }

        public int hashCode() {
            return this.value;
        }

        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if ((obj == null) || (obj.getClass() != this.getClass())) return false;
            final Counter other = (Counter) obj;
            return this.value == other.value;
        }
    }
}
