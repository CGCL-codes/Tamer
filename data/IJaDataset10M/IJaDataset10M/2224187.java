package chsec.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.classic.Session;
import chsec.dao.DonationCatDAO;
import chsec.domain.DonationCat;

public class HibernateDonationCatDAO extends GenericHibernateDAO<DonationCat, Integer> implements DonationCatDAO {

    public List<DonationCat> findAllTopLevelCat() {
        Session ses = getSession();
        List resL = ses.getNamedQuery("finfTopLevelCats").list();
        return conv2DonCat(resL);
    }

    private List<DonationCat> conv2DonCat(List resL) {
        ArrayList<DonationCat> donCatL = new ArrayList<DonationCat>(resL.size());
        for (Object obj : resL) {
            donCatL.add((DonationCat) obj);
        }
        return donCatL;
    }

    public List<DonationCat> findNonAbstrTopLevelCat() {
        Session ses = getSession();
        List resL = ses.getNamedQuery("findNATopLevelCats").list();
        return conv2DonCat(resL);
    }

    public boolean find4Day(Date dt, List<DonationCat> donCatL) {
        Session ses = getSession();
        List resL = ses.getNamedQuery("donNotesByDate").setDate("donDt", dt).list();
        for (Object obj : resL) {
            DonationCat donCat = (DonationCat) obj;
            if (donCat.getBaseCat() != null) {
                DonationCat baseCat = findById(donCat.getBaseCat().getCatCd(), false);
                baseCat.equals(baseCat.getCatNm());
                donCat.setBaseCat(baseCat);
            }
            donCatL.add((DonationCat) obj);
        }
        return resL.size() > 0;
    }
}
