package orm;

import java.io.Serializable;

public class Tcc_alumno implements Serializable {

    public Tcc_alumno() {
    }

    private java.util.Set this_getSet(int key) {
        if (key == orm.ORMConstants.KEY_TCC_ALUMNO_TCC_ANOTACION) {
            return ORM_tcc_anotacion;
        } else if (key == orm.ORMConstants.KEY_TCC_ALUMNO_TCC_NOTA) {
            return ORM_tcc_nota;
        } else if (key == orm.ORMConstants.KEY_TCC_ALUMNO_TCC_ASISTENCIA) {
            return ORM_tcc_asistencia;
        } else if (key == orm.ORMConstants.KEY_TCC_ALUMNO_TCC_ASISTENCIAEVENTO) {
            return ORM_tcc_asistenciaevento;
        }
        return null;
    }

    private void this_setOwner(Object owner, int key) {
        if (key == orm.ORMConstants.KEY_TCC_ALUMNO_TCC_CURSOCU) {
            this.tcc_cursocu = (orm.Tcc_curso) owner;
        } else if (key == orm.ORMConstants.KEY_TCC_ALUMNO_TCC_APODERADOAP) {
            this.tcc_apoderadoap = (orm.Tcc_apoderado) owner;
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

    private int al_id;

    private String al_rut;

    private String al_nombre;

    private int al_asistencia;

    private orm.Tcc_curso tcc_cursocu;

    private orm.Tcc_apoderado tcc_apoderadoap;

    private java.util.Set ORM_tcc_anotacion = new java.util.HashSet();

    private java.util.Set ORM_tcc_nota = new java.util.HashSet();

    private java.util.Set ORM_tcc_asistencia = new java.util.HashSet();

    private java.util.Set ORM_tcc_asistenciaevento = new java.util.HashSet();

    private void setAl_id(int value) {
        this.al_id = value;
    }

    public int getAl_id() {
        return al_id;
    }

    public int getORMID() {
        return getAl_id();
    }

    /**
	 * Rut del alumno
	 */
    public void setAl_rut(String value) {
        this.al_rut = value;
    }

    /**
	 * Rut del alumno
	 */
    public String getAl_rut() {
        return al_rut;
    }

    /**
	 * Nombre del alumno
	 */
    public void setAl_nombre(String value) {
        this.al_nombre = value;
    }

    /**
	 * Nombre del alumno
	 */
    public String getAl_nombre() {
        return al_nombre;
    }

    /**
	 * Porcentaje de asistencia del alumno
	 */
    public void setAl_asistencia(int value) {
        this.al_asistencia = value;
    }

    /**
	 * Porcentaje de asistencia del alumno
	 */
    public int getAl_asistencia() {
        return al_asistencia;
    }

    public void setTcc_cursocu(orm.Tcc_curso value) {
        if (tcc_cursocu != null) {
            tcc_cursocu.tcc_alumno.remove(this);
        }
        if (value != null) {
            value.tcc_alumno.add(this);
        }
    }

    public orm.Tcc_curso getTcc_cursocu() {
        return tcc_cursocu;
    }

    /**
	 * This method is for internal use only.
	 */
    public void setORM_Tcc_cursocu(orm.Tcc_curso value) {
        this.tcc_cursocu = value;
    }

    private orm.Tcc_curso getORM_Tcc_cursocu() {
        return tcc_cursocu;
    }

    public void setTcc_apoderadoap(orm.Tcc_apoderado value) {
        if (tcc_apoderadoap != null) {
            tcc_apoderadoap.tcc_alumno.remove(this);
        }
        if (value != null) {
            value.tcc_alumno.add(this);
        }
    }

    public orm.Tcc_apoderado getTcc_apoderadoap() {
        return tcc_apoderadoap;
    }

    /**
	 * This method is for internal use only.
	 */
    public void setORM_Tcc_apoderadoap(orm.Tcc_apoderado value) {
        this.tcc_apoderadoap = value;
    }

    private orm.Tcc_apoderado getORM_Tcc_apoderadoap() {
        return tcc_apoderadoap;
    }

    private void setORM_Tcc_anotacion(java.util.Set value) {
        this.ORM_tcc_anotacion = value;
    }

    private java.util.Set getORM_Tcc_anotacion() {
        return ORM_tcc_anotacion;
    }

    public final orm.Tcc_anotacionSetCollection tcc_anotacion = new orm.Tcc_anotacionSetCollection(this, _ormAdapter, orm.ORMConstants.KEY_TCC_ALUMNO_TCC_ANOTACION, orm.ORMConstants.KEY_TCC_ANOTACION_TCC_ALUMNOAL, orm.ORMConstants.KEY_MUL_ONE_TO_MANY);

    private void setORM_Tcc_nota(java.util.Set value) {
        this.ORM_tcc_nota = value;
    }

    private java.util.Set getORM_Tcc_nota() {
        return ORM_tcc_nota;
    }

    public final orm.Tcc_notaSetCollection tcc_nota = new orm.Tcc_notaSetCollection(this, _ormAdapter, orm.ORMConstants.KEY_TCC_ALUMNO_TCC_NOTA, orm.ORMConstants.KEY_TCC_NOTA_TCC_ALUMNOAL, orm.ORMConstants.KEY_MUL_ONE_TO_MANY);

    private void setORM_Tcc_asistencia(java.util.Set value) {
        this.ORM_tcc_asistencia = value;
    }

    private java.util.Set getORM_Tcc_asistencia() {
        return ORM_tcc_asistencia;
    }

    public final orm.Tcc_asistenciaSetCollection tcc_asistencia = new orm.Tcc_asistenciaSetCollection(this, _ormAdapter, orm.ORMConstants.KEY_TCC_ALUMNO_TCC_ASISTENCIA, orm.ORMConstants.KEY_TCC_ASISTENCIA_TCC_ALUMNOAL, orm.ORMConstants.KEY_MUL_ONE_TO_MANY);

    private void setORM_Tcc_asistenciaevento(java.util.Set value) {
        this.ORM_tcc_asistenciaevento = value;
    }

    private java.util.Set getORM_Tcc_asistenciaevento() {
        return ORM_tcc_asistenciaevento;
    }

    public final orm.Tcc_asistenciaeventoSetCollection tcc_asistenciaevento = new orm.Tcc_asistenciaeventoSetCollection(this, _ormAdapter, orm.ORMConstants.KEY_TCC_ALUMNO_TCC_ASISTENCIAEVENTO, orm.ORMConstants.KEY_TCC_ASISTENCIAEVENTO_TCC_ALUMNOAL, orm.ORMConstants.KEY_MUL_ONE_TO_MANY);

    public String toString() {
        return String.valueOf(getAl_id());
    }
}
