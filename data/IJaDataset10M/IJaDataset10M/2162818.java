package FDUFW.localhost.Sys;

import java.io.File;

public class ListRootsTest1 {

    void listRoots() {
        File[] roots = File.listRoots();
        for (int i = 0; i < roots.length; i++) {
            System.out.println(roots[i]);
        }
    }

    void listDirectory(String dir) {
        File f = new File(dir);
        File[] files = f.listFiles();
        if (files == null) return;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                System.out.println("-----------" + files[i] + " is a directory has more files bellow:");
                listDirectory(files[i].getAbsolutePath());
            } else {
                System.out.println(files[i]);
            }
        }
    }

    public static void main(String[] args) {
        ListRootsTest1 lrt = new ListRootsTest1();
        System.out.println("-----------" + "start list roots" + "----------");
        lrt.listRoots();
        System.out.println("-------" + "start list directory d:" + "-------");
        lrt.listDirectory("D:" + File.separator);
    }
}
