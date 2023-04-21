package de.hackerdan.projectcreator.common;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Util f�r Selektionen.
 * 
 * @author Daniel Hirscher
 */
public final class SelectionUtil {

    private SelectionUtil() {
    }

    /**
    * Liefert das erste Element der Selektion oder <code>null</code> wenn
    * nichts drin is.
    * 
    * @param selection Selektion
    * @return erstes Objekt oder <code>null</code>
    */
    public static Object getFirstElement(final ISelection selection) {
        final Object result;
        if ((selection instanceof IStructuredSelection) && (!selection.isEmpty())) {
            result = ((IStructuredSelection) selection).getFirstElement();
        } else {
            result = null;
        }
        return result;
    }
}
