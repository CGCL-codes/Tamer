package com.ojt.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import com.ojt.CompetitionDescriptor;
import com.ojt.Competitor;
import com.ojt.CompetitorGroup;
import com.ojt.CompetitorsGroupUtilities;
import com.ojt.OJTConfiguration;
import com.ojt.OjtConstants;
import com.ojt.export.CompetitorsGroupExporter;
import com.ojt.export.CompetitorsGroupExporterFactory;
import com.ojt.export.GroupExportListener;

public class ExportPanel extends JPanel implements GroupExportListener {

    private JTextField filenameField;

    private JProgressBar progressBar;

    private JLabel title;

    private final Logger logger = Logger.getLogger(getClass());

    private File exportFile;

    private JButton openButton;

    public ExportPanel() {
        super();
        buildGui();
    }

    @Override
    public void groupExportBegin(final String groupName, final CompetitorGroup group, final int groupNumber) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                filenameField.setText("Export du groupe '" + groupName + "'");
            }
        });
    }

    @Override
    public void exportFinished() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                title.setText("Poules export�es dans le fichier :   ");
                progressBar.setVisible(false);
                filenameField.setText(exportFile.getAbsolutePath());
                openButton.setVisible(true);
            }
        });
    }

    @Override
    public void competitorExported(final Competitor competitor) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                progressBar.setValue((progressBar.getValue() + 1));
            }
        });
    }

    public void process(final File importFile, final List<CompetitorGroup> list, final CompetitionDescriptor compDesc, final File modelsFile, final GroupExportListener listener) {
        initProcessing();
        progressBar.setMaximum(CompetitorsGroupUtilities.calculateNbCompetitorsFromGroups(list));
        String output = importFile.getName();
        if (!output.endsWith(".xls")) {
            output += ".xls";
        }
        int i = 1;
        output = output.replace(".xls", "_poules_" + i + ".xls");
        final File exportDir = OjtConstants.EXPORT_DIRECTORY;
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        while (new File(exportDir, output).exists()) {
            output = output.replace("_poules_" + i + ".xls", "_poules_" + (i + 1) + ".xls");
            i++;
        }
        exportFile = new File(exportDir, output);
        logger.info("fabien >> in : " + importFile);
        logger.info("fabien >> out : " + output);
        logger.info("fabien >> export : " + exportFile);
        try {
            if (OJTConfiguration.getInstance().getPropertyAsBoolean(OJTConfiguration.EXPORT_FROM_MODELS) && (modelsFile == null)) {
                JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(this), "Le fichier de mod�le n'existe pas, on utilise le mod�le par d�faut", "Utilisation de mod�le", JOptionPane.WARNING_MESSAGE);
            }
            final CompetitorsGroupExporter exporter = CompetitorsGroupExporterFactory.getInstance().createCompetitorsGroupExporter(exportFile, modelsFile);
            exporter.addGroupExportListener(this);
            exporter.addGroupExportListener(listener);
            Executors.newSingleThreadExecutor().execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        exporter.exportCompetitors(list, compDesc);
                    } catch (final Exception ex) {
                        logger.error("", ex);
                        displayExportError(ex);
                    }
                }
            });
        } catch (final Exception ex) {
            logger.error("", ex);
            displayExportError(ex);
        }
    }

    private void displayExportError(final Exception ex) {
        filenameField.setText("Erreur lors de l'export : " + ex.getCause() == null ? ex.getMessage() : ex.getCause().getMessage());
        JOptionPane.showMessageDialog(this, "Erreur lors de l'export : " + ex.getCause() == null ? ex.getMessage() : ex.getCause().getMessage(), "Erreur d'export", JOptionPane.ERROR_MESSAGE);
    }

    private void buildGui() {
        setLayout(new GridBagLayout());
        title = new JLabel("Export en cours :                     ");
        add(title, new GridBagConstraints(0, 0, 1, 2, 0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        filenameField = new JTextField();
        filenameField.setEditable(false);
        filenameField.setBorder(null);
        filenameField.setText("Lancement de l'export en cours...");
        add(filenameField, new GridBagConstraints(1, 0, 1, 1, 2.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        progressBar = new JProgressBar();
        add(progressBar, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        openButton = new OjtButton("Ouvrir");
        openButton.setPreferredSize(new Dimension(80, 22));
        openButton.setMinimumSize(new Dimension(80, 22));
        openButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent evt) {
                try {
                    Runtime.getRuntime().exec("cmd /c start " + exportFile.getAbsolutePath());
                } catch (final IOException ex) {
                    logger.info("Unable to open file " + exportFile, ex);
                }
            }
        });
        if (System.getProperty("os.name").contains("Windows")) {
            add(openButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
            openButton.setVisible(false);
        }
    }

    private void initProcessing() {
        title.setText("Export en cours :                     ");
        filenameField.setText("Lancement de l'export en cours...");
        progressBar.setValue(0);
        progressBar.setVisible(true);
        openButton.setVisible(false);
    }
}
