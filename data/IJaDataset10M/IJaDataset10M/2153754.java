package jam4j.lang;

import jam4j.build.Target;

final class UserProcedure extends Procedure {

    final Statement body;

    final String[] argNames;

    UserProcedure(String name, String[] argNames, Statement body) {
        this.argNames = argNames == null ? Expression.UNDEF : argNames;
        this.body = body;
    }

    String[] call(Context cxt, Target onTarget, String[]... args) {
        if (onTarget != null) cxt.enterOnTarget(onTarget); else cxt.enterScope();
        final int argc = args.length;
        for (int ix = 0; ix < argc; ix++) {
            if (argNames != null && ix < argNames.length) cxt.setLocal(argNames[ix], args[ix]);
            cxt.setLocal(String.valueOf(ix + 1), args[ix]);
        }
        try {
            return body.runRuleBody(cxt);
        } finally {
            cxt.leaveScope();
        }
    }

    @Override
    public String toString() {
        return "UserProcedure [" + body + ']';
    }
}
