package it.unibo.deis.interaction.p2p.tucson.tuples;

import it.unibo.deis.interaction.p2p.utility.ObjectToStringUtility;
import alice.logictuple.InvalidTupleOperationException;
import alice.logictuple.InvalidVarNameException;
import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;

/**
 * Rappresenta una tupla del tipo <code>serviceAnswer(ServiceName, OpName, Id, Payload)</code><br/>
 * Viene utilizzata da un server per fornire il ritorno a una operazione su di un certo servizio, 
 * quando il protocollo utilizzato � TUPLESPACE.<br/>
 * <br/>
 * ServiceName: indica un nome significativo per il servizio (non necessariamente human-readable)<br/> 
 * OpName: indica l'operazione che si vuole venga eseguita dal servitore di ServiceName<br/> 
 * Id: identifica univocamente la chiamata all'interno del sottospazio relativo (L'Id della tupla serviceAnswer 
 * deve corrispondere all'Id della tupla serviceRequest associata)<br/>
 * Payload: una stringa rappresentante i parametri di invocazione<br/>
 * 
 * @author Gianfy
 */
public class ServiceAnswerTuple extends LogicTuple {

    /**
	 * @param serviceName Indica un nome significativo per il servizio (non necessariamente human-readable)
	 * @param id Identifica univcamente la chiamata all'interno del sottospazio relativo
	 * @param payload Il valore di ritorno, sotto forma di stringa
	 */
    public ServiceAnswerTuple(String serviceName, String id, String payload) {
        super("serviceAnswer", new Value(serviceName), new Value(id), new Value(payload));
    }

    /**
	 * @param serviceName Indica un nome significativo per il servizio (non necessariamente human-readable)
	 * @param opName Indica l'operazione che si vuole venga eseguita dal servitore di ServiceName
	 * @param id Identifica univcamente la chiamata all'interno del sottospazio relativo
	 * @param payload Il valore di ritorno, sotto forma di generico oggetto JAVA
	 */
    public ServiceAnswerTuple(String serviceName, String id, Object payload) {
        this(serviceName, id, ObjectToStringUtility.serialize(payload));
    }

    public ServiceAnswerTuple(String serviceName, String id) {
        super("serviceAnswer", new Value(serviceName), new Value(id), TupleUtility.newVar("Payload"));
    }

    public String getPayload() {
        try {
            return this.getArg(2).toString();
        } catch (InvalidTupleOperationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
