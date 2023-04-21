package com.googlecode.dbtesting;

import com.googlecode.dbtesting.datastubs.PersonData;
import com.googlecode.dbtesting.dto.AddressDTO;
import com.googlecode.dbtesting.dto.TelephoneDTO;
import com.googlecode.dbtesting.dto.UserDTO;
import com.googlecode.dbtesting.dto.LogDTO;
import org.hibernate.Session;
import java.util.Date;
import java.util.List;

public class FillDatabase {

    private final int MAX_USERS = 10;

    private final int MAX_ADDRESSES = 100;

    private final int MAX_TELEPHONES = 1000;

    public static void main(String[] args) {
        FillDatabase fill = new FillDatabase();
        fill.run();
    }

    @SuppressWarnings("unchecked")
    public void run() {
        PersonData pd = PersonData.getInstance();
        for (int userNum = 0; userNum < MAX_USERS; userNum++) {
            Date begin = new Date();
            Session session1 = HibernateUtil.getSession();
            session1.beginTransaction();
            UserDTO user = new UserDTO();
            user.name = pd.getNextName() + " " + pd.getNextSoname() + " " + userNum;
            user.birthDay = new Date();
            session1.save(user);
            session1.getTransaction().commit();
            session1.close();
            for (int addressNum = 0; addressNum < MAX_ADDRESSES; addressNum++) {
                Session session2 = HibernateUtil.getSession();
                session2.beginTransaction();
                AddressDTO address = new AddressDTO();
                address.name = "Address" + addressNum + " for user " + user.name;
                address.user = user;
                session2.save(address);
                for (int telNum = 0; telNum < MAX_TELEPHONES; telNum++) {
                    TelephoneDTO tel = new TelephoneDTO();
                    tel.number = "Tel " + telNum + " for address" + address.name;
                    tel.address = address;
                    session2.save(tel);
                }
                session2.getTransaction().commit();
                session2.close();
                System.out.println("Commiting address " + address.name + " " + address.user);
            }
            System.out.println("Commiting user " + user.name + " " + user.birthDay);
            Date end = new Date();
            LogDTO log = new LogDTO();
            log.dateBegin = begin;
            log.dateEnd = end;
            log.duration = (int) (end.getTime() - begin.getTime());
            log.comment = "User number " + userNum + " done in " + log.duration + " ms";
            Session sessionLog = HibernateUtil.getSession();
            sessionLog.beginTransaction();
            sessionLog.save(log);
            sessionLog.getTransaction().commit();
            sessionLog.close();
        }
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        List<LogDTO> result = (List<LogDTO>) session.createQuery("from LogDTO").list();
        session.getTransaction().commit();
        session.close();
        for (LogDTO log : result) System.out.println("LogDTO: " + log.dateBegin + " " + log.dateEnd + " " + log.duration + " " + log.comment);
    }
}
