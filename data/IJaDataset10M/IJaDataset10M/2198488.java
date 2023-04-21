package Action.lineMode.lineModeCommon;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import Action.AxisInfo;
import Action.lineMode.accessionSearch.AccessionSearchCriteria;
import Action.lineMode.lineModeCommon.relaxer.ExternalLink;
import Action.lineMode.lineModeCommon.relaxer.LINK;
import Action.lineMode.lineModeCommon.relaxer.SEGMENT;
import Action.lineMode.lineModeCommon.relaxer.TMGFF;
import Action.lineMode.lineModeCommon.relaxer.TYPE;
import Action.lineMode.lineModeCommon.relaxer.VALUE;
import Action.lineMode.regularSQL.ThresholdConfig;
import Action.species.InitXml;
import Action.species.Species;

/**
 * @author moroda
 * @version 3.1
 *  
 */
public abstract class LineModeAction {

    static Logger log = Logger.getLogger(LineModeAction.class);

    protected Datasource datasource;

    protected String linkTarget = "";

    protected boolean linkUseParam = false;

    protected boolean forceNoHistogram = false;

    protected AxisInfo horizontal;

    protected String[] keyword;

    protected String[] highlight;

    protected int[] procNameIdList;

    protected Map thresholdMap;

    protected AccessionSearchCriteria searchCriteria;

    protected String valge;

    protected String valle;

    protected String tissueName;

    protected String experimentId;

    protected Hashtable paramTable;

    protected StringBuffer urlBuf;

    protected String maxhit;

    public abstract TMGFF[] rangeSearch();

    /**
	 * @param con
	 * @param name
	 * @return AxisInfo[] name�̈ʒu�B�����ꍇ��size 0��Array��Ԃ�
	 * @throws SQLException
	 */
    public abstract TMGFF[] positionSearch(String accessionName);

    /**
	 *  
	 */
    public LineModeAction() {
        super();
        thresholdMap = new HashMap();
    }

    /**
	 * @param action
	 */
    public LineModeAction(LineModeAction action) {
        this.datasource = action.datasource;
        this.linkTarget = action.linkTarget;
        this.linkUseParam = action.linkUseParam;
        this.horizontal = action.horizontal;
        this.keyword = action.keyword;
        this.procNameIdList = action.procNameIdList;
        this.thresholdMap = action.thresholdMap;
        this.forceNoHistogram = action.forceNoHistogram;
        this.searchCriteria = action.searchCriteria;
        this.valge = action.valge;
        this.valle = action.valle;
        this.tissueName = action.tissueName;
        this.experimentId = action.experimentId;
        this.paramTable = action.paramTable;
        this.highlight = action.highlight;
        this.maxhit = action.maxhit;
    }

    /**
	 * id��DataSource��ID�ŁA�e�f�[�^��iproc�j����臒l��ݒ肵�Ă���B session -> DasServerMapping ->
	 * system default�̏��Ɍ���āA�擾�B
	 * 
	 * @param id
	 * @return
	 */
    protected ThresholdConfig getThresholdConfig(int id) {
        ThresholdConfig config = null;
        if (thresholdMap.containsKey(new Integer(id))) {
            config = (ThresholdConfig) thresholdMap.get(new Integer(id));
        } else {
            config = new ThresholdConfig(id);
            try {
                if (datasource.hasProperty("upper_threshold")) {
                    config.setUpperThreshold(Float.parseFloat(datasource.getProperty("upper_threshold")));
                }
                if (datasource.hasProperty("lower_threshold")) {
                    config.setLowerThreshold(Float.parseFloat(datasource.getProperty("lower_threshold")));
                }
                if (datasource.hasProperty("max_picture_size")) {
                    config.setMaxPictureSize(Float.parseFloat(datasource.getProperty("max_picture_size")));
                }
                if (datasource.hasProperty("min_picture_size")) {
                    config.setMinPictureSize(Float.parseFloat(datasource.getProperty("min_picture_size")));
                }
            } catch (NumberFormatException e) {
                log.error("[" + datasource.getDescription() + "] config error: ");
                log.error(e);
            }
        }
        return config;
    }

