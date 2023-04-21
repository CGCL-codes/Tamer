package com.ixora.common.ui;

/**
 * @author Daniel Moraru
 */
public interface AppView {

    /**
	 * Invoked after the view has been instantiated.
	 * More complex work should be perform here rather than in the constructor.
	 * @throws Exception
	 */
    void initialize() throws Exception;

    /**
	 * Invoked to close the view.
     * @return true to veto the close
	 */
    boolean close();
}
