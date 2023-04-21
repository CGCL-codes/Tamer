package org.objectwiz.fxclient.renderer;

import fr.helmet.javafx.table.ObjectToColumnsParser;
import org.objectwiz.client.UnitProxy;
import org.objectwiz.metadata.MappedClass;

/**
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class EntityToColumnParser extends ObjectToColumnsParser {

    private UnitProxy proxy;

    public EntityToColumnParser(UnitProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public String[] getColumnNames() {
        return new String[] { "Class", "Id" };
    }

    @Override
    public Object[] getColumns(Object obj) {
        try {
            MappedClass mc = proxy.getMappedClass(obj);
            Object id = proxy.getEntityId(obj);
            String classLabel = proxy.getCustomization().getDefinedClassLabel(mc);
            return new Object[] { classLabel, id };
        } catch (ClassNotFoundException e) {
            return new Object[] { "<error>", null };
        }
    }
}
