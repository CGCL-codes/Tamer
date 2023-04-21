package com.genia.toolbox.basics.manager;

import java.io.File;
import com.genia.toolbox.basics.exception.technical.TechnicalIOException;

/**
 * manager to handle files.
 */
public interface FileManager {

    /**
   * Creates an empty directory in the default temporary-file directory, using
   * the given prefix and suffix to generate its name. This directory will be
   * deleted when the JVM shutdown.
   * 
   * @param prefix
   *          The prefix string to be used in generating the file's name; must
   *          be at least three characters long
   * @param suffix
   *          The suffix string to be used in generating the file's name; may be
   *          <code>null</code>, in which case the suffix <code>".tmp"</code>
   *          will be used
   * @return An abstract pathname denoting a newly-created empty directory
   * @throws TechnicalIOException
   *           when an error occured
   * @see File#createTempFile(String, String)
   */
    public abstract File createAutoDeletableTempDirectory(final String prefix, final String suffix) throws TechnicalIOException;

    /**
   * Creates an empty file in the default temporary-file directory, using the
   * given prefix and suffix to generate its name. This file will be deleted
   * when the JVM shutdown.
   * 
   * @param prefix
   *          The prefix string to be used in generating the file's name; must
   *          be at least three characters long
   * @param suffix
   *          The suffix string to be used in generating the file's name; may be
   *          <code>null</code>, in which case the suffix <code>".tmp"</code>
   *          will be used
   * @return An abstract pathname denoting a newly-created empty directory
   * @throws TechnicalIOException
   *           when an error occured
   * @see File#createTempFile(String, String)
   */
    public abstract File createAutoDeletableTempFile(final String prefix, final String suffix) throws TechnicalIOException;

    /**
   * Creates an empty directory in the default temporary-file directory, using
   * the given prefix and suffix to generate its name.
   * 
   * @param prefix
   *          The prefix string to be used in generating the file's name; must
   *          be at least three characters long
   * @param suffix
   *          The suffix string to be used in generating the file's name; may be
   *          <code>null</code>, in which case the suffix <code>".tmp"</code>
   *          will be used
   * @return An abstract pathname denoting a newly-created empty directory
   * @throws TechnicalIOException
   *           when an error occured
   * @see File#createTempFile(String, String)
   */
    public abstract File createTempDirectory(final String prefix, final String suffix) throws TechnicalIOException;

    /**
   * Creates an empty directory in the default temporary-file directory, using
   * the given prefix and suffix to generate its name.
   * 
   * @param prefix
   *          The prefix string to be used in generating the file's name; must
   *          be at least three characters long
   * @param suffix
   *          The suffix string to be used in generating the file's name; may be
   *          <code>null</code>, in which case the suffix <code>".tmp"</code>
   *          will be used
   * @param rootDirectory
   *          The directory in which the file is to be created, or
   *          <code>null</code> if the default temporary-file directory is to
   *          be used
   * @return An abstract pathname denoting a newly-created empty directory
   * @throws TechnicalIOException
   *           when an error occured
   * @see File#createTempFile(String, String)
   */
    public File createTempDirectory(String prefix, String suffix, File rootDirectory) throws TechnicalIOException;

    /**
   * delete the given file as well as its children.
   * 
   * @param root
   *          the root of the files to delete
   */
    public abstract void deleteRecursively(File root);
}
