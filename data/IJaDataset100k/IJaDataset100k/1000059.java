package org.unicore.ajo;

import org.unicore.sets.ResourceSet;

/**
 * Test attributes of a file.
 * <p>
 * The attributes that can be tested are the same as those returned in 
 * directory listings ({@link org.unicore.outcome.XFile})
 * <p>
 * The value of each attribute is tested against the template in the FileCheck.
 * If any attribute fails to match the template value, then the {@link org.unicore.outcome.Decision}
 * is {@link org.unicore.outcome.FileCheck_Outcome}.NOT_SATISFIED.
 * If all the requested attributes match, then the {@link org.unicore.outcome.Decision}
 * is {@link org.unicore.outcome.FileCheck_Outcome}.SATISFIED.
 * <p>
 * The location of the file is given by an instance of {@link org.unicore.resources.Storage}
 * in the resources (in the same way as for {@link FileAction}).
 *
 * @see org.unicore.outcome.FileCheck_Outcome
 * @see org.unicore.outcome.XFile
 * @see If
 * @see RepeatGroup
 * 
 * @author D. Snelling (Fujitsu Laboratories of Europe)
 * @author S. van den Berghe (Fujitsu Laboratories of Europe)
 *
 * @since AJO 3
 *
 * @version $Id: FileCheck.java,v 1.2 2004/05/26 16:31:45 svenvdb Exp $
 *
 **/
public final class FileCheck extends FileAction implements Decidable {

    static final long serialVersionUID = 400;

    /**
     * Create a new FileCheck.
     * 
     * @param name The name of the FileCheck
     * @param extra_information Client supplied extra information about the FileCheck
     * @param resources Resources needed to execute the FileCheck
     * @param file The file to check
     *
     **/
    public FileCheck(String name, byte[] extra_information, ResourceSet resources, String file) {
        super(name, extra_information, resources);
        this.file = file;
    }

    /**
     * Create a new FileCheck.
     * 
     * @param name The name of the FileCheck
     * @param file The file to check
     *
     **/
    public FileCheck(String name, String file) {
        this(name, (byte[]) null, (ResourceSet) null, file);
    }

    /**
     * Create a new FileCheck.
     * <p>
     * @param name The name of the FileCheck
     *
     **/
    public FileCheck(String name) {
        this(name, (byte[]) null, (ResourceSet) null, (String) null);
    }

    public FileCheck() {
        this("");
    }

    private String file;

    /**
     * Return the name of the file to check.
     *
     **/
    public String getFileToCheck() {
        return file;
    }

    /**
     * Set the name of the file to check.
     *
     **/
    public void setFileToCheck(String file) {
        this.file = file;
    }

    private Boolean file_exists;

    /**
     * Set the test on file existance.
     * <dl>
     * <dt> file_exists = true  <dd> test result is true if the file exists at run time
     * <dt> file_exists = false <dd> test result is true if the file does not exist at run time
     * <dt> file_exists = null  <dd> test is not performed, result is not included in the result
     * </dl>
     *
     **/
    public void setFileExists(Boolean file_exists) {
        this.file_exists = file_exists;
    }

    /**
     * @return The value of the file existance test
     *
     **/
    public Boolean getFileExists() {
        return file_exists;
    }

    private Boolean can_read_file;

    /**
     * Set the test on file readability.
     * <dl>
     * <dt> can_read_file = true  <dd> test result is true if the file can be read by the user (as incarnated)
     * <dt> can_read_file = false <dd> test result is true if the file cannot be read by the user (as incarnated)
     * <dt> can_read_file = null  <dd> test is not performed, result is not included in the result
     * </dl>
     *
     **/
    public void setCanReadFile(Boolean can_read_file) {
        this.can_read_file = can_read_file;
    }

    /**
     * @return The value of the test condition on ability to read the file
     *
     **/
    public Boolean getCanReadFile() {
        return can_read_file;
    }

    private Boolean can_write_file;

