package com.qasystems.qstudio.java.codec;

import com.qasystems.international.MessageResource;
import com.qasystems.qstudio.configuration.*;
import com.qasystems.qstudio.java.QStudioVersion;
import com.qasystems.qstudio.java.gui.Project;
import com.qasystems.qstudio.java.gui.ProjectMember;
import com.qasystems.qstudio.java.gui.ProjectMembersSet;
import com.qasystems.util.Utilities;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Vector;

/**
 * This class implements an encoder and decoder for the project
 * stored in QCF format
 */
public class ProjectQCFConverter {

    private static final String VERSION = QStudioVersion.asText();

    private static final Name NAME_NAME = new Name("Name");

    private static final Name JAVA_VERSION_NAME = new Name("JavaVersion");

    private static final Name MEMBERS_NAME = new Name("Members");

    private static final Name CLASSPATH_NAME = new Name("ClassPath");

    private static final Name PMDCLASSPATH_NAME = new Name("PMDClassPath");

    private static final Name SOURCEPATH_NAME = new Name("SourcePath");

    private static final Name CLASSPATH_PATH_NAME = new Name("ClassPath.Path");

    private static final Name PMDCLASSPATH_PATH_NAME = new Name("PMDClassPath.Path");

    private static final Name SOURCEPATH_PATH_NAME = new Name("SourcePath.Path");

    private static final Name OUTPUTPATH_NAME = new Name("OutputPath");

    private static final Name PRODUCT_ID_NAME = new Name("ProductID");

    private static final Name PROJECT_MEMBERS_DIR_NAME = new Name("ProjectMembersDir");

    private static final Name VERSION_NAME = new Name("Version");

    private static final Name SHOW_INCOMPLETE_MESSAGE = new Name("ShowIncompleteMessage");

    private static final MessageResource RESOURCES = MessageResource.getClientInstance();

    /**
   * Default constructor
   */
    public ProjectQCFConverter() {
        super();
    }

    /**
   * Convert from QCF format to data.
   *
   * @param data the object to be filled
   * @param config the QCF parse tree
   * @throws java.text.ParseException
   */
    public void from(Object data, Configuration config) throws ParseException {
        if (data instanceof Project) {
            decode((Project) data, config);
        }
    }

    /**
   * Convert from data to QCF format.
   *
   * @param config the QCF parse tree
   * @param data the object to be used to create the parse tree
   */
    public void to(Object data, Configuration config) {
        if (data instanceof Project) {
            encode(config, (Project) data);
        }
    }

