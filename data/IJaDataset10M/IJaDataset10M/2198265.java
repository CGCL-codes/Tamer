package fr.alesia.deepstack.bean;

import java.util.Comparator;
import java.util.Date;

public interface Tournament extends Comparable<Tournament>, Comparator<Tournament> {

    String getId();

    Date getDate();

    void setId(String id);

    void setDate(Date date);

    boolean isRunning();

    void setRunning(boolean running);
}
