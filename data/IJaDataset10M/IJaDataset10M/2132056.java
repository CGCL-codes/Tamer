package multiplayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.rmi.registry.*;

/**
     *
     * Codigo pricipal que gera a Interface para o usurio de uma aplicao
     * que usa um Canvas de OpenGL portado para Java com a ajuda de GL4JAva
     *
     * Baseado no Trabalho de  Jeff Kirby, Darren Hodges e Neon Helium.
     *
     */
public class Base extends JApplet implements MouseMotionListener, MouseListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    SoloCanvas canvas;

    Quadriculado blankArea;

    Pacote pac;

    static final int nQuad = 40;

    int i, j;

    ServerInterface myCServer;

    /** void init()  Inicializa o applet.  */
    public void init() {
        String userInput = JOptionPane.showInputDialog(null, "Escolha servidor (n para nenhum):");
        if (userInput.equals("n")) {
            JOptionPane.showMessageDialog(null, new String("sem servidor"));
            pac = new Pacote(40);
        } else try {
            Registry registry = LocateRegistry.getRegistry("localhost", 10000);
            myCServer = (ServerInterface) registry.lookup("Servidor");
            pac = myCServer.getPacote();
            JOptionPane.showMessageDialog(null, new String("Logado no Servidor"));
        } catch (java.rmi.ConnectException ce) {
            System.out.println("Conexao com o servidor encerrou.");
            System.exit(1);
        } catch (RemoteException Re) {
            System.out.println(Re.getMessage());
        } catch (NotBoundException NBe) {
            System.out.println(NBe.getMessage());
        }
        JPanel contentPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        contentPane.setLayout(gridbag);
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        blankArea = new Quadriculado(nQuad, (int) (getSize().width * 0.4), pac.matrizObjeto);
        gridbag.setConstraints(blankArea, c);
        contentPane.add(blankArea);
        ImageIcon icon = new ImageIcon("middle.gif");
        JTabbedPane tabbedPane = new JTabbedPane();
        Component panel1 = makeTextPanel("Blah");
        tabbedPane.addTab("One", icon, panel1, "Does nothing");
        tabbedPane.setSelectedIndex(0);
        Component panel2 = makeTextPanel("Blah blah");
        tabbedPane.addTab("Two", icon, panel2, "Does twice as much nothing");
        Component panel3 = makeTextPanel("Blah blah blah");
        tabbedPane.addTab("Three", icon, panel3, "Still does nothing");
        Component panel4 = makeTextPanel("Blah blah blah blah");
        tabbedPane.addTab("Four", icon, panel4, "Does nothing at all");
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        gridbag.setConstraints(tabbedPane, c);
        contentPane.add(tabbedPane);
        c.anchor = GridBagConstraints.NORTHEAST;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 2;
        c.fill = GridBagConstraints.NONE;
        c.ipady = 0;
        canvas = new SoloCanvas(640, 480, nQuad);
        canvas.setMatriz(pac.matrizAltura, pac.matrizObjeto);
        canvas.requestFocus();
        gridbag.setConstraints(canvas, c);
        contentPane.add(canvas);
        blankArea.addMouseMotionListener(this);
        addMouseMotionListener(this);
        blankArea.addMouseListener(this);
        addMouseListener(this);
        setContentPane(contentPane);
    }

    protected Component makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JButton botao = new JButton(text);
        JButton botao2 = new JButton(text);
        panel.setLayout(new GridLayout(6, 5));
        panel.add(botao);
        panel.add(botao2);
        return panel;
    }

    /** void start() Comea o applet. */
    public void start() {
    }

    /** void stop() Para o applet. */
    public void stop() {
    }

    /** void destroy() Destroi o applet. */
    public void destroy() {
    }

    public void mouseMoved(MouseEvent e) {
        if (e.getComponent().getClass().getName() == "civitas.Quadriculado") {
            int largQuad = blankArea.getLarg();
            int x = (int) (e.getX() / largQuad);
            int y = (int) (e.getY() / largQuad);
            Objeto2 objeto = new Objeto2(2);
            boolean pode = true;
            for (i = x; i < (x + objeto.larg); i++) for (j = y; j < (y + objeto.comp); j++) if (j < nQuad && i < nQuad) {
                if (pac.matrizObjeto[i][j] != 0) pode = false;
            } else pode = false;
            blankArea.setCorMouse(pode);
            blankArea.setPonto(new Point(e.getX(), e.getY()));
            blankArea.setObjDim(objeto.larg, objeto.comp);
            blankArea.repaint();
            canvas.setPonto(new Point(x, y));
            canvas.setObjDim(new Dimension(objeto.larg, objeto.comp));
        }
        if (myCServer != null) try {
            pac = myCServer.getPacote();
            canvas.setMatriz(pac.matrizAltura, pac.matrizObjeto);
            blankArea.setMatriz(pac.matrizObjeto);
        } catch (java.rmi.RemoteException ce) {
            System.out.println("Conexo com o servidor falhou!!");
            System.exit(1);
        }
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        if (e.getComponent().getClass().getName() == "civitas.Quadriculado") {
            blankArea.setPontoNull();
            blankArea.repaint();
            canvas.setPontoNull();
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getComponent().getClass().getName() == "civitas.Quadriculado") {
            int largQuad = blankArea.getLarg();
            int x = (int) (e.getX() / largQuad);
            int y = (int) (e.getY() / largQuad);
            Objeto2 objeto = new Objeto2(2);
            boolean pode = true;
            for (i = x; i < (x + objeto.larg); i++) for (j = y; j < (y + objeto.comp); j++) if (j < nQuad && i < nQuad) {
                if (pac.matrizObjeto[i][j] != 0) pode = false;
            } else pode = false;
            if (pode) {
                if (objeto.espaco) for (i = (x - 1); i < (x + objeto.larg + 1); i++) for (j = (y - 1); j < (y + objeto.comp + 1); j++) if (i > -1 && j > -1 && j < nQuad && i < nQuad) pac.matrizObjeto[i][j] = 1; else for (i = x; i < (x + objeto.larg); i++) for (j = y; j < (y + objeto.comp); j++) if (j < nQuad && i < nQuad) pac.matrizObjeto[i][j] = 1;
                pac.matrizObjeto[x][y] = objeto.codigo;
                canvas.setMatriz(pac.matrizAltura, pac.matrizObjeto);
                blankArea.setMatriz(pac.matrizObjeto);
                if (myCServer != null) try {
                    pac = myCServer.sendPacote(pac);
                } catch (java.rmi.RemoteException ce) {
                    System.out.println("Conexo com o servidor falhou!!");
                    System.exit(1);
                }
            }
        }
    }

    public static void main(String args[]) {
        Base applet = new Base();
        applet.setSize(800, 600);
        applet.init();
        applet.start();
        JFrame f = new JFrame("Base do Civitas em OpenGL");
        f.getContentPane().add(applet);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}
