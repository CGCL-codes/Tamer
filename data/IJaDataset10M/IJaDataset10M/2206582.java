package org.diamondspin;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import javax.sound.sampled.*;

/**
 * DSKeyboardPanel is a JPanel with buttons in its layout (as a physical keyboard). It can be included
 * in DSFrame to replace the lack of keyboard on DiamondTouch.
 *
 * <table border=1 width = "90%"><tr><td>
 * Copyright 2002 Mitsubishi Electric Research Laboratories.  All Rights Reserved.<br><br>
 *
 * Permission to use, copy, modify and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice and the following three paragraphs appear in all
 * copies.<br><br>
 *
 * To request Permission to incorporate this software into commercial products
 * contact MERL - Mitsubishi Electric Research Laboratories, 201 Broadway,
 * Cambridge, MA 02139.<br><br>
 *
 * IN NO EVENT SHALL MERL BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, SPECIAL,
 * INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING OUT OF
 * THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF MERL HAS BEEN ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGES.<br><br>
 *
 * MERL SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS,
 * AND MERL HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 * ENHANCEMENTS, OR MODIFICATIONS.<br>
 * </td></tr></table><br>
 *
 * Creation date : april 19th 2002
 * @author        : Frederic vernier, (vernier@merl.com)  and Meredith Ringel (merrie@stanford.edu) under Chia Shen supervision (shen@merl.com).
 * @version       : 1.0
**/
public class DSKeyboardPanel extends JPanel {

    private int DebugLevel = 0;

    public void setDebugLevel(int DebugLevel_Arg) {
        DebugLevel = DebugLevel_Arg;
    }

    private void debug(int Level, String Message) {
        if (Level <= DebugLevel) {
            if (Message.equals("")) {
                try {
                    throw (new Exception());
                } catch (Exception e) {
                    StringWriter StringWriter = new StringWriter();
                    PrintWriter PrintWriter = new PrintWriter(StringWriter);
                    e.printStackTrace(PrintWriter);
                    StringBuffer Error = StringWriter.getBuffer();
                    System.out.println("ERROR = " + Error.toString());
                }
            }
            if (Level == 1) System.err.println("-------------------- " + Message + " --------------------"); else System.err.println("DEBUG MESSAGE: " + Message);
        }
    }

    ;

    private int RATIO_NUMERATEUR = 1;

    private int RATIO_DENOMINATEUR = 1;

    private JLabel JLabel1 = new JLabel();

    private JLabel JLabel2 = new JLabel();

    private JLabel JLabel3 = new JLabel();

    private JLabel JLabel4 = new JLabel();

    private JLabel JLabel5 = new JLabel();

    private JLabel JLabel6 = new JLabel();

    private JLabel JLabel7 = new JLabel();

    private JLabel JLabel8 = new JLabel();

    private JLabel JLabel9 = new JLabel();

    private JLabel JLabel10 = new JLabel();

    private JLabel JLabel11 = new JLabel();

    private JLabel JLabel12 = new JLabel();

    private JLabel JLabel13 = new JLabel();

    private JLabel JLabel14 = new JLabel();

    private JLabel JLabel15 = new JLabel();

    private JLabel JLabel16 = new JLabel();

    private JLabel JLabel17 = new JLabel();

    private JLabel JLabel18 = new JLabel();

    private JLabel JLabel19 = new JLabel();

    private JLabel JLabel20 = new JLabel();

    private JLabel JLabel21 = new JLabel();

    private JLabel JLabel22 = new JLabel();

    private JLabel JLabel23 = new JLabel();

    private JLabel JLabel24 = new JLabel();

    private JLabel JLabel25 = new JLabel();

    private JLabel JLabel26 = new JLabel();

    private JLabel JLabel27 = new JLabel();

    private JLabel JLabel28 = new JLabel();

    private JLabel JLabel29 = new JLabel();

    private JLabel JLabel30 = new JLabel();

    private JLabel JLabel31 = new JLabel();

    private JLabel JLabel32 = new JLabel();

    private JLabel JLabel33 = new JLabel();

    private JLabel JLabel34 = new JLabel();

    private JLabel JLabel35 = new JLabel();

    private JLabel JLabel36 = new JLabel();

    private JLabel JLabel37 = new JLabel();

    private JLabel JLabel38 = new JLabel();

    private JLabel JLabel39 = new JLabel();

    private JLabel JLabel40 = new JLabel();

    private JLabel JLabel41 = new JLabel();

    private JLabel JLabel42 = new JLabel();

    private JLabel JLabel43 = new JLabel();

    private JLabel JLabel44 = new JLabel();

    private JLabel JLabel45 = new JLabel();

    private JLabel JLabel46 = new JLabel();

    private JLabel JLabel47 = new JLabel();

    private JLabel JLabel48 = new JLabel();

    private JLabel JLabel49 = new JLabel();

