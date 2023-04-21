package cl.hhha.web.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Óscar Álvarez V
 * @date 20-07-2009 - 9:14:42
 * @version 1.0
 */
@Entity
@Table(name = "webh_encuesta_opcion")
public class Respuesta extends AuditableBaseObject implements Serializable, Auditable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5995073923724735994L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Encuesta encuesta;

    @ManyToOne
    private Opcion opcionSeleccionada;

    /**
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return the encuesta
	 */
    public Encuesta getEncuesta() {
        return encuesta;
    }

    /**
	 * @param encuesta the encuesta to set
	 */
    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    /**
	 * @return the opcionSeleccionada
	 */
    public Opcion getOpcionSeleccionada() {
        return opcionSeleccionada;
    }

    /**
	 * @param opcionSeleccionada the opcionSeleccionada to set
	 */
    public void setOpcionSeleccionada(Opcion opcionSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }
}
