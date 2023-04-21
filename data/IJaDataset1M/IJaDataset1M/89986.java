package com.friendfeed.api;

public class FriendFeedServiceException extends RuntimeException {

    public FriendFeedServiceException(String message) {
        super(message);
    }

    public FriendFeedServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
