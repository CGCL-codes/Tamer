package org.deft.repository;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Observer;
import java.util.UUID;
import org.deft.repository.ast.TreeNode;
import org.deft.repository.ast.annotation.Format;
import org.deft.repository.ast.annotation.IFormatChangedListener;
import org.deft.repository.ast.annotation.Ident;
import org.deft.repository.ast.annotation.Format.DisplayType;
import org.deft.repository.exception.DeftCrossProjectRelationException;
import org.deft.repository.exception.DeftFragmentAlreadyExistsException;
import org.deft.repository.exception.DeftIllegalFragmentTreeException;
import org.deft.repository.exception.DeftIllegalRevisionException;
import org.deft.repository.exception.DeftMultipleParserException;
import org.deft.repository.exception.DeftParseException;
import org.deft.repository.fragment.Chapter;
import org.deft.repository.fragment.CodeFile;
import org.deft.repository.fragment.CodeSnippet;
import org.deft.repository.fragment.EmbeddableFragment;
import org.deft.repository.fragment.Folder;
import org.deft.repository.fragment.Fragment;
import org.deft.repository.fragment.HierarchyFragment;
import org.deft.repository.fragment.IFragmentFilter;
import org.deft.repository.fragment.Image;
import org.deft.repository.fragment.Project;
import org.deft.repository.fragment.Tutorial;
import org.deft.repository.query.Query;
import org.deft.repository.xfsr.reference.CodeSnippetRef;
import org.deft.repository.xfsr.reference.ImageRef;
import org.deft.repository.xfsr.reference.Reference;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface IRepository {

    /**
	 * Returns an object that contains options for the repository, such as the root path
	 * for a file based repository or database connection information for a database 
	 * repository.
	 * 
	 * @return the option object for the repository
	 */
    public IRepositoryOptions getRepositoryOptions();

    /**
	 * Returns the project to which the fragment belongs.
	 * 
	 * @param fragment
	 *            the fragment for which to get the Project
	 *            
	 * @throws IllegalArgumentException if fragment 
	 * is <code>null</code> or not registered with the repository
	 *            
	 * @return the Project of the fragment
	 * 
	 */
    public Project getProjectOf(Fragment fragment);

    /**
	 * Returns a list of all Projects that are stored in the repository.
	 * If there are no Projects, an empty list is returned.
	 * 
	 * @return a List of all Projects in the repository or an empty list
	 */
    public List<Project> getProjects();

    /**
	 * Checks whether there is a Project with the given name in the repository.
	 * 
	 * @param name the name to check
	 * 
	 * @throws IllegalArgumentException if name is <code>null</code>
	 * 
	 * @return <code>true</code> if a Project with that name exists, otherwise
	 *         <code>false</code>
	 */
    public boolean projectExists(String name);

    /**
	 * Checks whether there are any Projects in the repository.
	 * 
	 * @return <code>true</code> if there is at least one Project, otherwise
	 *         <code>false</code>
	 */
    public boolean hasProjects();

    /**
	 * Checks whether there is a Chapter with the given name in the Project.
	 * 
	 * @param project the Project whose Chapters are to be checked
	 * @param name the name of the Chapter
	 *            
	 * @throws IllegalArgumentException if <code>project</code> or
	 * 			<code>name</code> are <code>null</code>
	 *             
	 * @return <code>true</code> if there is a Chapter with that name in the
	 *         Project, otherwise <code>false</code>
	 */
    public boolean chapterExists(Project project, String name);

    /**
	 * Checks whether there is a Tutorial with the given name in the Project.
	 * 
	 * @param project the Project whose Tutorials are to be checked
	 * @param name the name of the Tutorial
	 * 
	 * @throws IllegalArgumentException if <code>project</code> or
	 * 			<code>name</code> are <code>null</code>
	 *  
	 * @return <code>true</code> if there is a Tutorial with that name in the
	 *         Project, otherwise <code>false</code>
	 */
    public boolean tutorialExists(Project project, String name);

    /**
	 * Checks whether there is a Chapter with the given name in the Tutorial.
	 * 
	 * @param tutorial the Tutorial whose Chapters are to be checked
	 * @param name the name of the Chapter
	 * 
	 * @throws IllegalArgumentException if <code>tutorial</code> or
	 * 			<code>name</code> are <code>null</code>
	 *  
	 * @return <code>true</code> if there is a Chapter with that name in the
	 *         Tutorial, otherwise <code>false</code>
	 */
    public boolean tutorialChapterExists(Tutorial tutorial, String name);

    /**
	 * Checks whether there is a CodeFile with the given name in a HierarchyFragment
	 * (e.g. Category or a certain Folder).
	 * 
	 * @param hierarchyFragment the HierarchyFragment to be checked for the CodeFile 
	 * @param name the name of the CodeFile
	 * 
	 * @throws IllegalArgumentException if <code>hierarchyFragment</code> or
	 * 			<code>name</code> are <code>null</code> 
	 * 
	 * @return <code>true</code> if there is a CodeFile with that name in
	 * the HierarchyFragment, otherwise <code>false</code>.
	 */
    public boolean codeFileExists(HierarchyFragment hierarchyFragment, String name);

    /**
	 * Checks whether there is a Folder with the given name in a HierarchyFragment
	 * (e.g. Category or another Folder).
	 * 
	 * @param hierarchyFragment HierarchyFragment to be checked for the Folder 
	 * @param name the name of the Folder
	 * 
	 * @throws IllegalArgumentException if <code>hierarchyFragment</code> or
	 * 			<code>name</code> are <code>null</code> 
	 * 
	 * @return <code>true</code> if there is a Folder with that name in
	 * the HierarchyFragment, otherwise <code>false</code>.
	 */
    public boolean folderExists(HierarchyFragment hierarchyFragment, String name);

    /**
	 * Checks whether there is a CodeSnippet with the given name in the Project.
	 * 
	 * @param project
	 *            the Project whose CodeSnippets are to be checked
	 * @param name
	 *            the name of the CodeSnippet
	 *            
	 * @throws IllegalArgumentException if <code>project</code> or
	 * 			<code>name</code> are <code>null</code> 
	 *             
	 * @return <code>true</code> if there is a CodeSnippet with that name in
	 *         the Project, otherwise <code>false</code>
	 */
    public boolean codeSnippetExists(Project project, String name);

    /**
	 * Checks whether there is an Image with the given name in a HierarchyFragment
	 * (e.g. Category or another Folder).
	 * 
	 * @param hierarchyFragment the HierarchyFragment to be checked for the Image 
	 * @param name the name of the Image
	 * 
	 * @throws IllegalArgumentException if <code>hierarchyFragment</code> or
	 * 			<code>name</code> are <code>null</code> 
	 * 
	 * @return <code>true</code> if there is an Image with that name in
	 * 			the HierarchyFragment, otherwise <code>false</code>.
	 */
    public boolean imageExists(HierarchyFragment hierarchyFragment, String name);

    /**
	 * Checks whether a Fragment is registered with the repository. It is
	 * explicitly checked for Object equality, so passing a copy/clone
	 * of a Fragment as a parameter will return <code>false</code>.
	 * 
	 * @param fragment the fragment to be checked
	 *        
	 * @throws IllegalArgumentException if <code>fragment</code> is <code>null</code>
	 *            
	 * @return <code>true</code> if the fragment object is registered with the
	 *         repository, otherwise <code>false</code>
	 */
    public boolean fragmentExists(Fragment fragment);

    /**
	 * Sets the position of a Chapter within a Tutorial. Counting starts with
	 * <code>0</code>. The chapter must be a tutorial chapter.
	 * 
	 * @param chapter the Chapter whose position is to be set
	 * @param position the new position of the Chapter in its Tutorial
	 * 
	 * @throws IllegalArgumentException if 
	 * 			<ul>
	 * 				<li><code>chapter</code> is <code>null</code></li>
	 * 				<li><code>chapter</code> is not contained in repository</li>
	 * 				<li><code>chapter</code> is not a tutorial chapter</li>
	 * 				<li><code>position</code> is &lt; 0</li>
	 * 				<li><code>position</code> is &gt;= the number of tutorial chapters  
	 * 			</ul>
	 */
    public void setChapterPosition(Chapter chapter, int position);

    /**
	 * Checks whether a Fragment is a Chapter that belongs to a Tutorial. 
	 * A so-called Tutorial Chapter is a copy of a Chapter which is 
	 * part of a Tutorial. This method returns <code>true</code> only if 
	 * the checked Fragment is such a copied Chapter. If the Fragment 
	 * is an "original" Chapter, <code>false</code> is returned,
	 * even if the Chapter does have copies that serve as Tutorial Chapters.
	 * 
	 * @param chapter the Fragment to be checked
	 * 
	 * @throws IllegalArgumentException if fragment is <code>null</code>
	 * 
	 * @return <code>true</code> if the Fragment is a copy used for a Tutorial
	 * 			AND contained in the repository, otherwise <code>false</code>
	 */
    public boolean isTutorialChapter(Fragment fragment);

    /**
	 * <p>Returns a List of Fragments that depend on a certain Fragment. Dependency
	 * means that if the original Fragment does not exist, the
	 * dependent Fragments can also not exist. This method does NOT return
	 * children and successors of a Fragments. Those can be accessed
	 * via the getParent() and getChildren() methods of the Fragments.</p>
	 * 
	 * <p>Examples of dependent Fragments are tutorial chapters, which depend on
	 * Chapters, or CodeSnippets, which make no sense without the belonging
	 * CodeFile.</p>
	 * 
	 * @param fragment the Fragment for which to get dependent Fragments
	 * 
	 * @throws IllegalArgumentException if fragment is <code>null</code> or
	 * 	if it is not registered with the repository 
	 *  
	 * @return a collection of dependent Fragments or an empty collection, if
	 * there are no dependent Fragments
	 */
    public Collection<Fragment> getDependentFragments(Fragment fragment);

    /**
	 * Returns the CodeFile that is associated with a CodeSnippet.
	 * 
	 * @param codeSnippet the CodeSnippet for which to get the CodeFile
	 * 
	 * @throws IllegalArgumentException if CodeSnippet is <code>null</code> or
	 * 	if it is not registered with the repository
	 *  
	 * @return the CodeFile the CodeSnippet belongs to
	 */
    public CodeFile getParentCodeFile(CodeSnippet codeSnippet);

    public Chapter getParentChapter(Chapter tutorialChapter);

    /**
	 * Creates a new Project. It must have a unique name.
	 * Creating the project involves creating the project's
	 * categories: Chapters, CodeFiles, CodeSnippets, Images, Tutorials.
	 * 
	 * @param name the name of the Project to be created
	 * 
	 * @throws IllegalArgumentException if name is <code>null</code>
	 * @throws DeftFragmentAlreadyExistsException
	 *             if there is already a Project with that name            
	 *             
	 * @return the new Project             
	 */
    public Project createNewProject(String name) throws DeftFragmentAlreadyExistsException;

    /**
	 * Creates a new Chapter in a Project. Each Chapter within a Project must
	 * have a unique name.
	 * 
	 * @param project the Project in which to create the Chapter
	 * @param name the name of the Chapter to be created
	 *
	 * @throws IllegalArgumentException if 
	 * 			<ul>
	 * 				<li><code>project</code> is <code>null</code></li>
	 * 				<li><code>name</code> is <code>null</code></li>
	 * 				<li><code>project</code> is not contained in repository</li>
	 * 			</ul>
	 * @throws DeftFragmentAlreadyExistsException
	 *             if there is already a Chapter with the name in the Project 
	 * 
	 * @return the created Chapter
	 */
    public Chapter createNewChapter(Project project, String name) throws DeftFragmentAlreadyExistsException;

    /**
	 * Creates a new Tutorial in a Project. Each Tutorial within a Project must
	 * have a unique name.
	 * 
	 * @param project the Project in which to create the Tutorial
	 * @param name the name of the Tutorial to be created
	 *
	 * @throws IllegalArgumentException if 
	 * 			<ul>
	 * 				<li><code>project</code> is <code>null</code></li>
	 * 				<li><code>name</code> is <code>null</code></li>
	 * 				<li><code>project</code> is not contained in repository</li>
	 * 			</ul>
	 * @throws DeftFragmentAlreadyExistsException
	 *             if there is already a Tutorial with the name in the Project 
	 * 
	 * @return the created Tutorial
	 */
    public Tutorial createNewTutorial(Project project, String name) throws DeftFragmentAlreadyExistsException;

    /**
	 * Creates a new child Folder for a HierarchyFragment (i.e. a Category
	 * or another Folder). There must already exist a Fragment with given name 
	 * in the HierarchyFragment.
	 * 
	 * @param parent the HierarchyFragment for which to create the child Folder
	 * @param name the name of the Folder to be created
	 *
	 * @throws IllegalArgumentException if 
	 * 			<ul>
	 * 				<li><code>parent</code> is <code>null</code></li>
	 * 				<li><code>name</code> is <code>null</code></li>
	 * 				<li><code>parent</code> is not contained in repository</li>
	 * 			</ul>
	 * @throws DeftFragmentAlreadyExistsException
	 *             	if there is already a Fragment with the name <code>name</code>
	 *             	in the parent HierarchyFragment
	 * @throws DeftIllegalFragmentTreeException
	 * 				if <code>parent</code> cannot contain a child folder. 
	 * 				In particular, <code>parent</code> can only be a
	 * 				Chapters Category, a CodeSnippet category,
	 * 				an Images Category or a Folder. 
	 * 
	 * @return the created Folder
	 */
    public Folder createNewFolder(HierarchyFragment parent, String name) throws DeftFragmentAlreadyExistsException, DeftIllegalFragmentTreeException;

    /**
	 * Creates a copy of a Chapter and adds it to the Tutorial. The Tutorial
	 * must not already have a copy of that Chapter. The new Tutorial Chapter 
	 * must have the same UUID as the original Chapter.
	 * 
	 * @param tutorial the Tutorial to which to add a Tutorial Chapter
	 * @param chapter the Chapter of which to add a copy to the Tutorial
	 * 
	 * @throws IllegalArgumentException if
	 * 			<ul>
	 * 				<li><code>tutorial</code> is <code>null</code></li>
	 * 				<li><code>chapter</code> is <code>null</code></li>
	 * 				<li><code>tutorial</code> is not contained in repository</li>
	 * 				<li><code>chapter</code> is not contained in repository</li>
	 * 				<li><code>chapter</code> is a Tutorial Chapter</li>
	 * 			</ul> 
	 * @throws DeftFragmentAlreadyExistsException
	 *              if the Chapter has already been added to the Tutorial
	 * @throws DeftCrossProjectRelationException
	 * 				if <code>chapter</code> and <code>tutorial</code>
	 * 				belong to different Projects
	 *   
	 * @return the Tutorial Chapter
	 * 
	 */
    public Chapter addChapterToTutorial(Tutorial tutorial, Chapter chapter) throws DeftFragmentAlreadyExistsException, DeftCrossProjectRelationException;

    /**
	 * <p>Checks whether the Fragment <code>parent</code> could possibly have
	 * a child Fragment of type <code>childType</code>. The <code>childType</code>
	 * can be one of the following:<p>
	 * <ul>
	 * 		<li>project</li>
	 * 		<li>category</li>
	 * 		<li>chapter</li>
	 * 		<li>codefile</li>
	 * 		<li>codesnippet</li>
	 * 		<li>image</li>
	 * 		<li>tutorial</li>
	 * 		<li>folder</li>
	 * </ul>
	 * <p>Those child types equal the fragments' type strings that can be accessed
	 * via {@link Fragment#getTypeString()}.</p>
	 * 
	 * <p>The result is computed according to the following rules:</p>
	 * <ul>
	 * 		<li>Categories cannot contain incompatible Fragments (e.g. 
	 * 			the Chapter Category cannot contain Images).</li>
	 * 		<li>The CodeFile, CodeSnippet and Image Categories can contain Folders.</li>
	 * 		<li>Folders can contain Folders.</li>
	 * 		<li>Folders cannot contain incompatible Fragments (e.g. a Folder
	 * 			in the Chapter Category cannot contain Images).</li>
	 * </ul>
	 * 
	 * <p>Note that only "flexible" fragment relationships are covered by this method.
	 * "Fixed" ones, such as a Project containing its five Categories 
	 * or a Tutorial containing Chapters are not considered, i.e. 
	 * <code>canContain(project, "category")</code> would return
	 * <code>false</code>, even though <code>project</code> contains categories.</p>
	 * 
	 * <p>This is because this method is intended to be used only by methods that try to
	 * alter the Fragment tree, such as {@link #createNewFolder(HierarchyFragment, String)}.</p>
	 * 
	 * @param parent the Fragment for which to check whether a special 
	 * 			child type is allowed
	 * @param childType a String denoting the type of the child Fragment
	 * 
	 * @throws IllegalArgumentException if 
	 * 			<ul>
	 * 				<li><code>parent</code> is <code>null</code></li>
	 * 				<li><code>childType</code> is <code>null</code></li>
	 * 				<li><code>parent</code> is not contained in repository</li>
	 *			</ul>
	 * 
	 * @return <code>true</code> if the parent Fragment is allowed to have a child
	 * 			of the given type, otherwise <code>false</code>
	 */
    public boolean canContain(Fragment parent, String childType);

    /**
	 * <p>Checks whether the Fragment <code>parent</code> could possibly have
	 * a the Fragment <code>child</code> as one of its children.</p>
	 * 
	 * <p>This method returns similar results as 
	 * {@link #canContain(Fragment, String)}, but has some additional restrictions.
	 * In {@link #canContain(Fragment, String)} it was only checked, whether the parent
	 * fragment could have one special type of Fragment 
	 * (identified by <code>childType</code>) as a child. Here it is checked, 
	 * whether the parent Fragment could contain the Fragment <code>child</code>,
	 * <b>including all of its descendants</b>.</p>
	 * 
	 * <p>The result is computed according to the following rules:</p>
	 * <ul>
	 * 		<li>Fragments cannot contain themselves or their ancestors</li>
	 * 		<li>Categories cannot contain incompatible Fragments (e.g. 
	 * 			the Chapter Category cannot contain Images).</li>
	 * 		<li>The CodeFile, CodeSnippet and Image Categories can contain Folders.</li>
	 * 		<li>Folders can contain Folders.</li>
	 * 		<li>Folders cannot contain incompatible Fragments (e.g. a Folder
	 * 			in the Chapter Category cannot contain Images).</li>
	 * 		<li>Categories and Folders cannot contain Folders, that have descendants
	 * 			incompatible with the Category (e.g. a Folder with an Image cannot
	 * 			be added to a Folder in the CodeFile Category).</li> 
	 * </ul>
	 * 
	 * <p>Note that only "flexible" fragment relationships are covered by this method.
	 * "Fixed" ones, such as a Project containing its five Categories 
	 * or a Tutorial containing Chapters are not considered, i.e. 
	 * <code>canContain(project, project.getCategory(...))</code> would return
	 * <code>false</code>, even though the project contains the category.</p>
	 * 
	 * <p>This is because this method is intended to be used only by methods that try to
	 * alter the Fragment tree, such as {@link #move(Fragment, HierarchyFragment)}.</p>
	 * 
	 * <p>Note further, that it is <b>not</b> checked, whether <code>child</code> 
	 * is registered with the repository, whether <code>child</code> and 
	 * <code>parent</code> are from the same Project, or whether there is 
	 * a name clash (i.e. <code>parent</code> already has a child with the same
	 * name as the <code>child</code> parameter). This can be achieved by other means.</p> 
	 * 
	 * <p>Finally note, that <code>false</code> is returned if <code>child</code> has
	 * descendants that are somehow inconsistent. Inconsistencies can only occur
	 * for Fragments that are not in the repository. This leaves the following cases:
	 * <ul>
	 * 		<li><code>child</code> is not in repository and its descendants
	 * 			do not have unique UUIDs</li>
	 * 		<li><code>child</code> is not in repository and some of its 
	 * 			descendants have a UUID for which a Fragment is already registered
	 * 			with the repository</li>
	 * </ul>

	 *  
	 * @param parent the Fragment for which to check whether it can contain 
	 * 			<code>child</code> 			
	 * @param child the Fragment for which to check whether it can be 
	 * 			child of <code>parent</code>
	 * 
	 * @throws IllegalArgumentException if 
	 * 			<ul>
	 * 				<li><code>parent</code> is <code>null</code></li>
	 * 				<li><code>child</code> is <code>null</code></li>
	 * 				<li><code>parent</code> is not contained in repository</li>
	 *			</ul>
	 * 
	 * @return <code>true</code> if the parent Fragment can contain the child,
	 * 			 otherwise <code>false</code>
	 */
    public boolean canContain(Fragment parent, Fragment child);

    public boolean nameContained(Fragment parent, String childName);

    public boolean canContainName(Fragment parent, String childName);

    /**
	 * Imports a file as a new Chapter in a Project. The Chapter name must not
	 * already exist within the Project. The imported file must conform to the
	 * chapter specification, otherwise it is rejected //TODO chapter check
	 * 
	 * @param project
	 *            the Project into which to import the Chapter
	 * @param filename
	 *            the absolute location of the file to be imported
	 * @param name
	 *            the name of the Chapter in the repository
	 * @return the imported Chapter
	 * @throws DeftFragmentAlreadyExistsException
	 *             if there is already a Chapter with the name in the Project
	 */
    public Chapter importChapter(Project project, String filename, String name) throws DeftFragmentAlreadyExistsException;

    /**
	 * This is subject to change
	 * 
	 * @param project
	 * @param filename
	 * @param name
	 * @return
	 * @throws DeftFragmentAlreadyExistsException
	 */
    public CodeFile importCodeFile(HierarchyFragment parent, String filename, String name) throws DeftFragmentAlreadyExistsException, DeftParseException, DeftIllegalFragmentTreeException, DeftMultipleParserException;

    public CodeFile importCodeFile(HierarchyFragment parent, String filename, String name, String parserID) throws DeftFragmentAlreadyExistsException, DeftParseException, DeftIllegalFragmentTreeException, DeftMultipleParserException;

    public CodeFile importCodeFile(HierarchyFragment parent, String filename, String name, int revision, String parserID) throws DeftFragmentAlreadyExistsException, DeftParseException, DeftIllegalFragmentTreeException, DeftMultipleParserException, DeftIllegalRevisionException;

    /**
	 * Imports an image file as a new Image in a Project. The Image name must
	 * not already exist within the Project. The imported file must be a valid
	 * image, otherwise it is rejected //TODO image check
	 * 
	 * @param project
	 *            the Project into which to import the Image
	 * @param filename
	 *            the absolute location of the file to be imported
	 * @param name
	 *            the name of the Image in the repository
	 * @return the imported Image
	 * @throws DeftFragmentAlreadyExistsException
	 *             if there is already an Image with the name in the Project
	 */
    public Image importImage(Project project, String filename, String name) throws DeftFragmentAlreadyExistsException, DeftIllegalFragmentTreeException;

    public Image importImage(HierarchyFragment parent, String filename, String name) throws DeftFragmentAlreadyExistsException, DeftIllegalFragmentTreeException;

    /**
	 * Creates a CodeSnippet within a Project. The CodeSnippet has a CodeFile
	 * that it depends on and an associated Query that marks the part of the
	 * CodeFile represented by the CodeSnippet. Note that if the CodeFile is
	 * deleted, the CodeSnippet will also be deleted because it cannot exist
	 * alone.
	 * @param parentCodeFile
	 *            the CodeFile which the CodeSnippet refers to
	 * @param name
	 *            the name of the CodeSnippet
	 * @param query
	 *            the Query to be executed on the CodeFile, representing the
	 *            CodeSnippet's actual "content"
	 * 
	 * @return the CodeSnippet
	 */
    public CodeSnippet createCodeSnippet(CodeFile parentCodeFile, String name, Query query);

    public Document getXmlContentTree(CodeFile codeFile, Collection<CodeSnippetRef> codeSnippetRefs, Ident... idents);

    public Document getXmlContentTree(CodeFile codeFile, int revision, Collection<CodeSnippetRef> codeSnippetRefs, Ident... idents);

    public TreeNode getAst(CodeFile codeFile, int revision);

    /**
	 * Returns the structure of the CodeFile as an AST in the internal TreeNode
	 * format.
	 * 
	 * @param codeFile
	 *            the CodeFile to get the AST of
	 * @return the AST of the CodeFile
	 */
    public TreeNode getAst(CodeFile codeFile);

    public TreeNode getAst(CodeSnippet codeSnippet);

    public TreeNode getAst(CodeSnippet codeSnippet, int revision);

    /**
	 * <p>
	 * Returns the marked up content that a CodeSnippet represents. The returned
	 * Node is a DOM Element, it must not be a Document. The node can have the
	 * same content (text) as the original CodeFile which the CodeSnippet
	 * references. It is, however, also possible that the content is only an
	 * excerpt of the CodeFile, containing only the queried part.
	 * </p>
	 * 
	 * <p>
	 * The returned content is marked up with information, such as regions that
	 * have been selected with the CodeSnippet's query or types of Terminals
	 * (numbers, strings, ...). The information are intended for output
	 * formatting.
	 * </p>
	 * 
	 * @param codeSnippet
	 *            the CodeSnippet representing the queried CodeFile
	 * @param tType
	 *            the output format (XML, XHTML, plain text, ...)
	 * @param dType
	 *            the display type (INLINE, BLOCK)
	 * 
	 * @param format
	 *            the format that should be applied on the code snippet
	 * @return the marked up CodeFile
	 */
    public Node getXmlContentTree(CodeSnippet codeSnippet, DisplayType dType, Format format, Ident... idents);

    public Node getXmlContentTree(CodeSnippet codeSnippet, int revision, DisplayType dType, Format format, Ident... idents);

    /**
	 * Returns an InputStream for a Fragment to retrieve its content.
	 * 
	 * @param fragment
	 *            the fragment whose content to get
	 * @return the InputStream for the Fragment
	 */
    public InputStream getInputStream(Fragment fragment);

    /**
	 * Returns an OutputStream for a Fragment to write changed content back to
	 * the repository.
	 * 
	 * @param fragment
	 *            the fragment whose content to write
	 * @return the OutputStream for the Fragment
	 */
    public OutputStream getOutputStream(Fragment fragment);

    /**
	 * Returns the Fragment with a given UUID. Note that it is not possible to
	 * get tutorial chapters that way, because they have the same UUID as the
	 * original chapter. If the method is called with that UUID, the original
	 * Chapter will be returned.
	 * 
	 * @param uuid
	 *            the UUID of the fragment to be retrieved
	 * @return the retrieved Fragment
	 */
    public Fragment getFragment(UUID uuid);

    public Collection<Fragment> getFragments(Project project, IFragmentFilter filter);

    /**
	 * Deletes a fragment. If the fragment has children (e.g. a Tutorial having
	 * tutorial chapters), those children are deleted first. Similarly, if the
	 * fragment has dependent fragments (e.g. a CodeFile having a dependent
	 * CodeSnippet, see also {@link #getDependentFragments(Fragment)}), those
	 * fragments are also deleted.
	 * 
	 * @param fragment
	 *            the fragment to be deleted, including all its children and
	 *            dependent fragments
	 * @return a list of fragments that for some reason could not be removed
	 */
    public List<Fragment> removeFragment(Fragment fragment);

    /**
	 * Returns the query that is specified for the given code snippet
	 * 
	 * @param codeSnippet
	 * @return the query that is specified for the given code snippet
	 */
    public Query getQuery(CodeSnippet codeSnippet);

    /**
	 * Currently not implemented
	 * 
	 * @param fragment
	 * @param newName
	 * @throws DeftFragmentAlreadyExistsException 
	 */
    public void rename(Fragment fragment, String newName) throws DeftFragmentAlreadyExistsException;

    public void move(Fragment toMove, HierarchyFragment target) throws DeftFragmentAlreadyExistsException, DeftIllegalFragmentTreeException, DeftCrossProjectRelationException;

    /**
     * <p>Records that a code snippet has been added to a chapter.</p>
     * 
     * <p>Note that this method is not responsible for modifying the file containing
     * the chapter content. If there is markup to be added to the chapter file, this must be done
     * additionally. This method only has to create and save (in memory or a database) a CodeSnippetRef 
     * representing the relation between the chapter and the codeSnippet.</p>
     * 
     * <p>If it is planned to use the repository with a GUI containing a user friendly editor for chapters,
     * one must be aware that the user might want to close a chapter without saving changes. In that case
     * all CodeSnippets that have been added since the last save have to be removed. It is the task of the
     * repository implementor to take care of that.</p>
     *  
     * @param chapter the Chapter to which a CodeSnippet is added
     * @param codeSnippet the CodeSnippet being added to the Chapter
     * @return the CodeSnippetRef representing the Chapter-CodeSnippet relationship
     * @throws DeftCrossProjectRelationException 
     * @see commitChapterChanges(Chapter)
     * @see rollbackChapterChanges(Chapter)
     */
    public CodeSnippetRef addCodeSnippetToChapter(Chapter chapter, CodeSnippet codeSnippet) throws DeftCrossProjectRelationException;

    /**
     * <p>Removes a Reference to a CodeSnippet or an image.
     * This can be understood as the deletion of a CodeSnippet or Image from a Chapter.</p>
     * 
     * <p>Note, however, that this method is not responsible for deleting respective 
     * markup from the chapter file. This must be done additionally. 
     * This method only removes the reference representing the relation between
     * Chapter and CodeSnippet that is being held in memory or a database.</p>
     * 
     * <p>If it is planned to use the repository with a GUI containing a 
     * user friendly editor for chapters, one must be aware that the user 
     * might want to close a chapter without saving changes. In that case
     * all CodeSnippets that have been removed since the last save have 
     * to be added again. It is the task of the
     * repository implementor to take care of that.</p> 
     * 
     * @param ref the CodeSnippetRef to be deleted
     * @see commitChapterChanges(Chapter)
     * @see rollbackChapterChanges(Chapter) 
     */
    public void removeReference(Reference ref);

    /**
	 * Returns the Reference that has the given id.
	 *  
	 * @param refId the id of the Reference to be returned
	 * @return the Reference
	 */
    public Reference getReference(UUID refId);

    /**
     * <p>Returns all CodeSnippetRefs for a given Chapter, i.e. all CodeSnippetRefs representing
     * a relation between that Chapter and an arbitrary CodeSnippet.</p>
     * 
     * @param chapter the chapter for which all CodeSnippetRefs are to be returned
     * @return a List of all CodeSnippetRefs for the Chapter
     */
    public List<CodeSnippetRef> getCodeSnippetReferences(Chapter chapter);

    /**
     * <p>Returns all CodeSnippetRefs for a given CodeSnippet, i.e. all CodeSnippetRefs representing
     * a relation between that CodeSnippet and an arbitrary Chapter.</p>
	 *
     * @param codeSnippet the CodeSnippet for which all CodeSnippetRefs are to be returned
     * @return a List of all CodeSnippetRefs for the CodeSnippet 
     */
    public List<CodeSnippetRef> getCodeSnippetReferences(CodeFile codeFile);

    /**
     * <p>Returns all CodeSnippetRefs that contain CodeSnippets from the given CodeFile, 
     * i.e. all CodeSnippetRefs representing a relation between an arbitrary Chapter and a CodeSnippet
     * which has the same parentCodeFile as has been passed as a parameter.</p>
     * 
     * @param codeFile the CodeFile for which all CodeSnippetRefs are to be returned
     * @return a List of all CodeSnippetRefs for the CodeFile
     */
    public List<CodeSnippetRef> getCodeSnippetReferences(CodeSnippet codeSnippet);

    /**
	 * 
	 * @param chapter
	 * @param image
	 */
    public ImageRef addImageToChapter(Chapter chapter, Image image) throws DeftCrossProjectRelationException;

    public List<ImageRef> getImageReferences(Chapter chapter);

    public List<ImageRef> getImageReferences(Image image);

    public List<? extends Reference> getReferences(Chapter chapter);

    public List<? extends Reference> getReferences(EmbeddableFragment fragment);

    /**
     * <p>Commits additions and removals of CodeSnippets and Images to and from Chapters.</p>
     * 
     * <p>If an interactive editor is provided, the addition and removal of CodeSnippets and Images 
     * requires the execution of the respective add- and remove- methods. The resulting CodeSnippetRefs
     * and ImageRefs must only be held temporarily for the case that the user decides to close the
     * chapter file in the editor and thus discards all additions and removals.</p>
     * 
     * <p>If the user saves the changes made to the chapter, this method must be called and the
     * temporarily saved changes for the references must be made permanent.</p>
     *  
     * @param chapter the Chapter whose CodeSnippetRefs are to be made permanent
     */
    public void commitChapterChanges(Chapter chapter);

    /**
     * <p>Rolls back additions and removals of CodeSnippets and Images to and from Chapters.</p>
     * 
     * <p>If an interactive editor is provided, the addition and removal of CodeSnippets and Images 
     * requires the execution of the respective add- and remove- methods. The resulting CodeSnippetRefs
     * and ImageRefs must only be held temporarily for the case that the user decides to close the
     * chapter file in the editor and thus discards all additions and removals.</p>
     * 
     * <p>If the discards the changes made to the chapter, this method must be called and the
     * temporarily saved changes for the references must be rolled back.</p>
     *  
     * @param chapter the Chapter whose CodeSnippetRefs are to be made permanent
     */
    public void rollbackChapterChanges(Chapter chapter);

    public void addObserver(Observer observer);

    public void deleteObserver(Observer observer);

    public String getResourceLocation(String resourceName);

    public boolean formatExists(CodeSnippet snippet, Format format);

    public void registerFormat(CodeSnippet codeSnippet, Format format);

    public void unregisterFormat(CodeSnippet codeSnippet, Format format);

    public void updateFormat(CodeSnippet snippet, Format oldFormat, Format newFormat);

    public Format getFormat(CodeSnippet codeSnippet, String formatName);

    public List<Format> getValidFormats(CodeSnippet codeSnippet);

    public List<Format> getValidFormats(CodeSnippet codeSnippet, DisplayType display);

    public boolean isDefaultFormat(CodeSnippet codeSnippet, String formatName);

    public void addFormatChangedListener(IFormatChangedListener formatChangedListener);

    /**
	 * Returns the highest revision number that any CodeFile in the
	 * specified Project has. If there have no CodeFiles been imported yet,
	 * <code>0</code> is returned.
	 * 
	 * @param project the Project for which to get the highest revision number
	 * @return the highest revision number of the Project
	 * @throws IllegalArgumentException if project 
	 * 			is <code>null</code> or not registered with the repository 
	 */
    public int getLatestRevision(Project project);

    /**
	 * Returns the highest revision number for the specified CodeFile. Every 
	 * CodeFile has at least 1 revision number (assigned at import) or more
	 * (on update).
	 * 
	 * @param codeFile the CodeFile for which to get the highest revision number
	 * @return the highest revision number of the CodeFile
	 * @throws IllegalArgumentException if codeFile
	 * 			is <code>null</code> or not registered with the repository 
	 */
    public int getRevision(CodeFile codeFile);

    public List<TreeNode> getSelectedNodes(CodeSnippet codeSnippet);

    public CodeSnippet updateQuery(CodeSnippet cs, Query newQuery);

    public void updateCodeFile(CodeFile codeFile);

    public CodeFile updateCodeFile(CodeFile codeFile, String filename, boolean rename) throws DeftFragmentAlreadyExistsException, DeftParseException, DeftIllegalFragmentTreeException, DeftMultipleParserException, DeftIllegalRevisionException;

    public List<CodeSnippetRef> getChangedCodeSnippetReferences();
}
