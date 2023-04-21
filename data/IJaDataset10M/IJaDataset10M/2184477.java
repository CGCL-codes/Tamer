package net.sf.brightside.commercials.metamodel;

import java.util.Date;

public interface CommercialShowing {

    String getTerm();

    void setTerm(String term);

    int getDuration();

    void setDuration(int duration);

    Date getDate();

    void setDate(Date date);

    Commercial getCommercial();

    void setCommercial(Commercial commercial);

    Television getTelevision();

    void setTelevision(Television television);
}
