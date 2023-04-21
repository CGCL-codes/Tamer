package edu.rice.cs.drjava.model.definitions.reducedmodel;

/** A subclass of ReducedToken that represents sequences of non-special characters.
  * @version $Id: Gap.java 5175 2010-01-20 08:46:32Z mgricken $
  */
class Gap extends ReducedToken {

    private volatile int _size;

    /** Creates a new Gap.
    * @param size the size of the gap
    * @param state the state of the reduced model
    */
    Gap(int size, ReducedModelState state) {
        super(state);
        _size = size;
    }

    /** Gets the size of this gap.
    * @return _size
    */
    public int getSize() {
        return _size;
    }

    /** Gets the token type.
    * @return the empty string
    */
    public String getType() {
        return "";
    }

    /** Blows up.  The type of a Gap cannot be set.
    * @param type the type to set to
    * @throws RuntimeException always
    */
    public void setType(String type) {
        throw new RuntimeException("Can't set type on Gap!");
    }

    /** Blows up.  A Gap cannot be flipped.
    * @throws RuntimeException always
    */
    public void flip() {
        throw new RuntimeException("Can't flip a Gap!");
    }

    /** Increases the size of the gap.
    * @param delta the amount by which the gap is augmented.
    */
    public void grow(int delta) {
        if (delta >= 0) _size += delta;
    }

    /** Decreases the size of the gap.
    * @param delta the amount by which the gap is diminished.
    */
    public void shrink(int delta) {
        if (delta <= _size && delta >= 0) _size -= delta;
    }

    /** Converts a Brace to a String.  Used for debugging.
    * @return the String representation of the Gap
    */
    public String toString() {
        return "Gap<" + _size + ">";
    }

    /** Determines that this is not a multi-char brace.
    * @return <code>false</code>
    */
    public boolean isMultipleCharBrace() {
        return false;
    }

    /** Determines that this is a gap.
    * @return <code>true</code>
    */
    public boolean isGap() {
        return true;
    }

    /** Determines that this is not a line comment.
    * @return <code>false</code>
    */
    public boolean isLineComment() {
        return false;
    }

    /** Determines that this is not the start of a block comment.
    * @return <code>false</code>
    */
    public boolean isBlockCommentStart() {
        return false;
    }

    /** Determines that this is not the end of a block comment.
    * @return <code>false</code>
    */
    public boolean isBlockCommentEnd() {
        return false;
    }

    /** Determines that this is not a newline.
    * @return <code>false</code>
    */
    public boolean isNewline() {
        return false;
    }

    /** Determines that this is not a /.
    * @return <code>false</code>
    */
    public boolean isSlash() {
        return false;
    }

    /** Determines that this is not a *.
    * @return <code>false</code>
    */
    public boolean isStar() {
        return false;
    }

    /** Determines that this is not a ".
    * @return <code>false</code>
    */
    public boolean isDoubleQuote() {
        return false;
    }

    /** Determines that this is not a '.
    * @return <code>false</code>
    */
    public boolean isSingleQuote() {
        return false;
    }

    /** Determines that this is not a double escape sequence.
    * @return <code>false</code>
    */
    public boolean isDoubleEscapeSequence() {
        return false;
    }

    /** Determines that this is not a double escape.
    * @return <code>false</code>
    */
    public boolean isDoubleEscape() {
        return false;
    }

    /** Determines that this is not a \'.
    * @return <code>false</code>
    */
    public boolean isEscapedSingleQuote() {
        return false;
    }

    /** Determines that this is not a \".
    * @return <code>false</code>
    */
    public boolean isEscapedDoubleQuote() {
        return false;
    }

    /** Determines that this is not open.
    * @return <code>false</code>
    */
    public boolean isOpen() {
        return false;
    }

    /** Determines that this is not closed. */
    public boolean isClosed() {
        return false;
    }

    /** Determines that this is not a match.
    * @param other the token to compare to
    * @return <code>false</code>
    */
    public boolean isMatch(Brace other) {
        return false;
    }

    /** Determines that this ReducedToken is not matchable (one of "{", "}", "(", ")", "[", "]") */
    public boolean isMatchable() {
        return false;
    }

    /** Determines that this is not an open brace. */
    public boolean isOpenBrace() {
        return false;
    }

    /** Determines that this is not a closed brace. */
    public boolean isClosedBrace() {
        return false;
    }
}
