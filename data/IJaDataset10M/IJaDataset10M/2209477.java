package egu.plugin.util.implementation;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.ast.DOMException;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.index.IIndexFileLocation;
import org.eclipse.cdt.core.index.IIndexManager;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModelUtil;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.IFunctionDeclaration;
import org.eclipse.cdt.core.model.IMethodDeclaration;
import org.eclipse.cdt.core.model.IStructure;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.make.core.makefile.IMacroDefinition;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import egu.plugin.Makefile.IMacroList;
import egu.plugin.Makefile.IMainProgramRule;
import egu.plugin.Makefile.ITranslationUnitRule;
import egu.plugin.Makefile.MacroList;
import egu.plugin.Makefile.MainProgramRule;
import egu.plugin.Makefile.TranslationUnitRule;
import egu.plugin.dialog.CollaboratorDialog;
import egu.plugin.mocker.MethodModifiable;
import egu.plugin.mocker.SelectCollaboratorFromFunction;
import egu.plugin.mocker.SelectCollaboratorFromStructure;
import egu.plugin.mocker.Interface.IListMockClass;
import egu.plugin.mocker.Interface.IMockClass;
import egu.plugin.mocker.Interface.IMockableClass;
import egu.plugin.mocker.Interface.ISelectCollaborator;
import egu.plugin.mocker.implementation.ListMockClass;
import egu.plugin.mocker.implementation.MockClass;
import egu.plugin.preference.IPropertiesValues;
import egu.plugin.preference.PropertiesValues;
import egu.plugin.runnable.RunnableCreateUnitTestClass;
import egu.plugin.testSource.GoogleTestUnitClass;
import egu.plugin.testSource.TestFileSource;
import egu.plugin.util.ASTFunctionCollector;
import egu.plugin.util.ASTFunctionDefCollector;
import egu.plugin.util.ASTINamecollector;
import egu.plugin.util.LocationFunctionCollector;
import egu.plugin.util.interfaces.IASTCollaboratorCollector;
import egu.plugin.util.interfaces.ICollaborators;
import egu.plugin.util.interfaces.IFactory;
import egu.plugin.util.interfaces.IListExternReference;
import egu.plugin.util.interfaces.IListInclude;
import egu.plugin.util.interfaces.IModel2AST;
import egu.plugin.util.interfaces.ISourceFile;
import egu.plugin.util.interfaces.IUpdateAST;
import egu.plugin.util.interfaces.IUtilInclude;
import egu.plugin.util.interfaces.IUtilIndex;

public class Factory implements IFactory {

    private Shell shell;

    public Factory(Shell shell) {
        this.shell = shell;
    }

    public IIndexManager getIndexManager() {
        return CCorePlugin.getIndexManager();
    }

    public IListInclude getListInclude(ITranslationUnit sourceTranslationUnit) throws CoreException {
        return new ListInclude(sourceTranslationUnit);
    }

    public IPropertiesValues getPropertyValues(IProject project) {
        return new PropertiesValues(project);
    }

    @Override
    public IListExternReference getListExternReference(ITranslationUnit sourceFile, IIndexManager indexManager) throws CoreException, InterruptedException {
        return new listExternReference(sourceFile, indexManager, this);
    }

    @Override
    public ITranslationUnitRule getTranslationUnitRule(ITranslationUnit sourceTranslationUnit, IPath makefilePath) throws CoreException {
        return new TranslationUnitRule(sourceTranslationUnit, makefilePath, this);
    }

    @Override
    public String getString(Object obj) {
        return obj.toString();
    }

    @Override
    public IMainProgramRule getMainProgramRule() {
        return new MainProgramRule();
    }

    @Override
    public ISourceFile getSourceFile(ICElement source) {
        return new SourceFile(source);
    }

    @Override
    public ISourceFile getSourceFile(IPath source) {
        return new SourceFile(source);
    }

    @Override
    public IMacroList getMacroList(String name) {
        return new MacroList(name);
    }

