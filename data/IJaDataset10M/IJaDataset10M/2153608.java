package panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.table.AbstractTableModel;
import main.Controller;
import main.Course;

public class ResultTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private ArrayList<Course> courses;

    private String[] columnNames = { "Course", "Name", "Credits", "Score" };

    public ResultTableModel(ArrayList<Course> courses) {
        this.courses = courses;
        Comparator<Course> c = new Comparator<Course>() {

            public int compare(Course c1, Course c2) {
                if (c2.getScore() > c1.getScore()) return 1; else if (c2.getScore() < c1.getScore()) return -1; else return 0;
            }
        };
        Collections.sort(courses, c);
    }

    public Course getCourse(int id) {
        return courses.get(id);
    }

    private int getNumRecommendedCourses() {
        double suggestedCredits = Controller.getRecord().getSuggestedCredits();
        int numRecommendedCourses = 0;
        for (Course course : courses) {
            double credits = course.getCredits();
            if (credits <= suggestedCredits) {
                suggestedCredits -= credits;
                numRecommendedCourses++;
            } else break;
        }
        return numRecommendedCourses;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return courses.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Course course = courses.get(row);
        String str;
        switch(col) {
            case 0:
                str = course.getNumber();
                break;
            case 1:
                str = course.getName();
                break;
            case 2:
                str = Double.valueOf(course.getCredits()).toString();
                break;
            case 3:
                str = Double.valueOf(course.getScore()).toString();
                break;
            default:
                str = null;
                break;
        }
        if (row < getNumRecommendedCourses()) return Controller.embedHtml("<b>" + str + "</b>"); else return str;
    }
}
