package org.scribble.conversation.export.monitor;

import org.scribble.export.*;
import org.scribble.extensions.RegistryInfo;
import org.scribble.model.*;
import org.scribble.monitor.model.ConversationType;
import org.scribble.conversation.model.*;
import org.scribble.export.monitor.*;

/**
 * This class implements the monitor based export rule for the
 * ConversationModel entity.
 */
@RegistryInfo(extension = ExportRule.class, notation = ConversationNotation.NOTATION_CODE)
public class ConversationModelMonitorExportRule extends AbstractModelMonitorExportRule {

    /**
	 * This method determines whether the exporter rule is appropriate
	 * for the supplied model object and format.
	 * 
	 * @param modelObject The model object
	 * @param format The format
	 * @return Whether the rule is appropriate for the model object and
	 * 						format
	 */
    public boolean isSupported(ModelObject modelObject, Formatter format) {
        return (super.isSupported(modelObject, format) && ConversationModel.class.isAssignableFrom(modelObject.getClass()));
    }

    /**
	 * This method exports the model object.
	 * 
	 * @param modelObject The model object
	 * @param context The context
	 * @throws IOException Failed to record export information
	 */
    public void export(ModelObject modelObject, ExporterContext context) throws java.io.IOException {
        ConversationModel model = (ConversationModel) modelObject;
        MonitorFormatter formatter = (MonitorFormatter) context.getFormatter();
        super.export(model, context);
        if (model.getConversation() != null) {
            context.export(model.getConversation());
        }
        Object obj = context.pop();
        if (obj instanceof ConversationType) {
            formatter.setDescription((ConversationType) obj);
        }
    }
}
