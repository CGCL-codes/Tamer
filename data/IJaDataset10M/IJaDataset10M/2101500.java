package ar.com.arkios.kfconsap.rfc;

import ar.com.arkios.kfconsap.excepciones.DataTypeInvalidConversionException;
import ar.com.arkios.kfconsap.excepciones.RFCNotFoundException;
import ar.com.arkios.kfconsap.excepciones.SapGeneralException;
import ar.com.arkios.kfconsap.excepciones.TableNotFoundException;
import ar.com.arkios.kfconsap.tablas.LoteDAO;
import java.io.IOException;

/**
 *
 * @author Ale
 * @version 1.01
 * Clase LotesCentrosReadRCF
 * Remote Call Function Z_RFC_LOTES_CENTROS
 * 
 */
public class LotesCentrosReadRCF extends GenericRFC {

    /**
     * Atributo myLoteDAO del tipo LoteDAO
     * Atributo myRFCFieldParameter Campo de esta RFC para Filtrar busqueda
     * Atributo myRFCValueParameter Valor del Campo a filtrar.
     */
    private LoteDAO myLoteDAO;

    private static String myRFCFieldParameter = "WERKS";

    private static String myRFCValueParameter = "UEAB";

    /**
     * Constructor que llama al constructor base, setea el nombre de la RFC
     * y le indica si usa parametros de filtro
     * @throws java.io.IOException
     * 
     */
    public LotesCentrosReadRCF() throws SapGeneralException {
        super();
        super.setRfcName("Z_RFC_LOTES_CENTROS");
        super.setTieneParametrosRFC(true);
        super.setFieldParam(myRFCFieldParameter);
        super.setValueParam(myRFCValueParameter);
        try {
            super.createFunction();
        } catch (RFCNotFoundException ex) {
            throw new SapGeneralException(ex.getMessage());
        }
    }

    /**
     * Override del metodo loadTables de GenericRFC    
     * 
     */
    @Override
    public void loadTables() throws DataTypeInvalidConversionException, TableNotFoundException {
        this.myLoteDAO = new LoteDAO(super.getMyFunction());
    }

    /**
     * Decuelve el atributo myLoteDAO tipo LoteDAO
     * @return myLoteDAO
     */
    public LoteDAO getMyLoteDAO() {
        return myLoteDAO;
    }
}
