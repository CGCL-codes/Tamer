package org.soqqo.vannitator.support;

import java.io.Serializable;
import org.soqqo.vannitator.annotation.VannitateManyToOne;

public class ManyToOneNameHandler extends AbstractNameHandler implements Serializable, NameHandler<VannitateManyToOne> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private VannitateManyToOne conf;

    public ManyToOneNameHandler(VannitateManyToOne conf) {
        this.conf = conf;
    }

    @Override
    public String getNewPackageName(String currentPackageName) {
        return currentPackageName;
    }

    @Override
    public String getNewSimpleClassName(String currentClassName) {
        return "".equals(conf.newClassName()) ? currentClassName : conf.newClassName();
    }

    @Override
    public String getNewQualifiedName(String currentClassName, String currentPackageName) {
        if ("".equals(currentPackageName)) {
            return getNewSimpleClassName(currentClassName);
        } else {
            return getNewPackageName(currentPackageName) + "." + getNewSimpleClassName(currentClassName);
        }
    }
}
