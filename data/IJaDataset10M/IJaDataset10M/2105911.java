package org.eclipse.epsilon.fptc.simulation.includes;

import java.io.File;
import org.eclipse.epsilon.commons.util.FileUtil;

public final class Includes {

    private Includes() {
    }

    private static File get(String name) {
        return FileUtil.getFile(name, Includes.class);
    }

    public static File getCloning() {
        return get("Cloning.eol");
    }

    public static File getCrossProduct() {
        return get("CrossProduct.eol");
    }

    public static File getTransform() {
        return get("Transform.eol");
    }

    public static File getUnification() {
        return get("Unification.eol");
    }

    public static File getExpressionSelection() {
        return get("ExpressionSelection.eol");
    }

    public static File getExpressionApplication() {
        return get("ExpressionApplication.eol");
    }
}
