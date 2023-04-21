package net.mym.bcnmetro.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransFerDto extends ParadaDto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private List<Integer> transbordo = new ArrayList<Integer>();

    public List<Integer> getTransbordo() {
        return transbordo;
    }

    public void setTransbordo(List<Integer> transbordo) {
        this.transbordo = transbordo;
    }
}
