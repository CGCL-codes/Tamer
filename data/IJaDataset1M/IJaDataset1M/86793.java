package com.softaria.spkiller.metadata;

public interface ClassIdentifier {

    String getClassName();

    String getPackageName();

    boolean matchesPath(String path);

    String getPath();
}
