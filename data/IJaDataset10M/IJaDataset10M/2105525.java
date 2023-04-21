package pokeglobal.client.ui;

import java.io.PrintWriter;
import org.newdawn.slick.Color;
import org.newdawn.slick.loading.LoadingList;
import pokeglobal.client.logic.OurPokemon;
import pokeglobal.client.network.PacketGenerator;
import pokeglobal.client.ui.base.Button;
import pokeglobal.client.ui.base.Container;
import pokeglobal.client.ui.base.Frame;
import pokeglobal.client.ui.base.Label;
import pokeglobal.client.ui.base.ProgressBar;
import pokeglobal.client.ui.base.ToggleButton;
import pokeglobal.client.ui.base.event.ActionEvent;
import pokeglobal.client.ui.base.event.ActionListener;

public class TeamForBox extends Frame {

    Container[] pokes = new Container[6];

    ToggleButton[] pokeIcon = new ToggleButton[6];

    Label[] pokeName = new Label[6];

    Label[] level = new Label[6];

    ProgressBar[] hp = new ProgressBar[6];

    Button accept = new Button();

    Button cancel = new Button();

    private int teamIndex = 0, boxNumber = 0, boxIndex = 0;

    private PacketGenerator packetGen;

    public TeamForBox(OurPokemon[] ourPokes, int boxNum, int boxInd, PacketGenerator out) {
        boxNumber = boxNum;
        boxIndex = boxInd;
        packetGen = out;
        loadImages(ourPokes);
        initGUI();
        setVisible(true);
    }

    public void initGUI() {
        int y = 0;
        for (int i = 0; i < 6; i++) {
            pokes[i] = new Container();
            pokes[i].setSize(170, 42);
            pokes[i].setVisible(true);
            pokes[i].setLocation(0, y);
            y += 41;
            getContentPane().add(pokes[i]);
            pokes[i].setOpaque(true);
            try {
                pokes[i].add(pokeIcon[i]);
                pokeIcon[i].setLocation(2, 3);
                pokes[i].add(pokeName[i]);
                pokeName[i].setLocation(40, 5);
                pokes[i].add(level[i]);
                level[i].setLocation(pokeName[i].getX() + pokeName[i].getWidth() + 10, 5);
                hp[i].setSize(114, 10);
                hp[i].setLocation(40, pokeName[i].getY() + pokeName[i].getHeight() + 5);
                pokes[i].add(hp[i]);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        accept.setSize(80, 30);
        accept.setLocation(3, 245);
        accept.setText("Accept");
        accept.setEnabled(false);
        accept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                switchPokes(boxNumber, boxIndex, teamIndex);
                packetGen.write("F");
                setVisible(false);
            }
        });
        add(accept);
        cancel.setSize(80, 30);
        cancel.setLocation(86, 245);
        cancel.setText("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                packetGen.write("F");
                setVisible(false);
            }
        });
        add(cancel);
        getTitleBar().setVisible(false);
        setResizable(false);
        setSize(170, 302);
        setAlwaysOnTop(true);
        setOpaque(true);
    }

    public void loadImages(OurPokemon[] pokes) {
        LoadingList.setDeferredLoading(true);
        for (int i = 0; i < 6; i++) {
            pokeIcon[i] = new ToggleButton();
            pokeName[i] = new Label();
            level[i] = new Label();
            hp[i] = new ProgressBar(0, 0);
            hp[i].setForeground(Color.green);
            final int j = i;
            pokeIcon[i].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    setChoice(j);
                }
            });
            pokeIcon[i].setSize(32, 32);
            pokeName[i].pack();
            try {
                if (pokes[i] != null) {
                    level[i].setText("Lv: " + String.valueOf(pokes[i].getLevel()));
                    level[i].pack();
                    pokeName[i].setText(pokes[i].getName());
                    pokeIcon[i].setImage(pokes[i].getIcon());
                    pokes[i].setIcon();
                    hp[i].setMaximum(pokes[i].getMaxHP());
                    hp[i].setForeground(Color.green);
                    hp[i].setValue(pokes[i].getCurHP());
                    if (pokes[i].getCurHP() > pokes[i].getMaxHP() / 2) {
                        hp[i].setForeground(Color.green);
                    } else if (pokes[i].getCurHP() < pokes[i].getMaxHP() / 2 && pokes[i].getCurHP() > pokes[i].getMaxHP() / 3) {
                        hp[i].setForeground(Color.orange);
                    } else if (pokes[i].getCurHP() < pokes[i].getMaxHP() / 3) {
                        hp[i].setForeground(Color.red);
                    }
                    pokes[i].setIcon();
                    pokeIcon[i].setImage(pokes[i].getIcon());
                    pokeIcon[i].setSize(32, 32);
                    pokeName[i].setText(pokes[i].getName());
                    pokeName[i].pack();
                    level[i].setText("Lv: " + String.valueOf(pokes[i].getLevel()));
                    level[i].pack();
                } else {
                    hp[i].setVisible(false);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        LoadingList.setDeferredLoading(false);
    }

    public void setChoice(int x) {
        for (int i = 0; i < 6; i++) {
            pokeIcon[i].setSelected(false);
        }
        pokeIcon[x].setSelected(true);
        accept.setEnabled(true);
        teamIndex = x;
    }

    public int setSpriteNumber(int x) {
        int i = 0;
        if (x <= 385) {
            i = x + 1;
        } else if (x <= 388) {
            i = 386;
        } else if (x <= 414) {
            i = x - 2;
        } else if (x <= 416) {
            i = 413;
        } else {
            i = x - 4;
        }
        return i;
    }

    public void switchPokes(int boxNum, int boxIndex, int teamIndex) {
        packetGen.write("w" + boxNum + "," + boxIndex + "," + teamIndex);
    }
}
