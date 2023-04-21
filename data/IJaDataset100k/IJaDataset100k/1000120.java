package games.basicgame;

public class PropertyInterfaceNotImplementedException extends PropertyInstantiationException {

    private static final long serialVersionUID = 5742456481218188295L;

    public PropertyInterfaceNotImplementedException(String propertyName, String propertyType) {
        super(propertyName, "property class " + propertyType + " does not implement interface games.basicgame.objects.Property.");
    }
}
