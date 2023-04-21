package westworld.states;

import westworld.InvalidStateException;
import westworld.Miner;
import westworld.messaging.Message;

public class EatStewState extends State<Miner> {

    private static EatStewState instance = null;

    private EatStewState() {
    }

    public static EatStewState getInstance() {
        if (instance == null) instance = new EatStewState();
        return instance;
    }

    @Override
    public void enter(Miner entity) throws InvalidStateException {
        System.out.println(entity.getName() + ": Smells Reaaal good, Elsa!");
    }

    @Override
    public void execute(Miner entity) throws InvalidStateException {
        System.out.println(entity.getName() + ": Tastes real good too!");
        entity.getStateMachine().revertToPreviousState();
    }

    @Override
    public void exit(Miner entity) throws InvalidStateException {
        System.out.println(entity.getName() + ": Thankya li'lle lady. Ah better get back to whatever" + " ah wuz doin'");
    }

    @Override
    public boolean onMessage(Miner entity, Message message) throws InvalidStateException {
        return false;
    }
}
