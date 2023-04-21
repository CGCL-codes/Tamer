package setgen.gui;

import iso.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class MainPanel extends Panel implements ActionListener {

    private ElementSet set;

    private SetFileControl setFileLoader;

    private SetFileControl setFileWriter;

    private TextField setnamefield;

    private JTable settable;

    private DefaultTableModel tablemodel;

    private Button addbutton, removebutton;

    private Image elementImg;

    private Label errorLabel;

    public MainPanel() {
        this.setLayout(null);
        this.setBackground(Color.GREEN);
        initSurface();
        reset();
        loadImage("");
    }

    private void initSurface() {
        Insets insets = this.getInsets();
        Label namelabel = new Label("Set's name:");
        namelabel.setBounds(insets.left + 10, insets.top + 25, 80, 20);
        setnamefield = new TextField();
        setnamefield.setBounds(insets.left + 90, insets.top + 25, 200, 20);
        initTable(insets);
        addbutton = new Button("Add");
        addbutton.setBounds(insets.left + 415, insets.top + 185, 50, 25);
        addbutton.addActionListener(this);
        removebutton = new Button("Remove");
        removebutton.setBounds(insets.left + 470, insets.top + 185, 60, 25);
        removebutton.addActionListener(this);
        errorLabel = new Label("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(insets.left + 90, insets.top + 8, 200, 20);
        this.add(namelabel);
        this.add(setnamefield);
        this.add(addbutton);
        this.add(removebutton);
        this.add(errorLabel);
    }

    private void initTable(Insets insets) {
        String[] colnames = new String[4];
        colnames[0] = new String("Name");
        colnames[1] = new String("ID");
        colnames[2] = new String("A segment");
        colnames[3] = new String("Body Height");
        tablemodel = new DefaultTableModel(colnames, 0) {

            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        settable = new JTable(tablemodel) {

            public void valueChanged(ListSelectionEvent e) {
                super.valueChanged(e);
                refreshImage();
            }
        };
        TableColumn col = settable.getColumnModel().getColumn(0);
        col.setPreferredWidth(150);
        col = settable.getColumnModel().getColumn(1);
        col.setPreferredWidth(50);
        col = settable.getColumnModel().getColumn(2);
        col.setPreferredWidth(50);
        col = settable.getColumnModel().getColumn(3);
        col.setPreferredWidth(50);
        JScrollPane scrollPane = new JScrollPane(settable);
        scrollPane.setBounds(insets.left + 10, insets.top + 60, 400, 150);
        this.add(scrollPane);
    }

    public void reset() {
        set = new ElementSet();
        setFileLoader = new SetFileControl("./temp/load");
        setFileWriter = new SetFileControl("./temp/save");
        refreshTable();
    }

    public void open(String path) {
        try {
            set = setFileLoader.loadSet(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setnamefield.setText(set.getName());
        refreshTable();
    }

    public void save(String path) {
        String setname = setnamefield.getText();
        if (!setname.equals("")) {
            errorLabel.setText("");
            set.setName(setname);
            try {
                setFileWriter.saveSet(path, set);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else errorLabel.setText("Error: Missing Set name!");
    }

    public void refreshTable() {
        Object[] row;
        ListIterator<SetElement> iterator;
        SetElement cur;
        for (int i = tablemodel.getRowCount() - 1; i >= 0; i--) tablemodel.removeRow(i);
        iterator = set.getInList().listIterator();
        while (iterator.hasNext()) {
            cur = iterator.next();
            row = new Object[4];
            row[0] = cur.name;
            row[1] = cur.ID;
            row[2] = cur.a_segment;
            row[3] = cur.bodyHeight;
            tablemodel.addRow(row);
        }
    }

    private void loadImage(String path) {
        Toolkit t = this.getToolkit();
        if (path == null || path.equals("")) elementImg = t.createImage("./data/noimg.png"); else elementImg = t.getImage(path);
    }

    private void refreshImage() {
        int selrow = settable.getSelectedRow();
        String path = "";
        if (selrow >= 0) path = set.get(selrow).imgPath;
        loadImage(path);
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        int width, height;
        double scale;
        scale = (double) (elementImg.getHeight(this) / (double) elementImg.getWidth(this));
        if (scale > 1) {
            scale = (double) (elementImg.getWidth(this) / (double) elementImg.getHeight(this));
            height = 100;
            width = (int) (height * scale);
        } else {
            width = 100;
            height = (int) (width * scale);
        }
        g.drawImage(elementImg, 425, 20, width, height, this);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Add")) {
            AddWindow aw = new AddWindow(this, set);
            aw.show();
        } else if (command.equals("Remove")) {
            int selrow = settable.getSelectedRow();
            set.removeSetElement(selrow);
            refreshTable();
        }
    }
}
