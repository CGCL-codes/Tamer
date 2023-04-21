package es.caib.xml.registro.factoria.impl;

import es.caib.xml.CampoObligatorioException;
import es.caib.xml.EstablecerPropiedadException;
import es.caib.xml.registro.factoria.ConstantesAsientoXML;

/** Objeto de DatosAnexoDocumentacion que encapsula el nodo XML DATOS_ANEXO_DOCUMENTACION de los documentos
 * XML de justificante. Para la implementaci�n se usa un objeto
 * generado mediante el API JAXB (Java Application XML Binding).
 * 
 * @author magroig
 *
 */
public class DatosAnexoDocumentacion extends NodoRegistroBase {

    private static final int MAX_LON_TIPO_DOCUMENTO = 2;

    private Boolean estructurado;

    private Character tipoDocumento;

    private String identificadorDocumento;

    private String codigoRDS;

    private String nombreDocumento;

    private String extractoDocumento;

    private String hashDocumento;

    private String codigoNormalizado;

    private Boolean firmaTerceros;

    public DatosAnexoDocumentacion() {
    }

    public Boolean isEstructurado() {
        return estructurado;
    }

    public void setEstucturado(Boolean estructurado) {
        this.estructurado = estructurado;
    }

    public Character getTipoDocumento() {
        return this.tipoDocumento;
    }

    public void setTipoDocumento(Character tipoDocumento) throws EstablecerPropiedadException {
        Character valoresPermitidos[] = { new Character(ConstantesAsientoXML.DATOSANEXO_OTROS), new Character(ConstantesAsientoXML.DATOSANEXO_DATOS_PROPIOS), new Character(ConstantesAsientoXML.DATOSANEXO_AVISO_NOTIFICACION), new Character(ConstantesAsientoXML.DATOSANEXO_OFICIO_REMISION), new Character(ConstantesAsientoXML.DATOSANEXO_EXTENSION_ASIENTO) };
        validaLongitudCampo("DatosAnexoDocumentacion", "TipoDocumento", "" + tipoDocumento, MAX_LON_TIPO_DOCUMENTO);
        validaCampoConListaValores("DatosAnexoDocumentacion", "TipoDocumento", tipoDocumento, valoresPermitidos);
        this.tipoDocumento = tipoDocumento;
    }

    public String getIdentificadorDocumento() {
        return this.identificadorDocumento;
    }

    public void setIdentificadorDocumento(String identificadorDocumento) throws EstablecerPropiedadException {
        validaLongitudCampo("DatosAnexoDocumentacion", "IdentificadorDocumento", identificadorDocumento, ConstantesAsientoXML.DATOSANEXO_IDENTIFICADORDOCUMENTO_MAX);
        validaCampoObligatorio("DatosAnexoDocumentacion", "IdentificadorDocumento", identificadorDocumento);
        this.identificadorDocumento = identificadorDocumento;
    }

    public String getNombreDocumento() {
        return this.nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) throws EstablecerPropiedadException {
        validaLongitudCampo("DatosAnexoDocumentacion", "NombreDocumento", nombreDocumento, ConstantesAsientoXML.DATOSANEXO_NOMBREDOCUMENTO_MAX);
        validaCampoObligatorio("DatosAnexoDocumentacion", "NombreDocumento", nombreDocumento);
        this.nombreDocumento = nombreDocumento;
    }

    public String getExtractoDocumento() {
        return this.extractoDocumento;
    }

    public void setExtractoDocumento(String extractoDocumento) throws EstablecerPropiedadException {
        validaLongitudCampo("DatosAnexoDocumentacion", "ExtractoDocumento", extractoDocumento, ConstantesAsientoXML.DATOSANEXO_EXTRACTODOCUMENTO_MAX);
        this.extractoDocumento = extractoDocumento;
    }

    public String getHashDocumento() {
        return this.hashDocumento;
    }

    public void setHashDocumento(String hashDocumento) throws EstablecerPropiedadException {
        validaLongitudCampo("DatosAnexoDocumentacion", "HashDocumento", hashDocumento, ConstantesAsientoXML.DATOSANEXO_HASHDOCUMENTO_MAX);
        this.hashDocumento = hashDocumento;
    }

    public String getCodigoNormalizado() {
        return this.codigoNormalizado;
    }

