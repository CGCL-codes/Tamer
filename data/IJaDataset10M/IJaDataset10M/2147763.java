package nz.ng.utilities.dbmodel;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * This class represent a relation between tables. It's available for use
 * but will work only on postgres DB.
 * @author juanjose.sanchez
 * last revision: 14/09/2010
 */
public class NzRelations {

    /** The name of the table referenced by the fk.*/
    private String idTablaRef = null;

    /** The name of the fk field.*/
    private String origen = null;

    /** The name of the field referenced by the fk.*/
    private String destino = null;

    /** Constraint name for the fk.*/
    private String idFk = null;

    /**
	 * @return the idTablaRef
	 */
    public String getIdTablaRef() {
        return idTablaRef;
    }

    /**
	 * @param idTablaRef the idTablaRef to set
	 */
    public void setIdTablaRef(String idTablaRef) {
        this.idTablaRef = idTablaRef;
    }

    /**
	 * @return the origen
	 */
    public String getOrigen() {
        return origen;
    }

    /**
	 * @param origen the origen to set
	 */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    /**
	 * @return the destino
	 */
    public String getDestino() {
        return destino;
    }

    /**
	 * @param destino the destino to set
	 */
    public void setDestino(String destino) {
        this.destino = destino;
    }

    /**
	 * @return the idFk
	 */
    public String getidFk() {
        return idFk;
    }

    /**
	 * @param idFk the idFk to set
	 */
    public void setIdFk(String idFk) {
        this.idFk = idFk;
    }

    /**
	 * Write all the xml tags related to relations and the corresponding data 
	 * for each one of them.
	 * @param stream the output file to be written
	 * @param spaceTabs the xml indentation tabs
	 * @throws IOException if the driver could not be found
	 */
    public final void writeToXML(OutputStreamWriter stream, String spaceTabs) throws IOException {
        stream.write(spaceTabs + spaceTabs + spaceTabs + spaceTabs + "<idFk>");
        stream.write((spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + idFk).trim());
        stream.write((spaceTabs + spaceTabs + spaceTabs + spaceTabs + "</idFk>\n").trim());
        stream.write("\n");
        stream.write(spaceTabs + spaceTabs + spaceTabs + spaceTabs + "<idTablaRef>".trim());
        stream.write((spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + idTablaRef).trim());
        stream.write((spaceTabs + spaceTabs + spaceTabs + spaceTabs + "</idTablaRef>\n").trim());
        stream.write("\n");
        stream.write(spaceTabs + spaceTabs + spaceTabs + spaceTabs + "<campos>\n");
        stream.write(spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + "<campo>\n");
        stream.write(spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + "<origen>");
        stream.write((spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + origen).trim());
        stream.write((spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + "</origen>\n").trim());
        stream.write("\n");
        stream.write(spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + "<destino>".trim());
        stream.write((spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + destino).trim());
        stream.write((spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + "</destino>\n").trim());
        stream.write("\n");
        stream.write(spaceTabs + spaceTabs + spaceTabs + spaceTabs + spaceTabs + "</campo>\n");
        stream.write(spaceTabs + spaceTabs + spaceTabs + spaceTabs + "</campos>\n");
    }
}
