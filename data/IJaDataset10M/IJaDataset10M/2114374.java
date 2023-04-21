package com.completex.objective.components.persistency.meta;

import com.completex.objective.components.persistency.MetaTable;

/**
 * @author Gennady Krizhevsky
 */
public interface MetaModelAssembly {

    MetaTable registerObject(MetaTable metaTable);

    MetaTable unregisterObject(MetaTable metaTable);

    MetaTable getMetaTable(String key);

    MetaTable[] getMetaTables();

    void registgerAll(MetaModelAssembly modelAssembly);

    void unregistgerAll(MetaModelAssembly modelAssembly);
}
