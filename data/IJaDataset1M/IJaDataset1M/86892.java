package org.alfredlibrary.formatadores;

import org.alfredlibrary.AlfredException;
import org.alfredlibrary.utilitarios.inscricaoestadual.InscricaoEstadual.PadraoInscricaoEstadual;
import org.alfredlibrary.utilitarios.texto.Texto;
import org.alfredlibrary.validadores.Numeros;

/**
 * Formatador de Inscrição Estadual.
 * 
 * @author Rodrigo Moreira Fagundes
 * @since 11/06/2010
 */
public final class InscricaoEstadual {

    private InscricaoEstadual() {
        throw new AssertionError();
    }

    /**
	 * Obter uma Inscrição Estadual qualquer e formatá-la. Qualquer caracter
	 * diferente de números será ignorado.
	 * 
	 * @param ie Número da Inscrição Estadual.
	 * @return Inscrição Estadual formatada.
	 */
    public static String formatar(PadraoInscricaoEstadual padrao, String ie) {
        String ieSoNumeros = limpar(padrao, ie);
        if (ieSoNumeros.length() != Texto.removerPontuacao(padrao.getFormato()).length()) {
            throw new AlfredException("Inscrição Estadual inválida. Tamanho de uma Inscrição Estadual válida para a UF informada é " + Texto.removerPontuacao(padrao.getFormato()).length() + ". Esta Inscrição Estadual possui " + ieSoNumeros.length() + " números.");
        }
        StringBuilder sb = new StringBuilder();
        int indicePadrao = 0;
        for (int i = 0; i < ieSoNumeros.length(); i++) {
            if (padrao.getFormato().charAt(indicePadrao) == 'P') {
                sb.append(padrao.getFormato().charAt(indicePadrao));
                indicePadrao++;
                i++;
            }
            if (padrao.getFormato().charAt(indicePadrao) == '.' || padrao.getFormato().charAt(indicePadrao) == '-' || padrao.getFormato().charAt(indicePadrao) == '/') {
                sb.append(padrao.getFormato().charAt(indicePadrao));
                indicePadrao++;
            }
            sb.append(ieSoNumeros.charAt(i));
            indicePadrao++;
        }
        return sb.toString();
    }

    /**
	 * Limpar a Inscrição Estadual, mantendo somente os números.
	 * Não verifica se é uma Inscrição Estaudal válida.
	 * @param padrao 
	 * 
	 * @param ie Inscrição Estadual que deve ser limpa.
	 * @return Inscrição Estadual apenas com números.
	 */
    public static String limpar(PadraoInscricaoEstadual padrao, String ie) {
        if (ie == null) throw new AlfredException("A Inscrição Estadual informada é nula.");
        if ("".equals(ie)) throw new AlfredException("A Inscrição Estadual informada é vazia.");
        char[] chars = ie.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int indice = 0; indice < chars.length; indice++) {
            if (Numeros.isInteger(String.valueOf(chars[indice]))) {
                sb.append(chars[indice]);
            } else if (padrao.equals(PadraoInscricaoEstadual.SAO_PAULO_PRODUTOR_RURAL) && chars[indice] == 'P') {
                sb.append(chars[indice]);
            }
        }
        return sb.toString();
    }
}
