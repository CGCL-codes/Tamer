package negocio.busqueda;

import negocio.cv.Condicion;
import negocio.cv.Institucion;
import negocio.cv.Nivel;
import negocio.cv.TipoEstudio;
import negocio.cv.Titulo;

public class EstudioPerfil {

    private int idEstudioPerfil;

    private TipoEstudio tipoEstudio;

    private Titulo titulo;

    private Condicion condicion;

    private Nivel nivel;

    private Institucion institucion;

    private Boolean excluyente;

    /**
    * @roseuid 3C312A75036B
    */
    public EstudioPerfil() {
    }

    public EstudioPerfil(TipoEstudio tipoEstudio, Titulo titulo, Condicion condicion, Nivel nivel, Institucion institucion, Boolean excluyente) {
        super();
        this.tipoEstudio = tipoEstudio;
        this.titulo = titulo;
        this.condicion = condicion;
        this.nivel = nivel;
        this.institucion = institucion;
        this.excluyente = excluyente;
    }

    public TipoEstudio getTipoEstudio() {
        return tipoEstudio;
    }

    public void setTipoEstudio(TipoEstudio tipoEstudio) {
        this.tipoEstudio = tipoEstudio;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    public Condicion getCondicion() {
        return condicion;
    }

    public void setCondicion(Condicion condicion) {
        this.condicion = condicion;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Boolean getExcluyente() {
        return excluyente;
    }

    public void setExcluyente(Boolean excluyente) {
        this.excluyente = excluyente;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public int getIdEstudioPerfil() {
        return idEstudioPerfil;
    }

    public void setIdEstudioPerfil(int idEstudioPerfil) {
        this.idEstudioPerfil = idEstudioPerfil;
    }

    public boolean equals(Object obj) {
        EstudioPerfil e = (EstudioPerfil) obj;
        boolean igual = false;
        if (tipoEstudio.equals(e.tipoEstudio) && titulo.equals(e.titulo) && institucion.equals(e.institucion)) igual = true;
        return igual;
    }
}
