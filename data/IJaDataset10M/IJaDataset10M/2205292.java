package orm;

import java.io.Serializable;

public class Tcc_nota implements Serializable {

    public Tcc_nota() {
    }

    private void this_setOwner(Object owner, int key) {
        if (key == orm.ORMConstants.KEY_TCC_NOTA_TCC_ALUMNOAL) {
            this.tcc_alumnoal = (orm.Tcc_alumno) owner;
        } else if (key == orm.ORMConstants.KEY_TCC_NOTA_TCC_ASIGNATURAAS) {
            this.tcc_asignaturaas = (orm.Tcc_asignatura) owner;
        }
    }

    org.orm.util.ORMAdapter _ormAdapter = new org.orm.util.AbstractORMAdapter() {

        public void setOwner(Object owner, int key) {
            this_setOwner(owner, key);
        }
    };

    private int no_id;

    private String no_nota;

    private orm.Tcc_alumno tcc_alumnoal;

    private orm.Tcc_asignatura tcc_asignaturaas;

    private void setNo_id(int value) {
        this.no_id = value;
    }

    public int getNo_id() {
        return no_id;
    }

    public int getORMID() {
        return getNo_id();
    }

    public void setNo_nota(String value) {
        this.no_nota = value;
    }

    public String getNo_nota() {
        return no_nota;
    }

    public void setTcc_alumnoal(orm.Tcc_alumno value) {
        if (tcc_alumnoal != null) {
            tcc_alumnoal.tcc_nota.remove(this);
        }
        if (value != null) {
            value.tcc_nota.add(this);
        }
    }

    public orm.Tcc_alumno getTcc_alumnoal() {
        return tcc_alumnoal;
    }

    /**
	 * This method is for internal use only.
	 */
    public void setORM_Tcc_alumnoal(orm.Tcc_alumno value) {
        this.tcc_alumnoal = value;
    }

    private orm.Tcc_alumno getORM_Tcc_alumnoal() {
        return tcc_alumnoal;
    }

    public void setTcc_asignaturaas(orm.Tcc_asignatura value) {
        if (tcc_asignaturaas != null) {
            tcc_asignaturaas.tcc_nota.remove(this);
        }
        if (value != null) {
            value.tcc_nota.add(this);
        }
    }

    public orm.Tcc_asignatura getTcc_asignaturaas() {
        return tcc_asignaturaas;
    }

    /**
	 * This method is for internal use only.
	 */
    public void setORM_Tcc_asignaturaas(orm.Tcc_asignatura value) {
        this.tcc_asignaturaas = value;
    }

    private orm.Tcc_asignatura getORM_Tcc_asignaturaas() {
        return tcc_asignaturaas;
    }

    public String toString() {
        return String.valueOf(getNo_id());
    }
}
