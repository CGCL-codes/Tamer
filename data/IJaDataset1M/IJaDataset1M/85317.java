package br.com.dip.controller.cadastro;

import java.util.ArrayList;
import java.util.List;
import br.com.dip.controller.listagem.ListagemSubMenuController;
import br.com.dip.controller.listagem.ListagemTipoUsuarioController;
import br.com.dip.entidade.SubMenu;
import br.com.dip.entidade.TipoUsuario;
import br.com.dip.excecoes.ApplicationException;
import br.com.dip.excecoes.ValidadorException;
import br.com.dip.gerentes.GerenteCadastro;
import br.com.dip.ouvinte.OuvinteSelecao;
import br.com.dip.inicializador.InicializadorTipoUsuario;

public class CadastroTipoUsuarioController extends CadastroPadraoController {

    private TipoUsuario tipoUsuario;

    private SubMenu subMenu;

    private List<TipoUsuario> lista;

    private Boolean abrirListagemSubMenu;

    private Boolean abrirListagemTipoUsuario;

    public TipoUsuario getTipoUsuario() {
        if (tipoUsuario == null) {
            tipoUsuario = new TipoUsuario();
        }
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void addSubMenu() {
        if (getSubMenu().getId() == null) {
            setErroMessage("Precisa informar um SubMenu v�lido antes de tentar adicion�-lo.");
        } else {
            getTipoUsuario().getListaAcesso().add(getSubMenu());
            setSubMenu(null);
        }
    }

    public void removerSubmenu() {
        getTipoUsuario().getListaAcesso().remove(getSubMenu());
        setSubMenu(null);
    }

    public void mostrarListagemTipoUsuario() {
        ListagemTipoUsuarioController listagem = new ListagemTipoUsuarioController();
        TipoUsuario query = new TipoUsuario();
        query.setDescricao(getTipoUsuario().getDescricao());
        listagem.setQuery(query);
        listagem.setOuvinteSelecao(new OuvinteSelecao() {

            public void selecaoFeita(Object object) {
                InicializadorTipoUsuario inicializador = new InicializadorTipoUsuario();
                setTipoUsuario((TipoUsuario) inicializador.inicializar(object));
            }
        });
        setAbrirListagemTipoUsuario(Boolean.TRUE);
    }

    public void mostrarListagemSubMenu() {
        ListagemSubMenuController listagem = new ListagemSubMenuController();
        SubMenu query = new SubMenu();
        query.setDescricao(getSubMenu().getDescricao());
        listagem.setQuery(query);
        listagem.setOuvinteSelecao(new OuvinteSelecao() {

            public void selecaoFeita(Object object) {
                setSubMenu((SubMenu) object);
            }
        });
        setAbrirListagemSubMenu(Boolean.TRUE);
    }

    public void entidadeSelecionada() {
        InicializadorTipoUsuario inicializador = new InicializadorTipoUsuario();
        setTipoUsuario((TipoUsuario) inicializador.inicializar(getTipoUsuario()));
    }

    @Override
    public void acaoAlterar() {
        try {
            GerenteCadastro gc = getFabricaGerentes().getGerenteCadastro();
            gc.alterar(getTipoUsuario());
        } catch (ApplicationException e) {
            setErroMessage(e.getMessage());
            return;
        } catch (ValidadorException e) {
            setErroMessage(e.getMessage());
            return;
        }
        setInfoMessage("Dados alterados com sucesso");
    }

    @Override
    public void acaoDeletar() {
        try {
            GerenteCadastro gc = getFabricaGerentes().getGerenteCadastro();
            gc.deletar(getTipoUsuario());
            setLista(null);
        } catch (ApplicationException e) {
            setErroMessage(e.getMessage());
            return;
        }
        setInfoMessage("Dados deletados com sucesso");
    }

    @Override
    public void acaoInserir() {
        try {
            GerenteCadastro gc = getFabricaGerentes().getGerenteCadastro();
            gc.incluir(getTipoUsuario());
            setLista(null);
        } catch (ApplicationException e) {
            setErroMessage(e.getMessage());
            return;
        } catch (ValidadorException e) {
            setErroMessage(e.getMessage());
            return;
        }
        setInfoMessage("Dados cadastrados com sucesso");
    }

    @Override
    public void acaoLimpar() {
        setTipoUsuario(null);
    }

    public List<TipoUsuario> getLista() {
        GerenteCadastro gc = getFabricaGerentes().getGerenteCadastro();
        try {
            lista = gc.obterListaEntidade(TipoUsuario.class, new TipoUsuario(), "descricao");
        } catch (ApplicationException e) {
            setErroMessage(e.getMessage());
            return new ArrayList<TipoUsuario>();
        }
        return lista;
    }

    public void setLista(List<TipoUsuario> lista) {
        this.lista = lista;
    }

    public SubMenu getSubMenu() {
        if (subMenu == null) {
            subMenu = new SubMenu();
        }
        return subMenu;
    }

    public void setSubMenu(SubMenu subMenu) {
        this.subMenu = subMenu;
    }

    public Boolean getAbrirListagemSubMenu() {
        Boolean value = abrirListagemSubMenu;
        setAbrirListagemSubMenu(Boolean.FALSE);
        return value;
    }

    public void setAbrirListagemSubMenu(Boolean abrirListagemSubMenu) {
        this.abrirListagemSubMenu = abrirListagemSubMenu;
    }

    public Boolean getAbrirListagemTipoUsuario() {
        Boolean value = abrirListagemTipoUsuario;
        setAbrirListagemTipoUsuario(Boolean.FALSE);
        return value;
    }

    public void setAbrirListagemTipoUsuario(Boolean abrirListagemTipoUsuario) {
        this.abrirListagemTipoUsuario = abrirListagemTipoUsuario;
    }
}
