package ch.laoe.plugin;

import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import ch.laoe.ui.LProgressViewer;

/***********************************************************

This file is part of LAoE.

LAoE is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published
by the Free Software Foundation; either version 2 of the License,
or (at your option) any later version.

LAoE is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with LAoE; if not, write to the Free Software Foundation,
Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


Class:			GPUndo
Autor:			olivier g�umann, neuch�tel (switzerland)
JDK:				1.3

Desctription:	plugin to perform one undo-step.

History:
Date:			Description:									Autor:
03.01.02		first draft										oli4

***********************************************************/
public class GPUndo extends GPlugin {

    public GPUndo(GPluginHandler ph) {
        super(ph);
    }

    public String getName() {
        return "undo";
    }

    public JMenuItem createMenuItem() {
        return super.createMenuItem(KeyEvent.VK_Z);
    }

    public void start() {
        super.start();
        try {
            LProgressViewer.getInstance().entrySubProgress(getName());
            getFocussedClip().getHistory().undo(getFocussedClip());
            updateFrameTitle();
            LProgressViewer.getInstance().exitSubProgress();
            reloadFocussedClipEditor();
        } catch (NullPointerException e) {
        }
    }
}
