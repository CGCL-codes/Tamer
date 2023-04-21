package com.sns.service;

import com.sns.pojo.Activity;
import com.sns.pojo.ActivityComment;

public interface ActivityService {

    public void addActivity(Activity activity);

    public void updateActivity(Activity activaty);

    public void deleteActivity(Activity activity);

    public void addActivityComment(ActivityComment activityComment);
}
