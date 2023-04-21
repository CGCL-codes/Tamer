package org.openscience.cdk.reaction.type;

import java.util.ArrayList;
import java.util.Iterator;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.reaction.IReactionProcess;
import org.openscience.cdk.reaction.ReactionEngine;
import org.openscience.cdk.reaction.ReactionSpecification;
import org.openscience.cdk.reaction.mechanism.TautomerizationMechanism;
import org.openscience.cdk.reaction.type.parameters.IParameterReact;
import org.openscience.cdk.reaction.type.parameters.SetReactionCenter;
import org.openscience.cdk.tools.ILoggingTool;
import org.openscience.cdk.tools.LoggingToolFactory;

/**
 * <p>IReactionProcess which produces a tautomerization chemical reaction. 
 * As most commonly encountered, this reaction results in the formal migration
 * of a hydrogen atom or proton, accompanied by a switch of a single bond and adjacent double bond</p>
 * 
 * <pre>X=Y-Z-H => X(H)-Y=Z</pre>
 * 
 * <p>Below you have an example how to initiate the mechanism.</p>
 * <p>It is processed by the HeterolyticCleavageMechanism class</p>
 * <pre>
 *  IMoleculeSet setOfReactants = NewDefaultChemObjectBuilder.getInstance().newMoleculeSet();
 *  setOfReactants.addMolecule(new Molecule());
 *  IReactionProcess type = new TautomerizationReaction();
 *  Object[] params = {Boolean.FALSE};
    type.setParameters(params);
 *  IReactionSet setOfReactions = type.initiate(setOfReactants, null);
 *  </pre>
 * 
 * <p>We have the possibility to localize the reactive center. Good method if you
 * want to specify the reaction in a fixed point.</p>
 * <pre>atoms[0].setFlag(CDKConstants.REACTIVE_CENTER,true);</pre>
 * <p>Moreover you must put the parameter Boolean.TRUE</p>
 * <p>If the reactive center is not specified then the reaction process will
 * try to find automatically the possible reaction centers.</p>
 * 
 * 
 * @author         Miguel Rojas
 * 
 * @cdk.created    2008-02-11
 * @cdk.module     reaction
 * @cdk.set        reaction-types
 * 
 * @see TautomerizationMechanism
 **/
@TestClass(value = "org.openscience.cdk.reaction.type.TautomerizationReactionTest")
public class TautomerizationReaction extends ReactionEngine implements IReactionProcess {

    private static ILoggingTool logger = LoggingToolFactory.createLoggingTool(TautomerizationReaction.class);

    /**
	 * Constructor of the TautomerizationReaction object.
	 *
	 */
    public TautomerizationReaction() {
    }

    /**
	 *  Gets the specification attribute of the TautomerizationReaction object.
	 *
	 *@return    The specification value
	 */
    @TestMethod("testGetSpecification")
    public ReactionSpecification getSpecification() {
        return new ReactionSpecification("http://almost.cubic.uni-koeln.de/jrg/Members/mrc/reactionDict/reactionDict#Tautomerization", this.getClass().getName(), "$Id$", "The Chemistry Development Kit");
    }

