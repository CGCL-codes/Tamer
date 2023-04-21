package net.sf.opentranquera.persistence.queryengine;

import java.util.HashSet;
import net.sf.opentranquera.persistence.queryengine.ColumnaConstanteQl;
import net.sf.opentranquera.persistence.queryengine.ColumnaPropiedadQl;
import net.sf.opentranquera.persistence.queryengine.QlColumn;

/**
 * @author mmartinez
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class OTFilter {

    private HashSet columns = new HashSet();

    public OTFilter() {
    }

    public OTFilter(String[] target) {
        addColumns(target);
    }

    public OTFilter(QlColumn[] target) {
        addColumns(target);
    }

    public void addColumns(String[] propiedades) {
        for (int i = 0; i < propiedades.length; i++) {
            addColumn(propiedades[i]);
        }
    }

    public void addColumns(QlColumn[] propiedades) {
        for (int i = 0; i < propiedades.length; i++) {
            addColumn(propiedades[i]);
        }
    }

    public void addColumn(String propiedad) {
        addColumn(getColumnaPropiedad(propiedad, null));
    }

    public void addColumn(QlColumn columna) {
        columns.add(columna);
    }

    protected ColumnaPropiedadQl getColumnaPropiedad(String property, String alias) {
        return new ColumnaPropiedadQl(property, alias);
    }

    protected ColumnaConstanteQl getColumnaConstante(String property, String alias) {
        return new ColumnaConstanteQl(property, alias);
    }

    public QlColumn[] getColumns() {
        return (QlColumn[]) columns.toArray(new QlColumn[0]);
    }
}
