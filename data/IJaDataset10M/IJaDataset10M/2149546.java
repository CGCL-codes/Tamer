package net.webassembletool.wicket.container;

import net.webassembletool.wicket.utils.WATNullResponse;
import org.apache.wicket.Response;

/**
 * A container for default content on error. It encloses a block which will be
 * displayed in the target content is not available.
 * 
 * <p>
 * Usage :
 * </p>
 * 
 * <p>
 * Page :
 * </p>
 * <code>
 * WATBlock block1  = new WATBlock( "block", "url/of/content" );<br/>
 * WATError error = new WATParam( "defaultError" );<br/>
 * 
 * block1.add( error);
 * </code>
 * 
 * <p>
 * HTML :
 * </p>
 * <code>
 * 
 * &lt;div wicket:id='block1'&gt;<br/>
 *   &lt;div wicket:id='defaultError&gt;<br/>
 *    Block content. Can be pure html or other wicket components.<br/>
 *   &lt;/div&gt;<br/>
 *  
 *  &lt;/div&gt;<br/>
 *  </code>
 * 
 * @author Nicolas Richeton
 * @see WATTemplate
 * 
 */
public class WATError extends AbstractWatBufferedContainer {

    /**
	 * Serialization ID.
	 */
    private static final long serialVersionUID = 1L;

    private final Integer errorCode;

    /**
	 * Create a parameter block
	 * 
	 * @param id
	 *            - Must be unique in the page
	 * @param name
	 *            - Name of the block in the template.
	 */
    public WATError(String id, Integer errorCode) {
        super(id);
        this.errorCode = errorCode;
    }

    private String getBlockName() {
        if (errorCode == null) {
            return AbstractWatDriverContainer.WAT_ERROR_PREFIX;
        }
        return AbstractWatDriverContainer.WAT_ERROR_PREFIX + errorCode;
    }

    @Override
    protected void process(String content) {
        Response r = getResponse();
        if (r instanceof WATNullResponse) {
            WATNullResponse watResponse = (WATNullResponse) r;
            watResponse.getBlocks().put(getBlockName(), content);
        } else {
            throw new RuntimeException("WATError can only be used within a AbstractWatDriverContainer.");
        }
    }
}
