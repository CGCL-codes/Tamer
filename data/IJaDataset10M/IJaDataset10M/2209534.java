package org.epics.dds.dynamic.ami;

import org.omg.dds.dynamic.TypeDescriptor;
import org.omg.dds.dynamic.TypeKind;
import org.omg.dds.dynamic.MemberDescriptor;
import org.epics.dds.dynamic.DynamicTypeFactory;
import org.epics.dds.dynamic.DynamicTypeRegistry;
import org.epics.dds.dynamic.StructureType;

/**
 * Defines a structure for sending a request to the Machine Server .
 */
public class MachineRequestType extends StructureType {

    private static TypeDescriptor s_td;

    private static MemberDescriptor[] s_mds;

    static {
        String[] memberNames = { "command", "file" };
        s_td = new TypeDescriptor();
        s_td.name = "org.epics.dds.dynamic.ami.MachineRequestType";
        s_td.bound = new int[1];
        s_td.bound[0] = memberNames.length;
        s_td.kind = TypeKind.STRUCTURE_TYPE;
        org.omg.dds.dynamic.DynamicTypeFactory dtf = DynamicTypeFactory.get_instance();
        s_mds = new MemberDescriptor[memberNames.length];
        for (int i = 0; i < memberNames.length; i++) {
            s_mds[i] = new MemberDescriptor();
            s_mds[i].name = memberNames[i];
            s_mds[i].id = i;
            s_mds[i].index = i;
            s_mds[i].type = dtf.create_string_type(0);
        }
    }

    /** Constructor */
    public MachineRequestType() {
        super(s_td, s_mds);
    }
}
