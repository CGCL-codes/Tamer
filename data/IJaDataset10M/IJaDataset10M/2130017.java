package Timer;

import Kino;
import java.awt.Panel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.TextField;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.Integer;
import java.lang.NumberFormatException;
import java.lang.Thread;
import java.lang.InterruptedException;
import java.util.Date;
import java.lang.Integer;
import java.awt.event.*;

public class FaktorPanel extends Panel {

    int secFaktor;

    boolean wie;

    Button set, gs;

    TextField stringFaktor;

    Calc calculiere;

    CinemaDate cD;

    public FaktorPanel(Calc calculiere, CinemaDate cD) {
        wie = true;
        setLayout(new BorderLayout(2, 2));
        this.calculiere = calculiere;
        this.cD = cD;
        set = new Button("Set/Reset");
        gs = new Button("Start/Stop");
        stringFaktor = new TextField(14);
        gs.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (wie) {
                    try {
                        int temp = (int) (Integer.parseInt(stringFaktor.getText()));
                        if (temp <= 3600) setFactor(temp); else {
                            stringFaktor.setText(" Error");
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ei) {
                            }
                            stringFaktor.setText("");
                            setFactor(1);
                        }
                    } catch (NumberFormatException exc) {
                        stringFaktor.setText(" Error");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ei) {
                        }
                        stringFaktor.setText("");
                        setFactor(1);
                    }
                    wie = false;
                } else {
                    wie = true;
                    setFactor(1);
                    stringFaktor.setText("");
                }
            }
        });
        set.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent eve) {
                boolean b1 = false;
                boolean b2 = false;
                boolean b3 = false;
                String test = stringFaktor.getText();
                if (test.length() == 8 || test.length() == 10) {
                    if (stringFaktor.getText().substring(2, 3).equals(stringFaktor.getText().substring(5, 6))) if (stringFaktor.getText().substring(2, 3).equals(".") || stringFaktor.getText().substring(2, 3).equals(":")) b1 = true;
                    try {
                        int i1 = (int) Integer.parseInt(stringFaktor.getText().substring(0, 2));
                        int i2 = (int) Integer.parseInt(stringFaktor.getText().substring(3, 5));
                        if (stringFaktor.getText().length() == 8) {
                            int i3 = (int) Integer.parseInt(stringFaktor.getText().substring(6, 8));
                            if (i1 < 24 && i1 >= 0 && i2 < 60 && i2 >= 0 && i3 < 60 && i3 >= 0) b3 = true;
                        } else {
                            int i3 = (int) Integer.parseInt(stringFaktor.getText().substring(6, 10));
                            if (i1 < 32 && i1 >= 0 && i2 < 13 && i2 >= 0 && i3 >= 1980) b3 = true;
                        }
                        b2 = true;
                    } catch (NumberFormatException ne) {
                    }
                }
                if (b1 && b2 && b3) {
                    if (stringFaktor.getText().length() == 8) {
                        int ho = (int) Integer.parseInt(stringFaktor.getText().substring(0, 2));
                        int mi = (int) Integer.parseInt(stringFaktor.getText().substring(3, 5));
                        int se = (int) Integer.parseInt(stringFaktor.getText().substring(6, 8));
                        stringFaktor.setText("");
                        setFactor(1);
                        setTime(ho, mi, se);
                    } else {
                        int da = (int) Integer.parseInt(stringFaktor.getText().substring(0, 2));
                        int mo = (int) Integer.parseInt(stringFaktor.getText().substring(3, 5));
                        int ye = (int) Integer.parseInt(stringFaktor.getText().substring(6, 10));
                        System.out.println("bin drin");
                        stringFaktor.setText("");
                        setFactor(1);
                        setDate(da, mo, ye);
                    }
                } else {
                    Date date = new Date();
                    int se = (int) Integer.parseInt(date.toString().substring(17, 19));
                    int mi = (int) Integer.parseInt(date.toString().substring(14, 16));
                    int ho = (int) Integer.parseInt(date.toString().substring(11, 13));
                    int ye = (int) Integer.parseInt(date.toString().substring(24, 28));
                    int da = (int) Integer.parseInt(date.toString().substring(8, 10));
                    int mo = monatInt(date.toString().substring(4, 7));
                    stringFaktor.setText("");
                    setFactor(1);
                    setTime(ho, mi, se);
                    setDate(da, mo, ye);
                }
            }
        });
        gs.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                if (Kino.curHelpLine != null) Kino.curHelpLine.setText("Nach Zeitfaktoreingabe Zeit schneller stellen oder wieder normal laufen lassen");
            }

            public void mouseExited(MouseEvent e) {
                if (Kino.curHelpLine != null) Kino.curHelpLine.setText("");
            }
        });
        set.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                if (Kino.curHelpLine != null) Kino.curHelpLine.setText("Dr�cken ohne Eingabe setzen auf Realzeit sonst auf Datum oder Zeit (je nach Eingabe)");
            }

            public void mouseExited(MouseEvent e) {
                if (Kino.curHelpLine != null) Kino.curHelpLine.setText("");
            }
        });
        stringFaktor.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                if (Kino.curHelpLine != null) Kino.curHelpLine.setText("Datum: dd.mm.yyyy   Zeit: hh:mm:ss   Faktor: 1..3600 sec");
            }

            public void mouseExited(MouseEvent e) {
                if (Kino.curHelpLine != null) Kino.curHelpLine.setText("");
            }
        });
        secFaktor = 1;
        add("West", gs);
        add("Center", stringFaktor);
        add("East", set);
    }

    /**liefert die bevorzugte Gr�sse des Panels*/
    public Dimension getPreferredSize() {
        return new Dimension(110, 20);
    }

    /**liefert die minimale Gr�sse des Panels*/
    public Dimension getMinimumSize() {
        return new Dimension(110, 20);
    }

    /**setzen des Faktor secFaktor in Klasse Calc*/
    public void setFactor(int secFactor) {
        calculiere.setFactor(secFactor);
    }

    /**setzen des Faktors tgesetzt in Klasse Calc und setzen der neuen Zeit<br>
	in Klasse CinemaDate*/
    public void setTime(int ho, int mi, int se) {
        calculiere.setTgesetzt(true);
        cD.setTime(ho, mi, se);
    }

    /**setzen des Faktors dgesetzt in Klasse Calc und setzen des neuen Datums<br>
	in Klasse CinemaDate*/
    public void setDate(int da, int mo, int ye) {
        calculiere.setDgesetzt(true);
        cD.setDate(da, mo, ye);
        cD.getDateString();
    }

    /**ruft die Methode monatInt der Klasse Calc auf um einen Integerwert zur�ck<br>
	zu bekommen*/
    public int monatInt(String monat) {
        return calculiere.monatInt(monat);
    }
}
