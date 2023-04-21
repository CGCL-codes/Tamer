package org.tn5250j.encoding;

/**
 * Just a workaround to make {@link ToolboxCodePage} methods public available.
 * 
 * @author maki
 */
public class ToolboxCodePageProvider {

    /**
	 * Just a workaround to make {@link ToolboxCodePage#getCodePage(String)} methods public available.
	 * 
	 * @param encoding
	 * @return
	 */
    public static final CodePage getCodePage(String encoding) {
        return ToolboxCodePage.getCodePage(encoding);
    }
}
