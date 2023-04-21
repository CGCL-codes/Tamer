package com.swgman.skills;

import java.io.*;
import org.xml.sax.*;
import com.swgman.util.*;
import com.swgman.util.xml.*;
import com.swgman.util.xml.SAXBuilder.*;

public class ProfessionFactory {

    private static SAXBuilder builder;

    private static SAXBuilder getBuilder() throws XmlException {
        if (builder == null) {
            CreateRootElement createRootElement = new SAXBuilder.CreateRootElement() {

                public XmlElement create(String name) {
                    return new CProfessions(name);
                }
            };
            builder = new SAXBuilder(createRootElement, "Professions");
        }
        return builder;
    }

    public static Professions parseProfessions(InputStream inputStream) throws IOException {
        try {
            return (CProfessions) getBuilder().build(inputStream);
        } catch (SAXParseException e) {
            throw new FatalError("Line: " + e.getLineNumber() + ", column: " + e.getColumnNumber() + "\n" + e.getMessage(), e);
        } catch (SAXException e) {
            throw new FatalError(e);
        }
    }
}
