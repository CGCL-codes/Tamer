package dxunderground.TestData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;

public class ObjSerializationWrite {

    public static void main(String[] args) {
        Employee sue = new Employee("Sue Smith", 12, 45000f);
        Employee bob = new Employee("Bob Brown", 14, 45000f);
        ObjectOutputStream objOut = null;
        try {
            FileOutputStream fos = new FileOutputStream("EmployeeObjData");
            objOut = new ObjectOutputStream(fos);
            objOut.writeObject(sue);
            objOut.writeObject(bob);
        } catch (NotSerializableException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            if (objOut != null) {
                try {
                    objOut.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        }
    }
}
