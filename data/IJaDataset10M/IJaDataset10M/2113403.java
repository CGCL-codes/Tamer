package uk.ac.ebi.pride.tools.converter.gui.forms;

import psidev.psi.tools.validator.ValidatorException;
import psidev.psi.tools.validator.ValidatorMessage;
import uk.ac.ebi.pride.tools.converter.gui.component.table.ShortFilePathStringRenderer;
import uk.ac.ebi.pride.tools.converter.gui.model.ConverterData;
import uk.ac.ebi.pride.tools.converter.gui.model.GUIException;
import uk.ac.ebi.pride.tools.converter.report.io.ReportReaderDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.util.*;

/**
 * @author User #3
 */
public class MergerReportForm extends AbstractForm {

    public MergerReportForm() {
        initComponents();
    }

    private void initComponents() {
        scrollPane1 = new JScrollPane();
        fileTable = new JTable();
        label2 = new JLabel();
        label1 = new JLabel();
        outputFileTextField = new JTextField();
        {
            fileTable.setModel(new DefaultTableModel(new Object[][] { { null }, { null } }, new String[] { "Source Files" }) {

                boolean[] columnEditable = new boolean[] { false };

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnEditable[columnIndex];
                }
            });
            scrollPane1.setViewportView(fileTable);
        }
        label2.setText("PRIDE XML Mergring Complete!");
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        label2.setFont(label2.getFont().deriveFont(label2.getFont().getSize() + 2f));
        label1.setText("Output file: ");
        outputFileTextField.setEditable(false);
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup().addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(scrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE).addComponent(label2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE).addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(label1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(outputFileTextField, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup().addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(label2, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label1).addComponent(outputFileTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE).addContainerGap()));
    }

    private JScrollPane scrollPane1;

    private JTable fileTable;

    private JLabel label2;

    private JLabel label1;

    private JTextField outputFileTextField;

    @Override
    public Collection<ValidatorMessage> validateForm() throws ValidatorException {
        return Collections.emptyList();
    }

    @Override
    public void clear() {
    }

    @Override
    public void save(ReportReaderDAO dao) {
    }

    @Override
    public void load(ReportReaderDAO dao) {
    }

    @Override
    public String getFormName() {
        return "Merge Report";
    }

    @Override
    public String getFormDescription() {
        return config.getString("mergereport.form.description");
    }

    @Override
    public Icon getFormIcon() {
        return getFormIcon("mergereport.form.icon");
    }

    @Override
    public String getHelpResource() {
        return "help.ui.merger.report";
    }

    @Override
    public void start() {
        outputFileTextField.setText(ConverterData.getInstance().getMergedOutputFile());
        List<String> inputFiles = new ArrayList<String>(ConverterData.getInstance().getInputFiles());
        Collections.sort(inputFiles);
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        for (int i = 0; i < inputFiles.size(); i++) {
            Vector<Object> row = new Vector<Object>();
            row.add(inputFiles.get(i));
            data.add(row);
        }
        Vector<Object> headers = new Vector<Object>();
        headers.add("Input Files");
        fileTable.setModel(new DefaultTableModel(data, headers) {

            boolean[] columnEditable = new boolean[] { false };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnEditable[columnIndex];
            }
        });
        {
            TableColumnModel cm = fileTable.getColumnModel();
            cm.getColumn(0).setResizable(false);
        }
        fileTable.setDefaultRenderer(String.class, new ShortFilePathStringRenderer());
    }

    @Override
    public void finish() throws GUIException {
    }
}
