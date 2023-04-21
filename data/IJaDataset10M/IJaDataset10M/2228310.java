package cn.com.believer.songyuanframework.openapi.storage.xdrive.impl.simple.functions.collection;

import java.io.IOException;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.log4j.Logger;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.constant.XDriveConstant;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.impl.simple.core.XDriveHTTPManager;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.object.core.CollectionObject;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.object.functional.collection.CollectionNewInput;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.object.functional.collection.CollectionNewOutput;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.util.ObjConvertor;
import cn.com.believer.songyuanframework.openapi.storage.xdrive.util.ParamConverter;

/**
 * @author Jimmy
 * 
 */
public class Collection implements ICollection {

    /** log4j logger. */
    protected static final Logger LOGGER = Logger.getLogger(Collection.class);

    /**
     * 
     */
    private XDriveHTTPManager httpManager = XDriveHTTPManager.getXDriveHTTPManager();

    /**
     * @param collectionNewInput
     *            input
     * @return output
     * @throws IOException
     *             IO exception
     */
    public CollectionNewOutput collectionNew(CollectionNewInput collectionNewInput) throws IOException {
        CollectionNewOutput collectionNewOutput = new CollectionNewOutput();
        JSONObject postObj = new JSONObject();
        CollectionObject collection = collectionNewInput.getCollection();
        postObj.put("collection", collection.toJSONString());
        String jsonResult = httpManager.doPost(ParamConverter.convert2HTTPorHTTPS(collectionNewInput) + XDriveConstant.JSON_VERSION + XDriveConstant.ACTION_COLLECTION_NEW + ParamConverter.convert2URLParameters(collectionNewInput), postObj.toString());
        JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(jsonResult);
        JSONObject resultsObj = (JSONObject) jsonObj.get("results");
        JSONArray exceptionsAry = (JSONArray) jsonObj.get("exceptions");
        if (resultsObj != null) {
            JSONObject collectionObj = (JSONObject) resultsObj.get("collection");
            CollectionObject collectionObject = ObjConvertor.toCollectionObject(collectionObj);
            collectionNewOutput.setCollection(collectionObject);
        }
        if (exceptionsAry != null) {
            List eList = ObjConvertor.toExceptionObjectList(exceptionsAry);
            collectionNewOutput.setExceptions(eList);
        }
        return collectionNewOutput;
    }
}
