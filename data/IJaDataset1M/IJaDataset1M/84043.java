package org.ekstrabilet.stadium;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.ekstrabilet.game.beans.Game;
import org.ekstrabilet.game.beans.SectorPrice;
import org.ekstrabilet.game.gui.AddGameTab;
import org.ekstrabilet.game.gui.GameEditPane;
import org.ekstrabilet.game.logic.Row;
import org.ekstrabilet.stadium.beans.Sector;
import org.ekstrabilet.stadium.beans.Stadium;
import org.ekstrabilet.stadium.beans.Tribune;
import org.ekstrabilet.stadium.constants.StadiumConstants;

/**
 * 
 * @author Maciej Koch
 * Class responsible for setting ticket prices in each of sectors.
 */
public class StadiumPricesPane extends JPanel {

    Game game;

    Stadium stadium;

    View view = new View();

    JPanel pane;

    List<JTextField> prices;

    List<JLabel> labels;

    JButton save;

    AddGameTab addGameTab;

    public StadiumPricesPane(AddGameTab addGameTab) {
        this.addGameTab = addGameTab;
    }

    private void init() {
        removeAll();
        save = new JButton("apply");
        save.addActionListener(new SaveListener());
        int l = 0;
        for (Tribune t : stadium.getTribunes()) for (Sector s : t.getSectors()) l++;
        pane = new JPanel(new GridLayout(l, 2));
        prices = new ArrayList<JTextField>();
        labels = new ArrayList<JLabel>();
        int i = 0;
        for (Tribune t : stadium.getTribunes()) for (Sector s : t.getSectors()) {
            JLabel label = new JLabel((char) (i + 65) + "");
            labels.add(label);
            pane.add(label);
            JTextField text = new JTextField();
            text.setColumns(3);
            text.setText(StadiumConstants.sectorPrice + "");
            prices.add(text);
            pane.add(text);
            i++;
        }
        add(save);
        add(pane);
    }

    public void paintComponent(Graphics g) {
        g.fillRect(0, 0, 250, 150);
        g.setColor(Color.green);
        g.fillRect(StadiumConstants.mPX, StadiumConstants.mPY, StadiumConstants.mPW, StadiumConstants.mPH);
        view.renderTribunes(g, stadium);
        view.renderSigns(g, stadium);
    }

    /**
	 * Sets game bean
	 * @param game
	 */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
	 * Sets stadium
	 * @param stadium
	 */
    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
        Tribune[] tribunes = stadium.getTribunes();
        int h = calculateHeight(stadium.getCapacity());
        tribunes[0].set(StadiumConstants.mPX, StadiumConstants.mPY - h, StadiumConstants.mPW, h);
        tribunes[1].set(StadiumConstants.mPX, StadiumConstants.mPY + StadiumConstants.mPH, StadiumConstants.mPW, h);
        tribunes[2].set(StadiumConstants.mPX - h, StadiumConstants.mPY - h, h, StadiumConstants.mPH + 2 * h);
        tribunes[3].set(StadiumConstants.mPX + StadiumConstants.mPW, StadiumConstants.mPY - h, h, StadiumConstants.mPH + 2 * h);
        int sign = 65;
        for (int i = 0; i < 4; i++) {
            List<Sector> s = tribunes[i].getSectors();
            for (int j = 0; j < s.size(); j++) {
                if (i < 2) {
                    float width = tribunes[i].width / s.size();
                    s.get(j).set(tribunes[i].x + j * width, tribunes[i].y, width, tribunes[i].height);
                } else {
                    float height = tribunes[i].height / s.size();
                    s.get(j).set(tribunes[i].x, tribunes[i].y + j * height, tribunes[i].width, height);
                }
                s.get(j).setSign((char) sign++);
            }
        }
        init();
    }

    /**
	 * 
	 * @return stadium
	 */
    public Stadium getStadium() {
        return stadium;
    }

    private int calculateHeight(int h) {
        return 25;
    }

    class SaveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int i = 0;
            List<SectorPrice> sp = new LinkedList<SectorPrice>();
            SectorPrice p;
            for (Tribune t : stadium.getTribunes()) for (Sector s : t.getSectors()) {
                try {
                    p = new SectorPrice(s.getSectorId(), Integer.parseInt(prices.get(i).getText()));
                } catch (NumberFormatException arg0) {
                    JOptionPane.showMessageDialog(pane, "Wrong values!");
                    return;
                }
                i++;
                sp.add(p);
            }
            game.setSectorPrices(sp);
            Row row = new Row();
            row.setHomeTeam(game.getTeamA());
            row.setVisitorTeam(game.getTeamB());
            row.setCity(game.getStadiumCity());
            row.setStadium(game.getStadiumName());
            row.setDate(game.getGameDate());
            row.setTime(game.getGameTime());
            addGameTab.getGameEditPane().getGameTable().addRow(row);
            addGameTab.getGameEditPane().getAddList().add(game);
            addGameTab.switchPane(AddGameTab.GAME);
        }
    }
}