    /**
     * Set the test on file writable.
     * <dl>
     * <dt> can_write_file = true  <dd> test result is true if the file can be written by the user (as incarnated)
     * <dt> can_write_file = false <dd> test result is true if the file cannot be written by the user (as incarnated)
     * <dt> can_write_file = null  <dd> test is not performed, result is not included in the result
     * </dl>
     *
     **/
    public void setCanWriteFile(Boolean can_write_file) {
        this.can_write_file = can_write_file;
    }

    /**
     * @return The value of the test condition on ability to write to the file
     *
     **/
    public Boolean getCanWriteFile() {
        return can_write_file;
    }

    private Boolean can_execute_file;

    /**
     * Set the test on file execution.
     * <dl>
     * <dt> can_execute_file = true  <dd> test result is true if the file can be executed by the user (as incarnated)
     * <dt> can_execute_file = false <dd> test result is true if the file cannot be executed by the user (as incarnated)
     * <dt> can_execute_file = null  <dd> test is not performed, result is not included in the result
     * </dl>
     *
     **/
    public void setCanExecuteFile(Boolean can_execute_file) {
        this.can_execute_file = can_execute_file;
    }

    /**
     * @return The value of the test condition on ability to execute the file
     *
     **/
    public Boolean getCanExecuteFile() {
        return can_execute_file;
    }

    private Boolean file_owned_by_user;

    /**
     * Set the test on file ownership.
     * <dl>
     * <dt> file_owned_by_user = true  <dd> test result is true if the file is owned by the user (as incarnated)
     * <dt> file_owned_by_user = false <dd> test result is true if the file is not owned by the user (as incarnated)
     * <dt> file_owned_by_user = null  <dd> test is not performed, result is not included in the result
     * </dl>
     *
     **/
    public void setFileOwnedByUser(Boolean file_owned_by_user) {
        this.file_owned_by_user = file_owned_by_user;
    }

    /**
     * @return The value of the test condition on ownership of the the file
     *
     **/
    public Boolean getFileOwnedByUser() {
        return file_owned_by_user;
    }

    private Boolean is_directory;

    /**
     * Set the test on file being a directory.
     * <dl>
     * <dt> is_directory = true  <dd> test result is true if the file is a directory
     * <dt> is_directory = false <dd> test result is true if the file is not a directory
     * <dt> is_directory = null  <dd> test is not performed, result is not included in the result
     * </dl>
     *
     **/
    public void setIsDirectory(Boolean is_directory) {
        this.is_directory = is_directory;
    }

    /**
     * @return The value of the test condition on file being a directory.
     *
     **/
    public Boolean getIsDirectory() {
        return is_directory;
    }

    private Expression file_size;

    /**
     * Set the file size test expression. The value of this expression must be a java.lang.Long.
     * The expression can be null, in which case there is no test on file size. 
     *
     * @param file_size The test expression (null if this condition should not be part of the final result)
     * @throws ClassCastException If the expression value is not an java.lang.Long
     *
     **/
    public void setFileSize(Expression file_size) {
        if (file_size == null || file_size.getValue() instanceof java.lang.Long) {
            this.file_size = file_size;
        } else {
            throw new ClassCastException("The value of the expression for the file size in FileCheck must be a Long, got: " + file_size.getValue().getClass());
        }
    }

    /**
     * @return The current file size expression
     *
     **/
    public Expression getFileSize() {
        return file_size;
    }

    private Expression file_modification_date;

    /**
     * Set the file date test expression. The value of this expression must be a java.util.Date
     * The expression can be null, in which case there is no test on the file modification date.
     *
     * @param file_modification_date The test expression (null if this condition should not be part of the final result)
     * @throws ClassCastException If the expression value is not a java.util.Date
     *
     **/
    public void setFileModificationDate(Expression file_modification_date) {
        if (file_modification_date == null || file_modification_date.getValue() instanceof java.util.Date) {
            this.file_modification_date = file_modification_date;
        } else {
            throw new ClassCastException("The value of the expression for the file date in FileCheck must be a java.util.Date, got: " + file_modification_date.getValue().getClass());
        }
    }

    /**
     * @return The current file modification date expression
     *
     **/
    public Expression getFileModificationDate() {
        return file_modification_date;
    }
}
