package alp;

/**
 * @author niki.waibel{@}gmx.net
 */
public class SessionEvent extends java.util.EventObject {

    enum Type {

        stateChanged, security, exception
    }

    ;

    Type type;

    SessionEvent(Type type) {
        super(new Object());
        this.type = type;
    }
}
