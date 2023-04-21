package jumpingnotes.service.remoting;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import jumpingnotes.codec.CodecParams;
import jumpingnotes.codec.CodecTask;
import jumpingnotes.codec.CodecService;
import jumpingnotes.codec.CodecTaskListener;
import jumpingnotes.storage.StorageService;
import jumpingnotes.deploy.DeployService;
import jumpingnotes.deploy.DeployEventListener;
import jumpingnotes.deploy.DeployTask;
import jumpingnotes.model.*;
import jumpingnotes.model.entity.ActivityMediaItem;
import jumpingnotes.model.entity.ActivityTemplateParam;
import jumpingnotes.model.entity.Audio;
import jumpingnotes.model.entity.Book;
import jumpingnotes.model.entity.Member;
import jumpingnotes.model.entity.RecordAlbum;
import jumpingnotes.model.entity.RecordTask;
import jumpingnotes.model.entity.Chapter;
import jumpingnotes.model.entity.Sync;
import jumpingnotes.model.entity.Deploy;
import jumpingnotes.util.MailHelper;

public class RecordServiceImpl extends GenericService implements RecordService, CodecTaskListener, DeployEventListener {

    public static final String EMAIL_REGEX = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    private MailHelper mailHelper;

    private CodecService codecService;

    private StorageService storageService;

    private DeployService deployService;

    private ConcurrentHashMap<String, String> codecMap = new ConcurrentHashMap<String, String>();

    private ConcurrentHashMap<String, String> deployMap = new ConcurrentHashMap<String, String>();

    public MailHelper getMailHelper() {
        return mailHelper;
    }

    public void setMailHelper(MailHelper mailHelper) {
        this.mailHelper = mailHelper;
    }

    public void setCodecService(CodecService codecService) {
        this.codecService = codecService;
    }

    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public void setDeployService(DeployService deployService) {
        this.deployService = deployService;
    }

    public void init() {
        codecService.addListener(this);
    }

    @Override
    public Result recordAlbumCreate(String memberID, String bookID, String publicFlag, String publicGroupID, String collabFlag, String name) {
        Book book = accessService.findBookById(Integer.parseInt(bookID));
        if (book == null) return new Result(ErrorType.BOOK_NOT_EXIST);
        Member member = accessService.findMemberById(Integer.parseInt(memberID));
        if (member == null) return new Result(ErrorType.USER_NOT_EXIST);
        RecordAlbum recordAlbum = new RecordAlbum();
        recordAlbum.setBook(book);
        recordAlbum.setCollabFlag(Integer.parseInt(collabFlag));
        recordAlbum.setCreateTime(new Date());
        recordAlbum.setMember(member);
        int pflag = Integer.parseInt(publicFlag);
        recordAlbum.setPublicFlag(pflag);
        if (pflag == RecordAlbumFlagType.RECORD_ALBUM_PUBLIC_GROUP) recordAlbum.setPublicGroupId(Integer.parseInt(publicGroupID));
        recordAlbum.setName(name);
        recordAlbum.setActiveLevel(0);
        recordAlbum.setExpectRate(0);
        recordAlbum.setRatingCount(0);
        recordAlbum.setRatingTotal(0);
        accessService.saveRecordAlbum(recordAlbum);
        ActivityTemplateParam[] activityParams = new ActivityTemplateParam[2];
        activityParams[0] = new ActivityTemplateParam();
        activityParams[0].setName("member");
        activityParams[0].setValue(member.getMemberId() + "&" + member.getNickName());
        activityParams[1] = new ActivityTemplateParam();
        activityParams[1].setName("book");
        activityParams[1].setValue(book.getBookId() + "&" + book.getName());
        ActivityMediaItem[] mediaItems = new ActivityMediaItem[1];
        mediaItems[0] = new ActivityMediaItem();
        mediaItems[0].setMimeType(String.valueOf(ResourceTypes.TYPE_IMAGE));
        mediaItems[0].setUrl(book.getImage());
        memberActivityCreate(memberID, ActivityType.RECORD_ALBUM_CREATE, activityParams, mediaItems, null, "1");
        return new Result(recordAlbum.getRecordAlbumId());
    }