    /**
	 * 
	 * keyword����s���Ɍ����݂̂�Ԃ��ꍇ��臒l��擾����B<p>
	 * (init.xml�ɋL�q����Ă���j
	 * limitCount��HTTP���N�G�X�g�p�����[�^�ɂ��B��ꍇ�́A�������D�悷��
	 * 
	 * @return ������臒l
	 */
    public int getLimitSearchCnt() {
        int limitSearchCnt = 0;
        try {
            if (this.maxhit != null && this.maxhit.length() > 0) {
                limitSearchCnt = Integer.parseInt(this.maxhit);
            } else {
                limitSearchCnt = InitXml.getInstance().getLimitSearchCnt();
            }
            log.debug("limitCount = " + limitSearchCnt);
        } catch (IOException e) {
            log.error(e);
            e.printStackTrace();
        }
        return limitSearchCnt;
    }

    /**
	 * @param horizontal
	 *            The horizontal to set.
	 */
    public void setHorizontal(AxisInfo horizontal) {
        this.horizontal = horizontal;
    }

    /**
	 * @param keyword
	 *            The keyword to set.
	 */
    public void setKeyword(String[] keyword) {
        this.keyword = keyword;
    }

    public void setHighlight(String[] highlitght) {
        this.highlight = highlitght;
    }

