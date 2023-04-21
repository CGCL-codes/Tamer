package com.zom.hibernate.associationmapping.bidirectionalnojoin.onetoone2;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.zom.hibernate.associationmapping.bidirectionalnojoin.onetoone2.BidO2ONoJoinAddress2;
import com.zom.hibernate.associationmapping.bidirectionalnojoin.onetoone2.BidO2ONoJoinPerson2;

public class InsertClient {

    public static void main(String args[]) {
        Session session = new Configuration().configure().buildSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        BidO2ONoJoinPerson2 person1 = new BidO2ONoJoinPerson2();
        BidO2ONoJoinPerson2 person2 = new BidO2ONoJoinPerson2();
        BidO2ONoJoinAddress2 address1 = new BidO2ONoJoinAddress2();
        address1.setPerson(person1);
        session.save(address1);
        BidO2ONoJoinAddress2 address2 = new BidO2ONoJoinAddress2();
        address2.setPerson(person2);
        session.save(address2);
        person1.setAddress(address1);
        session.save(person1);
        person1.setAddress(address2);
        session.save(person1);
        address1.setPerson(person1);
        session.saveOrUpdate(address1);
        tx.commit();
    }
}
