package com.esa.dbi;

import java.util.List;
import com.esa.javabean.Account;

public interface AccountDBI {

    public boolean addAccount(Account accnt);

    /**
	 * ����˻���ɾ��һ���˻���Ϣ
*/
    public boolean deleteAccount(String acname);

    public boolean updateAccount(Account accnt);

    /**
	 * ����˻���getһ���˻���Ϣ
*/
    public Account getAccount(String acname);

    /**
	 * get�����˻���Ϣ
*/
    public List<Account> getAllAcounts();

    /**
	 * ��֤�˻���Ϣ
*/
    public boolean validate(Account accnt);
}
