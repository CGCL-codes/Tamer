package net.teqlo.components.standard.xmlDbV2_0;

import java.util.UUID;
import net.teqlo.TeqloException;
import net.teqlo.components.OutputSet;
import net.teqlo.db.ActivityLookup;
import net.teqlo.db.User;
import net.teqlo.db.UserDocument;

/**
 * Inserts a record into the XML document managed by our executor
 * 
 * @author jthwaites
 * 
 */
public class InsertActivity extends AbstractDbActivity {

    public static final String PARENT_XPATH_ATTRIBUTE = "xmldb.parentXPath";

    public static final String INSERTED_OUTPUT_KEY = "Inserted";

    public static final String NOT_INSERTED_OUTPUT_KEY = "NotInserted";

    private String parentXPath = null;

    /**
	 * Constructor saves executor and looks up the parent xpath attribute
	 * 
	 * @param al
	 * @param executor
	 * @throws TeqloException if any error occurs
	 */
    public InsertActivity(User user, ActivityLookup al, DbExecutor executor) throws TeqloException {
        super(user, al, executor);
        this.parentXPath = al.getAttributes().getAttributeValue(PARENT_XPATH_ATTRIBUTE);
    }

    @Override
    protected void actionsOnRun() throws Exception {
        OutputSet output = this.addOutputSet(NOT_INSERTED_OUTPUT_KEY);
        UserDocument userDoc = this.getDocument();
        String record = (String) this.input.get(RECORD_FIELD_KEY);
        UUID uuid = userDoc.insertRecord(this.parentXPath, record);
        output.setOutputKey(INSERTED_OUTPUT_KEY);
        if (this.al.getOutputs().getOutput(INSERTED_OUTPUT_KEY).getKeys().contains(KEY_FIELD_KEY)) {
            output.put(KEY_FIELD_KEY, uuid.toString());
        }
        if (this.al.getOutputs().getOutput(INSERTED_OUTPUT_KEY).getKeys().contains(RECORD_FIELD_KEY)) {
            String insertedRecord = userDoc.getRecord(uuid);
            output.put(RECORD_FIELD_KEY, insertedRecord);
        }
    }

    /**
	 * Static validation invoked by executor
	 * 
	 * @param al
	 * @throws TeqloException
	 */
    public static void validate(ActivityLookup al) throws TeqloException {
        if (al.getAttributes().getAttributeValue(PARENT_XPATH_ATTRIBUTE) == null) throw new TeqloException(al, PARENT_XPATH_ATTRIBUTE, null, "This activity must have this attribute defined");
        if (!al.getInput().getKeys().contains(RECORD_FIELD_KEY)) throw new TeqloException(al, RECORD_FIELD_KEY, null, "This activity must have input field with this key");
    }
}
