package br.org.databasetools.core.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import br.org.databasetools.core.images.ColorManager;
import br.org.databasetools.core.table.Bean;
import br.org.databasetools.core.table.TableFieldMask;
import br.org.databasetools.core.table.TableMask;
import br.org.databasetools.core.util.BeanProvider;
import br.org.databasetools.core.validator.RequiredLookupValidator;
import br.org.databasetools.core.validator.RequiredValidator;
import br.org.databasetools.core.validator.ValidatorAbstract;
import br.org.databasetools.core.view.fields.FieldFactory;
import br.org.databasetools.core.view.fields.TAbstractField;
import br.org.databasetools.core.view.fields.TCepField;
import br.org.databasetools.core.view.fields.TCheckBoxField;
import br.org.databasetools.core.view.fields.TCnpjField;
import br.org.databasetools.core.view.fields.TComboBoxField;
import br.org.databasetools.core.view.fields.TComboBoxFieldDB;
import br.org.databasetools.core.view.fields.TCpfField;
import br.org.databasetools.core.view.fields.TDateField;
import br.org.databasetools.core.view.fields.TDecimalField;
import br.org.databasetools.core.view.fields.THourField;
import br.org.databasetools.core.view.fields.TIntegerField;
import br.org.databasetools.core.view.fields.TLookupCepField;
import br.org.databasetools.core.view.fields.TLookupField;
import br.org.databasetools.core.view.fields.TPasswordField;
import br.org.databasetools.core.view.fields.TTextAreaField;
import br.org.databasetools.core.view.fields.TTextField;
import br.org.databasetools.core.view.window.Controller;

public class TabularView extends View {

    static Log LOG = LogFactory.getLog(TabularView.class);

    private static final long serialVersionUID = 2776571229380290585L;

    private List<TAbstractField> componentsEdit = new ArrayList<TAbstractField>();

    private List<TLabel> labels = new ArrayList<TLabel>();

    private List<Boolean> enableds = new ArrayList<Boolean>();

    private List<String> fieldsName = new ArrayList<String>();

    private Controller controller;

    private TableMask form;

    private List<TableFieldMask> fields;

    public TabularView(TableMask form, Controller controller) throws Exception {
        super(controller);
        this.controller = controller;
        this.form = form;
        this.createGUI();
    }

    public TabularView(List<TableFieldMask> fields, Controller controller) throws Exception {
        super(controller);
        this.controller = controller;
        this.form = null;
        this.fields = fields;
        this.createGUI();
    }

