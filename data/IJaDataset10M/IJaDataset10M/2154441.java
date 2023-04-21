package net.sf.campusip.domain.personas;

/**
 * 
 * @hibernate.class table="BECA" proxy = "net.sf.campusip.domain.personas.Beca"
 * @author vns
 *
 */
public class Beca {

    /**
     * 
     */
    public Beca() {
        super();
    }

    /**
 * <p>
 * Represents ...
 * </p>
 */
    private int id;

    /**
 * <p>
 * Represents ...
 * </p>
 */
    private String descripcion;

    /**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return The ID BECA
 * @hibernate.id column = "beca_PK" generator-class = "increment" unsaved-value = "0"
 */
    public int getId() {
        return id;
    }

    /**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @param _id 
 */
    public void setId(int _id) {
        id = _id;
    }

    /**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @return the aula description  
 * @hibernate.property column = "DESCRIPCION" not-null = "true" length = "80"
 */
    public String getDescripcion() {
        return descripcion;
    }

    /**
 * <p>
 * Does ...
 * </p>
 * 
 * 
 * @param _descripcion 
 */
    public void setDescripcion(String _descripcion) {
        descripcion = _descripcion;
    }
}
