package orm.orm;

import java.io.Serializable;

public class Cam_tipo_reporte implements Serializable {

    public Cam_tipo_reporte() {
    }

    private java.util.Set this_getSet(int key) {
        if (key == orm.orm.ORMConstants.KEY_CAM_TIPO_REPORTE_CAM_REPORTE) {
            return ORM_cam_reporte;
        }
        return null;
    }

    org.orm.util.ORMAdapter _ormAdapter = new org.orm.util.AbstractORMAdapter() {

        public java.util.Set getSet(int key) {
            return this_getSet(key);
        }
    };

    private int ti_id_reporte;

    private String ti_descripcion;

    private java.util.Set ORM_cam_reporte = new java.util.HashSet();

    private void setTi_id_reporte(int value) {
        this.ti_id_reporte = value;
    }

    public int getTi_id_reporte() {
        return ti_id_reporte;
    }

    public int getORMID() {
        return getTi_id_reporte();
    }

    public void setTi_descripcion(String value) {
        this.ti_descripcion = value;
    }

    public String getTi_descripcion() {
        return ti_descripcion;
    }

    private void setORM_Cam_reporte(java.util.Set value) {
        this.ORM_cam_reporte = value;
    }

    private java.util.Set getORM_Cam_reporte() {
        return ORM_cam_reporte;
    }

    public final orm.orm.Cam_reporteSetCollection cam_reporte = new orm.orm.Cam_reporteSetCollection(this, _ormAdapter, orm.orm.ORMConstants.KEY_CAM_TIPO_REPORTE_CAM_REPORTE, orm.orm.ORMConstants.KEY_CAM_REPORTE_TI_ID_REPORTE, orm.orm.ORMConstants.KEY_MUL_ONE_TO_MANY);

    public String toString() {
        return String.valueOf(getTi_id_reporte());
    }
}
