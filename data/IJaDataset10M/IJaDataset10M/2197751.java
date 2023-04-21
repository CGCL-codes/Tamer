package vidis.ui.model.impl.guielements.variableDisplays;

import javax.media.opengl.GL;
import javax.vecmath.Tuple3b;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple3i;
import org.apache.log4j.Logger;
import vidis.data.var.vars.AVariable;
import vidis.util.Rounding;

public class Tuple3Display extends Display {

    private static Logger logger = Logger.getLogger(Tuple3Display.class);

    public Tuple3Display(AVariable v) {
        super(v);
        this.setText("Label");
    }

    private String convertUnknownTupleToString() {
        Object tuple = var.getData();
        if (tuple instanceof Tuple3d) {
            return Rounding.round(((Tuple3d) tuple).x, 3) + "; " + Rounding.round(((Tuple3d) tuple).y, 3) + "; " + Rounding.round(((Tuple3d) tuple).z, 3);
        } else if (tuple instanceof Tuple3b) {
            return ((Tuple3b) tuple).toString();
        } else if (tuple instanceof Tuple3f) {
            return Rounding.round(((Tuple3f) tuple).x, 3) + ", " + Rounding.round(((Tuple3f) tuple).y, 3) + "; " + Rounding.round(((Tuple3f) tuple).z, 3);
        } else if (tuple instanceof Tuple3i) {
            return ((Tuple3i) tuple).toString();
        } else {
            return "ERROR";
        }
    }

    @Override
    public void renderContainer(GL gl) {
        String txt = prefix + var.getIdentifierWithoutNamespace() + " -> (" + convertUnknownTupleToString() + ")";
        this.setText(txt);
        super.renderContainer(gl);
    }
}
