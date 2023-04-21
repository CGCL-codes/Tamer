package org.log4j;

/**
   CategoryKey is a specialized class for accelerating category
   searches in a hash table.

   @author Ceki Gulcu - cgu@urbanet.ch */
class CategoryKey {

    String name;

    int hashCache;

    CategoryKey(String name) {
        this.name = name.intern();
        hashCache = name.hashCode();
    }

    public final int hashCode() {
        return hashCache;
    }

    public final boolean equals(Object r) {
        if (r instanceof CategoryKey) return this.name == ((CategoryKey) r).name; else return false;
    }
}
