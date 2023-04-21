package org.nakedobjects.noa.adapter.value;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.Facet;
import java.util.Date;

public interface DateValueFacet extends Facet {

    public static final int DATE = 0x01;

    public static final int TIME = 0x10;

    public static final int PRECISION = 0x100;

    public static final int DATE_ONLY = DATE;

    public static final int TIME_ONLY = TIME;

    public static final int DATE_AND_TIME = DATE + TIME;

    public static final int TIMESTAMP = DATE + TIME + PRECISION;

    Date dateValue(NakedObject object);

    NakedObject createValue(Date date);

    int getLevel();
}
