package petshop.classes;

/**
 * @author arthur
 *
 */
public class Endereco {

    private String rua;

    private int num;

    private String complemento;

    private String bairro;

    private String cidade;

    private String uf;

    private String cep;

    /**
     * @param rua
     * @param num
     * @param complemento
     * @param bairro
     * @param cidade
     * @param uf
     * @param cep
     */
    public Endereco(String rua, int num, String complemento, String bairro, String cidade, String uf, String cep) {
        super();
        this.rua = rua;
        this.num = num;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

    Endereco() {
    }

    /**
     * @return
     */
    public String getRua() {
        return rua;
    }

    /**
     * @param rua
     */
    public void setRua(String rua) {
        this.rua = rua;
    }

    /**
     * @return
     */
    public int getNum() {
        return num;
    }

    /**
     * @param num
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * @return
     */
    public String getComplemento() {
        return complemento;
    }

    /**
     * @param complemento
     */
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    /**
     * @return
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * @param bairro
     */
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    /**
     * @return
     */
    public String getCidade() {
        return cidade;
    }

    /**
     * @param cidade
     */
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    /**
     * @return
     */
    public String getUf() {
        return uf;
    }

    /**
     * @param uf
     */
    public void setUf(String uf) {
        this.uf = uf;
    }

    /**
     * @return
     */
    public String getCep() {
        return cep;
    }

    /**
     * @param cep
     */
    public void setCep(String cep) {
        this.cep = cep;
    }
}
