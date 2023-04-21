package uk.icat.cmd.custom.create;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import uk.icat3.client.EntityBaseBean;

public class EntityBaseBeansExtractor {

    private static final String UK_ICAT3_CLIENT_PACKAGE = "uk.icat3.client";

    public static List<String> getSimpleNames() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(EntityBaseBean.class));
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(UK_ICAT3_CLIENT_PACKAGE);
        List<String> result = new ArrayList<String>();
        for (BeanDefinition bd : candidateComponents) {
            result.add(getSimpleName(bd.getBeanClassName()));
        }
        return result;
    }

    private static String getSimpleName(String name) {
        int lastIndexOf = name.lastIndexOf('.');
        return name.substring(lastIndexOf + 1);
    }
}
