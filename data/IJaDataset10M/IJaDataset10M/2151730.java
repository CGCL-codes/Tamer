package edu.univalle.lingweb.agent.model;

import jade.content.Concept;

/**
 * Clase que permite encapsular los datos b�sicos de un anuncio del sistema
 * @author Julio Cesar Puentes
 */
public class Announcement implements java.io.Serializable, Concept {

    private static final long serialVersionUID = 1L;

    private Long announcementId;

    private String title;

    private String content;

    private String creationDate;

    private Long type;

    private String typeDescription;

    public Announcement() {
        super();
    }

    /**
	 * @return Long c�digo del anuncio
	 */
    public Long getAnnouncementId() {
        return this.announcementId;
    }

    /**
	 * @return String titulo del anuncio
	 */
    public String getTitle() {
        return this.title;
    }

    /**
	 * @return String contenido del anuncio
	 */
    public String getContent() {
        return this.content;
    }

    /**
	 * @return String fecha de creaci�n
	 */
    public String getCreationDate() {
        return this.creationDate;
    }

    /**
	 * @return Long tipo de anuncio
	 */
    public Long getType() {
        return this.type;
    }

    /**
	 * @return String descripci�n del tipo de anuncio
	 */
    public String getTypeDescription() {
        return this.typeDescription;
    }

    /**
	 * @param announcementId c�digo del anuncio
	 */
    public void setAnnouncementId(Long announcementId) {
        this.announcementId = announcementId;
    }

    /**
	 * @param title titulo del anuncio 
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * @param content contenido del anuncio 
	 */
    public void setContent(String content) {
        this.content = content;
    }

    /**
	 * @param creationDate fecha de creaci�n
	 */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
	 * @param tipo de anuncio
	 */
    public void setType(Long type) {
        this.type = type;
    }

    /**
	 * @param descripcion del tipo de anuncio
	 */
    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }
}
