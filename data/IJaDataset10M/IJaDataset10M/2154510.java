package interfaces.rechercheiti;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PanelPrefVilleTouris extends PanelPref {

    protected ButtonGroup radiogroupVilleTouris;

    protected JLabel labelVilleTouris;

    protected JRadioButton radioVilleTourisIndif;

    protected JRadioButton radioVilleTourisOui;

    protected JRadioButton radioVilleTourisNon;

    public PanelPrefVilleTouris(PanelRechercheItineraire _ri) {
        ri = _ri;
        labelVilleTouris = new JLabel();
        radioVilleTourisIndif = new JRadioButton();
        radioVilleTourisOui = new JRadioButton();
        radioVilleTourisNon = new JRadioButton();
        radiogroupVilleTouris = new ButtonGroup();
        setBackground(new Color(255, 255, 102));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setForeground(new Color(102, 102, 255));
        labelVilleTouris.setForeground(new Color(255, 0, 0));
        labelVilleTouris.setText("Ville touristique");
        radiogroupVilleTouris.add(radioVilleTourisIndif);
        radioVilleTourisIndif.setText("Indiff�rent");
        radioVilleTourisIndif.setSelected(true);
        radioVilleTourisIndif.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioVilleTourisIndif.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radioVilleTourisIndif.setOpaque(false);
        radioVilleTourisIndif.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setIndifferent(true);
            }
        });
        radiogroupVilleTouris.add(radioVilleTourisOui);
        radioVilleTourisOui.setText("Oui");
        radioVilleTourisOui.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioVilleTourisOui.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radioVilleTourisOui.setOpaque(false);
        radioVilleTourisOui.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setIndifferent(false);
            }
        });
        radiogroupVilleTouris.add(radioVilleTourisNon);
        radioVilleTourisNon.setText("Non");
        radioVilleTourisNon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioVilleTourisNon.setMargin(new java.awt.Insets(0, 0, 0, 0));
        radioVilleTourisNon.setOpaque(false);
        radioVilleTourisNon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setIndifferent(false);
            }
        });
        javax.swing.GroupLayout panelVilleTourisLayout = new javax.swing.GroupLayout(this);
        setLayout(panelVilleTourisLayout);
        panelVilleTourisLayout.setHorizontalGroup(panelVilleTourisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelVilleTourisLayout.createSequentialGroup().addContainerGap().addComponent(labelVilleTouris, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(33, 33, 33).addComponent(radioVilleTourisIndif, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(29, 29, 29).addComponent(radioVilleTourisOui, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(75, 75, 75).addComponent(radioVilleTourisNon, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 195, Short.MAX_VALUE).addComponent(labelNumero).addComponent(flecheMonter).addComponent(flecheDescendre).addContainerGap()));
        panelVilleTourisLayout.setVerticalGroup(panelVilleTourisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVilleTourisLayout.createSequentialGroup().addGap(22, 22, 22).addGroup(panelVilleTourisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(radioVilleTourisIndif).addComponent(labelVilleTouris).addComponent(radioVilleTourisOui).addComponent(radioVilleTourisNon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(11, 11, 11)).addGroup(javax.swing.GroupLayout.Alignment.CENTER, panelVilleTourisLayout.createParallelGroup().addComponent(labelNumero).addComponent(flecheMonter).addComponent(flecheDescendre)));
        panelVilleTourisLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new Component[] { labelNumero, flecheDescendre, flecheMonter });
        panelVilleTourisLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new Component[] { labelNumero, flecheDescendre, flecheMonter });
    }
}
