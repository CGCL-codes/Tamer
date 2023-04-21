package ssgen.sql.oracle8.writer;

import ssgen.core.element.SsGenElement;
import ssgen.core.writer.AbstractElementWriter;
import ssgen.core.writer.ElementWriter;
import ssgen.sql.common.element.function.FunctionTypeMarker;
import ssgen.sql.common.writer.function.NoParameterFunctionWriter;
import ssgen.sql.oracle8.element.Oracle8Function;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Tadaya Tsuyukubo
 * <p/>
 * $Id$
 */
public class Oracle8NoParamFunctionWriter extends AbstractElementWriter {

    private static Map<FunctionTypeMarker, ElementWriter> writers = new HashMap<FunctionTypeMarker, ElementWriter>();

    static {
        writers.put(Oracle8Function.EMPTY_BLOB, new NoParameterFunctionWriter("EMPTY_BLOB"));
        writers.put(Oracle8Function.EMPTY_CLOB, new NoParameterFunctionWriter("EMPTY_CLOB"));
        writers.put(Oracle8Function.SYS_GUID, new NoParameterFunctionWriter("SYS_GUID"));
        writers.put(Oracle8Function.SYSDATE, new NoParameterFunctionWriter("SYSDATE", false));
        writers.put(Oracle8Function.UID, new NoParameterFunctionWriter("UID", false));
        writers.put(Oracle8Function.USER, new NoParameterFunctionWriter("USER", false));
    }

    public String write(SsGenElement element) {
        return null;
    }
}
