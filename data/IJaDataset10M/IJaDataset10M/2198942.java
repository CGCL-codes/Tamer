package com.hmw.task;

import java.text.ParseException;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import com.hmw.Constant;
import com.hmw.bean.Infomation;
import com.hmw.main.Main;

public class GanJiTask extends AbsAnalyzeTask {

    private static final Pattern PATTERN_PUBLISH_TIME = Pattern.compile("发布时间：(.+?)[\r\n]");

    private static final Pattern PATTERN_PRICE = Pattern.compile("价格：(.+?)[\r\n]");

    private static final Pattern PATTERN_LOCATION = Pattern.compile("所在地：(.+?)[\r\n]|所在地：(.+?)$");

    private static final Pattern PATTERN_QQ = Pattern.compile("QQ[：:]\\s*(\\d{5,11})");

    @Override
    public Infomation fetchInformation(String url) {
        Infomation info = new Infomation();
        info.setName(config.getName());
        info.setUrl(url);
        try {
            Parser p = new Parser(url);
            p.setEncoding(config.getCharset());
            OrFilter filter = new OrFilter(new NodeFilter[] { new TagNameFilter("h1"), new CssSelectorNodeFilter(".iblock") });
            NodeList nodeList = p.parse(filter);
            Node node = null;
            for (int i = 0, size = nodeList.size(); i < size; i++) {
                node = nodeList.elementAt(i);
                if (node instanceof HeadingTag) {
                    HeadingTag h1 = (HeadingTag) node;
                    info.setTitle(h1.toPlainTextString().replaceAll("[\r\n]", ""));
                    Node detailNode = h1.getNextSibling();
                    while (detailNode instanceof TextNode) {
                        detailNode = detailNode.getNextSibling();
                    }
                    String detail = detailNode.toPlainTextString();
                    Matcher matcher = PATTERN_PUBLISH_TIME.matcher(detail);
                    if (matcher.find()) {
                        String date = matcher.group(1);
                        try {
                            info.setPublishTime(DateUtils.parseDate(date, new String[] { config.getPublishTimeFormate() }));
                        } catch (ParseException e) {
                            logger.warn(url + "    将日期[" + date + "]转换为[" + config.getPublishTimeFormate() + "]格式发生错误。");
                        }
                    }
                    matcher = PATTERN_PRICE.matcher(detail);
                    if (matcher.find()) {
                        info.setPrice(matcher.group(1));
                    }
                    matcher = PATTERN_LOCATION.matcher(detail);
                    if (matcher.find()) {
                        String location = matcher.group(1);
                        info.setLocation(StringUtils.isBlank(location) ? matcher.group(2) : location);
                    }
                } else if (node instanceof Span) {
                    String text = node.getParent().toPlainTextString();
                    Matcher matcher = PATTERN_QQ.matcher(text);
                    if (matcher.find()) {
                        String qq = matcher.group(1);
                        info.setQq(qq);
                        customLog.info(qq + "\t" + url);
                    }
                }
            }
        } catch (ParserException e) {
            logger.error("任务===" + config.getName() + "===将取消：解析网页[" + url + "]发生异常。", e);
        }
        return info;
    }

    @Override
    public LinkedHashSet<String> getAllInformationUrl(String url) {
        LinkedHashSet<String> urls = new LinkedHashSet<String>();
        try {
            Parser p = new Parser(url);
            p.setEncoding(config.getCharset());
            NodeList nodeList = p.parse(new LinkRegexFilter("/\\w+/\\d{8}_\\d{7}\\.htm"));
            LinkTag link;
            for (int i = 0, size = nodeList.size(); i < size; i++) {
                link = (LinkTag) nodeList.elementAt(i);
                if (Main.getOperationCode() == Constant.OPERATION_CODE_ANALYZE_FIRST_PAGE) {
                    if (!link.getParent().toPlainTextString().trim().startsWith("置顶信息")) {
                        urls.add(link.getLink());
                    }
                } else {
                    urls.add(link.getLink());
                }
            }
        } catch (ParserException e) {
            logger.error("===" + config.getName() + "===将取消：从网页 " + url + " 获取所有链接发生异常。", e);
        }
        return urls;
    }
}
