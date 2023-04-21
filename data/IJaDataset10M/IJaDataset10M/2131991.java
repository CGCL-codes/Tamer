package net.sf.xpontus.modules.gui.components;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

/**
 * A drop down list with history support
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class MemoryComboBox extends JComboBox {

    /**
         *
         */
    private static final long serialVersionUID = -2709128554628262310L;

    public static int NO_MAX = -1;

    private int _maxMemoryCount = NO_MAX;

    /**
     *
     */
    public MemoryComboBox() {
        this(NO_MAX);
    }

    /**
     * @param maxMemoryCount
     */
    public MemoryComboBox(int maxMemoryCount) {
        super();
        setMaxMemoryCount(maxMemoryCount);
    }

    /**
     * @param value
     */
    public void setMaxMemoryCount(int value) {
        _maxMemoryCount = (value > NO_MAX) ? value : NO_MAX;
    }

    public void addItem(Object item) {
        if (item != null) {
            if (!item.toString().trim().equals("")) {
                removeItem(item);
                insertItemAt(item, 0);
                setSelectedIndex(0);
                if ((_maxMemoryCount > NO_MAX) && (getItemCount() > _maxMemoryCount)) {
                    removeItemAt(getItemCount() - 1);
                }
            }
        }
    }

    public void insertItemAt(Object anObject, int index) {
        super.insertItemAt(anObject, index);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                while ((_maxMemoryCount > NO_MAX) && (getItemCount() > _maxMemoryCount)) {
                    removeItemAt(0);
                }
            }
        });
    }
}
