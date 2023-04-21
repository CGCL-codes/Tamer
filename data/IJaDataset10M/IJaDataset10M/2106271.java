package br.unb.cic.gerval.client.produto;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import br.unb.cic.gerval.client.rpc.vo.Linha;
import br.unb.cic.gerval.client.usuario.Atualizavel;

public class ModeloLinhaProduto {

    private List linhas;

    private Atualizavel tela;

    public ModeloLinhaProduto(Atualizavel tela) {
        this.tela = tela;
    }

    public List getLinhas() {
        return linhas;
    }

    public void setLinhas(List linhas) {
        this.linhas = linhas;
        ordena();
        tela.atualizar();
    }

    public void atualiza(Linha linha) {
        int i = linhas.indexOf(linha);
        if (i > -1) {
            linhas.remove(i);
        }
        linhas.add(linha);
        ordena();
        tela.atualizar();
    }

    private void ordena() {
        Collections.sort(linhas, new Comparator() {

            public int compare(Object arg0, Object arg1) {
                Linha linha0 = (Linha) arg0;
                Linha linha1 = (Linha) arg1;
                return linha0.getNome().compareTo(linha1.getNome());
            }
        });
    }

    public void removerLinha(Integer i) {
        linhas.remove(i.intValue());
        tela.atualizar();
    }
}
