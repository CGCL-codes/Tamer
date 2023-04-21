package net.community.apps.tools.commenter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * <P>Copyright as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jun 25, 2009 2:11:15 PM
 */
public interface Commenter {

    void addComment(Reader r, Writer w, String cmnt, boolean isCommentFile) throws IOException;
}
