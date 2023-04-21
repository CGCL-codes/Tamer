package org.openorb.orb.io;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.ORB;
import org.omg.CORBA_2_3.portable.OutputStream;
import org.omg.CORBA_2_3.portable.InputStream;
import org.apache.avalon.framework.logger.Logger;

/**
 * This class masquarades as the valuetype passed to it in a typecode. It can
 * be used in any situation where the original valuetype was used, providing
 * the runtime type is a general java.io.Serializable, so it can be inserted
 * and extracted from anys and marshaled using DII.
 *
 * @author Chris Wood
 * @version $Revision: 1.4 $ $Date: 2004/02/10 21:02:50 $
 */
public class TypeCodeStreamableValue implements org.omg.CORBA.portable.StreamableValue {

    private TypeCode m_tc;

    private ORB m_orb;

    private ListOutputStream m_os;

    private String[] m_truncatable_ids = null;

    private Logger m_logger = null;

    /**
     * @throws org.omg.CORBA.TypeCodePackage.BadKind The specified typecode is
     * not a valuetype.
     */
    public TypeCodeStreamableValue(ORB orb, TypeCode tc) throws org.omg.CORBA.TypeCodePackage.BadKind {
        if (m_tc.kind() != TCKind.tk_value) {
            throw new org.omg.CORBA.TypeCodePackage.BadKind();
        }
        m_orb = orb;
        m_tc = tc;
    }

    public TypeCode getTypeCode() {
        return m_tc;
    }

    public OutputStream create_output_stream() {
        m_os = (ListOutputStream) m_orb.create_output_stream();
        return m_os;
    }

    public InputStream create_input_stream() {
        if (m_os == null) {
            return null;
        }
        return (InputStream) m_os.create_input_stream();
    }

    public void _read(org.omg.CORBA.portable.InputStream is) {
        m_os = (ListOutputStream) m_orb.create_output_stream();
        copyState(m_tc, (InputStream) is, m_os, getLogger());
    }

    public void _write(org.omg.CORBA.portable.OutputStream os) {
        copyState(m_tc, (InputStream) m_os.create_input_stream(), (OutputStream) os, getLogger());
    }

    public org.omg.CORBA.TypeCode _type() {
        return m_tc;
    }

    public String[] _truncatable_ids() {
        if (m_truncatable_ids == null) {
            try {
                int count = 1;
                TypeCode base = m_tc;
                while (base != null && base.type_modifier() == org.omg.CORBA.VM_TRUNCATABLE.value) {
                    count++;
                    base = base.concrete_base_type();
                }
                m_truncatable_ids = new String[count];
                count = 0;
                base = m_tc;
                do {
                    m_truncatable_ids[count++] = base.id();
                    base = base.concrete_base_type();
                } while (base != null && base.type_modifier() == org.omg.CORBA.VM_TRUNCATABLE.value);
            } catch (org.omg.CORBA.TypeCodePackage.BadKind ex) {
                if (getLogger().isErrorEnabled()) {
                    getLogger().error("BadKind exception should be impossible here.", ex);
                }
            }
        }
        return m_truncatable_ids;
    }

    private static void copyState(TypeCode tc, InputStream is, OutputStream os, Logger logger) {
        try {
            TypeCode base = tc.concrete_base_type();
            if (base != null) {
                copyState(base, is, os, logger);
            }
            for (int i = 0; i < tc.member_count(); ++i) {
                StreamHelper.copy_stream(tc.member_type(i), is, os);
            }
        } catch (org.omg.CORBA.TypeCodePackage.BadKind ex) {
            if (logger.isErrorEnabled()) {
                logger.error("BadKind exception should be impossible here.", ex);
            }
        } catch (org.omg.CORBA.TypeCodePackage.Bounds ex) {
            if (logger.isErrorEnabled()) {
                logger.error("BadKind exception should be impossible here.", ex);
            }
        }
    }

    /**
     * Return current Logger
     */
    private Logger getLogger() {
        if (null == m_logger) {
            m_logger = ((org.openorb.orb.core.ORBSingleton) m_orb).getLogger();
        }
        return m_logger;
    }
}
