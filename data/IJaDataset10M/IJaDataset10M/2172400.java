package test;

import java.io.*;

public class TSerial {

    public static void main(String[] args) throws Exception {
        MostFields f = new MostFields(1, 2, 3);
        FileOutputStream fos = new FileOutputStream("TSerial.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(f);
        oos.flush();
        fos.close();
        System.out.println(f);
        FileInputStream fis = new FileInputStream("TSerial.dat");
        ObjectInputStream iis = new ObjectInputStream(fis);
        MostFields f2 = (MostFields) iis.readObject();
        fis.close();
        System.out.println(f2);
        java.lang.System.exit(0);
    }

    static class MostFields implements Serializable {

        private final int m_int;

        private final transient int m_foo;

        private transient int m_bar = 2;

        MostFields() {
            m_int = 5;
            m_foo = 5;
            m_bar = 2;
        }

        MostFields(int i, int foo, int bar) {
            m_int = 1;
            m_foo = foo;
            m_bar = bar;
        }

        public String toString() {
            return "Int = " + m_int + ", Foo = " + m_foo + ", Bar = " + m_bar;
        }
    }
}
