package extjsdyntran.servlets.actions;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import ws4is.engine.utils.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import extjsdyntran.servlets.Constants;
import extjsdyntran.translation.ITranslationService;

/**
 * @description Main class for Translation Editor Grid to support record deletion/creation/update
 * 			    Used only from EditorActions
 */
final class EditorRecordManager {

    static final Logger logger = LoggerFactory.get();

    public static final String errmsg = "Error converting to JSON...";

    private static Gson gs = new Gson();

    private static String getError(Exception e) {
        try {
            EditorResponse er = new EditorResponse();
            er.setSuccess(false);
            er.setMsg(e.getMessage());
            return gs.toJson(er);
        } catch (Exception e1) {
            logger.error(errmsg, e1);
            return Constants.json_false;
        }
    }

    private static EditorResponse getResponse(boolean status, String msg) {
        EditorResponse er = new EditorResponse();
        er.setSuccess(status);
        er.setMsg(msg);
        return er;
    }

    public static String getTranslationRecords(ITranslationService service, String language) {
        try {
            Map<String, String> lang = service.getTranslations(language);
            EditorResponse er = getResponse(false, Constants.err1);
            if (service == null) {
                return gs.toJson(er);
            }
            er.reset();
            er.setSuccess(true);
            er.setTranslation(lang);
            er.setLanguage(language);
            return gs.toJson(er);
        } catch (Exception e) {
            logger.error(errmsg, e);
            return getError(e);
        }
    }

    public static String deleteRecords(ITranslationService service, String language, String values) {
        try {
            EditorResponse er = getResponse(false, Constants.err1);
            JsonElement je = new JsonParser().parse(values);
            if (je.isJsonPrimitive()) {
                er.reset();
                boolean result = service.deleteTranslation(language, je.getAsString());
                er.setSuccess(result);
                if (!result) er.setMsg(Constants.err14);
                return gs.toJson(er);
            }
            EditorRecord[] data = null;
            try {
                data = gs.fromJson(values, EditorRecord[].class);
            } catch (Exception e) {
                String[] keys = gs.fromJson(values, String[].class);
                for (String key : keys) {
                    service.deleteTranslation(language, key);
                }
                er.reset();
                er.setSuccess(true);
                return gs.toJson(er);
            }
            int j = 0;
            while (j < data.length) {
                service.deleteTranslation(language, data[j].getKey());
                j++;
            }
            ;
            er.reset();
            er.setSuccess(true);
            return gs.toJson(er);
        } catch (Exception e) {
            logger.error(errmsg, e);
            return getError(e);
        }
    }

    public static String updateRecords(ITranslationService service, String language, String values) {
        return createRecords(service, language, values);
    }

    public static String createRecords(ITranslationService service, String language, String values) {
        try {
            EditorResponse er = getResponse(false, Constants.err1);
            if (service == null) return gs.toJson(er);
            er.reset();
            er.setSuccess(true);
            Collection<EditorRecord> data = null;
            Type mapType = new TypeToken<Collection<EditorRecord>>() {
            }.getType();
            try {
                data = gs.fromJson(values, mapType);
                er.getData().addAll(data);
            } catch (Exception e) {
                EditorRecord rc = gs.fromJson(values, EditorRecord.class);
                er.addRecord(rc);
            }
            Iterator<Object> it = er.getData().iterator();
            while (it.hasNext()) {
                EditorRecord rc = (EditorRecord) it.next();
                service.saveTranslation(language, rc.getKey(), rc.getValue());
            }
            er.setSuccess(true);
            return gs.toJson(er);
        } catch (Exception e) {
            logger.error(errmsg, e);
            return getError(e);
        }
    }
}
