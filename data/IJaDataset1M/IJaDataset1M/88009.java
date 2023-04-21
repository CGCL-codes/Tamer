package com.discourse.lambda;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import com.discourse.lambda.Predicate;

public class Dictionary {

    /**
     * the built-in predicates
     */
    List<Predicate> builtin = new ArrayList<Predicate>();

    List<Entry> entries;

    public String getEntry(String s) {
        for (int n = 0; n < entries.size(); n++) if (entries.get(n).getWord() != null && s.compareTo(entries.get(n).getWord()) == 0) return entries.get(n).toString();
        return "Entry not found";
    }

    public Entry getEntryEntry(String s) {
        for (int n = 0; n < entries.size(); n++) if (entries.get(n).getWord() != null && s.compareTo(entries.get(n).getWord()) == 0) return entries.get(n);
        return null;
    }

    public Dictionary() {
        this.entries = new ArrayList<Entry>();
    }

    public Dictionary(List<Entry> l) {
        this.entries = new ArrayList<Entry>(l);
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Predicate> getBuiltin() {
        return builtin;
    }

    public int parse(String filename) {
        String current_line = "";
        try {
            FileReader f = new FileReader(filename);
            BufferedReader in = new BufferedReader(f);
            current_line = in.readLine();
            while (current_line != null) {
                String s = "";
                Entry e = new Entry();
                s = current_line.substring(0, current_line.indexOf(":"));
                e.setWord(s);
                current_line = current_line.replaceAll(" ", "");
                current_line = current_line.substring(current_line.indexOf(":") + 1, current_line.length());
                String cat = current_line.substring(0, current_line.indexOf(":"));
                String lam = current_line.substring(current_line.indexOf(":") + 1, current_line.length());
                String extra = "";
                if (lam.indexOf(":") > 0) {
                    extra = lam.substring(lam.indexOf(":") + 1, lam.length());
                    lam = lam.substring(0, lam.indexOf(":"));
                }
                while (cat.length() > 0) {
                    if (cat.indexOf(",") > 0) {
                        e.addCategory(cat.substring(0, cat.indexOf(",")));
                        cat = cat.substring(cat.indexOf(",") + 1, cat.length());
                        if (lam.indexOf(",") > 0) {
                            e.addLambda(lam.substring(0, lam.indexOf(",")));
                            lam = lam.substring(lam.indexOf(",") + 1, lam.length());
                        } else {
                            e.addLambda(lam);
                            if (!extra.equals("")) e.addParLambda(extra);
                        }
                    } else {
                        if (lam.indexOf(",") > 0) {
                            e.addLambda(lam.substring(0, lam.indexOf(",")));
                            lam = lam.substring(lam.indexOf(",") + 1, lam.length());
                            e.addCategory(cat);
                        } else {
                            e.addLambda(lam);
                            e.addCategory(cat);
                            cat = "";
                        }
                    }
                }
                while (extra.length() > 0) {
                    if (extra.indexOf(",") > 0) {
                        String ss = extra.substring(0, extra.indexOf(","));
                        e.addParLambda(ss);
                        extra = extra.substring(extra.indexOf(","), extra.length());
                    } else {
                        e.addParLambda(extra);
                        extra = "";
                    }
                }
                this.entries.add(e);
                current_line = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }
}
