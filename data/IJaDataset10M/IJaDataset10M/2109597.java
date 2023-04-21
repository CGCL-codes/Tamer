package it.gashale.jacolib.shell;

import it.gashale.jacolib.core.EvalError;
import it.gashale.jacolib.core.JacolibError;

public interface ShellInterface {

    String getLanguageName() throws JacolibError;

    String hello() throws JacolibError;

    Object get(String key) throws EvalError, JacolibError;

    void set(String key, Object obj) throws EvalError, JacolibError;

    void unset(String key) throws EvalError, JacolibError;

    Object eval(String code) throws EvalError, JacolibError;

    Object source(String filename) throws EvalError, JacolibError;
}
