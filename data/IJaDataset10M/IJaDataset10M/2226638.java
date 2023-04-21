package ru.dreamjteam.entity;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;
import javax.ejb.RemoveException;

public interface PointEntityBeanLocalHome extends EJBLocalHome {

    PointEntityBeanLocal findByPrimaryKey(Integer key) throws FinderException;

    Collection findChain(Integer id) throws FinderException;

    PointEntityBeanLocal createPoint(String addr, Integer prevId) throws CreateException;
}
