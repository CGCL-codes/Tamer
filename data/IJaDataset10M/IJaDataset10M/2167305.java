package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import data_model.gambler;
import database.DatabaseConnection;
import database.ProblemwithDataApplication;

public class player_choose_entry extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    player_choose_main owner;

    DatabaseConnection con = DatabaseConnection.getInstance();

    JLabel name_Label;

    String name;

    public player_choose_entry(player_choose_main main_owner, int id, String name, int money, int style) {
        owner = main_owner;
        name_Label = new JLabel(name);
        this.name = name;
        JLabel money_Label = new JLabel("Goldstand: " + money + "  ");
        JButton delete_player = new JButton();
        name_Label.setFont(main_window.getFont(24f));
        money_Label.setFont(main_window.getFont(18f));
        name_Label.setForeground(new Color(109, 39, 9));
        money_Label.setForeground(new Color(109, 39, 9));
        ImageIcon icon = new ImageIcon("./src/img/Delete.png");
        delete_player.setIcon(icon);
        delete_player.setBorder(null);
        delete_player.setPreferredSize(new Dimension(icon.getIconHeight() - 4, icon.getIconWidth() - 4));
        delete_player.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        this.setLayout(new BorderLayout());
        add(name_Label, BorderLayout.NORTH);
        add(money_Label, BorderLayout.WEST);
        add(delete_player, BorderLayout.EAST);
        this.setOpaque(false);
        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent me) {
                selectGamlber(me);
            }
        });
    }

    private void selectGamlber(MouseEvent me) {
        gambler g = null;
        try {
            con.connect();
            g = con.getGambler(this.name);
            main_window.setSelected_gambler(g);
            con.shutdown();
        } catch (ProblemwithDataApplication e) {
            e.printStackTrace();
        }
    }

    private void deleteActionPerformed(MouseEvent evt) {
        try {
            con.connect();
            con.deleteGambler(name);
            con.shutdown();
            owner.addEntrys();
        } catch (ProblemwithDataApplication e) {
            e.printStackTrace();
            System.out.println("PENG");
        }
    }

    public player_choose_entry(player_choose_main main_owner) {
        owner = main_owner;
        JLabel new_player = new JLabel("Neuer Spieler");
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("./src/font/Northwood High.ttf"));
            Font font_l = font.deriveFont(24f);
            new_player.setFont(font_l);
        } catch (FontFormatException ex) {
            Logger.getLogger(game_menu_panel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(game_menu_panel.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setOpaque(false);
        this.add(new_player);
        new_player.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent me) {
                entryActionPerformed(me);
            }
        });
    }

    private void entryActionPerformed(MouseEvent me) {
        if (owner != null) {
            player_choose_new add = new player_choose_new(this, owner);
        }
    }
}
