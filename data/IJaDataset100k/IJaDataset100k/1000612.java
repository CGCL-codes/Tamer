package br.com.arsmachina.introducaotapestry;

/**
 * Classe que representa um cliente.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class Cliente {

    private Integer id;

    private String nome;

    private String email;

    private int saldo;

    /**
	 * Returns the value of the <code>id</code> property.
	 * 
	 * @return a {@link Integer}.
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * Changes the value of the <code>id</code> property.
	 * 
	 * @param id a {@link Integer}.
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * Returns the value of the <code>nome</code> property.
	 * 
	 * @return a {@link String}.
	 */
    public String getNome() {
        return nome;
    }

    /**
	 * Changes the value of the <code>nome</code> property.
	 * 
	 * @param nome a {@link String}.
	 */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
	 * Returns the value of the <code>email</code> property.
	 * 
	 * @return a {@link String}.
	 */
    public String getEmail() {
        return email;
    }

    /**
	 * Changes the value of the <code>email</code> property.
	 * 
	 * @param email a {@link String}.
	 */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
	 * Returns the value of the <code>saldo</code> property.
	 * 
	 * @return a {@link int}.
	 */
    public int getSaldo() {
        return saldo;
    }

    /**
	 * Changes the value of the <code>saldo</code> property.
	 * 
	 * @param saldo a {@link int}.
	 */
    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Cliente other = (Cliente) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }
}
