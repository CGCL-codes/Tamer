package bookTrading.buyer;

import jade.core.Agent;

@SuppressWarnings("serial")
public class AgentBuyer extends Agent {

    @Override
    protected void setup() {
        System.out.println("[Agent] Comprador " + getAID().getName() + " est� pronto!");
        addBehaviour(new BuyerCallForPropose(this));
        addBehaviour(new BuyerPropose(this));
        addBehaviour(new BuyerAcceptProposal(this));
        addBehaviour(new BuyerInform(this));
    }
}
