package ar.com.arkios.kfconsap.tablas;

import ar.com.arkios.kfconmodelo.modelo.Variedad;
import ar.com.arkios.kfconsap.excepciones.DataTypeInvalidConversionException;
import ar.com.arkios.kfconsap.excepciones.SapGeneralException;
import ar.com.arkios.kfconsap.excepciones.TableNotFoundException;
import com.sap.mw.jco.JCO;
import java.util.ArrayList;
import java.util.List;

/**
* Columnas de Variedades
* ZESPECIE	ZZESPECIE	CHAR	4	0	Especie de frutas
* ZVARIEDAD	ZZVARIEDA	CHAR	4	0	Variedad de frutas
* ZDENOMINAC	ZDENOMIN	CHAR	30	0	Denominación
* ZTEXTO1       CHAR13  	CHAR	13	0	Descrip. Variedad 1
* ZTEXTO2	CHAR13  	CHAR	13	0	Descrip. Variedad
 */
public class VariedadDAO extends GenericSAPDAO {

    private List<Variedad> misVariedades;

    public VariedadDAO(JCO.Function aFunction) throws DataTypeInvalidConversionException, TableNotFoundException {
        super.setMyTableName("I_VARIEDAD");
        super.setMyTable(aFunction);
        this.misVariedades = new ArrayList();
    }

    public List<Variedad> getMisVariedades() {
        return misVariedades;
    }

    /**
     * Agrega una "variedad" a la coleccion "misVariedades".
     * @param laVariedad
     */
    private void addUnaVariedad(Variedad laVariedad) {
        this.misVariedades.add(laVariedad);
    }

    /**
     * Recorre "super.getMyTable(" de la RFC y por cada registro crea una "variedad" y lo agrega a "misVariedades".
     */
    @Override
    public void findAll() throws SapGeneralException {
        Variedad variedad;
        try {
            for (int i = 0; i < super.getMyTable().getNumRows(); i++) {
                super.getMyTable().setRow(i);
                variedad = new Variedad(super.getMyTable().getString("ZESPECIE"), super.getMyTable().getString("ZVARIEDAD"), super.getMyTable().getString("ZDENOMINAC"), super.getMyTable().getString("ZTEXTO1"), super.getMyTable().getString("ZTEXTO2"));
                addUnaVariedad(variedad);
            }
        } catch (JCO.ConversionException ex) {
            try {
                throw new DataTypeInvalidConversionException(ex.getMessage());
            } catch (DataTypeInvalidConversionException ex1) {
                throw new SapGeneralException(ex1.getMessage());
            }
        }
    }

    /**
     * Solo para las pruebas.
     * Imprime todos los registros de "super.getMyTable(".
     */
    @Override
    public void printAll() {
        System.out.println("ZESPECIE" + '\t' + "ZVARIEDAD" + '\t' + "ZDENOMINAC" + '\t' + "ZTEXTO1" + '\t' + "ZTEXTO2");
        for (int i = 0; i < super.getMyTable().getNumRows(); i++) {
            super.getMyTable().setRow(i);
            System.out.println(super.getMyTable().getString("ZESPECIE") + '\t' + super.getMyTable().getString("ZVARIEDAD") + '\t' + super.getMyTable().getString("ZDENOMINAC") + '\t' + super.getMyTable().getString("ZTEXTO1") + '\t' + super.getMyTable().getString("ZTEXTO2"));
        }
    }
}
