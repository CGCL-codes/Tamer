package de.debeka.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.stream.FileImageInputStream;
import de.debeka.tiere.Schwein;

public class Main {

    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream("piggy.schwein");
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            List<Schwein> liste = (List<Schwein>) in.readObject();
            in.close();
            System.out.println(Schwein.getAnzahl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
