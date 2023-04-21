package org.sss.common.model;

import java.io.Serializable;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 对象接口类
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 542 $ $Date: 2009-10-04 01:41:37 -0400 (Sun, 04 Oct 2009) $
 */
public interface IModule extends IParent {

    static final Log log = LogFactory.getLog(IModule.class);

    public static final IModule[] EMPTY = new IModule[] {};

    public void setSelected(boolean selected);

    public boolean isSelected();

    public Serializable getIdentifier();

    public void setValues(IResult... results);

    public boolean isChildAdded();

    public IModule clone();

    public void copyValue(IModule module);

    public Collection<IParent> getModules();

    public Collection<IDatafield> getDatafields();

    public IBaseObject get(String key);

    public IBaseObject put(String key, IBaseObject object);
}
