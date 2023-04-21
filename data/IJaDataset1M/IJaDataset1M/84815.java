package net.sf.zorobot.tr;

import java.util.*;

public class MiruVerb extends InflictedVerb {

    public MiruVerb(Symbol verb) {
        super(verb);
        type = POS.SPECIAL_VERB;
    }

    public ArrayList getEnglish() {
        ArrayList l = verb.getEnglish();
        eng = l;
        return l;
    }

    public ArrayList getEnglishVerb(ArrayList subject, ArrayList ver, boolean plural, boolean past, boolean question) {
        if (ver == null || ver.size() == 0) {
            return getEnglish();
        }
        ArrayList wrapper = new ArrayList(1);
        ArrayList sb;
        boolean negative = ((InflictedVerb) verb).isNegative();
        boolean want = ((InflictedVerb) verb).isWant();
        for (int i = 0; i < ver.size(); i++) {
            sb = new ArrayList();
            ArrayList toAdd = (ArrayList) ver.get(i);
            if (negative) sb.add("\001");
            boolean neg = false;
            if (((String) toAdd.get(0)).equals("\001")) {
                neg = true;
                toAdd.remove(0);
            }
            if (want) {
                sb.add("want");
                if (neg) {
                    sb.add("to try not to");
                } else {
                    sb.add("to try to");
                }
            } else {
                sb.add("try");
                if (neg) sb.add("not to"); else sb.add("to");
            }
            sb.addAll(toAdd);
            wrapper.add(sb);
        }
        eng = wrapper;
        return wrapper;
    }

    public boolean isPast() {
        return ((InflictedVerb) verb).isPast();
    }

    public boolean isNegative() {
        System.out.println("Is negative is called: " + ((InflictedVerb) verb).isNegative());
        return ((InflictedVerb) verb).isNegative();
    }

    public boolean isTeForm() {
        return ((InflictedVerb) verb).isTeForm();
    }

    public boolean isUForm() {
        return ((InflictedVerb) verb).isUForm();
    }

    public boolean isWant() {
        return ((InflictedVerb) verb).isWant();
    }

    public boolean isQuestion() {
        return ((InflictedVerb) verb).isQuestion();
    }

    public ParseTreeNode getNode() {
        ParseTreeNode ptn = verb.getNode();
        ptn.eng = toEng();
        return ptn;
    }
}
