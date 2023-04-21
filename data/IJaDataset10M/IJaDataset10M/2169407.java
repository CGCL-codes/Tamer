package uk.ac.ebi.pride.gui.component.metadata;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.swingx.table.TableColumnExt;
import uk.ac.ebi.pride.data.core.*;
import uk.ac.ebi.pride.gui.component.table.TableFactory;
import uk.ac.ebi.pride.gui.component.table.listener.HyperLinkCellMouseClickListener;
import uk.ac.ebi.pride.gui.component.table.listener.TableCellMouseMotionListener;
import uk.ac.ebi.pride.gui.component.table.model.ParamTableModel;
import uk.ac.ebi.pride.gui.component.table.renderer.HyperLinkCellRenderer;
import uk.ac.ebi.pride.term.CvTermReference;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author User #2
 */
public class GeneralMetadataPanel extends JPanel {

    public GeneralMetadataPanel(MetaData metaData) {
        populateComponents(metaData);
        initComponents();
    }

    /**
     * Create key components and populate them with values
     *
     * @param metaData meta data
     */
    private void populateComponents(MetaData metaData) {
        String accession = metaData.getAccession();
        accessionField = new JTextField();
        if (accession != null) {
            accessionField.setText(accession);
        }
        expTitleField = new JTextField();
        shortLabelField = new JTextField();
        if (metaData instanceof Experiment) {
            String expTitle = ((Experiment) metaData).getTitle();
            if (expTitle != null) {
                expTitleField.setText(expTitle);
            }
            String sl = ((Experiment) metaData).getShortLabel();
            if (sl != null) {
                shortLabelField.setText(sl);
            }
        }
        expTitleField.setCaretPosition(0);
        shortLabelField.setCaretPosition(0);
        projectField = new JTextField();
        expDescArea = new JTextPane();
        List<CvParam> cvs = metaData.getCvParams();
        if (cvs != null) {
            for (CvParam cv : cvs) {
                if (CvTermReference.PROJECT_NAME.getAccession().equals(cv.getAccession())) {
                    projectField.setText(cv.getValue());
                } else if (CvTermReference.EXPERIMENT_DESCRIPTION.getAccession().equals(cv.getAccession())) {
                    expDescArea.setText(cv.getValue());
                }
            }
        }
        projectField.setCaretPosition(0);
        expDescArea.setCaretPosition(0);
        speciesField = new JTextField();
        String species = "";
        Set<String> speciesAcc = new HashSet<String>();
        String tissues = "";
        Set<String> tissuesAcc = new HashSet<String>();
        List<Sample> samples = metaData.getSamples();
        if (samples != null) {
            for (Sample sample : samples) {
                for (CvParam cvParam : sample.getCvParams()) {
                    String cvAcc = cvParam.getAccession();
                    String name = cvParam.getName();
                    String cvLabel = cvParam.getCvLookupID().toLowerCase();
                    if ("newt".equals(cvLabel)) {
                        if (!speciesAcc.contains(cvAcc)) {
                            species += ("".equals(species) ? "" : ", ") + name;
                            speciesAcc.add(cvAcc);
                        }
                    } else if ("bto".equals(cvLabel)) {
                        if (!tissuesAcc.contains(cvAcc)) {
                            tissues += ("".equals(tissues) ? "" : ", ") + name;
                            tissuesAcc.add(cvAcc);
                        }
                    }
                }
            }
        }
        speciesField.setText(species);
        speciesField.setCaretPosition(0);
        tissueField = new JTextField();
        tissueField.setText(tissues);
        tissueField.setCaretPosition(0);
        instrumentField = new JTextField();
        String instrumentStr = "";
        List<InstrumentConfiguration> instruments = metaData.getInstrumentConfigurations();
        for (InstrumentConfiguration instrument : instruments) {
            instrumentStr += instrument.getId();
        }
        instrumentField.setText(instrumentStr);
        instrumentField.setCaretPosition(0);
        if (metaData instanceof Experiment && ((Experiment) metaData).getReferences() != null) {
            List<Reference> references = ((Experiment) metaData).getReferences();
            referenceTable = TableFactory.createReferenceTable(references);
        } else {
            referenceTable = TableFactory.createReferenceTable(new ArrayList<Reference>());
        }
        List<ParamGroup> contacts = metaData.getFileDescription().getContacts();
        contactTable = TableFactory.createContactTable(contacts == null ? new ArrayList<ParamGroup>() : contacts);
        ParamGroup paramGroup = new ParamGroup();
        List<CvParam> cvParams = metaData.getCvParams();
        if (cvParams != null) {
            for (CvParam cvParam : cvParams) {
                String acc = cvParam.getAccession();
                if (!CvTermReference.PROJECT_NAME.getAccession().equals(acc) && !CvTermReference.EXPERIMENT_DESCRIPTION.getAccession().equals(acc)) {
                    paramGroup.addCvParam(cvParam);
                }
            }
        }
        List<UserParam> userParams = metaData.getUserParams();
        if (userParams != null) {
            paramGroup.addUserParams(userParams);
        }
        additionalTable = TableFactory.createParamTable(paramGroup);
        String valColumnHeader = ParamTableModel.TableHeader.VALUE.getHeader();
        TableColumnExt accColumn = (TableColumnExt) additionalTable.getColumn(valColumnHeader);
        accColumn.setCellRenderer(new HyperLinkCellRenderer(Pattern.compile("http.*"), true));
        additionalTable.addMouseMotionListener(new TableCellMouseMotionListener(additionalTable, valColumnHeader));
        additionalTable.addMouseListener(new HyperLinkCellMouseClickListener(additionalTable, valColumnHeader, null));
    }

