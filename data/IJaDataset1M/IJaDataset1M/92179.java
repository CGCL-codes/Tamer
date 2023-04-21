package de.consolewars.api.parser;

import de.consolewars.api.data.Comment;

/**
 * 
 * @author cerpin (arrewk@gmail.com)
 *
 */
public class SAXCommentParser extends AbstractSAXParser<Comment> {

    private boolean isComment = true;

    public SAXCommentParser(String APIURL) {
        super(APIURL);
    }

    @Override
    protected Comment createTempItem() {
        return new Comment();
    }

    @Override
    protected boolean isValidItem() {
        return isComment;
    }

    @Override
    protected void parseItem(String uri, String localName, String qName) {
        if (qName.equals("navi")) {
        } else if (qName.equals("currpage")) {
            getTempItem().setCurrpage(Integer.parseInt(tempValue));
        } else if (qName.equals("pagecount")) {
            getTempItem().setPagecount(Integer.parseInt(tempValue));
        } else if (qName.equals("mode")) {
            if (tempValue.equalsIgnoreCase("comment")) {
                isComment = true;
                getTempItem().setMode(tempValue);
            } else {
                isComment = false;
            }
        } else if (qName.equals("unixtime")) {
            getTempItem().setUnixtime(Integer.parseInt(tempValue));
        } else if (qName.equals("statement")) {
            getTempItem().setStatement(tempValue);
        } else if (qName.equals("quote")) {
            getTempItem().setQuote(tempValue);
        } else if (qName.equals("username")) {
            getTempItem().setUsername(tempValue);
        } else if (qName.equals("uid")) {
            getTempItem().setUid(Integer.parseInt(tempValue));
        } else if (qName.equals("usertitle")) {
            getTempItem().setUsertitle(tempValue);
        } else if (qName.equals("commentcount")) {
            getTempItem().setCommentcount(Integer.parseInt(tempValue));
        } else if (qName.equals("postcount")) {
            getTempItem().setPostcount(Integer.parseInt(tempValue));
        } else if (qName.equals("cid")) {
            getTempItem().setCid(Integer.parseInt(tempValue));
        }
    }
}
