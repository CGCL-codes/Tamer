package org.jopsdb.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObservationMatrix {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private Map lines = new HashMap();

    private List names = new ArrayList();

    private List times = new ArrayList();

    public void addObservation(Observation observation) {
        String dt = sdf.format(observation.getCreated());
        lines.put(dt, observation);
        for (Iterator it = observation.getMeasurements().iterator(); it.hasNext(); ) {
            Measurement m = (Measurement) it.next();
            if (names.indexOf(m.getNode().getName()) == -1) {
                names.add(m.getNode().getName());
            }
            if (times.indexOf(dt) == -1) {
                times.add(dt);
            }
        }
    }

    public String[] getNames() {
        return (String[]) names.toArray(new String[0]);
    }

    public List getTimes() {
        return times;
    }

    public Measurement getMeasurement(String time, String name) {
        Observation observation = (Observation) lines.get(time);
        if (observation != null) {
            for (Iterator it = observation.getMeasurements().iterator(); it.hasNext(); ) {
                Measurement m = (Measurement) it.next();
                if (m.getNode().getName().equals(name)) {
                    return m;
                }
            }
        } else {
            System.out.println("no o found time:" + time + " name:" + name);
        }
        return null;
    }
}
