package dbgate.ermanagement.support.persistant.inheritancetest;

/**
 * Created by IntelliJ IDEA.
 * User: Adipa
 * Date: Oct 2, 2010
 * Time: 4:23:25 PM
 */
public interface IInheritanceTestSubEntityA extends IInheritanceTestSuperEntity {

    String getNameA();

    void setNameA(String nameA);
}
