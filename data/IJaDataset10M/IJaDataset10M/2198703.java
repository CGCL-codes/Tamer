package br.fir.compiladores.compilador;

import java.util.ArrayList;

/**
 * @author David Ribeiro
 * 
 */
public class Compilador {

    private static Compilador aInstancia = null;

    public static Compilador getInstancia() {
        if (aInstancia == null) {
            aInstancia = new Compilador();
        }
        return aInstancia;
    }

    /**
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        ArrayList<String> vAlTokens = null;
        Sintatico sintatico = new Sintatico();
        vAlTokens = sintatico.validarEstrutura("E:\\FIR\\workspace\\Lua\\src\\fibo.lua");
        Semantico semantico = new Semantico();
        semantico.executarAcao(null, Constantes.CD_INICIO);
        Compilador.getInstancia().compila(vAlTokens, 0, semantico);
        semantico.executarAcao(null, Constantes.CD_FIM);
    }

    public void compila(ArrayList<String> vAlTokens, int pIndex, Semantico semantico) throws Exception {
        for (int i = pIndex; i < vAlTokens.size(); i++) {
            String vStrToken = vAlTokens.get(i);
            if (vStrToken.equals(Constantes.DS_ASSIGN)) {
                vStrToken = vAlTokens.get(i + 1);
                Token vToken = new Token(vStrToken, Constantes.CD_IL_INT32);
                semantico.executarAcao(vToken, Constantes.CD_ASSIGN);
                vToken.setExpresao(Funcoes.getExpressao(vAlTokens, i + 2));
                i = i + vToken.getExpresao().size();
                semantico.executarAcao(vToken, Constantes.CD_REALIZA_OPERACAO);
            } else if (vStrToken.equals(Constantes.DS_STM_PRINT)) {
                vStrToken = vAlTokens.get(i + 2);
                Token vToken = new Token(vStrToken);
                vToken.aIsAtribuicao = false;
                vToken.setExpresao(Funcoes.getExpressao(vAlTokens, i + 2));
                i = i + vToken.getExpresao().size();
                semantico.executarAcao(vToken, Constantes.CD_REALIZA_OPERACAO);
                semantico.executarAcao(vToken, Constantes.CD_STM_PRINT);
            } else if (vStrToken.equals(Constantes.DS_STM_IF)) {
                IFStatment vStatmentIF = Funcoes.getIFStatment(vAlTokens, i + 1);
                Token vToken = new Token(vStrToken);
                vToken.setIFStatment(vStatmentIF);
                i = i + vToken.getIFStatment().aStackExpressao.size() + vToken.getIFStatment().aStackBlocoVerdadeiro.size() + vToken.getIFStatment().aStackBlocoFalso.size();
                semantico.executarAcao(vToken, Constantes.CD_REALIZA_EXPRESSAO_IF);
            } else if (vStrToken.equals(Constantes.DS_STM_WHILE)) {
                WHILEStatment vStatmentWHILE = Funcoes.getWHILEStatment(vAlTokens, i + 1);
                Token vToken = new Token(vStrToken);
                vToken.setWHILEStatment(vStatmentWHILE);
                i = i + vToken.getWHILEStatment().aStackExpressao.size() + vToken.getWHILEStatment().aStackBloco.size();
                semantico.executarAcao(vToken, Constantes.CD_REALIZA_EXPRESSAO_WHILE);
            }
        }
    }

    /**
	 * 
	 */
    public Compilador() {
        super();
    }
}
