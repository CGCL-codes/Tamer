package view.componentes;

import operating.Gerenciador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import util.Data;
import operating.Strings;
import java.awt.ComponentOrientation;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class JStatusBarX extends JPanel {

    private static final long serialVersionUID = 2604907680947049228L;

    private JLabel jlHora;

    private JTextField jtUsuario = null;

    private JTextField jtRelease = null;

    private JTextField jtAplication = null;

    private JTextField jtEmpresa = null;

    private JLabel jlModo = null;

    private JlabelLink jLabelLink = null;

    private static final DateFormat FORMATO = new SimpleDateFormat("HH:mm:ss");

    private JPanel cantinhoRiscado = null;

    private JPanel jpNotificacao = null;

    private JPanel jpStatus = null;

    public JStatusBarX() {
        Timer t = new Timer("ClockTimer", true);
        t.schedule(new ClockTask(), 0, 1000);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(28, 28));
        add(getLabelLink(), BorderLayout.WEST);
        add(getNotificacao(), BorderLayout.EAST);
        add(getPainelCentral(), BorderLayout.CENTER);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        setBackground(new Color(240, 240, 240));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.setColor(Color.gray);
        graphics.drawLine(0, 0, getWidth(), 0);
        int r = 150, g = 150, b = 150;
        for (int y = 1; y < 6; y++) {
            graphics.setColor(new Color(r, g, b));
            graphics.drawLine(0, y, getWidth(), y);
            r = r + 20;
            g = g + 20;
            b = b + 20;
        }
    }

    /**
     * Método que retorna o link a ser chamado
     * @return
     */
    private JlabelLink getLabelLink() {
        if (jLabelLink == null) {
            jLabelLink = new JlabelLink(Strings.link);
            jLabelLink.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        }
        return jLabelLink;
    }

    /**
     * Cria o lado esquerdo da barra de status
     * @return
     */
    private JPanel getNotificacao() {
        if (jpNotificacao == null) {
            jpNotificacao = new JPanel();
            jpNotificacao.setLayout(new BorderLayout());
            jpNotificacao.add(getLabelHora(), BorderLayout.WEST);
            jpNotificacao.add(getCantinhoRiscado());
            jpNotificacao.setOpaque(false);
        }
        return jpNotificacao;
    }

    private JLabel getLabelHora() {
        if (jlHora == null) {
            jlHora = new JLabel();
            Data data = new Data();
            jlHora.setToolTipText(data.getData());
            jlHora.setIcon(new ImageIcon(getClass().getResource("/images/barrinha.PNG")));
        }
        return jlHora;
    }

    private JPanel getCantinhoRiscado() {
        if (cantinhoRiscado == null) {
            cantinhoRiscado = new JPanel(new BorderLayout());
            cantinhoRiscado.add(new JLabel(new AngledLinesWindowsCornerIcon()), BorderLayout.SOUTH);
            cantinhoRiscado.setOpaque(false);
        }
        return cantinhoRiscado;
    }

    private JPanel getPainelCentral() {
        if (jpStatus == null) {
            jpStatus = new JPanel();
            jpStatus.setLayout(null);
            jpStatus.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            jpStatus.setOpaque(false);
            jtUsuario = new JTextField();
            jtUsuario.setText(Gerenciador.getUsuario().getUsuNome());
            jtUsuario.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            jtUsuario.setHorizontalAlignment(JLabel.CENTER);
            jtUsuario.setBounds(new Rectangle(5, 5, 100, 18));
            jtUsuario.setEditable(false);
            jtUsuario.setBackground(new Color(225, 225, 225));
            jtAplication = new JTextField();
            jtAplication.setText(Strings.aplication);
            jtAplication.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            jtAplication.setHorizontalAlignment(JLabel.CENTER);
            jtAplication.setBounds(new Rectangle(110, 5, 100, 18));
            jtAplication.setEditable(false);
            jtAplication.setBackground(new Color(225, 225, 225));
            jtRelease = new JTextField();
            jtRelease.setText(Strings.release);
            jtRelease.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            jtRelease.setHorizontalAlignment(JLabel.CENTER);
            jtRelease.setBounds(new Rectangle(215, 5, 80, 18));
            jtRelease.setEditable(false);
            jtRelease.setBackground(new Color(225, 225, 225));
            jtEmpresa = new JTextField();
            jtEmpresa.setText("© " + Strings.licensed);
            jtEmpresa.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            jtEmpresa.setBounds(new Rectangle(300, 5, 295, 18));
            jtEmpresa.setEditable(false);
            jtEmpresa.setBackground(new Color(225, 225, 225));
            jlModo = new JLabel();
            jlModo.setForeground(new Color(85, 180, 55));
            jlModo.setText(Strings.modoNavegacao);
            jlModo.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            jlModo.setHorizontalAlignment(JLabel.CENTER);
            jlModo.setBounds(new Rectangle(600, 5, 80, 18));
            jlModo.setBackground(new Color(225, 225, 225));
            jpStatus.add(jtUsuario, null);
            jpStatus.add(jtAplication, null);
            jpStatus.add(jtRelease, null);
            jpStatus.add(jtEmpresa, null);
            jpStatus.add(jlModo, null);
        }
        return jpStatus;
    }

    public JlabelLink getJLabelLink() {
        return jLabelLink;
    }

    public void setJLabelLink(JlabelLink jLabelLink) {
        this.jLabelLink = jLabelLink;
    }

    public JLabel getJlHora() {
        return jlHora;
    }

    public void setJlHora(JLabel jlHora) {
        this.jlHora = jlHora;
    }

    public JLabel getJlModo() {
        return jlModo;
    }

    public void setJlModo(JLabel jlModo) {
        this.jlModo = jlModo;
    }

    public JPanel getJpNotificacao() {
        return jpNotificacao;
    }

    public void setJpNotificacao(JPanel jpNotificacao) {
        this.jpNotificacao = jpNotificacao;
    }

    public JPanel getJpStatus() {
        return jpStatus;
    }

    public void setJpStatus(JPanel jpStatus) {
        this.jpStatus = jpStatus;
    }

    public JTextField getJtAplication() {
        return jtAplication;
    }

    public void setJtAplication(JTextField jtAplication) {
        this.jtAplication = jtAplication;
    }

    public JTextField getJtEmpresa() {
        return jtEmpresa;
    }

    public void setJtEmpresa(JTextField jtEmpresa) {
        this.jtEmpresa = jtEmpresa;
    }

    public JTextField getJtRelease() {
        return jtRelease;
    }

    public void setJtRelease(JTextField jtRelease) {
        this.jtRelease = jtRelease;
    }

    public JTextField getJtUsuario() {
        return jtUsuario;
    }

    public void setJtUsuario(JTextField jtUsuario) {
        this.jtUsuario = jtUsuario;
    }

    public void setHora(Date date) {
        getLabelHora().setText(FORMATO.format(date));
    }

    private class ClockTask extends TimerTask {

        @Override
        public void run() {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    setHora(new Date());
                }
            });
        }
    }
}

class AngledLinesWindowsCornerIcon implements Icon {

    private static final Color WHITE_LINE_COLOR = new Color(255, 255, 255);

    private static final Color GRAY_LINE_COLOR = new Color(172, 168, 153);

    private static final int WIDTH = 13;

    private static final int HEIGHT = 13;

    public int getIconHeight() {
        return WIDTH;
    }

    public int getIconWidth() {
        return HEIGHT;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(WHITE_LINE_COLOR);
        g.drawLine(0, 12, 12, 0);
        g.drawLine(5, 12, 12, 5);
        g.drawLine(10, 12, 12, 10);
        g.setColor(GRAY_LINE_COLOR);
        g.drawLine(1, 12, 12, 1);
        g.drawLine(2, 12, 12, 2);
        g.drawLine(3, 12, 12, 3);
        g.drawLine(6, 12, 12, 6);
        g.drawLine(7, 12, 12, 7);
        g.drawLine(8, 12, 12, 8);
        g.drawLine(11, 12, 12, 11);
        g.drawLine(12, 12, 12, 12);
    }
}
