package uniriotec.pm.empresa.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author LABCCET
 */
public class Empregado implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cpf;

    private String nome;

    private String sexo;

    private Date dataNascimento;

    private Date dataAdmissao;

    private double salario;

    private Date dataDesligamento;

    public Empregado(String cpf, String nome) {
        this.cpf = cpf;
        this.nome = nome;
    }

    public Empregado() {
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (this.validaCPF()) {
            this.cpf = cpf;
        }
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Date getDataDesligamento() {
        return dataDesligamento;
    }

    public void setDataDesligamento(Date dataDesligamento) {
        this.dataDesligamento = dataDesligamento;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public boolean validaCPF() {
        int d1, d2;
        int digito1, digito2, resto;
        int digitoCPF;
        String nDigResult;
        d1 = d2 = 0;
        digito1 = digito2 = resto = 0;
        for (int nCount = 1; nCount < cpf.length() - 1; nCount++) {
            digitoCPF = Integer.valueOf(cpf.substring(nCount - 1, nCount)).intValue();
            d1 = d1 + (11 - nCount) * digitoCPF;
            d2 = d2 + (12 - nCount) * digitoCPF;
        }
        resto = (d1 % 11);
        if (resto < 2) digito1 = 0; else digito1 = 11 - resto;
        d2 += 2 * digito1;
        resto = (d2 % 11);
        if (resto < 2) digito2 = 0; else digito2 = 11 - resto;
        String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());
        nDigResult = String.valueOf(digito1) + String.valueOf(digito2);
        return nDigVerific.equals(nDigResult);
    }
}
