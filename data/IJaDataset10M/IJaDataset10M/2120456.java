package com.k_int.gen.Z39_50_APDU_1995;

import org.apache.log4j.Category;
import java.math.BigInteger;
import com.k_int.codec.runtime.*;
import com.k_int.gen.AsnUseful.*;

public class ResourceReportRequest_codec extends base_codec {

    private static Category cat = Category.getInstance("a2j");

    public static ResourceReportRequest_codec me = null;

    private ReferenceId_codec i_referenceid_codec = ReferenceId_codec.getCodec();

    private OtherInformation_codec i_otherinformation_codec = OtherInformation_codec.getCodec();

    private ResourceReportId_codec i_resourcereportid_codec = ResourceReportId_codec.getCodec();

    public static synchronized ResourceReportRequest_codec getCodec() {
        if (me == null) {
            me = new ResourceReportRequest_codec();
        }
        return me;
    }

    public Object serialize(SerializationManager sm, Object type_instance, boolean is_optional, String type_name) throws java.io.IOException {
        ResourceReportRequest_type retval = (ResourceReportRequest_type) type_instance;
        if (sm.sequenceBegin()) {
            if (sm.getDirection() == SerializationManager.DIRECTION_DECODE) {
                retval = new ResourceReportRequest_type();
            }
            retval.referenceId = (java.lang.String) i_referenceid_codec.serialize(sm, retval.referenceId, true, "referenceId");
            retval.opId = (java.lang.String) sm.implicit_tag(i_referenceid_codec, retval.opId, 128, 210, true, "opId");
            retval.prefResourceReportFormat = (int[]) sm.implicit_tag(i_resourcereportid_codec, retval.prefResourceReportFormat, 128, 49, true, "prefResourceReportFormat");
            retval.otherInfo = (java.util.Vector) i_otherinformation_codec.serialize(sm, retval.otherInfo, true, "otherInfo");
            sm.sequenceEnd();
        }
        return retval;
    }
}
