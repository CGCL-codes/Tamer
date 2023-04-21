package br.com.aulaweb.persistence;

import org.apache.ojb.broker.PersistenceBrokerFactory;

/**
 * implements Use Case "Quit Application".
 * @author: thma
 */
public class UCQuitApplication extends AbstractUseCase {

    /**
     * UCQuitApplication constructor comment.
     */
    public UCQuitApplication(org.apache.ojb.broker.PersistenceBroker b) {
        super(b);
    }

    /**
     * apply method comment.
     */
    public void apply() {
        broker.close();
        System.out.println("bye...");
        System.exit(0);
    }

    /**
     * getDescription method comment.
     */
    public String getDescription() {
        return "Quit Application";
    }
}
