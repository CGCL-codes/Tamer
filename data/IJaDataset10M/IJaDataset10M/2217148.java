package edu.wpi.hri.gen.policy;

import ros.NodeHandle;
import ros.Publisher;
import ros.RosException;
import ros.ServiceClient;
import ros.pkg.engagement_msgs.msg.APAction;
import ros.pkg.engagement_msgs.msg.ActorID;
import ros.pkg.bml_msgs.msg.Entity;
import ros.pkg.bml_msgs.msg.Flag;
import ros.pkg.engagement_msgs.msg.Speech;
import ros.pkg.engagement_srvs.srv.RobotAdjacencyPair;
import edu.wpi.hri.bml.behavior.ConstraintBehavior;
import edu.wpi.hri.bml.behavior.ConstraintBehavior.SyncBlock;
import edu.wpi.hri.bml.behavior.EmitBehavior;
import edu.wpi.hri.bml.behavior.GazeBehavior;
import edu.wpi.hri.bml.behavior.SyncRef;
import edu.wpi.hri.bml.behavior.SyncRef.SyncPoint;
import edu.wpi.hri.gen.GenerationParams;
import edu.wpi.hri.gen.comm.BMLEmitListener;
import edu.wpi.hri.gen.ebml.EBMLList;
import edu.wpi.hri.log.Logger;
import edu.wpi.hri.log.Logger.Colors;
import edu.wpi.hri.log.Logger.LoggerLevel;

/**
 * The TurnPositionPolicy has one job, to add behaviors corresponding to the
 * actions required to take when completing one turn of an adjacency pair. The
 * actions to take are decided based on if the turn is a first turn and the
 * position of the turn given. The actions taken can be found by the following
 * table:<br/>
 * <table border="1">
 * <tr>
 * <th>Position</th>
 * <th>Policy</th>
 * </tr>
 * <tr>
 * <td>Beginning</td>
 * <td>if first turn: start a Robot Initiated AP</td>
 * </tr>
 * <tr>
 * <td>Middle</td>
 * <td>-</td>
 * </tr>
 * <tr>
 * <td>End</td>
 * <td>Execute MFG after all other actions</td>
 * </tr>
 * <tr>
 * <td>Full</td>
 * <td>Perform Beginning and End Policies</td>
 * </tr>
 * </table>
 * 
 * @author Aaron Holroyd (aholroyd (at) wpi (dot) edu)
 */
public class TurnPositionPolicy {

    private final Logger logger;

    private final BMLEmitListener emit;

    private APWaiter waiter;

    private final ServiceClient<RobotAdjacencyPair.Request, RobotAdjacencyPair.Response, RobotAdjacencyPair> apsrv;

    private final Publisher<Speech> bcpub;

    private final boolean full;

    /**
	 * Create a new Turn Position Policy
	 * 
	 * @param handle
	 *            THe node handle to connect the ap client to.
	 * @param logger
	 *            The logger to base this Policy's logger on.
	 * @param emit
	 *            The emit object to register emits with.
	 * @param full
	 *            True to use MFG and BC connection events, false not to.
	 * @throws RosException
	 *             if the publisher to recognition can not be created
	 */
    public TurnPositionPolicy(NodeHandle handle, Logger logger, BMLEmitListener emit, boolean full) throws RosException {
        this.logger = logger.sub(Colors.POLICY, "TrnPos");
        this.emit = emit;
        this.apsrv = handle.serviceClient(GenerationParams.ROBOT_AP_PATH.getString(), new RobotAdjacencyPair());
        if (full) {
            this.bcpub = handle.advertise("performing/utterance", new Speech(), 10);
        } else {
            this.bcpub = null;
        }
        this.full = full;
        this.logger.debug(LoggerLevel.INIT, "Created ...");
    }

    /**
	 * Shutdown this policy and it's connection to recognition.
	 */
    public void shutdown() {
        if (this.waiter != null) this.waiter.getResponse();
        this.apsrv.shutdown();
    }

    /**
	 * Determine the actions to complete for the given situation.
	 * 
	 * @param list
	 *            The list to add the behaviors to.
	 * @param position
	 *            The position in the turn of this fragment.
	 * @param actor
	 *            The actor to whom the fragment is directed.
	 * @param firstTurn
	 *            True for the first turn of a AP, false otherwise.
	 * @param delay
	 *            True if this fragment should hold or delay the turn.
	 * @param delayTarget
	 *            The target to look at if it is a delay, if not provided
	 *            UP_RIGHT is used.
	 * @return The APWaiter which will return the response of the actor if the
	 *         AP should be waited for.
	 * @throws Exception
	 *             If the parameters given are invalid
	 */
    public APWaiter apply(EBMLList list, String position, String actor, boolean firstTurn, boolean delay, String delayTarget) throws Exception {
        logger.debug(LoggerLevel.POLICY_EXECUTION, "Applying ...");
        if (position.equalsIgnoreCase("BEGINNING")) {
            this.execBeginning(firstTurn, actor);
            if (delay) this.execDelay(list, delayTarget);
            return null;
        } else if (position.equalsIgnoreCase("MIDDLE")) {
            if (delay) this.execDelay(list, delayTarget);
            return null;
        } else if (position.equalsIgnoreCase("END")) {
            if (delay) throw new Exception("Can not end and delay turn");
            this.execEnd(list, actor);
            return this.waiter;
        } else if (position.equalsIgnoreCase("FULL")) {
            if (delay) throw new Exception("Can not full and delay turn");
            this.execBeginning(firstTurn, actor);
            this.execEnd(list, actor);
            return this.waiter;
        } else if (position.equalsIgnoreCase("BACKCHANNEL")) {
            if (delay) throw new Exception("Can not backchannel and delay turn");
            this.execBackchannel(list);
            return null;
        } else {
            throw new Exception("In valid turn position given");
        }
    }

