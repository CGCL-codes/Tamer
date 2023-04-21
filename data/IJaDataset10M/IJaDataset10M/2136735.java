package org.vexi.instrument.memoryhist;

import java.awt.FlowLayout;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;
import org.ibex.js.*;

public class MemoryHist extends JS.Immutable implements Instr.Memory {

    public MemoryHist() {
        Instr.memory = this;
        new Panel().go();
    }

    private static Map hashToObj = new HashMap();

    private static boolean doesOverrideHashCode(Object o) {
        try {
            java.lang.reflect.Method m = o.getClass().getMethod("hashCode", new Class[] {});
            Class declaring = m.getDeclaringClass();
            if (Object.class != declaring) return false;
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void register(JS o) {
        if (!(o instanceof JSExn.ExnJSObj)) {
            if (!doesOverrideHashCode(o)) throw new RuntimeException("Cannot register objects with overridden hashcodes " + o.getClass().getName());
            Integer hash = new Integer(o.hashCode());
            hashToObj.put(hash, o);
        }
    }

    public synchronized void unregister(JS o) {
        if (!(o instanceof JSExn.ExnJSObj)) {
            Integer hash = new Integer(o.hashCode());
            hashToObj.remove(hash);
        }
    }

    private String lpad(String s) {
        while (s.length() < 4) s = " " + s;
        return s;
    }

    private void hist_inc(SortedMap<Integer, Integer> hist, Map hash) {
        Integer size = hash == null ? 0 : hash.size();
        Integer count = hist.get(size);
        if (count == null) count = 0;
        hist.put(size, count + 1);
    }

    private void hist_print(String title, SortedMap<Integer, Integer> hist) {
        System.out.println("--- " + title + " histogram ---");
        for (Integer k : hist.keySet()) {
            System.out.println(lpad(k + "") + ":   " + hist.get(k));
        }
    }

    final NumberFormat format = NumberFormat.getPercentInstance();

    {
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    private String hist_no(SortedMap<Integer, Integer> hist) {
        Integer no = hist.get(0);
        if (no == null) no = 0;
        double percent = (no) / (double) hashToObj.size();
        return format.format(percent);
    }

    private synchronized void printHistogram() {
        SortedMap<Integer, Integer> propsHist = new TreeMap();
        SortedMap<Integer, Integer> trapsHist = new TreeMap();
        int nboxes = 0;
        for (Iterator I = hashToObj.values().iterator(); I.hasNext(); ) {
            JS.Obj o = (JS.Obj) I.next();
            if (o instanceof org.vexi.core.Box) nboxes++;
            hist_inc(propsHist, o.getPropsMap());
            hist_inc(trapsHist, o.getTrapsMap());
        }
        int totalobjs = hashToObj.size();
        System.out.println("total objs: " + totalobjs);
        System.out.println("total boxes: " + nboxes);
        System.out.println("%no props: " + hist_no(propsHist));
        System.out.println("%no traps: " + hist_no(trapsHist));
        hist_print("properties", propsHist);
        hist_print("traps", trapsHist);
    }

    class Panel {

        public void go() {
            JFrame frame = new JFrame("Profiler Control");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());
            JButton button1 = new JButton("Print Histogram");
            button1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.gc();
                    System.runFinalization();
                    printHistogram();
                }
            });
            frame.add(button1);
            frame.setSize(100, 200);
            frame.setVisible(true);
        }
    }
}
