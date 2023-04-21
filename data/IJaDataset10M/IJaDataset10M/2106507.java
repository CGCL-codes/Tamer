package org.sharp.vocreader.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.sharp.intf.DownloadEventListener;
import org.sharp.intf.PayInfoSupport;
import org.sharp.intf.PaySupport;
import org.sharp.intf.PointsSupport;
import org.sharp.intf.UnzipEventListener;
import org.sharp.utils.IOUtils;
import org.sharp.utils.Option;
import org.sharp.vocreader.beans.AudioInfo;
import org.sharp.vocreader.beans.Course;
import org.sharp.vocreader.beans.State;
import org.sharp.vocreader.beans.UrlSetting;
import org.sharp.vocreader.intf.Constants;
import org.sharp.vocreader.intf.OsSupport;

public class JpWordsReader {

    private OsSupport mAs;

    private PointsSupport mPs;

    private PaySupport mPas;

    public static interface EventListenr {

        void viewNeedsFresh();

        void showError(int errorCode);
    }

    public static class UnitUtils {

        public static String[] unitTitles() {
            List<String> tl = new ArrayList<String>();
            for (int i = 0; i < 12; i++) {
                tl.add("Unit " + (i + 1));
            }
            return tl.toArray(new String[0]);
        }

        public static int unitNoOf(String path) {
            Pattern p = Pattern.compile("unit(\\d+)/");
            Matcher m = p.matcher(path);
            if (m.find()) return Integer.parseInt(m.group(1)); else return Integer.MIN_VALUE;
        }

        public static String unitDir(int unitNo) {
            return "unit" + unitNo;
        }
    }

    public static class CourseUtils {

        public static String courseCacheFilePath(OsSupport oss, int courseNo) {
            return CourseUtils.unzippedDirPath(courseNo) + File.separator + Constants.cacheFileName;
        }

        public static String[] courseTitles() {
            List<String> tl = new ArrayList<String>();
            for (int i = 0; i < Constants.MAX_COURSE_NO; i++) {
                tl.add("Course " + (i + 1));
            }
            return tl.toArray(new String[0]);
        }

        public static Option<Integer> courseNoOf(String path) {
            Pattern p = Pattern.compile("unit\\d+/(\\d+)/");
            Matcher m = p.matcher(path);
            if (m.find()) return new Option<Integer>(new Integer(m.group(1))); else return new Option<Integer>(null);
        }

        private static String courseZipFileName(int courseNo) {
            return courseNo + ".zip";
        }

        private static String courseDir(int courseNo) {
            return UnitUtils.unitDir((courseNo - 1) / 4 + 1) + File.separator + courseNo;
        }

        public static Course[] courseList(OsSupport os) {
            List<Course> tl = new ArrayList<Course>();
            for (int i = 1; i <= Constants.MAX_COURSE_NO; i++) {
                Course course = new Course(i);
                if (courseDownloaded(os, i)) course.setStatus(Constants.COURSE_STATUS_DOWNLOADED); else course.setStatus(Constants.COURSE_STATUS_NOTEXIST_NOINFO);
                tl.add(course);
            }
            return tl.toArray(new Course[0]);
        }

        public static Course[] courseList2(OsSupport os) {
            Course[] ca = NetWorkSupport.fetchCourseList(os);
            if (ca == null) {
                return courseList(os);
            }
            for (int i = 1; i <= Constants.MAX_COURSE_NO; i++) {
                Course course = ca[i - 1];
                if (courseDownloaded(os, i)) course.setStatus(Constants.COURSE_STATUS_DOWNLOADED); else course.setStatus(Constants.COURSE_STATUS_NOTEXIST);
            }
            return ca;
        }

        public static String zipFilePath(int courseNo) {
            return IOUtils.fullPath(Constants.jpMp3Path, courseZipFileName(courseNo));
        }

        public static String zipFileDir() {
            return Constants.jpMp3Path;
        }

        public static String unzippedDirPath(int courseNo) {
            return IOUtils.fullPath(Constants.jpMp3Path, courseDir(courseNo));
        }

