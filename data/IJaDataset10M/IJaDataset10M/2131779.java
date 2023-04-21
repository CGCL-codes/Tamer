package org.deft.vextoolkit.common;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
import net.sf.vex.dom.Element;
import org.deft.repository.IRepository;
import org.deft.repository.RepositoryFactory;
import org.deft.repository.ast.annotation.Format;
import org.deft.repository.ast.annotation.FormatChangedEvent;
import org.deft.repository.ast.annotation.IFormatChangedListener;
import org.deft.repository.ast.annotation.Templates;
import org.deft.repository.ast.annotation.Format.DisplayType;
import org.deft.repository.exception.DeftCrossProjectRelationException;
import org.deft.repository.fragment.Chapter;
import org.deft.repository.fragment.CodeFile;
import org.deft.repository.fragment.CodeSnippet;
import org.deft.repository.fragment.Fragment;
import org.deft.repository.xfsr.reference.CodeSnippetRef;
import org.deft.repository.xfsr.reference.ImageRef;
import org.deft.share.FragmentEditorInput;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Node;

/**
 * This class deals as a facade to the repository. It is a Singleton that caches
 * all code snippet queries.
 * 
 * TODO: What happens if the code snippet has changed during runtime? For
 * example if the user changes the code query or the meaning of the format.
 * 
 * @author Thomas.Janke
 * 
 */
public class RepositoryFacade implements IFormatChangedListener, Observer {

    private static RepositoryFacade instance;

    private HashMap<String, Node> nodes;

    private HashMap<String, Image> images;

    private IRepository repository;

    private RepositoryFacade() {
        nodes = new HashMap<String, Node>();
        images = new HashMap<String, Image>();
        repository = RepositoryFactory.getRepository();
        repository.addFormatChangedListener(this);
        repository.addObserver(this);
    }

    public static RepositoryFacade getInstance() {
        if (instance == null) {
            instance = new RepositoryFacade();
        }
        return instance;
    }

    /**
	 * Is called whenever a format was unregistered or updated. Handles the
	 * deletion of cached code snippet nodes.
	 */
    public void formatChanged(FormatChangedEvent e) {
        deleteCachedSnippetNodes(e.getCodeSnippet(), e.getFormat());
    }

    /**
	 * Is called whenever the repository changed a fragment. Handles the
	 * deletion of cached code snippet nodes.
	 */
    public void update(Observable arg0, Object arg1) {
        if (arg1 instanceof CodeFile) {
            CodeFile codeFile = (CodeFile) arg1;
            List<CodeSnippetRef> refs = repository.getCodeSnippetReferences(codeFile);
            for (CodeSnippetRef codeSnippetRef : refs) {
                if (codeSnippetRef.getChangedCodeSnippet()) {
                    deleteCachedSnippetNodes(codeSnippetRef.getCodeSnippet(), null);
                }
            }
        }
    }

    private void deleteCachedSnippetNodes(CodeSnippet snippet, Format format) {
        if (snippet == null) return;
        List<String> todelete = new ArrayList<String>();
        for (String key : nodes.keySet()) {
            if (key.startsWith(snippet.getUUID().toString())) {
                if (format == null) {
                    todelete.add(key);
                } else {
                    if (key.endsWith(format.getName())) {
                        todelete.add(key);
                    }
                }
            }
        }
        for (String string : todelete) {
            nodes.remove(string);
        }
    }

    /**
	 * Returns the code snippet node for the corresponding coderef element. If
	 * the code reference could not be resolved or the argument is not a valid
	 * coderef element null is returned.
	 * 
	 * @param coderef
	 *            element
	 * @return node
	 */
    public Node getCodeSnippetNode(Element element) {
        if (!Util.isCodeRefTag(element)) return null;
        String snippetID = Util.getID(element);
        Fragment frag = repository.getFragment(UUID.fromString(snippetID));
        if (frag == null) {
            return null;
        }
        String formatName = Util.getFormat(element);
        String display = Util.getDisplay(element);
        String key = snippetID + "#" + display + "#" + formatName;
        if (frag instanceof CodeSnippet) {
            CodeSnippet snippet = (CodeSnippet) frag;
            if (nodes.containsKey(key)) {
                return nodes.get(key);
            }
            Node node = repository.getXmlContentTree(snippet, DisplayType.fromString(display), repository.getFormat(snippet, formatName), Templates.CSFORMAT, Templates.SELECTED, Templates.TOKENTYPE, Templates.ASTNAME);
            if (node != null) {
                nodes.put(key, node);
                return node;
            }
        }
        return null;
    }

    /**
	 * Adds an ImageRef or CodeSnippetRef depending on the given elements type.
	 * 
	 * @param e
	 */
    public void addFragmentRef(Element e) {
        if (Util.isCodeRefTag(e)) addCodeSnippetRef(e); else if (Util.isImageTag(e)) addImageRef(e);
    }

    /**
	 * Adds a CodeSnippetRef for the given coderef element.
	 * 
	 * @param e
	 */
    public void addCodeSnippetRef(Element e) {
        Chapter chapter = getActiveChapter();
        CodeSnippet snippet = getCodeSnippet(e);
        if (chapter != null && snippet != null) {
            try {
                repository.addCodeSnippetToChapter(chapter, snippet);
            } catch (DeftCrossProjectRelationException e1) {
                VexPlugin.log("Fragment relation is not valid.", e1);
            }
        }
    }

