package net.sourceforge.modelintegra.core.generation;

import java.util.Iterator;
import net.sourceforge.modelintegra.core.configuration.ConfigurationHelper;
import net.sourceforge.modelintegra.core.data.DataSingleton;
import net.sourceforge.modelintegra.core.metamodel.extension.util.CustomMIModelValidator;
import net.sourceforge.modelintegra.core.metamodel.extension.util.UCFlowHelper;
import net.sourceforge.modelintegra.core.metamodel.mimodel.UCFlow;
import net.sourceforge.modelintegra.core.metamodel.mimodel.UCTypeEnum;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;

/**
 * 
 */
public class GenerationHelper {

    private static final Logger LOGGER = Logger.getLogger(GenerationHelper.class.getName());

    private static Iterator iterContent;

    public static void run() {
        LOGGER.setLevel(Level.WARN);
        LOGGER.debug("start - GenerationHelper.run()");
        DataSingleton ds = DataSingleton.getInstance();
        LOGGER.debug("Validation");
        CustomMIModelValidator.run();
        LOGGER.debug("generate use-case variants");
        iterContent = ds.getMimodel().eAllContents();
        while (iterContent.hasNext()) {
            EObject eObject = (EObject) iterContent.next();
            if (eObject instanceof UCFlow) {
                UCFlow aUCFlow = (UCFlow) eObject;
                for (int i = 1; i <= 5; i++) {
                    if (UCFlowHelper.getBasicFlow(aUCFlow, i) != null) UCVariantGenerator.generate((UCFlow) eObject, i);
                }
            }
        }
        LOGGER.debug("generate test-cases");
        if (ConfigurationHelper.TESTCASE_GENERATION) {
            iterContent = ds.getMimodel().eAllContents();
            while (iterContent.hasNext()) {
                EObject eObject = (EObject) iterContent.next();
                if (eObject instanceof UCFlow && (((UCFlow) eObject).getUc().getType() == UCTypeEnum.PRIMARY_LITERAL || ((UCFlow) eObject).getUc().getType() == UCTypeEnum.SECONDARY_LITERAL)) {
                    TestCaseGenerator.generate((UCFlow) eObject);
                }
            }
        }
        LOGGER.debug("end - GenerationHelper.run()");
    }
}
