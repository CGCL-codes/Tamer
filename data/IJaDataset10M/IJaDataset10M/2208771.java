package teste;

import java.util.ArrayList;
import java.util.HashMap;
import beans.Acoes;
import beans.Companhias;
import parsers.TitulosNegociaveis;

/**
 *
 * @author Daniel
 */
public class IniciaParseTitulosNegociaveis {

    public static void main(String[] args) {
        TitulosNegociaveis titulosNegociaveis = new TitulosNegociaveis();
        titulosNegociaveis.ObtemArquivoTxt();
        HashMap<String, Companhias> listaDeCompanhias = new HashMap<String, Companhias>();
        HashMap<String, ArrayList<Acoes>> hashListaDeAcoes = new HashMap<String, ArrayList<Acoes>>();
        titulosNegociaveis.ParseTitulosNegociaveis(listaDeCompanhias, hashListaDeAcoes);
        titulosNegociaveis.insereCompanhiasNoBanco(listaDeCompanhias);
    }
}
