package edu.wpi.hri.gen.policy.ref;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import edu.wpi.hri.bml.behavior.ConstraintBehavior;
import edu.wpi.hri.bml.behavior.ConstraintBehavior.SyncBlock;
import edu.wpi.hri.bml.behavior.EmitBehavior;
import edu.wpi.hri.bml.behavior.GazeBehavior;
import edu.wpi.hri.bml.behavior.GestureBehavior;
import edu.wpi.hri.bml.behavior.SpeechBehavior;
import edu.wpi.hri.bml.behavior.SyncRef;
import edu.wpi.hri.bml.behavior.SyncRef.SyncPoint;
import edu.wpi.hri.gen.comm.BMLEmitListener;
import edu.wpi.hri.gen.comm.GazeKnowledge;
import edu.wpi.hri.gen.comm.SituationKnowledge;
import edu.wpi.hri.gen.comm.SituationKnowledge.InvalidIDException;
import edu.wpi.hri.gen.ebml.EBMLList;
import edu.wpi.hri.gen.ebml.ReferenceBehavior;
import edu.wpi.hri.gen.policy.ref.ReferenceOptions.UnreferencableException;
import edu.wpi.hri.log.Logger;
import edu.wpi.hri.log.Logger.Colors;
import edu.wpi.hri.log.Logger.LoggerLevel;

/**
 * The ReferencePolicy class is used for creating directed gazes from objects
 * which need to be referenced in the shared space. The full algorithm is
 * lengthy and can not be fully explained here. The basics are to find a
 * gesture-speech pair with the minimum reliabilty and the lowest cost.
 * 
 * @author Aaron Holroyd (aholroyd (at) wpi (dot) edu)
 */
public class ReferencePolicy {

    protected final Logger logger;

    protected final BMLEmitListener emitListener;

    protected final NoPointZone noZone;

    /**
	 * Create a new reference policy to act on the given reference behavior. The
	 * logger given will be used to print out the information along the way.
	 * 
	 * @param logger
	 *            The logger to print information for the user to.
	 * @param emit
	 *            The emit tag listener for the connection events.
	 * @param noZone
	 *            The zone which the robot can not point to.
	 */
    public ReferencePolicy(Logger logger, BMLEmitListener emit, NoPointZone noZone) {
        this.logger = logger.sub(Colors.POLICY, "REF");
        this.emitListener = emit;
        this.noZone = noZone;
        this.logger.debug(LoggerLevel.INIT, "Created ...");
    }

    /**
	 * Find the gesture-speech pairs with the lowest cost for each reference in
	 * the list of behaviors.
	 * 
	 * @param list
	 *            The list to add the gesture-speech pair to
	 * @param knowledge
	 *            The knowledge of where the robot is looking.
	 * @param world
	 *            The knowledge of where objects are in the world.
	 * @return True if the references were all applied correctly, false if at
	 *         least one can not be accomplished
	 */
    public boolean apply(EBMLList list, GazeKnowledge knowledge, SituationKnowledge world, String actor) {
        try {
            logger.debug(LoggerLevel.POLICY_EXECUTION, "Applying for entire list");
            ReferenceBehavior[] orderedRefs = this.findOrderedList(list);
            ReferenceOptions[] orderedOptions = new ReferenceOptions[orderedRefs.length];
            if (orderedRefs.length == 0) return true;
            for (int c = 0; c < orderedRefs.length; c++) {
                orderedOptions[c] = new ReferenceOptions(logger, orderedRefs[c], world);
            }
            return this.applyOptions(list, knowledge, world, orderedOptions, actor);
        } catch (InvalidIDException e) {
            logger.error("Reference policy failed: " + e.getMessage());
            return false;
        }
    }

