package gov.nasa.gsfc.visbard.gui.categoryview;

import gov.nasa.gsfc.visbard.repository.category.Category;
import gov.nasa.gsfc.visbard.repository.category.CategoryListener;
import gov.nasa.gsfc.visbard.repository.category.ColorPalette;
import gov.nasa.gsfc.visbard.repository.category.ColorPaletteFactory;
import gov.nasa.gsfc.visbard.repository.category.DefaultColorPalette;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

/**
**/
public class PaletteView extends JPanel implements CategoryListener {

    private static ColorPalette[] sPalettes;

    static {
        sPalettes = new ColorPalette[4];
        sPalettes[0] = ColorPaletteFactory.getInstance().createROYGBIVPalette();
        sPalettes[1] = ColorPaletteFactory.getInstance().createColorPalette(Color.red, Color.yellow);
        sPalettes[2] = ColorPaletteFactory.getInstance().createColorPalette(Color.blue, Color.cyan);
        sPalettes[3] = ColorPaletteFactory.getInstance().createColorPalette(Color.darkGray, Color.white);
    }

    private Category fCategory = null;

    private PalettePainterPanel fLegend;

    private JComboBox fPalettes;

    private JButton fCustomColorStart;

    private JButton fCustomColorFinish;

    private Color fColorStart = Color.black;

    private Color fColorFinish = Color.white;

    private JLabel fCustomRangeLabel;

    private ActionListener fComboListener;

    static org.apache.log4j.Category sLogger = org.apache.log4j.Category.getInstance(PaletteView.class.getName());

    /**
     * constructor
    **/
    public PaletteView(Category category) {
        super();
        fCategory = category;
        initGUI();
        addListeners();
        synchronizeWithCurrentPalette();
        fCategory.addListener(this);
    }

