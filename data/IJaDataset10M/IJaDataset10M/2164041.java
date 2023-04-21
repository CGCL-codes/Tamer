package net.sourceforge.freejava.sio.indent;

public interface IIndention {

    int getIndentLevel();

    /**
     * @throws IllegalArgumentException
     *             If <code>indentLevel</code> is negative.
     */
    void setIndentLevel(int indentLevel);

    /**
     * @return indent-level after increased.
     */
    int increaseIndentLevel();

    /**
     * @throws IllegalStateException
     *             If can't unindent any more.
     * @return indent-level after decreased.
     */
    int decreaseIndentLevel();
}
