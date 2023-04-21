package com.hardcode.gdbms.engine.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.driver.exceptions.WriteDriverException;
import com.hardcode.gdbms.engine.data.indexes.FixedIndexSet;
import com.hardcode.gdbms.engine.data.persistence.DataSourceLayerMemento;
import com.hardcode.gdbms.engine.data.persistence.Memento;
import com.hardcode.gdbms.engine.data.persistence.MementoException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueCollection;
import com.hardcode.gdbms.engine.values.ValueFactory;

/**
 * Implementaci�n del tipo de datos DataSource. Decorator sobre el driver que
 * a�ade capacidades a la tabla como tener nombre, asociarse con un fichero
 * concreto o soporte para el acceso por nombre a los campos
 *
 * @author Fernando Gonz�lez Cort�s
 */
public abstract class AbstractDataSource extends DataSourceCommonImpl implements DataSource {

    private FieldNameAccessSupport fnaSupport = new FieldNameAccessSupport(this);

    protected DataSourceFactory dsf;

    protected ArrayList dataSourceListeners = new ArrayList();

    /**
     * Array de �ndices de las tablas. Un indice por campo. Si el elemento
     * i-�simo es null significa que el campo i-�simo no est� indexado
     */
    private FixedIndexSet[] indexes;

    protected SourceInfo sourceInfo;

    /**
     * @see com.hardcode.gdbms.data.DataSource#getDataSourceFactory()
     */
    public DataSourceFactory getDataSourceFactory() {
        return dsf;
    }

    /**
     * @see com.hardcode.gdbms.data.DataSource#setDataSourceFactory(DataSourceFactory)
     */
    public void setDataSourceFactory(DataSourceFactory dsf) {
        this.dsf = dsf;
    }

    /**
     * @see com.hardcode.gdbms.data.DataSource#close()
     */
    public int getFieldIndexByName(String fieldName) throws ReadDriverException {
        return fnaSupport.getFieldIndexByName(fieldName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public long[] getWhereFilter() throws IOException {
        return null;
    }

    /**
     * @see com.hardcode.gdbms.engine.data.DataSource#setSourceInfo(com.hardcode.gdbms.engine.data.DataSourceFactory.DriverInfo)
     */
    public void setSourceInfo(SourceInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    /**
     * @see com.hardcode.gdbms.engine.data.DataSource#getSourceInfo()
     */
    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }

    /**
     * @see com.hardcode.gdbms.engine.data.DataSource#getMemento()
     */
    public Memento getMemento() throws MementoException {
        DataSourceLayerMemento m = new DataSourceLayerMemento(sourceInfo.name, getName());
        return m;
    }

    /**
     * @see com.hardcode.gdbms.engine.data.DataSource#remove()
     */
    public void remove() throws WriteDriverException {
        dsf.remove(this);
    }

    /**
     * Gets the hashmap with the key-value pairs from the string
     *
     * @param str a & separated 'key=value' list
     *
     * @return a hashmap
     */
    private HashMap getMap(String str) {
        if (str == null) {
            return null;
        }
        HashMap m = new HashMap();
        String[] pairs = str.split("&");
        for (int i = 0; i < pairs.length; i++) {
            String[] keyValue = pairs[i].split("=");
            m.put(keyValue[0], keyValue[1]);
        }
        return m;
    }

    /**
     * Gets the name of the ith primary key field
     *
     * @param i index of the primary key field name to be retrieved
     *
     * @return String
     * @throws ReadDriverException TODO
     */
    private String getPKFieldName(int i) throws ReadDriverException {
        int[] fieldsId = getPrimaryKeys();
        return getFieldName(fieldsId[i]);
    }

    /**
     * @see com.hardcode.gdbms.engine.data.DataSource#getPKValue(long)
     */
    public ValueCollection getPKValue(long rowIndex) throws ReadDriverException {
        int[] fieldsId = getPrimaryKeys();
        Value[] pks = new Value[fieldsId.length];
        for (int i = 0; i < pks.length; i++) {
            pks[i] = getFieldValue(rowIndex, fieldsId[i]);
        }
        return ValueFactory.createValue(pks);
    }

    /**
     * @see com.hardcode.gdbms.engine.data.DataSource#getPKName(int)
     */
    public String getPKName(int fieldId) throws ReadDriverException {
        int[] fieldsId = getPrimaryKeys();
        return getFieldName(fieldsId[fieldId]);
    }

    /**
     * @see com.hardcode.gdbms.engine.data.DataSource#getPKType(int)
     */
    public int getPKType(int i) throws ReadDriverException {
        int[] fieldsId = getPrimaryKeys();
        return getFieldType(fieldsId[i]);
    }

    /**
     * @see com.hardcode.gdbms.engine.data.DataSource#getPKCardinality()
     */
    public int getPKCardinality() throws ReadDriverException {
        return 1;
    }

    /**
     * @see com.hardcode.gdbms.engine.data.DataSource#getPKNames()
     */
    public String[] getPKNames() throws ReadDriverException {
        String[] ret = new String[getPKCardinality()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = getPKName(i);
        }
        return ret;
    }

    public boolean isVirtualField(int fieldId) throws ReadDriverException {
        return false;
    }

    public void addDataSourceListener(IDataSourceListener listener) {
        this.dataSourceListeners.add(listener);
    }

    public void removeDataSourceListener(IDataSourceListener listener) {
        this.dataSourceListeners.remove(listener);
    }

    public void raiseEventReloaded() {
        Iterator iter = this.dataSourceListeners.iterator();
        while (iter.hasNext()) {
            ((IDataSourceListener) iter.next()).reloaded(this);
        }
    }
}
