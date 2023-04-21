package dmsgui;

import java.awt.Cursor;
import java.util.Date;
import java.util.Hashtable;
import javax.swing.JLabel;
import net.sf.dms.lib.DMSManagerFactory;
import net.sf.dms.lib.document.Document;
import net.sf.dms.lib.group.Group;

/**
 *
 * @author  daniel
 */
public class StatisticsFrame extends javax.swing.JFrame {

    private static StatisticsFrame statisticsFrame = null;

    /** Creates new form StatisticsFrame */
    private StatisticsFrame() {
        initComponents();
        RefreshBus.getBus().add(new Refresher(hashCode()) {

            public void run() {
                refresh();
            }
        });
        setVisible(true);
        refresh();
    }

    private void initComponents() {
        jLabel5 = new javax.swing.JLabel();
        documentsNumberLabelPrefix = new javax.swing.JLabel();
        documentsNumberLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        documentsAverageSizeLabelPrefix = new javax.swing.JLabel();
        documentsAverageSizeLabel = new javax.swing.JLabel();
        documentMaxAgeLabelPrefix = new javax.swing.JLabel();
        documentMinAgeLabelPrefix = new javax.swing.JLabel();
        documentAverageAgeLabelPrefix = new javax.swing.JLabel();
        groupsNumberLabelPrefix = new javax.swing.JLabel();
        documentMaxAgeLabel = new javax.swing.JLabel();
        documentMinAgeLabel = new javax.swing.JLabel();
        documentAverageAgeLabel = new javax.swing.JLabel();
        groupsNumberLabel = new javax.swing.JLabel();
        groupsSmallestLabelPrefix = new javax.swing.JLabel();
        groupsSmallestLabel = new javax.swing.JLabel();
        groupsSmallestLabelSuffix = new javax.swing.JLabel();
        groupsLargestLabelPrefix = new javax.swing.JLabel();
        groupsLargestLabel = new javax.swing.JLabel();
        groupsLargestLabelSuffix = new javax.swing.JLabel();
        documentsAverageSizeSuffix = new javax.swing.JLabel();
        documentMaxAgeLabelSuffix = new javax.swing.JLabel();
        documentMinAgeLabelSuffix = new javax.swing.JLabel();
        documentAverageAgeLabelSuffix = new javax.swing.JLabel();
        groupsAverageLabelPrefix = new javax.swing.JLabel();
        groupsAverageLabel = new javax.swing.JLabel();
        groupsAverageLabelSuffix = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        fileTypesLabel = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        documentsTotalSizeLabelPrefix = new javax.swing.JLabel();
        documentsTotalSizeLabel = new javax.swing.JLabel();
        documentsTotalSizeLabelSuffix = new javax.swing.JLabel();
        documentsMinSizeLabelPrefix = new javax.swing.JLabel();
        documentsMinSizeLabel = new javax.swing.JLabel();
        documentsMinSizeLabelSuffix = new javax.swing.JLabel();
        documentsMaxSizeLabelPrefix = new javax.swing.JLabel();
        documentsMaxSizeLabel = new javax.swing.JLabel();
        documentsMaxSizeLabelSuffix = new javax.swing.JLabel();
        jLabel5.setText("jLabel5");
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Statistics");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        documentsNumberLabelPrefix.setText("Number of documents:");
        documentsNumberLabel.setText("0");
        documentsAverageSizeLabelPrefix.setText("Average file size:");
        documentsAverageSizeLabel.setText("0");
        documentMaxAgeLabelPrefix.setText("Maximum age:");
        documentMinAgeLabelPrefix.setText("Minimum age:");
        documentAverageAgeLabelPrefix.setText("Average age:");
        groupsNumberLabelPrefix.setText("Number of groups:");
        documentMaxAgeLabel.setText("0");
        documentMinAgeLabel.setText("0");
        documentAverageAgeLabel.setText("0");
        groupsNumberLabel.setText("0");
        groupsSmallestLabelPrefix.setText("Smallest group:");
        groupsSmallestLabel.setText("0");
        groupsSmallestLabelSuffix.setText("documents");
        groupsLargestLabelPrefix.setText("Largest group:");
        groupsLargestLabel.setText("0");
        groupsLargestLabelSuffix.setText("documents");
        documentsAverageSizeSuffix.setText("bytes");
        documentMaxAgeLabelSuffix.setText("days");
        documentMinAgeLabelSuffix.setText("days");
        documentAverageAgeLabelSuffix.setText("days");
        groupsAverageLabelPrefix.setText("Average size:");
        groupsAverageLabel.setText("0");
        groupsAverageLabelSuffix.setText("documents");
        jLabel1.setText("Number of file types:");
        fileTypesLabel.setText("0");
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        documentsTotalSizeLabelPrefix.setText("Total amount of data:");
        documentsTotalSizeLabel.setText("0");
        documentsTotalSizeLabelSuffix.setText("bytes");
        documentsMinSizeLabelPrefix.setText("Minimum file size:");
        documentsMinSizeLabel.setText("0");
        documentsMinSizeLabelSuffix.setText("bytes");
        documentsMaxSizeLabelPrefix.setText("Maximum file size:");
        documentsMaxSizeLabel.setText("0");
        documentsMaxSizeLabelSuffix.setText("bytes");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(groupsLargestLabelPrefix).addComponent(groupsSmallestLabelPrefix).addComponent(groupsAverageLabelPrefix, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(groupsNumberLabelPrefix, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel1)).addGap(20, 20, 20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(fileTypesLabel).addGroup(layout.createSequentialGroup().addComponent(groupsSmallestLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(groupsSmallestLabelSuffix)).addComponent(groupsNumberLabel).addGroup(layout.createSequentialGroup().addComponent(groupsLargestLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(groupsLargestLabelSuffix)).addGroup(layout.createSequentialGroup().addComponent(groupsAverageLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(groupsAverageLabelSuffix)))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(documentsNumberLabelPrefix).addComponent(documentsAverageSizeLabelPrefix).addComponent(documentsTotalSizeLabelPrefix).addComponent(documentAverageAgeLabelPrefix).addComponent(documentMinAgeLabelPrefix).addComponent(documentMaxAgeLabelPrefix)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(documentsNumberLabel).addComponent(documentAverageAgeLabel).addComponent(documentMinAgeLabel).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(documentsTotalSizeLabel, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE).addComponent(documentsAverageSizeLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(documentsMinSizeLabel, javax.swing.GroupLayout.Alignment.LEADING).addComponent(documentsMaxSizeLabel, javax.swing.GroupLayout.Alignment.LEADING)).addComponent(documentMaxAgeLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(documentsMaxSizeLabelSuffix).addComponent(documentsMinSizeLabelSuffix).addComponent(documentMaxAgeLabelSuffix).addComponent(documentsTotalSizeLabelSuffix).addComponent(documentsAverageSizeSuffix).addComponent(documentMinAgeLabelSuffix).addComponent(documentAverageAgeLabelSuffix))))).addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(closeButton))).addGap(47, 47, 47)).addGroup(layout.createSequentialGroup().addComponent(documentsMinSizeLabelPrefix).addContainerGap(173, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addComponent(documentsMaxSizeLabelPrefix).addContainerGap(169, Short.MAX_VALUE)))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(documentsNumberLabelPrefix).addComponent(documentsNumberLabel)).addGap(6, 6, 6).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(documentsMaxSizeLabelPrefix).addComponent(documentsMaxSizeLabel).addComponent(documentsMaxSizeLabelSuffix)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(documentsMinSizeLabelPrefix).addComponent(documentsMinSizeLabel).addComponent(documentsMinSizeLabelSuffix)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(documentsAverageSizeLabelPrefix).addComponent(documentsAverageSizeLabel).addComponent(documentsAverageSizeSuffix)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(documentsTotalSizeLabelPrefix).addComponent(documentsTotalSizeLabel).addComponent(documentsTotalSizeLabelSuffix)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(documentMaxAgeLabel).addComponent(documentMaxAgeLabelSuffix).addComponent(documentMaxAgeLabelPrefix)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(documentMinAgeLabelPrefix).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(documentAverageAgeLabelPrefix)).addGroup(layout.createSequentialGroup().addComponent(documentMinAgeLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(documentAverageAgeLabel).addComponent(documentAverageAgeLabelSuffix))).addComponent(documentMinAgeLabelSuffix)).addGap(4, 4, 4).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(groupsNumberLabelPrefix).addComponent(groupsNumberLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(groupsLargestLabelPrefix).addComponent(groupsLargestLabel).addComponent(groupsLargestLabelSuffix)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(groupsSmallestLabelPrefix).addComponent(groupsSmallestLabel).addComponent(groupsSmallestLabelSuffix)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(groupsAverageLabelPrefix).addComponent(groupsAverageLabel).addComponent(groupsAverageLabelSuffix)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(fileTypesLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE).addComponent(closeButton, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap()));
        pack();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        quit();
    }

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        quit();
    }

    /**
     * StatisticsFrame is a singleton. This method returns the instance of StatisticsFrame,
     * creating a new one if necessary.
     **/
    public static StatisticsFrame getStatisticsFrame() {
        if (statisticsFrame == null) {
            statisticsFrame = new StatisticsFrame();
        }
        statisticsFrame.toFront();
        statisticsFrame.requestFocus();
        statisticsFrame.refresh();
        return statisticsFrame;
    }

    private javax.swing.JButton closeButton;

    private javax.swing.JLabel documentAverageAgeLabel;

    private javax.swing.JLabel documentAverageAgeLabelPrefix;

    private javax.swing.JLabel documentAverageAgeLabelSuffix;

    private javax.swing.JLabel documentMaxAgeLabel;

    private javax.swing.JLabel documentMaxAgeLabelPrefix;

    private javax.swing.JLabel documentMaxAgeLabelSuffix;

    private javax.swing.JLabel documentMinAgeLabel;

    private javax.swing.JLabel documentMinAgeLabelPrefix;

    private javax.swing.JLabel documentMinAgeLabelSuffix;

    private javax.swing.JLabel documentsAverageSizeLabel;

    private javax.swing.JLabel documentsAverageSizeLabelPrefix;

    private javax.swing.JLabel documentsAverageSizeSuffix;

    private javax.swing.JLabel documentsMaxSizeLabel;

    private javax.swing.JLabel documentsMaxSizeLabelPrefix;

    private javax.swing.JLabel documentsMaxSizeLabelSuffix;

    private javax.swing.JLabel documentsMinSizeLabel;

    private javax.swing.JLabel documentsMinSizeLabelPrefix;

    private javax.swing.JLabel documentsMinSizeLabelSuffix;

    private javax.swing.JLabel documentsNumberLabel;

    private javax.swing.JLabel documentsNumberLabelPrefix;

    private javax.swing.JLabel documentsTotalSizeLabel;

    private javax.swing.JLabel documentsTotalSizeLabelPrefix;

    private javax.swing.JLabel documentsTotalSizeLabelSuffix;

    private javax.swing.JLabel fileTypesLabel;

    private javax.swing.JLabel groupsAverageLabel;

    private javax.swing.JLabel groupsAverageLabelPrefix;

    private javax.swing.JLabel groupsAverageLabelSuffix;

    private javax.swing.JLabel groupsLargestLabel;

    private javax.swing.JLabel groupsLargestLabelPrefix;

    private javax.swing.JLabel groupsLargestLabelSuffix;

    private javax.swing.JLabel groupsNumberLabel;

    private javax.swing.JLabel groupsNumberLabelPrefix;

    private javax.swing.JLabel groupsSmallestLabel;

    private javax.swing.JLabel groupsSmallestLabelPrefix;

    private javax.swing.JLabel groupsSmallestLabelSuffix;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JProgressBar progressBar;

    private void refresh() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        progressBar.setValue(progressBar.getMinimum());
        Document[] documents = new Document[0];
        documents = DMSManagerFactory.getDocumentManager().getAll().toArray(documents);
        documentsNumberLabel.setText(new Integer(documents.length).toString());
        if (documents.length > 0) {
            Long documentsMinSize = documents[0].getSize();
            Long documentsMaxSize = documents[0].getSize();
            Long documentsTotalSize = new Long(0);
            Date documentsMinAge = documents[0].getLastAccessed();
            Date documentsMaxAge = documents[0].getLastAccessed();
            Long documentsTotalAge = new Long(0);
            Date today = new Date();
            for (int i = 0; i < documents.length; i++) {
                documentsTotalSize += documents[i].getSize();
                if (documents[i].getSize() >= documentsMaxSize) {
                    documentsMaxSize = documents[i].getSize();
                } else {
                    documentsMinSize = documents[i].getSize();
                }
                documentsTotalAge += documents[i].getLastAccessed().getTime();
                if (documents[i].getLastAccessed().compareTo(documentsMaxAge) >= 0) {
                    documentsMaxAge = documents[i].getLastAccessed();
                } else {
                    documentsMinAge = documents[i].getLastAccessed();
                }
                progressBar.setValue(i / documents.length * (progressBar.getMaximum() - progressBar.getMinimum()) / 2);
            }
            documentsMaxSizeLabel.setText(toScientific(documentsMaxSize));
            documentsMinSizeLabel.setText(toScientific(documentsMinSize));
            documentsAverageSizeLabel.setText(toScientific(documentsTotalSize / documents.length));
            documentsTotalSizeLabel.setText(toScientific(documentsTotalSize));
            documentMaxAgeLabel.setText(new Float((int) Math.floor((today.getTime() - documentsMaxAge.getTime()) / 8640000) / 10).toString());
            documentMinAgeLabel.setText(new Float((int) Math.floor((today.getTime() - documentsMinAge.getTime()) / 8640000) / 10).toString());
            documentAverageAgeLabel.setText(new Float(((int) Math.floor(today.getTime() - documentsTotalAge / documents.length) / 8640000) / 10).toString());
        } else {
            documentsMaxSizeLabel.setText("0");
            documentsMinSizeLabel.setText("0");
            documentsAverageSizeLabel.setText("0");
            documentsTotalSizeLabel.setText("0");
            documentMaxAgeLabel.setText("0");
            documentMinAgeLabel.setText("0");
            documentAverageAgeLabel.setText("0");
        }
        Group[] groups = new Group[0];
        groups = DMSManagerFactory.getGroupManager().getAll().toArray(groups);
        groupsNumberLabel.setText(new Integer(groups.length).toString());
        if (groups.length > 0) {
            Integer groupsMinSize = groups[0].documentsSize();
            Integer groupsMaxSize = groups[0].documentsSize();
            Integer groupsTotalSize = 0;
            for (int i = 0; i < groups.length; i++) {
                groupsTotalSize += groups[i].documentsSize();
                if (groups[i].documentsSize() >= groupsMaxSize) {
                    groupsMaxSize = groups[i].documentsSize();
                } else {
                    groupsMinSize = groups[i].documentsSize();
                }
                progressBar.setValue(i / groups.length * (progressBar.getMaximum() - progressBar.getMinimum()) / 2 + (progressBar.getMaximum() - progressBar.getMinimum()) / 2);
            }
            groupsLargestLabel.setText(new Integer(groupsMaxSize).toString());
            groupsSmallestLabel.setText(new Integer(groupsMinSize).toString());
            groupsAverageLabel.setText(new Integer(groupsTotalSize / groups.length).toString());
        } else {
            groupsLargestLabel.setText("0");
            groupsSmallestLabel.setText("0");
            groupsAverageLabel.setText("0");
        }
        fileTypesLabel.setText(new Integer(DMSManagerFactory.getFileTypeManager().getAll().size()).toString());
        progressBar.setValue(progressBar.getMaximum());
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /** Returns a String which represents the value in a scientifically shorter form.
     * Example: 4096 => 4K
     *
     */
    private String toScientific(long value) {
        if (value == 0) {
            return "0";
        }
        String[] prefix = { "", "K", "M", "G", "T" };
        int power = (int) Math.floor(Math.log(value) / Math.log(1024));
        double base = value / Math.pow(1024, power);
        base = ((int) base * 10) / 10;
        return base + prefix[power];
    }

    private void quit() {
        RefreshBus.getBus().remove(hashCode());
        statisticsFrame = null;
        dispose();
    }
}
