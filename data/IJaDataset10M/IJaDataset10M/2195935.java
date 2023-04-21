package abstrasy.pcfx;

import abstrasy.Heap;
import abstrasy.Interpreter;
import abstrasy.Node;
import abstrasy.PCoder;
import abstrasy.StaticHeap;
import abstrasy.interpreter.BaseContextSnapshot;

/**
 * Abstrasy Interpreter
 *
 * Copyright : Copyright (c) 2006-2012, Luc Bruninx.
 *
 * Concédée sous licence EUPL, version 1.1 uniquement (la «Licence»).
 *
 * Vous ne pouvez utiliser la présente oeuvre que conformément à la Licence.
 * Vous pouvez obtenir une copie de la Licence à l’adresse suivante:
 *
 *   http://www.osor.eu/eupl
 *
 * Sauf obligation légale ou contractuelle écrite, le logiciel distribué sous
 * la Licence est distribué "en l’état", SANS GARANTIES OU CONDITIONS QUELLES
 * QU’ELLES SOIENT, expresses ou implicites.
 *
 * Consultez la Licence pour les autorisations et les restrictions
 * linguistiques spécifiques relevant de la Licence.
 *
 *
 * @author Luc Bruninx
 * @version 1.0
 */
public class PCFx_ensure extends PCFx {

    public PCFx_ensure() {
    }

    /**
     * eval
     *
     * @param startAt Node
     * @return Node
     * @throws Exception
     * @todo Implémenter cette méthode abstrasy.PCFx
     */
    public Node eval(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(4);
        Node xnode = startAt.getSubNode(1, Node.TYPE_LAZY);
        startAt.requirePCode(2, PCoder.PC_TERMINATION);
        Node enode = startAt.getSubNode(3, Node.TYPE_LAZY);
        Node res = null;
        Interpreter interpreter = Interpreter.mySelf();
        BaseContextSnapshot contextSnapshot = new BaseContextSnapshot(interpreter);
        Exception allExcep = null;
        try {
            Heap.push();
            res = xnode.exec(true);
            Heap.pull();
        } catch (Exception ex) {
            allExcep = ex;
        }
        long old_deadlockTime_after = interpreter.getDeadlockTime();
        boolean old_timeOutCheck_after = interpreter.isTimeOutCheck();
        boolean old_timeOutTimerStatus_after = interpreter.getTimeOutTimer().isRunning();
        if (old_timeOutCheck_after || old_timeOutTimerStatus_after) {
            interpreter.setTimeOutCheck(false);
            interpreter.getTimeOutTimer().stop();
        }
        boolean canloop_after = interpreter.isCanLoop();
        boolean inloop_after = interpreter.isInLoop();
        long callerSignature_after = interpreter.getCallerSignature();
        boolean tailNode_after = interpreter.isTailNode();
        boolean terminalNode_after = interpreter.isTerminalNode();
        Node oldThisNode_after = interpreter.getThisNode();
        boolean stop_status_after = interpreter.isThreadStopped();
        boolean breakingFromSemaphore_after = interpreter.isBreakingFromSEMAPHORE();
        boolean endingFromSemaphore_after = interpreter.isEndingFromSEMAPHORE();
        contextSnapshot.restore();
        boolean old_failOver = interpreter.isFailOver();
        interpreter.setFailOver(true);
        Node tres = null;
        try {
            Heap.push();
            tres = enode.exec(true);
            Heap.pull();
        } catch (Exception e) {
            Interpreter.Log("Exception in termination:\n" + e);
        }
        if (tres != null) {
            Interpreter.Log("Result value of termination:\n" + tres.toString());
        }
        contextSnapshot.restoreGlobalOnly();
        interpreter.setCanLoop(canloop_after);
        interpreter.setInLoop(inloop_after);
        interpreter.setCallerSignature(callerSignature_after);
        interpreter.setTailNode(tailNode_after);
        interpreter.setTerminalNode(terminalNode_after);
        interpreter.setThisNode(oldThisNode_after);
        interpreter.setDeadlockTime(old_deadlockTime_after);
        interpreter.setTimeOutCheck(old_timeOutCheck_after);
        if (old_timeOutCheck_after || old_timeOutTimerStatus_after) {
            interpreter.setTimeOutCheck(true);
            interpreter.getTimeOutTimer().start();
        }
        interpreter.setThreadStopped(stop_status_after || interpreter.isThreadStopped());
        interpreter.setBreakingFromSEMAPHORE(breakingFromSemaphore_after || interpreter.isBreakingFromSEMAPHORE());
        interpreter.setEndingFromSEMAPHORE(endingFromSemaphore_after || interpreter.isEndingFromSEMAPHORE());
        interpreter.setTimeOutRaising(old_timeOutCheck_after);
        interpreter.setFailOver(old_failOver);
        if (allExcep != null) {
            throw allExcep;
        } else {
            interpreter.throwInterThreadException();
        }
        return res;
    }
}
