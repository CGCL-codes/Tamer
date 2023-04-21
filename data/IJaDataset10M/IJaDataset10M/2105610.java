package br.gov.demoiselle.escola.bean;

import java.io.InputStream;
import br.gov.framework.demoiselle.core.bean.IPojo;

public class Foto implements IPojo {

    private static final long serialVersionUID = 1L;

    private String nome;

    private InputStream inputStream;

    public Foto(String nome, InputStream inputStream) {
        super();
        this.nome = nome;
        this.inputStream = inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
