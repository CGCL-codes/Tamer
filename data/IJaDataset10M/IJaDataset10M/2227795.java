package org.nomadpim.core.entity.io;

import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.util.key.Key;

public interface ILocalResolver {

    IEntity get(Key key);
}
