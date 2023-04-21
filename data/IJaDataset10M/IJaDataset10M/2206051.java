package com.common.to;

import java.util.ArrayList;
import java.util.List;

/**
 * clase que abtrae el comportamiento de un objeto de ejecuci�n
 * 
 * @author Fernando Abad
 * 
 */
public class ExecutionTO extends BaseTO {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7934891319037122225L;

    private List contents;

    public ExecutionTO() {
        this.contents = new ArrayList();
    }

    public ExecutionTO(List contents) {
        this.contents = contents;
    }

    public List getContents() {
        return contents;
    }

    public void setContents(List contents) {
        this.contents = contents;
    }
}
