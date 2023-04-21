package com.coyousoft.wangyu.constant;

import static org.sysolar.util.Constants.LS;

public final class Sql {

    public static final class BbsPostSql {

        public static final String create = "insert into BBS_POST(POST_ID,OWNER_ID,POSTER_ID,POST_TYPE,POST_TEXT,POST_CDATE)values(?,?,?,?,?,?)";

        public static final String remove = "delete from BBS_POST where POST_ID=?";

        public static final String fetch = "select * from BBS_POST where POST_ID=?";

        public static final String export = "select * from BBS_POST order by POST_ID desc limit ?,?";

        public static final String fetchList4User = "select T3.USER_NICKNAME,T3.PHOTO_STATUS,T3.USER_ID,T2.POST_ID,T2.POSTER_ID,T2.POST_TYPE,T2.POST_TEXT,T2.POST_CDATE,T5.USER_NICKNAME REPLIER_NICKNAME,T5.PHOTO_STATUS REPLIER_PHOTO_STATUS,T4.REPLY_ID,T4.REPLIER_ID,T4.REPLY_TEXT,T4.REPLY_CDATE from(select POST_ID from BBS_POST where OWNER_ID=? order by POST_ID desc limit ?,?)T1 inner join BBS_POST T2 on T2.POST_ID=T1.POST_ID inner join WANGYU_USER T3 on T2.POSTER_ID=T3.USER_ID left join BBS_REPLY T4 on T4.POST_ID=T1.POST_ID left join WANGYU_USER T5 on T5.USER_ID=T4.REPLIER_ID order by T2.POST_ID desc,T4.REPLY_ID asc";

        public static final String fetchCount4User = "select count(T1.POST_ID)from BBS_POST T1 where T1.OWNER_ID=?";

        public static final String fetchList4People = "select T3.USER_NICKNAME,T3.PHOTO_STATUS,T3.USER_ID,T2.POST_ID,T2.POSTER_ID,T2.POST_TYPE,T2.POST_TEXT,T2.POST_CDATE,T5.USER_NICKNAME REPLIER_NICKNAME,T5.PHOTO_STATUS REPLIER_PHOTO_STATUS,T4.REPLY_ID,T4.REPLIER_ID,T4.REPLY_TEXT,T4.REPLY_CDATE from(select POST_ID from BBS_POST where OWNER_ID=? and(POST_TYPE >=2 or POSTER_ID=?)order by POST_ID desc limit ?,?)T1 inner join BBS_POST T2 on T2.POST_ID=T1.POST_ID inner join WANGYU_USER T3 on T2.POSTER_ID=T3.USER_ID left join BBS_REPLY T4 on T4.POST_ID=T1.POST_ID left join WANGYU_USER T5 on T5.USER_ID=T4.REPLIER_ID order by T2.POST_ID desc,T4.REPLY_ID asc";

        public static final String fetchCount4People = "select count(T1.POST_ID)from BBS_POST T1 where T1.OWNER_ID=? and(POST_TYPE >=2 or POSTER_ID=?)";

        public static final String removeByPostId$OwnerId = "delete from BBS_POST where POST_ID=? and OWNER_ID=?";
    }

    public static final class BbsReplySql {

        public static final String create = "insert into BBS_REPLY(REPLY_ID,POST_ID,REPLIER_ID,REPLY_TEXT,REPLY_CDATE)values(?,?,?,?,?)";

        public static final String remove = "delete from BBS_REPLY where REPLY_ID=?";

        public static final String fetch = "select * from BBS_REPLY where REPLY_ID=?";

        public static final String export = "select * from BBS_REPLY order by REPLY_ID desc limit ?,?";

        public static final String removeByPost = "delete from BBS_REPLY where POST_ID=?";

        public static final String fetchList4User = "select T3.USER_NICKNAME,T3.PHOTO_STATUS,T3.USER_ID,T2.POST_ID,T2.POSTER_ID,T2.POST_TYPE,T2.POST_TEXT,T2.POST_CDATE,T2.OWNER_ID,T5.USER_NICKNAME REPLIER_NICKNAME,T5.PHOTO_STATUS REPLIER_PHOTO_STATUS,T4.REPLY_ID,T4.REPLIER_ID,T4.REPLY_TEXT,T4.REPLY_CDATE from(select POST_ID from BBS_POST where POSTER_ID=? order by POST_ID desc limit ?,?)T1 inner join BBS_POST T2 on T2.POST_ID=T1.POST_ID inner join WANGYU_USER T3 on T2.OWNER_ID=T3.USER_ID left join BBS_REPLY T4 on T4.POST_ID=T1.POST_ID left join WANGYU_USER T5 on T5.USER_ID=T4.REPLIER_ID order by T2.POST_ID desc,T4.REPLY_ID asc";

        public static final String fetchCount4User = "select count(T1.POST_ID)from BBS_POST T1 where T1.POSTER_ID=?";

        public static final String fetchList4People = "select T3.USER_NICKNAME,T3.PHOTO_STATUS,T3.USER_ID,T2.POST_ID,T2.POSTER_ID,T2.POST_TYPE,T2.POST_TEXT,T2.POST_CDATE,T2.OWNER_ID,T5.USER_NICKNAME REPLIER_NICKNAME,T5.PHOTO_STATUS REPLIER_PHOTO_STATUS,T4.REPLY_ID,T4.REPLIER_ID,T4.REPLY_TEXT,T4.REPLY_CDATE from(select POST_ID from BBS_POST where POSTER_ID=? and(POST_TYPE >=2 or OWNER_ID=?)order by POST_ID desc limit ?,?)T1 inner join BBS_POST T2 on T2.POST_ID=T1.POST_ID inner join WANGYU_USER T3 on T2.OWNER_ID=T3.USER_ID left join BBS_REPLY T4 on T4.POST_ID=T1.POST_ID left join WANGYU_USER T5 on T5.USER_ID=T4.REPLIER_ID order by T2.POST_ID desc,T4.REPLY_ID asc";

        public static final String fetchCount4People = "select count(T1.POST_ID)from BBS_POST T1 where T1.POSTER_ID=? and(POST_TYPE >=2 or OWNER_ID=?)";
    }

    public static final class CoreUrlSql {

        public static final String create = "insert into CORE_URL(CORE_URL_ID,DOMAIN_ID,CORE_URL_NAME,CORE_URL_HREF,CORE_URL_DESC,CORE_URL_STATUS,CORE_URL_INFO_STATUS,CORE_URL_STORE_NUM,CORE_URL_SHARE_NUM,CORE_URL_POST_NUM,CORE_URL_CLICK_NUM,CORE_URL_DIG_NUM,CORE_URL_SCORE,CORE_URL_HREF_TYPE,CORE_URL_IMG_TYPE,FIRST_STORE_USER_ID,FIRST_STORE_DATE,LAST_STORE_USER_ID,LAST_STORE_DATE,FIRST_SHARE_USER_ID,FIRST_SHARE_DATE,LAST_SHARE_DATE,LAST_SHARE_USER_ID,CORE_URL_CDATE,CORE_URL_UDATE)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        public static final String remove = "delete from CORE_URL where CORE_URL_ID=?";

        public static final String fetch = "select * from CORE_URL where CORE_URL_ID=?";

        public static final String export = "select * from CORE_URL order by CORE_URL_ID desc limit ?,?";

        public static final String updateClickNum = "update CORE_URL set CORE_URL_CLICK_NUM=(CORE_URL_CLICK_NUM + ?)where CORE_URL_ID=?";

        public static final String fetchByHref = "select T1.CORE_URL_ID,T1.DOMAIN_ID,T1.CORE_URL_NAME,T1.CORE_URL_HREF,T1.CORE_URL_DESC,T1.FIRST_STORE_USER_ID,T2.DOMAIN_STATUS,T2.ICO_STATUS,T2.ICO_URL from CORE_URL T1 inner join DOMAIN T2 on T1.DOMAIN_ID=T2.DOMAIN_ID where T1.CORE_URL_HREF=?";

