package br.gov.sjc.classes;

/**
 * @author rodrigo.ramos
 *
 */
public class Nome {

    private int _id;

    private int SiasN;

    private int idMembro;

    private String Nome;

    private String DNasc;

    private String NomeMae;

    private String NomePai;

    private String NaturalCidade;

    /**
	 * @return the _id
	 */
    public int get_id() {
        return _id;
    }

    /**
	 * @return the siasN
	 */
    public int getSiasN() {
        return SiasN;
    }

    /**
	 * @return the idMembro
	 */
    public int getIdMembro() {
        return idMembro;
    }

    /**
	 * @return the nome
	 */
    public String getNome() {
        return Nome;
    }

    /**
	 * @return the dNasc
	 */
    public String getDNasc() {
        return DNasc;
    }

    /**
	 * @return the nomeMae
	 */
    public String getNomeMae() {
        return NomeMae;
    }

    /**
	 * @return the nomePai
	 */
    public String getNomePai() {
        return NomePai;
    }

    /**
	 * @return the naturalCidade
	 */
    public String getNaturalCidade() {
        return NaturalCidade;
    }

    /**
	 * @param _id the _id to set
	 */
    public void set_id(int _id) {
        this._id = _id;
    }

    /**
	 * @param siasN the siasN to set
	 */
    public void setSiasN(int siasN) {
        SiasN = siasN;
    }

    /**
	 * @param idMembro the idMembro to set
	 */
    public void setIdMembro(int idMembro) {
        this.idMembro = idMembro;
    }

    /**
	 * @param nome the nome to set
	 */
    public void setNome(String nome) {
        Nome = nome;
    }

    /**
	 * @param dNasc the dNasc to set
	 */
    public void setDNasc(String dNasc) {
        DNasc = dNasc;
    }

    /**
	 * @param nomeMae the nomeMae to set
	 */
    public void setNomeMae(String nomeMae) {
        NomeMae = nomeMae;
    }

    /**
	 * @param nomePai the nomePai to set
	 */
    public void setNomePai(String nomePai) {
        NomePai = nomePai;
    }

    /**
	 * @param naturalCidade the naturalCidade to set
	 */
    public void setNaturalCidade(String naturalCidade) {
        NaturalCidade = naturalCidade;
    }

    @Override
    public String toString() {
        return "Nome [_id=" + _id + ", SiasN=" + SiasN + ", idMembro=" + idMembro + ", Nome=" + Nome + ", DNasc=" + DNasc + ", NomeMae=" + NomeMae + ", NomePai=" + NomePai + ", NaturalCidade=" + NaturalCidade + "]";
    }
}
