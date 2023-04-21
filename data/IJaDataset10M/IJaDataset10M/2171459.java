package br.org.databasetools.datamanager;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import br.org.databasetools.core.action.TAbstractAction;
import br.org.databasetools.core.connection.Transaction;
import br.org.databasetools.core.connection.TransactionManager;
import br.org.databasetools.core.exception.NotFoundException;
import br.org.databasetools.core.images.ErrorManager;
import br.org.databasetools.core.images.ImageManager;
import br.org.databasetools.core.images.WindowManager;
import br.org.databasetools.core.table.Bean;
import br.org.databasetools.core.table.Table;
import br.org.databasetools.core.table.TableField;
import br.org.databasetools.core.table.TableFieldMask;
import br.org.databasetools.core.table.TableFilter;
import br.org.databasetools.core.table.TableView;
import br.org.databasetools.core.view.fields.TAbstractField;
import br.org.databasetools.core.view.fields.TComboBoxField;
import br.org.databasetools.core.view.window.ManagerWindow;

/**
 * Classe Exemplo para funcionamento de um gerenciador de qualquer view do usuário.
 * <br> Usarei o esquema para gerenciamento de uma tabela de usuários.
 * <br> OBS.: não é um CRUD (ainda hehe) as funções tem q ser implementadas separadamente.
 * 
 * @author Felipe
 *
 */
public class DataManagerWindow extends ManagerWindow {

    private static final long serialVersionUID = 1L;

    private static Log LOG = LogFactory.getLog(DataManagerWindow.class);

    private Transaction transaction;

    private DataManagerWindowController controller;

    public DataManagerWindow() throws Exception {
        super("Gerenciador de Dados");
        transaction = TransactionManager.getInstance().getTransaction(3);
        controller = new DataManagerWindowController(this, transaction);
        setMasterView(new DataTableView(getTransaction()));
        registrarFiltros();
        registrarActions();
        createGUI();
        LOG.info("Executando Gerenciador de Dados");
    }

    @Override
    public void find() {
        List<TableFilter> filters = getFilters();
        try {
            WindowManager.getInstance().showWaitWindow(this, "Carregando dados, aguarde...");
            if (filters != null && filters.size() > 0) {
                getListView().setRows(getMasterView().open(filters));
            } else {
                getListView().setRows(getMasterView().open());
            }
        } catch (NotFoundException e) {
            getListView().setRows(new ArrayList<ContaPagarReceberBean>(0));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            WindowManager.getInstance().showMessageError(this, e.getMessage());
        } finally {
            WindowManager.getInstance().hideWaitWindow(this);
        }
    }

    @Override
    public Transaction getTransaction() throws Exception {
        return transaction;
    }

