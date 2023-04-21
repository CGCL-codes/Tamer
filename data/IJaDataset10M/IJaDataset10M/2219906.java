package org.gcreator.pineapple.pinec.compiler.api;

import java.util.List;

/**
 *
 * @author Luís Reis
 */
public interface Library {

    void addClass(APIClass cls);

    void addVariable(APIVariable var);

    List<APIVariable> getVariables();

    List<APIClass> getClasses();
}
