package chsec.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import chsec.domain.CheckDonation;
import chsec.domain.DonationCat;
import chsec.domain.DonationCatExtType;

public class HibernateDonationCatDAOTest extends HibernateTestCase {

    HibernateDonationCatDAO dao;

    DonationCat donCat;

    DonationCat adHoc1;

    DonationCat adHoc2;

    DonationCat fixedCat;

    Date createdDt;

    public HibernateDonationCatDAOTest(String tn) {
        super(tn);
    }

    public HibernateDonationCatDAOTest() {
    }

    public void testFind4Day() {
    }
}
