package br.com.promove.view.form;

import java.util.Iterator;
import br.com.promove.entity.Filial;
import br.com.promove.entity.TipoUsuario;
import br.com.promove.entity.Usuario;
import br.com.promove.exception.PromoveException;
import br.com.promove.service.CadastroService;
import br.com.promove.service.ServiceFactory;
import br.com.promove.utils.StringUtilities;
import br.com.promove.view.UsuarioView;
import com.vaadin.data.Item;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class UsuarioForm extends BaseForm {

    private Button save;

    private Button novo;

    private Button remove;

    private VerticalLayout form_layout = new VerticalLayout();

    private CadastroService cadastroService;

    private UsuarioView view;

    public UsuarioForm() {
        cadastroService = ServiceFactory.getService(CadastroService.class);
        buildForm();
    }

    public void setView(UsuarioView view) {
        this.view = view;
    }

    private void buildForm() {
        setWriteThrough(false);
        setImmediate(true);
        setSizeFull();
        save = new Button("Salvar", new UsuarioFormListener());
        remove = new Button("Excluir", new UsuarioFormListener());
        novo = new Button("Novo", new UsuarioFormListener());
        createFormBody(new BeanItem<Usuario>(new Usuario()));
        form_layout.addComponent(this);
        form_layout.addComponent(createFooter());
        form_layout.setSpacing(true);
        form_layout.setMargin(false, true, false, true);
    }

    public Component getFormLayout() {
        return form_layout;
    }

    private Component createFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addComponent(save);
        footer.addComponent(remove);
        footer.addComponent(novo);
        footer.setVisible(true);
        return footer;
    }

    public void createFormBody(BeanItem<Usuario> item) {
        setItemDataSource(item);
        setFormFieldFactory(new UsuarioFieldFactory(item.getBean().getId() == null));
        setVisibleItemProperties(new Object[] { "codigo", "nome", "mail", "senha", "filial", "tipo" });
    }

    private void addNewUsuario() {
        createFormBody(new BeanItem<Usuario>(new Usuario()));
    }

    class UsuarioFieldFactory extends DefaultFieldFactory {

        private boolean newUser;

        public UsuarioFieldFactory(boolean b) {
            newUser = b;
        }

        @Override
        public Field createField(Item item, Object propertyId, Component uiContext) {
            Field f = super.createField(item, propertyId, uiContext);
            if (f instanceof TextField) {
                ((TextField) f).setNullRepresentation("");
                f.setRequired(true);
                f.setRequiredError("Preenchimento do campo '" + StringUtilities.capitalize(propertyId.toString()) + "' é obrigatório.");
            }
            if (propertyId.equals("codigo")) {
                if (!newUser) f.setReadOnly(true);
            } else if (propertyId.equals("senha")) {
                ((TextField) f).setSecret(true);
            } else if (propertyId.equals("filial")) {
                try {
                    ComboBox c = new ComboBox("Filial");
                    c.addContainerProperty("label", String.class, null);
                    for (Filial fi : cadastroService.buscarTodasFiliais()) {
                        Item i = c.addItem(fi);
                        i.getItemProperty("label").setValue(fi.getNome());
                    }
                    c.setRequired(true);
                    c.setRequiredError("Filial obrigatória");
                    c.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
                    c.setImmediate(true);
                    c.setNullSelectionAllowed(false);
                    c.setPropertyDataSource(item.getItemProperty(propertyId));
                    c.setItemCaptionPropertyId("label");
                    if (c.size() > 0) {
                        Filial f2 = ((BeanItem<Usuario>) getItemDataSource()).getBean().getFilial();
                        if (f2 != null) {
                            Iterator<Filial> it = c.getItemIds().iterator();
                            while (it.hasNext()) {
                                Filial f1 = it.next();
                                if (f2.getId().equals(f1.getId())) {
                                    c.setValue(f1);
                                }
                            }
                        }
                    }
                    return c;
                } catch (PromoveException e) {
                    showErrorMessage(view, "Não foi possível buscar Filiais");
                }
            } else if (propertyId.equals("mail")) {
                f.setRequired(false);
            } else if (propertyId.equals("tipo")) {
                try {
                    ComboBox c = new ComboBox("Tipo");
                    c.addContainerProperty("label", String.class, null);
                    for (TipoUsuario tp : cadastroService.buscarTodosTiposUsuarios()) {
                        Item i = c.addItem(tp);
                        i.getItemProperty("label").setValue(tp.getNome());
                    }
                    c.setRequired(true);
                    c.setRequiredError("Tipo obrigatório");
                    c.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
                    c.setImmediate(true);
                    c.setNullSelectionAllowed(false);
                    c.setPropertyDataSource(item.getItemProperty(propertyId));
                    c.setItemCaptionPropertyId("label");
                    if (c.size() > 0) {
                        TipoUsuario tp2 = ((BeanItem<Usuario>) getItemDataSource()).getBean().getTipo();
                        if (tp2 != null) {
                            Iterator<TipoUsuario> it = c.getItemIds().iterator();
                            while (it.hasNext()) {
                                TipoUsuario tp1 = it.next();
                                if (tp2.getId().equals(tp1.getId())) {
                                    c.setValue(tp1);
                                }
                            }
                        }
                    }
                    return c;
                } catch (PromoveException e) {
                    showErrorMessage(view, "Não foi possível buscar os Tipos de Usuários");
                }
            }
            return f;
        }
    }

    class UsuarioFormListener implements ClickListener {

        @Override
        public void buttonClick(ClickEvent event) {
            if (event.getButton() == save) {
                try {
                    validate();
                    if (isValid()) {
                        commit();
                        BeanItem<Usuario> item = (BeanItem<Usuario>) getItemDataSource();
                        item.getBean().setSenha(item.getBean().getSenha().toUpperCase());
                        cadastroService.salvarUsuario(item.getBean());
                        view.getTable().getContainer().addItem(item.getBean());
                        addNewUsuario();
                        showSuccessMessage(view, "Usuario salvo!");
                    }
                } catch (InvalidValueException ive) {
                    setValidationVisible(true);
                } catch (PromoveException de) {
                    showErrorMessage(view, "Não foi possível salvar Usuario");
                }
            } else if (event.getButton() == novo) {
                addNewUsuario();
            } else if (event.getButton() == remove) {
                try {
                    BeanItem<Usuario> item = (BeanItem<Usuario>) getItemDataSource();
                    if (item.getBean().getId() != null) {
                        cadastroService.excluirUsuario(item.getBean());
                        view.getTable().getContainer().removeItem(item.getBean());
                        showSuccessMessage(view, "Usuario removido");
                    }
                    addNewUsuario();
                } catch (PromoveException de) {
                    showErrorMessage(view, "Não foi possível remover Usuario");
                }
            }
        }
    }
}
