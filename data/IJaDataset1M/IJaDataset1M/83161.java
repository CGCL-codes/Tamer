package org.apache.axis.wsdl.toJava;

import org.apache.axis.utils.Messages;
import org.apache.axis.wsdl.gen.Generator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.io.*;
import java.util.StringTokenizer;

/**
 * Emitter knows about WSDL writers, one each for PortType, Binding, Service,
 * Definition, Type.  But for some of these WSDL types, Wsdl2java generates
 * multiple files.  Each of these files has a corresponding writer that extends
 * JavaWriter.  So the Java WSDL writers (JavaPortTypeWriter, JavaBindingWriter,
 * etc.) each calls a file writer (JavaStubWriter, JavaSkelWriter, etc.) for
 * each file that that WSDL generates.
 * <p/>
 * <p>For example, when Emitter calls JavaWriterFactory for a Binding Writer, it
 * returns a JavaBindingWriter.  JavaBindingWriter, in turn, contains a
 * JavaStubWriter, JavaSkelWriter, and JavaImplWriter since a Binding may cause
 * a stub, skeleton, and impl template to be generated.
 * <p/>
 * <p>Note that the writers that are given to Emitter by JavaWriterFactory DO NOT
 * extend JavaWriter.  They simply implement Writer and delegate the actual
 * task of writing to extensions of JavaWriter.
 * <p/>
 * <p>All of Wsdl2java's Writer implementations follow a common behaviour.
 * JavaWriter is the abstract base class that dictates this common behaviour.
 * This behaviour is primarily placed within the generate method.  The generate
 * method calls, in succession (note:  the starred methods are the ones you are
 * probably most interested in):
 * <dl>
 * <dt> * getFileName
 * <dd> This is an abstract method that must be implemented by the subclass.
 * It returns the fully-qualified file name.
 * <dt> isFileGenerated(file)
 * <dd> You should not need to override this method.  It checks to see whether
 * this file is in the List returned by emitter.getGeneratedFileNames.
 * <dt> registerFile(file)
 * <dd> You should not need to override this method.  It registers this file by
 * calling emitter.getGeneratedFileInfo().add(...).
 * <dt> * verboseMessage(file)
 * <dd> You may override this method if you want to provide more information.
 * The generate method only calls verboseMessage if verbose is turned on.
 * <dt> getPrintWriter(file)
 * <dd> You should not need to override this method.  Given the file name, it
 * creates a PrintWriter for it.
 * <dt> * writeFileHeader(pw)
 * <dd> You may want to override this method.  The default implementation
 * generates nothing.
 * <dt> * writeFileBody(pw)
 * <dd> This is an abstract method that must be implemented by the subclass.
 * This is where the body of a file is generated.
 * <dt> * writeFileFooter(pw)
 * <dd> You may want to override this method.  The default implementation
 * generates nothing.
 * <dt> closePrintWriter(pw)
 * <dd> You should not need to override this method.  It simply closes the
 * PrintWriter.
 * </dl>
 */
public abstract class JavaWriter implements Generator {

    /** This controls how many characters per line for javadoc comments */
    protected static final int LINE_LENGTH = 65;

    /** Field emitter */
    protected Emitter emitter;

    /** Field type */
    protected String type;

    /**
     * Constructor.
     *
     * @param emitter
     * @param type
     */
    protected JavaWriter(Emitter emitter, String type) {
        this.emitter = emitter;
        this.type = type;
    }

    /**
     * Generate a file.
     *
     * @throws IOException
     */
    public void generate() throws IOException {
        String file = getFileName();
        if (isFileGenerated(file)) {
            throw new DuplicateFileException(Messages.getMessage("duplicateFile00", file), file);
        }
        registerFile(file);
        if (emitter.isVerbose()) {
            String msg = verboseMessage(file);
            if (msg != null) {
                System.out.println(msg);
            }
        }
        PrintWriter pw = getPrintWriter(file);
        writeFileHeader(pw);
        writeFileBody(pw);
        writeFileFooter(pw);
        closePrintWriter(pw);
    }

    /**
     * This method must be implemented by a subclass.  It
     * returns the fully-qualified name of the file to be
     * generated.
     *
     * @return
     */
    protected abstract String getFileName();

    /**
     * You should not need to override this method. It checks
     * to see whether the given file is in the List returned
     * by emitter.getGeneratedFileNames.
     *
     * @param file
     * @return
     */
    protected boolean isFileGenerated(String file) {
        return emitter.getGeneratedFileNames().contains(file);
    }

    /**
     * You should not need to override this method.
     * It registers the given file by calling
     * emitter.getGeneratedFileInfo().add(...).
     *
     * @param file
     */
    protected void registerFile(String file) {
        emitter.getGeneratedFileInfo().add(file, null, type);
    }

