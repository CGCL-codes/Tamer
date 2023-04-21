package alice.tucson.service;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LanguageInfo implements Serializable {

    private String languageName;

    private String containerComponentName;

    private String resolverComponentName;

    public LanguageInfo(String languageName) {
        this.languageName = languageName;
    }

    public String getName() {
        return languageName;
    }

    public void setContainerComponentName(String comp) {
        containerComponentName = comp;
    }

    public void setResolverComponentName(String comp) {
        resolverComponentName = comp;
    }

    public String getContainerComponentName() {
        return containerComponentName;
    }

    public String getResolverComponentName() {
        return resolverComponentName;
    }
}
