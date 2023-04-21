package de.tum.in.botl.util.modelHelper.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import de.tum.in.botl.metamodel.Attribute;
import de.tum.in.botl.model.BotlObject;
import de.tum.in.botl.model.ChangeableBotlObject;
import de.tum.in.botl.model.ModelException;
import de.tum.in.botl.model.ModelFactory;
import de.tum.in.botl.model.ModelFragment;
import de.tum.in.botl.model.ObjectAssociation;
import de.tum.in.botl.model.ObjectAttribute;
import de.tum.in.botl.transformer.MergeException;
import de.tum.in.botl.transformer.TransformationException;
import de.tum.in.botl.util.instanceModel.InstanceAssociation;
import de.tum.in.botl.util.instanceModel.InstanceModel;
import de.tum.in.botl.util.instanceModel.InstanceObject;
import de.tum.in.botl.util.modelHelper.ModelHelper;

/**
 * @author Frank
 *
 */
public class ModelHelperImpl implements ModelHelper {

    private ModelFactory modelFactory;

    public ModelHelperImpl(ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    public ModelFragment mergeModelFragments(ModelFragment mf0, ModelFragment mf1) throws ModelException {
        ModelFragment nmf = modelFactory.createModelFragment(mf0.getMetamodel());
        Map old2new = new HashMap();
        for (Iterator it = mf0.getObjects().iterator(); it.hasNext(); ) {
            BotlObject bo = (BotlObject) it.next();
            BotlObject nbo = addCopy(bo, nmf);
            old2new.put(bo, nbo);
        }
        for (Iterator it = mf1.getObjects().iterator(); it.hasNext(); ) {
            BotlObject bo = (BotlObject) it.next();
            old2new.put(bo, mergeCopyOfObjectIntoFragment(bo, mf0));
        }
        for (Iterator it = mf0.getAssos().iterator(); it.hasNext(); ) {
            ObjectAssociation oa = (ObjectAssociation) it.next();
            nmf.addObjAssociation((BotlObject) old2new.get(oa.getEnd0().getObject()), (BotlObject) old2new.get(oa.getEnd1().getObject()), oa.getClassAssociation(), oa.getEnd0().getClassAssociationEnd(), oa.getEnd1().getClassAssociationEnd(), oa.getCard());
        }
        for (Iterator it = mf1.getAssos().iterator(); it.hasNext(); ) {
            ObjectAssociation oa = (ObjectAssociation) it.next();
            ObjectAssociation noa = nmf.getObjAssociation((BotlObject) old2new.get(oa.getEnd0().getObject()), (BotlObject) old2new.get(oa.getEnd1().getObject()), oa.getClassAssociation(), oa.getEnd0().getClassAssociationEnd(), oa.getEnd1().getClassAssociationEnd());
            if (noa != null) {
                if (noa.getCard() == -1 || oa.getCard() == -1) noa.setCard(-1); else noa.setCard(Math.max(noa.getCard(), oa.getCard()));
            } else {
                nmf.addObjAssociation((BotlObject) old2new.get(oa.getEnd0().getObject()), (BotlObject) old2new.get(oa.getEnd1().getObject()), oa.getClassAssociation(), oa.getEnd0().getClassAssociationEnd(), oa.getEnd1().getClassAssociationEnd(), oa.getCard());
            }
        }
        nmf.fixAssoEnds();
        return nmf;
    }

    public BotlObject mergeCopyOfObjectIntoFragment(BotlObject bo, ModelFragment mf) throws ModelException {
        for (Iterator it = mf.getObjects().iterator(); it.hasNext(); ) {
            ChangeableBotlObject actObject = (ChangeableBotlObject) it.next();
            if (actObject.getType().equals(bo.getType()) && actObject.getId().equals(bo.getId())) {
                for (Iterator it2 = bo.getAttributes().iterator(); it2.hasNext(); ) {
                    Attribute att = ((ObjectAttribute) it2.next()).getAttType();
                    if (actObject.getAttValue(att) != null && bo.getAttValue(att) != null && !actObject.getAttValue(att).equals(bo.getAttValue(att))) throw new ModelException("Merging of object " + bo.getId() + " of type " + bo.getType() + " into a model fragment with the metamodel " + mf.getMetamodel() + " failed."); else if (bo.getAttValue(att) != null) actObject.setAttValue(att, bo.getAttValue(att));
                }
                return actObject;
            }
        }
        BotlObject nbo = addCopy(bo, mf);
        return nbo;
    }

    public ModelFragment getCopy(ModelFragment org) {
        ModelFragment mf = modelFactory.createModelFragment(org.getMetamodel());
        Map old2new = new HashMap(org.getObjects().size());
        for (Iterator it = org.getObjects().iterator(); it.hasNext(); ) {
            BotlObject bo = (BotlObject) it.next();
            BotlObject nbo = addCopy(bo, mf);
            old2new.put(bo, nbo);
        }
        for (Iterator it = org.getAssos().iterator(); it.hasNext(); ) {
            ObjectAssociation oa = (ObjectAssociation) it.next();
            mf.addObjAssociation((BotlObject) old2new.get(oa.getEnd0().getObject()), (BotlObject) old2new.get(oa.getEnd1().getObject()), oa.getClassAssociation(), oa.getEnd0().getClassAssociationEnd(), oa.getEnd1().getClassAssociationEnd(), oa.getCard());
        }
        mf.fixAssoEnds();
        return mf;
    }

    public BotlObject addCopy(BotlObject org, ModelFragment targetModelFragement) {
        ChangeableBotlObject bo = targetModelFragement.addObject(org.getType(), org.getId());
        for (Iterator it = org.getAttributes().iterator(); it.hasNext(); ) {
            Attribute att = ((ObjectAttribute) it.next()).getAttType();
            bo.setAttValue(att, org.getAttValue(att));
        }
        return bo;
    }

    public Map createNewModelFragmentWithMap(InstanceModel m) {
        Map io2o = new HashMap();
        ModelFragment mf = modelFactory.createModelFragment(m.getMetamodel());
        for (Iterator it = m.getInstObjects().iterator(); it.hasNext(); ) {
            InstanceObject io = (InstanceObject) it.next();
            BotlObject bo = mf.addObject(io.getType());
            io2o.put(io, bo);
        }
        for (Iterator it = m.getInstAssos().iterator(); it.hasNext(); ) {
            InstanceAssociation ia = (InstanceAssociation) it.next();
            mf.addObjAssociation((BotlObject) io2o.get(ia.getInstEnd0().getInstObject()), (BotlObject) io2o.get(ia.getInstEnd1().getInstObject()), ia.getClassAssociation(), ia.getInstEnd0().getClassAssociationEnd(), ia.getInstEnd1().getClassAssociationEnd(), ia.getCard());
        }
        return io2o;
    }

    /**
   * Merges the contents of o1 into o0
   */
    private static void mergeObjects(ChangeableBotlObject o0, BotlObject o1) throws TransformationException {
        if (o0 == null || o1 == null) throw new TransformationException("mergeObjects() called with NULL as objects parameter."); else if (o0.getType() != o1.getType()) throw new TransformationException("mergeObjects() called for objects with differnt type:\n" + "Object " + o0.getId() + " is of type " + o0.getType().getName() + "\nObject " + o1.getId() + " is of type " + o1.getType().getName()); else if (!o0.getId().equals(o1.getId())) throw new TransformationException("mergeObjects() called for objects with differnt ID\n" + "Object " + o0.getId() + " is of type " + o0.getType().getName() + "\nObject " + o1.getId() + " is of type " + o1.getType().getName());
        for (Iterator it = o0.getType().getAllAttributes().iterator(); it.hasNext(); ) {
            Attribute actAtt = (Attribute) it.next();
            if (o1.getAttValue(actAtt) != null) {
                if (o0.getAttValue(actAtt) == null) o0.setAttValue(actAtt, o1.getAttValue(actAtt)); else if (!o0.getAttValue(actAtt).equals(o1.getAttValue(actAtt))) {
                    throw new MergeException(actAtt, o0, o1);
                }
            }
        }
    }

    public void merge(ModelFragment targetModel, ModelFragment mf) throws TransformationException {
        if (targetModel == null || mf == null) throw new TransformationException("No model to merge.");
        if (!targetModel.getMetamodel().equals(mf.getMetamodel())) throw new TransformationException("Merge of model fragments with different metamodels is not possible.");
        Map object2TargetObject = new HashMap();
        for (Iterator it = mf.getObjects().iterator(); it.hasNext(); ) {
            BotlObject actObject = (BotlObject) it.next();
            ChangeableBotlObject targetObject = (ChangeableBotlObject) targetModel.getObject(actObject.getType(), actObject.getId());
            if (targetObject != null) {
                mergeObjects(targetObject, actObject);
                object2TargetObject.put(actObject, targetObject);
            } else {
                BotlObject newTargetObject = addCopy(actObject, targetModel);
                object2TargetObject.put(actObject, newTargetObject);
            }
        }
        for (Iterator it = mf.getAssos().iterator(); it.hasNext(); ) {
            ObjectAssociation actAsso = (ObjectAssociation) it.next();
            ObjectAssociation targetAsso = targetModel.getObjAssociation((BotlObject) object2TargetObject.get(actAsso.getEnd0().getObject()), (BotlObject) object2TargetObject.get(actAsso.getEnd1().getObject()), actAsso.getClassAssociation(), actAsso.getClassAssociation().getClassAssociationEnd0(), actAsso.getClassAssociation().getClassAssociationEnd1());
            if (targetAsso == null) targetModel.addObjAssociation((BotlObject) object2TargetObject.get(actAsso.getEnd0().getObject()), (BotlObject) object2TargetObject.get(actAsso.getEnd1().getObject()), actAsso.getClassAssociation(), actAsso.getClassAssociation().getClassAssociationEnd0(), actAsso.getClassAssociation().getClassAssociationEnd1(), actAsso.getCard()); else if (targetAsso.getCard() < actAsso.getCard()) targetAsso.setCard(actAsso.getCard());
        }
    }
}
