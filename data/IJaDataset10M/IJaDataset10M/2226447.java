package serviceImplementations.evaluation;

import java.util.Vector;
import coordinator.wscoordination.CoordinationContext;

/**
 * The participant manager of the first concrete service.
 * The manager is responsible for monitoring the transactions
 * that a concurrently operating on this service, and to inform
 * dependent transactions if a dominant transaction fails.
 * 
 * This manager class is a singleton.
 * 
 * @author Michael Sch�fer
 *
 */
public class ParticipantManagerConcreteService1 implements ParticipantManager {

    private static final String IDENTIFIER_MANAGER = "http://sourceforge.net/projects/frogs/manager1/";

    private static ParticipantManager instance = new ParticipantManagerConcreteService1();

    private Vector<EvaluationParticipantService> participatingTransactions;

    /**
	 * Constructs a new participant manager object.
	 *
	 */
    private ParticipantManagerConcreteService1() {
        participatingTransactions = new Vector<EvaluationParticipantService>();
    }

    /**
	 * Returns the single existing instance of this participant manager.
	 * @return The single existing instance of this participant manager.
	 */
    public static ParticipantManager getInstance() {
        return instance;
    }

    /**
	 * Processes the required operation.
	 * @param operation The number of the operation.
	 * @param service The service object that will be added or has failed.
	 */
    public synchronized void process(int operation, EvaluationParticipantService service) {
        switch(operation) {
            case 1:
                {
                    this.participatingTransactions.add(service);
                    System.out.println("Added transaction: Client " + service.getClientIdentifier());
                    break;
                }
            case 2:
                {
                    String clientIdentifier = this.participatingTransactions.get(0).getClientIdentifier();
                    this.participatingTransactions.remove(0);
                    if (this.participatingTransactions.size() > 0) {
                        MayCompleteManager completeManager = new MayCompleteManager(this.participatingTransactions.get(0));
                        completeManager.start();
                    }
                    break;
                }
            case 3:
                {
                    int indexFailed = this.participatingTransactions.indexOf(service);
                    if (indexFailed == -1) {
                        break;
                    }
                    CoordinationContext context;
                    String identifier;
                    String address;
                    AbortTransactionManager manager;
                    for (int i = indexFailed + 1; i < this.participatingTransactions.size(); i++) {
                        context = this.participatingTransactions.get(i).getCoordinationContext();
                        identifier = IDENTIFIER_MANAGER + "id" + i;
                        address = context.getRegistrationService().getAddress().get_value().toString();
                        manager = new AbortTransactionManager(address, identifier, context);
                        manager.start();
                        System.out.println("FAIL: " + this.participatingTransactions.get(i).getClientIdentifier() + "(" + (i) + ")");
                    }
                    int end = this.participatingTransactions.size();
                    for (int i = (end - 1); i >= indexFailed; i--) {
                        this.participatingTransactions.remove(i);
                    }
                    break;
                }
        }
    }

    /**
	 * Adds the service instance, which handles one transaction.
	 * @param service The service instance.
	 */
    public void addTransaction(EvaluationParticipantService service) {
        this.process(1, service);
    }

    /**
	 * Removes the current dominant transaction.
	 */
    public void removeDominantTransaction() {
        this.process(2, null);
    }

    /**
	 * Sets the given service instance, which handles one transaction, as failed.
	 * @param service The service instance.
	 */
    public void failTransaction(EvaluationParticipantService service) {
        this.process(3, service);
    }

    /**
	 * Checks whether the given service instance, which handles one transaction, is 
	 * a dependent transaction.
	 * @param service The service instance.
	 * @return True, if the service handles a dependent transaction, otherwise false.
	 */
    public synchronized boolean isDependentTransaction(EvaluationParticipantService service) {
        return !this.isDominantTransaction(service);
    }

    /**
	 * Checks whether the given service instance, which handles one transaction, is 
	 * the dominant transaction.
	 * @param service The service instance.
	 * @return True, if the service handles the dominant transaction, otherwise false.
	 */
    public synchronized boolean isDominantTransaction(EvaluationParticipantService service) {
        if (this.participatingTransactions.indexOf(service) == 0) return true; else return false;
    }
}
