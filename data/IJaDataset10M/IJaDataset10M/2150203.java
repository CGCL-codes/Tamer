package br.com.gesclub.business.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa os locais onde os eventos ocorrem.
 *
 */
@Entity
@Table(name = "LOCAL_EVENTO")
public class LocalEvento extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CD_LOCAL_EVENTO", updatable = false, nullable = false, unique = true)
    private Long id;

    @Column(name = "DS_LOCAL_EVENTO", length = 45)
    private String local;

    @Column(name = "DS_TELEFONE", length = 8)
    private String telefone;

    @Column(name = "NU_CAPACIDADE_PESSOAS")
    private Integer capacidade;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public boolean isPersistenciaValida() {
        return atributosInvalidos().isEmpty();
    }

    public List<String> atributosInvalidos() {
        List<String> attr = new ArrayList<String>();
        return attr;
    }
}
