package br.com.senai.classes;

public class Funcionario extends Cadastro {

    private String rg;

    private String cpf;

    private String setor;

    private String turno;

    private String cargo;

    private double salario;

    private int inicio;

    private int termino;

    public void escrevaResumo() {
        System.out.printf("\nSalario: %f\nCPF: %d\nC�dogo: %d\nSetor: %s\n Turno: %s\n" + "Cargo: %s\nIn�cio: %d\nT�rmino: %d", salario, cpf, setor, turno, cargo, inicio, termino);
        super.escreverResumo();
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getTermino() {
        return termino;
    }

    public void setTermino(int termino) {
        this.termino = termino;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getRg() {
        return rg;
    }
}
