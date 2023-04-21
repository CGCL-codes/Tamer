package br.ufpa.spider.mplan.persistence;

import br.ufpa.spider.mplan.model.Report;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ReportDAO {

    private ReportDAO() {
    }

    private static List<Report> listWithAllReport = null;

    public static void addReport(Report mp) {
        GenericDAO.create(mp);
        updateListWithAllHistoric();
    }

    public static void updateListWithAllHistoric() {
        List list = GenericDAO.getAll("report");
        listWithAllReport = new ArrayList<Report>();
        for (int i = 0; i < list.size(); ++i) {
            listWithAllReport.add((Report) list.get(i));
        }
    }

    public static int searchMeasureDuplicate(String versionDigited, long id_m) {
        String version;
        int ver = 0;
        versionDigited = versionDigited.toLowerCase();
        List<Report> mpList = ReportDAO.getAllHistoryByOrganization(id_m);
        for (Report hm : mpList) {
            version = hm.getVersion().toLowerCase();
            if (versionDigited.contentEquals(version)) {
                ver = 1;
                break;
            } else {
                ver = 0;
            }
        }
        return ver;
    }

    public static List<Report> getAll() {
        List lisfOfEntities = null;
        List<Report> in = new ArrayList<Report>();
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("spider_mplan");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        lisfOfEntities = em.createQuery("select a from report a").getResultList();
        em.getTransaction().commit();
        em.close();
        emf.close();
        for (int i = 0; i < lisfOfEntities.size(); ++i) {
            in.add((Report) lisfOfEntities.get(i));
        }
        return in;
    }

    public static List<Report> getAllHistoryByOrganization(Long id) {
        List lisfOfEntities = null;
        List<Report> in = new ArrayList<Report>();
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("spider_mplan");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        lisfOfEntities = em.createQuery("select a from report a where a.organization=" + id).getResultList();
        em.getTransaction().commit();
        em.close();
        emf.close();
        for (int i = 0; i < lisfOfEntities.size(); ++i) {
            in.add((Report) lisfOfEntities.get(i));
        }
        return in;
    }

    public static List<Report> getAllMeasure() {
        if (ReportDAO.listWithAllReport == null) {
            updateListWithAllHistoric();
        }
        List<Report> listWithAllOrganization = new ArrayList<Report>();
        for (Report in : ReportDAO.listWithAllReport) {
            listWithAllOrganization.add(in);
        }
        return listWithAllOrganization;
    }

    public static List<Report> searchHistory(String keyword, Long id) {
        List<Report> listOfHistorySearch = ReportDAO.getAllHistoryByOrganization(id);
        List<Report> listOfHistory = new ArrayList<Report>();
        String userName, name;
        for (Report mp : listOfHistorySearch) {
            userName = mp.getNote().toLowerCase();
            name = mp.getNote().toLowerCase();
            keyword = keyword.toLowerCase();
            if (name.contains(keyword) || name.contains(keyword)) {
                listOfHistory.add(mp);
            }
        }
        return listOfHistory;
    }

    public static Report getMeasurePlanById(long id) {
        if (listWithAllReport == null) {
            updateListWithAllHistoric();
        }
        for (Report in : listWithAllReport) {
            if (in.getId() == id) {
                return in;
            }
        }
        return null;
    }
}
