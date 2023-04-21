package observable;

import java.util.Observable;

/**
 * This class simply change the visibility of
 * java.util.Observable#clearChanged() and java.util.Observable#setChanged().
 * When a class Foo cannot extend Observable, it also cannot use it as a delegate
 * object because the java.util.Observable#clearChanged() and
 * java.util.Observable#setChanged() are protected.<br />
 * So, class Foo can use a DelegateObservable instead but should not give the
 * access to java.util.Observable#clearChanged() and
 * java.util.Observable#setChanged(). This way,
 * java.util.Observable#clearChanged() and java.util.Observable#setChanged() can
 * be call only by the class that hold the DelegateObservable and by no other
 * class as if a classical extend will be used.
 * 
 * @author Desprez Jean-Marc
 * 
 */
public class DelegateObservable extends Observable implements IObervable {

    /**
   * @see java.util.Observable#Observable()
   */
    public DelegateObservable() {
        super();
    }

    @Override
    public synchronized void clearChanged() {
        super.clearChanged();
    }

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }
}
