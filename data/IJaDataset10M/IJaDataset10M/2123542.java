package net.sf.jaybox.iso;

import java.io.*;
import net.sf.jaybox.common.*;

public class DebugReader implements ReaderCallback {

    private int n;

    public DebugReader() {
        n = 0;
    }

    public void onEnter(String path, String dir) {
        for (int i = 0; i < n; i++) System.out.print(" ");
        System.out.println(dir);
        n++;
    }

    public void onLeave(String path, String dir) {
        n--;
    }

    public void onFile(ReadHelper rh, String path, DirHeader dh) {
        for (int i = 0; i < n; i++) System.out.print(" ");
        System.out.println(dh.name + ", " + dh.size + " bytes");
    }

    public void onExit() {
        System.out.println("ALL DONE!");
    }

    public void setProgress(Progress p) {
    }
}