    @Override
    public Result recordAlbumDelete(String memberID, String recordAlbumID) {
        Member member = accessService.findMemberById(Integer.parseInt(memberID));
        if (member == null) return new Result(ErrorType.USER_NOT_EXIST);
        RecordAlbum recordAlbum = accessService.findRecordAlbumById(Integer.parseInt(recordAlbumID));
        if (recordAlbum == null) return new Result(ErrorType.RECORDALBUM_NOT_EXIST);
        accessService.deleteRecordAlbum(recordAlbum);
        return new Result();
    }

    @Override
    public Result recordTaskCreate(String recordAlbumID, String chapterID) {
        RecordAlbum recordAlbum = accessService.findRecordAlbumById(Integer.parseInt(recordAlbumID));
        if (recordAlbum == null) return new Result(ErrorType.RECORDALBUM_NOT_EXIST);
        Chapter chapter = accessService.findChapterById(Integer.parseInt(chapterID));
        if (chapter == null) return new Result(ErrorType.BOOK_CHAPTER_NOT_EXIST);
        if (queryService.checkRecordTaskUnique(Integer.parseInt(chapterID), Integer.parseInt(recordAlbumID))) {
            return new Result(ErrorType.RECORDTASK_DUPLICATE);
        }
        RecordTask rt = new RecordTask();
        rt.setPlayCount(0);
        rt.setRatingCount(0);
        rt.setRatingTotal(0);
        rt.setChapter(chapter);
        rt.setRecordAlbum(recordAlbum);
        rt.setCreateTime(new Date());
        rt.setUpdateTime(new Date());
        rt.setStatus(RecordTaskStatusType.RECORD_TASK_RECORDING);
        int recordTaskId = accessService.saveRecordTask(rt);
        Book book = chapter.getBook();
        Member member = recordAlbum.getMember();
        ActivityTemplateParam[] activityParams = new ActivityTemplateParam[3];
        activityParams[0] = new ActivityTemplateParam();
        activityParams[0].setName("member");
        activityParams[0].setValue(member.getMemberId() + "&" + member.getNickName());
        activityParams[1] = new ActivityTemplateParam();
        activityParams[1].setName("book");
        activityParams[1].setValue(book.getBookId() + "&" + book.getName());
        activityParams[2] = new ActivityTemplateParam();
        activityParams[2].setName("chapter");
        activityParams[2].setValue(chapter.getName() + "&" + chapter.getName());
        ActivityMediaItem[] mediaItems = new ActivityMediaItem[1];
        mediaItems[0] = new ActivityMediaItem();
        mediaItems[0].setMimeType(String.valueOf(ResourceTypes.TYPE_IMAGE));
        mediaItems[0].setUrl(book.getImage());
        memberActivityCreate(member.getMemberId().toString(), ActivityType.RECORD_TASK_CREATE, activityParams, mediaItems, null, "1");
        return new Result(new Integer(recordTaskId));
    }

    @Override
    public Result recordTaskPublish(String recordTaskID) {
        RecordTask rt = accessService.findRecordTaskById(Integer.parseInt(recordTaskID));
        if (rt == null) return new Result(ErrorType.RECORDTASK_NOT_EXIST);
        if (rt.getLatestAudioId() == null) return new Result(ErrorType.INVALID_RECORDTASK);
        rt.setPublishedAudioId(rt.getLatestAudioId());
        rt.setStatus(RecordTaskStatusType.RECORD_TASK_PUBLISHED);
        rt.setUpdateTime(new Date());
        accessService.updateRecordTask(rt);
        Member member = rt.getRecordAlbum().getMember();
        Chapter chapter = rt.getChapter();
        Book book = chapter.getBook();
        ActivityTemplateParam[] activityParams = new ActivityTemplateParam[3];
        activityParams[0] = new ActivityTemplateParam();
        activityParams[0].setName("member");
        activityParams[0].setValue(member.getMemberId() + "&" + member.getNickName());
        activityParams[1] = new ActivityTemplateParam();
        activityParams[1].setName("book");
        activityParams[1].setValue(book.getBookId() + "&" + book.getName());
        activityParams[2] = new ActivityTemplateParam();
        activityParams[2].setName("chapter");
        activityParams[2].setValue(chapter.getName() + "&" + chapter.getName());
        ActivityMediaItem[] mediaItems = new ActivityMediaItem[1];
        mediaItems[0] = new ActivityMediaItem();
        mediaItems[0].setMimeType(String.valueOf(ResourceTypes.TYPE_IMAGE));
        mediaItems[0].setUrl(book.getImage());
        memberActivityCreate(member.getMemberId().toString(), ActivityType.RECORD_TASK_PUBLISH, activityParams, mediaItems, null, "1");
        return new Result(ErrorType.NO_ERROR);
    }

