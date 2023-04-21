package org.wizard4j.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository of wizard repositories, this allows to use different
 * implementations of wizardRepositories in a single application.
 */
public class WizardRepositoryRepository {

    private static WizardRepositoryRepository instance = null;

    private Map<String, WizardRepository> repositoryMap = new HashMap<String, WizardRepository>();

    public static synchronized WizardRepositoryRepository getInstance() {
        if (instance == null) {
            instance = new WizardRepositoryRepository();
        }
        return instance;
    }

    public void addWizardRepository(String wizardRepositoryName, WizardRepository wizardRepository) {
        repositoryMap.put(wizardRepositoryName, wizardRepository);
    }

    public WizardRepository getWizardRepository(String wizardRepositoryName) {
        return repositoryMap.get(wizardRepositoryName);
    }

    public String toRepositoriesXml() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        buffer.append("<repositories>\n");
        for (String key : repositoryMap.keySet()) {
            buffer.append("  <repository name=\"").append(key).append("\">\n");
            buffer.append("  </repository>\n");
        }
        buffer.append("</repositories>\n");
        return buffer.toString();
    }
}
