package net.sf.refactorit.ui.module.extractsuper;

import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.ui.treetable.BinTreeTableNode;
import java.util.Set;

/**
 * Selectable node with additional "abstract" flag.
 *
 * @author Anton Safonov
 */
public class ExtractableMemberNode extends BinTreeTableNode {

    private ExtractableMembersModel model;

    private boolean visiting;

    private boolean convertPrivates;

    private boolean abstr;

    public ExtractableMemberNode(ExtractableMembersModel model, Object bin) {
        super(bin, false);
        this.model = model;
    }

    public void setSelected(boolean selected) {
        setSelectedSilently(selected);
        model.updatePreview();
    }

    public boolean isConvertPrivates() {
        return convertPrivates;
    }

    public void setConvertPrivates(boolean convertPrivates) {
        this.convertPrivates = convertPrivates;
    }

    public boolean isAbstract() {
        if (isForcedAbstract()) {
            return true;
        }
        return this.abstr;
    }

    public boolean isForcedAbstract() {
        return (model.getExtractor().isForceExtractMethodsAbstract() || !model.getExtractor().isExtractClass()) && getBin() instanceof BinMethod;
    }

    public void setAbstract(boolean abstr) {
        if (getBin() instanceof BinMethod) {
            this.abstr = abstr;
            setAbstractMethod((BinMethod) getBin(), abstr);
            if (!abstr && isSelected()) {
                setSelected(false);
                model.updateSelection((BinMember) this.getBin(), true);
            }
            model.updatePreview();
        }
    }

    public void setAbstractMethod(BinMethod method, boolean abstr) {
        Set abstrs = model.getExtractor().getExplicitlyAbstractMethods();
        if (abstr) {
            abstrs.add(method);
        } else {
            abstrs.remove(method);
        }
    }

    /**
   * @return <code>true</code> if selection changed
   */
    public Boolean setSelectedSilently(boolean selected) {
        if (isSelected() == selected) {
            return Boolean.FALSE;
        }
        model.updateSelection((BinMember) this.getBin(), selected);
        super.setSelected(selected);
        return Boolean.TRUE;
    }

    public boolean isVisiting() {
        return this.visiting;
    }

    public void setVisiting(boolean visiting) {
        this.visiting = visiting;
    }
}
