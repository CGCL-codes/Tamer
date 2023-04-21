package pl.edu.pjwstk.mteam.pubsub.tests;

import java.util.List;
import pl.edu.pjwstk.mteam.p2p.P2PNode;
import pl.edu.pjwstk.mteam.pubsub.interestconditions.InterestConditions;
import pl.edu.pjwstk.mteam.pubsub.logging.Logger;
import org.junit.*;
import pl.edu.pjwstk.mteam.core.NetworkObject;
import pl.edu.pjwstk.mteam.core.Node;
import pl.edu.pjwstk.mteam.core.NodeCallback;
import pl.edu.pjwstk.mteam.core.NodeInfo;
import pl.edu.pjwstk.mteam.pubsub.accesscontrol.AccessControlRules;
import pl.edu.pjwstk.mteam.pubsub.algorithm.implementation.DefaultCustomizableAlgorithm;
import pl.edu.pjwstk.mteam.pubsub.core.PubSubConstants;
import pl.edu.pjwstk.mteam.pubsub.core.CoreAlgorithm;
import pl.edu.pjwstk.mteam.pubsub.core.Subscriber;
import pl.edu.pjwstk.mteam.pubsub.core.Topic;
import pl.edu.pjwstk.mteam.pubsub.core.User;
import pl.edu.pjwstk.mteam.pubsub.message.request.PublishRequest;
import pl.edu.pjwstk.mteam.pubsub.message.request.SubscribeRequest;
import pl.edu.pjwstk.mteam.pubsub.message.response.StandardResponse;

public class DefaultAlgorithm_Test {

    static Logger logger = Logger.getLogger("pl.edu.pjwstk.mteam.pubsub.tests");

    static Topic topic;

    static CoreAlgorithm pubsubmngr;

    static DefaultCustomizableAlgorithm algorithm;

