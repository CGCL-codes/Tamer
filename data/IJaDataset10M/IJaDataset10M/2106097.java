package vf1.iu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.*;
import java.awt.Dialog;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.sun.org.apache.xml.internal.utils.StringComparable;
import com.sun.xml.internal.ws.util.StringUtils;
import vf1.so.*;
import vf1.rn.*;
import vf2.so.VF2_SO_IServidorRMI;

/**
 * Class VF1_IU_Batalha
 */
public class VF1_IU_Batalha extends JPanel implements VF1_IU_IBatalha, MouseListener {

    /**
	 * 
	 */
    private static VF1_SO_BFTCliente cliente = VF1_SO_BFTCliente.getCliente();

    private static VF2_SO_IServidorRMI servidorrmi = VF1_SO_BFTCliente.getRmi_servidor();

    private static final long serialVersionUID = 1L;

    private JFrame frame = new JFrame();

    private JLayeredPane pane = new JLayeredPane();

    private Image bgImage = new ImageIcon("src/vf1/iu/TelaBatalha.png").getImage();

    private Image barbaraImage = new ImageIcon("src/vf1/iu/Barbara.png").getImage();

    private Image guerreiraImage = new ImageIcon("src/vf1/iu/Guerreira.png").getImage();

    private Image ninjaImage = new ImageIcon("src/vf1/iu/Ninja.png").getImage();

    private Image p1Image = new ImageIcon("src/vf1/iu/p1.png").getImage();

    private Image p2Image = new ImageIcon("src/vf1/iu/p2.png").getImage();

    private Image p3Image = new ImageIcon("src/vf1/iu/p3.png").getImage();

    private Image p4Image = new ImageIcon("src/vf1/iu/p4.png").getImage();

    private VF1_RN_Batalha batalha = new VF1_RN_Batalha();

    private int movimento;

    private int ataque;

    private JButton mover = new JButton("Mover");

    private JButton atacar = new JButton("Atacar");

    private boolean botao_mover_apertado = false;

    private boolean botao_atacar_apertado;

    private int pv1b = 15;

    private int pv1g = 15;

    private int pv1n = 15;

    private int pv2b = 15;

    private int pv2g = 15;

    private int pv2n = 15;

    private int pv3b = 0;

    private int pv3g = 0;

    private int pv3n = 0;

    private int pv4b = 0;

    private int pv4g = 0;

    private int pv4n = 0;

    private int p1bx = 0;

    private int p1by = 0;

    private int p1gx = 1;

    private int p1gy = 0;

    private int p1nx = 0;

    private int p1ny = 1;

    private int p2bx = 9;

    private int p2by = 0;

    private int p2gx = 8;

    private int p2gy = 0;

    private int p2nx = 9;

    private int p2ny = 1;

    private int p3bx = 0;

    private int p3by = 9;

    private int p3gx = 1;

    private int p3gy = 9;

    private int p3nx = 0;

    private int p3ny = 8;

    private int p4bx = 9;

    private int p4by = 9;

    private int p4gx = 8;

    private int p4gy = 9;

    private int p4nx = 9;

    private int p4ny = 8;

    public VF1_IU_Batalha() {
    }

    ;

    /**
	 * Set the value of batalha
	 * @param newVar the new value of batalha
	 */
    public void setBatalha(VF1_RN_Batalha newVar) {
        batalha = newVar;
    }

    /**
	 * Get the value of batalha
	 * @return the value of batalha
	 */
    public VF1_RN_Batalha getBatalha() {
        return batalha;
    }

    /**
	 * Set the value of movimento
	 * @param newVar the new value of movimento
	 */
    private void setMovimento(int newVar) {
        movimento = newVar;
    }

    /**
	 * Get the value of movimento
	 * @return the value of movimento
	 */
    private int getMovimento() {
        return movimento;
    }

    /**
	 * Set the value of ataque
	 * @param newVar the new value of ataque
	 */
    private void setAtaque(int newVar) {
        ataque = newVar;
    }

    /**
	 * Get the value of ataque
	 * @return the value of ataque
	 */
    private int getAtaque() {
        return ataque;
    }

    /**
	 * Set the value of mover
	 * @param newVar the new value of mover
	 */
    private void setMover(JButton newVar) {
        mover = newVar;
    }

    /**
	 * Get the value of mover
	 * @return the value of mover
	 */
    public JButton getMover() {
        return mover;
    }

