package nl.romme.tools.metacheck.stylist;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * A summarizing stylist, to create the toplevel index.html and top.html.
 *
 * @author Johan Romme
 * @version $Revision: 1.1.1.1 $
 */
public class ReportStylist extends Task {

    private transient PrintWriter writer;

    private String checkstyle;

    private String cpd;

    private String findbugs;

    private String hammurapi;

    private String jcsc;

    private String lint4j;

    private String pmd;

    private String qjpro;

    private String todir;

    /**
     * Setter for checkstyle.
     *
     * @param checkstyle The checkstyle to set.
     */
    public final void setCheckstyle(final String checkstyle) {
        this.checkstyle = checkstyle;
    }

    /**
     * Getter for checkstyle.
     *
     * @return Returns the checkstyle.
     */
    public final String getCheckstyle() {
        return checkstyle;
    }

    /**
     * Setter for cpd.
     *
     * @param cpd The cpd to set.
     */
    public final void setCpd(final String cpd) {
        this.cpd = cpd;
    }

    /**
     * Getter for cpd.
     *
     * @return Returns the cpd.
     */
    public final String getCpd() {
        return cpd;
    }

    /**
     * Setter for findbugs.
     *
     * @param findbugs The findbugs to set.
     */
    public final void setFindbugs(final String findbugs) {
        this.findbugs = findbugs;
    }

    /**
     * Getter for findbugs.
     *
     * @return Returns the findbugs.
     */
    public final String getFindbugs() {
        return findbugs;
    }

    /**
     * Setter for hammurapi.
     *
     * @param hammurapi The hammurapi to set.
     */
    public final void setHammurapi(String hammurapi) {
        this.hammurapi = hammurapi;
    }

    /**
     * Getter for hammurapi.
     *
     * @return Returns the hammurapi.
     */
    public final String getHammurapi() {
        return hammurapi;
    }

    /**
     * Setter for jcsc.
     *
     * @param jcsc The jcsc to set.
     */
    public final void setJcsc(String jcsc) {
        this.jcsc = jcsc;
    }

    /**
     * Getter for jcsc.
     *
     * @return Returns the jcsc.
     */
    public final String getJcsc() {
        return jcsc;
    }

    /**
     * Setter for lint4j.
     *
     * @param lint4j The lint4j to set.
     */
    public final void setLint4j(String lint4j) {
        this.lint4j = lint4j;
    }

    /**
     * Getter for lint4j.
     *
     * @return Returns the lint4j.
     */
    public final String getLint4j() {
        return lint4j;
    }

    /**
     * Setter for pmd.
     *
     * @param pmd The pmd to set.
     */
    public final void setPmd(final String pmd) {
        this.pmd = pmd;
    }

    /**
     * Getter for pmd.
     *
     * @return Returns the pmd.
     */
    public final String getPmd() {
        return pmd;
    }

    /**
     * Setter for qjpro.
     *
     * @param qjpro The qjpro to set.
     */
    public final void setQjpro(String qjpro) {
        this.qjpro = qjpro;
    }

    /**
     * Getter for qjpro.
     *
     * @return Returns the qjpro.
     */
    public final String getQjpro() {
        return qjpro;
    }

    /**
     * Setter for todir.
     *
     * @param todir The todir to set.
     */
    public final void setTodir(final String todir) {
        this.todir = todir;
    }

    /**
     * Getter for todir.
     *
     * @return Returns the todir.
     */
    public final String getTodir() {
        return todir;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws BuildException DOCUMENT ME!
     */
    public void execute() throws BuildException {
        try {
            writer = new PrintWriter(new FileWriter(todir + "/index.html"));
            writer.println("<html>");
            writer.println("<frameset rows='25,*' border='0'/>");
            writer.println("<frame name='top' src='top.html'/>");
            writer.println("<frame name='rep' />");
            writer.println("<frameset>");
            writer.println("<html>");
            writer.close();
            writer = new PrintWriter(new FileWriter(todir + "/top.html"));
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<style type='text/css' media='all'>");
            writer.println("@import url('./meta/style/maven-base.css');");
            writer.println("@import url('./meta/style/maven-theme.css');");
            writer.println("</style>");
            writer.println("</head>");
            writer.println("<body bgcolor='#ccffcc'>");
            writer.println("<table width='100%'><tr>");
            if (null == todir) {
                throw new BuildException("no todir specified");
            }
            if (null != jcsc) {
                JcscStylist.exec(jcsc, addTask("JCSC", todir + "/jcsc"));
            }
            if (null != cpd) {
                CpdStylist.exec(cpd, addTask("CPD", todir + "/cpd"));
            }
            if (null != pmd) {
                PmdStylist.exec(pmd, addTask("PMD", todir + "/pmd"));
            }
            if (null != checkstyle) {
                CheckStyleStylist.exec(checkstyle, addTask("CheckStyle", todir + "/checkstyle"));
            }
            if (null != lint4j) {
                Lint4jStylist.exec(lint4j, addTask("Lint4j", todir + "/lint4j"));
            }
            if (null != findbugs) {
                FindbugsStylist.exec(findbugs, addTask("Findbugs", todir + "/findbugs"));
            }
            if (null != hammurapi) {
                HammurapiStylist.exec(hammurapi, addTask("Hammurapi", todir + "/hammurapi"));
            }
            if (null != qjpro) {
                QJProStylist.exec(qjpro, addTask("QJ-Pro", todir + "/qjpro"));
            }
            MetaStylist.execute(addTask("META", todir + "/meta"));
            writer.println("</tr></table>");
            writer.println("</body>");
            writer.println("</html>");
            writer.close();
        } catch (IOException e) {
            throw new BuildException("io error" + e);
        }
    }

    private String addTask(final String name, final String sdir) {
        File dir = new File(sdir);
        if (dir.exists()) {
        } else if (dir.mkdirs()) {
        } else {
            throw new BuildException("create dir " + sdir + " failed");
        }
        if (!dir.isDirectory()) {
            throw new BuildException("open dir " + sdir + " failed");
        }
        writer.println(" <td align='center'><b><a target='rep' href='file://" + sdir.replace('\\', '/') + "/index.html'>" + name + "</a></b></td>");
        return sdir;
    }
}
