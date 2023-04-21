package net.sf.annaf.server.data;

import java.io.File;
import net.sf.annaf.core.data.DataBean;
import net.sf.annaf.core.data.DataBeanAccessException;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-09-21
 *
 */
public interface SerialisationStrategy {

    <V extends DataBean> V readDataBean(File file, Class<V> v) throws DataBeanAccessException;

    <V extends DataBean> void writeDataBean(V v, File file) throws DataBeanAccessException;

    <V extends DataBean> V getNewDataBean();
}
