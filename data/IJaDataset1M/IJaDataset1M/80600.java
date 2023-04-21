package br.com.radaction.entidades;

/**
 *
 * @author robson
 */
public class Cargo {

    private int cod;

    private String nome;

    private String funcao;

    private Departamento departamento;

    public Cargo(int cod) {
        this.cod = cod;
    }

    public Cargo() {
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int hashCode() {
        return this.cod;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cargo other = (Cargo) obj;
        if (this.cod != other.cod) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
