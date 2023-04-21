package com.aipo.social.opensocial.spi;

import java.util.Set;
import java.util.concurrent.Future;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.UserId;
import com.aipo.social.opensocial.model.Activity;

/**
 * 
 */
public interface ActivityService {

    Future<RestfulCollection<Activity>> getActivities(Set<UserId> userIds, GroupId groupId, String appId, Set<String> fields, CollectionOptions options, SecurityToken token) throws ProtocolException;

    Future<RestfulCollection<Activity>> getActivities(UserId userId, GroupId groupId, String appId, Set<String> fields, CollectionOptions options, Set<String> activityIds, SecurityToken token) throws ProtocolException;

    Future<Activity> getActivity(UserId userId, GroupId groupId, String appId, Set<String> fields, String activityId, SecurityToken token) throws ProtocolException;

    Future<Void> deleteActivities(UserId userId, GroupId groupId, String appId, Set<String> activityIds, SecurityToken token) throws ProtocolException;

    Future<Void> createActivity(UserId userId, GroupId groupId, String appId, Set<String> fields, Activity activity, SecurityToken token) throws ProtocolException;
}
