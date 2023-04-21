package sg.edu.nus.iss.se07.ui.widget;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import sg.edu.nus.iss.se07.bc.DiscountManager;
import sg.edu.nus.iss.se07.bc.Member;
import sg.edu.nus.iss.se07.bc.Product;
import sg.edu.nus.iss.se07.common.Tuple4;
import sg.edu.nus.iss.se07.common.exceptions.AppException;
import sg.edu.nus.iss.se07.main.AppController;
import sg.edu.nus.iss.se07.utils.StringUtil;

public class CheckOutPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private ApplicationMainForm mainForm;

    private Font f;

    private GridBagLayout layout;

    private Panel frmPanel;

    private Panel chkPanel;

    private Panel btnPanel;

    private Panel btnListPanel;

    private Panel chkMemberPanel;

    private Label lblMemberId;

    private Label lblDeem;

    private Label lblDeemValue;

    private Label lblBarCode;

    private Label lblQty;

    private Label lblTotal;

    private Label lblTotalValue;

    private Label lblDeemUsed;

    private Label lblDiscount;

    private Label lblGrandTotal;

    private Label lblGrandTotalValue;

    private Label lblCheckOutList;

    private Label lblDiscountValue;

    private Label lblChkMember;

    private Label lblchk;

    private Label lblPaymentAmt;

    private Label lblRefund;

    private Label lblRefundValue;

    private TextField txtMemberId;

    private TextField txtBarCode;

    private TextField txtQty;

    private TextField txtDeemUsed;

    private TextField txtPaymentAmt;

    private CheckboxGroup cbg;

    private Checkbox chkNo;

    private Checkbox chkYes;

    private CheckboxGroup cbgMember;

    private Checkbox chkMember;

    private Checkbox chkNonMember;

    private Button btnCheckOut;

    private Button btnCancel;

    private Button btnAddItem;

    private Button btnRemoveItem;

    private List lstCheckOut;

    private DiscountManager discountManager = new DiscountManager();

    private Member member = null;

    private Product product = null;

    private AppController appController = null;

    private int qty = 0;

    private int finaldeem = 0;

    private int dis = 0;

    private int deempoint = 0;

    private float totalAmt = 0.0f;

    private float totalforeach = 0.0f;

    private float grandtotal = 0.0f;

    private ArrayList<Integer> qtyList = new ArrayList<Integer>();

    private ArrayList<Float> totalList = new ArrayList<Float>();

    private ArrayList<Product> productList = new ArrayList<Product>();

    ArrayList<Tuple4<String, String, Integer, Date>> purchases = new ArrayList<Tuple4<String, String, Integer, Date>>();

    public static final String Product_REPORT_FORMAT = "%1$5s\t%2$-30s\t%3$-15s\t%4$-5s\t%5$-10s\n";

    int no = 0;

    private ViewReportPanel viewReport = null;

    public CheckOutPanel() {
        add(createFormPanel());
        setSize(450, 450);
    }

    public CheckOutPanel(ApplicationMainForm mainForm) {
        this();
        this.mainForm = mainForm;
    }

    private void attachComponent(Component c, int gridx, int gridy, int gridwidth, int gridheight) {
        attachComponent(c, gridx, gridy, gridwidth, gridheight, GridBagConstraints.BOTH);
    }

    private void attachComponent(Component c, int gridx, int gridy, int gridwidth, int gridheight, int fill) {
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridx = gridx;
        constraint.gridy = gridy;
        constraint.gridwidth = gridwidth;
        constraint.gridheight = gridheight;
        constraint.fill = fill;
        constraint.insets = new Insets(3, 3, 3, 3);
        constraint.weightx = 5.0;
        constraint.weighty = 1.0;
        frmPanel.add(c);
        layout.setConstraints(c, constraint);
    }

    private Panel createButtonPanel() {
        Panel p = new Panel();
        p.setLayout(new FlowLayout());
        appController = AppController.getInstance();
        btnCheckOut = new Button("Check Out");
        ActionListener l = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String listForReorder = "List of Product below TrashHold are";
                btnCheckOut.setEnabled(false);
                try {
                    if (txtPaymentAmt.getText().trim().length() != 0) {
                        for (int i = 0; i < productList.size(); i++) {
                            Product p = productList.get(i);
                            int qty = qtyList.get(i);
                            addpurchase(p, txtMemberId.getText().toString(), qty);
                        }
                        if (appController.addTransactions(purchases, finaldeem)) {
                            ArrayList<Product> plist = appController.getProductBelowThreshold();
                            if (plist != null) {
                                for (int j = 0; j < plist.size(); j++) {
                                    Product p = plist.get(j);
                                    listForReorder += p.getProductName();
                                    if (j < plist.size() - 1) {
                                        listForReorder += ",";
                                    }
                                    MessageDialog m = new MessageDialog(mainForm, "Product Below Threshold", listForReorder);
                                    m.setResizable(false);
                                    m.setVisible(true);
                                }
                            }
                            MessageDialog m = new MessageDialog(mainForm, "Adding Transaction", "Transaction is successfully added.");
                            m.setResizable(false);
                            m.setVisible(true);
                            clear();
                            String reportfilename = appController.printTransactionReceipt(purchases, finaldeem, txtMemberId.getText(), totalAmt, deempoint, dis, grandtotal);
                            viewReport = new ViewReportPanel(mainForm, reportfilename, "Transaction Report");
                            viewReport.setVisible(true);
                        }
                    } else {
                        MessageDialog m = new MessageDialog(mainForm, "Enter Payment Amount", "Enter Payment Amount Before CheckOut");
                        m.setResizable(false);
                        m.setVisible(true);
                    }
                } catch (AppException ex) {
                    MessageDialog m = new MessageDialog(mainForm, "Error in Transaction.", ex.getMessage());
                    m.setResizable(false);
                    m.setVisible(true);
                }
            }
        };
        btnCheckOut.addActionListener(l);
        btnCancel = new Button("Cancel");
        l = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                mainForm.setLblTitle("");
                setVisible(false);
            }
        };
        btnCancel.addActionListener(l);
        p.add(btnCheckOut);
        p.add(btnCancel);
        return p;
    }

    private Panel createCheckOutPanel() {
        Panel p = new Panel();
        p.setLayout(new BorderLayout());
        lblCheckOutList = new Label("Purchase Item Invoice : ");
        lblCheckOutList.setFont(f);
        lstCheckOut = new List(10);
        lstCheckOut.setMultipleMode(false);
        String header0 = StringUtil.createFixedWidthString("No", 5);
        String header1 = StringUtil.createFixedWidthString("Product Name", 30);
        String header2 = StringUtil.createFixedWidthString("Unit Price", 15);
        String header3 = StringUtil.createFixedWidthString("Qty", 5);
        String header4 = StringUtil.createFixedWidthString("Total", 10);
        String outputHeader = String.format(Product_REPORT_FORMAT, header0, header1, header2, header3, header4);
        lstCheckOut.add(outputHeader);
        p.add("North", lblCheckOutList);
        p.add("Center", lstCheckOut);
        p.add("South", createListButtonPanel());
        return p;
    }

    private Panel createListButtonPanel() {
        Panel p = new Panel();
        p.setLayout(new FlowLayout());
        btnAddItem = new Button("Add");
        ActionListener l = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    String barcode = txtBarCode.getText().trim();
                    String StQty = txtQty.getText().trim();
                    if (barcode.length() != 0 && StQty.length() != 0) {
                        product = appController.getProductByBarcode(barcode);
                        qty = Integer.parseInt(StQty);
                        if (qty <= product.getQuantityAvailable()) {
                            if (product != null) {
                                float price = product.getProductPrice();
                                totalforeach = price * qty;
                                totalAmt += totalforeach;
                                productList.add(product);
                                qtyList.add(qty);
                                totalList.add(totalforeach);
                                no = no + 1;
                                String recordNo = StringUtil.createFixedWidthString(String.valueOf(no), 5);
                                String productName = StringUtil.createFixedWidthString(product.getProductName(), 30);
                                String priceSt = StringUtil.createFixedWidthString(Float.toString(price), 15);
                                String quantity = StringUtil.createFixedWidthString(Integer.toString(qty), 5);
                                String Total = StringUtil.createFixedWidthString(Float.toString(totalforeach), 10);
                                String outputProduct = String.format(Product_REPORT_FORMAT, recordNo, productName, priceSt, quantity, Total);
                                Calculate();
                                lstCheckOut.add(outputProduct);
                                txtBarCode.setText("");
                                txtQty.setText("0");
                            } else {
                                MessageDialog m = new MessageDialog(mainForm, "No Product For barcode", "The product is not found.");
                                m.setResizable(false);
                                m.setVisible(true);
                                txtQty.setText("");
                                txtBarCode.setText("");
                            }
                        } else {
                            MessageDialog m = new MessageDialog(mainForm, "Out of Stock", "The available for this product is " + product.getQuantityAvailable());
                            m.setResizable(false);
                            m.setVisible(true);
                            txtQty.setText("");
                        }
                    } else {
                        MessageDialog m = new MessageDialog(mainForm, "Enter Required Data", "Please Enter the BarCode No and Quantity.");
                        m.setResizable(false);
                        m.setVisible(true);
                        txtQty.setText("");
                        clear();
                    }
                } catch (NumberFormatException ex) {
                    MessageDialog m = new MessageDialog(mainForm, "Wrong Input Format", "Product Qty,Deem Point and Payment Amount must be Number.");
                    m.setResizable(false);
                    m.setVisible(true);
                    txtQty.setText("");
                } catch (AppException ex) {
                    MessageDialog m = new MessageDialog(mainForm, "Error Adding Product", ex.getMessage());
                    m.setResizable(false);
                    m.setVisible(true);
                }
            }
        };
        btnAddItem.addActionListener(l);
        btnRemoveItem = new Button("Remove");
        l = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = getSelectedProduct();
                if (index != -1) {
                    lstCheckOut.remove(index + 1);
                    productList.remove(index);
                    qtyList.remove(index);
                    totalList.remove(index);
                    Calculate();
                    String pay = txtPaymentAmt.getSelectedText().trim();
                    if (pay.length() != 0) {
                        float payment = Float.parseFloat(pay);
                        if (payment > grandtotal) {
                            float refund = calulateRefund(Float.parseFloat(pay));
                            lblRefund.setText(Float.toString(refund));
                        }
                    }
                    no--;
                } else {
                    MessageDialog m = new MessageDialog(mainForm, "Please select a product", "Please select a product to remove from list.");
                    m.setResizable(false);
                    m.setVisible(true);
                }
            }
        };
        btnRemoveItem.addActionListener(l);
        p.add(btnAddItem);
        p.add(btnRemoveItem);
        return p;
    }

    private Panel createFormPanel() {
        appController = AppController.getInstance();
        f = new Font("Verdana", Font.BOLD, 13);
        layout = new GridBagLayout();
        frmPanel = new Panel();
        frmPanel.setLayout(layout);
        chkPanel = createChkPaymentPanel();
        btnPanel = createButtonPanel();
        btnListPanel = createCheckOutPanel();
        chkMemberPanel = createMemberPanel();
        lblMemberId = new Label("Member ID");
        lblMemberId.setFont(f);
        lblDeem = new Label("Total Deem Point");
        lblDeem.setFont(f);
        lblDeemValue = new Label("0");
        lblBarCode = new Label("Bar Code");
        lblBarCode.setFont(f);
        lblQty = new Label("Qty");
        lblQty.setFont(f);
        lblTotal = new Label("Total");
        lblTotal.setFont(f);
        lblTotalValue = new Label();
        lblDeemUsed = new Label("Deem Point Used");
        lblDeemUsed.setFont(f);
        lblDiscount = new Label("Discount");
        lblDiscount.setFont(f);
        lblGrandTotal = new Label("Grand Total");
        lblGrandTotal.setFont(f);
        lblGrandTotalValue = new Label();
        lblchk = new Label("Does Customer Want to Redeem? ");
        lblchk.setFont(f);
        lblChkMember = new Label("Choose Member or Non-Member:");
        lblChkMember.setFont(f);
        txtMemberId = new TextField(15);
        FocusListener l = new FocusListener() {

            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                String id = txtMemberId.getText().trim();
                if (id.length() != 0) {
                    try {
                        member = appController.selectMember(id);
                        if (member != null) {
                            int point = appController.getMemberPoint(id);
                            setlblDeemValue(Integer.toString(point));
                        } else {
                            MessageDialog m = new MessageDialog(mainForm, "No Member", "This MemberID is not exist.");
                            m.setResizable(false);
                            m.setVisible(true);
                            txtMemberId.setText("");
                        }
                    } catch (AppException ex) {
                        MessageDialog m = new MessageDialog(mainForm, "Error Loading Category", ex.getMessage());
                        m.setResizable(false);
                        m.setVisible(true);
                    }
                }
            }
        };
        txtMemberId.addFocusListener(l);
        txtBarCode = new TextField(15);
        txtQty = new TextField(3);
        txtDeemUsed = new TextField(3);
        l = new FocusListener() {

            public void focusGained(FocusEvent arg0) {
            }

            public void focusLost(FocusEvent arg0) {
                try {
                    if (checkDeemPointAmt() == true) Calculate();
                } catch (Exception e) {
                    MessageDialog m = new MessageDialog(mainForm, "Wrong Input Format", "Deem point must be Number.");
                    m.setResizable(false);
                    m.setVisible(true);
                    txtDeemUsed.setText("0");
                }
            }
        };
        txtDeemUsed.addFocusListener(l);
        txtPaymentAmt = new TextField(10);
        l = new FocusListener() {

            public void focusGained(FocusEvent arg0) {
            }

            public void focusLost(FocusEvent arg0) {
                String pay = txtPaymentAmt.getText().trim();
                if (pay.length() != 0) {
                    try {
                        float payment = Float.parseFloat(pay);
                        float refund = 0.0f;
                        if (payment > grandtotal) {
                            refund = calulateRefund(payment);
                        } else if (payment < grandtotal) {
                            MessageDialog m = new MessageDialog(mainForm, "Invalid Payment", "Payment Amount must be greater than Grandtotal.");
                            m.setResizable(false);
                            m.setVisible(true);
                            txtPaymentAmt.setText("0");
                        }
                        lblRefundValue.setText(Float.toString(refund));
                    } catch (Exception e) {
                        MessageDialog m = new MessageDialog(mainForm, "Wrong Input Format", "Payment Amount must be Number.");
                        m.setResizable(false);
                        m.setVisible(true);
                    }
                }
            }
        };
        txtPaymentAmt.addFocusListener(l);
        lblDiscountValue = new Label();
        lblPaymentAmt = new Label("Payment Amt");
        lblPaymentAmt.setFont(f);
        lblRefund = new Label("Refund");
        lblRefund.setFont(f);
        lblRefundValue = new Label();
        txtDeemUsed.setText("0");
        txtDeemUsed.setEnabled(false);
        l = new FocusListener() {

            public void focusGained(FocusEvent arg0) {
            }

            public void focusLost(FocusEvent arg0) {
                try {
                    if (checkDeemPointAmt()) {
                        Integer.parseInt(txtDeemUsed.getText().trim());
                    } else {
                        MessageDialog m = new MessageDialog(mainForm, "NOT ENOUGH", "Deem Point is not enough");
                        m.setResizable(false);
                        m.setVisible(true);
                        txtDeemUsed.setText(lblDeemValue.getText());
                    }
                } catch (Exception e) {
                    MessageDialog m = new MessageDialog(mainForm, "Wrong Input Format", "Deem Point must be Number.");
                    m.setResizable(false);
                    m.setVisible(true);
                    txtDeemUsed.setText("0");
                }
            }
        };
        txtDeemUsed.addFocusListener(l);
        attachComponent(lblChkMember, 0, 0, 4, 1);
        attachComponent(chkMemberPanel, 0, 1, 2, 1);
        attachComponent(lblMemberId, 0, 2, 1, 1);
        attachComponent(txtMemberId, 1, 2, 1, 1);
        attachComponent(lblDeem, 2, 2, 1, 1);
        attachComponent(lblDeemValue, 3, 2, 1, 1);
        attachComponent(lblBarCode, 0, 3, 1, 1);
        attachComponent(txtBarCode, 1, 3, 1, 1);
        attachComponent(lblQty, 2, 3, 1, 1);
        attachComponent(txtQty, 3, 3, 1, 1);
        attachComponent(btnListPanel, 0, 4, 4, 1);
        attachComponent(lblchk, 0, 5, 4, 1);
        attachComponent(chkPanel, 0, 6, 3, 1);
        attachComponent(lblDeemUsed, 2, 7, 1, 1);
        attachComponent(txtDeemUsed, 3, 7, 1, 1);
        attachComponent(lblTotal, 2, 8, 1, 1);
        attachComponent(lblTotalValue, 3, 8, 1, 1);
        attachComponent(lblDiscount, 2, 9, 1, 1);
        attachComponent(lblDiscountValue, 3, 9, 1, 1);
        attachComponent(lblGrandTotal, 2, 10, 1, 1);
        attachComponent(lblGrandTotalValue, 3, 10, 1, 1);
        attachComponent(lblPaymentAmt, 2, 11, 1, 1);
        attachComponent(txtPaymentAmt, 3, 11, 1, 1);
        attachComponent(lblRefund, 2, 12, 1, 1);
        attachComponent(lblRefundValue, 3, 12, 1, 1);
        attachComponent(btnPanel, 0, 13, 4, 1);
        lblDiscountValue.setText(discountManager.getMaxMemberDiscount().getPercentage());
        return frmPanel;
    }

    private Panel createChkPaymentPanel() {
        Panel p = new Panel();
        p.setLayout(new FlowLayout());
        cbg = new CheckboxGroup();
        chkNo = new Checkbox("No", cbg, true);
        chkYes = new Checkbox("Yes", cbg, false);
        p.add(chkNo);
        p.add(chkYes);
        chkNo.addItemListener(new CheckBoxPaymentListener());
        chkYes.addItemListener(new CheckBoxPaymentListener());
        return p;
    }

    private Panel createMemberPanel() {
        Panel p = new Panel();
        p.setLayout(new FlowLayout());
        cbgMember = new CheckboxGroup();
        chkMember = new Checkbox("Member", cbgMember, true);
        chkNonMember = new Checkbox("Non-Member", cbgMember, false);
        p.add(chkMember);
        p.add(chkNonMember);
        chkMember.addItemListener(new CheckBoxMemberListener());
        chkNonMember.addItemListener(new CheckBoxMemberListener());
        return p;
    }

    public void setlblDeemValue(String string) {
        lblDeemValue.setText(string);
    }

    public void setlblTotalValue(String string) {
        lblTotalValue.setText(string);
    }

    public void setlblGrandTotalValue(String string) {
        lblGrandTotalValue.setText(string);
    }

    public void Calculate() {
        float total = 0.0f;
        dis = Integer.parseInt(lblDiscountValue.getText());
        deempoint = Integer.parseInt(txtDeemUsed.getText().trim());
        for (int i = 0; i < totalList.size(); i++) {
            float total1 = totalList.get(i);
            total += total1;
        }
        float finalTotal = 0.0f;
        int deem = Integer.parseInt(lblDeemValue.getText().trim());
        if (deempoint != 0) {
            checkDeemPointAmt();
            float dollar = pointToDollar(deempoint);
            finalTotal = total;
            grandtotal = (finalTotal - (finalTotal * dis / 100)) - dollar;
            finaldeem = (dollarToPoint(grandtotal) + deem) - deempoint;
        } else {
            finalTotal = total;
            grandtotal = finalTotal - (finalTotal * dis / 100);
            if (txtMemberId.getText().trim().equalsIgnoreCase("PUBLIC") == false) {
                finaldeem = dollarToPoint(grandtotal);
            }
        }
        lblTotalValue.setText(Float.toString(finalTotal));
        lblGrandTotalValue.setText(Float.toString(grandtotal));
    }

    public void setlblDiscountValue(String string) {
        lblDiscountValue.setText(string);
    }

    public void setlblRefundValue(String string) {
        lblRefundValue.setText(string);
    }

    class CheckBoxPaymentListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            Checkbox cb = cbg.getSelectedCheckbox();
            if (cb.getLabel().toString().equalsIgnoreCase("No")) {
                txtDeemUsed.setText("0");
                txtDeemUsed.setEnabled(false);
            } else if (cb.getLabel().toString().equalsIgnoreCase("Yes")) {
                if (checkDeemPointValue() == true) {
                    txtDeemUsed.setText("0");
                    txtDeemUsed.setEnabled(true);
                } else {
                    showErrMsgForDeemPoint();
                }
            }
        }
    }

    class CheckBoxMemberListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            Checkbox cb = cbgMember.getSelectedCheckbox();
            if (cb.getLabel().toString().equalsIgnoreCase("Member")) {
                txtMemberId.setText("");
                txtMemberId.setEnabled(true);
                chkYes.setEnabled(true);
                lblDiscountValue.setText(discountManager.getMaxMemberDiscount().getPercentage());
                Calculate();
            } else {
                txtMemberId.setText("PUBLIC");
                txtMemberId.setEnabled(false);
                txtDeemUsed.setText("0");
                txtDeemUsed.setEnabled(false);
                chkYes.setEnabled(false);
                lblDeemValue.setText("0");
                lblDiscountValue.setText(discountManager.getMaxNonMemberDiscount().getPercentage());
                Calculate();
            }
        }
    }

    public boolean checkDeemPointValue() {
        boolean success = false;
        int i = Integer.parseInt(lblDeemValue.getText().toString());
        if (i > 0) success = true;
        return success;
    }

    public boolean checkDeemPointAmt() {
        boolean success = false;
        int deem = Integer.parseInt(lblDeemValue.getText().toString());
        int deemUsed = Integer.parseInt(txtDeemUsed.getText().trim());
        if (deem >= deemUsed) success = true;
        return success;
    }

    public void showErrMsgForDeemPoint() {
        MessageDialog m = new MessageDialog(mainForm, "No Deem Point", "Customer does not have any deem point.Please Pay by Cash.");
        m.setVisible(true);
        chkNo.setState(true);
    }

    public int getSelectedProduct() {
        int idx = lstCheckOut.getSelectedIndex();
        if (idx == -1) {
            return -1;
        } else {
            int index = idx - 1;
            return (index == -1) ? -1 : index;
        }
    }

    public int dollarToPoint(float dollar) {
        int point = 0;
        point = (int) (dollar / 10);
        return point;
    }

    public Float pointToDollar(int deempoint) {
        Float dollar = 0.0f;
        dollar = deempoint / 20.0f;
        return dollar;
    }

    public Float calulateRefund(Float payment) {
        float refund = 0.0f;
        refund = payment - grandtotal;
        return refund;
    }

    public void clear() {
        txtMemberId.setText("");
        lblDeemValue.setText("0");
        txtBarCode.setText("");
        txtQty.setText("");
        lstCheckOut.removeAll();
        String header0 = StringUtil.createFixedWidthString("No", 5);
        String header1 = StringUtil.createFixedWidthString("Product Name", 30);
        String header2 = StringUtil.createFixedWidthString("Unit Price", 15);
        String header3 = StringUtil.createFixedWidthString("Qty", 5);
        String header4 = StringUtil.createFixedWidthString("Total", 10);
        String outputHeader = String.format(Product_REPORT_FORMAT, header0, header1, header2, header3, header4);
        lstCheckOut.add(outputHeader);
        txtDeemUsed.setText("0");
        lblTotalValue.setText("");
        lblGrandTotalValue.setText("");
        txtPaymentAmt.setText("");
        lblRefundValue.setText("");
    }

    public void addpurchase(Product p, String m, int qty) {
        appController = AppController.getInstance();
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String productid = p.getProductID();
        Tuple4<String, String, Integer, Date> item = new Tuple4<String, String, Integer, Date>(productid, m, qty, date);
        purchases.add(item);
    }
}
