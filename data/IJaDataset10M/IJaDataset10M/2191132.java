package org.fudaa.fudaa.meshviewer.export;

import org.fudaa.dodico.ef.EfGridData;

public abstract class MvExportOperationSelectionItem extends MvExportOperationItem {

    /**
   * @param _src la source adaptee au pas de temps choisi
   * @return le constructeur de filtre
   */
    public abstract MvExportOperationBuilderSelection getBuilder(EfGridData _src);
}
