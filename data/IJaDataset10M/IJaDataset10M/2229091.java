package simulation_1_11.Drawing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JColorChooser;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.*;

/**
 * 2Dͼ�λ����������
 */
public class PaintMainFrame extends JFrame {

    JPanel commandPanel = new JPanel();

    JPanel colorPanel = new JPanel();

    JPanel mainPanel = new JPanel();

    JButton btnRed = new JButton("Red");

    JButton btnGreen = new JButton("Green");

    JButton btnBlue = new JButton("Blue");

    JButton btnDefine = new JButton("�Զ�����ɫ");

    JLabel lable = new JLabel("�Ƿ����");

    JCheckBox check = new JCheckBox();

    JButton btnLine = new JButton("Line");

    JButton btnRectangle = new JButton("Rectangle");

    JButton btnCircle = new JButton("Circle");

    JButton btnArc = new JButton("Arc");

    JButton btnPolygon = new JButton("Polygon");

    JButton btnUndo = new JButton("Undo");

    JButton btnRedo = new JButton("Redo");

    JButton btnSave = new JButton("Save");

    JButton btnOpen = new JButton("Open");

    JButton btnExit = new JButton("Exit");

    PaintBoard paintBoard = new PaintBoard();

    public PaintMainFrame() {
        this.getContentPane().setLayout(new BorderLayout());
        btnLine.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnLine_actionPerformed(e);
            }
        });
        btnRectangle.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnRectangle_actionPerformed(e);
            }
        });
        btnCircle.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnCircle_actionPerformed(e);
            }
        });
        btnArc.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnArc_actionPerformed(e);
            }
        });
        btnPolygon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnPolygon_actionPerformed(e);
            }
        });
        btnExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnExit_actionPerformed(e);
            }
        });
        btnUndo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnUndo_actionPerformed(e);
            }
        });
        btnRedo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnRedo_actionPerformed(e);
            }
        });
        btnSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnSave_actionPerformed(e);
            }
        });
        btnOpen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnOpen_actionPerformed(e);
            }
        });
        commandPanel.setLayout(new FlowLayout());
        commandPanel.add(btnOpen);
        commandPanel.add(btnLine);
        commandPanel.add(btnRectangle);
        commandPanel.add(btnCircle);
        commandPanel.add(btnArc);
        commandPanel.add(btnPolygon);
        commandPanel.add(btnUndo);
        commandPanel.add(btnRedo);
        commandPanel.add(btnSave);
        commandPanel.add(btnExit);
        btnRed.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnRed_actionPerformed(e);
            }
        });
        btnGreen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnGreen_actionPerformed(e);
            }
        });
        btnBlue.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnBlue_actionPerformed(e);
            }
        });
        btnDefine.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnDefine_actionPerformed(e);
            }
        });
        colorPanel.setLayout(new FlowLayout());
        colorPanel.add(btnRed, null);
        colorPanel.add(btnGreen, null);
        colorPanel.add(btnBlue, null);
        colorPanel.add(btnDefine, null);
        check.setSelected(false);
        colorPanel.add(check, null);
        colorPanel.add(lable, null);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(commandPanel, BorderLayout.NORTH);
        mainPanel.add(colorPanel, BorderLayout.CENTER);
        this.getContentPane().add(mainPanel, BorderLayout.SOUTH);
        this.getContentPane().add(paintBoard, BorderLayout.CENTER);
        btnLine.setForeground(Color.red);
        paintBoard.setCommand(Command.LINE);
        btnBlue.setForeground(Color.red);
        paintBoard.setColor(Color.blue);
    }

    /******�¼�����****/
    void btnExit_actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    void btnUndo_actionPerformed(ActionEvent e) {
        paintBoard.undo();
    }

    void btnRedo_actionPerformed(ActionEvent e) {
        paintBoard.redo();
    }

    void btnLine_actionPerformed(ActionEvent e) {
        btnLine.setForeground(Color.red);
        btnRectangle.setForeground(Color.black);
        btnCircle.setForeground(Color.black);
        btnArc.setForeground(Color.black);
        btnPolygon.setForeground(Color.black);
        btnSave.setForeground(Color.black);
        btnOpen.setForeground(Color.black);
        paintBoard.setCommand(Command.LINE);
    }

    void btnRectangle_actionPerformed(ActionEvent e) {
        btnLine.setForeground(Color.black);
        btnRectangle.setForeground(Color.red);
        btnCircle.setForeground(Color.black);
        btnArc.setForeground(Color.black);
        btnPolygon.setForeground(Color.black);
        btnSave.setForeground(Color.black);
        btnOpen.setForeground(Color.black);
        paintBoard.setCommand(Command.RECTANGLE);
    }

    void btnCircle_actionPerformed(ActionEvent e) {
        btnLine.setForeground(Color.black);
        btnRectangle.setForeground(Color.black);
        btnCircle.setForeground(Color.red);
        btnArc.setForeground(Color.black);
        btnPolygon.setForeground(Color.black);
        btnSave.setForeground(Color.black);
        btnOpen.setForeground(Color.black);
        paintBoard.setCommand(Command.CIRCLE);
    }

    void btnArc_actionPerformed(ActionEvent e) {
        btnLine.setForeground(Color.black);
        btnRectangle.setForeground(Color.black);
        btnCircle.setForeground(Color.black);
        btnArc.setForeground(Color.red);
        btnPolygon.setForeground(Color.black);
        btnSave.setForeground(Color.black);
        btnOpen.setForeground(Color.black);
        paintBoard.setCommand(Command.ARC);
    }

    void btnPolygon_actionPerformed(ActionEvent e) {
        btnLine.setForeground(Color.black);
        btnRectangle.setForeground(Color.black);
        btnCircle.setForeground(Color.black);
        btnPolygon.setForeground(Color.red);
        btnSave.setForeground(Color.black);
        btnOpen.setForeground(Color.black);
        paintBoard.setCommand(Command.POLYGON);
    }

    void btnRed_actionPerformed(ActionEvent e) {
        btnRed.setForeground(Color.red);
        btnGreen.setForeground(Color.black);
        btnBlue.setForeground(Color.black);
        paintBoard.setColor(Color.red);
    }

    void btnGreen_actionPerformed(ActionEvent e) {
        btnRed.setForeground(Color.black);
        btnGreen.setForeground(Color.red);
        btnBlue.setForeground(Color.black);
        paintBoard.setColor(Color.green);
    }

    void btnBlue_actionPerformed(ActionEvent e) {
        btnRed.setForeground(Color.black);
        btnGreen.setForeground(Color.black);
        btnBlue.setForeground(Color.red);
        paintBoard.setColor(Color.blue);
    }

    void btnDefine_actionPerformed(ActionEvent e) {
        btnRed.setForeground(Color.black);
        btnGreen.setForeground(Color.black);
        btnBlue.setForeground(Color.black);
        btnBlue.setForeground(Color.red);
        Color color = JColorChooser.showDialog(this, "�Զ�����ɫ", Color.blue);
        paintBoard.setColor(color);
    }

    void btnSave_actionPerformed(ActionEvent e) {
        btnSave.setForeground(Color.red);
        btnLine.setForeground(Color.black);
        btnRectangle.setForeground(Color.black);
        btnCircle.setForeground(Color.black);
        btnOpen.setForeground(Color.black);
        String str = JOptionPane.showInputDialog(this, "������Ҫ������ļ���", "�ļ�����", JOptionPane.INFORMATION_MESSAGE);
        paintBoard.save(str);
    }

    void btnOpen_actionPerformed(ActionEvent e) {
        btnOpen.setForeground(Color.red);
        btnLine.setForeground(Color.black);
        btnRectangle.setForeground(Color.black);
        btnCircle.setForeground(Color.black);
        btnSave.setForeground(Color.black);
        String str = JOptionPane.showInputDialog(this, "�������ļ���", "���ļ�", JOptionPane.INFORMATION_MESSAGE);
        paintBoard.open(str);
    }

    public static void main(String[] args) {
        PaintMainFrame paintApp = new PaintMainFrame();
        paintApp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        paintApp.setSize(600, 500);
        paintApp.setTitle("�ҵ�2D����");
        paintApp.setVisible(true);
    }
}