    private JLabel JLabel50 = new JLabel();

    private Color KeyColor = Color.lightGray;

    private Color TextColor = Color.black;

    /**
   * Constructor
  **/
    public DSKeyboardPanel() {
        initComponents();
        prepareClick();
    }

    public DSKeyboardPanel(int num, int denom) {
        RATIO_NUMERATEUR = num;
        RATIO_DENOMINATEUR = denom;
        initComponents();
        prepareClick();
    }

    private Vector Listeners = new Vector();

    /**
   * register a key listener who want to listen this virtual keyboard key events
   */
    public void addKeyListener(KeyListener KeyListener_Arg) {
        Listeners.add(KeyListener_Arg);
    }

    /**
   * unregister a key listener who want to listen this virtual keyboard key events
   */
    public void removeKeyListener(KeyListener KeyListener_Arg) {
        Listeners.remove(KeyListener_Arg);
    }

    private int Modifiers = 0;

    /**
   * transform a "shifted" char into its normal char
   * @param KeyChar_Arg the capitalized character
   * @return the non capitalized character
  **/
    public char capsToNorm(char KeyChar_Arg) {
        debug(1, "capsToNorm DSKeyboardPanel= " + KeyChar_Arg);
        char KeyChar = 0;
        switch(KeyChar_Arg) {
            case '!':
                KeyChar = '1';
                break;
            case '@':
                KeyChar = '2';
                break;
            case '#':
                KeyChar = '3';
                break;
            case '$':
                KeyChar = '4';
                break;
            case '%':
                KeyChar = '5';
                break;
            case '^':
                KeyChar = '6';
                break;
            case '&':
                KeyChar = '7';
                break;
            case '*':
                KeyChar = '8';
                break;
            case '(':
                KeyChar = '9';
                break;
            case ')':
                KeyChar = '0';
                break;
            case '_':
                KeyChar = '-';
                break;
            case '+':
                KeyChar = '=';
                break;
            case 'Q':
                KeyChar = 'q';
                break;
            case 'W':
                KeyChar = 'w';
                break;
            case 'E':
                KeyChar = 'e';
                break;
            case 'R':
                KeyChar = 'r';
                break;
            case 'T':
                KeyChar = 't';
                break;
            case 'Y':
                KeyChar = 'y';
                break;
            case 'U':
                KeyChar = 'u';
                break;
            case 'I':
                KeyChar = 'i';
                break;
            case 'O':
                KeyChar = 'o';
                break;
            case 'P':
                KeyChar = 'p';
                break;
            case '{':
                KeyChar = '[';
                break;
            case '}':
                KeyChar = ']';
                break;
            case '|':
                KeyChar = '\\';
                break;
            case 'A':
                KeyChar = 'a';
                break;
            case 'S':
                KeyChar = 's';
                break;
            case 'D':
                KeyChar = 'd';
                break;
            case 'F':
                KeyChar = 'f';
                break;
            case 'G':
                KeyChar = 'g';
                break;
            case 'H':
                KeyChar = 'h';
                break;
            case 'J':
                KeyChar = 'j';
                break;
            case 'K':
                KeyChar = 'k';
                break;
            case 'L':
                KeyChar = 'l';
                break;
            case ':':
                KeyChar = ';';
                break;
            case '"':
                KeyChar = '\'';
                break;
            case 'Z':
                KeyChar = 'z';
                break;
            case 'X':
                KeyChar = 'x';
                break;
            case 'C':
                KeyChar = 'c';
                break;
            case 'V':
                KeyChar = 'v';
                break;
            case 'B':
                KeyChar = 'b';
                break;
            case 'N':
                KeyChar = 'n';
                break;
            case 'M':
                KeyChar = 'm';
                break;
            case '<':
                KeyChar = ',';
                break;
            case '>':
                KeyChar = '.';
                break;
            case '?':
                KeyChar = '/';
                break;
        }
        if (KeyChar != 0) return KeyChar; else return KeyChar_Arg;
    }

