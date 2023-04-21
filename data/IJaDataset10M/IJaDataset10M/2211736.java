package com.ssg.tools.jsonxml;

import com.ssg.tools.jsonxml.utils.StructureErrorInfo;
import com.ssg.tools.jsonxml.utils.StructureProcessingContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Should support object structure validation based on schema.
 * Simplest (and the only for a while) validation is based on JSON schema.
 * 
 * @author ssg
 */
public class Validator {

    public static List<StructureErrorInfo> validate(StructureProcessingContext context, Object schema, Object object, List<StructureErrorInfo> errors) {
        if (errors == null) {
            errors = new ArrayList<StructureErrorInfo>();
        }
        return errors;
    }
}
