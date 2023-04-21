package es.caib.bantel.modelInterfaz;

import java.io.Serializable;

/**
 * Datos del documento referentes a su presentaci�n presencial. El esquema de tramitaci�n permite que 
 * un documento deba ser presencialmente compulsado, que se entregue una fotocopia,etc.   
 */
public class DatosDocumentoPresencial implements Serializable {

    /**
	 * Identificador del documento. 
	 */
    private String identificador;

    /**
	 * N�mero de instancia del documento. 
	 */
    private int numeroInstancia = 1;

    /**
	 * Tipo de documento (Ver es.caib.xml.datospropios.factoria.ConstantesDatosPropiosXML)
	 */
    private char tipoDocumento;

    /**
     * Indica si el documento se ha compulsado con el original
     */
    private char compulsarDocumento;

    /**
     * Indica si se ha entregado una fotocopia
     */
    private char fotocopia;

    /**
     * Indica si ha sido firmado
     */
    private char firma;

    /**
     * Indica si el documento se ha compulsado con el original
     */
    public char getCompulsarDocumento() {
        return compulsarDocumento;
    }

    /**
     * Indica si el documento se ha compulsado con el original
     */
    public void setCompulsarDocumento(char compulsarDocumento) {
        this.compulsarDocumento = compulsarDocumento;
    }

    /**
     * Indica si ha sido firmado
     */
    public char getFirma() {
        return firma;
    }

    /**
     * Indica si ha sido firmado
     */
    public void setFirma(char firma) {
        this.firma = firma;
    }

    /**
     * Indica si se ha entregado una fotocopia
     */
    public char getFotocopia() {
        return fotocopia;
    }

    /**
     * Indica si se ha entregado una fotocopia
     */
    public void setFotocopia(char fotocopia) {
        this.fotocopia = fotocopia;
    }

    /**
	 * Tipo de documento (Ver es.caib.xml.datospropios.factoria.ConstantesDatosPropiosXML)
	 */
    public char getTipoDocumento() {
        return tipoDocumento;
    }

    /**
	 * Tipo de documento (Ver es.caib.xml.datospropios.factoria.ConstantesDatosPropiosXML)
	 */
    public void setTipoDocumento(char tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
	 * Identificador documento dentro del tr�mite
	 * @return
	 */
    public String getIdentificador() {
        return identificador;
    }

    /**
	 * Identificador documento dentro del tr�mite
	 * @param identificador
	 */
    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    /**
	 * Instancia documento dentro del tr�mite
	 * @return
	 */
    public int getNumeroInstancia() {
        return numeroInstancia;
    }

    /**
	 * Instancia documento dentro del tr�mite
	 */
    public void setNumeroInstancia(int numeroInstancia) {
        this.numeroInstancia = numeroInstancia;
    }
}
