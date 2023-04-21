package ar.com.arkios.kfconsap.rfc;

import ar.com.arkios.kfconsap.excepciones.DataTypeInvalidConversionException;
import ar.com.arkios.kfconsap.excepciones.RFCNotFoundException;
import ar.com.arkios.kfconsap.excepciones.SapGeneralException;
import ar.com.arkios.kfconsap.excepciones.TableNotFoundException;
import ar.com.arkios.kfconsap.tablas.ContramarcaDAO;
import java.io.IOException;

/**
 * 
 * @author Ale
 * @version 1.01
 * Clase ContramarcaReadRFC
 * Remote Call Function Z_RFC_READ_ZCLCONTRAM   
 * 
 */
public class ContramarcaReadRFC extends GenericRFC {

    /**
     * Atributo myContramarcaDAO del tipo ContramarcaDAO
     */
    private ContramarcaDAO myContramarcaDAO;

    /**
     * Constructor que llama al constructor base, setea el nombre de la RFC y informa 
     * si tiene filtros.
     * @throws java.io.IOException
     * 
     */
    public ContramarcaReadRFC() throws SapGeneralException {
        super();
        super.setRfcName("Z_RFC_READ_ZCLCONTRAM");
        super.setTieneParametrosRFC(false);
        try {
            super.createFunction();
        } catch (RFCNotFoundException ex) {
            throw new SapGeneralException(ex.getMessage());
        }
    }

    /**
     * Retorna la ContramarcaDAO que debe crearse previamente.
     * @return ContramarcaDAO
     */
    public ContramarcaDAO getMyContramarcaDAO() {
        return myContramarcaDAO;
    }

    /**
     * Override del metodo loadTables de GenericRFC
     */
    @Override
    public void loadTables() throws DataTypeInvalidConversionException, TableNotFoundException {
        this.myContramarcaDAO = new ContramarcaDAO(super.getMyFunction());
    }
}
