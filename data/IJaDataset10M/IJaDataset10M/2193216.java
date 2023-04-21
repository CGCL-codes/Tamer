package common.scrum;

import java.io.Serializable;

public class DailyMeeting implements Serializable {

    private String usuario;

    private String tarea;

    private int id_tarea;

    private String pregunta1;

    private String pregunta2;

    private String pregunta3;

    @Override
    public String toString() {
        return "DailyMeeting [id_tarea=" + id_tarea + ", pregunta1=" + pregunta1 + ", pregunta2=" + pregunta2 + ", pregunta3=" + pregunta3 + ", tarea=" + tarea + ", usuario=" + usuario + "]";
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getUsuario() {
        return usuario;
    }

    public int getId_tarea() {
        return id_tarea;
    }

    public String getPregunta1() {
        return pregunta1;
    }

    public String getPregunta2() {
        return pregunta2;
    }

    public String getPregunta3() {
        return pregunta3;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setId_tarea(int idTarea) {
        id_tarea = idTarea;
    }

    public void setPregunta1(String pregunta1) {
        this.pregunta1 = pregunta1;
    }

    public void setPregunta2(String pregunta2) {
        this.pregunta2 = pregunta2;
    }

    public void setPregunta3(String pregunta3) {
        this.pregunta3 = pregunta3;
    }
}
