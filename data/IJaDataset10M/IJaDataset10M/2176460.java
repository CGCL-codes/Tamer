package com.j2biz.compote.model;

import java.io.Serializable;
import net.sf.hibernate.Session;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public interface IExtension extends Serializable {

    public void beforeSave(Session session, Object object);

    public void afterSave(Session session, Object object);

    public void onUpdate(Session session, Object object);

    public void onDelete(Session session, Object object);

    public void onLoad(Session session, Object object);
}