    protected boolean applyOptions(EBMLList list, GazeKnowledge knowledge, SituationKnowledge world, ReferenceOptions[] orderedOptions, String actor) {
        logger.debug(LoggerLevel.POLICY_EXECUTION, "created options, finding best");
        try {
            this.findLowestCost(orderedOptions, world, new GestureState(knowledge, world));
            logger.debug(LoggerLevel.POLICY_EXECUTION, "found best, adding to list");
            for (ReferenceOptions opt : orderedOptions) this.addToList(list, opt, world, actor);
            list.getReferences().clear();
            logger.debug(LoggerLevel.POLICY_EXECUTION, "finished applying all list");
        } catch (UnreferencableException e) {
            logger.warn("unreferencable: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
	 * Find the order which the references are ordered and return them in that
	 * order in an array.
	 * 
	 * @TODO actually find the ordered list of references
	 * 
	 * @param list
	 *            all of the behaviors list.
	 * @return The reference behavior execution ordered list.
	 */
    protected ReferenceBehavior[] findOrderedList(EBMLList list) {
        return list.getReferences().toArray(new ReferenceBehavior[0]);
    }

    /**
	 * Find the optimal (lowest cost) gesture-speech pairs for each of the
	 * references. If a reference does not have any viable options (most likely
	 * because the minimum reliability was too high), the Unreferenceable
	 * Exception is thrown.
	 * 
	 * @param options
	 *            All of the options.
	 * @param situation
	 *            The situation the reference is in.
	 * @param state
	 *            The robot state before the reference.
	 * @throws UnreferencableException
	 *             if the reference does not have any options.
	 */
    private void findLowestCost(ReferenceOptions[] options, SituationKnowledge situation, GestureState state) throws UnreferencableException {
        if (options.length > 0) {
            ReferenceOptions last = options[0];
            for (int c = 1; c < options.length; c++) {
                last.setNextOption(options[c]);
                last = options[c];
            }
            options[0].findBest(situation, state, noZone);
        }
    }

    private void addToList(EBMLList list, ReferenceOptions option, SituationKnowledge situation, String actor) throws UnreferencableException {
        GazeBehavior gaze = option.createGaze();
        GestureBehavior point = option.createPoint(situation);
        SpeechBehavior speech = option.createSpeech();
        GazeBehavior finalGaze = (gaze == null) ? null : new GazeBehavior(logger, "gaze-actor-" + option.getBehavior().getID(), actor, option.getBehavior().isRequired());
        EmitBehavior emit = option.createDG(actor, emitListener, gaze != null ? gaze : speech);
        logger.debug(LoggerLevel.POLICY_EXECUTION, "Created all behaviors");
        ConstraintBehavior constraint = option.getBehavior().isRequired() ? list.getRequiredConstraints() : list.getOptionalConstraints();
        if (gaze != null && point != null) {
            SyncBlock block = new SyncBlock(new SyncRef(gaze, SyncPoint.STROKE_START, 0));
            block.addRef(new SyncRef(point, SyncPoint.START, 0));
            constraint.addSynchronized(block);
        }
        if (gaze != null && point != null) {
            SyncBlock block = new SyncBlock(new SyncRef(gaze, SyncPoint.STROKE, 0));
            block.addRef(new SyncRef(point, SyncPoint.READY, 0));
            constraint.addSynchronized(block);
        }
        if (point != null) {
            SyncBlock block = new SyncBlock(new SyncRef(point, SyncPoint.STROKE_START, 0));
            block.addRef(new SyncRef(speech, SyncPoint.START, 0));
            constraint.addSynchronized(block);
        } else if (gaze != null) {
            SyncBlock block = new SyncBlock(new SyncRef(gaze, SyncPoint.STROKE, 0));
            block.addRef(new SyncRef(speech, SyncPoint.START, 0));
            constraint.addSynchronized(block);
        }
        if (gaze != null && point != null) {
            SyncBlock block = new SyncBlock(new SyncRef(point, SyncPoint.STROKE, 0));
            block.addRef(new SyncRef(finalGaze, SyncPoint.START, 0));
            constraint.addSynchronized(block);
        }
        if (gaze != null) {
            SyncBlock block = new SyncBlock(new SyncRef(speech, SyncPoint.END, 0));
            block.addRef(new SyncRef(finalGaze, SyncPoint.STROKE, 0));
            constraint.addSynchronized(block);
        }
        if (point != null) {
            SyncBlock block = new SyncBlock(new SyncRef(option.isLast() ? emit : speech, SyncPoint.END, 0));
            block.addRef(new SyncRef(point, SyncPoint.STROKE_END, 0));
            constraint.addSynchronized(block);
        }
        logger.debug(LoggerLevel.POLICY_EXECUTION, "added in all known constraints");
        SyncRef start = (gaze != null) ? new SyncRef(gaze, SyncPoint.START, 0) : new SyncRef(speech, SyncPoint.START, 0);
        SyncRef stroke = (point != null) ? new SyncRef(point, SyncPoint.STROKE_START, 0) : new SyncRef(speech, SyncPoint.START, 0);
        SyncRef end = (gaze != null) ? new SyncRef(finalGaze, SyncPoint.STROKE, 0) : new SyncRef(speech, SyncPoint.END, 0);
        this.dealWithAllConstraints(list, option, start, stroke, end);
        if (speech != null) list.getSpeeches().add(speech);
        if (gaze != null) {
            list.getGazes().add(gaze);
            list.getGazes().add(finalGaze);
        }
        if (point != null) list.getGestures().add(point);
        if (emit != null) list.getEmits().add(emit);
        logger.debug(LoggerLevel.POLICY_EXECUTION, "added in all incoming constraints");
    }

    private void dealWithAllConstraints(EBMLList list, ReferenceOptions option, SyncRef start, SyncRef stroke, SyncRef end) throws UnreferencableException {
        String id = option.getBehavior().getID();
        ConstraintBehavior reqCons = list.getRequiredConstraints();
        Set<SyncBlock> required = this.findConstraints(reqCons, id);
        ConstraintBehavior optCons = list.getOptionalConstraints();
        Set<SyncBlock> optional = this.findConstraints(optCons, id);
        logger.debug(LoggerLevel.POLICY_EXECUTION, "Found " + required.size() + " required blocks and " + optional.size() + " optional blocks");
        if ((required.size() + optional.size()) == 0) logger.warn("Could not find constraints, you should add some");
        for (SyncBlock block : required) {
            fixBlock(block, id, start, stroke, end);
        }
        for (SyncBlock block : optional) {
            try {
                fixBlock(block, id, start, stroke, end);
            } catch (UnreferencableException e) {
                logger.warn("optional constraint ignored: " + e.getMessage());
            }
        }
    }

    private void fixBlock(SyncBlock block, String id, SyncRef start, SyncRef stroke, SyncRef end) throws UnreferencableException {
        if (block.getStartRef().getID().equals(id)) {
            SyncRef blockStart = null;
            if (block.getStartRef().getPoint() == SyncPoint.START) blockStart = start; else if (block.getStartRef().getPoint() == SyncPoint.STROKE) blockStart = stroke; else if (block.getStartRef().getPoint() == SyncPoint.END) blockStart = end; else throw new UnreferencableException("Can not constrain");
            block.setStartRef(blockStart);
        }
        for (SyncRef ref : block.getRefs()) {
            if (ref.getID().equals(id)) {
                SyncRef refStart = null;
                if (ref.getPoint() == SyncPoint.START) refStart = start; else if (ref.getPoint() == SyncPoint.STROKE) refStart = stroke; else if (ref.getPoint() == SyncPoint.END) refStart = end; else throw new UnreferencableException("Can not constrain");
                block.removeRef(ref);
                block.addRef(refStart);
                return;
            }
        }
    }

    private Set<SyncBlock> findConstraints(ConstraintBehavior cb, String id) {
        logger.debug(LoggerLevel.POLICY_EXECUTION, "Finding constraints for " + id);
        Set<SyncBlock> allBlocks = new HashSet<SyncBlock>();
        allBlocks.addAll(this.findFromList(cb.getBefore(), id));
        allBlocks.addAll(this.findFromList(cb.getSynchronize(), id));
        allBlocks.addAll(this.findFromList(cb.getAfter(), id));
        return allBlocks;
    }

    private Set<SyncBlock> findFromList(List<SyncBlock> list, String id) {
        Set<SyncBlock> allBlocks = new HashSet<SyncBlock>();
        logger.debug(LoggerLevel.ALL, "comparing " + list.size() + " constraints ...");
        for (SyncBlock sb : list) {
            logger.debug(LoggerLevel.ALL, "Comparing start Constraint: [" + sb.getStartRef().getID() + "] to [" + id + "]");
            if (sb.getStartRef().getID().equals(id)) allBlocks.add(sb);
            for (SyncRef ref : sb.getRefs()) {
                logger.debug(LoggerLevel.ALL, "Comparing ref Constraint: [" + ref.getID() + "] to [" + id + "]");
                if (ref.getID().equals(id)) allBlocks.add(sb);
            }
        }
        return allBlocks;
    }
}
