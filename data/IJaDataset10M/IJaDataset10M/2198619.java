package persistencia.objetos;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 * Experiencias generated by hbm2java
 */
public class Experiencias implements java.io.Serializable {

    private Integer idExperiencia;

    private Curriculum curriculum;

    private int idCurriculum;

    private Date dataInicio;

    private Date dataFinal;

    private String entidade;

    private String cargo;

    private String descricao;

    private String telefoneEntidade;

    public Experiencias() {
    }

    public Experiencias(int idCurriculumVitae, Date dataInicio, Date dataFinal, String entidade, String cargo, String descricao) {
        this.idCurriculum = idCurriculumVitae;
        this.dataInicio = dataInicio;
        this.dataFinal = dataFinal;
        this.entidade = entidade;
        this.cargo = cargo;
        this.descricao = descricao;
    }

    public Experiencias(Curriculum curriculum, int idCurriculumVitae, Date dataInicio, Date dataFinal, String entidade, String cargo, String descricao, String telefoneEntidade) {
        this.curriculum = curriculum;
        this.idCurriculum = idCurriculumVitae;
        this.dataInicio = dataInicio;
        this.dataFinal = dataFinal;
        this.entidade = entidade;
        this.cargo = cargo;
        this.descricao = descricao;
        this.telefoneEntidade = telefoneEntidade;
    }

    public Experiencias getExperiencia(HttpServletRequest request) {
        this.setCargo(request.getParameter(""));
        this.setDataFinal(utils.Date.inicializaDate(request.getParameter("dia"), request.getParameter("mes"), request.getParameter("ano")));
        this.setDataInicio(utils.Date.inicializaDate(request.getParameter("dia"), request.getParameter("mes"), request.getParameter("ano")));
        this.setDescricao(request.getParameter(""));
        this.setEntidade(request.getParameter(""));
        this.setTelefoneEntidade(request.getParameter(""));
        return this;
    }

    public Integer getIdExperiencia() {
        return this.idExperiencia;
    }

    public void setIdExperiencia(Integer idExperiencia) {
        this.idExperiencia = idExperiencia;
    }

    public Curriculum getCurriculum() {
        return this.curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public int getIdCurriculum() {
        return this.idCurriculum;
    }

    public void setIdCurriculum(int idCurriculumVitae) {
        this.idCurriculum = idCurriculumVitae;
    }

    public Date getDataInicio() {
        return this.dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFinal() {
        return this.dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getEntidade() {
        return this.entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public String getCargo() {
        return this.cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTelefoneEntidade() {
        return this.telefoneEntidade;
    }

    public void setTelefoneEntidade(String telefoneEntidade) {
        this.telefoneEntidade = telefoneEntidade;
    }
}
