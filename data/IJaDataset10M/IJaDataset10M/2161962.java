package org.jboleto.bancos;

import org.jboleto.Banco;
import org.jboleto.JBoletoBean;

/**
 * Classe responsavel em criar os campos do Banco Bradesco.
 * @author Fabio Souza
 */
public class Bradesco implements Banco {

    JBoletoBean boleto;

    public String getNumero() {
        return "237";
    }

    public Bradesco(JBoletoBean boleto) {
        this.boleto = boleto;
    }

    private String getCampoLivre() {
        String campo = boleto.getAgencia() + boleto.getCarteira() + boleto.getNossoNumero() + boleto.getContaCorrente() + "0";
        return campo;
    }

    private String getCampo1() {
        String campo = getNumero() + boleto.getMoeda() + getCampoLivre().substring(0, 5);
        return boleto.getDigitoCampo(campo, 2);
    }

    private String getCampo2() {
        String campo = getCampoLivre().substring(5, 15);
        return boleto.getDigitoCampo(campo, 1);
    }

    private String getCampo3() {
        String campo = getCampoLivre().substring(15, 25);
        return boleto.getDigitoCampo(campo, 1);
    }

    private String getCampo4() {
        String campo = getNumero() + boleto.getMoeda() + boleto.getFatorVencimento() + boleto.getValorTitulo() + boleto.getAgencia() + boleto.getCarteira() + boleto.getNossoNumero() + boleto.getContaCorrente() + "0";
        return boleto.getDigitoCodigoBarras(campo);
    }

    private String getCampo5() {
        String campo = boleto.getFatorVencimento() + boleto.getValorTitulo();
        return campo;
    }

    public String getCodigoBarras() {
        String campo = getNumero() + String.valueOf(boleto.getMoeda()) + getCampo4() + boleto.getFatorVencimento() + boleto.getValorTitulo() + boleto.getAgencia() + boleto.getCarteira() + boleto.getNossoNumero() + boleto.getContaCorrente() + "0";
        return campo;
    }

    public String getLinhaDigitavel() {
        return getCampo1().substring(0, 5) + "." + getCampo1().substring(5) + "  " + getCampo2().substring(0, 5) + "." + getCampo2().substring(5) + "  " + getCampo3().substring(0, 5) + "." + getCampo3().substring(5) + "  " + getCampo4() + "  " + getCampo5();
    }
}
