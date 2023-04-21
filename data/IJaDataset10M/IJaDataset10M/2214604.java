package com.Freshyboy.test;

import com.Freshyboy.bean.FreshyprofileBean;
import com.Freshyboy.dao.FreshyprofileDAO;
import com.Freshyboy.dao.HibernateDAOFactory;
import com.Freshyboy.dao.SchoolDAO;
import com.Freshyboy.domain.Freshyprofile;
import com.Freshyboy.domain.School;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Rilsikane
 */
public class test2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        String location;
        FreshyprofileDAO dao = HibernateDAOFactory.freshyprofileDAO;
        System.out.println(dao.getlocation(21L).getFilelocation());
    }
}
