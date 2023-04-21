package atmon;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ByteEdFrame extends JFrame implements ActionListener, KeyListener, WindowListener {

    private static final long serialVersionUID = 2677221060184016270L;

    public static final int DEF_BYTE_CNT = 1;

    public static final int DEF_VAL_CNT = 1;

    public static final int MIN_BYTE_CNT = 1;

    public static final int MAX_BYTE_CNT = 8;

    public static final int MIN_VAL_CNT = 1;

    public static final int MAX_VAL_CNT = Comm.RAM_SIZE;

    private static final int LAB_BIT_W = 20;

    private static final int W_O = 16;

    private static final int H_O = 36;

    private static final int LAB_TOP = 6;

    private static final int LAB_ED_W = 25;

    private static final int LAB_H = 10;

    private static final int LAB_BIT_O = BitBoxPanel.BIT_SP + BitBox.W / 2 - 4;

    private static final int ABOX_O = 16;

    private static final int REFR_O = 20;

    private static final int HEX_O = 20;

    private static final int DEC_O = HEX_O + 12;

    private static final int VALS_T_O = 16;

    private int refresh;

    private Timer timer = null;

    private JCheckBox autoBox;

    private JButton refreshBut;

    private int addr, byteCnt, valCnt;

    private Comm comm;

    private boolean ioPort;

    private ByteEdValPanel[] values;

    public ByteEdFrame(Comm comm, int addr, boolean ioPort, int refresh) {
        this(comm, addr, ioPort, refresh, DEF_BYTE_CNT);
    }

    public ByteEdFrame(Comm comm, int addr, boolean ioPort, int refresh, int byteCnt) {
        this(comm, addr, ioPort, refresh, byteCnt, DEF_VAL_CNT);
    }

    public ByteEdFrame(Comm comm, int addr, boolean ioPort, int refresh, int byteCnt, int valCnt) {
        this.comm = comm;
        this.addr = addr;
        this.ioPort = ioPort;
        this.refresh = refresh;
        this.valCnt = valCnt;
        this.byteCnt = byteCnt;
        int bitCnt = byteCnt * ByteEdValPanel.BITS_PER_BYTE;
        Label l;
        int x, bw;
        bw = BitBox.W + BitBoxPanel.BIT_SP;
        Container root = getContentPane();
        setResizable(false);
        addKeyListener(this);
        addWindowListener(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        root.setLayout(null);
        for (int i = 0; i < bitCnt; i++) {
            l = new Label(((i < 10) ? " " : "") + i);
            root.add(l);
            x = (bitCnt - 1 - i) * bw;
            l.setSize(LAB_BIT_W, LAB_H);
            l.setLocation(x + LAB_BIT_O, LAB_TOP);
        }
        l = new Label("hex");
        l.setSize(LAB_ED_W, LAB_H);
        l.setLocation(bitCnt * bw + HEX_O + 2, LAB_TOP);
        root.add(l);
        l = new Label("dec");
        l.setSize(LAB_ED_W, LAB_H);
        l.setLocation(bitCnt * bw + byteCnt * ByteEdValPanel.EDH_W_M + ByteEdValPanel.EDH_W_O + DEC_O + 2, LAB_TOP);
        root.add(l);
        values = new ByteEdValPanel[valCnt];
        int vw = 0;
        for (int i = 0; i < valCnt; i++) {
            values[i] = new ByteEdValPanel(this, comm, addr + (i * byteCnt), ioPort, refresh, byteCnt);
            values[i].setLocation(0, VALS_T_O + i * values[i].getHeight());
            root.add(values[i]);
            if (values[i].getWidth() > vw) {
                vw = values[i].getWidth();
            }
        }
        autoBox = new JCheckBox("autoref");
        root.add(autoBox);
        autoBox.setSize(autoBox.getPreferredSize());
        autoBox.setLocation(vw + ABOX_O, 0);
        autoBox.addActionListener(this);
        autoBox.addKeyListener(this);
        refreshBut = new JButton("refresh");
        root.add(refreshBut);
        refreshBut.setSize(60, 24);
        refreshBut.setMargin(new Insets(1, 1, 1, 1));
        refreshBut.setLocation(vw + REFR_O, 24);
        refreshBut.addActionListener(this);
        refreshBut.addKeyListener(this);
        setSize(refreshBut.getLocation().x + refreshBut.getWidth() + W_O, valCnt * ByteEdValPanel.HEIGHT + VALS_T_O + H_O);
        Utils.centerFrame(this);
        setTitle("Byte editor - address: " + (ioPort ? "IO: " + Utils.byteToHex(addr) : Utils.wordToHex(addr)) + " (dec " + addr + ")");
        readValues();
        setVisible(true);
        if (values.length > 0) {
            values[0].focusHex();
        }
    }

    public void setAutoRefresh(boolean state) {
        if (state) {
            timer = new Timer(refresh, this);
            timer.start();
        } else {
            timer.stop();
            timer = null;
        }
        autoBox.setSelected(state);
    }

    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == refreshBut) {
            readValues();
        } else if (ev.getSource() == autoBox) {
            setAutoRefresh(autoBox.isSelected());
        } else if (ev.getSource() == timer) {
            readValues();
        }
    }

    public void readValues() {
        long val, v;
        if (ioPort) {
            for (int j = 0; j < valCnt; j++) {
                val = 0;
                for (int i = 0; i < byteCnt; i++) {
                    v = comm.readIO(addr + j * byteCnt + i);
                    if (v < 0) {
                        return;
                    }
                    val |= v << (i * ByteEdValPanel.BITS_PER_BYTE);
                }
                values[j].setValue(val);
            }
        } else {
            byte[] data = comm.readMem(addr, valCnt * byteCnt);
            if ((data == null) || (data.length != valCnt * byteCnt)) {
                return;
            }
            for (int j = 0; j < valCnt; j++) {
                val = 0;
                for (int i = 0; i < byteCnt; i++) {
                    v = Utils.ub(data[j * byteCnt + i]);
                    val |= v << (i * ByteEdValPanel.BITS_PER_BYTE);
                }
                values[j].setValue(val);
            }
        }
    }

    public void keyTyped(KeyEvent ev) {
    }

    public void keyPressed(KeyEvent ev) {
        if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dispose();
            ev.consume();
        }
    }

    public void keyReleased(KeyEvent ev) {
    }

    public void windowOpened(WindowEvent ev) {
    }

    public void windowClosing(WindowEvent ev) {
    }

    public void windowClosed(WindowEvent ev) {
        if (timer != null) {
            timer.stop();
        }
    }

    public void windowIconified(WindowEvent ev) {
    }

    public void windowDeiconified(WindowEvent ev) {
    }

    public void windowActivated(WindowEvent ev) {
    }

    public void windowDeactivated(WindowEvent ev) {
    }
}
