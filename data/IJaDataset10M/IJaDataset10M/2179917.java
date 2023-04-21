package abc.weaving.weaver.around;

import polyglot.util.InternalCompilerError;

class InternalAroundError extends InternalCompilerError {

    InternalAroundError(String message) {
        super("ARD around weaver internal error: " + message);
    }

    InternalAroundError(String message, Throwable cause) {
        super("ARD around weaver internal error: " + message, cause);
    }

    InternalAroundError() {
        super("ARD around weaver internal error");
    }
}
