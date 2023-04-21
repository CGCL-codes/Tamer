package ee.meelisbm.dirlist;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author meelis
 */
public class DirlistStreamReader {

    private static XMLInputFactory factory = XMLInputFactory.newInstance();

    private InputStream input;

    private XMLStreamReader parser;

    private int event;

    /**
     * 
     * @param input
     */
    public DirlistStreamReader(InputStream input) throws XMLStreamException {
        this.input = input;
        parser = factory.createXMLStreamReader(input);
    }

    /**
     * 
     * @return
     * @throws javax.xml.stream.XMLStreamException
     */
    public DirlistDocument parseDocument() throws XMLStreamException, DirlistFileFormatException {
        DirlistDocument document = new DirlistDocument();
        while (true) {
            event = parser.next();
            if (event == XMLStreamConstants.END_DOCUMENT) {
                parser.close();
                if (document.validate()) {
                    return document;
                }
                throw new DirlistFileFormatException();
            }
            if (event == XMLStreamConstants.START_ELEMENT) {
                if (parser.getLocalName().equals("dirlistDocument")) {
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        setDocumentAttribute(document, parser.getAttributeLocalName(i), parser.getAttributeValue(i));
                    }
                } else if (parser.getLocalName().equals("rootDirectory")) {
                    document.setRootDirectory(parseDirectory("rootDirectory"));
                }
            }
        }
    }

    /**
     * 
     * @param elementName
     * @return
     */
    protected DirlistDirectory parseDirectory(String elementName) throws XMLStreamException {
        DirlistDirectory directory = new DirlistDirectory();
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            setDirectoryAttribute(directory, parser.getAttributeLocalName(i), parser.getAttributeValue(i));
        }
        while (true) {
            event = parser.next();
            if (event == XMLStreamConstants.END_ELEMENT) {
                return directory;
            } else if (event == XMLStreamConstants.START_ELEMENT) {
                if (parser.getLocalName().equals("subDirectories")) {
                    directory.setSubDirectories(parseSubDirectories());
                } else if (parser.getLocalName().equals("files")) {
                    directory.setFiles(parseFiles());
                }
            }
        }
    }

    /**
     * 
     * @return
     * @throws javax.xml.stream.XMLStreamException
     */
    protected DirlistDirectory[] parseSubDirectories() throws XMLStreamException {
        List<DirlistDirectory> directories = new ArrayList<DirlistDirectory>();
        while (true) {
            event = parser.next();
            if (event == XMLStreamConstants.END_ELEMENT) {
                return directories.toArray(new DirlistDirectory[0]);
            } else if (event == XMLStreamConstants.START_ELEMENT) {
                if (parser.getLocalName().equals("directory")) {
                    directories.add(parseDirectory("directory"));
                }
            }
        }
    }

    protected DirlistFile[] parseFiles() throws XMLStreamException {
        List<DirlistFile> files = new ArrayList<DirlistFile>();
        while (true) {
            event = parser.next();
            if (event == XMLStreamConstants.END_ELEMENT) {
                return files.toArray(new DirlistFile[0]);
            } else if (event == XMLStreamConstants.START_ELEMENT) {
                if (parser.getLocalName().equals("file")) {
                    files.add(parseFile());
                }
            }
        }
    }

    protected DirlistFile parseFile() throws XMLStreamException {
        DirlistFile file = new DirlistFile();
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            setFileAttribute(file, parser.getAttributeLocalName(i), parser.getAttributeValue(i));
        }
        while (true) {
            event = parser.next();
            if (event == XMLStreamConstants.END_ELEMENT) {
                return file;
            }
        }
    }

    /**
     * 
     * @param document
     * @param attributeName
     * @param attributeValue
     */
    protected void setDocumentAttribute(DirlistDocument document, String name, String value) {
        if (name.equals("creationDate")) {
            document.setCreationDate(Long.parseLong(value));
        } else if (name.equals("directoriesCount")) {
            document.setDirectoriesCount(Long.parseLong(value));
        } else if (name.equals("filesCount")) {
            document.setFilesCount(Long.parseLong(value));
        } else if (name.equals("rootDirectoryLocation")) {
            document.setRootDirectoryLocation(value);
        }
    }

    /**
     * 
     * @param directory
     * @param name
     * @param value
     */
    protected void setDirectoryAttribute(DirlistDirectory directory, String name, String value) {
        if (name.equals("name")) {
            directory.setName(value);
        } else if (name.equals("lastModified")) {
            directory.setLastModified(Long.valueOf(value));
        } else if (name.equals("totalSize")) {
            directory.setTotalSize(Long.valueOf(value));
        }
    }

    /**
     * 
     * @param file
     * @param name
     * @param value
     */
    protected void setFileAttribute(DirlistFile file, String name, String value) {
        if (name.equals("name")) {
            file.setName(value);
        } else if (name.equals("lastModified")) {
            file.setLastModified(Long.parseLong(value));
        } else if (name.equals("length")) {
            file.setLength(Long.parseLong(value));
        }
    }
}
