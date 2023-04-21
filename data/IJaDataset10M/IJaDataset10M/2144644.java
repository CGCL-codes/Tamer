package com.cube42.util.file;

import com.cube42.util.system.SystemCode;

/**
 * Collection of system codes specific to the File package
 *
 * @author Matt Paulin
 * @version $Id: FileSystemCodes.java,v 1.6 2003/01/15 06:52:12 zer0wing Exp $
 */
public class FileSystemCodes {

    /**
     * Used if a object could not be save to a file
     * <p>
     * <ul>
     * <li> first parameter = the type of object </li>
     * <li> second parameter = the filename used when saving </li>
     * <li> third parameter = the exception message </li>
     * </ul>
     */
    public static final SystemCode COULD_NOT_SAVE_OBJECT = new FileSystemCode("COULD_NOT_SAVE_OBJECT", "Could not save object {0} to file {1} message: {2}");

    /**
     * Used if a String could not be save to a file
     * <p>
     * <ul>
     * <li> first parameter = the filename used when saving </li>
     * <li> second parameter = the exception message </li>
     * </ul>
     */
    public static final SystemCode COULD_NOT_SAVE_STRING = new FileSystemCode("COULD_NOT_SAVE_STRING", "Could not save a string to the file {0} message: {1}");

    /**
     * Used if a object could not be loaded from a file
     * <p>
     * <ul>
     * <li> first parameter = the type of object </li>
     * <li> second parameter = the filename used when loading </li>
     * <li> third parameter = the exception message </li>
     * </ul>
     */
    public static final SystemCode COULD_NOT_LOAD_OBJECT = new FileSystemCode("COULD_NOT_LOAD_OBJECT", "Could not load object {0} from file {1} message: {2}");

    /**
     * Used if a string could not be loaded from a file
     * <p>
     * <ul>
     * <li> first parameter = the filename used when loading </li>
     * <li> second parameter = the exception message </li>
     * </ul>
     */
    public static final SystemCode COULD_NOT_LOAD_STRING = new FileSystemCode("COULD_NOT_LOAD_STRING", "Could not load string from file {0} message: {1}");

    /**
     * Used if a was not found in the specified file
     * <p>
     * <ul>
     * <li> first parameter = the filename used when loading the object </li>
     * <li> second parameter = the exception message </li>
     * </ul>
     */
    public static final SystemCode OBJECT_NOT_FOUND = new FileSystemCode("OBJECT_NOT_FOUND", "No object found in file {0} message: {1}");

    /**
     * Used if a compressed class cannot be added to the compressed class
     * collection it already contains a file using that filename.
     * <p>
     * <ul>
     * <li> first parameter = the duplicate class in the collection
     * <li> second parameter = the jar that originated the first class
     * <li> third parameter = the jar that originated the duplicate
     * </li>
     * </ul>
     */
    public static final SystemCode DUPLICATE_CLASS_IN_CLASSCOLLECTION = new FileSystemCode("DUPLICATE_CLASS_IN_CLASSCOLLECTION", "The compressed class [{0}] could not be added because an " + "another compressed class with the same name is already " + "in the collection, the classes originated from the jar " + "[{1}] and [{2}]");

    /**
     * Used if a file cannot be added to the ZipFileHandler because it already
     * contains a file using that filename.
     * <p>
     * <ul>
     * <li> first parameter = the filename used to add the file to the handler
     * </li>
     * </ul>
     */
    public static final SystemCode DUPLICATE_ENTRY_IN_ZIPFILEHANDLER = new FileSystemCode("DUPLICATE_ENTRY_IN_ZIPFILEHANDLER", "The entry {0} could not be added because an entry is " + "already loaded with that name");

    /**
     * Used if the zip file could not be created because of an IOException
     * <p>
     * <ul>
     * <li> first parameter = the IOException message </li>
     * </ul>
     */
    public static final SystemCode IOEXCEPTION_CREATING_ZIPFILE = new FileSystemCode("IOEXCEPTION_CREATING_ZIPFILE", "IOException thrown when creating the zipfile " + "Exception Message: {0}");

    /**
     * Used if an IOException is thrown while loading the zip file
     * <p>
     * <ul>
     * <li> first parameter = the filename </li>
     * <li> second parameter = the IOException message </li>
     * </ul>
     */
    public static final SystemCode IOEXCEPTION_LOADING_ZIPFILE = new FileSystemCode("IOEXCEPTION_LOADING_ZIPFILE", "IOException thrown when loading the zipfile " + "{0} Exception Message: {1}");

    /**
     * Used if a ZipException is thrown while loading the zip file
     * <p>
     * <ul>
     * <li> first parameter = the filename </li>
     * <li> second parameter = the ZipException message </li>
     * </ul>
     */
    public static final SystemCode ZIP_EXCEPTION_LOADING_ZIPFILE = new FileSystemCode("ZIP_EXCEPTION_LOADING_ZIPFILE", "ZipException thrown when loading the zipfile " + "{0} Exception Message: {1}");

    /**
     * Used if an empty classname was provided
     */
    public static final SystemCode EMPTY_CLASSNAME_PROVIDED = new FileSystemCode("EMPTY_CLASSNAME_PROVIDED", "An empty classname was provided ");

    /**
     * Used if an empty zipname was provided
     */
    public static final SystemCode EMPTY_ZIPNAME_PROVIDED = new FileSystemCode("EMPTY_ZIPNAME_PROVIDED", "An empty zipname was provided ");

    /**
     * A bad zipname was provided
     * <p>
     * <ul>
     * <li> first parameter = the bad zipname </li>
     * </ul>
     */
    public static final SystemCode BAD_ZIPNAME_PROVIDED = new FileSystemCode("BAD_ZIPNAME_PROVIDED", "The bad zipname {0} was provided must be in format" + " com/cube42/util/Class.class");

    /**
     * A non class file was loaded into the CompressedClass
     * <p>
     * <ul>
     * <li> first parameter = the name in the bad CompressedFile </li>
     * </ul>
     */
    public static final SystemCode BAD_COMPRESSED_FILE_PROVIDED = new FileSystemCode("BAD_COMPRESSED_FILE_PROVIDED", "The bad compressed file {0} was provided then name " + "must be in format com/cube42/util/Class.class");

    /**
     * Private constructor.  This class should never be created
     */
    private FileSystemCodes() {
        throw new RuntimeException("Do not instantiate this class");
    }

    /**
     * Internal class that is used to create all FileSystemCodes
     */
    static class FileSystemCode extends SystemCode {

        /**
         * Constructs a FileSystemCode
         *
         * @param   type    The type of system code this is
         * @param   message An explaination of what this type of code means
         */
        FileSystemCode(String type, String message) {
            super.setType(type);
            super.setMessage(message);
        }
    }
}