    /**
	 * Set the value of atacar
	 * @param newVar the new value of atacar
	 */
    private void setAtacar(JButton newVar) {
        atacar = newVar;
    }

    /**
	 * Get the value of atacar
	 * @return the value of atacar
	 */
    public JButton getAtacar() {
        return atacar;
    }

    /**
	 * @param nomes_jogadores 
	 */
    public void apresentarTelaBatalha(List<String> nomes_jogadores) {
        this.addMouseListener(this);
        getBatalha().registerObserver(this);
        JFrame f = getFrame();
        setBackground(Color.lightGray);
        setForeground(Color.black);
        setFont(new Font("Dialog", Font.CENTER_BASELINE, 10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLayeredPane p = getPane();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(Box.createRigidArea(new Dimension(800, 600)));
        this.add(p);
        f.setContentPane(this);
        f.pack();
        int w = f.getWidth();
        int h = f.getHeight();
        p.removeAll();
        p.add(Box.createVerticalStrut(h - 600 + 480));
        p.add(Box.createHorizontalStrut(w - 800 + 700));
        this.getMover().addMouseListener(this);
        p.add(this.getMover());
        this.getAtacar().addMouseListener(this);
        p.add(this.getAtacar());
        this.add(p);
        p.setOpaque(false);
        f.setContentPane(this);
        f.setSize(w, h);
        f.setResizable(false);
        this.desabilitarBotaoMover();
        this.desabilitarBotaoAtacar();
        f.setVisible(true);
        VF1_RN_Jogador jogador1 = new VF1_RN_Jogador();
        VF1_RN_Jogador jogador2 = new VF1_RN_Jogador();
        VF1_RN_Jogador jogador3 = new VF1_RN_Jogador();
        VF1_RN_Jogador jogador4 = new VF1_RN_Jogador();
        jogador1.setNome(nomes_jogadores.get(0));
        jogador2.setNome(nomes_jogadores.get(1));
        jogador3.setNome(nomes_jogadores.get(2));
        jogador4.setNome(nomes_jogadores.get(3));
        jogador1.getBarbara().setPosicao_x(p1bx);
        jogador1.getBarbara().setPosicao_y(p1by);
        jogador1.getGuerreira().setPosicao_x(p1gx);
        jogador1.getGuerreira().setPosicao_y(p1gy);
        jogador1.getNinja().setPosicao_x(p1nx);
        jogador1.getNinja().setPosicao_y(p1ny);
        jogador2.getBarbara().setPosicao_x(p2bx);
        jogador2.getBarbara().setPosicao_y(p2by);
        jogador2.getGuerreira().setPosicao_x(p2gx);
        jogador2.getGuerreira().setPosicao_y(p2gy);
        jogador2.getNinja().setPosicao_x(p2nx);
        jogador2.getNinja().setPosicao_y(p2ny);
        jogador3.getBarbara().setPosicao_x(p3bx);
        jogador3.getBarbara().setPosicao_y(p3by);
        jogador3.getGuerreira().setPosicao_x(p3gx);
        jogador3.getGuerreira().setPosicao_y(p3gy);
        jogador3.getNinja().setPosicao_x(p3nx);
        jogador3.getNinja().setPosicao_y(p3ny);
        jogador4.getBarbara().setPosicao_x(p4bx);
        jogador4.getBarbara().setPosicao_y(p4by);
        jogador4.getGuerreira().setPosicao_x(p4gx);
        jogador4.getGuerreira().setPosicao_y(p4gy);
        jogador4.getNinja().setPosicao_x(p4nx);
        jogador4.getNinja().setPosicao_y(p4ny);
        batalha.iniciarBatalha(jogador1, jogador2, jogador3, jogador4);
    }

    /**
	 */
    public void habilitarBotaoMover(int id) {
        getMover().setEnabled(true);
        System.out.printf("\nId do habilitarBotaoMover é: %s\n", id);
        setMovimento(id);
    }

    /**
	 */
    public void desabilitarBotaoMover() {
        getMover().setEnabled(false);
        setMovimento(0);
    }

    /**
	 * @param        personagem
	 */
    public void exibirAreaMovimento(VF1_RN_Personagem personagem) {
    }

    /**
	 */
    public void habilitarBotaoAtacar(int id) {
        getAtacar().setEnabled(true);
        setAtaque(id);
    }

    /**
	 */
    public void desabilitarBotaoAtacar() {
        getAtacar().setEnabled(false);
        setAtaque(0);
    }

    /**
	 * @param        personagem
	 * @param        status
	 */
    public void atualizarStatus(VF1_RN_Personagem personagem, int status) {
    }

    /**
	 * @param        personagem
	 */
    public void exibirAreaAtaque(VF1_RN_Personagem personagem) {
    }

    /**
	 * @param        personagem
	 * @param        posicao_x
	 * @param        posicao_y
	 */
    public void apresentarPosicao(VF1_RN_Personagem personagem, int posicao_x, int posicao_y) {
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        g.drawImage(getBgImage(), 0, 0, null);
        g.drawImage(getP1Image(), 49 + p1bx * 48, 32 + p1by * 48, null);
        g.drawImage(getBarbaraImage(), 49 + p1bx * 48, 32 + p1by * 48, null);
        g.drawImage(getP1Image(), 49 + p1gx * 48, 32 + p1gy * 48, null);
        g.drawImage(getGuerreiraImage(), 49 + p1gx * 48, 32 + p1gy * 48, null);
        g.drawImage(getP1Image(), 49 + p1nx * 48, 32 + p1ny * 48, null);
        g.drawImage(getNinjaImage(), 49 + p1nx * 48, 32 + p1ny * 48, null);
        g.drawImage(getP2Image(), 49 + p2bx * 48, 32 + p2by * 48, null);
        g.drawImage(getBarbaraImage(), 49 + p2bx * 48, 32 + p2by * 48, null);
        g.drawImage(getP2Image(), 49 + p2gx * 48, 32 + p2gy * 48, null);
        g.drawImage(getGuerreiraImage(), 49 + p2gx * 48, 32 + p2gy * 48, null);
        g.drawImage(getP2Image(), 49 + p2nx * 48, 32 + p2ny * 48, null);
        g.drawImage(getNinjaImage(), 49 + p2nx * 48, 32 + p2ny * 48, null);
        g.drawImage(getP3Image(), 49 + p3bx * 48, 32 + p3by * 48, null);
        g.drawImage(getBarbaraImage(), 49 + p3bx * 48, 32 + p3by * 48, null);
        g.drawImage(getP3Image(), 49 + p3gx * 48, 32 + p3gy * 48, null);
        g.drawImage(getGuerreiraImage(), 49 + p3gx * 48, 32 + p3gy * 48, null);
        g.drawImage(getP3Image(), 49 + p3nx * 48, 32 + p3ny * 48, null);
        g.drawImage(getNinjaImage(), 49 + p3nx * 48, 32 + p3ny * 48, null);
        g.drawImage(getP4Image(), 49 + p4bx * 48, 32 + p4by * 48, null);
        g.drawImage(getBarbaraImage(), 49 + p4bx * 48, 32 + p4by * 48, null);
        g.drawImage(getP4Image(), 49 + p4gx * 48, 32 + p4gy * 48, null);
        g.drawImage(getGuerreiraImage(), 49 + p4gx * 48, 32 + p4gy * 48, null);
        g.drawImage(getP4Image(), 49 + p4nx * 48, 32 + p4ny * 48, null);
        g.drawImage(getNinjaImage(), 49 + p4nx * 48, 32 + p4ny * 48, null);
        g.drawString(Integer.toString(getPv1b()), 612, 209);
        g.drawString(Integer.toString(getPv1n()), 612, 157);
        g.drawString(Integer.toString(getPv1g()), 612, 107);
        g.drawString(Integer.toString(getPv2b()), 739, 209);
        g.drawString(Integer.toString(getPv2n()), 739, 157);
        g.drawString(Integer.toString(getPv2g()), 739, 107);
        g.drawString(Integer.toString(getPv3b()), 612, 468);
        g.drawString(Integer.toString(getPv3n()), 612, 415);
        g.drawString(Integer.toString(getPv3g()), 612, 362);
        g.drawString(Integer.toString(getPv4b()), 739, 468);
        g.drawString(Integer.toString(getPv4n()), 739, 415);
        g.drawString(Integer.toString(getPv4g()), 739, 362);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JLayeredPane getPane() {
        return pane;
    }

    public void setPane(JLayeredPane pane) {
        this.pane = pane;
    }

    public Image getBgImage() {
        return bgImage;
    }

    public void setBgImage(Image bgImage) {
        this.bgImage = bgImage;
    }

    private int x;

    private int y;

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = getMousePosition();
        if (e.getSource() == getMover()) {
            if (botao_mover_apertado == false) {
                botao_mover_apertado = true;
                botao_atacar_apertado = false;
            }
        } else if (botao_mover_apertado) {
            botao_mover_apertado = false;
            System.out.printf("\n%s %s\n", cliente.getJogador().getNome(), getBatalha().getJogador1().getNome());
            if (cliente.getJogador().getNome().equals(getBatalha().getJogador1().getNome())) {
                System.out.printf("%d\n", movimento);
                if (movimento == 1) {
                    System.out.printf("%s\n", p);
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setMovimento(0);
                        setP1bx(x);
                        setP1by(y);
                        repaint();
                        try {
                            servidorrmi.moverPersonagem(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (movimento == 2) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setMovimento(0);
                        setP1gx(x);
                        setP1gy(y);
                        repaint();
                        try {
                            servidorrmi.moverPersonagem(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (movimento == 3) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setMovimento(0);
                        setP1nx(x);
                        setP1ny(y);
                        repaint();
                        try {
                            servidorrmi.moverPersonagem(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            } else if (cliente.getJogador().getNome().equals(getBatalha().getJogador2().getNome())) {
                if (movimento == 1) {
                    System.out.printf("%s\n", p);
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setMovimento(0);
                        setP2bx(x);
                        setP2by(y);
                        repaint();
                        try {
                            servidorrmi.moverPersonagem(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (movimento == 2) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setMovimento(0);
                        setP2gx(x);
                        setP2gy(y);
                        repaint();
                        try {
                            servidorrmi.moverPersonagem(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (movimento == 3) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setMovimento(0);
                        setP2nx(x);
                        setP2ny(y);
                        repaint();
                        try {
                            servidorrmi.moverPersonagem(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            } else if (cliente.getJogador().getNome().equals(getBatalha().getJogador3().getNome())) {
                if (movimento == 1) {
                    System.out.printf("%s\n", p);
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setMovimento(0);
                        setP3bx(x);
                        setP3by(y);
                        repaint();
                        try {
                            servidorrmi.moverPersonagem(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (movimento == 2) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setMovimento(0);
                        setP3gx(x);
                        setP3gy(y);
                        repaint();
                        try {
                            servidorrmi.moverPersonagem(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (movimento == 3) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setMovimento(0);
                        setP3nx(x);
                        setP3ny(y);
                        repaint();
                        try {
                            servidorrmi.moverPersonagem(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            } else if (cliente.getJogador().getNome().equals(getBatalha().getJogador4().getNome())) {
                if (movimento == 1) {
                    System.out.printf("%s\n", p);
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setMovimento(0);
                        setP4bx(x);
                        setP4by(y);
                        repaint();
                        try {
                            servidorrmi.moverPersonagem(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (movimento == 2) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setMovimento(0);
                        setP4gx(x);
                        setP4gy(y);
                        repaint();
                        try {
                            servidorrmi.moverPersonagem(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (movimento == 3) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setMovimento(0);
                        setP4nx(x);
                        setP4ny(y);
                        repaint();
                        try {
                            servidorrmi.moverPersonagem(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
            desabilitarBotaoMover();
        }
        if (e.getSource() == getAtacar()) {
            if (botao_atacar_apertado == false) {
                botao_atacar_apertado = true;
                botao_mover_apertado = false;
            }
        } else if (botao_atacar_apertado) {
            botao_atacar_apertado = false;
            System.out.printf("\n%s %s\n", cliente.getJogador().getNome(), getBatalha().getJogador1().getNome());
            if (cliente.getJogador().getNome().equals(getBatalha().getJogador1().getNome())) {
                System.out.printf("%d\n", ataque);
                if (ataque == 1) {
                    System.out.printf("%s\n", p);
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setAtaque(0);
                        System.out.printf("\nBárbara atacou (%d,%d)\n", x, y);
                        try {
                            servidorrmi.atacarPosicao(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (ataque == 2) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setAtaque(0);
                        System.out.printf("\nGuerreira atacou (%d,%d)\n", x, y);
                        try {
                            servidorrmi.atacarPosicao(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (ataque == 3) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setAtaque(0);
                        System.out.printf("\nNinja atacou (%d,%d)\n", x, y);
                        try {
                            servidorrmi.atacarPosicao(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            } else if (cliente.getJogador().getNome().equals(getBatalha().getJogador2().getNome())) {
                if (ataque == 1) {
                    System.out.printf("%s\n", p);
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setAtaque(0);
                        System.out.printf("\nBárbara atacou (%d,%d)\n", x, y);
                        try {
                            servidorrmi.atacarPosicao(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (ataque == 2) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setAtaque(0);
                        System.out.printf("\nGuerreira atacou (%d,%d)\n", x, y);
                        try {
                            servidorrmi.atacarPosicao(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (ataque == 3) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setAtaque(0);
                        System.out.printf("\nNinja atacou (%d,%d)\n", x, y);
                        try {
                            servidorrmi.atacarPosicao(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            } else if (cliente.getJogador().getNome().equals(getBatalha().getJogador3().getNome())) {
                if (ataque == 1) {
                    System.out.printf("%s\n", p);
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setAtaque(0);
                        System.out.printf("\nBárbara atacou (%d,%d)\n", x, y);
                        try {
                            servidorrmi.atacarPosicao(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (ataque == 2) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setAtaque(0);
                        System.out.printf("\nGuerreira atacou (%d,%d)\n", x, y);
                        try {
                            servidorrmi.atacarPosicao(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (ataque == 3) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setAtaque(0);
                        System.out.printf("\nNinja atacou (%d,%d)\n", x, y);
                        try {
                            servidorrmi.atacarPosicao(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            } else if (cliente.getJogador().getNome().equals(getBatalha().getJogador4().getNome())) {
                if (ataque == 1) {
                    System.out.printf("%s\n", p);
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setAtaque(0);
                        System.out.printf("\nBárbara atacou (%d,%d)\n", x, y);
                        try {
                            servidorrmi.atacarPosicao(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (ataque == 2) {
                    if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                        x = ((p.x - 49) / 48);
                        y = ((p.y - 32) / 48);
                        setAtaque(0);
                        System.out.printf("\nGuerreira atacou (%d,%d)\n", x, y);
                        try {
                            servidorrmi.atacarPosicao(x, y);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
            if (ataque == 3) {
                if ((((p.x - 49) / 48) < 10) && (p.x >= 49) && (((p.y - 32) / 48) < 10) && (p.y >= 32)) {
                    x = ((p.x - 49) / 48);
                    y = ((p.y - 32) / 48);
                    setAtaque(0);
                    System.out.printf("\nNinja atacou (%d,%d)\n", x, y);
                    try {
                        servidorrmi.atacarPosicao(x, y);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            desabilitarBotaoAtacar();
        }
        System.out.printf("%d,", x);
        System.out.printf("%d\n", y);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void synchronizeState(VF1_RN_IBatalha subject) {
        if (subject.equals(getBatalha())) {
            System.out.printf("\nSynchronize dentro do if\n");
            VF1_RN_Jogador jogador1 = getBatalha().getJogador1();
            VF1_RN_Jogador jogador2 = getBatalha().getJogador2();
            VF1_RN_Jogador jogador3 = getBatalha().getJogador3();
            VF1_RN_Jogador jogador4 = getBatalha().getJogador4();
            pv1b = jogador1.getBarbara().getPv();
            pv1g = jogador1.getGuerreira().getPv();
            pv1n = jogador1.getNinja().getPv();
            pv2b = jogador2.getBarbara().getPv();
            pv2g = jogador2.getGuerreira().getPv();
            pv2n = jogador2.getNinja().getPv();
            pv3b = jogador3.getBarbara().getPv();
            pv3g = jogador3.getGuerreira().getPv();
            pv3n = jogador3.getNinja().getPv();
            pv4b = jogador4.getBarbara().getPv();
            pv4g = jogador4.getGuerreira().getPv();
            pv4n = jogador4.getNinja().getPv();
            p1bx = jogador1.getBarbara().getPosicao_x();
            p1by = jogador1.getBarbara().getPosicao_y();
            p1gx = jogador1.getGuerreira().getPosicao_x();
            p1gy = jogador1.getGuerreira().getPosicao_y();
            p1nx = jogador1.getNinja().getPosicao_x();
            p1ny = jogador1.getNinja().getPosicao_y();
            p2bx = jogador2.getBarbara().getPosicao_x();
            p2by = jogador2.getBarbara().getPosicao_y();
            p2gx = jogador2.getGuerreira().getPosicao_x();
            p2gy = jogador2.getGuerreira().getPosicao_y();
            p2nx = jogador2.getNinja().getPosicao_x();
            p2ny = jogador2.getNinja().getPosicao_y();
            p3bx = jogador3.getBarbara().getPosicao_x();
            p3by = jogador3.getBarbara().getPosicao_y();
            p3gx = jogador3.getGuerreira().getPosicao_x();
            p3gy = jogador3.getGuerreira().getPosicao_y();
            p3nx = jogador3.getNinja().getPosicao_x();
            p3ny = jogador3.getNinja().getPosicao_y();
            p4bx = jogador4.getBarbara().getPosicao_x();
            p4by = jogador4.getBarbara().getPosicao_y();
            p4gx = jogador4.getGuerreira().getPosicao_x();
            p4gy = jogador4.getGuerreira().getPosicao_y();
            p4nx = jogador4.getNinja().getPosicao_x();
            p4ny = jogador4.getNinja().getPosicao_y();
            repaint();
        }
        System.out.printf("\nSynchronize fora do if\n");
    }

    public void finalizarTela(String vencedor) {
        JOptionPane.showMessageDialog(this, String.format("O vencedor � %s", vencedor));
        VF1_SO_BFTCliente cliente = VF1_SO_BFTCliente.getCliente();
        cliente.finalizarCliente();
    }

    protected int getPv1b() {
        return pv1b;
    }

    protected void setPv1b(int pv1b) {
        this.pv1b = pv1b;
    }

    protected int getPv1g() {
        return pv1g;
    }

    protected void setPv1g(int pv1g) {
        this.pv1g = pv1g;
    }

    protected int getPv1n() {
        return pv1n;
    }

    protected void setPv1n(int pv1n) {
        this.pv1n = pv1n;
    }

    protected int getPv2b() {
        return pv2b;
    }

    protected void setPv2b(int pv2b) {
        this.pv2b = pv2b;
    }

    protected int getPv2g() {
        return pv2g;
    }

    protected void setPv2g(int pv2g) {
        this.pv2g = pv2g;
    }

    protected int getPv2n() {
        return pv2n;
    }

    protected void setPv2n(int pv2n) {
        this.pv2n = pv2n;
    }

    protected int getPv3b() {
        return pv3b;
    }

    protected void setPv3b(int pv3b) {
        this.pv3b = pv3b;
    }

    protected int getPv3g() {
        return pv3g;
    }

    protected void setPv3g(int pv3g) {
        this.pv3g = pv3g;
    }

    protected int getPv3n() {
        return pv3n;
    }

    protected void setPv3n(int pv3n) {
        this.pv3n = pv3n;
    }

    protected int getPv4b() {
        return pv4b;
    }

    protected void setPv4b(int pv4b) {
        this.pv4b = pv4b;
    }

    protected int getPv4g() {
        return pv4g;
    }

    protected void setPv4g(int pv4g) {
        this.pv4g = pv4g;
    }

    protected int getPv4n() {
        return pv4n;
    }

    protected void setPv4n(int pv4n) {
        this.pv4n = pv4n;
    }

    protected Image getBarbaraImage() {
        return barbaraImage;
    }

    protected void setBarbaraImage(Image barbaraImage) {
        this.barbaraImage = barbaraImage;
    }

    protected Image getGuerreiraImage() {
        return guerreiraImage;
    }

    protected void setGuerreiraImage(Image guerreiraImage) {
        this.guerreiraImage = guerreiraImage;
    }

    protected Image getNinjaImage() {
        return ninjaImage;
    }

    protected void setNinjaImage(Image ninjaImage) {
        this.ninjaImage = ninjaImage;
    }

    protected Image getP1Image() {
        return p1Image;
    }

    protected void setP1Image(Image image) {
        p1Image = image;
    }

    protected Image getP2Image() {
        return p2Image;
    }

    protected void setP2Image(Image image) {
        p2Image = image;
    }

    protected Image getP3Image() {
        return p3Image;
    }

    protected void setP3Image(Image image) {
        p3Image = image;
    }

    protected Image getP4Image() {
        return p4Image;
    }

    protected void setP4Image(Image image) {
        p4Image = image;
    }

    public int getP1bx() {
        return p1bx;
    }

    public void setP1bx(int p1bx) {
        this.p1bx = p1bx;
    }

    public int getP1by() {
        return p1by;
    }

    public void setP1by(int p1by) {
        this.p1by = p1by;
    }

    public int getP2bx() {
        return p2bx;
    }

    public void setP2bx(int p2bx) {
        this.p2bx = p2bx;
    }

    public int getP2by() {
        return p2by;
    }

    public void setP2by(int p2by) {
        this.p2by = p2by;
    }

    public static VF1_SO_BFTCliente getCliente() {
        return cliente;
    }

    public static void setCliente(VF1_SO_BFTCliente cliente) {
        VF1_IU_Batalha.cliente = cliente;
    }

    public static VF2_SO_IServidorRMI getServidorrmi() {
        return servidorrmi;
    }

    public static void setServidorrmi(VF2_SO_IServidorRMI servidorrmi) {
        VF1_IU_Batalha.servidorrmi = servidorrmi;
    }

    public boolean isBotao_mover_apertado() {
        return botao_mover_apertado;
    }

    public void setBotao_mover_apertado(boolean botao_mover_apertado) {
        this.botao_mover_apertado = botao_mover_apertado;
    }

    public boolean isBotao_atacar_apertado() {
        return botao_atacar_apertado;
    }

    public void setBotao_atacar_apertado(boolean botao_atacar_apertado) {
        this.botao_atacar_apertado = botao_atacar_apertado;
    }

    public int getP1gx() {
        return p1gx;
    }

    public void setP1gx(int p1gx) {
        this.p1gx = p1gx;
    }

    public int getP1gy() {
        return p1gy;
    }

    public void setP1gy(int p1gy) {
        this.p1gy = p1gy;
    }

    public int getP1nx() {
        return p1nx;
    }

    public void setP1nx(int p1nx) {
        this.p1nx = p1nx;
    }

    public int getP1ny() {
        return p1ny;
    }

    public void setP1ny(int p1ny) {
        this.p1ny = p1ny;
    }

    public int getP2gx() {
        return p2gx;
    }

    public void setP2gx(int p2gx) {
        this.p2gx = p2gx;
    }

    public int getP2gy() {
        return p2gy;
    }

    public void setP2gy(int p2gy) {
        this.p2gy = p2gy;
    }

    public int getP2nx() {
        return p2nx;
    }

    public void setP2nx(int p2nx) {
        this.p2nx = p2nx;
    }

    public int getP2ny() {
        return p2ny;
    }

    public void setP2ny(int p2ny) {
        this.p2ny = p2ny;
    }

    public int getP3bx() {
        return p3bx;
    }

    public void setP3bx(int p3bx) {
        this.p3bx = p3bx;
    }

    public int getP3by() {
        return p3by;
    }

    public void setP3by(int p3by) {
        this.p3by = p3by;
    }

    public int getP3gx() {
        return p3gx;
    }

    public void setP3gx(int p3gx) {
        this.p3gx = p3gx;
    }

    public int getP3gy() {
        return p3gy;
    }

    public void setP3gy(int p3gy) {
        this.p3gy = p3gy;
    }

    public int getP3nx() {
        return p3nx;
    }

    public void setP3nx(int p3nx) {
        this.p3nx = p3nx;
    }

    public int getP3ny() {
        return p3ny;
    }

    public void setP3ny(int p3ny) {
        this.p3ny = p3ny;
    }

    public int getP4bx() {
        return p4bx;
    }

    public void setP4bx(int p4bx) {
        this.p4bx = p4bx;
    }

    public int getP4by() {
        return p4by;
    }

    public void setP4by(int p4by) {
        this.p4by = p4by;
    }

    public int getP4gx() {
        return p4gx;
    }

    public void setP4gx(int p4gx) {
        this.p4gx = p4gx;
    }

    public int getP4gy() {
        return p4gy;
    }

    public void setP4gy(int p4gy) {
        this.p4gy = p4gy;
    }

    public int getP4nx() {
        return p4nx;
    }

    public void setP4nx(int p4nx) {
        this.p4nx = p4nx;
    }

    public int getP4ny() {
        return p4ny;
    }

    public void setP4ny(int p4ny) {
        this.p4ny = p4ny;
    }
}