        public static boolean courseUnzipped(OsSupport os, int courseNo) {
            return os.pathExists(IOUtils.fullPath(Constants.jpMp3Path, courseDir(courseNo)));
        }

        public static boolean courseDownloaded(OsSupport os, int courseNo) {
            return os.pathExists(zipFilePath(courseNo));
        }

        public static boolean valid(int courseNo) {
            return courseNo != Constants.ILLEGAL_INT && courseNo >= 1 && courseNo <= Constants.MAX_COURSE_NO;
        }

        public static Course[] loadCourseList(OsSupport oss) {
            return (Course[]) oss.fromString(oss.readFile(courseListFilePath()), Course[].class);
        }

        public static void saveCourseList(OsSupport oss, Course[] courses) {
            oss.writeFile(courseListFilePath(), oss.toString(courses));
        }

        private static String courseListFilePath() {
            return IOUtils.fullPath(Constants.jpMp3Path, Constants.coursesInfoFileName);
        }
    }

    private List<AudioInfo> infoList() {
        return levels.levelList(-1);
    }

    private List<AudioInfo> levelList() {
        return levels.levelList(mState.level);
    }

    public static class LevelsInfo {

        private Map<Integer, List<AudioInfo>> levelListMap = new HashMap<Integer, List<AudioInfo>>();

        public List<AudioInfo> levelList(int level) {
            if (levelListMap.containsKey(level)) {
                return levelListMap.get(level);
            } else {
                List<AudioInfo> lList = new ArrayList<AudioInfo>();
                levelListMap.put(level, lList);
                return lList;
            }
        }

        public int levelWC(int level) {
            if (levelListMap.containsKey(level)) {
                return levelListMap.get(level).size();
            } else {
                return 0;
            }
        }

        public void add(AudioInfo ai) {
            levelList(ai.level).add(ai);
        }

        public void remove(AudioInfo ai) {
            levelList(ai.level).remove(ai);
        }

        public void clear(int start, int end) {
            for (int i = start; i <= end; i++) {
                levelList(i).clear();
            }
        }

        public String toString(int start, int end, String separator) {
            Integer[] sa = new Integer[end - start + 1];
            for (int i = start; i <= end; i++) {
                sa[i] = levelList(i).size();
            }
            return StringUtils.join(sa, separator);
        }
    }

    public LevelsInfo levels = new LevelsInfo();

    public State mState = null;

    public JpWordsReader.EventListenr listener;

    private Course[] mCourses;

    private Option<UrlSetting> mSetting;

    private PayInfoSupport mPais;

    protected void freshView() {
        if (listener != null) listener.viewNeedsFresh(); else mAs.log("no view updated");
    }

    public JpWordsReader(OsSupport as, PointsSupport ps, PaySupport paySupport) {
        mAs = as;
        mPs = ps;
        mPas = paySupport;
    }

    public void setPointsSupport(PointsSupport ps) {
        mPs = ps;
    }

    public void setPaySupport(PaySupport paySupport) {
        mPas = paySupport;
    }

    public void setPayInfoSupport(PayInfoSupport payInfoSupport) {
        mPais = payInfoSupport;
    }

    private static void loadMp3s(OsSupport oss, List<AudioInfo> infoList, LevelsInfo lvs, String path) {
        oss.log("load mp3 from path " + path);
        File[] mp3files = oss.findMp3File(path);
        for (int i = 0; i < mp3files.length; i++) {
            Option<Integer> cn = CourseUtils.courseNoOf(mp3files[i].getPath());
            int un = UnitUtils.unitNoOf(mp3files[i].getPath());
            if (cn.isNull() || un == Integer.MIN_VALUE) continue;
            AudioInfo ai = new AudioInfo();
            ai.mp3Path = oss.relativePath(mp3files[i].getPath(), Constants.jpMp3Path);
            ai.name = FilenameUtils.getBaseName(mp3files[i].getName());
            ai.courseNo = cn.value();
            ai.unitNo = un;
            infoList.add(ai);
            lvs.add(ai);
        }
        oss.log("from path total " + infoList.size() + " mp3 loaded.");
    }

