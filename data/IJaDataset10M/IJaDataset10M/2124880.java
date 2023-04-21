package sun.misc.resources;

/**
 * <p> This class represents the <code>ResourceBundle</code>
 * for sun.misc.
 *
 * @author Michael Colburn
 */
public class Messages_ko extends java.util.ListResourceBundle {

    /**
     * Returns the contents of this <code>ResourceBundle</code>.
     * <p>
     * @return the contents of this <code>ResourceBundle</code>.
     */
    public Object[][] getContents() {
        return contents;
    }

    private static final Object[][] contents = { { "optpkg.versionerror", "오류: {0} JAR 파일에 잘못된 버전 형식이 사용되었습니다. 설명서를 참조하여 지원되는 버전 형식을 확인하십시오." }, { "optpkg.attributeerror", "오류: 필요한 {0} JAR 표시 속성이 {1} JAR 파일에 설정되어 있지 않습니다." }, { "optpkg.attributeserror", "오류: 필요한 JAR 표시 속성 일부가 {0} JAR 파일에 설정되어 있지 않습니다." } };
}
