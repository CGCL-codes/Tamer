package org.jostraca.process;

import org.jostraca.Property;
import org.jostraca.format.FormatManager;
import org.jostraca.util.ErrorUtil;
import org.jostraca.util.Internal;
import org.jostraca.util.Standard;
import org.jostraca.util.PropertySet;
import org.jostraca.util.UserMessageHandler;
import org.jostraca.util.SimpleObjectManager;
import java.util.List;
import java.util.Iterator;

/** Management class for controlling processing class instances.
 */
public class BasicProcessManager extends SimpleObjectManager implements ProcessManager {

    public static final String DEFAULT_JAVA_PACKAGE = "org.jostraca.process.";

    private UserMessageHandler iUserMessageHandler;

    private FormatManager mFormatManager;

    /** Constructor. @see org.jostraca.util.SimpleObjectManager */
    public BasicProcessManager(PropertySet pPropertySet) {
        super(ProcessStage.class, DEFAULT_JAVA_PACKAGE);
        String processStages = pPropertySet.get(Property.main_ProcessStages);
        loadClasses(processStages, "ProcessStage");
    }

    public void setUserMessageHandler(UserMessageHandler pUserMessageHandler) {
        iUserMessageHandler = (UserMessageHandler) Internal.null_arg(pUserMessageHandler);
    }

    public void setFormatManager(FormatManager pFormatManager) {
        mFormatManager = pFormatManager;
    }

    public void process(List pTemplateList) {
        try {
            List tmlist = (List) Internal.null_arg(pTemplateList);
            ErrorUtil.not_null(iUserMessageHandler);
            for (Iterator psT = iObjects.iterator(); psT.hasNext(); ) {
                ProcessStage ps = (ProcessStage) psT.next();
                ps.setUserMessageHandler(iUserMessageHandler);
                ps.setFormatManager(mFormatManager);
                ps.process(tmlist);
            }
        } catch (ProcessException pe) {
            throw pe;
        } catch (Exception e) {
            throw new ProcessException(e);
        }
    }

    public String toString() {
        return "ProcessManager" + Standard.COLON_OPEN_SQUARE_BRACKET + super.toString() + Standard.CLOSE_SQUARE_BRACKET;
    }
}
