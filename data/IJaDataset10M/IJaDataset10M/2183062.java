package persistencia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import dominio.TipoLocacao;

/**Classe que faz a persistencia dos tipos de locacao, suas buscas, verificações e gerações de arraylists
 *
 * @author thiago
 */
public class PersistenciaTipoLocacao {

    public static ArrayList<TipoLocacao> tipoLocacaoList = new ArrayList<TipoLocacao>();

    public static void gravarobjeto(TipoLocacao tipolocacao) {
        tipoLocacaoList.add(tipolocacao);
    }

    public static void escreverArqTipoLocacao() throws Exception {
        File arqTipoLocacao = new File("arqTipoLocacao.txt");
        FileWriter escritor = new FileWriter(arqTipoLocacao);
        PrintWriter escrever = new PrintWriter(escritor);
        if (tipoLocacaoList.size() >= 0) {
            for (int i = 0; i < tipoLocacaoList.size(); i++) {
                escrever.println();
                escrever.println(tipoLocacaoList.get(i).getTipo());
                escrever.println(tipoLocacaoList.get(i).getTaxaBase());
                escrever.println(tipoLocacaoList.get(i).getPrecoKM());
                escrever.println(tipoLocacaoList.get(i).getIdTipoLocacao());
            }
            escrever.close();
            escritor.close();
        }
    }

    public static void lerArqTipoLocacao() throws Exception {
        String tipo;
        Double taxaBase, precoKM;
        int numero;
        File arquivo = new File("arqTipoLocacao.txt");
        if (arquivo.exists()) {
            File arqTipoLocacao = new File("arqTipoLocacao.txt");
            FileReader filereader = new FileReader(arqTipoLocacao);
            BufferedReader leitor = new BufferedReader(filereader);
            String linha = null;
            while ((linha = leitor.readLine()) != null) {
                tipo = (leitor.readLine());
                taxaBase = (Double.parseDouble(leitor.readLine()));
                precoKM = (Double.parseDouble(leitor.readLine()));
                numero = (Integer.parseInt(leitor.readLine()));
                TipoLocacao v = new TipoLocacao(tipo, taxaBase, numero);
                v.setPrecoKM(precoKM);
                tipoLocacaoList.add(v);
            }
        }
    }

    public static void imprimirListaTipo() {
        for (int i = 0; i < PersistenciaTipoLocacao.tipoLocacaoList.size(); i++) {
            System.out.println("Tipo: " + PersistenciaTipoLocacao.tipoLocacaoList.get(i).getTipo());
            System.out.println("Taxa Base: " + PersistenciaTipoLocacao.tipoLocacaoList.get(i).getTaxaBase());
            System.out.println("Preço por KM: " + PersistenciaTipoLocacao.tipoLocacaoList.get(i).getPrecoKM());
            System.out.println("ID: " + PersistenciaTipoLocacao.tipoLocacaoList.get(i).getIdTipoLocacao());
            System.out.println("");
        }
    }

    public static String[] listaTipo() {
        imprimirListaTipo();
        String[] vetor = new String[PersistenciaTipoLocacao.tipoLocacaoList.size()];
        for (int i = 0; i < PersistenciaTipoLocacao.tipoLocacaoList.size(); i++) {
            vetor[i] = ("Tipo: " + PersistenciaTipoLocacao.tipoLocacaoList.get(i).getTipo());
        }
        return vetor;
    }

    public static boolean existeTipo(Integer id) {
        boolean resposta = false;
        for (int i = 0; i < PersistenciaTipoLocacao.tipoLocacaoList.size(); i++) {
            if (PersistenciaTipoLocacao.tipoLocacaoList.get(i).getIdTipoLocacao() == id) {
                resposta = true;
            }
        }
        return resposta;
    }

    public static Integer geraIDTipoLocacao() {
        Integer maior = -1;
        for (int i = 0; i < PersistenciaTipoLocacao.tipoLocacaoList.size(); i++) {
            if (PersistenciaTipoLocacao.tipoLocacaoList.get(i).getIdTipoLocacao() > maior) {
                maior = PersistenciaTipoLocacao.tipoLocacaoList.get(i).getIdTipoLocacao();
            }
        }
        return maior + 1;
    }

    public static Integer retornaPosicao(Integer Id) {
        Integer posicao = 0;
        for (int i = 0; i < PersistenciaTipoLocacao.tipoLocacaoList.size(); i++) {
            if (PersistenciaTipoLocacao.tipoLocacaoList.get(i).getIdTipoLocacao() == Id) {
                posicao = i;
            }
        }
        return posicao;
    }

    public static String[] stringListaTipoLocacao() {
        String[] vetor = new String[PersistenciaTipoLocacao.tipoLocacaoList.size() + 1];
        for (int i = 0; i < PersistenciaTipoLocacao.tipoLocacaoList.size(); i++) {
            if (PersistenciaTipoLocacao.tipoLocacaoList.size() == 0) {
                vetor[0] = ("Cadastre um tipo de locação");
            } else {
                if (i == 0) {
                    vetor[i] = ("Selecione");
                } else {
                    vetor[i] = (PersistenciaTipoLocacao.tipoLocacaoList.get(i).getTipo());
                }
            }
        }
        return vetor;
    }
}
