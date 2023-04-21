package chaman.view;

import chaman.model.Dimention;
import chaman.model.RangeDimention;

/**
 * Vista correspondiente a una {@linkplain chaman.model.RangeDimention dimensi�n de rango}.
 * @author Mart�n Bradaschia
 * @author Gast�n Martini
 */
public class RangeDimentionView extends DimentionView {

    public RangeDimentionView(CubeView parent, Dimention dimention) {
        super(parent, dimention);
    }

    public RangeDimention getRangeDimention() {
        return (RangeDimention) getDimention();
    }
}