    @Override
    public IMacroList getMacroList(IMacroDefinition macro) {
        return new MacroList(macro);
    }

    @Override
    public IMockClass getMockClass(IMockableClass mockableClass) {
        return new MockClass(mockableClass);
    }

    @Override
    public IUtilInclude getUtilInclude(IIndex index) {
        return new utilInclude(index);
    }

    @Override
    public ICollaborators getCollaborators() {
        return new Collaborators();
    }

    @Override
    public ASTINamecollector getASTINamecollector() {
        return new ASTINamecollector();
    }

    @Override
    public CollaboratorDialog getCollaboratorDialog(ICollaborators collaborators) {
        return new CollaboratorDialog(shell, collaborators);
    }

    @Override
    public MessageBox getMessageBox(int style) {
        return new MessageBox(shell, style);
    }

    @Override
    public IASTCollaboratorCollector getIASTCollaboratorCollector() {
        return new ASTCollaboratorCollector();
    }

    @Override
    public IModel2AST getIModel2AST() {
        return new Model2AST(this);
    }

    @Override
    public ASTFunctionCollector getASTFunctionCollector(IFunctionDeclaration funcDecl) throws CModelException {
        return new ASTFunctionCollector(funcDecl);
    }

    @Override
    public IUtilIndex getutilIndex() {
        return new utilIndex(this);
    }

    @Override
    public MethodModifiable getMethodModifiable(IASTFileLocation indexName, String fileName, ICProject project) throws CoreException, DOMException {
        return new MethodModifiable(indexName, fileName, project, this);
    }

    @Override
    public IWorkbench getIWorkbench() {
        return PlatformUI.getWorkbench();
    }

    @Override
    public IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    @Override
    public FileEditorInput getFileEditorInput(IFile file) {
        return new FileEditorInput(file);
    }

    @Override
    public LocationFunctionCollector getLocationFunctionCollector(IASTFileLocation fileLocation) {
        return new LocationFunctionCollector(fileLocation);
    }

    @Override
    public RunnableCreateUnitTestClass getRunnableCreateUnitTestClass(IStructure classToTest, ISelectCollaborator selectedCollaborator) {
        return new RunnableCreateUnitTestClass(classToTest, selectedCollaborator, this);
    }

    @Override
    public ProgressMonitorDialog getProgressMonitorDialog() {
        return new ProgressMonitorDialog(shell);
    }

    @Override
    public GoogleTestUnitClass gettestClass(String className) {
        return new GoogleTestUnitClass(className);
    }

    @Override
    public TestFileSource getTestFileSource(ICElement classToTest, IListMockClass mockClass) throws CoreException {
        return new TestFileSource(classToTest, mockClass, this);
    }

    @Override
    public ListMockClass getListMockClass(ISelectCollaborator selectCollaborator) {
        return new ListMockClass(selectCollaborator, this);
    }

    @Override
    public ISelectCollaborator getSelectCollaborator(IStructure cElement) throws DOMException, InterruptedException, CoreException, BadLocationException {
        return new SelectCollaboratorFromStructure(cElement, this);
    }

    @Override
    public ISelectCollaborator getSelectCollaborator(IFunctionDeclaration funcDecl) throws DOMException, InterruptedException, CoreException, BadLocationException {
        return new SelectCollaboratorFromFunction(funcDecl, this);
    }

    @Override
    public IUpdateAST getUpdateAST() {
        return new UpdateAST(this);
    }

    @Override
    public ITranslationUnit findTranslationUnitForLocation(IIndexFileLocation indexFileLocation, ICProject cProject) throws CModelException {
        return CoreModelUtil.findTranslationUnitForLocation(indexFileLocation, cProject);
    }

    @Override
    public ASTFunctionDefCollector getASTFunctionDefCollector(IMethodDeclaration funcDecl) throws CModelException {
        return new ASTFunctionDefCollector(funcDecl, this);
    }
}
