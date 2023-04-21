package Action.lineMode.lineModeCommon;

import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import Action.AxisInfo;
import Action.Keyword;
import Action.lineMode.lineModeCommon.relaxer.GROUP;
import Action.lineMode.lineModeCommon.relaxer.LineMode;
import Action.lineMode.lineModeCommon.relaxer.TMGFF;
import Action.lineMode.lineModeCommon.relaxer.TMGFFSequence;
import Action.lineMode.regularSQL.ThresholdConfig;

/**
 * @author moroda
 * @version 3.1
 *  
 */
public class OuterLineModeResource extends LineModeAction {

    public TMGFF[] rangeSearch() {
        LineMode lineMode = null;
        String baseUrl = this.datasource.getUrl();
        String hHead = horizontal.getHeadString();
        String hCheck = horizontal.getCheck();
        StringBuffer urlBuf = new StringBuffer();
        urlBuf.append(baseUrl);
        if (baseUrl.indexOf("?") < 0) {
            urlBuf.append("?");
        }
        urlBuf.append("&hHead=");
        int idx = hHead.lastIndexOf("--");
        if (idx > -1) {
            hHead = hHead.substring(0, idx + 1) + "0";
        }
        urlBuf.append(hHead);
        urlBuf.append("&hCheck=");
        urlBuf.append(hCheck);
        try {
            if ((keyword.length == 0 && !datasource.getSearchTarget().equals("keyword")) || (keyword.length > 0 && !datasource.getSearchTarget().equals("interval"))) {
                if (keyword.length > 0) {
                    urlBuf.append("&keyword=");
                    log.debug("keyword = " + URLEncoder.encode(Keyword.combineKeyword(keyword)));
                    urlBuf.append(URLEncoder.encode(Keyword.combineKeyword(keyword)));
                }
                if (valge != null) {
                    urlBuf.append("&valge=");
                    urlBuf.append(URLEncoder.encode(valge));
                }
                if (valle != null) {
                    urlBuf.append("&valle=");
                    urlBuf.append(URLEncoder.encode(valle));
                }
                if (getLimitSearchCnt() >= 0) {
                    urlBuf.append("&maxhit=");
                    urlBuf.append(getLimitSearchCnt());
                }
                if (datasource.getProperty("histogram_threshold").length() > 0) {
                    urlBuf.append("&histogram_threshold=");
                    urlBuf.append(datasource.getProperty("histogram_threshold"));
                }
                if (highlight != null && highlight.length > 0) {
                    for (int i = 0; i < highlight.length; i++) {
                        urlBuf.append("&highlight=");
                        urlBuf.append(URLEncoder.encode(highlight[i]));
                    }
                }
                if (searchCriteria.isTargetAccession()) urlBuf.append("&targetColumn=accession");
                if (searchCriteria.isTargetAnnotation()) urlBuf.append("&targetColumn=annotation");
                if (searchCriteria.isMatchFull()) urlBuf.append("&isPerfectMatch=" + searchCriteria.isMatchFull());
                log.debug(urlBuf.toString());
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(urlBuf.toString());
                NodeList list = doc.getElementsByTagName("TMGFF");
                for (int i = 0; i < list.getLength(); i++) {
                    Element element = (Element) list.item(i);
                    int id = DatasourceManager.getId(datasource);
                    element.setAttribute("id", Integer.toString(id));
                }
                lineMode = new LineMode(doc);
            } else {
                lineMode = new LineMode();
            }
        } catch (Exception e) {
            log.error("Load error: " + urlBuf.toString());
            log.error(e);
            lineMode = new LineMode();
        }
        if (lineMode.sizeTMGFF() <= 0) {
            TMGFF tmgff = new TMGFF();
            LineModeDispatcher.setDatasourceConfig(tmgff, datasource);
            if (datasource.hasModifier()) {
                tmgff.setParentSource(datasource.getDescription());
                ThresholdConfig config = getThresholdConfig(tmgff.getId());
                TMGFFSequence thresholds = new TMGFFSequence();
                thresholds.setUpper(config.getUpperThreshold());
                thresholds.setLower(config.getLowerThreshold());
                thresholds.setMaxPicSize(config.getMaxPictureSize());
                thresholds.setMinPicSize(config.getMinPictureSize());
                log.debug("config.getUpperThreshold() = " + config.getUpperThreshold());
                tmgff.setTMGFFSequence(thresholds);
            }
            lineMode.addTMGFF(tmgff);
        } else {
            TMGFF[] tmgffs = lineMode.getTMGFF();
            for (int i = 0; i < tmgffs.length; i++) {
                TMGFF tmgff = tmgffs[i];
                tmgff.setTarget(datasource.getSearchTarget());
                tmgff.setSource(datasource.getDescription());
                if (datasource.hasAnnotation()) {
                    String annotation = datasource.getAnnotation();
                    tmgff.setAnnotation(annotation);
                    String href = datasource.getAnnotationHref();
                    if (href != null && href.length() > 0) {
                        tmgff.setHref(href);
                    }
                    boolean useGPSParam = datasource.isUseGPSParam();
                    if (useGPSParam) {
                        tmgff.setUseGPSParam(useGPSParam);
                    }
                }
                if (datasource.hasModifier()) {
                    tmgff.setParentSource(datasource.getDescription());
                    ThresholdConfig config = getThresholdConfig(tmgff.getId());
                    TMGFFSequence thresholds = new TMGFFSequence();
                    thresholds.setUpper(config.getUpperThreshold());
                    thresholds.setLower(config.getLowerThreshold());
                    thresholds.setMaxPicSize(config.getMaxPictureSize());
                    thresholds.setMinPicSize(config.getMinPictureSize());
                    tmgff.setTMGFFSequence(thresholds);
                }
                GROUP[] groups = new GROUP[0];
                if (tmgff.getSEGMENT().length > 0) groups = tmgff.getSEGMENT(0).getGROUP();
                for (int j = 0; j < groups.length; j++) {
                    String type = groups[j].getType();
                    if (datasource.hasProperty("type:" + type)) {
                        String[] style = datasource.getProperty("type:" + type).split(":");
                        if (style.length == 2 && style[0].equals("style")) groups[j].setStyle(style[1]);
                    }
                }
            }
        }
        return lineMode.getTMGFF();
    }

    public TMGFF[] positionSearch(String accessionName) {
        this.keyword = new String[] { accessionName };
        horizontal.setChromosome(AxisInfo.WHOLE_GENOME);
        return rangeSearch();
    }
}
