package com.cowlark.cowcalc;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Main extends MIDlet implements CommandListener {

    static Main instance;

    Console console;

    Command exitCommand = new Command("Exit", Command.EXIT, 0);

    Command helpCommand = new Command("Help", Command.HELP, 0);

    static final int ENTRYINT = 0;

    static final int ENTRYDEC = 1;

    static final int ANSWER = 2;

    int state = ANSWER;

    Fraction result = new Fraction(0);

    long left = 0;

    long right = 0;

    int precision = 0;

    boolean negative = false;

    static final int NOOP = -1;

    static final int PLUS = 0;

    static final int MINUS = 1;

    static final int TIMES = 2;

    static final int DIVIDE = 3;

    int operator = NOOP;

    static Main getInstance() {
        return instance;
    }

    public Main() {
        instance = this;
        console = new Console();
        console.addCommand(exitCommand);
        console.addCommand(helpCommand);
        console.setCommandListener(this);
    }

    protected void destroyApp(boolean forced) throws MIDletStateChangeException {
    }

    protected void pauseApp() {
    }

    protected void startApp() throws MIDletStateChangeException {
        setDisplayable(null);
        console.write("CowCalc v" + Settings.Version + "\n" + "Ready.\n");
        updateDisplay();
    }

    public void setDisplayable(Displayable d) {
        if (d == null) d = console;
        Display.getDisplay(this).setCurrent(d);
    }

    public void commandAction(Command c, Displayable s) {
        if (c == exitCommand) notifyDestroyed(); else if (c == helpCommand) setDisplayable(new HelpViewer());
    }

    public void message(String s) {
        Alert alert = new Alert("CowCalc", s, null, AlertType.INFO);
        alert.setTimeout(Alert.FOREVER);
        Display.getDisplay(this).setCurrent(alert, console);
    }

    static String l2s(long l, int length) {
        String s = Long.toString(l);
        int sl = s.length();
        if (sl < length) s = "0000000000000000".substring(1, 1 + length - sl) + s;
        return s;
    }

    static String l2s(long l) {
        return Long.toString(l);
    }

    void updateDisplay() {
        int y = console.getY();
        int w = console.getWidthChars();
        String s = "";
        if (state == ANSWER) {
            s = result.toString();
        } else {
            StringBuffer sb = new StringBuffer();
            if (negative) sb.append('-');
            sb.append(l2s(left));
            if (state == ENTRYDEC) {
                if (Settings.FractionMode) {
                    sb.append('/');
                    sb.append(right);
                } else {
                    sb.append('.');
                    if (precision > 0) sb.append(l2s(right, precision));
                }
            }
            s = sb.toString();
        }
        int xo = w - s.length();
        for (int x = 0; x < w; x++) {
            char c = 0;
            if (x >= xo) c = s.charAt(x - xo);
            console.writeAt(x, y, c);
        }
        console.repaintLine(y);
    }

    Fraction getValue() {
        Fraction f = null;
        switch(state) {
            case ANSWER:
                f = result;
                break;
            case ENTRYINT:
                f = new Fraction(left);
                if (negative) f.mul(-1);
                break;
            case ENTRYDEC:
                if (Settings.FractionMode) f = new Fraction(left, right); else {
                    f = new Fraction(right, Util.pow10(precision));
                    f.add(left);
                }
                if (negative) f.mul(-1);
                f.simplify();
                break;
        }
        return f;
    }

    void keypress(boolean held, char c) {
        if (!held) {
            if (c == '#') {
                switch(state) {
                    case ANSWER:
                        result = new Fraction(0);
                        break;
                    case ENTRYINT:
                        left /= 10;
                        break;
                    case ENTRYDEC:
                        if (precision > 1) {
                            right /= 10;
                            precision--;
                        } else {
                            right = precision = 0;
                            state = ENTRYINT;
                        }
                        break;
                }
            } else if (Character.isDigit(c)) {
                switch(state) {
                    case ANSWER:
                        state = ENTRYINT;
                        left = right = precision = 0;
                        negative = false;
                    case ENTRYINT:
                        {
                            long l = left;
                            try {
                                l = Util.mul(l, 10);
                                l = Util.add(l, c - '0');
                                left = l;
                            } catch (OverflowException e) {
                            }
                            break;
                        }
                    case ENTRYDEC:
                        {
                            long r = right;
                            try {
                                r = Util.mul(r, 10);
                                r = Util.add(r, c - '0');
                                right = r;
                                precision++;
                            } catch (OverflowException e) {
                            }
                            break;
                        }
                }
            } else if (c == '*') {
                switch(state) {
                    case ANSWER:
                        left = right = precision = 0;
                        negative = false;
                    case ENTRYINT:
                        state = ENTRYDEC;
                        break;
                    case ENTRYDEC:
                        break;
                }
            }
        } else {
            switch(c) {
                case '3':
                    setDisplayable(new KeymapViewer());
                    break;
                case '7':
                    switch(state) {
                        case ANSWER:
                            result.mul(-1);
                            break;
                        case ENTRYINT:
                        case ENTRYDEC:
                            negative = !negative;
                            break;
                    }
                    break;
                case '9':
                    {
                        result = getValue();
                        result.sqrt();
                        state = ANSWER;
                        console.write("\nsqrt\n");
                        break;
                    }
                case '*':
                    {
                        result = getValue();
                        state = ANSWER;
                        Settings.FractionMode = !Settings.FractionMode;
                        if (Settings.FractionMode) console.write("\nfracmode on\n"); else console.write("\nfracmode off\n");
                        break;
                    }
                default:
                    {
                        Fraction f = getValue();
                        state = ANSWER;
                        switch(operator) {
                            case PLUS:
                                result.add(f);
                                break;
                            case MINUS:
                                result.sub(f);
                                break;
                            case TIMES:
                                result.mul(f);
                                break;
                            case DIVIDE:
                                f.invert();
                                result.mul(f);
                                break;
                            case NOOP:
                                result = f;
                                break;
                        }
                        switch(c) {
                            case '2':
                                operator = DIVIDE;
                                console.write("\n/\n");
                                break;
                            case '5':
                                operator = TIMES;
                                console.write("\n*\n");
                                break;
                            case '8':
                                operator = MINUS;
                                console.write("\n-\n");
                                break;
                            case '0':
                                operator = PLUS;
                                console.write("\n+\n");
                                break;
                            case '#':
                                operator = NOOP;
                                console.write("\n=\n");
                                break;
                        }
                    }
            }
        }
        updateDisplay();
    }
}