        public static final String fetchListByInfoStatus = "select CORE_URL_ID,CORE_URL_NAME,DOMAIN_ID,CORE_URL_HREF,CORE_URL_HREF_TYPE from CORE_URL where CORE_URL_INFO_STATUS=? and DOMAIN_ID !=2 order by CORE_URL_ID desc limit 0,?";

        public static final String updateCoreUrlInfoById = "update CORE_URL set CORE_URL_NAME=?,CORE_URL_DESC=?,CORE_URL_STORE_NUM=(select count(USER_URL_ID)from USER_URL where CORE_URL_ID=?),CORE_URL_INFO_STATUS=?,CORE_URL_UDATE=? where CORE_URL_ID=?";

        public static final String fetchList4Image = "select CORE_URL_ID,CORE_URL_HREF from CORE_URL where CORE_URL_IMG_TYPE=0 order by CORE_URL_STORE_NUM desc,CORE_URL_CLICK_NUM desc,CORE_URL_ID desc limit 0,?";

        public static final String updateCoreUrlImgTypeById = "update CORE_URL set CORE_URL_IMG_TYPE=? where CORE_URL_ID=?";

        public static final String fetchHotUrlList;

        static {
            StringBuilder buffer = new StringBuilder(512);
            buffer.append("select T1.CORE_URL_ID").append(LS);
            buffer.append(",T1.CORE_URL_HREF").append(LS);
            buffer.append(",T1.DOMAIN_ID").append(LS);
            buffer.append(",T1.CORE_URL_NAME").append(LS);
            buffer.append(",T1.CORE_URL_DESC").append(LS);
            buffer.append(",T1.CORE_URL_IMG_TYPE").append(LS);
            buffer.append(",T1.FIRST_STORE_DATE").append(LS);
            buffer.append(",T1.LAST_STORE_DATE").append(LS);
            buffer.append(",T2.ICO_STATUS").append(LS);
            buffer.append(",T2.ICO_URL").append(LS);
            buffer.append(",T3.USER_ID").append(LS);
            buffer.append(",T3.PHOTO_STATUS").append(LS);
            buffer.append(",T3.USER_NICKNAME").append(LS);
            buffer.append("from CORE_URL T1").append(LS);
            buffer.append("inner join DOMAIN T2 on T1.DOMAIN_ID = T2.DOMAIN_ID").append(LS);
            buffer.append("inner join WANGYU_USER T3 on T1.LAST_STORE_USER_ID = T3.USER_ID").append(LS);
            buffer.append("inner join CORE_URL_TAG T4 on T1.CORE_URL_ID = T4.CORE_URL_ID -- @5").append(LS);
            buffer.append("where T2.DOMAIN_STATUS = 1").append(LS);
            buffer.append("and T2.DOMAIN_TYPE != 2").append(LS);
            buffer.append("and T3.USER_TYPE >= 0").append(LS);
            buffer.append("and T1.CORE_URL_STATUS > 0").append(LS);
            buffer.append("and T4.TAG_NAME like ?         -- @1").append(LS);
            buffer.append("order by").append(LS);
            buffer.append("T1.LAST_STORE_DATE desc, T1.CORE_URL_STORE_NUM desc, T1.CORE_URL_CLICK_NUM desc -- @2").append(LS);
            buffer.append("T1.CORE_URL_STORE_NUM desc, T1.LAST_STORE_DATE desc, T1.CORE_URL_CLICK_NUM desc -- @3").append(LS);
            buffer.append("T1.CORE_URL_CLICK_NUM desc, T1.LAST_STORE_DATE desc, T1.CORE_URL_STORE_NUM desc -- @4").append(LS);
            buffer.append("limit ?,?").append(LS);
            fetchHotUrlList = buffer.toString();
            buffer.delete(0, buffer.length());
        }

        public static final String fetchHotUrlCount;

        static {
            StringBuilder buffer = new StringBuilder(512);
            buffer.append("select count(T1.CORE_URL_ID)").append(LS);
            buffer.append("from CORE_URL T1").append(LS);
            buffer.append("inner join DOMAIN T2 on T1.DOMAIN_ID = T2.DOMAIN_ID").append(LS);
            buffer.append("inner join WANGYU_USER T3 on T1.LAST_STORE_USER_ID = T3.USER_ID").append(LS);
            buffer.append("inner join CORE_URL_TAG T4 on T1.CORE_URL_ID = T4.CORE_URL_ID -- @1").append(LS);
            buffer.append("where T2.DOMAIN_STATUS = 1").append(LS);
            buffer.append("and T3.USER_TYPE >= 0").append(LS);
            buffer.append("and T1.CORE_URL_STATUS > 0").append(LS);
            buffer.append("and T4.TAG_NAME like ? -- @2").append(LS);
            fetchHotUrlCount = buffer.toString();
            buffer.delete(0, buffer.length());
        }

        public static final String updateFirstStore = "update CORE_URL set FIRST_STORE_USER_ID=?,FIRST_STORE_DATE=?,LAST_STORE_USER_ID=?,LAST_STORE_DATE=? where CORE_URL_ID=?";

        public static final String updateLastStore = "update CORE_URL set LAST_STORE_USER_ID=?,LAST_STORE_DATE=? where CORE_URL_ID=?";

        public static final String updateName$Des = "update CORE_URL set CORE_URL_NAME=?,CORE_URL_DESC=?,CORE_URL_INFO_STATUS=?,CORE_URL_UDATE=? where CORE_URL_ID=?";

        public static final String updateCoreUrlInfoStatus = "update CORE_URL set CORE_URL_INFO_STATUS=?,CORE_URL_STORE_NUM=(select count(USER_URL_ID)from USER_URL where CORE_URL_ID=?)where CORE_URL_ID=?";

        public static final String updateCoreUrlStoreNumById = "update CORE_URL set CORE_URL_STORE_NUM=(select count(USER_URL_ID)from USER_URL where CORE_URL_ID=?)where CORE_URL_ID=?";
    }

    public static final class CoreUrlTagSql {

        public static final String create = "insert into CORE_URL_TAG(TAG_ID,CORE_URL_ID,TAG_NAME)values(?,?,?)";

        public static final String remove = "delete from CORE_URL_TAG where TAG_ID=?";

        public static final String fetch = "select * from CORE_URL_TAG where TAG_ID=?";

        public static final String export = "select * from CORE_URL_TAG order by TAG_ID desc limit ?,?";
    }

    public static final class CoyouSiteSql {

        public static final String create = "insert into COYOU_SITE(SITE_ID,SITE_HREF,SITE_NAME,SITE_CDATE,SITE_UDATE)values(?,?,?,?,?)";

        public static final String remove = "delete from COYOU_SITE where SITE_ID=?";

        public static final String fetch = "select * from COYOU_SITE where SITE_ID=?";

        public static final String export = "select * from COYOU_SITE order by SITE_ID desc limit ?,?";
    }

    public static final class DomainSql {

        public static final String create = "insert into DOMAIN(DOMAIN_ID,TOP_DOMAIN_ID,DOMAIN_HREF,DOMAIN_RANK,DOMAIN_TYPE,DOMAIN_STATUS,DOMAIN_NAME,ICO_STATUS,ICO_URL,DOMAIN_CDATE,DOMAIN_UDATE)values(?,?,?,?,?,?,?,?,?,?,?)";

        public static final String remove = "delete from DOMAIN where DOMAIN_ID=?";

        public static final String fetch = "select * from DOMAIN where DOMAIN_ID=?";

        public static final String export = "select * from DOMAIN order by DOMAIN_ID desc limit ?,?";

