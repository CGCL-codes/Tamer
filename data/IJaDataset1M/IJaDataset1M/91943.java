package net.sf.xpontus.codecompletion.xml;

import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yves Zoundi
 */
public interface ICompletionParser {

    public void init(List tagList, Map nsTagListMap);

    public void updateCompletionInfo(String pubid, String sysid, Reader in);
}
