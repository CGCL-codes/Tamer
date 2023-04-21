package coffea.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.operation.IRunnableWithProgress;
import coffea.actors.CodeModelWorker;
import coffea.actors.JavaCodeWorker;
import coffea.tools.UnpreparedToolsException;

/**
 * Tool for reading a <a href="http://www.java.net">Java</a> 
 * {@link IJavaProject project} in the workspace. Processing the types 
 * through a syntax tree (provided by the {@link ASTParser eclipse parser}),  
 * this tool delegates the {@link org.eclipse.uml2.uml.Model UML model} 
 * management to {@link #worker a model worker}. 
 */
public class CodeProcessor {

    /** The <em>Java</em> project to read */
    protected IJavaProject javaProject;

    /** 
	 * Worker linking the processed <a href="http://www.java.net">Java</a> 
	 * code to an {@link org.eclipse.uml2.uml.Model UML model}
	 * @see CodeModelWorker#getModelHandler()
	 */
    protected CodeModelWorker worker;

    /** 
	 * Code worker glasses construction 
	 * @param w
	 * Value of {@link #worker}
	 */
    public CodeProcessor(CodeModelWorker w) {
        if (w != null) {
            worker = w;
        } else {
            throw new IllegalArgumentException(new String() + w);
        }
    }

    /**
	 * Process an element of the file system (file or directory
	 * @param elm
	 * 	The element to be processed
	 */
    public void process(File elm) {
        this.readElement(elm, null);
    }

    /**
	 * Read an element of the file system (file or directory)
	 * @param elm
	 *            The element to be read
	 * @param container
	 *            Container directory
	 */
    protected void readElement(File elm, File container) {
        if (elm.isFile()) this.readFile(elm, container); else if (elm.isDirectory()) this.readDirectory(elm, container);
    }

    /**
	 * Read a directory
	 * @param dir
	 *            Directory to be read
	 * @param container
	 *            Container directory
	 *            to be read, <code>null</code> if it's in the root
	 *            <em>package</em>
	 */
    protected void readDirectory(File dir, File container) {
        if (dir.isDirectory()) {
            File[] content = dir.listFiles();
            for (int i = 0; i < content.length; i++) {
                File elm = content[i];
                this.readElement(elm, dir);
            }
        } else throw new IllegalArgumentException();
    }

    /**
	 * Read a file
	 * 
	 * @param fil
	 *            File to be read
	 * @param container
	 *            Directory containing the file to be read,
	 *            <code>null</code> if it's in the root <em>package</em>
	 */
    protected void readFile(File fil, File container) {
        if (fil.getName().lastIndexOf(JavaCodeWorker.javaFileSuffix) != -1) {
            try {
                this.readJavaFile(fil, container);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (UnpreparedToolsException e) {
                System.err.println("The manufacturers tools cannot be prepared ! Please " + "take care of any of the given parameters.");
                e.printStackTrace();
            }
        }
    }

    /**
	 * Parses a Java source file
	 * @param parseFile
	 * 	The Java source file to parse
	 * @param container
	 * 	Container directory
	 * @throws IllegalArgumentException 
	 * 	If the file cannot be read (the specified <em>filJ</em> parameter is 
	 * 	not a file or the user account executing the program is not allowed to 
	 * 	read it)
	 * @throws UnpreparedToolsException
	 * 	If the manufacturers tool have not been prepared and it doesn't seem 
	 * 	possible to prepare them
	 */
    protected void readJavaFile(File parseFile, File container) throws IllegalArgumentException, UnpreparedToolsException {
        IRunnableWithProgress runnable = new Parser(parseFile);
        try {
            worker.getWorkbenchWindow().run(false, true, runnable);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** The AST parser runnable */
    class Parser implements IRunnableWithProgress {

        /** The <em>Java</em> file to parse */
        protected File parseFile;

        /** 
		 * AST parser runnable construction
		 * @param jFile
		 * 	Value of {@link #parseFile}
		 */
        Parser(File jFile) {
            parseFile = jFile;
        }

        public void run(IProgressMonitor monitor) {
            try {
                monitor.beginTask(parseFile.getName(), 6);
                IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(worker.getCoffeeName());
                monitor.worked(1);
                if ((parseFile.isFile()) && (parseFile.canRead())) {
                    try {
                        FileReader flReader = new FileReader(parseFile);
                        monitor.worked(2);
                        char[] readPart = new char[5000];
                        StringBuffer buffer = new StringBuffer();
                        int nbRead;
                        try {
                            while ((nbRead = flReader.read(readPart, 0, readPart.length)) >= 0) {
                                buffer.append(readPart, 0, nbRead);
                            }
                            flReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if ((javaProject == null) || (!javaProject.getElementName().equals(worker.getCoffeeName()))) {
                                javaProject = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
                                if (javaProject == null) {
                                    javaProject = JavaCore.create(project);
                                }
                                javaProject.open(null);
                            }
                        } catch (JavaModelException e1) {
                            e1.printStackTrace();
                        } catch (CoreException e2) {
                            e2.printStackTrace();
                        }
                        monitor.worked(3);
                        if (javaProject != null) {
                            ASTParser parser = ASTParser.newParser(AST.JLS3);
                            parser.setProject(javaProject);
                            parser.setResolveBindings(true);
                            char[] charArray = new char[buffer.length()];
                            buffer.getChars(0, buffer.length(), charArray, 0);
                            parser.setUnitName(javaProject.getElementName());
                            parser.setSource(charArray);
                            monitor.worked(4);
                            CompilationUnit result = (CompilationUnit) parser.createAST(null);
                            monitor.worked(5);
                            List<?> types = (List<?>) result.types();
                            for (int i = 0; i < types.size(); i++) {
                                ASTNode node = (ASTNode) types.get(i);
                                if (node instanceof TypeDeclaration) {
                                    TypeDeclaration type = (TypeDeclaration) node;
                                    try {
                                        worker.getJavaWorker().processType(type, result);
                                    } catch (UnpreparedToolsException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            monitor.worked(6);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else throw new IllegalArgumentException();
            } finally {
                monitor.done();
            }
        }
    }
}
