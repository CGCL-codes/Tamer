package Sistema.Ambiente;

import java.awt.Image;
import javax.swing.ImageIcon;
import Nucleo.Intermediario.GerenteArquivos;

public class ElementoDisponivel {

    public Image imagemBasica, imagemSelecionada;

    public ImageIcon iconeBasico, iconeSelecionado;

    public String nomeImagemBasica, nomeImagemSelecionada;

    public int xImagem, yImagem, larguraImagem, alturaImagem;

    public String nome;

    public int xTexto, yTexto;

    public boolean selecionado;

    public String anexo1;

    public String anexo2;

    public ElementoDisponivel() {
    }

    public ElementoDisponivel(String pNomeImagemBasica, String pNomeImagemSelecionada, int pXImagem, int pYImagem, String pNome, String pAnexo1, String pAnexo2) {
        nomeImagemBasica = pNomeImagemBasica;
        nomeImagemSelecionada = pNomeImagemSelecionada;
        iconeBasico = new ImageIcon(GerenteArquivos.converte(pNomeImagemBasica));
        iconeSelecionado = new ImageIcon(GerenteArquivos.converte(pNomeImagemSelecionada));
        xImagem = pXImagem;
        yImagem = pYImagem;
        nome = pNome;
        anexo1 = pAnexo1;
        anexo2 = pAnexo2;
    }

    public String toString() {
        return nome;
    }
}
