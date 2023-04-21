package com.forca.cliente;

import java.net.*;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import com.DataBase;
import com.forca.formulario.Jogo;
import com.forca.servidor.Servidor;

public class Cliente {

    public static Cliente singleton;

    public static JLabel nome;

    public static JLabel ip;

    public static JLabel nivel;

    public static JTextField txNome;

    public static JTextField txIp;

    public static JTextField txNivel;

    public static JButton start;

    public static String SERVER_IP = "192.168.1.9";

    public static String CLIENT_NAME;

    public static String GAME_LEVEL;

    public static String QUERO_NOVA_PALAVRA = "0";

    public static String TESTA_LETRA = "1";

    public static int erros = 0;

    public static PrintStream ps;

    public static Jogo jogo;

    public static JFrame config;

    public static Cliente getSingleton() {
        if (singleton == null) {
            singleton = new Cliente();
        }
        return singleton;
    }

    public Cliente() {
        jogo = new Jogo(this);
        configurationOk();
    }

    public void configurationOk() {
        while (Jogo.flag != 1) {
        }
        String host = Cliente.SERVER_IP;
        Socket socket = null;
        try {
            socket = new Socket(host, 6500);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ps.println(QUERO_NOVA_PALAVRA);
        ps.println(GAME_LEVEL);
        ps.println(CLIENT_NAME);
        while (true) {
            String line = null;
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line.equals(Servidor.VICTORY)) {
                erros = 0;
                int outravez = JOptionPane.showConfirmDialog(null, "PARABENS, VOCE VENCEU !! Jogar outra vez ?");
                if (outravez == JOptionPane.OK_OPTION) {
                    ps.println(QUERO_NOVA_PALAVRA);
                    ps.println(GAME_LEVEL);
                    ps.println(CLIENT_NAME);
                }
            }
            if (line.equals(Servidor.GAME_OVER)) {
                jogo.setImagem(erros + 1);
                erros = 0;
                String rankingTxt = "";
                try {
                    rankingTxt = br.readLine();
                } catch (HeadlessException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rankingTxt = pt.moredata.util.StringUtils.replace(rankingTxt, "|", "\n");
                JOptionPane.showMessageDialog(null, rankingTxt);
                int outravez = JOptionPane.showConfirmDialog(null, "GAME OVER !! Jogar outra vez ?");
                if (outravez == JOptionPane.OK_OPTION) {
                    ps.println(QUERO_NOVA_PALAVRA);
                    ps.println(GAME_LEVEL);
                    ps.println(CLIENT_NAME);
                } else {
                    break;
                }
            }
            if (line.equals(Servidor.PALAVRA_SETADA) || line.equals(Servidor.LETRA_NAO_ENCONTRADA)) {
                if (line.equals(Servidor.LETRA_NAO_ENCONTRADA)) {
                    erros += 1;
                    jogo.setImagem(erros);
                }
                if (line.equals(Servidor.PALAVRA_SETADA)) {
                    JOptionPane.showMessageDialog(null, "Servidor est� pronto!");
                    try {
                        jogo.getDica().setText(br.readLine().toUpperCase());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        jogo.getLabel().setText(br.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    jogo.setImagem(0);
                }
                jogo.getCaixa().setText("");
                jogo.getCaixa().requestFocus();
            }
            if (line.charAt(0) == Servidor.LETRA_ENCONTRADA.toCharArray()[0]) {
                String exibir = line.substring(2);
                jogo.getLabel().setText(exibir);
                jogo.getCaixa().setText("");
                jogo.getCaixa().requestFocus();
                if (exibir.indexOf("_") == -1) {
                    jogo.setImagem(erros + 1);
                    erros = 0;
                    int outravez = JOptionPane.showConfirmDialog(null, "GAME OVER !! Jogar outra vez ?");
                    if (outravez == JOptionPane.OK_OPTION) {
                        ps.println(QUERO_NOVA_PALAVRA);
                        ps.println(GAME_LEVEL);
                        ps.println(CLIENT_NAME);
                    } else {
                        break;
                    }
                }
            }
        }
        System.out.println("saiu do loop");
        try {
            socket.close();
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public static void main(String args[]) throws Exception {
        Cliente c = new Cliente();
    }
}
