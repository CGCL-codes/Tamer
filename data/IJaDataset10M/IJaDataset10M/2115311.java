package net.sf.parser4j.generator.service.match;

/**
 * to have package name of match manager class
 * 
 * @author luc peuvrier
 * 
 */
public final class GenMatchPackage {

    private GenMatchPackage() {
        super();
    }

    public static String getPackageName() {
        return GenMatchPackage.class.getPackage().getName();
    }
}
