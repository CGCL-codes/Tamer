package org.j2uml.intermediate.records.interfaces;

/**
 * @author Vijayaraghavan Kalyanapasupathy
 *         vijayaraghavan.k@isis.vanderbilt.edu
 *
 */
public abstract class VisibilityType {

    public abstract String toString();

    public static final class Private extends VisibilityType {

        public String toString() {
            return "private";
        }
    }

    public static final class Public extends VisibilityType {

        public String toString() {
            return "public";
        }
    }

    public static final class Protected extends VisibilityType {

        public String toString() {
            return "protected";
        }
    }

    public static final class Package extends VisibilityType {

        public String toString() {
            return "default";
        }
    }

    public static final Private PrivateVisibility = new Private();

    public static final Public PublicVisibility = new Public();

    public static final Protected ProtectedVisibility = new Protected();

    public static final Package DefaultVisibility = new Package();
}
