package examples.navbar;

import net.sf.sotacs.navbar.api.ItemMode;
import net.sf.sotacs.navbar.api.item.INavBarItem;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.html.BasePage;

public abstract class Forrest extends BasePage {

    public abstract INavBarItem getItem();

    public abstract int getLevel();

    public abstract ItemMode getMode();

    public abstract void setSelected(String sel);

    public String getSpaces() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getLevel(); i++) {
            sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        return sb.append("&nbsp;&nbsp;").toString();
    }

    /**
	 * listens to the DirectLinks
	 */
    public void onClickDirectItem(IRequestCycle cycle, String id) {
        System.out.println("item '" + id + "' was invoked (DirectLink)");
        setSelected(id);
    }
}
