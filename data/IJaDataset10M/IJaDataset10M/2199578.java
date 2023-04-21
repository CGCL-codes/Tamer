package org.jboss.seam.Comercios;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

@Entity
@Name("comercioHerencia")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "nom")
public class ComercioHerencia implements Serializable {

    private static final long serialVersionUID = 1L;

    String nom;

    String descripcion;

    public ComercioHerencia() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    @NotNull
    @Id
    public String getNom() {
        return nom;
    }

    public void setDescripcion(String desc) {
        this.descripcion = desc;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
