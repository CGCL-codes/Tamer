package org.eclipse.ui;

import nill.NullInterface;
import org.eclipse.jface.viewers.ISelection;

public interface ISelectionService extends NullInterface {

    ISelection getSelection();
}
