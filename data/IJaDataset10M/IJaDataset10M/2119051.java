package ArianneEditor;

import java.text.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.borland.jbcl.layout.*;
import java.util.logging.Level;
import ArianneUtil.LogHandler;

/**
 * ButtonShape � la classe derivata da Shapes che implementa le regole di
 * visualizzazione degli ogetti Button
 * <p>
 *
 * @author      Andrea Annibali
 * @version     1.0
 */
class ButtonShape extends EditorShapes {

    private String textVal = null;

    private DecimalFormat stringFormatter = new DecimalFormat();

    private String formatter = null;

    private Color oldTextColor = Color.gray;

    private int mousePosX = -1;

    private int mousePosY = -1;

    private int textHAlignment = SwingConstants.CENTER;

    private int textVAlignment = SwingConstants.CENTER;

    private boolean insideArea = false;

    private boolean enabled = true;

    private boolean blinking = false;

    private Color alternateColor = Color.lightGray;

    private javax.swing.Timer blinkTimer = new javax.swing.Timer(1000, new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            if (button.getForeground().getRGB() != alternateColor.getRGB()) {
                oldTextColor = button.getForeground();
                button.setForeground(alternateColor);
            } else {
                setTextColor(oldTextColor);
                button.setForeground(oldTextColor);
            }
            arrangeLocation(getFatherPanel());
        }
    });

    JButton button = new JButton("Button");

    JMenuItem menuItem2;

    JMenuItem menuItem3;

    private int fontSize = 11;

    private Font actFont = new java.awt.Font("Courier New", 0, fontSize);

    /**
   * Costruttore richiamato quando viene disegnata un nuovo bottone
   *
   * @param elId identificativo univoco dell'oggetto
   * @param tV label del bottone
   * @param ePoint end-point
   * @param sPoint start-point
   * @param c colore del bottone
   * @param f font della label del bottone
   * @param imgN nome dell'immagine di appartenenza
   * @param p il EditorDrawingPanel su cui viene disegnato il bottone
   * @param enbld stato di abilitazione del bottone
   */
    ButtonShape(String imgN, int elId, String tV, Point ePoint, Point sPoint, Color c, java.awt.Font f, EditorDrawingPanel p, boolean enbld, boolean blnk, int tHAlignment, int tVAlignment, int ovl) {
        super(imgN, p, elId, "Button", 4, ovl, ePoint, sPoint, true);
        LogHandler.log("Inizio creazione del bottone", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        this.textVal = tV;
        this.actFont = f;
        this.setInBackground(true);
        this.enabled = enbld;
        this.textHAlignment = tHAlignment;
        this.textVAlignment = tVAlignment;
        if (blnk) this.startBlinking(); else this.stopBlinking();
        setShapeBorderColor(c);
        initButton("Button", getFatherPanel(), true);
        LogHandler.log("Bottone creato", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        setIntCoord();
    }

    /**
   * Costruttore utilizzato durante la loadButtonShape
   * @param elId identificativo univoco dell'oggetto
   * @param tV testo della label
   * @param xPnt array delle coordinate X del poligono di inscrizione del bottone
   * @param yPnt array delle coordinate X del poligono di inscrizione del bottone
   * @param sColor colore del bordo
   * @param fColor colore di riempimento
   * @param tColor colore del testo
   * @param f font della label del bottone
   * @param imgN nome dell'immagine di appartenenza
   * @param precision formattazione della label del bottone
   * @param p il EditorDrawingPanel su cui viene disegnato il bottone
   * @param enbld stato di abilitazione del bottone
   * @param blnk stato di lampeggiamento del bottone
   * @param tHAlignment allineamento orizzontale
   * @param tVAlignment allineamento verticale
   */
    ButtonShape(int elId, String tV, double xPnt[], double yPnt[], int sColor, int fColor, int tColor, java.awt.Font f, String imgN, String precision, EditorDrawingPanel p, boolean enbld, boolean blnk, int tHAlignment, int tVAlignment, int ovl) {
        super(imgN, p, elId, "Button", 4, xPnt, yPnt, ovl, true);
        LogHandler.log("Inizio creazione del bottone", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        this.textVal = tV;
        this.setInBackground(true);
        this.formatter = precision;
        this.enabled = enbld;
        this.textHAlignment = tHAlignment;
        this.textVAlignment = tVAlignment;
        if (blnk) this.startBlinking(); else this.stopBlinking();
        setShapeBorderColor(new Color(sColor));
        setTextColor(new Color(tColor));
        actFont = f;
        setXPoints(xPnt);
        setYPoints(yPnt);
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for (int i = 0; i < xPnt.length; i++) if (xPnt[i] < minX) minX = xPnt[i];
        for (int i = 0; i < xPnt.length; i++) if (xPnt[i] > maxX) maxX = xPnt[i];
        for (int i = 0; i < yPnt.length; i++) if (yPnt[i] < minY) minY = yPnt[i];
        for (int i = 0; i < yPnt.length; i++) if (yPnt[i] > maxY) maxY = yPnt[i];
        this.inscribePoints(new Point((int) Math.round(maxX), (int) Math.round(maxY)), new Point((int) Math.round(minX), (int) Math.round(minY)));
        initButton(tV, getFatherPanel(), true);
        LogHandler.log("Fine creazione del bottone", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        setIntCoord();
    }

    /**
   * Costruttore invocato alla pressione del tasto di inserimento nuovo bottone del PictureEditor
   * @param c colore del bordo del bottone
   * @param tV testo della label
   * @param p il EditorDrawingPanel su cui viene disegnato il bottone
   */
    ButtonShape(String imgN, int elId, Color c, String tV, EditorDrawingPanel p, int ovl) {
        super(imgN, p, elId, "Button", 4, new double[4], new double[4], ovl, true);
        LogHandler.log("Inizio creazione del bottone", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        this.textVal = tV;
        this.enabled = false;
        setXPoints(new double[getNumVertex()]);
        setYPoints(new double[getNumVertex()]);
        for (int i = 0; i < getNumVertex(); i++) {
            setXPoint(i, 0);
            setYPoint(i, 0);
        }
        setShapeBorderColor(c);
        initButton(tV, getFatherPanel(), false);
        LogHandler.log("Fine creazione del bottone", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        setIntCoord();
    }

    /**
   * Restituisce true se il bottone � abilitato
   * @return true se abilitato
   */
    public boolean isEnabled() {
        return enabled;
    }

    /**
   * Imposta l'abilitazione del bottone
   * @param v se � true il bottone viene abilitato
   */
    public void setEnabled(boolean v) {
        LogHandler.log("Abilitazione del bottone", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        enabled = v;
        this.getButton().setEnabled(enabled);
        if (!enabled) {
            this.getButton().setForeground(Color.gray);
        } else {
            this.getButton().setForeground(this.getTextColor());
        }
    }

    /**
   * Restituisce una stringa contenente la label visualizzata sul bottone
   * @return la label associata al bottone
   */
    public String getButtonLabel() {
        return textVal;
    }

    /**
   * Imposta la label associata al bottone
   * @param bLabel la label da assocuare al bottone
   */
    public void setButtonLabel(String bLabel, JPanel p) {
        arrangeLocation(p);
        LogHandler.log("Impostazione della label del bottone a " + bLabel, Level.FINEST, "LOG_MSG", isLoggingEnabled());
        textVal = bLabel;
        bLabel = bLabel.replaceAll("&&", "<br>");
        getButton().setText("<HTML>" + bLabel + "</HTML>");
        arrangeLocation(p);
    }

    public void arrangeLocation(JPanel p) {
        if (button.getX() != getXCoordinates()[0] || button.getY() != getYCoordinates()[0]) {
            button.setLocation(getXCoordinates()[0], getYCoordinates()[0]);
            p.remove(button);
            addToPanel(p);
        }
        if (button.getSize().getWidth() != (int) Math.round(getWidth()) || button.getSize().getHeight() != (int) Math.round(getHeight())) {
            Dimension newDim = new Dimension((int) Math.round(getWidth()), (int) Math.round(getHeight()));
            button.setSize(newDim);
            button.setPreferredSize(newDim);
            button.setMaximumSize(newDim);
            button.setMinimumSize(newDim);
            p.remove(button);
            addToPanel(p);
        }
    }

    /**
   * Inizializza le proprieta' del bottone (bordo in rilievo, colore, ...)
   * @param bLabel la label che viene associata al bottone
   */
    public void initButton(String bLabel, JPanel p, boolean addToPanel) {
        LogHandler.log("Inizializzazione del bottone", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        if (this.getFatherPanel() != null && addToPanel) {
            this.getFatherPanel().add(button, new XYConstraints(getXCoordinates()[0], getYCoordinates()[0], (int) Math.round(getWidth()), (int) Math.round(getHeight())));
        }
        this.getButton().setContentAreaFilled(true);
        this.getButton().setBorderPainted(true);
        this.getButton().setDoubleBuffered(false);
        this.getButton().setOpaque(true);
        this.getButton().addMouseListener(new Button_this_mouseAdapter(this));
        this.getButton().addMouseMotionListener(new Shape_Button_mouseMotionAdapter(this));
        this.setButtonLabel(bLabel, p);
        this.getButton().setToolTipText("Id = " + getElemId());
        setFont(actFont);
        this.getButton().setHorizontalAlignment(getTextHAlignMent());
        this.getButton().setVerticalAlignment(getTextVAlignMent());
        this.setEnabled(this.enabled);
        if (getTextColor().getRGB() == alternateColor.getRGB()) alternateColor = Color.white;
        LogHandler.log("Fine inizializzazione del bottone", Level.FINEST, "LOG_MSG", isLoggingEnabled());
    }

    /**
   * Restituisce l'allineamento orizzontale della label del bottone
   * @return la costante intera che rappresenta l'allineamento del bottone
   */
    public int getTextHAlignMent() {
        return this.textHAlignment;
    }

    /**
   * Imposta l'allineamento orizzontale della label del bottone
   * @param tha la costante intera che rappresenta l'allineamento del bottone
   */
    public void setTextHAlignment(int tha) {
        textHAlignment = tha;
        this.getButton().setHorizontalAlignment(tha);
    }

    /**
   * Restituisce l'allineamento verticale della label del bottone
   * @return la costante intera che rappresenta l'allineamento del bottone
   */
    public int getTextVAlignMent() {
        return this.textVAlignment;
    }

    /**
   * Imposta l'allineamento verticale della label del bottone
   * @param tha la costante intera che rappresenta l'allineamento del bottone
   */
    public void setTextVAlignment(int tva) {
        textVAlignment = tva;
        this.getButton().setVerticalAlignment(tva);
    }

    /**
   * Imposta il font di visualizzazione della label del bottone
   * @param f il font della label
   */
    public void setFont(Font f) {
        actFont = f;
        this.getButton().setFont(actFont);
    }

    public void incPoints(JPanel p, Point ePoint, Point sPoint, int imgWidth, int imgHeight) {
        if (p != null) {
            p.remove(button);
            incPoints(p, ePoint, sPoint, imgWidth, imgHeight, 1, 1);
            addToPanel(p);
        }
    }

    /**
   * Aggiunge il bottone al EditorDrawingPanel di appartenenza
   * @param p il EditorDrawingPanel di appartenenza
   */
    public void addToPanel(JPanel p) {
        LogHandler.log("Inizio aggiunta al pannello", Level.FINEST, "LOG_MSG", isLoggingEnabled());
        if (p instanceof EditorDrawingPanel) setFatherPanel((EditorDrawingPanel) p);
        for (int i = 0; i < getNumVertex(); i++) {
            setXCoord(i, (int) Math.round(getXPoints()[i]));
            setYCoord(i, (int) Math.round(getYPoints()[i]));
        }
        p.add(button, new XYConstraints(getXCoordinates()[0], getYCoordinates()[0], getIntWidth(), getIntHeight()), 0);
        LogHandler.log("Aggiunto al EditorDrawingPanel", Level.FINEST, "LOG_MSG", isLoggingEnabled());
    }

    /**
   * Restituisce true se il bottone e'lampeggiante
   * @return il valore del flag 'blinking'
   */
    public boolean isBlinking() {
        return this.blinking;
    }

    /**
   * Rende il bottone lampeggiante
   */
    public void startBlinking() {
        blinking = true;
        blinkTimer.start();
    }

    /**
   * Rende il bottone non lampeggiante
   */
    public void stopBlinking() {
        blinking = false;
        blinkTimer.stop();
    }

    /**
   * Questo metodo viene invocato quando viene selezionato il tasto destro del mouse
   * @param e l'evento che ha scatenato l'invocazione di questo metodo
   */
    public void this_mouseClicked(MouseEvent e) {
        this.getFatherPanel().getFather().getCustomGlassPane().repaint();
        if (e.getModifiers() == Event.META_MASK) {
            LogHandler.log("Richiamato il pop-up mediante click del bottone destro", Level.FINEST, "LOG_MSG", isLoggingEnabled());
            popup.setInvoker(this.getFatherPanel());
            popup.show(getFatherPanel(), e.getX() + (int) Math.round(getMinX()), e.getY() + (int) Math.round(getMinY()));
        }
        e.translatePoint(getButton().getX(), getButton().getY());
        getFatherPanel().this_mouseClicked(e);
    }

    /**
   * Questo metodo viene invocato quando viene eseguita la traslazione con il mouse
   * @param e l'evento che ha scatenato l'invocazione di questo metodo
   */
    public void this_mouseDragged(MouseEvent e) {
        e.translatePoint(getButton().getX(), getButton().getY());
        getFatherPanel().this_mouseDragged(e);
    }

    public void this_mouseMoved(MouseEvent e) {
        e.translatePoint(getButton().getX(), getButton().getY());
        getFatherPanel().this_mouseMoved(e);
    }

    public void this_mouseEntered(MouseEvent e) {
        insideArea = true;
        e.translatePoint(getButton().getX(), getButton().getY());
        if (getFatherPanel() != null) {
            getFatherPanel().this_mouseEntered(e);
            getFatherPanel().refresh();
        }
    }

    public void this_mouseExited(MouseEvent e) {
        insideArea = false;
        e.translatePoint(getButton().getX(), getButton().getY());
        if (getFatherPanel() != null) {
            getFatherPanel().this_mouseExited(e);
            getFatherPanel().refresh();
        }
    }

    /**
   * Inizializza i menu di pop-up che si attivano con il tasto destro
   * del mouse
   */
    public void initMenu() {
        super.initMenu();
        menuItem2 = new JMenuItem("Display rule");
        menuItem2.addActionListener(new Button_menuItem2_actionAdapter(this));
        popup.add(menuItem2);
        menuItem3 = new JMenuItem("Button Link Dialog");
        menuItem3.addActionListener(new Button_menuItem3_actionAdapter(this));
        popup.add(menuItem3);
        LogHandler.log("Inizializzazione del menu completata", Level.FINEST, "LOG_MSG", isLoggingEnabled());
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        e.translatePoint(getButton().getX(), getButton().getY());
        getFatherPanel().this_mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void button_actionPerformed(ActionEvent e) {
    }

    /**
   * Restituisce il font di visualizzazione della label del bottone
   * @return il font con cui e' visualizzata la label del bottone
   */
    public Font getFont() {
        return this.actFont;
    }

    /**
   * Restituisce il formatter impostato per l'oggetto testuale corrente
   * @return la stringa con cui e' stato costruito il formatter
   */
    public String getFormatter() {
        return formatter;
    }

    /**
        * Imposta il formatter della stringa di visualizzazione. L'impostazione del
        * formatter rende possibile l'interpretazione del formato dei numeri secondo
        * il 'locale' desiderato. Consente l'intepretazione e visualizzazione di diversi
        * tipi di numeri, compresi gli interi (123), numeri in virgola fissa (123.4),
        * notazione scientifica (1.23E4), percentuali (12%) e valuta ($123).
        * <p>
        * <b>Pattern e simboli</b><br>
        * La formattazione pu� essere impostata definendo un pattern ed un insieme di
        * simboli.
        * Il pattern ha la seguente sintassi:
        * <p><FONT face=Courier>
        * Pattern:<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;PositivePattern<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;PositivePattern ; NegativePattern<br>
        * </FONT><p><FONT face=Courier>
        * PositivePattern:<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;Prefixopt Number Suffixopt<br>
        * </FONT><p><FONT face=Courier>
        * NegativePattern:<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;Prefixopt Number Suffixopt<br>
        * </FONT><p><FONT face=Courier>
        * Prefix:<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;qualsiasi carattere Unicode eccetto \\uFFFE, \\uFFFF, e caratteri speciali<br>
        * </FONT><p><FONT face=Courier>
        * Suffix:<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;qualsiasi carattere Unicode eccetto \\uFFFE, \\uFFFF, e caratteri speciali<br>
        * </FONT><p><FONT face=Courier>
        * Number:<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;Integer Exponentopt<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;Integer . Fraction Exponentopt<br>
        * </FONT><p><FONT face=Courier>
        * Integer:<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;MinimumInteger<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;#<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;# Integer<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;# , Integer<br>
        * </FONT><p><FONT face=Courier>
        * MinimumInteger:<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;0<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;0 MinimumInteger<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;0 , MinimumInteger<br>
        * </FONT><p><FONT face=Courier>
        * Fraction:<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;MinimumFractionopt OptionalFractionopt<br>
        * </FONT><p><FONT face=Courier>
        * MinimumFraction:<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;0 MinimumFractionopt<br>
        * </FONT><p><FONT face=Courier>
        * OptionalFraction:<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;# OptionalFractionopt<br>
        * </FONT><p><FONT face=Courier>
        * Exponent:
        * &nbsp;&nbsp;&nbsp;&nbsp;E MinimumExponent<br>
        * </FONT><p><FONT face=Courier>
        * MinimumExponent:<br>
        * &nbsp;&nbsp;&nbsp;&nbsp;0 MinimumExponentopt<br>
        * </FONT>
        * Un pattern contiene un subpattern positivo e uno negativo, ad esempio "#,##0.00;(#,##0.00)".
        * Ogni subpattern � costituito da un prefisso, una parte numerica e un suffisso.
        * Il pattern negativo � opzionale; se � assente, allora viene utilizzato come subpattern
        * negativo il subpattern positivo insieme al prefisso '-'. Cos� "0.00" da solo � perfettamente
        * equivalente a "0.00;-0.00". Il subpattern negativo esplicito, serve solo a specificare il prefisso
        * negatico ed il suffisso. Il numero di digit e le altre caratteristiche sono le stesse del
        * subpattern positivo. Ci� significa che  "#,##0.0#;(#)" da' luogo allo stesso comporamento di
        * "#,##0.0#;(#,##0.0#)".
        * <p>
        * I prefissi, i suffissi e i vari simboli usati per l'infinito, il numero di digit, i separatori
        * delle migliaia, etc. possono essere dei valori arbitrari. Comunque deve essere fatta attenzione
        * a non far andare in conflitto i simboli con le stringhe, altrimenti il parsing potrebbe essere
        * inaffidabile. Ad esempio entrambi i prefissi positivi e negativi o i suffissi devono essere distinti,
        * altrimenti il parsing non sarebbe in grado di distinguere correttamente i valori positivi da
        * quelli negativi. Allo stesso modo il separatore delle migliaia e la virgola mobile devono
        * essere dei simboli diversi, altrimenti il parsing � impossibile.
        * <p>
        * <b>Special Pattern Characters</b><br>
        * I caratteri speciali servono nei pattern per indicare classi di caratteri, stringhe o altri
        * caratteri.
        * <p>
        * Viene data di seguito spiegazione dei caratteri speciali utilizzabili.
        * <p>
        * <table border="0" cellpadding="0" cellspacing="3">
        * <center>
        * <tbody><tr bgcolor="#ccccff">
        * <td align="left"><b>Simbolo</b> </td><td align="left"><b>Localizzazione</b> </td><td align="left"><b>Significato</b> </td></tr>
        * <tr valign="top">
        * <td><code>0</code> </td><td>Numero </td><td>Digit </td></tr>
        * <tr bgcolor="#eeeeff" valign="top">
        * <td><code>1-9</code> </td><td>Numero </td><td>Da '1' a '9' indica l'arrotondamento. </td></tr>
        * <tr valign="top">
        * <td><code> @</code> </td><td>Numero </td><td>Cifre significative</td></tr>
        * <tr bgcolor="#eeeeff" valign="top">
        * <td><code>#</code> </td><td>Numero </td><td>Cifra</td></tr>
        * <tr valign="top">
        * <td><code>.</code> </td><td>Numero </td><td>Separatore decimale</td></tr>
        * <tr bgcolor="#eeeeff" valign="top">
        * <td><code>-</code> </td><td>Numero </td><td>Segno meno</td></tr>
        * <tr valign="top">
        * <td><code>,</code> </td><td>Numero </td><td>Separatore delle migliaia (raggruppamento di cifre)</td></tr>
        * <tr bgcolor="#eeeeff" valign="top">
        * <td><code>E</code> </td><td>Numero </td><td>Separa mantissa ed esponente in notazione scientifica. <em>Non ha bisogno di essere tra virgolette nel prefisso o nel suffisso.</em> </td></tr>
        * <tr valign="top">
        * <td><code>+</code> </td><td>Esponente </td><td>Prefisso dell'esponente positivo. <em>Non ha bisogno di essere tra virgolette nel prefisso o nel suffisso.</em> </td></tr>
        * <tr bgcolor="#eeeeff" valign="top">
        * <td><code>;</code> </td><td>Delimitatore del subpattern</td><td>Separa il subpattern positivo da quello negativo</td></tr>
        * <tr valign="top">
        * <td><code>%</code> </td><td>Prefisso o suffisso</td><td>Moltiplica per 100 e visualizzato come percentuale</td></tr>
        * <tr bgcolor="#eeeeff" valign="top">
        * <td><code>‰</code> </td><td>Prefisso o suffisso</td><td>Moltiplica per 1000 e visualizzato come per mille</td></tr>
        * <tr valign="top">
        * <td><code> �</code> (<code>¤</code>) </td><td>Prefisso o suffisso</td><td>Segno della valuta
        * sostituito dal segno della valuta. Se messo doppio, � il simbolo della valuta internazionale.
        * Se presente in un pattern, il separatore decimale monetario viene usato come separatore decimale.</td></tr>
        * <tr bgcolor="#eeeeff" valign="top">
        * <td><code>'</code> </td><td>Prefisso o suffisso</td><td>utilizzato per visualizzare
        * i caratteri speciali nel prefisso o nel suffisso, ad esempio, <code>"'#'#"</code>rappresenta 123 come<code>
        * "#123"</code>. </td></tr>
        * <tr valign="top">
        * <td><code>*</code> </td><td>Delimitatore del prefisso o del suffisso</td><td>Pad escape, precede il carattere di padding</td></tr>
        * </tbody></table>
        * </center>
        * @param form la stringa con cui costruire il formatter (ad es. "####.##,##")
        */
    public void setFormatter(String form) {
        formatter = form;
    }

    /**
   * Restituisce la posizione X del mouse
   * @return int
   */
    public int getMousePosX() {
        return this.mousePosX;
    }

    /**
   * Restituisce la posizione Y del mouse
   * @return int
   */
    public int getMousePosY() {
        return this.mousePosY;
    }

    /**
   * Imposta la posizione X del mouse
   * @param xPos int
   */
    public void setMousePosX(int xPos) {
        this.mousePosX = xPos;
    }

    /**
   * Imposta la posizione Y del mouse
   * @param yPos int
   */
    public void setMousePosY(int yPos) {
        this.mousePosY = yPos;
    }

    /**
   * Restituisce il componente JButton associato a questo bottone
   * @return il JButton associato
   */
    public JButton getButton() {
        return button;
    }

    public void setButton(JButton newB) {
        button = newB;
    }

    public void setButtonVisibility() {
        button.setVisible(isInOverlay() && ((getFatherEditor().bckVisible() && isInBackground()) || (getFatherEditor().fgVisible() && !isInBackground())));
    }

    public void draw(Graphics2D g2d, JPanel p, boolean toDraw) {
        if (p != null) {
            arrangeLocation(p);
            button.setEnabled(isEnabled());
            setButtonVisibility();
            if (isSelected()) {
                int selWidth = ra * 2;
                int selHeight = ra * 2;
                int cX = ra;
                int cY = ra;
                g2d.setColor(Color.black);
                for (int i = 0; i < getNumVertex(); i++) drawOval(g2d, getXCoordinates()[i] - cX, getYCoordinates()[i] - cY, selWidth, selHeight, toDraw);
                for (int i = 0; i < getNumVertex(); i++) {
                    double midX = (getXPoints()[i] + getXPoints()[(i + 1) % getNumVertex()]) / 2;
                    double midY = (getYPoints()[i] + getYPoints()[(i + 1) % getNumVertex()]) / 2;
                    drawRect(g2d, (int) Math.round(midX) - ra, (int) Math.round(midY) - ra, selWidth, selHeight, toDraw);
                }
            }
            if (getSquareDrawActive()) {
                Stroke drawingStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
                g2d.setStroke(drawingStroke);
                drawRect(g2d, getXCoordinates()[0], getYCoordinates()[0], getXCoordinates()[2] - getXCoordinates()[0], getYCoordinates()[2] - getYCoordinates()[0], toDraw);
            }
        }
    }

    public boolean isInsideArea(Point p) {
        return insideArea;
    }

    public void inscribePoints(Point ePoint, Point sPoint) {
        int startX = (int) Math.min(ePoint.getX(), sPoint.getX());
        int endX = (int) Math.max(ePoint.getX(), sPoint.getX());
        int startY = (int) Math.min(ePoint.getY(), sPoint.getY());
        int endY = (int) Math.max(ePoint.getY(), sPoint.getY());
        setXPoint(0, startX);
        setYPoint(0, startY);
        setXPoint(1, endX);
        setYPoint(1, startY);
        setXPoint(2, endX);
        setYPoint(2, endY);
        setXPoint(3, startX);
        setYPoint(3, endY);
        setIntCoord();
    }

    void ButtonmenuItem2_actionPerformed(ActionEvent e) {
        this.getFatherPanel().disableKeyListening();
        ButtonPropDialog bpd = new ButtonPropDialog(getFatherPanel().getFatherFrame(), "Button properties", isLoggingEnabled());
        Dimension dlgSize = bpd.getPreferredSize();
        Dimension frmSize = getFatherPanel().getFatherFrame().getSize();
        Point loc = getFatherPanel().getFatherFrame().getLocation();
        bpd.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        bpd.init(this.getTextColor(), this.getShapeBorderColor(), button.getBackground(), this.getFont(), this.getFormatter(), this.getButtonLabel(), this.isEnabled(), this.isBlinking(), this.getTextHAlignMent(), this.getTextVAlignMent(), this.getButton().getWidth(), this.getButton().getHeight());
        bpd.pack();
        getFatherPanel().centerFrame(bpd);
        bpd.setVisible(true);
        this.setTextColor(bpd.getTextColor());
        this.setShapeBorderColor(bpd.getShapeBorderColor());
        setFont(bpd.getFont());
        this.setFormatter(bpd.getFormatter());
        this.setButtonLabel(bpd.getTextVal(), getFatherPanel());
        this.getFatherPanel().enableKeyListening();
        if (bpd.getBlinking()) this.startBlinking(); else this.stopBlinking();
        this.setEnabled(bpd.getEnabled());
        this.setTextHAlignment(bpd.getTextHAlignment());
        this.setTextVAlignment(bpd.getTextVAlignment());
        this.setXPoint(1, getXPoints()[0] + bpd.getButtonWidth());
        this.setXPoint(2, getXPoints()[0] + bpd.getButtonWidth());
        this.setYPoint(2, getYPoints()[0] + bpd.getButtonHeight());
        this.setYPoint(3, getYPoints()[0] + bpd.getButtonHeight());
        this.getFatherPanel().refresh();
    }

    void ButtonmenuItem3_actionPerformed(ActionEvent e) {
        this.getFatherPanel().disableKeyListening();
        callUpAdiacencesDialog();
        this.getFatherPanel().enableKeyListening();
    }
}

class Button_this_mouseAdapter extends java.awt.event.MouseAdapter {

    ButtonShape adaptee;

    Button_this_mouseAdapter(ButtonShape adaptee) {
        this.adaptee = adaptee;
    }

    public void mousePressed(MouseEvent e) {
        adaptee.mousePressed(e);
    }

    public void mouseEntered(MouseEvent e) {
        adaptee.this_mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        adaptee.this_mouseExited(e);
    }

    public void mouseClicked(MouseEvent e) {
        adaptee.this_mouseClicked(e);
    }
}

class Shape_Button_actionAdapter implements java.awt.event.ActionListener {

    ButtonShape adaptee;

    Shape_Button_actionAdapter(ButtonShape adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.button_actionPerformed(e);
    }
}

class Shape_Button_mouseMotionAdapter extends MouseMotionAdapter {

    ButtonShape adaptee;

    Shape_Button_mouseMotionAdapter(ButtonShape adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseDragged(MouseEvent e) {
        adaptee.this_mouseDragged(e);
    }

    public void mouseMoved(MouseEvent e) {
        adaptee.this_mouseMoved(e);
    }
}

class Button_this_mouseMotionAdapter extends java.awt.event.MouseMotionAdapter {

    ButtonShape adaptee;

    Button_this_mouseMotionAdapter(ButtonShape adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseDragged(MouseEvent e) {
        adaptee.this_mouseDragged(e);
    }

    public void mouseMoved(MouseEvent e) {
        adaptee.this_mouseMoved(e);
    }
}

class Button_menuItem2_actionAdapter implements java.awt.event.ActionListener {

    ButtonShape adaptee;

    Button_menuItem2_actionAdapter(ButtonShape adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ButtonmenuItem2_actionPerformed(e);
    }
}

class Button_menuItem3_actionAdapter implements java.awt.event.ActionListener {

    ButtonShape adaptee;

    Button_menuItem3_actionAdapter(ButtonShape adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.ButtonmenuItem3_actionPerformed(e);
    }
}
