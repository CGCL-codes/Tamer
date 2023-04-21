package verifier.task;

import java.io.IOException;
import java.io.OutputStream;
import sexpression.*;
import verifier.*;
import verifier.value.*;
import verifier.ast.*;

/**
 * Tasks represent a computation job, the result of which will be realized in
 * some future instance. Tasks are abstract because they exist in two places: on
 * the compute node, and on the controller.
 * 
 * @author derrley
 * 
 */
public class Task {

    public final Long _future;

    protected final AST _ast;

    protected final ActivationRecord _environment;

    private OutputStream _outbound;

    /**
	 * @param future
	 *            Construct a task whose results will get realized in this
	 *            future.
	 * @param ast
	 *            Construct a task which evaluates this AST.
	 * @param environment
	 *            Construct a task which does an evaluation in this environment.
	 */
    public Task(Future future, AST ast, ActivationRecord environment) {
        if (future == null) _future = null; else _future = future._id;
        _ast = ast;
        _environment = environment;
    }

    /**
	 * @param os
	 *            When the task is finished, the result of the task will be
	 *            written to this stream.
	 */
    public void setOutbound(OutputStream os) {
        _outbound = os;
    }

    /**
	 * Construct a task object from its wire format.
	 * 
	 * @param ase
	 *            This is the s-expression heard over the wire.
	 */
    public Task(ASExpression ase) {
        ListExpression list = (ListExpression) ase;
        _future = Long.parseLong(list.get(0).toString());
        _ast = ASTParser.PARSER.parse(list.get(1));
        _environment = new ActivationRecord(list.get(2));
    }

    /**
	 * @return This method returns the s-expression format for the target task.
	 */
    public ASExpression toASE() {
        return new ListExpression(StringExpression.make(Long.toString(_future)), _ast.toASE(), _environment.toASE());
    }

    public void run() {
        Value v = _ast.eval(_environment);
        try {
            _outbound.write(new ListExpression(StringExpression.make(Long.toString(_future)), v.toASE()).toVerbatim());
            _outbound.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
