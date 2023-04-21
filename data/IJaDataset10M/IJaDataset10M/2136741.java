package ghm.follow.font;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * FontSelectionPanel v1.1, copied without functional modification from <a
 * href="http://gregmerrill.imagineis.com/fontSelectionPanel"
 * >http://gregmerrill.imagineis.com/fontSelectionPanel</a>.
 */
public class FontSelectionPanel extends JPanel {

    /**
	 * Like {@link #FontSelectionPanel(java.awt.Font)}, except an initialFont of <code>null</code>
	 * will be used.
	 */
    public FontSelectionPanel() {
        this(null);
    }

    /**
	 * Like {@link #FontSelectionPanel(java.awt.Font, String[], int[])}, except that a default list
	 * of styles (<code>{"Plain", "Bold", "Italic", "Bold Italic"}</code>) and font sizes (<code>{8, 9, 10, 12, 14}</code>)
	 * will be used.
	 * 
	 * @param initialFont
	 *            see {@link #FontSelectionPanel(java.awt.Font, String[], int[])}
	 */
    public FontSelectionPanel(Font initialFont) {
        this(initialFont, new String[] { "Plain", "Bold", "Italic", "Bold Italic" }, new int[] { 8, 9, 10, 12, 14 });
    }

    /**
	 * Construct a new FontSelectionPanel whose family, style & size widget selections are set
	 * according to the supplied initial Font. Additionally, the style & size values available will
	 * be dictated by the values in styleDisplayNames and predefinedSizes, respectively.
	 * 
	 * @param initialFont
	 *            the newly constructed FontSelectionPanel's family, style, and size widgets will be
	 *            set according to this value. This value may be null, in which case an initial font
	 *            will be automatically created. This auto-created font will have a family, style,
	 *            and size corresponding to the first avaiable value in the widget form family,
	 *            style, and size respectively.
	 * @param styleDisplayNames
	 *            must contain exactly four members. The members of this array represent the
	 *            following styles, in order: Font.PLAIN, Font.BOLD, Font.ITALIC, and
	 *            Font.BOLD+Font.ITALIC
	 * @param predefinedSizes
	 *            must contain one or more predefined font sizes which will be available to the user
	 *            as a convenience for populating the font size text field; all values must be
	 *            greater than 0.
	 */
    public FontSelectionPanel(Font initialFont, String[] styleDisplayNames, int[] predefinedSizes) {
        super(new GridBagLayout());
        this.setBorder(new EmptyBorder(12, 12, 11, 11));
        GridBagConstraints gbc = new GridBagConstraints();
        String[] availableFontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        if (initialFont == null) {
            initialFont = new Font(availableFontFamilyNames[0], Font.PLAIN, predefinedSizes[0]);
        }
        fontFamilyList = new JList(availableFontFamilyNames);
        fontFamilyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontFamilyList.setVisibleRowCount(8);
        ListSelectionListener phraseCanvasUpdater = new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    observable.setChanged();
                    observable.notifyObservers();
                }
            }
        };
        fontFamilyList.addListSelectionListener(phraseCanvasUpdater);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 2;
        this.add(new JScrollPane(fontFamilyList), gbc);
        fontStyleList = new FontStyleList(styleDisplayNames);
        fontStyleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontStyleList.setVisibleRowCount(4);
        fontStyleList.addListSelectionListener(phraseCanvasUpdater);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        this.add(new JScrollPane(fontStyleList), gbc);
        fontSize = new JTextField();
        fontSize.setHorizontalAlignment(JTextField.RIGHT);
        fontSize.setColumns(4);
        gbc.gridx = 2;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(fontSize, gbc);
        fontSizeList = new JList(validateAndConvertPredefinedSizes(predefinedSizes));
        fontSizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontSizeList.setVisibleRowCount(1);
        fontSizeList.setCellRenderer(new ListCellRenderer());
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        this.add(new JScrollPane(fontSizeList), gbc);
        phraseCanvas = new PhraseCanvas(initialFont.getFamily(), initialFont, Color.black);
        addObserver(new Observer() {

            public void update(Observable o, Object arg) {
                try {
                    phraseCanvas.setPhrase((String) fontFamilyList.getSelectedValue());
                    phraseCanvas.setFont(FontSelectionPanel.this.getSelectedFont());
                } catch (InvalidFontException e) {
                    phraseCanvas.setPhrase("");
                }
                phraseCanvas.invalidate();
                phraseCanvas.repaint();
            }
        });
        phraseCanvas.setSize((int) this.getPreferredSize().getWidth(), 100);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(new JScrollPane(phraseCanvas), gbc);
        FontSizeSynchronizer fontSizeSynchronizer = new FontSizeSynchronizer(fontSizeList, fontSize);
        fontSizeList.addListSelectionListener(fontSizeSynchronizer);
        fontSize.getDocument().addDocumentListener(fontSizeSynchronizer);
        fontFamilyList.setSelectedValue(initialFont.getFamily(), true);
        fontStyleList.setSelectedStyle(initialFont.getStyle());
        fontSize.setText(String.valueOf(initialFont.getSize()));
    }

    /** JList for font family */
    protected JList fontFamilyList;

    /** FontStlyeList (subclass of JList) for font style */
    protected FontStyleList fontStyleList;

    /** JTextField for font size */
    protected JTextField fontSize;

    /** JList for font size */
    protected JList fontSizeList;

    /** PhraseCanvas in which font samples are displayed */
    protected PhraseCanvas phraseCanvas;

    /**
	 * @exception IllegalArgumentException
	 *                thrown if
	 *                <ul>
	 *                <li>predefinedSizes does not contain one or more integer values
	 *                <li>predefinedSizes contains any integers with a value of less than 1
	 *                </ul>
	 */
    private Integer[] validateAndConvertPredefinedSizes(int[] predefinedSizes) {
        if (predefinedSizes == null) {
            throw new IllegalArgumentException("int[] predefinedSizes may not be null");
        }
        if (predefinedSizes.length < 1) {
            throw new IllegalArgumentException("int[] predefinedSizes must contain one or more values");
        }
        Integer[] predefinedSizeIntegers = new Integer[predefinedSizes.length];
        for (int i = 0; i < predefinedSizes.length; i++) {
            if (predefinedSizes[i] < 1) {
                throw new IllegalArgumentException("int[] predefinedSizes may not contain integers with value less than 1");
            }
            predefinedSizeIntegers[i] = new Integer(predefinedSizes[i]);
        }
        return predefinedSizeIntegers;
    }

    /**
	 * Adds an Observer to this FontSelectionPanel; the supplied Observer will have its update()
	 * method called any time the Font currently specified in the FontSelectionPanel changes. (The
	 * <tt>arg</tt> supplied to the Observer will be <tt>null</tt>.)
	 * 
	 * @param o
	 *            observer to be added
	 * @see java.util.Observer
	 */
    public void addObserver(Observer o) {
        observable.addObserver(o);
    }

    /**
	 * Removes an Observer from this FontSelectionPanel.
	 * 
	 * @param o
	 *            Observer to be removed
	 * @see java.util.Observer
	 */
    public void deleteObserver(Observer o) {
        observable.deleteObserver(o);
    }

    /** Observable used for registering/notifying Observers */
    protected PublicChangeObservable observable = new PublicChangeObservable();

    /**
	 * Returns the currently selected font family
	 * 
	 * @return currently selected font family
	 * @exception NoFontFamilySelectedException
	 *                thrown if no font family is currently selected
	 */
    public String getSelectedFontFamily() throws NoFontFamilySelectedException {
        String fontFamily = (String) fontFamilyList.getSelectedValue();
        if (fontFamily == null) {
            throw new NoFontFamilySelectedException("No font family is currently selected");
        }
        return fontFamily;
    }

    /**
	 * Returns the currently selected font style.
	 * 
	 * @return currently selected font style. This value will correspond to one of the font styles
	 *         specified in {@link java.awt.Font}
	 * @exception NoFontStyleSelectedException
	 *                thrown if no font style is currently selected
	 */
    public int getSelectedFontStyle() throws NoFontStyleSelectedException {
        return fontStyleList.getSelectedStyle();
    }

    /**
	 * Returns the currently selected font size.
	 * 
	 * @return currently selected font size.
	 * @exception NoFontSizeSpecifiedException
	 *                thrown if no font size is currently specified
	 * @exception InvalidFontSizeException
	 *                thrown if the font size currently specified is invalid
	 */
    public int getSelectedFontSize() throws NoFontSizeSpecifiedException, InvalidFontSizeException {
        String fontSizeText = fontSize.getText();
        if ((fontSize == null) || (fontSize.equals(""))) {
            throw new NoFontSizeSpecifiedException("No font size specified");
        }
        if (fontSizeText.length() > maxNumCharsInFontSize_) {
            throw new InvalidFontSizeException("Too many characters in font size");
        }
        try {
            return Integer.parseInt(fontSizeText);
        } catch (NumberFormatException e) {
            throw new InvalidFontSizeException("The number specified in the font size text field (" + fontSize.getText() + ") is not a valid integer.");
        }
    }

    /**
	 * Returns the currently selected font.
	 * 
	 * @return currently selected font.
	 * @exception InvalidFontException
	 *                thrown if no valid font is currently specified; the actual class of the
	 *                exception thrown may be {@link InvalidFontException},
	 *                {@link NoFontFamilySelectedException}, {@link NoFontStyleSelectedException},
	 *                {@link NoFontSizeSpecifiedException}, or {@link InvalidFontSizeException}
	 */
    public Font getSelectedFont() throws InvalidFontException {
        return new Font(getSelectedFontFamily(), getSelectedFontStyle(), getSelectedFontSize());
    }

    /**
	 * Changes the currently selected font by assigning all widget values to match the
	 * family/style/size values of the supplied font
	 * 
	 * @param font
	 *            font whose values should be used to set widgets
	 * @exception IllegalArgumentException
	 *                thrown if the family or style of the font supplied are not available or
	 *                invalid
	 */
    public void setSelectedFont(Font font) {
        setSelectedFontFamily(font.getFamily());
        setSelectedFontStyle(font.getStyle());
        setSelectedFontSize(font.getSize());
    }

    /**
	 * Sets the currently selected font family.
	 * 
	 * @param family
	 *            family to which selection should change
	 * @exception IllegalArgumentException
	 *                thrown if the supplied font family is not among the list of available font
	 *                families
	 */
    public void setSelectedFontFamily(String family) {
        ListModel familyListModel = fontFamilyList.getModel();
        for (int i = 0; i < familyListModel.getSize(); i++) {
            String listEntry = String.valueOf(familyListModel.getElementAt(i));
            if (listEntry.equalsIgnoreCase(family)) {
                fontFamilyList.setSelectedValue(listEntry, true);
                return;
            }
        }
        throw new IllegalArgumentException("The font family supplied, '" + family + "', is not in the list of availalbe " + "font families.");
    }

    /**
	 * Sets the currently selected font style.
	 * 
	 * @param style
	 *            style to which selection should change
	 * @exception IllegalArgumentException
	 *                thrown if the supplied font style is not one of Font.PLAIN, Font.BOLD,
	 *                Font.ITALIC, or Font.BOLD+Font.ITALIC
	 */
    public void setSelectedFontStyle(int style) {
        fontStyleList.setSelectedStyle(style);
    }

    /**
	 * Sets the currently selected font size.
	 * 
	 * @param size
	 *            size to which selection should change
	 */
    public void setSelectedFontSize(int size) {
        fontSize.setText(String.valueOf(size));
    }

    /** Maximum number of characters permissibile in a valid font size */
    protected int maxNumCharsInFontSize_ = 3;

    /**
	 * This class synchronizes font size value between the list containing available font sizes &
	 * the text field in which font size is ultimately specified.
	 */
    protected class FontSizeSynchronizer implements DocumentListener, ListSelectionListener {

        /**
		 * @param list
		 *            list containing predefined font sizes
		 * @param textField
		 *            text field in which font size is specified
		 */
        public FontSizeSynchronizer(JList list, JTextField textField) {
            list_ = list;
            textField_ = textField;
        }

        /** @see javax.swing.event.ListSelectionListener */
        public void valueChanged(ListSelectionEvent e) {
            if (updating_) {
                return;
            }
            updating_ = true;
            if (!e.getValueIsAdjusting()) {
                Object selectedValue = ((JList) e.getSource()).getSelectedValue();
                if (selectedValue != null) {
                    textField_.setText(selectedValue.toString());
                }
                observable.setChanged();
                observable.notifyObservers();
            }
            updating_ = false;
        }

        /** @see javax.swing.event.DocumentListener */
        public void changedUpdate(DocumentEvent e) {
            handle(e);
        }

        /** @see javax.swing.event.DocumentListener */
        public void insertUpdate(DocumentEvent e) {
            handle(e);
        }

        /** @see javax.swing.event.DocumentListener */
        public void removeUpdate(DocumentEvent e) {
            handle(e);
        }

        /** Handles all DocumentEvents */
        protected void handle(DocumentEvent e) {
            if (updating_) {
                return;
            }
            updating_ = true;
            try {
                Integer currentFontSizeInteger = Integer.valueOf(textField_.getText());
                boolean currentSizeWasInList = false;
                Object listMember;
                for (int i = 0; i < list_.getModel().getSize(); i++) {
                    listMember = list_.getModel().getElementAt(i);
                    if (listMember.equals(currentFontSizeInteger)) {
                        list_.setSelectedValue(currentFontSizeInteger, true);
                        currentSizeWasInList = true;
                        break;
                    }
                }
                if (!currentSizeWasInList) {
                    list_.clearSelection();
                }
            } catch (NumberFormatException nfe) {
                list_.clearSelection();
            }
            observable.setChanged();
            observable.notifyObservers();
            updating_ = false;
        }

        protected JList list_;

        protected JTextField textField_;

        protected boolean updating_;
    }

    /**
	 * Represents a list of the four font styles: plain, bold, italic, and bold italic
	 */
    protected static class FontStyleList extends JList {

        /**
		 * Construct a new FontStyleList, using the supplied values for style display names
		 * 
		 * @param styleDisplayNames
		 *            must contain exactly four members. The members of this array represent the
		 *            following styles, in order: Font.PLAIN, Font.BOLD, Font.ITALIC, and
		 *            Font.BOLD+Font.ITALIC
		 * @exception IllegalArgumentException
		 *                thrown if styleDisplayNames does not contain exactly four String values
		 */
        public FontStyleList(String[] styleDisplayNames) {
            super(validateStyleDisplayNames(styleDisplayNames));
        }

        private static String[] validateStyleDisplayNames(String[] styleDisplayNames) {
            if (styleDisplayNames == null) {
                throw new IllegalArgumentException("String[] styleDisplayNames may not be null");
            }
            if (styleDisplayNames.length != 4) {
                throw new IllegalArgumentException("String[] styleDisplayNames must have a length of 4");
            }
            for (int i = 0; i < styleDisplayNames.length; i++) {
                if (styleDisplayNames[i] == null) {
                    throw new IllegalArgumentException("No member of String[] styleDisplayNames may be null");
                }
            }
            return styleDisplayNames;
        }

        /**
		 * @return currently selected font style
		 * @exception NoFontStyleSelectedException
		 *                thrown if no font style is currently selected
		 */
        public int getSelectedStyle() throws NoFontStyleSelectedException {
            switch(this.getSelectedIndex()) {
                case 0:
                    return Font.PLAIN;
                case 1:
                    return Font.BOLD;
                case 2:
                    return Font.ITALIC;
                case 3:
                    return Font.BOLD + Font.ITALIC;
                default:
                    throw new NoFontStyleSelectedException("No font style is currently selected");
            }
        }

        /**
		 * Change the currently selected style in this FontStyleList
		 * 
		 * @param style
		 *            new selected style for this FontStyleList
		 * @exception IllegalArgumentException
		 *                thrown if style is not one of Font.PLAIN, Font.BOLD, Font.ITALIC, or
		 *                Font.BOLD+Font.ITALIC
		 */
        public void setSelectedStyle(int style) {
            switch(style) {
                case Font.PLAIN:
                    this.setSelectedIndex(0);
                    break;
                case Font.BOLD:
                    this.setSelectedIndex(1);
                    break;
                case Font.ITALIC:
                    this.setSelectedIndex(2);
                    break;
                case Font.BOLD + Font.ITALIC:
                    this.setSelectedIndex(3);
                    break;
                default:
                    throw new IllegalArgumentException("int style must come from java.awt.Font");
            }
        }
    }

    /**
	 * An implementation of {@link javax.swing.ListCellRenderer} which right justifies all cells.
	 */
    protected static class ListCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setHorizontalAlignment(JLabel.RIGHT);
            return label;
        }
    }

    /**
	 * Subclass of {@link java.util.Observable} which allows <tt>public</tt> access to the
	 * setChanged() method.
	 */
    protected static class PublicChangeObservable extends Observable {

        /** @see java.util.Observable#setChanged() */
        public void setChanged() {
            super.setChanged();
        }
    }

    /**
	 * Component for displaying a "phrase" (a brief, one or two word String) using a particular font &
	 * a particular color.
	 */
    static class PhraseCanvas extends Canvas {

        /**
		 * Constructs a new PhraseCanvas with the supplied phrase, font, and color.
		 * 
		 * @param phrase
		 *            phrase to be displayed in this PhraseCanvas
		 * @param font
		 *            Font to use when rendering the phrase
		 * @param color
		 *            Color to use when rendering the phrase
		 */
        public PhraseCanvas(String phrase, Font font, Color color) {
            phrase_ = phrase;
            font_ = font;
            color_ = color;
        }

        /** @see java.awt.Canvas#paint(java.awt.Graphics) */
        public void paint(Graphics g) {
            Font dummyFont = new Font(font_.getFamily(), font_.getStyle(), font_.getSize() + 1);
            dummyFont.createGlyphVector(new FontRenderContext(null, antialiasOn_, false), phrase_);
            GlyphVector glyphVector = font_.createGlyphVector(new FontRenderContext(null, antialiasOn_, false), phrase_);
            Rectangle2D logicalBounds = glyphVector.getLogicalBounds();
            double x;
            if (logicalBounds.getWidth() < this.getWidth()) {
                x = (this.getWidth() / 2) - (logicalBounds.getWidth() / 2);
            } else {
                x = 0;
            }
            double y;
            if (logicalBounds.getHeight() < this.getHeight()) {
                y = (this.getHeight() / 2) + (logicalBounds.getHeight() / 2);
            } else {
                y = this.getHeight();
            }
            g.setColor(color_);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawGlyphVector(glyphVector, (float) x, (float) y);
        }

        /**
		 * Returns the phrase to be rendered by this PhraseCanvas.
		 * 
		 * @return phrase to be rendered by this PhraseCanvas
		 */
        public String getPhrase() {
            return phrase_;
        }

        /**
		 * Sets the phrase to be rendered by this PhraseCanvas.
		 * 
		 * @param phrase
		 *            new phrase to be rendered by this PhraseCanvas; this new value will be
		 *            rendered the next time {@link #paint(java.awt.Graphics)} is called
		 */
        public void setPhrase(String phrase) {
            phrase_ = phrase;
        }

        protected String phrase_;

        /**
		 * Returns the font to use when rendering the phrase.
		 * 
		 * @return font to use when rendering the phrase
		 */
        public Font getFont() {
            return font_;
        }

        /**
		 * Sets the font to use when rendering the phrase.
		 * 
		 * @param font
		 *            new font to use when rendering the phrase; this new value will be used to
		 *            render the phrase the next time {@link #paint(java.awt.Graphics)} is called
		 */
        public void setFont(Font font) {
            font_ = font;
        }

        protected Font font_;

        /**
		 * Returns the color to use when rendering the phrase.
		 * 
		 * @return color to use when rendering the phrase
		 */
        public Color getColor() {
            return color_;
        }

        /**
		 * Sets the color to use when rendering the phrase.
		 * 
		 * @param color
		 *            new color to use when rendering the phrase; this new value will be used to
		 *            render the phrase the next time {@link #paint(java.awt.Graphics)} is called
		 */
        public void setColor(Color color) {
            color_ = color;
        }

        protected Color color_;

        /**
		 * Returns true iff anti-aliasing is used when rendering the phrase.
		 * 
		 * @return whether or not anti-aliasing is used when rendering the phrase
		 */
        public boolean isAntialiasOn() {
            return antialiasOn_;
        }

        /**
		 * Turn anti-aliasing on or off.
		 * 
		 * @param antialiasOn
		 *            whether or not to use anti-aliasing when rendering the phrase this new value
		 *            will be used to render the phrase the next time
		 *            {@link #paint(java.awt.Graphics)} is called
		 */
        public void setAntialiasOn(boolean antialiasOn) {
            antialiasOn_ = antialiasOn;
        }

        protected boolean antialiasOn_;
    }
}
