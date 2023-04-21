package net.ar.guia.managers.names;

import java.text.*;
import java.util.*;
import net.ar.guia.helpers.*;
import net.ar.guia.own.implementation.*;
import net.ar.guia.own.interfaces.*;

/**
 * Nombra los componentes de la siguiente forma: si el numero de rama es menor o
 * igual a 52 entonces usa una letra para identificarla, y si es mayor usar un
 * numero con el siguiente formato "XXX". Y luego le concatena un "_" seguido
 * del nombre del componente con el numero de rama. ej: aabac_067eb.JButton02
 * 
 * @author Fernando Damian Petrola
 */
public class CharacterComponentNameManager extends ComponentVisitorAdapter implements ComponentNameManager {

    private static final String NAME2COMPONENT = "name2component";

    private static final String UNIQUE_ID = "uniqueId";

    public static final String UNIQUE_NAME_PROPERTY = "theUniqueName";

    public static final String USER_UNIQUE_NAME_PROPERTY = "theUserUniqueName";

    int theCurrentChildNumber;

    protected static final char SEPARATOR = '_';

    protected DecimalFormat digitsFormat = new DecimalFormat("#000");

    protected VisualComponent rootComponent;

    protected Map name2component;

    public VisualComponent findComponentWithNameIn(String aComponentName, VisualComponent aRootComponent) {
        findRootComponent(aRootComponent);
        return (VisualComponent) name2component.get(aComponentName);
    }

    private void findRootComponent(VisualComponent aRootComponent) {
        rootComponent = GuiaHelper.getTopParent(aRootComponent);
        name2component = (Map) rootComponent.getClientProperty(NAME2COMPONENT);
        if (name2component == null) rootComponent.putClientProperty(NAME2COMPONENT, name2component = new Hashtable());
    }

    public void assignNamesFrom(VisualComponent aComponent) {
        findRootComponent(aComponent);
        if (aComponent == rootComponent) name2component.clear();
        theCurrentChildNumber = 0;
        aComponent.accept(this);
    }

    public void visitComponentBegin(VisualComponent aComponent) {
        boolean hasParent = aComponent.getParent() != null;
        String parentUniqueId = hasParent ? getUniqueId(aComponent.getParent()) : "";
        String theHead = pathToString(hasParent && !(aComponent.getParent() instanceof WindowComponent) ? parentUniqueId.substring(0, parentUniqueId.indexOf(SEPARATOR)) : "");
        internalSetUniqueId(aComponent.getOwner(), theHead + SEPARATOR + getTail(aComponent));
        theCurrentChildNumber = 0;
    }

    public void visitComponentEnd(VisualComponent aComponent) {
        try {
            int separatorIndex = getUniqueId(aComponent).indexOf(SEPARATOR);
            if (separatorIndex != -1) {
                char lastChar = getUniqueId(aComponent).charAt(separatorIndex - 1);
                if (Character.isDigit(lastChar)) theCurrentChildNumber = digitsFormat.parse(getUniqueId(aComponent).substring(separatorIndex - 3, separatorIndex)).intValue() + 1; else theCurrentChildNumber = CharacterConverter.charToPosition[lastChar] + 1;
            }
        } catch (ParseException e) {
            throw new GuiaException(e);
        }
    }

    public String getTail(VisualComponent aComponent) {
        String theTail = (String) aComponent.getClientProperty(USER_UNIQUE_NAME_PROPERTY);
        if (theTail == null) theTail = aComponent.getTypeId().substring(aComponent.getTypeId().lastIndexOf('.') + 1) + theCurrentChildNumber;
        int indexOfMoney = theTail.indexOf('$');
        if (indexOfMoney != -1) theTail = theTail.substring(0, indexOfMoney);
        return theTail;
    }

    protected String pathToString(String aParentPath) {
        String theResult = "";
        if (theCurrentChildNumber < CharacterConverter.positionToChar.length) theResult = aParentPath + CharacterConverter.positionToChar[theCurrentChildNumber]; else theResult = aParentPath + digitsFormat.format(theCurrentChildNumber);
        return theResult;
    }

    public static class CharacterConverter {

        public static char[] positionToChar = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        public static int[] charToPosition = new CharacterConverter().getIntMapping();

        public int[] getIntMapping() {
            int[] theIntMap = new int[300];
            for (int i = 0; i < positionToChar.length; i++) theIntMap[positionToChar[i]] = i;
            return theIntMap;
        }
    }

    public String getUniqueId(VisualComponent aVisualComponent) {
        return (String) aVisualComponent.getClientProperty(UNIQUE_ID);
    }

    public void setUniqueId(VisualComponent aVisualComponent, String anId) {
        findRootComponent(aVisualComponent);
        internalSetUniqueId(aVisualComponent, anId);
    }

    private void internalSetUniqueId(VisualComponent aVisualComponent, String anId) {
        name2component.put(anId, aVisualComponent);
        aVisualComponent.putClientProperty(UNIQUE_ID, anId);
    }
}