        public static final String fetchByHref = "select * from DOMAIN where DOMAIN_HREF=?";

        public static final String updateDomainNameById = "update DOMAIN set DOMAIN_NAME=? where DOMAIN_ID=?";

        public static final String fetchList4IcoByStatus = "select DOMAIN_ID,DOMAIN_RANK,DOMAIN_HREF from DOMAIN where ICO_STATUS=? and DOMAIN_TYPE=1 order by DOMAIN_ID desc limit 0,?";

        public static final String updateIcoStatus = "update DOMAIN set ICO_STATUS=?,ICO_URL=? where DOMAIN_ID=?";

        public static final String fetchListByStatus = "select DOMAIN_ID,DOMAIN_NAME,DOMAIN_HREF,DOMAIN_STATUS,DOMAIN_RANK,DOMAIN_CDATE from DOMAIN where DOMAIN_STATUS=? order by DOMAIN_ID desc limit 0,?";

        public static final String fetchCountByStatus = "select count(DOMAIN_ID)from DOMAIN where DOMAIN_STATUS=?";

        public static final String updateStatus = "update DOMAIN set DOMAIN_STATUS=? where DOMAIN_ID=?";

        public static final String fetchList4Ico4Admin = "select DOMAIN_ID,DOMAIN_NAME,DOMAIN_HREF,DOMAIN_STATUS,DOMAIN_RANK,DOMAIN_CDATE,ICO_STATUS from DOMAIN where ICO_STATUS=? order by DOMAIN_ID asc limit 0,?";

        public static final String fetchCountByIcoStatus = "select count(DOMAIN_ID)from DOMAIN where ICO_STATUS=?";

        public static final String updateIcoUrl = "update DOMAIN set ICO_URL=?,ICO_STATUS=? where DOMAIN_ID=?";
    }

    public static final class FriendApplySql {

        public static final String create = "insert into FRIEND_APPLY(APPLY_ID,OWNER_ID,APPLIER_ID,APPLY_STATUS,APPLY_CDATE,APPLY_MEMO)values(?,?,?,?,?,?)";

        public static final String remove = "delete from FRIEND_APPLY where APPLY_ID=?";

        public static final String fetch = "select * from FRIEND_APPLY where APPLY_ID=?";

        public static final String export = "select * from FRIEND_APPLY order by APPLY_ID desc limit ?,?";

        public static final String updateMemo$CdateByOwnerId$ApplienId = "update FRIEND_APPLY set APPLY_MEMO=?,APPLY_CDATE=? where OWNER_ID=? and APPLIER_ID=?";

        public static final String fetchListByOwnerId = "select T1.APPLY_ID,T1.OWNER_ID,T1.APPLIER_ID,T1.APPLY_MEMO,T1.APPLY_CDATE,T2.USER_ID,T2.USER_NICKNAME,T2.PHOTO_STATUS,T2.URL_NUM,T2.VISITED_NUM,T2.WEB_NUM from FRIEND_APPLY T1 inner join WANGYU_USER T2 on T1.APPLIER_ID=T2.USER_ID where T1.OWNER_ID=? and T1.APPLY_STATUS=0 limit ?,?";

        public static final String fetchCountByOwnerId = "select count(APPLY_ID)from FRIEND_APPLY where OWNER_ID=?";

        public static final String removeByOwnerId$ApplierId = "delete from FRIEND_APPLY where OWNER_ID=? and APPLIER_ID=?";
    }

    public static final class GenericSql {

        public static final String fetchLastInsertId = "select @@IDENTITY";
    }

    public static final class GoldUserSql {

        public static final String create = "insert into GOLD_USER(USER_ID,USER_NAME,USER_PASS,USER_MAIL,MAIL_STATUS,USER_QQ,USER_MSN,USER_PHONE,USER_ADDRESS,USER_CITY_CODE,USER_CDATE,USER_UDATE)values(?,?,?,?,?,?,?,?,?,?,?,?)";

        public static final String remove = "delete from GOLD_USER where USER_ID=?";

        public static final String fetch = "select * from GOLD_USER where USER_ID=?";

        public static final String export = "select * from GOLD_USER order by USER_ID desc limit ?,?";

        public static final String fetchByUsername$Password = "select * from GOLD_USER where USER_NAME=? and USER_PASS=?";

        public static final String fetchByUserName = "select * from GOLD_USER where USER_NAME=?";

        public static final String fetchByUserMail = "select * from GOLD_USER where USER_MAIL=?";

        public static final String updateUserPass = "update GOLD_USER set USER_PASS=?,USER_UDATE=? where USER_ID=?";

        public static final String fetchEmailByUserName = "select USER_MAIL from GOLD_USER where USER_NAME=?";
    }

    public static final class MailLinkSql {

        public static final String create = "insert into MAIL_LINK(LINK_ID,USER_ID,LINK_TYPE,LINK_CODE,IS_CONFIRMED,LINK_CDATE,LINK_UDATE)values(?,?,?,?,?,?,?)";

        public static final String remove = "delete from MAIL_LINK where LINK_ID=?";

        public static final String fetch = "select * from MAIL_LINK where LINK_ID=?";

        public static final String export = "select * from MAIL_LINK order by LINK_ID desc limit ?,?";

        public static final String fetchByLinkCode = "select * from MAIL_LINK where LINK_CODE=?";

        public static final String updateIsConfirmed = "update MAIL_LINK set IS_CONFIRMED=?,LINK_UDATE=? where LINK_ID=?";
    }

    public static final class NoticeCenterSql {

        public static final String create = "insert into NOTICE_CENTER(NOTICE_ID,USER_ID,NOTICE_TYPE,NOTICE_STATUS,NOTICE_CDATE)values(?,?,?,?,?)";

        public static final String remove = "delete from NOTICE_CENTER where NOTICE_ID=?";

        public static final String fetch = "select * from NOTICE_CENTER where NOTICE_ID=?";

        public static final String export = "select * from NOTICE_CENTER order by NOTICE_ID desc limit ?,?";

        public static final String fetchNoticeCount = "select count(NOTICE_ID)from NOTICE_CENTER where USER_ID=? and NOTICE_TYPE=? and NOTICE_STATUS=0";

        public static final String removeByType = "delete from NOTICE_CENTER where NOTICE_TYPE=? and USER_ID=?";
    }

    public static final class SiteClickLogSql {

        public static final String create = "insert into SITE_CLICK_LOG(LOG_ID,SITE_ID,IP,REFER,PATH,UA,CDATE)values(?,?,?,?,?,?,?)";

        public static final String remove = "delete from SITE_CLICK_LOG where LOG_ID=?";

        public static final String fetch = "select * from SITE_CLICK_LOG where LOG_ID=?";

        public static final String export = "select * from SITE_CLICK_LOG order by LOG_ID desc limit ?,?";

        public static final String fetchDetailList = "select T1.CDATE,T1.LAST_DATE,T1.CLICK_NUM,T1.IP,T1.REFER,T2.PATH from(select min(CDATE)CDATE,max(CDATE)LAST_DATE,count(LOG_ID)CLICK_NUM,IP,REFER,min(LOG_ID)MIN_LOG_ID from SITE_CLICK_LOG where SITE_ID=? and CDATE >=? and CDATE < ? group by IP,REFER)T1 inner join SITE_CLICK_LOG T2 on T1.MIN_LOG_ID=T2.LOG_ID order by LAST_DATE desc limit 0,?";

        public static final String fetchCountByIp = "select count(distinct IP)from SITE_CLICK_LOG where SITE_ID=? and CDATE >=? and CDATE < ?";
    }

    public static final class SkinCatSql {

        public static final String create = "insert into SKIN_CAT(CAT_ID,CAT_NAME,SKIN_NUM,CAT_CDATE,CAT_UDATE)values(?,?,?,?,?)";

