package feedReader;

import mobi.ilabs.common.MIDletContext;
import mobi.ilabs.common.Debug;
import java.util.Enumeration;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import mobi.ilabs.feed.Feed;
import mobi.ilabs.feed.FeedList;

/**
* FeedReader sample application.
* <p>
* @author �ystein Myhre
*/
public class FeedListScreen extends List implements Debug.Constants {

    private static final Command exitCommand = new Command("Exit", Command.EXIT, 1);

    private static final Command backCommand = new Command("Back", Command.BACK, 2);

    private Displayable prev;

    public FeedListScreen(final FeedList feedList) {
        super("My Feeds", List.IMPLICIT);
        if (DEBUG) Debug.println("New BlogListScreen");
        this.addCommand(exitCommand);
        this.addCommand(backCommand);
        this.setCommandListener(new CommandListener() {

            public void commandAction(Command c, Displayable s) {
                if (c == exitCommand) {
                    MIDletContext.exit();
                } else if (c == backCommand) {
                    MIDletContext.setCurrent(prev);
                } else if (c == SELECT_COMMAND) {
                    if (DEBUG) Debug.println("BlogListScreen.SELECT_COMMAND: " + c);
                    String title = getString(getSelectedIndex());
                    Feed blog = feedList.getFeed(title);
                    FeedEntryListScreen entryListScreen = new FeedEntryListScreen(blog);
                    entryListScreen.show();
                }
            }
        });
        for (Enumeration e = feedList.elements(); e.hasMoreElements(); ) {
            Feed blog = (Feed) e.nextElement();
            this.append(blog.getTitle(), null);
        }
    }

    public void show() {
        prev = MIDletContext.setCurrent(this);
    }
}
