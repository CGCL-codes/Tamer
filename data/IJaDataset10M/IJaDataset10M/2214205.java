package gdg.tables.tableView;

import gdg.tables.tableModel.Column;
import gdg.tables.tableModel.Reference;
import gdg.tables.tableModel.Table;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class TableBox extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected Table table;

    protected List<Column> columns;

    protected List<Reference> references;

    protected List<ColumnEmbedder> columnButtons;

    protected JPanel title;

    protected JLabel tableTitle;

    protected JTextField tableRecords;

    protected JPanel columnPane;

    protected GridBagConstraints c;

    private final Color[] goodColors = { new Color(0xffaaaa), new Color(0xffaa00), new Color(0x77ff77), new Color(0xaaaa00) };

    ;

    private int nextColor = 0;

    private Color color = new Color(0xeeeeee);

    ;

    public TableBox(Table table) {
        this.table = table;
        this.columns = table.getColumns();
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        setBorder(BorderFactory.createLineBorder(Color.black));
        addColumns();
        addHeader();
        addFKey();
    }

    public void addListeners(ActionListener buttonListener, TableListener tableBoxListener, InputVerifier tableVerifier) {
        table.addTableListener(tableBoxListener);
        tableRecords.setInputVerifier(tableVerifier);
        for (ColumnEmbedder ce : columnButtons) {
            ce.addActionListener(buttonListener);
        }
    }

    protected void addHeader() {
        title = new JPanel();
        tableTitle = new JLabel();
        tableRecords = new JTextField(7);
        title.add(tableTitle);
        title.add(tableRecords);
        updateTitle();
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = columnButtons.size();
        add(title, c);
    }

    public void updateTitle() {
        String tableName;
        tableName = (table.getName()).toUpperCase();
        tableTitle.setText("<html>" + tableName + ' ' + "</html>");
        if (getTopLevelAncestor() != null) {
            getTopLevelAncestor().repaint();
        }
    }

    protected void addColumns() {
        ColumnEmbedder currentButton;
        JLabel[] columnProperties;
        JPanel columnPropertiesPane;
        ListIterator<Column> iter;
        String currentColumnName;
        String currentColumnType;
        Column currentColumn;
        int pos = 0;
        columnButtons = new LinkedList<ColumnEmbedder>();
        iter = columns.listIterator();
        while (iter.hasNext()) {
            currentColumn = iter.next();
            currentColumnName = (currentColumn.getName()).toUpperCase();
            currentColumnType = currentColumn.getType().toString();
            if (currentColumn.isKey()) {
                currentColumnName = "<html><u>" + currentColumnName + "</u></html>";
            }
            currentButton = new ColumnEmbedder(currentColumn);
            ((JButton) currentButton).setActionCommand("editColumn");
            columnButtons.add(currentButton);
            columnPropertiesPane = new JPanel();
            columnProperties = initProperties(currentColumn, columnPropertiesPane);
            c.gridy = 1;
            c.gridx = pos;
            add(currentButton, c);
            c.gridy = 2;
            c.gridx = pos;
            c.fill = GridBagConstraints.BOTH;
            add(columnPropertiesPane, c);
            pos++;
        }
    }

    private void nextColor() {
        Color result = goodColors[nextColor];
        nextColor = (nextColor + 1) % goodColors.length;
        this.color = result;
    }

    protected JLabel[] initProperties(Column currentColumn, JPanel columnPropertiesPane) {
        JLabel[] columnProperties = new JLabel[4];
        String colorUnique = "#aa00aa";
        String colorKey = "#dd0000";
        String colorNullable = "#00bb00";
        String currentColumnName;
        String currentColumnType;
        columnPropertiesPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        columnPropertiesPane.setLayout(new BoxLayout(columnPropertiesPane, BoxLayout.PAGE_AXIS));
        this.nextColor = 0;
        if (currentColumn.isKey()) this.nextColor();
        for (int i = 0; i < columnProperties.length; i++) {
            columnProperties[i] = new JLabel();
            columnProperties[i].setBackground(this.color);
            columnPropertiesPane.add(columnProperties[i]);
        }
        currentColumnName = (currentColumn.getName()).toUpperCase();
        currentColumnType = currentColumn.getType().toString();
        columnProperties[0].setText("<html><font color=#0000ff>" + currentColumnType + "</font></html>");
        columnProperties[1].setText("<html><font color=" + colorKey + ">KEY</font></html>");
        columnProperties[2].setText("<html><font color=" + colorUnique + ">UNIQUE</font></html>");
        columnProperties[3].setText("<html><font color=" + colorNullable + ">NULLABLE</font></html>");
        if (!currentColumn.isKey()) columnPropertiesPane.remove(columnProperties[1]);
        if (!currentColumn.isUnique()) columnPropertiesPane.remove(columnProperties[2]);
        if (!currentColumn.isNullable()) columnPropertiesPane.remove(columnProperties[3]);
        return columnProperties;
    }

    public void addFKey() {
        ListIterator<Reference> iter;
        Reference currentReference;
        Table currentTableTo;
        Table currentTableFrom;
        references = table.getForeignKeys();
        iter = references.listIterator();
        while (iter.hasNext()) {
            currentReference = iter.next();
        }
    }

    public void edit() {
    }

    public Table getTable() {
        return table;
    }
}
