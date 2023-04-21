package org.hitchhackers.tools.jmx.commands.dto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * This object describes an attribute (or value in an attribute that is a CompositeDataObject)
 * that should be queried remotely via JMX.
 * 
 * @author ptraeder
 */
public class ObjectNameAttributeName {

    private Pattern objectNameAttributeName = Pattern.compile("(.+?)/(.+?)(?:/(.+))*");

    ObjectName objectName;

    String attributeName;

    String compositePath;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public ObjectName getObjectName() {
        return objectName;
    }

    public void setObjectName(ObjectName objectName) {
        this.objectName = objectName;
    }

    public ObjectNameAttributeName(ObjectName name2, String name) {
        super();
        attributeName = name;
        objectName = name2;
    }

    public ObjectNameAttributeName(String string) {
        Matcher objectNameMatcher = objectNameAttributeName.matcher(string);
        if (objectNameMatcher.matches()) {
            String objectNameString = objectNameMatcher.group(1);
            String attributeName = objectNameMatcher.group(2);
            ObjectName objectName;
            try {
                objectName = ObjectName.getInstance(objectNameString);
            } catch (MalformedObjectNameException e) {
                throw new IllegalArgumentException("invalid object name '" + objectNameString + "' : " + e.toString());
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("BUG: parsed object name as 'null' - something is very wrong here!");
            }
            this.objectName = objectName;
            this.attributeName = attributeName;
            if (objectNameMatcher.groupCount() > 2) {
                this.compositePath = objectNameMatcher.group(3);
            }
        } else {
            throw new IllegalArgumentException("invalid parameter '" + string + "' - expecting <objectname>/<attributename> pairs");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(objectName.toString());
        sb.append("/");
        sb.append(attributeName);
        if (this.compositePath != null) {
            sb.append("/");
            sb.append(compositePath);
        }
        sb.append("");
        return sb.toString();
    }

    public String getCompositePath() {
        return compositePath;
    }

    public void setCompositePath(String compositePath) {
        this.compositePath = compositePath;
    }

    public static ArrayList<ObjectNameAttributeName> readObjectAttributeNamesFromFile(File attributeFile) throws IOException {
        ArrayList<ObjectNameAttributeName> result = new ArrayList<ObjectNameAttributeName>();
        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            fileReader = new FileReader(attributeFile);
            reader = new BufferedReader(fileReader);
            String theLine;
            while ((theLine = reader.readLine()) != null) {
                ObjectNameAttributeName objectNameAttributeName = new ObjectNameAttributeName(theLine);
                result.add(objectNameAttributeName);
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
