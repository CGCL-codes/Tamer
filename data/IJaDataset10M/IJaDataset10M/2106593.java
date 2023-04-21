package es.caib.zkib.datamodel.xml.handler;

import java.lang.reflect.Method;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.w3c.dom.Element;
import org.zkoss.zk.ui.UiException;
import es.caib.zkib.datamodel.DataContext;
import es.caib.zkib.datamodel.xml.ParseException;
import es.caib.zkib.datamodel.xml.definition.DeleteMethodDefinition;
import es.caib.zkib.datamodel.xml.definition.HandlerMethodDefinition;
import es.caib.zkib.datamodel.xml.definition.InsertMethodDefinition;
import es.caib.zkib.datamodel.xml.definition.MethodDefinition;
import es.caib.zkib.datamodel.xml.definition.UpdateMethodDefinition;

public class EJBHandler extends AbstractEJBHandler implements PersistenceHandler {

    private InsertMethodDefinition insertMethod;

    private UpdateMethodDefinition updateMethod;

    private DeleteMethodDefinition deleteMethod;

    private String exception;

    public EJBHandler() {
        super();
    }

    public void doInsert(DataContext ctx) throws Exception {
        if (insertMethod == null) throw new RuntimeException("Insert not allowed");
        invokeMethod(ctx, insertMethod);
    }

    public void doDelete(DataContext ctx) throws Exception {
        if (deleteMethod == null) throw new RuntimeException("Delete not allowed");
        invokeMethod(ctx, deleteMethod);
    }

    public void doUpdate(DataContext ctx) throws Exception {
        if (updateMethod == null) throw new RuntimeException("Update not allowed");
        invokeMethod(ctx, updateMethod);
    }

    public void add(InsertMethodDefinition method) throws ParseException {
        if (insertMethod != null) exception = "Only one insert method is allowed";
        insertMethod = method;
    }

    public void add(DeleteMethodDefinition method) throws ParseException {
        if (deleteMethod != null) exception = "Only one delete method is allowed";
        deleteMethod = method;
    }

    public void add(UpdateMethodDefinition method) throws ParseException {
        if (updateMethod != null) exception = "Only one update method is allowed";
        updateMethod = method;
    }

    /**
	 * @return Returns the deleteMethod.
	 */
    public DeleteMethodDefinition getDeleteMethod() {
        return deleteMethod;
    }

    /**
	 * @return Returns the insertMethod.
	 */
    public InsertMethodDefinition getInsertMethod() {
        return insertMethod;
    }

    /**
	 * @return Returns the updateMethod.
	 */
    public UpdateMethodDefinition getUpdateMethod() {
        return updateMethod;
    }

    public void test(Element element) throws ParseException {
        if (exception != null) throw new ParseException(exception, element);
    }
}
