package net.sf.refactorit.commonIDE;

import net.sf.refactorit.ui.module.ActionProxy;
import javax.swing.KeyStroke;

/**
 * @author tonis
 */
public interface ShortcutAction extends ActionProxy {

    KeyStroke getKeyStroke();
}
