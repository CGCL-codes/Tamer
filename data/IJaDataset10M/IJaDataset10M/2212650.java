package elliott803.view;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import elliott803.hardware.Store;
import elliott803.view.component.DisplayAddr;
import elliott803.view.component.DisplayCore;
import elliott803.view.component.DisplayWord;

/**
 * A visual representation of the core store.
 *
 * Not really much to see in the store, but to give a view of what's going on we'll display
 * a blocks of 128x64 pixels representing the 8K words of store.  A pixel will be toggled
 * on/off each time a word is written.  The last stored value will also be displayed.
 * 
 * @author Baldwin
 */
public class StoreView extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    Store store;

    DisplayCore coreStore;

    DisplayWord lastValue;

    DisplayAddr lastAddr;

    public StoreView(Store store) {
        super("Core Store", false, false, false, true);
        this.store = store;
        coreStore = new DisplayCore();
        lastAddr = new DisplayAddr("Write");
        lastValue = new DisplayWord(DisplayWord.Type.OCTAL);
        JPanel p1 = new JPanel();
        p1.setBorder(BorderFactory.createTitledBorder("Core"));
        p1.setAlignmentX(LEFT_ALIGNMENT);
        p1.add(coreStore);
        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
        p2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        p2.setAlignmentX(LEFT_ALIGNMENT);
        p2.add(lastAddr);
        p2.add(Box.createHorizontalStrut(5));
        p2.add(lastValue);
        store.setView(this);
        Container content = getContentPane();
        content.add(p1, BorderLayout.CENTER);
        content.add(p2, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    public void updateCore(int addr, long value) {
        if (!isIcon()) {
            coreStore.setValue(addr, value);
            lastAddr.setValue(addr);
            lastValue.setValue(value);
        }
    }

    public void updateCore(long[] store) {
        if (!isIcon()) {
            coreStore.setValues(store);
        }
    }
}
