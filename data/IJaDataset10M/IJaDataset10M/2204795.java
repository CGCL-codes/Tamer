package totoCharts;

import java.util.Date;
import com.google.appengine.api.datastore.Key;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class totoGeneralFillout {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private int[] home;

    @Persistent
    private int[] away;

    @Persistent
    private int[] draw;

    @Persistent
    private Date date;

    public totoGeneralFillout() {
        home = new int[16];
        away = new int[16];
        draw = new int[16];
        this.setDate(new Date());
    }

    public int[] getHome() {
        return this.home;
    }

    public int[] getDraw() {
        return this.draw;
    }

    public int[] getAway() {
        return this.away;
    }

    public void updateHome(boolean flag, int row) {
        if (flag) home[row]++; else home[row]--;
    }

    public void homeReplace(boolean flag, int[] array) {
        home = array;
    }

    public void drawReplace(boolean flag, int[] array) {
        draw = array;
    }

    public void awayReplace(boolean flag, int[] array) {
        away = array;
    }

    public String toString() {
        String result = "";
        int[] home = getHome();
        int[] draw = getDraw();
        int[] away = getAway();
        for (int i = 0; i < 15; i++) {
            result += home[i];
            result += '#';
            result += draw[i];
            result += '#';
            result += away[i];
            result += '#';
        }
        result += home[15];
        result += '#';
        result += draw[15];
        result += '#';
        result += away[15];
        return result;
    }

    public Key getKey() {
        return key;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
