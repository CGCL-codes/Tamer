package model;

public class Bug {

    private String id;

    private String nomeUnix;

    private String titulo;

    private String prioridade;

    private String descricao;

    private String status;

    private String usuario;

    /**
	 * @return the usuario
	 */
    public String getUsuario() {
        return usuario;
    }

    /**
	 * @param usuario the usuario to set
	 */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return the nomeUnix
	 */
    public String getNomeUnix() {
        return nomeUnix;
    }

    /**
	 * @param nomeUnix the nomeUnix to set
	 */
    public void setNomeUnix(String nomeUnix) {
        this.nomeUnix = nomeUnix;
    }

    /**
	 * @return the titulo
	 */
    public String getTitulo() {
        return titulo;
    }

    /**
	 * @param titulo the titulo to set
	 */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
	 * @return the prioridade
	 */
    public String getPrioridade() {
        return prioridade;
    }

    /**
	 * @param prioridade the prioridade to set
	 */
    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    /**
	 * @return the descricao
	 */
    public String getDescricao() {
        return descricao;
    }

    /**
	 * @param descricao the descricao to set
	 */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
	 * @return the status
	 */
    public String getStatus() {
        return status;
    }

    /**
	 * @param status the status to set
	 */
    public void setStatus(String status) {
        this.status = status;
    }
}
