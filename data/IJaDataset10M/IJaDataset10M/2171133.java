package jm2pc.server.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jm2pc.server.i18n.Messages;

public class LogoWindow extends JWindow {

    public static final long serialVersionUID = 1l;

    private ServerFrame frame;

    private Menu menu;

    private Image imCelular;

    private Color clBackDisplay;

    public LogoWindow(ServerFrame frame) {
        super();
        this.frame = frame;
        this.setAlwaysOnTop(true);
        this.setPreferredSize(new Dimension(20, 40));
        MouseListener listener = new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON3) {
                    menu.show(LogoWindow.this, 10, 20);
                } else if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 2) {
                    abrirFrame();
                }
            }
        };
        MouseMotionListener motionListener = new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent m) {
                Point p = m.getPoint();
                int x = getX() + p.x;
                int y = getY() + p.y;
                LogoWindow.this.setLocation(x - 10, y - 20);
            }
        };
        addMouseListener(listener);
        addMouseMotionListener(motionListener);
        menu = new Menu();
        add(menu);
        clBackDisplay = Color.BLACK;
        imCelular = Imagem.createMobileImage(clBackDisplay);
        pack();
        setLocationRelativeTo(null);
    }

    public void abrirFrame() {
        frame.setVisible(true);
        this.setVisible(false);
        frame.pack();
        frame.requestFocus();
        frame.setState(JFrame.NORMAL);
    }

    public void animar(final Color cor) {
        class AnimarDisplay implements Runnable {

            public void run() {
                imCelular = Imagem.createMobileImage(cor);
                LogoWindow.this.repaint();
                try {
                    Thread.sleep(500);
                    imCelular = Imagem.createMobileImage(clBackDisplay);
                    LogoWindow.this.repaint();
                } catch (InterruptedException e) {
                }
            }
        }
        Thread thread = new Thread(new AnimarDisplay());
        thread.start();
    }

    public void setServerIniciado(boolean start) {
        if (start) {
            clBackDisplay = new Color(193, 214, 230);
        } else {
            clBackDisplay = Color.BLACK;
        }
        updateGUI();
        menu.setServerIniciado(start);
    }

    private void updateGUI() {
        imCelular = Imagem.createMobileImage(clBackDisplay);
        repaint();
    }

    public void setDisplayColor(Color color) {
        clBackDisplay = color;
    }

    public void paint(Graphics g) {
        g.drawImage(imCelular, 0, 0, null);
    }

    public void alterarIdioma() {
        menu.menuAbrir.setLabel(Messages.getMessage("open"));
        menu.menuFechar.setLabel(Messages.getMessage("exit"));
        menu.menuIniciar.setLabel(Messages.getMessage("start"));
        menu.menuParar.setLabel(Messages.getMessage("stop"));
    }

    class Menu extends PopupMenu {

        static final long serialVersionUID = 1l;

        private MenuItem menuAbrir;

        private MenuItem menuFechar;

        private MenuItem menuIniciar;

        private MenuItem menuParar;

        public Menu() {
            super();
            menuAbrir = new MenuItem(Messages.getMessage("open"));
            menuAbrir.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    abrirFrame();
                }
            });
            menuIniciar = new MenuItem(Messages.getMessage("start"));
            menuIniciar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    frame.iniciar();
                }
            });
            menuParar = new MenuItem(Messages.getMessage("stop"));
            menuParar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    frame.parar();
                }
            });
            menuParar.setEnabled(false);
            menuFechar = new MenuItem(Messages.getMessage("exit"));
            menuFechar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    frame.fechar();
                }
            });
            this.add(menuAbrir);
            addSeparator();
            this.add(menuIniciar);
            this.add(menuParar);
            addSeparator();
            this.add(menuFechar);
        }

        public void setServerIniciado(boolean iniciado) {
            if (iniciado) {
                menuIniciar.setEnabled(false);
                menuParar.setEnabled(true);
            } else {
                menuIniciar.setEnabled(true);
                menuParar.setEnabled(false);
            }
        }
    }
}