    @Override
    public Result recordTaskUpdate(String recordTaskID, String status, String latestAudioID) {
        RecordTask rt = accessService.findRecordTaskById(Integer.parseInt(recordTaskID));
        if (rt == null) return new Result(ErrorType.RECORDTASK_NOT_EXIST);
        if (latestAudioID != null) {
            Audio audio = accessService.findAudioById(Integer.parseInt(latestAudioID));
            if (audio == null) return new Result(ErrorType.AUDIO_NOT_EXIST);
            rt.setLatestAudioId(Integer.parseInt(latestAudioID));
        }
        rt.setStatus(Integer.parseInt(status));
        rt.setUpdateTime(new Date());
        return new Result(ErrorType.NO_ERROR);
    }

    @Override
    public Result recordTaskPrepare(String recordTaskID) {
        RecordTask rt = accessService.findRecordTaskById(Integer.parseInt(recordTaskID));
        if (rt == null) return new Result(ErrorType.RECORDTASK_NOT_EXIST);
        Audio audio;
        if (rt.getStatus() == RecordTaskStatusType.RECORD_TASK_EDITING) {
            audio = accessService.findAudioById(rt.getLatestAudioId());
        } else {
            Audio mp3Audio = accessService.findAudioById(rt.getLatestAudioId());
            audio = accessService.findAudioById(mp3Audio.getFlvAudioId());
        }
        if (audio == null) return new Result(ErrorType.AUDIO_NOT_EXIST);
        try {
            if (!storageService.restoreTmpObject(audio.getType(), audio.getSubtype(), audio.getUuid(), audio.getUuid())) return new Result(ErrorType.RESOURCE_COPY_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ErrorType.RESOURCE_COPY_FAILED);
        }
        return new Result(ErrorType.NO_ERROR);
    }

    @Override
    public Result audioCreate(String recordTaskID, String syncID, String flvAudioID, String type, String subtype, String name, String uuid, String duration, String memberID, String collabMemberIDs) {
        RecordTask rt = accessService.findRecordTaskById(Integer.parseInt(recordTaskID));
        Audio audio = new Audio();
        if (rt == null) return new Result(ErrorType.RECORDTASK_NOT_EXIST);
        Sync sync = null;
        if (syncID != null && !("".equals(syncID))) {
            sync = accessService.findSyncById(Integer.parseInt(syncID));
            if (sync == null) return new Result(ErrorType.SYNC_NOT_EXIST);
            audio.setSync(sync);
        }
        Member member = accessService.findMemberById(Integer.parseInt(memberID));
        if (member == null) return new Result(ErrorType.USER_NOT_EXIST);
        audio.setCollabMemberIds(collabMemberIDs);
        audio.setCreateTime(new Date());
        audio.setMemberId(Integer.parseInt(memberID));
        if (ResourceTypes.SUBTYPE_AUDIO_MP3 == Integer.parseInt(subtype) && flvAudioID != null) {
            audio.setFlvAudioId(Integer.parseInt(flvAudioID));
        }
        audio.setPlayCount(0);
        audio.setRating(0);
        audio.setRatingCount(0);
        audio.setRatingTotal(0);
        audio.setRecordTask(rt);
        audio.setSubtype(Integer.parseInt(subtype));
        audio.setType(Integer.parseInt(type));
        audio.setUuid(uuid);
        audio.setName(name);
        audio.setDuration(Integer.parseInt(duration));
        int rtId = accessService.saveAudio(audio);
        try {
            if (!storageService.persistTmpObject(ResourceTypes.TYPE_AUDIO, Integer.parseInt(subtype), uuid, uuid)) return new Result(ErrorType.RESOURCE_COPY_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ErrorType.RESOURCE_COPY_FAILED);
        }
        return new Result(new Integer(rtId));
    }

