package com.pallas.unicore.client.plugins.compile;

import java.util.logging.Level;
import org.unicore.ajo.ActionGroup;
import org.unicore.ajo.DeletePortfolio;
import org.unicore.ajo.Dependency;
import org.unicore.ajo.DoubleLength;
import org.unicore.ajo.FortranSourceForm;
import org.unicore.ajo.FortranTask;
import org.unicore.ajo.IntegerLength;
import org.unicore.ajo.LinkTask;
import org.unicore.ajo.MakePortfolio;
import org.unicore.ajo.PreprocessLevel;
import org.unicore.ajo.RealLength;
import org.unicore.sets.OptionSet;
import org.unicore.sets.PortfolioEnumeration;
import org.unicore.sets.PortfolioList;
import org.unicore.sets.PortfolioSet;
import org.unicore.sets.ResourceSet;
import com.pallas.unicore.container.CompileContainer;
import com.pallas.unicore.container.GroupContainer;
import com.pallas.unicore.extensions.NamedResourceSet;
import com.pallas.unicore.resourcemanager.ResourceManager;

/**
 * @author Thomas Kentemich
 * @version $Id: FortranContainer.java,v 1.1 2004/05/25 14:58:47 rmenday Exp $
 */
public class FortranContainer extends CompileContainer {

    static final long serialVersionUID = 4983481768961721914L;

    private boolean checkArrayBounds;

    private boolean checkArguments;

    private PreprocessLevel preprocessLevel;

    private IntegerLength integerLength;

    private RealLength realLength;

    private DoubleLength doubleLength;

    private FortranSourceForm sourceForm;

    private OptionSet defines;

    private OptionSet undefines;

    public FortranContainer(GroupContainer parentContainer) {
        super(parentContainer);
    }

    public void setCheckArrayBounds(boolean check) {
        this.checkArrayBounds = check;
    }

    public void setCheckArguments(boolean check) {
        this.checkArguments = check;
    }

    public void setPreprocessLevel(PreprocessLevel level) {
        this.preprocessLevel = level;
    }

    public void setIntegerLength(IntegerLength len) {
        this.integerLength = len;
    }

    public void setRealLength(RealLength len) {
        this.realLength = len;
    }

    public void setDoubleLength(DoubleLength len) {
        this.doubleLength = len;
    }

    public void setSourceForm(FortranSourceForm form) {
        this.sourceForm = form;
    }

    public void setDefines(OptionSet defines) {
        this.defines = defines;
    }

    public void setUndefines(OptionSet undefines) {
        this.undefines = undefines;
    }

    public boolean getCheckArrayBounds() {
        return checkArrayBounds;
    }

    public boolean getCheckArguments() {
        return checkArguments;
    }

    public PreprocessLevel getPreprocessLevel() {
        return preprocessLevel;
    }

    public IntegerLength getIntegerLength() {
        return integerLength;
    }

    public RealLength getRealLength() {
        return realLength;
    }

    public DoubleLength getDoubleLength() {
        return doubleLength;
    }

    public FortranSourceForm getSourceForm() {
        return sourceForm;
    }

    public OptionSet getDefines() {
        return defines;
    }

    public OptionSet getUndefines() {
        return undefines;
    }

