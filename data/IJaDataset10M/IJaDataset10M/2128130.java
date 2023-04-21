package org.jmlnitrate.transformation.inbound;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmlnitrate.transformation.Transformation;
import org.jmlnitrate.transformation.TransformationFactory;
import org.jmlnitrate.util.CacheManager;
import org.jmlnitrate.util.JMLNitratePlant;

/**
 * This {@link TransformationFactory} is responsible for creating a {@link
 * Transformation} the can handle a {@link InboundHandler} that contains a
 * {@link javax.servlet.http.HttpServletInbound} representation.
 * <br>
 * It can take 1..* parameters.  the mandatory pparameter is:
 * <ul>
 *     <li>key - transfomer CONSTANT
 *     <li>value - FQN of the transformer to use
 * </ul>
 *
 * @author Arthur Copeland
 * @version $Revision: 3 $
 *
 */
public class HttpServletInboundTransformationFactory extends TransformationFactory {

    /**
     * Holds the logger
     */
    private static final Log logger = LogFactory.getLog(HttpServletInboundTransformationFactory.class);

    /**
     * Transformer Param Key
     */
    private static final String KEY = "transformer";

    /**
     * Default Ctor. Friendly for security.
     */
    public HttpServletInboundTransformationFactory() {
        super();
    }

    /**
     * Creates and return the {@link Transformation} that can handle the
     * request.
     *
     * @return the Transformation
     * @throws Exception if an error happens creating the {@link
     * Transformation}.
     */
    public Transformation getTransformation() throws Exception {
        Transformation transformation = null;
        JMLNitratePlant plant = (JMLNitratePlant) getParameter();
        String factoryName = (String) (((List) plant.getParam(KEY)).get(0));
        Class clazz = CacheManager.getClass(factoryName);
        transformation = (Transformation) clazz.newInstance();
        if (plant.getParamCount() > 1) {
            HashMap params = new HashMap();
            Iterator pit = plant.getParamKeys();
            while (pit.hasNext()) {
                Object pkey = pit.next();
                if (pkey != null) {
                    Object pvalue = plant.getParam(pkey);
                    params.put(pkey, pvalue);
                }
            }
            transformation.setParameters(params);
        }
        return transformation;
    }
}
