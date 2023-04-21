package com.gorillalogic.dal.common;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;

public interface ExecutionBlock {

    /**
	 * Every block may have a user-defined name. For Methods this
	 * will be the method name. Triggers are optionally named.
	 *
	 * @return the block name
	 */
    String getName();

    void setName(String name) throws AccessException;

    /**
	 * The format identifies the language the body is defined in,
	 * e.g. "gosh", "gython", "java", etc.
	 *
	 * @return a <code>String</code> value
	 */
    String getFormat();

    /**
	 * The actual function definition.
	 *
	 * @return the body definition if available, else null
	 */
    String getBody();

    /**
	 * Where the body can be overwritten explicitly (isWritable
	 * returns true), provide that access here.
	 *
	 * @param body the new function body
	 */
    void setBody(String body) throws AccessException;

    /**
	 * Indicate whether setBody can be called.
	 *
	 * @return true iff setBody can be called without generating an
	 * illegal operation exception.
	 */
    boolean isWritable();

    boolean outOfDate();

    /**
	 * Prepare body for execution.
	 *
	 * TBR -reuse of blocks across models; if so precompilation
	 * in that context; also too open here to allow the type to
	 * be passed in a public method.
	 *
	 * @param scope the <code>CommonScope</code> starting scope;
	 * 'self' will be set to this value and any arguments are in
	 * scope.invocation().localVars()
	 *
	 * @exception AccessException if an error occurs
	 */
    void preCompile(CommonScope scope) throws AccessException;
}
