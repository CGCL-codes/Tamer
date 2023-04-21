package br.com.dip.controller.listagem;

import java.util.ArrayList;
import java.util.List;
import br.com.dip.entidade.Marca;
import br.com.dip.excecoes.ApplicationException;
import br.com.dip.gerentes.GerenteCadastro;

public class ListagemMarcaController extends ListagemPadraoController {

    private List<Marca> lista;

    public void entidadeSelecionada() {
        getOuvinteSelecao().selecaoFeita(getQuery());
        setFecharListagem(Boolean.TRUE);
    }

    public List<Marca> getLista() {
        if (lista == null) {
            GerenteCadastro gc = getFabricaGerentes().getGerenteCadastro();
            try {
                lista = gc.obterListaEntidade(Marca.class, getQuery(), "descricao", 20);
            } catch (ApplicationException e) {
                setErroMessage(e.getMessage());
                lista = new ArrayList<Marca>();
            }
        }
        return lista;
    }

    public void setLista(List<Marca> lista) {
        this.lista = lista;
    }

    @Override
    public void acaoPesquisar() {
        setLista(null);
    }

    @Override
    public String getNome() {
        return "listagemMarca";
    }
}