    @Override
    public Result audioUpdate(String audioID, String syncID, String name, String duration, String memberID, String collabMemberIDs) {
        Audio audio = accessService.findAudioById(Integer.parseInt(audioID));
        if (audio == null) return new Result(ErrorType.AUDIO_NOT_EXIST);
        if (syncID != null && !("".equals(syncID))) {
            Sync sync = accessService.findSyncById(Integer.parseInt(syncID));
            if (sync == null) return new Result(ErrorType.SYNC_NOT_EXIST);
            audio.setSync(sync);
        }
        Member member = accessService.findMemberById(Integer.parseInt(memberID));
        if (member == null) return new Result(ErrorType.USER_NOT_EXIST);
        audio.setName(name);
        audio.setCollabMemberIds(collabMemberIDs);
        audio.setMemberId(Integer.parseInt(memberID));
        audio.setDuration(Integer.parseInt(duration));
        accessService.updateAudio(audio);
        try {
            if (!storageService.persistTmpObject(ResourceTypes.TYPE_AUDIO, audio.getSubtype(), audio.getUuid(), audio.getUuid())) return new Result(ErrorType.RESOURCE_COPY_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(ErrorType.RESOURCE_COPY_FAILED);
        }
        return new Result(audioID);
    }

    @Override
    public Result syncCreate(String uuid, String content) {
        Sync sync = new Sync();
        sync.setUuid(uuid);
        sync.setCreateTime(new Date());
        if (content != null) {
            try {
                ByteArrayInputStream contentStream = new ByteArrayInputStream(content.getBytes());
                storageService.saveStream(ResourceTypes.TYPE_SYNC, 0, uuid, contentStream);
            } catch (Exception e) {
                return new Result(ErrorType.SYSTEM_ERROR);
            }
        }
        int syncId = accessService.saveSync(sync);
        return new Result(new Integer(syncId));
    }

    @Override
    public Result syncUpdate(String syncID, String content) {
        Sync sync = accessService.findSyncById(Integer.parseInt(syncID));
        if (sync == null) return new Result(ErrorType.SYNC_NOT_EXIST);
        if (content != null) {
            try {
                ByteArrayInputStream contentStream = new ByteArrayInputStream(content.getBytes());
                storageService.saveStream(ResourceTypes.TYPE_SYNC, 0, sync.getUuid(), contentStream);
            } catch (Exception e) {
                return new Result(ErrorType.SYSTEM_ERROR);
            }
        } else {
            return new Result(ErrorType.SYSTEM_ERROR);
        }
        return new Result(ErrorType.NO_ERROR);
    }

    @Override
    public Result audioRate(String audioID, String rating) {
        Audio audio = accessService.findAudioById(Integer.parseInt(audioID));
        if (audio == null) return new Result(ErrorType.AUDIO_NOT_EXIST);
        audio.setRatingCount(audio.getRatingCount() + 1);
        audio.setRatingTotal(audio.getRatingTotal() + Integer.parseInt(rating));
        accessService.updateAudio(audio);
        RecordTask rt = audio.getRecordTask();
        rt.setRatingCount(rt.getRatingCount() + 1);
        rt.setRatingTotal(rt.getRatingTotal() + Integer.parseInt(rating));
        accessService.updateRecordTask(rt);
        RecordAlbum ra = rt.getRecordAlbum();
        ra.setRatingCount(ra.getRatingCount() + 1);
        ra.setRatingTotal(ra.getRatingTotal() + Integer.parseInt(rating));
        accessService.updateRecordAlbum(ra);
        return new Result(ErrorType.NO_ERROR);
    }

    @Override
    public Result audioDeploy(String audioUuid, String deployType, String vendor, String model) {
        Audio audio = new Audio();
        audio.setUuid(audioUuid);
        List<Audio> audioList = accessService.findAllAudio(audio);
        if (audioList.size() != 1) return new Result(ErrorType.AUDIO_NOT_EXIST);
        audio = audioList.get(0);
        String deployUuid = UUID.randomUUID().toString();
        Deploy deploy = new Deploy();
        deploy.setAudio(audio);
        deploy.setUuid(deployUuid);
        deploy.setModel(model);
        deploy.setVendor(vendor);
        deploy.setSubtype(ResourceTypes.SUBTYPE_RESERVED);
        deploy.setDeployType(deployType);
        deploy.setCreateTime(new Date());
        int deployRst = deployService.deploy(deploy, null, null);
        if (deployRst >= 0) {
            accessService.saveDeploy(deploy);
            return new Result(deployUuid);
        } else {
            return new Result(ErrorType.SYSTEM_ERROR);
        }
    }

    @Override
    public Result audioFilter(String input, String output, String bitRate, String sampleRate, String volume, String startTime, String duration) {
        CodecParams codecParams = new CodecParams(Integer.parseInt(bitRate), Integer.parseInt(sampleRate), Integer.parseInt(volume), Integer.parseInt(startTime), Integer.parseInt(duration));
        CodecTask codecTask = new CodecTask(UUID.randomUUID().toString(), input, output, codecParams);
        codecTask.setContext(null);
        if (codecService.addTask(codecTask)) {
            return new Result(ErrorType.NO_ERROR);
        } else {
            return new Result(ErrorType.SYSTEM_ERROR);
        }
    }

    @Override
    public Result audioDeployProgress(String audioUuid, String deployType) {
        return null;
    }

    @Override
    public Result audioDeployProgress(String deployUuid) {
        if (deployMap.contains(deployUuid)) {
            if (deployMap.get(deployUuid) != "100") {
                return new Result(deployService.getCodecTaskProgress(deployUuid));
            } else return new Result("100");
        } else return new Result(ErrorType.AUDIO_DEPLOY_NOT_EXIST);
    }

    @Override
    public Result audioFilterProgress(String input, String output) {
        String key = input + output;
        if (codecMap.containsKey(key)) {
            return new Result(codecMap.get(key));
        } else {
            return new Result(ErrorType.AUDIO_FILTER_NOT_EXIST);
        }
    }

    @Override
    public void onTaskDone(CodecTask task) {
        String key = task.getInputFilename() + task.getOutputFilename();
        codecMap.replace(key, "100");
        System.out.println("onTaskDone");
    }

    @Override
    public void onTaskProgress(CodecTask task, int percentage) {
        String key = task.getInputFilename() + task.getOutputFilename();
        codecMap.replace(key, String.valueOf(percentage));
        System.out.println("onTaskProgress.key:" + key + " percentage:" + percentage);
    }

    @Override
    public void onTaskStart(CodecTask task) {
        String key = task.getInputFilename() + task.getOutputFilename();
        if (codecMap.replace(key, "0") == null) {
            codecMap.put(key, "0");
        }
        System.out.println("onTaskStart");
    }

    @Override
    public void onDeployDone(DeployTask task) {
        String key = task.getDeploy().getUuid();
        deployMap.replace(key, "100");
        System.out.println("onDeployDone");
    }

    @Override
    public void onDeployProgress(DeployTask task, int percentage) {
    }

    @Override
    public void onDeployStart(DeployTask task) {
        String key = task.getDeploy().getUuid();
        if (deployMap.replace(key, "0") == null) {
            deployMap.put(key, "0");
        }
        System.out.println("onDeployStart");
    }

    @Override
    public Result deployPlay(String memberID, String deployUuid, String subtype) {
        Member member = accessService.findMemberById(Integer.parseInt(memberID));
        if (member == null) {
            return new Result(ErrorType.USER_NOT_EXIST);
        }
        Deploy exampleDeploy = new Deploy();
        exampleDeploy.setUuid(deployUuid);
        List<Deploy> deployList = accessService.findAllDeploy(exampleDeploy);
        if (deployList.get(0) == null) {
            return new Result(ErrorType.DEPLOY_NOT_EXIST);
        }
        Deploy deploy = deployList.get(0);
        Chapter chapter = deploy.getAudio().getRecordTask().getChapter();
        Book book = chapter.getBook();
        ActivityTemplateParam[] activityParams = new ActivityTemplateParam[3];
        activityParams[0] = new ActivityTemplateParam();
        activityParams[0].setName("member");
        activityParams[0].setValue(member.getMemberId() + "&" + member.getNickName());
        activityParams[1] = new ActivityTemplateParam();
        activityParams[1].setName("book");
        activityParams[1].setValue(book.getBookId() + "&" + book.getName());
        activityParams[2] = new ActivityTemplateParam();
        activityParams[2].setName("chapter");
        activityParams[2].setValue(chapter.getChapterId() + "&" + chapter.getName());
        ActivityMediaItem[] mediaItems = new ActivityMediaItem[1];
        mediaItems[0] = new ActivityMediaItem();
        mediaItems[0].setMimeType(String.valueOf(ResourceTypes.TYPE_IMAGE));
        mediaItems[0].setUrl(book.getImage());
        memberActivityCreate(memberID, ActivityType.DEPLOY_PLAY, activityParams, mediaItems, null, "1");
        return new Result(ErrorType.NO_ERROR);
    }

    @Override
    public Result audioRecord(String memberID, String bookID, String recordTaskID) {
        Member member = accessService.findMemberById(Integer.parseInt(memberID));
        if (member == null) {
            return new Result(ErrorType.USER_NOT_EXIST);
        }
        Book book = accessService.findBookById(Integer.parseInt(bookID));
        if (book == null) {
            return new Result(ErrorType.BOOK_NOT_EXIST);
        }
        RecordTask task = accessService.findRecordTaskById(Integer.parseInt(recordTaskID));
        if (task == null) {
            return new Result(ErrorType.RECORDTASK_NOT_EXIST);
        }
        Chapter chapter = task.getChapter();
        ActivityTemplateParam[] activityParams = new ActivityTemplateParam[3];
        activityParams[0] = new ActivityTemplateParam();
        activityParams[0].setName("member");
        activityParams[0].setValue(member.getMemberId() + "&" + member.getNickName());
        activityParams[1] = new ActivityTemplateParam();
        activityParams[1].setName("book");
        activityParams[1].setValue(book.getBookId() + "&" + book.getName());
        activityParams[2] = new ActivityTemplateParam();
        activityParams[2].setName("chapter");
        activityParams[2].setValue(chapter.getChapterId() + "&" + chapter.getName());
        ActivityMediaItem[] mediaItems = new ActivityMediaItem[1];
        mediaItems[0] = new ActivityMediaItem();
        mediaItems[0].setMimeType(String.valueOf(ResourceTypes.TYPE_IMAGE));
        mediaItems[0].setUrl(book.getImage());
        memberActivityCreate(memberID, ActivityType.AUDIO_RECORD, activityParams, mediaItems, null, "1");
        return new Result(ErrorType.NO_ERROR);
    }

    @Override
    public Result deployDownload(String memberID, String deployUuid, String subtype) {
        Member member = accessService.findMemberById(Integer.parseInt(memberID));
        if (member == null) {
            return new Result(ErrorType.USER_NOT_EXIST);
        }
        Deploy exampleDeploy = new Deploy();
        exampleDeploy.setUuid(deployUuid);
        List<Deploy> deployList = accessService.findAllDeploy(exampleDeploy);
        if (deployList.get(0) == null) {
            return new Result(ErrorType.DEPLOY_NOT_EXIST);
        }
        Deploy deploy = deployList.get(0);
        Chapter chapter = deploy.getAudio().getRecordTask().getChapter();
        Book book = chapter.getBook();
        ActivityTemplateParam[] activityParams = new ActivityTemplateParam[3];
        activityParams[0] = new ActivityTemplateParam();
        activityParams[0].setName("member");
        activityParams[0].setValue(member.getMemberId() + "&" + member.getNickName());
        activityParams[1] = new ActivityTemplateParam();
        activityParams[1].setName("book");
        activityParams[1].setValue(book.getBookId() + "&" + book.getName());
        activityParams[2] = new ActivityTemplateParam();
        activityParams[2].setName("chapter");
        activityParams[2].setValue(chapter.getChapterId() + "&" + chapter.getName());
        ActivityMediaItem[] mediaItems = new ActivityMediaItem[1];
        mediaItems[0] = new ActivityMediaItem();
        mediaItems[0].setMimeType(String.valueOf(ResourceTypes.TYPE_IMAGE));
        mediaItems[0].setUrl(book.getImage());
        memberActivityCreate(memberID, ActivityType.DEPLOY_DOWNLOAD, activityParams, mediaItems, null, "1");
        return new Result(ErrorType.NO_ERROR);
    }
}
