package leeon.forpeddy;

import ipworks.IPWorksException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import leeon.forpeddy.download.HtmlDownloader;
import leeon.forpeddy.excel.GradeExcelWrapper;
import leeon.forpeddy.htmlparse.GradeHtmlParser;
import leeon.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GradeHelper {

    private static Log logger = LogFactory.getLog(GradeHelper.class);

    public static final String BASE_DIR = "D:\\project\\eurospace\\forpeddy\\misc\\";

    public static final String HTTP_URL_5 = "http://218.1.69.105/bachelor/student/scorecollecttablebk_ChongOnly.jsp?studentno=";

    public static final String HTTP_URL_6 = "http://218.1.69.105/acad/student/scorecollecttablebk_ygao.jsp?studentno=";

    public static final String HTTP_URL_70 = "http://218.1.69.105/acad/student/scorecollecttablebk3_ygao.jsp?studentno=";

    public static final String HTTP_URL_72 = "http://218.1.69.105/gsz/student/scorecollecttablebk.jsp?studentno=";

    public static final String HTTP_URL_71 = "http://218.1.69.105/gqb/student/scorecollecttablebk.jsp?studentno=";

    public static final String FINISH_TIME_END = "2007-03-31";

    public static Map<String, String> CREDIT = null;

    static {
        CREDIT = new HashMap<String, String>();
        CREDIT.put("03秋工商管理", "88.0");
        CREDIT.put("03秋行政管理", "88.0");
        CREDIT.put("03秋国际经济与贸易", "88.0");
        CREDIT.put("03秋计算机科学与技术", "88.0");
        CREDIT.put("03秋金融学", "88.0");
        CREDIT.put("03秋英语（商务）", "87.0");
        CREDIT.put("03秋国际经济与贸易（上中）", "97.0");
        CREDIT.put("03秋会计学", "89.0");
        CREDIT.put("03秋园林（七宝）", "77.0");
        CREDIT.put("03秋国际经济与贸易（七宝）", "84.0");
        CREDIT.put("03秋工商管理（七宝）", "85.0");
        CREDIT.put("03春工商管理", "87.0");
        CREDIT.put("03春行政管理", "85.0");
        CREDIT.put("03春国际经济与贸易", "86.0");
        CREDIT.put("03春计算机科学与技术", "85.0");
        CREDIT.put("03春金融学", "86.0");
        CREDIT.put("03春英语（商务）", "86.0");
        CREDIT.put("03春国际经济与贸易（七宝）", "88.0");
        CREDIT.put("03春计算机科学与技术（七宝）", "87.0");
    }

    private List<String> studentList = null;

    private List<GradeHtmlParser> ghpList = null;

    GradeExcelWrapper list = null;

    private GradeHelper() throws WriteException, BiffException, IOException {
        this.list = new GradeExcelWrapper(new File(BASE_DIR + "list.xls"));
        this.studentList = list.readStudentNo();
    }

    public void doStartDownload() throws IPWorksException, TooManyListenersException, IOException {
        HtmlDownloader h = new HtmlDownloader();
        for (String studentNo : this.studentList) {
            File file = new File(BASE_DIR + studentNo + ".html");
            if (!file.exists() || file.length() == 0) {
                String url = null;
                if (studentNo.startsWith("5")) {
                    url = HTTP_URL_5;
                } else if (studentNo.startsWith("6")) {
                    url = HTTP_URL_6;
                } else if (studentNo.startsWith("7")) {
                    if (studentNo.charAt(8) == '0') {
                        url = HTTP_URL_70;
                    } else if (studentNo.charAt(8) == '1') {
                        url = HTTP_URL_71;
                    } else if (studentNo.charAt(8) == '2') {
                        url = HTTP_URL_72;
                    } else {
                        logger.warn("there is a student " + studentNo + " can not judge his subject");
                        continue;
                    }
                } else {
                    url = HTTP_URL_6;
                }
                h.start(url + studentNo);
                h.saveToFile(file);
            } else {
                logger.info("download html for student " + studentNo + " file exists");
            }
        }
        h.destroy();
    }

    public void doParseHtml() throws IOException {
        this.ghpList = new ArrayList<GradeHtmlParser>();
        for (String studentNo : this.studentList) {
            logger.info("parse html for student [" + studentNo + "]");
            GradeHtmlParser ghp = new GradeHtmlParser(BASE_DIR + studentNo + ".html");
            if (ghp.isAvail()) {
                logger.info("parse html for student " + studentNo + " : [" + ghp + "]");
                ghpList.add(ghp);
            } else {
                logger.info("parse html for student " + studentNo + " not exist");
                ghpList.add(null);
            }
        }
    }

    public void doCheck() {
        int i = 0;
        for (GradeHtmlParser ghp : this.ghpList) {
            logger.info("check for student [" + this.studentList.get(i) + "]");
            if (ghp != null) {
                String msg = check1(ghp);
                ghp.setCheckResult1(msg);
                if (msg.indexOf("毕业通过") != -1) {
                    msg = check2(ghp);
                    ghp.setCheckResult2(msg);
                } else {
                    ghp.setCheckResult2("毕业条件不满足，没有学位");
                }
                logger.info("check for student " + ghp.getNo() + " : [(" + ghp.getCheckResult1() + "), (" + ghp.getCheckResult2() + ")]");
            } else {
                logger.info("check for student " + this.studentList.get(i) + " not exist");
            }
            i++;
        }
    }

    public void doWrite() throws WriteException, IOException {
        int i = 0;
        for (GradeHtmlParser ghp : this.ghpList) {
            logger.info("save student " + this.studentList.get(i) + " to excel file");
            if (ghp != null) {
                list.writeStudentInfo(ghp, i++);
            } else {
                list.writeStudentNotExist(i++);
            }
        }
    }

    public void doSave() throws WriteException, IOException {
        list.save();
    }

    public void doRename() {
        for (GradeHtmlParser ghp : this.ghpList) {
            if (ghp != null) {
                File file = new File(BASE_DIR + ghp.getNo() + ".html");
                if (file.exists()) {
                    if (ghp.getNo().startsWith("7049") && "04秋毕业通过".equals(ghp.getCheckResult1())) {
                        file.renameTo(new File(BASE_DIR + ghp.getNo() + ".htm"));
                    } else if (ghp.getNo().startsWith("7041") && ghp.getCheckResult1().indexOf("毕业通过") != -1) {
                        file.renameTo(new File(BASE_DIR + ghp.getNo() + ".htm"));
                    }
                }
            }
        }
    }

    public void doRevert() {
        for (String studentNo : this.studentList) {
            File file = new File(BASE_DIR + studentNo + ".htm");
            if (file.exists()) {
                file.renameTo(new File(BASE_DIR + studentNo + ".html"));
            }
        }
    }

    public String check1(GradeHtmlParser ghp) {
        String g = ghp.getNo().substring(2, 4);
        if (Integer.parseInt(g) <= 41) {
            if (ghp.getCredit() == null) {
                return "学分没有填写";
            } else {
                if ("41".equals(g)) {
                    if (Float.parseFloat(ghp.getCredit()) < 88) {
                        return "学分没有修满，只有" + ghp.getCredit();
                    }
                } else if ("39".equals(g)) {
                    String a = CREDIT.get("03秋" + ghp.getMajor());
                    if (a == null) {
                        return "总学分没有找到，需要人工判断";
                    } else if (Float.parseFloat(ghp.getCredit()) < Float.parseFloat(a)) {
                        return "学分没有修满，只有" + ghp.getCredit() + ", 应有" + a;
                    }
                } else if ("32".equals(g)) {
                    String a = CREDIT.get("03春" + ghp.getMajor());
                    if (a == null) {
                        return "总学分没有找到，需要人工判断";
                    } else if (Float.parseFloat(ghp.getCredit()) < Float.parseFloat(a)) {
                        return "学分没有修满，只有" + ghp.getCredit() + ", 应有" + a;
                    }
                } else {
                    return "不能判断学分，需要人工判断";
                }
            }
            if ("不及格".equals(ghp.getArticle())) {
                return "论文没有通过，结果是不及格";
            } else if (ghp.getArticle() == null) {
                return "论文成绩没有填写";
            }
            if (ghp.getFinishTime() == null) {
                return "大专毕业时间没有填写";
            } else {
                try {
                    if (!DateUtil.gtDiffYear(ghp.getFinishTime(), FINISH_TIME_END, 2)) {
                        return "04春(含)以前毕业通过(大专毕业时间未满两年，毕业于:" + ghp.getFinishTime() + ")";
                    }
                } catch (ParseException e) {
                    logger.error("can not parse time [" + ghp.getFinishTime() + "]", e);
                    return "大专毕业时间不能解析";
                }
            }
            return "04春(含)以前毕业通过";
        } else {
            if (ghp.getCredit() != null && Float.parseFloat(ghp.getCredit()) < 88) {
                return "学分没有修满，只有" + ghp.getCredit();
            } else if (ghp.getCredit() == null) {
                return "学分没有填写";
            }
            if ("不及格".equals(ghp.getArticle())) {
                return "论文没有通过，结果是不及格";
            } else if (ghp.getArticle() == null) {
                return "论文成绩没有填写";
            }
            if (ghp.getFinishTime() == null) {
                return "大专毕业时间没有填写";
            } else try {
                if (!DateUtil.gtDiffYear(ghp.getFinishTime(), FINISH_TIME_END, 2)) {
                    return "大专毕业时间未满两年，毕业于:" + ghp.getFinishTime();
                }
            } catch (ParseException e) {
                logger.error("can not parse time [" + ghp.getFinishTime() + "]", e);
                return "大专毕业时间不能解析";
            }
            return "04秋毕业通过";
        }
    }

    public String check2(GradeHtmlParser ghp) {
        if (ghp.getScore() != null && Float.parseFloat(ghp.getScore()) < 75) {
            return "平均分没有75分，只有" + ghp.getScore();
        } else if (ghp.getScore() == null) {
            return "平均分没有填写";
        }
        if (("低于CET-4").equals(ghp.getEnglish())) {
            return "英文CET-4没有通过";
        } else if (ghp.getEnglish() == null) {
            return "英文成绩没有填写";
        }
        if ("不及格".equals(ghp.getArticle()) || "及格".equals(ghp.getArticle()) || "中".equals(ghp.getArticle())) {
            return "论文没有通过，结果是" + ghp.getArticle();
        } else if (ghp.getArticle() == null) {
            return "论文成绩没有填写";
        }
        if (!checkDgreeTest(ghp.getDgreeTest1()) || !checkDgreeTest(ghp.getDgreeTest2())) {
            return "学位考试不通过，包括[" + ghp.getDgreeTest1() + "][" + ghp.getDgreeTest2() + "]";
        }
        return "学位通过";
    }

    public boolean checkDgreeTest(String degreeTest) {
        if (degreeTest == null) return false;
        int s = degreeTest.lastIndexOf(':');
        if (s != -1) {
            degreeTest = degreeTest.substring(s + 1);
            if ("无成绩".equals(degreeTest)) {
                return false;
            } else {
                return Float.parseFloat(degreeTest) >= 60;
            }
        } else return false;
    }

    /**
	 * @param args
	 * @throws IOException 
	 * @throws WriteException 
	 */
    public static void main(String[] args) throws WriteException, IOException {
        GradeHelper gh = null;
        try {
            gh = new GradeHelper();
            gh.doStartDownload();
        } catch (Exception e) {
            logger.error("something wrong...", e);
        } finally {
            gh.doSave();
        }
    }
}
