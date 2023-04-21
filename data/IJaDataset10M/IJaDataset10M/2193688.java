package tcl.lang;

/**
 * This class implements the built-in "java::try" command. The command
 * provides access to the java's try-catch-finally construct.
 */
public class JavaTryCmd implements Command {

    /**

 *----------------------------------------------------------------------
 *
 * cmdProc --
 *
 *      This procedure is invoked as part of the Command interface to
 *      process the "java::try" Tcl command.  See the user documentation
 *      for details on what it does.
 *
 * Results:
 *      None.
 *
 * Side effects:
 *      See the user documentation.
 *
 *----------------------------------------------------------------------
 */
    public void cmdProc(Interp interp, TclObject[] argv) throws TclException {
        int argv_length_mod = argv.length % 3;
        boolean try_catch_finally = false;
        boolean try_catch = false;
        if (argv_length_mod == 1) {
            try_catch_finally = true;
        } else if (argv_length_mod == 2) {
            try_catch = true;
        }
        if (argv.length < 4 || (!try_catch && !try_catch_finally)) {
            throw new TclNumArgsException(interp, 1, argv, "script ?catch exception_pair script? ?finally script?");
        }
        eval(interp, argv[1]);
        if (exrec.exception_thrown) {
            if (debug) {
                System.out.print("Exception thrown inside body block, type = ");
                if (exrec.reflect_exception != null) System.out.println("ReflectException"); else if (exrec.tcl_exception != null) System.out.println("TclException"); else if (exrec.runtime_exception != null) System.out.println("RuntimeException"); else throw new TclRuntimeError("Java exception not found");
            }
            int end_loop = argv.length;
            if (try_catch_finally) {
                end_loop -= 2;
            }
            for (int i = 2; i < end_loop; i += 3) {
                TclObject catch_clause = argv[i];
                TclObject exception_pair = argv[i + 1];
                TclObject exception_script = argv[i + 2];
                if (!catch_clause.toString().equals("catch")) {
                    throw new TclException(interp, "invalid catch clause \"" + catch_clause.toString() + "\"");
                }
                boolean exception_pair_problem = false;
                TclObject type = null;
                TclObject var = null;
                try {
                    if (TclList.getLength(interp, exception_pair) != 2) {
                        exception_pair_problem = true;
                    } else {
                        type = TclList.index(interp, exception_pair, 0);
                        var = TclList.index(interp, exception_pair, 1);
                        if (TclList.getLength(interp, type) != 1 || TclList.getLength(interp, var) != 1) {
                            exception_pair_problem = true;
                        }
                    }
                } catch (TclException e) {
                    exception_pair_problem = true;
                }
                if (exception_pair_problem) {
                    throw new TclException(interp, "invalid exception_pair \"" + exception_pair.toString() + "\"");
                }
                if (debug) {
                    System.out.println("type is \"" + type.toString() + "\"");
                    System.out.println("var is \"" + var.toString() + "\"");
                    System.out.println("script is \"" + exception_script.toString() + "\"");
                }
                Class matched_class = null;
                String type_name = type.toString();
                int type_len = type_name.length();
                if (debug) {
                    System.out.println("type_name is \"" + type_name + "\"");
                }
                Class ex_type;
                if (exrec.reflect_exception != null) {
                    ex_type = exrec.reflect_exception.getThrowable().getClass();
                } else if (exrec.tcl_exception != null) {
                    ex_type = exrec.tcl_exception.getClass();
                } else if (exrec.runtime_exception != null) {
                    ex_type = exrec.runtime_exception.getClass();
                } else {
                    throw new TclRuntimeError("Exception not found");
                }
                if (debug) {
                    System.out.println("ex_type.getName() is " + ex_type.getName());
                }
                String ex_type_name;
                if (type_name.equals("TclInterruptedException") || type_name.equals("tcl.lang.TclInterruptedException")) {
                    throw new TclException(interp, "can't catch TclInterruptedException");
                } else if ((exrec.runtime_exception != null) && ex_type.getName().equals("tcl.lang.TclInterruptedException")) {
                    if (debug) {
                        System.out.println("skipped TclInterruptedException");
                    }
                    break;
                } else if (type_name.equals("TclException") || (exrec.tcl_exception != null)) {
                    if (type_name.equals("TclException") && (exrec.tcl_exception != null)) {
                        matched_class = TclException.class;
                        if (debug) {
                            System.out.println("match for \"" + matched_class.getName() + "\" == \"" + type_name + "\"");
                        }
                    } else {
                        if (debug) {
                            System.out.println("skipping catch block because " + "of TclException mismatch");
                            System.out.println("exception name = \"" + type_name + "\"");
                            System.out.println("caught TclException = " + (exrec.tcl_exception != null));
                        }
                        continue;
                    }
                } else {
                    while (ex_type != null) {
                        ex_type_name = ex_type.getName();
                        if (debug) {
                            System.out.println("ex_type_name is \"" + ex_type_name + "\"");
                        }
                        if (ex_type_name.equals(type_name)) {
                            matched_class = ex_type;
                        } else {
                            int last = ex_type_name.lastIndexOf('.');
                            if ((last != -1) && ((ex_type_name.length() - (last + 1)) == type_len) && ex_type_name.regionMatches(last + 1, type_name, 0, type_len)) {
                                matched_class = ex_type;
                            }
                        }
                        if (matched_class != null) {
                            if (debug) {
                                System.out.println("match for \"" + matched_class.getName() + "\" == \"" + type_name + "\"");
                            }
                            break;
                        }
                        if (ex_type == Throwable.class) {
                            ex_type = null;
                        } else {
                            ex_type = ex_type.getSuperclass();
                        }
                    }
                }
                if (matched_class != null) {
                    if (matched_class == TclException.class) {
                        if (debug) {
                            System.out.println("TclException result getting saved in exception varaible");
                        }
                        TclObject res = interp.getResult();
                        res.preserve();
                        try {
                            interp.setVar(var.toString(), res, 0);
                        } catch (TclException e) {
                            throw new TclRuntimeError("could not reflect or set exception variable");
                        }
                        res.release();
                    } else {
                        if (debug) {
                            System.out.println("JavaException result getting saved in exception varaible");
                        }
                        Throwable t;
                        if (exrec.reflect_exception != null) {
                            t = exrec.reflect_exception.getThrowable();
                        } else if (exrec.runtime_exception != null) {
                            t = exrec.runtime_exception;
                        } else {
                            throw new TclRuntimeError("Java exception not found");
                        }
                        try {
                            interp.setVar(var.toString(), ReflectObject.newInstance(interp, matched_class, t), 0);
                        } catch (TclException e) {
                            throw new TclRuntimeError("could not reflect or set exception variable");
                        }
                    }
                    eval(interp, exception_script);
                    if (debug) {
                        if (exrec.exception_thrown) System.out.println("exception thrown by exception handler"); else System.out.println("no exception thrown by exception handler");
                    }
                    break;
                }
            }
        }
        if (try_catch_finally) {
            TclObject finally_clause = argv[argv.length - 2];
            TclObject finally_script = argv[argv.length - 1];
            if (!finally_clause.toString().equals("finally")) {
                throw new TclException(interp, "invalid finally clause \"" + finally_clause.toString() + "\"");
            }
            if (debug) {
                System.out.println("finally script is \"" + finally_script.toString() + "\"");
            }
            TclObject res = interp.getResult();
            res.preserve();
            interp.resetResult();
            ExRecord tmp = exrec;
            exrec = tmp_exrec;
            tmp_exrec = tmp;
            eval(interp, finally_script);
            if ((exrec.exception_thrown == false) || ((exrec.exception_thrown == true) && (tmp_exrec.runtime_exception != null) && tmp_exrec.runtime_exception.getClass().getName().equals("tcl.lang.TclInterruptedException"))) {
                if (debug) {
                    System.out.println("resetting result and exception record");
                }
                interp.setResult(res);
                tmp = exrec;
                exrec = tmp_exrec;
                tmp_exrec = tmp;
            }
            res.release();
        }
        if (exrec.exception_thrown) {
            if (debug) {
                System.out.print("throwing end of try method exception, type = ");
                if (exrec.reflect_exception != null) System.out.println("ReflectException"); else if (exrec.tcl_exception != null) System.out.println("TclException"); else if (exrec.runtime_exception != null) System.out.println("RuntimeException"); else throw new TclRuntimeError("Java exception not found");
            }
            if (exrec.reflect_exception != null) {
                if (debug) {
                    System.out.println("rethrowing ReflectException " + exrec.reflect_exception);
                }
                throw exrec.reflect_exception;
            } else if (exrec.tcl_exception != null) {
                if (debug) {
                    System.out.println("rethrowing TclException " + exrec.tcl_exception);
                }
                throw exrec.tcl_exception;
            } else if (exrec.runtime_exception != null) {
                if (debug) {
                    System.out.println("rethrowing RuntimeException " + exrec.runtime_exception);
                }
                throw exrec.runtime_exception;
            } else {
                throw new TclRuntimeError("Java exception not found");
            }
        }
    }

    private static class ExRecord {

        ReflectException reflect_exception;

        TclException tcl_exception;

        RuntimeException runtime_exception;

        boolean exception_thrown;
    }

    ExRecord exrec = new ExRecord();

    ExRecord tmp_exrec = new ExRecord();

    private void eval(Interp interp, TclObject script) {
        exrec.reflect_exception = null;
        exrec.tcl_exception = null;
        exrec.runtime_exception = null;
        exrec.exception_thrown = false;
        try {
            interp.eval(script, 0);
        } catch (ReflectException e) {
            if (debug) {
                System.out.println("eval() caught ReflectException");
            }
            exrec.reflect_exception = e;
            exrec.exception_thrown = true;
        } catch (TclException e) {
            if (debug) {
                System.out.println("eval() caught TclException");
            }
            exrec.tcl_exception = e;
            exrec.exception_thrown = true;
            int code = e.getCompletionCode();
            if (code == TCL.RETURN) {
                exrec.exception_thrown = false;
            }
        } catch (RuntimeException e) {
            if (debug) {
                System.out.println("catching RuntimeException");
            }
            exrec.runtime_exception = e;
            exrec.exception_thrown = true;
        }
    }

    private static final boolean debug = false;
}
