package compacted;

import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.text.BadLocationException;
import javax.swing.text.Segment;
import operation.TSDelC;
import operation.TSInsC;
import operation.TSUndelC;
import soct2.TSOperation;

public class CompactedModel extends ModelI {

    private ArrayList<CompactedChar> S = new ArrayList();

    private TreeMap<Integer, RemoteCaret> remotePmodel = new TreeMap();

    public CompactedModel() {
        S.add(new CompactedChar(']', 0));
    }

    public CompactedModel(String s) {
        setInitialState(s);
    }

    public CompactedModel(CompactedModel c) {
        for (int i = 0; i < c.S.size(); i++) {
            S.add(c.S.get(i).clone());
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < S.size() - 1; i++) {
            sb.append(S.get(i).getCharacter());
        }
        return sb.toString();
    }

    public void executeLocal(TSOperation op) {
        int pmodel = S.get(op.getPosition()).getPosition();
        if (op instanceof TSInsC) {
            CompactedChar c = new CompactedChar(op.getCharacter(), pmodel);
            S.add(op.getPosition(), c);
            for (int i = op.getPosition() + 1; i < S.size(); i++) S.get(i).incrementPosition();
        } else if (op instanceof TSDelC) {
            S.remove(op.getPosition());
        } else if (op instanceof TSUndelC) {
            CompactedChar c = new CompactedChar(op.getCharacter(), pmodel);
            S.add(op.getPosition(), c);
        }
        if (System.getProperty("CaretRemote").equals("YES")) updateRemoteCarets(op);
        op.setPosition(pmodel);
    }

    public void executeRemote(TSOperation op) {
        int pmodel = op.getPosition();
        int pview;
        if (System.getProperty("CaretRemote").equals("YES")) {
            pview = model2view(pmodel, op.getSid());
        } else pview = model2view(pmodel);
        if (op instanceof TSInsC) {
            CompactedChar c = new CompactedChar(op.getCharacter(), op.getPosition());
            S.add(pview, c);
            for (int j = pview + 1; j < S.size(); j++) S.get(j).incrementPosition();
        } else if (op instanceof TSDelC) {
            TSDelC del = (TSDelC) op;
            if (S.get(pview).getPosition() == pmodel) {
                S.remove(pview);
            }
        } else if (op instanceof TSUndelC) {
            CompactedChar c = new CompactedChar(op.getCharacter(), op.getPosition());
            S.add(pview, c);
        }
        if (System.getProperty("CaretRemote").equals("YES")) updateRemoteCarets(op);
        op.setPosition(pview);
    }

    public int model2view(int pmodel, int workspaceSid) {
        RemoteCaret remoteCaret = remotePmodel.get(workspaceSid);
        if (remoteCaret == null) {
            remoteCaret = new RemoteCaret(0, 0);
            remotePmodel.put(workspaceSid, remoteCaret);
        }
        int pview = getViewByRemoteCaret(remoteCaret, pmodel);
        remotePmodel.get(workspaceSid).setModel(pmodel);
        remotePmodel.get(workspaceSid).setView(pview);
        return pview;
    }

    private void remoteCaretUpdate(TSOperation opmodel, RemoteCaret remoteCaret) {
        int pmodel = remoteCaret.getModel();
        int pview = remoteCaret.getView();
        if (opmodel.getPosition() <= pmodel) {
            if (opmodel instanceof TSDelC) {
                if (pview > 0) pview--;
                pmodel = S.get(pview).getPosition();
            } else if (opmodel instanceof TSInsC) {
                pview++;
                pmodel = S.get(pview).getPosition();
            }
        }
        remoteCaret.setModel(pmodel);
        remoteCaret.setView(pview);
    }

    @Override
    public int getLength() {
        return S.size() - 1;
    }

    @Override
    public String getText(int offset, int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = offset; (i < offset + length) && (i < getLength()); i++) {
            sb.append(S.get(i).getCharacter());
        }
        return sb.toString();
    }

    @Override
    public String internalString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < S.size(); i++) {
            sb.append("(" + S.get(i).getPosition() + "," + S.get(i).getCharacter() + ")");
        }
        return sb.toString();
    }

    @Override
    public void setInitialState(String s) {
        S = new ArrayList();
        for (int i = 0; i < s.length(); i++) {
            S.add(new CompactedChar(s.charAt(i), i));
        }
        S.add(new CompactedChar(']', s.length()));
    }

    @Override
    public ModelI clone() {
        return new CompactedModel(this);
    }

    @Override
    public int caretUpdate(int caretView, int caretModel, int i) {
        return S.get(caretView + i).getPosition();
    }

    public int model2view(int pmodel) {
        int i = 0;
        while (S.get(i).getPosition() < pmodel) {
            i++;
        }
        return i;
    }

    public int getViewByRemoteCaret(RemoteCaret remoteCaret, int pmodel) {
        int n = remoteCaret.getView();
        int delta = pmodel - remoteCaret.getModel();
        if (delta >= 0) {
            while (S.get(n).getPosition() < pmodel) n++;
            return n;
        } else {
            while (S.get(n).getPosition() > pmodel && n > 0) n--;
            if (n > 0 && S.get(n).getPosition() < pmodel) n++;
            return n;
        }
    }

    public TSOperation executeLocal(TSOperation op, int pmodel) {
        if (op instanceof TSInsC) {
            TSInsC ins = (TSInsC) op;
            CompactedChar c = new CompactedChar(ins.getCharacter(), pmodel);
            S.add(ins.getPosition(), c);
            for (int i = ins.getPosition() + 1; i < S.size(); i++) S.get(i).incrementPosition();
            return new TSInsC(pmodel, ins);
        } else if (op instanceof TSDelC) {
            TSDelC del = (TSDelC) op;
            S.remove(del.getPosition());
            return new TSDelC(pmodel, del);
        } else if (op instanceof TSUndelC) {
            TSUndelC undel = (TSUndelC) op;
            return undel;
        }
        return null;
    }

    @Override
    public boolean exist(int position) {
        int pos = model2view(position);
        if (S.get(pos).getPosition() == position) return true;
        return false;
    }

    private void updateRemoteCarets(TSOperation op) {
        for (int rc : remotePmodel.keySet()) {
            remoteCaretUpdate(op, remotePmodel.get(rc));
        }
    }

    @Override
    public void getChars(int where, int length, Segment chars) throws BadLocationException {
        if (where + length > this.length()) {
            throw new BadLocationException("Invalid location", this.getLength());
        }
        char[] c = new char[S.size()];
        for (int i = where; i < (where + length) && i < S.size(); i++) c[i] = ((CompactedChar) S.get(i)).getCharacter();
        chars.array = c;
        chars.offset = where;
        chars.count = length;
    }

    @Override
    public char getChar(int offset) {
        return S.get(offset).getCharacter();
    }

    @Override
    public int view2model(int i) {
        return i;
    }

    @Override
    public int getVL(int pmodel) {
        return 0;
    }
}
