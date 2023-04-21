package abstrasy.pcfx;

import abstrasy.externals.External_Exception;
import abstrasy.Interpreter;
import abstrasy.Node;
import abstrasy.PCoder;
import abstrasy.interpreter.SilentException;
import abstrasy.interpreter.AbortException;
import abstrasy.interpreter.BaseContextSnapshot;
import abstrasy.interpreter.InterpreterException;
import abstrasy.interpreter.RestartException;
import abstrasy.interpreter.RetryException;

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
public class PCFx_try extends PCFx {

    public PCFx_try() {
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
        startAt.requirePCode(2, PCoder.PC_CATCH);
        Node enode = startAt.getSubNode(3, Node.TYPE_LAZY);
        Node res = null;
        Interpreter interpreter = Interpreter.mySelf();
        BaseContextSnapshot contextSnapshot = new BaseContextSnapshot(interpreter);
        boolean isRetrying = true;
        while (isRetrying) {
            try {
                abstrasy.Heap.push();
                res = xnode.exec(true);
                abstrasy.Heap.pull();
                isRetrying = false;
            } catch (AbortException abortExc) {
                contextSnapshot.restore();
                isRetrying = false;
            } catch (RetryException retryExc) {
                contextSnapshot.restore();
                isRetrying = true;
            } catch (RestartException restartExc) {
                contextSnapshot.restore();
                throw restartExc;
            } catch (SilentException silex) {
                contextSnapshot.restore();
                throw silex;
            } catch (Exception excep) {
                contextSnapshot.restore();
                if (excep instanceof InterpreterException) {
                    if (((InterpreterException) excep).getErrCode() < 0) {
                        throw excep;
                    }
                }
                try {
                    Node argv = Node.createEmptyList();
                    argv.addElement(External_Exception.create(excep));
                    abstrasy.Heap.push();
                    abstrasy.Heap.setv(PCoder.ARGV, argv);
                    res = enode.exec(true);
                    abstrasy.Heap.pull();
                    isRetrying = false;
                } catch (RetryException retryExc) {
                    contextSnapshot.restore();
                    isRetrying = true;
                } catch (AbortException abortExc) {
                    contextSnapshot.restore();
                    isRetrying = false;
                } catch (Exception otherex) {
                    contextSnapshot.restore();
                    throw otherex;
                }
            }
        }
        return res;
    }
}
