package com.sisioh.erp.core.names;

import com.sisioh.erp.core.entity.shipping.StockShelf;
import com.sisioh.erp.core.names.ProductNames._ProductNames;
import com.sisioh.erp.core.names.StorageNames._StorageNames;
import com.sisioh.erp.core.names.UserAccountNames._UserAccountNames;
import com.sisioh.erp.core.type.CreatePathType;
import java.util.Date;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * {@link StockShelf}のプロパティ名の集合です。
 * 
 * @author S2JDBC-Gen
 * @suppresshack com.google.code.hack.ej2.ToStringRewriter
 */
public class StockShelfNames {

    /**
     * stockShelfIdのプロパティ名を返します。
     * 
     * @return stockShelfIdのプロパティ名
     */
    public static PropertyName<Long> stockShelfId() {
        return new PropertyName<Long>("stockShelfId");
    }

    /**
     * stockShelfSystemCodeのプロパティ名を返します。
     * 
     * @return stockShelfSystemCodeのプロパティ名
     */
    public static PropertyName<String> stockShelfSystemCode() {
        return new PropertyName<String>("stockShelfSystemCode");
    }

    /**
     * stockShelfCodeのプロパティ名を返します。
     * 
     * @return stockShelfCodeのプロパティ名
     */
    public static PropertyName<String> stockShelfCode() {
        return new PropertyName<String>("stockShelfCode");
    }

    /**
     * deleteTypeのプロパティ名を返します。
     * 
     * @return deleteTypeのプロパティ名
     */
    public static PropertyName<Boolean> deleteType() {
        return new PropertyName<Boolean>("deleteType");
    }

    /**
     * disableTypeのプロパティ名を返します。
     * 
     * @return disableTypeのプロパティ名
     */
    public static PropertyName<Boolean> disableType() {
        return new PropertyName<Boolean>("disableType");
    }

    /**
     * storageIdのプロパティ名を返します。
     * 
     * @return storageIdのプロパティ名
     */
    public static PropertyName<Long> storageId() {
        return new PropertyName<Long>("storageId");
    }

    /**
     * productIdのプロパティ名を返します。
     * 
     * @return productIdのプロパティ名
     */
    public static PropertyName<Long> productId() {
        return new PropertyName<Long>("productId");
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
     * productのプロパティ名を返します。
     * 
     * @return productのプロパティ名
     */
    public static _ProductNames product() {
        return new _ProductNames("product");
    }

    /**
     * storageのプロパティ名を返します。
     * 
     * @return storageのプロパティ名
     */
    public static _StorageNames storage() {
        return new _StorageNames("storage");
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
    public static class _StockShelfNames extends PropertyName<StockShelf> {

        /**
         * インスタンスを構築します。
         */
        public _StockShelfNames() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param name
         *            名前
         */
        public _StockShelfNames(final String name) {
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
        public _StockShelfNames(final PropertyName<?> parent, final String name) {
            super(parent, name);
        }

        /**
         * stockShelfIdのプロパティ名を返します。
         *
         * @return stockShelfIdのプロパティ名
         */
        public PropertyName<Long> stockShelfId() {
            return new PropertyName<Long>(this, "stockShelfId");
        }

        /**
         * stockShelfSystemCodeのプロパティ名を返します。
         *
         * @return stockShelfSystemCodeのプロパティ名
         */
        public PropertyName<String> stockShelfSystemCode() {
            return new PropertyName<String>(this, "stockShelfSystemCode");
        }

        /**
         * stockShelfCodeのプロパティ名を返します。
         *
         * @return stockShelfCodeのプロパティ名
         */
        public PropertyName<String> stockShelfCode() {
            return new PropertyName<String>(this, "stockShelfCode");
        }

        /**
         * deleteTypeのプロパティ名を返します。
         *
         * @return deleteTypeのプロパティ名
         */
        public PropertyName<Boolean> deleteType() {
            return new PropertyName<Boolean>(this, "deleteType");
        }

        /**
         * disableTypeのプロパティ名を返します。
         *
         * @return disableTypeのプロパティ名
         */
        public PropertyName<Boolean> disableType() {
            return new PropertyName<Boolean>(this, "disableType");
        }

        /**
         * storageIdのプロパティ名を返します。
         *
         * @return storageIdのプロパティ名
         */
        public PropertyName<Long> storageId() {
            return new PropertyName<Long>(this, "storageId");
        }

        /**
         * productIdのプロパティ名を返します。
         *
         * @return productIdのプロパティ名
         */
        public PropertyName<Long> productId() {
            return new PropertyName<Long>(this, "productId");
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
         * productのプロパティ名を返します。
         * 
         * @return productのプロパティ名
         */
        public _ProductNames product() {
            return new _ProductNames(this, "product");
        }

        /**
         * storageのプロパティ名を返します。
         * 
         * @return storageのプロパティ名
         */
        public _StorageNames storage() {
            return new _StorageNames(this, "storage");
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
