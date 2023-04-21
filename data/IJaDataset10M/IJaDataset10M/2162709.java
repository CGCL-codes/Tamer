package com.jspx.sober.sequences;

import com.jspx.sober.SoberSupport;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2005-11-12
 * Time: 17:20:21
 * 
 */
public interface SequencesDAO extends SoberSupport {

    String getNextKey(String table);

    Sequences getSequences(final String table);
}
