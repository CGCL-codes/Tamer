package com.sisioh.erp.core.names;

import com.sisioh.erp.core.entity.useraccount.UserAccount;
import com.sisioh.erp.core.names.UserAccountConfigNames._UserAccountConfigNames;
import com.sisioh.erp.core.names.UserAccountNames._UserAccountNames;
import com.sisioh.erp.core.names.UserAccountProfileNames._UserAccountProfileNames;
import com.sisioh.erp.core.names.UserAccountRoleNames._UserAccountRoleNames;
import com.sisioh.erp.core.type.CreatePathType;
import java.util.Date;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link UserAccount}のプロパティ名の集合です。
 * 
 * @author S2JDBC-Gen
 * @suppresshack com.google.code.hack.ej2.ToStringRewriter
 */
public class UserAccountNames {

    /**
     * userAccountIdのプロパティ名を返します。
     * 
     * @return userAccountIdのプロパティ名
     */
    public static PropertyName<Long> userAccountId() {
        return new PropertyName<Long>("userAccountId");
    }

    /**
     * userAccountSystemCodeのプロパティ名を返します。
     * 
     * @return userAccountSystemCodeのプロパティ名
     */
    public static PropertyName<String> userAccountSystemCode() {
        return new PropertyName<String>("userAccountSystemCode");
    }

    /**
     * userAccountCodeのプロパティ名を返します。
     * 
     * @return userAccountCodeのプロパティ名
     */
    public static PropertyName<String> userAccountCode() {
        return new PropertyName<String>("userAccountCode");
    }

    /**
     * userAccountRoleIdのプロパティ名を返します。
     * 
     * @return userAccountRoleIdのプロパティ名
     */
    public static PropertyName<Long> userAccountRoleId() {
        return new PropertyName<Long>("userAccountRoleId");
    }

    /**
     * startDateのプロパティ名を返します。
     * 
     * @return startDateのプロパティ名
     */
    public static PropertyName<Date> startDate() {
        return new PropertyName<Date>("startDate");
    }

    /**
     * endDateのプロパティ名を返します。
     * 
     * @return endDateのプロパティ名
     */
    public static PropertyName<Date> endDate() {
        return new PropertyName<Date>("endDate");
    }

    /**
     * remarksのプロパティ名を返します。
     * 
     * @return remarksのプロパティ名
     */
    public static PropertyName<String> remarks() {
        return new PropertyName<String>("remarks");
    }

    /**
     * createPathTypeのプロパティ名を返します。
     * 
     * @return createPathTypeのプロパティ名
     */
    public static PropertyName<CreatePathType> createPathType() {
        return new PropertyName<CreatePathType>("createPathType");
    }

    /**
     * createUserAccountIdのプロパティ名を返します。
     * 
     * @return createUserAccountIdのプロパティ名
     */
    public static PropertyName<Long> createUserAccountId() {
        return new PropertyName<Long>("createUserAccountId");
    }

    /**
     * updateUserAccountIdのプロパティ名を返します。
     * 
     * @return updateUserAccountIdのプロパティ名
     */
    public static PropertyName<Long> updateUserAccountId() {
        return new PropertyName<Long>("updateUserAccountId");
    }

    /**
     * createDateのプロパティ名を返します。
     * 
     * @return createDateのプロパティ名
     */
    public static PropertyName<Date> createDate() {
        return new PropertyName<Date>("createDate");
    }

    /**
     * updateDateのプロパティ名を返します。
     * 
     * @return updateDateのプロパティ名
     */
    public static PropertyName<Date> updateDate() {
        return new PropertyName<Date>("updateDate");
    }

    /**
     * versionNoのプロパティ名を返します。
     * 
     * @return versionNoのプロパティ名
     */
    public static PropertyName<Long> versionNo() {
        return new PropertyName<Long>("versionNo");
    }

    /**
     * userAccountProfileのプロパティ名を返します。
     * 
     * @return userAccountProfileのプロパティ名
     */
    public static _UserAccountProfileNames userAccountProfile() {
        return new _UserAccountProfileNames("userAccountProfile");
    }

    /**
     * userAccountConfigのプロパティ名を返します。
     * 
     * @return userAccountConfigのプロパティ名
     */
    public static _UserAccountConfigNames userAccountConfig() {
        return new _UserAccountConfigNames("userAccountConfig");
    }

    /**
     * userAccountRoleのプロパティ名を返します。
     * 
     * @return userAccountRoleのプロパティ名
     */
    public static _UserAccountRoleNames userAccountRole() {
        return new _UserAccountRoleNames("userAccountRole");
    }

    /**
     * createUserAccountのプロパティ名を返します。
     * 
     * @return createUserAccountのプロパティ名
     */
    public static _UserAccountNames createUserAccount() {
        return new _UserAccountNames("createUserAccount");
    }

