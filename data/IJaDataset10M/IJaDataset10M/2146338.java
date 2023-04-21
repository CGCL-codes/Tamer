package org.opensih.descop.Modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.Type;
import org.hibernate.validator.NotNull;

@Entity
public class Seccion implements Serializable {

    private static final long serialVersionUID = -5004832327191989509L;

    private int id;

    private String titulo;

    private String texto;

    @NotNull
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int Id) {
        id = Id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Type(type = "text")
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
