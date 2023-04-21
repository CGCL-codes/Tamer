package com.cafe.serve.view.cashbox;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * TODO FIXME This classs should be generated with velocity.
 * 
 * @version 1.0
 * @author Asterios Raptis
 */
public class DrinkImageButtonPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JButton productButton1 = null;

    private JButton productButton2 = null;

    private JButton productButton3 = null;

    private JButton productButton4 = null;

    private JButton productButton5 = null;

    private JButton productButton6 = null;

    private JButton productButton7 = null;

    private JButton productButton8 = null;

    private JButton productButton9 = null;

    private JButton productButton10 = null;

    private JButton productButton11 = null;

    private JButton productButton12 = null;

    private JButton productButton13 = null;

    private JButton productButton14 = null;

    private JButton productButton15 = null;

    private JButton productButton16 = null;

    private JButton productButton17 = null;

    private JButton productButton18 = null;

    private JButton productButton19 = null;

    private JButton productButton20 = null;

    private JButton productButton21 = null;

    private JButton productButton22 = null;

    private JButton productButton23 = null;

    private JButton productButton24 = null;

    private JButton productButton25 = null;

    private JButton productButton26 = null;

    private JButton productButton27 = null;

    private JButton productButton28 = null;

    private JButton productButton29 = null;

    private JButton productButton30 = null;

    private JButton productButton31 = null;

    private JButton productButton32 = null;

    Insets inset = new Insets(10, 10, 10, 10);

    /**
     * This is the default constructor
     */
    public DrinkImageButtonPanel() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        GridLayout gridLayout = new GridLayout(8, 4, 10, 20);
        gridLayout.setColumns(4);
        gridLayout.setRows(8);
        this.setLayout(gridLayout);
        this.setBounds(new Rectangle(0, 0, 724, 628));
        this.add(getProductButton1(), null);
        this.add(getProductButton2(), null);
        this.add(getProductButton3(), null);
        this.add(getProductButton4(), null);
        this.add(getProductButton5(), null);
        this.add(getProductButton6(), null);
        this.add(getProductButton7(), null);
        this.add(getProductButton8(), null);
        this.add(getProductButton9(), null);
        this.add(getProductButton10(), null);
        this.add(getProductButton11(), null);
        this.add(getProductButton12(), null);
        this.add(getProductButton13(), null);
        this.add(getProductButton14(), null);
        this.add(getProductButton15(), null);
        this.add(getProductButton16(), null);
        this.add(getProductButton17(), null);
        this.add(getProductButton18(), null);
        this.add(getProductButton19(), null);
        this.add(getProductButton20(), null);
        this.add(getProductButton21(), null);
        this.add(getProductButton22(), null);
        this.add(getProductButton23(), null);
        this.add(getProductButton24(), null);
        this.add(getProductButton25(), null);
        this.add(getProductButton26(), null);
        this.add(getProductButton27(), null);
        this.add(getProductButton28(), null);
        this.add(getProductButton29(), null);
        this.add(getProductButton30(), null);
        this.add(getProductButton31(), null);
        this.add(getProductButton32(), null);
    }

    /**
     * This method initializes the field <code>productButton1</code> if it is
     * null. And then returns the field <code>productButton1</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton1</code>.
     */
    public JButton getProductButton1() {
        if (productButton1 == null) {
            productButton1 = new JButton();
            productButton1.setText("Pepsi Cola");
            productButton1.setToolTipText("Pepsi Cola");
            productButton1.setBorderPainted(true);
            ImageIcon icon = new ImageIcon("icons/pepsibuttonsmall.gif");
            productButton1.setIcon(icon);
        }
        return productButton1;
    }

    /**
     * This method initializes the field <code>productButton2</code> if it is
     * null. And then returns the field <code>productButton2</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton2</code>.
     */
    public JButton getProductButton2() {
        if (productButton2 == null) {
            productButton2 = new JButton();
            productButton2.setText("Fanta");
            productButton2.setToolTipText("Fanta");
            productButton2.setMargin(inset);
        }
        return productButton2;
    }

    /**
     * This method initializes the field <code>productButton3</code> if it is
     * null. And then returns the field <code>productButton3</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton3</code>.
     */
    public JButton getProductButton3() {
        if (productButton3 == null) {
            productButton3 = new JButton();
            productButton3.setText("Cafe");
            productButton3.setToolTipText("Cafe");
        }
        return productButton3;
    }

    /**
     * This method initializes the field <code>productButton4</code> if it is
     * null. And then returns the field <code>productButton4</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton4</code>.
     */
    public JButton getProductButton4() {
        if (productButton4 == null) {
            productButton4 = new JButton();
            productButton4.setText("Capuccino");
            productButton4.setToolTipText("Capuccino");
        }
        return productButton4;
    }

    /**
     * This method initializes the field <code>productButton5</code> if it is
     * null. And then returns the field <code>productButton5</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton5</code>.
     */
    public JButton getProductButton5() {
        if (productButton5 == null) {
            productButton5 = new JButton();
            productButton5.setText("Espresso");
            productButton5.setToolTipText("Espresso");
        }
        return productButton5;
    }

    /**
     * This method initializes the field <code>productButton6</code> if it is
     * null. And then returns the field <code>productButton6</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton6</code>.
     */
    public JButton getProductButton6() {
        if (productButton6 == null) {
            productButton6 = new JButton();
            productButton6.setText("Kakao");
            productButton6.setToolTipText("Kakao");
        }
        return productButton6;
    }

    /**
     * This method initializes the field <code>productButton7</code> if it is
     * null. And then returns the field <code>productButton7</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton7</code>.
     */
    public JButton getProductButton7() {
        if (productButton7 == null) {
            productButton7 = new JButton();
            productButton7.setText("Sprite");
            productButton7.setToolTipText("Sprite");
        }
        return productButton7;
    }

    /**
     * This method initializes the field <code>productButton8</code> if it is
     * null. And then returns the field <code>productButton8</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton8</code>.
     */
    public JButton getProductButton8() {
        if (productButton8 == null) {
            productButton8 = new JButton();
            productButton8.setText("Latte Machatto");
            productButton8.setToolTipText("Latte Machatto");
        }
        return productButton8;
    }

    /**
     * This method initializes the field <code>productButton9</code> if it is
     * null. And then returns the field <code>productButton9</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton9</code>.
     */
    public JButton getProductButton9() {
        if (productButton9 == null) {
            productButton9 = new JButton();
            productButton9.setText("Red Bull");
            productButton9.setToolTipText("Red Bull");
        }
        return productButton9;
    }

    /**
     * This method initializes the field <code>productButton10</code> if it is
     * null. And then returns the field <code>productButton10</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton10</code>.
     */
    public JButton getProductButton10() {
        if (productButton10 == null) {
            productButton10 = new JButton();
            productButton10.setText("Rox");
            productButton10.setToolTipText("Rox");
        }
        return productButton10;
    }

    /**
     * This method initializes the field <code>productButton11</code> if it is
     * null. And then returns the field <code>productButton11</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton11</code>.
     */
    public JButton getProductButton11() {
        if (productButton11 == null) {
            productButton11 = new JButton();
            productButton11.setText("Orangina");
            productButton11.setToolTipText("Orangina");
        }
        return productButton11;
    }

    /**
     * This method initializes the field <code>productButton12</code> if it is
     * null. And then returns the field <code>productButton12</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton12</code>.
     */
    public JButton getProductButton12() {
        if (productButton12 == null) {
            productButton12 = new JButton();
            productButton12.setText("Orangina rouge");
            productButton12.setToolTipText("Orangina rouge");
        }
        return productButton12;
    }

    /**
     * This method initializes the field <code>productButton13</code> if it is
     * null. And then returns the field <code>productButton13</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton13</code>.
     */
    public JButton getProductButton13() {
        if (productButton13 == null) {
            productButton13 = new JButton();
            productButton13.setText("Green Tea");
            productButton13.setToolTipText("Green Tea");
        }
        return productButton13;
    }

    /**
     * This method initializes the field <code>productButton14</code> if it is
     * null. And then returns the field <code>productButton14</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton14</code>.
     */
    public JButton getProductButton14() {
        if (productButton14 == null) {
            productButton14 = new JButton();
            productButton14.setText("Evian");
            productButton14.setToolTipText("Evian");
        }
        return productButton14;
    }

    /**
     * This method initializes the field <code>productButton15</code> if it is
     * null. And then returns the field <code>productButton15</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton15</code>.
     */
    public JButton getProductButton15() {
        if (productButton15 == null) {
            productButton15 = new JButton();
            productButton15.setText("Bitter Lemon");
            productButton15.setToolTipText("Bitter Lemon");
        }
        return productButton15;
    }

    /**
     * This method initializes the field <code>productButton16</code> if it is
     * null. And then returns the field <code>productButton16</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton16</code>.
     */
    public JButton getProductButton16() {
        if (productButton16 == null) {
            productButton16 = new JButton();
            productButton16.setText("Becks Gold");
            productButton16.setToolTipText("Becks Gold");
        }
        return productButton16;
    }

    /**
     * This method initializes the field <code>productButton17</code> if it is
     * null. And then returns the field <code>productButton17</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton17</code>.
     */
    public JButton getProductButton17() {
        if (productButton17 == null) {
            productButton17 = new JButton();
            productButton17.setText("Ginger Ale");
            productButton17.setToolTipText("Ginger Ale");
        }
        return productButton17;
    }

    /**
     * This method initializes the field <code>productButton18</code> if it is
     * null. And then returns the field <code>productButton18</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton18</code>.
     */
    public JButton getProductButton18() {
        if (productButton18 == null) {
            productButton18 = new JButton();
            productButton18.setText("Frappe");
            productButton18.setToolTipText("Frappe");
        }
        return productButton18;
    }

    /**
     * This method initializes the field <code>productButton19</code> if it is
     * null. And then returns the field <code>productButton19</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton19</code>.
     */
    public JButton getProductButton19() {
        if (productButton19 == null) {
            productButton19 = new JButton();
            productButton19.setText("Heineken");
            productButton19.setToolTipText("Heineken");
        }
        return productButton19;
    }

    /**
     * This method initializes the field <code>productButton20</code> if it is
     * null. And then returns the field <code>productButton20</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton20</code>.
     */
    public JButton getProductButton20() {
        if (productButton20 == null) {
            productButton20 = new JButton();
            productButton20.setText("Miller");
            productButton20.setToolTipText("Miller");
        }
        return productButton20;
    }

    /**
     * This method initializes the field <code>productButton21</code> if it is
     * null. And then returns the field <code>productButton21</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton21</code>.
     */
    public JButton getProductButton21() {
        if (productButton21 == null) {
            productButton21 = new JButton();
            productButton21.setText("Desperados");
            productButton21.setToolTipText("Desperados");
        }
        return productButton21;
    }

    /**
     * This method initializes the field <code>productButton22</code> if it is
     * null. And then returns the field <code>productButton22</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton22</code>.
     */
    public JButton getProductButton22() {
        if (productButton22 == null) {
            productButton22 = new JButton();
            productButton22.setText("Gin Tonic");
            productButton22.setToolTipText("Gin Tonic");
        }
        return productButton22;
    }

    /**
     * This method initializes the field <code>productButton23</code> if it is
     * null. And then returns the field <code>productButton23</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton23</code>.
     */
    public JButton getProductButton23() {
        if (productButton23 == null) {
            productButton23 = new JButton();
            productButton23.setText("Vodka Lemon");
            productButton23.setToolTipText("Vodka Lemon");
        }
        return productButton23;
    }

    /**
     * This method initializes the field <code>productButton24</code> if it is
     * null. And then returns the field <code>productButton24</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton24</code>.
     */
    public JButton getProductButton24() {
        if (productButton24 == null) {
            productButton24 = new JButton();
            productButton24.setText("Bacardi Cola");
            productButton24.setToolTipText("Bacardi Cola");
        }
        return productButton24;
    }

    /**
     * This method initializes the field <code>productButton25</code> if it is
     * null. And then returns the field <code>productButton25</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton25</code>.
     */
    public JButton getProductButton25() {
        if (productButton25 == null) {
            productButton25 = new JButton();
            productButton25.setText("Jack Daniel's");
            productButton25.setToolTipText("Jack Daniel's");
        }
        return productButton25;
    }

    /**
     * This method initializes the field <code>productButton26</code> if it is
     * null. And then returns the field <code>productButton26</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton26</code>.
     */
    public JButton getProductButton26() {
        if (productButton26 == null) {
            productButton26 = new JButton();
            productButton26.setText("Ballantines");
            productButton26.setToolTipText("Ballantines");
        }
        return productButton26;
    }

    /**
     * This method initializes the field <code>productButton27</code> if it is
     * null. And then returns the field <code>productButton27</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton27</code>.
     */
    public JButton getProductButton27() {
        if (productButton27 == null) {
            productButton27 = new JButton();
            productButton27.setText("Jim Beam");
            productButton27.setToolTipText("Jim Beam");
        }
        return productButton27;
    }

    /**
     * This method initializes the field <code>productButton28</code> if it is
     * null. And then returns the field <code>productButton28</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton28</code>.
     */
    public JButton getProductButton28() {
        if (productButton28 == null) {
            productButton28 = new JButton();
            productButton28.setText("Metaxa *****");
            productButton28.setToolTipText("Metaxa *****");
        }
        return productButton28;
    }

    /**
     * This method initializes the field <code>productButton29</code> if it is
     * null. And then returns the field <code>productButton29</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton29</code>.
     */
    public JButton getProductButton29() {
        if (productButton29 == null) {
            productButton29 = new JButton();
            productButton29.setText("Gordon's Gin");
            productButton29.setToolTipText("Gordon's Gin");
        }
        return productButton29;
    }

    /**
     * This method initializes the field <code>productButton30</code> if it is
     * null. And then returns the field <code>productButton30</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton30</code>.
     */
    public JButton getProductButton30() {
        if (productButton30 == null) {
            productButton30 = new JButton();
            productButton30.setText("Campari");
            productButton30.setToolTipText("Campari");
        }
        return productButton30;
    }

    /**
     * This method initializes the field <code>productButton31</code> if it is
     * null. And then returns the field <code>productButton31</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton31</code>.
     */
    public JButton getProductButton31() {
        if (productButton31 == null) {
            productButton31 = new JButton();
            productButton31.setText("Ouzo");
            productButton31.setToolTipText("Ouzo");
        }
        return productButton31;
    }

    /**
     * This method initializes the field <code>productButton32</code> if it is
     * null. And then returns the field <code>productButton32</code>.
     * 
     * @return javax.swing.JButton The field <code>productButton32</code>.
     */
    public JButton getProductButton32() {
        if (productButton32 == null) {
            productButton32 = new JButton();
            productButton32.setText("Caipirinha");
            productButton32.setToolTipText("Caipirinha");
        }
        return productButton32;
    }
}
