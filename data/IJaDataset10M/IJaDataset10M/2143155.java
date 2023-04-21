package br.com.petrobras.model;

import java.io.Serializable;

public class PessoaFisicaVO extends PessoaVO implements Serializable {

    static final long serialVersionUID = -2600992248250595486L;

    private String cpf;

    public PessoaFisicaVO() {
        super();
    }

    public PessoaFisicaVO(Integer cd) {
        super(cd);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCpfFmt() {
        if ((getCpf() != null) && (getCpf().length() == 11)) return String.format("%s.%s.%s/%s", getCpf().substring(0, 3), getCpf().substring(3, 6), getCpf().substring(6, 9), getCpf().substring(9)); else return "";
    }

    public boolean isCpfRepetido(String cpfAnterior, String cpf) {
        return ((cpf.length() > 0) && (getCpf().length() > 0) && !getCpf().equalsIgnoreCase(cpfAnterior) && getCpf().equalsIgnoreCase(cpf));
    }
}
