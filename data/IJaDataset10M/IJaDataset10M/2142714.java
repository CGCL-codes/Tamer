package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.skin.VisibilityModifier;

public class MemberImpl implements Member {

    private String display;

    private final boolean staticModifier;

    private final boolean abstractModifier;

    private final VisibilityModifier visibilityModifier;

    public MemberImpl(String display, boolean isMethod) {
        final String lower = display.toLowerCase();
        this.staticModifier = lower.contains("{static}") || lower.contains("{classifier}");
        this.abstractModifier = lower.contains("{abstract}");
        String displayClean = display.replaceAll("(?i)\\{(static|classifier|abstract)\\}", "").trim();
        if (displayClean.length() == 0) {
            displayClean = " ";
        }
        if (VisibilityModifier.isVisibilityCharacter(displayClean.charAt(0))) {
            visibilityModifier = VisibilityModifier.getVisibilityModifier(display.charAt(0), isMethod == false);
            this.display = displayClean.substring(1).trim();
        } else {
            this.display = displayClean;
            visibilityModifier = null;
        }
    }

    public String getDisplay(boolean withVisibilityChar) {
        if (withVisibilityChar) {
            return getDisplayWithVisibilityChar();
        }
        return getDisplayWithoutVisibilityChar();
    }

    public String getDisplayWithoutVisibilityChar() {
        assert display.length() == 0 || VisibilityModifier.isVisibilityCharacter(display.charAt(0)) == false;
        return display;
    }

    public String getDisplayWithVisibilityChar() {
        if (isPrivate()) {
            return "-" + display;
        }
        if (isPublic()) {
            return "+" + display;
        }
        if (isPackagePrivate()) {
            return "~" + display;
        }
        if (isProtected()) {
            return "#" + display;
        }
        return display;
    }

    @Override
    public boolean equals(Object obj) {
        final MemberImpl other = (MemberImpl) obj;
        return this.display.equals(other.display);
    }

    @Override
    public int hashCode() {
        return display.hashCode();
    }

    public final boolean isStatic() {
        return staticModifier;
    }

    public final boolean isAbstract() {
        return abstractModifier;
    }

    private boolean isPrivate() {
        return visibilityModifier == VisibilityModifier.PRIVATE_FIELD || visibilityModifier == VisibilityModifier.PRIVATE_METHOD;
    }

    private boolean isProtected() {
        return visibilityModifier == VisibilityModifier.PROTECTED_FIELD || visibilityModifier == VisibilityModifier.PROTECTED_METHOD;
    }

    private boolean isPublic() {
        return visibilityModifier == VisibilityModifier.PUBLIC_FIELD || visibilityModifier == VisibilityModifier.PUBLIC_METHOD;
    }

    private boolean isPackagePrivate() {
        return visibilityModifier == VisibilityModifier.PACKAGE_PRIVATE_FIELD || visibilityModifier == VisibilityModifier.PACKAGE_PRIVATE_METHOD;
    }

    public final VisibilityModifier getVisibilityModifier() {
        return visibilityModifier;
    }
}
