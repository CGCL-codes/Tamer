package syndicus.gui.residential;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.binding.form.support.DefaultFormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ObservableList;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.support.DialogCommand;
import org.springframework.richclient.form.AbstractDetailForm;
import org.springframework.richclient.form.AbstractTableMasterForm;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import syndicus.dao.ResidentieDAO;
import syndicus.domain.BaseBean;
import syndicus.domain.Residential;
import syndicus.gui.helper.CityValueHolder;
import syndicus.gui.helper.CollectionWrapper;
import syndicus.gui.helper.ResidentieValueHolder;
import syndicus.service.ServiceLocator;

public class ResidentieTable extends AbstractTableMasterForm {

    private static Collection<? extends BaseBean> getCollection() {
        return ServiceLocator.getService(ResidentieDAO.class).findAll();
    }

    private ResidentieValueHolder residentieHolder = ServiceLocator.getService(ResidentieValueHolder.class);

    private CityValueHolder cityValueHolder = ServiceLocator.getService(CityValueHolder.class);

    public ResidentieTable() {
        super(new DefaultFormModel(new CollectionWrapper(getCollection())), "collection", "residentie", Residential.class);
        setSortProperty("fullName");
        getDeleteCommand().setVisible(false);
    }

    @Override
    protected AbstractDetailForm createDetailForm(final HierarchicalFormModel parentFormModel, final ValueModel valueHolder, final ObservableList masterList) {
        return new AbstractDetailForm(parentFormModel, "residentie", valueHolder, masterList) {

            @SuppressWarnings("unchecked")
            @Override
            public void commit() {
                boolean editingNewFormObject = isEditingNewFormObject();
                super.commit();
                Residential residential = (Residential) getFormObject();
                if (editingNewFormObject) {
                    ServiceLocator.getService(ResidentieDAO.class).persist(residential);
                } else {
                    residential = ServiceLocator.getService(ResidentieDAO.class).merge(residential);
                }
                masterList.clear();
                masterList.addAll(getCollection());
            }

            @Override
            protected JComponent createFormControl() {
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(400, 0));
                SwingBindingFactory bf = new SwingBindingFactory(getFormModel());
                TableFormBuilder formBuilder = new TableFormBuilder(getBindingFactory());
                formBuilder.setLabelAttributes("colGrId=label colSpec=right:pref");
                final Binding createBoundComboBox = bf.createBoundComboBox("main", residentieHolder, "name");
                formBuilder.add(createBoundComboBox);
                JButton residentieButton = new JButton(new AbstractAction("Manage") {

                    public void actionPerformed(ActionEvent e) {
                        ((DialogCommand) ServiceLocator.getBean("manageResidentieDialogCommand")).execute();
                    }
                });
                JButton clear = new JButton(new AbstractAction("X") {

                    public void actionPerformed(ActionEvent e) {
                        ((JComboBox) createBoundComboBox.getControl()).setSelectedItem(null);
                    }
                });
                formBuilder.getLayoutBuilder().cell(residentieButton);
                formBuilder.getLayoutBuilder().cell(clear);
                formBuilder.row();
                formBuilder.row();
                formBuilder.add("name");
                formBuilder.row();
                final JTextField addressField = (JTextField) formBuilder.add("address")[1];
                formBuilder.row();
                formBuilder.add(bf.createBoundComboBox("city", cityValueHolder));
                formBuilder.row();
                String[] months = new String[12];
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("MMM");
                for (int i = 0; i < months.length; i++) {
                    calendar.set(Calendar.MONTH, i);
                    months[i] = df.format(calendar.getTime());
                }
                ValueModel selectableItems = new ValueHolder(months);
                formBuilder.add(bf.createBoundComboBox("startCalendarYearAbbr", selectableItems));
                formBuilder.row();
                formBuilder.getLayoutBuilder().cell(createButtonBar());
                updateControlsForState();
                panel.add(formBuilder.getForm());
                ((JComboBox) createBoundComboBox.getControl()).addItemListener(new ItemListener() {

                    public void itemStateChanged(ItemEvent e) {
                        Residential residential = (Residential) e.getItem();
                        addressField.setText(residential.getAddress());
                    }
                });
                return panel;
            }
        };
    }

    @Override
    protected JTable createTable(TableModel tableModel) {
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        return table;
    }

    @Override
    protected String[] getColumnPropertyNames() {
        return new String[] { "fullName", "address", "city" };
    }
}