    static {
        topic = new Topic("Software Developement");
        Subscriber subscriber = new Subscriber("paulina", topic);
        topic.setOwner(subscriber);
        Node n = new P2PNode(new NodeCallback() {

            @Override
            public void onDisconnect(Node node) {
                ;
            }

            @Override
            public void onInsertObject(Node node, NetworkObject object) {
                ;
            }

            @Override
            public void onJoin(Node node) {
                ;
            }

            @Override
            public void onObjectLookup(Node node, Object object) {
                ;
            }

            @Override
            public void onOverlayError(Node node, Object sourceID, int errorCode) {
                ;
            }

            @Override
            public void onTopicCreate(Node node, Object topicID) {
                ;
            }

            @Override
            public void onTopicRemove(Node node, Object topicID) {
                ;
            }

            @Override
            public void onTopicSubscribe(Node node, Object topicID) {
                ;
            }

            @Override
            public void onUserLookup(Node node, Object userInfo) {
                ;
            }

            public boolean onDeliverRequest(List<NetworkObject> objectList) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean onForwardingRequest(List<NetworkObject> objectList) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onTopicNotify(Node node, Object topicID, byte[] message, boolean historical, short eventType) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onTopicCreate(Node node, Object topicID, int transID) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onTopicSubscribe(Node node, Object topicID, int transID) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onTopicUnsubscribe(Node node, Object topicID, int respCode) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onOverlayError(Node node, Object sourceID, int errorCode, int transID) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onPubSubError(Node node, Object topicID, short operationType, int errorCode) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onPubSubError(Node node, Object topicID, short operationType, int errorCode, int transID) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        pubsubmngr = new CoreAlgorithm(9700, n);
        pubsubmngr.setCustomizableAlgorithm();
        algorithm = (DefaultCustomizableAlgorithm) pubsubmngr.getCustomizableAlgorithm();
    }

    @Test
    public void DefaultAlgorithm_isForwardingNeeded1_Test() {
        topic.setParent(new NodeInfo("parentnode"));
        logger.trace("Entering test 1....");
        topic.setDistance(-1);
        logger.trace("This node info: " + pubsubmngr.getNodeInfo());
        logger.trace("This node distance: " + topic.getDistance());
        SubscribeRequest req = new SubscribeRequest(7000, new NodeInfo("12341", "10.69.40.111", "tiia", 9070), new NodeInfo("12342", "10.69.40.222", "tiia2", 9072), "Software developement", 100000000, -1, -1);
        logger.trace("Request originator's info: " + req.getSourceInfo());
        logger.trace("Request originator's distance: " + req.getDistance());
        pubsubmngr.getTopology().onDeliverSubscribe(req, topic);
    }

    @Test
    public void DefaultAlgorithm_isForwardingNeeded2_Test() {
        logger.trace("\nEntering test 2....");
        topic.setDistance(10);
        logger.trace("This node info: " + pubsubmngr.getNodeInfo());
        logger.trace("This node distance: " + topic.getDistance());
        SubscribeRequest req = new SubscribeRequest(7000, new NodeInfo("12341", "10.69.40.111", "tiia", 9070), new NodeInfo("12342", "10.69.40.222", "tiia2", 9072), "Software developement", 100000000, 20, 30);
        logger.trace("Request originator's info: " + req.getSourceInfo());
        logger.trace("Request originator's distance: " + req.getDistance());
        pubsubmngr.getTopology().onDeliverSubscribe(req, topic);
    }

    @Test
    public void DefaultAlgorithm_isForwardingNeeded3_Test() {
        logger.trace("Entering test 3....");
        topic.setDistance(50);
        logger.trace("This node info: " + pubsubmngr.getNodeInfo());
        logger.trace("This node distance: " + topic.getDistance());
        SubscribeRequest req = new SubscribeRequest(7000, new NodeInfo("12341", "10.69.40.111", "tiia", 9070), new NodeInfo("12342", "10.69.40.222", "tiia2", 9072), "Software developement", 100000000, 20, 30);
        logger.trace("Request originator's info: " + req.getSourceInfo());
        logger.trace("Request originator's distance: " + req.getDistance());
        pubsubmngr.getTopology().onDeliverSubscribe(req, topic);
    }

    @Test
    public void DefaultAlgorithm_isForwardingNeeded4_Test() {
        logger.trace("Entering test 4....");
        topic.setDistance(-1);
        logger.trace("This node info: " + pubsubmngr.getNodeInfo());
        logger.trace("This node distance: " + topic.getDistance());
        SubscribeRequest req = new SubscribeRequest(7000, new NodeInfo("12339", "10.69.40.111", "tiia", 9070), new NodeInfo("12342", "10.69.40.222", "tiia2", 9072), "Software developement", 100000000, 20, -1);
        logger.trace("Request originator's info: " + req.getSourceInfo());
        logger.trace("Request originator's distance: " + req.getDistance());
        pubsubmngr.getTopology().onDeliverSubscribe(req, topic);
    }

    @Test
    public void DefaultAlgorithm_onDeliverCreateTopicWithSubscribeResponse_Test() {
        logger.trace("Entering test 5....");
        pubsubmngr.DEBUG_showTopics();
        pubsubmngr.getCustomizableAlgorithm().createTopic(topic.getID(), true);
        StandardResponse res = new StandardResponse(1, PubSubConstants.RESP_SUCCESS, new NodeInfo("12398", "192.96.45.4", "source", 7890), new NodeInfo("dest"), topic.getID());
        pubsubmngr.onDeliverResponse(res);
        pubsubmngr.DEBUG_showTopics();
        pubsubmngr.getTopic(topic.getID()).DEBUG_showChildren();
    }

    @Test
    public void DefaultAlgorithm_onDeliverCreateTopicResponse_Test() {
        logger.trace("Entering test 6....");
        pubsubmngr.getCustomizableAlgorithm().createTopic("Plants", false);
        StandardResponse res = new StandardResponse(2, PubSubConstants.RESP_SUCCESS, new NodeInfo("12398", "192.96.45.4", "source", 7890), new NodeInfo("dest"), "Plants");
        pubsubmngr.onDeliverResponse(res);
        pubsubmngr.DEBUG_showTopics();
    }

    @Test
    public void DefaultAlgorithm_onDeliverCreateTopicResponse_AlreadyExists_Test() {
        logger.trace("Entering test 7....");
        pubsubmngr.getCustomizableAlgorithm().createTopic("Plants", false);
        StandardResponse res = new StandardResponse(3, PubSubConstants.RESP_ALREADYEXISTS, new NodeInfo("12398", "192.96.45.4", "source", 7890), new NodeInfo("dest"), "Plants");
        pubsubmngr.onDeliverResponse(res);
    }

    @Test
    public void DefaultAlgorithm_createTopic_AlreadyExistsLocally_Test() {
        logger.trace("Entering test 8....");
        pubsubmngr.getCustomizableAlgorithm().createTopic("Software Developement", false);
    }

    @Test
    public void DefaultAlgorithm_createTopic_DoesNotExist_Test() {
        logger.trace("Entering test 9....");
        pubsubmngr.getCustomizableAlgorithm().createTopic("Plants", true);
        StandardResponse res = new StandardResponse(4, PubSubConstants.RESP_SUCCESS, new NodeInfo("12398", "192.96.45.4", "source", 7890), new NodeInfo("dest"), "Plants");
        pubsubmngr.onDeliverResponse(res);
        pubsubmngr.DEBUG_showTopics();
        pubsubmngr.getTopic("Plants").DEBUG_showChildren();
    }

    @Test
    public void DefaultAlgorithm_onDeliverResponse_TransactionDoesNotExist_Test() {
        logger.trace("Entering test 10....");
        StandardResponse res = new StandardResponse(5, PubSubConstants.RESP_SUCCESS, new NodeInfo("12398", "192.96.45.4", "source", 7890), new NodeInfo("dest"), "Plants");
        pubsubmngr.onDeliverResponse(res);
    }

    @Test
    public void DefaultAlgorithm_onDeliverCreateTopicResponse_CustomAC_Test() {
        logger.trace("Entering test 11....");
        Topic modTopic = new Topic("Palaeontology");
        AccessControlRules ac = new AccessControlRules(modTopic);
        ac.getRule(PubSubConstants.OPERATION_SUBSCRIBE).addUser(PubSubConstants.EVENT_ALL, new Subscriber("allowed", modTopic));
        pubsubmngr.getCustomizableAlgorithm().createTopic("Palaeontology", true, ac);
        StandardResponse res = new StandardResponse(5, PubSubConstants.RESP_SUCCESS, new NodeInfo("12398", "192.96.45.4", "source", 7890), new NodeInfo("dest"), "Palaeontology");
        pubsubmngr.onDeliverResponse(res);
        pubsubmngr.DEBUG_showTopics();
        logger.trace("\nRules for the topic 'Palaeontology'\n" + pubsubmngr.getTopic("Palaeontology").getAccessControlRules());
    }

    @Test
    public void DefaultAlgorithm_onDeliverPublishTopicResponse_Accepted_Test() {
        logger.trace("Entering test 12....");
        String msg = "vivarium";
        pubsubmngr.getCustomizableAlgorithm().networkPublish("Plants", msg.getBytes());
        StandardResponse res = new StandardResponse(6, PubSubConstants.RESP_SUCCESS, new NodeInfo("12398", "192.96.45.4", "source", 7890), new NodeInfo("dest"), "Plants");
        pubsubmngr.onDeliverResponse(res);
    }

    @Test
    public void DefaultAlgorithm_onDeliverPublishTopicResponse_Error_Test() {
        logger.trace("Entering test 12....");
        String msg = "vivarium";
        pubsubmngr.getCustomizableAlgorithm().networkPublish("Plants", msg.getBytes());
        StandardResponse res = new StandardResponse(7, PubSubConstants.RESP_FORBIDDEN, new NodeInfo("12398", "192.96.45.4", "source", 7890), new NodeInfo("dest"), "Plants");
        pubsubmngr.onDeliverResponse(res);
    }

    @Test
    public void DefaultAlgorithm_onDeliverPublishTopicResponse_RemoveTopic_Test() {
        logger.trace("Entering test 13....");
        pubsubmngr.getCustomizableAlgorithm().removeTopic("Plants");
        StandardResponse res = new StandardResponse(8, PubSubConstants.RESP_SUCCESS, new NodeInfo("12398", "192.96.45.4", "source", 7890), new NodeInfo("dest"), "Plants");
        pubsubmngr.onDeliverResponse(res);
    }

    @Test
    public void DefaultAlgorithm_onDeliverPublishTopicResponse_ModifyAC_Test() {
        logger.trace("Entering test 14....");
        Topic t = new Topic("Plants");
        AccessControlRules ac = new AccessControlRules(t);
        pubsubmngr.getCustomizableAlgorithm().modifyAccessControlRules(t.getID(), ac);
        StandardResponse res = new StandardResponse(9, PubSubConstants.RESP_SUCCESS, new NodeInfo("12398", "192.96.45.4", "source", 7890), new NodeInfo("dest"), "Plants");
        pubsubmngr.onDeliverResponse(res);
    }

    @Test
    public void DefaultAlgorithm_onDeliverSubscribeResponse_TopicExistsLocally_Test() {
        logger.trace("Entering test 15....");
        pubsubmngr.getTopic("Plants").removeSubscriber("12340");
        pubsubmngr.getTopic("Plants").DEBUG_showChildren();
        pubsubmngr.getCustomizableAlgorithm().networkSubscribe("Plants");
        pubsubmngr.getTopic("Plants").DEBUG_showChildren();
    }

    @Test
    public void DefaultAlgorithm_onDeliverSubscribeResponse_Accepted_Test() {
        logger.trace("Entering test 15....");
        pubsubmngr.getCustomizableAlgorithm().networkSubscribe("Vivarium");
        StandardResponse res = new StandardResponse(10, PubSubConstants.RESP_SUCCESS, new NodeInfo("12398", "192.96.45.4", "source", 7890), new NodeInfo("dest"), "Vivarium");
        pubsubmngr.onDeliverResponse(res);
        pubsubmngr.getTopic("Vivarium").DEBUG_showChildren();
    }

    @Test
    public void DefaultAlgorithm_onDeliverSubscribeResponse_Rejected_Test() {
        logger.trace("Entering test 16....");
        pubsubmngr.removeTopic("Vivarium");
        Topic.DEBUG_showAllChildren();
        StandardResponse res = new StandardResponse(11, PubSubConstants.RESP_DOESNOTEXIST, new NodeInfo("12398", "192.96.45.4", "source", 7890), new NodeInfo("dest"), "Vivarium");
        pubsubmngr.onDeliverResponse(res);
        Topic.DEBUG_showAllChildren();
        pubsubmngr.DEBUG_showTopics();
    }

    @Test
    public void DefaultAlgorithm_onDeliverPublishRequest_ByRootInterestingEvent_Test() {
        logger.trace("Entering test 17....");
        pubsubmngr.getTopic("Software Developement").setParent(null);
        String eventMessage = "Helo ;)";
        PublishRequest req = new PublishRequest(7000, new NodeInfo("12345", "10.69.40.111", "tiia", 9070), new NodeInfo("67467", "10.69.40.222", "tiia2", 9072), "Software Developement", PubSubConstants.EVENT_CUSTOM, eventMessage.getBytes(), new User("tiia"));
        pubsubmngr.onDeliverRequest(req);
    }

    @Test
    public void DefaultAlgorithm_onDeliverPublishRequest_ByRootNotInteresting_Test() {
        logger.trace("Entering test 18....");
        InterestConditions ic = new InterestConditions(new Topic("Plants"));
        ic.getRule(PubSubConstants.OPERATION_NOTIFY).addUser(PubSubConstants.EVENT_CUSTOM, new User("interesting"));
        pubsubmngr.getCustomizableAlgorithm().networkSubscribe("Plants", ic, PubSubConstants.HISTORY_NONE);
        pubsubmngr.getTopic("Plants").setParent(null);
        String eventMessage = "Helo ;)";
        PublishRequest req = new PublishRequest(7000, new NodeInfo("12345", "10.69.40.111", "tiia", 9070), new NodeInfo("67467", "10.69.40.222", "tiia2", 9072), "Plants", PubSubConstants.EVENT_CUSTOM, eventMessage.getBytes(), new User("tiia"));
        pubsubmngr.onDeliverRequest(req);
    }

    @Test
    public void DefaultAlgorithm_onICModification_Test() {
        logger.trace("Entering test 18....");
        pubsubmngr.DEBUG_showTopics();
        pubsubmngr.getTopic("Palaeontology").DEBUG_showChildren();
        logger.trace(pubsubmngr.getTopic("Palaeontology").getChild("12340").getSubscription("Palaeontology").getInterestConditions());
        pubsubmngr.getTopic("Plants").DEBUG_showChildren();
        logger.trace(pubsubmngr.getTopic("Plants").getChild("12340").getSubscription("Plants").getInterestConditions());
    }

    @Test
    public void DefaultAlgorithm_onDeliverPublishRequest_TwoChildrenWithDifferentIC_Test() {
        logger.trace("Entering test 19....");
        SubscribeRequest req = new SubscribeRequest(7000, new NodeInfo("87654", "10.69.40.111", "tiia", 9070), new NodeInfo("56844", "10.69.40.222", "tiia2", 9072), "Plants", 100000000, -1, 30);
        pubsubmngr.onDeliverRequest(req);
        String eventMessage = "Helo again ;)";
        PublishRequest preq = new PublishRequest(7000, new NodeInfo("12345", "10.69.40.111", "tiia", 9070), new NodeInfo("67467", "10.69.40.222", "tiia2", 9072), "Plants", PubSubConstants.EVENT_CUSTOM, eventMessage.getBytes(), new User("tiia"));
        pubsubmngr.onDeliverRequest(preq);
    }

    @Test
    public void DefaultAlgorithm_unsubscribe_RootMoreSubscribers_Test() {
        logger.trace("Entering test 20....");
        pubsubmngr.getTopic("Plants").DEBUG_showChildren();
        pubsubmngr.getCustomizableAlgorithm().networkUnsubscribe("Plants");
        pubsubmngr.getTopic("Plants").DEBUG_showChildren();
    }

    @Test
    public void DefaultAlgorithm_unsubscribe_RootNoMoreSubscribers_Test() {
        logger.trace("Entering test 21....");
        pubsubmngr.getTopic("Software Developement").DEBUG_showChildren();
        pubsubmngr.getCustomizableAlgorithm().networkUnsubscribe("Software Developement");
        pubsubmngr.getTopic("Software Developement").DEBUG_showChildren();
    }

    @Test
    public void DefaultAlgorithm_unsubscribe_NotRootNoMoreSubscribers_Test() {
        logger.trace("Entering test 22....");
        pubsubmngr.DEBUG_showTopics();
        Topic.DEBUG_showParents();
        pubsubmngr.getTopic("Palaeontology").DEBUG_showChildren();
        pubsubmngr.getCustomizableAlgorithm().networkUnsubscribe("Palaeontology");
        Topic.DEBUG_showAllChildren();
        Topic.DEBUG_showParents();
    }

    @Test
    public void DefaultAlgorithm_unsubscribe_NotRootMoreSubscribers_Test() {
        logger.trace("Entering test 23....");
    }

    @Test
    public void DefaultAlgorithm_unsubscribe_NoSuchSubscriber_Test() {
        logger.trace("Entering test 24....");
        pubsubmngr.getTopic("Plants").DEBUG_showChildren();
        pubsubmngr.getCustomizableAlgorithm().networkUnsubscribe("Plants");
        Topic.DEBUG_showAllChildren();
        pubsubmngr.DEBUG_showTopics();
    }
}
