package dao;

import java.io.Serializable;
import bean.Orderlist;

public interface IOrderlistDao {

    public Orderlist findById(int id);

    public Serializable saveOrderlist(Orderlist orderlist);

    public void delete(int id);
}
