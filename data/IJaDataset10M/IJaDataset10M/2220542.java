package aml.ramava.gui.exibition.participant;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import aml.ramava.common.RamavaException;
import aml.ramava.common.Utilities;
import aml.ramava.data.ExibitionEntityControler;
import aml.ramava.data.entities.Exibition;
import aml.ramava.data.entities.ExibitionParticipant;
import aml.ramava.data.entities.ExibitionParticipantProduct;
import aml.ramava.data.entities.comparators.ExibitionParticipantProductComparator;
import aml.ramava.gui.MainFrame;
import aml.ramava.gui.exibition.participant.product.ExibitionParticipantProductTableModel;

public class ExibitionParticipantProductDataPanel extends ExibitionParticipantDataPanel implements ListSelectionListener, MouseListener {

    /**
	 *
	 */
    private static final long serialVersionUID = -2163337664975491864L;

    private JScrollPane scrollPane;

    private JPanel pnlSummary;

    private JTable productTable;

    private JLabel lblSum;

    private JLabel lblVatFive;

    private JLabel lblVatNine;

    private JLabel lblTotalSum;

    private JLabel lblVat21;

    private JLabel lblVatZero;

    private JLabel lblDiscountTotal;

    private ExibitionParticipantProductTableModel tableModel;

    private ChooserPanelProducts dataChooserPanel;

    private DialogAddProductToParticipant dialogProduct;

    private DialogEditExibitionParticipant dialog;

    private ExibitionParticipant participant = null;

    private Exibition exibition = null;

    private DecimalFormat formater = Utilities.getLVLFormat();

    private boolean contentChanged = false;

