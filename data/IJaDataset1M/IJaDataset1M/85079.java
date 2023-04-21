package org.smth.search.parser;

import org.smth.search.types.BoardInfo;
import org.smth.search.types.PostItem;

public interface PostItemParser {

    public BoardInfo getPageCount(String content);

    public PostItem[] parsePostItems(String content, boolean withTop);
}
