package etp.client.gui;

import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import etp.client.Etp;
import etp.client.Projeto;
import etp.client.Tarefa;
import etp.client.TaskModel;
import etp.client.Logger.LoggerType;
import etp.client.gui.TarefaForm.FormTarefaHandler;
import etp.client.rpc.TarefaAdminService;
import etp.client.rpc.TarefaAdminServiceAsync;

public class EditorTarefas extends AbsolutePanel {

    /**
	 * Projeto selecionado. Todas as tarefas exibidas neste editor fazem parte
	 * deste projeto.
	 */
    private Projeto selectedProject;

    /**
	 * Modelo padr�o para exibi��o de tarefas.
	 */
    private RemoteTaskModel model;

    /**
	 * Painel principal.
	 */
    private HorizontalSplitPanel pnlMain = new HorizontalSplitPanel();

    /**
	 * Menu de op��es.
	 */
    private ETPMenu emnuMenu = new ETPMenu();

    /**
	 * Formul�rio para cadastro de tarefas.
	 */
    private TarefaForm frmNovaTarefa;

    /**
	 * Visualiza��o corrente
	 */
    private VisualizacaoTarefas viewCorrente;

    /**
	 * Objeto que cont�m a interface com o servido de aplica��o.
	 */
    private TarefaAdminServiceAsync tarefaService = GWT.create(TarefaAdminService.class);

    /**
	 * Constr�i um objeto para editar e visualizar tarefas.
	 */
    public EditorTarefas() {
        this.addStyleName("etp-EditorTarefas");
        this.pnlMain.setSplitPosition("12%");
        this.pnlMain.add(this.emnuMenu);
        this.add(this.pnlMain);
        this.setupMenu();
        this.model = new RemoteTaskModel(this.tarefaService);
        this.frmNovaTarefa = new TarefaForm(this.model);
        this.frmNovaTarefa.addCadastroHandler(new ActFormTarefaCadastro());
        this.setDefaultView();
    }

    /**
	 * Configura o menu da editor.
	 */
    protected void setupMenu() {
        this.emnuMenu.setHeight("100%");
        this.emnuMenu.setWidth("100%");
        this.emnuMenu.addMenuItem("Novo", new ActNovaTarefa());
        this.emnuMenu.addMenuItem("WBS", new ActShowWBS());
    }

    /**
	 * Retorna o objeto que gerencia as tarefas.
	 * 
	 * @return
	 */
    public TaskModel getModel() {
        return model;
    }

    /**
	 * Cadastra uma nova tarefa. Se n�o houver visualizador, o padr�o ser�
	 * selecionado. Caso nenhum projeto tenha sido selecionado retorna e 
	 * imprime uma mensagem de erro. 
	 * 
	 * @param t Tarefa a ser cadastrada.
	 */
    public void novaTarefa(Tarefa t) {
        if (this.selectedProject == null) {
            Etp.logger.show("Nenhum projeto selecionado.", LoggerType.ERROR);
            return;
        }
        t.setProjeto(this.selectedProject.getCod());
        Etp.logger.show("Cadastrando nova tarefa.", LoggerType.WAITING);
        this.model.cadastraTarefa(t, new AsyncCallback<Tarefa>() {

            @Override
            public void onFailure(Throwable caught) {
                Etp.logger.show(caught.getMessage(), LoggerType.ERROR);
            }

            @Override
            public void onSuccess(Tarefa result) {
                Etp.logger.show("Tarefa cadastrada com sucesso.", LoggerType.SUCCESS);
            }
        });
    }

    public void editarTarefa(Tarefa t) {
    }

    /**
	 * Carrega as tarefas do projeto corrente na visualiza��o selecionada.
	 */
    public void loadTarefas() {
        this.viewCorrente.clear();
        this.model.loadTasks(new AsyncCallback<List<Tarefa>>() {

            @Override
            public void onFailure(Throwable caught) {
                Etp.logger.show(caught.getMessage(), LoggerType.ERROR);
            }

            @Override
            public void onSuccess(List<Tarefa> result) {
            }
        });
    }

    /**
	 * Retorna o projeto ao qual todas as tarefas deste editor est�o associadas.
	 * 
	 * @return Projeto
	 */
    public Projeto getSelectedProject() {
        return selectedProject;
    }

    /**
	 * Seta o projeto para o qual este editor exibir� suas tarefas.
	 * 
	 * @param selectedProject Projeto.
	 */
    public void setSelectedProject(Projeto selectedProject) {
        this.selectedProject = selectedProject;
        this.model.setProjeto(selectedProject);
        this.model.loadTasks(new AsyncCallback<List<Tarefa>>() {

            @Override
            public void onFailure(Throwable caught) {
                Etp.logger.show(caught.getMessage(), LoggerType.ERROR);
            }

            @Override
            public void onSuccess(List<Tarefa> result) {
            }
        });
    }

    /**
	 * Retorna o objeto que � utilizado para exibir todas as tarefas.
	 * 
	 * @return Visualizador de tarefas.
	 */
    public VisualizacaoTarefas getViewCorrente() {
        return viewCorrente;
    }

    /**
	 * Configura um novo visualizador de tarefas.
	 * 
	 * @param viewCorrente Visualizador de tarefas.
	 */
    public void setViewCorrente(VisualizacaoTarefas viewCorrente) {
        if (this.viewCorrente != null) {
            this.pnlMain.remove(viewCorrente);
        }
        this.viewCorrente = viewCorrente;
        this.pnlMain.add(this.viewCorrente);
    }

    /**
	 * Configura o visuzalizador de tarefas padr�o.
	 */
    public void setDefaultView() {
        this.setViewCorrente(new TaskBoxes(this.model));
    }

    private class ActFormTarefaCadastro implements FormTarefaHandler {

        @Override
        public void cadastrarTarefa(Tarefa t) {
            EditorTarefas.this.novaTarefa(t);
            Etp.logger.setVisible(false);
        }

        @Override
        public void editarTarefa(Tarefa t) {
            Etp.logger.setVisible(false);
        }
    }

    protected class CallbackAddTarefa implements AsyncCallback<Tarefa> {

        @Override
        public void onFailure(Throwable caught) {
            Etp.logger.show(caught.getMessage(), LoggerType.ERROR);
        }

        @Override
        public void onSuccess(Tarefa result) {
            Etp.logger.show("Tarefa cadastrada com sucesso.", LoggerType.SUCCESS);
        }
    }

    protected class ActNovaTarefa implements ClickHandler {

        @Override
        public void onClick(ClickEvent event) {
            if (EditorTarefas.this.selectedProject == null) {
                Etp.logger.show("Nenhum projeto selecionado.", LoggerType.ERROR);
                return;
            }
            frmNovaTarefa.setModal(true);
            frmNovaTarefa.novo();
            frmNovaTarefa.center();
        }
    }

    protected class ActShowWBS implements ClickHandler {

        @Override
        public void onClick(ClickEvent event) {
        }
    }
}
