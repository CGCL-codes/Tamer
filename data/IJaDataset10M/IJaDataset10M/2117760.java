package Action.lineMode.lineModeCommon;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.collections.SequencedHashMap;
import org.apache.log4j.Logger;
import Action.lineMode.lineModeCommon.relaxer.GROUP;
import Action.lineMode.lineModeCommon.relaxer.LINK;
import Action.lineMode.lineModeCommon.relaxer.SEGMENT;
import Action.lineMode.lineModeCommon.relaxer.TMGFF;
import Action.lineMode.lineModeCommon.relaxer.VALUE;

public abstract class SQLSearchActionAbstract extends LineModeAction {

    static Logger log = Logger.getLogger(SQLSearchActionAbstract.class);

    protected int positiveColor[] = { 0xFF, 0x00, 0x00 };

    protected int negativeColor[] = { 0x00, 0xFF, 0x00 };

    protected int middleColor[] = { 0x88, 0x88, 0x88 };

    public SQLSearchActionAbstract() {
        super();
    }

    public SQLSearchActionAbstract(SQLSearchActionAbstract action) {
        super(action);
    }

    protected abstract TMGFF[] rangeSearch(Connection con) throws SQLException;

    protected abstract TMGFF[] positionSearch(String name, Connection con) throws SQLException;

    public TMGFF[] rangeSearch() {
        TMGFF[] tmgffs = null;
        Connection con = null;
        try {
            setColorConfig();
            con = datasource.getConnection();
            tmgffs = rangeSearch(con);
            for (int i = 0; i < tmgffs.length; i++) {
                SEGMENT[] segments = tmgffs[i].getSEGMENT();
                ArrayList<SEGMENT> newSegments = new ArrayList<SEGMENT>();
                for (int j = 0; j < segments.length; j++) {
                    if (segments[j].sizeGROUP() > 0) {
                        newSegments.add(segments[j]);
                    }
                }
                tmgffs[i].setSEGMENT((SEGMENT[]) newSegments.toArray(new SEGMENT[0]));
            }
        } catch (Exception e) {
            log.error("error occured: " + datasource.getName() + "[" + datasource.getUrl() + "]");
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception e1) {
            }
        }
        return tmgffs;
    }

    public TMGFF[] positionSearch(String name) {
        TMGFF[] articles = null;
        Connection con = null;
        try {
            con = datasource.getConnection();
            articles = positionSearch(name, con);
        } catch (Exception e) {
            articles = new TMGFF[0];
            log.error("error occured: " + datasource.getName() + "[" + datasource.getUrl() + "]");
            log.error(e);
        } finally {
            try {
                con.close();
            } catch (Exception e1) {
            }
        }
        return articles;
    }

    protected void setColorConfig() {
        try {
            String baseColor = datasource.getProperty("middle_color");
            if (baseColor.length() > 0) {
                this.middleColor = convertColorValue(Integer.parseInt(baseColor, 16));
            }
            String positiveColor = datasource.getProperty("positive_color");
            if (positiveColor.length() > 0) {
                this.positiveColor = convertColorValue(Integer.parseInt(positiveColor, 16));
            }
            String negativeColor = datasource.getProperty("negative_color");
            if (negativeColor.length() > 0) {
                this.negativeColor = convertColorValue(Integer.parseInt(negativeColor, 16));
            }
        } catch (Exception e) {
            log.error("color configuration error: " + this.datasource.getName());
            log.error(e);
        }
    }

    public static final int[] convertColorValue(int value) {
        return new int[] { (value >> 16) & 0xff, (value >> 8) & 0xff, (value >> 0) & 0xff };
    }

    public String getMiddleColor() {
        int v = middleColor[0] * 65536 + middleColor[1] * 256 + middleColor[2];
        return convertRgbString(v);
    }

    public String getNegativeColor() {
        int v = negativeColor[0] * 65536 + negativeColor[1] * 256 + negativeColor[2];
        return convertRgbString(v);
    }

    public String getPositiveColor() {
        int v = positiveColor[0] * 65536 + positiveColor[1] * 256 + positiveColor[2];
        return convertRgbString(v);
    }

    public static final String convertRgbString(int value) {
        String s = Integer.toHexString(value);
        while (s.length() < 6) {
            s = "0" + s;
        }
        return s;
    }

    /**
	 * 臒l��������f�[�^�v�f�������Ă���ꍇ�ɁA �q�X�g�O�����̃f�[�^�ɕϊ�����
	 * 
	 * @param tmgff
	 * @return
	 */
    protected TMGFF convertHistogram(TMGFF tmgff) {
        if (tmgff.sizeSEGMENT() == 0) {
            return tmgff;
        }
        try {
            for (int k = 0; k < tmgff.sizeSEGMENT(); k++) {
                SEGMENT segm = tmgff.getSEGMENT(k);
                SequencedHashMap countMap = new SequencedHashMap();
                GROUP[] groups = segm.getGROUP();
                if (groups.length > DatasourceManager.COUNT_THRETHOLD) {
                    long start = segm.getStart();
                    long range = (long) Math.floor(((float) segm.getEnd() - segm.getStart()) / DatasourceManager.DATA_AREA_COUNT) + 1;
                    for (int i = 0; i < groups.length; i++) {
                        GROUP group = groups[i];
                        float pos = (group.getEND() + group.getSTART()) / 2f;
                        String strand = group.getORIENTATION();
                        int index = (int) Math.floor((pos - start) / range);
                        if (index < 0 || index >= DatasourceManager.DATA_AREA_COUNT) {
                            segm.removeGROUP(group);
                            log.debug("reject: out of index");
                            continue;
                        }
                        int[] counts = null;
                        if (countMap.containsKey(strand)) {
                            counts = (int[]) countMap.get(strand);
                        } else {
                            counts = new int[DatasourceManager.DATA_AREA_COUNT];
                        }
                        counts[index]++;
                        countMap.put(strand, counts);
                        segm.removeGROUP(group);
                    }
                    for (int i = 0; i < DatasourceManager.DATA_AREA_COUNT; i++) {
                        GROUP _g = new GROUP();
                        _g.setId(groups[0].getType());
                        _g.setLabel(groups[0].getType());
                        _g.setType("Histogram");
                        _g.setSTART(start);
                        _g.setEND(start + range - 1);
                        _g.setLINK(new LINK());
                        for (int j = 0; j < countMap.size(); j++) {
                            String strand = (String) countMap.get(j);
                            int[] counts = (int[]) countMap.get(strand);
                            GROUP group = (GROUP) _g.clone();
                            group.setORIENTATION(strand);
                            VALUE v = new VALUE();
                            v.setName("count");
                            v.setContent(counts[i]);
                            group.setVALUE(v);
                            group.setNOTE(DatasourceManager.FREQUENCY + Integer.toString(counts[i]) + ", " + DatasourceManager.POSITION + group.getSTART() + " - " + group.getEND());
                            segm.addGROUP(group);
                        }
                        start += range;
                    }
                    tmgff.addSEGMENT(k, segm);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return tmgff;
    }
}