    /**
   * Decode from QCF format to ProjectMember.
   *
   * @param data the project object to be filled
   * @param config the QCF parse tree
   * @throws java.text.ParseException
   */
    private void decode(Project data, Configuration config) throws ParseException {
        AttributeValue value = null;
        final Configuration membersConfig = config.retrieveConfiguration(MEMBERS_NAME);
        final Configuration classPathConfig = config.retrieveConfiguration(CLASSPATH_NAME);
        final Configuration pmdClassPathConfig = config.retrieveConfiguration(PMDCLASSPATH_NAME);
        final Configuration sourcePathConfig = config.retrieveConfiguration(SOURCEPATH_NAME);
        value = config.retrieve(NAME_NAME);
        if (value instanceof StringLiteral) {
            data.setName(((StringLiteral) value).getValue());
        } else {
            throwParseException(RESOURCES.format(RESOURCES.getString("MESSAGE_042"), new Object[] { "Name" }));
        }
        value = config.retrieve(JAVA_VERSION_NAME);
        if (value instanceof StringLiteral) {
            data.setJavaVersion(((StringLiteral) value).getValue());
        }
        value = config.retrieve(OUTPUTPATH_NAME);
        if (value instanceof StringLiteral) {
            Utilities.discardBooleanResult(data.setOutputPath(new File(((StringLiteral) value).getValue())));
        }
        value = config.retrieve(PRODUCT_ID_NAME);
        if (value instanceof StringLiteral) {
            data.setCodingStandard((((StringLiteral) value).getValue()), data.getRules());
        }
        value = config.retrieve(SHOW_INCOMPLETE_MESSAGE);
        if (value instanceof BooleanLiteral) {
            data.setShowIncompleteMessage(((BooleanLiteral) value).getValue());
        }
        if (sourcePathConfig != null) {
            for (int i = 0; i < sourcePathConfig.getElementCount(); i++) {
                value = sourcePathConfig.getValueAt(i);
                if (value instanceof StringLiteral) {
                    data.addSourcePath(new File(((StringLiteral) value).getValue()));
                }
            }
        }
        if (classPathConfig != null) {
            for (int i = 0; i < classPathConfig.getElementCount(); i++) {
                value = classPathConfig.getValueAt(i);
                if (value instanceof StringLiteral) {
                    data.addClassPath(new File(((StringLiteral) value).getValue()));
                }
            }
        }
        if (membersConfig != null) {
            for (int i = 0; (i < membersConfig.getElementCount()) && !data.stopLoading(); i++) {
                final Configuration memberConfig = membersConfig.getConfigurationAt(i);
                try {
                    Utilities.discardBooleanResult(data.addMember(new ProjectMember(memberConfig)));
                } catch (FileNotFoundException e) {
                    data.showFileNotFoundException(e.getMessage());
                }
            }
        }
        value = config.retrieve(PROJECT_MEMBERS_DIR_NAME);
        if (value instanceof StringLiteral) {
            data.setCurrentProjectMembersDir(((StringLiteral) value).getValue());
        }
    }

    /**
   * Encode from data to QCF format.
   *
   * @param config the QCF parse tree
   * @param data the project object to be used to create the parse
   *        tree
   */
    private void encode(Configuration config, Project data) {
        config.store(VERSION_NAME, new StringLiteral(VERSION));
        config.store(NAME_NAME, new StringLiteral(data.getName()));
        config.store(JAVA_VERSION_NAME, new StringLiteral(data.getJavaVersion()));
        config.store(PRODUCT_ID_NAME, new StringLiteral(data.getProductID()));
        config.store(SHOW_INCOMPLETE_MESSAGE, new BooleanLiteral(data.isShowIncompleteMessage()));
        final ProjectMembersSet members = data.getMembers();
        if (members != null) {
            config.store(MEMBERS_NAME, members.convertToQCF());
        }
        if (data.getOutputPath() != null) {
            config.store(OUTPUTPATH_NAME, new StringLiteral(data.getOutputPath().getAbsolutePath()));
        }
        final Vector classPath = data.getClassPath();
        if (classPath != null) {
            for (int i = 0; i < classPath.size(); i++) {
                config.store(CLASSPATH_PATH_NAME, new StringLiteral((String) classPath.get(i)));
            }
        }
        final Vector sourcePath = data.getSourcePath();
        if (sourcePath != null) {
            for (int i = 0; i < sourcePath.size(); i++) {
                config.store(SOURCEPATH_PATH_NAME, new StringLiteral((String) sourcePath.get(i)));
            }
        }
        config.store(PROJECT_MEMBERS_DIR_NAME, new StringLiteral(data.getCurrentProjectMembersDir()));
    }

    /**
   * Check for equality.
   *
   * @param obj the object to compare this object to
   * @return <tt>true</tt> if this object equals obj
   */
    public boolean equals(Object obj) {
        return (this == obj);
    }

    /**
   * Returns a hash code value for this object.
   *
   * @return the hash code
   */
    public int hashCode() {
        final int value = super.hashCode();
        return (value);
    }

    /**
   * Clone not supported.
   *
   * @return a clone
   * @see java.lang.Object#clone
   * @throws java.lang.CloneNotSupportedException
   */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
   * Returns <i>class_name</i>@<i>object_hashcode</i>.
   *
   * @return the string
   */
    public String toString() {
        return (getClass().getName() + "@" + Integer.toHexString(hashCode()));
    }

    private void throwParseException(String text) throws ParseException {
        throw new ParseException(text, -1);
    }
}