    /**
     * Return the string:  "Generating <file>".  Override this
     * method if you want to provide more information.
     *
     * @param file
     * @return
     */
    protected String verboseMessage(String file) {
        return Messages.getMessage("generating", file);
    }

    /**
     * You should not need to override this method.
     * Given the file name, it creates a PrintWriter for it.
     *
     * @param filename
     * @return
     * @throws IOException
     */
    protected PrintWriter getPrintWriter(String filename) throws IOException {
        File file = new File(filename);
        File parent = new File(file.getParent());
        parent.mkdirs();
        FileOutputStream out = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
        return new PrintWriter(writer);
    }

    /**
     * This method is intended to be overridden as necessary
     * to generate file header information.  This default
     * implementation does nothing.
     *
     * @param pw
     * @throws IOException
     */
    protected void writeFileHeader(PrintWriter pw) throws IOException {
    }

    /**
     * This method must be implemented by a subclass.  This
     * is where the body of a file is generated.
     *
     * @param pw
     * @throws IOException
     */
    protected abstract void writeFileBody(PrintWriter pw) throws IOException;

    /**
     * You may want to override this method.  This default
     * implementation generates nothing.
     *
     * @param pw
     * @throws IOException
     */
    protected void writeFileFooter(PrintWriter pw) throws IOException {
    }

    /**
     * Close the print writer.
     *
     * @param pw
     */
    protected void closePrintWriter(PrintWriter pw) {
        pw.close();
    }

    /**
     * Takes out new lines and wraps at Javadoc tags
     * @param documentation the raw comments from schema
     * @param addTab if true adds a tab character when wrapping (methods)
     */
    protected String getJavadocDescriptionPart(String documentation, boolean addTab) {
        if (documentation == null) {
            return "";
        }
        String doc = documentation.trim();
        if (documentation.trim().length() == 0) {
            return doc;
        }
        StringTokenizer st = new StringTokenizer(doc, "@");
        StringBuffer newComments;
        if (st.hasMoreTokens()) {
            String token = st.nextToken();
            boolean startLine = Character.isWhitespace(token.charAt(token.length() - 1)) && (token.charAt(token.length() - 1) != '\n');
            newComments = new StringBuffer(token);
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if (startLine) {
                    newComments.append('\n');
                }
                newComments.append('@');
                startLine = Character.isWhitespace(token.charAt(token.length() - 1)) & (token.charAt(token.length() - 1) != '\n');
                newComments.append(token);
            }
        } else {
            newComments = new StringBuffer(doc);
        }
        newComments.insert(0, addTab ? "     * " : " * ");
        int pos = newComments.toString().indexOf("*/");
        while (pos >= 0) {
            newComments.insert(pos + 1, ' ');
            pos = newComments.toString().indexOf("*/");
        }
        int lineStart = 0;
        int newlinePos = 0;
        while (lineStart < newComments.length()) {
            newlinePos = newComments.toString().indexOf("\n", lineStart);
            if (newlinePos == -1) {
                newlinePos = newComments.length();
            }
            if ((newlinePos - lineStart) > LINE_LENGTH) {
                lineStart += LINE_LENGTH;
                while ((lineStart < newComments.length()) && !Character.isWhitespace(newComments.charAt(lineStart))) {
                    lineStart++;
                }
                if (lineStart < newComments.length()) {
                    char next = newComments.charAt(lineStart);
                    if ((next == '\r') || (next == '\n')) {
                        newComments.insert(lineStart + 1, addTab ? "     * " : " * ");
                        lineStart += addTab ? 8 : 4;
                    } else {
                        newComments.insert(lineStart, addTab ? "\n     * " : "\n * ");
                        lineStart += addTab ? 8 : 4;
                    }
                }
                while ((lineStart < newComments.length()) && (newComments.charAt(lineStart) == ' ')) {
                    newComments.delete(lineStart, lineStart + 1);
                }
            } else {
                if (++newlinePos < newComments.length()) {
                    newComments.insert(newlinePos, addTab ? "     * " : " * ");
                }
                lineStart = newlinePos;
                lineStart += addTab ? 7 : 3;
            }
        }
        return newComments.toString();
    }

    /**
     * Output a documentation element as a Java comment.
     *
     * @param pw
     * @param element
     */
    protected void writeComment(PrintWriter pw, Element element) {
        writeComment(pw, element, true);
    }

    /**
     * Output a documentation element as a Java comment.
     *
     * @param pw
     * @param element
     * @param addTab
     */
    protected void writeComment(PrintWriter pw, Element element, boolean addTab) {
        if (element == null) {
            return;
        }
        Node child = element.getFirstChild();
        if (child == null) {
            return;
        }
        String comment = child.getNodeValue();
        if (comment != null) {
            int start = 0;
            pw.println();
            pw.println(addTab ? "    /**" : "/**");
            pw.println(getJavadocDescriptionPart(comment, addTab));
            pw.println(addTab ? "     */" : " */");
        }
    }
}
