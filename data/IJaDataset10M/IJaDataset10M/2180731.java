package com.google.doclava;

public interface Scoped {

    boolean isPublic();

    boolean isProtected();

    boolean isPackagePrivate();

    boolean isPrivate();

    boolean isHidden();
}
