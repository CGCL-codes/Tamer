package guiStuff.buddyListStuff;

import java.awt.Canvas;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import upperAbstractionLayer.AccountManager;
import abstractionLayer.Buddy;

public class BuddyRendererCreator extends Canvas implements ListCellRenderer {

    private static final long serialVersionUID = 1L;

    protected AccountManager myAM;

    protected boolean highlight;

    protected boolean showMerge;

    public BuddyRendererCreator(AccountManager theAM, boolean highlightSelected, boolean showingMerge) {
        myAM = theAM;
        highlight = highlightSelected;
        showMerge = showingMerge;
    }

    public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3, boolean arg4) {
        return new BuddyRenderer((Buddy) arg1, arg0, (arg3 && highlight), myAM, showMerge);
    }
}
