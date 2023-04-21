package net.sf.ipn.data;

import java.util.List;
import org.objectstyle.cayenne.access.DataContext;
import org.objectstyle.cayenne.exp.ExpressionFactory;
import org.objectstyle.cayenne.query.SelectQuery;

public class PrayerItem extends net.sf.ipn.data.auto._PrayerItem {

    public static PrayerItem withId(Long id, DataContext dc) {
        PrayerItem temp = null;
        SelectQuery query = new SelectQuery(PrayerItem.class, ExpressionFactory.matchDbExp("ID", id));
        List results = dc.performQuery(query);
        if (results.size() > 0) {
            temp = (PrayerItem) results.get(0);
        }
        return temp;
    }

    public String getStatusCode() {
        PrayerItemStatus status = this.getStatus();
        if (status == null) return null;
        return status.getCode();
    }

    public void setStatusCode(String code) {
        if (code == null) throw new IllegalArgumentException("code arg cannot be null.");
        PrayerItemStatus status = PrayerItemStatus.withCode(code, this.getDataContext());
        if (status == null) throw new IllegalArgumentException("PrayerItemStatus with code '" + code + "' not found.");
        this.setStatus(status);
    }

    public String getTypeCode() {
        PrayerItemType temp = this.getType();
        if (temp == null) return null;
        return temp.getCode();
    }

    public void setTypeCode(String code) {
        if (code == null) throw new IllegalArgumentException("code arg cannot be null.");
        PrayerItemType temp = PrayerItemType.withCode(code, this.getDataContext());
        if (temp == null) throw new IllegalArgumentException("PrayerItemStatus with code '" + code + "' not found.");
        this.setType(temp);
    }

    public String getLangCode() {
        Lang temp = this.getLang();
        if (temp == null) return null;
        return temp.getCode();
    }

    public void setLangCode(String code) {
        if (code == null) throw new IllegalArgumentException("code arg cannot be null.");
        Lang temp = Lang.withCode(code, this.getDataContext());
        if (temp == null) throw new IllegalArgumentException("Lang with code '" + code + "' not found.");
        this.setLang(temp);
    }

    public void setPrayerGroupId(Long id) {
        PrayerGroup g = PrayerGroup.withId(id, this.getDataContext());
        if (g == null) throw new IllegalArgumentException("prayerGroupId does not exist: " + id);
        this.setPrayerGroup(g);
    }

    public Number getId() {
        return (Number) this.getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
    }
}
