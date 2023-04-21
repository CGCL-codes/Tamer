package connex.plugins.filesharing.model;

import net.jxta.share.ContentAdvertisement;
import java.util.Vector;
import connex.app.utils.TableUtils.TableRow;

public class ContentRow extends TableRow {

    protected Vector v = new Vector();

    public ContentRow() {
    }

    public boolean hasMoreAdvertisements() {
        if (v.size() > 1) {
            return true;
        } else {
            return false;
        }
    }

    public ContentAdvertisement getContentAdv() {
        return (ContentAdvertisement) v.firstElement();
    }

    public ContentAdvertisement[] getContentAdvs() {
        ContentAdvertisement[] advs = new ContentAdvertisement[v.size()];
        v.copyInto(advs);
        return advs;
    }

    private void setData() {
        long length;
        String pName = "";
        if (getContentAdv().getMetadata() != null) {
            pName = getContentAdv().getMetadata()[0].getValue().toString();
        }
        this.ID = getContentAdv().getContentId().toString();
        this.add(getContentAdv().getName().toString());
        length = (getContentAdv().getLength() / 1000);
        this.add(length + " KB");
        this.add(getContentAdv().getType() + "");
        this.add(pName + "");
        this.add(v.size() + "");
        this.add(getContentAdv().getDescription() + "");
    }

    public void addAdvertesement(ContentAdvertisement adv) {
        int size = v.size();
        ContentAdvertisement oldContent;
        for (int i = 0; i < size; i++) {
            oldContent = (ContentAdvertisement) v.elementAt(i);
            if ((adv.getContentId().toString().equals(oldContent.getContentId().toString())) && (adv.getAddress().equals(oldContent.getAddress()))) {
                return;
            }
        }
        v.addElement(adv);
        if (v.size() == 1) {
            setData();
        }
        this.set(4, v.size() + "");
    }
}
