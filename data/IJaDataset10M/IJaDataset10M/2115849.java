package it.unibo.deis.interaction.p2p.tucson.tuples;

import java.net.InetAddress;
import alice.logictuple.InvalidTupleOperationException;
import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;

/** 
 * Specifica una tupla del tipo tupleSpaceChecked(TupleSpaceName, Host, Port)<br/>
 * Viene utilizzata nel sistema per richiedere al server di naming il controllo di validit� 
 * delle informazioni riguardanti la locazione del server delle tuple master di TupleSpaceName<br/>
 * <br/>
 * TupleSpaceName: indica il nome attribuito nel nodo di Tucson a quello spazio delle tuple<br/>
 * Host: indica un nome logico di rete o l'indirizzo IP dove � in esecuzione il nodo Tucson<br/>
 * Port: la porta dove � possibile raggiungere il nodo Tucson<br/>
 * 
 * @author Gianfy
 */
public class SpaceLocationCheckedTuple extends LogicTuple {

    /**
	 * Costruttore per preparare una tupla di template
	 * 
	 * @param tupleSpaceName Indica il nome attribuito nel nodo di Tucson a quello spazio delle tuple
	 */
    public SpaceLocationCheckedTuple(String tupleSpaceName) {
        super("tupleSpaceChecked", new Value(tupleSpaceName), new Var(), new Var());
    }

    /**
	 * @return Il nome attribuito nel nodo di Tucson a quello spazio delle tuple
	 */
    public String getTupleSpaceName() {
        try {
            return this.getArg(0).toString();
        } catch (InvalidTupleOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * @return Un nome logico di rete o l'indirizzo IP dove � in esecuzione il nodo Tucson
	 */
    public String getHost() {
        try {
            return this.getArg(1).toString();
        } catch (InvalidTupleOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * @return La porta dove � possibile raggiungere il nodo Tucson
	 */
    public int getPort() {
        try {
            return this.getArg(1).intValue();
        } catch (InvalidTupleOperationException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
