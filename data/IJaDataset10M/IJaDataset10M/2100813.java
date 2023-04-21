package distributed;

import java.io.Serializable;

public class TestClass implements Serializable {

    private static final long serialVersionUID = 6534231380836688760L;

    private int age;

    private String name;

    private LocalObjectId localObjectId;

    public TestClass() {
        age = 0;
        name = "";
        localObjectId = new LocalObjectId();
    }

    public TestClass(int age, String name) {
        this.age = age;
        this.name = name;
        localObjectId = new LocalObjectId();
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int[] testMethod(int[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = a[i] + 1;
        }
        return b;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "age = " + age + ", name = " + name;
    }

    public LocalObjectId getLocalObjectId() {
        return localObjectId;
    }
}
