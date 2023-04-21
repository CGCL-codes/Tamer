package whf.framework.tools.generator;

import whf.framework.dao.DAO;
import whf.framework.exception.CreateException;
import whf.framework.exception.DAOException;
import whf.framework.exception.NotFoundException;
import whf.framework.exception.RemoveException;
import whf.framework.exception.UpdateException;
import whf.framework.util.Output;

/**
 * @author wanghaifeng
 *
 */
public class DAOGenerator extends AbstractGenerator {

    private String packageName;

    private String className;

    private String boClassName;

    public DAOGenerator(String packageName, String className, String boName) {
        this.packageName = packageName;
        this.className = className;
        this.boClassName = boName;
    }

    protected void gen() {
        super.setPackageName(this.packageName + ".dao");
        super.addImport(this.packageName + ".entity.*");
        super.addImport(DAOException.class.getPackage().getName() + ".*");
        super.setClassName(this.className);
        super.setSuperClassName(DAO.class.getName());
        super.setInterface(true);
        String boParamName = boClassName.substring(0, 1).toLowerCase() + boClassName.substring(1);
        super.addAbstractMethod("create", "void", new String[] { boClassName }, new String[] { boParamName }, new Class[] { CreateException.class });
        super.addAbstractMethod("remove", "void", new String[] { boClassName }, new String[] { boParamName }, new Class[] { RemoveException.class });
        super.addAbstractMethod("remove", "void", new String[] { "long" }, new String[] { "id" }, new Class[] { RemoveException.class });
        super.addAbstractMethod("update", "void", new String[] { boClassName }, new String[] { boParamName }, new Class[] { UpdateException.class });
        super.addAbstractMethod("findByPrimaryKey", this.boClassName, new String[] { "long" }, new String[] { "id" }, new Class[] { NotFoundException.class });
    }

    public static void main(String args[]) throws Exception {
        Output out = new Output(System.out);
        DAOGenerator gen = new DAOGenerator("com.hh.shop", "IShopDAO", "ShopDO");
        gen.output(out);
    }
}
