package opencafe.beans;

import opencafe.beans.exception.ValidationException;

public class Cliente extends Entidade {

    private String endereco;

    private String telefone;

    private String cpf;

    private double saldo;

    public void debitar(double debito) {
        saldo -= debito;
    }

    public void creditar(double credito) {
        saldo += credito;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getSaldo() {
        return saldo;
    }

    @Override
    public void validar() throws ValidationException {
        String msg = "";
        if (getDescricao() == null || "".equals(getDescricao())) msg += "O Nome est� em branco.\n";
        if (endereco == null || "".equals(endereco)) msg += "O endere�o est� em branco.\n";
        if (telefone == null || "".equals(telefone)) msg += "O telefone est� em branco.\n"; else if (telefone != null && !telefone.matches("[(][0-9]{2}[)][0-9]{4}[-][0-9]{4}")) msg += "O telefone � inv�lido.\n";
        if (cpf == null || "".equals(cpf)) msg += "O CPF est� em branco.\n"; else if (cpf != null && !cpf.matches("[0-9]{3} [0-9]{3} [0-9]{3} [0-9]{2}")) msg += "O cpf � inv�lido.\n";
        if (!"".equals(msg)) throw new ValidationException(msg);
    }
}
