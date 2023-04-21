package Sistema.Compilador;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import Nucleo.Excecao.ArquivoNaoEncontrado;
import Nucleo.Excecao.ErroAplicacao;
import Nucleo.Excecao.ErroCompilacao;
import Nucleo.Excecao.ErroEntradaSaida;
import Nucleo.Excecao.ErroInternoCompilacao;
import Nucleo.Excecao.MetodoNativoIndefinido;

class LinhaNativo {

    String conteudo;

    int posic;

    int tamanho;

    public LinhaNativo(String pConteudo) {
        atribui(pConteudo);
    }

    public void atribui(String pConteudo) {
        conteudo = pConteudo;
        posic = 0;
        if (conteudo != null) tamanho = conteudo.length(); else tamanho = 0;
    }

    public void proxCaractere() {
        if (posic < tamanho) posic++;
    }

    void pulaEspacos() {
        while ((posic < tamanho) && (conteudo.charAt(posic) == ' ')) posic++;
    }

    String palavra(char delimitador) {
        StringBuffer aux = new StringBuffer("");
        while ((posic < tamanho) && (conteudo.charAt(posic) != delimitador)) {
            aux.append(conteudo.charAt(posic));
            posic++;
        }
        return aux.toString();
    }
}

public class Nativo {

    Hashtable tabelaNativos;

    public Nativo(String idArquivo) throws ErroAplicacao {
        int j = 0;
        boolean fimArquivo = false;
        String serieTexto;
        InputStream arquivo = null;
        DataInputStream arqFormatado;
        LinhaNativo linha = null;
        String auxLinha = null;
        String auxClasse, auxMetodo, chave;
        Integer auxValor;
        tabelaNativos = new Hashtable();
        arquivo = abreArquivo(idArquivo);
        arqFormatado = new DataInputStream(arquivo);
        try {
            linha = new LinhaNativo(arqFormatado.readLine());
            if (linha == null) fimArquivo = true;
        } catch (IOException erro) {
            System.out.println("Erro na leitura");
        }
        while (!fimArquivo) {
            linha.pulaEspacos();
            auxClasse = linha.palavra('.');
            linha.proxCaractere();
            auxMetodo = linha.palavra(' ');
            linha.pulaEspacos();
            serieTexto = linha.palavra(' ');
            auxValor = new Integer(serieTexto);
            chave = new String(auxClasse + "." + auxMetodo);
            tabelaNativos.put(chave, auxValor);
            try {
                auxLinha = arqFormatado.readLine();
                if (auxLinha != null) {
                    linha.atribui(auxLinha);
                    j++;
                } else fimArquivo = true;
            } catch (IOException erro) {
                System.out.println("Erro na leitura");
            }
        }
    }

    InputStream abreArquivo(String idArquivo) throws ErroEntradaSaida {
        InputStream auxArq = Nativo.class.getResourceAsStream(idArquivo);
        if (auxArq == null) throw new ArquivoNaoEncontrado("Arquivo de Nativos n�o encontrado");
        return auxArq;
    }

    public int indiceNativo(String classe, String metodo) throws ErroCompilacao {
        String chave;
        Integer auxIndice;
        if (classe == null) throw new ErroInternoCompilacao("refer�ncia de classe nula"); else if (metodo == null) throw new ErroInternoCompilacao("refer�ncia de metodo nula");
        chave = new String(classe + "." + metodo);
        auxIndice = (Integer) tabelaNativos.get(chave);
        if (auxIndice == null) throw new MetodoNativoIndefinido("metodo nativo n�o possui refer�ncia interna");
        return auxIndice.intValue();
    }
}
