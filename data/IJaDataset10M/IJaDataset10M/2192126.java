package com.rb.lottery.analysis.thread;

import info.informatica.doc.DocumentException;
import info.informatica.doc.DocumentFragment;
import info.informatica.html.HTMLDoc;
import info.informatica.html.HTMLFragment;
import info.informatica.html.NameTagFinder;
import info.informatica.html.tag.HTMLTag;
import info.informatica.html.tag.TagParsingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.rb.lottery.analysis.common.EncodingConstants;
import com.rb.lottery.analysis.common.HtmlTagConstants;
import com.rb.lottery.analysis.common.RegexConstants;
import com.rb.lottery.analysis.common.SystemConstants;
import com.rb.lottery.analysis.common.SystemFunctions;
import com.rb.lottery.analysis.domain.BetMatch;
import com.rb.lottery.analysis.exception.LotteryException;
import com.rb.lottery.analysis.http.DataHttpClient;
import com.rb.lottery.analysis.manager.IOManager;

/**
 * @类功能说明:竞彩足球数据更新线程
 * @类修改者:
 * @修改日期:
 * @修改说明:
 * @作者: robin
 * @创建时间: 2011-12-5 下午02:11:27
 * @版本: 1.0.0
 */
public class JczqDataUpdateThread extends DataUpdateThread {

    private static final Logger log = Logger.getLogger(JczqDataUpdateThread.class);

    private String period;

    /**
	 * @类名: JczqDataUpdateThread.java
	 * @描述: TODO
	 * @param group
	 * @param jczqSite
	 */
    public JczqDataUpdateThread(ThreadGroup group, String site) {
        super(group, 1, site);
    }

    public JczqDataUpdateThread(ThreadGroup group, String site, String period) {
        super(group, 1, site);
        this.period = period;
    }

    /**
	 * @return period
	 */
    public String getPeriod() {
        return period;
    }

    /**
	 * @param period
	 *            period
	 */
    public void setPeriod(String period) {
        this.period = period;
    }

