package SOAPVO;

public class SubsectorSOAPVO {

    int id;

    String nombre;

    int idCurso;

    String nombreCurso;

    /**
	 * Crea un nuevo subsector con sus datos respectivos
	 * @param subsectorOrm
	 * @return
	 */
    public static SubsectorSOAPVO crearSubsectorSOAPVO(orm.Tan_subsector subsectorOrm) {
        SubsectorSOAPVO objeto = new SubsectorSOAPVO();
        objeto.setId(subsectorOrm.getSub_id());
        objeto.setNombre(subsectorOrm.getSub_nombre());
        objeto.setIdCurso(subsectorOrm.getTan_cursocur().getCur_id());
        objeto.setNombreCurso(subsectorOrm.getTan_cursocur().getCur_nombre());
        return objeto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }
}
