package org.eclipse.tptp.models.web.common.test.data.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tptp.models.web.common.test.data.CriterionType;
import org.eclipse.tptp.models.web.common.test.data.factory.ICriterionFactory;
import org.eclipse.tptp.test.tools.web.lib.utils.WebTESTBasicStaticUtils;

public class CriterionFactoryProvider {

    protected static Map criterionTypesImpl = new LinkedHashMap();

    protected static CriterionType[] criterionTypes;

    static {
        List configElements = new ArrayList(2);
        IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(WebTESTBasicStaticUtils.MODEL_PLUGIN_ID, "criterion_type").getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            IConfigurationElement[] configures = extensions[i].getConfigurationElements();
            configElements.addAll(Arrays.asList(configures));
        }
        criterionTypes = new CriterionType[configElements.size()];
        for (int i = 0; i < configElements.size(); i++) {
            try {
                ICriterionFactory factory = (ICriterionFactory) ((IConfigurationElement) configElements.get(i)).createExecutableExtension("class");
                criterionTypes[i] = factory.getCriterionType();
                criterionTypesImpl.put(criterionTypes[i], factory);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        Arrays.sort(criterionTypes, CriterionType.compCriterionType);
        int a = 0;
    }

    public static ICriterionFactory getCriterionFactory(CriterionType type) {
        return (ICriterionFactory) criterionTypesImpl.get(type);
    }

    public static CriterionType[] getCriterionTypes() {
        return criterionTypes;
    }

    public static int getCriterionTypePos(String criterionTypeName) {
        for (int i = 0; i < criterionTypes.length; i++) {
            if (criterionTypes[i].name.equals(criterionTypeName)) return i;
        }
        return -1;
    }
}
