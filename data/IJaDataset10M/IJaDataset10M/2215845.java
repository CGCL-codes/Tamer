package com.nhncorp.cubridqa.test.console;

import java.io.File;
import com.nhncorp.cubridqa.utils.FileUtil;

public class Rename {

    private static final String parent = "/home/cn11766/workspace/qa_repository/scenario/sql";

    private static final boolean RENAME = false;

    public static void main(String[] args) {
        File file = new File(parent);
        if (RENAME) {
            rename(file);
        } else {
            copy(file);
        }
    }

    private static String WILL = "";

    private static void copy(File file) {
        File[] sons = file.listFiles();
        if (null == sons) {
            System.out.println("null sons");
            return;
        }
        String path = file.getAbsolutePath();
        if (path.endsWith("/answers")) {
            System.out.println(" answers path--" + path);
            for (File son : sons) {
                String name = son.getName();
                String source = son.getAbsolutePath();
                String destion = source.replaceFirst(WILL + "_win", WILL).replaceFirst("/sql/_1", "/sql/_0");
                File destionFile = new File(destion);
                File desParent = destionFile.getParentFile();
                String absParent = desParent.getAbsolutePath() + "_win";
                File realParent = new File(absParent);
                File realSon = new File(realParent, name);
                destion = realSon.getAbsolutePath();
                System.out.println("source--" + source);
                System.out.println("destion--" + destion);
                if (!realParent.exists()) {
                    realParent.mkdirs();
                }
                FileUtil.copyFile(source, destion);
            }
            return;
        }
        if (!path.endsWith("_win") && file.getParentFile().getAbsoluteFile().equals(parent)) {
            return;
        }
        if (path.endsWith("_win")) {
            int pos = path.indexOf("/");
            WILL = path.substring(pos).replace("_win", "");
            System.out.println("WILL--" + WILL);
        }
        for (File son : sons) {
            if (!son.isDirectory()) {
                continue;
            }
            copy(son);
        }
    }

    private static void rename(File file) {
        String path = file.getAbsolutePath();
        if (path.endsWith("/answers")) {
            System.out.println(" answers path--" + path);
            File reNameTo = new File(file.getParent(), "answers_linux");
            System.out.println(" answers_linux path--" + reNameTo.getAbsolutePath());
            file.renameTo(reNameTo);
        }
        if (path.endsWith("_win")) {
            return;
        }
        File[] sons = file.listFiles();
        if (null == sons) {
            return;
        }
        for (File son : sons) {
            if (!son.isDirectory()) {
                continue;
            }
            rename(son);
        }
    }
}