    private void initComponents() {
        accessionLabel = new JLabel();
        expTitleLabel = new JLabel();
        shortLabel = new JLabel();
        projectLabel = new JLabel();
        expDescLabel = new JLabel();
        scrollPane1 = new JScrollPane();
        referenceLabel = new JLabel();
        scrollPane2 = new JScrollPane();
        contactLabel = new JLabel();
        scrollPane3 = new JScrollPane();
        additionalLabel = new JLabel();
        scrollPane4 = new JScrollPane();
        speciesLabel = new JLabel();
        tissueLabel = new JLabel();
        instrumentLabel = new JLabel();
        setFocusable(false);
        accessionLabel.setText("Experiment Accession");
        accessionLabel.setFont(accessionLabel.getFont().deriveFont(accessionLabel.getFont().getStyle() | Font.BOLD));
        expTitleLabel.setText("Experiment Title");
        expTitleLabel.setFont(expTitleLabel.getFont().deriveFont(expTitleLabel.getFont().getStyle() | Font.BOLD));
        shortLabel.setText("Experiment Label");
        shortLabel.setFont(shortLabel.getFont().deriveFont(shortLabel.getFont().getStyle() | Font.BOLD));
        projectLabel.setText("Project Name");
        projectLabel.setFont(projectLabel.getFont().deriveFont(projectLabel.getFont().getStyle() | Font.BOLD));
        expDescLabel.setText("Experiment Description");
        expDescLabel.setFont(expDescLabel.getFont().deriveFont(expDescLabel.getFont().getStyle() | Font.BOLD));
        accessionField.setEditable(false);
        expTitleField.setEditable(false);
        shortLabelField.setEditable(false);
        projectField.setEditable(false);
        {
            expDescArea.setEditable(false);
            scrollPane1.setViewportView(expDescArea);
        }
        referenceLabel.setText("Reference");
        referenceLabel.setFont(referenceLabel.getFont().deriveFont(referenceLabel.getFont().getStyle() | Font.BOLD));
        {
            scrollPane2.setPreferredSize(new Dimension(300, 220));
            referenceTable.setPreferredScrollableViewportSize(new Dimension(400, 200));
            scrollPane2.setViewportView(referenceTable);
        }
        contactLabel.setText("Contact");
        contactLabel.setFont(contactLabel.getFont().deriveFont(contactLabel.getFont().getStyle() | Font.BOLD));
        {
            contactTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
            scrollPane3.setViewportView(contactTable);
        }
        additionalLabel.setText("Additional");
        additionalLabel.setFont(additionalLabel.getFont().deriveFont(additionalLabel.getFont().getStyle() | Font.BOLD));
        {
            additionalTable.setPreferredScrollableViewportSize(new Dimension(450, 200));
            scrollPane4.setViewportView(additionalTable);
        }
        speciesLabel.setText("Species");
        speciesLabel.setFont(speciesLabel.getFont().deriveFont(speciesLabel.getFont().getStyle() | Font.BOLD));
        speciesField.setEditable(false);
        tissueLabel.setText("Tissue");
        tissueLabel.setFont(tissueLabel.getFont().deriveFont(tissueLabel.getFont().getStyle() | Font.BOLD));
        tissueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tissueField.setEditable(false);
        instrumentLabel.setText("Instrument");
        instrumentLabel.setFont(instrumentLabel.getFont().deriveFont(instrumentLabel.getFont().getStyle() | Font.BOLD));
        instrumentField.setEditable(false);
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup().add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup().add(layout.createSequentialGroup().add(projectLabel).addContainerGap(701, Short.MAX_VALUE)).add(instrumentLabel, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE).add(layout.createSequentialGroup().add(contactLabel, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).addContainerGap(625, Short.MAX_VALUE)).add(layout.createSequentialGroup().add(referenceLabel, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).addContainerGap(625, Short.MAX_VALUE)).add(layout.createSequentialGroup().add(additionalLabel, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).addContainerGap(625, Short.MAX_VALUE)).add(GroupLayout.TRAILING, layout.createSequentialGroup().add(layout.createParallelGroup(GroupLayout.TRAILING).add(GroupLayout.LEADING, scrollPane4, GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE).add(GroupLayout.LEADING, scrollPane3, GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE).add(GroupLayout.LEADING, scrollPane2, GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE).add(layout.createSequentialGroup().add(layout.createParallelGroup().add(accessionLabel).add(expTitleLabel).add(layout.createParallelGroup(GroupLayout.TRAILING, false).add(GroupLayout.LEADING, speciesLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(GroupLayout.LEADING, shortLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).add(expDescLabel, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.RELATED).add(layout.createParallelGroup(GroupLayout.TRAILING).add(GroupLayout.LEADING, scrollPane1, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE).add(GroupLayout.LEADING, layout.createSequentialGroup().add(speciesField, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE).add(18, 18, 18).add(tissueLabel, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.RELATED).add(tissueField, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)).add(GroupLayout.LEADING, expTitleField, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE).add(GroupLayout.LEADING, accessionField, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE).add(GroupLayout.LEADING, projectField, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE).add(GroupLayout.LEADING, shortLabelField, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE).add(instrumentField, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)))).add(11, 11, 11)))));
        layout.setVerticalGroup(layout.createParallelGroup().add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(GroupLayout.BASELINE).add(accessionLabel).add(accessionField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).add(18, 18, 18).add(layout.createParallelGroup(GroupLayout.BASELINE).add(expTitleLabel).add(expTitleField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).add(18, 18, 18).add(layout.createParallelGroup(GroupLayout.BASELINE).add(shortLabel).add(shortLabelField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).add(18, 18, 18).add(layout.createParallelGroup(GroupLayout.BASELINE).add(projectLabel).add(projectField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).add(18, 18, 18).add(layout.createParallelGroup(GroupLayout.BASELINE).add(speciesLabel).add(speciesField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(tissueLabel).add(tissueField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).add(23, 23, 23).add(layout.createParallelGroup(GroupLayout.BASELINE).add(instrumentLabel).add(instrumentField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).add(layout.createParallelGroup().add(layout.createSequentialGroup().add(34, 34, 34).add(expDescLabel)).add(layout.createSequentialGroup().add(27, 27, 27).add(scrollPane1, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))).addPreferredGap(LayoutStyle.RELATED).add(referenceLabel).addPreferredGap(LayoutStyle.RELATED).add(scrollPane2, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(contactLabel).addPreferredGap(LayoutStyle.RELATED).add(scrollPane3, GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE).addPreferredGap(LayoutStyle.RELATED).add(additionalLabel).addPreferredGap(LayoutStyle.RELATED).add(scrollPane4, GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE).add(26, 26, 26)));
    }

    private JLabel accessionLabel;

    private JLabel expTitleLabel;

    private JLabel shortLabel;

    private JLabel projectLabel;

    private JLabel expDescLabel;

    private JTextField accessionField;

    private JTextField expTitleField;

    private JTextField shortLabelField;

    private JTextField projectField;

    private JScrollPane scrollPane1;

    private JTextPane expDescArea;

    private JLabel referenceLabel;

    private JScrollPane scrollPane2;

    private JTable referenceTable;

    private JLabel contactLabel;

    private JScrollPane scrollPane3;

    private JTable contactTable;

    private JLabel additionalLabel;

    private JScrollPane scrollPane4;

    private JTable additionalTable;

    private JLabel speciesLabel;

    private JTextField speciesField;

    private JLabel tissueLabel;

    private JTextField tissueField;

    private JLabel instrumentLabel;

    private JTextField instrumentField;
}
