package org.scrinch.gui;

import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import com.lowagie.text.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.scrinch.model.Item;
import org.scrinch.model.ScrinchEnvToolkit;
import org.scrinch.model.Sprint;

public class SprintGraphDialog extends javax.swing.JDialog implements Pageable {

    private Sprint sprint;

    private JFreeChart chart;

    public SprintGraphDialog(java.awt.Frame parent, boolean modal, Sprint sprint) {
        super(parent, modal);
        initComponents();
        ScrinchGuiToolkit.centerFrame(this);
        this.sprint = sprint;
        createGraph();
    }

    private CategoryDataset createDataSet() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Collection<Item> itemList = sprint.getItemList();
        List<Date> dateList = sprint.getWorkingDates();
        String theoreticalSeriesLabel = "Objective";
        int theoreticalWork = 0;
        for (Item item : itemList) {
            if (item.isActive()) {
                theoreticalWork += item.getEvaluation().intValue();
            }
        }
        String realSeriesLabel = "Result";
        int dateWork = 0;
        dataset.addValue(0, realSeriesLabel, "Beginning of sprint");
        dataset.addValue(0, theoreticalSeriesLabel, "Beginning of sprint");
        int i = 0;
        for (Date date : dateList) {
            String category = ScrinchGuiToolkit.getDefaultDayFormat().format(date);
            if (date.compareTo(ScrinchEnvToolkit.getInstance().getCurrentDate()) <= 0) {
                for (Item item : itemList) {
                    Item.Visa lastVisa = item.getLastDoneVisaIfTaskDoneInSprint(sprint);
                    if (lastVisa != null && lastVisa.date.equals(date) && (lastVisa.status == Item.Status.OK || lastVisa.status == Item.Status.DONE)) {
                        dateWork += item.getEvaluation().intValue();
                    }
                }
                dataset.addValue((double) dateWork, realSeriesLabel, category);
            }
            dataset.addValue(((double) theoreticalWork * (++i) / (double) dateList.size()), theoreticalSeriesLabel, category);
        }
        return dataset;
    }

    private void createGraph() {
        CategoryDataset dataset = createDataSet();
        chart = ChartFactory.createLineChart(sprint.getTitle(), "Date", "Work", dataset, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart, false);
        graphPanel.add(chartPanel, BorderLayout.CENTER);
        CategoryPlot plot = chart.getCategoryPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
        plot.setDomainGridlinesVisible(true);
    }

    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        Printable printable = new Printable() {

            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                graphPanel.getComponent(0).paint(graphics);
                return Printable.PAGE_EXISTS;
            }
        };
        return printable;
    }

    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        PageFormat pageFormat = new PageFormat();
        pageFormat.setOrientation(PageFormat.LANDSCAPE);
        return pageFormat;
    }

    public int getNumberOfPages() {
        return 1;
    }

    private void exportToPdfFile() throws FileNotFoundException, DocumentException, IOException {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        File defaultFile = new File(System.getProperty("user.dir") + File.separator + this.sprint.getTitle().replace(' ', '_') + ".pdf");
        fileChooser.setSelectedFile(defaultFile);
        if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(this)) {
            Rectangle pageRect = PageSize.A4.rotate();
            int margin = 50;
            com.lowagie.text.Document iTextDocument = new com.lowagie.text.Document(pageRect, margin, margin, margin, margin);
            PdfWriter writer = PdfWriter.getInstance(iTextDocument, new FileOutputStream(fileChooser.getSelectedFile()));
            iTextDocument.open();
            PdfContentByte cb = writer.getDirectContent();
            int width = (int) (pageRect.width() - (2.0f * margin));
            int height = (int) (pageRect.height() - (2.0f * margin));
            PdfTemplate tp = cb.createTemplate(width, height);
            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
            Rectangle2D r2d = new Rectangle2D.Double(0, 0, width, height);
            chart.draw(g2, r2d);
            g2.dispose();
            cb.addTemplate(tp, margin, margin);
            iTextDocument.close();
            org.scrinch.ostools.Runtime.pdfOpen(fileChooser.getSelectedFile().getPath());
        }
    }

    private void initComponents() {
        graphPanel = new javax.swing.JPanel();
        menuPanel = new javax.swing.JPanel();
        exportToPDFButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        graphPanel.setLayout(new java.awt.BorderLayout());
        graphPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().add(graphPanel, java.awt.BorderLayout.CENTER);
        exportToPDFButton.setText("Export to PDF");
        exportToPDFButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportToPDFButtonActionPerformed(evt);
            }
        });
        menuPanel.add(exportToPDFButton);
        getContentPane().add(menuPanel, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void exportToPDFButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            exportToPdfFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void print() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        boolean doPrint = printerJob.printDialog();
        if (doPrint) {
            try {
                printerJob.setPageable(this);
                printerJob.print();
            } catch (PrinterException exception) {
                System.err.println("Printing error: " + exception);
            }
        }
    }

    private javax.swing.JButton exportToPDFButton;

    private javax.swing.JPanel graphPanel;

    private javax.swing.JPanel menuPanel;
}