    protected void buildExecuteGroup() {
        if (getSources() == null) {
            return;
        }
        executeGroup = new ActionGroup(getName() + "_EXECUTION");
        NamedResourceSet taskResourceSet = getResourceSet();
        ResourceSet uspaceResourceSet = new ResourceSet(taskResourceSet.getUspace());
        MakePortfolio makePortfolio = new MakePortfolio(getName() + "_Sources");
        makePortfolio.setFiles(getSources());
        String[] includes = getIncludes();
        MakePortfolio includePortfolio = null;
        PortfolioList pList = null;
        if (includes != null && includes.length > 0) {
            includePortfolio = new MakePortfolio(getName() + "_Includes");
            includePortfolio.setFiles(includes);
            pList = new PortfolioList(includePortfolio.getPortfolio());
        }
        FortranTask compileTask = new FortranTask(getName(), null, taskResourceSet.getResourceSet(), getEnv(), getCommandLine(), null, getRedirectStdout(), getRedirectStderr(), isVerboseOn(), isVersionOn(), new PortfolioSet(makePortfolio.getPortfolio()), getListingLevel(), getOptimisationLevel(), pList, getCompileForDebug(), getCompileForProfiling(), getCheckArrayBounds(), getCheckArguments(), getIntegerLength(), getRealLength(), getDoubleLength(), getSourceForm(), getPreprocessLevel(), getDefines(), getUndefines());
        this.setObjectsPortfolio(compileTask.getObjects());
        executeGroup.add(makePortfolio);
        executeGroup.add(compileTask);
        try {
            if (includes != null && includes.length > 0) {
                executeGroup.add(includePortfolio);
                executeGroup.addDependency(new Dependency(includePortfolio, compileTask));
            }
            executeGroup.addDependency(new Dependency(makePortfolio, compileTask));
            if (getRedirectStdout()) {
                compileTask.setStdoutFileName(this.getStdout());
                DeletePortfolio delStdoutPortfolio = new DeletePortfolio(getName() + "_DELETE_STDOUT_" + ResourceManager.getNextObjectIdentifier());
                delStdoutPortfolio.setPortfolio(compileTask.getStdout().getId());
                executeGroup.add(delStdoutPortfolio);
                executeGroup.addDependency(new Dependency(compileTask, delStdoutPortfolio));
            }
            if (getRedirectStderr()) {
                compileTask.setStderrFileName(this.getStderr());
                DeletePortfolio delStderrPortfolio = new DeletePortfolio(getName() + "_DELETE_STDERR_" + ResourceManager.getNextObjectIdentifier());
                delStderrPortfolio.setPortfolio(compileTask.getStderr().getId());
                executeGroup.add(delStderrPortfolio);
                executeGroup.addDependency(new Dependency(compileTask, delStderrPortfolio));
            }
            MakePortfolio stdinPortfolio = null;
            if (getRedirectStdin()) {
                String[] stdinfiles = { this.getStdin() };
                stdinPortfolio = new MakePortfolio(getName() + "_MP_STDIN_" + ResourceManager.getNextObjectIdentifier(), null, uspaceResourceSet, stdinfiles);
                compileTask.setStdin(stdinPortfolio.getPortfolio());
                executeGroup.add(stdinPortfolio);
                executeGroup.addDependency(new Dependency(stdinPortfolio, compileTask));
            }
            if (this.getObjects() != null) {
                MakePortfolio makeObjectPortfolio = new MakePortfolio(getName() + "_Objects");
                PortfolioEnumeration pEnum = getObjects().elements();
                while (pEnum.hasMoreElements()) {
                    makeObjectPortfolio.addFile(pEnum.nextElement().getName());
                }
                LinkTask linkTask = new LinkTask(getName(), null, taskResourceSet.getResourceSet(), getEnv(), getCommandLine(), null, getRedirectStdout(), getRedirectStderr(), isVerboseOn(), isVersionOn(), new PortfolioList(makeObjectPortfolio.getPortfolio()), getLibraries(), getLinkMapLevel(), getLinkForDebug(), getLinkForProfiling());
                if (getRedirectStdout()) {
                    linkTask.setStdoutFileName(this.getStdout());
                }
                if (getRedirectStderr()) {
                    linkTask.setStderrFileName(this.getStderr());
                }
                if (getRedirectStdin()) {
                    linkTask.setStdin(stdinPortfolio.getPortfolio());
                }
                executeGroup.add(makeObjectPortfolio);
                executeGroup.add(linkTask);
                executeGroup.addDependency(new Dependency(compileTask, makeObjectPortfolio));
                executeGroup.addDependency(new Dependency(makeObjectPortfolio, linkTask));
            }
        } catch (ActionGroup.InvalidDependencyException ex) {
            logger.log(Level.SEVERE, "Could not add dependency.", ex);
        }
    }
}
