package br.com.controlegastos.entity;

import br.com.controlegastos.util.Constantes;
import br.com.framework.persistenceservice.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 *
 * @author p0011651
 */
@javax.persistence.Entity
@Table(schema = Constantes.SCHEMA, name = "LOCAL")
public class Local extends BaseEntity<Long> {

    @Column(name = "NOME", nullable = false, length = 80)
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public Class getIDClass() {
        return Long.class;
    }
}
