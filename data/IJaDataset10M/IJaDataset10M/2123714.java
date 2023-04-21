package mf.torneo.model;

import java.io.Serializable;
import java.util.List;
import com.opensymphony.xwork.util.XWorkList;

/**
 * @author Mauro Fugante
 *
 */
public class IscrizioniList implements Serializable {

    private static final long serialVersionUID = -2830158841202117679L;

    private List items = new XWorkList(Iscrizioni.class);

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}
