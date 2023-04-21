package simplex3d.math.floatx;

import java.io.Serializable;
import simplex3d.math.types.*;

/**
 * @author Aleksey Nikiforov (lex)
 */
abstract class ProtectedMat3f<P> extends AnyMat3<P> implements Serializable {

    public static final long serialVersionUID = 8104346712419693669L;

    float p00;

    float p01;

    float p02;

    float p10;

    float p11;

    float p12;

    float p20;

    float p21;

    float p22;
}
