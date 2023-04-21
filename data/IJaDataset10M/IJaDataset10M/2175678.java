package sample.core;

import java.sql.Date;

public interface Patient extends Person {

    public Date getLastCheckup();

    public void setLastCheckup(Date date);

    public Physician getPhysician();

    public void setPhysician(Physician physician);
}
