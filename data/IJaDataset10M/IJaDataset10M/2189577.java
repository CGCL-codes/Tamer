package ar.com.esaweb.vo;

public class LevelVO {

    /**
	 * Nombre del nivel a mostrar en el IOlapCube de Flex
	 */
    private String displayName;

    /**
	 * Nombre del nivel utilizado por el IOlapCube de Flex
	 */
    private String attributeName;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
