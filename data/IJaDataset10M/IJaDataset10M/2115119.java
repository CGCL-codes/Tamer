package edu.stanford.genetics.treeview.plugin.dendroview;

import java.awt.*;
import java.awt.event.*;
import edu.stanford.genetics.treeview.NatField;

/**
 *  Allows selection of fonts for a FontSelectable
 *
 * @author     Alok Saldanha <alok@genome.stanford.edu>
 * @version $Revision: 1.2 $ $Date: 2008-06-11 01:58:57 $
 */
public class FontSelector extends Panel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final String[] fonts = Toolkit.getDefaultToolkit().getFontList();

    private String title;

    private Choice font_choice;

    private Choice style_choice;

    private NatField size_field;

    private Button display_button;

    private Frame top;

    private Dialog d;

    private FontSelectable client;

    String size_prop, face_prop, style_prop;

    /**
	 *  Place component using gridbaglayout
	 *
	 * @param  gbl     Layout to use
	 * @param  comp    Compnent to layout
	 * @param  x       x coordinate in layout
	 * @param  y       y coordinate in layout
	 * @param  width   width in layout
	 * @param  anchor  anchor direction
	 * @return         GridBagConstraints used
	 */
    private GridBagConstraints place(GridBagLayout gbl, Component comp, int x, int y, int width, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.anchor = anchor;
        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(comp, gbc);
        return gbc;
    }

    /**
	 *  Sets up widgets
	 */
    private void setupWidgets() {
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        Label font_label = new Label("Font:", Label.LEFT);
        add(font_label);
        font_choice = new Choice();
        for (int i = 0; i < fonts.length; ++i) {
            font_choice.addItem(fonts[i]);
        }
        font_choice.select(client.getFace());
        add(font_choice);
        Label style_label = new Label("Style:", Label.LEFT);
        add(style_label);
        style_choice = new Choice();
        for (int i = 0; i < styles.length; ++i) {
            style_choice.addItem(styles[i]);
        }
        style_choice.select(decode_style(client.getStyle()));
        add(style_choice);
        Label size_label = new Label("Size:", Label.LEFT);
        add(size_label);
        size_field = new NatField(client.getPoints(), 3);
        add(size_field);
        display_button = new Button("Display");
        display_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                String string = font_choice.getSelectedItem();
                int i = encode_style(style_choice.getSelectedItem());
                int size = size_field.getNat();
                client.setFace(string);
                client.setStyle(i);
                client.setPoints(size);
            }
        });
        add(display_button);
        place(gbl, font_label, 0, 0, 1, GridBagConstraints.WEST);
        place(gbl, font_choice, 1, 0, 1, GridBagConstraints.EAST);
        place(gbl, style_label, 0, 1, 1, GridBagConstraints.WEST);
        place(gbl, style_choice, 1, 1, 1, GridBagConstraints.EAST);
        place(gbl, size_label, 0, 2, 1, GridBagConstraints.WEST);
        place(gbl, size_field, 1, 2, 1, GridBagConstraints.EAST);
        place(gbl, display_button, 0, 3, 2, GridBagConstraints.WEST);
    }

    /**
	 *  Description of the Field
	 */
    public static final String[] styles = { "Plain", "Italic", "Bold", "Bold Italic" };

    /**
	* turn a style number from class java.awt.Font into a string
	 *
	 * @param  style  style index
	 * @return        string description
	 */
    public static final String decode_style(int style) {
        switch(style) {
            case Font.PLAIN:
                return styles[0];
            case Font.ITALIC:
                return styles[1];
            case Font.BOLD:
                return styles[2];
            default:
                return styles[3];
        }
    }

    /**
	* turn a string into a style number
	 *
	 * @param  style  string description
	 * @return        integer encoded representation
	 */
    public static final int encode_style(String style) {
        return style == styles[0] ? Font.PLAIN : style == styles[1] ? Font.ITALIC : style == styles[2] ? Font.BOLD : Font.BOLD + Font.ITALIC;
    }

    /**
	 *  Constructor for the FontSelector object
	 *
	 * @param  fs    FontSelectable to modify
	 * @param  name  Title for the titlebar
	 */
    public FontSelector(FontSelectable fs, String name) {
        title = name;
        client = fs;
        setupWidgets();
    }

    /**
	 *  Create a toplevel font selecting frame
	 */
    public void makeTop() {
        top = new Frame(getTitle());
        top.add(this);
        top.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                we.getWindow().dispose();
            }
        });
        top.pack();
        top.setVisible(true);
    }

    /**
	 *  Create a blocking font selecting dialog
	 *
	 * @param  f  frame to block
	 */
    public void showDialog(Frame f) {
        d = new Dialog(f, getTitle());
        d.add(this);
        d.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                we.getWindow().dispose();
            }
        });
        d.pack();
        d.setVisible(true);
    }

    /**
	 * @return    The title of this FontSelector
	 */
    protected String getTitle() {
        return title;
    }
}
