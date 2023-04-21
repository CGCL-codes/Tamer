package net.sf.brightside.travelsystem.core.command.hibernate;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import net.sf.brightside.travelsystem.core.command.CommandException;
import net.sf.brightside.travelsystem.core.command.DeleteEntity;

public class DeleteEntityImpl extends HibernateDaoSupport implements DeleteEntity {

    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    @Transactional
    public Boolean execute() throws CommandException {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(object);
        return null;
    }
}
