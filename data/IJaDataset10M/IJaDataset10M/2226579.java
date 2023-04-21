package balmysundaycandy.core.operations;

import java.util.HashMap;
import java.util.Map;
import balmysundaycandy.core.operations.OperationNamespaces.blobstore;
import balmysundaycandy.core.operations.OperationNamespaces.datastore_v3;
import balmysundaycandy.core.operations.OperationNamespaces.images;
import balmysundaycandy.core.operations.OperationNamespaces.mail;
import balmysundaycandy.core.operations.OperationNamespaces.memcache;
import balmysundaycandy.core.operations.OperationNamespaces.taskqueue;
import balmysundaycandy.core.operations.OperationNamespaces.urlfetch;
import balmysundaycandy.core.operations.OperationNamespaces.user;
import balmysundaycandy.core.operations.OperationNamespaces.xmpp;
import com.google.appengine.api.blobstore.BlobstoreServicePb.CreateUploadURLRequest;
import com.google.appengine.api.blobstore.BlobstoreServicePb.CreateUploadURLResponse;
import com.google.appengine.api.blobstore.BlobstoreServicePb.DeleteBlobRequest;
import com.google.appengine.api.images.ImagesServicePb.ImagesCompositeRequest;
import com.google.appengine.api.images.ImagesServicePb.ImagesCompositeResponse;
import com.google.appengine.api.images.ImagesServicePb.ImagesHistogramRequest;
import com.google.appengine.api.images.ImagesServicePb.ImagesHistogramResponse;
import com.google.appengine.api.images.ImagesServicePb.ImagesTransformRequest;
import com.google.appengine.api.images.ImagesServicePb.ImagesTransformResponse;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueAddRequest;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueAddResponse;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueDeleteQueueRequest;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueDeleteQueueResponse;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueDeleteRequest;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueDeleteResponse;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueFetchQueueStatsRequest;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueFetchQueueStatsResponse;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueFetchQueuesRequest;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueFetchQueuesResponse;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueQueryTasksRequest;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueQueryTasksResponse;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueUpdateQueueRequest;
import com.google.appengine.api.labs.taskqueue.TaskQueuePb.TaskQueueUpdateQueueResponse;
import com.google.appengine.api.mail.MailServicePb.MailMessage;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheBatchIncrementRequest;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheBatchIncrementResponse;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheDeleteRequest;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheDeleteResponse;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheFlushRequest;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheFlushResponse;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheGetRequest;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheGetResponse;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheGrabTailRequest;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheGrabTailResponse;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheIncrementRequest;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheIncrementResponse;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheSetRequest;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheSetResponse;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheStatsRequest;
import com.google.appengine.api.memcache.MemcacheServicePb.MemcacheStatsResponse;
import com.google.appengine.api.urlfetch.URLFetchServicePb.URLFetchRequest;
import com.google.appengine.api.urlfetch.URLFetchServicePb.URLFetchResponse;
import com.google.appengine.api.xmpp.XMPPServicePb.PresenceRequest;
import com.google.appengine.api.xmpp.XMPPServicePb.PresenceResponse;
import com.google.appengine.api.xmpp.XMPPServicePb.XmppInviteRequest;
import com.google.appengine.api.xmpp.XMPPServicePb.XmppInviteResponse;
import com.google.appengine.api.xmpp.XMPPServicePb.XmppMessageRequest;
import com.google.appengine.api.xmpp.XMPPServicePb.XmppMessageResponse;
import com.google.appengine.repackaged.com.google.io.protocol.ProtocolMessage;
import com.google.appengine.repackaged.com.google.protobuf.GeneratedMessage;
import com.google.appengine.repackaged.com.google.protobuf.InvalidProtocolBufferException;
import com.google.apphosting.api.ApiBasePb.Integer64Proto;
import com.google.apphosting.api.ApiBasePb.StringProto;
import com.google.apphosting.api.ApiBasePb.VoidProto;
import com.google.apphosting.api.DatastorePb.AllocateIdsRequest;
import com.google.apphosting.api.DatastorePb.AllocateIdsResponse;
import com.google.apphosting.api.DatastorePb.CommitResponse;
import com.google.apphosting.api.DatastorePb.CompositeIndices;
import com.google.apphosting.api.DatastorePb.Cursor;
import com.google.apphosting.api.DatastorePb.DeleteRequest;
import com.google.apphosting.api.DatastorePb.DeleteResponse;
import com.google.apphosting.api.DatastorePb.GetRequest;
import com.google.apphosting.api.DatastorePb.GetResponse;
import com.google.apphosting.api.DatastorePb.GetSchemaRequest;
import com.google.apphosting.api.DatastorePb.NextRequest;
import com.google.apphosting.api.DatastorePb.PutRequest;
import com.google.apphosting.api.DatastorePb.PutResponse;
import com.google.apphosting.api.DatastorePb.Query;
import com.google.apphosting.api.DatastorePb.QueryExplanation;
import com.google.apphosting.api.DatastorePb.QueryResult;
import com.google.apphosting.api.DatastorePb.RunCompiledQueryRequest;
import com.google.apphosting.api.DatastorePb.Schema;
import com.google.apphosting.api.DatastorePb.Transaction;
import com.google.apphosting.api.UserServicePb.CreateLoginURLRequest;
import com.google.apphosting.api.UserServicePb.CreateLoginURLResponse;
import com.google.apphosting.api.UserServicePb.CreateLogoutURLRequest;
import com.google.apphosting.api.UserServicePb.CreateLogoutURLResponse;
import com.google.storage.onestore.v3.OnestoreEntity.CompositeIndex;

