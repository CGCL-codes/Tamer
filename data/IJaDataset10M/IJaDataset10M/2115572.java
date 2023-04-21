package net.sf.traser.client.scenario;

import javax.swing.JFrame;
import net.sf.traser.client.scanner.IdentifierListener;
import net.sf.traser.client.scanner.IdentifierScanner;
import net.sf.traser.common.ConfigInitializable;
import net.sf.traser.common.ConfigManager;
import org.apache.axiom.om.OMElement;

/**
 *
 * @author szmarcell
 */
public abstract class TFrame extends JFrame implements IdentifierListener, ConfigInitializable {

    /**
     * Scanner that pumps read identifiers.
     */
    private IdentifierScanner scanner;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void dispose() {
        scanner.unRegister(this);
        super.dispose();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void setVisible(boolean b) {
        pack();
        super.setVisible(b);
        scanner.register(this);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void init(OMElement config, ConfigManager manager) {
        scanner = manager.get(IdentifierScanner.class);
    }
}
