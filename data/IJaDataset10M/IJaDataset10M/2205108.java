package br.edu.unifei.VRaptor.modelo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author Todo Poderoso
 */
@Entity
@DiscriminatorValue(value = "Mentor")
public class Mentor extends Pessoa {

    private String graduacao;

    public String getGraduacao() {
        return graduacao;
    }

    public void setGraduacao(String graduacao) {
        this.graduacao = graduacao;
    }
}
