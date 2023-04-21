package ru.cos.sim.visualizer.traffic.parser.trace.base;

import java.util.ArrayList;
import org.jdom.Element;
import ru.cos.sim.visualizer.traffic.parser.base.Entity;

public class StaffCollector extends Entity {

    protected ArrayList<Staff> staffs;

    public StaffCollector(Element e) {
        super(e);
    }

    public StaffCollector() {
        super();
        this.staffs = new ArrayList<Staff>();
    }

    public StaffCollector(int uid) {
        super(uid);
    }

    public ArrayList<Staff> getStaffs() {
        return staffs;
    }
}
