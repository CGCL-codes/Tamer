package com.nullfish.app.jfd2.viewer.constraints;

import com.nullfish.app.jfd2.JFDComponent;
import com.nullfish.app.jfd2.ui.container2.ContainerPosition;
import com.nullfish.app.jfd2.viewer.FileViewerConstraints;

/**
 * @author shunji
 *
 */
public class SubConstraints extends FileViewerConstraints {

    public static final String NAME = "sub";

    /**
	 * @param name
	 */
    public SubConstraints() {
        super(NAME);
    }

    /**
	 * 
	 * @param jfd
	 * @return
	 */
    public ContainerPosition getPosition(JFDComponent component) {
        return ContainerPosition.SUB_PANEL;
    }
}
