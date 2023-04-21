package com.hk.svr.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.bean.Feed;
import com.hk.bean.User;
import com.hk.frame.util.DataUtil;
import com.hk.svr.FeedService;
import com.hk.svr.UserService;

public class FeedProcessor {

    @Autowired
    private FeedService feedService;

    @Autowired
    private UserService userService;

    private static Comparator<Feed> feedSortComparator = new Comparator<Feed>() {

        public int compare(Feed o1, Feed o2) {
            if (o1.getFeedId() > o2.getFeedId()) {
                return -1;
            }
            return 1;
        }
    };

    public List<Feed> getFeedListByFeedType(byte feedType, boolean buildUser, int begin, int size) {
        List<Feed> list = feedService.getFeedListByFeedType(feedType, begin, size);
        if (buildUser) {
            List<Long> idList = new ArrayList<Long>();
            for (Feed o : list) {
                idList.add(o.getUserId());
            }
            Map<Long, User> usermap = userService.getUserMapInId(idList);
            for (Feed o : list) {
                o.setUser(usermap.get(o.getUserId()));
            }
        }
        return list;
    }

    public List<Feed> getFeedListByCityIdForIndex(int cityId, int begin, int size) {
        List<Feed> list = new ArrayList<Feed>();
        List<Feed> badgefeedlist = this.feedService.getFeedListByFeedTypeAndCityid(Feed.FEEDTYPE_GETBADGE, cityId, begin, size);
        List<Feed> tipfeedlist = this.feedService.getFeedListByFeedTypeAndCityid(Feed.FEEDTYPE_WRITETIPS, cityId, begin, size);
        List<Feed> venuefeedlist = this.feedService.getFeedListByFeedTypeAndCityid(Feed.FEEDTYPE_CREATEVENUE, cityId, begin, size);
        List<Feed> mayorfeedlist = this.feedService.getFeedListByFeedTypeAndCityid(Feed.FEEDTYPE_BECOME_MAYOR, cityId, begin, size);
        List<Feed> invitefeedlist = this.feedService.getFeedListByFeedTypeAndCityid(Feed.FEEDTYPE_INVITE, cityId, begin, size);
        list.addAll(badgefeedlist);
        list.addAll(tipfeedlist);
        list.addAll(venuefeedlist);
        list.addAll(mayorfeedlist);
        list.addAll(invitefeedlist);
        Collections.sort(list, feedSortComparator);
        list = DataUtil.subList(list, 0, 20);
        return list;
    }

    public List<Feed> getFeedListForIndex(int begin, int size) {
        List<Feed> list = new ArrayList<Feed>();
        List<Feed> badgefeedlist = this.feedService.getFeedListByFeedType(Feed.FEEDTYPE_GETBADGE, begin, size);
        List<Feed> tipfeedlist = this.feedService.getFeedListByFeedType(Feed.FEEDTYPE_WRITETIPS, begin, size);
        List<Feed> venuefeedlist = this.feedService.getFeedListByFeedType(Feed.FEEDTYPE_CREATEVENUE, begin, size);
        List<Feed> mayorfeedlist = this.feedService.getFeedListByFeedType(Feed.FEEDTYPE_BECOME_MAYOR, begin, size);
        List<Feed> invitefeedlist = this.feedService.getFeedListByFeedType(Feed.FEEDTYPE_INVITE, begin, size);
        list.addAll(badgefeedlist);
        list.addAll(tipfeedlist);
        list.addAll(venuefeedlist);
        list.addAll(mayorfeedlist);
        list.addAll(invitefeedlist);
        Collections.sort(list, feedSortComparator);
        list = DataUtil.subList(list, 0, 20);
        return list;
    }
}