    /**
	 * Adds a ImageRef for the given img element.
	 * 
	 * @param e
	 */
    public void addImageRef(Element e) {
        Chapter chapter = getActiveChapter();
        org.deft.repository.fragment.Image image = getImageFragment(e);
        if (chapter != null && image != null) {
            try {
                repository.addImageToChapter(chapter, image);
            } catch (DeftCrossProjectRelationException e1) {
                VexPlugin.log("Fragment relation is not valid.", e1);
            }
        }
    }

    /**
	 * Removes a CodeSnippetRef or an ImageRef depending on the given elements
	 * type.
	 * 
	 * @param e
	 */
    public void removeFragmentRef(Element e) {
        if (Util.isCodeRefTag(e)) removeCodeSnippetRef(e); else if (Util.isImageTag(e)) removeImageRef(e);
    }

    /**
	 * Removes the CodeSnippetRef that is related to the given coderef element.
	 * 
	 * @param e
	 */
    public void removeCodeSnippetRef(Element e) {
        CodeSnippetRef ref = getCodeSnippetRef(e);
        if (ref != null) repository.removeReference(ref);
    }

    /**
	 * Returns a CodeSnippetRef that connects the CodeSnippet related to the
	 * given element and the chapter related to the active editor.
	 * 
	 * @param e
	 * @return
	 */
    public CodeSnippetRef getCodeSnippetRef(Element e) {
        Chapter chapter = getActiveChapter();
        CodeSnippet snippet = getCodeSnippet(e);
        return getCodeSnippetRef(snippet, chapter);
    }

    /**
	 * Returns a CodeSnippetRef that connects the given CodeSnippet and Chapter.
	 * 
	 * @param e
	 * @return
	 */
    public CodeSnippetRef getCodeSnippetRef(CodeSnippet snippet, Chapter chapter) {
        if (chapter != null && snippet != null) {
            List<CodeSnippetRef> snippetRefs = repository.getCodeSnippetReferences(snippet);
            for (CodeSnippetRef ref : snippetRefs) {
                if (ref.getChapter().equals(chapter)) {
                    return ref;
                }
            }
        }
        return null;
    }

    /**
	 * Removes the ImageRef that is related to the given element.
	 * 
	 * @param e
	 */
    public void removeImageRef(Element e) {
        Chapter chapter = getActiveChapter();
        org.deft.repository.fragment.Image img = getDeftImage(e);
        if (chapter != null && img != null) {
            List<ImageRef> imgRefs = repository.getImageReferences(img);
            for (ImageRef ref : imgRefs) {
                if (ref.getChapter().equals(chapter)) {
                    repository.removeReference(ref);
                    break;
                }
            }
        }
    }

    public org.deft.repository.fragment.Image getDeftImage(Element e) {
        Fragment frag = repository.getFragment(UUID.fromString(Util.getID(e)));
        if (frag instanceof org.deft.repository.fragment.Image) {
            return (org.deft.repository.fragment.Image) frag;
        }
        return null;
    }

    /**
	 * Returns the CodeSnippet for the given element or null if the id does not
	 * exist.
	 * 
	 * @param e
	 * @return
	 */
    public CodeSnippet getCodeSnippet(Element e) {
        Fragment frag = repository.getFragment(UUID.fromString(Util.getID(e)));
        if (frag instanceof CodeSnippet) {
            return (CodeSnippet) frag;
        }
        return null;
    }

    /**
	 * Returns the chapter that is shown in the active editor or null.
	 * 
	 * @return
	 */
    public Chapter getActiveChapter() {
        IEditorInput input = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();
        if (input instanceof FragmentEditorInput) {
            Fragment frag = ((FragmentEditorInput) input).getFragment();
            if (frag instanceof Chapter) {
                return (Chapter) frag;
            }
        }
        return null;
    }

    /**
	 * Returns the Image for the given element.
	 * 
	 * @param e
	 * @return
	 */
    public org.deft.repository.fragment.Image getImageFragment(Element e) {
        Fragment frag = repository.getFragment(UUID.fromString(Util.getID(e)));
        if (frag instanceof org.deft.repository.fragment.Image) {
            return (org.deft.repository.fragment.Image) frag;
        }
        return null;
    }

    /**
	 * Returns the image that is related to the given img element.
	 * 
	 * @param element
	 * @return
	 */
    public Image getImage(Element element) {
        if (!Util.isImageTag(element)) {
            return null;
        }
        String key = Util.getID(element);
        Fragment frag = repository.getFragment(UUID.fromString(key));
        if (frag == null) {
            return null;
        }
        if (images.containsKey(key)) {
            return images.get(key);
        }
        if (frag != null) {
            InputStream is = repository.getInputStream(frag);
            Image img = null;
            try {
                img = new Image(Display.getCurrent(), is);
            } catch (Exception e) {
            }
            images.put(key, img);
            return img;
        }
        return null;
    }
}
