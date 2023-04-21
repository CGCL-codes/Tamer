package polr.client.ui;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import polr.client.GlobalGame;
import polr.client.logic.OurPokemon;
import polr.client.ui.base.Button;
import polr.client.ui.base.Container;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;

public class RequestWindow extends Frame {

    ArrayList<Label> playerInfo = new ArrayList<Label>();

    ArrayList<Button> tradeButtons = new ArrayList<Button>();

    ArrayList<Button> battleButtons = new ArrayList<Button>();

    ArrayList<Offer> m_offers = new ArrayList<Offer>();

    public String tradeOfferInfo, trader;

    OurPokemon[] party = new OurPokemon[6];

    Container current, otherOffers;

    Label amount, status, c1, c2, you, them, selectedPoke;

    public Button tradeCancel, tradeAccept, tradeDecline, tradeSend, incPokeIndex, decPokeIndex, incAmount, decAmount;

    int pokeIndex;

    int pokedollars;

    public RequestWindow() {
        pokeIndex = 0;
        pokedollars = 0;
        initGUI();
    }

    public void setPokeData(OurPokemon[] pokes) {
        party = pokes;
    }

    public void addTradeRequest(String username) {
        String s = "tk" + username.toString();
        Offer r = new Offer(username);
        if (m_offers.size() > 4) {
            m_offers.remove(0);
            m_offers.trimToSize();
        }
        m_offers.add(r);
        reloadRequests();
    }

