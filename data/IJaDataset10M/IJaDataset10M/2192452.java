package com.sisioh.erp.core.names;

import com.sisioh.erp.core.entity.member.Member;
import com.sisioh.erp.core.names.AccountCodeCompositionNames._AccountCodeCompositionNames;
import com.sisioh.erp.core.names.AccountCodeNames._AccountCodeNames;
import com.sisioh.erp.core.names.CustomerHistoryDefineNames._CustomerHistoryDefineNames;
import com.sisioh.erp.core.names.CustomerHistoryNames._CustomerHistoryNames;
import com.sisioh.erp.core.names.CustomerInChargeNames._CustomerInChargeNames;
import com.sisioh.erp.core.names.CustomerNames._CustomerNames;
import com.sisioh.erp.core.names.DepartmentNames._DepartmentNames;
import com.sisioh.erp.core.names.EmployeeJobTypeNames._EmployeeJobTypeNames;
import com.sisioh.erp.core.names.EmployeeNames._EmployeeNames;
import com.sisioh.erp.core.names.EstimationNames._EstimationNames;
import com.sisioh.erp.core.names.JournalDebitCreditDetailNames._JournalDebitCreditDetailNames;
import com.sisioh.erp.core.names.JournalNames._JournalNames;
import com.sisioh.erp.core.names.MemberConfigNames._MemberConfigNames;
import com.sisioh.erp.core.names.MemberProfileNames._MemberProfileNames;
import com.sisioh.erp.core.names.OrderReceivedNames._OrderReceivedNames;
import com.sisioh.erp.core.names.PartnerNames._PartnerNames;
import com.sisioh.erp.core.names.PayNames._PayNames;
import com.sisioh.erp.core.names.ProductCategoryNames._ProductCategoryNames;
import com.sisioh.erp.core.names.ProductNames._ProductNames;
import com.sisioh.erp.core.names.PurchaseNames._PurchaseNames;
import com.sisioh.erp.core.names.PurchaseOrderNames._PurchaseOrderNames;
import com.sisioh.erp.core.names.ReceivedMoneyAccountNames._ReceivedMoneyAccountNames;
import com.sisioh.erp.core.names.ReceivedMoneyNames._ReceivedMoneyNames;
import com.sisioh.erp.core.names.SalesNames._SalesNames;
import com.sisioh.erp.core.names.ShippingTargetNames._ShippingTargetNames;
import com.sisioh.erp.core.names.StorageDepartmentNames._StorageDepartmentNames;
import com.sisioh.erp.core.names.StorageNames._StorageNames;
import com.sisioh.erp.core.names.SupplierNames._SupplierNames;
import com.sisioh.erp.core.names.TaxTradeNames._TaxTradeNames;
import com.sisioh.erp.core.names.UserAccountNames._UserAccountNames;
import com.sisioh.erp.core.type.CreatePathType;
import java.util.Date;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link Member}のプロパティ名の集合です。
 * 
 * @author S2JDBC-Gen
 * @suppresshack com.google.code.hack.ej2.ToStringRewriter
 */
public class MemberNames {

    /**
     * memberIdのプロパティ名を返します。
     * 
     * @return memberIdのプロパティ名
     */
    public static PropertyName<Long> memberId() {
        return new PropertyName<Long>("memberId");
    }

    /**
     * memberSystemCodeのプロパティ名を返します。
     * 
     * @return memberSystemCodeのプロパティ名
     */
    public static PropertyName<String> memberSystemCode() {
        return new PropertyName<String>("memberSystemCode");
    }

    /**
     * memberCodeのプロパティ名を返します。
     * 
     * @return memberCodeのプロパティ名
     */
    public static PropertyName<String> memberCode() {
        return new PropertyName<String>("memberCode");
    }

