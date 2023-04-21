package classes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import gui.Window;
import serialisation.File;

public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Mediatheque mediatheque = new Mediatheque();
        Window window = new Window(mediatheque);
        window.setVisible(true);
        mediatheque.getNom();
        User usr1 = new User("Thomas", "Browet", "tbrw", "toto", "tbrw86@gmail.com", "Iram Corp.");
        User usr2 = new User("Steeve", "Jobs", "steevee", "supermaster", "steevee@mac.com", "Apple Corp.");
        mediatheque.addUser(usr1);
        mediatheque.addUser(usr2);
    }
}
