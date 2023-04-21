package net.sourceforge.yagurashogi.client.gui;

import net.sourceforge.yagurashogi.client.ShogiClient;
import net.sourceforge.yagurashogi.client.network.*;
import net.sourceforge.yagurashogi.client.core.*;

public class ProposeGameForm extends javax.swing.JFrame {

    public ProposeGameForm() {
        initComponents();
    }

    public String gameID, playerName1, playerName2;

    public boolean myTurn;

    public int byoyomi, totalTime;

    private String getParameter(String paramName, String source) {
        String tmp = source.substring(source.indexOf(paramName));
        return tmp.substring(paramName.length() + 1, tmp.indexOf("\n"));
    }

    public void showProposition(String gameConditions) {
        gameID = getParameter("Game_ID", gameConditions);
        playerName1 = getParameter("Name+", gameConditions);
        playerName2 = getParameter("Name-", gameConditions);
        String turn = getParameter("Your_Turn", gameConditions);
        myTurn = turn.equals("+");
        jLabel6.setText(myTurn ? "YES" : "NO");
        String totalTimeStr = getParameter("Total_Time", gameConditions);
        try {
            totalTime = Integer.parseInt(totalTimeStr);
        } catch (Exception e) {
            totalTime = 1500;
            totalTimeStr = "1500";
        }
        jLabel11.setText(totalTimeStr);
        String byoyomiStr = getParameter("Byoyomi", gameConditions);
        try {
            byoyomi = Integer.parseInt(byoyomiStr);
        } catch (Exception e) {
            byoyomi = 90;
            byoyomiStr = "90";
        }
        jLabel13.setText(byoyomiStr);
        jLabel3.setText(gameID);
        jLabel7.setText(playerName1);
        jLabel9.setText(playerName2);
        jTextArea1.setText(gameConditions);
        jTextArea1.repaint();
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        scrollPane1 = new java.awt.ScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        setTitle("Game proposition");
        setLocationByPlatform(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel1.setText("Game proposition");
        jButton1.setText("Accept");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setText("Reject");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jLabel2.setText("Game ID:");
        jLabel3.setText("<ID>");
        jLabel4.setText("Game details:");
        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        scrollPane1.add(jScrollPane1);
        jLabel5.setText("Your turn:");
        jLabel6.setText("<your_turn>");
        jLabel7.setText("<player1>");
        jLabel8.setText("Player 1:");
        jLabel9.setText("<player 2>");
        jLabel10.setText("Player 2:");
        jLabel11.setText("<total_time>");
        jLabel12.setText("Total time:");
        jLabel13.setText("<byoyomi>");
        jLabel14.setText("Byoyomi:");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addGap(54, 54, 54).addComponent(jLabel3).addContainerGap(318, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 334, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(99, 99, 99).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)))).addGap(45, 45, 45)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(99, 99, 99).addComponent(jLabel6)).addGroup(layout.createSequentialGroup().addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(281, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(99, 99, 99).addComponent(jLabel7)).addGroup(layout.createSequentialGroup().addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(293, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(99, 99, 99).addComponent(jLabel9)).addGroup(layout.createSequentialGroup().addComponent(jLabel10).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(290, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(99, 99, 99).addComponent(jLabel11)).addGroup(layout.createSequentialGroup().addComponent(jLabel12).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(99, 99, 99).addComponent(jLabel13)).addGroup(layout.createSequentialGroup().addComponent(jLabel14).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(281, Short.MAX_VALUE)))).addGroup(layout.createSequentialGroup().addGap(151, 151, 151).addComponent(jLabel1).addContainerGap(145, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(14, 14, 14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel8).addComponent(jLabel7)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel10).addComponent(jLabel9)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel12).addComponent(jLabel11)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel14).addComponent(jLabel13)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton1).addComponent(jButton2))).addComponent(jLabel4)).addContainerGap()));
        pack();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        ServerConnection.out.println("REJECT " + gameID);
        setVisible(false);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        ShogiClient.mainForm.board = new ShogiBoard();
        ShogiClient.mainForm.board.setStandardBoard(ShogiClient.mainForm.jGamePanel, ShogiClient.mainForm.jBaseDownPanel, ShogiClient.mainForm.jBaseUpPanel);
        Player player1, player2;
        int mySide = (ShogiClient.connectForm.jTextField3.getText().equals(playerName1) ? Game.DOWN : Game.UP);
        if (ShogiClient.connectForm.jTextField3.getText().equals(playerName1)) {
            player1 = new HumanPlayer(ShogiButton.DOWN);
            player2 = new NetworkPlayer(ShogiButton.UP);
            ShogiClient.listener.setMainPlayer((NetworkPlayer) player2);
        } else {
            player1 = new NetworkPlayer(ShogiButton.DOWN);
            player2 = new HumanPlayer(ShogiButton.UP);
            ShogiClient.listener.setMainPlayer((NetworkPlayer) player1);
        }
        Game game = new Game(player1, player2, ShogiClient.mainForm.board);
        ShogiClient.mainForm.game = game;
        game.setFirstPlayer((mySide == Game.DOWN && myTurn) || (mySide == Game.UP && !myTurn) ? Game.DOWN : Game.UP);
        game.setByoyomi(byoyomi);
        game.setTotalTime(totalTime);
        game.setGameTimeLabel(ShogiClient.mainForm.jLabel6);
        game.setCurrentPlayerLabel(ShogiClient.mainForm.jLabel7);
        game.setByoyomiLabel(ShogiClient.mainForm.jLabel8);
        ShogiClient.mainForm.jLabel9.setText((totalTime / 60 < 10 ? "0" : "") + totalTime / 60 + ":" + (totalTime % 60 < 10 ? "0" : "") + totalTime % 60);
        ShogiClient.mainForm.jLabel10.setText((byoyomi / 60 < 10 ? "0" : "") + byoyomi / 60 + ":" + (byoyomi % 60 < 10 ? "0" : "") + byoyomi % 60);
        ShogiClient.mainForm.clearGameHistory();
        ShogiClient.mainForm.clearChat();
        ServerConnection.out.println("AGREE " + gameID);
        setVisible(false);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        ServerConnection.out.println("REJECT " + gameID);
        setVisible(false);
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea jTextArea1;

    private java.awt.ScrollPane scrollPane1;
}
