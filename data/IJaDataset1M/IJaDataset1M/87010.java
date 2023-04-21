package ine5612.components.virtualkeyboard.gui;

import ine5612.components.virtualkeyboard.VirtualMultilayoutKeyboard;

/**
 *
 * @author Gabriel, Ramon
 */
public class CalcKeyboard extends javax.swing.JPanel {

    /** Creates new form CalcKeyboard */
    public CalcKeyboard() {
        initComponents();
    }

    private void initComponents() {
        b7 = new javax.swing.JButton();
        b8 = new javax.swing.JButton();
        b9 = new javax.swing.JButton();
        b4 = new javax.swing.JButton();
        b5 = new javax.swing.JButton();
        b6 = new javax.swing.JButton();
        b1 = new javax.swing.JButton();
        b2 = new javax.swing.JButton();
        b3 = new javax.swing.JButton();
        b0 = new javax.swing.JButton();
        bInverse = new javax.swing.JButton();
        bPoint = new javax.swing.JButton();
        setLayout(new java.awt.GridLayout(4, 3, 3, 3));
        b7.setText("7");
        b7.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcKeyboard.this.actionPerformed(evt);
            }
        });
        add(b7);
        b8.setText("8");
        b8.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcKeyboard.this.actionPerformed(evt);
            }
        });
        add(b8);
        b9.setText("9");
        b9.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcKeyboard.this.actionPerformed(evt);
            }
        });
        add(b9);
        b4.setText("4");
        b4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcKeyboard.this.actionPerformed(evt);
            }
        });
        add(b4);
        b5.setText("5");
        b5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcKeyboard.this.actionPerformed(evt);
            }
        });
        add(b5);
        b6.setText("6");
        b6.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcKeyboard.this.actionPerformed(evt);
            }
        });
        add(b6);
        b1.setText("1");
        b1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcKeyboard.this.actionPerformed(evt);
            }
        });
        add(b1);
        b2.setText("2");
        b2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcKeyboard.this.actionPerformed(evt);
            }
        });
        add(b2);
        b3.setText("3");
        b3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcKeyboard.this.actionPerformed(evt);
            }
        });
        add(b3);
        b0.setText("0");
        b0.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcKeyboard.this.actionPerformed(evt);
            }
        });
        add(b0);
        bInverse.setText("+/-");
        bInverse.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcKeyboard.this.actionPerformed(evt);
            }
        });
        add(bInverse);
        bPoint.setText(".");
        bPoint.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CalcKeyboard.this.actionPerformed(evt);
            }
        });
        add(bPoint);
    }

    private void actionPerformed(java.awt.event.ActionEvent evt) {
        this.fireEvent(evt);
    }

    private javax.swing.JButton b0;

    private javax.swing.JButton b1;

    private javax.swing.JButton b2;

    private javax.swing.JButton b3;

    private javax.swing.JButton b4;

    private javax.swing.JButton b5;

    private javax.swing.JButton b6;

    private javax.swing.JButton b7;

    private javax.swing.JButton b8;

    private javax.swing.JButton b9;

    private javax.swing.JButton bInverse;

    private javax.swing.JButton bPoint;

    /**
     * Utility field used by event firing mechanism.
     */
    private javax.swing.event.EventListenerList listenerList = null;

    /**
     * Registers ActionListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addActionListener(java.awt.event.ActionListener listener) {
        if (listenerList == null) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add(java.awt.event.ActionListener.class, listener);
    }

    /**
     * Removes ActionListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeActionListener(java.awt.event.ActionListener listener) {
        listenerList.remove(java.awt.event.ActionListener.class, listener);
    }

    /**
     * Notifies all registered listeners about the event.
     * 
     * @param event The event to be fired
     */
    private void fireEvent(java.awt.event.ActionEvent event) {
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == java.awt.event.ActionListener.class) {
                ((java.awt.event.ActionListener) listeners[i + 1]).actionPerformed(event);
            }
        }
    }
}
