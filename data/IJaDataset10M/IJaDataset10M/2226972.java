package edu.stanford.genetics.treeview;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class ThreadListener extends Thread {

    boolean runin = true;

    Frame top;

    TextArea textarea;

    private static void print_thread_info(PrintStream out, Thread t, String indent) {
        if (t == null) return;
        out.println(indent + "Thread: " + t.getName() + " Priority: " + t.getPriority() + (t.isDaemon() ? "Daemon" : "Not Daemon") + (t.isAlive() ? " Alive" : " Not Alive"));
    }

    private static void list_group(PrintStream out, ThreadGroup g, String indent) {
        if (g == null) return;
        int num_threads = g.activeCount();
        int num_groups = g.activeGroupCount();
        Thread threads[] = new Thread[num_threads];
        ThreadGroup groups[] = new ThreadGroup[num_groups];
        g.enumerate(threads, false);
        g.enumerate(groups, false);
        out.println(indent + "Thread Group: " + g.getName() + " Max Priority " + g.getMaxPriority() + (g.isDaemon() ? " Daemon" : " Not Daemon"));
        for (int i = 0; i < num_threads; i++) print_thread_info(out, threads[i], indent + " ");
        for (int i = 0; i < num_groups; i++) list_group(out, groups[i], indent + " ");
    }

    public static void listAllThreads(PrintStream out) {
        ThreadGroup current_thread_group;
        ThreadGroup root_thread_group;
        ThreadGroup parent;
        current_thread_group = Thread.currentThread().getThreadGroup();
        root_thread_group = current_thread_group;
        parent = root_thread_group.getParent();
        while (parent != null) {
            root_thread_group = parent;
            parent = parent.getParent();
        }
        list_group(out, root_thread_group, "");
    }

    public synchronized void run() {
        while (runin == true) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(os);
            listAllThreads(ps);
            textarea.setText(os.toString());
            textarea.validate();
            textarea.repaint();
            try {
                this.wait(1000);
            } catch (InterruptedException e) {
                System.out.println("Somebody set us up the bomb!");
            }
        }
    }

    public void finish() {
        runin = false;
    }

    public ThreadListener() {
        top = new Frame();
        textarea = new TextArea(20, 100);
        top.add(textarea);
        top.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                runin = false;
                top.dispose();
            }
        });
        top.pack();
        top.setVisible(true);
    }
}
