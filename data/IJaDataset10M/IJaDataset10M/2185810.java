package Model;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Tiago Peres
 * @version 3.0
 * @since 3.0
 */
public abstract class Atividade {

    private int id;

    private Grupo grupo;

    private String titulo;

    private String parceiros;

    private String descricao;

    private String justificativa;

    private Date dataInicio;

    private Date dataFim;

    private String comentario;

    private String resultadosEsperados;

    private String resultadosAlcancados;

    private boolean coletiva;

    private ArrayList<Aluno> responsaveis;

    /**
     * Cria um array de alunos responsáveis pela atividade e define a atividade coletiva como falso.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * 
     */
    public Atividade() {
        responsaveis = new ArrayList<Aluno>();
        coletiva = false;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return id da atividade
     */
    public int getId() {
        return id;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return id da atividade como uma string
     */
    public String getIdAsString() {
        return String.valueOf(id);
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return titulo das atividades a serem exibidas
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return descrição das atividades a serem exibidas
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Muda o id das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param id das atividades a serem exibidas
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Muda o titulo das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param titulo das atividades a serem exibidas
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Muda a descrição das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param descrição das atividades a serem exibidas
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return parceiros das atividades a serem exibidas
     */
    public String getParceiros() {
        return parceiros;
    }

    /**
     * Muda os parceiros das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param descrição dos parceiros a serem exibidas
     */
    public void setParceiros(String parceiros) {
        this.parceiros = parceiros;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return justificativa das atividades a serem exibidas
     */
    public String getJustificativa() {
        return justificativa;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return data de inicio das atividades a serem exibidas
     */
    public Date getDataInicio() {
        return dataInicio;
    }

    /**
     * Muda a data de inicio das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param data de inicio das atividades a serem exibidas
     */
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return data de fim das atividades a serem exibidas
     */
    public Date getDataFim() {
        return dataFim;
    }

    /**
     * Muda a data de fim das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param data de fim das atividades a serem exibidas
     */
    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return comentario das atividades a serem exibidas
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Muda o comentario das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param comentario das atividades a serem exibidas
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return resultados esperados das atividades a serem exibidas
     */
    public String getResultadosEsperados() {
        return resultadosEsperados;
    }

    /**
     * Muda os resultados esperados das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param resultados esperados das atividades a serem exibidas
     */
    public void setResultadosEsperados(String resultadosEsperados) {
        this.resultadosEsperados = resultadosEsperados;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return resultados alcançados das atividades a serem exibidas
     */
    public String getResultadosAlcancados() {
        return resultadosAlcancados;
    }

    /**
     * Muda os resultados alcancados das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param resultados alcancados das atividades a serem exibidas
     */
    public void setResultadosAlcancados(String resultadosAlcancados) {
        this.resultadosAlcancados = resultadosAlcancados;
    }

    /**
     * Muda as justificativas das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param justificativas das atividades a serem exibidas
     */
    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return responsaveis das atividades a serem exibidas
     */
    public ArrayList<Aluno> getResponsaveis() {
        return responsaveis;
    }

    /**
     * Muda os responsaveis das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param responsaveis das atividades a serem exibidas
     */
    public void setResponsaveis(ArrayList<Aluno> responsaveis) {
        this.responsaveis = responsaveis;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return o status é coletiva das atividades a serem exibidas
     */
    public boolean isColetiva() {
        return coletiva;
    }

    /**
     * Muda o status de coletivo das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param status coletivo das atividades a serem exibidas
     */
    public void setColetiva(boolean coletiva) {
        this.coletiva = coletiva;
    }

    /**
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @return grupo das atividades a serem exibidas
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * Muda o grupo das atividades a serem exibidas.
     * 
     * @author Tiago Peres
     * @version 3.0
     * @since 3.0
     * @param grupo das atividades a serem exibidas
     */
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
