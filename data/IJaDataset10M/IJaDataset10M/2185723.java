package org.fudaa.fudaa.tr.post.profile;

import java.util.Iterator;
import org.fudaa.ctulu.CtuluVariable;
import org.fudaa.ebli.courbe.EGCourbeChild;
import org.fudaa.ebli.courbe.EGGrapheDuplicator;
import org.fudaa.ebli.courbe.EGGroup;

/**
 * @author fred deniger
 * @version $Id: MvProfileCourbeGroup.java,v 1.5 2007-05-04 13:59:49 deniger Exp $
 */
public class MvProfileCourbeGroup extends EGGroup {

    private final CtuluVariable var_;

    public MvProfileCourbeGroup(final CtuluVariable _var) {
        super();
        var_ = _var;
        setTitle(var_.toString());
    }

    @Override
    public boolean isTitleModifiable() {
        return false;
    }

    public CtuluVariable getVar() {
        return var_;
    }

    @Override
    public EGGroup duplicate(EGGrapheDuplicator _duplicator) {
        EGGroup duplic = new MvProfileCourbeGroup(var_);
        duplic.setVisible(this.isVisible_);
        duplic.setTitle(this.getTitle());
        duplic.setEgParent(this.getEgParent());
        duplic.setAxeY(this.getAxeY().duplicate());
        for (Iterator it = this.getChildren().iterator(); it.hasNext(); ) {
            Object item = it.next();
            if (item instanceof EGCourbeChild) {
                duplic.addEGComponent((EGCourbeChild) ((EGCourbeChild) item).duplicate(duplic, _duplicator));
            }
        }
        return duplic;
    }
}
