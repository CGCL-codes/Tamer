package org.openorb.orb.core.dynany;

/**
 * Implantation de DynUnion
 *
 * @author Jerome Daniel
 * @version $Revision: 1.6 $ $Date: 2004/02/13 11:43:05 $
 */
class DynUnionImpl extends org.openorb.orb.core.dynany.DynAnyImpl implements org.omg.DynamicAny.DynUnion {

    /**
     * Position de l'element actuel
     */
    private int m_current;

    /**
     * Membres de l'union ( Discriminant + Membre )
     */
    private org.omg.DynamicAny.DynAny[] m_members;

    /**
     * Constructeur
     */
    public DynUnionImpl(org.omg.DynamicAny.DynAnyFactory factory, org.omg.CORBA.ORB orb, org.omg.CORBA.TypeCode type) {
        super(factory, orb);
        m_type = type;
        m_current = 0;
        m_members = create_dyn_any_graph(type);
        rewind();
    }

    /**
     * Operation assign
     */
    public void assign(org.omg.DynamicAny.DynAny dyn_any) throws org.omg.DynamicAny.DynAnyPackage.TypeMismatch {
        if (!dyn_any.type().equivalent(m_type)) {
            throw new org.omg.DynamicAny.DynAnyPackage.TypeMismatch();
        }
        m_members = ((DynUnionImpl) dyn_any).copy_dyn_any_graph(((DynUnionImpl) dyn_any).m_members);
    }

    /**
     * Operation from_any
     */
    public void from_any(org.omg.CORBA.Any value) throws org.omg.DynamicAny.DynAnyPackage.InvalidValue, org.omg.DynamicAny.DynAnyPackage.TypeMismatch {
        org.omg.CORBA.portable.InputStream stream;
        if (!value.type().equal(m_type)) {
            throw new org.omg.DynamicAny.DynAnyPackage.TypeMismatch();
        }
        stream = value.create_input_stream();
        stream_to_dyn_any_graph(m_members, stream);
    }

    /**
     * Operation to_any
     */
    public org.omg.CORBA.Any to_any() {
        org.omg.CORBA.Any any = m_orb.create_any();
        any.type(m_type);
        org.omg.CORBA.portable.OutputStream stream = any.create_output_stream();
        dyn_any_graph_to_stream(m_members, stream);
        return any;
    }

    /**
     * Operation destroy
     */
    public void destroy() {
        m_members = null;
        System.gc();
    }

    /**
     * Operation copy
     */
    public org.omg.DynamicAny.DynAny copy() {
        DynUnionImpl dyn_un = new DynUnionImpl(m_factory, m_orb, m_type);
        dyn_un.m_members[0] = m_members[0].copy();
        dyn_un.m_members[1] = m_members[1].copy();
        return dyn_un;
    }

    /**
     * Operation current_component
     */
    public org.omg.DynamicAny.DynAny current_component() {
        return m_members[m_current];
    }

    /**
     * Operation next
     */
    public boolean next() {
        m_current++;
        if (m_current < m_members.length) {
            if (m_current == 1) {
                buildMember();
            }
            m_any = (org.openorb.orb.core.Any) m_members[m_current].to_any();
            return true;
        }
        m_current--;
        return false;
    }

    /**
     * Operation seek
     */
    public boolean seek(int index) {
        if (index == -1) {
            return false;
        }
        if (index < m_members.length) {
            m_current = index;
            if (m_current == 1) {
                buildMember();
            }
            m_any = (org.openorb.orb.core.Any) m_members[m_current].to_any();
            return true;
        }
        return false;
    }

    /**
     * Operation rewind
     */
    public void rewind() {
        m_current = 0;
        m_any = (org.openorb.orb.core.Any) m_members[0].to_any();
    }

    /**
     * Operation component_count
     */
    public int component_count() {
        if (m_members[1] == null) {
            return 1;
        }
        return 2;
    }

    /**
     * Operation set_to_default_member
     */
    public void set_to_default_member() throws org.omg.DynamicAny.DynAnyPackage.TypeMismatch {
        try {
            if (m_type.default_index() != -1) {
                findValue();
                buildMember();
            } else {
                throw new org.omg.DynamicAny.DynAnyPackage.TypeMismatch();
            }
        } catch (org.omg.CORBA.TypeCodePackage.BadKind ex) {
        }
    }

