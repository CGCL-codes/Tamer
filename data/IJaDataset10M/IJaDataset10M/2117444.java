package net.sf.coxarcwelder.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.TableModel;

/**
 *
 * @author Noir
 */
public class Mission extends AbstractStoryElement {

    public String getName() {
        return "Mission:" + getValue("Title");
    }

    public void load(CharSequence source) {
        Pattern mishPattern = Pattern.compile("^(.*?)Detail", Pattern.DOTALL);
        Pattern detailPattern = Pattern.compile("Detail\\s*?\\{(.*?)\\}", Pattern.DOTALL);
        Matcher mishMatcher = mishPattern.matcher(source);
        if (mishMatcher.find()) {
            parseParams(mishMatcher.group(1));
        } else {
        }
        Matcher detailMatcher = detailPattern.matcher(source);
        while (detailMatcher.find()) {
            Detail detail = new Detail();
            detail.load(detailMatcher.group(1));
            getChildren().add(detail);
        }
    }

    public CharSequence save() {
        StringBuffer rv = new StringBuffer();
        rv.append("StartMission\r\n");
        rv = new StringBuffer(saveParams(rv, "\t"));
        for (StoryElement curr : getChildren()) {
            rv.append(curr.save());
        }
        rv.append("EndMission\r\n\r\n");
        return rv;
    }
}