    protected void createGUI() throws Exception {
        this.setLayout(new BorderLayout());
        if (this.fields == null) this.fields = form.getFieldsMask();
        if (this.fields == null) throw new Exception("Os campos n�o est�o especificados para criacao da tabular.");
        labels.clear();
        componentsEdit.clear();
        for (Iterator<TableFieldMask> i = fields.iterator(); i.hasNext(); ) {
            TableFieldMask fieldMask = i.next();
            if (fieldMask.isVisible() == false) continue;
            TAbstractField fieldEdit = FieldFactory.createField(fieldMask, this.controller);
            TLabel fieldLabel = new TLabel(fieldMask.getLabel());
            if (fieldMask.isRequired()) {
                fieldLabel.setForeground(ColorManager.getInstance().getEditRequired());
            }
            labels.add(fieldLabel);
            componentsEdit.add(fieldEdit);
            fieldsName.add(fieldMask.getField().getNameColumn());
            enableds.add(new Boolean(fieldEdit.isEnabled()));
        }
        JPanel painel = new JPanel();
        painel.setLayout(new GroupLayout(painel));
        GroupLayout layout = new GroupLayout(painel);
        painel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        ParallelGroup labelsHGroup = layout.createParallelGroup();
        ParallelGroup fieldsHGroup = layout.createParallelGroup();
        for (int i = 0; i < labels.size(); i++) {
            labelsHGroup.addComponent(labels.get(i));
        }
        for (int i = 0; i < componentsEdit.size(); i++) {
            int tamanho = componentsEdit.get(i).getComponent().getPreferredSize().width;
            if (tamanho > 400) fieldsHGroup.addComponent(componentsEdit.get(i).getComponent()); else fieldsHGroup.addComponent(componentsEdit.get(i).getComponent(), GroupLayout.PREFERRED_SIZE, tamanho, GroupLayout.PREFERRED_SIZE);
        }
        layout.setHorizontalGroup(layout.createSequentialGroup().addGap(4).addGroup(labelsHGroup).addGroup(fieldsHGroup).addGap(4));
        SequentialGroup mainVGroup = layout.createSequentialGroup();
        mainVGroup.addGap(4);
        for (int i = 0; i < componentsEdit.size(); i++) {
            int altura = 18;
            if (componentsEdit.get(i) instanceof TTextAreaField) {
                altura = componentsEdit.get(i).getComponent().getPreferredSize().height;
            }
            mainVGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(labels.get(i), GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE).addComponent(componentsEdit.get(i).getComponent(), GroupLayout.PREFERRED_SIZE, altura, GroupLayout.PREFERRED_SIZE));
        }
        mainVGroup.addGap(4);
        layout.setVerticalGroup(mainVGroup);
        JScrollPane scroll = new JScrollPane(painel);
        this.add(scroll, BorderLayout.CENTER);
    }

    public Bean getRow() {
        return (Bean) super.getRow();
    }

    public void setRow(Bean bean) {
        super.setRow(bean);
    }

    public void requestFocus() {
        int k = 0;
        for (Iterator<TAbstractField> i = componentsEdit.iterator(); i.hasNext(); ) {
            TAbstractField jatf = i.next();
            Boolean boo = enableds.get(k);
            if (boo == true) {
                jatf.requestFocus();
                break;
            }
            k++;
        }
    }

    @SuppressWarnings("unused")
    private int linearSearch(String[] array, String key) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equalsIgnoreCase(key)) {
                return i;
            }
        }
        return -1;
    }

    public void modelToView() throws Exception {
        for (Iterator<TAbstractField> i = componentsEdit.iterator(); i.hasNext(); ) {
            TAbstractField jatf = i.next();
            jatf.setFieldValue(null);
        }
        int k = 0;
        for (Iterator<TAbstractField> i = componentsEdit.iterator(); i.hasNext(); ) {
            TAbstractField jatf = i.next();
            jatf.setValueObject((Bean) this.getRow());
            Object object = BeanProvider.getProperty(getRow(), jatf.getFieldName());
            String fieldname = fieldsName.get(k++);
            LOG.debug("Executando metodo modelToView para o field " + fieldname + ", valor: " + object);
            if (object == null) continue;
            if (jatf instanceof TTextField) {
                jatf.setFieldValue((String) object);
            } else if (jatf instanceof TPasswordField) {
                jatf.setFieldValue((String) object);
            } else if (jatf instanceof TTextAreaField) {
                jatf.setFieldValue((String) object);
            } else if (jatf instanceof TCpfField) {
                jatf.setFieldValue((String) object);
            } else if (jatf instanceof TCepField) {
                jatf.setFieldValue((String) object);
            } else if (jatf instanceof TCnpjField) {
                jatf.setFieldValue((String) object);
            } else if (jatf instanceof THourField) {
                jatf.setFieldValue((String) object);
            } else if ((jatf instanceof TIntegerField)) {
                Integer value = (Integer) object;
                jatf.setFieldValue(value);
            } else if ((jatf instanceof TDecimalField)) {
                Double value = (Double) object;
                jatf.setFieldValue(value);
            } else if ((jatf instanceof TDateField)) {
                Date value = (Date) object;
                jatf.setFieldValue(value);
            } else if ((jatf instanceof TLookupField)) {
                Integer value = (Integer) object;
                jatf.setFieldValue(value);
            } else if ((jatf instanceof TLookupCepField)) {
                jatf.setFieldValue(null);
            } else if ((jatf instanceof TComboBoxFieldDB)) {
                Integer value = (Integer) object;
                jatf.setFieldValue(value);
            } else if ((jatf instanceof TComboBoxField)) {
                jatf.setFieldValue(object);
            } else if ((jatf instanceof TCheckBoxField)) {
                jatf.setFieldValue(object);
            }
        }
    }

    public void viewToModel() throws Exception {
        for (Iterator<TAbstractField> i = componentsEdit.iterator(); i.hasNext(); ) {
            TAbstractField jatf = i.next();
            Object object = jatf.getFieldValue();
            BeanProvider.setProperty(getRow(), jatf.getFieldName(), object);
        }
    }

    public void refresh() {
    }

    public void setFieldsEnabled(boolean enabled) {
        int k = 0;
        for (Iterator<TAbstractField> i = componentsEdit.iterator(); i.hasNext(); ) {
            TAbstractField jatf = i.next();
            boolean podeHabilitar = true;
            if ((this.form != null) && (this.form.getFieldMaskByBeanName(jatf.getFieldName()) != null)) {
                podeHabilitar = this.form.getFieldMaskByBeanName(jatf.getFieldName()).isEnabled();
            }
            if (podeHabilitar == true) {
                if (enabled) {
                    Boolean boo = enableds.get(k);
                    if (boo == true) jatf.setFocusable(enabled);
                } else {
                    jatf.setFocusable(enabled);
                }
            } else {
                jatf.setFocusable(false);
            }
            ++k;
        }
    }

    public List<ValidatorAbstract> createValidators() {
        List<ValidatorAbstract> result = new ArrayList<ValidatorAbstract>();
        int k = 0;
        for (Iterator<TableFieldMask> i = fields.iterator(); i.hasNext(); ) {
            TableFieldMask field = i.next();
            if (field.getField().isRequired()) {
                if (field.getEditType() == TAbstractField.LOOKUPFIELD) {
                    TLookupField lookupfield = (TLookupField) componentsEdit.get(k);
                    result.add(new RequiredLookupValidator(lookupfield, field.getField().getNameBean(), field.getLabel()));
                } else {
                    result.add(new RequiredValidator((Bean) this.getRow(), field.getField().getNameBean(), field.getLabel()));
                }
            }
            k++;
        }
        return result;
    }

    public List<TAbstractField> getComponentsEdit() {
        return componentsEdit;
    }

    public void first() {
    }

    public void last() {
    }

    public void next() {
    }

    public void prior() {
    }
}
