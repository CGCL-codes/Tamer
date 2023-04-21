package org.mushroomdb.catalog;

/**
 * Representa una columna de una tabla.
 * 
 * @author Matias
 *
 */
public class Column implements Cloneable {

    private int type;

    /** java.sql.Types type + custom types */
    private int size;

    private String name;

    private Table table;

    private boolean pk;

    private boolean fk;

    private boolean unique;

    private boolean allowNulls;

    /**
	 * Contruye una columna usando un tipo java.sql.Types
	 */
    public Column(String name, int type) {
        this.name = name;
        this.type = type;
        this.size = 1;
    }

    /**
	 * Contruye una columna usando un tipo java.sql.Types y un tama�o
	 */
    public Column(String name, int type, int size) {
        this.name = name;
        this.type = type;
        this.size = size;
    }

    /**
	 * Setea la tabla de esta columna
	 * @param table
	 */
    protected void setTable(Table table) {
        this.table = table;
    }

    /**
	 * Devuelve la tabla a la que pertenece esta columna
	 * @return
	 */
    public Table getTable() {
        return this.table;
    }

    /**
	 * Devuelve un java.sql.Types
	 * @return
	 */
    public int getType() {
        return this.type;
    }

    /**
	 * Devuelve el tama�o del campo. Si el tipo no acepta tama�o, devuelve 1.
	 * @return Returns the size.
	 */
    public int getSize() {
        return this.size;
    }

    /**
	 * Devuelve el tama�o en bytes que ocupa la columna.
	 * @return
	 */
    public int getBytes() {
        return Types.getSize(this.type) * this.size;
    }

    /**
	 * Devuelve el nombre de la columna.
	 * @return
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * Verifica si esta columna es Foreign Key
	 * @return
	 */
    public boolean isFk() {
        return this.fk;
    }

    /**
	 * Verifica si esta columna es Primary Key
	 * @return
	 */
    public boolean isPk() {
        return this.pk;
    }

    /**
	 * Verifica si esta columna es Unique
	 * @return
	 */
    public boolean isUnique() {
        return this.unique;
    }

    /**
	 * Establece si esta columna es Foreign Key
	 * @param fk
	 */
    public void setFk(boolean fk) {
        this.fk = fk;
    }

    /**
	 * Establece si esta columna es Primary Key
	 * @param
	 */
    public void setPk(boolean pk) {
        this.pk = pk;
        if (pk) {
            this.setUnique(false);
            this.setAllowNulls(false);
        }
    }

    /**
	 * Establece si esta columna es Unique.
	 * @param unique
	 */
    public void setUnique(boolean unique) {
        this.unique = unique;
        if (unique) {
            this.setPk(false);
            this.setAllowNulls(false);
        }
    }

    /**
	 * Verifica si esta columna permite valores nulos.
	 * @return
	 */
    public boolean isAllowNulls() {
        return this.allowNulls;
    }

    /**
	 * Establece si esta columna acepta valores nulos o no.
	 * @param allowNulls FALSE implicar� establecer Pk y Unique en FALSE.
	 */
    public void setAllowNulls(boolean allowNulls) {
        this.allowNulls = allowNulls;
        if (allowNulls) {
            this.setPk(false);
            this.setUnique(false);
        }
    }

    /**
	 * toString
	 */
    public String toString() {
        String ret = this.name;
        if (this.getTable() != null) {
            ret = this.table.getName() + '.' + ret;
        }
        return ret;
    }

    public boolean equals(Object arg0) {
        return this.toString().equals(arg0.toString());
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public Column clone() {
        Column ret = new Column(this.getName(), this.getType(), this.getSize());
        ret.allowNulls = this.allowNulls;
        ret.fk = this.fk;
        ret.pk = this.pk;
        ret.table = this.table;
        ret.unique = this.unique;
        return ret;
    }
}
