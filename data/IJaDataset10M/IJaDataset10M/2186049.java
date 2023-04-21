package gem.model.test;

import gem.model.course.Course;
import gem.model.course.Department;
import gem.model.course.ScienceDomain;

public class DepartmentTreeTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Department d1 = new Department("Kat 1");
        ScienceDomain s1 = new ScienceDomain("Oblast 1 1", d1);
        ScienceDomain s2 = new ScienceDomain("Oblast 1 2", d1);
        Course c1 = new Course("Kurs 1 1 1", s1);
        Course c2 = new Course("Kurs 1 2 2", s2);
        Department d2 = new Department("Kat 2");
        s1.addCourse(c2);
        System.out.println(d1);
        for (ScienceDomain s : d1.getScienceDomains()) {
            System.out.print(s);
            System.out.println("------");
            for (Course c : s.getCourses()) {
                System.out.println(c);
            }
        }
        System.out.println(d2);
        d2.addScienceDomain(s1);
        for (ScienceDomain s : d2.getScienceDomains()) {
            System.out.print(s);
            System.out.println("------");
            for (Course c : s.getCourses()) {
                System.out.println(c);
            }
        }
    }
}
