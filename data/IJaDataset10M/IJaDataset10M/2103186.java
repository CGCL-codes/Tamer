package net.sf.brightside.aikido.service.hibernate;

import java.util.LinkedList;
import java.util.List;
import net.sf.brightside.aikido.metamodel.Aikidoist;
import net.sf.brightside.aikido.metamodel.Club;
import net.sf.brightside.aikido.metamodel.Dojo;
import net.sf.brightside.aikido.metamodel.Practice;
import net.sf.brightside.aikido.service.ListPracticesAssociateWithDojoCommand;
import org.hibernate.Hibernate;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class ListPracticesAssociateWithDojoCommandImp extends HibernateDaoSupport implements ListPracticesAssociateWithDojoCommand {

    private Dojo dojo;

    public Session provideManager() {
        return getSessionFactory().getCurrentSession();
    }

    @Override
    public Dojo getDojo() {
        return dojo;
    }

    @Override
    public void setDojo(Dojo dojo) {
        this.dojo = dojo;
    }

    @Transactional
    public List<Practice> execute() {
        provideManager().update(dojo);
        List<Practice> listOfPractices = new LinkedList<Practice>();
        Hibernate.initialize(dojo.getPractices());
        listOfPractices = dojo.getPractices();
        return listOfPractices;
    }
}
