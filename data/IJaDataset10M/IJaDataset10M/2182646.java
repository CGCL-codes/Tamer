package com.siasal.documentos.business;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import com.common.exception.ExceptionUtility;
import com.common.exception.InternalApplicationException;
import com.siasal.documentos.commons.DocumentoTO;
import com.siasal.documentos.commons.LibroTO;
import com.siasal.documentos.commons.RevistaTO;

/**
 * @hibernate.joined-subclass table="doc_revista"
 * @hibernate.joined-subclass-key column="rev_id"
 */
public class Revista extends DocumentoBasico {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5663426227150164832L;

    private static final String PARAM_PUBLICACION_PERIODICA = "publicacionPeriodica";

    /** @modelguid {930D2BAC-E171-4223-8A4D-EF9F9A8F23AA} */
    private PublicacionPeriodica publicacionPeriodica;

    /**
	 * @hibernate.many-to-one column="pub_per_id"
	 *                        class="com.siasal.documentos.business.PublicacionPeriodica"
	 */
    public PublicacionPeriodica getPublicacionPeriodica() {
        return publicacionPeriodica;
    }

    public void setPublicacionPeriodica(PublicacionPeriodica publicacionPeriodica) {
        this.publicacionPeriodica = publicacionPeriodica;
    }

    public Revista() {
        super();
    }

    public Map getTextFields() {
        Map textFields = super.getTextFields();
        textFields.put(PARAM_PUBLICACION_PERIODICA, getPublicacionPeriodica().getNombre());
        return textFields;
    }

    public Map getUnStoredFields() {
        Map unStoredFields = super.getUnStoredFields();
        unStoredFields.put(PARAM_CONTENIDO, getPublicacionPeriodica().getNombre() + " " + (String) unStoredFields.get(PARAM_CONTENIDO));
        return unStoredFields;
    }

    public Revista(String codigo, Set ejemplares, int estado, Integer id, Set autores, Date fecha, Idioma idioma, Tipo tipo, String titulo, PublicacionPeriodica periodica) {
        super(codigo, ejemplares, estado, id, autores, fecha, idioma, titulo);
        publicacionPeriodica = periodica;
    }

    public Revista(RevistaTO revistaTO) {
        construir(revistaTO.getCodigo(), revistaTO.getEjemplares(), revistaTO.getEstado(), revistaTO.getId(), revistaTO.getAutores(), revistaTO.getFecha(), revistaTO.getIdioma(), revistaTO.getTitulo(), revistaTO.getContenido());
    }

    public Revista(DocumentoTO documentoTO) {
        if (documentoTO == null) {
            throw new IllegalArgumentException("No se puede crear revista ==>" + null);
        }
        try {
            RevistaTO revistaTO = (RevistaTO) documentoTO;
            construir(revistaTO.getCodigo(), revistaTO.getEjemplares(), revistaTO.getEstado(), revistaTO.getId(), revistaTO.getAutores(), revistaTO.getFecha(), revistaTO.getIdioma(), revistaTO.getTitulo(), revistaTO.getContenido());
        } catch (ClassCastException e) {
            throw new InternalApplicationException("El value object no es del tipo RevistaTO ==>" + documentoTO);
        }
    }

    public DocumentoTO getTO() {
        return null;
    }

    public DocumentoTO getTOPresentacion() {
        RevistaTO revistaTO = new RevistaTO();
        llenarPresentacion(revistaTO);
        return revistaTO;
    }

    protected void llenarPresentacion(RevistaTO revistaTO) {
        ExceptionUtility.exceptForNulls(revistaTO, "Libro a llenar");
        super.llenarPresentacion(revistaTO);
        revistaTO.setPublicacionPeriodica(getPublicacionPeriodica().getPublicacionPeriodicaTO());
    }

    public void inicializarTipoDocumento() {
        setCodigoTipo("03");
    }

    public String getTipoLabel() {
        return "Revista";
    }
}
