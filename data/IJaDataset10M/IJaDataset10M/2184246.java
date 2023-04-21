package com.bluebrim.gemstone.client;

import java.awt.event.*;
import javax.swing.*;
import com.bluebrim.transact.shared.*;

/**
 * Small extension to CoCommand implementing the one-liner that is needed
 * for a simple transaction pattern that in many cases halves the number
 * of subclasses used. It also avoids creating new instances of them every
 * time they are used.
 *
 * Subclasses of this class are suitable as listeners to menu items,
 * buttons and wherever ActionListeners are used.
 *
 * NOTE: The capitalization of the name is a feature, not a bug!
 *
 * @author Markus Persson 2001-10-19
 */
public abstract class CoTransActionCommand extends CoCommand {

    public CoTransActionCommand(String name) {
        super(name);
    }

    public CoTransActionCommand(String name, Icon icon) {
        super(name, icon);
    }

    /**
 * Execute ourselves in a transaction.
 *
 * This method is final because it is the only method that
 * differentiates this class from its superclass. If you want
 * to override this method, you should subclass CoCommand
 * directly instead.
 *
 * You may think that calling super would be better than
 * repeating the line below somewhere in your sibling
 * subclass. I don't, for among others the following reasons:
 *
 * - Both are one-liners. (Repeating is at most 12 chars longer.)
 * - The line below will not change. Any changes will be in
 *   CoTransactionUtilities.
 * - It is easier to find out what really is happening if you
 *   don't have to traverse a deep class hierarchy.
 * - Repeating is more flexible since non-null targets could
 *   be specified. (Although they are not recommended.)
 *
 * @author Markus Persson 2001-10-19
 */
    public final void actionPerformed(ActionEvent event) {
        CoTransactionUtilities.execute(this);
    }
}
