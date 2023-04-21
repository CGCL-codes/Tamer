package org.cleartk.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.cas.TOP;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * 
 * @author Philip Ogren
 * @author Philipp Wetzler
 * @author Steven Bethard
 * 
 */
public class UIMAUtil {

    public static FSArray toFSArray(JCas jCas, List<? extends FeatureStructure> fsList) {
        if (fsList == null) {
            return new FSArray(jCas, 0);
        }
        FSArray fsArray = new FSArray(jCas, fsList.size());
        fsArray.copyFromArray(fsList.toArray(new FeatureStructure[fsList.size()]), 0, 0, fsList.size());
        return fsArray;
    }

    public static StringArray toStringArray(JCas jCas, String[] sArray) {
        StringArray uimaSArray = new StringArray(jCas, sArray.length);
        uimaSArray.copyFromArray(sArray, 0, 0, sArray.length);
        return uimaSArray;
    }

    public static List<String> toList(StringArray sArray) {
        List<String> result = new ArrayList<String>(sArray.size());
        for (int i = 0; i < sArray.size(); i++) {
            result.add(sArray.get(i));
        }
        return result;
    }

    public static <T extends FeatureStructure> List<T> toList(FSArray fsArray, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        if (fsArray == null) {
            return list;
        }
        for (FeatureStructure fs : fsArray.toArray()) {
            list.add(cls.cast(fs));
        }
        return list;
    }

    public static Type getCasType(JCas jCas, Class<? extends TOP> cls) {
        try {
            return jCas.getCasType(cls.getField("type").getInt(null));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String readSofa(JCas view) throws IOException {
        InputStream in = view.getSofaDataStream();
        StringBuffer tmp = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            tmp.append(new String(b, 0, n));
        }
        return tmp.toString();
    }
}
