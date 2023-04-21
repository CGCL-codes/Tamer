package org.ikross.twitter.adapter;

import org.ikross.twitter.connector.IConnector;
import org.ikross.twitter.exception.ConnectorException;

public interface IAdapter {

    public IConnector createPublicTimeLineTask() throws ConnectorException;

    public IConnector createFriendsTimeLineTask() throws ConnectorException;

    public IConnector createUserTimeLineTask() throws ConnectorException;

    public IConnector createGetUpdateTask(String idUpdate) throws ConnectorException;

    public IConnector createUpdateTask(String status) throws ConnectorException;

    public IConnector createGetRepliesTask() throws ConnectorException;

    public IConnector createDestroyUpdateTask(String idUpdate) throws ConnectorException;

    public IConnector createGetFriendsTask() throws ConnectorException;

    public IConnector createGetFollowersTask() throws ConnectorException;

    public IConnector createGetFeaturedUsersTask() throws ConnectorException;

    public IConnector createGetUserTask(String id) throws ConnectorException;

    public IConnector createGetReceivedDirectMessagesTask() throws ConnectorException;

    public IConnector createGetSentDirectMessagesTask() throws ConnectorException;

    public IConnector createSendDirectMessageTask(String recipient, String message) throws ConnectorException;

    public IConnector createDestroyDirectMessage(String idDirectMessage) throws ConnectorException;

    public IConnector createNewFriendshipTask(String idUser) throws ConnectorException;

    public IConnector createDestroyFriendshipTask(String idUser) throws ConnectorException;

    public IConnector createConfirmFriendshipTask(String idUserA, String idUserB) throws ConnectorException;

    public IConnector createVerifyCredentialsTask() throws ConnectorException;

    public IConnector createEndSessionTask() throws ConnectorException;

    public IConnector createGetArchiveTask() throws ConnectorException;

    public IConnector createUpdateLocationGETTask(String location) throws ConnectorException;

    public IConnector createUpdateLocationPOSTTask(String location) throws ConnectorException;

    public IConnector createUpdateDeliveryDeviceTask(String deliveryDevice) throws ConnectorException;

    public IConnector createRateLimitStatusTask() throws ConnectorException;

    public IConnector createGetFavoritesTask() throws ConnectorException;

    public IConnector createSetFavoriteTask(String idUpdate) throws ConnectorException;

    public IConnector createDestroyFavoriteTask(String idUpdate) throws ConnectorException;

    public IConnector createFollowUserTask(String idUser) throws ConnectorException;

    public IConnector createUnfollowUserTask(String idUser) throws ConnectorException;

    public IConnector createBlockUserTask(String idUser) throws ConnectorException;

    public IConnector createUnblockUserTask(String idUser) throws ConnectorException;

    public IConnector createTestTask() throws ConnectorException;

    public IConnector createDowntimeScheduleTask() throws ConnectorException;
}
