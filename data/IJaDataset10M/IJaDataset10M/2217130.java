package org.jalcedo.server.dao.gen.impl;

import org.eclipse.jdt.core.IType;
import org.jalcedo.server.dao.gen.DaoName;
import org.jalcedo.server.dao.gen.DaoNamingStrategy;

/**
 * 指定したEntityタイプに対して以下の命名戦略に従ってDaoクラスの名前を生成する.
 * <dl>
 * 
 * <dt>Daoクラス名</dt>
 * <dd>Entityのクラス名+Dao</dd>
 * 
 * <dt>Daoパッケージ名</dt>
 * <dd>Entityクラスのパッケージ名+.dao</dd>
 * 
 * </dl>
 * 
 * 例えばEntityクラスが「com.necsoft.rcbbs.Comment」であった場合、
 * クラス名は「CommentDao」、パッケージ名は「com.necsoft.rcbbs.dao」となる。
 * 
 * @author yuanying
 *
 */
public class SimpleDaoNamingStrategy implements DaoNamingStrategy {

    public DaoName createDaoName(final IType type) {
        return new SimpleDaoName(type);
    }

    public class SimpleDaoName implements DaoName {

        private final IType type;

        public SimpleDaoName(IType type) {
            this.type = type;
        }

        public String getClassName() {
            return this.getEntityType().getElementName() + "Dao";
        }

        public IType getEntityType() {
            return this.type;
        }

        public String getPackageName() {
            return this.getEntityType().getPackageFragment().getElementName() + ".dao";
        }
    }
}
