package ch.olsen.routes.atom;

import java.io.Serializable;
import ch.olsen.routes.data.DataElement;

/**
 * This class represents data going from one atom to the next. Because 
 * we want to resume execution, data flows are controlled and a stack of
 * these elements will represent the stack of execution at any given time 
 * @author vito
 *
 */
public interface RoutesStep extends Serializable {

    /**
	 * informations about next link and data to send
	 * @return
	 */
    LinkInput getLink();

    DataElement getData();

    /**
	 * time determines the execution order. Elements with the same time
	 * are executed in parallel, otherwise sequentially
	 * @return
	 */
    int getTime();
}
