package bean;

/**
 *
 * @author galluzzo
 */
public class ProBean {

    private int id;

    private String text;

    private ProposalBean proposta;

    private UsuarioBean owner;

    /** Creates a new instance of ProBean */
    public ProBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsuarioBean getOwner() {
        return owner;
    }

    public void setOwner(UsuarioBean owner) {
        this.owner = owner;
    }

    public ProposalBean getProposta() {
        return proposta;
    }

    public void setProposta(ProposalBean proposta) {
        this.proposta = proposta;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
