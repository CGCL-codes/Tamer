package de.grogra.blocks.xFrogFileParser;

import java.util.StringTokenizer;

public class Branches extends Expr {

    public Branches(Expr a) {
        this.a = a;
        if (aktKeyFrame == 0) {
            String ss = a.toString().replaceAll("\n", " ");
            StringTokenizer sn = new StringTokenizer(ss);
            int anz = 0;
            while (sn.hasMoreTokens()) {
                anz++;
                sn.nextElement();
            }
            branches.put(aktStructName, anz + " " + ss);
            if (!children.containsKey(aktStructName)) {
                children.put(aktStructName, anz + " " + ss);
            } else {
                sn = new StringTokenizer((String) children.get(aktStructName));
                String ss3 = "";
                int anzA = Integer.parseInt((String) sn.nextElement());
                while (sn.hasMoreTokens()) {
                    ss3 += " " + sn.nextElement();
                }
                children.remove(aktStructName);
                children.put(aktStructName, (anz + anzA) + " " + ss + ss3);
            }
        }
        if (debug) System.out.println(getClass().getSimpleName() + " :: " + a);
    }

    @Override
    public String toString() {
        if (debugS) System.out.println("TS  " + getClass().getSimpleName());
        return "Branches {\n" + a.toString() + "\n" + "}\n";
    }
}
