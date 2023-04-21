package uk.ac.ebi.pride.tools.converter.gui.forms;

import psidev.psi.tools.validator.ValidatorException;
import psidev.psi.tools.validator.ValidatorMessage;
import uk.ac.ebi.pride.tools.converter.gui.component.table.ShortFilePathStringRenderer;
import uk.ac.ebi.pride.tools.converter.gui.model.ConverterData;
import uk.ac.ebi.pride.tools.converter.gui.model.FileBean;
import uk.ac.ebi.pride.tools.converter.gui.model.GUIException;
import uk.ac.ebi.pride.tools.converter.report.io.ReportReaderDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.util.*;

/**
 * @author User #3
 */
public class FilterReportForm extends AbstractForm {

    public FilterReportForm() {
        initComponents();
    }

    private void initComponents() {
        scrollPane1 = new JScrollPane();
        fileTable = new JTable();
        fileGeneratedLabel = new JLabel();
        label1 = new JLabel();
        {
            fileTable.setModel(new DefaultTableModel(new Object[][] { { null, null }, { null, null } }, new String[] { "Input File", "Filtered File" }) {

                boolean[] columnEditable = new boolean[] { false, false };

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnEditable[columnIndex];
                }
            });
            {
                TableColumnModel cm = fileTable.getColumnModel();
                cm.getColumn(0).setResizable(false);
                cm.getColumn(1).setResizable(false);
            }
            scrollPane1.setViewportView(fileTable);
        }
        fileGeneratedLabel.setText("Files Generated: ");
        label1.setText("PRIDE XML Filtering Complete!");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 2f));
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup().addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(scrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE).addComponent(label1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE).addComponent(fileGeneratedLabel, GroupLayout.Alignment.LEADING)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup().addGroup(layout.createSequentialGroup().addContainerGap().addComponent(label1).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(fileGeneratedLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE).addContainerGap()));
    }

    private JScrollPane scrollPane1;

    private JTable fileTable;

    private JLabel fileGeneratedLabel;

    private JLabel label1;

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
        return "PRIDE XML Filter Report";
    }

    @Override
    public String getFormDescription() {
        return config.getString("filterreport.form.description");
    }

    @Override
    public Icon getFormIcon() {
        return getFormIcon("filterreport.form.icon");
    }

    @Override
    public String getHelpResource() {
        return "help.ui.filter.report";
    }

    @Override
    public void start() {
        Set<FileBean> files = ConverterData.getInstance().getDataFiles();
        List<String> inputFiles = new ArrayList<String>();
        List<String> filteredFiles = new ArrayList<String>();
        for (FileBean fileBean : files) {
            inputFiles.add(fileBean.getInputFile());
            if (fileBean.getOutputFile() != null) {
                filteredFiles.add(fileBean.getOutputFile());
            }
        }
        Collections.sort(inputFiles);
        Collections.sort(filteredFiles);
        if (inputFiles.size() != filteredFiles.size()) {
            throw new IllegalStateException("File number mismatch: number of input files does not equal number of filtered files");
        }
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        for (int i = 0; i < inputFiles.size(); i++) {
            Vector<Object> row = new Vector<Object>();
            row.add(inputFiles.get(i));
            row.add(filteredFiles.get(i));
            data.add(row);
        }
        Vector<Object> headers = new Vector<Object>();
        headers.add("Input File");
        headers.add("Filtered XML File");
        fileTable.setModel(new DefaultTableModel(data, headers) {

            boolean[] columnEditable = new boolean[] { false, false };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnEditable[columnIndex];
            }
        });
        {
            TableColumnModel cm = fileTable.getColumnModel();
            cm.getColumn(0).setResizable(false);
            cm.getColumn(1).setResizable(false);
        }
        fileTable.setDefaultRenderer(String.class, new ShortFilePathStringRenderer());
    }

    @Override
    public void finish() throws GUIException {
    }
}