    /**
     * updateUserAccountのプロパティ名を返します。
     * 
     * @return updateUserAccountのプロパティ名
     */
    public static _UserAccountNames updateUserAccount() {
        return new _UserAccountNames("updateUserAccount");
    }

    /**
     * @author S2JDBC-Gen
     * @suppresshack com.google.code.hack.ej2.ToStringRewriter
     */
    public static class _UserAccountNames extends PropertyName<UserAccount> {

        /**
         * インスタンスを構築します。
         */
        public _UserAccountNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _UserAccountNames(final String name) {
            super(name);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param parent
         *            親
         * @param name
         *            名前
         */
        public _UserAccountNames(final PropertyName<?> parent, final String name) {
            super(parent, name);
        }

        /**
         * userAccountIdのプロパティ名を返します。
         *
         * @return userAccountIdのプロパティ名
         */
        public PropertyName<Long> userAccountId() {
            return new PropertyName<Long>(this, "userAccountId");
        }

        /**
         * userAccountSystemCodeのプロパティ名を返します。
         *
         * @return userAccountSystemCodeのプロパティ名
         */
        public PropertyName<String> userAccountSystemCode() {
            return new PropertyName<String>(this, "userAccountSystemCode");
        }

        /**
         * userAccountCodeのプロパティ名を返します。
         *
         * @return userAccountCodeのプロパティ名
         */
        public PropertyName<String> userAccountCode() {
            return new PropertyName<String>(this, "userAccountCode");
        }

        /**
         * userAccountRoleIdのプロパティ名を返します。
         *
         * @return userAccountRoleIdのプロパティ名
         */
        public PropertyName<Long> userAccountRoleId() {
            return new PropertyName<Long>(this, "userAccountRoleId");
        }

        /**
         * startDateのプロパティ名を返します。
         *
         * @return startDateのプロパティ名
         */
        public PropertyName<Date> startDate() {
            return new PropertyName<Date>(this, "startDate");
        }

        /**
         * endDateのプロパティ名を返します。
         *
         * @return endDateのプロパティ名
         */
        public PropertyName<Date> endDate() {
            return new PropertyName<Date>(this, "endDate");
        }

        /**
         * remarksのプロパティ名を返します。
         *
         * @return remarksのプロパティ名
         */
        public PropertyName<String> remarks() {
            return new PropertyName<String>(this, "remarks");
        }

        /**
         * createPathTypeのプロパティ名を返します。
         *
         * @return createPathTypeのプロパティ名
         */
        public PropertyName<CreatePathType> createPathType() {
            return new PropertyName<CreatePathType>(this, "createPathType");
        }

        /**
         * createUserAccountIdのプロパティ名を返します。
         *
         * @return createUserAccountIdのプロパティ名
         */
        public PropertyName<Long> createUserAccountId() {
            return new PropertyName<Long>(this, "createUserAccountId");
        }

        /**
         * updateUserAccountIdのプロパティ名を返します。
         *
         * @return updateUserAccountIdのプロパティ名
         */
        public PropertyName<Long> updateUserAccountId() {
            return new PropertyName<Long>(this, "updateUserAccountId");
        }

        /**
         * createDateのプロパティ名を返します。
         *
         * @return createDateのプロパティ名
         */
        public PropertyName<Date> createDate() {
            return new PropertyName<Date>(this, "createDate");
        }

        /**
         * updateDateのプロパティ名を返します。
         *
         * @return updateDateのプロパティ名
         */
        public PropertyName<Date> updateDate() {
            return new PropertyName<Date>(this, "updateDate");
        }

        /**
         * versionNoのプロパティ名を返します。
         *
         * @return versionNoのプロパティ名
         */
        public PropertyName<Long> versionNo() {
            return new PropertyName<Long>(this, "versionNo");
        }

        /**
         * userAccountProfileのプロパティ名を返します。
         * 
         * @return userAccountProfileのプロパティ名
         */
        public _UserAccountProfileNames userAccountProfile() {
            return new _UserAccountProfileNames(this, "userAccountProfile");
        }

        /**
         * userAccountConfigのプロパティ名を返します。
         * 
         * @return userAccountConfigのプロパティ名
         */
        public _UserAccountConfigNames userAccountConfig() {
            return new _UserAccountConfigNames(this, "userAccountConfig");
        }

        /**
         * userAccountRoleのプロパティ名を返します。
         * 
         * @return userAccountRoleのプロパティ名
         */
        public _UserAccountRoleNames userAccountRole() {
            return new _UserAccountRoleNames(this, "userAccountRole");
        }

        /**
         * createUserAccountのプロパティ名を返します。
         * 
         * @return createUserAccountのプロパティ名
         */
        public _UserAccountNames createUserAccount() {
            return new _UserAccountNames(this, "createUserAccount");
        }

        /**
         * updateUserAccountのプロパティ名を返します。
         * 
         * @return updateUserAccountのプロパティ名
         */
        public _UserAccountNames updateUserAccount() {
            return new _UserAccountNames(this, "updateUserAccount");
        }
    }
}
