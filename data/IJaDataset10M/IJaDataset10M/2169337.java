package jumpingnotes.service.remoting;

import jumpingnotes.model.Result;
import jumpingnotes.model.entity.ActivityMediaItem;
import jumpingnotes.model.entity.ActivityTemplateParam;
import jumpingnotes.model.entity.Audio;
import jumpingnotes.security.SessionManager;

public interface FlashRemotingService extends MemberService, FriendService, GroupService, BookService, RecordService {

    public Result memberRegister(String email, String password, String nickname);

    public Result isEmailUnique(String email);

    public Result isPasswordCorrect(String memberID, String password);

    public Result memberLogin(String email, String password);

    public Result memberLogout(String email, String password);

    public Result memberRegisterInvite(String email, String memberID);

    public Result memberRegisterActivate(String email, String password);

    public Result memberRegisterActivate(String memberConfirmId);

    public Result memberRegisterInviteActivate(String email, String password);

    public Result memberPasswordUpdate(String memberID, String password);

    public Result memberPasswordRetrieve(String email);

    public Result memberPortraitUpload(String memberID, String image, String imageBig, String imageSmall, String imageSquare);

    public Result memberInfoUpdate(String memberID, String nickname, String aboutMe, String birthday, Character gender, String locationCity, String locationProvince, String locationCountry, String homepage);

    public Result messageSend(String memberIDFrom, String memberIDTo, String title, String body);

    public Result messageDelete(String memberID, String[] messageIDArray);

    public Result friendInvite(String memberIDTo, String memberIDFrom);

    public Result friendInviteConfirm(String memberIDTo, String friendConfirmID);

    public Result friendInviteDelete(String memberIDTo, String friendConfirmID);

    public Result friendDelete(String memberIDTo, String memberIDFrom);

    public Result isFriend(String memberIDTo, String memberIDFrom);

    public Result friendInviteByEmail(String memberToEmail, String memberIDFrom);

    public Result groupCreate(String memberID, String name, String intro, String image, String tag, String publicFlag);

    public Result groupImageUpload(String memberID, String groupID, String image);

    public Result groupPrivacyUpdate(String memberID, String groupID, String publicFlag);

    public Result groupInfoUpdate(String memberID, String groupID, String name, String intro, String tag);

    public Result groupJoin(String memberID, String groupID);

    public Result groupInvite(String memberIDTo, String memberIDFrom, String groupID);

    public Result groupInviteConfirm(String memberIDTo, String groupMemberConfirmID);

    public Result groupInviteDelete(String memberIDTo, String groupMemberConfirmID);

    public Result groupAdminPromote(String memberID, String groupID);

    public Result groupAdminDemote(String memberID, String groupID);

    public Result groupTopicCreate(String memberID, String groupID, String title, String body);

    public Result groupTopicUpdate(String memberID, String groupTopicID, String title, String body);

    public Result groupTopicDelete(String memberID, String groupTopicID);

    public Result groupTopicCommentCreate(String memberID, String groupTopicID, String comment);

    public Result groupTopicCommentDelete(String memberID, String groupTopicCommentID);

    public Result groupTopicSticky(String memberID, String groupTopicID);

    public Result groupTopicUnsticky(String memberID, String groupTopicID);

    public Result isMemberOfGroup(String memberID, String groupID);

    public Result bookCreate(String bookName, String author, String memberID);

    public Result bookInfoVersionCreate(String bookID, String name, String subTitle, String abstract_, String author, String authorIntro, String publishPress, String publishYear, String chapterCount, String wordCount, String completeStatus, String website, String url, String memberID);

    public Result bookCoverFilter(String uuid, String subtype, String ratio, String x, String y, String width, String height);

    public Result bookCoverVersionUpload(String bookID, String memberID, String image);

    public Result chapterCreate(String bookID, String memberID, String name, int number, String abstract_, String uuid);

    public Result chapterVersionCreate(String chapterID, String memberID, String number, String name, String abstract_, String uuid);

    public Result bookFavoriteCreate(String bookID, String memberID);

    public Result bookFavoriteDelete(String bookID, String memberID);

    public Result bookCommentCreate(String bookID, String memberID, String comment);

    public Result bookCommentDelete(String bookCommentID, String memberID);

    public SessionManager getSessionManager();

    public Result memberActivityCreate(String memberID, String activityType, ActivityTemplateParam[] activityParams, ActivityMediaItem[] mediaItems, String url, String priority);

    public Result bookRecommendCreate(String bookID, String memberID, String target, String reason);

    public Result bookExpectCreate(String bookID, String memberID, String reason);

    public Result errorReportCreate(String memberID, String targetID, String subject, String body);

    public Result countMemberByFavoriteBook(String bookID);

    public Result recordAlbumCreate(String memberID, String bookID, String publicFlag, String publicGroupID, String collabFlag, String name);

    public Result recordAlbumDelete(String memberID, String recordAlbumID);

    public Result recordTaskCreate(String recordAlbumID, String chapterID);

    public Result recordTaskUpdate(String recordTaskID, String status, String latestAudioID);

    public Result recordTaskPublish(String recordTaskID);

    public Result recordTaskPrepare(String recordTaskID);

    public Result audioCreate(String recordTaskID, String syncID, String flvAudioID, String type, String subtype, String name, String uuid, String duration, String memberID, String collabMemberIDs);

    public Result audioUpdate(String audioID, String syncID, String name, String duration, String memberID, String collabMemberIDs);

    public Result audioRate(String audioID, String rating);

    public Result audioFilter(String input, String output, String bitRate, String sampleRate, String volume, String startTime, String duration);

    public Result audioFilterProgress(String input, String output);

    public Result audioDeploy(String audioUuid, String deployType, String vendor, String model);

    public Result audioDeployProgress(String audioUuid, String deployType);

    public Result audioDeployProgress(String deployUuid);

    public Result syncCreate(String uuid, String content);

    public Result syncUpdate(String syncID, String content);

    public Result deployPlay(String memberID, String deployUuid, String subtype);

    public Result deployDownload(String memberID, String deployUuid, String subtype);

    public Result audioRecord(String memberID, String bookID, String recordTaskID);
}
