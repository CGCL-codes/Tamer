package oe34_array_declare_ini;

/**
 *
 * @author SCJP
 */
public class Student {

    String name;

    int id;

    double age;

    boolean vip;

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean get_a_adfsdafsadasdfasdfasd_Vip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public Student() {
        System.out.println("123");
    }

    public Student(String x) {
        this.name = x;
        int i = 1;
        String j;
        j = x;
        i++;
        Student k = new VIPStudent("k");
        Student k1;
        k1 = k;
        for (i = 1; i < 10; i++) {
        }
        System.out.println(i);
    }

    public Student(String name, int id, double age) {
        this.name = name;
        this.id = id;
        this.age = age;
        int i = Inf.k;
    }

    void pay() {
    }

    static void goOut() {
        System.out.println("keke");
    }

    void goOut(int x) {
    }
}
