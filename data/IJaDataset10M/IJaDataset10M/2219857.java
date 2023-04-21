package net.sf.webwarp.modules.datasource;

import net.sf.webwarp.util.hibernate.dao.Preload;
import net.sf.webwarp.util.hibernate.dao.SingleMutationEntityDAO;

public interface DBLinkDAO<L extends DBLink> extends SingleMutationEntityDAO<L, Integer> {

    public static final Preload WITH_EXTRACTS = new Preload("extracts");

    public DBLink getDBLink(String name);
}
