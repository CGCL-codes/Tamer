package dao;

import java.util.ArrayList;
import pojo.Bid;

/**
 *
 * @author luctanbinh
 */
public class BidDao {

    public static ArrayList<Bid> getAll() {
        ArrayList<Bid> lst = new ArrayList<Bid>();
        try {
            String hql = "from Bid where deleted = 0";
            DaoHelper helper = new DaoHelper(hql);
            lst = (ArrayList<Bid>) helper.getListResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lst;
    }

    public static Bid get(int id) {
        Bid Bid = new Bid();
        try {
            String hql = "from Bid where id = :ID and deleted = 0";
            DaoHelper helper = new DaoHelper(hql);
            helper.setParam("ID", id);
            Bid = (Bid) helper.getUniqueResult();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Bid;
    }

    public static boolean save(Bid Bid) {
        Bid.setDeleted(Boolean.FALSE);
        try {
            DaoHelper helper = new DaoHelper();
            helper.saveOrUpdate(Bid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static boolean delete(Bid Bid) {
        Bid.setDeleted(Boolean.TRUE);
        try {
            DaoHelper helper = new DaoHelper();
            helper.saveOrUpdate(Bid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}