        public static final String remove = "delete from SKIN_CAT where CAT_ID=?";

        public static final String fetch = "select * from SKIN_CAT where CAT_ID=?";

        public static final String export = "select * from SKIN_CAT order by CAT_ID desc limit ?,?";

        public static final String fetchList = "select CAT_ID,CAT_NAME,SKIN_NUM from SKIN_CAT";
    }

    public static final class UrlClickLogSql {

        public static final String create = "insert into URL_CLICK_LOG(LOG_ID,CORE_URL_ID,DOMAIN_ID,USER_URL_ID,USER_ID,USER_STATUS,OWNER_ID,CLICK_FROM,CLICK_DATE,LOG_CDATE)values(?,?,?,?,?,?,?,?,?,?)";

        public static final String remove = "delete from URL_CLICK_LOG where LOG_ID=?";

        public static final String fetch = "select * from URL_CLICK_LOG where LOG_ID=?";

        public static final String export = "select * from URL_CLICK_LOG order by LOG_ID desc limit ?,?";
    }

    public static final class UserFriendSql {

        public static final String create = "insert into USER_FRIEND(USER_FRIEND_ID,GROUP_ID,OWNER_ID,FRIEND_ID,FRIEND_MEMO,FRIEND_CDATE)values(?,?,?,?,?,?)";

        public static final String remove = "delete from USER_FRIEND where USER_FRIEND_ID=?";

        public static final String fetch = "select * from USER_FRIEND where USER_FRIEND_ID=?";

        public static final String export = "select * from USER_FRIEND order by USER_FRIEND_ID desc limit ?,?";

        public static final String fetchByOwnerId$FriendId = "select USER_FRIEND_ID from USER_FRIEND where OWNER_ID=? and FRIEND_ID=?";

        public static final String moveUser2DefGroup = "update USER_FRIEND set GROUP_ID=(select GROUP_ID from USER_FRIEND_GROUP where USER_ID=? and MAKE_TYPE=0)where GROUP_ID=? and OWNER_ID=?";

        public static final String updateGroup = "update USER_FRIEND set GROUP_ID=? where USER_FRIEND_ID=?";

        public static final String fetchList;

        static {
            StringBuilder buffer = new StringBuilder(512);
            buffer.append("select T1.USER_FRIEND_ID").append(LS);
            buffer.append(",T1.GROUP_ID").append(LS);
            buffer.append(",T1.OWNER_ID").append(LS);
            buffer.append(",T1.FRIEND_ID").append(LS);
            buffer.append(",T1.FRIEND_MEMO").append(LS);
            buffer.append(",T1.FRIEND_CDATE").append(LS);
            buffer.append(",T2.USER_NICKNAME").append(LS);
            buffer.append(",T2.USER_ID").append(LS);
            buffer.append(",T2.PHOTO_STATUS").append(LS);
            buffer.append(",T2.LOGIN_STATUS").append(LS);
            buffer.append(",T2.URL_NUM").append(LS);
            buffer.append(",T2.VISITED_NUM").append(LS);
            buffer.append(",T2.WEB_NUM").append(LS);
            buffer.append(",T3.GROUP_NAME").append(LS);
            buffer.append("from USER_FRIEND T1").append(LS);
            buffer.append("inner join WANGYU_USER T2 on T1.FRIEND_ID = T2.USER_ID").append(LS);
            buffer.append("inner join USER_FRIEND_GROUP T3 on T1.GROUP_ID = T3.GROUP_ID").append(LS);
            buffer.append("where T1.OWNER_ID = ?").append(LS);
            buffer.append("and T1.GROUP_ID = ? -- @1").append(LS);
            buffer.append("order by T1.USER_FRIEND_ID limit ?, ?").append(LS);
            fetchList = buffer.toString();
            buffer.delete(0, buffer.length());
        }

        public static final String fetchCount;

        static {
            StringBuilder buffer = new StringBuilder(512);
            buffer.append("select COUNT(USER_FRIEND_ID) from USER_FRIEND where OWNER_ID = ?").append(LS);
            buffer.append("and GROUP_ID = ? -- @1").append(LS);
            fetchCount = buffer.toString();
            buffer.delete(0, buffer.length());
        }

        public static final String removeByUserFriendId$OwnerId = "delete from USER_FRIEND where USER_FRIEND_ID=? and OWNER_ID=?";
    }

    public static final class UserFriendGroupSql {

        public static final String create = "insert into USER_FRIEND_GROUP(GROUP_ID,USER_ID,GROUP_NAME,GROUP_ORDER,FRIEND_COUNT,MAKE_TYPE,GROUP_CDATE,GROUP_UDATE)values(?,?,?,?,?,?,?,?)";

        public static final String remove = "delete from USER_FRIEND_GROUP where GROUP_ID=?";

        public static final String fetch = "select * from USER_FRIEND_GROUP where GROUP_ID=?";

        public static final String export = "select * from USER_FRIEND_GROUP order by GROUP_ID desc limit ?,?";

        public static final String fetchMaxOrderByUserId = "select max(GROUP_ORDER)from USER_FRIEND_GROUP where USER_ID=?";

        public static final String fetchList4User = "select GROUP_ID,USER_ID,GROUP_NAME,GROUP_ORDER,FRIEND_COUNT,MAKE_TYPE,GROUP_CDATE from USER_FRIEND_GROUP where USER_ID=? order by GROUP_ORDER";

        public static final String updateName = "update USER_FRIEND_GROUP set GROUP_NAME=?,GROUP_UDATE=? where GROUP_ID=? and USER_ID=?";

        public static final String removeByGroupId$UserId = "delete from USER_FRIEND_GROUP where GROUP_ID=? and USER_ID=?";

        public static final String batchUpdateOrder = "update USER_FRIEND_GROUP set GROUP_ORDER=(GROUP_ORDER -1)where USER_ID=? and GROUP_ORDER > ?";

        public static final String fetchDefGroupByOwnerId = "select GROUP_ID from USER_FRIEND_GROUP where USER_ID=? and MAKE_TYPE=0";

        public static final String updateFriendCountById = "update USER_FRIEND_GROUP set FRIEND_COUNT=(select count(USER_FRIEND_ID)from USER_FRIEND where GROUP_ID=?)where GROUP_ID=?";
    }

    public static final class UserTagSql {

        public static final String create = "insert into USER_TAG(TAG_ID,USER_ID,TAG_NAME,TAG_CDATE)values(?,?,?,?)";

        public static final String remove = "delete from USER_TAG where TAG_ID=?";

        public static final String fetch = "select * from USER_TAG where TAG_ID=?";

        public static final String export = "select * from USER_TAG order by TAG_ID desc limit ?,?";
    }

    public static final class UserUrlSql {

        public static final String create = "insert into USER_URL(USER_URL_ID,CAT_ID,CORE_URL_ID,USER_ID,DOMAIN_ID,URL_HREF,URL_NAME,URL_MEMO,URL_COL_NUM,URL_ORDER,URL_SHOW_TYPE,URL_CREATE_TYPE,SELF_CLICK_NUM,SELF_CLICK_DATE,URL_CDATE,URL_UDATE)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        public static final String remove = "delete from USER_URL where USER_URL_ID=?";

        public static final String fetch = "select * from USER_URL where USER_URL_ID=?";

        public static final String export = "select * from USER_URL order by USER_URL_ID desc limit ?,?";

        public static final String fetchUrlList4Self = "select T1.*,T2.USER_URL_ID,T2.DOMAIN_ID,T2.CORE_URL_ID,T2.URL_HREF,T2.URL_NAME,T2.URL_ORDER,T2.URL_SHOW_TYPE,T2.URL_MEMO from USER_URL_CAT T1 left join USER_URL T2 on T1.CAT_ID=T2.CAT_ID where T1.USER_ID=? order by T1.CAT_COL_NUM asc,T1.CAT_ORDER asc,T2.URL_ORDER asc";

