package org.epics.dds.dynamic.protobuf;

import java.io.IOException;
import java.util.Map;
import com.google.protobuf.WireFormat;
import com.google.protobuf.CodedInputStream;
import org.omg.dds.dynamic.TypeKind;
import org.omg.dds.dynamic.DynamicType;
import org.omg.dds.dynamic.DynamicTypeMember;
import org.omg.dds.dynamic.DynamicData;
import org.epics.dds.dynamic.DynamicTypeRegistry;
import org.epics.dds.dynamic.DynamicDataFactory;
import org.epics.dds.dynamic.PrimitiveData.*;
import org.epics.dds.dynamic.StringData;
import org.epics.dds.dynamic.StructureData;
import org.epics.dds.dynamic.SequenceData.*;

/**
 * Builder of dunamic data
 */
public class DynamicDataBuilder {

    /** singleton */
    static DynamicDataBuilder s_instance;

    /** Returns singleton */
    public static DynamicDataBuilder get_instance() {
        if (s_instance == null) {
            s_instance = new DynamicDataBuilder();
        }
        return s_instance;
    }

    /** Desierialize */
    public DynamicData parseFrom(byte[] data) throws IOException {
        CodedInputStream input = CodedInputStream.newInstance(data);
        DynamicType dt = getDynamicType(input);
        if (dt == null) return null;
        return createDynamicData(dt, input);
    }

    /** Desierialize */
    public DynamicData parseFrom(DynamicType dt, byte[] data) throws IOException {
        CodedInputStream input = CodedInputStream.newInstance(data);
        return createDynamicData(dt, input);
    }

    private DynamicDataBuilder() {
    }

    private DynamicType getDynamicType(CodedInputStream input) throws IOException {
        String typeName = input.readString();
        DynamicTypeRegistry dtr = DynamicTypeRegistry.get_instance();
        DynamicType dt = dtr.getType(typeName);
        return dt;
    }

    private DynamicData createDynamicData(DynamicType dt, CodedInputStream input) throws IOException {
        if (dt.is_complex()) {
            return createComplexData(dt, input);
        } else {
            return createSimpleData(dt, input);
        }
    }

    private DynamicData createSimpleData(DynamicType dt, CodedInputStream input) throws IOException {
        DynamicData dd = null;
        switch(dt.get_kind()) {
            case INT_32_TYPE:
                dd = new IntegerData();
                dd.set_int32_value(0, input.readSFixed32());
                break;
            case INT_64_TYPE:
                dd = new LongData();
                dd.set_int64_value(0, input.readSFixed64());
                break;
            case FLOAT_32_TYPE:
                dd = new FloatData();
                dd.set_float32_value(0, input.readFloat());
                break;
            case FLOAT_64_TYPE:
                dd = new DoubleData();
                dd.set_float64_value(0, input.readDouble());
                break;
            case STRING_TYPE:
                dd = new StringData(dt);
                dd.set_string_value(0, input.readString());
                break;
        }
        return dd;
    }

    private DynamicData createComplexData(DynamicType dt, CodedInputStream input) throws IOException {
        switch(dt.get_kind()) {
            case STRUCTURE_TYPE:
                return createStructureData(dt, input);
            case SEQUENCE_TYPE:
                return createSequenceData(dt, input);
        }
        return null;
    }

    private DynamicData createStructureData(DynamicType dt, CodedInputStream input) throws IOException {
        DynamicData dd = new StructureData(dt);
        Map<String, Integer> ids = dt.get_all_member_ids();
        int length = input.readSFixed32();
        for (int i = 0; i < ids.size(); i++) {
            int index = WireFormat.getTagFieldNumber(input.readTag()) - 1;
            DynamicTypeMember dtm = dt.get_member_by_id(i);
            DynamicType type = dtm.get_descriptor().type;
            if (type.is_complex()) {
                setComplexData(dd, index, type, input);
            } else {
                setSimpleData(type.get_kind(), dd, index, input);
            }
        }
        return dd;
    }

    private DynamicData createSequenceData(DynamicType dt, CodedInputStream input) throws IOException {
        int length = input.readSFixed32();
        org.omg.dds.dynamic.DynamicDataFactory ddf = DynamicDataFactory.get_instance();
        DynamicData seq = ddf.create_data(dt);
        seq.set_length(length);
        DynamicType elementType = dt.get_descriptor().element_type;
        TypeKind kind = elementType.get_kind();
        if (elementType.is_complex()) {
            for (int i = 0; i < length; i++) {
                setComplexData(seq, i, elementType, input);
            }
        } else {
            for (int i = 0; i < length; i++) {
                setSimpleData(kind, seq, i, input);
            }
        }
        return seq;
    }

    private void setSimpleData(TypeKind kind, DynamicData dd, int i, CodedInputStream input) throws IOException {
        switch(kind) {
            case INT_32_TYPE:
                dd.set_int32_value(i, input.readSFixed32());
                break;
            case INT_64_TYPE:
                dd.set_int64_value(i, input.readSFixed64());
                break;
            case FLOAT_32_TYPE:
                dd.set_float32_value(i, input.readFloat());
                break;
            case FLOAT_64_TYPE:
                dd.set_float64_value(i, input.readDouble());
                break;
            case STRING_TYPE:
                dd.set_string_value(i, input.readString());
                break;
        }
    }

    private void setComplexData(DynamicData container, int index, DynamicType dt, CodedInputStream input) throws IOException {
        DynamicData dd = createComplexData(dt, input);
        container.set_complex_value(index, dd);
    }
}