    public void initGUI() {
        this.setTitle("PvP Battles/Trades");
        this.setBackground(new Color(0, 0, 0, 85));
        this.setForeground(new Color(255, 255, 255));
        this.setSize(210, 360);
        this.setLocation(0, 0);
        this.setResizable(false);
        current = new Container();
        current.setForeground(new Color(255, 255, 255));
        current.setBackground(new Color(0, 0, 0, 85));
        current.setFont(GlobalGame.getDPFontSmall());
        current.setBounds(2, 16, 240, 174);
        this.add(current);
        otherOffers = new Container();
        otherOffers.setForeground(new Color(255, 255, 255));
        otherOffers.setBackground(new Color(0, 0, 0, 85));
        otherOffers.setFont(GlobalGame.getDPFontSmall());
        otherOffers.setBounds(2, 182, 240, 174);
        this.add(otherOffers);
        tradeCancel = new Button();
        tradeCancel.setText("Cancel");
        tradeCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                GlobalGame.packetGen.write("tc");
                loadRequestInterface();
            }
        });
        tradeCancel.setSize(64, 32);
        tradeCancel.setLocation(120, 114);
        tradeCancel.pack();
        tradeAccept = new Button();
        tradeAccept.setText("Accept");
        tradeAccept.setSize(64, 32);
        tradeAccept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                GlobalGame.packetGen.write("tk");
                tradeAccept.setEnabled(false);
                tradeDecline.setEnabled(false);
            }
        });
        tradeAccept.setLocation(4, 114);
        tradeAccept.setEnabled(false);
        tradeAccept.pack();
        tradeDecline = new Button();
        tradeDecline.setText("Decline");
        tradeDecline.setSize(64, 32);
        tradeDecline.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                GlobalGame.packetGen.write("tc");
                loadRequestInterface();
            }
        });
        tradeDecline.setEnabled(false);
        tradeDecline.setLocation(60, 114);
        tradeDecline.pack();
        tradeSend = new Button();
        tradeSend.setText("Send Offer");
        tradeSend.setSize(64, 32);
        tradeSend.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                GlobalGame.packetGen.write("to" + "," + pokedollars + "," + (pokeIndex + 1));
                status.setText("Waiting...");
                tradeSend.setEnabled(false);
            }
        });
        tradeSend.setLocation(4, tradeAccept.getY() - 25);
        tradeSend.pack();
        you = new Label();
        you.setText("Your Offer:");
        you.setLocation(4, 4);
        you.setFont(GlobalGame.getDPFontSmall());
        you.setForeground(new Color(255, 255, 255));
        you.pack();
        them = new Label();
        them.setText("Their Offer:");
        them.setLocation(88, 4);
        them.setFont(GlobalGame.getDPFontSmall());
        them.setForeground(new Color(255, 255, 255));
        them.pack();
        amount = new Label();
        amount.setText("PD: " + pokedollars);
        amount.setLocation(48, 114);
        amount.setFont(GlobalGame.getDPFontSmall());
        amount.setForeground(new Color(255, 255, 255));
        amount.pack();
        current.add(amount);
        incAmount = new Button();
        incAmount.setSize(8, 8);
        incAmount.setLocation(16, 114);
        incAmount.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                pokedollars = pokedollars + 5;
                updateAmountLabel();
            }
        });
        incAmount.pack();
        current.add(incAmount);
        decAmount = new Button();
        decAmount.setSize(8, 8);
        decAmount.setLocation(16, 124);
        decAmount.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (pokedollars > 0) {
                    pokedollars = pokedollars - 5;
                    updateAmountLabel();
                }
            }
        });
        decAmount.pack();
        current.add(decAmount);
        incPokeIndex = new Button();
        incPokeIndex.setSize(8, 8);
        incPokeIndex.setLocation(4, 24);
        incPokeIndex.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (pokeIndex < 5 && party[pokeIndex + 1] != null) {
                    pokeIndex++;
                    updatePokeLabel();
                }
            }
        });
        incPokeIndex.pack();
        decPokeIndex = new Button();
        decPokeIndex.setSize(8, 8);
        decPokeIndex.setLocation(4, 32);
        decPokeIndex.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (pokeIndex > -1) {
                    pokeIndex--;
                    if (pokeIndex == -1) {
                        selectedPoke.setText("Money Only");
                    } else {
                        updatePokeLabel();
                    }
                }
            }
        });
        decPokeIndex.pack();
        selectedPoke = new Label();
        selectedPoke.setText("test");
        selectedPoke.setForeground(new Color(255, 255, 255));
        selectedPoke.setFont(GlobalGame.getDPFontSmall());
        selectedPoke.setLocation(20, 48);
        selectedPoke.pack();
        status = new Label();
        status.setText("Waiting...");
        status.setForeground(new Color(255, 255, 255));
        status.setFont(GlobalGame.getDPFontSmall());
        status.setLocation(94, 24);
        status.pack();
        c1 = new Label();
        c1.setText("Send a Request");
        c1.setFont(GlobalGame.getDPFontSmall());
        c1.setForeground(new Color(255, 255, 255));
        c1.setLocation(2, 2);
        c1.pack();
        this.add(c1);
        c2 = new Label();
        c2.setText("Requests Received");
        c2.setFont(GlobalGame.getDPFontSmall());
        c2.setForeground(new Color(255, 255, 255));
        c2.setLocation(2, 162);
        c2.pack();
        this.add(c2);
    }

    public void showOffer(int pokedollars2, OurPokemon requestedPokemon) {
        getDisplay().add(new PokeInfoPane(requestedPokemon));
        status.setText("PD: " + pokedollars2);
        tradeAccept.setEnabled(true);
        tradeDecline.setEnabled(true);
    }

    public void loadRequestInterface() {
        current.removeAll();
        incAmount.setLocation(16, 114);
        incAmount.pack();
        current.add(incAmount);
        decAmount.setLocation(16, 124);
        decAmount.pack();
        current.add(decAmount);
        amount.setLocation(48, 114);
        amount.pack();
        current.add(amount);
        for (int i = 0; i < playerInfo.size(); i++) {
            playerInfo.get(i).setLocation(2, (32 * i) + 4);
            playerInfo.get(i).setVisible(true);
            playerInfo.get(i).pack();
            current.add(playerInfo.get(i));
            tradeButtons.get(i).setLocation(110, (32 * i));
            tradeButtons.get(i).setVisible(true);
            tradeButtons.get(i).pack();
            current.add(tradeButtons.get(i));
            battleButtons.get(i).setLocation(156, (32 * i));
            battleButtons.get(i).setVisible(true);
            battleButtons.get(i).pack();
            current.add(battleButtons.get(i));
        }
    }

    public void updateAmountLabel() {
        amount.setText("PD: " + pokedollars);
    }

    public void updatePokeLabel() {
        selectedPoke.setText(party[pokeIndex].getName());
    }

    public void enableTrade() {
        tradeAccept.setEnabled(true);
        tradeDecline.setEnabled(true);
        tradeSend.setEnabled(true);
    }

    public void loadTradeInterface() {
        c1.setText("Trade : " + trader);
        current.removeAll();
        current.add(you);
        current.add(them);
        current.add(tradeCancel);
        current.add(tradeAccept);
        current.add(tradeDecline);
        current.add(tradeSend);
        incAmount.setLocation(86, tradeAccept.getY() - 25);
        current.add(incAmount);
        decAmount.setLocation(86, tradeAccept.getY() - 16);
        current.add(decAmount);
        amount.setLocation(112, tradeAccept.getY() - 25);
        current.add(amount);
        current.add(status);
        current.add(incPokeIndex);
        current.add(decPokeIndex);
        if (pokeIndex != -1 && party[pokeIndex] != null) {
            selectedPoke.setText(party[pokeIndex].getName());
        } else {
            selectedPoke.setText("Money only");
        }
        current.add(selectedPoke);
    }

    public void reloadRequests() {
        c1.setText("Send a Request");
        otherOffers.removeAll();
        for (int i = 0; i < m_offers.size(); i++) {
            m_offers.get(i).getUserLabel().setLocation(0, (48 * i) + 4);
            m_offers.get(i).getUserLabel().pack();
            otherOffers.add(m_offers.get(i).getUserLabel());
            m_offers.get(i).getButton().setLocation(124, (48 * i));
            m_offers.get(i).getButton().pack();
            otherOffers.add(m_offers.get(i).getButton());
            m_offers.get(i).getDetails().setLocation(0, (48 * i) + 24);
            m_offers.get(i).getDetails().pack();
            otherOffers.add(m_offers.get(i).getDetails());
        }
    }

    public boolean contains(String username) {
        boolean result = false;
        for (int i = 0; i < m_offers.size(); i++) {
            if (m_offers.get(i).getUsername().equalsIgnoreCase(username)) {
                result = true;
                break;
            }
        }
        for (int i = 0; i < playerInfo.size(); i++) {
            if (playerInfo.get(i).getText().substring(0, playerInfo.get(i).getText().indexOf(" L:")) == username) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void remove(String username) {
        for (int i = 0; i < m_offers.size(); i++) {
            if (m_offers.get(i).getUsername().equalsIgnoreCase(username)) {
                m_offers.remove(i);
                m_offers.trimToSize();
                reloadRequests();
            }
        }
        for (int i = 0; i < playerInfo.size(); i++) {
            if (playerInfo.get(i).getText().substring(0, playerInfo.get(i).getText().indexOf(" L:")) == username) {
                playerInfo.remove(i);
                playerInfo.trimToSize();
                tradeButtons.remove(i);
                tradeButtons.trimToSize();
                battleButtons.remove(i);
                battleButtons.trimToSize();
                loadRequestInterface();
            }
        }
    }

    public void clear() {
        playerInfo.clear();
        tradeButtons.clear();
        battleButtons.clear();
        m_offers.clear();
        current.removeAll();
        incAmount.setLocation(16, 114);
        incAmount.pack();
        current.add(incAmount);
        decAmount.setLocation(16, 124);
        decAmount.pack();
        current.add(decAmount);
        amount.setLocation(48, 114);
        amount.pack();
        current.add(amount);
        otherOffers.removeAll();
    }

    public void addRequest(final String username, String request) {
        if (request.charAt(0) == 'f') {
            System.out.println(username + " " + request);
            String s = "c" + username.toString() + "," + request.substring(request.indexOf(',') + 1);
            Offer r = new Offer(username, s, request.substring(1, request.indexOf(',')));
            if (m_offers.size() > 4) {
                m_offers.remove(0);
                m_offers.trimToSize();
            }
            m_offers.add(r);
            reloadRequests();
        } else if (request.charAt(0) == 'a') {
            Label l = new Label();
            if (username.length() > 7) l.setText(username.substring(0, 8) + " L: " + request.substring(1)); else l.setText(username + " L: " + request.substring(1));
            l.setFont(GlobalGame.getDPFontSmall());
            l.setForeground(new Color(255, 255, 255));
            l.pack();
            if (playerInfo.size() > 4) {
                playerInfo.remove(0);
                playerInfo.trimToSize();
            }
            playerInfo.add(l);
            Button b = new Button();
            b.setText("Battle");
            b.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    GlobalGame.packetGen.write("c" + username.toString() + "," + pokedollars);
                    setEnabled(false);
                }
            });
            b.pack();
            if (battleButtons.size() > 4) {
                battleButtons.remove(0);
                battleButtons.trimToSize();
            }
            battleButtons.add(b);
            Button t = new Button();
            t.setText("Trade");
            t.setToolTipText(new String(username));
            t.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    trader = username;
                    GlobalGame.packetGen.write("tb" + username);
                    setEnabled(false);
                }
            });
            t.pack();
            if (tradeButtons.size() > 4) {
                tradeButtons.remove(0);
                tradeButtons.trimToSize();
            }
            tradeButtons.add(t);
            loadRequestInterface();
        }
    }

    public void showOffer(int pokedollars2) {
        status.setText("PD: " + pokedollars2);
        tradeAccept.setEnabled(true);
        tradeDecline.setEnabled(true);
    }
}

