package com.swgman.schematics;

import java.io.*;
import org.xml.sax.*;
import com.swgman.util.*;
import com.swgman.util.xml.*;
import com.swgman.util.xml.SAXBuilder.*;

public final class DraftSchematicsFactory {

    private static SAXBuilder builder;

    private static SAXBuilder getBuilder() throws XmlException {
        if (builder == null) {
            CreateRootElement createRootElement = new SAXBuilder.CreateRootElement() {

                public XmlElement create(String name) {
                    return new CDraftSchematics(name);
                }
            };
            builder = new SAXBuilder(createRootElement, "Schematics");
        }
        return builder;
    }

    public static DraftSchematics parseDraftSchematics(InputStream inputStream) throws IOException {
        try {
            return (CDraftSchematics) getBuilder().build(inputStream);
        } catch (SAXParseException e) {
            if (e.getException() != null) e.initCause(e.getException());
            throw new FatalError("Line: " + e.getLineNumber() + ", column: " + e.getColumnNumber() + "\n" + e.getMessage(), e);
        } catch (SAXException e) {
            if (e.getException() != null) e.initCause(e.getException());
            throw new FatalError(e);
        }
    }
}
