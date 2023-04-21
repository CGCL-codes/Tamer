package com.siemens.ct.exi.context;

import java.util.List;
import com.siemens.ct.exi.core.container.PreReadValue;
import com.siemens.ct.exi.core.container.ValueAndDatatype;
import com.siemens.ct.exi.grammar.event.StartElement;
import com.siemens.ct.exi.values.StringValue;

public class RuntimeQNameContextEntries {

    List<StringValue> strings;

    StartElement globalStartElement;

    List<ValueAndDatatype> valuesAndDataypes;

    PreReadValue preReadValue;

    public PreReadValue getPreReadValue() {
        return preReadValue;
    }

    public void setPreReadValue(PreReadValue prrReadValue) {
        this.preReadValue = prrReadValue;
    }

    public RuntimeQNameContextEntries() {
    }

    public void clear() {
        strings = null;
        globalStartElement = null;
        valuesAndDataypes = null;
        preReadValue = null;
    }

    public List<ValueAndDatatype> getValuesAndDataypes() {
        return valuesAndDataypes;
    }
}