    /**
	 *  Initiate process.
	 *  It is needed to call the addExplicitHydrogensToSatisfyValency
	 *  from the class tools.HydrogenAdder.
	 *
	 *@param  reactants         reactants of the reaction
	 *@param  agents            agents of the reaction (Must be in this case null)
	 *
	 *@exception  CDKException  Description of the Exception
	 */
    @TestMethod("testInitiate_IMoleculeSet_IMoleculeSet")
    public IReactionSet initiate(IMoleculeSet reactants, IMoleculeSet agents) throws CDKException {
        logger.debug("initiate reaction: TautomerizationReaction");
        if (reactants.getMoleculeCount() != 1) {
            throw new CDKException("TautomerizationReaction only expects one reactant");
        }
        if (agents != null) {
            throw new CDKException("TautomerizationReaction don't expects agents");
        }
        IReactionSet setOfReactions = DefaultChemObjectBuilder.getInstance().newInstance(IReactionSet.class);
        IMolecule reactant = reactants.getMolecule(0);
        IParameterReact ipr = super.getParameterClass(SetReactionCenter.class);
        if (ipr != null && !ipr.isSetParameter()) setActiveCenters(reactant);
        Iterator<IAtom> atoms = reactant.atoms().iterator();
        while (atoms.hasNext()) {
            IAtom atomi = atoms.next();
            if (atomi.getFlag(CDKConstants.REACTIVE_CENTER) && (atomi.getFormalCharge() == CDKConstants.UNSET ? 0 : atomi.getFormalCharge()) == 0 && reactant.getConnectedSingleElectronsCount(atomi) == 0) {
                Iterator<IBond> bondis = reactant.getConnectedBondsList(atomi).iterator();
                while (bondis.hasNext()) {
                    IBond bondi = bondis.next();
                    if (bondi.getFlag(CDKConstants.REACTIVE_CENTER) && bondi.getOrder() == IBond.Order.DOUBLE) {
                        IAtom atomj = bondi.getConnectedAtom(atomi);
                        if (atomj.getFlag(CDKConstants.REACTIVE_CENTER) && (atomj.getFormalCharge() == CDKConstants.UNSET ? 0 : atomj.getFormalCharge()) == 0 && reactant.getConnectedSingleElectronsCount(atomj) == 0) {
                            Iterator<IBond> bondjs = reactant.getConnectedBondsList(atomj).iterator();
                            while (bondjs.hasNext()) {
                                IBond bondj = bondjs.next();
                                if (bondj.equals(bondi)) continue;
                                if (bondj.getFlag(CDKConstants.REACTIVE_CENTER) && bondj.getOrder() == IBond.Order.SINGLE) {
                                    IAtom atomk = bondj.getConnectedAtom(atomj);
                                    if (atomk.getFlag(CDKConstants.REACTIVE_CENTER) && (atomk.getFormalCharge() == CDKConstants.UNSET ? 0 : atomk.getFormalCharge()) == 0 && reactant.getConnectedSingleElectronsCount(atomk) == 0) {
                                        Iterator<IBond> bondks = reactant.getConnectedBondsList(atomk).iterator();
                                        while (bondks.hasNext()) {
                                            IBond bondk = bondks.next();
                                            if (bondk.equals(bondj)) continue;
                                            if (bondk.getFlag(CDKConstants.REACTIVE_CENTER) && bondk.getOrder() == IBond.Order.SINGLE) {
                                                IAtom atoml = bondk.getConnectedAtom(atomk);
                                                if (atoml.getFlag(CDKConstants.REACTIVE_CENTER) && atoml.getSymbol().equals("H")) {
                                                    ArrayList<IAtom> atomList = new ArrayList<IAtom>();
                                                    atomList.add(atomi);
                                                    atomList.add(atomj);
                                                    atomList.add(atomk);
                                                    atomList.add(atoml);
                                                    ArrayList<IBond> bondList = new ArrayList<IBond>();
                                                    bondList.add(bondi);
                                                    bondList.add(bondj);
                                                    bondList.add(bondk);
                                                    IMoleculeSet moleculeSet = reactant.getBuilder().newInstance(IMoleculeSet.class);
                                                    moleculeSet.addMolecule(reactant);
                                                    IReaction reaction = mechanism.initiate(moleculeSet, atomList, bondList);
                                                    if (reaction == null) continue; else setOfReactions.addReaction(reaction);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return setOfReactions;
    }

    /**
	 * set the active center for this molecule. 
	 * The active center will be those which correspond with X=Y-Z-H.
	 * <pre>
	 * X: Atom
	 * =: bond
	 * Y: Atom
	 * -: bond
	 * Z: Atom
	 * -: bond
	 * H: Atom
	 *  </pre>
	 * 
	 * @param reactant The molecule to set the activity
	 * @throws CDKException 
	 */
    private void setActiveCenters(IMolecule reactant) throws CDKException {
        Iterator<IAtom> atoms = reactant.atoms().iterator();
        while (atoms.hasNext()) {
            IAtom atomi = atoms.next();
            if ((atomi.getFormalCharge() == CDKConstants.UNSET ? 0 : atomi.getFormalCharge()) == 0 && reactant.getConnectedSingleElectronsCount(atomi) == 0) {
                Iterator<IBond> bondis = reactant.getConnectedBondsList(atomi).iterator();
                while (bondis.hasNext()) {
                    IBond bondi = bondis.next();
                    if (bondi.getOrder() == IBond.Order.DOUBLE) {
                        IAtom atomj = bondi.getConnectedAtom(atomi);
                        if ((atomj.getFormalCharge() == CDKConstants.UNSET ? 0 : atomj.getFormalCharge()) == 0 && reactant.getConnectedSingleElectronsCount(atomj) == 0) {
                            Iterator<IBond> bondjs = reactant.getConnectedBondsList(atomj).iterator();
                            while (bondjs.hasNext()) {
                                IBond bondj = bondjs.next();
                                if (bondj.equals(bondi)) continue;
                                if (bondj.getOrder() == IBond.Order.SINGLE) {
                                    IAtom atomk = bondj.getConnectedAtom(atomj);
                                    if ((atomk.getFormalCharge() == CDKConstants.UNSET ? 0 : atomk.getFormalCharge()) == 0 && reactant.getConnectedSingleElectronsCount(atomk) == 0) {
                                        Iterator<IBond> bondks = reactant.getConnectedBondsList(atomk).iterator();
                                        while (bondks.hasNext()) {
                                            IBond bondk = bondks.next();
                                            if (bondk.equals(bondj)) continue;
                                            if (bondk.getOrder() == IBond.Order.SINGLE) {
                                                IAtom atoml = bondk.getConnectedAtom(atomk);
                                                if (atoml.getSymbol().equals("H")) {
                                                    atomi.setFlag(CDKConstants.REACTIVE_CENTER, true);
                                                    atomj.setFlag(CDKConstants.REACTIVE_CENTER, true);
                                                    atomk.setFlag(CDKConstants.REACTIVE_CENTER, true);
                                                    atoml.setFlag(CDKConstants.REACTIVE_CENTER, true);
                                                    bondi.setFlag(CDKConstants.REACTIVE_CENTER, true);
                                                    bondj.setFlag(CDKConstants.REACTIVE_CENTER, true);
                                                    bondk.setFlag(CDKConstants.REACTIVE_CENTER, true);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