    /**
     * Operation set_to_no_active_member
     */
    public void set_to_no_active_member() throws org.omg.DynamicAny.DynAnyPackage.TypeMismatch {
        try {
            if (((org.openorb.orb.core.typecode.TypeCodeBase) m_type)._base_type().default_index() == -1) {
                throw new org.omg.DynamicAny.DynAnyPackage.TypeMismatch();
            }
        } catch (org.omg.CORBA.TypeCodePackage.BadKind ex) {
        }
        m_members[1] = null;
        findValue();
        rewind();
    }

    /**
     * Operation has_no_active_member
     */
    public boolean has_no_active_member() {
        org.omg.CORBA.Any a = m_members[0].to_any();
        try {
            org.omg.CORBA.TypeCode tc = ((org.openorb.orb.core.typecode.TypeCodeBase) m_type)._base_type();
            for (int i = 0; i < tc.member_count(); i++) {
                if (a.equal(tc.member_label(i))) {
                    return false;
                }
            }
            return true;
        } catch (org.omg.CORBA.TypeCodePackage.BadKind ex) {
        } catch (org.omg.CORBA.TypeCodePackage.Bounds ex) {
        }
        return false;
    }

    /**
     * Operation get_discriminator
     */
    public org.omg.DynamicAny.DynAny get_discriminator() {
        return m_members[0];
    }

    /**
     * Operation set_discriminator
     */
    public void set_discriminator(org.omg.DynamicAny.DynAny d) throws org.omg.DynamicAny.DynAnyPackage.TypeMismatch {
        if (!d.type().equivalent(m_members[0].type())) {
            throw new org.omg.DynamicAny.DynAnyPackage.TypeMismatch();
        }
        m_members[0] = d;
        if (!has_no_active_member()) {
            seek(1);
        } else {
            rewind();
        }
    }

    /**
     * Operation discriminator_kind
     */
    public org.omg.CORBA.TCKind discriminator_kind() {
        return m_members[0].type().kind();
    }

    /**
     * Operation member
     */
    public org.omg.DynamicAny.DynAny member() throws org.omg.DynamicAny.DynAnyPackage.InvalidValue {
        buildMember();
        return m_members[1];
    }

    /**
     * Read accessor for member_name attribute
     * @return the attribute value
     */
    public java.lang.String member_name() throws org.omg.DynamicAny.DynAnyPackage.InvalidValue {
        org.omg.CORBA.Any any = null;
        try {
            any = m_members[0].to_any();
            for (int i = 0; i < m_type.member_count(); i++) {
                if (any.equal(m_type.member_label(i))) {
                    return m_type.member_name(i);
                }
            }
            if (m_type.default_index() != -1) {
                return m_type.member_name(m_type.default_index());
            }
        } catch (org.omg.CORBA.TypeCodePackage.BadKind ex) {
        } catch (org.omg.CORBA.TypeCodePackage.Bounds ex) {
        }
        return null;
    }

    /**
     * Operation member_kind
     */
    public org.omg.CORBA.TCKind member_kind() throws org.omg.DynamicAny.DynAnyPackage.InvalidValue {
        if (m_members[1] != null) {
            return m_members[1].type().kind();
        }
        throw new org.omg.DynamicAny.DynAnyPackage.InvalidValue();
    }

