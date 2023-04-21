package gov.nasa.gsfc.visbard.gui.resourcemanip;

import gov.nasa.gsfc.visbard.model.Dataset;
import gov.nasa.gsfc.visbard.model.Property;
import gov.nasa.gsfc.visbard.repository.Repository;
import gov.nasa.gsfc.visbard.repository.category.Category;
import gov.nasa.gsfc.visbard.repository.category.CategoryType;
import gov.nasa.gsfc.visbard.repository.resource.Column;
import gov.nasa.gsfc.visbard.repository.resource.ColumnASCII;
import gov.nasa.gsfc.visbard.repository.resource.ColumnCdf;
import gov.nasa.gsfc.visbard.repository.resource.ColumnCombined;
import gov.nasa.gsfc.visbard.repository.resource.RDFWriter;
import gov.nasa.gsfc.visbard.repository.resource.ReadConfiguration;
import gov.nasa.gsfc.visbard.repository.resource.Resource;
import gov.nasa.gsfc.visbard.repository.resource.ResourceInfoCdf;
import gov.nasa.gsfc.visbard.util.Range;
import gov.nasa.gsfc.visbard.util.VisbardDate;
import gov.nasa.gsfc.visbard.util.VisbardException;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Component displays the resource through GUI using the stored resource
 * configuration. Provides a way for the user to change the configuration.
 * Does NOT modify the resource by itself!
 */
public class ResourceView extends JPanel {

    private static final org.apache.log4j.Category sLogger = org.apache.log4j.Category.getInstance(ResourceView.class.getName());

    private Range fReaderSpan;

    private Resource fResource;

    private ReadConfiguration fConfig;

    private RangePanel fRangePanel;

    private ArrayList fCheckBoxes = new ArrayList();

    public static String[] sDataSizeVals = new String[] { "float", "double", "int", "unknown" };

    public static String[] sDataTypeVals = new String[] { "scalar", "vector", "unknown" };

    public static String[] sUnitVals = new String[] { "km/s", "nT", "ms", "km", "eV", "cm^-3", "Re", "MK", "K" };

    public static String[] sDisplayNames = new String[] { "Time", "Location", "Velocity", "Thermal Speed", "Flow speed", "MagField", "MagField RMS", "Magfield Transverse Delta", "Total MagField", "Density", "Electron Density", "Ion energy flux" };

    /**
     * constructor
    **/
    public ResourceView(Resource resource, Dataset set) {
        fResource = resource;
        fReaderSpan = fResource.getReader().getTimespan();
        fConfig = (ReadConfiguration) fResource.getCurrentConfig();
        initGUI(set);
    }

    public Resource getResource() {
        return fResource;
    }

