package doghost.entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author duo
 */
@Entity
public class Animal implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected String nome;

    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date nacimento;

    protected String caracteristicas;

    @ManyToOne
    protected Cliente cliente;

    @ManyToOne
    protected Raca raca;

    @ManyToOne
    protected Veterinario veterinario;

    @ManyToOne
    protected Racao racao;

    protected String obsRacao;

    protected String obsSaude;

    protected boolean alternaVeterinario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        Long oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    /**
     * Get the value of nome
     *
     * @return the value of nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Set the value of nome
     *
     * @param nome new value of nome
     */
    public void setNome(String nome) {
        String oldNome = this.nome;
        this.nome = nome;
        changeSupport.firePropertyChange("nome", oldNome, nome);
    }

    /**
     * Get the value of nacimento
     *
     * @return the value of nacimento
     */
    public Date getNacimento() {
        return nacimento;
    }

    /**
     * Set the value of nacimento
     *
     * @param nacimento new value of nacimento
     */
    public void setNacimento(Date nacimento) {
        this.nacimento = nacimento;
    }

    /**
     * Get the value of raca
     *
     * @return the value of raca
     */
    public Raca getRaca() {
        return raca;
    }

    /**
     * Set the value of raca
     *
     * @param raca new value of raca
     */
    public void setRaca(Raca raca) {
        this.raca = raca;
    }

    /**
     * Get the value of veterinario
     *
     * @return the value of veterinario
     */
    public Veterinario getVeterinario() {
        return veterinario;
    }

    /**
     * Set the value of veterinario
     *
     * @param veterinario new value of veterinario
     */
    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    /**
     * Get the value of racao
     *
     * @return the value of racao
     */
    public Racao getRacao() {
        return racao;
    }

    /**
     * Set the value of racao
     *
     * @param racao new value of racao
     */
    public void setRacao(Racao racao) {
        this.racao = racao;
    }

    /**
     * Get the value of obsRacao
     *
     * @return the value of obsRacao
     */
    public String getObsRacao() {
        return obsRacao;
    }

    /**
     * Set the value of obsRacao
     *
     * @param obsRacao new value of obsRacao
     */
    public void setObsRacao(String obsRacao) {
        this.obsRacao = obsRacao;
    }

    /**
     * Get the value of obsSaude
     *
     * @return the value of obsSaude
     */
    public String getObsSaude() {
        return obsSaude;
    }

    /**
     * Set the value of obsSaude
     *
     * @param obsSaude new value of obsSaude
     */
    public void setObsSaude(String obsSaude) {
        this.obsSaude = obsSaude;
    }

    /**
     * Get the value of alternaVeterinario
     *
     * @return the value of alternaVeterinario
     */
    public boolean isAlternaVeterinario() {
        return alternaVeterinario;
    }

    /**
     * Set the value of alternaVeterinario
     *
     * @param alternaVeterinario new value of alternaVeterinario
     */
    public void setAlternaVeterinario(boolean alternaVeterinario) {
        this.alternaVeterinario = alternaVeterinario;
    }

    /**
     * Get the value of caracteristicas
     *
     * @return the value of caracteristicas
     */
    public String getCaracteristicas() {
        return caracteristicas;
    }

    /**
     * Set the value of caracteristicas
     *
     * @param caracteristicas new value of caracteristicas
     */
    public void setCaracteristicas(String caracteristicas) {
        String oldCaracteristicas = this.caracteristicas;
        this.caracteristicas = caracteristicas;
        changeSupport.firePropertyChange("caracteristicas", oldCaracteristicas, caracteristicas);
    }

    /**
     * Get the value of cliente
     *
     * @return the value of cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Set the value of cliente
     *
     * @param cliente new value of cliente
     */
    public void setCliente(Cliente cliente) {
        Cliente oldCliente = this.cliente;
        this.cliente = cliente;
        changeSupport.firePropertyChange("cliente", oldCliente, cliente);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Animal)) {
            return false;
        }
        Animal other = (Animal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getNome();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
