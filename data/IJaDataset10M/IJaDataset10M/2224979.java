package prisms.util.json;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/** Parses a JSON schema for validation */
public class JsonSchemaParser {

    /** Used by the JSON schema API for logging */
    public static final Logger log = Logger.getLogger(JsonSchemaParser.class);

    private final SAJParser theParser;

    private java.util.Map<String, String> theSchemaRoots;

    private java.util.Map<String, JSONObject> theStoredSchemas;

    /** Creates a schema parser */
    public JsonSchemaParser() {
        theSchemaRoots = new java.util.HashMap<String, String>();
        theStoredSchemas = new java.util.HashMap<String, JSONObject>();
        theParser = new SAJParser();
    }

    /** @return The JSON parser that parses schema files for this schema parser */
    public SAJParser getParser() {
        return theParser;
    }

    /**
	 * Adds a schema to this parser
	 * 
	 * @param name The name of the schema
	 * @param schemaRoot The root URL to use to search for .json files under the additional schema
	 */
    public void addSchema(String name, String schemaRoot) {
        theSchemaRoots.put(name, schemaRoot);
    }

    /**
	 * Parses a JSON schema
	 * 
	 * @param parent The parent element of the schema, or null if called from the root
	 * @param name The name of the schema element
	 * @param schemaEl The JSON-source of the schema element
	 * @return The schema element to validate with
	 */
    public JsonElement parseSchema(JsonElement parent, String name, Object schemaEl) {
        JsonElement ret = createElementFor(schemaEl);
        ret.configure(this, parent, name, schemaEl instanceof JSONObject ? (JSONObject) schemaEl : null);
        return ret;
    }

    /**
	 * Creates an unconfigured JsonElement for the given schema object
	 * 
	 * @param schemaEl The schema object to create an element for
	 * @return The JsonElement to parse the given schema type
	 */
    public JsonElement createElementFor(Object schemaEl) {
        if (schemaEl instanceof JSONObject) {
            JSONObject jsonSchema = (JSONObject) schemaEl;
            String typeName = (String) jsonSchema.get("valueType");
            if (typeName == null) return new JsonObjectElement(); else if (typeName.equals("set")) return new SetElement(); else if (typeName.equals("oneOf")) return new EnumElement(); else if (typeName.equals("string")) return new StringElement(); else if (typeName.equals("boolean")) return new BooleanElement(); else if (typeName.equals("number")) return new NumberElement(); else return createElementForType(typeName);
        } else if (schemaEl == null || schemaEl instanceof Boolean || schemaEl instanceof String || schemaEl instanceof Number) return new ConstantElement(schemaEl); else throw new IllegalStateException("Unrecognized schema element type: " + schemaEl.getClass().getName());
    }

    /**
	 * @param type The type of schema to get
	 * @return The schema for the given type
	 */
    public CustomSchemaElement createElementForType(String type) {
        int idx = type.indexOf('/');
        if (idx < 0) throw new IllegalStateException("No schema root to find schema type " + type);
        String schemaName = type.substring(0, idx);
        String schemaRoot = theSchemaRoots.get(schemaName);
        if (schemaRoot == null) throw new IllegalStateException("Unrecognized schema: " + schemaName);
        String typeName = type.substring(idx + 1);
        return new CustomSchemaElement(type, schemaRoot + "/" + typeName + ".json");
    }

    private static enum CommentState {

        NONE, LINE, BLOCK
    }

    /**
	 * @param schemaName The name of the schema to load
	 * @param schemaLocation The location of the schema to load
	 * @param parent The parent json element for the schema element
	 * @return The schema at the given location
	 */
    public JsonElement getExternalSchema(String schemaName, String schemaLocation, JsonElement parent) {
        JSONObject schema = theStoredSchemas.get(schemaName);
        if (schema != null) return createElementFor(schema);
        try {
            schema = (JSONObject) parseJSON(new java.net.URL(schemaLocation));
        } catch (java.net.MalformedURLException e) {
            throw new IllegalStateException("Malformed URL: " + schemaLocation, e);
        }
        JsonElement ret = createElementFor(schema);
        ret.configure(this, parent, schemaName, schema);
        return ret;
    }

    /**
	 * Parses a schema
	 * 
	 * @param schemaName The name of the schema
	 * @param schemaRoot The URL to a file in the schema
	 * @return The parsed file
	 */
    public JsonElement parseSchema(String schemaName, java.net.URL schemaRoot) {
        JsonSchemaParser parser = new JsonSchemaParser();
        String jsonLoc = schemaRoot.toString();
        jsonLoc = jsonLoc.substring(0, jsonLoc.lastIndexOf('/'));
        parser.addSchema(schemaName, jsonLoc);
        JSONObject jsonSchema = (JSONObject) parseJSON(schemaRoot);
        JsonElement ret = parser.createElementFor(jsonSchema);
        ret.configure(parser, null, schemaName, jsonSchema);
        return ret;
    }

    /**
	 * Parses JSON from a URL. This method removes all line- and block-style commments.
	 * 
	 * @param url The URL to get the JSON from
	 * @return The parsed JSON
	 */
    public Object parseJSON(java.net.URL url) {
        String json;
        java.io.Reader reader = null;
        java.io.BufferedReader br = null;
        try {
            reader = new java.io.InputStreamReader(url.openStream());
            java.io.StringWriter sw = new java.io.StringWriter();
            br = new java.io.BufferedReader(reader);
            int read;
            CommentState state = CommentState.NONE;
            int buffer = -1;
            while ((read = br.read()) >= 0) {
                switch(state) {
                    case NONE:
                        if (read == '/') {
                            if (buffer == '/') {
                                buffer = -1;
                                state = CommentState.LINE;
                                continue;
                            }
                            if (buffer >= 0) sw.write(buffer);
                            buffer = read;
                            continue;
                        } else if (read == '*' && buffer == '/') {
                            state = CommentState.BLOCK;
                            buffer = -1;
                            continue;
                        } else {
                            if (buffer >= 0) {
                                sw.write(buffer);
                                buffer = -1;
                            }
                            sw.write(read);
                        }
                        break;
                    case LINE:
                        if (read == '\n' || read == '\r') state = CommentState.NONE;
                        break;
                    case BLOCK:
                        if (read == '*') buffer = read; else if (read == '/') {
                            if (buffer == '*') state = CommentState.NONE;
                            buffer = -1;
                        } else buffer = -1;
                        break;
                }
            }
            json = sw.toString();
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Could not find JSON at " + url, e);
        } finally {
            if (br != null) try {
                br.close();
            } catch (java.io.IOException e) {
                log.error("Could not close stream", e);
            }
        }
        JSONObject schema;
        try {
            schema = (JSONObject) theParser.parse(new java.io.StringReader(json), new SAJParser.DefaultHandler());
        } catch (Throwable e) {
            throw new IllegalStateException("Could not parse JSON at " + url + ":\n" + json, e);
        }
        if (schema == null) throw new IllegalStateException("Could not parse JSON at " + url + ":\n" + json);
        return schema;
    }
}
