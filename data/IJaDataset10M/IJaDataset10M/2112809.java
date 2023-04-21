package com.antoarts.med;

import java.util.ArrayList;

public class Commands {

    public static void show(ArrayList<String> args) {
        if (args.size() < 2) {
            for (int i = 0; i < BufferManager.getBufferList().get(BufferManager.getActiveBuffer()).getTextList().size(); i++) {
                System.out.println(BufferManager.getBufferList().get(BufferManager.getActiveBuffer()).getTextList().get(i));
            }
        } else if (args.size() == 2) {
        } else if (args.size() == 3 && args.get(2).equals("true")) {
            for (int i = 0; i < BufferManager.getBufferList().get(BufferManager.getActiveBuffer()).getTextList().size(); i++) {
                if (i % 5 == 0) {
                    System.out.println("\n\t1    5    10   15   20   25   30   35   40   45   50   55   60   65   70   75");
                }
                System.out.println(i + ".\t" + BufferManager.getBufferList().get(BufferManager.getActiveBuffer()).getTextList().get(i));
            }
        }
    }

    public static void append(ArrayList<String> args) {
    }

    public static void multiappend(ArrayList<String> args) {
    }

    public static void quit(ArrayList<String> args) {
        System.out.println("Quitting...");
        System.exit(0);
    }

    public static void listBuffers() {
        for (int i = 0; i < BufferManager.getBufferList().size(); i++) {
            System.out.printf("%d\t%s\t%s\n", i, BufferManager.getBufferList().get(i).getFilename(), BufferManager.getBufferList().get(i).getTextList().get(0));
        }
    }

    public static void changeBuffer(ArrayList<String> args) {
        if (args.size() < 2) {
            if (BufferManager.getActiveBuffer() == BufferManager.getBufferList().size() - 1) {
                BufferManager.setActiveBuffer(0);
            } else {
                BufferManager.setActiveBuffer(BufferManager.getActiveBuffer() + 1);
            }
        } else {
            try {
                if (BufferManager.getBufferList().size() > Integer.parseInt(args.get(1)) && Integer.parseInt(args.get(1)) >= 0) {
                    BufferManager.setActiveBuffer(Integer.parseInt(args.get(1)));
                }
            } catch (Exception e) {
                Helpers.printError(10);
            }
        }
    }

    public static void justify(ArrayList<String> args) {
    }

    public static void openFile(ArrayList<String> args) {
        if (args.size() == 2) {
            BufferManager.newBuffer(args.get(1));
            BufferManager.setActiveBuffer(BufferManager.getBufferList().size() - 1);
        } else if (args.size() == 3) {
            if (args.get(2).equals("true")) {
                BufferManager.newBuffer(args.get(1));
                BufferManager.setActiveBuffer(BufferManager.getBufferList().size() - 1);
            } else {
                BufferManager.getBufferList().get(BufferManager.getActiveBuffer()).open(args.get(1));
            }
        } else {
            Helpers.printError(10);
        }
    }

    public static void closeBuffer(ArrayList<String> args) {
        if (args.size() < 2) {
            BufferManager.closeBuffer(BufferManager.getActiveBuffer());
            if (BufferManager.getBufferList().size() < 1) {
                BufferManager.newBuffer();
            }
            BufferManager.setActiveBuffer(0);
        } else {
            BufferManager.closeBuffer(Integer.parseInt(args.get(1)));
        }
    }

    public static void printClipboardList(ArrayList<String> args) {
        for (int i = 0; i < ClipboardManager.getClipboardList().size(); i++) {
            System.out.printf("%d\t%s\n", i, ClipboardManager.getClipboardList().get(i));
        }
    }

    public static void removeFromClipboard(ArrayList<String> args) {
    }
}
