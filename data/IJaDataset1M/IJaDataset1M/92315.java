package no.ugland.utransprod.dao.hibernate;

import java.util.List;
import no.ugland.utransprod.dao.DocumentDAO;
import no.ugland.utransprod.model.Document;
import no.ugland.utransprod.util.Periode;
import no.ugland.utransprod.util.Util;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class DocumentDAOHibernate extends BaseDAOHibernate<Document> implements DocumentDAO {

    public DocumentDAOHibernate() {
        super(Document.class);
    }

    @SuppressWarnings("unchecked")
    public final List<Integer> getDocumentIdByPeriode(final Periode periode) {
        return (List<Integer>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(final Session session) {
                Integer startDate = Util.convertDateToInt(periode.getStartDate());
                Integer endDate = Util.convertDateToInt(periode.getEndDate());
                String sql = "select distinct document.documentId" + "       from Document document,Appointment appointment,Doctmpl doctmpl" + "       where   document.documentId=appointment.documentId and " + "                appointment.taskIdx=doctmpl.doctmplId and " + "         doctmpl.doctmplId in(102,103,153,154) and " + "         document.registered between :startDate and :endDate";
                return session.createQuery(sql).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
            }
        });
    }
}
