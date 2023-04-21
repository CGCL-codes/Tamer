package test.odmg;

import java.io.*;
import org.odmg.*;
import org.ozoneDB.odmg.*;

public class Client {

    public static Implementation odmg;

    public static void main(String[] args) throws Exception {
        odmg = new OzoneODMG();
        Database db = odmg.newDatabase();
        db.open("ozonedb:remote://localhost:3333", Database.OPEN_READ_WRITE);
        Transaction tx = odmg.newTransaction();
        tx.begin();
        Auto auto = null;
        try {
            auto = (Auto) db.lookup("name");
        } catch (Exception e) {
            auto = new Auto("Name");
            System.out.println(auto.getClass().getSuperclass().getName());
            System.out.println(Thread.currentThread().getName() + ": " + auto);
            db.bind(auto, "name");
        }
        auto.setName("Trabant");
        System.out.println(Thread.currentThread().getName() + ": " + auto);
        tx.commit();
        db.close();
    }
}
