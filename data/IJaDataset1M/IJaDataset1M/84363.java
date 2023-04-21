package org.dmd.features.extgwt.extended;

import org.dmd.features.extgwt.generated.dmw.MvcBackgroundMenuDMW;

public class MvcBackgroundMenu extends MvcBackgroundMenuDMW {

    @Override
    public String getDefaultImport() {
        return ("org.dmd.features.extgwt.client.util.menu.DefaultBackgroundMenu");
    }

    @Override
    public String getDefaultClass() {
        return ("DefaultBackgroundMenu");
    }

    @Override
    public String getInstantiation() {
        if (isCustomRender()) {
            return (null);
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("        " + getVariableName() + " = new " + getDefaultClass() + "(");
            sb.append("\"" + getName() + "\"");
            sb.append(");\n");
            return (sb.toString());
        }
    }
}
