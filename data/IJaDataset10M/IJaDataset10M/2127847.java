package javax.media.ding3d.utils.scenegraph.io.state.javax.media.ding3d;

import java.io.*;
import javax.media.ding3d.AmbientLight;
import javax.media.ding3d.utils.scenegraph.io.retained.Controller;
import javax.media.ding3d.utils.scenegraph.io.retained.SymbolTableData;

public class AmbientLightState extends LightState {

    public AmbientLightState(SymbolTableData symbol, Controller control) {
        super(symbol, control);
    }

    protected javax.media.ding3d.SceneGraphObject createNode() {
        return new AmbientLight();
    }
}
