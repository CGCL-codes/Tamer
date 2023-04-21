package jacky.lanlan.song.extension.struts.util.bean;

import jacky.lanlan.song.extension.struts.util.ClassUtils;
import jacky.lanlan.song.extension.struts.util.StringUtils;
import java.beans.PropertyEditorSupport;

/**
 * Property editor for {@link java.lang.Class java.lang.Class}, to enable the direct
 * population of a <code>Class</code> property without recourse to having to use a
 * String class name property as bridge.
 *
 * <p>Also supports "java.lang.String[]"-style array class names, in contrast to the
 * standard {@link Class#forName(String)} method.
 *
 * @author Juergen Hoeller
 * @author Rick Evans
 * @since 13.05.2003
 * @see java.lang.Class#forName
 * @see org.springframework.util.ClassUtils#forName(String, ClassLoader)
 */
public class ClassEditor extends PropertyEditorSupport {

    private final ClassLoader classLoader;

    /**
	 * Create a default ClassEditor, using the thread context ClassLoader.
	 */
    public ClassEditor() {
        this(null);
    }

    /**
	 * Create a default ClassEditor, using the given ClassLoader.
	 * @param classLoader the ClassLoader to use
	 * (or <code>null</code> for the thread context ClassLoader)
	 */
    public ClassEditor(ClassLoader classLoader) {
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }

    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            setValue(ClassUtils.resolveClassName(text.trim(), this.classLoader));
        } else {
            setValue(null);
        }
    }

    public String getAsText() {
        Class<?> clazz = (Class<?>) getValue();
        return (clazz != null) ? ClassUtils.getQualifiedName(clazz) : "";
    }
}
