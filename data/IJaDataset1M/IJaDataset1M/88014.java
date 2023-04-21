package sun.misc.resources;

/**
 * <p> This class represents the <code>ResourceBundle</code>
 * for sun.misc.
 *
 * @author Michael Colburn
 */
public class Messages_zh_CN extends java.util.ListResourceBundle {

    /**
     * Returns the contents of this <code>ResourceBundle</code>.
     * <p>
     * @return the contents of this <code>ResourceBundle</code>.
     */
    public Object[][] getContents() {
        return contents;
    }

    private static final Object[][] contents = { { "optpkg.versionerror", "错误：{0} JAR 文件中使用的版本格式无效。请检查文档以了解支持的版本格式。" }, { "optpkg.attributeerror", "错误：必要的 {0} JAR 标明属性未在 {1} JAR 文件中设置。" }, { "optpkg.attributeserror", "错误：某些必要的 JAR 标明属性未在 {0} JAR 文件中设置。" } };
}