    public void setCodigoNormalizado(String codigoNormalizado) throws EstablecerPropiedadException {
        validaLongitudCampo("DatosAnexoDocumentacion", "CodigoNormalizado", codigoNormalizado, ConstantesAsientoXML.DATOSANEXO_CODIGONORMALIZADO_MAX);
        this.codigoNormalizado = codigoNormalizado;
    }

    public Boolean isFirmaTerceros() {
        return this.firmaTerceros;
    }

    public void setFirmaTerceros(Boolean firmaTerceros) {
        this.firmaTerceros = firmaTerceros;
    }

    public String getCodigoRDS() {
        return this.codigoRDS;
    }

    public void setCodigoRDS(String codigoRDS) throws EstablecerPropiedadException {
        this.codigoRDS = codigoRDS;
    }

    public void comprobarDatosRequeridos() throws EstablecerPropiedadException {
        String estructuradoRaw = null;
        String tipoDocumentoRaw = null;
        String identificadorDocumento = null;
        String nombreDocumento = null;
        if (isEstructurado() == null) throw new CampoObligatorioException("DatosAnexoDocumentacion", "Estructurado");
        if (getTipoDocumento() == null) throw new CampoObligatorioException("DatosAnexoDocumentacion", "TipoDocumento");
        identificadorDocumento = getIdentificadorDocumento();
        if ((identificadorDocumento == null) || (identificadorDocumento.trim().equals(""))) throw new CampoObligatorioException("DatosAnexoDocumentacion", "IdentificadorDocumento");
        nombreDocumento = getNombreDocumento();
        if ((nombreDocumento == null) || (nombreDocumento.trim().equals(""))) throw new CampoObligatorioException("DatosAnexoDocumentacion", "NombreDocumento");
        if ((getCodigoRDS() == null) || (getCodigoRDS().trim().equals(""))) throw new CampoObligatorioException("DatosAnexoDocumentacion", "CodigoRDS");
    }

    public boolean equals(Object obj) {
        if (obj instanceof DatosAnexoDocumentacion) {
            if (obj == null) return false;
            DatosAnexoDocumentacion anexo = (DatosAnexoDocumentacion) obj;
            if (isEstructurado() != anexo.isEstructurado()) return false;
            if (getTipoDocumento() != anexo.getTipoDocumento()) return false;
            String idDocumento = getIdentificadorDocumento();
            String idDocumentoExt = anexo.getIdentificadorDocumento();
            if ((idDocumento != null) || (idDocumentoExt != null)) if ((idDocumentoExt != null) && (idDocumento != null)) {
                if (!idDocumento.equals(idDocumentoExt)) return false;
            } else if ((idDocumento != null) || (idDocumentoExt != null)) return false;
            String nombreDocumento = getNombreDocumento();
            String nombreDocumentoExt = anexo.getNombreDocumento();
            if ((nombreDocumento != null) || (nombreDocumentoExt != null)) if ((nombreDocumentoExt != null) && (nombreDocumento != null)) {
                if (!nombreDocumento.equals(nombreDocumentoExt)) return false;
            } else if ((nombreDocumento != null) || (nombreDocumentoExt != null)) return false;
            String extracto = getExtractoDocumento();
            String extractoExt = anexo.getExtractoDocumento();
            if ((extracto != null) || (extractoExt != null)) if ((extractoExt != null) && (extracto != null)) {
                if (!extracto.equals(extractoExt)) return false;
            } else if ((extracto != null) || (extractoExt != null)) return false;
            String hash = getHashDocumento();
            String hashExt = anexo.getHashDocumento();
            if ((hash != null) || (hashExt != null)) if ((hashExt != null) && (hash != null)) {
                if (!hash.equals(hashExt)) return false;
            } else if ((hash != null) || (hashExt != null)) return false;
            String codigoNorm = getCodigoNormalizado();
            String codigoNormExt = anexo.getCodigoNormalizado();
            if ((codigoNorm != null) || (codigoNormExt != null)) if ((codigoNormExt != null) && (codigoNorm != null)) {
                if (!codigoNorm.equals(codigoNormExt)) return false;
            } else if ((codigoNorm != null) || (codigoNormExt != null)) return false;
            if (!this.objetosIguales(isFirmaTerceros(), anexo.isFirmaTerceros())) return false;
            if (!objetosIguales(getCodigoRDS(), anexo.getCodigoRDS())) return false;
            return true;
        }
        return super.equals(obj);
    }
}
