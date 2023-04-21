package model;

import java.util.Date;

public class Notificacion implements Cloneable {

    public String remitente;

    public String asunto;

    public String descripcion;

    public Date fechaEmision;

    /**
	 * Utiliza el setter de asunto, para modificar su contenido.
	 * @param asunto
	 */
    public void editarAsunto(String asunto) {
        this.setAsunto(asunto);
    }

    /**
	 * Utiliza el setter de descripci�n, para modificar su contenido.
	 * @param descripcion
	 */
    public void editarDescripcion(String descripcion) {
        this.setDescripcion(descripcion);
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    /**
	 * Define un nuevo constructor que retorna una notificaci�n con contenido pre-definido
	 * @return
	 * @param horario
	 * @param remitente
	 */
    public Notificacion(Horario horario, String remitente) {
        this.setRemitente(remitente);
        this.setAsunto("Nuevo horario");
        this.setFechaEmision(new Date());
        this.setDescripcion(String.format("Se agreg� en el d�a de la fecha (%s) el horario: %s a la c�tedra %s", this.getFechaEmision().toString(), horario.toString(), remitente));
    }

    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }

    public boolean equals(Notificacion notificacion) {
        return (this.asunto == notificacion.asunto && this.descripcion == notificacion.descripcion && this.fechaEmision == notificacion.fechaEmision && this.remitente == notificacion.remitente);
    }
}
