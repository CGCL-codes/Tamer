package gem.model.test;

import gem.model.course.Association;
import gem.model.course.Course;
import gem.model.course.Department;
import gem.model.course.ScienceDomain;

public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        java.util.Vector<Department> deps = gem.database.ReaderWriter.loadDepartmentsFully();
        CourseFrame cf = new CourseFrame();
        cf.setDepartments(deps);
        cf.setVisible(true);
        for (Department d : deps) {
            java.util.Vector<ScienceDomain> sdomains = d.getScienceDomains();
            for (ScienceDomain sd : sdomains) {
                java.util.Vector<Course> courses = sd.getCourses();
                for (Course c : courses) {
                }
            }
        }
    }

    public static void insertNew() {
        Department nova = new Department("Nova test katedra");
        nova.addScienceDomain(new ScienceDomain("jedan", nova));
        nova.addScienceDomain(new ScienceDomain("dva", nova));
        nova.addScienceDomain(new ScienceDomain("tri", nova));
        gem.database.ReaderWriter.insertNewDepartmentFully(nova);
    }
}
