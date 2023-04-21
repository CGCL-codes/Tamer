package metso.paradigma.core.business.regole;

import java.io.Serializable;
import java.util.Set;

public class Contratto implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7034545545829L;

    private int id;

    private String nome;

    private Set<RegolaOperatore> regole;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<RegolaOperatore> getRegole() {
        return this.regole;
    }

    public void setRegole(Set<RegolaOperatore> regole) {
        this.regole = regole;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Contratto other = (Contratto) obj;
        if (id != other.id) return false;
        return true;
    }
}