        public static final String fetchUrlList4Guest = "select T3.CAT_ID,T3.USER_ID,T3.CAT_NAME,T3.CAT_CREATE_TYPE,T3.CAT_SHOW_TYPE,T3.CAT_SHOW_NUM,T3.CAT_COL_NUM,T3.CAT_ORDER,T3.PRIVATE_URL_COUNT,T3.PROTECTED_URL_COUNT,T3.PUBLIC_URL_COUNT,T1.USER_URL_ID,T1.DOMAIN_ID,T1.CORE_URL_ID,T1.URL_HREF,T1.URL_NAME,T1.URL_ORDER,T1.URL_SHOW_TYPE,T1.URL_MEMO from USER_URL T1 inner join DOMAIN T2 on T1.DOMAIN_ID=T2.DOMAIN_ID and T2.DOMAIN_STATUS >=1 right join USER_URL_CAT T3 on T1.CAT_ID=T3.CAT_ID and T1.URL_SHOW_TYPE=3 and T1.URL_ORDER < T3.CAT_SHOW_NUM where T3.USER_ID=? and T3.CAT_SHOW_TYPE=3 order by T3.CAT_COL_NUM asc,T3.CAT_ORDER asc,T1.URL_ORDER asc";

        public static final String fetchFavList4Self = "select T1.USER_URL_ID,T1.URL_NAME,T1.URL_HREF,T1.DOMAIN_ID,T1.CORE_URL_ID,T1.USER_ID,T1.CAT_ID,T1.URL_ORDER,T2.ICO_STATUS from USER_URL T1 left join DOMAIN T2 on T1.DOMAIN_ID=T2.DOMAIN_ID inner join USER_URL_CAT T3 on T1.CAT_ID=T3.CAT_ID where T1.USER_ID=? order by T1.SELF_CLICK_NUM desc,T1.USER_URL_ID asc limit 0,?";

        public static final String fetchFavList4Guest = "select T1.USER_URL_ID,T1.URL_NAME,T1.URL_HREF,T1.DOMAIN_ID,T1.CORE_URL_ID,T1.USER_ID,T1.CAT_ID,T1.URL_ORDER,T2.ICO_STATUS from USER_URL T1 left join DOMAIN T2 on T1.DOMAIN_ID=T2.DOMAIN_ID inner join USER_URL_CAT T3 on T1.CAT_ID=T3.CAT_ID where T1.USER_ID=? and T1.URL_SHOW_TYPE >=3 and T2.DOMAIN_STATUS=1 and T3.CAT_SHOW_TYPE >=3 order by T1.SELF_CLICK_NUM desc,T1.USER_URL_ID asc limit 0,?";

        public static final String removeByCatId$UserId = "delete from USER_URL where CAT_ID=? and USER_ID=?";

        public static final String fetchList4Self = "select T1.USER_URL_ID,T1.USER_ID,T1.DOMAIN_ID,T1.CORE_URL_ID,T1.CAT_ID,T1.URL_HREF,T1.URL_NAME,T1.URL_COL_NUM,T1.URL_ORDER,T1.URL_SHOW_TYPE,T1.SELF_CLICK_NUM,T1.SELF_CLICK_DATE,T1.URL_MEMO,T1.URL_CDATE,T1.URL_UDATE,T2.ICO_STATUS,T2.ICO_URL from USER_URL T1 left join DOMAIN T2 on T1.DOMAIN_ID=T2.DOMAIN_ID where T1.USER_ID=? order by T1.URL_ORDER";

        public static final String fetchList4People = "select T1.USER_URL_ID,T1.USER_ID,T1.DOMAIN_ID,T1.CORE_URL_ID,T1.CAT_ID,T1.URL_HREF,T1.URL_NAME,T1.URL_COL_NUM,T1.URL_ORDER,T1.URL_SHOW_TYPE,T1.SELF_CLICK_NUM,T1.SELF_CLICK_DATE,T1.URL_CDATE,T1.URL_UDATE,T2.ICO_STATUS,T2.ICO_URL from USER_URL T1 left join DOMAIN T2 on T1.DOMAIN_ID=T2.DOMAIN_ID inner join USER_URL_CAT T3 on T1.CAT_ID=T3.CAT_ID where T1.USER_ID=? and T1.URL_SHOW_TYPE >=3 and T2.DOMAIN_STATUS=1 and T3.CAT_SHOW_TYPE >=3 order by T1.URL_ORDER";

        public static final String fetchListByCatId4Self;

        static {
            StringBuilder buffer = new StringBuilder(512);
            buffer.append("select T1.USER_URL_ID").append(LS);
            buffer.append(",T1.USER_ID").append(LS);
            buffer.append(",T1.DOMAIN_ID").append(LS);
            buffer.append(",T1.CAT_ID").append(LS);
            buffer.append(",T1.URL_HREF").append(LS);
            buffer.append(",T1.URL_NAME").append(LS);
            buffer.append(",T1.URL_COL_NUM").append(LS);
            buffer.append(",T1.URL_ORDER").append(LS);
            buffer.append(",T1.URL_SHOW_TYPE").append(LS);
            buffer.append(",T1.SELF_CLICK_NUM").append(LS);
            buffer.append(",T1.URL_MEMO").append(LS);
            buffer.append(",T1.URL_CDATE").append(LS);
            buffer.append(",T1.URL_UDATE").append(LS);
            buffer.append(",T2.ICO_STATUS").append(LS);
            buffer.append("from USER_URL T1").append(LS);
            buffer.append("left join DOMAIN T2 on T1.DOMAIN_ID = T2.DOMAIN_ID").append(LS);
            buffer.append("inner join USER_URL_CAT T3 on T1.CAT_ID = T3.CAT_ID").append(LS);
            buffer.append("where T1.USER_ID = ?").append(LS);
            buffer.append("and T1.CAT_ID = ? -- @1").append(LS);
            buffer.append("order by").append(LS);
            buffer.append("T1.SELF_CLICK_NUM desc,T1.USER_URL_ID asc").append(LS);
            buffer.append("limit ?,?").append(LS);
            fetchListByCatId4Self = buffer.toString();
            buffer.delete(0, buffer.length());
        }

        public static final String fetchCountByCatId4Self;

        static {
            StringBuilder buffer = new StringBuilder(512);
            buffer.append("select count(USER_URL_ID)").append(LS);
            buffer.append("from USER_URL T1").append(LS);
            buffer.append("inner join USER_URL_CAT T2 on T1.CAT_ID = T2.CAT_ID").append(LS);
            buffer.append("where T1.USER_ID = ?").append(LS);
            buffer.append("and T1.CAT_ID = ?  -- @1").append(LS);
            fetchCountByCatId4Self = buffer.toString();
            buffer.delete(0, buffer.length());
        }

        public static final String updateSelfClickNum = "update USER_URL set SELF_CLICK_NUM=(SELF_CLICK_NUM + 1),SELF_CLICK_DATE=? where USER_URL_ID=?";

        public static final String fetchSumSelfClickNumByUserId = "select sum(SELF_CLICK_NUM)from USER_URL where USER_ID=?";

        public static final String fetchSumMaxTwoSelfClickNumByUserId = "select sum(T.SELF_CLICK_NUM)from(select SELF_CLICK_NUM from USER_URL where USER_ID=? order by SELF_CLICK_NUM desc limit 0,2)T";

        public static final String fetchByHref$UserId = "select * from USER_URL where URL_HREF=? and USER_ID=?";

        public static final String updateWithoutUrlHref = "update USER_URL set URL_NAME=?,URL_SHOW_TYPE=?,URL_MEMO=?,URL_UDATE=? where USER_URL_ID=? and USER_ID=?";

