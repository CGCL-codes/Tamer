package mandelbrot.components;

import java.util.HashMap;
import org.apache.log4j.Logger;
import org.procol.framework.components.AbstractComponent;
import org.procol.framework.components.exceptions.ComponentInvocationException;
import lights.exceptions.TupleSpaceException;
import lights.interfaces.ITuple;
import lights.interfaces.ITupleSpace;
import lights.space.Field;
import lights.space.Tuple;
import lights.utils.ObjectTuple;
import mandelbrot.dataobjects.Complex;
import mandelbrot.dataobjects.Image;

public class Renderer extends AbstractComponent {

    private static final int MAX_ITER = 128;

    private Image image;

    public Renderer(HashMap<String, ITupleSpace> spaces) {
        super(spaces);
    }

    @Override
    public Boolean call() throws ComponentInvocationException {
        ITuple template = new Tuple().add(new Field().setType(Integer.class)).add(new Field().setType(Complex.class)).add(new Field().setType(Complex.class)).add(new Field().setType(Integer[][].class)).add(new Field().setValue(false));
        ObjectTuple objtuple = null;
        try {
            objtuple = (ObjectTuple) spaces.get("Space").in(template);
        } catch (TupleSpaceException e1) {
            e1.printStackTrace();
        }
        this.image = (Image) objtuple.getObject();
        renderImage();
        try {
            spaces.get("Space").out(this.image.toTuple());
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void renderImage() {
        Complex c;
        for (int row = 0; row < this.image.getHeight(); row++) {
            for (int col = 0; col < this.image.getWidth(); col++) {
                c = this.image.getPoint(row, col);
                this.image.setLevel(row, col, calculateMandelbrot(c));
            }
        }
        this.image.setIsRendered(true);
    }

    private int calculateMandelbrot(Complex c) {
        Complex zi = c;
        for (int i = 1; i < MAX_ITER; i++) {
            if (zi.magnitude() < 2) {
                zi = zi.mul(zi).add(c);
            } else {
                return i;
            }
        }
        return 0;
    }
}
