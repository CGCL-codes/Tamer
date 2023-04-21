package org.sekomintory.xmlrpc.query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.io.FileUtils;
import org.sekomintory.ApplicationContext;
import org.sekomintory.xmlrpc.SearchResultEntry;

/**
 * @author Liang ZHANG
 * 
 */
public class QueryHandler {

    public ArrayList<ArrayList<Object>> getTime(String projectId, String filePath, String codeFragment) {
        ArrayList<SearchResultEntry> searchResult = ApplicationContext.getInstance().getSearchResult(projectId, filePath);
        ArrayList<ArrayList<Object>> contributionList = new ArrayList<ArrayList<Object>>();
        try {
            for (SearchResultEntry searchResultEntry : searchResult) {
                String sourceFileContent = FileUtils.readFileToString(new File(searchResultEntry.getSourceFilePath()), "UTF-8");
                int startIndex = sourceFileContent.indexOf(codeFragment);
                if (startIndex != -1) {
                    String metaFileContent = FileUtils.readFileToString(new File(searchResultEntry.getMetaFilePath()), "UTF-8");
                    String[] timeArray = metaFileContent.split(" ");
                    ArrayList<String> timeFragment = new ArrayList<String>();
                    for (int i = 0, length = codeFragment.length(); i < length; i++) {
                        timeFragment.add(timeArray[startIndex + i]);
                    }
                    contributionList.add(new ArrayList<Object>(Arrays.asList(searchResultEntry.getContributor(), timeFragment)));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contributionList;
    }
}
