package org.one.stone.soup.wiki;

import java.util.ArrayList;
import java.util.List;
import org.one.stone.soup.entity.KeyValuePair;
import org.one.stone.soup.wiki.file.manager.FileManagerInterface;

public class WikiLinkListPage extends WikiListPage {

    public WikiLinkListPage(FileManagerInterface fileManager, String pageName) {
        super(fileManager, pageName);
    }

    public void loadPagesList() {
        try {
            String[][] list = WikiDataHelper.getPageAsList(getFileManager().getPageSourceAsString(getPageName(), getFileManager().getSystemLogin()));
            List<KeyValuePair> newList = new ArrayList<KeyValuePair>();
            for (int loop = 0; loop < list.length; loop++) {
                String key = list[loop][0];
                String value = list[loop][1];
                KeyValuePair kvp = new KeyValuePair(key, value);
                newList.add(kvp);
            }
            setList(newList);
            setTimeStamp(getFileManager().getPageTimeStamp(getPageName(), getFileManager().getSystemLogin()));
        } catch (Exception e) {
        }
    }
}
