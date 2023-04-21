package org.granite.generator.as3;

import java.io.File;
import org.granite.generator.Input;
import org.granite.generator.as3.reflect.JavaType;

/**
 * @author Franck WOLFF
 */
public class JavaAs3Input implements Input<Class<?>> {

    private final Class<?> type;

    private final File file;

    private JavaType javaType = null;

    public JavaAs3Input(Class<?> type, File file) {
        this.type = type;
        this.file = file;
    }

    public Class<?> getType() {
        return type;
    }

    public String getDescription() {
        return type.getName();
    }

    public File getFile() {
        return file;
    }

    public JavaType getJavaType() {
        return javaType;
    }

    public void setJavaType(JavaType javaType) {
        this.javaType = javaType;
    }
}
