package pogvue.datamodel;

import java.util.*;
import java.awt.event.*;
import pogvue.gui.hub.*;
import pogvue.gui.event.*;
import pogvue.gui.*;

public class SequenceFragment extends Sequence implements ActionListener {

    Vector frags;

    String type;

    Vector threads = null;

    Controller c;

    AlignViewport av;

    String org;

    public SequenceFragment(String name, String sequence, int start, int end) {
        super(name, sequence, start, end);
        this.length = end - start + 1;
        frags = new Vector();
    }

    public void setAlignViewport(AlignViewport av) {
        this.av = av;
        if (av.getController() != null) {
            setController(av.getController());
        }
    }

    public void setController(Controller c) {
        this.c = c;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public void getSequenceRegion(int tmpstart, int tmpend) {
        Fragment found = null;
        for (int i = 0; i < frags.size(); i++) {
            Fragment f = (Fragment) frags.elementAt(i);
            if (f.getStart() <= tmpstart && f.getEnd() >= tmpend) {
                found = (Fragment) frags.elementAt(i);
            }
        }
        Vector newfrags = findFrags(tmpstart, tmpend);
        for (int i = 0; i < newfrags.size(); i++) {
            Fragment f = (Fragment) newfrags.elementAt(i);
            fetchFragment((Fragment) newfrags.elementAt(i));
            frags.addElement(f);
        }
    }

    public void fetchFragment(Fragment f) {
        GetRegionThread r = new GetRegionThread(getName(), String.valueOf(f.getStart()), String.valueOf(f.getEnd()), this, f, org);
        r.setFragment(f);
        r.start();
        if (threads == null) {
            threads = new Vector();
        }
        threads.addElement(r);
    }

    public Vector findFrags(int start, int end) {
        Vector newFrags = new Vector();
        if (frags.size() == 0) {
            Fragment f = new Fragment(start, end, "");
            newFrags.addElement(f);
            return newFrags;
        }
        Collections.sort(frags, new FragmentStartComparer());
        Fragment prev = null;
        for (int i = 0; i < frags.size(); i++) {
            Fragment f1 = (Fragment) frags.elementAt(i);
            if (i == 0) {
                int tmpstart = 1;
                int tmpend = f1.getStart() - 1;
                if (!(tmpstart > end || tmpend < start)) {
                    if (start < tmpstart) {
                        start = tmpstart;
                    }
                    if (end > tmpend) {
                        end = tmpend;
                    }
                    Fragment f = new Fragment(start, end, "");
                    newFrags.addElement(f);
                }
            }
            if (i > 0) {
                int tmpstart = prev.getEnd() + 1;
                int tmpend = ((Fragment) frags.elementAt(i)).getStart() - 1;
                if (tmpend >= tmpstart) {
                    if (!(tmpstart > end || tmpend < start)) {
                        if (start < tmpstart) {
                            start = tmpstart;
                        }
                        if (end > tmpend) {
                            end = tmpend;
                        }
                        Fragment f = new Fragment(start, end, "");
                        newFrags.addElement(f);
                    }
                }
            }
            if (i == frags.size() - 1) {
                int tmpstart = f1.getEnd() + 1;
                int tmpend = getEnd();
                if (!(tmpstart > end || tmpend < start)) {
                    if (start < tmpstart) {
                        start = tmpstart;
                    }
                    if (end > tmpend) {
                        end = tmpend;
                    }
                    Fragment f = new Fragment(start, end, "");
                    newFrags.addElement(f);
                }
            }
            prev = (Fragment) frags.elementAt(i);
        }
        return newFrags;
    }

    public void printFrags() {
        System.out.println("\n*****");
        for (int i = 0; i < frags.size(); i++) {
            Fragment f = (Fragment) frags.elementAt(i);
            System.out.println(" - " + f.getStart() + " " + f.getEnd() + " " + (f.getEnd() - f.getStart() + 1));
        }
        System.out.println("\n*****\n");
    }

    public String getSubString(int i, int j) {
        StringBuffer s = new StringBuffer();
        while (i <= j) {
            s.append(getCharAt(i));
            i++;
        }
        return s.toString();
    }

    public String getSequence(int start, int end) {
        StringBuffer str = new StringBuffer();
        for (int i = start; i <= end; i++) {
            str.append(getCharAt(i));
        }
        return str.toString();
    }

    public char getCharAt_dummy(int coord) {
        return 'A';
    }

    public char getCharAt(int coord) {
        char c = 'A';
        boolean found = false;
        for (int i = 0; i < frags.size(); i++) {
            Fragment f = (Fragment) frags.elementAt(i);
            if (!(coord + getStart() - 1 > f.getEnd() || coord + getStart() - 1 < f.getStart())) {
                if (f.getStr().length() > 0) {
                    c = f.getStr().charAt(coord - f.getStart() + getStart());
                } else {
                    c = 'C';
                }
                found = true;
            }
        }
        if (found == true) {
            return c;
        }
        int tmpstart = coord - 100000 + getStart();
        if (tmpstart < 1) {
            tmpstart = 1;
        }
        int tmpend = coord + 100000 + getStart();
        if (tmpend > getEnd()) {
            tmpend = getEnd();
        }
        getSequenceRegion(tmpstart, tmpend);
        for (int i = 0; i < frags.size(); i++) {
            Fragment f = (Fragment) frags.elementAt(i);
            if (!(f.getStart() > coord + getStart() - 1 || f.getEnd() < coord + getStart() - 1)) {
                if (f.getStr() != null && f.getStr().length() > coord) {
                    c = f.getStr().charAt(coord - f.getStart() + getStart());
                } else {
                    c = 'T';
                }
            }
        }
        return c;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Done")) {
            for (int ii = 0; ii < threads.size(); ii++) {
                GetRegionThread t = (GetRegionThread) threads.elementAt(ii);
                Fragment f = t.getFragment();
                if (e.getSource() == t) {
                    SequenceI[] seqs = t.getOutput();
                    f.setStr(seqs[1].getSequence());
                    threads.removeElement(t);
                    if (c != null) {
                        if (!(av.getStartRes() > t.getFragment().getEnd() - getStart() || av.getEndRes() < t.getFragment().getStart() - getStart())) {
                        }
                    }
                }
            }
        }
    }
}
