package org.openmobster.core.mobileCloud.android.module.sync;

/**
 * 
 * @author openmobster@gmail.com
 *
 */
public final class MapItem {

    /**
	 * 
	 */
    private String source;

    private String target;

    /**
	 * 
	 *
	 */
    public MapItem() {
    }

    /**
	 * 
	 * @return
	 */
    public String getSource() {
        return source;
    }

    /**
	 * 
	 * @param source
	 */
    public void setSource(String source) {
        this.source = source;
    }

    /**
	 * 
	 * @return
	 */
    public String getTarget() {
        return target;
    }

    /**
	 * 
	 * @param target
	 */
    public void setTarget(String target) {
        this.target = target;
    }
}
