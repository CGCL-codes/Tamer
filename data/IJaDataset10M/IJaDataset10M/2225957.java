package org.nbrowse.dbloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.nbrowse.sql.NoResult;
import org.nbrowse.utils.ErrorUtil;

/** For loading up urls/links. Currently nb1/hueylings dataloader_url.pl loads
 * node urls and for edge it just loads from-to syns and doesnt handle edge syn urls
 * and edge attrib urls
 * First to add here is edge attrib urls as that is needed right away (for pubmed ids for tair)
 * then edge syn urls should be added (note there is a edge syn urls in nyu nbrowse db, but
 * the script that did this seems to be lost and perhaps not general?)
 * @author mgibson
 */
public final class UrlLoader {

    private static int MIN_COLS = 5;

    private String urlType;

    private String linkTitle;

    private String edgeType;

    private String edgeDataset;

    private Param lastParam;

    private Param secondToLastParam;

    private String baseUrl;

    /** @param args currently not used, filename is in config file 
   * @throws Exception */
    public static void main(String[] args) throws Exception {
        parseFile();
    }

    private static void parseFile() throws Exception {
        TabFile f = new TabFile(getUrlFile(), MIN_COLS);
        String[] lineArray = f.getTabSplitLine();
        while (lineArray != null) {
            UrlLoader l;
            try {
                l = new UrlLoader(lineArray);
                l.insert();
            } catch (BadUrl e) {
                ErrorUtil.error("Skipping bad url " + e);
            }
            lineArray = f.getTabSplitLine();
        }
    }

    private static String getUrlFile() {
        return LoaderConfig.inst().getUrlAttrFilename();
    }

    public UrlLoader(String[] lineArray) throws BadUrl {
        mapArrayToVars(lineArray);
    }

    private void mapArrayToVars(String[] lineArray) throws BadUrl {
        urlType = lineArray[0];
        linkTitle = lineArray[1];
        edgeType = lineArray[2];
        edgeDataset = lineArray[3];
        String url = lineArray[4];
        parseUrl(url);
    }

    private class BadUrl extends Exception {

        BadUrl(String s) {
            super(s);
        }
    }

    /** parses url into String baseUrl and Param lastParam & secondToLastParam */
    private void parseUrl(String url) throws BadUrl {
        if (url == null) throw new BadUrl(url);
        String[] baseAndParams = url.split("\\?");
        if (baseAndParams.length == 1) {
            baseAndParams = url.split("/");
            String last = baseAndParams[baseAndParams.length - 1];
            lastParam = new Param(last);
            baseUrl = url.substring(0, url.length() - last.length());
            return;
        }
        baseUrl = baseAndParams[0] + "?";
        String paramsString = baseAndParams[1];
        if (paramsString == null || paramsString.trim().equals("")) throw new BadUrl(url);
        String params[] = paramsString.split("&");
        for (int i = 0; params.length > 2 && i < params.length - 2; i++) baseUrl += params[i] + "&";
        String lastParamString = params[params.length - 1];
        lastParam = new Param(lastParamString);
        String secondToLastParamStr = params.length > 1 ? params[params.length - 2] : null;
        secondToLastParam = new Param(secondToLastParamStr);
    }

    /** param contains both callString and value eg &gene=AB123 would have callString gene=
   * and value AB123 */
    private final class Param {

        private final String callString, value;

        private final boolean haveCallString;

        private Param(String param) throws BadUrl {
            String[] callStrAndVal = param.split("=");
            if (callStrAndVal.length == 1) {
                callString = "";
                haveCallString = false;
                value = param;
            } else {
                callString = callStrAndVal[0] + "=";
                haveCallString = true;
                value = callStrAndVal[1];
            }
        }

        public String toString() {
            return callString + value;
        }

        public boolean hasCallString() {
            return haveCallString;
        }
    }

    private void insert() throws BadUrl, Exception {
        String val = lastParam.value;
        EdgeAttribute ea;
        try {
            List<EdgeAttribute> edgeAtts = EdgeAttributeExtent.inst().queryValue(val, 1);
            ea = edgeAtts.get(0);
        } catch (NoResult e) {
            throw new BadUrl(val + " not found in db(edge_att)");
        }
        ExternalUrl e = new ExternalUrl(taxon(), urlType, linkTitle, url());
        e.insert();
        Attribute callStrAtt = null;
        if (haveCallString()) callStrAtt = Attribute.makeWithAbbrev(abbrev());
        UrlAttribute u = new UrlAttribute(e, ea, attribLookupTableName(), callStrAtt);
        u.insert();
    }

    private boolean haveCallString() {
        return lastParam.hasCallString();
    }

    private int taxon() {
        return LoaderConfig.inst().getTaxonId();
    }

    private String attribLookupTableName() {
        return "attribute";
    }

    private String url() {
        return baseUrl;
    }

    /** for now assumes theres just lastParam(not from-to)*/
    private String abbrev() {
        return lastParam.callString;
    }
}
