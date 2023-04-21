package org.nmc.pachyderm.foundation;

import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;
import ca.ucalgary.apollo.core.*;
import java.util.*;
import org.nmc.jdom.*;

public abstract class PXDescResolutionPhase extends PXBuildPhase {

    public static final String TimestampFormatKey = "TimestampFormat";

    public static final String DescriptionFieldsKey = "DescriptionFields";

    private NSTimestampFormatter _timestampFormatter;

    private NSArray _descriptionFields;

    public PXDescResolutionPhase(NSDictionary archive) {
        super();
        _timestampFormatter = new NSTimestampFormatter((String) CoreServices._dictionaryValueOrDefault(archive, TimestampFormatKey, "%Y-%m-%d"));
        _descriptionFields = (NSArray) CoreServices._dictionaryValueOrDefault(archive, DescriptionFieldsKey, NSArray.EmptyArray);
    }

    public DescriptionValueResolver resolverWithObject(Object object) {
        DescriptionValueResolver resv = new DescriptionValueResolver(object);
        resv.setTimestampFormatter(_timestampFormatter);
        return resv;
    }

    public void writeDescriptionFieldsToElement(Element desc, DescriptionValueResolver vres) {
        Enumeration dfields = _descriptionFields.objectEnumerator();
        while (dfields.hasMoreElements()) {
            NSDictionary field = (NSDictionary) dfields.nextElement();
            String name = (String) field.objectForKey("name");
            String keypath = (String) field.objectForKey("keypath");
            Element elem = new Element(name);
            Content content = vres.contentForKeyPath(keypath);
            elem.setContent(content);
            desc.addContent(elem);
        }
    }

    public class DescriptionValueResolver {

        private Object _object;

        private NSTimestampFormatter _timestampFormatter;

        DescriptionValueResolver(Object object) {
            _object = object;
        }

        public Object object() {
            return _object;
        }

        void setTimestampFormatter(NSTimestampFormatter formatter) {
            _timestampFormatter = formatter;
        }

        Content contentForKeyPath(String keyPath) {
            Object value = NSKeyValueCodingAdditions.Utility.valueForKeyPath(this, keyPath);
            Content content;
            if (value == null) {
                value = "*null*";
            }
            if (value instanceof String) {
                content = new Text((String) value);
            } else if (value instanceof NSTimestamp) {
                content = new Text(_timestampFormatter.format((NSTimestamp) value));
            } else {
                content = new Text(value.toString());
            }
            return content;
        }
    }
}
