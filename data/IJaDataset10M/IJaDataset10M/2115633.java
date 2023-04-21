package observer;

/**
 * PatternBox: "ConcreteObserver" implementation.
 * <ul>
 *   <li>maintains a reference to a ConcreteSubject object.</li>
 *   <li>stores state that should stay consistent with the subject's. </li>
 *   <li>implements the Observer updating interface to keep its state consistent with the subject's.</li>
 * </ul>
 * 
 * @author <a href="mailto:dirk.ehms@patternbox.com">Dirk Ehms</a>
 * @author lx
 */
public class NetObserver implements Observer {

    /** stores the state of the ConcreteObserver */
    private State fObserverState;

    /** stores the associated ConcreteSubject */
    private final ConcreteSubject fConcreteSubject;

    /** 
	 * Constructor
	 */
    public NetObserver(ConcreteSubject subject) {
        super();
        fConcreteSubject = subject;
        fObserverState = fConcreteSubject.getState();
    }

    /** 
	 * This method updates the ConcreteObserver's state to be consistent
	 * with the ConcreteSubject's state.
	 */
    public void update() {
        fObserverState = fConcreteSubject.getState();
        System.out.println("�������緢���ش󲩿�" + fObserverState.getValue());
    }
}