    public void initGUI(Dataset set) {
        JLabel lbl;
        JPanel overall = new JPanel(new GridBagLayout());
        Insets ins = new Insets(2, 2, 2, 2);
        lbl = new JLabel("Resource name :");
        overall.add(lbl, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, ins, 0, 0));
        lbl = new JLabel(fResource.getReader().getName());
        lbl.setForeground(Color.black);
        overall.add(lbl, new GridBagConstraints(1, GridBagConstraints.RELATIVE, 3, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, ins, 0, 0));
        lbl = new JLabel("File(s) :");
        overall.add(lbl, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, ins, 0, 0));
        File[] files = fResource.getFiles();
        if ((files == null) || (files.length <= 0)) {
            overall.add(new JLabel("<combined>"), new GridBagConstraints(1, GridBagConstraints.RELATIVE, 3, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 2), 0, 0));
        } else {
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                JLabel label = new JLabel(name);
                label.setForeground(Color.black);
                label.setFont(new Font("Serif", Font.PLAIN, 12));
                overall.add(label, new GridBagConstraints(1, GridBagConstraints.RELATIVE, 3, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 2), 0, 0));
                if (i > 0) overall.add(new JPanel(), new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, ins, 0, 0));
            }
        }
        lbl = new JLabel("Res. Description File used :");
        overall.add(lbl, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, ins, 0, 0));
        File resRdf = fResource.getInfo().getRDF();
        String rdfPath;
        if (resRdf != null) rdfPath = resRdf.getAbsolutePath(); else rdfPath = new String("N/A");
        overall.add(new JLabel(rdfPath), new GridBagConstraints(1, GridBagConstraints.RELATIVE, 3, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 2), 0, 0));
        overall.add(new JLabel("Vars :"), new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, ins, 0, 0));
        JPanel panel = new JPanel(new GridBagLayout());
        HashSet colset = new HashSet(Arrays.asList(fResource.getInfo().getAllDescribedColumns()));
        ArrayList availCols = new ArrayList(Arrays.asList(fResource.getReader().getAllAvaliableColumns()));
        colset.addAll(availCols);
        ArrayList cols = new ArrayList(colset);
        if (!cols.isEmpty()) {
            for (int i = 0; i < cols.size(); i++) {
                Column col = (Column) cols.get(i);
                String scalvec = col.isScalar() ? "(scalar)" : "(vector)";
                String varName;
                String varSource;
                if (col instanceof ColumnCdf) {
                    ColumnCdf colcdf = (ColumnCdf) col;
                    varName = colcdf.getName();
                    varSource = colcdf.getSource() + " " + scalvec;
                } else {
                    varName = col.getName();
                    varSource = new String(varName + " " + scalvec);
                }
                class CheckboxListenerVB implements ActionListener {

                    private Column fCol;

                    public CheckboxListenerVB(Column colm) {
                        fCol = colm;
                    }

                    public void actionPerformed(ActionEvent e) {
                        boolean sel = ((JCheckBox) e.getSource()).isSelected();
                        fConfig.setColumn(fCol, sel);
                    }
                }
                class ComboBoxListenerVB implements ActionListener {

                    private Column fCol;

                    private String fLastSelectedItem;

                    private JPanel fPanel;

                    public ComboBoxListenerVB(Column col, JPanel panel) {
                        fCol = col;
                        fLastSelectedItem = col.getName();
                        fPanel = panel;
                    }

                    public void actionPerformed(ActionEvent e) {
                        if (e.getActionCommand().equals("comboBoxChanged")) {
                            if (fCol instanceof ColumnCdf) {
                                fConfig.setColumnName(fCol, ((JComboBox) e.getSource()).getSelectedItem().toString());
                                fLastSelectedItem = fCol.getName();
                                File resRdf = fResource.getInfo().getRDF();
                                if (resRdf != null) {
                                    JOptionPane offerRdfWritePane = new JOptionPane("Would you like this remapping to the \"" + fCol.getName() + "\" variable to become\npermanent by " + "rewriting this data product's associated Resource Description File\n(" + resRdf.getAbsolutePath() + ")?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
                                    JDialog offerRdfWriteDialog = offerRdfWritePane.createDialog(null, "Rewrite associated RDF?");
                                    offerRdfWriteDialog.show();
                                    offerRdfWriteDialog.dispose();
                                    if ((offerRdfWritePane.getValue() != null) && (((Integer) offerRdfWritePane.getValue()).intValue() != JOptionPane.NO_OPTION)) {
                                        ResourceInfoCdf info = (ResourceInfoCdf) fResource.getInfo();
                                        HashMap idHashMap = info.getIdMap();
                                        ArrayList attribs = new ArrayList(idHashMap.keySet());
                                        String idMap[][] = new String[attribs.size()][AutoGenRDFPanel.MAX_CDF_VARS];
                                        for (int i = 0; i < attribs.size(); i++) {
                                            idMap[i][0] = (String) attribs.get(i);
                                            idMap[i][1] = (String) idHashMap.get(idMap[i][0]);
                                        }
                                        HashSet colset = new HashSet(Arrays.asList(fResource.getInfo().getAllDescribedColumns()));
                                        ArrayList availCols = new ArrayList(Arrays.asList(fResource.getReader().getAllAvaliableColumns()));
                                        colset.addAll(availCols);
                                        ArrayList cols = new ArrayList(colset);
                                        ArrayList varNames = new ArrayList();
                                        ArrayList varSources = new ArrayList();
                                        ArrayList varSize = new ArrayList();
                                        ArrayList varType = new ArrayList();
                                        ArrayList varUnit = new ArrayList();
                                        if (!cols.isEmpty()) {
                                            for (int i = 0; i < cols.size(); i++) {
                                                Column col = (Column) cols.get(i);
                                                ColumnCdf colcdf = (ColumnCdf) col;
                                                varNames.add(colcdf.getName());
                                                varSources.add(colcdf.getSource());
                                                varSize.add(colcdf.isDouble() ? "double" : "float");
                                                varType.add(colcdf.isScalar() ? "scalar" : "vector");
                                                varUnit.add(colcdf.getUnit().toString());
                                            }
                                        }
                                        String varData[][] = new String[varSources.size()][5];
                                        for (int i = 0; i < varSources.size(); i++) {
                                            varData[i][0] = (String) varSources.get(i);
                                            varData[i][1] = (String) varNames.get(i);
                                            varData[i][2] = (String) varSize.get(i);
                                            varData[i][3] = (String) varType.get(i);
                                            varData[i][4] = (String) varUnit.get(i);
                                        }
                                        File newRdf;
                                        try {
                                            newRdf = RDFWriter.generateRDF(info.getRDF().getAbsolutePath(), true, info.getName(), idMap, info.getFilenameBase(), info.getWebServicesName(), varData);
                                        } catch (VisbardException ve) {
                                            sLogger.error("Error while generating RDF: " + ve.getMessage());
                                            newRdf = null;
                                            JOptionPane infoPane = new JOptionPane("Error generating RDF:\n  " + ve.getMessage(), JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                                            JDialog infoDialog = infoPane.createDialog(null, "Error Generating RDF");
                                            infoDialog.show();
                                            infoDialog.dispose();
                                            return;
                                        }
                                        try {
                                            JOptionPane infoPane = new JOptionPane("New Resource Description File (RDF) successfully recreated in file:\n  " + newRdf.getCanonicalPath(), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
                                            JDialog infoDialog = infoPane.createDialog(null, "RDF Successfully Recreated");
                                            infoDialog.show();
                                            infoDialog.dispose();
                                        } catch (IOException ioe) {
                                            sLogger.error("Error showing full path of new RDF: " + ioe.getMessage());
                                        }
                                    }
                                }
                                if (fResource.getLastLoadedConfig() != null) {
                                    JOptionPane reloadReminderPane = new JOptionPane("Since this resource is already loaded, note that " + "you must press \"Load\" to\nrefresh the variable mapping " + "currently in memory.", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
                                    JDialog reloadReminderDialog = reloadReminderPane.createDialog(null, "Reload data reminder");
                                    reloadReminderDialog.show();
                                    reloadReminderDialog.dispose();
                                }
                            } else if (fCol instanceof ColumnCombined) {
                                JOptionPane infoPane = new JOptionPane("Variables of \"combined\" data files can only " + "be remapped when they are a \"New Resource (Not Loaded)\".\n" + "Please unload this file, reopen it, remap the desired variables, then \"Load\" it.", JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                                JDialog infoDialog = infoPane.createDialog(fPanel, "Error during variable remapping");
                                infoDialog.show();
                                infoDialog.dispose();
                                ((JComboBox) e.getSource()).setSelectedItem(fLastSelectedItem);
                            } else if (fCol instanceof ColumnASCII) {
                                JOptionPane infoPane = new JOptionPane("At this time, variables of ASCII data files cannot be remapped within ViSBARD.\n" + "Please load the file in a text editor and manually rename the desired variables.", JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
                                JDialog infoDialog = infoPane.createDialog(fPanel, "Error during variable remapping");
                                infoDialog.show();
                                infoDialog.dispose();
                                ((JComboBox) e.getSource()).setSelectedItem(fLastSelectedItem);
                            }
                        }
                    }
                }
                JCheckBox chk = new JCheckBox(varSource, true);
                chk.setSelected(fConfig.hasColumn(col));
                chk.addActionListener(new CheckboxListenerVB(col));
                ArrayList alDisplayNames = new ArrayList();
                for (int j = 0; j < sDisplayNames.length; j++) alDisplayNames.add(sDisplayNames[j]);
                if (!alDisplayNames.contains(varName)) alDisplayNames.add(varName);
                JComboBox comboBox = new JComboBox(alDisplayNames.toArray());
                comboBox.setSelectedItem(varName);
                comboBox.setEditable(true);
                comboBox.addActionListener(new ComboBoxListenerVB(col, this));
                if ((col.getName().equals(CategoryType.TIME.getName())) || (col.getName().equals(CategoryType.LOCATION.getName())) || (!availCols.contains(col))) {
                    chk.setEnabled(false);
                    comboBox.setEnabled(false);
                }
                panel.add(chk, new GridBagConstraints(0, i, 2, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                panel.add(comboBox, new GridBagConstraints(GridBagConstraints.RELATIVE, i, 2, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                fCheckBoxes.add(chk);
            }
        }
        overall.add(panel, new GridBagConstraints(1, GridBagConstraints.RELATIVE, 3, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, ins, 0, 0));
        lbl = new JLabel("Available Range :");
        overall.add(lbl, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, ins, 0, 0));
        lbl = new JLabel(VisbardDate.getString(fReaderSpan.fStart) + " - " + VisbardDate.getString(fReaderSpan.fEnd));
        lbl.setForeground(Color.black);
        lbl.setFont(new Font("Serif", Font.PLAIN, 12));
        overall.add(lbl, new GridBagConstraints(1, GridBagConstraints.RELATIVE, 3, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, ins, 0, 0));
        if (set != null) {
            lbl = new JLabel("Loaded Range :");
            overall.add(lbl, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, ins, 0, 0));
            Repository rep = set.getRepository();
            Category time = (Category) set.getBasePropVal(Property.sTime);
            double start = rep.getScalarDouble(time, 0);
            double end = rep.getScalarDouble(time, rep.getNumRows() - 1);
            lbl = new JLabel(VisbardDate.getString(start) + " - " + VisbardDate.getString(end));
            lbl.setForeground(Color.black);
            lbl.setFont(new Font("Serif", Font.PLAIN, 12));
            overall.add(lbl, new GridBagConstraints(1, GridBagConstraints.RELATIVE, 3, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, ins, 0, 0));
        }
        lbl = new JLabel("Available readings :");
        overall.add(lbl, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, ins, 0, 0));
        int availReadings = fResource.getReader().getNumReadings();
        String readings = Integer.toString(availReadings);
        if (set != null) {
            readings += " (" + Integer.toString(set.getRepository().getNumRows()) + " loaded)";
        }
        lbl = new JLabel(readings);
        lbl.setForeground(Color.black);
        lbl.setFont(new Font("Serif", Font.PLAIN, 12));
        overall.add(lbl, new GridBagConstraints(1, GridBagConstraints.RELATIVE, 3, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, ins, 0, 0));
        lbl = new JLabel("Approximate Delta :");
        overall.add(lbl, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, ins, 0, 0));
        double delta = (availReadings > 1) ? fReaderSpan.getExtent() / (availReadings - 1) : fReaderSpan.getExtent();
        String deltaS = VisbardDate.getRelativeString(delta);
        if (set != null) {
            Category time = (Category) set.getBasePropVal(Property.sTime);
            int numrows = set.getRepository().getNumRows();
            int extent = (numrows < 150) ? numrows : 150;
            double ld = set.getRepository().computeApproximateDelta(time, 0, extent);
            deltaS += " (" + VisbardDate.getRelativeString(ld) + " loaded)";
        }
        lbl = new JLabel(deltaS);
        lbl.setForeground(Color.black);
        lbl.setFont(new Font("Serif", Font.PLAIN, 12));
        overall.add(lbl, new GridBagConstraints(1, GridBagConstraints.RELATIVE, 3, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, ins, 0, 0));
        fRangePanel = new RangePanel(fConfig.getTimeRange(), fReaderSpan, fConfig.getDecimation()) {

            protected void changed() {
                fConfig.setTimeRange(new Range(fRangePanel.getRange()));
                fConfig.setDecimation(fRangePanel.getDecimation());
            }
        };
        overall.add(fRangePanel, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 4, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, ins, 0, 0));
        this.setLayout(new GridBagLayout());
        this.add(overall, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    }
}
