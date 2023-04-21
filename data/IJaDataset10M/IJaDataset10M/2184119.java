package sistema.monetario.entidade;

public class ContaCartao extends ContaCorrente {

    public ContaCartao(double saldo, String numero) {
        super(saldo, numero);
    }

    public String toString() {
        return "********* Conta Cart�o ************\n" + "* N�mero da Conta : " + getNumero() + "\n" + "* Saldo : " + getSaldo() + "\n" + "***********************************\n";
    }
}
