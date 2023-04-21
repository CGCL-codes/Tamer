package es.prodevelop.cit.gvsig.arcims.gui.panels.utils;

import javax.swing.JToolTip;

/**
 * This class allows nice multiline tool tips.
 *
 * @author Zafir Anjum
 * @author jldominguez
 */
public class JMultilineToolTip extends JToolTip {

    private static final long serialVersionUID = 0;

    String tipText;

    protected int columns = 0;

    protected int fixedwidth = 0;

    public JMultilineToolTip() {
        updateUI();
    }

    public void updateUI() {
        setUI(MultilineToolTipUI.createUI(this));
    }

    public void setColumns(int columns) {
        this.columns = columns;
        this.fixedwidth = 0;
    }

    public int getColumns() {
        return columns;
    }

    public void setFixedWidth(int width) {
        this.fixedwidth = width;
        this.columns = 0;
    }

    public int getFixedWidth() {
        return fixedwidth;
    }
}
