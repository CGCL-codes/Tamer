package net.turambar.just.jpp.ant;

import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.util.TaskLogger;
import org.apache.tools.ant.types.FileSet;
import net.turambar.just.jpp.JPP;
import net.turambar.just.jpp.CommentSnifferFactory;
import net.turambar.just.Logger;
import net.turambar.just.CommentSniffer;
import net.turambar.just.LineCommentParser;
import net.turambar.just.BlockCommentParser;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/** An ant task which runs jpp.
 *  Supported attributes:
 *  <dl>
 *      <dt>srcdir</dt><dd>specifies the directory containing files to preprocess
 *          if no nested <code>fileset</code> element is present.</dd>
 *      <dt>destdir</dt><dd>specifies the directory where preprocessed files should be created</dd>
 *      <dt>encoding</dt><dd>specifies character encoding of input (and output) files</dd>
 *      <dt>includepath</dt><dd>semicolon separated list of directories which will be searched for included files.</dd>
 *      <dt>messages</dt><dd>boolean value switching displaying of developement messages - todo, debug and other tags.</dd>
 *      <dt>preservelines</dt><dd>boolean value specifying if generated files should preserve line numbering of their sources.</dd>
 *      <dt>loglevel</dt><dd>amount of logging that JPP will produce; may be between 0 (silent) to 6 (debug). Default level is 4(info).</dd>
 *      <dt>defineFile</dt><dd>a properites file from which defined macros will be read, instead of finding a default one in classpath.</dd>
 *      <dt>force</dt><dd>always replace the output files, not comparing it's modification date with the input file.</dd>
 *  </dl>
 *  Supported nested elements:
 *  <dl>
 *      <dt>fileset</dt><dd>The task may contain one <code>fileset</code> element, with a specified base directory,
 *          which defines the files which will be preprocessed.</dd>
 *      <dt>define</dt><dd> Defines a macro. Takes two attributes: <code>name</code>, which is required,
 *          and optional <code>body</code>.
 *          Each element &lt;define name="macro" value="body" &gt; defines a macro named "macro" with an
 *          (optional) value equal to "body".</dd>
 *      <dt>filetype</dt><dd> Registers a new file type. It will be associated with the
 *          extention specified by the required <code>extention</code> attribute. You also have to provide
 *          either
 *      <ul>
 *          <li> a <code>sniffer</code> attribute with a fully qualified name of a <code>CommentSniffer</code> class
 *          that will be used to find comments in files of this type,</li>
 *          <li> or a <code>linetag</code> wich value is the line comment tag in these files - in this case a
 *          {@link LineCommentParser LineCommentParser} will be used,</li>
 *          <li> or <code>openttag</code> and <code>closetag</code> attributes, which specify the tags which
 *          denote the begin of a comment and the end of it.</li>
 *      </ul>
 *      The attributes are considered in this order.
 *      </dd>
 *  </dl>
 *  @author Marcin Moscicki
 */
public class JPPTask extends Task {

    private FileSet src;

    private String includePath = ".";

    private boolean messagesEnabled = false;

    private boolean force = false;

    private boolean preservelines = true;

    private String encoding;

    private String destdir;

    private String srcdir;

    private String defineFile;

    private int loglevel = Logger.LEVEL_INFO;

    private List defines;

    private List filetypes;

    public JPPTask() {
        defines = new ArrayList();
        filetypes = new ArrayList();
    }

    public void setIncludepath(String includePath) {
        this.includePath = includePath;
    }

    public void setMessages(boolean enabled) {
        this.messagesEnabled = enabled;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public void setPreservelines(boolean preserve) {
        this.preservelines = preserve;
    }

    public void setDestdir(String destdir) {
        this.destdir = destdir;
    }

    public void setSrcdir(String srcdir) {
        this.srcdir = srcdir;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setLoglevel(int loglevel) {
        this.loglevel = loglevel;
    }

    public void setDefineFile(String defineFile) {
        this.defineFile = defineFile;
    }

    public void addConfiguredFileset(FileSet fileSet) {
        src = fileSet;
    }

    public void addConfiguredDefine(DefineElement define) {
        defines.add(define);
    }

    public void addConfiguredFiletype(FiletypeElement filetype) {
        filetypes.add(filetype);
    }

    public void execute() throws BuildException {
        JPP jpp = new JPP();
        String[] srcFiles = null;
        if (src != null) {
            DirectoryScanner ds = src.getDirectoryScanner(getProject());
            srcFiles = ds.getIncludedFiles();
            srcdir = ds.getBasedir().getPath();
        }
        Logger log = new Logger(loglevel);
        jpp.setLogger(log);
        jpp.setDefineBundleFile(defineFile);
        if (filetypes.size() != 0) {
            CommentSnifferFactory factory = new CommentSnifferFactory(log);
            for (Iterator iterator = filetypes.iterator(); iterator.hasNext(); ) {
                FiletypeElement filetype = (FiletypeElement) iterator.next();
                if (filetype.getExtention() == null) {
                    System.err.println("<filetype> element must have an extention attribute");
                    continue;
                }
                if (filetype.getSniffer() != null) {
                    try {
                        CommentSniffer cf = (CommentSniffer) Class.forName(filetype.getSniffer()).newInstance();
                        factory.registerCommentSniffer(filetype.getExtention(), cf);
                    } catch (Exception e) {
                        System.err.println("Failed to register CommentSniffer \"" + filetype.getSniffer() + "\" for file extention " + filetype.getExtention());
                        System.err.println("Exception was: " + e);
                    }
                } else if (filetype.getLinetag() != null && filetype.getLinetag().length() > 0) {
                    factory.registerCommentSniffer(filetype.getExtention(), new LineCommentParser(filetype.getLinetag()));
                } else if (filetype.getOpentag() != null && filetype.getOpentag().length() > 0 && filetype.getClosetag() != null && filetype.getClosetag().length() > 0) {
                    factory.registerCommentSniffer(filetype.getExtention(), new BlockCommentParser(filetype.getOpentag(), filetype.getClosetag()));
                } else {
                    System.err.println("<filetype> element must have either a sniffer, linetag, or opentag and close tag attributes");
                }
            }
        }
        for (Iterator iterator = defines.iterator(); iterator.hasNext(); ) {
            DefineElement defineElement = (DefineElement) iterator.next();
            jpp.define(defineElement.getName(), defineElement.getValue());
        }
        jpp.setIncludePath(includePath);
        jpp.setMessagesEnabled(messagesEnabled);
        jpp.setPreserveLineNumbering(preservelines);
        jpp.setEncoding(encoding);
        try {
            jpp.process(srcFiles, null, srcdir, destdir, force);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
