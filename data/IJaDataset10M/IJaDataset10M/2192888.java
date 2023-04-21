package de.xmlsicherheit.cheatsheets;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;

/**
 * <p>Cheat Sheet action. Opens the preferences of the current workspace.</p>
 *
 * <p>This plug-in is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.</p>
 *
 * <p>This plug-in is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.</p>
 *
 * <p>You should have received a copy of the GNU Lesser General Public License along
 * with this library;<br>
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA</p>
 *
 * @author Dominik Schadow (info@xml-sicherheit.de), www.xml-sicherheit.de
 * @version 1.6.1, 03.10.2006
 */
public class OpenPreferences extends Action implements ICheatSheetAction {

    /**
	 * Constructor.
	 */
    public OpenPreferences() {
    }

    /**
	 * Opens the preferences dialog.
	 *
	 * @param params param attributes
	 * @param manager The Cheat Sheet manager
	 */
    public void run(String[] params, ICheatSheetManager manager) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        PreferenceManager pm = workbench.getPreferenceManager();
        if (pm != null) {
            PreferenceDialog d = new PreferenceDialog(workbench.getActiveWorkbenchWindow().getShell(), pm);
            d.create();
            d.open();
        }
    }
}