        public static final String update4UrlHref = "update USER_URL set URL_HREF=?,URL_NAME=?,URL_SHOW_TYPE=?,CORE_URL_ID=null,DOMAIN_ID=null,URL_MEMO=?,URL_UDATE=? where USER_URL_ID=? and USER_ID=?";

        public static final String fetchCoreUrlId4Null = "select USER_URL_ID,URL_NAME,USER_ID,DOMAIN_ID,URL_SHOW_TYPE,URL_HREF,URL_CDATE,URL_CREATE_TYPE from USER_URL where CORE_URL_ID is NULL order by USER_URL_ID asc limit 0,?";

        public static final String updateDomainId$CoreUrlId = "update USER_URL set DOMAIN_ID=?,CORE_URL_ID=?,URL_UDATE=? where USER_URL_ID=?";

        public static final String batchPlusUrlOrder01 = "update USER_URL set URL_ORDER=(URL_ORDER + 1)where CAT_ID=? and URL_COL_NUM=? and URL_ORDER >=? and USER_ID=?";

        public static final String batchMinusUrlOrder01 = "update USER_URL set URL_ORDER=(URL_ORDER - 1)where CAT_ID=? and URL_COL_NUM=? and URL_ORDER > ? and USER_ID=?";

        public static final String batchPlusUrlOrder02 = "update USER_URL set URL_ORDER=(URL_ORDER + 1)where CAT_ID=? and URL_COL_NUM=? and URL_ORDER >=? and URL_ORDER < ? and USER_ID=?";

        public static final String batchMinusUrlOrder02 = "update USER_URL set URL_ORDER=(URL_ORDER - 1)where CAT_ID=? and URL_COL_NUM=? and URL_ORDER > ? and URL_ORDER <=? and USER_ID=?";

        public static final String updateUrlOrder$ColNum$CatId = "update USER_URL set URL_ORDER=?,URL_COL_NUM=?,CAT_ID=? where USER_URL_ID=? and USER_ID=?";

        public static final String updateUrlOrder$ColNum$CatId$ShowType = "update USER_URL set URL_ORDER=?,URL_COL_NUM=?,CAT_ID=?,URL_SHOW_TYPE=? where USER_URL_ID=? and USER_ID=?";

        public static final String fetchMaxUrlOrderMap = "select CAT_ID,URL_COL_NUM,max(URL_ORDER)URL_ORDER from USER_URL where USER_ID=? group by CAT_ID,URL_COL_NUM";

        public static final String fetchMaxUrlOrderMapByCat = "select CAT_ID,URL_COL_NUM,max(URL_ORDER)URL_ORDER from USER_URL where CAT_ID=? group by CAT_ID,URL_COL_NUM";

        public static final String fetch4CopyCat = "select T1.CORE_URL_ID,T1.DOMAIN_ID,T1.URL_NAME,T1.URL_HREF from USER_URL T1 inner join DOMAIN T2 on T1.DOMAIN_ID=T2.DOMAIN_ID left join USER_URL T3 on T1.CORE_URL_ID=T3.CORE_URL_ID and T3.USER_ID=? where T1.CAT_ID=? and T1.URL_SHOW_TYPE=3 and T2.DOMAIN_STATUS=1 and T3.USER_URL_ID is null";

        public static final String fetchListInDefCatByUserId = "select T1.USER_URL_ID,T1.USER_ID,T1.DOMAIN_ID,T1.CORE_URL_ID,T1.CAT_ID,T1.URL_HREF,T1.URL_NAME,T1.URL_COL_NUM,T1.URL_ORDER,T1.URL_SHOW_TYPE,T1.SELF_CLICK_NUM,T1.URL_MEMO,T1.URL_CDATE,T1.URL_UDATE,T2.ICO_STATUS,T2.ICO_URL from USER_URL T1 left join DOMAIN T2 on T1.DOMAIN_ID=T2.DOMAIN_ID inner join USER_URL_CAT T3 on T1.CAT_ID=T3.CAT_ID where T1.USER_ID=? and T3.CAT_CREATE_TYPE=2 order by T1.URL_ORDER";

        public static final String fetchListByUsername = "select T1.USER_URL_ID,T1.USER_ID,T1.DOMAIN_ID,T1.CORE_URL_ID,T1.CAT_ID,T1.URL_HREF,T1.URL_NAME,T1.URL_COL_NUM,T1.URL_ORDER,T1.URL_SHOW_TYPE,T1.SELF_CLICK_NUM,T1.URL_MEMO,T1.URL_CDATE,T1.URL_UDATE,T2.ICO_STATUS,T2.ICO_URL from USER_URL T1 left join DOMAIN T2 on T1.DOMAIN_ID=T2.DOMAIN_ID inner join GOLD_USER T3 on T1.USER_ID=T3.USER_ID where T3.USER_NAME=? order by T1.URL_ORDER";

        public static final String updateUrlShowTypeByCatId = "update USER_URL set URL_SHOW_TYPE=? where CAT_ID=? and USER_ID=?";
    }

    public static final class UserUrlCatSql {

        public static final String create = "insert into USER_URL_CAT(CAT_ID,USER_ID,CAT_NAME,CAT_CREATE_TYPE,CAT_SHOW_TYPE,CAT_COL_NUM,CAT_ORDER,URL_COUNT,CAT_CDATE,CAT_UDATE)values(?,?,?,?,?,?,?,?,?,?)";

        public static final String remove = "delete from USER_URL_CAT where CAT_ID=?";

        public static final String fetch = "select * from USER_URL_CAT where CAT_ID=?";

        public static final String export = "select * from USER_URL_CAT order by CAT_ID desc limit ?,?";

        public static final String updateUrlCount = "update USER_URL_CAT set URL_COUNT=(select count(USER_URL_ID)from USER_URL where CAT_ID=?)where CAT_ID=?";

        public static final String fetchUrlList4Self = "select T1.*,T2.USER_URL_ID,T2.DOMAIN_ID,T2.CORE_URL_ID,T2.URL_HREF,T2.URL_NAME,T2.URL_ORDER,T2.URL_SHOW_TYPE,T2.URL_MEMO from USER_URL_CAT T1 left join USER_URL T2 on T1.CAT_ID=T2.CAT_ID where T1.USER_ID=? order by T1.CAT_COL_NUM asc,T1.CAT_ORDER asc,T2.URL_ORDER asc";

        public static final String fetchList4Self = "select CAT_ID,USER_ID,CAT_NAME,CAT_CREATE_TYPE,CAT_SHOW_TYPE,CAT_COL_NUM,CAT_ORDER,URL_COUNT,CAT_CDATE,CAT_UDATE from USER_URL_CAT where USER_ID=? order by CAT_ORDER asc,CAT_COL_NUM asc";

        public static final String fetchList4People = "select CAT_ID,USER_ID,CAT_NAME,CAT_CREATE_TYPE,CAT_SHOW_TYPE,CAT_COL_NUM,CAT_ORDER,URL_COUNT,CAT_CDATE,CAT_UDATE from USER_URL_CAT where USER_ID=? and CAT_SHOW_TYPE >=3 order by CAT_ORDER asc,CAT_COL_NUM asc";

        public static final String fetchUrlList4Guest = "select T3.CAT_ID,T3.USER_ID,T3.CAT_NAME,T3.CAT_CREATE_TYPE,T3.CAT_SHOW_TYPE,T3.CAT_SHOW_NUM,T3.CAT_COL_NUM,T3.CAT_ORDER,T3.PRIVATE_URL_COUNT,T3.PROTECTED_URL_COUNT,T3.PUBLIC_URL_COUNT,T1.USER_URL_ID,T1.DOMAIN_ID,T1.CORE_URL_ID,T1.URL_HREF,T1.URL_NAME,T1.URL_ORDER,T1.URL_SHOW_TYPE,T1.URL_MEMO from USER_URL T1 inner join DOMAIN T2 on T1.DOMAIN_ID=T2.DOMAIN_ID and T2.DOMAIN_STATUS >=1 right join USER_URL_CAT T3 on T1.CAT_ID=T3.CAT_ID and T1.URL_SHOW_TYPE=3 and T1.URL_ORDER < T3.CAT_SHOW_NUM where T3.USER_ID=? and T3.CAT_SHOW_TYPE=3 order by T3.CAT_COL_NUM asc,T3.CAT_ORDER asc,T1.URL_ORDER asc";

