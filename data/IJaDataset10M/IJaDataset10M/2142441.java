package ac3.ead.spring.sample5;

public class InjectSimple {

    private String name;

    private int age;

    private float height;

    private boolean isProgrammer;

    public void setIsProgrammer(boolean isProgrammer) {
        this.isProgrammer = isProgrammer;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Name :" + name + "\n" + "Age:" + age + "\n" + "Height: " + height + "\n" + "Is Programmer?: " + isProgrammer;
    }
}
