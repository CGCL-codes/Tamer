package com.liferay.portlet.themegallery.util;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.ReleaseInfo;
import com.liferay.portlet.themegallery.model.ThemeEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * <a href="ThemeGalleryUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ThemeGalleryUtil {

    public static String[] getCategories() {
        return _instance._categories;
    }

    public static List getEntries(String categoryId) {
        List entries = null;
        if (Validator.isNotNull(categoryId)) {
            entries = (List) _instance._entriesByCategory.get(categoryId);
        }
        if (entries == null) {
            entries = (List) _instance._entriesByCategory.get("ALL");
        }
        return entries;
    }

    public static List getEntries(String categoryId, int begin, int end) {
        List entries = getEntries(categoryId);
        if (end > entries.size()) {
            end = entries.size();
        }
        return entries.subList(begin, end);
    }

    public static int getEntriesSize(String categoryId) {
        List entries = getEntries(categoryId);
        return entries.size();
    }

    private ThemeGalleryUtil() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            SAXReader reader = new SAXReader();
            Document doc = reader.read(classLoader.getResourceAsStream("com/liferay/portlet/themegallery/dependencies/gallery.xml"));
            Element root = doc.getRootElement();
            _categories = StringUtil.split(root.elementText("available-categories"));
            for (int i = 0; i < _categories.length; i++) {
                _entriesByCategory.put(_categories[i], new ArrayList());
            }
            List allEntries = new ArrayList();
            _entriesByCategory.put("ALL", allEntries);
            Iterator itr = root.elements("entry").iterator();
            while (itr.hasNext()) {
                Element entryEl = (Element) itr.next();
                String download = entryEl.elementText("download");
                String version = ReleaseInfo.getVersion();
                version = StringUtil.replace(version, " ", "");
                version = version.toLowerCase();
                download = StringUtil.replace(download, ".war", "-" + version + ".war");
                ThemeEntry entry = new ThemeEntry(entryEl.attributeValue("name"), entryEl.elementText("description"), download, entryEl.elementText("image-large"), entryEl.elementText("image-medium"), entryEl.elementText("image-small"));
                String[] categories = StringUtil.split(entryEl.elementText("categories"));
                for (int i = 0; i < categories.length; i++) {
                    List entries = (List) _entriesByCategory.get(categories[i]);
                    entries.add(entry);
                }
                allEntries.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ThemeGalleryUtil _instance = new ThemeGalleryUtil();

    private String[] _categories = null;

    private Map _entriesByCategory = new HashMap();
}
