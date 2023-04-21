package com.angis.fx.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.angis.fx.data.CKTypeInfo;
import com.angis.fx.data.GISZFJGInfo;

public class SystemUpdateParseUtil {

    public static Map<String, List<CKTypeInfo>> CKResultParse(String pSource) {
        try {
            String[] lRess = pSource.split("\\^");
            Map<String, List<CKTypeInfo>> lMap = new HashMap<String, List<CKTypeInfo>>();
            if (null != lRess && lRess[0].length() > 0) {
                String[] lRes = lRess[0].split("&");
                List<CKTypeInfo> lZCTypeInfoList = new ArrayList<CKTypeInfo>();
                if (null != lRes) {
                    for (String res : lRes) {
                        String[] lWGType = res.split("#");
                        if (null != lWGType && lWGType.length == 4) {
                            CKTypeInfo lCKTypeInfo = new CKTypeInfo();
                            lCKTypeInfo.setWid(lWGType[0]);
                            lCKTypeInfo.setConditionType(lWGType[1]);
                            lCKTypeInfo.setWgType(lWGType[2]);
                            lCKTypeInfo.setCreationTime(lWGType[3]);
                            lZCTypeInfoList.add(lCKTypeInfo);
                        }
                    }
                }
                lMap.put("ZCTYPE", lZCTypeInfoList);
            }
            if (null != lRess && lRess[1].length() > 0) {
                String[] lRes = lRess[1].split("&");
                List<CKTypeInfo> lWGTypeInfoList = new ArrayList<CKTypeInfo>();
                if (null != lRes) {
                    for (String res : lRes) {
                        String[] lWGType = res.split("#");
                        if (null != lWGType && lWGType.length == 4) {
                            CKTypeInfo lCKTypeInfo = new CKTypeInfo();
                            lCKTypeInfo.setWid(lWGType[0]);
                            lCKTypeInfo.setConditionType(lWGType[1]);
                            lCKTypeInfo.setWgType(lWGType[2]);
                            lCKTypeInfo.setCreationTime(lWGType[3]);
                            lWGTypeInfoList.add(lCKTypeInfo);
                        }
                    }
                }
                lMap.put("WGTYPE", lWGTypeInfoList);
            }
            return lMap;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String> ecnomicTypeParse(String pSource) {
        try {
            String[] lRes = pSource.split("\\~");
            List<String> lEcnomicTypeList = new ArrayList<String>();
            if (null != lRes) {
                for (String res : lRes) {
                    if (res.length() > 0) {
                        lEcnomicTypeList.add(res);
                    }
                }
            }
            return lEcnomicTypeList;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String> businessTypeParse(String pSource) {
        try {
            String[] lRes = pSource.split("\\~");
            List<String> lBusinessTypeList = new ArrayList<String>();
            if (null != lRes) {
                for (String res : lRes) {
                    if (res.length() > 0) {
                        lBusinessTypeList.add(res);
                    }
                }
            }
            return lBusinessTypeList;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<GISZFJGInfo> gisZFJGParse(String pSource) {
        try {
            String[] lRes = pSource.split("\\~");
            List<GISZFJGInfo> lGISZFJGInfoList = new ArrayList<GISZFJGInfo>();
            if (null != lRes) {
                for (String res : lRes) {
                    if (res.length() > 0) {
                        String[] lRess = res.split("\\^");
                        if (null != lRess && lRess.length == 3) {
                            GISZFJGInfo lGisZFJGInfo = new GISZFJGInfo();
                            lGisZFJGInfo.setUserlayer(lRess[0]);
                            lGisZFJGInfo.setX(lRess[1]);
                            lGisZFJGInfo.setY(lRess[1]);
                            lGISZFJGInfoList.add(lGisZFJGInfo);
                        }
                    }
                }
            }
            return lGISZFJGInfoList;
        } catch (Exception e) {
            return null;
        }
    }
}
