package dex.structure;

import java.util.List;

/**
 * {@code DexFile} represents a whole dex file, containing multiple classes.
 */
public interface DexFile extends NamedElement {

    /**
     * Returns a list of {@code DexClass} elements that are part of this {@code
     * DexFile}.
     * 
     * @return a list of {@code DexClass} elements that are part of this {@code
     *         DexFile}
     */
    public List<DexClass> getDefinedClasses();
}
