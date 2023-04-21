package com.jpress.model;

import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Usuario autor;

    @ManyToOne
    private Post post;

    private String texto;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar data;

    public Comentario() {
    }

    public Comentario(Usuario autor, Post post, String texto, Calendar data) {
        super();
        this.autor = autor;
        this.post = post;
        this.texto = texto;
        this.data = data;
    }

    public Comentario(Usuario autor, String texto, Calendar data) {
        super();
        this.autor = autor;
        this.texto = texto;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Comentario [id=" + id + ", autor=" + autor + ", post=" + post + ", comentario=" + texto + ", data=" + data + "]";
    }
}
