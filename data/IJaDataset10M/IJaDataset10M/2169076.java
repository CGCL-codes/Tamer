package ces.platform.system.ldap;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPModification;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchResults;

/**
 * @author  liusheng
 * @version 1.0.0
 * ��˾��  �Ϻ�����
 * 2005-4-1
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class OperationBase extends AccessBase {

    /**
     * �޸Ķ�ID
     */
    private int modifyUserId;

    /**
     * �����
     */
    private int order;

    /**
     * �޸�
     */
    private String modifyDate;

    /**
     * �޸�ʱ��
     */
    private String modifyTime;

    /**
     * ״̬
     */
    private int status;

    /**
     * �Լ�DN
     */
    protected String selfDn;

    /**
     * ��һ��DN
     */
    protected String parentDn;

    /**
     * @return Returns the modifyUserId.
     */
    public int getModifyUserId() {
        return modifyUserId;
    }

    /**
     * @param modifyUserId The modifyUserId to set.
     */
    public void setModifyUserId(int modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    /**
     * @return Returns the order.
     */
    public int getOrder() {
        return order;
    }

    /**
     * @param order The order to set.
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * @return Returns the parentDn.
     */
    public String getParentDn() {
        return parentDn;
    }

    /**
     * @param parentDn The parentDn to set.
     */
    public void setParentDn(String parentDn) {
        this.parentDn = parentDn;
    }

    /**
     * @return Returns the selfDn.
     */
    public String getSelfDn() {
        return selfDn;
    }

    /**
     * @param selfDn The selfDn to set.
     */
    public void setSelfDn(String selfDn) {
        this.selfDn = selfDn;
    }

    /**
     * @return Returns the status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return Returns the modifyDate.
     */
    public String getModifyDate() {
        return modifyDate;
    }

    /**
     * @param modifyDate The modifyDate to set.
     */
    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * @return Returns the modifyTime.
     */
    public String getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime The modifyTime to set.
     */
    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * �õ���������
     * @return
     */
    protected abstract void getPrivateAttribute(LDAPAttributeSet attributeSet);

    /**
     * ����Լ���DN�Ƿ���ȷ
     * @param strDn  �Լ�DN
     * @return  true:��ȷ flase:����
     */
    public abstract boolean verifySelfDn(String strDn);

    /**
     * �����һ����DN�Ƿ���ȷ
     * @param strDn ��һ��DN
     * @return true:��ȷ flase:����
     */
    public abstract boolean verifyParentDn(String strDn);

    /**
     * �������е�����ֵ
     * @return true:��ȷ flase:����
     */
    public abstract boolean verifyDate();

    /**
     * ����
     */
    public void doNew(LDAPConnection conn) throws Exception {
        this.generateSelfDn();
        if (!this.verifySelfDn(this.getSelfDn())) {
            throw new Exception("��Ч��DN");
        }
        if (!this.verifyParentDn(this.getParentDn())) {
            throw new Exception("��Ч����һ��DN");
        }
        if (!verifyDate()) {
            throw new Exception("��Ϣ��д��ȫ");
        }
        try {
            LDAPAttributeSet attributeSet = new LDAPAttributeSet();
            this.getPrivateAttribute(attributeSet);
            LDAPEntry newEntry = new LDAPEntry(this.getSelfDn(), attributeSet);
            conn.add(newEntry);
        } catch (LDAPException e) {
            throw new Exception("���ʧ�ܣ�" + e.getMessage() + "\n DN: " + this.getSelfDn() + "\n" + e.getLDAPErrorMessage() + "\n" + e.getLocalizedMessage());
        }
    }

    /**
     * ɾ��û�еݹ飩
     */
    public void doDelete(LDAPConnection conn) throws Exception {
        String strDn = this.getSelfDn();
        if (strDn != null && strDn.length() > 0) {
            conn.delete(strDn);
        }
    }

    /**
     * �ݹ�ɾ��
     * @param strDn
     * @throws Exception
     */
    public void doDelete(String strDn) throws Exception, LDAPException {
        Pool pool = Pool.getInstance();
        LDAPConnection conn = pool.getConn();
        LDAPSearchConstraints cons = new LDAPSearchConstraints();
        cons.setTimeLimit(Constant.OVER_TIME);
        cons.setMaxResults(Constant.MAX_RESULT);
        LDAPSearchResults searchResults = conn.search(strDn, LDAPConnection.SCOPE_ONE, "", null, false, cons);
        while (searchResults.hasMore()) {
            try {
                LDAPEntry nextEntry = searchResults.next();
                String dn = nextEntry.getDN();
                if (dn != null && dn.length() > 0) {
                    this.doDelete(dn);
                    conn.delete(dn);
                }
            } catch (Exception e) {
                continue;
            }
        }
        conn.delete(strDn);
        pool.release(conn);
    }

    /**
     * �޸�
     */
    public void doUpdate(LDAPConnection conn) throws Exception {
        ArrayList modList = new ArrayList();
        LDAPAttributeSet attribSet = new LDAPAttributeSet();
        this.getPrivateAttribute(attribSet);
        Object[] attribs = attribSet.toArray();
        int intAttrbsLen = attribs.length;
        for (int i = 0; i < intAttrbsLen; i++) {
            LDAPAttribute attrib = (LDAPAttribute) attribs[i];
            modList.add(new LDAPModification(LDAPModification.REPLACE, attrib));
        }
        LDAPModification[] mods = new LDAPModification[modList.size()];
        mods = (LDAPModification[]) modList.toArray(mods);
        try {
            conn.modify(this.getSelfDn(), mods);
        } catch (LDAPException e1) {
            if (e1.getResultCode() != LDAPException.ATTRIBUTE_OR_VALUE_EXISTS) {
                throw new Exception("���ܹ������������:" + e1.toString());
            } else {
                throw new Exception("�޸�ʧ��:" + e1.toString());
            }
        }
    }

    /**
     * ��ldap�в鵽�����
     * @throws Exception
     */
    public void load(LDAPConnection conn) throws Exception {
        if (!verifySelfDn(this.getSelfDn())) {
            throw new Exception("����DN");
        }
        LDAPSearchResults searchResults = conn.search(this.getSelfDn(), LDAPConnection.SCOPE_BASE, "", null, false);
        this.setAllAttribute(searchResults);
        Pool.getInstance().release(conn);
        if (this.getSelfDn() == null || this.getSelfDn().length() == 0) {
            throw new Exception("û�в�ѯ�����");
        }
    }

    /**
	 * ��ldap�в鵽�����
	 * @throws Exception
	 */
    public void loadData(LDAPConnection conn) throws Exception {
        LDAPSearchResults searchResults = conn.search(this.getSelfDn(), LDAPConnection.SCOPE_SUB, "", null, false);
        this.setAllAttribute(searchResults);
        Pool.getInstance().release(conn);
        if (this.getSelfDn() == null || this.getSelfDn().length() == 0) {
            throw new Exception("û�в�ѯ�����");
        }
    }

    /**
     * ������������
     * @param searchResults
     * @throws Exception
     */
    public void setAllAttribute(LDAPSearchResults searchResults) throws Exception {
        while (searchResults.hasMore()) {
            LDAPEntry nextEntry = searchResults.next();
            String strTempDn = nextEntry.getDN();
            this.setSelfDn(strTempDn);
            int index = strTempDn.indexOf(",");
            if (index >= 0) {
                this.setParentDn(strTempDn.substring(index + 1));
            }
            this.setPrivateAttribute(nextEntry);
        }
    }

    public void setAllAttribute(LDAPEntry nextEntry) throws Exception {
        String strTempDn = nextEntry.getDN();
        this.setSelfDn(strTempDn);
        int index = strTempDn.indexOf(",");
        if (index >= 0) {
            this.setParentDn(strTempDn.substring(index + 1));
        }
        this.setPrivateAttribute(nextEntry);
    }

    /**
     * ���ò�ͬ�����е�����
     * @param nextEntry
     * @throws Exception
     */
    public abstract void setPrivateAttribute(LDAPEntry nextEntry) throws Exception;

    /**
     * �����һ��DN���Լ���ID�������Լ���DN
     *
     */
    public abstract void generateSelfDn();

    /**
     * �õ�����ֵ
     * @param allValues
     * @return
     */
    protected Object setValue(Enumeration allValues) {
        Vector v = new Vector();
        if (allValues != null) {
            while (allValues.hasMoreElements()) {
                String Value = (String) allValues.nextElement();
                v.add(Value);
            }
        }
        int intLen = v.size();
        if (intLen == 0) {
            return null;
        }
        if (intLen == 1) {
            return v.elementAt(0);
        } else {
            String temp[] = new String[intLen];
            for (int i = 0; i < intLen; i++) {
                temp[i] = (String) v.elementAt(i);
            }
            return temp;
        }
    }
}
