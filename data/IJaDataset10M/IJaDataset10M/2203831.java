package ai;

import java.util.LinkedList;

/**
 *
 * @author root
 */
public class SerialExp extends GramExp {

    public SerialExp() {
        mExpList = new LinkedList<LabelExp>();
        mSemExp = null;
    }

    public void addExp(LabelExp pExp) {
        mExpList.add(pExp);
    }

    public String toString() {
        String concat = "";
        for (LabelExp l : mExpList) {
            concat = concat + l.getName() + " ";
        }
        return concat;
    }

    public LinkedList<LabelExp> mExpList;

    public SemanticExp mSemExp;
}
