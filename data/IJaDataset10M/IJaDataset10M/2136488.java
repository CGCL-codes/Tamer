package org.jprovocateur.serializer.io.xml;

/**
 * An interface for a {@link org.jprovocateur.serializer.io.HierarchicalStreamWriter} supporting XML-friendly names.
 * 
 * @author J&ouml;rg Schaible
 * @author Mauro Talevi
 * @since 1.3
 */
public interface XmlFriendlyWriter {

    /**
     * Escapes XML name (node or attribute) to be XML-friendly
     * 
     * @param name the unescaped XML name
     * @return An escaped name with original characters replaced
     */
    String escapeXmlName(String name);
}
