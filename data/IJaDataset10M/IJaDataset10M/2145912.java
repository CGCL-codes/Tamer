package uk.co.rubox;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class humanPlayer implements rPlayer {

    rOutput output;

    rInput input;

    Object[] data;

    int dataidx;

    public void setDisplayMethod(String classname) {
        try {
            Class col = ClassLoader.getSystemClassLoader().loadClass(classname);
            Constructor colcon = col.getConstructor(new Class[] {});
            output = (rOutput) colcon.newInstance(new Object[] {});
        } catch (Exception e) {
            System.err.println("Error: The specified class does not implement rOutput interface.");
            System.exit(1);
        }
    }

    public humanPlayer() {
        data = new Object[10];
        for (int i = 0; i < 10; i++) {
            data[i] = null;
        }
        dataidx = 0;
    }

    public void makeMove(int x1, int y1, int z1, int x2, int y2, int z2, int type) {
    }

    public void attachOutput(rOutput outputsystem) {
        return;
    }

    public void attachInput(rInput inputsystem) {
        return;
    }

    public void attachController(rController maininstance) {
        return;
    }

    public void newGame(int x, int y, int z) {
        return;
    }

    public rOutput getOutput() {
        return output;
    }

    public rInput getInput() {
        return input;
    }

    public String toString() {
        return "HumanPlayer::" + (String) data[0];
    }

    public int createData(String type) {
        try {
            Class col = ClassLoader.getSystemClassLoader().loadClass(type);
            Constructor colcon = col.getConstructor(new Class[] {});
            data[dataidx] = colcon.newInstance(new Object[] {});
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        return dataidx++;
    }

    public int createData(Object value) {
        try {
            data[dataidx] = value;
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        return dataidx++;
    }

    public void setData(int index, Object value) {
        data[index] = value;
        return;
    }

    public Object getData(int index) {
        return data[index];
    }

    public Object obtainData(int index) {
        return new Object();
    }
}
