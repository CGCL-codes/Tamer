package br.com.caelum.stella.boleto.bancos;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Emissor;
import java.net.URL;
import java.util.Calendar;

/**
 * @author Alberto Pc
 */
public class HSBC implements Banco {

    public static final String LOCAL_PAGAMENTO = "Pagar preferencialmente em ag�ncia do HSBC";

    private static final String NUMERO_HSBC = "399";

    private DVGenerator dvGenerator = new DVGenerator();

    private static final String CODIGO_APLICATIVO = "2";

    private int getSegundoDigitoVerificador(Boleto boleto) {
        int tipo = getTipoIdentificador(boleto);
        long codigoDoDocumento = Long.parseLong(getNossoNumeroDoEmissorFormatado(boleto.getEmissor()));
        long codigo = Long.valueOf("" + codigoDoDocumento + getPrimeiroDigitoVerificador(boleto) + tipo);
        int codigoDoCedente = boleto.getEmissor().getCodFornecidoPelaAgencia();
        int resultado;
        String soma;
        if (tipo == 4) {
            int data = Integer.valueOf(String.format("%1$td%1$tm%1$ty", boleto.getDatas().getVencimento()));
            soma = String.format("%010d", codigo + codigoDoCedente + data);
        } else {
            soma = String.format("%010d", codigo + codigoDoCedente);
        }
        resultado = 0;
        int i = 0;
        for (int x : new int[] { 8, 9, 2, 3, 4, 5, 6, 7, 8, 9 }) {
            int parcela = x * (soma.charAt(i) - '0');
            resultado += parcela;
            i++;
        }
        resultado %= 11;
        resultado %= 10;
        return resultado;
    }

    public String getCodigoDoDocumentoFinalComDigitosVerificadores(Boleto boleto) {
        return String.format("%s%d%d%d", boleto.getNoDocumento(), getPrimeiroDigitoVerificador(boleto), getTipoIdentificador(boleto), getSegundoDigitoVerificador(boleto));
    }

    private int getPrimeiroDigitoVerificador(Boleto boleto) {
        return dvGenerator.geraDVMod11(getNossoNumeroDoEmissorFormatado(boleto.getEmissor()));
    }

    public String geraCodigoDeBarrasPara(Boleto boleto) {
        StringBuilder digitosVerificadores = new StringBuilder();
        int primeiroDigitoVerificador = getPrimeiroDigitoVerificador(boleto);
        int tipo = getTipoIdentificador(boleto);
        int segundoDigitoVerificador = getSegundoDigitoVerificador(boleto);
        digitosVerificadores.append(primeiroDigitoVerificador);
        digitosVerificadores.append(tipo);
        digitosVerificadores.append(segundoDigitoVerificador);
        StringBuilder codigoDeBarras = new StringBuilder();
        codigoDeBarras.append(getNumeroFormatado());
        codigoDeBarras.append(boleto.getCodEspecieMoeda());
        codigoDeBarras.append(boleto.getFatorVencimento());
        codigoDeBarras.append(boleto.getValorFormatado());
        codigoDeBarras.append(String.format("%07d", boleto.getEmissor().getCodFornecidoPelaAgencia()));
        codigoDeBarras.append(getNossoNumeroDoEmissorFormatado(boleto.getEmissor()));
        codigoDeBarras.append(getDataFormatoJuliano(boleto.getDatas().getVencimento(), tipo));
        codigoDeBarras.append(HSBC.CODIGO_APLICATIVO);
        codigoDeBarras.insert(4, dvGenerator.geraDVMod11(codigoDeBarras.toString()));
        return codigoDeBarras.toString();
    }

    private int getTipoIdentificador(Boleto boleto) {
        int tipo = 4;
        if (boleto.getDatas().getVencimento() == null || (boleto.getFatorVencimento() == null || boleto.getFatorVencimento().trim().equals(""))) {
            throw new IllegalArgumentException("Boletos do HSBC sem data de vencimento não são suportados.");
        }
        return tipo;
    }

    /**
     * @param vencimento vencimento do boleto
     * @param tipo       tipo do identificador, 4 ou 5
     * @return data no formato Juliano
     */
    public String getDataFormatoJuliano(Calendar vencimento, int tipo) {
        String result;
        Calendar dataLimite = Calendar.getInstance();
        dataLimite.set(Calendar.DAY_OF_MONTH, 1);
        dataLimite.set(Calendar.MONTH, 7 - 1);
        dataLimite.set(Calendar.YEAR, 1997);
        if (vencimento.before(dataLimite)) {
            result = "0000";
        } else {
            if (tipo == 4) {
                int diaDoAno = vencimento.get(Calendar.DAY_OF_YEAR);
                int digitoDoAno = vencimento.get(Calendar.YEAR) % 10;
                result = String.format("%03d%d", diaDoAno, digitoDoAno);
            } else if (tipo == 5) {
                result = "0000";
            } else {
                throw new IllegalArgumentException("Tipo inv�lido");
            }
        }
        return result;
    }

    public String getCarteiraDoEmissorFormatado(Emissor emissor) {
        return "CNR";
    }

    public String getContaCorrenteDoEmissorFormatado(Emissor emissor) {
        return String.format("%07d", emissor.getContaCorrente());
    }

    public URL getImage() {
        return getClass().getResource(String.format("/br/com/caelum/stella/boleto/img/%s.png", getNumeroFormatado()));
    }

    public String getNossoNumeroDoEmissorFormatado(Emissor emissor) {
        return String.format("%013d", emissor.getNossoNumero());
    }

    public String getNumeroFormatado() {
        return HSBC.NUMERO_HSBC;
    }
}