/**
 * hold protocol buffer object with key. key is composed by service name and
 * method name.
 * 
 * @author marblejenka
 * 
 */
public class ProtocolBufferObjectHolder {

    /**
	 * hold all protocol message objects.
	 */
    private static final Map<ProtocolBufferKey, ProtocolBufferValue> protocolmessages = new HashMap<ProtocolBufferKey, ProtocolBufferValue>();

    /**
	 * hold all generated message objects.
	 */
    private static final Map<ProtocolBufferKey, ProtocolBufferValue> generatedmessages = new HashMap<ProtocolBufferKey, ProtocolBufferValue>();

    static {
        protocolmessages.put(new ProtocolBufferKey(blobstore.packageName, blobstore.methodName.CreateUploadURL), new ProtocolBufferValue(new CreateUploadURLRequest(), new CreateUploadURLResponse()));
        protocolmessages.put(new ProtocolBufferKey(blobstore.packageName, blobstore.methodName.DeleteBlob), new ProtocolBufferValue(new DeleteBlobRequest(), new VoidProto()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.Get), new ProtocolBufferValue(new GetRequest(), new GetResponse()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.Put), new ProtocolBufferValue(new PutRequest(), new PutResponse()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.Delete), new ProtocolBufferValue(new DeleteRequest(), new DeleteResponse()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.RunQuery), new ProtocolBufferValue(new Query(), new QueryResult()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.RunCompiledQuery), new ProtocolBufferValue(new RunCompiledQueryRequest(), new QueryResult()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.Next), new ProtocolBufferValue(new NextRequest(), new QueryResult()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.Count), new ProtocolBufferValue(new Query(), new Integer64Proto()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.Explain), new ProtocolBufferValue(new Query(), new QueryExplanation()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.DeleteCursor), new ProtocolBufferValue(new Cursor(), new VoidProto()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.BeginTransaction), new ProtocolBufferValue(new VoidProto(), new Transaction()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.Commit), new ProtocolBufferValue(new Transaction(), new CommitResponse()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.Rollback), new ProtocolBufferValue(new Transaction(), new VoidProto()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.GetSchema), new ProtocolBufferValue(new GetSchemaRequest(), new Schema()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.AllocateIds), new ProtocolBufferValue(new AllocateIdsRequest(), new AllocateIdsResponse()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.CreateIndex), new ProtocolBufferValue(new CompositeIndex(), new Integer64Proto()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.UpdateIndex), new ProtocolBufferValue(new CompositeIndex(), new VoidProto()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.GetIndices), new ProtocolBufferValue(new StringProto(), new CompositeIndices()));
        protocolmessages.put(new ProtocolBufferKey(datastore_v3.packageName, datastore_v3.methodName.DeleteIndex), new ProtocolBufferValue(new CompositeIndex(), new VoidProto()));
        protocolmessages.put(new ProtocolBufferKey(images.packageName, images.methodName.Composite), new ProtocolBufferValue(new ImagesCompositeRequest(), new ImagesCompositeResponse()));
        protocolmessages.put(new ProtocolBufferKey(images.packageName, images.methodName.Histogram), new ProtocolBufferValue(new ImagesHistogramRequest(), new ImagesHistogramResponse()));
        protocolmessages.put(new ProtocolBufferKey(images.packageName, images.methodName.Transform), new ProtocolBufferValue(new ImagesTransformRequest(), new ImagesTransformResponse()));
        protocolmessages.put(new ProtocolBufferKey(mail.packageName, mail.methodName.Send), new ProtocolBufferValue(new MailMessage(), new VoidProto()));
        protocolmessages.put(new ProtocolBufferKey(mail.packageName, mail.methodName.SendToAdmins), new ProtocolBufferValue(new MailMessage(), new VoidProto()));
        generatedmessages.put(new ProtocolBufferKey(memcache.packageName, memcache.methodName.BatchIncrement), new ProtocolBufferValue(MemcacheBatchIncrementRequest.newBuilder(), MemcacheBatchIncrementResponse.newBuilder()));
        generatedmessages.put(new ProtocolBufferKey(memcache.packageName, memcache.methodName.Delete), new ProtocolBufferValue(MemcacheDeleteRequest.newBuilder(), MemcacheDeleteResponse.newBuilder()));
        generatedmessages.put(new ProtocolBufferKey(memcache.packageName, memcache.methodName.Get), new ProtocolBufferValue(MemcacheGetRequest.newBuilder(), MemcacheGetResponse.newBuilder()));
        generatedmessages.put(new ProtocolBufferKey(memcache.packageName, memcache.methodName.FlashAll), new ProtocolBufferValue(MemcacheFlushRequest.newBuilder(), MemcacheFlushResponse.newBuilder()));
        generatedmessages.put(new ProtocolBufferKey(memcache.packageName, memcache.methodName.GrabTail), new ProtocolBufferValue(MemcacheGrabTailRequest.newBuilder(), MemcacheGrabTailResponse.newBuilder()));
        generatedmessages.put(new ProtocolBufferKey(memcache.packageName, memcache.methodName.Increment), new ProtocolBufferValue(MemcacheIncrementRequest.newBuilder(), MemcacheIncrementResponse.newBuilder()));
        generatedmessages.put(new ProtocolBufferKey(memcache.packageName, memcache.methodName.Set), new ProtocolBufferValue(MemcacheSetRequest.newBuilder(), MemcacheSetResponse.newBuilder()));
        generatedmessages.put(new ProtocolBufferKey(memcache.packageName, memcache.methodName.Stats), new ProtocolBufferValue(MemcacheStatsRequest.newBuilder(), MemcacheStatsResponse.newBuilder()));
        protocolmessages.put(new ProtocolBufferKey(taskqueue.packageName, taskqueue.methodName.Add), new ProtocolBufferValue(new TaskQueueAddRequest(), new TaskQueueAddResponse()));
        protocolmessages.put(new ProtocolBufferKey(taskqueue.packageName, taskqueue.methodName.Delete), new ProtocolBufferValue(new TaskQueueDeleteRequest(), new TaskQueueDeleteResponse()));
        protocolmessages.put(new ProtocolBufferKey(taskqueue.packageName, taskqueue.methodName.DeleteQueue), new ProtocolBufferValue(new TaskQueueDeleteQueueRequest(), new TaskQueueDeleteQueueResponse()));
        protocolmessages.put(new ProtocolBufferKey(taskqueue.packageName, taskqueue.methodName.FetchQueues), new ProtocolBufferValue(new TaskQueueFetchQueuesRequest(), new TaskQueueFetchQueuesResponse()));
        protocolmessages.put(new ProtocolBufferKey(taskqueue.packageName, taskqueue.methodName.FetchQueueStats), new ProtocolBufferValue(new TaskQueueFetchQueueStatsRequest(), new TaskQueueFetchQueueStatsResponse()));
        protocolmessages.put(new ProtocolBufferKey(taskqueue.packageName, taskqueue.methodName.QueryTasks), new ProtocolBufferValue(new TaskQueueQueryTasksRequest(), new TaskQueueQueryTasksResponse()));
        protocolmessages.put(new ProtocolBufferKey(taskqueue.packageName, taskqueue.methodName.UpdateQueue), new ProtocolBufferValue(new TaskQueueUpdateQueueRequest(), new TaskQueueUpdateQueueResponse()));
        generatedmessages.put(new ProtocolBufferKey(urlfetch.packageName, urlfetch.methodName.Fetch), new ProtocolBufferValue(URLFetchRequest.newBuilder(), URLFetchResponse.newBuilder()));
        protocolmessages.put(new ProtocolBufferKey(user.packageName, user.methodName.CreateLoginURL), new ProtocolBufferValue(new CreateLoginURLRequest(), new CreateLoginURLResponse()));
        protocolmessages.put(new ProtocolBufferKey(user.packageName, user.methodName.CreateLogoutURL), new ProtocolBufferValue(new CreateLogoutURLRequest(), new CreateLogoutURLResponse()));
        protocolmessages.put(new ProtocolBufferKey(xmpp.packageName, xmpp.methodName.GetPresence), new ProtocolBufferValue(new PresenceRequest(), new PresenceResponse()));
        protocolmessages.put(new ProtocolBufferKey(xmpp.packageName, xmpp.methodName.SendInvite), new ProtocolBufferValue(new XmppInviteRequest(), new XmppInviteResponse()));
        protocolmessages.put(new ProtocolBufferKey(xmpp.packageName, xmpp.methodName.SendMessage), new ProtocolBufferValue(new XmppMessageRequest(), new XmppMessageResponse()));
    }

