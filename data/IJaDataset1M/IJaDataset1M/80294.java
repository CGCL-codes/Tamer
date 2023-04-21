package org.vrspace.client;

import org.vrspace.util.*;
import java.io.*;
import java.util.Observable;

public class Camera extends org.vrspace.util.Console {

    public boolean record = true;

    public static void main(String[] args) {
        new Logger();
        Camera cam = new Camera();
        cam.login();
        cam.run();
    }

    ;

    public Camera() {
        super();
        try {
            output = new PrintStream(new FileOutputStream("camera.log", true), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print(String s) {
        System.out.print(s);
    }

    public void update(Observable conn, Object arg) {
        if (record) {
            output.println(System.currentTimeMillis() + " " + arg);
        }
        System.out.println(arg);
    }
}
