package br.com.dimag.safetycar.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "sigla" }) })
public class UF extends BaseEntity {

    @NotNull
    @Length(max = 2, min = 2)
    private String sigla;

    @NotNull
    @Length(max = 30)
    private String descricao;

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String getTextDefault() {
        return sigla;
    }
}