    /**
   * transform a normal char into its "shifted" char
   * @param KeyChar_Arg the non capitalized character
   * @return the capitalized character
  **/
    public char normToCaps(char KeyChar_Arg) {
        debug(1, "normToCaps DSKeyboardPanel= " + KeyChar_Arg);
        char KeyChar = 0;
        switch(KeyChar_Arg) {
            case '1':
                KeyChar = '!';
                break;
            case '2':
                KeyChar = '@';
                break;
            case '3':
                KeyChar = '#';
                break;
            case '4':
                KeyChar = '$';
                break;
            case '5':
                KeyChar = '%';
                break;
            case '6':
                KeyChar = '^';
                break;
            case '7':
                KeyChar = '&';
                break;
            case '8':
                KeyChar = '*';
                break;
            case '9':
                KeyChar = '(';
                break;
            case '0':
                KeyChar = ')';
                break;
            case '-':
                KeyChar = '_';
                break;
            case '=':
                KeyChar = '+';
                break;
            case 'q':
                KeyChar = 'Q';
                break;
            case 'w':
                KeyChar = 'W';
                break;
            case 'e':
                KeyChar = 'E';
                break;
            case 'r':
                KeyChar = 'R';
                break;
            case 't':
                KeyChar = 'T';
                break;
            case 'y':
                KeyChar = 'Y';
                break;
            case 'u':
                KeyChar = 'U';
                break;
            case 'i':
                KeyChar = 'I';
                break;
            case 'o':
                KeyChar = 'O';
                break;
            case 'p':
                KeyChar = 'P';
                break;
            case '[':
                KeyChar = '{';
                break;
            case ']':
                KeyChar = '}';
                break;
            case '|':
                KeyChar = '\\';
                break;
            case 'a':
                KeyChar = 'A';
                break;
            case 's':
                KeyChar = 'S';
                break;
            case 'd':
                KeyChar = 'D';
                break;
            case 'f':
                KeyChar = 'F';
                break;
            case 'g':
                KeyChar = 'G';
                break;
            case 'h':
                KeyChar = 'H';
                break;
            case 'j':
                KeyChar = 'J';
                break;
            case 'k':
                KeyChar = 'K';
                break;
            case 'l':
                KeyChar = 'L';
                break;
            case ':':
                KeyChar = ';';
                break;
            case '"':
                KeyChar = '\'';
                break;
            case 'z':
                KeyChar = 'Z';
                break;
            case 'x':
                KeyChar = 'X';
                break;
            case 'c':
                KeyChar = 'C';
                break;
            case 'v':
                KeyChar = 'V';
                break;
            case 'b':
                KeyChar = 'B';
                break;
            case 'n':
                KeyChar = 'N';
                break;
            case 'm':
                KeyChar = 'M';
                break;
            case ',':
                KeyChar = '<';
                break;
            case '.':
                KeyChar = '>';
                break;
            case '/':
                KeyChar = '?';
                break;
        }
        if (KeyChar != 0) return KeyChar; else return KeyChar_Arg;
    }

