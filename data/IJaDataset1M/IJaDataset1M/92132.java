package sample.core;

import java.sql.Timestamp;

public interface Address {

    public String getSuite();

    public void setSuite(String s);

    public String getStreet1();

    public void setStreet1(String s);

    public String getStreet2();

    public void setStreet2(String s);

    public String getCity();

    public void setCity(String s);

    public String getState();

    public void setState(String s);

    public String getZipCode();

    public void setZipCode(String s);

    public Timestamp getUpdateTime();
}