    /**
     * memberSubCodeのプロパティ名を返します。
     * 
     * @return memberSubCodeのプロパティ名
     */
    public static PropertyName<Integer> memberSubCode() {
        return new PropertyName<Integer>("memberSubCode");
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
     * accountCodeListのプロパティ名を返します。
     * 
     * @return accountCodeListのプロパティ名
     */
    public static _AccountCodeNames accountCodeList() {
        return new _AccountCodeNames("accountCodeList");
    }

    /**
     * accountCodeCompositionListのプロパティ名を返します。
     * 
     * @return accountCodeCompositionListのプロパティ名
     */
    public static _AccountCodeCompositionNames accountCodeCompositionList() {
        return new _AccountCodeCompositionNames("accountCodeCompositionList");
    }

    /**
     * customerListのプロパティ名を返します。
     * 
     * @return customerListのプロパティ名
     */
    public static _CustomerNames customerList() {
        return new _CustomerNames("customerList");
    }

    /**
     * customerHistoryListのプロパティ名を返します。
     * 
     * @return customerHistoryListのプロパティ名
     */
    public static _CustomerHistoryNames customerHistoryList() {
        return new _CustomerHistoryNames("customerHistoryList");
    }

    /**
     * customerHistoryDefineListのプロパティ名を返します。
     * 
     * @return customerHistoryDefineListのプロパティ名
     */
    public static _CustomerHistoryDefineNames customerHistoryDefineList() {
        return new _CustomerHistoryDefineNames("customerHistoryDefineList");
    }

    /**
     * customerInChargeListのプロパティ名を返します。
     * 
     * @return customerInChargeListのプロパティ名
     */
    public static _CustomerInChargeNames customerInChargeList() {
        return new _CustomerInChargeNames("customerInChargeList");
    }

    /**
     * departmentListのプロパティ名を返します。
     * 
     * @return departmentListのプロパティ名
     */
    public static _DepartmentNames departmentList() {
        return new _DepartmentNames("departmentList");
    }

    /**
     * employeeListのプロパティ名を返します。
     * 
     * @return employeeListのプロパティ名
     */
    public static _EmployeeNames employeeList() {
        return new _EmployeeNames("employeeList");
    }

    /**
     * employeeJobTypeListのプロパティ名を返します。
     * 
     * @return employeeJobTypeListのプロパティ名
     */
    public static _EmployeeJobTypeNames employeeJobTypeList() {
        return new _EmployeeJobTypeNames("employeeJobTypeList");
    }

    /**
     * estimationListのプロパティ名を返します。
     * 
     * @return estimationListのプロパティ名
     */
    public static _EstimationNames estimationList() {
        return new _EstimationNames("estimationList");
    }

    /**
     * journalListのプロパティ名を返します。
     * 
     * @return journalListのプロパティ名
     */
    public static _JournalNames journalList() {
        return new _JournalNames("journalList");
    }

    /**
     * journalDebitCreditDetailListのプロパティ名を返します。
     * 
     * @return journalDebitCreditDetailListのプロパティ名
     */
    public static _JournalDebitCreditDetailNames journalDebitCreditDetailList() {
        return new _JournalDebitCreditDetailNames("journalDebitCreditDetailList");
    }

    /**
     * memberConfigのプロパティ名を返します。
     * 
     * @return memberConfigのプロパティ名
     */
    public static _MemberConfigNames memberConfig() {
        return new _MemberConfigNames("memberConfig");
    }

    /**
     * memberProfileのプロパティ名を返します。
     * 
     * @return memberProfileのプロパティ名
     */
    public static _MemberProfileNames memberProfile() {
        return new _MemberProfileNames("memberProfile");
    }

    /**
     * orderReceivedListのプロパティ名を返します。
     * 
     * @return orderReceivedListのプロパティ名
     */
    public static _OrderReceivedNames orderReceivedList() {
        return new _OrderReceivedNames("orderReceivedList");
    }

    /**
     * partenerListのプロパティ名を返します。
     * 
     * @return partenerListのプロパティ名
     */
    public static _PartnerNames partenerList() {
        return new _PartnerNames("partenerList");
    }

    /**
     * payListのプロパティ名を返します。
     * 
     * @return payListのプロパティ名
     */
    public static _PayNames payList() {
        return new _PayNames("payList");
    }

    /**
     * productListのプロパティ名を返します。
     * 
     * @return productListのプロパティ名
     */
    public static _ProductNames productList() {
        return new _ProductNames("productList");
    }

    /**
     * productCategoryListのプロパティ名を返します。
     * 
     * @return productCategoryListのプロパティ名
     */
    public static _ProductCategoryNames productCategoryList() {
        return new _ProductCategoryNames("productCategoryList");
    }

    /**
     * purchaseListのプロパティ名を返します。
     * 
     * @return purchaseListのプロパティ名
     */
    public static _PurchaseNames purchaseList() {
        return new _PurchaseNames("purchaseList");
    }

    /**
     * purchaseOrderListのプロパティ名を返します。
     * 
     * @return purchaseOrderListのプロパティ名
     */
    public static _PurchaseOrderNames purchaseOrderList() {
        return new _PurchaseOrderNames("purchaseOrderList");
    }

    /**
     * receivedMoneyListのプロパティ名を返します。
     * 
     * @return receivedMoneyListのプロパティ名
     */
    public static _ReceivedMoneyNames receivedMoneyList() {
        return new _ReceivedMoneyNames("receivedMoneyList");
    }

    /**
     * receivedMoneyAccountListのプロパティ名を返します。
     * 
     * @return receivedMoneyAccountListのプロパティ名
     */
    public static _ReceivedMoneyAccountNames receivedMoneyAccountList() {
        return new _ReceivedMoneyAccountNames("receivedMoneyAccountList");
    }

    /**
     * salesListのプロパティ名を返します。
     * 
     * @return salesListのプロパティ名
     */
    public static _SalesNames salesList() {
        return new _SalesNames("salesList");
    }

    /**
     * shippingTargetListのプロパティ名を返します。
     * 
     * @return shippingTargetListのプロパティ名
     */
    public static _ShippingTargetNames shippingTargetList() {
        return new _ShippingTargetNames("shippingTargetList");
    }

    /**
     * storageListのプロパティ名を返します。
     * 
     * @return storageListのプロパティ名
     */
    public static _StorageNames storageList() {
        return new _StorageNames("storageList");
    }

    /**
     * storageDepartmentListのプロパティ名を返します。
     * 
     * @return storageDepartmentListのプロパティ名
     */
    public static _StorageDepartmentNames storageDepartmentList() {
        return new _StorageDepartmentNames("storageDepartmentList");
    }

    /**
     * supplierListのプロパティ名を返します。
     * 
     * @return supplierListのプロパティ名
     */
    public static _SupplierNames supplierList() {
        return new _SupplierNames("supplierList");
    }

    /**
     * taxTradeListのプロパティ名を返します。
     * 
     * @return taxTradeListのプロパティ名
     */
    public static _TaxTradeNames taxTradeList() {
        return new _TaxTradeNames("taxTradeList");
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
    public static class _MemberNames extends PropertyName<Member> {

        /**
         * インスタンスを構築します。
         */
        public _MemberNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _MemberNames(final String name) {
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
        public _MemberNames(final PropertyName<?> parent, final String name) {
            super(parent, name);
        }

        /**
         * memberIdのプロパティ名を返します。
         *
         * @return memberIdのプロパティ名
         */
        public PropertyName<Long> memberId() {
            return new PropertyName<Long>(this, "memberId");
        }

        /**
         * memberSystemCodeのプロパティ名を返します。
         *
         * @return memberSystemCodeのプロパティ名
         */
        public PropertyName<String> memberSystemCode() {
            return new PropertyName<String>(this, "memberSystemCode");
        }

        /**
         * memberCodeのプロパティ名を返します。
         *
         * @return memberCodeのプロパティ名
         */
        public PropertyName<String> memberCode() {
            return new PropertyName<String>(this, "memberCode");
        }

        /**
         * memberSubCodeのプロパティ名を返します。
         *
         * @return memberSubCodeのプロパティ名
         */
        public PropertyName<Integer> memberSubCode() {
            return new PropertyName<Integer>(this, "memberSubCode");
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
         * accountCodeListのプロパティ名を返します。
         * 
         * @return accountCodeListのプロパティ名
         */
        public _AccountCodeNames accountCodeList() {
            return new _AccountCodeNames(this, "accountCodeList");
        }

        /**
         * accountCodeCompositionListのプロパティ名を返します。
         * 
         * @return accountCodeCompositionListのプロパティ名
         */
        public _AccountCodeCompositionNames accountCodeCompositionList() {
            return new _AccountCodeCompositionNames(this, "accountCodeCompositionList");
        }

        /**
         * customerListのプロパティ名を返します。
         * 
         * @return customerListのプロパティ名
         */
        public _CustomerNames customerList() {
            return new _CustomerNames(this, "customerList");
        }

        /**
         * customerHistoryListのプロパティ名を返します。
         * 
         * @return customerHistoryListのプロパティ名
         */
        public _CustomerHistoryNames customerHistoryList() {
            return new _CustomerHistoryNames(this, "customerHistoryList");
        }

        /**
         * customerHistoryDefineListのプロパティ名を返します。
         * 
         * @return customerHistoryDefineListのプロパティ名
         */
        public _CustomerHistoryDefineNames customerHistoryDefineList() {
            return new _CustomerHistoryDefineNames(this, "customerHistoryDefineList");
        }

        /**
         * customerInChargeListのプロパティ名を返します。
         * 
         * @return customerInChargeListのプロパティ名
         */
        public _CustomerInChargeNames customerInChargeList() {
            return new _CustomerInChargeNames(this, "customerInChargeList");
        }

        /**
         * departmentListのプロパティ名を返します。
         * 
         * @return departmentListのプロパティ名
         */
        public _DepartmentNames departmentList() {
            return new _DepartmentNames(this, "departmentList");
        }

        /**
         * employeeListのプロパティ名を返します。
         * 
         * @return employeeListのプロパティ名
         */
        public _EmployeeNames employeeList() {
            return new _EmployeeNames(this, "employeeList");
        }

        /**
         * employeeJobTypeListのプロパティ名を返します。
         * 
         * @return employeeJobTypeListのプロパティ名
         */
        public _EmployeeJobTypeNames employeeJobTypeList() {
            return new _EmployeeJobTypeNames(this, "employeeJobTypeList");
        }

        /**
         * estimationListのプロパティ名を返します。
         * 
         * @return estimationListのプロパティ名
         */
        public _EstimationNames estimationList() {
            return new _EstimationNames(this, "estimationList");
        }

        /**
         * journalListのプロパティ名を返します。
         * 
         * @return journalListのプロパティ名
         */
        public _JournalNames journalList() {
            return new _JournalNames(this, "journalList");
        }

        /**
         * journalDebitCreditDetailListのプロパティ名を返します。
         * 
         * @return journalDebitCreditDetailListのプロパティ名
         */
        public _JournalDebitCreditDetailNames journalDebitCreditDetailList() {
            return new _JournalDebitCreditDetailNames(this, "journalDebitCreditDetailList");
        }

        /**
         * memberConfigのプロパティ名を返します。
         * 
         * @return memberConfigのプロパティ名
         */
        public _MemberConfigNames memberConfig() {
            return new _MemberConfigNames(this, "memberConfig");
        }

        /**
         * memberProfileのプロパティ名を返します。
         * 
         * @return memberProfileのプロパティ名
         */
        public _MemberProfileNames memberProfile() {
            return new _MemberProfileNames(this, "memberProfile");
        }

        /**
         * orderReceivedListのプロパティ名を返します。
         * 
         * @return orderReceivedListのプロパティ名
         */
        public _OrderReceivedNames orderReceivedList() {
            return new _OrderReceivedNames(this, "orderReceivedList");
        }

        /**
         * partenerListのプロパティ名を返します。
         * 
         * @return partenerListのプロパティ名
         */
        public _PartnerNames partenerList() {
            return new _PartnerNames(this, "partenerList");
        }

        /**
         * payListのプロパティ名を返します。
         * 
         * @return payListのプロパティ名
         */
        public _PayNames payList() {
            return new _PayNames(this, "payList");
        }

        /**
         * productListのプロパティ名を返します。
         * 
         * @return productListのプロパティ名
         */
        public _ProductNames productList() {
            return new _ProductNames(this, "productList");
        }

        /**
         * productCategoryListのプロパティ名を返します。
         * 
         * @return productCategoryListのプロパティ名
         */
        public _ProductCategoryNames productCategoryList() {
            return new _ProductCategoryNames(this, "productCategoryList");
        }

        /**
         * purchaseListのプロパティ名を返します。
         * 
         * @return purchaseListのプロパティ名
         */
        public _PurchaseNames purchaseList() {
            return new _PurchaseNames(this, "purchaseList");
        }

        /**
         * purchaseOrderListのプロパティ名を返します。
         * 
         * @return purchaseOrderListのプロパティ名
         */
        public _PurchaseOrderNames purchaseOrderList() {
            return new _PurchaseOrderNames(this, "purchaseOrderList");
        }

        /**
         * receivedMoneyListのプロパティ名を返します。
         * 
         * @return receivedMoneyListのプロパティ名
         */
        public _ReceivedMoneyNames receivedMoneyList() {
            return new _ReceivedMoneyNames(this, "receivedMoneyList");
        }

        /**
         * receivedMoneyAccountListのプロパティ名を返します。
         * 
         * @return receivedMoneyAccountListのプロパティ名
         */
        public _ReceivedMoneyAccountNames receivedMoneyAccountList() {
            return new _ReceivedMoneyAccountNames(this, "receivedMoneyAccountList");
        }

        /**
         * salesListのプロパティ名を返します。
         * 
         * @return salesListのプロパティ名
         */
        public _SalesNames salesList() {
            return new _SalesNames(this, "salesList");
        }

        /**
         * shippingTargetListのプロパティ名を返します。
         * 
         * @return shippingTargetListのプロパティ名
         */
        public _ShippingTargetNames shippingTargetList() {
            return new _ShippingTargetNames(this, "shippingTargetList");
        }

        /**
         * storageListのプロパティ名を返します。
         * 
         * @return storageListのプロパティ名
         */
        public _StorageNames storageList() {
            return new _StorageNames(this, "storageList");
        }

        /**
         * storageDepartmentListのプロパティ名を返します。
         * 
         * @return storageDepartmentListのプロパティ名
         */
        public _StorageDepartmentNames storageDepartmentList() {
            return new _StorageDepartmentNames(this, "storageDepartmentList");
        }

        /**
         * supplierListのプロパティ名を返します。
         * 
         * @return supplierListのプロパティ名
         */
        public _SupplierNames supplierList() {
            return new _SupplierNames(this, "supplierList");
        }

        /**
         * taxTradeListのプロパティ名を返します。
         * 
         * @return taxTradeListのプロパティ名
         */
        public _TaxTradeNames taxTradeList() {
            return new _TaxTradeNames(this, "taxTradeList");
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
