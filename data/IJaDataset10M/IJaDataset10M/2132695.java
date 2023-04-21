package com.mangobop.impl.project;

import java.util.Collection;
import com.mangobop.project.Resource;

/**
 *
 * @author Stefan Meyer
 */
interface Repository {

    public Collection findResourceByAlias(String alias);

    public Resource findResourceByAliasAndDataType(String alias, String dataType);

    public Collection findResourceByDataType(String alias);

    public void init();

    public Collection getResources();
}
