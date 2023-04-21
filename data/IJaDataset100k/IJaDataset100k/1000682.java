package pl.xperios.rtfcompiler;

/**
 *
 * @author Praca
 */
class Condition {

    private String condition;

    boolean setCondition(String name) {
        try {
            int start = name.indexOf("[@");
            int end = name.indexOf("]");
            condition = name.substring(start + 1, end);
            name = name.substring(0, start);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    boolean checkCondition(XMLElement xmlElement) {
        System.out.print("SPRAWDZAM WARUNEK: " + condition + " dla elementu: " + xmlElement.getAtributesAll());
        String[] parts;
        if (condition.contains(" AND ")) {
            parts = condition.split(" AND ");
            for (int i = 0; i < parts.length; i++) {
                if (!checkPart(parts[i], xmlElement)) {
                    System.out.print(" FALSE");
                    System.out.println("");
                    return false;
                }
            }
            System.out.print(" TRUE");
            System.out.println("");
            return true;
        }
        if (condition.contains(" OR ")) {
            parts = condition.split(" OR ");
            for (int i = 0; i < parts.length; i++) {
                if (checkPart(parts[i], xmlElement)) {
                    System.out.print(" TRUE");
                    System.out.println("");
                    return true;
                }
            }
            System.out.print(" FALSE");
            System.out.println("");
            return false;
        }
        if (condition.contains("=")) {
            return checkPart(condition, xmlElement);
        }
        System.out.print(" FALSE");
        System.out.println("");
        return false;
    }

    private boolean checkPart(String part, XMLElement xmlElement) {
        String[] variables = part.split("=");
        if (variables[0].startsWith("@")) {
            if (xmlElement.getAtributesAll().getProperty(variables[0].substring(1)).equals(variables[1])) {
                return true;
            }
        }
        return false;
    }
}