    private void initGUI() {
        this.removeAll();
        TitledBorder brd = new TitledBorder(new EtchedBorder(), fCategory.getType().getName());
        brd.setTitleJustification(TitledBorder.CENTER);
        setBorder(brd);
        setLayout(new GridBagLayout());
        fLegend = new PalettePainterPanel(fCategory.getColorPalette());
        fPalettes = new JComboBox(sPalettes);
        fPalettes.addItem(" Custom");
        fPalettes.setOpaque(true);
        fPalettes.setRenderer(new PaletteRenderer(fPalettes));
        fPalettes.setMaximumRowCount(10);
        fPalettes.setPreferredSize(new Dimension(100, 25));
        fCustomColorStart = new JButton(new ColorBox(null));
        fCustomColorStart.setFocusPainted(false);
        fCustomColorStart.setPreferredSize(new Dimension(20, 20));
        fCustomColorFinish = new JButton(new ColorBox(null));
        fCustomColorFinish.setFocusPainted(false);
        fCustomColorFinish.setPreferredSize(new Dimension(20, 20));
        add(fLegend, new GridBagConstraints(0, 0, 4, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(fPalettes, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        fCustomRangeLabel = new JLabel("Custom Range : ");
        add(fCustomRangeLabel, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 0), 0, 0));
        add(fCustomColorStart, new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        add(fCustomColorFinish, new GridBagConstraints(3, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.setMinimumSize(new Dimension(210, 95));
        this.setPreferredSize(new Dimension(210, 95));
    }

    private void addListeners() {
        fComboListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (fPalettes.getSelectedItem() instanceof String) {
                    fCategory.setColorPalette(ColorPaletteFactory.getInstance().createColorPalette(fColorStart, fColorFinish));
                } else {
                    ColorPalette p = (ColorPalette) fPalettes.getSelectedItem();
                    fCategory.setColorPalette(p);
                }
            }
        };
        fPalettes.addActionListener(fComboListener);
        fCustomColorStart.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color col = JColorChooser.showDialog(getParent(), "Select Palette Color", fColorStart);
                if (col != null) {
                    fColorStart = col;
                    fColorFinish = col;
                    fCategory.setColorPalette(ColorPaletteFactory.getInstance().createColorPalette(fColorStart, fColorFinish));
                }
            }
        });
        fCustomColorFinish.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color col = JColorChooser.showDialog(getParent(), "Select Palette Color", fColorStart);
                if (col != null) {
                    fColorFinish = col;
                    if (fColorStart == null) fColorStart = col;
                    fCategory.setColorPalette(ColorPaletteFactory.getInstance().createColorPalette(fColorStart, fColorFinish));
                }
            }
        });
    }

    class PaletteRenderer extends JPanel implements ListCellRenderer {

        private PalettePainterPanel fPPal;

        private JComboBox fCombo;

        public PaletteRenderer(JComboBox combo) {
            super(new GridBagLayout());
            fCombo = combo;
            fPPal = new PalettePainterPanel(null);
            this.add(fPPal, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));
        }

        public void setBounds(int x, int y, int w, int h) {
            super.setBounds(x, y, w, h);
            validate();
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof String) {
                JLabel lbl = new JLabel((String) value);
                lbl.setOpaque(true);
                if (isSelected) {
                    lbl.setBackground(list.getSelectionBackground());
                    lbl.setForeground(list.getSelectionForeground());
                } else {
                    lbl.setBackground(list.getBackground());
                    lbl.setForeground(list.getForeground());
                }
                return lbl;
            }
            if (isSelected) {
                setBackground(list.getSelectionBackground());
            } else {
                setBackground(list.getBackground());
            }
            fPPal.setColorPalette((ColorPalette) value);
            return this;
        }
    }

    class ColorBox implements Icon {

        Color fCol;

        public ColorBox(Color col) {
            fCol = col;
        }

        public int getIconWidth() {
            return 10;
        }

        public int getIconHeight() {
            return 10;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            if (fCol != null) {
                g.setColor(Color.black);
                g.fillRect(x, y, getIconWidth() - 1, getIconHeight() - 1);
                g.setColor(fCol);
                g.fillRect(x + 2, y + 2, getIconWidth() - 4 - 1, getIconHeight() - 4 - 1);
            }
        }
    }

    /**
     * An event indicating something about the category has changed.
     */
    public void categoryChanged(Category source, int eventID) {
        if (eventID == CategoryListener.PALETTE_CHANGED) {
            sLogger.debug("Updating palette view due to category change.");
            synchronizeWithCurrentPalette();
        }
    }

    private void synchronizeWithCurrentPalette() {
        ColorPalette pal = fCategory.getColorPalette();
        loop: for (int i = 0; i < sPalettes.length; i++) {
            if (sPalettes[i].equals(pal)) {
                fLegend.setColorPalette(pal);
                fCustomColorFinish.setEnabled(false);
                fCustomColorStart.setEnabled(false);
                fCustomRangeLabel.setEnabled(false);
                fCustomColorStart.setIcon(new ColorBox(null));
                fCustomColorFinish.setIcon(new ColorBox(null));
                fPalettes.removeActionListener(fComboListener);
                fPalettes.setSelectedIndex(i);
                fPalettes.addActionListener(fComboListener);
                PaletteView.this.revalidate();
                fLegend.repaint();
                return;
            }
        }
        if (pal instanceof DefaultColorPalette) {
            DefaultColorPalette dpal = (DefaultColorPalette) pal;
            fColorStart = dpal.getStartColor();
            fColorFinish = dpal.getEndColor();
            fLegend.setColorPalette(dpal);
            fCustomColorFinish.setEnabled(true);
            fCustomColorStart.setEnabled(true);
            fCustomRangeLabel.setEnabled(true);
            fCustomColorFinish.setIcon(new ColorBox(fColorFinish));
            fCustomColorStart.setIcon(new ColorBox(fColorStart));
            fPalettes.removeActionListener(fComboListener);
            fPalettes.setSelectedItem(" Custom");
            fPalettes.addActionListener(fComboListener);
            PaletteView.this.revalidate();
            fLegend.repaint();
            return;
        }
        sLogger.error("Failed to display the specified palette");
    }

    /**
     * Unregister as listener and free up ram. Called before removing.
     */
    public void dispose() {
        fCategory.removeListener(this);
    }
}
