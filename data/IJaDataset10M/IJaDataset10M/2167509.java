package examples;

import java.util.Vector;
import org.adapit.wctoolkit.models.config.I18N;
import org.adapit.wctoolkit.models.util.exceptions.ApplicationException;
import org.adapit.wctoolkit.uml.classes.kernel.Class;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.AbstractTransformer;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.SpecializationPoint;

public class CodeGenerationFrameTransformer extends AbstractTransformer {

    public Object doTransformation() throws Exception {
        Class inputClass = (Class) getParameter("default");
        SpecializationPoint sp1 = getSpecializationPoint("ClassGenerator");
        try {
            sp1.setValue(inputClass);
            String text = (String) ((Vector) sp1.execute()).get(0);
        } catch (ApplicationException ap) {
            ap.showMessageDialog(I18N.getInstance(), null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
