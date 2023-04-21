package schooltimetablecsp;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * @author five_stars
 */
public class LeisureClass {

    protected int id;

    protected int year;

    protected char section;

    protected int tot_hour;

    protected List<Subject> modules;

    protected String timetable[][];

    protected List<Dispo> to_assign;

    protected List<Day> all_days;

    protected javax.swing.JTable jTable;

    protected javax.swing.table.DefaultTableModel model;

    public LeisureClass(int i, int y, char s) {
        this.id = i;
        this.year = y;
        this.section = s;
        to_assign = new LinkedList<Dispo>();
        all_days = new LinkedList<Day>();
        modules = new LinkedList<Subject>();
        this.timetable = new String[6][8];
    }

    public LeisureClass(int i, int y, char s, int h) {
        this.id = i;
        this.year = y;
        this.section = s;
        this.tot_hour = h;
        to_assign = new LinkedList<Dispo>();
        all_days = new LinkedList<Day>();
        modules = new LinkedList<Subject>();
        this.timetable = new String[6][8];
    }

    public LeisureClass(int i, int y, char s, int h, List<Day> dd) {
        this.id = i;
        this.year = y;
        this.section = s;
        this.tot_hour = h;
        this.all_days = dd;
        to_assign = new LinkedList<Dispo>();
        all_days = new LinkedList<Day>();
        modules = new LinkedList<Subject>();
        this.timetable = new String[6][8];
    }

    public int generateSlots(Day day, List<FlexiTime> hours, int start_id) {
        Iterator<FlexiTime> ft = hours.iterator();
        int count = start_id;
        while (ft.hasNext()) {
            to_assign.add(new Dispo(day, (FlexiTime) ft.next(), count, this));
            count += 1;
        }
        return count + 1;
    }

    public String toString() {
        return (Integer.toString(this.year) + Character.toString(this.section));
    }
}