    public Course[] getCourseList() {
        return mCourses;
    }

    public void playMp3() {
        int i = mState.current;
        try {
            if (levelList() != null && i >= 0 && i < levelList().size()) {
                AudioInfo info = current(levelList(), i);
                if (info != null) {
                    mAs.playMp3(IOUtils.fullPath(Constants.jpMp3Path, info.mp3Path));
                }
            }
        } catch (Exception e) {
            mAs.log("", e);
        }
    }

    public void toBeginning() {
        mState.last = mState.current;
        mState.current = 0;
        freshView();
    }

    public void toEnding() {
        mState.last = mState.current;
        mState.current = levelList().size() - 1;
        freshView();
    }

    public boolean back() {
        if (mState.current - 1 >= 0) {
            mState.last = mState.current;
            mState.current--;
            freshView();
            return true;
        }
        return false;
    }

    public boolean forward() {
        if (mState.current + 1 < levelList().size()) {
            mState.last = mState.current;
            mState.current++;
            freshView();
            return true;
        }
        return false;
    }

    public void downLevel() {
        AudioInfo info = current(levelList(), mState.current);
        if (info != null && info.level - 1 >= 0) {
            levels.remove(info);
            info.level--;
            levels.add(info);
            setCurrentToLast(mState, levelList());
            freshView();
        }
    }

    private static void setCurrentToLast(State state, List<AudioInfo> al) {
        if (state.last >= 0 && state.last < al.size()) state.current = state.last; else if (state.last < 0) state.current = 0; else if (state.last >= al.size()) state.current = al.size() - 1;
    }

    public AudioInfo current(List<AudioInfo> levelList, int current) {
        if (mState.current < 0 || mState.current >= levelList().size()) return null; else return levelList().get(mState.current);
    }

    public void upLevel() {
        AudioInfo info = current(levelList(), mState.current);
        if (info != null && info.level + 1 <= Constants.MAX_LEVEL) {
            levels.remove(info);
            info.level++;
            levels.add(info);
            setCurrentToLast(mState, levelList());
            freshView();
        }
    }