    public static final ProtocolMessage<?> cloneProtocolMessageRequestObject(String servicename, String methodname) {
        return protocolmessages.get(new ProtocolBufferKey(servicename, methodname)).cloneProtocolMessageRequest();
    }

    public static final ProtocolMessage<?> cloneProtocolMessageResponseObject(String servicename, String methodname) {
        return protocolmessages.get(new ProtocolBufferKey(servicename, methodname)).cloneProtocolMessageResponse();
    }

    public static final GeneratedMessage.Builder<?> cloneGeneratedMessageRequestObject(String servicename, String methodname) {
        return generatedmessages.get(new ProtocolBufferKey(servicename, methodname)).cloneGeneratedMessageRequest();
    }

    public static final GeneratedMessage.Builder<?> cloneGeneratedMessageResponseObject(String servicename, String methodname) {
        return generatedmessages.get(new ProtocolBufferKey(servicename, methodname)).cloneGeneratedMessageResponse();
    }

    public static final String requestAsString(String servicename, String methodname, byte[] requset) {
        if (protocolmessages.containsKey(new ProtocolBufferKey(servicename, methodname))) {
            ProtocolMessage<?> protocolMessage = cloneProtocolMessageRequestObject(servicename, methodname);
            protocolMessage.mergeFrom(requset);
            return protocolMessage.toString();
        }
        if (generatedmessages.containsKey(new ProtocolBufferKey(servicename, methodname))) {
            GeneratedMessage generatedMessage = (GeneratedMessage) cloneGeneratedMessageRequestObject(servicename, methodname).build();
            try {
                return generatedMessage.newBuilderForType().mergeFrom(requset).build().toString();
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            return generatedMessage.toString();
        }
        return null;
    }

    public static final String responseAsString(String servicename, String methodname, byte[] resonse) {
        if (protocolmessages.containsKey(new ProtocolBufferKey(servicename, methodname))) {
            ProtocolMessage<?> protocolMessage = cloneProtocolMessageResponseObject(servicename, methodname);
            protocolMessage.mergeFrom(resonse);
            return protocolMessage.toString();
        }
        if (generatedmessages.containsKey(new ProtocolBufferKey(servicename, methodname))) {
            GeneratedMessage generatedMessage = (GeneratedMessage) cloneGeneratedMessageResponseObject(servicename, methodname).build();
            try {
                return generatedMessage.newBuilderForType().mergeFrom(resonse).build().toString();
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
