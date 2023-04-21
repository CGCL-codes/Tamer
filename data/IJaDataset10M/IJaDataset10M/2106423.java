package trstudio.blueboxalife.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import trstudio.blueboxalife.bos.BOSVariable;
import trstudio.blueboxalife.graphic.physic.AlifeLine;
import trstudio.blueboxalife.graphic.physic.AlifePoint;
import trstudio.blueboxalife.graphic.physic.AlifeVector;

public class Serialization {

    public static void main(String[] args) {
        AlifeVector vector = new AlifeVector(10);
        serialize(vector);
        vector = new AlifeVector(1);
        deserialize(vector);
        AlifePoint point = new AlifePoint(10, 10);
        serialize(point);
        point = new AlifePoint(-1, -1);
        deserialize(point);
        AlifeLine line = new AlifeLine(new AlifePoint(1, 1), new AlifePoint(1, 9));
        serialize(line);
        line = new AlifeLine(new AlifePoint(-5, -5), new AlifePoint(50, 3));
        deserialize(line);
        BOSVariable variable = new BOSVariable();
        variable.setValue("Heeelloooo!");
        serialize(variable);
        variable = new BOSVariable(10);
        deserialize(variable);
        variable = new BOSVariable();
        variable.setValue(vector);
        serialize(variable);
        variable = new BOSVariable(10);
        deserialize(variable);
    }

    private static void serialize(Object object) {
        System.out.println();
        String filename = object.getClass().getSimpleName();
        System.out.println("Before serialization of " + filename + ":");
        System.out.println(object.toString());
        try {
            FileOutputStream fos = new FileOutputStream(filename + ".serial");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            try {
                oos.writeObject(object);
                oos.flush();
            } finally {
                oos.close();
                fos.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void deserialize(Object object) {
        String filename = object.getClass().getSimpleName();
        try {
            FileInputStream fis = new FileInputStream(filename + ".serial");
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                object = ois.readObject();
            } finally {
                try {
                    ois.close();
                } finally {
                    fis.close();
                }
            }
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println("After serialization of " + filename + ":");
        System.out.println(object.toString());
    }
}
