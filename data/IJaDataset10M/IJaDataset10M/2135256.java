package org.apache.bcel.classfile;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.bcel.Constants;

/**
 * Wrapper class that parses a given Java .class file. The method <A
 * href ="#parse">parse</A> returns a <A href ="JavaClass.html">
 * JavaClass</A> object on success. When an I/O error or an
 * inconsistency occurs an appropiate exception is propagated back to
 * the caller.
 *
 * The structure and the names comply, except for a few conveniences,
 * exactly with the <A href="ftp://java.sun.com/docs/specs/vmspec.ps">
 * JVM specification 1.0</a>. See this paper for
 * further details about the structure of a bytecode file.
 *
 * @version $Id: ClassParser.java 386056 2006-03-15 11:31:56Z tcurdt $
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A> 
 */
public final class ClassParser {

    private DataInputStream file;

    private boolean fileOwned;

    private String file_name;

    private String zip_file;

    private int class_name_index, superclass_name_index;

    private int major, minor;

    private int access_flags;

    private int[] interfaces;

    private ConstantPool constant_pool;

    private Field[] fields;

    private Method[] methods;

    private Attribute[] attributes;

    private boolean is_zip;

    private static final int BUFSIZE = 8192;

    /**
     * Parse class from the given stream.
     *
     * @param file Input stream
     * @param file_name File name
     */
    public ClassParser(InputStream file, String file_name) {
        this.file_name = file_name;
        fileOwned = false;
        String clazz = file.getClass().getName();
        is_zip = clazz.startsWith("java.util.zip.") || clazz.startsWith("java.util.jar.");
        if (file instanceof DataInputStream) {
            this.file = (DataInputStream) file;
        } else {
            this.file = new DataInputStream(new BufferedInputStream(file, BUFSIZE));
        }
    }

    /** Parse class from given .class file.
     *
     * @param file_name file name
     */
    public ClassParser(String file_name) throws IOException {
        is_zip = false;
        this.file_name = file_name;
        fileOwned = true;
    }

    /** Parse class from given .class file in a ZIP-archive
     *
     * @param zip_file zip file name
     * @param file_name file name
     */
    public ClassParser(String zip_file, String file_name) {
        is_zip = true;
        fileOwned = true;
        this.zip_file = zip_file;
        this.file_name = file_name;
    }

    /**
     * Parse the given Java class file and return an object that represents
     * the contained data, i.e., constants, methods, fields and commands.
     * A <em>ClassFormatException</em> is raised, if the file is not a valid
     * .class file. (This does not include verification of the byte code as it
     * is performed by the java interpreter).
     *
     * @return Class object representing the parsed class file
     * @throws  IOException
     * @throws  ClassFormatException
     */
    public JavaClass parse() throws IOException, ClassFormatException {
        ZipFile zip = null;
        try {
            if (fileOwned) {
                if (is_zip) {
                    zip = new ZipFile(zip_file);
                    ZipEntry entry = zip.getEntry(file_name);
                    file = new DataInputStream(new BufferedInputStream(zip.getInputStream(entry), BUFSIZE));
                } else {
                    file = new DataInputStream(new BufferedInputStream(new FileInputStream(file_name), BUFSIZE));
                }
            }
            readID();
            readVersion();
            readConstantPool();
            readClassInfo();
            readInterfaces();
            readFields();
            readMethods();
            readAttributes();
        } finally {
            if (fileOwned) {
                file.close();
                if (zip != null) {
                    zip.close();
                }
            }
        }
        return new JavaClass(class_name_index, superclass_name_index, file_name, major, minor, access_flags, constant_pool, interfaces, fields, methods, attributes, is_zip ? JavaClass.ZIP : JavaClass.FILE);
    }

    /**
     * Read information about the attributes of the class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void readAttributes() throws IOException, ClassFormatException {
        int attributes_count;
        attributes_count = file.readUnsignedShort();
        attributes = new Attribute[attributes_count];
        for (int i = 0; i < attributes_count; i++) {
            attributes[i] = Attribute.readAttribute(file, constant_pool);
        }
    }

    /**
     * Read information about the class and its super class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void readClassInfo() throws IOException, ClassFormatException {
        access_flags = file.readUnsignedShort();
        if ((access_flags & Constants.ACC_INTERFACE) != 0) {
            access_flags |= Constants.ACC_ABSTRACT;
        }
        if (((access_flags & Constants.ACC_ABSTRACT) != 0) && ((access_flags & Constants.ACC_FINAL) != 0)) {
            throw new ClassFormatException("Class can't be both final and abstract");
        }
        class_name_index = file.readUnsignedShort();
        superclass_name_index = file.readUnsignedShort();
    }

    /**
     * Read constant pool entries.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void readConstantPool() throws IOException, ClassFormatException {
        constant_pool = new ConstantPool(file);
    }

    /**
     * Read information about the fields of the class, i.e., its variables.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void readFields() throws IOException, ClassFormatException {
        int fields_count;
        fields_count = file.readUnsignedShort();
        fields = new Field[fields_count];
        for (int i = 0; i < fields_count; i++) {
            fields[i] = new Field(file, constant_pool);
        }
    }

    /**
     * Check whether the header of the file is ok.
     * Of course, this has to be the first action on successive file reads.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void readID() throws IOException, ClassFormatException {
        int magic = 0xCAFEBABE;
        if (file.readInt() != magic) {
            throw new ClassFormatException(file_name + " is not a Java .class file");
        }
    }

    /**
     * Read information about the interfaces implemented by this class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void readInterfaces() throws IOException, ClassFormatException {
        int interfaces_count;
        interfaces_count = file.readUnsignedShort();
        interfaces = new int[interfaces_count];
        for (int i = 0; i < interfaces_count; i++) {
            interfaces[i] = file.readUnsignedShort();
        }
    }

    /**
     * Read information about the methods of the class.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void readMethods() throws IOException, ClassFormatException {
        int methods_count;
        methods_count = file.readUnsignedShort();
        methods = new Method[methods_count];
        for (int i = 0; i < methods_count; i++) {
            methods[i] = new Method(file, constant_pool);
        }
    }

    /**
     * Read major and minor version of compiler which created the file.
     * @throws  IOException
     * @throws  ClassFormatException
     */
    private final void readVersion() throws IOException, ClassFormatException {
        minor = file.readUnsignedShort();
        major = file.readUnsignedShort();
    }
}
