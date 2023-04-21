package mx.unam.ecologia.gye.model;

/**
 * A repeat is a sequence of bases implementing
 * {@link SequenceUnit}.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version @version@ (@date@)
 */
public class Repeat extends SequenceImpl implements SequenceUnit {

    private boolean m_Assembled = false;

    public Repeat(int unitsize) {
        super(unitsize);
        setTracingChanges(false);
    }

    public void setAssembled() {
        m_Assembled = true;
    }

    public void add(SequenceUnit u) {
        if (m_Assembled) {
            throw new UnsupportedOperationException();
        } else {
            super.add(u);
        }
    }

    public void add(int pos, SequenceUnit u) {
        if (m_Assembled) {
            throw new UnsupportedOperationException();
        } else {
            super.add(pos, u);
        }
    }

    public void replace(int pos, SequenceUnit b) {
        if (m_Assembled) {
            throw new UnsupportedOperationException();
        } else {
            super.replace(pos, b);
        }
    }

    public void remove(int pos) {
        if (m_Assembled) {
            throw new UnsupportedOperationException();
        } else {
            super.remove(pos);
        }
    }
}
