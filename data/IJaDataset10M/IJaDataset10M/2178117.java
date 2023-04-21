package org.freedom.ecf.test;

import junit.framework.TestCase;
import org.freedom.ecf.driver.ECFElginX5;
import org.freedom.ecf.driver.STResult;

public class ECFElginX5Test extends TestCase {

    private static final String PORTA = "COM4";

    public ECFElginX5Test(String name) {
        super(name);
    }

    public void testGenerico() {
        ECFElginX5 ecf = new ECFElginX5(PORTA);
        trataresultFuncao(ecf.executa("DefineMeioPagamento", "NomeMeioPagamento=\"DINHEIRO\"", "PermiteVinculado=true"));
        trataresultFuncao(ecf.executa("DefineMeioPagamento", "NomeMeioPagamento=\"CHEQUE\"", "PermiteVinculado=true"));
        trataresultFuncao(ecf.executa("DefineMeioPagamento", "NomeMeioPagamento=\"CARTAO\"", "PermiteVinculado=true"));
        ecf.leituraX();
    }

    public void testCancelaCupom() {
        ECFElginX5 ecf = new ECFElginX5(PORTA);
        assertTrue(trataresultFuncao(ecf.cancelaCupom()));
    }

    public void testComandosDeCupomFiscal() {
        ECFElginX5 ecf = new ECFElginX5(PORTA);
        System.out.print("aberturaDeCupom > ");
        assertTrue(trataresultFuncao(ecf.aberturaDeCupom()));
        System.out.print("vendaItem > ");
        assertTrue(trataresultFuncao(ecf.vendaItem("000001", "Produto Teste", "FF", 'I', 1f, 10f, 'D', 0f)));
        System.out.print("efetuaFormaPagamento Dinheiro > ");
        String indice = ecf.programaFormaPagamento("Cheque");
        assertTrue(trataresultFuncao(ecf.efetuaFormaPagamento(indice, 10.00f, "")));
        System.out.print("finalizaFechamentoCupom > ");
        assertTrue(trataresultFuncao(ecf.finalizaFechamentoCupom("Obrigado e volte sempre pra testar!")));
    }

    public void testComandosDeOperacoesNaoFiscais() {
        ECFElginX5 ecf = new ECFElginX5(PORTA);
        testComandosDeCupomFiscal();
        System.out.print("abreComprovanteNFiscalVinculado > ");
        assertTrue(trataresultFuncao(ecf.abreComprovanteNFiscalVinculado("Cheque", 10f, Integer.parseInt(ecf.resultNumeroCupom()))));
        System.out.print("usaComprovanteNFiscalVinculado > ");
        assertTrue(trataresultFuncao(ecf.usaComprovanteNFiscalVinculado("Usando o Comprovante Nao fiscal Vinculado")));
        System.out.print("fechamentoRelatorioGerencial > ");
        assertTrue(trataresultFuncao(ecf.fechamentoRelatorioGerencial()));
    }

    public void testComandosDeRelatoriosFiscais() {
        ECFElginX5 ecf = new ECFElginX5(PORTA);
        System.out.print("leituraX > ");
        assertTrue(trataresultFuncao(ecf.leituraX()));
    }

    public void testComandosDeInformacoesDaImpressora() {
        ECFElginX5 ecf = new ECFElginX5(PORTA);
        System.out.print("result da ultima redu��o Z > ");
        System.out.println(ecf.resultUltimaReducao());
    }

    public boolean trataresultFuncao(final STResult result) {
        boolean returnOfAction = !result.isInError();
        System.out.println(result.getMessages());
        return returnOfAction;
    }
}