    private void registrarActions() {
        try {
            addAction("Inserir", null, "Inserir registro", ImageManager.INSERT, new InserirAction(), true);
            addAction("Editar", null, "Editar registro", ImageManager.EDIT, new EditarAction(), true);
            addAction("Excluir", null, "Excluir registro", ImageManager.DELETE, new ExcluirAction(), true);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void registrarFiltros() {
        TComboBoxField cbSituacao = new TComboBoxField(new Integer[] { 0, 1 }, new String[] { "Pendentes", "Solucionadas" }, getController(), false, true);
        addFilter("ContaSituacao", "Situação", cbSituacao, TableFilter.getConditionEqualInteger());
    }

    private class DataTableView extends TableView {

        /**
		 * Onde busco os campos, e os mostro na tela do gerenciador
		 * <br> OBS.: ó a query lá em baixo!!!!  =D
		 */
        public DataTableView(Transaction transaction) {
            setOpened(false);
            setTransaction(transaction);
            List<TableField> lstFields = getTable().getFields();
            List<TableFieldMask> lstFieldMask = getFieldsMask();
            TableField f = new TableField("ContaId", "conta_id", TableField.INTEGER, 11, 0);
            TableFieldMask fm = new TableFieldMask(f, "Conta", "Conta", TAbstractField.INTEGERFIELD);
            fm.setShowInList(true);
            fm.setVisible(true);
            lstFields.add(f);
            lstFieldMask.add(fm);
            f = new TableField("ContaTipo", "conta_tipo", TableField.INTEGER, 11, 0);
            fm = new TableFieldMask(f, "Tipo Conta", "Conta Tipo", TAbstractField.INTEGERFIELD);
            fm.setShowInList(true);
            fm.setVisible(true);
            lstFields.add(f);
            lstFieldMask.add(fm);
            f = new TableField("ContaValor", "conta_valor", TableField.DECIMAL, 11, 2);
            fm = new TableFieldMask(f, "Valor origem", "Valor origem", TAbstractField.DECIMALFIELD);
            fm.setShowInList(true);
            fm.setVisible(true);
            lstFields.add(f);
            lstFieldMask.add(fm);
            f = new TableField("ContaNome", "conta_nome", TableField.STRING, 30, 0);
            fm = new TableFieldMask(f, "Nome", "Nome", TAbstractField.TEXTFIELD);
            fm.setShowInList(true);
            fm.setVisible(true);
            lstFields.add(f);
            lstFieldMask.add(fm);
            f = new TableField("ContaDataRegistro", "conta_dataregistro", TableField.DATE, 30, 0);
            fm = new TableFieldMask(f, "Data Registro", "Data Registro", TAbstractField.DATEFIELD);
            fm.setShowInList(true);
            fm.setVisible(true);
            lstFields.add(f);
            lstFieldMask.add(fm);
            f = new TableField("ContaDataPagamento", "conta_datapagamento", TableField.DATE, 30, 0);
            fm = new TableFieldMask(f, "Data Pagamento", "Data Pagamento", TAbstractField.DATEFIELD);
            fm.setShowInList(true);
            fm.setVisible(true);
            lstFields.add(f);
            lstFieldMask.add(fm);
            f = new TableField("ContaSituacao", "conta_situacao", TableField.STRING, 15, 0);
            fm = new TableFieldMask(f, "situação", "Situação", TAbstractField.TEXTFIELD);
            fm.setShowInList(true);
            fm.setVisible(true);
            lstFields.add(f);
            lstFieldMask.add(fm);
            StringBuilder sb = new StringBuilder();
            sb.append(" select  conta_id,conta_tipo,conta_nome,conta_valor, 						 ");
            sb.append("			date_format(conta_dataregistro, '%d/%m/%Y') as conta_dataregistro,   ");
            sb.append("			date_format(conta_datapagamento, '%d/%m/%Y') as conta_datapagamento, ");
            sb.append("			conta_situacao ");
            sb.append("   from  comercio.contas 	/* WHERE */										 ");
            getTable().setQuery(sb.toString());
        }

        @Override
        public Table getTable() {
            if (this.table == null) {
                this.table = new Table() {

                    @Override
                    public Bean create(boolean sequence) {
                        return new ContaPagarReceberBean();
                    }
                };
            }
            return this.table;
        }
    }

    /**=================================================
	 * 				Actions implementadas
	 ===================================================*/
    private class InserirAction extends TAbstractAction {

        private static final long serialVersionUID = 1L;

        @Override
        public void execute() {
            try {
                ContaPagarReceberBean bean = new ContaPagarReceberBean();
                bean.setContaTipo(1);
                bean.setContaNome("Datena!");
                bean.setContaValor(400d);
                int row = controller.inserirRegistroDAO(bean);
                if (row != 0) WindowManager.getInstance().showMessage(DataManagerWindow.this, "Operação Concluída!"); else WindowManager.getInstance().showMessage(DataManagerWindow.this, "Operação Falhou!");
                DataManagerWindow.this.find();
            } catch (Exception ex) {
                WindowManager.getInstance().showMessageError(DataManagerWindow.this, "Não foi possível concluir a operação!");
                ErrorManager.getInstance().show(DataManagerWindow.this, new Exception("Ocorreu um erro durante a operação!", ex));
            }
        }
    }

    private class EditarAction extends TAbstractAction {

        private static final long serialVersionUID = 1L;

        @Override
        public void execute() {
            try {
                if (listView.getRow() != null) {
                    ContaPagarReceberBean bean = (ContaPagarReceberBean) listView.getRow();
                    bean.setContaNome("Simone");
                    bean.setContaValor(200d);
                    int row = controller.editarRegistroDAO(bean);
                    if (row != 0) WindowManager.getInstance().showMessage(DataManagerWindow.this, "Operação Concluída!"); else WindowManager.getInstance().showMessage(DataManagerWindow.this, "Operação Falhou!");
                    DataManagerWindow.this.find();
                } else {
                    WindowManager.getInstance().showMessage(DataManagerWindow.this, "Selecione um registro!");
                }
            } catch (Exception ex) {
                WindowManager.getInstance().showMessageError(DataManagerWindow.this, "Não foi possível concluir a operação!");
                ErrorManager.getInstance().show(DataManagerWindow.this, new Exception("Ocorreu um erro durante a operação!", ex));
            }
        }
    }

    private class ExcluirAction extends TAbstractAction {

        private static final long serialVersionUID = 1L;

        @Override
        public void execute() {
            try {
                if (listView.getRow() != null) {
                    if (WindowManager.getInstance().showQuestionYesNo(DataManagerWindow.this, "Deseja realmente excluir o registro ?")) {
                        ContaPagarReceberBean bean = (ContaPagarReceberBean) listView.getRow();
                        int row = controller.deletarRegistroDAO(bean);
                        if (row != 0) WindowManager.getInstance().showMessage(DataManagerWindow.this, "Operação Concluída!"); else WindowManager.getInstance().showMessage(DataManagerWindow.this, "Operação Falhou!");
                        DataManagerWindow.this.find();
                    }
                } else {
                    WindowManager.getInstance().showMessage(DataManagerWindow.this, "Selecione um registro!");
                }
            } catch (Exception ex) {
                WindowManager.getInstance().showMessageError(DataManagerWindow.this, "Não foi possível concluir a operação!");
                ErrorManager.getInstance().show(DataManagerWindow.this, new Exception("Ocorreu um erro durante a operação!", ex));
            }
        }
    }
}
