package rene.zirkel.macro;

import java.util.Enumeration;
import java.util.Vector;
import rene.util.xml.XmlTag;
import rene.util.xml.XmlTagText;
import rene.util.xml.XmlTree;
import rene.util.xml.XmlWriter;
import rene.zirkel.ZirkelCanvas;
import rene.zirkel.construction.Construction;
import rene.zirkel.construction.ConstructionException;
import rene.zirkel.objects.ConstructionObject;

/**
 * Macros are stored as constructions. Some of the construction objects are
 * parameters, which divide into two groups: primary parapemers and secondary
 * parameters (e.g. endpoints of a segment). Moreover, there is a separate list
 * of the primary parameters and the prompts to display to the user at each
 * primary parameter. Some objects are marked as targets.
 */
public class Macro extends Construction implements Cloneable {

    public String Name;

    public String Prompts[];

    public ConstructionObject Params[];

    public String PromptFor[] = new String[0];

    public String PromptName[] = new String[0];

    public String LastParams[];

    public boolean Fixed[];

    boolean Protected = false;

    boolean HideDuplicates = true;

    /**
	 * @param zc
	 *            The ZirkelCanvas.
	 * @param name
	 *            The name of this macro
	 * @param comment
	 *            The macro comment.
	 * @param e
	 *            The parameter prompt strings.
	 */
    public Macro(final ZirkelCanvas zc, final String name, final String comment, final String s[]) {
        Name = name;
        Comment = comment;
        Prompts = s;
    }

    public String getName() {
        return Name;
    }

    public void setName(final String name) {
        Name = name;
    }

    @Override
    public String getComment() {
        return Comment;
    }

    public String[] getPrompts() {
        return Prompts;
    }

    /**
	 * Set the list of parameters. The setup of the macro is done in a function
	 * in ZirkelCanvas.defineMacro().
	 */
    public void setParams(final ConstructionObject p[]) {
        Params = p;
    }

    public ConstructionObject[] getParams() {
        return Params;
    }

    /**
	 * Denote and recall previously set parameters.
	 */
    public void initLast() {
        LastParams = new String[Params.length];
    }

    public void setLast(final String name, final int i) {
        try {
            LastParams[i] = name;
        } catch (final Exception e) {
        }
    }

    public String getLast(final int i) {
        if (LastParams != null && LastParams[i] != null) return LastParams[i]; else return "";
    }

    public void setPromptFor(final String s[]) {
        PromptFor = s;
        PromptName = new String[PromptFor.length];
        for (int i = 0; i < PromptFor.length; i++) PromptName[i] = PromptFor[i];
    }

    public void setPromptName(final int i, final String s) {
        PromptName[i] = s;
    }

    public boolean promptFor(final String s) {
        return getPromptFor(s) >= 0;
    }

    public int getPromptFor(final String s) {
        for (int i = 0; i < PromptFor.length; i++) if (PromptFor[i].equals(s)) return i;
        return -1;
    }

    public String getPromptName(final String s) {
        for (int i = 0; i < PromptFor.length; i++) if (PromptFor[i].equals(s)) return PromptName[i];
        return "";
    }

    public int countPrompts() {
        return PromptFor.length;
    }

    /**
	 * Save a macro.
	 */
    public void saveMacro(final XmlWriter xml) {
        xml.startTagStart("Macro");
        xml.printArg("Name", Name);
        if (!HideDuplicates) xml.printArg("showduplicates", "true");
        xml.startTagEndNewLine();
        for (int i = 0; i < Params.length; i++) {
            xml.startTagStart("Parameter");
            xml.printArg("name", Params[i].getName());
            if (Fixed != null && Fixed[i] && LastParams != null && LastParams[i] != null) xml.printArg("fixed", LastParams[i]);
            xml.startTagEnd();
            xml.print(Prompts[i]);
            xml.endTagNewLine("Parameter");
        }
        if (!getComment().equals("")) {
            xml.startTagNewLine("Comment");
            xml.printParagraphs(getComment(), 60);
            xml.endTagNewLine("Comment");
        }
        xml.startTagNewLine("Objects");
        save(xml);
        xml.endTagNewLine("Objects");
        if (PromptFor.length > 0) {
            xml.startTagStart("PromptFor");
            for (int i = 0; i < PromptFor.length; i++) {
                xml.printArg("object" + i, PromptFor[i]);
                xml.printArg("prompt" + i, PromptName[i]);
            }
            xml.finishTagNewLine();
        }
        xml.endTagNewLine("Macro");
    }

