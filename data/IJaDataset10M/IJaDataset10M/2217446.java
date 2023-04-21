package com.rich.oa.test;

import java.util.Iterator;
import java.util.List;
import org.springframework.orm.hibernate3.HibernateTemplate;
import com.rich.oa.hibernate.domain.CalendarSend;
import com.rich.oa.hibernate.domain.FlowNames;
import com.rich.oa.hibernate.domain.Purview;
import com.rich.oa.hibernate.domain.RolPur;
import com.rich.oa.hibernate.domain.Role;
import com.rich.oa.hibernate.domain.Test;
import com.rich.oa.hibernate.domain.User;
import com.rich.oa.hibernate.domain.UserRole;
import com.rich.oa.hibernate.domain.WeekCalendar;
import com.rich.oa.hibernate.domain.file.FileList;
import com.rich.oa.hibernate.domain.file.Files;
import com.rich.oa.hibernate.domain.info.AddressList;
import com.rich.oa.hibernate.domain.info.Bulletin;
import com.rich.oa.hibernate.domain.info.Dictionary;
import com.rich.oa.hibernate.domain.info.Index;

public class TestDAO implements ITestDAO {

    private HibernateTemplate hibernateTemplate;

    private HibernateTemplate hibernateTemplate2;

    public HibernateTemplate getHibernateTemplate2() {
        return hibernateTemplate2;
    }

    public void setHibernateTemplate2(HibernateTemplate hibernateTemplate2) {
        this.hibernateTemplate2 = hibernateTemplate2;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void save() {
        List<User> list = hibernateTemplate2.find("FROM Bulletin");
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Bulletin entity = (Bulletin) iter.next();
            hibernateTemplate.save(entity);
        }
    }

    public static void main(String args[]) {
    }

    @Override
    public void get() {
        List<Test> list = hibernateTemplate.find("FROM Test");
        System.out.println(list.size());
    }
}
