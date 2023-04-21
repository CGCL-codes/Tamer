package br.uff.javaavancado.controller;

import br.uff.javaavancado.controletransacao.FabricaDeAppService;
import br.uff.javaavancado.exception.AplicacaoException;
import br.uff.javaavancado.modelos.Categoria;
import br.uff.javaavancado.service.CategoriaService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.ListDataModel;

/**
 *
 * @author proac
 */
public class CategoriaController extends BaseController {

    private ListDataModel tabelaCategorias;

    private Categoria categoriaCorrente;

    private CategoriaService categoriaService;

    public CategoriaController() {
        try {
            categoriaService = FabricaDeAppService.getAppService(CategoriaService.class);
        } catch (Exception ex) {
            Logger.getLogger(CategoriaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String novoCategoria() {
        categoriaCorrente = new Categoria();
        return "editarCategoria";
    }

    public String visualizarCategoria() {
        categoriaCorrente = (Categoria) tabelaCategorias.getRowData();
        return "editarCategoria";
    }

    public String salvarCategoria() {
        if (categoriaCorrente.getId() == null) {
            categoriaService.salvar(categoriaCorrente);
        } else {
            categoriaService.atualizar(categoriaCorrente);
        }
        return "controleCategoria";
    }

    public String deletarCategoria() {
        categoriaCorrente = (Categoria) tabelaCategorias.getRowData();
        try {
            categoriaService.excluir(categoriaCorrente);
        } catch (AplicacaoException e) {
            this.error(getMensagemBundled("erroDeletarCategoria"));
        }
        return null;
    }

    public Categoria getCategoriaCorrente() {
        return categoriaCorrente;
    }

    public void setCategoriaCorrente(Categoria categoriaCorrente) {
        this.categoriaCorrente = categoriaCorrente;
    }

    public CategoriaService getCategoriaService() {
        return categoriaService;
    }

    public void setCategoriaService(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public ListDataModel getTabelaCategorias() {
        List categorias = categoriaService.getListaCompleta();
        tabelaCategorias = new ListDataModel(categorias);
        return tabelaCategorias;
    }

    public void setTabelaCategorias(ListDataModel tabelaCategorias) {
        this.tabelaCategorias = tabelaCategorias;
    }
}
