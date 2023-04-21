package ch.iserver.ace.collaboration;

/**
 * Request passed to the application layer whenever another user tries to
 * join a published document. The method
 * {@link ch.iserver.ace.collaboration.PublishedSessionCallback#joinRequest(JoinRequest)}
 * is invoked passing in an object implementing this interface. The request
 * can either be accepted or rejected.
 */
public interface JoinRequest {

    /**
	 * Rejection code specifying that the request was rejected by the publisher.
	 */
    int REJECTED = 1;

    /**
	 * Rejection code specifying that the request was rejected because the
	 * joining user is on the session black list.
	 */
    int BLACKLISTED = 2;

    /**
	 * Rejection code specifying that the request was rejected because the 
	 * joining user is unknown.
	 */
    int UNKNOWN_USER = 3;

    /**
	 * Rejection code specifying that the request was rejected because the 
	 * document server is shutting down.
	 */
    int SHUTDOWN = 4;

    /**
	 * Rejection code specifying that the request was rejected because a join
	 * request from the same user was received earlier.
	 */
    int IN_PROGRESS = 5;

    /**
	 * Rejection code specifying that the user trying to join is already part
	 * of the session.
	 */
    int JOINED = 6;

    /**
	 * Gets the user that whishes to join the published document.
	 * 
	 * @return the user trying to join
	 */
    RemoteUser getUser();

    /**
	 * Accepts the join request. The other user is then officially accepted in
	 * the session and becomes a full member of it.
	 */
    void accept();

    /**
	 * Rejects the join request. The other user cannot get the document or
	 * send requests to it.
	 */
    void reject();
}
