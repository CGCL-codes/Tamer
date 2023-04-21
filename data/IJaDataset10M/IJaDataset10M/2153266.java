package br.com.napoleao.controlfin.form;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import br.com.napoleao.controlfin.core.entity.CategoriaEntity;
import br.com.napoleao.controlfin.core.persistence.dao.CategoriaDAO;
import br.com.napoleao.controlfin.util.GUIUtil;
import com.swtdesigner.SWTResourceManager;

public class FiltroControleCategoriaDialog extends Dialog {

    protected Object result;

    protected Shell shell;

    private Table tblCat;

    private Button buttonTodosSelected;

    private List<CategoriaEntity> categorias;

    private List<CategoriaEntity> categoriasSelecionadas;

    /**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
    public FiltroControleCategoriaDialog(Shell parent, int style) {
        super(parent, style);
        setText("Filtro de Categoria");
    }

    /**
	 * Open the dialog.
	 * @return the result
	 */
    public Object open() {
        createContents();
        GUIUtil.centralizeShell(shell, shell.getParent().getShell());
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
	 * Create contents of the dialog.
	 */
    private void createContents() {
        shell = new Shell(getParent(), getStyle());
        shell.setSize(300, 320);
        shell.setText(getText());
        shell.setLayout(new FormLayout());
        Group grpCategoria = new Group(shell, SWT.NONE);
        grpCategoria.setText("Categoria");
        grpCategoria.setLayout(null);
        FormData fd_grpCategoria = new FormData();
        fd_grpCategoria.bottom = new FormAttachment(0, 244);
        fd_grpCategoria.top = new FormAttachment(0, 10);
        fd_grpCategoria.left = new FormAttachment(0, 10);
        fd_grpCategoria.right = new FormAttachment(0, 284);
        grpCategoria.setLayoutData(fd_grpCategoria);
        buttonTodosSelected = new Button(grpCategoria, SWT.CHECK);
        buttonTodosSelected.setLocation(19, 26);
        buttonTodosSelected.setSize(13, 16);
        buttonTodosSelected.moveAbove(tblCat);
        buttonTodosSelected.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem[] items = tblCat.getItems();
                boolean selection = buttonTodosSelected.getSelection();
                for (TableItem item : items) {
                    item.setChecked(selection);
                }
            }
        });
        tblCat = new Table(grpCategoria, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
        tblCat.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                verificarStatusBotaoTodos();
            }
        });
        tblCat.setBounds(10, 20, 254, 204);
        tblCat.setHeaderVisible(true);
        tblCat.setLinesVisible(true);
        TableColumn tableColumn = new TableColumn(tblCat, SWT.NONE);
        tableColumn.setWidth(26);
        TableColumn tblclmnNome = new TableColumn(tblCat, SWT.NONE);
        tblclmnNome.setWidth(203);
        tblclmnNome.setText("Nome");
        Button btnAplicar = new Button(shell, SWT.NONE);
        btnAplicar.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                categoriasSelecionadas = new ArrayList<CategoriaEntity>();
                for (int i = 0; i < tblCat.getItems().length; i++) {
                    if (tblCat.getItems()[i].getChecked()) {
                        categoriasSelecionadas.add(categorias.get(i));
                    }
                }
                shell.dispose();
            }
        });
        btnAplicar.setImage(SWTResourceManager.getImage(FiltroControleCategoriaDialog.class, "/br/com/napoleao/controlfin/form/resources/aplicar.png"));
        FormData fd_btnAplicar = new FormData();
        fd_btnAplicar.bottom = new FormAttachment(100, -10);
        fd_btnAplicar.right = new FormAttachment(100, -10);
        btnAplicar.setLayoutData(fd_btnAplicar);
        btnAplicar.setText("Aplicar");
        popularTabela();
    }

    public void popularTabela() {
        tblCat.removeAll();
        categorias = new CategoriaDAO().listAll(CategoriaEntity.class, "nome", "asc");
        for (int i = 0; i < categorias.size(); i++) {
            TableItem ti = new TableItem(tblCat, SWT.NONE);
            ti.setText(1, categorias.get(i).getNome());
            for (int x = 0; x < categoriasSelecionadas.size(); x++) {
                if (categorias.get(i).equals(categoriasSelecionadas.get(x))) {
                    ti.setChecked(true);
                    break;
                }
            }
        }
        verificarStatusBotaoTodos();
    }

    public void verificarStatusBotaoTodos() {
        TableItem[] items = tblCat.getItems();
        boolean selecionar = true;
        for (TableItem item : items) {
            if (!item.getChecked()) {
                selecionar = false;
                break;
            }
        }
        buttonTodosSelected.setSelection(selecionar);
    }

    public List<CategoriaEntity> getCategoriasSelecionadas() {
        return categoriasSelecionadas;
    }

    public void setCategoriasSelecionadas(List<CategoriaEntity> categoriasSelecionadas) {
        this.categoriasSelecionadas = categoriasSelecionadas;
    }
}
