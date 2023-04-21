package org.jadapter.tests.adapters;

import org.jadapter.tests.adapters.SetterAdapter;
import org.jadapter.tests.examples.Csv;
import java.util.List;

public class StaticFactoryAdapter {

    public static Csv getCommaSeparated(List<?> object) {
        SetterAdapter csl = new SetterAdapter();
        csl.setList(object);
        return csl;
    }
}
