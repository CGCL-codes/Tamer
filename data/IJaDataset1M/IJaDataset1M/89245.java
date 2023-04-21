package jscicalc;

import jscicalc.button.*;

/**
 * This class creates the main CalculatorPanel, which contains 44 buttons and
 * a display panel (DisplayPanel object) to emulate an LCD display.
 * The panel needs to know about the <em>controller</em> CalculatorApplet because
 * the controller creates the DisplayPanel and because each button must can
 * ask the CalculatorApplet object to change state or add something to the
 * expression stored in Parser.
 *
 * The CalculatorPanel is the pane that sits inside the main frame or is the
 * visible part of the applet.
 *
 * In fact the calculator has more than 44 buttons. This creates an obvious problem:
 * how to make all the buttons available without cluttering up the GUI in much
 * the way that a real calculator does and without making the buttons small
 * and unreadable.
 *
 * The buttons are derived from CalculatorButton and use HTML to draw their text
 * representation. This allows us to have very readable buttons that we can rescale.
 * But it means we cannot redraw buttons instantly. So, the solution of redrawing
 * buttons as needed in response to a press of the SHIFT button produces a noticeable
 * slowdown in the calculator operation.
 * 
 * My solution is to have several CalculatorPanel objects, each derived from
 * this class and to replace them as needed. This creates its own problem. We need
 * to setup and draw six panels with nearly 350 buttons. Morevover the calculator
 * should be extensible and allow the possibility of even more. The consequence of
 * all this drawing is that the calculator would be quite slow to start up - nothing
 * like as slow as starting M$ office - but enough to be noticeable and irritating.
 *
 * There are several ways to get round this, not mutually incompatible. For
 * example, you could use separate button panels so that not all buttons get
 * replaced. But it suited me to use multithreading instead. The first panel
 * available should be the PlainPanel object and it is created and added first.
 * The remaining panels are created on a low priority thread. So, at least for
 * simple calculations and switching on, the calculator starts reasonably quickly
 * and, in particular, shows a visible GUI almost immediately after the frame
 * appears.
 *
 * To make the multithreading work, this calculator implements the Runnable
 * interface.
 * @see CalculatorButton
 * @see CalculatorApplet
 * @see DisplayPanel
 *
 * @author J.&nbsp;D.&nbsp;Lamb 
 * @version $Revision: 14 $
 */
public abstract class CalculatorPanel extends javax.swing.JPanel implements Runnable {

    /**
     * We use this rather than a constructor to create a CalculatorPanel
     * because in practice we always want a subclass.
     * We supply the CalculatorApplet object and a
     * SpecialButtonType used to identify which subclass we want.
     * We also pass a colour. Conceivably different panels could have different
     * colours, though it might be better to get each to ask CalculatorApplet for
     * its colour.
     * 
     * If sbt is SpecialButtonType.NONE then the CalculatorPanel is a PlainPanel
     * and is created now. Otherwise it is created in a background thread.
     * @param applet The CalculatorApplet object needed for the buttons and
     * DisplayPanel
     * @param sbt Used to identify what to construct
     * @param colour What colour should the panel background be?
     * @see CalculatorApplet
     * @see SpecialButtonType
     */
    public static CalculatorPanel createPanel(CalculatorApplet applet, SpecialButtonType sbt, java.awt.Color colour) {
        CalculatorPanel p = null;
        switch(sbt) {
            case NONE:
                p = new PlainPanel(applet, sbt, colour);
                p.run();
                return p;
            case SHIFT:
                p = new ShiftPanel(applet, sbt, colour);
                break;
            case STAT:
                p = new StatPanel(applet, sbt, colour);
                break;
            case SHIFT_STAT:
                p = new ShiftStatPanel(applet, sbt, colour);
                break;
            case HEX:
                p = new HexPanel(applet, sbt, colour);
                break;
            case SHIFT_HEX:
                p = new ShiftHexPanel(applet, sbt, colour);
                break;
            default:
                p = null;
        }
        Thread t = new Thread(p);
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
        return p;
    }

    /**
     * Required to implement Runnable interface. Calls setUp().
     */
    public synchronized void run() {
        setUp();
    }