    /**
	 * @param datasource
	 *            The datasource to set.
	 */
    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;
        this.linkTarget = this.datasource.getProperty("link_target");
        if (this.datasource.getProperty("link_use_param").equals("true")) {
            this.linkUseParam = true;
        }
    }

    /**
	 * @param thresholdMap
	 *            The thresholdMap to set.
	 */
    public void setThresholdMap(Map thresholdMap) {
        this.thresholdMap = thresholdMap;
    }

    /**
	 * �\���ΏۂƂȂBĂ���f�[�^��id��Array null�͕\���̐����s��Ȃ�
	 * 
	 * @param list
	 */
    public void setProcNameIdList(int[] idList) {
        procNameIdList = idList;
    }

    /**
	 * @return Returns the datasource.
	 */
    public Datasource getDatasource() {
        return datasource;
    }

    /**
	 * @return Returns the linkTarget.
	 */
    public String getLinkTarget() {
        return linkTarget;
    }

    /**
	 * @param linkTarget
	 *            The linkTarget to set.
	 */
    public void setLinkTarget(String linkTarget) {
        this.linkTarget = linkTarget;
    }

    /**
	 * @return Returns the horizontal.
	 */
    public AxisInfo getHorizontal() {
        return horizontal;
    }

    /**
	 * @return Returns the keyword.
	 */
    public String[] getKeyword() {
        return keyword;
    }

    public String[] getHighlight() {
        return highlight;
    }

    /**
	 * @return Returns the linkUseParam.
	 */
    public boolean isLinkUseParam() {
        return linkUseParam;
    }

    /**
	 * @return Returns the procNameIdList.
	 */
    public int[] getProcNameIdList() {
        return procNameIdList;
    }

    /**
	 * @return Returns the thresholdMap.
	 */
    public Map getThresholdMap() {
        return thresholdMap;
    }

    /**
	 * @return Returns the forceNoHistogram.
	 */
    public boolean isForceNoHistogram() {
        return forceNoHistogram;
    }

    /**
	 * @param forceNoHistogram
	 *            The forceNoHistogram to set.
	 */
    public void setForceNoHistogram(boolean forceAllData) {
        this.forceNoHistogram = forceAllData;
    }

    /**
	 * @return Returns the searchCriteria.
	 */
    public AccessionSearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

    /**
	 * @param searchCriteria
	 *            The searchCriteria to set.
	 */
    public void setSearchCriteria(AccessionSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public final SEGMENT[] createSEGMENTS() throws IOException {
        Species species = InitXml.getInstance().getSpecies(horizontal.getSpeciesAlias(), horizontal.getCheck());
        List chrList = species.getChromosomeNames();
        SEGMENT[] segments = new SEGMENT[chrList.size()];
        for (int i = 0; i < chrList.size(); i++) {
            String chrName = (String) chrList.get(i);
            long length = species.getChromosomeLength(chrName);
            SEGMENT segment = new SEGMENT();
            segment.setId(chrName);
            segment.setStart(1);
            segment.setEnd(length);
            segments[i] = segment;
        }
        return segments;
    }

    /**
	 * Method createSEGMENT.
	 * 
	 * @param axis
	 * @return
	 */
    protected static final SEGMENT createSEGMENT(AxisInfo axis) {
        SEGMENT segment = new SEGMENT();
        segment.setId(axis.getChromosome());
        segment.setStart(axis.getStart());
        segment.setEnd(axis.getEnd());
        return segment;
    }

    /**
	 * Method createSEGMENT.
	 * 
	 * @param speciesAlias
	 * @param check
	 * @param chromosome
	 * @return
	 */
    private static final SEGMENT createSEGMENT(String speciesAlias, String check, String chromosome) {
        SEGMENT segment = new SEGMENT();
        segment.setId(chromosome);
        try {
            Species species = InitXml.getInstance().getSpecies(speciesAlias, check);
            long len = species.getChromosomeLength(chromosome);
            segment.setStart(1);
            segment.setEnd(len);
        } catch (IOException e) {
            log.error("", e);
        }
        return segment;
    }

    /**
	 * Method makeTYPE.
	 * 
	 * @param id
	 * @param name
	 * @return TYPE
	 */
    protected static final TYPE makeTYPE(String id, String name) {
        TYPE _type = new TYPE();
        _type.setIdByString(id);
        _type.setContent(name);
        return _type;
    }

    /**
	 * Method makeLINK.
	 * 
	 * @param url
	 * @param text
	 * @return LINK
	 */
    protected LINK makeLINK(String url, String text) {
        if (url == null) {
            url = "";
        }
        if (text == null) {
            text = "";
        }
        LINK _link = new LINK();
        _link.setHrefByString(url);
        _link.setContent(text);
        if (this.linkTarget.length() > 0) {
            _link.setTarget(this.linkTarget);
        }
        if (this.linkUseParam) {
            _link.setUseParam(true);
        }
        return _link;
    }

    protected ExternalLink makeExternalLink(String url, String text, int width, int height) {
        ExternalLink _link = new ExternalLink();
        _link.setHrefByString(url);
        _link.setContent(text);
        if (width > 0) {
            _link.setWidth(width);
        }
        if (height > 0) {
            _link.setHeight(height);
        }
        if (this.linkTarget.length() > 0) {
            _link.setTarget(this.linkTarget);
        }
        return _link;
    }

    /**
	 * Method makeValue.
	 * 
	 * @param name
	 * @param value
	 * @return VALUE
	 */
    protected static final VALUE makeVALUE(String name, int value) {
        return makeVALUE(name, (double) value);
    }

    /**
	 * Method makeValue.
	 * 
	 * @param name
	 * @param value
	 * @return VALUE
	 */
    protected static final VALUE makeVALUE(String name, double value) {
        VALUE _value = new VALUE();
        _value.setName(name);
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("##0.########");
        String valueString = df.format(value);
        _value.setContentByString(valueString);
        return _value;
    }

    /**
     * @return valge ��߂��܂��B
     */
    public String getValge() {
        return valge;
    }

    /**
     * @return valle ��߂��܂��B
     */
    public String getValle() {
        return valle;
    }

    /**
     * @return tissueName ��߂��܂��B
     */
    public String getTissueName() {
        return tissueName;
    }

    /**
     * @return experimentId ��߂��܂��B
     */
    public String getExperimentId() {
        return experimentId;
    }

    /**
     * @return paramTable��߂��܂��B
     */
    public Hashtable getParamTable() {
        return paramTable;
    }

    /**
     * @param valge valge ��ݒ�B
     */
    public void setValge(String valge) {
        this.valge = valge;
    }

    /**
     * @param valle valle ��ݒ�B
     */
    public void setValle(String valle) {
        this.valle = valle;
    }

    /**
     * @param tissueName tissueName ��ݒ�B
     */
    public void setTissueName(String tissueName) {
        this.tissueName = tissueName;
    }

    /**
     * @param experimentId experimentId ��ݒ�B
     */
    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    /**
     * @param paramTable paramTable��ݒ�B
     */
    public void setParamTable(Hashtable paramTable) {
        this.paramTable = paramTable;
    }

    public String getUrl() {
        return urlBuf.toString();
    }

    public void setUrl(StringBuffer url) {
        this.urlBuf = url;
    }

    public String getMaxhit() {
        return maxhit;
    }

    public void setMaxhit(String maxhit) {
        this.maxhit = maxhit;
    }
}
