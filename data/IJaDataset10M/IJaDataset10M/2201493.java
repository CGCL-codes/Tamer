package bierse.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import bierse.model.Drink;
import bierse.model.Model;

public class DefaultDrinkSellView extends JFrame implements IDrinkSellView, IModelChangedListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7479281171426356909L;

    JTextField amountSoldTextField;

    JTextField directPriceTextField;

    JLabel timeLeftLabel;

    JLabel iterationLabel;

    JMenuItem displayInformationViewMI;

    JMenuItem displaySettingsViewMI;

    DecimalFormat df = new DecimalFormat("0.00");

    DefaultTableModel drinkTableModel;

    String[] drinkTableHeader = new String[] { "Name", "Taste", "Preis(akt)", "Preis (Min/Max)", "Neuer Preis", "Verkauf(Soll)", "Verkauf(akt)", "Verkauft (avg)", "Preis(avg)", "Verkauf-Preis (avg)", "Verkauft(ges)", "Preis halten" };

    int numberOfColumns = 12;

    int keepPriceColumnIndex = 11;

    Model model;

    public DefaultDrinkSellView(String name, Model model) {
        super(name);
        model.registerModelChangedListener(this);
        this.model = model;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                beforeClose();
            }
        });
        getContentPane().setLayout(new BorderLayout());
        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("Datei");
        fileMenu.setMnemonic(KeyEvent.VK_D);
        menubar.add(fileMenu);
        displayInformationViewMI = new JMenuItem("Zeige neue Info-Anzeige");
        displayInformationViewMI.setMnemonic(KeyEvent.VK_I);
        displayInformationViewMI.setActionCommand(IDrinkSellView.MI_DISPLAY_INFO_VIEW);
        fileMenu.add(displayInformationViewMI);
        displaySettingsViewMI = new JMenuItem("Einstellungen");
        displaySettingsViewMI.setMnemonic(KeyEvent.VK_E);
        displaySettingsViewMI.setActionCommand(IDrinkSellView.MI_DISPLAY_SETTINGS_VIEW);
        fileMenu.add(displaySettingsViewMI);
        setJMenuBar(menubar);
        iterationLabel = new JLabel("Runde: " + model.getIteration());
        getContentPane().add(iterationLabel, BorderLayout.NORTH);
        drinkTableModel = new DefaultTableModel(drinkTableHeader, model.getLstUsedDrink().size()) {

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == keepPriceColumnIndex) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == keepPriceColumnIndex) {
                    return true;
                }
                return false;
            }
        };
        drinkTableModel.setDataVector(getDrinkTableData(), drinkTableHeader);
        JTable drinkTable = new JTable(drinkTableModel);
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 25);
        drinkTable.setFont(font);
        drinkTable.setRowHeight(drinkTable.getFontMetrics(font).getHeight() + 4);
        drinkTable.setAutoCreateColumnsFromModel(false);
        JScrollPane drinkPanelMain = new JScrollPane(drinkTable);
        getContentPane().add(drinkPanelMain, BorderLayout.CENTER);
        amountSoldTextField = new JTextField();
        amountSoldTextField.setText("1");
        amountSoldTextField.setColumns(3);
        amountSoldTextField.grabFocus();
        amountSoldTextField.selectAll();
        directPriceTextField = new JTextField("0", 5);
        JPanel drinkSoldPanel = new JPanel();
        drinkSoldPanel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Menge: ");
        drinkSoldPanel.add(label);
        drinkSoldPanel.add(amountSoldTextField);
        JLabel directPriceLabel = new JLabel("Preis-Direkteingabe: ");
        drinkSoldPanel.add(directPriceLabel);
        drinkSoldPanel.add(directPriceTextField);
        timeLeftLabel = new JLabel("Time left: " + model.getTimeLeft());
        drinkSoldPanel.add(timeLeftLabel);
        getContentPane().add(drinkSoldPanel, BorderLayout.SOUTH);
        pack();
        setSize(Double.valueOf(getMaximumSize().getWidth()).intValue(), getHeight());
        setVisible(true);
    }

    private void beforeClose() {
        model.beforeClose();
    }

    private Object[][] getDrinkTableData() {
        Object[][] data = new Object[model.getLstUsedDrink().size()][numberOfColumns];
        for (int i = 0; i < model.getLstUsedDrink().size(); i++) {
            Drink drink = model.getLstUsedDrink().get(i);
            data[i][0] = drink.getName();
            data[i][1] = KeyEvent.getKeyText(drink.getKey());
            data[i][2] = String.valueOf(drink.getCurrentPrice());
            data[i][3] = String.valueOf(drink.getMinPrice() + " / " + drink.getMaxPrice());
            if (drink.getDirectPrice() != 0) {
                data[i][4] = String.valueOf(drink.getDirectPrice());
            } else {
                data[i][4] = "---";
            }
            data[i][5] = String.valueOf(drink.getTargetAmount());
            data[i][6] = String.valueOf(drink.getLastSold());
            data[i][7] = String.valueOf(Math.round(drink.getAverageAmount()));
            data[i][8] = df.format(drink.getAveragePrice());
            data[i][9] = df.format(drink.getAverageSoldPrice());
            data[i][10] = drink.getTotalSold();
            data[i][11] = drink.isKeepPrice();
        }
        return data;
    }

    private void repaintTimeLeftLabel() {
        timeLeftLabel.setText("Time left: " + model.getTimeLeft());
    }

    private void repaintIterationLabel() {
        iterationLabel.setText("Runde: " + model.getIteration());
    }

    @Override
    public void fireModelChanged(int eventType) {
        if ((eventType & Model.EVENT_DRINK_LIST_CHANGED + Model.EVENT_DRINK_SOLD + Model.EVENT_RECALCULATED + Model.EVENT_SETTINGS_CHANGED) > 0) {
            drinkTableModel.setDataVector(getDrinkTableData(), drinkTableHeader);
            drinkTableModel.fireTableDataChanged();
        }
        if (Model.EVENT_RECALCULATED == eventType) {
            repaintIterationLabel();
        }
        if (eventType == Model.EVENT_TIME_LEFT_CHANGED) {
            repaintTimeLeftLabel();
        }
        this.validate();
        this.repaint();
    }

    @Override
    public int getAmount() {
        if (!amountSoldTextField.getText().isEmpty()) {
            return Integer.valueOf(amountSoldTextField.getText()).intValue();
        }
        return 0;
    }

    @Override
    public void setAmount(int amount) {
        amountSoldTextField.setText(String.valueOf(amount));
        amountSoldTextField.grabFocus();
        amountSoldTextField.selectAll();
    }

    @Override
    public int getDirectPrice() {
        if (!directPriceTextField.getText().isEmpty()) {
            return Integer.valueOf(directPriceTextField.getText()).intValue();
        }
        return 0;
    }

    @Override
    public void setDirectPrice(int directPrice) {
        directPriceTextField.setText(String.valueOf(directPrice));
        directPriceTextField.selectAll();
    }

    public void setDrinkSellController(KeyListener kl) {
        amountSoldTextField.addKeyListener(kl);
        directPriceTextField.addKeyListener(kl);
    }

    public void setMenuController(ActionListener al) {
        displayInformationViewMI.addActionListener(al);
        displaySettingsViewMI.addActionListener(al);
    }

    public void setTableModelController(TableModelListener tml) {
        drinkTableModel.addTableModelListener(tml);
    }
}
