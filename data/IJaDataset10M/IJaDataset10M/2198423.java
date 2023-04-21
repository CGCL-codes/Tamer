package orm;

import java.io.Serializable;

public class Tda_alumno implements Serializable {

    public Tda_alumno() {
    }

    public boolean equals(Object aObj) {
        if (aObj == this) return true;
        if (!(aObj instanceof Tda_alumno)) return false;
        Tda_alumno tda_alumno = (Tda_alumno) aObj;
        if ((getAlum_rut() != null && !getAlum_rut().equals(tda_alumno.getAlum_rut())) || (getAlum_rut() == null && tda_alumno.getAlum_rut() != null)) return false;
        if (getAlum_id() != tda_alumno.getAlum_id()) return false;
        return true;
    }

    public int hashCode() {
        int hashcode = 0;
        hashcode = hashcode + (getAlum_rut() == null ? 0 : getAlum_rut().hashCode());
        hashcode = hashcode + (int) getAlum_id();
        return hashcode;
    }

    private java.util.Set this_getSet(int key) {
        if (key == orm.ORMConstants.KEY_TDA_ALUMNO_TDA_ANOTACION) {
            return ORM_tda_anotacion;
        }
        return null;
    }

    private void this_setOwner(Object owner, int key) {
        if (key == orm.ORMConstants.KEY_TDA_ALUMNO_TDA_CURSOCURSO) {
            this.tda_cursocurso = (orm.Tda_curso) owner;
        }
    }

    org.orm.util.ORMAdapter _ormAdapter = new org.orm.util.AbstractORMAdapter() {

        public java.util.Set getSet(int key) {
            return this_getSet(key);
        }

        public void setOwner(Object owner, int key) {
            this_setOwner(owner, key);
        }
    };

    private String alum_rut;

    private int alum_id;

    private String alum_nombre;

    private String alum_descripcion;

    private orm.Tda_curso tda_cursocurso;

    private java.util.Set ORM_tda_anotacion = new java.util.HashSet();

    public void setAlum_rut(String value) {
        this.alum_rut = value;
    }

    public String getAlum_rut() {
        return alum_rut;
    }

    public void setAlum_id(int value) {
        this.alum_id = value;
    }

    public int getAlum_id() {
        return alum_id;
    }

    public void setAlum_nombre(String value) {
        this.alum_nombre = value;
    }

    public String getAlum_nombre() {
        return alum_nombre;
    }

    public void setAlum_descripcion(String value) {
        this.alum_descripcion = value;
    }

    public String getAlum_descripcion() {
        return alum_descripcion;
    }

    public void setTda_cursocurso(orm.Tda_curso value) {
        if (tda_cursocurso != null) {
            tda_cursocurso.tda_alumno.remove(this);
        }
        if (value != null) {
            value.tda_alumno.add(this);
        }
    }

    public orm.Tda_curso getTda_cursocurso() {
        return tda_cursocurso;
    }

    /**
	 * This method is for internal use only.
	 */
    public void setORM_Tda_cursocurso(orm.Tda_curso value) {
        this.tda_cursocurso = value;
    }

    private orm.Tda_curso getORM_Tda_cursocurso() {
        return tda_cursocurso;
    }

    private void setORM_Tda_anotacion(java.util.Set value) {
        this.ORM_tda_anotacion = value;
    }

    private java.util.Set getORM_Tda_anotacion() {
        return ORM_tda_anotacion;
    }

    public final orm.Tda_anotacionSetCollection tda_anotacion = new orm.Tda_anotacionSetCollection(this, _ormAdapter, orm.ORMConstants.KEY_TDA_ALUMNO_TDA_ANOTACION, orm.ORMConstants.KEY_TDA_ANOTACION_TDA_ALUMNOALUM_RUT, orm.ORMConstants.KEY_MUL_ONE_TO_MANY);

    public String toString() {
        return String.valueOf(getAlum_rut() + " " + getAlum_id());
    }
}
