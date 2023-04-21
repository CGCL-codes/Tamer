package com.cresus.modules.exportmodules;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import com.cresus.model.Operation;
import com.cresus.model.OperationsList;
import com.google.gson.stream.JsonWriter;

public class JSONExport implements ExportModule {

    private static final String OUTPUT_PARAMETER = "output";

    public boolean exportOperations(Map<String, Object> parameters, OperationsList operations) {
        if (parameters.containsKey(OUTPUT_PARAMETER)) {
            Writer fileWriter = Writer.class.cast(parameters.get(OUTPUT_PARAMETER));
            JsonWriter writer = new JsonWriter(fileWriter);
            try {
                writer.beginArray();
                for (Iterator<Operation> iterator = operations.operations(); iterator.hasNext(); ) {
                    Operation operation = (Operation) iterator.next();
                    writer.beginObject();
                    writer.name("Amount").value(operation.getAmount());
                    writer.name("Date").value(operation.getDate().getTime());
                    writer.name("Label").value(operation.getLabel());
                    writer.endObject();
                }
                writer.endArray();
                return true;
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
