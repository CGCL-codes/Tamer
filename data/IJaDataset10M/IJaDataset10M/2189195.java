package iws.db.object;

import java.io.Serializable;

public class Esercente implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private String username;

    private String password;

    private String nome;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
