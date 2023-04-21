package org.maximodeveloper.core;

public class MboNames5 extends MboNames implements IMboNames {

    private final String[][] mboMboSetPairs = { { "psdi.mbo.Mbo", "psdi.mbo.MboSet" }, { "psdi.mbo.NonPersistentMbo", "psdi.mbo.NonPersistentMboSet" }, { "psdi.mbo.StatefulMbo", "psdi.mbo.MboSet" } };

    private final String[] fieldClasses = { "psdi.mbo.MboValueAdapter", "psdi.mbo.BasicDomain", "psdi.mbo.MAXTableDomain", "psdi.mbo.CrossOverDomain" };

    public String[][] getMboMboSetPairs() {
        return mboMboSetPairs;
    }

    public String[] getFieldClasses() {
        return fieldClasses;
    }
}
