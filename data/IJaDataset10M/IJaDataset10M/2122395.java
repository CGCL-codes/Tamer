package view;

import connectivity.QueryManager;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import main.WinkelApplication;
import model.Customer;
import model.Product;

public class Payment extends JPanel implements MouseListener, ActionListener {

    private final int verticalPosition = 80;

    private final int productOffset = 20;

    private final int formOffset = 30;

    private JTextField tfNaam;

    private JTextField tfAddress;

    private JTextField tfPostcode;

    private JTextField tfWoonplaats;

    private JComboBox cmbPayMethod;

    private JTextField tfNote;

    private JComboBox customerBox;

    private final String[] payMethods = { "Vooraf per bank", "Onder rembours", "Geen" };

    public Payment() {
        super();
        this.setLayout(null);
        initComponents();
    }

    private void initComponents() {
        addTitle();
        addProductList();
        addForm();
    }

    private void addTitle() {
        JLabel lblTitle1 = new JLabel();
        lblTitle1.setText("Winkelapplicatie");
        lblTitle1.setBounds(20, 20, 150, 20);
        lblTitle1.setFont(WinkelApplication.FONT_16_BOLD);
        lblTitle1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblTitle1.addMouseListener(this);
        add(lblTitle1);
        JLabel lblTitle2 = new JLabel();
        lblTitle2.setText("-");
        lblTitle2.setBounds(170, 20, 20, 20);
        lblTitle2.setFont(WinkelApplication.FONT_16_BOLD);
        add(lblTitle2);
        JLabel lblTitle3 = new JLabel();
        lblTitle3.setText("Betaling");
        lblTitle3.setBounds(190, 20, 500, 20);
        lblTitle3.setFont(WinkelApplication.FONT_16_BOLD);
        add(lblTitle3);
    }

    private void addProductList() {
        List<Product> products = WinkelApplication.getBasket().getProducts();
        JLabel lblProductHeader = new JLabel();
        lblProductHeader.setText("Producten");
        lblProductHeader.setBounds(20, 60, 150, 20);
        lblProductHeader.setFont(WinkelApplication.FONT_10_BOLD);
        add(lblProductHeader);
        JLabel lblAmountHeader = new JLabel();
        lblAmountHeader.setText("Aantal");
        lblAmountHeader.setBounds(400, 60, 150, 20);
        lblAmountHeader.setFont(WinkelApplication.FONT_10_BOLD);
        add(lblAmountHeader);
        JLabel lblPriceHeader = new JLabel();
        lblPriceHeader.setText("Prijs");
        lblPriceHeader.setBounds(480, 60, 150, 20);
        lblPriceHeader.setFont(WinkelApplication.FONT_10_BOLD);
        add(lblPriceHeader);
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            JLabel lblProduct = new JLabel(product.getName());
            lblProduct.setBounds(20, verticalPosition + i * productOffset, 420, 20);
            lblProduct.setFont(WinkelApplication.FONT_10_PLAIN);
            add(lblProduct);
            JLabel lblAmount = new JLabel(String.valueOf(WinkelApplication.getBasket().getProductAmount(product)));
            lblAmount.setBounds(410, verticalPosition + i * productOffset, 70, 20);
            lblAmount.setFont(WinkelApplication.FONT_10_PLAIN);
            add(lblAmount);
            JLabel lblPrice = new JLabel(WinkelApplication.CURRENCY + product.getPrice());
            lblPrice.setBounds(480, verticalPosition + i * productOffset, 70, 20);
            lblPrice.setFont(WinkelApplication.FONT_10_PLAIN);
            add(lblPrice);
        }
        JLabel lblTotal = new JLabel("Totaal: ");
        lblTotal.setBounds(20, verticalPosition + products.size() * productOffset, 50, 20);
        lblTotal.setFont(WinkelApplication.FONT_10_BOLD);
        add(lblTotal);
        JLabel lblTotalPrice = new JLabel(WinkelApplication.CURRENCY + WinkelApplication.getBasket().getTotalCosts());
        lblTotalPrice.setBounds(480, verticalPosition + products.size() * productOffset, 70, 20);
        lblTotalPrice.setFont(WinkelApplication.FONT_10_BOLD);
        add(lblTotalPrice);
    }

    private void addForm() {
        List<Product> products = WinkelApplication.getBasket().getProducts();
        JLabel lblFormTitle = new JLabel("Verzendgegevens");
        lblFormTitle.setBounds(20, verticalPosition + products.size() * productOffset + (formOffset * 2), 150, 20);
        lblFormTitle.setFont(WinkelApplication.FONT_12_BOLD);
        add(lblFormTitle);
        QueryManager qm = WinkelApplication.getQueryManager();
        List<Customer> customerList = qm.getCustomerList();
        System.out.println(customerList.get(0).getName());
        String[] customers = new String[customerList.size()];
        for (int i = 0; i < customers.length; i++) {
            customers[i] = customerList.get(i).getId() + ", " + customerList.get(i).getName();
        }
        customerBox = new JComboBox(customers);
        customerBox.setBounds(20, verticalPosition + products.size() * productOffset + (formOffset * 3), 100, 20);
        customerBox.setFont(WinkelApplication.FONT_10_BOLD);
        customerBox.setEditable(false);
        add(customerBox);
        JLabel lblPayMethod = new JLabel("Betaalmethode:");
        lblPayMethod.setBounds(20, verticalPosition + products.size() * productOffset + (formOffset * 5), 100, 20);
        lblPayMethod.setFont(WinkelApplication.FONT_10_BOLD);
        add(lblPayMethod);
        cmbPayMethod = new JComboBox(payMethods);
        cmbPayMethod.setBounds(120, verticalPosition + products.size() * productOffset + (formOffset * 5), 250, 20);
        cmbPayMethod.setFont(WinkelApplication.FONT_10_BOLD);
        add(cmbPayMethod);
        JLabel lblNote = new JLabel("Opmerking:");
        lblNote.setBounds(20, verticalPosition + products.size() * productOffset + (formOffset * 6), 100, 20);
        lblNote.setFont(WinkelApplication.FONT_10_BOLD);
        add(lblNote);
        tfNote = new JTextField();
        tfNote.setBounds(120, verticalPosition + products.size() * productOffset + (formOffset * 6), 250, 80);
        tfNote.setFont(WinkelApplication.FONT_10_BOLD);
        add(tfNote);
        JButton btnSend = new JButton("Verzend bestelling");
        btnSend.setBounds(120, verticalPosition + products.size() * productOffset + (formOffset * 9), 150, 20);
        btnSend.setFont(WinkelApplication.FONT_10_BOLD);
        btnSend.addActionListener(this);
        this.add(btnSend);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        int klantID = Integer.parseInt(customerBox.getSelectedItem().toString().split(",")[0]);
        String opmerking = tfNote.getText();
        String betaalmethode = (String) cmbPayMethod.getSelectedItem();
        int order_id = WinkelApplication.getQueryManager().setOrder(WinkelApplication.getBasket(), klantID, opmerking, betaalmethode);
        WinkelApplication.getBasket().empty();
        WinkelApplication.getInstance().showPanel(new OrderSend(order_id));
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        WinkelApplication.getInstance().showPanel(new view.CategoryList());
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.drawLine(20, 45, 540, 45);
    }
}
