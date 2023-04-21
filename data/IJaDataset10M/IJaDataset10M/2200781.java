package com.mockturtlesolutions.snifflib.util;

import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;

/**
Interface for functions meant to operate on the data of quadtrees.
*/
public interface QuadTreeFun {

    /**
	This method should take a Quad's XData and YData and return
	a DblMatrix the same size as the YData.
	*/
    public DblMatrix quadEval(DblMatrix[] XData, DblMatrix YData);
}
