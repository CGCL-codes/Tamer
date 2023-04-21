package jumpingnotes.service;

import java.util.List;
import jumpingnotes.dao.*;
import jumpingnotes.model.entity.*;
import jumpingnotes.util.Sorter;

public class AccessServiceImpl implements AccessService {

    protected ActivityDao activityDao;

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public Activity findActivityById(int id) {
        return activityDao.findById(id);
    }

    public List<Activity> findAllActivity(Activity exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return activityDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<Activity> findAllActivity(Activity exampleEntity, int firstResult, int maxResults) {
        return activityDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<Activity> findAllActivity(Activity exampleEntity, Sorter sorter) {
        return activityDao.findAll(exampleEntity, sorter);
    }

    public List<Activity> findAllActivity(Activity exampleEntity) {
        return activityDao.findAll(exampleEntity);
    }

    public List<Activity> findAllActivity(int firstResult, int maxResults, Sorter sorter) {
        return activityDao.findAll(firstResult, maxResults, sorter);
    }

    public List<Activity> findAllActivity(int firstResult, int maxResults) {
        return activityDao.findAll(firstResult, maxResults);
    }

    public List<Activity> findAllActivity(Sorter sorter) {
        return activityDao.findAll(sorter);
    }

    public List<Activity> findAllActivity() {
        return activityDao.findAll();
    }

    public int saveActivity(Activity entity) {
        return activityDao.save(entity);
    }

    public void updateActivity(Activity entity) {
        activityDao.update(entity);
    }

    public void deleteActivity(Activity entity) {
        activityDao.delete(entity);
    }

    public void deleteActivity(int id) {
        activityDao.delete(id);
    }

    protected ActivityMediaItemDao activityMediaItemDao;

    public void setActivityMediaItemDao(ActivityMediaItemDao activityMediaItemDao) {
        this.activityMediaItemDao = activityMediaItemDao;
    }

    public ActivityMediaItem findActivityMediaItemById(int id) {
        return activityMediaItemDao.findById(id);
    }

    public List<ActivityMediaItem> findAllActivityMediaItem(ActivityMediaItem exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return activityMediaItemDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<ActivityMediaItem> findAllActivityMediaItem(ActivityMediaItem exampleEntity, int firstResult, int maxResults) {
        return activityMediaItemDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<ActivityMediaItem> findAllActivityMediaItem(ActivityMediaItem exampleEntity, Sorter sorter) {
        return activityMediaItemDao.findAll(exampleEntity, sorter);
    }

    public List<ActivityMediaItem> findAllActivityMediaItem(ActivityMediaItem exampleEntity) {
        return activityMediaItemDao.findAll(exampleEntity);
    }

    public List<ActivityMediaItem> findAllActivityMediaItem(int firstResult, int maxResults, Sorter sorter) {
        return activityMediaItemDao.findAll(firstResult, maxResults, sorter);
    }

    public List<ActivityMediaItem> findAllActivityMediaItem(int firstResult, int maxResults) {
        return activityMediaItemDao.findAll(firstResult, maxResults);
    }

    public List<ActivityMediaItem> findAllActivityMediaItem(Sorter sorter) {
        return activityMediaItemDao.findAll(sorter);
    }

    public List<ActivityMediaItem> findAllActivityMediaItem() {
        return activityMediaItemDao.findAll();
    }

    public int saveActivityMediaItem(ActivityMediaItem entity) {
        return activityMediaItemDao.save(entity);
    }

    public void updateActivityMediaItem(ActivityMediaItem entity) {
        activityMediaItemDao.update(entity);
    }

    public void deleteActivityMediaItem(ActivityMediaItem entity) {
        activityMediaItemDao.delete(entity);
    }

    public void deleteActivityMediaItem(int id) {
        activityMediaItemDao.delete(id);
    }

    protected ActivityTemplateDao activityTemplateDao;

    public void setActivityTemplateDao(ActivityTemplateDao activityTemplateDao) {
        this.activityTemplateDao = activityTemplateDao;
    }

    public ActivityTemplate findActivityTemplateById(int id) {
        return activityTemplateDao.findById(id);
    }

    public List<ActivityTemplate> findAllActivityTemplate(ActivityTemplate exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return activityTemplateDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<ActivityTemplate> findAllActivityTemplate(ActivityTemplate exampleEntity, int firstResult, int maxResults) {
        return activityTemplateDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<ActivityTemplate> findAllActivityTemplate(ActivityTemplate exampleEntity, Sorter sorter) {
        return activityTemplateDao.findAll(exampleEntity, sorter);
    }

    public List<ActivityTemplate> findAllActivityTemplate(ActivityTemplate exampleEntity) {
        return activityTemplateDao.findAll(exampleEntity);
    }

    public List<ActivityTemplate> findAllActivityTemplate(int firstResult, int maxResults, Sorter sorter) {
        return activityTemplateDao.findAll(firstResult, maxResults, sorter);
    }

    public List<ActivityTemplate> findAllActivityTemplate(int firstResult, int maxResults) {
        return activityTemplateDao.findAll(firstResult, maxResults);
    }

    public List<ActivityTemplate> findAllActivityTemplate(Sorter sorter) {
        return activityTemplateDao.findAll(sorter);
    }

    public List<ActivityTemplate> findAllActivityTemplate() {
        return activityTemplateDao.findAll();
    }

    public int saveActivityTemplate(ActivityTemplate entity) {
        return activityTemplateDao.save(entity);
    }

    public void updateActivityTemplate(ActivityTemplate entity) {
        activityTemplateDao.update(entity);
    }

    public void deleteActivityTemplate(ActivityTemplate entity) {
        activityTemplateDao.delete(entity);
    }

    public void deleteActivityTemplate(int id) {
        activityTemplateDao.delete(id);
    }

    protected ActivityTemplateParamDao activityTemplateParamDao;

    public void setActivityTemplateParamDao(ActivityTemplateParamDao activityTemplateParamDao) {
        this.activityTemplateParamDao = activityTemplateParamDao;
    }

    public ActivityTemplateParam findActivityTemplateParamById(int id) {
        return activityTemplateParamDao.findById(id);
    }

    public List<ActivityTemplateParam> findAllActivityTemplateParam(ActivityTemplateParam exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return activityTemplateParamDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<ActivityTemplateParam> findAllActivityTemplateParam(ActivityTemplateParam exampleEntity, int firstResult, int maxResults) {
        return activityTemplateParamDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<ActivityTemplateParam> findAllActivityTemplateParam(ActivityTemplateParam exampleEntity, Sorter sorter) {
        return activityTemplateParamDao.findAll(exampleEntity, sorter);
    }

    public List<ActivityTemplateParam> findAllActivityTemplateParam(ActivityTemplateParam exampleEntity) {
        return activityTemplateParamDao.findAll(exampleEntity);
    }

    public List<ActivityTemplateParam> findAllActivityTemplateParam(int firstResult, int maxResults, Sorter sorter) {
        return activityTemplateParamDao.findAll(firstResult, maxResults, sorter);
    }

    public List<ActivityTemplateParam> findAllActivityTemplateParam(int firstResult, int maxResults) {
        return activityTemplateParamDao.findAll(firstResult, maxResults);
    }

    public List<ActivityTemplateParam> findAllActivityTemplateParam(Sorter sorter) {
        return activityTemplateParamDao.findAll(sorter);
    }

    public List<ActivityTemplateParam> findAllActivityTemplateParam() {
        return activityTemplateParamDao.findAll();
    }

    public int saveActivityTemplateParam(ActivityTemplateParam entity) {
        return activityTemplateParamDao.save(entity);
    }

    public void updateActivityTemplateParam(ActivityTemplateParam entity) {
        activityTemplateParamDao.update(entity);
    }

    public void deleteActivityTemplateParam(ActivityTemplateParam entity) {
        activityTemplateParamDao.delete(entity);
    }

    public void deleteActivityTemplateParam(int id) {
        activityTemplateParamDao.delete(id);
    }

    protected AudioDao audioDao;

    public void setAudioDao(AudioDao audioDao) {
        this.audioDao = audioDao;
    }

    public Audio findAudioById(int id) {
        return audioDao.findById(id);
    }

    public List<Audio> findAllAudio(Audio exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return audioDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<Audio> findAllAudio(Audio exampleEntity, int firstResult, int maxResults) {
        return audioDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<Audio> findAllAudio(Audio exampleEntity, Sorter sorter) {
        return audioDao.findAll(exampleEntity, sorter);
    }

    public List<Audio> findAllAudio(Audio exampleEntity) {
        return audioDao.findAll(exampleEntity);
    }

    public List<Audio> findAllAudio(int firstResult, int maxResults, Sorter sorter) {
        return audioDao.findAll(firstResult, maxResults, sorter);
    }

    public List<Audio> findAllAudio(int firstResult, int maxResults) {
        return audioDao.findAll(firstResult, maxResults);
    }

    public List<Audio> findAllAudio(Sorter sorter) {
        return audioDao.findAll(sorter);
    }

    public List<Audio> findAllAudio() {
        return audioDao.findAll();
    }

    public int saveAudio(Audio entity) {
        return audioDao.save(entity);
    }

    public void updateAudio(Audio entity) {
        audioDao.update(entity);
    }

    public void deleteAudio(Audio entity) {
        audioDao.delete(entity);
    }

    public void deleteAudio(int id) {
        audioDao.delete(id);
    }

    protected BookDao bookDao;

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public Book findBookById(int id) {
        return bookDao.findById(id);
    }

    public List<Book> findAllBook(Book exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return bookDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<Book> findAllBook(Book exampleEntity, int firstResult, int maxResults) {
        return bookDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<Book> findAllBook(Book exampleEntity, Sorter sorter) {
        return bookDao.findAll(exampleEntity, sorter);
    }

    public List<Book> findAllBook(Book exampleEntity) {
        return bookDao.findAll(exampleEntity);
    }

    public List<Book> findAllBook(int firstResult, int maxResults, Sorter sorter) {
        return bookDao.findAll(firstResult, maxResults, sorter);
    }

    public List<Book> findAllBook(int firstResult, int maxResults) {
        return bookDao.findAll(firstResult, maxResults);
    }

    public List<Book> findAllBook(Sorter sorter) {
        return bookDao.findAll(sorter);
    }

    public List<Book> findAllBook() {
        return bookDao.findAll();
    }

    public int saveBook(Book entity) {
        return bookDao.save(entity);
    }

    public void updateBook(Book entity) {
        bookDao.update(entity);
    }

    public void deleteBook(Book entity) {
        bookDao.delete(entity);
    }

    public void deleteBook(int id) {
        bookDao.delete(id);
    }

    protected BookCommentDao bookCommentDao;

    public void setBookCommentDao(BookCommentDao bookCommentDao) {
        this.bookCommentDao = bookCommentDao;
    }

    public BookComment findBookCommentById(int id) {
        return bookCommentDao.findById(id);
    }

    public List<BookComment> findAllBookComment(BookComment exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return bookCommentDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<BookComment> findAllBookComment(BookComment exampleEntity, int firstResult, int maxResults) {
        return bookCommentDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<BookComment> findAllBookComment(BookComment exampleEntity, Sorter sorter) {
        return bookCommentDao.findAll(exampleEntity, sorter);
    }

    public List<BookComment> findAllBookComment(BookComment exampleEntity) {
        return bookCommentDao.findAll(exampleEntity);
    }

    public List<BookComment> findAllBookComment(int firstResult, int maxResults, Sorter sorter) {
        return bookCommentDao.findAll(firstResult, maxResults, sorter);
    }

    public List<BookComment> findAllBookComment(int firstResult, int maxResults) {
        return bookCommentDao.findAll(firstResult, maxResults);
    }

    public List<BookComment> findAllBookComment(Sorter sorter) {
        return bookCommentDao.findAll(sorter);
    }

    public List<BookComment> findAllBookComment() {
        return bookCommentDao.findAll();
    }

    public int saveBookComment(BookComment entity) {
        return bookCommentDao.save(entity);
    }

    public void updateBookComment(BookComment entity) {
        bookCommentDao.update(entity);
    }

    public void deleteBookComment(BookComment entity) {
        bookCommentDao.delete(entity);
    }

    public void deleteBookComment(int id) {
        bookCommentDao.delete(id);
    }

    protected BookExpectDao bookExpectDao;

    public void setBookExpectDao(BookExpectDao bookExpectDao) {
        this.bookExpectDao = bookExpectDao;
    }

    public BookExpect findBookExpectById(int id) {
        return bookExpectDao.findById(id);
    }

    public List<BookExpect> findAllBookExpect(BookExpect exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return bookExpectDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<BookExpect> findAllBookExpect(BookExpect exampleEntity, int firstResult, int maxResults) {
        return bookExpectDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<BookExpect> findAllBookExpect(BookExpect exampleEntity, Sorter sorter) {
        return bookExpectDao.findAll(exampleEntity, sorter);
    }

    public List<BookExpect> findAllBookExpect(BookExpect exampleEntity) {
        return bookExpectDao.findAll(exampleEntity);
    }

    public List<BookExpect> findAllBookExpect(int firstResult, int maxResults, Sorter sorter) {
        return bookExpectDao.findAll(firstResult, maxResults, sorter);
    }

    public List<BookExpect> findAllBookExpect(int firstResult, int maxResults) {
        return bookExpectDao.findAll(firstResult, maxResults);
    }

    public List<BookExpect> findAllBookExpect(Sorter sorter) {
        return bookExpectDao.findAll(sorter);
    }

    public List<BookExpect> findAllBookExpect() {
        return bookExpectDao.findAll();
    }

    public int saveBookExpect(BookExpect entity) {
        return bookExpectDao.save(entity);
    }

    public void updateBookExpect(BookExpect entity) {
        bookExpectDao.update(entity);
    }

    public void deleteBookExpect(BookExpect entity) {
        bookExpectDao.delete(entity);
    }

    public void deleteBookExpect(int id) {
        bookExpectDao.delete(id);
    }

    protected BookFavoriteDao bookFavoriteDao;

    public void setBookFavoriteDao(BookFavoriteDao bookFavoriteDao) {
        this.bookFavoriteDao = bookFavoriteDao;
    }

    public BookFavorite findBookFavoriteById(int id) {
        return bookFavoriteDao.findById(id);
    }

    public List<BookFavorite> findAllBookFavorite(BookFavorite exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return bookFavoriteDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<BookFavorite> findAllBookFavorite(BookFavorite exampleEntity, int firstResult, int maxResults) {
        return bookFavoriteDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<BookFavorite> findAllBookFavorite(BookFavorite exampleEntity, Sorter sorter) {
        return bookFavoriteDao.findAll(exampleEntity, sorter);
    }

    public List<BookFavorite> findAllBookFavorite(BookFavorite exampleEntity) {
        return bookFavoriteDao.findAll(exampleEntity);
    }

    public List<BookFavorite> findAllBookFavorite(int firstResult, int maxResults, Sorter sorter) {
        return bookFavoriteDao.findAll(firstResult, maxResults, sorter);
    }

    public List<BookFavorite> findAllBookFavorite(int firstResult, int maxResults) {
        return bookFavoriteDao.findAll(firstResult, maxResults);
    }

    public List<BookFavorite> findAllBookFavorite(Sorter sorter) {
        return bookFavoriteDao.findAll(sorter);
    }

    public List<BookFavorite> findAllBookFavorite() {
        return bookFavoriteDao.findAll();
    }

    public int saveBookFavorite(BookFavorite entity) {
        return bookFavoriteDao.save(entity);
    }

    public void updateBookFavorite(BookFavorite entity) {
        bookFavoriteDao.update(entity);
    }

    public void deleteBookFavorite(BookFavorite entity) {
        bookFavoriteDao.delete(entity);
    }

    public void deleteBookFavorite(int id) {
        bookFavoriteDao.delete(id);
    }

    protected BookRecommendDao bookRecommendDao;

    public void setBookRecommendDao(BookRecommendDao bookRecommendDao) {
        this.bookRecommendDao = bookRecommendDao;
    }

    public BookRecommend findBookRecommendById(int id) {
        return bookRecommendDao.findById(id);
    }

    public List<BookRecommend> findAllBookRecommend(BookRecommend exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return bookRecommendDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<BookRecommend> findAllBookRecommend(BookRecommend exampleEntity, int firstResult, int maxResults) {
        return bookRecommendDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<BookRecommend> findAllBookRecommend(BookRecommend exampleEntity, Sorter sorter) {
        return bookRecommendDao.findAll(exampleEntity, sorter);
    }

    public List<BookRecommend> findAllBookRecommend(BookRecommend exampleEntity) {
        return bookRecommendDao.findAll(exampleEntity);
    }

    public List<BookRecommend> findAllBookRecommend(int firstResult, int maxResults, Sorter sorter) {
        return bookRecommendDao.findAll(firstResult, maxResults, sorter);
    }

    public List<BookRecommend> findAllBookRecommend(int firstResult, int maxResults) {
        return bookRecommendDao.findAll(firstResult, maxResults);
    }

    public List<BookRecommend> findAllBookRecommend(Sorter sorter) {
        return bookRecommendDao.findAll(sorter);
    }

    public List<BookRecommend> findAllBookRecommend() {
        return bookRecommendDao.findAll();
    }

    public int saveBookRecommend(BookRecommend entity) {
        return bookRecommendDao.save(entity);
    }

    public void updateBookRecommend(BookRecommend entity) {
        bookRecommendDao.update(entity);
    }

    public void deleteBookRecommend(BookRecommend entity) {
        bookRecommendDao.delete(entity);
    }

    public void deleteBookRecommend(int id) {
        bookRecommendDao.delete(id);
    }

    protected BookTagDao bookTagDao;

    public void setBookTagDao(BookTagDao bookTagDao) {
        this.bookTagDao = bookTagDao;
    }

    public BookTag findBookTagById(int id) {
        return bookTagDao.findById(id);
    }

    public List<BookTag> findAllBookTag(BookTag exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return bookTagDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<BookTag> findAllBookTag(BookTag exampleEntity, int firstResult, int maxResults) {
        return bookTagDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<BookTag> findAllBookTag(BookTag exampleEntity, Sorter sorter) {
        return bookTagDao.findAll(exampleEntity, sorter);
    }

    public List<BookTag> findAllBookTag(BookTag exampleEntity) {
        return bookTagDao.findAll(exampleEntity);
    }

    public List<BookTag> findAllBookTag(int firstResult, int maxResults, Sorter sorter) {
        return bookTagDao.findAll(firstResult, maxResults, sorter);
    }

    public List<BookTag> findAllBookTag(int firstResult, int maxResults) {
        return bookTagDao.findAll(firstResult, maxResults);
    }

    public List<BookTag> findAllBookTag(Sorter sorter) {
        return bookTagDao.findAll(sorter);
    }

    public List<BookTag> findAllBookTag() {
        return bookTagDao.findAll();
    }

    public int saveBookTag(BookTag entity) {
        return bookTagDao.save(entity);
    }

    public void updateBookTag(BookTag entity) {
        bookTagDao.update(entity);
    }

    public void deleteBookTag(BookTag entity) {
        bookTagDao.delete(entity);
    }

    public void deleteBookTag(int id) {
        bookTagDao.delete(id);
    }

    protected BookTagRelationDao bookTagRelationDao;

    public void setBookTagRelationDao(BookTagRelationDao bookTagRelationDao) {
        this.bookTagRelationDao = bookTagRelationDao;
    }

    public BookTagRelation findBookTagRelationById(int id) {
        return bookTagRelationDao.findById(id);
    }

    public List<BookTagRelation> findAllBookTagRelation(BookTagRelation exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return bookTagRelationDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<BookTagRelation> findAllBookTagRelation(BookTagRelation exampleEntity, int firstResult, int maxResults) {
        return bookTagRelationDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<BookTagRelation> findAllBookTagRelation(BookTagRelation exampleEntity, Sorter sorter) {
        return bookTagRelationDao.findAll(exampleEntity, sorter);
    }

    public List<BookTagRelation> findAllBookTagRelation(BookTagRelation exampleEntity) {
        return bookTagRelationDao.findAll(exampleEntity);
    }

    public List<BookTagRelation> findAllBookTagRelation(int firstResult, int maxResults, Sorter sorter) {
        return bookTagRelationDao.findAll(firstResult, maxResults, sorter);
    }

    public List<BookTagRelation> findAllBookTagRelation(int firstResult, int maxResults) {
        return bookTagRelationDao.findAll(firstResult, maxResults);
    }

    public List<BookTagRelation> findAllBookTagRelation(Sorter sorter) {
        return bookTagRelationDao.findAll(sorter);
    }

    public List<BookTagRelation> findAllBookTagRelation() {
        return bookTagRelationDao.findAll();
    }

    public int saveBookTagRelation(BookTagRelation entity) {
        return bookTagRelationDao.save(entity);
    }

    public void updateBookTagRelation(BookTagRelation entity) {
        bookTagRelationDao.update(entity);
    }

    public void deleteBookTagRelation(BookTagRelation entity) {
        bookTagRelationDao.delete(entity);
    }

    public void deleteBookTagRelation(int id) {
        bookTagRelationDao.delete(id);
    }

    protected BookVersionDao bookVersionDao;

    public void setBookVersionDao(BookVersionDao bookVersionDao) {
        this.bookVersionDao = bookVersionDao;
    }

    public BookVersion findBookVersionById(int id) {
        return bookVersionDao.findById(id);
    }

    public List<BookVersion> findAllBookVersion(BookVersion exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return bookVersionDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<BookVersion> findAllBookVersion(BookVersion exampleEntity, int firstResult, int maxResults) {
        return bookVersionDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<BookVersion> findAllBookVersion(BookVersion exampleEntity, Sorter sorter) {
        return bookVersionDao.findAll(exampleEntity, sorter);
    }

    public List<BookVersion> findAllBookVersion(BookVersion exampleEntity) {
        return bookVersionDao.findAll(exampleEntity);
    }

    public List<BookVersion> findAllBookVersion(int firstResult, int maxResults, Sorter sorter) {
        return bookVersionDao.findAll(firstResult, maxResults, sorter);
    }

    public List<BookVersion> findAllBookVersion(int firstResult, int maxResults) {
        return bookVersionDao.findAll(firstResult, maxResults);
    }

    public List<BookVersion> findAllBookVersion(Sorter sorter) {
        return bookVersionDao.findAll(sorter);
    }

    public List<BookVersion> findAllBookVersion() {
        return bookVersionDao.findAll();
    }

    public int saveBookVersion(BookVersion entity) {
        return bookVersionDao.save(entity);
    }

    public void updateBookVersion(BookVersion entity) {
        bookVersionDao.update(entity);
    }

    public void deleteBookVersion(BookVersion entity) {
        bookVersionDao.delete(entity);
    }

    public void deleteBookVersion(int id) {
        bookVersionDao.delete(id);
    }

    protected ChapterDao chapterDao;

    public void setChapterDao(ChapterDao chapterDao) {
        this.chapterDao = chapterDao;
    }

    public Chapter findChapterById(int id) {
        return chapterDao.findById(id);
    }

    public List<Chapter> findAllChapter(Chapter exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return chapterDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<Chapter> findAllChapter(Chapter exampleEntity, int firstResult, int maxResults) {
        return chapterDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<Chapter> findAllChapter(Chapter exampleEntity, Sorter sorter) {
        return chapterDao.findAll(exampleEntity, sorter);
    }

    public List<Chapter> findAllChapter(Chapter exampleEntity) {
        return chapterDao.findAll(exampleEntity);
    }

    public List<Chapter> findAllChapter(int firstResult, int maxResults, Sorter sorter) {
        return chapterDao.findAll(firstResult, maxResults, sorter);
    }

    public List<Chapter> findAllChapter(int firstResult, int maxResults) {
        return chapterDao.findAll(firstResult, maxResults);
    }

    public List<Chapter> findAllChapter(Sorter sorter) {
        return chapterDao.findAll(sorter);
    }

    public List<Chapter> findAllChapter() {
        return chapterDao.findAll();
    }

    public int saveChapter(Chapter entity) {
        return chapterDao.save(entity);
    }

    public void updateChapter(Chapter entity) {
        chapterDao.update(entity);
    }

    public void deleteChapter(Chapter entity) {
        chapterDao.delete(entity);
    }

    public void deleteChapter(int id) {
        chapterDao.delete(id);
    }

    protected ChapterVersionDao chapterVersionDao;

    public void setChapterVersionDao(ChapterVersionDao chapterVersionDao) {
        this.chapterVersionDao = chapterVersionDao;
    }

    public ChapterVersion findChapterVersionById(int id) {
        return chapterVersionDao.findById(id);
    }

    public List<ChapterVersion> findAllChapterVersion(ChapterVersion exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return chapterVersionDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<ChapterVersion> findAllChapterVersion(ChapterVersion exampleEntity, int firstResult, int maxResults) {
        return chapterVersionDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<ChapterVersion> findAllChapterVersion(ChapterVersion exampleEntity, Sorter sorter) {
        return chapterVersionDao.findAll(exampleEntity, sorter);
    }

    public List<ChapterVersion> findAllChapterVersion(ChapterVersion exampleEntity) {
        return chapterVersionDao.findAll(exampleEntity);
    }

    public List<ChapterVersion> findAllChapterVersion(int firstResult, int maxResults, Sorter sorter) {
        return chapterVersionDao.findAll(firstResult, maxResults, sorter);
    }

    public List<ChapterVersion> findAllChapterVersion(int firstResult, int maxResults) {
        return chapterVersionDao.findAll(firstResult, maxResults);
    }

    public List<ChapterVersion> findAllChapterVersion(Sorter sorter) {
        return chapterVersionDao.findAll(sorter);
    }

    public List<ChapterVersion> findAllChapterVersion() {
        return chapterVersionDao.findAll();
    }

    public int saveChapterVersion(ChapterVersion entity) {
        return chapterVersionDao.save(entity);
    }

    public void updateChapterVersion(ChapterVersion entity) {
        chapterVersionDao.update(entity);
    }

    public void deleteChapterVersion(ChapterVersion entity) {
        chapterVersionDao.delete(entity);
    }

    public void deleteChapterVersion(int id) {
        chapterVersionDao.delete(id);
    }

    protected DeployDao deployDao;

    public void setDeployDao(DeployDao deployDao) {
        this.deployDao = deployDao;
    }

    public Deploy findDeployById(int id) {
        return deployDao.findById(id);
    }

    public List<Deploy> findAllDeploy(Deploy exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return deployDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<Deploy> findAllDeploy(Deploy exampleEntity, int firstResult, int maxResults) {
        return deployDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<Deploy> findAllDeploy(Deploy exampleEntity, Sorter sorter) {
        return deployDao.findAll(exampleEntity, sorter);
    }

    public List<Deploy> findAllDeploy(Deploy exampleEntity) {
        return deployDao.findAll(exampleEntity);
    }

    public List<Deploy> findAllDeploy(int firstResult, int maxResults, Sorter sorter) {
        return deployDao.findAll(firstResult, maxResults, sorter);
    }

    public List<Deploy> findAllDeploy(int firstResult, int maxResults) {
        return deployDao.findAll(firstResult, maxResults);
    }

    public List<Deploy> findAllDeploy(Sorter sorter) {
        return deployDao.findAll(sorter);
    }

    public List<Deploy> findAllDeploy() {
        return deployDao.findAll();
    }

    public int saveDeploy(Deploy entity) {
        return deployDao.save(entity);
    }

    public void updateDeploy(Deploy entity) {
        deployDao.update(entity);
    }

    public void deleteDeploy(Deploy entity) {
        deployDao.delete(entity);
    }

    public void deleteDeploy(int id) {
        deployDao.delete(id);
    }

    protected ErrorReportDao errorReportDao;

    public void setErrorReportDao(ErrorReportDao errorReportDao) {
        this.errorReportDao = errorReportDao;
    }

    public ErrorReport findErrorReportById(int id) {
        return errorReportDao.findById(id);
    }

    public List<ErrorReport> findAllErrorReport(ErrorReport exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return errorReportDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<ErrorReport> findAllErrorReport(ErrorReport exampleEntity, int firstResult, int maxResults) {
        return errorReportDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<ErrorReport> findAllErrorReport(ErrorReport exampleEntity, Sorter sorter) {
        return errorReportDao.findAll(exampleEntity, sorter);
    }

    public List<ErrorReport> findAllErrorReport(ErrorReport exampleEntity) {
        return errorReportDao.findAll(exampleEntity);
    }

    public List<ErrorReport> findAllErrorReport(int firstResult, int maxResults, Sorter sorter) {
        return errorReportDao.findAll(firstResult, maxResults, sorter);
    }

    public List<ErrorReport> findAllErrorReport(int firstResult, int maxResults) {
        return errorReportDao.findAll(firstResult, maxResults);
    }

    public List<ErrorReport> findAllErrorReport(Sorter sorter) {
        return errorReportDao.findAll(sorter);
    }

    public List<ErrorReport> findAllErrorReport() {
        return errorReportDao.findAll();
    }

    public int saveErrorReport(ErrorReport entity) {
        return errorReportDao.save(entity);
    }

    public void updateErrorReport(ErrorReport entity) {
        errorReportDao.update(entity);
    }

    public void deleteErrorReport(ErrorReport entity) {
        errorReportDao.delete(entity);
    }

    public void deleteErrorReport(int id) {
        errorReportDao.delete(id);
    }

    protected FriendDao friendDao;

    public void setFriendDao(FriendDao friendDao) {
        this.friendDao = friendDao;
    }

    public Friend findFriendById(int id) {
        return friendDao.findById(id);
    }

    public List<Friend> findAllFriend(Friend exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return friendDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<Friend> findAllFriend(Friend exampleEntity, int firstResult, int maxResults) {
        return friendDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<Friend> findAllFriend(Friend exampleEntity, Sorter sorter) {
        return friendDao.findAll(exampleEntity, sorter);
    }

    public List<Friend> findAllFriend(Friend exampleEntity) {
        return friendDao.findAll(exampleEntity);
    }

    public List<Friend> findAllFriend(int firstResult, int maxResults, Sorter sorter) {
        return friendDao.findAll(firstResult, maxResults, sorter);
    }

    public List<Friend> findAllFriend(int firstResult, int maxResults) {
        return friendDao.findAll(firstResult, maxResults);
    }

    public List<Friend> findAllFriend(Sorter sorter) {
        return friendDao.findAll(sorter);
    }

    public List<Friend> findAllFriend() {
        return friendDao.findAll();
    }

    public int saveFriend(Friend entity) {
        return friendDao.save(entity);
    }

    public void updateFriend(Friend entity) {
        friendDao.update(entity);
    }

    public void deleteFriend(Friend entity) {
        friendDao.delete(entity);
    }

    public void deleteFriend(int id) {
        friendDao.delete(id);
    }

    protected FriendConfirmDao friendConfirmDao;

    public void setFriendConfirmDao(FriendConfirmDao friendConfirmDao) {
        this.friendConfirmDao = friendConfirmDao;
    }

    public FriendConfirm findFriendConfirmById(int id) {
        return friendConfirmDao.findById(id);
    }

    public List<FriendConfirm> findAllFriendConfirm(FriendConfirm exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return friendConfirmDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<FriendConfirm> findAllFriendConfirm(FriendConfirm exampleEntity, int firstResult, int maxResults) {
        return friendConfirmDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<FriendConfirm> findAllFriendConfirm(FriendConfirm exampleEntity, Sorter sorter) {
        return friendConfirmDao.findAll(exampleEntity, sorter);
    }

    public List<FriendConfirm> findAllFriendConfirm(FriendConfirm exampleEntity) {
        return friendConfirmDao.findAll(exampleEntity);
    }

    public List<FriendConfirm> findAllFriendConfirm(int firstResult, int maxResults, Sorter sorter) {
        return friendConfirmDao.findAll(firstResult, maxResults, sorter);
    }

    public List<FriendConfirm> findAllFriendConfirm(int firstResult, int maxResults) {
        return friendConfirmDao.findAll(firstResult, maxResults);
    }

    public List<FriendConfirm> findAllFriendConfirm(Sorter sorter) {
        return friendConfirmDao.findAll(sorter);
    }

    public List<FriendConfirm> findAllFriendConfirm() {
        return friendConfirmDao.findAll();
    }

    public int saveFriendConfirm(FriendConfirm entity) {
        return friendConfirmDao.save(entity);
    }

    public void updateFriendConfirm(FriendConfirm entity) {
        friendConfirmDao.update(entity);
    }

    public void deleteFriendConfirm(FriendConfirm entity) {
        friendConfirmDao.delete(entity);
    }

    public void deleteFriendConfirm(int id) {
        friendConfirmDao.delete(id);
    }

    protected GroupDao groupDao;

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public Group findGroupById(int id) {
        return groupDao.findById(id);
    }

    public List<Group> findAllGroup(Group exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return groupDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<Group> findAllGroup(Group exampleEntity, int firstResult, int maxResults) {
        return groupDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<Group> findAllGroup(Group exampleEntity, Sorter sorter) {
        return groupDao.findAll(exampleEntity, sorter);
    }

    public List<Group> findAllGroup(Group exampleEntity) {
        return groupDao.findAll(exampleEntity);
    }

    public List<Group> findAllGroup(int firstResult, int maxResults, Sorter sorter) {
        return groupDao.findAll(firstResult, maxResults, sorter);
    }

    public List<Group> findAllGroup(int firstResult, int maxResults) {
        return groupDao.findAll(firstResult, maxResults);
    }

    public List<Group> findAllGroup(Sorter sorter) {
        return groupDao.findAll(sorter);
    }

    public List<Group> findAllGroup() {
        return groupDao.findAll();
    }

    public int saveGroup(Group entity) {
        return groupDao.save(entity);
    }

    public void updateGroup(Group entity) {
        groupDao.update(entity);
    }

    public void deleteGroup(Group entity) {
        groupDao.delete(entity);
    }

    public void deleteGroup(int id) {
        groupDao.delete(id);
    }

    protected GroupAdminDao groupAdminDao;

    public void setGroupAdminDao(GroupAdminDao groupAdminDao) {
        this.groupAdminDao = groupAdminDao;
    }

    public GroupAdmin findGroupAdminById(int id) {
        return groupAdminDao.findById(id);
    }

    public List<GroupAdmin> findAllGroupAdmin(GroupAdmin exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return groupAdminDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<GroupAdmin> findAllGroupAdmin(GroupAdmin exampleEntity, int firstResult, int maxResults) {
        return groupAdminDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<GroupAdmin> findAllGroupAdmin(GroupAdmin exampleEntity, Sorter sorter) {
        return groupAdminDao.findAll(exampleEntity, sorter);
    }

    public List<GroupAdmin> findAllGroupAdmin(GroupAdmin exampleEntity) {
        return groupAdminDao.findAll(exampleEntity);
    }

    public List<GroupAdmin> findAllGroupAdmin(int firstResult, int maxResults, Sorter sorter) {
        return groupAdminDao.findAll(firstResult, maxResults, sorter);
    }

    public List<GroupAdmin> findAllGroupAdmin(int firstResult, int maxResults) {
        return groupAdminDao.findAll(firstResult, maxResults);
    }

    public List<GroupAdmin> findAllGroupAdmin(Sorter sorter) {
        return groupAdminDao.findAll(sorter);
    }

    public List<GroupAdmin> findAllGroupAdmin() {
        return groupAdminDao.findAll();
    }

    public int saveGroupAdmin(GroupAdmin entity) {
        return groupAdminDao.save(entity);
    }

    public void updateGroupAdmin(GroupAdmin entity) {
        groupAdminDao.update(entity);
    }

    public void deleteGroupAdmin(GroupAdmin entity) {
        groupAdminDao.delete(entity);
    }

    public void deleteGroupAdmin(int id) {
        groupAdminDao.delete(id);
    }

    protected GroupMemberDao groupMemberDao;

    public void setGroupMemberDao(GroupMemberDao groupMemberDao) {
        this.groupMemberDao = groupMemberDao;
    }

    public GroupMember findGroupMemberById(int id) {
        return groupMemberDao.findById(id);
    }

    public List<GroupMember> findAllGroupMember(GroupMember exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return groupMemberDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<GroupMember> findAllGroupMember(GroupMember exampleEntity, int firstResult, int maxResults) {
        return groupMemberDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<GroupMember> findAllGroupMember(GroupMember exampleEntity, Sorter sorter) {
        return groupMemberDao.findAll(exampleEntity, sorter);
    }

    public List<GroupMember> findAllGroupMember(GroupMember exampleEntity) {
        return groupMemberDao.findAll(exampleEntity);
    }

    public List<GroupMember> findAllGroupMember(int firstResult, int maxResults, Sorter sorter) {
        return groupMemberDao.findAll(firstResult, maxResults, sorter);
    }

    public List<GroupMember> findAllGroupMember(int firstResult, int maxResults) {
        return groupMemberDao.findAll(firstResult, maxResults);
    }

    public List<GroupMember> findAllGroupMember(Sorter sorter) {
        return groupMemberDao.findAll(sorter);
    }

    public List<GroupMember> findAllGroupMember() {
        return groupMemberDao.findAll();
    }

    public int saveGroupMember(GroupMember entity) {
        return groupMemberDao.save(entity);
    }

    public void updateGroupMember(GroupMember entity) {
        groupMemberDao.update(entity);
    }

    public void deleteGroupMember(GroupMember entity) {
        groupMemberDao.delete(entity);
    }

    public void deleteGroupMember(int id) {
        groupMemberDao.delete(id);
    }

    protected GroupMemberConfirmDao groupMemberConfirmDao;

    public void setGroupMemberConfirmDao(GroupMemberConfirmDao groupMemberConfirmDao) {
        this.groupMemberConfirmDao = groupMemberConfirmDao;
    }

    public GroupMemberConfirm findGroupMemberConfirmById(int id) {
        return groupMemberConfirmDao.findById(id);
    }

    public List<GroupMemberConfirm> findAllGroupMemberConfirm(GroupMemberConfirm exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return groupMemberConfirmDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<GroupMemberConfirm> findAllGroupMemberConfirm(GroupMemberConfirm exampleEntity, int firstResult, int maxResults) {
        return groupMemberConfirmDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<GroupMemberConfirm> findAllGroupMemberConfirm(GroupMemberConfirm exampleEntity, Sorter sorter) {
        return groupMemberConfirmDao.findAll(exampleEntity, sorter);
    }

    public List<GroupMemberConfirm> findAllGroupMemberConfirm(GroupMemberConfirm exampleEntity) {
        return groupMemberConfirmDao.findAll(exampleEntity);
    }

    public List<GroupMemberConfirm> findAllGroupMemberConfirm(int firstResult, int maxResults, Sorter sorter) {
        return groupMemberConfirmDao.findAll(firstResult, maxResults, sorter);
    }

    public List<GroupMemberConfirm> findAllGroupMemberConfirm(int firstResult, int maxResults) {
        return groupMemberConfirmDao.findAll(firstResult, maxResults);
    }

    public List<GroupMemberConfirm> findAllGroupMemberConfirm(Sorter sorter) {
        return groupMemberConfirmDao.findAll(sorter);
    }

    public List<GroupMemberConfirm> findAllGroupMemberConfirm() {
        return groupMemberConfirmDao.findAll();
    }

    public int saveGroupMemberConfirm(GroupMemberConfirm entity) {
        return groupMemberConfirmDao.save(entity);
    }

    public void updateGroupMemberConfirm(GroupMemberConfirm entity) {
        groupMemberConfirmDao.update(entity);
    }

    public void deleteGroupMemberConfirm(GroupMemberConfirm entity) {
        groupMemberConfirmDao.delete(entity);
    }

    public void deleteGroupMemberConfirm(int id) {
        groupMemberConfirmDao.delete(id);
    }

    protected GroupTopicDao groupTopicDao;

    public void setGroupTopicDao(GroupTopicDao groupTopicDao) {
        this.groupTopicDao = groupTopicDao;
    }

    public GroupTopic findGroupTopicById(int id) {
        return groupTopicDao.findById(id);
    }

    public List<GroupTopic> findAllGroupTopic(GroupTopic exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return groupTopicDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<GroupTopic> findAllGroupTopic(GroupTopic exampleEntity, int firstResult, int maxResults) {
        return groupTopicDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<GroupTopic> findAllGroupTopic(GroupTopic exampleEntity, Sorter sorter) {
        return groupTopicDao.findAll(exampleEntity, sorter);
    }

    public List<GroupTopic> findAllGroupTopic(GroupTopic exampleEntity) {
        return groupTopicDao.findAll(exampleEntity);
    }

    public List<GroupTopic> findAllGroupTopic(int firstResult, int maxResults, Sorter sorter) {
        return groupTopicDao.findAll(firstResult, maxResults, sorter);
    }

    public List<GroupTopic> findAllGroupTopic(int firstResult, int maxResults) {
        return groupTopicDao.findAll(firstResult, maxResults);
    }

    public List<GroupTopic> findAllGroupTopic(Sorter sorter) {
        return groupTopicDao.findAll(sorter);
    }

    public List<GroupTopic> findAllGroupTopic() {
        return groupTopicDao.findAll();
    }

    public int saveGroupTopic(GroupTopic entity) {
        return groupTopicDao.save(entity);
    }

    public void updateGroupTopic(GroupTopic entity) {
        groupTopicDao.update(entity);
    }

    public void deleteGroupTopic(GroupTopic entity) {
        groupTopicDao.delete(entity);
    }

    public void deleteGroupTopic(int id) {
        groupTopicDao.delete(id);
    }

    protected GroupTopicCommentDao groupTopicCommentDao;

    public void setGroupTopicCommentDao(GroupTopicCommentDao groupTopicCommentDao) {
        this.groupTopicCommentDao = groupTopicCommentDao;
    }

    public GroupTopicComment findGroupTopicCommentById(int id) {
        return groupTopicCommentDao.findById(id);
    }

    public List<GroupTopicComment> findAllGroupTopicComment(GroupTopicComment exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return groupTopicCommentDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<GroupTopicComment> findAllGroupTopicComment(GroupTopicComment exampleEntity, int firstResult, int maxResults) {
        return groupTopicCommentDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<GroupTopicComment> findAllGroupTopicComment(GroupTopicComment exampleEntity, Sorter sorter) {
        return groupTopicCommentDao.findAll(exampleEntity, sorter);
    }

    public List<GroupTopicComment> findAllGroupTopicComment(GroupTopicComment exampleEntity) {
        return groupTopicCommentDao.findAll(exampleEntity);
    }

    public List<GroupTopicComment> findAllGroupTopicComment(int firstResult, int maxResults, Sorter sorter) {
        return groupTopicCommentDao.findAll(firstResult, maxResults, sorter);
    }

    public List<GroupTopicComment> findAllGroupTopicComment(int firstResult, int maxResults) {
        return groupTopicCommentDao.findAll(firstResult, maxResults);
    }

    public List<GroupTopicComment> findAllGroupTopicComment(Sorter sorter) {
        return groupTopicCommentDao.findAll(sorter);
    }

    public List<GroupTopicComment> findAllGroupTopicComment() {
        return groupTopicCommentDao.findAll();
    }

    public int saveGroupTopicComment(GroupTopicComment entity) {
        return groupTopicCommentDao.save(entity);
    }

    public void updateGroupTopicComment(GroupTopicComment entity) {
        groupTopicCommentDao.update(entity);
    }

    public void deleteGroupTopicComment(GroupTopicComment entity) {
        groupTopicCommentDao.delete(entity);
    }

    public void deleteGroupTopicComment(int id) {
        groupTopicCommentDao.delete(id);
    }

    protected MemberDao memberDao;

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findMemberById(int id) {
        return memberDao.findById(id);
    }

    public List<Member> findAllMember(Member exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return memberDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<Member> findAllMember(Member exampleEntity, int firstResult, int maxResults) {
        return memberDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<Member> findAllMember(Member exampleEntity, Sorter sorter) {
        return memberDao.findAll(exampleEntity, sorter);
    }

    public List<Member> findAllMember(Member exampleEntity) {
        return memberDao.findAll(exampleEntity);
    }

    public List<Member> findAllMember(int firstResult, int maxResults, Sorter sorter) {
        return memberDao.findAll(firstResult, maxResults, sorter);
    }

    public List<Member> findAllMember(int firstResult, int maxResults) {
        return memberDao.findAll(firstResult, maxResults);
    }

    public List<Member> findAllMember(Sorter sorter) {
        return memberDao.findAll(sorter);
    }

    public List<Member> findAllMember() {
        return memberDao.findAll();
    }

    public int saveMember(Member entity) {
        return memberDao.save(entity);
    }

    public void updateMember(Member entity) {
        memberDao.update(entity);
    }

    public void deleteMember(Member entity) {
        memberDao.delete(entity);
    }

    public void deleteMember(int id) {
        memberDao.delete(id);
    }

    protected MemberConfirmDao memberConfirmDao;

    public void setMemberConfirmDao(MemberConfirmDao memberConfirmDao) {
        this.memberConfirmDao = memberConfirmDao;
    }

    public MemberConfirm findMemberConfirmById(int id) {
        return memberConfirmDao.findById(id);
    }

    public List<MemberConfirm> findAllMemberConfirm(MemberConfirm exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return memberConfirmDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<MemberConfirm> findAllMemberConfirm(MemberConfirm exampleEntity, int firstResult, int maxResults) {
        return memberConfirmDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<MemberConfirm> findAllMemberConfirm(MemberConfirm exampleEntity, Sorter sorter) {
        return memberConfirmDao.findAll(exampleEntity, sorter);
    }

    public List<MemberConfirm> findAllMemberConfirm(MemberConfirm exampleEntity) {
        return memberConfirmDao.findAll(exampleEntity);
    }

    public List<MemberConfirm> findAllMemberConfirm(int firstResult, int maxResults, Sorter sorter) {
        return memberConfirmDao.findAll(firstResult, maxResults, sorter);
    }

    public List<MemberConfirm> findAllMemberConfirm(int firstResult, int maxResults) {
        return memberConfirmDao.findAll(firstResult, maxResults);
    }

    public List<MemberConfirm> findAllMemberConfirm(Sorter sorter) {
        return memberConfirmDao.findAll(sorter);
    }

    public List<MemberConfirm> findAllMemberConfirm() {
        return memberConfirmDao.findAll();
    }

    public int saveMemberConfirm(MemberConfirm entity) {
        return memberConfirmDao.save(entity);
    }

    public void updateMemberConfirm(MemberConfirm entity) {
        memberConfirmDao.update(entity);
    }

    public void deleteMemberConfirm(MemberConfirm entity) {
        memberConfirmDao.delete(entity);
    }

    public void deleteMemberConfirm(int id) {
        memberConfirmDao.delete(id);
    }

    protected MemberExperienceDao memberExperienceDao;

    public void setMemberExperienceDao(MemberExperienceDao memberExperienceDao) {
        this.memberExperienceDao = memberExperienceDao;
    }

    public MemberExperience findMemberExperienceById(int id) {
        return memberExperienceDao.findById(id);
    }

    public List<MemberExperience> findAllMemberExperience(MemberExperience exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return memberExperienceDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<MemberExperience> findAllMemberExperience(MemberExperience exampleEntity, int firstResult, int maxResults) {
        return memberExperienceDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<MemberExperience> findAllMemberExperience(MemberExperience exampleEntity, Sorter sorter) {
        return memberExperienceDao.findAll(exampleEntity, sorter);
    }

    public List<MemberExperience> findAllMemberExperience(MemberExperience exampleEntity) {
        return memberExperienceDao.findAll(exampleEntity);
    }

    public List<MemberExperience> findAllMemberExperience(int firstResult, int maxResults, Sorter sorter) {
        return memberExperienceDao.findAll(firstResult, maxResults, sorter);
    }

    public List<MemberExperience> findAllMemberExperience(int firstResult, int maxResults) {
        return memberExperienceDao.findAll(firstResult, maxResults);
    }

    public List<MemberExperience> findAllMemberExperience(Sorter sorter) {
        return memberExperienceDao.findAll(sorter);
    }

    public List<MemberExperience> findAllMemberExperience() {
        return memberExperienceDao.findAll();
    }

    public int saveMemberExperience(MemberExperience entity) {
        return memberExperienceDao.save(entity);
    }

    public void updateMemberExperience(MemberExperience entity) {
        memberExperienceDao.update(entity);
    }

    public void deleteMemberExperience(MemberExperience entity) {
        memberExperienceDao.delete(entity);
    }

    public void deleteMemberExperience(int id) {
        memberExperienceDao.delete(id);
    }

    protected MessageDao messageDao;

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public Message findMessageById(int id) {
        return messageDao.findById(id);
    }

    public List<Message> findAllMessage(Message exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return messageDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<Message> findAllMessage(Message exampleEntity, int firstResult, int maxResults) {
        return messageDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<Message> findAllMessage(Message exampleEntity, Sorter sorter) {
        return messageDao.findAll(exampleEntity, sorter);
    }

    public List<Message> findAllMessage(Message exampleEntity) {
        return messageDao.findAll(exampleEntity);
    }

    public List<Message> findAllMessage(int firstResult, int maxResults, Sorter sorter) {
        return messageDao.findAll(firstResult, maxResults, sorter);
    }

    public List<Message> findAllMessage(int firstResult, int maxResults) {
        return messageDao.findAll(firstResult, maxResults);
    }

    public List<Message> findAllMessage(Sorter sorter) {
        return messageDao.findAll(sorter);
    }

    public List<Message> findAllMessage() {
        return messageDao.findAll();
    }

    public int saveMessage(Message entity) {
        return messageDao.save(entity);
    }

    public void updateMessage(Message entity) {
        messageDao.update(entity);
    }

    public void deleteMessage(Message entity) {
        messageDao.delete(entity);
    }

    public void deleteMessage(int id) {
        messageDao.delete(id);
    }

    protected RecordTaskDao recordTaskDao;

    public void setRecordTaskDao(RecordTaskDao recordTaskDao) {
        this.recordTaskDao = recordTaskDao;
    }

    public RecordTask findRecordTaskById(int id) {
        return recordTaskDao.findById(id);
    }

    public List<RecordTask> findAllRecordTask(RecordTask exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return recordTaskDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<RecordTask> findAllRecordTask(RecordTask exampleEntity, int firstResult, int maxResults) {
        return recordTaskDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<RecordTask> findAllRecordTask(RecordTask exampleEntity, Sorter sorter) {
        return recordTaskDao.findAll(exampleEntity, sorter);
    }

    public List<RecordTask> findAllRecordTask(RecordTask exampleEntity) {
        return recordTaskDao.findAll(exampleEntity);
    }

    public List<RecordTask> findAllRecordTask(int firstResult, int maxResults, Sorter sorter) {
        return recordTaskDao.findAll(firstResult, maxResults, sorter);
    }

    public List<RecordTask> findAllRecordTask(int firstResult, int maxResults) {
        return recordTaskDao.findAll(firstResult, maxResults);
    }

    public List<RecordTask> findAllRecordTask(Sorter sorter) {
        return recordTaskDao.findAll(sorter);
    }

    public List<RecordTask> findAllRecordTask() {
        return recordTaskDao.findAll();
    }

    public int saveRecordTask(RecordTask entity) {
        return recordTaskDao.save(entity);
    }

    public void updateRecordTask(RecordTask entity) {
        recordTaskDao.update(entity);
    }

    public void deleteRecordTask(RecordTask entity) {
        recordTaskDao.delete(entity);
    }

    public void deleteRecordTask(int id) {
        recordTaskDao.delete(id);
    }

    protected SyncDao syncDao;

    public void setSyncDao(SyncDao syncDao) {
        this.syncDao = syncDao;
    }

    public Sync findSyncById(int id) {
        return syncDao.findById(id);
    }

    public List<Sync> findAllSync(Sync exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return syncDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<Sync> findAllSync(Sync exampleEntity, int firstResult, int maxResults) {
        return syncDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<Sync> findAllSync(Sync exampleEntity, Sorter sorter) {
        return syncDao.findAll(exampleEntity, sorter);
    }

    public List<Sync> findAllSync(Sync exampleEntity) {
        return syncDao.findAll(exampleEntity);
    }

    public List<Sync> findAllSync(int firstResult, int maxResults, Sorter sorter) {
        return syncDao.findAll(firstResult, maxResults, sorter);
    }

    public List<Sync> findAllSync(int firstResult, int maxResults) {
        return syncDao.findAll(firstResult, maxResults);
    }

    public List<Sync> findAllSync(Sorter sorter) {
        return syncDao.findAll(sorter);
    }

    public List<Sync> findAllSync() {
        return syncDao.findAll();
    }

    public int saveSync(Sync entity) {
        return syncDao.save(entity);
    }

    public void updateSync(Sync entity) {
        syncDao.update(entity);
    }

    public void deleteSync(Sync entity) {
        syncDao.delete(entity);
    }

    public void deleteSync(int id) {
        syncDao.delete(id);
    }

    protected SearchLogDao searchLogDao;

    public void setSearchLogDao(SearchLogDao searchLogDao) {
        this.searchLogDao = searchLogDao;
    }

    public SearchLog findSearchLogById(int id) {
        return searchLogDao.findById(id);
    }

    public List<SearchLog> findAllSearchLog(SearchLog exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return searchLogDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<SearchLog> findAllSearchLog(SearchLog exampleEntity, int firstResult, int maxResults) {
        return searchLogDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<SearchLog> findAllSearchLog(SearchLog exampleEntity, Sorter sorter) {
        return searchLogDao.findAll(exampleEntity, sorter);
    }

    public List<SearchLog> findAllSearchLog(SearchLog exampleEntity) {
        return searchLogDao.findAll(exampleEntity);
    }

    public List<SearchLog> findAllSearchLog(int firstResult, int maxResults, Sorter sorter) {
        return searchLogDao.findAll(firstResult, maxResults, sorter);
    }

    public List<SearchLog> findAllSearchLog(int firstResult, int maxResults) {
        return searchLogDao.findAll(firstResult, maxResults);
    }

    public List<SearchLog> findAllSearchLog(Sorter sorter) {
        return searchLogDao.findAll(sorter);
    }

    public List<SearchLog> findAllSearchLog() {
        return searchLogDao.findAll();
    }

    public int saveSearchLog(SearchLog entity) {
        return searchLogDao.save(entity);
    }

    public void updateSearchLog(SearchLog entity) {
        searchLogDao.update(entity);
    }

    public void deleteSearchLog(SearchLog entity) {
        searchLogDao.delete(entity);
    }

    public void deleteSearchLog(int id) {
        searchLogDao.delete(id);
    }

    protected RecordAlbumDao recordAlbumDao;

    public void setRecordAlbumDao(RecordAlbumDao recordAlbumDao) {
        this.recordAlbumDao = recordAlbumDao;
    }

    public RecordAlbum findRecordAlbumById(int id) {
        return recordAlbumDao.findById(id);
    }

    public List<RecordAlbum> findAllRecordAlbum(RecordAlbum exampleEntity, int firstResult, int maxResults, Sorter sorter) {
        return recordAlbumDao.findAll(exampleEntity, firstResult, maxResults, sorter);
    }

    public List<RecordAlbum> findAllRecordAlbum(RecordAlbum exampleEntity, int firstResult, int maxResults) {
        return recordAlbumDao.findAll(exampleEntity, firstResult, maxResults);
    }

    public List<RecordAlbum> findAllRecordAlbum(RecordAlbum exampleEntity, Sorter sorter) {
        return recordAlbumDao.findAll(exampleEntity, sorter);
    }

    public List<RecordAlbum> findAllRecordAlbum(RecordAlbum exampleEntity) {
        return recordAlbumDao.findAll(exampleEntity);
    }

    public List<RecordAlbum> findAllRecordAlbum(int firstResult, int maxResults, Sorter sorter) {
        return recordAlbumDao.findAll(firstResult, maxResults, sorter);
    }

    public List<RecordAlbum> findAllRecordAlbum(int firstResult, int maxResults) {
        return recordAlbumDao.findAll(firstResult, maxResults);
    }

    public List<RecordAlbum> findAllRecordAlbum(Sorter sorter) {
        return recordAlbumDao.findAll(sorter);
    }

    public List<RecordAlbum> findAllRecordAlbum() {
        return recordAlbumDao.findAll();
    }

    public int saveRecordAlbum(RecordAlbum entity) {
        return recordAlbumDao.save(entity);
    }

    public void updateRecordAlbum(RecordAlbum entity) {
        recordAlbumDao.update(entity);
    }

    public void deleteRecordAlbum(RecordAlbum entity) {
        recordAlbumDao.delete(entity);
    }

    public void deleteRecordAlbum(int id) {
        recordAlbumDao.delete(id);
    }
}