    /**
   * send a key pressed event to the listeners
   * @param KeyCode_Arg the key code
   * @param KeyChar_Arg the key character
  **/
    public void fireKeyPressed(int KeyCode_Arg, char KeyChar_Arg) {
        debug(1, "fireKeyPressed DSKeyboardPanel char= " + KeyChar_Arg + "   KeyCode_Arg=" + KeyCode_Arg);
        for (Enumeration Enumeration1 = Listeners.elements(); Enumeration1.hasMoreElements(); ) {
            KeyListener KeyListener1 = ((KeyListener) Enumeration1.nextElement());
            char KeyChar = KeyChar_Arg;
            if ((Modifiers & KeyEvent.VK_SHIFT) == 0) KeyChar = capsToNorm(KeyChar_Arg);
            KeyListener1.keyPressed(new KeyEvent(this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), Modifiers, KeyCode_Arg, KeyChar));
            KeyListener1.keyTyped(new KeyEvent(this, KeyEvent.KEY_TYPED, System.currentTimeMillis(), Modifiers, KeyEvent.VK_UNDEFINED, KeyChar));
            playClick();
        }
    }

    /**
   * send a key released event to the listeners
   * @param KeyCode_Arg the key code
   * @param KeyChar_Arg the key character
  **/
    public void fireKeyReleased(int KeyCode_Arg, char KeyChar_Arg) {
        debug(1, "fireKeyReleased DSKeyboardPanel= " + KeyChar_Arg);
        for (Enumeration Enumeration1 = Listeners.elements(); Enumeration1.hasMoreElements(); ) {
            KeyListener KeyListener1 = ((KeyListener) Enumeration1.nextElement());
            KeyListener1.keyReleased(new KeyEvent(this, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), Modifiers, KeyCode_Arg, KeyChar_Arg));
        }
    }

    /**
   * udpade the keyboard keys according to the shift (caps lock)
  **/
    public void updateKeyboard() {
        debug(1, "updateKeyboard DSKeyboardPanel");
        Component[] ComponentList = getComponents();
        for (int i = 0; i < ComponentList.length; i++) {
            if (ComponentList[i] instanceof JLabel) {
                JLabel JLabelA = (JLabel) ComponentList[i];
                if (!JLabelA.getText().equals("Enter") && !JLabelA.getText().equals("Shift") && !JLabelA.getText().equals("BS") && !JLabelA.getText().equals("") && !JLabelA.getText().equals("Del") && !JLabelA.getText().equals("Space") && !JLabelA.getText().equals("End") && !JLabelA.getText().equals("Hom")) {
                    if ((Modifiers & KeyEvent.VK_SHIFT) != 0) JLabelA.setText("" + normToCaps(JLabelA.getText().charAt(0))); else JLabelA.setText("" + capsToNorm(JLabelA.getText().charAt(0)));
                }
            }
        }
    }

    /**
   * return the Preferred Size for this keyboard.
  **/
    public Dimension getPreferredSize() {
        return new Dimension(236 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 88 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR);
    }

    /**
   * return the Minimum Size for this keyboard.
  **/
    public Dimension getMinimumSize() {
        return new Dimension(236 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 88 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR);
    }

    /**
   * init the sub-components. Has been generated by codewarrior.
  **/
    public void initComponents() {
        debug(1, "initComponents DSKeyboardPanel");
        setSize(new Dimension(236 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 88 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        setVisible(true);
        setLayout(null);
        add(JLabel1);
        add(JLabel2);
        add(JLabel3);
        add(JLabel4);
        add(JLabel5);
        add(JLabel6);
        add(JLabel7);
        add(JLabel8);
        add(JLabel9);
        add(JLabel10);
        add(JLabel11);
        add(JLabel12);
        add(JLabel13);
        add(JLabel14);
        add(JLabel15);
        add(JLabel16);
        add(JLabel17);
        add(JLabel18);
        add(JLabel19);
        add(JLabel20);
        add(JLabel21);
        add(JLabel22);
        add(JLabel23);
        add(JLabel24);
        add(JLabel25);
        add(JLabel26);
        add(JLabel27);
        add(JLabel28);
        add(JLabel29);
        add(JLabel30);
        add(JLabel31);
        add(JLabel32);
        add(JLabel33);
        add(JLabel34);
        add(JLabel35);
        add(JLabel36);
        add(JLabel37);
        add(JLabel38);
        add(JLabel39);
        add(JLabel40);
        add(JLabel41);
        add(JLabel42);
        add(JLabel43);
        add(JLabel44);
        add(JLabel45);
        add(JLabel46);
        add(JLabel47);
        add(JLabel48);
        add(JLabel49);
        add(JLabel50);
        JLabel1.setOpaque(true);
        JLabel1.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel1.setLocation(new Point(0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel1.setVisible(true);
        JLabel1.setText("Q");
        JLabel1.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_Q, 'Q');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_Q, 'Q');
            }
        });
        JLabel1.setForeground(TextColor);
        JLabel1.setBackground(KeyColor);
        JLabel1.setHorizontalAlignment(JLabel.CENTER);
        JLabel2.setOpaque(true);
        JLabel2.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel2.setLocation(new Point(18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel2.setVisible(true);
        JLabel2.setText("W");
        JLabel2.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_W, 'W');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_W, 'W');
            }
        });
        JLabel2.setForeground(TextColor);
        JLabel2.setBackground(KeyColor);
        JLabel2.setHorizontalAlignment(JLabel.CENTER);
        JLabel3.setOpaque(true);
        JLabel3.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel3.setLocation(new Point(36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel3.setVisible(true);
        JLabel3.setText("E");
        JLabel3.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_E, 'E');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_E, 'E');
            }
        });
        JLabel3.setForeground(TextColor);
        JLabel3.setBackground(KeyColor);
        JLabel3.setHorizontalAlignment(JLabel.CENTER);
        JLabel4.setOpaque(true);
        JLabel4.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel4.setLocation(new Point(54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel4.setVisible(true);
        JLabel4.setText("R");
        JLabel4.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_R, 'R');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_R, 'R');
            }
        });
        JLabel4.setForeground(TextColor);
        JLabel4.setBackground(KeyColor);
        JLabel4.setHorizontalAlignment(JLabel.CENTER);
        JLabel5.setOpaque(true);
        JLabel5.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel5.setLocation(new Point(126 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel5.setVisible(true);
        JLabel5.setText("I");
        JLabel5.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_I, 'I');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_I, 'I');
            }
        });
        JLabel5.setForeground(TextColor);
        JLabel5.setBackground(KeyColor);
        JLabel5.setHorizontalAlignment(JLabel.CENTER);
        JLabel6.setOpaque(true);
        JLabel6.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel6.setLocation(new Point(108 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel6.setVisible(true);
        JLabel6.setText("U");
        JLabel6.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_U, 'U');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_U, 'U');
            }
        });
        JLabel6.setForeground(TextColor);
        JLabel6.setBackground(KeyColor);
        JLabel6.setHorizontalAlignment(JLabel.CENTER);
        JLabel7.setOpaque(true);
        JLabel7.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel7.setLocation(new Point(90 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel7.setVisible(true);
        JLabel7.setText("Y");
        JLabel7.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_Y, 'Y');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_Y, 'Y');
            }
        });
        JLabel7.setForeground(TextColor);
        JLabel7.setBackground(KeyColor);
        JLabel7.setHorizontalAlignment(JLabel.CENTER);
        JLabel8.setOpaque(true);
        JLabel8.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel8.setLocation(new Point(72 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel8.setVisible(true);
        JLabel8.setText("T");
        JLabel8.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_T, 'T');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_T, 'T');
            }
        });
        JLabel8.setForeground(TextColor);
        JLabel8.setBackground(KeyColor);
        JLabel8.setHorizontalAlignment(JLabel.CENTER);
        JLabel9.setOpaque(true);
        JLabel9.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel9.setLocation(new Point(144 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel9.setVisible(true);
        JLabel9.setText("O");
        JLabel9.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_O, 'O');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_O, 'O');
            }
        });
        JLabel9.setForeground(TextColor);
        JLabel9.setBackground(KeyColor);
        JLabel9.setHorizontalAlignment(JLabel.CENTER);
        JLabel10.setOpaque(true);
        JLabel10.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel10.setLocation(new Point(162 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel10.setVisible(true);
        JLabel10.setText("P");
        JLabel10.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_P, 'P');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_P, 'P');
            }
        });
        JLabel10.setForeground(TextColor);
        JLabel10.setBackground(KeyColor);
        JLabel10.setHorizontalAlignment(JLabel.CENTER);
        JLabel11.setOpaque(true);
        JLabel11.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel11.setLocation(new Point(6 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel11.setVisible(true);
        JLabel11.setText("A");
        JLabel11.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_A, 'A');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_A, 'A');
            }
        });
        JLabel11.setForeground(TextColor);
        JLabel11.setBackground(KeyColor);
        JLabel11.setHorizontalAlignment(JLabel.CENTER);
        JLabel12.setOpaque(true);
        JLabel12.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel12.setLocation(new Point(24 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel12.setVisible(true);
        JLabel12.setText("S");
        JLabel12.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                MouseEvent_Arg.consume();
                fireKeyPressed(KeyEvent.VK_S, 'S');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_S, 'S');
            }
        });
        JLabel12.setForeground(TextColor);
        JLabel12.setBackground(KeyColor);
        JLabel12.setHorizontalAlignment(JLabel.CENTER);
        JLabel13.setOpaque(true);
        JLabel13.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel13.setLocation(new Point(42 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel13.setVisible(true);
        JLabel13.setText("D");
        JLabel13.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_D, 'D');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_D, 'D');
            }
        });
        JLabel13.setForeground(TextColor);
        JLabel13.setBackground(KeyColor);
        JLabel13.setHorizontalAlignment(JLabel.CENTER);
        JLabel14.setOpaque(true);
        JLabel14.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel14.setLocation(new Point(60 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel14.setVisible(true);
        JLabel14.setText("F");
        JLabel14.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_F, 'F');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_F, 'F');
            }
        });
        JLabel14.setForeground(TextColor);
        JLabel14.setBackground(KeyColor);
        JLabel14.setHorizontalAlignment(JLabel.CENTER);
        JLabel15.setOpaque(true);
        JLabel15.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel15.setLocation(new Point(78 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel15.setVisible(true);
        JLabel15.setText("G");
        JLabel15.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_G, 'G');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_G, 'G');
            }
        });
        JLabel15.setForeground(TextColor);
        JLabel15.setBackground(KeyColor);
        JLabel15.setHorizontalAlignment(JLabel.CENTER);
        JLabel16.setOpaque(true);
        JLabel16.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel16.setLocation(new Point(96 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel16.setVisible(true);
        JLabel16.setText("H");
        JLabel16.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_H, 'H');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_H, 'H');
            }
        });
        JLabel16.setForeground(TextColor);
        JLabel16.setBackground(KeyColor);
        JLabel16.setHorizontalAlignment(JLabel.CENTER);
        JLabel17.setOpaque(true);
        JLabel17.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel17.setLocation(new Point(114 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel17.setVisible(true);
        JLabel17.setText("J");
        JLabel17.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_J, 'J');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_J, 'J');
            }
        });
        JLabel17.setForeground(TextColor);
        JLabel17.setBackground(KeyColor);
        JLabel17.setHorizontalAlignment(JLabel.CENTER);
        JLabel18.setOpaque(true);
        JLabel18.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel18.setLocation(new Point(132 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel18.setVisible(true);
        JLabel18.setText("K");
        JLabel18.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_K, 'K');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_K, 'K');
            }
        });
        JLabel18.setForeground(TextColor);
        JLabel18.setBackground(KeyColor);
        JLabel18.setHorizontalAlignment(JLabel.CENTER);
        JLabel19.setOpaque(true);
        JLabel19.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel19.setLocation(new Point(150 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel19.setVisible(true);
        JLabel19.setText("L");
        JLabel19.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_L, 'L');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_L, 'L');
            }
        });
        JLabel19.setForeground(TextColor);
        JLabel19.setBackground(KeyColor);
        JLabel19.setHorizontalAlignment(JLabel.CENTER);
        JLabel20.setOpaque(true);
        JLabel20.setSize(new Dimension(32 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel20.setLocation(new Point(204 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel20.setVisible(true);
        JLabel20.setText("Enter");
        JLabel20.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_ENTER, '\n');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_ENTER, '\n');
            }
        });
        JLabel20.setForeground(TextColor);
        JLabel20.setBackground(KeyColor);
        JLabel20.setHorizontalAlignment(JLabel.CENTER);
        JLabel21.setOpaque(true);
        JLabel21.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel21.setLocation(new Point(12 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel21.setVisible(true);
        JLabel21.setText("Z");
        JLabel21.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_Z, 'Z');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_Z, 'Z');
            }
        });
        JLabel21.setForeground(TextColor);
        JLabel21.setBackground(KeyColor);
        JLabel21.setHorizontalAlignment(JLabel.CENTER);
        JLabel22.setOpaque(true);
        JLabel22.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel22.setLocation(new Point(30 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel22.setVisible(true);
        JLabel22.setText("X");
        JLabel22.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_X, 'X');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_X, 'X');
            }
        });
        JLabel22.setForeground(TextColor);
        JLabel22.setBackground(KeyColor);
        JLabel22.setHorizontalAlignment(JLabel.CENTER);
        JLabel23.setOpaque(true);
        JLabel23.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel23.setLocation(new Point(48 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel23.setVisible(true);
        JLabel23.setText("C");
        JLabel23.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_C, 'C');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_C, 'C');
            }
        });
        JLabel23.setForeground(TextColor);
        JLabel23.setBackground(KeyColor);
        JLabel23.setHorizontalAlignment(JLabel.CENTER);
        JLabel24.setOpaque(true);
        JLabel24.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel24.setLocation(new Point(66 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel24.setVisible(true);
        JLabel24.setText("V");
        JLabel24.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_V, 'V');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_V, 'V');
            }
        });
        JLabel24.setForeground(TextColor);
        JLabel24.setBackground(KeyColor);
        JLabel24.setHorizontalAlignment(JLabel.CENTER);
        JLabel25.setOpaque(true);
        JLabel25.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel25.setLocation(new Point(84 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel25.setVisible(true);
        JLabel25.setText("B");
        JLabel25.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_B, 'B');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_B, 'B');
            }
        });
        JLabel25.setForeground(TextColor);
        JLabel25.setBackground(KeyColor);
        JLabel25.setHorizontalAlignment(JLabel.CENTER);
        JLabel26.setOpaque(true);
        JLabel26.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel26.setLocation(new Point(102 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel26.setVisible(true);
        JLabel26.setText("N");
        JLabel26.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_N, 'N');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_N, 'N');
            }
        });
        JLabel26.setForeground(TextColor);
        JLabel26.setBackground(KeyColor);
        JLabel26.setHorizontalAlignment(JLabel.CENTER);
        JLabel27.setOpaque(true);
        JLabel27.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel27.setLocation(new Point(120 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel27.setVisible(true);
        JLabel27.setText("M");
        JLabel27.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_M, 'M');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_M, 'M');
            }
        });
        JLabel27.setForeground(TextColor);
        JLabel27.setBackground(KeyColor);
        JLabel27.setHorizontalAlignment(JLabel.CENTER);
        JLabel28.setOpaque(true);
        JLabel28.setSize(new Dimension(38 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel28.setLocation(new Point(192 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel28.setVisible(true);
        JLabel28.setText("Shift");
        JLabel28.setForeground(TextColor);
        JLabel28.setBackground(KeyColor);
        JLabel28.setHorizontalAlignment(JLabel.CENTER);
        JLabel28.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                if ((Modifiers & KeyEvent.VK_SHIFT) != 0) {
                    Modifiers = Modifiers & ~KeyEvent.VK_SHIFT;
                } else Modifiers = Modifiers | KeyEvent.VK_SHIFT;
                updateKeyboard();
            }
        });
        JLabel29.setOpaque(true);
        JLabel29.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel29.setLocation(new Point(180 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel29.setVisible(true);
        JLabel29.setText("{");
        JLabel29.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_BRACELEFT, '{');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_BRACELEFT, '{');
            }
        });
        JLabel29.setForeground(TextColor);
        JLabel29.setBackground(KeyColor);
        JLabel29.setHorizontalAlignment(JLabel.CENTER);
        JLabel30.setOpaque(true);
        JLabel30.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel30.setLocation(new Point(198 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel30.setVisible(true);
        JLabel30.setText("}");
        JLabel30.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_BRACERIGHT, '}');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_BRACERIGHT, '}');
            }
        });
        JLabel30.setForeground(TextColor);
        JLabel30.setBackground(KeyColor);
        JLabel30.setHorizontalAlignment(JLabel.CENTER);
        JLabel31.setOpaque(true);
        JLabel31.setSize(new Dimension(20 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel31.setLocation(new Point(216 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel31.setVisible(true);
        JLabel31.setText("|");
        JLabel31.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '|');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '|');
            }
        });
        JLabel31.setForeground(TextColor);
        JLabel31.setBackground(KeyColor);
        JLabel31.setHorizontalAlignment(JLabel.CENTER);
        JLabel32.setOpaque(true);
        JLabel32.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel32.setLocation(new Point(168 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel32.setVisible(true);
        JLabel32.setText(":");
        JLabel32.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, ':');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, ':');
            }
        });
        JLabel32.setForeground(TextColor);
        JLabel32.setBackground(KeyColor);
        JLabel32.setHorizontalAlignment(JLabel.CENTER);
        JLabel33.setOpaque(true);
        JLabel33.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel33.setLocation(new Point(186 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel33.setVisible(true);
        JLabel33.setText("\"");
        JLabel33.setForeground(TextColor);
        JLabel33.setBackground(KeyColor);
        JLabel33.setHorizontalAlignment(JLabel.CENTER);
        JLabel34.setOpaque(true);
        JLabel34.setSize(new Dimension(20 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel34.setLocation(new Point(216 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel34.setVisible(true);
        JLabel34.setText("BS");
        JLabel34.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_BACK_SPACE, (char) KeyEvent.VK_BACK_SPACE);
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_BACK_SPACE, (char) KeyEvent.VK_BACK_SPACE);
            }
        });
        JLabel34.setForeground(TextColor);
        JLabel34.setBackground(KeyColor);
        JLabel34.setHorizontalAlignment(JLabel.CENTER);
        JLabel35.setOpaque(true);
        JLabel35.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel35.setLocation(new Point(198 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel35.setVisible(true);
        JLabel35.setText("+");
        JLabel35.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '+');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '+');
            }
        });
        JLabel35.setForeground(TextColor);
        JLabel35.setBackground(KeyColor);
        JLabel35.setHorizontalAlignment(JLabel.CENTER);
        JLabel36.setOpaque(true);
        JLabel36.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel36.setLocation(new Point(180 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel36.setVisible(true);
        JLabel36.setText("_");
        JLabel36.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '_');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '_');
            }
        });
        JLabel36.setForeground(TextColor);
        JLabel36.setBackground(KeyColor);
        JLabel36.setHorizontalAlignment(JLabel.CENTER);
        JLabel37.setOpaque(true);
        JLabel37.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel37.setLocation(new Point(162 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel37.setVisible(true);
        JLabel37.setText(")");
        JLabel37.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, ')');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, ')');
            }
        });
        JLabel37.setForeground(TextColor);
        JLabel37.setBackground(KeyColor);
        JLabel37.setHorizontalAlignment(JLabel.CENTER);
        JLabel38.setOpaque(true);
        JLabel38.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel38.setLocation(new Point(144 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel38.setVisible(true);
        JLabel38.setText("(");
        JLabel38.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '(');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '(');
            }
        });
        JLabel38.setForeground(TextColor);
        JLabel38.setBackground(KeyColor);
        JLabel38.setHorizontalAlignment(JLabel.CENTER);
        JLabel39.setOpaque(true);
        JLabel39.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel39.setLocation(new Point(126 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel39.setVisible(true);
        JLabel39.setText("*");
        JLabel39.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '*');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '*');
            }
        });
        JLabel39.setForeground(TextColor);
        JLabel39.setBackground(KeyColor);
        JLabel39.setHorizontalAlignment(JLabel.CENTER);
        JLabel40.setOpaque(true);
        JLabel40.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel40.setLocation(new Point(108 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel40.setVisible(true);
        JLabel40.setText("&");
        JLabel40.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '&');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '&');
            }
        });
        JLabel40.setForeground(TextColor);
        JLabel40.setBackground(KeyColor);
        JLabel40.setHorizontalAlignment(JLabel.CENTER);
        JLabel41.setOpaque(true);
        JLabel41.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel41.setLocation(new Point(90 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel41.setVisible(true);
        JLabel41.setText("^");
        JLabel41.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '^');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '^');
            }
        });
        JLabel41.setForeground(TextColor);
        JLabel41.setBackground(KeyColor);
        JLabel41.setHorizontalAlignment(JLabel.CENTER);
        JLabel42.setOpaque(true);
        JLabel42.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel42.setLocation(new Point(72 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel42.setVisible(true);
        JLabel42.setText("%");
        JLabel42.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '%');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '%');
            }
        });
        JLabel42.setForeground(TextColor);
        JLabel42.setBackground(KeyColor);
        JLabel42.setHorizontalAlignment(JLabel.CENTER);
        JLabel43.setOpaque(true);
        JLabel43.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel43.setLocation(new Point(54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel43.setVisible(true);
        JLabel43.setText("$");
        JLabel43.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '$');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '$');
            }
        });
        JLabel43.setForeground(TextColor);
        JLabel43.setBackground(KeyColor);
        JLabel43.setHorizontalAlignment(JLabel.CENTER);
        JLabel44.setOpaque(true);
        JLabel44.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel44.setLocation(new Point(36 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel44.setVisible(true);
        JLabel44.setText("#");
        JLabel44.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '#');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '#');
            }
        });
        JLabel44.setForeground(TextColor);
        JLabel44.setBackground(KeyColor);
        JLabel44.setHorizontalAlignment(JLabel.CENTER);
        JLabel45.setOpaque(true);
        JLabel45.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel45.setLocation(new Point(18 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel45.setVisible(true);
        JLabel45.setText("@");
        JLabel45.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '@');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '@');
            }
        });
        JLabel45.setForeground(TextColor);
        JLabel45.setBackground(KeyColor);
        JLabel45.setHorizontalAlignment(JLabel.CENTER);
        JLabel46.setOpaque(true);
        JLabel46.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel46.setLocation(new Point(0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 0 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel46.setVisible(true);
        JLabel46.setText("!");
        JLabel46.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '!');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '!');
            }
        });
        JLabel46.setForeground(TextColor);
        JLabel46.setBackground(KeyColor);
        JLabel46.setHorizontalAlignment(JLabel.CENTER);
        JLabel47.setOpaque(true);
        JLabel47.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel47.setLocation(new Point(138 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel47.setVisible(true);
        JLabel47.setText("<");
        JLabel47.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '<');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '<');
            }
        });
        JLabel47.setForeground(TextColor);
        JLabel47.setBackground(KeyColor);
        JLabel47.setHorizontalAlignment(JLabel.CENTER);
        JLabel48.setOpaque(true);
        JLabel48.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel48.setLocation(new Point(156 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel48.setVisible(true);
        JLabel48.setText(">");
        JLabel48.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '>');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '>');
            }
        });
        JLabel48.setForeground(TextColor);
        JLabel48.setBackground(KeyColor);
        JLabel48.setHorizontalAlignment(JLabel.CENTER);
        JLabel49.setOpaque(true);
        JLabel49.setSize(new Dimension(16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel49.setLocation(new Point(174 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 54 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel49.setVisible(true);
        JLabel49.setText("?");
        JLabel49.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, '?');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, '?');
            }
        });
        JLabel49.setForeground(TextColor);
        JLabel49.setBackground(KeyColor);
        JLabel49.setHorizontalAlignment(JLabel.CENTER);
        JLabel50.setOpaque(true);
        JLabel50.setSize(new Dimension(120 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 16 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel50.setLocation(new Point(58 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR, 72 * RATIO_NUMERATEUR / RATIO_DENOMINATEUR));
        JLabel50.setVisible(true);
        JLabel50.setText("Space");
        JLabel50.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent MouseEvent_Arg) {
                fireKeyPressed(KeyEvent.VK_UNDEFINED, ' ');
            }

            public void mouseReleased(MouseEvent MouseEvent_Arg) {
                fireKeyReleased(KeyEvent.VK_UNDEFINED, ' ');
            }
        });
        JLabel50.setForeground(TextColor);
        JLabel50.setBackground(KeyColor);
        JLabel50.setHorizontalAlignment(JLabel.CENTER);
        setBackground(java.awt.Color.darkGray);
        updateKeyboard();
    }

    protected int IDOwner = -1;

    public void setIDOwner(int ID_Arg) {
        IDOwner = ID_Arg;
    }

    public int getIDOwner() {
        return IDOwner;
    }

    private Clip click = null;

    public void prepareClick() {
        try {
            URL SoundURL = ClassLoader.getSystemResource("sounds/keyclick.wav");
            if (SoundURL == null) {
                System.err.println("Failed to open:" + "sounds/keyclick.wav");
                return;
            }
            AudioInputStream AudioInputStream1 = AudioSystem.getAudioInputStream(SoundURL);
            AudioFormat AudioFormat1 = AudioInputStream1.getFormat();
            DataLine.Info DataLineInfo1 = new DataLine.Info(Clip.class, AudioFormat1, 423);
            Mixer.Info[] MixerInfoList = AudioSystem.getMixerInfo();
            Mixer Mixer1 = AudioSystem.getMixer(MixerInfoList[0]);
            click = (Clip) Mixer1.getLine(DataLineInfo1);
            click.open(AudioInputStream1);
        } catch (LineUnavailableException le) {
            System.err.println("javax.sound.sampled.LineUnavailableException: Expected Error for no sound");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playClick() {
        ClipPlayer cp = new ClipPlayer(click);
        cp.start();
    }

    private class ClipPlayer extends Thread {

        Clip c;

        public ClipPlayer(Clip c_arg) {
            c = c_arg;
        }

        public void run() {
            if (c != null) {
                c.setFramePosition(0);
                c.start();
            }
        }
    }
}
