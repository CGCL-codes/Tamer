package importPool;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.RandomAccessFile;
import java.util.Stack;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import dbmanager.DBManager;
import dbmanager.GetIdName;
import utility.CapitalChar;

public class SelectFields extends JFrame implements ItemListener, ActionListener {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private Vector<Object> jcheck = new Vector<Object>();

    private Vector<Object> jSelectedcheck = new Vector<Object>();

    private String[] arrFiels;

    private JButton Register;

    private JCheckBox jc;

    private String inputpath = " ";

    private String tableName = " ";

    DBManager sqlDB = new DBManager();

    GetIdName gin = new GetIdName(sqlDB);

    private Stack<Object> importStack = new Stack<Object>();

    public SelectFields(String[] arrFields, String path, String tablename, Stack<Object> importStack) {
        super();
        this.arrFiels = arrFields;
        this.importStack = importStack;
        System.out.println("importStack push ===========" + importStack);
        this.inputpath = path;
        this.tableName = tablename;
        initializeComponent(arrFiels);
        this.setVisible(true);
    }

    private void initializeComponent(String[] arrFields) {
        System.out.print("i am in initi");
        String fieldStr = "";
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(null);
        Register = new JButton();
        Register.setText("Register");
        Register.addActionListener(this);
        int y = 0;
        int i = 0;
        int fieldLength = arrFields.length;
        for (y = 0; y < fieldLength; ) {
            System.out.println("length====" + fieldLength);
            int x = 10;
            int count = 3, k = y;
            System.out.println("count =" + count);
            System.out.println("arrFields =" + fieldLength);
            while (count != 0) {
                System.out.println("count is" + count);
                System.out.println("i is" + i);
                try {
                    fieldStr = arrFields[i];
                } catch (Exception e) {
                    break;
                }
                System.out.println("fieldStr''''''''''''''''''=======" + fieldStr);
                jc = new JCheckBox(fieldStr);
                contentPane.add(jc);
                addComponent(contentPane, jc, x, (k) * 20, 170, 30);
                jc.addItemListener(this);
                jc.setVisible(true);
                jcheck.add(jc);
                count--;
                System.out.println("count" + count);
                i++;
                x = x + 200;
                y++;
                System.out.println("y '''''''''''''''''''''=======" + y);
                System.out.print("x=" + x + "y=" + y * 20);
            }
        }
        int dimR = 200;
        if (fieldLength < 3) dimR = 35 * fieldLength;
        addComponent(contentPane, Register, dimR, y * 18 + 100, 120, 30);
        this.setTitle("SelectFields");
        this.setLocation(new Point(0, 0));
        int dimX = 525;
        int dimY = 150;
        if (fieldLength < 3) dimX = 200 * (fieldLength);
        if (fieldLength > 3) {
            int inY = fieldLength / 3;
            dimY = 140 * (inY + 1);
            if (inY >= 3) dimY = dimY - (50 * inY);
        }
        this.setSize(new Dimension(dimX, dimY));
    }

    private void addComponent(Container container, Component c, int x, int y, int width, int height) {
        c.setBounds(x, y, width, height);
        container.add(c);
    }

    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == Register) {
            try {
                for (int i = 0; i < jcheck.size(); i++) {
                    jc = (JCheckBox) jcheck.get(i);
                    if (jc.isEnabled() == true) {
                        if (jc.isSelected() == true) {
                            jSelectedcheck.add(jc.getText());
                        }
                    }
                }
                if (!jSelectedcheck.isEmpty()) {
                    RandomAccessFile raf = new RandomAccessFile(inputpath, "rws");
                    String line = null;
                    line = raf.readLine();
                    String[] column = line.split("\t");
                    if (jSelectedcheck.contains(column[0])) {
                        try {
                            Class C = Class.forName("importPool." + CapitalChar.makeFirstCharCapital(tableName));
                            TableName tbl_name = (TableName) C.newInstance();
                            sqlDB.getConnect();
                            tbl_name.initialize(inputpath, jSelectedcheck);
                            C = null;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                        }
                        setVisible(false);
                        while (!importStack.empty()) {
                            try {
                                ImportFile im = (ImportFile) importStack.pop();
                                im.setVisible(true);
                            } catch (Exception e) {
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Object '" + column[0] + "' must be selected");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Select atleast one option");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Unable to get input path");
            }
        }
    }

    public void itemStateChanged(ItemEvent event) {
    }
}
