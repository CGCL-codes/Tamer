package net.sf.shineframework.server.services;

import java.io.Serializable;
import net.sf.shineframework.common.dal.dm.DalObject;

public interface FwService {

    public DalObject loadObjectByPk(Class<? extends DalObject> clazz, Serializable pk);
}
