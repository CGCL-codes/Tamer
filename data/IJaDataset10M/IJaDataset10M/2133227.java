package net.sf.tacos.demo.partial;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

public abstract class Partial extends BasePage {

    public static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);

    public List getItems() {
        Set countries = new HashSet();
        Locale[] locales = Locale.getAvailableLocales();
        List items = new ArrayList(locales.length);
        for (int i = 0; i < locales.length; i++) {
            Locale l = locales[i];
            if (!"".equals(l.getDisplayCountry()) && !countries.contains(l.getCountry())) {
                items.add(l);
                countries.add(l.getCountry());
            }
        }
        Collections.sort(items, new Comparator() {

            public int compare(Object o1, Object o2) {
                Locale l1 = (Locale) o1;
                Locale l2 = (Locale) o2;
                return l1.getDisplayCountry().compareTo(l2.getCountry());
            }
        });
        return items;
    }

    public void toggleItem(IRequestCycle cycle) {
        String item = (String) cycle.getServiceParameters()[0];
        Set s = getSelectedItems();
        if (s.contains(item)) {
            s.remove(item);
        } else {
            s.add(item);
        }
        setSelectedItems(s);
    }

    public boolean isCurrSelected() {
        return getSelectedItems().contains(getCurrItem().getCountry());
    }

    public abstract Locale getCurrItem();

    public abstract Set getSelectedItems();

    public abstract void setSelectedItems(Set selectedItems);
}
