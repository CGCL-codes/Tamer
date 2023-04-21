package geckoss.aeg.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import geckoss.aeg.spring.SimpleDao;
import java.util.Collection;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("./main.xml");
        BeanFactory factory = context;
        SimpleDao dao = (SimpleDao) context.getBean("mySimpleDao");
        System.out.println("I am here");
        Collection c = dao.loadDipByCddip(184);
        if (c == null) return;
        Iterator r = c.iterator();
        while (r.hasNext()) {
            RscpiId element = (RscpiId) r.next();
            System.out.println(element.getCdaziend());
        }
        System.out.println("Size " + c.size());
        System.out.println("Uso criteria");
        try {
            c = dao.QueryCriteria();
            if (c == null) return;
            Iterator it = c.iterator();
            while (it.hasNext()) {
                RscpiId element = (RscpiId) it.next();
                System.out.println(element.getCdaziend());
            }
        } catch (Exception e) {
        }
    }
}
