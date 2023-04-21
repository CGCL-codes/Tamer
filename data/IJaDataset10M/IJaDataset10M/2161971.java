package org.tolk.ipico.process.node.impl;

import org.tolk.ipico.util.TagVo;

/**
 * Class to format an output strings based on content in a TagVo value object and for 
 * each fwdToAllNextNodes call in the TtoFilter
 * 
 * @author Werner van Rensburg
 */
public interface TtoDataFormatDecorator extends DataFormatDecorator {

    /**
	 * Create a formatted output string when the handleFirstSeenTag() is called. 
	 *  
	 * @param tagVo
	 * @return formatted output string
	 */
    String getFormattedString_FirstSeen(TagVo tagVo);

    /**
     * Create a formatted output string when the generateLsString() is called. 
     *  
     * @param tagVo
     * @return formatted output string
     */
    String getFormattedString_LastSeen(TagVo tagVo);
}
