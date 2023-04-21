package net.conquiris.gson;

import static com.google.common.base.Preconditions.checkArgument;
import java.lang.reflect.Type;
import java.util.Map.Entry;
import net.conquiris.api.index.IndexInfo;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Gson representation for {@link IndexInfo}.
 * @author Andres Rodriguez
 */
public final class GsonIndexInfo implements JsonSerializer<IndexInfo>, JsonDeserializer<IndexInfo> {

    private static final String PROPERTIES = "properties";

    /** Number of documents. */
    private static final String N = "n";

    /** Constructor. */
    public GsonIndexInfo() {
    }

    @Override
    public JsonElement serialize(IndexInfo src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject object = new JsonObject();
        if (src.getCheckpoint() != null) {
            object.addProperty(IndexInfo.CHECKPOINT_NAME, src.getCheckpoint());
        }
        if (src.getTargetCheckpoint() != null) {
            object.addProperty(IndexInfo.TARGET_CHECKPOINT_NAME, src.getTargetCheckpoint());
        }
        object.addProperty(N, src.getDocuments());
        object.addProperty(IndexInfo.TIMESTAMP_NAME, src.getTimestamp());
        object.addProperty(IndexInfo.SEQUENCE_NAME, src.getSequence());
        if (!src.getProperties().isEmpty()) {
            JsonObject properties = new JsonObject();
            for (Entry<String, String> entry : src.getProperties().entrySet()) {
                properties.addProperty(entry.getKey(), entry.getValue());
            }
            object.add(PROPERTIES, properties);
        }
        return object;
    }

    private String getOptionalString(JsonObject object, String key) {
        if (object.has(key)) {
            return object.getAsJsonPrimitive(key).getAsString();
        }
        return null;
    }

    @Override
    public IndexInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        checkArgument(json.isJsonObject(), "Expected JSON Object to deserialize IndexInfo.");
        final JsonObject object = json.getAsJsonObject();
        final String checkpoint = getOptionalString(object, IndexInfo.CHECKPOINT_NAME);
        final String targetCheckpoint = getOptionalString(object, IndexInfo.TARGET_CHECKPOINT_NAME);
        final int documents = object.getAsJsonPrimitive(N).getAsInt();
        final long timestamp = object.getAsJsonPrimitive(IndexInfo.TIMESTAMP_NAME).getAsLong();
        final long sequence = object.getAsJsonPrimitive(IndexInfo.SEQUENCE_NAME).getAsLong();
        final ImmutableMap.Builder<String, String> b = ImmutableMap.builder();
        if (object.has(PROPERTIES)) {
            JsonObject properties = object.getAsJsonObject(PROPERTIES);
            for (Entry<String, JsonElement> entry : properties.entrySet()) {
                b.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return IndexInfo.of(checkpoint, targetCheckpoint, documents, timestamp, sequence, b.build());
    }
}
