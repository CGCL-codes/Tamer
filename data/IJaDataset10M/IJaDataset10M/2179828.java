package ru.dreamjteam.entity;

import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author abolmasov (18.10.2011 18:27:09)
 * @version $Revision: $
 */
public interface LocalOrderEntity extends EJBLocalObject {

    public Integer getId();

    public void setId(Integer id);

    public Integer getCost();

    public void setCost(Integer cost);

    public Integer getDistance();

    public void setDistance(Integer distInfact);

    public Integer getPassengers();

    public void setPassengers(Integer passengers);

    public Timestamp getTimeDest();

    public void setTimeDest(Timestamp timeDest);

    public Timestamp getTimeOrd();

    public void setTimeOrd(Timestamp timeOrd);

    public Boolean getCompleted();

    public String getAddrDep();

    public void setAddrDep(String addrDep);

    public String getAddrDest();

    public void setAddrDest(String addrDest);

    public Integer getCarId();

    public void setCarId(Integer carId);

    public String getPhone();

    public void setPhone(String phone);

    OrderVO getOrderVO(Boolean withDependences) throws FinderException, NamingException;

    void setOrderVO(OrderVO value);
}
