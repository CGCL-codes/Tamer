package domino.logic;

import java.util.NoSuchElementException;
import java.lang.IllegalStateException;

/**
* <p>Iterator that never returns an element.
*/
public class EmptyFSNIterator implements FSNIterator {

    /** <p>Returns <code>false</code>.</p> */
    public boolean hasNext() {
        return false;
    }

    ;

    /** 
  * <p>Throws {@link NoSuchElementException}.</p>
  * @see #nextFSN()
  */
    public Object next() {
        throw new NoSuchElementException();
    }

    ;

    /** 
  * <p>Throws {@link NoSuchElementException}.</p>
  * @see #next()
  */
    public FormulaSyntaxNet nextFSN() {
        throw new NoSuchElementException();
    }

    ;

    /** <p>Returns <code>false</code>.</p>*/
    public boolean preorder() {
        return false;
    }

    ;

    /** <p>Returns <code>false</code>.</p>*/
    public boolean postorder() {
        return false;
    }

    ;

    /** <p>Returns <code>false</code>.</p>*/
    public boolean inorder() {
        return false;
    }

    ;

    /** 
  * <p>Throws {@link IllegalStateException} .</p>
  *
  * @throws IllegalStateException
  */
    public void remove() {
        throw new IllegalStateException();
    }

    ;
}

;
