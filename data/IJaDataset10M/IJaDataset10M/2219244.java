package de.srcml.dom;

import java.util.List;
import org.dom4j.*;
import de.srcml.dom.Modifiers.Mods;
import de.srcml.dom.supports.*;

/**
 * Base class for SrcML definitions of annotations.
 *
 * @author Frank Raiser
 */
public class AnnotationDef extends SrcMLElement implements ISupportsModifiers, ISupportsName {

    protected AnnotationDef() {
        super(AnnotationDef.getSrcMLTagName());
    }

    protected AnnotationDef(QName f_name) {
        super(f_name);
    }

    protected AnnotationDef(QName f_name, int f_attrCount) {
        super(f_name, f_attrCount);
    }

    protected AnnotationDef(String f_name) {
        super(f_name);
    }

    protected AnnotationDef(String f_name, Namespace f_ns) {
        super(f_name, f_ns);
    }

    protected AnnotationDef(Element f_el) {
        super(f_el);
    }

    public static String getSrcMLTagName() {
        return "annotation_def";
    }

    public boolean addModifier(Mods f_mod) {
        return ISupportsModifiersImpl.addModifier(this, f_mod);
    }

    public void clearModifiers() {
        ISupportsModifiersImpl.clearModifiers(this);
    }

    public List<Mods> getModifiers() {
        return ISupportsModifiersImpl.getModifiers(this);
    }

    public Modifiers getModifiersNode() {
        return ISupportsModifiersImpl.getModifiersNode(this);
    }

    public boolean hasModifier(Mods f_mod) {
        return ISupportsModifiersImpl.hasModifier(this, f_mod);
    }

    public boolean removeModifier(Mods f_mod) {
        return ISupportsModifiersImpl.removeModifier(this, f_mod);
    }

    public boolean supportsModifier(Mods f_mod) {
        return ISupportsModifiersImpl.supportsModifier(this, f_mod);
    }

    public String getNameAttribute() {
        return ISupportsNameImpl.getNameAttribute(this);
    }

    public boolean setNameAttribute(String f_name) {
        return ISupportsNameImpl.setNameAttribute(this, f_name);
    }
}