    public String text() {
        try {
            if (levelList() != null && levelList().size() > 0) {
                if (mState.current >= 0 && mState.current < levelList().size()) {
                    AudioInfo info = current(levelList(), mState.current);
                    return info.name;
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            mAs.log("", e);
        }
        return null;
    }

    public static interface Info {

        int NO_ERROR = 0;

        int ERROR_STATE_NOT_LOADED = 1;

        int ERROR_MP3_NOT_LOADED = 2;

        int ERROR_CURRENT = 3;

        AudioInfo currentAudio();

        int current();

        int total();

        int level();

        int error();
    }

    private Info newInfo(final int error) {
        return newInfo(null, 0, 0, 0, error);
    }

    private Info newInfo(final int error, int level) {
        return newInfo(null, 0, 0, level, error);
    }

    private Info newInfo(final AudioInfo ai, final int current, final int total, final int level, final int error) {
        return new Info() {

            @Override
            public AudioInfo currentAudio() {
                return ai;
            }

            @Override
            public int current() {
                return current;
            }

            @Override
            public int total() {
                return total;
            }

            @Override
            public int level() {
                return level;
            }

            @Override
            public int error() {
                return error;
            }
        };
    }

    public Info info() {
        if (mState != null) {
            if (levelList() != null && levelList().size() > 0) {
                if (mState.current >= 0 && mState.current < levelList().size()) {
                    AudioInfo ca = current(levelList(), mState.current);
                    return newInfo(ca, mState.current + 1, levelList().size(), mState.level + 1, Info.NO_ERROR);
                } else {
                    mState.current = 0;
                    return newInfo(Info.ERROR_CURRENT);
                }
            } else {
                return newInfo(Info.ERROR_MP3_NOT_LOADED, mState.level + 1);
            }
        } else {
            return newInfo(Info.ERROR_STATE_NOT_LOADED);
        }
    }

    @Deprecated
    public String infoMessage() {
        try {
            if (mState != null) {
                if (levelList() != null && levelList().size() > 0) {
                    if (mState.current >= 0 && mState.current < levelList().size()) {
                        AudioInfo ca = current(levelList(), mState.current);
                        return "Mp3 file(unit:" + ca.unitNo + ",course:" + ca.courseNo + "," + mState.current + "/" + (levelList().size() - 1) + ")'s level:" + ca.level;
                    } else {
                        String message = "error:Mp3 file(unit:" + mState.unitNo + ",course:" + mState.courseNo + "," + mState.current + "/0)'s level:" + mState.level;
                        mState.current = 0;
                        return message;
                    }
                } else {
                    return "Mp3 file not loaded.";
                }
            } else {
                return "JpWordsReader's state not loaded.";
            }
        } catch (Exception e) {
            mAs.log("", e);
        }
        return null;
    }

    public void switchLC(int newLevel) {
        switch(mState.level) {
            case -1:
                mState.lal = mState.last;
                mState.lac = mState.current;
                break;
            case 0:
                mState.l0l = mState.last;
                mState.l0c = mState.current;
                break;
            case 1:
                mState.l1l = mState.last;
                mState.l1c = mState.current;
                break;
            case 2:
                mState.l2l = mState.last;
                mState.l2c = mState.current;
                break;
            case 3:
                mState.l3l = mState.last;
                mState.l3c = mState.current;
                break;
            case 4:
                mState.l4l = mState.last;
                mState.l4c = mState.current;
                break;
        }
        switch(newLevel) {
            case -1:
                mState.last = mState.lal;
                mState.current = mState.lac;
                break;
            case 0:
                mState.last = mState.l0l;
                mState.current = mState.l0c;
                break;
            case 1:
                mState.last = mState.l1l;
                mState.current = mState.l1c;
                break;
            case 2:
                mState.last = mState.l2l;
                mState.current = mState.l2c;
                break;
            case 3:
                mState.last = mState.l3l;
                mState.current = mState.l3c;
                break;
            case 4:
                mState.last = mState.l4l;
                mState.current = mState.l4c;
                break;
        }
        mState.level = newLevel;
        freshView();
    }

    static void loadCourse(OsSupport oss, LevelsInfo levels, List<AudioInfo> iList, List<AudioInfo> levelList, State state) {
        levels.clear(-1, Constants.MAX_LEVEL);
        loadCache(oss, iList, levels, CourseUtils.courseCacheFilePath(oss, state.courseNo));
        if (iList.isEmpty()) {
            oss.log("loading from cache failed.");
            loadMp3s(oss, iList, levels, CourseUtils.unzippedDirPath(state.courseNo));
        } else {
            oss.log("loaded from cache.");
        }
    }

    @Deprecated
    private void loadUnit(int unitNo) {
        mState.resetState(unitNo, false);
    }

    @Deprecated
    public void goUnit(int unitNo) {
        loadUnit(unitNo);
        freshView();
    }

    public void onCreate() {
        mAs.logStoragePath(Constants.jpMp3Path);
        mState = mAs.loadState();
        if (mState.bonus == Constants.ILLEGAL_INT && !mSetting.isNull()) {
            mState.bonus = mSetting.value().bonus_offer;
        }
        mAs.log("state loaded:" + mAs.toString(mState));
        if (CourseUtils.valid(mState.courseNo) && CourseUtils.courseUnzipped(mAs, mState.courseNo)) {
            loadCourse(mAs, levels, infoList(), levelList(), mState);
        } else {
            loadAssetCourse(mAs, levels, infoList(), levelList(), mState);
        }
        mCourses = CourseUtils.loadCourseList(mAs);
        if (mCourses == null) {
            mCourses = CourseUtils.courseList2(mAs);
        }
        mAs.log("app loaded successfully.");
    }

    private void loadAssetCourse(final OsSupport oss, final LevelsInfo lsi, final List<AudioInfo> infoList, final List<AudioInfo> levelList, final State state) {
        state.resetState(1, true);
        if (!CourseUtils.courseUnzipped(oss, 1)) {
            oss.copyAssetFile("jpwords/1.zip", CourseUtils.zipFilePath(1));
            oss.unzipFile(CourseUtils.zipFilePath(1), CourseUtils.zipFileDir(), new UnzipEventListener() {

                @Override
                public void complete(String filePath) {
                    loadCourse(oss, lsi, infoList, levelList, state);
                }
            });
        } else {
            loadCourse(oss, lsi, infoList, levelList, state);
        }
    }

    private static void loadCache(OsSupport oss, List<AudioInfo> iList, LevelsInfo lvs, String filePath) {
        try {
            long ct = System.currentTimeMillis();
            AudioInfo[] o = null;
            if (oss.pathExists(filePath)) {
                o = (AudioInfo[]) oss.fromString(oss.readFile(filePath), AudioInfo[].class);
            } else {
                oss.log("file " + filePath + " not exists.");
            }
            long ct2 = System.currentTimeMillis();
            int sec = (int) ((ct2 - ct) / 1000);
            if (o != null) {
                oss.log("takes " + sec + " seconds,total " + o.length + " audio info loaded.");
                for (AudioInfo ai : o) {
                    iList.add(ai);
                    lvs.add(ai);
                }
            }
        } catch (Exception e) {
            oss.log(e.getMessage(), e);
        }
    }

    private static void saveCache(OsSupport oss, List<AudioInfo> infoList, String filePath) {
        oss.writeFile(filePath, oss.toString(infoList.toArray(new AudioInfo[0])));
    }

    public void onPause() {
        mAs.saveState(mState);
        saveCache4Course(mAs, mState.courseNo, infoList());
        CourseUtils.saveCourseList(mAs, mCourses);
    }

    private static void saveCache4Course(OsSupport oss, int courseNo, List<AudioInfo> infoList) {
        if (CourseUtils.valid(courseNo)) {
            saveCache(oss, infoList, CourseUtils.courseCacheFilePath(oss, courseNo));
        }
    }

    private int decreaseBonus(int bonus) {
        if (bonus == Constants.ILLEGAL_INT) return Constants.ILLEGAL_INT;
        if (bonus - 1 >= 0) {
            return bonus - 1;
        } else return 0;
    }

    public void chooseCourse(final int courseNo) {
        saveCache4Course(mAs, mState.courseNo, infoList());
        mState.resetState(courseNo, true);
        Course c = mCourses[courseNo - 1];
        if (c.status == Constants.COURSE_STATUS_NOTEXIST) {
            if (!mSetting.isNull() && mPs != null) {
                mAs.log("checking points.");
                if (!mPs.checkPoints(mState.bonus)) return;
                mState.bonus = decreaseBonus(mState.bonus);
                mAs.log("starting to download course:" + courseNo);
                NetWorkSupport.downloadCourseZip(mAs, courseNo, CourseUtils.zipFilePath(courseNo), new DownloadEventListener() {

                    @Override
                    public void complete(String filePath) {
                        mCourses[courseNo - 1].status = Constants.COURSE_STATUS_DOWNLOADED;
                        String zipfilePath = CourseUtils.zipFilePath(courseNo);
                        mAs.unzipFile(zipfilePath, CourseUtils.zipFileDir(), new UnzipEventListener() {

                            @Override
                            public void complete(String filePath) {
                                unzipComplete();
                            }
                        });
                    }
                });
            } else {
                mAs.log("mSetting is null,download function will not be available.");
                listener.showError(Constants.FETCH_SETTING_ERROR_DOWNLOAD_CANCELED);
            }
        } else if (c.status == Constants.COURSE_STATUS_DOWNLOADED) {
            mAs.log("starting to open course:" + courseNo);
            if (CourseUtils.courseUnzipped(mAs, courseNo)) {
                loadCourse(mAs, levels, infoList(), levelList(), mState);
                freshView();
            } else {
                mAs.unzipFile(CourseUtils.zipFilePath(courseNo), CourseUtils.zipFileDir(), new UnzipEventListener() {

                    @Override
                    public void complete(String filePath) {
                        unzipComplete();
                    }
                });
            }
        }
    }

    private void unzipComplete() {
        loadCourse(mAs, levels, infoList(), levelList(), mState);
        freshView();
    }
}
