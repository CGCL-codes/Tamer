package ru.adv.db.app.request;

import ru.adv.db.base.Ancestor;
import ru.adv.db.base.MValue;
import ru.adv.db.base.MValueCollection;
import ru.adv.db.base.MObject;
import ru.adv.db.handler.HandlerException;
import ru.adv.db.handler.DeleteOptions;
import ru.adv.db.config.DBConfigException;
import ru.adv.util.Strings;
import ru.adv.util.ErrorCodeException;
import ru.adv.util.StringParser;
import ru.adv.util.BadBooleanException;
import java.util.*;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * Удаляет объекты из базы<p>
 * Атрибуты:
 * <ul>
 * <li>
 * objects<br>
 * Список объектов для удаления
 * </li
 * </ul>
 * @version $Revision: 1.15 $
 */
public class DeleteInstruction extends Instruction {

    private boolean isMultiply = false;

    public DeleteInstruction() {
    }

    /**
	 * парсит атрибуты инструкции
	 */
    private void parseAttributes() throws RequestException {
        try {
            isMultiply = StringParser.toBoolean(getAttribute("multiply", "false"));
        } catch (BadBooleanException e) {
            throw new RequestException(e);
        }
    }

    /**
	 * проверяет атрибут multiply
	 * @return
	 */
    private boolean isSingleDelete() {
        return !isMultiply;
    }

    /**
	 * Метод вызывается "на входе" в ноду XML запроса
	 */
    protected boolean onStartTag() throws RequestException {
        getContextStack().setExpiresTime(0);
        parseAttributes();
        return true;
    }

    /**
	 * Метод вызывается "на выходе" из ноды XML запроса
	 */
    protected void onEndTag() throws RequestException {
        try {
            final List<String> objects = Strings.split(getAttribute("objects", ""), ",");
            if (objects.size() == 0) {
                return;
            }
            getDBHandler().getConnection().getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    doDelete(objects);
                }
            });
        } catch (ErrorCodeException e) {
            throw new RequestException("Can not delete objects", e);
        } catch (Exception e) {
            throw new RequestException(0, e);
        }
    }

    private void doDelete(List objects) {
        List objectsToSave = getDBHandler().getDBConfig().getSaveOrder(objects);
        Ancestor a = calculateAncestor(objects);
        for (int i = objectsToSave.size() - 1; i >= 0; i--) {
            String o = (String) objectsToSave.get(i);
            MValueCollection mvc = a.get(o, "id");
            if (mvc != null) {
                for (Iterator m = mvc.iterator(); m.hasNext(); ) {
                    if (((MValue) m.next()).getSearchValue() == null) {
                        m.remove();
                    }
                }
            }
            if (mvc == null || mvc.size() == 0) {
                continue;
            } else {
                if (isSingleDelete() && mvc.size() > 1) {
                    RequestException e = new RequestException(RequestException.DB_CANNOT_DELETE, "Try delete many object in multiply=\"no\" mode");
                    e.setDatabase(getDBHandler().getDBConfig().getId());
                    e.setObject(o);
                    e.setAttr("id", mvc.toString());
                    throw e;
                }
                for (Iterator vi = mvc.iterator(); vi.hasNext(); ) {
                    DeleteOptions options = new DeleteOptions(createMObject(o), this, ((MValue) vi.next()).getSearchValue());
                    getDBHandler().delete(options);
                }
            }
        }
    }

    private MObject createMObject(String o) throws DBConfigException {
        return getDBHandler().getDBConfig().createMObject(o, null);
    }

    /**
	 * Добавляет в текущий Ancestor значения id объектов из
	 * верхних контекстов, если они помечены toProcess
	 */
    private Ancestor calculateAncestor(List objects) {
        Ancestor anc = _contextStack.peek().getAncestor().copy();
        Collection depObjects = _contextStack.peek().getDepend().getObjects();
        List contexts = _contextStack.getContexts();
        Set handledNames = new HashSet();
        for (int i = contexts.size() - 1; i > 0; i--) {
            Context ctx = (Context) contexts.get(i);
            RObjectCollection roCollections = ctx.getParents();
            if (roCollections.sizeToProcess() > 0) {
                String rName = roCollections.get(0).getName();
                if (!handledNames.contains(rName) && objects.contains(rName) && depObjects.contains(rName) && !anc.exists(rName, "id")) {
                    handledNames.add(rName);
                    anc.set(rName, "id", roCollections.idToProcess());
                }
            }
        }
        return anc;
    }
}
