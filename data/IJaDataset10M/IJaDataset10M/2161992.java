package ch.gmtech.lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class Report {

    public void populateMap() throws Exception {
        Collection<Student> schedules = Student.all();
        for (Iterator<Student> eachSchedule = schedules.iterator(); eachSchedule.hasNext(); ) {
            Student schedule = eachSchedule.next();
            for (Iterator<Offering> each = schedule.schedule.iterator(); each.hasNext(); ) {
                populateMapFor(schedule, each.next());
            }
        }
    }

    public void write(StringBuffer buffer) throws Exception {
        populateMap();
        Enumeration<Integer> enumeration = offeringToName.keys();
        while (enumeration.hasMoreElements()) {
            Integer offeringId = enumeration.nextElement();
            ArrayList<String> list = offeringToName.get(offeringId);
            writeOffering(buffer, list, Offering.find(offeringId.intValue()));
        }
        buffer.append("Number of scheduled offerings: ");
        buffer.append(offeringToName.size());
        buffer.append("\n");
    }

    public void writeOffering(StringBuffer buffer, ArrayList<String> list, Offering offering) {
        buffer.append(offering.getCourse().getName() + " " + offering.getDaysTimes() + "\n");
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
            String s = iterator.next();
            buffer.append("\t" + s + "\n");
        }
    }

    private void populateMapFor(Student schedule, Offering offering) {
        ArrayList<String> list = offeringToName.get(new Integer(offering.getId()));
        if (list == null) {
            list = new ArrayList<String>();
            offeringToName.put(new Integer(offering.getId()), list);
        }
        list.add(schedule._name);
    }

    Hashtable<Integer, ArrayList<String>> offeringToName = new Hashtable<Integer, ArrayList<String>>();
}
