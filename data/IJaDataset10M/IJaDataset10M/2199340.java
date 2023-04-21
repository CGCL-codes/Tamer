package jreceiver.common.rec.tag;

import jreceiver.common.rec.Rec;

/**
 * An interface describing a comment.
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/12/29 00:44:07 $
 */
public interface Comment extends Rec {

    public static final String HKEY_COMMENT_ID = "COMMENT_ID";

    public static final String HKEY_COMMENT_SUMMARY = "COMMENT_SUMMARY";

    public static final String HKEY_COMMENT_TEXT = "COMMENT_TEXT";

    public String getSummary();

    public String getText();

    public int getId();

    public void setSummary(String comment_summary);

    public void setText(String comment_text);

    public void setId(int comment_id);
}
