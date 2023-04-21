package interfaces.rechercheiti;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import donnees.TypeRoute;

public class PanelPrefTailleRoute extends PanelPref {

    protected ArrayList<JCheckBox> checksTaillesRoutes;

    protected JLabel labelTailleRoute;

    protected ArrayList<JLabel> labelsTaillesRoutes;

    public PanelPrefTailleRoute(PanelRechercheItineraire _ri) {
        ri = _ri;
        labelTailleRoute = new JLabel();
        labelsTaillesRoutes = new ArrayList<JLabel>();
        checksTaillesRoutes = new ArrayList<JCheckBox>();
        setBackground(new Color(255, 255, 102));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setForeground(new Color(102, 102, 255));
        labelTailleRoute.setForeground(new Color(255, 0, 0));
        labelTailleRoute.setText("Taille Route");
        for (String t : TypeRoute.getListeTypesRoutes()) {
            JLabel label = new JLabel(t);
            label.setForeground(new Color(0, 0, 204));
            labelsTaillesRoutes.add(label);
            JCheckBox check = new JCheckBox("Eviter");
            check.setOpaque(false);
            check.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    checkTailleRoute();
                }
            });
            checksTaillesRoutes.add(check);
        }
        javax.swing.GroupLayout panelTaillesRoutesLayout = new javax.swing.GroupLayout(this);
        ParallelGroup groupeLabel = panelTaillesRoutesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        for (JLabel label : labelsTaillesRoutes) {
            groupeLabel.addComponent(label);
        }
        ParallelGroup groupeCheck = panelTaillesRoutesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        for (JCheckBox check : checksTaillesRoutes) {
            groupeCheck.addComponent(check);
        }
        SequentialGroup groupeVertical = panelTaillesRoutesLayout.createSequentialGroup();
        groupeVertical.addComponent(labelTailleRoute);
        for (int i = 0; i < labelsTaillesRoutes.size(); i++) {
            ParallelGroup group = panelTaillesRoutesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
            groupeVertical.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
            groupeVertical.addGroup(group);
            group.addComponent(labelsTaillesRoutes.get(i));
            group.addComponent(checksTaillesRoutes.get(i));
        }
        setLayout(panelTaillesRoutesLayout);
        panelTaillesRoutesLayout.setHorizontalGroup(panelTaillesRoutesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelTaillesRoutesLayout.createSequentialGroup().addContainerGap().addGroup(panelTaillesRoutesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(labelTailleRoute).addGroup(panelTaillesRoutesLayout.createSequentialGroup().addGap(10, 10, 10).addGroup(groupeLabel).addGap(70, 70, 70).addGroup(groupeCheck))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 225, Short.MAX_VALUE).addComponent(labelNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(flecheMonter).addComponent(flecheDescendre).addContainerGap()));
        panelTaillesRoutesLayout.setVerticalGroup(panelTaillesRoutesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(groupeVertical).addGroup(panelTaillesRoutesLayout.createParallelGroup().addComponent(labelNumero).addComponent(flecheMonter).addComponent(flecheDescendre)));
        panelTaillesRoutesLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new Component[] { labelNumero, flecheDescendre, flecheMonter });
        panelTaillesRoutesLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new Component[] { labelNumero, flecheDescendre, flecheMonter });
    }

    private void checkTailleRoute() {
        boolean isIndif = true;
        for (JCheckBox check : checksTaillesRoutes) {
            isIndif = isIndif && !check.isSelected();
        }
        setIndifferent(isIndif);
    }
}
