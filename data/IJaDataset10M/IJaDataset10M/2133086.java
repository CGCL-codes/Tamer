package my.usm.cs.utmk.blexisma2.agents.Proximity.messages;

import my.usm.cs.utmk.blexisma2.kernel.messages.MessagesId;
import my.usm.cs.utmk.blexisma2.kernel.messages.RequestMessage;
import my.usm.cs.utmk.blexisma2.struct.ConceptualVector;

/**
 * A message to ask to Proximer agent the proxs of a conceptuel vector.
 * 
 * @author Schwab Didier
 * 
 */
public class GetProx extends RequestMessage {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ConceptualVector CV;

    public int nbProx;

    /**
	 * Method to get the closest items of a conceptual vector according to the
	 * angular distance Answer will be done by a Prox message.
	 * 
	 * @param CV
	 *            a conceptual vector of which the neighbors are wanted.
	 * @param nbProx
	 *            number of neighbors wanted.
	 * @see my.usm.cs.utmk.blexisma2.agents.Proximity.ProximityEvaluator#getProx(ConceptualVector,
	 *      int)
	 * @see Prox
	 */
    public GetProx(ConceptualVector CV, int nbProx) {
        id = MessagesId.ProximityGetProx;
        this.CV = CV;
        this.nbProx = nbProx;
    }
}
