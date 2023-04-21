package ar.com.tifad.domainmodel.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import ar.com.tifad.domainmodel.entities.contracts.IProgramacionDiariaElectrica;
import ar.com.tifad.domainmodel.entities.contracts.IProgramacionDiariaElectricaItem;

/**
 * Es una previsi�n entragada por el �rea de programaci�n diaria (en el dominio el�ctrico), hecha para el 
 * d�a siguiente el�ctrico (que va de 00:00hs a 23:59hs).
 * Contiene un conjunto de items que indican si una turbina (sin importar si es una TG o un ciclo combinado)
 * fue despachada o no, y qu� cantidad de energ�a entregar� al sistema. Asi mismo, como la cantidad de gas
 * que se espera se le entregue.
 * 
 * It's a prevision made by Diary Programming Area in the Electrical Domain, made for the next Electrical Day.
 * It contains a set of items that indicates wheteer a GasTurbine (no matter if it is single or combined cycle)
 * was dispatched or not, and which amount of Power will it supply the next electrical day.
 * @author Ricardo
 *
 */
public class ProgramacionDiariaElectrica implements IProgramacionDiariaElectrica, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4216652600587387020L;

    private int id;

    private Date date;

    private String comments;

    private Set<IProgramacionDiariaElectricaItem> items;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Date getDiaElectrico() {
        return date;
    }

    @Override
    public void setDiaElectrico(Date date) {
        this.date = date;
    }

    @Override
    public String getComentario() {
        return comments;
    }

    @Override
    public void setComentario(String comments) {
        this.comments = comments;
    }

    @Override
    public Set<IProgramacionDiariaElectricaItem> getItems() {
        return items;
    }

    @Override
    public void setItems(Set<IProgramacionDiariaElectricaItem> items) {
        this.items = items;
    }

    public ProgramacionDiariaElectrica() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ProgramacionDiariaElectrica other = (ProgramacionDiariaElectrica) obj;
        if (id != other.id) return false;
        return true;
    }

    public ProgramacionDiariaElectrica(int id, Date date, String comments, Set<IProgramacionDiariaElectricaItem> items) {
        super();
        this.id = id;
        this.date = date;
        this.comments = comments;
        this.items = items;
    }
}
