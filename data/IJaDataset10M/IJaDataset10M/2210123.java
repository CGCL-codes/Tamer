package net.sf.evemsp.gui.chr;

import java.util.Date;
import net.sf.evemsp.data.AccRecord;
import net.sf.evemsp.data.ChrRecord;
import net.sf.evemsp.data.EveRecordManager;
import net.sf.evemsp.data.io.ChrSupport;
import net.sf.evemsp.gui.EveCanvas;
import net.sf.evemsp.gui.Resources;
import net.sf.evemsp.gui.menu.BinaryPageItem;
import net.sf.evemsp.gui.menu.ItemAction;
import net.sf.evemsp.gui.menu.MenuPage;
import net.sf.evemsp.gui.menu.PageItem;

public class ChrUpdateItem extends BinaryPageItem implements ItemAction {

    private ChrRecord record;

    public ChrUpdateItem(ChrRecord record) {
        super(Resources.get("chrs.load"), EveCanvas.IMG_DOWNLOAD, EveCanvas.IMG_WAIT, null);
        this.record = record;
        setAction(this);
    }

    public boolean isEnabled() {
        Date now = new Date();
        return record.getCachedUntil() == null || record.getCachedUntil().getTime() <= now.getTime();
    }

    public void itemAction(MenuPage page, PageItem item) {
        ChrSupport chrSupport = new ChrSupport();
        AccRecord[] accs = EveRecordManager.getInstance().getAccounts();
        for (int i = 0; i < accs.length; i++) {
            if (accs[i].getId() == record.getAccId()) {
                chrSupport.updateCharacter(record, accs[i]);
                EveRecordManager.getInstance().store(record);
                addedToPage();
            }
        }
    }

    void recordUpdated(ChrRecord record) {
        addedToPage();
    }

    public boolean getBoolean() {
        return isEnabled();
    }

    public void setBoolean(boolean value) {
        addedToPage();
    }
}