    /**
     * Auto-generated main method to display this
     * JPanel inside a new JFrame.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new ExibitionParticipantProductDataPanel());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public ExibitionParticipantProductDataPanel() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            this.setLayout(thisLayout);
            {
                scrollPane = new JScrollPane();
                this.add(scrollPane, BorderLayout.CENTER);
                {
                    tableModel = new ExibitionParticipantProductTableModel();
                    tableModel.setProducts(new Vector());
                    productTable = new JTable();
                    scrollPane.setViewportView(productTable);
                    productTable.setModel(tableModel);
                    productTable.addMouseListener(this);
                }
            }
            {
                pnlSummary = new JPanel();
                BoxLayout pnlSummaryLayout = new BoxLayout(pnlSummary, javax.swing.BoxLayout.Y_AXIS);
                this.add(pnlSummary, BorderLayout.SOUTH);
                pnlSummary.setLayout(pnlSummaryLayout);
                pnlSummary.setBorder(BorderFactory.createTitledBorder("Kop�: "));
                {
                    lblSum = new JLabel();
                    pnlSummary.add(lblSum);
                    lblSum.setText("Kopā: ");
                    lblSum.setHorizontalAlignment(SwingConstants.RIGHT);
                    lblSum.setHorizontalTextPosition(SwingConstants.RIGHT);
                }
                {
                    lblVatZero = new JLabel();
                    pnlSummary.add(lblVatZero);
                    lblVatZero.setText("PVN 0%: ");
                }
                {
                    lblVatFive = new JLabel();
                    lblVatFive.setPreferredSize(new java.awt.Dimension(275, 21));
                    lblVatFive.setMinimumSize(new java.awt.Dimension(275, 21));
                    pnlSummary.add(lblVatFive);
                    lblVatFive.setText("PVN 5%: ");
                    lblVatFive.setHorizontalTextPosition(SwingConstants.RIGHT);
                }
                {
                    lblVatNine = new JLabel("PVN 9%: ");
                    pnlSummary.add(lblVatNine);
                }
                {
                    lblVat21 = new JLabel();
                    pnlSummary.add(lblVat21);
                    lblVat21.setText("PVN 21%: ");
                }
                {
                    lblDiscountTotal = new JLabel();
                    pnlSummary.add(lblDiscountTotal);
                    lblDiscountTotal.setText("Atlaide kop�: ");
                }
                {
                    lblTotalSum = new JLabel();
                    pnlSummary.add(lblTotalSum);
                    lblTotalSum.setText("Kopā ar PVN: ");
                }
            }
            dataChooserPanel = new ChooserPanelProducts();
            dialogProduct = new DialogAddProductToParticipant(MainFrame.MAIN_APP_FRAME);
            dialogProduct.setLocationRelativeTo(this);
            setParticipant(participant);
            setExibition(exibition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveChanges() {
        try {
            if (participant != null && exibition != null) {
                ExibitionEntityControler.addParticipantToExibition(participant, exibition);
            }
        } catch (RamavaException ex) {
            ex.printStackTrace();
            MainFrame.showErrorDialog(ex);
        }
    }

    public void setExibition(Exibition exibition) {
        dataChooserPanel.setExibition(exibition);
        this.exibition = exibition;
    }

    public void setParticipant(ExibitionParticipant participant) {
        this.participant = participant;
        if (participant != null) {
            tableModel.setProducts(participant.getProducts());
        } else {
            tableModel.setProducts(new Vector());
        }
        setLabelValues();
    }

    private void setLabelValues() {
        pnlSummary.remove(lblSum);
        {
            pnlSummary.remove(lblVatZero);
            pnlSummary.remove(lblVatFive);
            pnlSummary.remove(lblVatNine);
            pnlSummary.remove(lblVat21);
        }
        pnlSummary.remove(lblDiscountTotal);
        pnlSummary.remove(lblTotalSum);
        double vatZero = 0.0;
        double vatFive = 0.0;
        double vatTen = 0.0;
        double vat21 = 0.0;
        double sum = 0.0;
        double total = 0.0;
        double discountTotal = 0.0;
        if (participant != null) {
            Vector products = participant.getProducts();
            for (int i = 0; i < products.size(); i++) {
                ExibitionParticipantProduct p = (ExibitionParticipantProduct) products.get(i);
                sum += p.getTotal();
                if (p.getVatRate() > 0.0 && p.getVatRate() < 0.051) {
                    vatFive += p.getDiscountedTotal() * p.getVatRate();
                } else if (p.getVatRate() > 0.05 && p.getVatRate() < 0.101) {
                    vatTen += p.getDiscountedTotal() * p.getVatRate();
                } else if (p.getVatRate() > 0.09 && p.getVatRate() < 0.211) {
                    vat21 += p.getDiscountedTotal() * p.getVatRate();
                }
                discountTotal += p.getTotalDiscount();
                total += (p.getDiscountedTotal() + (p.getDiscountedTotal() * p.getVatRate()));
            }
        }
        pnlSummary.add(lblSum);
        {
            if (vatZero > 0.0) {
                pnlSummary.add(lblVatZero);
            }
            if (vatFive > 0.0) {
                pnlSummary.add(lblVatFive);
            }
            if (vatTen > 0.0) {
                pnlSummary.add(lblVatNine);
            }
            if (vat21 > 0.0) {
                pnlSummary.add(lblVat21);
            }
        }
        if (discountTotal > 0.0) {
            pnlSummary.add(lblDiscountTotal);
        }
        pnlSummary.add(lblTotalSum);
        lblSum.setText("Kop�: " + formater.format(sum));
        lblVatZero.setText("PVN 0%: " + formater.format(vatZero));
        lblVatFive.setText("PVN 5%: " + formater.format(vatFive));
        lblVatNine.setText("PVN 10%: " + formater.format(vatTen));
        lblVat21.setText("PVN 21%: " + formater.format(vat21));
        lblDiscountTotal.setText("Atlaide kop�: " + formater.format(discountTotal));
        lblTotalSum.setText("Kop� ar PVN: " + formater.format(total));
    }

    public boolean stopEditing() {
        return !contentChanged;
    }

    public void undoChanges() {
        contentChanged = false;
        try {
            participant.setProducts(ExibitionEntityControler.getExibitionParticipantProducts(participant));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tableModel.setProducts(participant.getProducts());
    }

    public void componentHidden(ComponentEvent e) {
        super.componentHidden(e);
    }

    public void startEdit() {
        if (participant == null || exibition == null) {
            return;
        }
        dataChooserPanel.setExibition(exibition);
        dataChooserPanel.setParticipant(participant);
        if (dialog == null) {
            dialog = new DialogEditExibitionParticipant(MainFrame.MAIN_APP_FRAME);
        }
        dialog.setDataPanel(dataChooserPanel);
        dialog.setVisible(true);
        if (!dialog.isCancelPressed()) {
            tableModel.setProducts(dataChooserPanel.getParticipantProducts());
            contentChanged = true;
        }
    }

    private void editProductProperties(ExibitionParticipantProduct product) {
        if (product != null) {
            dialogProduct.setParticipantProduct(product);
            dialogProduct.setEditDialog(true);
            dialogProduct.setVisible(true);
            product = dialogProduct.getParticipantProduct();
            if (product != null) {
                try {
                    ExibitionEntityControler.updateExibitionParticipantProduct(product, participant);
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                    MainFrame.showStatusMessage("K��DA: " + ex.getMessage());
                    return;
                } catch (RamavaException ex) {
                    ex.printStackTrace();
                    MainFrame.showStatusMessage("K��DA: " + ex.getMessage());
                    return;
                }
                Vector products = participant.getProducts();
                if (products.contains(product)) {
                    products.remove(product);
                }
                products.add(product);
                Collections.sort(products, new ExibitionParticipantProductComparator());
                setParticipant(participant);
                MainFrame.showStatusMessage("Dal�bnieka (" + participant.getName() + ") produkts (" + product.getName() + ") izmain�ts veiksm�gi");
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() >= 2) {
            int row = productTable.rowAtPoint(e.getPoint());
            editProductProperties(tableModel.getProductAtRow(row));
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