    private org.omg.CORBA.TypeCode findDefaultTypeCode() {
        try {
            if (m_members[0].type().equal(m_orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_octet))) {
                if (m_type.default_index() != -1) {
                    return m_type.member_type(m_type.default_index());
                }
            }
            org.omg.CORBA.Any dvalue = m_members[0].to_any();
            for (int i = 0; i < m_type.member_count(); i++) {
                if (i != m_type.default_index()) {
                    if (dvalue.equal(m_type.member_label(i))) {
                        return m_type.member_type(i);
                    }
                }
            }
            if (m_type.default_index() != -1) {
                return m_type.member_type(m_type.default_index());
            }
        } catch (org.omg.CORBA.TypeCodePackage.BadKind ex) {
        } catch (org.omg.CORBA.TypeCodePackage.Bounds ex) {
        }
        return m_orb.get_primitive_tc(org.omg.CORBA.TCKind.tk_null);
    }

    private void buildMember() {
        boolean toDo = true;
        org.omg.CORBA.TypeCode tc = findDefaultTypeCode();
        if (m_members[1] != null) {
            if (tc.equivalent(m_members[1].type())) {
                toDo = false;
            }
        }
        if (toDo) {
            m_members[1] = create_dyn_any(tc);
        }
    }

    private void findValue() throws org.omg.DynamicAny.DynAnyPackage.TypeMismatch {
        org.omg.CORBA.Any value = org.omg.CORBA.ORB.init().create_any();
        boolean found = false;
        org.omg.CORBA.TypeCode tc = ((org.openorb.orb.core.typecode.TypeCodeBase) m_type)._base_type();
        try {
            switch(tc.discriminator_type().kind().value()) {
                case org.omg.CORBA.TCKind._tk_boolean:
                    value.insert_boolean(false);
                    if (is_valid(tc, value)) {
                        found = true;
                    } else {
                        value.insert_boolean(true);
                        if (is_valid(tc, value)) {
                            found = true;
                        }
                    }
                    break;
                case org.omg.CORBA.TCKind._tk_short:
                    for (short i = java.lang.Short.MIN_VALUE; i < java.lang.Short.MAX_VALUE; i++) {
                        value.insert_short(i);
                        if (is_valid(tc, value)) {
                            found = true;
                            break;
                        }
                    }
                    break;
                case org.omg.CORBA.TCKind._tk_ushort:
                    for (short i = 0; i < java.lang.Short.MAX_VALUE; i++) {
                        value.insert_ushort(i);
                        if (is_valid(tc, value)) {
                            found = true;
                            break;
                        }
                    }
                    break;
                case org.omg.CORBA.TCKind._tk_long:
                    for (int i = java.lang.Integer.MIN_VALUE; i < java.lang.Integer.MAX_VALUE; i++) {
                        value.insert_long(i);
                        if (is_valid(tc, value)) {
                            found = true;
                            break;
                        }
                    }
                    break;
                case org.omg.CORBA.TCKind._tk_ulong:
                    for (int i = 0; i < java.lang.Integer.MAX_VALUE; i++) {
                        value.insert_long(i);
                        if (is_valid(tc, value)) {
                            found = true;
                            break;
                        }
                    }
                    break;
                case org.omg.CORBA.TCKind._tk_longlong:
                    for (long i = java.lang.Long.MIN_VALUE; i < java.lang.Long.MAX_VALUE; i++) {
                        value.insert_longlong(i);
                        if (is_valid(tc, value)) {
                            found = true;
                            break;
                        }
                    }
                    break;
                case org.omg.CORBA.TCKind._tk_ulonglong:
                    for (long i = 0; i < java.lang.Long.MAX_VALUE; i++) {
                        value.insert_ulonglong(i);
                        if (is_valid(tc, value)) {
                            found = true;
                            break;
                        }
                    }
                    break;
                case org.omg.CORBA.TCKind._tk_char:
                    for (short i = 0; i < 255; i++) {
                        value.insert_char((char) i);
                        if (is_valid(tc, value)) {
                            found = true;
                            break;
                        }
                    }
                    break;
                case org.omg.CORBA.TCKind._tk_wchar:
                    for (char i = Character.MIN_VALUE; i < Character.MAX_VALUE; i++) {
                        value.insert_wchar(i);
                        if (is_valid(tc, value)) {
                            found = true;
                            break;
                        }
                    }
                    break;
                case org.omg.CORBA.TCKind._tk_enum:
                    for (int i = 0; i < tc.discriminator_type().member_count(); i++) {
                        value.insert_ulong(i);
                        if (is_valid(tc, value)) {
                            found = true;
                            break;
                        }
                    }
                    break;
                default:
                    throw new org.omg.DynamicAny.DynAnyPackage.TypeMismatch();
            }
            if (found) {
                value.type(m_type.discriminator_type());
                m_members[0].from_any(value);
                return;
            }
        } catch (org.omg.CORBA.TypeCodePackage.BadKind ex) {
        } catch (org.omg.DynamicAny.DynAnyPackage.InvalidValue ex) {
        }
        throw new org.omg.DynamicAny.DynAnyPackage.TypeMismatch();
    }

    private boolean is_valid(org.omg.CORBA.TypeCode tc, org.omg.CORBA.Any val) {
        try {
            val.type(m_type.discriminator_type());
            for (int i = 0; i < tc.member_count(); i++) {
                if (val.equal(tc.member_label(i))) {
                    return false;
                }
            }
        } catch (org.omg.CORBA.TypeCodePackage.BadKind ex) {
        } catch (org.omg.CORBA.TypeCodePackage.Bounds ex) {
        }
        return true;
    }
}
