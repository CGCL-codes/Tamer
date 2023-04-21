package net.community.apps.tools.commenter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * <P>Copyright as per GPLv2</P>
 *
 * <P>Used to comment all Java/C/C++/C-Sharp files
 * @author Lyor G.
 * @since Jun 25, 2009 2:10:00 PM
 */
public class ProgramFileCommenter extends BaseCommenter {

    public ProgramFileCommenter() {
        super();
    }

    @Override
    public void addComment(Reader r, Writer w, String cmnt, boolean isCommentFile) throws IOException {
        w.append("/*\r\n");
        appendComment(" * ", w, cmnt, isCommentFile);
        w.append(" */\r\n\r\n");
        appendContent(r, w);
    }
}
