package gnu.javax.swing.text.html.parser.support.low;

import java.util.Arrays;

/**
 * A token queue.
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public class Queue {

    Token[] m = new Token[64];

    int a = 0;

    int b = 0;

    /**
   * True for the empty queue.
   */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
   *  Add this trace to the end of the queue.
   */
    public void add(Token u) {
        if (a < m.length) {
            m[a] = u;
            a++;
        } else {
            if (b > 0) {
                int d = b;
                System.arraycopy(m, b, m, 0, a - b);
                b = b - d;
                a = a - d;
                m[a] = u;
                a++;
            } else {
                int n = m.length * 2;
                Token[] nm = new Token[2 * n];
                System.arraycopy(m, 0, nm, 0, m.length);
                Arrays.fill(m, null);
                nm[a] = u;
                m = nm;
                a++;
            }
        }
    }

    /**
   * Clear the queue.
   */
    public void clear() {
        a = b = 0;
        Arrays.fill(m, null);
    }

    /**
   * Read the value ahead. 0 is the value that will be returned with
   * the following next. This method does not remove values from the
   * queue. To test if there is enough tokens in the queue, size() must
   * be checked before calling this method.
   */
    public Token get(int ahead) {
        int p = b + ahead;
        if (p < a) return m[p]; else throw new ArrayIndexOutOfBoundsException("Not enough tokens");
    }

    /**
   * Read the oldest value from the queue and remove this value from
   * the queue.
   */
    public Token next() {
        if (a == b) throw new ArrayIndexOutOfBoundsException("queue empty");
        Token r = m[b];
        m[b] = null;
        b++;
        return r;
    }

    /**
   * Size of the queue.
   */
    public int size() {
        return a - b;
    }
}
