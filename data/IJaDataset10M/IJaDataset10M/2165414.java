package biz.wolschon.scanner3d;

import java.util.*;
import java.util.logging.Logger;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * (c) 2009 by <a href="http://Wolschon.biz>Wolschon Softwaredesign und Beratung</a>.<br/>
 * Project: AOI3dScannerPlugin<br/>
 * MeshFactory.java<br/>
 * created: 06.06.2009 13:18:58 <br/>
 *<br/><br/>
 * Factory given by a {@link MeshSink} that produces a compatible mesh.
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 */
public interface MeshFactory<T> {

    T createPlanarMesh(final double[][] aDepthMap);
}
