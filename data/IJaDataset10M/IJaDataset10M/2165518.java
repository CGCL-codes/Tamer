package org.tolven.wiki.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Holds Utility Methods required for Tolven Wiki.
 * 
 * @author Anil
 *
 */
public class WikiUtil {

    /**
	 * Parse the content into multiple lines and Sort the lines.
	 * 
	 * @param content
	 * @return
	 */
    public static String sortContentByLine(String content) {
        StringBuffer sortedContent = new StringBuffer();
        StringReader reader = new StringReader(content);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        List<String> contentTobeSorted = new LinkedList<String>();
        try {
            while ((line = bufferedReader.readLine()) != null) {
                contentTobeSorted.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(contentTobeSorted);
        for (String sortedLine : contentTobeSorted) {
            sortedContent.append(sortedLine);
            sortedContent.append("\n");
        }
        return sortedContent.toString().trim();
    }

    public static void main(String args[]) {
        StringBuffer content = new StringBuffer();
        content.append("{{Concept (SCT)|conceptId=396519009|term=10 or more mitotic figures per 10 high power field}}\n");
        content.append("{{Relation (SCT)|relationshipId=116680003|relationship=Is a (SCT)|concept2=Finding of histologic grading differentiation AND/OR behavior (SCT 373369003)}}\n");
        content.append("{{Relation (SCT)|relationshipId=418775008|relationship=Finding method (SCT)|concept2=Procedure (SCT 71388002)}}\n");
        content.append("{{Relation (SCT)|relationshipId=363714003|relationship=Interprets (SCT)|concept2=Laboratory test (SCT 15220000)}}\n");
        content.append("{{Axis (SCT)|Clinical finding}}");
        WikiUtil.sortContentByLine(content.toString());
    }
}