    /**
	 * Read a macro (implemented as a constructor).
	 */
    public Macro(final ZirkelCanvas zc, XmlTree tree) throws ConstructionException {
        XmlTag tag = tree.getTag();
        if (!tag.name().equals("Macro")) throw new ConstructionException("No macro!");
        if (!tag.hasParam("Name")) throw new ConstructionException("Name missing!");
        Name = tag.getValue("Name");
        if (tag.hasParam("showduplicates")) HideDuplicates = false;
        Enumeration e = tree.getContent();
        while (e.hasMoreElements()) {
            final XmlTree t = (XmlTree) e.nextElement();
            tag = t.getTag();
            if (tag.name().equals("Objects")) {
                readConstruction(t);
                break;
            } else if (tag.name().equals("Comment")) {
                try {
                    setComment(t.parseComment());
                } catch (final Exception ex) {
                    throw new ConstructionException("Illegal Comment");
                }
            }
        }
        int ParamCount = 0;
        e = tree.getContent();
        while (e.hasMoreElements()) {
            final XmlTree t = (XmlTree) e.nextElement();
            tag = t.getTag();
            if (tag.name().equals("Parameter")) {
                if (!tag.hasParam("name")) throw new ConstructionException("Parameter name missing!");
                ParamCount++;
            } else if (tag.name().equals("PromptFor")) {
                if (tag.hasParam("object")) {
                    final String s[] = new String[1];
                    s[0] = tag.getValue("object");
                    setPromptFor(s);
                    if (tag.hasParam("prompt")) setPromptName(0, tag.getValue("prompt"));
                } else {
                    int n = 0;
                    while (tag.hasParam("object" + n)) n++;
                    final String s[] = new String[n];
                    for (int i = 0; i < n; i++) {
                        s[i] = tag.getValue("object" + i);
                    }
                    setPromptFor(s);
                    for (int i = 0; i < n; i++) {
                        if (tag.hasParam("prompt" + i)) setPromptName(i, tag.getValue("prompt" + i));
                    }
                }
            }
        }
        Params = new ConstructionObject[ParamCount];
        initLast();
        Prompts = new String[ParamCount];
        for (int pr = 0; pr < ParamCount; pr++) Prompts[pr] = "";
        int i = 0;
        e = tree.getContent();
        while (e.hasMoreElements()) {
            final XmlTree t = (XmlTree) e.nextElement();
            tag = t.getTag();
            if (tag.name().equals("Parameter")) {
                Params[i] = find(tag.getValue("name"));
                if (Params[i] == null) throw new ConstructionException("Illegal parameter " + tag.getValue("name") + "!");
                if (tag.hasParam("fixed")) {
                    if (Fixed == null) {
                        Fixed = new boolean[ParamCount];
                        for (int j = 0; j < ParamCount; j++) Fixed[j] = false;
                    }
                    Fixed[i] = true;
                    LastParams[i] = tag.getValue("fixed");
                }
                final Enumeration en = t.getContent();
                while (en.hasMoreElements()) {
                    tree = (XmlTree) en.nextElement();
                    if (tree.getTag() instanceof XmlTagText) {
                        Prompts[i] = ((XmlTagText) tree.getTag()).getContent();
                    }
                }
                i++;
            }
        }
    }

    /**
	 * @return A list of targets.
	 */
    @Override
    public Vector getTargets() {
        final Vector v = new Vector();
        final Enumeration e = V.elements();
        while (e.hasMoreElements()) {
            final ConstructionObject o = (ConstructionObject) e.nextElement();
            if (o.isTarget()) v.addElement(o);
        }
        return v;
    }

    public boolean hasFixed() {
        for (final String prompt : Prompts) if (prompt.startsWith("=")) return true;
        if (Fixed == null) return false;
        for (final boolean element : Fixed) if (element) return true;
        return false;
    }

    public boolean isFixed(final int i) {
        if (Fixed == null) return false;
        return Fixed[i];
    }

    public void setFixed(final int i, final boolean f) {
        if (Fixed == null) return;
        Fixed[i] = f;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (final Exception e) {
        }
        return null;
    }

    public boolean isProtected() {
        return Protected;
    }

    public void setProtected(final boolean flag) {
        Protected = flag;
    }

    public boolean hideDuplicates() {
        return HideDuplicates;
    }

    public void hideDuplicates(final boolean flag) {
        HideDuplicates = flag;
    }
}
