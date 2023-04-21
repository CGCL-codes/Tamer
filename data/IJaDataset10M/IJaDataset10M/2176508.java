package home.projects.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProxyTest {

    public static void main(final String[] args) {
        List<Student> list = new ArrayList<Student>();
        Student student = new Student("tom", 12);
        list.add(student);
        student = new Student("dick", 34);
        list.add(student);
        student = new Student("harry", 0);
        list.add(student);
        student = new Student("a sexy girl", -12);
        list.add(student);
        Collections.sort(list);
        System.out.println(list);
        Collections.sort(list, new Comparator<Student>() {

            public int compare(Student o1, Student o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        System.out.println(list);
    }
}

enum SingletonClass {

    INSTANCE;

    private SingletonClass() {
        System.out.println("Initializing SingletonClass...");
    }

    public void doSomething() {
        System.out.println("doing something with " + this);
    }
}

class Student implements Comparable<Student> {

    private String name;

    private int id;

    public Student(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int compareTo(Student student) {
        return this.id - student.id;
    }

    @Override
    public String toString() {
        return "{name:" + name + ",id:" + id + "}";
    }
}
