package jpianotrain.staff;

import java.util.List;
import java.util.Stack;

/**
 * A tune collects two sets of notes for both
 * hands, refers to a scale and maybe some
 * length information.
 *
 * @since 0
 * @author Alexander Methke
 */
public class Tune {

    public Tune() {
    }

    public List<Note> getLeftHand() {
        return leftHand;
    }

    public List<Note> getRightHand() {
        return rightHand;
    }

    public void setLeftHand(List<Note> ns) {
        leftHand = ns;
    }

    public void setRightHand(List<Note> ns) {
        rightHand = ns;
    }

    public Stack<Note> getLeftHandSequence() {
        Stack<Note> st = new Stack<Note>();
        for (int i = leftHand.size() - 1; i > -1; i--) {
            st.push(leftHand.get(i));
        }
        return st;
    }

    public Stack<Note> getRightHandSequence() {
        Stack<Note> st = new Stack<Note>();
        for (int i = rightHand.size() - 1; i > -1; i--) {
            st.push(rightHand.get(i));
        }
        return st;
    }

    private List<Note> leftHand;

    private List<Note> rightHand;
}