    /**
	 * The only thing for this method to complete is to cre if (interest !=
	 * null) { HumanAdjacencyPair ap = new HumanAdjacencyPair(); ap.action =
	 * res.action; interest.call(ap); }ate a new APWaiter which will wait for
	 * the AP to complete. This is done for Beginning and Full turn positions.
	 */
    private void execBeginning(boolean firstTurn, String actor) {
        if (firstTurn) this.waiter = new APWaiter(actor);
    }

    /**
	 * This method must create the appropriate behaviors for an end of turn
	 * position. This means firing an MFG and creating the behaviors which go
	 * along with an MFG.
	 * 
	 * @param list
	 *            The list to append the behaviors to.
	 */
    private void execEnd(EBMLList list, String actor) {
        if (full) {
            this.logger.debug(LoggerLevel.POLICY_EXECUTION, "executing end");
            GazeBehavior gazeBehav = new GazeBehavior(logger, "mfg-gaze", actor, false);
            EmitBehavior emitBehav = emit.createMFGEmit(actor, gazeBehav);
            ConstraintBehavior constraint = list.getRequiredConstraints();
            SyncBlock block = new SyncBlock(new SyncRef(list.getID(), SyncPoint.END, 0));
            block.addRef(new SyncRef(gazeBehav, SyncPoint.START, 0));
            block.addRef(new SyncRef(emitBehav, SyncPoint.START, 0));
            constraint.addSynchronized(block);
            list.getGazes().add(gazeBehav);
            list.getEmits().add(emitBehav);
        }
    }

    private void execBackchannel(EBMLList list) {
        if (full) {
            this.logger.debug(LoggerLevel.POLICY_EXECUTION, "executing BC");
            Speech speech = new Speech();
            speech.actor.id = ActorID.ROBOT;
            speech.begin.value = Flag.BACKCHANNEL;
            speech.response.value = Flag.FALSE;
            bcpub.publish(speech);
        }
    }

    private void execDelay(EBMLList list, String delayTarget) {
        if (full) {
            if (delayTarget == null || delayTarget.isEmpty()) delayTarget = Entity.UP_RIGHT;
            this.logger.debug(LoggerLevel.POLICY_EXECUTION, "executing delay");
            GazeBehavior gaze = new GazeBehavior(logger, "delay-gaze", delayTarget, false);
            list.getGazes().add(gaze);
        }
    }

    /**
	 * The APWaiter class is internal to the TurnPositionPolicy class because no
	 * other class can create this object. The APWaiter's sole purpose is to
	 * wait for Adjacency pair requests to complete, and return the response to
	 * any other object which requires the response.
	 * 
	 * @author Aaron Holroyd (aholroyd (at) wpi (dot) edu)
	 */
    public class APWaiter {

        private final Thread thread;

        private byte response;

        /**
		 * Create a new APWaiter to call an AP on the given actor, and return
		 * the response when getResponse() is called.
		 * 
		 * @param actor
		 *            The actor to complete an AP with.
		 */
        private APWaiter(final String actor) {
            this.response = APAction.NO_ACTION;
            this.thread = new Thread() {

                /**
				 * The Robot is initiating an adjacency pair. The given actor is
				 * the subject to which the AP is directed. This method will
				 * call recognition and will block until recognition determines
				 * the event has succeeded or failed.
				 * 
				 * @param actor
				 *            The actor to direct the AP towards.
				 * @return The response type that the Actor responded with.
				 *         INVALID will be returned if an error occurred.
				 */
                public void run() {
                    RobotAdjacencyPair.Request req = new RobotAdjacencyPair.Request();
                    req.actor.id = actor;
                    try {
                        RobotAdjacencyPair.Response res = apsrv.call(req);
                        if (res.result.value == Flag.SUCCESS) {
                            response = res.action.value;
                        } else response = APAction.NO_ACTION;
                    } catch (Exception e) {
                        response = APAction.NO_ACTION;
                    }
                }

                ;
            };
            this.thread.start();
        }

        /**
		 * Get the response type which was returned by the AP service.
		 * 
		 * @return The type of response from recognition.
		 */
        public byte getResponse() {
            logger.debug(LoggerLevel.POLICY_EXECUTION, "waiting for AP response");
            try {
                if (this.thread.isAlive()) this.thread.join();
            } catch (InterruptedException e) {
            }
            return this.response;
        }
    }
}
