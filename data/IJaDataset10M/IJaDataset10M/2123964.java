package samples.web.livrariademoiselle.bean.implementation;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author Flávio Gomes da Silva Lisboa
 * @version 0.1
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "funcionarios")
@SequenceGenerator(name = "FuncionarioSequence", sequenceName = "funcionarios_id_usuario_seq")
public class Funcionario extends samples.web.livrariademoiselle.bean.AbstractUsuario {

    @Id
    @GeneratedValue(generator = "FuncionarioSequence", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_usuario", columnDefinition = "serial")
    private Integer id;

    @Column(name = "matricula", length = 8)
    private String matricula;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "telefones_funcionario", joinColumns = @JoinColumn(name = "id_cliente", columnDefinition = "integer NOT NULL"), inverseJoinColumns = @JoinColumn(name = "id_funcionario", columnDefinition = "integer NOT NULL"))
    protected Set<Telefone> telefones = new HashSet<Telefone>();

    public Funcionario() {
        this.apelido = "";
        this.email = "";
        this.matricula = "";
        this.nome = "";
        this.senha = "";
        this.telefones = new HashSet<Telefone>();
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Set<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }
}
