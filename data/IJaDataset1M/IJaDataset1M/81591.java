package cn.openlab.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ObjectSerialization {

    public static void main(String[] args) throws Exception {
        ArrayList<Integer> l = (ArrayList<Integer>) getObject();
        for (Integer i : l) {
            System.out.println(i);
        }
    }

    public static void saveObject(Object o) throws Exception {
        File file = new File("f:\\list.dat");
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(o);
        oos.close();
        fos.close();
    }

    public static Object getObject() throws Exception {
        File file = new File("f:\\list.dat");
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object o = ois.readObject();
        ois.close();
        fis.close();
        return o;
    }
}
