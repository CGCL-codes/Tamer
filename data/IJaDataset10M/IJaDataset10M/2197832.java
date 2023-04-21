package org.zkoss.zkmax.xel.ognl;

import java.util.Map;
import ognl.ClassResolver;
import org.zkoss.lang.Classes;
import org.zkoss.xel.FunctionMapper;

class MapperClassResolver implements ClassResolver {

    private final FunctionMapper _mapper;

    public MapperClassResolver(FunctionMapper mapper) {
        _mapper = mapper;
    }

    public Class classForName(String className, Map context) throws ClassNotFoundException {
        final Class cls = _mapper != null ? _mapper.resolveClass(className) : null;
        return cls != null ? cls : Classes.forNameByThread(className);
    }
}
