package org.isportal.portlet.ecards;

import java.io.File;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.isportal.db.HibernateUtil;
import org.isportal.db.tables.ECategory;
import org.isportal.db.tables.EPicture;
import org.isportal.db.tables.Structure;
import org.isportal.portlet.ecards.ECategoryDAO;
import org.isportal.bean.SessionBean;

public class EDatabase {

    Session session = HibernateUtil.currentSession();

    Transaction tx = session.beginTransaction();

    Long l = new Long("0");

    public static void initEcards() {
        ECategory cat1 = new ECategory();
        ECategory cat2 = new ECategory();
        ECategory cat3 = new ECategory();
        ECategory cat4 = new ECategory();
        ECategory cat5 = new ECategory();
        EPicture slika01 = new EPicture();
        EPicture slika02 = new EPicture();
        EPicture slika03 = new EPicture();
        EPicture slika04 = new EPicture();
        EPicture slika05 = new EPicture();
        EPicture slika06 = new EPicture();
        EPicture slika07 = new EPicture();
        EPicture slika08 = new EPicture();
        EPicture slika09 = new EPicture();
        EPicture slika10 = new EPicture();
        Structure st1 = new Structure();
        Long l = new Long("0");
        st1.setParentId(l);
        cat1 = null;
        cat1 = new ECategory();
        cat1.setName("Narava");
        ECategoryDAO.saveCategory(cat1);
        cat2 = null;
        cat2 = new ECategory();
        cat2.setName("Obala");
        ECategoryDAO.saveCategory(cat2);
        cat3 = null;
        cat3 = new ECategory();
        cat3.setName("Planine");
        ECategoryDAO.saveCategory(cat3);
        cat4 = null;
        cat4 = new ECategory();
        cat4.setName("Mesto");
        ECategoryDAO.saveCategory(cat4);
        cat5 = null;
        cat5 = new ECategory();
        cat5.setName("Ostalo");
        ECategoryDAO.saveCategory(cat5);
        slika01 = null;
        slika01 = new EPicture();
        slika01.setPictureName("Slika01");
        slika01.setCategory(cat2);
        slika01.setLokacija("portlets/ecards/slike/Narava/01.JPG");
        EPictureDAO.savePicture(slika01);
        slika02 = null;
        slika02 = new EPicture();
        slika02.setPictureName("Slika02");
        slika02.setCategory(cat3);
        slika02.setLokacija("portlets/ecards/slike/Narava/02.JPG");
        EPictureDAO.savePicture(slika02);
        slika03 = null;
        slika03 = new EPicture();
        slika03.setPictureName("Slika03");
        slika03.setCategory(cat2);
        slika03.setLokacija("portlets/ecards/slike/Narava/03.JPG");
        EPictureDAO.savePicture(slika03);
        slika04 = null;
        slika04 = new EPicture();
        slika04.setPictureName("Slika04");
        slika04.setCategory(cat3);
        slika04.setLokacija("portlets/ecards/slike/Narava/04.JPG");
        EPictureDAO.savePicture(slika04);
        slika05 = null;
        slika05 = new EPicture();
        slika05.setPictureName("Slika05");
        slika05.setCategory(cat1);
        slika05.setLokacija("portlets/ecards/slike/Narava/05.JPG");
        EPictureDAO.savePicture(slika05);
        slika06 = null;
        slika06 = new EPicture();
        slika06.setPictureName("Slika06");
        slika06.setCategory(cat2);
        slika06.setLokacija("portlets/ecards/slike/Narava/06.JPG");
        EPictureDAO.savePicture(slika06);
        slika07 = null;
        slika07 = new EPicture();
        slika07.setPictureName("Slika07");
        slika07.setCategory(cat3);
        slika07.setLokacija("portlets/ecards/slike/Narava/07.JPG");
        EPictureDAO.savePicture(slika07);
        slika08 = null;
        slika08 = new EPicture();
        slika08.setPictureName("Slika08");
        slika08.setCategory(cat1);
        slika08.setLokacija("portlets/ecards/slike/Narava/08.JPG");
        EPictureDAO.savePicture(slika08);
        slika09 = null;
        slika09 = new EPicture();
        slika09.setPictureName("Slika09");
        slika09.setCategory(cat3);
        slika09.setLokacija("portlets/ecards/slike/Narava/09.JPG");
        EPictureDAO.savePicture(slika09);
        slika10 = null;
        slika10 = new EPicture();
        slika10.setPictureName("Slika10");
        slika10.setCategory(cat2);
        slika10.setLokacija("portlets/ecards/slike/Narava/10.JPG");
        EPictureDAO.savePicture(slika10);
    }

    public void dodajKat(String naziv) {
        Session s = HibernateUtil.currentSession();
        Transaction tx = s.beginTransaction();
        ECategory kat = new ECategory();
        kat.setName(naziv);
        s.saveOrUpdate(kat);
        tx.commit();
        HibernateUtil.closeSession();
    }

    public void dodajSliko(String ime, ECategory cat, String loc) {
        Session s = HibernateUtil.currentSession();
        Transaction tx = s.beginTransaction();
        EPicture sl = new EPicture();
        sl.setPictureName(ime);
        sl.setCategory(cat);
        sl.setLokacija(loc);
        s.saveOrUpdate(sl);
        tx.commit();
        HibernateUtil.closeSession();
    }
}
