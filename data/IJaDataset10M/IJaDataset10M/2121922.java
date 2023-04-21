package net.narusas.daumaccess;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoardTask extends Task {

    private final int startPage;

    private final int endPage;

    private final String name;

    private String grpid;

    private String fldid;

    private String firstbbsdepth;

    private String lastbbsdepth;

    private String prefix;

    static Pattern p = Pattern.compile("function goPage\\( page \\) \\{\\s+document.listForm.action=\"([^;]+)");

    static Pattern p1 = Pattern.compile("grpid=([^&\\s^\"]+)");

    static Pattern p2 = Pattern.compile("fldid=([^&\\s^\"]+)");

    static Pattern p3 = Pattern.compile("firstbbsdepth=([^&\\s^\"]+)");

    static Pattern p4 = Pattern.compile("lastbbsdepth=([^&\\s^\"]+)");

    static Pattern urlPattern = Pattern.compile("(http://[^/]+)");

    static Pattern writingLinkPattern = Pattern.compile("(/_c21_/bbs_read?[^\"]+)");

    private final boolean randomSelect;

    Random r = new Random(System.currentTimeMillis());

    public BoardTask(String boardURL, String name, int startPage, int endPage, int startAccount, int endAccount, boolean randomSelect) {
        super(boardURL, true, startAccount, endAccount);
        this.name = name;
        this.startPage = startPage;
        this.endPage = endPage;
        this.randomSelect = randomSelect;
        prefix = match(urlPattern, boardURL);
    }

    public int getStartPage() {
        return startPage;
    }

    public Object getEndPage() {
        return endPage;
    }

    @Override
    public void execute(DaumPageFetcher fetcher) throws IOException {
        Progress.getInstance().progress("Board Task " + name + "�� �����մϴ�.");
        Progress.getInstance().progress("������ �м��� ���� ù �������� ���ɴϴ�.");
        parseFirstPage(getFirstPage(fetcher));
        for (int page = startPage; page <= endPage; page++) {
            Progress.getInstance().progress("������ " + page + "�� ���ɴϴ�.");
            String pageHTML = getPage(page, fetcher);
            Progress.getInstance().progress("������ " + page + "�� ���Խ��ϴ�.");
            List<String> links = parseWriting(pageHTML);
            if (randomSelect) {
                Progress.getInstance().progress("�������߿��� ���������� �Խù��� �����մϴ�. ");
                int i = r.nextInt(links.size());
                String url = links.get(i);
                WritingTask writing = new WritingTask(prefix + url, getStartAccount(), getEndAccount());
                writing.execute(fetcher);
                break;
            } else {
                Progress.getInstance().progress("�������߿��� �����ϰ� �Խù��� �����մϴ�. ");
                for (String url : links) {
                    WritingTask writing = new WritingTask(prefix + url, getStartAccount(), getEndAccount());
                    writing.execute(fetcher);
                }
            }
            Progress.getInstance().progress("������ " + page + "�� �����߽��ϴ�. ");
        }
        Progress.getInstance().progress("Board Task " + name + "�� �����߽��ϴ�.");
    }

    @Override
    public String toString() {
        return "�Խ���: " + name + ", S=" + startPage + ", E=" + endPage + ", A=(" + getStartAccount() + "," + getEndAccount() + "), RANDOM=" + randomSelect + ", URL=" + url;
    }

    public String getName() {
        return name;
    }

    public String getFirstPage(DaumPageFetcher f) throws IOException {
        return f.fetch(url);
    }

    public String getgrpid() {
        return grpid;
    }

    public String getfldid() {
        return fldid;
    }

    public String getfirstbbsdepth() {
        return firstbbsdepth;
    }

    public String getlastbbsdepth() {
        return lastbbsdepth;
    }

    public void parseFirstPage(String firstPage) {
        String target = match(p, firstPage);
        grpid = match(p1, target);
        fldid = match(p2, target);
        firstbbsdepth = match(p3, target);
        lastbbsdepth = match(p4, target);
    }

    private String match(Pattern pt, String target) {
        Matcher m = pt.matcher(target);
        m.find();
        return m.group(1);
    }

    public String getPage(int page, DaumPageFetcher f) throws IOException {
        return f.fetch(pageURL(page));
    }

    private String pageURL(int page) {
        return prefix + "/_c21_/bbs_list?grpid=" + grpid + "&mgrpid=&fldid=" + fldid + "&page=" + page + "&prev_page=1&firstbbsdepth=" + firstbbsdepth + "&lastbbsdepth=" + lastbbsdepth;
    }

    public List<String> parseWriting(String src) {
        LinkedList<String> res = new LinkedList<String>();
        Matcher m = writingLinkPattern.matcher(src);
        while (m.find()) {
            res.add(m.group(1));
        }
        return res;
    }

    public boolean isRandomSelect() {
        return randomSelect;
    }
}