    /**
     * Constructor is protected so that this class never actually gets instantiated.
     * @param applet Needed so that buttons can call the <em>controller</em>
     * @param sbt Stored so that we can identify the subclass
     * @param colour The background colour.
     */
    protected CalculatorPanel(CalculatorApplet applet, SpecialButtonType sbt, java.awt.Color colour) {
        this.applet = applet;
        this.sbt = sbt;
        this.colour = colour;
    }

    /**
     * Paint the component: sets the colour for the background.
     * @param graphics Needed for all paintComponent methods
     */
    public synchronized void paintComponent(java.awt.Graphics graphics) {
        super.paintComponent(graphics);
        graphics.setColor(colour);
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Creates a key map for the CalculatorApplet, puts in nonstandard buttons,
     * asks the subclass method to set the standard buttons, and arranges all
     * buttons in a SpringLayout.
     */
    private void setUp() {
        keyMap = new java.util.HashMap<Character, CalculatorButton>();
        layout = new javax.swing.SpringLayout();
        setLayout(layout);
        buttons = new java.util.Vector<CalculatorButton>();
        for (int i = 0; i < 45; ++i) {
            CalculatorButton button = null;
            if (i == 0) button = new OffButton(applet); else if (i == 5) button = new ModeButton(applet); else if (i == 10) switch(sbt) {
                case SHIFT:
                case SHIFT_STAT:
                case SHIFT_HEX:
                    button = new OrigButton(applet);
                    break;
                default:
                    button = new ShiftButton(applet);
                    break;
            } else if (i == 14) switch(sbt) {
                case SHIFT:
                case SHIFT_STAT:
                case SHIFT_HEX:
                    if (applet.getGraphical()) button = new jscicalc.button.GraphButton(applet); else button = new CalculatorButton(applet);
                    break;
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 15) switch(sbt) {
                case SHIFT:
                case SHIFT_STAT:
                case SHIFT_HEX:
                    button = new CopyButton(applet);
                    break;
                case NONE:
                case STAT:
                case HEX:
                    button = new STOButton(applet);
                    break;
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 16) switch(sbt) {
                case SHIFT:
                case SHIFT_HEX:
                case SHIFT_STAT:
                    button = new SciButton(applet);
                    break;
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 20) switch(sbt) {
                case NONE:
                case STAT:
                case HEX:
                    button = new RCLButton(applet);
                    break;
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 21) switch(sbt) {
                case SHIFT:
                case SHIFT_HEX:
                case SHIFT_STAT:
                    button = new PolButton(applet);
                    break;
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 25) switch(sbt) {
                case SHIFT:
                case SHIFT_HEX:
                    button = new MminusButton(applet);
                    break;
                case STAT:
                    button = new SplusButton(applet);
                    break;
                case SHIFT_STAT:
                    button = new SminusButton(applet);
                    break;
                default:
                    button = new MplusButton(applet);
            } else if (i == 26) switch(sbt) {
                case SHIFT:
                case SHIFT_HEX:
                case SHIFT_STAT:
                    button = new CplxButton(applet);
                    break;
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 30) switch(sbt) {
                case NONE:
                case STAT:
                case HEX:
                    button = new DelButton(applet);
                    break;
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 32) switch(sbt) {
                case SHIFT:
                case SHIFT_HEX:
                    button = new HexButton(applet);
                    break;
                case SHIFT_STAT:
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 33) switch(sbt) {
                case SHIFT:
                case SHIFT_HEX:
                    button = new DecButton(applet);
                    break;
                case SHIFT_STAT:
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 34) switch(sbt) {
                case NONE:
                case STAT:
                case HEX:
                    button = new AnsButton(applet);
                    break;
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 35) switch(sbt) {
                case SHIFT:
                case SHIFT_HEX:
                    button = new MclButton(applet);
                    break;
                case SHIFT_STAT:
                    button = new SclButton(applet);
                    break;
                default:
                    button = new OnButton(applet);
            } else if (i == 37) switch(sbt) {
                case SHIFT:
                case SHIFT_HEX:
                    button = new BinButton(applet);
                    break;
                case SHIFT_STAT:
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 38) switch(sbt) {
                case SHIFT:
                case SHIFT_HEX:
                    button = new OctButton(applet);
                    break;
                case SHIFT_STAT:
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 39) switch(sbt) {
                case NONE:
                case STAT:
                case HEX:
                    button = new EqualsButton(applet);
                    break;
                default:
                    button = new CalculatorButton(applet);
            } else if (i == 40) button = new LeftButton(applet); else if (i == 41) button = new RightButton(applet); else if (i == 42) button = new InfoButton(applet); else if (i == 43) button = new UpButton(applet); else if (i == 44) button = new DownButton(applet); else button = new CalculatorButton(applet);
            buttons.add(button);
            add(button);
        }
        setButtons();
        layoutSprings();
    }

    /**
     * Subclasses implement this so that they can define all the standard buttons
     * like Numeral, Add, etc. Nonstandard buttons (ModeButton, ShiftButton, ...)
     * are handles separately by setUp().
     */
    protected abstract void setButtons();

    /**
     * Long function that lays out buttons and DisplayPanel. It uses a SpringLayout,
     * which is very powerful but not well documented by Sun, to place the buttons
     * exactly where I want them while allowing the whole calculator to be rescaled
     * as required.
     */
    public synchronized void layoutSprings() {
        xBorderSpring = javax.swing.Spring.constant(0, applet.strutSize(), Integer.MAX_VALUE);
        yBorderSpring = javax.swing.Spring.constant(0, applet.strutSize(), Integer.MAX_VALUE);
        strutX = javax.swing.Spring.constant(applet.strutSize());
        strutY = javax.swing.Spring.constant(applet.strutSize());
        minX = javax.swing.Spring.constant(applet.minSize());
        minY = javax.swing.Spring.constant(applet.minSize());
        buttonWidthSpring = javax.swing.Spring.constant(applet.buttonWidth());
        narrowButtonWidthSpring = javax.swing.Spring.constant(applet.buttonWidth() - applet.strutSize() * 2 - (applet.minSize() + applet.buttonHeight()) / 2);
        thinButtonWidthSpring = javax.swing.Spring.constant(applet.buttonHeight());
        buttonHeightSpring = javax.swing.Spring.constant(applet.buttonHeight());
        shortButtonHeightSpring = javax.swing.Spring.constant(applet.buttonHeight() - applet.minSize());
        int width3 = applet.buttonHeight();
        if ((applet.minSize() - width3) % 2 != 0) ++width3;
        int width23 = applet.buttonWidth() + (applet.minSize() - width3) / 2;
        shortButtonWidthSpring = javax.swing.Spring.constant(width3);
        displayWidthSpring = javax.swing.Spring.scale(buttonWidthSpring, 6);
        displayWidthSpring = javax.swing.Spring.sum(displayWidthSpring, strutX);
        displayWidthSpring = javax.swing.Spring.sum(displayWidthSpring, javax.swing.Spring.scale(minX, 4));
        displayHeightSpring = javax.swing.Spring.constant(applet.displayHeight());
        javax.swing.Spring panelWidthSpring = javax.swing.Spring.scale(xBorderSpring, 2);
        panelWidthSpring = javax.swing.Spring.sum(panelWidthSpring, javax.swing.Spring.scale(buttonWidthSpring, 8));
        panelWidthSpring = javax.swing.Spring.sum(panelWidthSpring, javax.swing.Spring.scale(strutX, 2));
        panelWidthSpring = javax.swing.Spring.sum(panelWidthSpring, javax.swing.Spring.scale(minX, 5));
        javax.swing.Spring panelHeightSpring = javax.swing.Spring.scale(yBorderSpring, 2);
        panelHeightSpring = javax.swing.Spring.sum(panelHeightSpring, displayHeightSpring);
        panelHeightSpring = javax.swing.Spring.sum(panelHeightSpring, strutY);
        panelHeightSpring = javax.swing.Spring.sum(panelHeightSpring, javax.swing.Spring.scale(buttonHeightSpring, 5));
        panelHeightSpring = javax.swing.Spring.sum(panelHeightSpring, javax.swing.Spring.scale(minY, 4));
        javax.swing.SpringLayout.Constraints constraints = layout.getConstraints(this);
        constraints.setWidth(panelWidthSpring);
        constraints.setHeight(panelHeightSpring);
        javax.swing.Spring x2 = javax.swing.Spring.sum(xBorderSpring, javax.swing.Spring.sum(buttonWidthSpring, minX));
        javax.swing.Spring x3 = javax.swing.Spring.sum(x2, javax.swing.Spring.sum(buttonWidthSpring, minX));
        javax.swing.Spring x4 = javax.swing.Spring.sum(x3, javax.swing.Spring.sum(buttonWidthSpring, strutX));
        javax.swing.Spring x5 = javax.swing.Spring.sum(x4, javax.swing.Spring.sum(buttonWidthSpring, minX));
        javax.swing.Spring x6 = javax.swing.Spring.sum(x5, javax.swing.Spring.sum(buttonWidthSpring, minX));
        javax.swing.Spring x7 = javax.swing.Spring.sum(x6, javax.swing.Spring.sum(buttonWidthSpring, strutX));
        javax.swing.Spring x8 = javax.swing.Spring.sum(x7, javax.swing.Spring.sum(buttonWidthSpring, minX));
        javax.swing.Spring y1 = javax.swing.Spring.sum(yBorderSpring, javax.swing.Spring.sum(displayHeightSpring, strutY));
        javax.swing.Spring y2 = javax.swing.Spring.sum(y1, javax.swing.Spring.sum(buttonHeightSpring, minY));
        javax.swing.Spring y3 = javax.swing.Spring.sum(y2, javax.swing.Spring.sum(buttonHeightSpring, minY));
        javax.swing.Spring y4 = javax.swing.Spring.sum(y3, javax.swing.Spring.sum(buttonHeightSpring, minY));
        javax.swing.Spring y5 = javax.swing.Spring.sum(y4, javax.swing.Spring.sum(buttonHeightSpring, minY));
        for (int i = 0; i < 40; ++i) {
            constraints = layout.getConstraints(buttons().elementAt(i));
            constraints.setWidth(buttonWidthSpring);
            constraints.setHeight(buttonHeightSpring);
            if (i < 5) constraints.setX(xBorderSpring); else if (i < 10) constraints.setX(x2); else if (i < 15) constraints.setX(x3); else if (i < 20) constraints.setX(x4); else if (i < 25) constraints.setX(x5); else if (i < 30) constraints.setX(x6); else if (i < 35) constraints.setX(x7); else constraints.setX(x8);
            int r = i % 5;
            if (r == 0) constraints.setY(y1); else if (r == 1) constraints.setY(y2); else if (r == 2) constraints.setY(y3); else if (r == 3) constraints.setY(y4); else constraints.setY(y5);
        }
        javax.swing.Spring s4ym = javax.swing.Spring.constant(applet.strutSize() + (applet.displayHeight() - applet.buttonHeight()) / 2);
        constraints = layout.getConstraints(buttons().elementAt(40));
        constraints.setWidth(narrowButtonWidthSpring);
        constraints.setHeight(buttonHeightSpring);
        javax.swing.Spring s40x = javax.swing.Spring.sum(displayWidthSpring, javax.swing.Spring.scale(strutX, 4));
        constraints.setX(s40x);
        constraints.setY(s4ym);
        constraints = layout.getConstraints(buttons().elementAt(42));
        constraints.setWidth(thinButtonWidthSpring);
        constraints.setHeight(buttonHeightSpring);
        javax.swing.Spring s42x = javax.swing.Spring.sum(s40x, javax.swing.Spring.sum(minX, narrowButtonWidthSpring));
        constraints.setX(s42x);
        constraints.setY(s4ym);
        constraints = layout.getConstraints(buttons().elementAt(41));
        constraints.setWidth(narrowButtonWidthSpring);
        constraints.setHeight(buttonHeightSpring);
        javax.swing.Spring s41x = javax.swing.Spring.sum(s42x, javax.swing.Spring.sum(minX, thinButtonWidthSpring));
        constraints.setX(s41x);
        constraints.setY(s4ym);
        constraints = layout.getConstraints(buttons().elementAt(43));
        constraints.setWidth(shortButtonWidthSpring);
        constraints.setHeight(shortButtonHeightSpring);
        javax.swing.Spring s434x = javax.swing.Spring.sum(javax.swing.Spring.constant(width23), javax.swing.Spring.sum(displayWidthSpring, javax.swing.Spring.scale(strutX, 2)));
        constraints.setX(s434x);
        constraints.setY(strutY);
        constraints = layout.getConstraints(buttons().elementAt(44));
        constraints.setWidth(shortButtonWidthSpring);
        constraints.setHeight(shortButtonHeightSpring);
        constraints.setX(s434x);
        javax.swing.Spring s44y = javax.swing.Spring.sum(javax.swing.Spring.scale(minY, 2), javax.swing.Spring.sum(shortButtonHeightSpring, javax.swing.Spring.sum(buttonHeightSpring, strutY)));
        constraints.setY(s44y);
    }

    public synchronized void setDisplayPanel() {
        remove(applet.displayPanel());
        add(applet.displayPanel());
        javax.swing.SpringLayout.Constraints constraints = ((javax.swing.SpringLayout) getLayout()).getConstraints(applet.displayPanel());
        constraints = layout.getConstraints(applet.displayPanel());
        constraints.setX(xBorderSpring);
        constraints.setY(yBorderSpring);
        constraints.setWidth(displayWidthSpring);
        constraints.setHeight(displayHeightSpring);
    }

    /**
     * CalculatorApplet might need this.
     * @return The list of buttons on this panel
     */
    public synchronized java.util.Vector<CalculatorButton> buttons() {
        return buttons;
    }

    /**
     * CalculatorApplet uses this to handle keyboard shortcuts. It needs to know
     * the char associated with each button so that it can find the button and
     * fire CalculatorButton.actionPerformed() as required.
     * @return a map from characters to buttons
     */
    public synchronized java.util.HashMap<Character, CalculatorButton> keyMap() {
        return keyMap;
    }

    /**
     * The method that creates the keyMap. It has a debug feature that tells you
     * if you&rsquo;ve used any character twice. This is useful for detecting subtle
     * problems.
     */
    protected void createKeyMap() {
        for (CalculatorButton o : buttons) {
            if (o.shortcut() == 0) continue;
            if (keyMap.containsKey(o.shortcut())) {
                System.out.print("Repeat of ");
                System.out.print(o.shortcut());
                System.out.print(" in ");
                System.out.println(o.tooltip());
            }
            keyMap.put(o.shortcut(), o);
        }
    }

    /**
     * I can&rsquo;t rememeber why we have this.
     * @return the base, which ought just to be the CalculatorApplet value.
     */
    public Base base() {
        return base;
    }

    /**
     * I can&rsquo;t rememeber why we have this. Sets tha Base.
     * @param base the base, which ought just to be the CalculatorApplet value.
     */
    public void base(Base base) {
        this.base = base;
    }

    /**
     * A Base. I don&rsquo;t know if we use this.
     */
    private Base base;

    /**
     * Background colour
     */
    protected java.awt.Color colour;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring xBorderSpring;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring yBorderSpring;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring strutX;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring strutY;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring minX;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring minY;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring buttonWidthSpring;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring narrowButtonWidthSpring;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring thinButtonWidthSpring;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring buttonHeightSpring;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring shortButtonHeightSpring;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring shortButtonWidthSpring;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring displayWidthSpring;

    /**
     * A Spring. Probably doesn&rsquo;t need to be a class member.
     */
    private javax.swing.Spring displayHeightSpring;

    /**
     * We need to know the CalculatorApplet so we can assign it to buttons as
     * we add them.
     */
    protected final CalculatorApplet applet;

    /**
     * used to identify panels, which are accessed through a CalculatorPanel pointer.
     */
    protected final SpecialButtonType sbt;

    /**
     * The Layout. Probably doesn&rsquo;t need to be a class member.
     */
    protected javax.swing.SpringLayout layout;

    /**
     * The list of buttons.
     */
    protected java.util.Vector<CalculatorButton> buttons;

    /**
     * A map used by CalculatorApplet to fire Action events to buttons based
     * on keyboard presses.
     * @see #keyMap()
     */
    protected java.util.HashMap<Character, CalculatorButton> keyMap;
}