        public static final String batchMinusCatOrder01 = "update USER_URL_CAT set CAT_ORDER=(CAT_ORDER - 1)where USER_ID=? and CAT_COL_NUM=? and CAT_ORDER >=?";

        public static final String batchPlusCatOrder01 = "update USER_URL_CAT set CAT_ORDER=(CAT_ORDER + 1)where USER_ID=? and CAT_COL_NUM=? and CAT_ORDER >=?";

        public static final String removeByCatId$UserId = "delete from USER_URL_CAT where CAT_ID=? and USER_ID=?";

        public static final String updateByCatId$UserId = "update USER_URL_CAT set CAT_NAME=?,CAT_SHOW_TYPE=?,CAT_UDATE=? where CAT_ID=? and USER_ID=?";

        public static final String batchMinusCatOrder02 = "update USER_URL_CAT set CAT_ORDER=(CAT_ORDER - 1)where USER_ID=? and CAT_COL_NUM=? and CAT_ORDER >=? and CAT_ORDER < ?";

        public static final String batchPlusCatOrder02 = "update USER_URL_CAT set CAT_ORDER=(CAT_ORDER + 1)where USER_ID=? and CAT_COL_NUM=? and CAT_ORDER >=? and CAT_ORDER < ?";

        public static final String updateCatOrder = "update USER_URL_CAT set CAT_ORDER=? where CAT_ID=?";

        public static final String updateCatOrderAndCatColNum = "update USER_URL_CAT set CAT_COL_NUM=?,CAT_ORDER=? where CAT_ID=?";

        public static final String fetchMaxCatOrderMap = "select CAT_COL_NUM,max(CAT_ORDER)CAT_ORDER from USER_URL_CAT where USER_ID=? group by CAT_COL_NUM";

        public static final String fetchCat4SelfWithUrlList = "select T1.*,T2.USER_URL_ID,T2.DOMAIN_ID,T2.CORE_URL_ID,T2.URL_HREF,T2.URL_NAME,T2.URL_ORDER,T2.URL_COL_NUM,T2.URL_SHOW_TYPE,T2.SELF_CLICK_NUM,T2.URL_MEMO,T3.ICO_STATUS,T3.ICO_URL from USER_URL_CAT T1 inner join USER_URL T2 on T1.CAT_ID=T2.CAT_ID inner join DOMAIN T3 on T3.DOMAIN_ID=T2.DOMAIN_ID where T1.CAT_ID=? order by T2.URL_ORDER asc";

        public static final String fetchListByUserId$CreateType = "select CAT_ID,USER_ID,CAT_NAME,CAT_CREATE_TYPE,CAT_SHOW_TYPE,CAT_COL_NUM,CAT_ORDER,URL_COUNT,CAT_CDATE,CAT_UDATE from USER_URL_CAT where USER_ID=? and CAT_CREATE_TYPE=?";

        public static final String fetchListByUsername = "select T1.CAT_ID,T1.USER_ID,T1.CAT_NAME,T1.CAT_CREATE_TYPE,T1.CAT_SHOW_TYPE,T1.CAT_COL_NUM,T1.CAT_ORDER,T1.URL_COUNT,T1.CAT_CDATE,T1.CAT_UDATE from USER_URL_CAT T1 inner join GOLD_USER T2 on T1.USER_ID=T2.USER_ID where T2.USER_NAME=? order by T1.CAT_ORDER asc,T1.CAT_COL_NUM asc";
    }

    public static final class UserVisitLogSql {

        public static final String create = "insert into USER_VISIT_LOG(LOG_ID,FROM_USER_ID,TO_USER_ID,VISIT_NUM,FIRST_VISIT_DATE,LAST_VISIT_DATE)values(?,?,?,?,?,?)";

        public static final String remove = "delete from USER_VISIT_LOG where LOG_ID=?";

        public static final String fetch = "select * from USER_VISIT_LOG where LOG_ID=?";

        public static final String export = "select * from USER_VISIT_LOG order by LOG_ID desc limit ?,?";

        public static final String updateVisitNum = "update USER_VISIT_LOG set VISIT_NUM=(VISIT_NUM + 1),LAST_VISIT_DATE=? where FROM_USER_ID=? and TO_USER_ID=?";

        public static final String fetchByToUserId = "select T2.USER_ID,T2.USER_NICKNAME,T2.PHOTO_STATUS,T2.URL_NUM,T2.VISITED_NUM,T2.WEB_NUM,T1.LAST_VISIT_DATE,T1.FROM_USER_ID from USER_VISIT_LOG T1 left join WANGYU_USER T2 on T1.FROM_USER_ID=T2. USER_ID where T1.TO_USER_ID=? and T2.USER_TYPE >=0 order by T1.LAST_VISIT_DATE desc limit ?,?";

        public static final String fetchCountByToUserId = "select count(LOG_ID)from USER_VISIT_LOG T1 left join WANGYU_USER T2 on T1.FROM_USER_ID=T2. USER_ID where TO_USER_ID=? and T2.USER_TYPE >=0";

        public static final String fetchByFromUserId = "select T2.USER_ID,T2.USER_NICKNAME,T2.PHOTO_STATUS,T2.URL_NUM,T2.VISITED_NUM,T2.WEB_NUM,T1.LAST_VISIT_DATE,T1.TO_USER_ID from USER_VISIT_LOG T1 left join WANGYU_USER T2 on T1.TO_USER_ID=T2. USER_ID where T1.FROM_USER_ID=? and T2.USER_TYPE >=0 order by T1.LAST_VISIT_DATE desc limit ?,?";

        public static final String fetchCountByFromUserId = "select count(LOG_ID)from USER_VISIT_LOG T1 left join WANGYU_USER T2 on T1.TO_USER_ID=T2. USER_ID where FROM_USER_ID=? and T2.USER_TYPE >=0";
    }

    public static final class WangyuSkinSql {

        public static final String create = "insert into WANGYU_SKIN(SKIN_ID,CAT_ID,FOLDER_NAME,SKIN_NAME,SKIN_AUTHOR,SKIN_CDATE,SKIN_UDATE)values(?,?,?,?,?,?,?)";

        public static final String remove = "delete from WANGYU_SKIN where SKIN_ID=?";

        public static final String fetch = "select * from WANGYU_SKIN where SKIN_ID=?";

        public static final String export = "select * from WANGYU_SKIN order by SKIN_ID desc limit ?,?";

        public static final String fetchList = "select SKIN_ID,CAT_ID,FOLDER_NAME,SKIN_NAME,SKIN_AUTHOR from WANGYU_SKIN";
    }

    public static final class WangyuUserSql {

        public static final String create = "insert into WANGYU_USER(USER_ID,SKIN_ID,USER_NICKNAME,USER_SIGN,USER_TYPE,LOGIN_STATUS,PHOTO_STATUS,URL_CAT_NUM,URL_NUM,WEB_CAT_NUM,WEB_NUM,LOGIN_NUM,VISITED_NUM,LAST_LOGIN_DATE,LAST_VISIT_DATE,USER_CDATE,USER_UDATE)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        public static final String remove = "delete from WANGYU_USER where USER_ID=?";

        public static final String fetch = "select * from WANGYU_USER where USER_ID=?";

        public static final String export = "select * from WANGYU_USER order by USER_ID desc limit ?,?";