class Offer {

    private String reqUsrname;

    private String offer;

    private Label userLabel;

    private Label details;

    private Button accept;

    public Offer(String user) {
        reqUsrname = user;
        userLabel = new Label();
        userLabel.setText(reqUsrname);
        userLabel.setForeground(new Color(255, 255, 255));
        userLabel.setFont(GlobalGame.getDPFontSmall());
        userLabel.pack();
        details = new Label();
        details.setText("Trade");
        details.setForeground(new Color(255, 255, 255));
        details.setFont(GlobalGame.getDPFontSmall());
        details.pack();
        accept = new Button();
        accept.setText("Accept");
        accept.setSize(64, 32);
        accept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                GlobalGame.packetGen.write("tb" + reqUsrname);
            }
        });
        accept.pack();
    }

    public Offer(String user, String request, String level) {
        reqUsrname = user;
        offer = request;
        System.out.println(reqUsrname + " " + offer);
        userLabel = new Label();
        userLabel.setText(reqUsrname + " L: " + level);
        userLabel.setForeground(new Color(255, 255, 255));
        userLabel.setFont(GlobalGame.getDPFontSmall());
        userLabel.pack();
        details = new Label();
        details.setText("PvP Battle: " + offer.substring(offer.indexOf(',') + 1));
        details.setForeground(new Color(255, 255, 255));
        details.setFont(GlobalGame.getDPFontSmall());
        details.pack();
        accept = new Button();
        accept.setText("Accept");
        accept.setSize(64, 32);
        accept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                GlobalGame.packetGen.write(offer);
            }
        });
        accept.pack();
    }

    public String getOffer() {
        return offer;
    }

    public String getUsername() {
        return reqUsrname;
    }

    public Label getUserLabel() {
        return userLabel;
    }

    public Label getDetails() {
        return details;
    }

    public Button getButton() {
        return accept;
    }
}
