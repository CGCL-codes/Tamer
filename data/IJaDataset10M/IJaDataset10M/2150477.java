package modelo;

public class Funcionario {

    private int matricula;

    private String nome;

    private boolean sexo;

    private double salario;

    private int codigodepto;

    public int getMatricula() {
        return matricula;
    }

    public String getNome() {
        return nome;
    }

    public boolean getSexo() {
        return sexo;
    }

    public double getSalario() {
        return salario;
    }

    public int getCodigoDepto() {
        return codigodepto;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSexo(boolean sexo) {
        this.sexo = sexo;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public void setCodigoDepto(int cd) {
        codigodepto = cd;
    }

    public void aumentarSalario(double percentualAumento) {
        salario *= percentualAumento;
    }
}
