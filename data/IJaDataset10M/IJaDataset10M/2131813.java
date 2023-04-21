package Sistema.Ambiente;

public class GrupoFerramentas {

    int ultimo;

    IconeFerramenta ferramenta[];

    public GrupoFerramentas(int numero) {
        ferramenta = new IconeFerramenta[numero];
        ultimo = -1;
    }

    public void insereFerramenta(IconeFerramenta icone) {
        ultimo++;
        ferramenta[ultimo] = icone;
    }

    public void ferramentaSelecionada(IconeFerramenta icone) {
        for (int cont = 0; cont <= ultimo; cont++) if (ferramenta[cont] != icone) ferramenta[cont].desseleciona(); else ferramenta[cont].seleciona();
    }
}
