package net.sf.brightside.gymcalendar.metamodel;

import java.util.List;

public interface BodyPart {

    String getName();

    void setName(String name);

    List<Measurement> getMeasurements();
}
