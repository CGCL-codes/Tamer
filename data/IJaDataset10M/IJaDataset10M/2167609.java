package br.com.dip.entidade;

import java.io.InputStream;

public class File {

    private String extencao;

    private String name;

    private InputStream stream;

    public String getExtencao() {
        return extencao;
    }

    public void setExtencao(String extencao) {
        this.extencao = extencao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        String ext[] = this.name.split("\\.");
        setExtencao(ext[1]);
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }
}
