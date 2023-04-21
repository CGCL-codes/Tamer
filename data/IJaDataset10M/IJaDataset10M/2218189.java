package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

public class Logic extends JToolBar {

    private static final long serialVersionUID = 1L;

    public Logic() {
        setFloatable(false);
        setRollover(true);
        setOrientation(JToolBar.VERTICAL);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JButton btnA = new JButton("AND", new ImageIcon("images/and.jpg"));
        btnA.setBackground(defaults.Defaults.OptionsButForColor);
        btnA.setForeground(defaults.Defaults.OptionsButBackColor);
        btnA.setHorizontalTextPosition(AbstractButton.LEFT);
        btnA.setName("AND,false,2");
        btnA.setTransferHandler(new TransferHandler("name"));
        btnA.addMouseListener(new DragMouseAdapter());
        btnA.setDropTarget(null);
        btnA.setMaximumSize(defaults.Defaults.LogicButSize);
        btnA.setMinimumSize(defaults.Defaults.LogicButSize);
        JButton btnB = new JButton("OR", new ImageIcon("images/or.jpg"));
        btnB.setBackground(defaults.Defaults.OptionsButForColor);
        btnB.setForeground(defaults.Defaults.OptionsButBackColor);
        btnB.setHorizontalTextPosition(AbstractButton.LEFT);
        btnB.setName("OR,false,2");
        btnB.setTransferHandler(new TransferHandler("name"));
        btnB.addMouseListener(new DragMouseAdapter());
        btnB.setDropTarget(null);
        btnB.setMaximumSize(defaults.Defaults.LogicButSize);
        btnB.setMinimumSize(defaults.Defaults.LogicButSize);
        JButton btnC = new JButton("XOR", new ImageIcon("images/xor.jpg"));
        btnC.setBackground(defaults.Defaults.OptionsButForColor);
        btnC.setForeground(defaults.Defaults.OptionsButBackColor);
        btnC.setHorizontalTextPosition(AbstractButton.LEFT);
        btnC.setName("XOR,false,2");
        btnC.setTransferHandler(new TransferHandler("name"));
        btnC.addMouseListener(new DragMouseAdapter());
        btnC.setDropTarget(null);
        btnC.setMaximumSize(defaults.Defaults.LogicButSize);
        btnC.setMinimumSize(defaults.Defaults.LogicButSize);
        JButton btnD = new JButton("NAND", new ImageIcon("images/nand.jpg"));
        btnD.setBackground(defaults.Defaults.OptionsButForColor);
        btnD.setForeground(defaults.Defaults.OptionsButBackColor);
        btnD.setHorizontalTextPosition(AbstractButton.LEFT);
        btnD.setName("AND,true,2");
        btnD.setTransferHandler(new TransferHandler("name"));
        btnD.addMouseListener(new DragMouseAdapter());
        btnD.setDropTarget(null);
        btnD.setMaximumSize(defaults.Defaults.LogicButSize);
        btnD.setMinimumSize(defaults.Defaults.LogicButSize);
        JButton btnE = new JButton("NOR", new ImageIcon("images/nor.jpg"));
        btnE.setBackground(defaults.Defaults.OptionsButForColor);
        btnE.setForeground(defaults.Defaults.OptionsButBackColor);
        btnE.setHorizontalTextPosition(AbstractButton.LEFT);
        btnE.setName("OR,true,2");
        btnE.setTransferHandler(new TransferHandler("name"));
        btnE.addMouseListener(new DragMouseAdapter());
        btnE.setDropTarget(null);
        btnE.setMaximumSize(defaults.Defaults.LogicButSize);
        btnE.setMinimumSize(defaults.Defaults.LogicButSize);
        JButton btnF = new JButton("XNOR", new ImageIcon("images/xnor.jpg"));
        btnF.setBackground(defaults.Defaults.OptionsButForColor);
        btnF.setForeground(defaults.Defaults.OptionsButBackColor);
        btnF.setHorizontalTextPosition(AbstractButton.LEFT);
        btnF.setName("XOR,true,2");
        btnF.setTransferHandler(new TransferHandler("name"));
        btnF.addMouseListener(new DragMouseAdapter());
        btnF.setDropTarget(null);
        btnF.setMaximumSize(defaults.Defaults.LogicButSize);
        btnF.setMinimumSize(defaults.Defaults.LogicButSize);
        JButton btnG = new JButton("NOT", new ImageIcon("images/not.jpg"));
        btnG.setBackground(defaults.Defaults.OptionsButForColor);
        btnG.setForeground(defaults.Defaults.OptionsButBackColor);
        btnG.setHorizontalTextPosition(AbstractButton.LEFT);
        btnG.setName("NOT,true,1");
        btnG.setTransferHandler(new TransferHandler("name"));
        btnG.addMouseListener(new DragMouseAdapter());
        btnG.setDropTarget(null);
        btnG.setMaximumSize(defaults.Defaults.LogicButSize);
        btnG.setMinimumSize(defaults.Defaults.LogicButSize);
        JButton btnH = new JButton("BUFFER", new ImageIcon("images/buffer.jpg"));
        btnH.setBackground(defaults.Defaults.OptionsButForColor);
        btnH.setForeground(defaults.Defaults.OptionsButBackColor);
        btnH.setHorizontalTextPosition(AbstractButton.LEFT);
        btnH.setName("NOT,false,1");
        btnH.setTransferHandler(new TransferHandler("name"));
        btnH.addMouseListener(new DragMouseAdapter());
        btnH.setDropTarget(null);
        btnH.setMaximumSize(defaults.Defaults.LogicButSize);
        btnH.setMinimumSize(defaults.Defaults.LogicButSize);
        add(btnH);
        add(btnA);
        add(btnB);
        add(btnC);
        add(btnG);
        add(btnD);
        add(btnE);
        add(btnF);
    }

    private class DragMouseAdapter extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            TransferHandler handler = c.getTransferHandler();
            handler.exportAsDrag(c, e, TransferHandler.COPY);
        }
    }
}