    /**
	 * @方法说明: 从指定网站得到数据进行解析
	 * @参数: @param dataSite
	 * @return void
	 * @throws
	 */
    @Override
    public int updateFromSite() {
        log.info(SystemConstants.JCZQ_THREAD + SystemConstants.START);
        System.out.println(SystemConstants.JCZQ_THREAD + SystemConstants.START);
        DateFormat format = new SimpleDateFormat(SystemConstants.DEFAULT_DATEFORMAT);
        Date current = null;
        try {
            current = format.parse(period);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        Date today = new Date(cal.getTimeInMillis());
        while (!current.after(today)) {
            String curr_str = format.format(current);
            boolean ud = update(curr_str);
            if (!ud) {
                System.out.println(SystemConstants.UNKNOWN_ERROR);
            }
            cal.setTime(current);
            cal.add(Calendar.DATE, 1);
            current = cal.getTime();
        }
        String ty_str = format.format(today).trim();
        ty_str = ty_str.replaceAll(SystemConstants.PART, SystemConstants.EMPTY_STRING);
        int pd = 0;
        if (ty_str.matches(RegexConstants.INTEGER_REGEX)) {
            pd = Integer.parseInt(ty_str);
        }
        log.info(SystemConstants.JCZQ_THREAD + SystemConstants.END);
        System.out.println(SystemConstants.JCZQ_THREAD + SystemConstants.END);
        return pd;
    }

    /**
	 * @方法说明:
	 * @参数: @param current
	 * @参数: @return
	 * @return boolean
	 * @throws
	 */
    private boolean update(String current_date) {
        String site = dataSite;
        site += SystemConstants.JCZQ_UPDATE_PARAMETER + current_date;
        String srcData = DataHttpClient.getGetResponse(site, EncodingConstants.UTF8_ENCODING);
        if (srcData == null) {
            return false;
        }
        int valid = doHtmlAnalyzing(srcData);
        return (valid != 0);
    }

    /**
	 * @方法说明: html源文件解析
	 * @参数: @param srcData html源文件
	 * @return int 返回期号
	 * @throws
	 */
    @Override
    public int doHtmlAnalyzing(String srcData) {
        qihao = doJczqHtmlAnalyzing(srcData);
        return qihao;
    }

    /**
	 * @方法说明: JCZQ html解析
	 * @参数: @param srcData
	 * @参数: @return
	 * @return int 返回期号
	 * @throws
	 */
    private int doJczqHtmlAnalyzing(String srcData) {
        int qihao = 0;
        HTMLDoc doc = new HTMLDoc();
        doc.load(srcData);
        HTMLFragment html = doc.getHTML();
        Map<String, List<BetMatch>> bmMap = new HashMap<String, List<BetMatch>>();
        HTMLTag table_tag = html.getNameFinder(HtmlTagConstants.HTML_TABLE).getTagBlock();
        while (table_tag != null) {
            try {
                String attr_cla = table_tag.getAttributes().getAttribute(HtmlTagConstants.HTML_CLASS);
                if (attr_cla != null && attr_cla.equalsIgnoreCase(HtmlTagConstants.HTML_DC_TABLE)) {
                    String period_id = table_tag.getAttributes().getAttribute(HtmlTagConstants.HTML_ID);
                    if (period_id != null) {
                        period_id = period_id.substring(2);
                        String ty_str = period_id.replaceAll(SystemConstants.PART, SystemConstants.EMPTY_STRING);
                        if (ty_str.matches(RegexConstants.INTEGER_REGEX)) {
                            qihao = Integer.parseInt(ty_str);
                        }
                        bmMap.put(period_id, getBetMatchFromJczqHtmlTag(table_tag, qihao, period_id));
                    }
                }
                html.removeBlock(table_tag);
                NameTagFinder tag_finder = html.getNameFinder(HtmlTagConstants.HTML_TABLE);
                if (tag_finder == null) {
                    break;
                } else {
                    table_tag = tag_finder.getTagBlock();
                }
            } catch (TagParsingException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        boolean status = IOManager.getInstance().updateJczqFiles(bmMap);
        if (!status) {
            try {
                throw (new LotteryException(15));
            } catch (LotteryException e) {
                e.printStackTrace();
            }
        }
        return qihao;
    }

    /**
	 * @方法说明: 将对应HTMLTag数据解析为竞彩足球比赛投注信息
	 * @参数: @param tag HTMLTag数据
	 * @参数: @param qihao 期号
	 * @参数: @param period 投注日期
	 * @return BetMatch 比赛投注信息
	 * @throws
	 */
    private List<BetMatch> getBetMatchFromJczqHtmlTag(HTMLTag tag, int qihao, String period) {
        List<BetMatch> matches = new ArrayList<BetMatch>();
        HTMLDoc doc = new HTMLDoc();
        doc.load(tag.toString());
        HTMLFragment html = doc.getHTML();
        HTMLTag currentTag = html.getNameFinder(HtmlTagConstants.HTML_TR).getTagBlock();
        while (currentTag != null) {
            BetMatch match = new BetMatch();
            match.setType(2);
            match.setQihao(qihao + SystemConstants.EMPTY_STRING);
            match.setPeriod(period);
            match.setStatus(0);
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_LABEL).getTagBlock();
            String id = currentTag.getTagData().toString().trim();
            if (id != null) {
                int indx = id.indexOf(SystemConstants.RIGHT_ANGLE);
                id = id.substring(indx + 1);
                match.setId(id);
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_A).getTagBlock();
            String competition = currentTag.getTagData().toString().trim();
            if (competition != null) {
                match.setCompetition(competition);
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
            String time = currentTag.getTagData().toString().trim();
            if (time != null) {
                match.setTime(time);
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
            if (currentTag != null) {
                DocumentFragment hk = currentTag.getTagData();
                if (hk != null) {
                    String homeRank = hk.toString().trim();
                    match.setHomeRank(homeRank);
                }
                try {
                    html.removeBlock(currentTag);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_A).getTagBlock();
            String home = currentTag.getTagData().toString().trim();
            if (home != null) {
                home = home.replaceAll(HtmlTagConstants.HTML_BLANK, SystemConstants.BLANK_STRING);
                match.setHome(home);
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            String ccd = currentTag.getTagData().toString().trim();
            int concede = 0;
            if (ccd != null) {
                if (ccd.matches(RegexConstants.INTEGER_REGEX)) {
                    concede = Integer.parseInt(ccd);
                    match.setConcede(concede);
                } else {
                    HTMLDoc tmp = new HTMLDoc();
                    tmp.load(ccd);
                    HTMLFragment tmpHtml = tmp.getHTML();
                    HTMLTag tmpTag = tmpHtml.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
                    ccd = tmpTag.getTagData().toString().trim();
                    if (ccd.matches(RegexConstants.INTEGER_REGEX)) {
                        concede = Integer.parseInt(ccd);
                        match.setConcede(concede);
                    }
                }
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_A).getTagBlock();
            String visitor = currentTag.getTagData().toString().trim();
            if (visitor != null) {
                visitor = visitor.replaceAll(HtmlTagConstants.HTML_BLANK, SystemConstants.BLANK_STRING);
                match.setVisitor(visitor);
            }
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
            if (currentTag != null) {
                DocumentFragment vk = currentTag.getTagData();
                if (vk != null) {
                    String vistRank = vk.toString().trim();
                    match.setVistRank(vistRank);
                }
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
            String wos = currentTag.getTagData().toString().trim();
            if (wos.matches(RegexConstants.POSITIVE_FLOAT_REGEX)) {
                double winOdds = Double.parseDouble(wos);
                match.setWinOdds(winOdds);
            }
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
            String dos = currentTag.getTagData().toString().trim();
            if (dos.matches(RegexConstants.POSITIVE_FLOAT_REGEX)) {
                double drawOdds = Double.parseDouble(dos);
                match.setDrawOdds(drawOdds);
            }
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
            String los = currentTag.getTagData().toString().trim();
            if (los.matches(RegexConstants.POSITIVE_FLOAT_REGEX)) {
                double lossOdds = Double.parseDouble(los);
                match.setLossOdds(lossOdds);
            }
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
            String wrs = currentTag.getTagData().toString().trim();
            if (wrs.matches(RegexConstants.PERCENT_FLOAT_REGEX)) {
                double winRate = SystemFunctions.percentToDouble(wrs);
                match.setWinRate(winRate);
            }
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
            String drs = currentTag.getTagData().toString().trim();
            if (drs.matches(RegexConstants.PERCENT_FLOAT_REGEX)) {
                double drawRate = SystemFunctions.percentToDouble(drs);
                match.setDrawRate(drawRate);
            }
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
            String lrs = currentTag.getTagData().toString().trim();
            if (lrs.matches(RegexConstants.PERCENT_FLOAT_REGEX)) {
                double lossRate = SystemFunctions.percentToDouble(lrs);
                match.setLossRate(lossRate);
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            String sre = currentTag.toString().trim();
            if (sre.indexOf(HtmlTagConstants.HTML_SPAN) >= 0) {
                currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
                String score = currentTag.getTagData().toString().trim();
                if (score.indexOf(SystemConstants.COLON) > 0) {
                    String[] sco = score.split(SystemConstants.COLON);
                    if (sco[0].matches(RegexConstants.INTEGER_REGEX)) {
                        match.setHomeGoal(Integer.parseInt(sco[0]));
                    }
                    if (sco[1].matches(RegexConstants.INTEGER_REGEX)) {
                        match.setVistGoal(Integer.parseInt(sco[1]));
                    }
                    match.setStatus(3);
                    match.generateKjNumber();
                } else if (score.equals(SystemConstants.DELAY)) {
                    match.setStatus(41);
                } else if (score.equals(SystemConstants.CUTTING)) {
                    match.setStatus(42);
                } else {
                    match.setStatus(0);
                }
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            int status = match.getStatus();
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            String isSP = currentTag.toString();
            if (isSP.indexOf(HtmlTagConstants.HTML_SPAN) >= 0) {
                currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
                String jsWinSP = currentTag.getTagData().toString().trim();
                if (jsWinSP.matches(RegexConstants.POSITIVE_FLOAT_REGEX)) {
                    double sp = Double.parseDouble(jsWinSP);
                    match.setJsWinSP(sp);
                    if (status == 3) {
                        match.setSP(sp);
                    }
                }
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            isSP = currentTag.toString();
            if (isSP.indexOf(HtmlTagConstants.HTML_SPAN) >= 0) {
                currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
                String jsDrawSP = currentTag.getTagData().toString().trim();
                if (jsDrawSP.matches(RegexConstants.POSITIVE_FLOAT_REGEX)) {
                    double sp = Double.parseDouble(jsDrawSP);
                    match.setJsDrawSP(sp);
                    if (status == 3) {
                        match.setSP(sp);
                    }
                }
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TD).getTagBlock();
            isSP = currentTag.toString();
            if (isSP.indexOf(HtmlTagConstants.HTML_SPAN) >= 0) {
                currentTag = html.getNameFinder(HtmlTagConstants.HTML_SPAN).getTagBlock();
                String jsLossSP = currentTag.getTagData().toString().trim();
                if (jsLossSP.matches(RegexConstants.POSITIVE_FLOAT_REGEX)) {
                    double sp = Double.parseDouble(jsLossSP);
                    match.setJsLossSP(sp);
                    if (status == 3) {
                        match.setSP(sp);
                    }
                }
            }
            matches.add(match);
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TR).getTagBlock();
            try {
                html.removeBlock(currentTag);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            currentTag = html.getNameFinder(HtmlTagConstants.HTML_TR).getTagBlock();
        }
        return matches;
    }
}