        public static final String fetchByUserName = "select T1.USER_ID,T1.SKIN_ID,T1.USER_NICKNAME,T1.USER_SIGN,T1.USER_TYPE,T1.LOGIN_STATUS,T1.PHOTO_STATUS,T1.URL_CAT_NUM,T1.URL_NUM,T1.WEB_CAT_NUM,T1.WEB_NUM,T1.LOGIN_NUM,T1.VISITED_NUM,T1.LAST_LOGIN_DATE,T1.LAST_VISIT_DATE,T1.USER_CDATE,T1.USER_UDATE,T2.USER_NAME,T3.CAT_ID,T3.FOLDER_NAME,T3.SKIN_NAME,T3.SKIN_AUTHOR from WANGYU_USER T1 inner join GOLD_USER T2 on T1.USER_ID=T2.USER_ID left join WANGYU_SKIN T3 on T1.SKIN_ID=T3.SKIN_ID where T2.USER_NAME=?";

        public static final String fetchByUserId = "select T1.USER_ID,T1.SKIN_ID,T1.USER_NICKNAME,T1.USER_SIGN,T1.USER_TYPE,T1.LOGIN_STATUS,T1.PHOTO_STATUS,T1.URL_CAT_NUM,T1.URL_NUM,T1.WEB_CAT_NUM,T1.WEB_NUM,T1.LOGIN_NUM,T1.VISITED_NUM,T1.LAST_LOGIN_DATE,T1.LAST_VISIT_DATE,T1.USER_CDATE,T1.USER_UDATE,T2.USER_NAME,T3.CAT_ID,T3.FOLDER_NAME,T3.SKIN_NAME,T3.SKIN_AUTHOR from WANGYU_USER T1 inner join GOLD_USER T2 on T1.USER_ID=T2.USER_ID left join WANGYU_SKIN T3 on T1.SKIN_ID=T3.SKIN_ID where T2.USER_Id=?";

        public static final String updateUserLoginNum = "update WANGYU_USER set LOGIN_NUM=(LOGIN_NUM + 1),LAST_LOGIN_DATE=? where USER_ID=?";

        public static final String updateUrlCatNum = "update WANGYU_USER set URL_CAT_NUM=(select count(CAT_ID)from USER_URL_CAT where USER_ID=?)where USER_ID=?";

        public static final String updateUrlNum = "update WANGYU_USER set URL_NUM=(select count(USER_URL_ID)from USER_URL where USER_ID=?)where USER_ID=?";

        public static final String updateUserVisitedNum = "update WANGYU_USER set VISITED_NUM=(VISITED_NUM + 1)where USER_ID=?";

        public static final String updateNickname = "update WANGYU_USER set USER_NICKNAME=? where USER_ID=?";

        public static final String updateSign = "update WANGYU_USER set USER_SIGN=? where USER_ID=?";

        public static final String updatePhotoStatus = "update WANGYU_USER set PHOTO_STATUS=1 where USER_ID=?";

        public static final String fetchPopularUser = "select USER_ID,USER_NICKNAME,PHOTO_STATUS,URL_NUM,VISITED_NUM,USER_SIGN from WANGYU_USER order by PHOTO_STATUS desc,LAST_VISIT_DATE desc limit 0,?";

        public static final String fetchTopUserList;

        static {
            StringBuilder buffer = new StringBuilder(512);
            buffer.append("select T1.USER_ID").append(LS);
            buffer.append(",T1.USER_NICKNAME").append(LS);
            buffer.append(",T1.USER_SIGN").append(LS);
            buffer.append(",T1.USER_TYPE").append(LS);
            buffer.append(",T1.LOGIN_STATUS").append(LS);
            buffer.append(",T1.PHOTO_STATUS").append(LS);
            buffer.append(",T1.URL_CAT_NUM").append(LS);
            buffer.append(",T1.URL_NUM").append(LS);
            buffer.append(",T1.WEB_CAT_NUM").append(LS);
            buffer.append(",T1.WEB_NUM").append(LS);
            buffer.append(",T1.LOGIN_NUM").append(LS);
            buffer.append(",T1.VISITED_NUM").append(LS);
            buffer.append(",T1.SKIN_ID").append(LS);
            buffer.append(",T1.LAST_LOGIN_DATE").append(LS);
            buffer.append(",T1.LAST_VISIT_DATE").append(LS);
            buffer.append(",T1.USER_CDATE").append(LS);
            buffer.append(",T1.USER_UDATE").append(LS);
            buffer.append("from WANGYU_USER T1").append(LS);
            buffer.append("inner join GOLD_USER T2 on T1.USER_ID = T2.USER_ID -- @1").append(LS);
            buffer.append("where T1.USER_TYPE >= 0").append(LS);
            buffer.append("and T1.USER_NICKNAME like ? or T2.USER_NAME like ? -- @4").append(LS);
            buffer.append("order by").append(LS);
            buffer.append("T1.LAST_VISIT_DATE desc -- @2").append(LS);
            buffer.append("T1.USER_CDATE desc -- @3").append(LS);
            buffer.append("limit ?, ?").append(LS);
            fetchTopUserList = buffer.toString();
            buffer.delete(0, buffer.length());
        }

        public static final String fetchTopUserCount;

        static {
            StringBuilder buffer = new StringBuilder(512);
            buffer.append("select count(T1.USER_ID)").append(LS);
            buffer.append("from WANGYU_USER T1").append(LS);
            buffer.append("inner join GOLD_USER T2 on T1.USER_ID = T2.USER_ID -- @1").append(LS);
            buffer.append("where").append(LS);
            buffer.append("T1.USER_NICKNAME like ? or T2.USER_NAME like ? and -- @4").append(LS);
            buffer.append("T1.USER_TYPE >= 0").append(LS);
            fetchTopUserCount = buffer.toString();
            buffer.delete(0, buffer.length());
        }

        public static final String fetchTopUserListByTagName = "select A.* from WANGYU_USER A inner join USER_TAG B ON A.USER_ID=B.USER_ID AND B.TAG_NAME=? order by A.LAST_VISIT_DATE desc limit ?,?";

        public static final String updateUrlCatNum$UrlNum = "update WANGYU_USER set URL_CAT_NUM=(select count(CAT_ID)from USER_URL_CAT where USER_ID=?),URL_NUM=(select count(USER_URL_ID)from USER_URL where USER_ID=?)where USER_ID=?";

        public static final String fetchNewUserList = "select USER_ID,USER_NICKNAME,USER_SIGN,USER_TYPE,LOGIN_STATUS,PHOTO_STATUS,URL_CAT_NUM,URL_NUM,WEB_CAT_NUM,WEB_NUM,LOGIN_NUM,VISITED_NUM,SKIN_ID,LAST_LOGIN_DATE,LAST_VISIT_DATE,USER_CDATE,USER_UDATE from WANGYU_USER where PHOTO_STATUS !=0 and USER_TYPE >=0 order by USER_ID desc limit ?,?";

        public static final String fetchActivityUserList = "select USER_ID,USER_NICKNAME,USER_SIGN,USER_TYPE,LOGIN_STATUS,PHOTO_STATUS,URL_CAT_NUM,URL_NUM,WEB_CAT_NUM,WEB_NUM,LOGIN_NUM,VISITED_NUM,SKIN_ID,LAST_LOGIN_DATE,LAST_VISIT_DATE,USER_CDATE,USER_UDATE from WANGYU_USER where PHOTO_STATUS !=0 and USER_TYPE >=0 order by LAST_VISIT_DATE desc limit ?,?";

        public static final String updateLastVisitDate = "update WANGYU_USER set LAST_VISIT_DATE=? where USER_ID=?";

        public static final String updateSkin = "update WANGYU_USER set SKIN_ID=? where USER_ID=?";
    }
}
