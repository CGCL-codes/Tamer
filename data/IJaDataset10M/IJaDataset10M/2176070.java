package Action.lineMode;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import process.primer3.Primer3Param;
import Action.species.InitXml;
import Action.species.Species;
import Action.textMode.Primer3Manager;
import Action.textMode.SequenceManager;
import Action.textMode.TextModeManager;

public class GenePrimerDesignServlet extends HttpServlet {

    public GenePrimerDesignServlet() {
    }

    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) throws ServletException {
        doService(httpservletrequest, httpservletresponse);
    }

    public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) throws ServletException {
        doService(httpservletrequest, httpservletresponse);
    }

    public void doPut(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) throws ServletException {
        doService(httpservletrequest, httpservletresponse);
    }

    public void doService(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        String message = "";
        String actionType = req.getParameter("actionType");
        String hHead = "";
        int upstreamSize = 0;
        int downstreamSize = 0;
        String[] targetRegions = null;
        String seq = "";
        if (actionType.equals("design")) {
            hHead = req.getParameter("hHead");
            String hCheck = req.getParameter("hCheck");
            String db = req.getParameter("db");
            String url = req.getParameter("url");
            String primerTarget = req.getParameter("primerTarget");
            String showAll = req.getParameter("showAll");
            String alias = hHead.substring(0, hHead.indexOf(":"));
            String chr = hHead.substring(hHead.indexOf(":") + 1, hHead.indexOf(":", hHead.indexOf(":") + 1));
            String from = hHead.substring((hHead.indexOf(":", hHead.indexOf(":") + 1)) + 1, hHead.indexOf("-"));
            String to = hHead.substring(hHead.indexOf("-") + 1);
            if (showAll == null) showAll = "false";
            long chrLength = 0;
            try {
                Species species = InitXml.getInstance().getSpecies(alias, hCheck);
                chrLength = species.getChromosomeLength(chr);
                long fromLong = Long.parseLong(from);
                long toLong = Long.parseLong(to);
                if (fromLong < 0 || toLong > chrLength || fromLong > toLong) {
                    message = "The selected interval is invalid.";
                }
            } catch (NumberFormatException e) {
                message = "Invalid Genomic Interval.";
            } catch (Exception e) {
                throw new ServletException(e);
            }
            if (primerTarget == null || primerTarget.length() == 0) {
                message = "Please select one gene.";
            }
            if (message.length() == 0) {
                String[] primerTargetStr = StringUtils.split(primerTarget, ":");
                String dsId = primerTargetStr[0];
                int groupIndex = Integer.parseInt(primerTargetStr[1]);
                int targetStart = Integer.parseInt(primerTargetStr[2]);
                int targetEnd = Integer.parseInt(primerTargetStr[3]);
                String targetStrand = primerTargetStr[4];
                String sourceUrl = getSourceUrl(req.getServerName(), db, hCheck, hHead, "", dsId, url);
                targetRegions = req.getParameterValues("targetRegion");
                int marginLength = Primer3Manager.DEFAULT_MARGIN_LENGTH;
                int productMaxSize = Primer3Manager.DEFAULT_PRIMER_PRODUCT_MAX_SIZE;
                int primerMaxSize = Primer3Manager.DEFAULT_PRIMER_MAX_SIZE;
                try {
                    marginLength = Integer.parseInt(req.getParameter("MARGIN_LENGTH"));
                } catch (Exception e) {
                }
                try {
                    productMaxSize = Integer.parseInt(req.getParameter("MUST_XLATE_PRODUCT_MAX_SIZE"));
                } catch (Exception e) {
                }
                try {
                    primerMaxSize = Integer.parseInt(req.getParameter("PRIMER_MAX_SIZE"));
                } catch (Exception e) {
                }
                try {
                    for (int i = 0; i < targetRegions.length; i++) {
                        if (targetRegions[i].equals("upstream")) {
                            upstreamSize = Integer.parseInt(req.getParameter("upstream_size"));
                        }
                    }
                } catch (Exception e) {
                }
                try {
                    for (int i = 0; i < targetRegions.length; i++) {
                        if (targetRegions[i].equals("downstream")) {
                            downstreamSize = Integer.parseInt(req.getParameter("downstream_size"));
                        }
                    }
                } catch (Exception e) {
                }
                int seqStart = 0;
                int seqEnd = 0;
                if (targetStrand.equals("-")) {
                    seqStart = targetStart - (marginLength + productMaxSize + primerMaxSize) - downstreamSize;
                    seqStart = seqStart > 0 ? seqStart : 1;
                    seqEnd = targetEnd + (marginLength + productMaxSize + primerMaxSize) + upstreamSize;
                    seqEnd = seqEnd < (int) chrLength ? seqEnd : (int) chrLength;
                } else {
                    seqStart = targetStart - (marginLength + productMaxSize + primerMaxSize) - upstreamSize;
                    seqStart = seqStart > 0 ? seqStart : 1;
                    seqEnd = targetEnd + (marginLength + productMaxSize + primerMaxSize) + downstreamSize;
                    seqEnd = seqEnd < (int) chrLength ? seqEnd : (int) chrLength;
                }
                String targetHHead = alias + ":" + chr + ":" + seqStart + "-" + seqEnd;
                TextModeManager manager = new TextModeManager(targetHHead, hCheck, sourceUrl);
                String[] strs = manager.filterDoc(dsId, groupIndex, upstreamSize, downstreamSize);
                SequenceManager seqManager = new SequenceManager(getInitParameter("SEQUENCE_SERVER_CONFIG"));
                try {
                    seq = seqManager.getSequence(targetHHead, hCheck);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    message = e1.getMessage();
                }
                Primer3Manager primer3Manager = null;
                try {
                    primer3Manager = setParameters(req);
                    primer3Manager.setUpstreamSize(upstreamSize);
                    primer3Manager.setDownstreamSize(downstreamSize);
                    primer3Manager.setTargetRegions(targetRegions);
                } catch (Exception e) {
                    message = e.getMessage();
                }
                if (message.length() == 0) {
                    primer3Manager.setPrimer3BinPath(getInitParameter("PRIMER3_BIN_PATH"));
                    primer3Manager.setWorkDirPath(getInitParameter("WORK_DIR_PATH"));
                    primer3Manager.setWholeSequence(seq);
                    primer3Manager.setMarginLength(marginLength);
                    ArrayList outputList = primer3Manager.exec(manager.getDoc());
                    ArrayList itemList = manager.lineMode2TextMode("+");
                    itemList = manager.addFlankingRegion(itemList, upstreamSize, downstreamSize, "+");
                    ArrayList primerList = manager.primer3Output2TextMode(outputList, targetStrand);
                    if (primerList != null) itemList.addAll(primerList);
                    ArrayList lineList = manager.getLineList(itemList, seq, showAll.equals("true"), "+");
                    req.setAttribute("LINE_LIST", lineList);
                    req.setAttribute("SEQUENCE", seq);
                    req.setAttribute("OUTPUT_LIST", outputList);
                    req.setAttribute("MARGIN_LENGTH", String.valueOf(primer3Manager.getMarginLength()));
                    req.setAttribute("upstreamSize", String.valueOf(upstreamSize));
                    req.setAttribute("downstreamSize", String.valueOf(downstreamSize));
                    req.setAttribute("targetRegions", targetRegions);
                }
                req.setAttribute("dsName", strs[0]);
                req.setAttribute("accession", strs[1]);
            }
            req.setAttribute("db", db);
            req.setAttribute("url", url);
            req.setAttribute("hHead", hHead);
            req.setAttribute("hCheck", hCheck);
            req.setAttribute("primerTarget", primerTarget);
            req.setAttribute("MESSAGE", message);
            req.setAttribute("actionType", actionType);
            req.setAttribute("showAll", showAll);
            String forwardUrl = "";
            forwardUrl = getInitParameter(actionType);
            if (forwardUrl != null && forwardUrl.length() > 0) {
                ServletContext sc = getServletContext();
                RequestDispatcher rd = sc.getRequestDispatcher(forwardUrl);
                try {
                    rd.forward(req, res);
                } catch (Exception e) {
                    throw new ServletException(e);
                }
            }
        }
        if (actionType.equals("designparent")) {
            ArrayList outputList = null;
            ArrayList paramList = null;
            String sequence = "";
            if (req.getParameter("sequence") != null && req.getParameter("sequence").length() > 0) {
                sequence = req.getParameter("sequence");
            }
            int marginLength = Primer3Manager.DEFAULT_MARGIN_LENGTH;
            try {
                marginLength = Integer.parseInt(req.getParameter("MARGIN_LENGTH"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Primer3Manager primer3Manager = null;
            try {
                primer3Manager = setParameters(req);
                primer3Manager.setUpstreamSize(upstreamSize);
                primer3Manager.setDownstreamSize(downstreamSize);
            } catch (Exception e) {
                message = e.getMessage();
            }
            if (message.length() == 0) {
                paramList = new ArrayList();
                if (req.getParameterValues("target") != null && req.getParameterValues("target").length > 0) {
                    String[] targets = req.getParameterValues("target");
                    for (int i = 0; i < targets.length; i++) {
                        String[] targetParams = StringUtils.split(targets[i], ",");
                        Primer3Param param = primer3Manager.createParam(targetParams[0], sequence, Integer.parseInt(targetParams[1]), Integer.parseInt(targetParams[2]));
                        paramList.add(param);
                    }
                }
                primer3Manager.setPrimer3BinPath(getInitParameter("PRIMER3_BIN_PATH"));
                primer3Manager.setWorkDirPath(getInitParameter("WORK_DIR_PATH"));
                primer3Manager.setWholeSequence(seq);
                primer3Manager.setMarginLength(marginLength);
                outputList = primer3Manager.execParent(paramList);
            }
            try {
                res.setContentType("application/octet-stream");
                ObjectOutputStream oos = new ObjectOutputStream(res.getOutputStream());
                oos.writeObject(outputList);
                oos.flush();
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * 
	 * LineMode�f�[�^�̒񋟌�URL��擾����B<p>
	 * 
	 * @param serverName �T�[�o��
	 * @param paramMap �p�����[�^
	 * @return URL
	 */
    private String getSourceUrl(String serverName, String db, String hCheck, String hHead, String keyword, String selectedList, String url) {
        String sourceUrl = "http://" + serverName;
        if (url != null && url.length() > 0 && !url.equals("null")) {
            sourceUrl += url + "?";
        } else {
            if (db.indexOf(".text") > 0) db = db.substring(0, db.indexOf(".text"));
            sourceUrl += "/gps/service";
            sourceUrl += "?db=" + db + "&";
        }
        sourceUrl += "hCheck=" + hCheck + "&hHead=" + hHead;
        if (keyword != null && keyword.length() > 0) {
            sourceUrl += "&keyword=" + keyword;
        }
        if (selectedList != null && selectedList.length() > 0) {
            sourceUrl += "&selectedList=" + selectedList;
        }
        sourceUrl += "&forceNoHistogram=true";
        return sourceUrl;
    }

    private Primer3Manager setParameters(HttpServletRequest req) throws Exception {
        String message = "";
        if (req.getParameter("MARGIN_LENGTH") == null || req.getParameter("MARGIN_LENGTH").length() == 0) {
            message += "Please input 'Margin Size'.";
        } else if (req.getParameter("OVERLAP_LENGTH") == null || req.getParameter("OVERLAP_LENGTH").length() == 0) {
            message = "Please input 'Overlap Size'.";
        } else if (req.getParameter("MUST_XLATE_PRODUCT_MIN_SIZE") == null || req.getParameter("MUST_XLATE_PRODUCT_MIN_SIZE").length() == 0 || req.getParameter("PRIMER_PRODUCT_OPT_SIZE") == null || req.getParameter("PRIMER_PRODUCT_OPT_SIZE").length() == 0 || req.getParameter("MUST_XLATE_PRODUCT_MAX_SIZE") == null || req.getParameter("MUST_XLATE_PRODUCT_MAX_SIZE").length() == 0) {
            message += "Please input 'Product Size'.";
        } else if (req.getParameter("PRIMER_MAX_END_STABILITY") == null || req.getParameter("PRIMER_MAX_END_STABILITY").length() == 0) {
            message += "Please input 'Max 3' Stability'.";
        } else if (req.getParameter("PRIMER_MAX_MISPRIMING") == null || req.getParameter("PRIMER_MAX_MISPRIMING").length() == 0) {
            message += "Please input 'Max Mispriming'.";
        } else if (req.getParameter("PRIMER_PAIR_MAX_MISPRIMING") == null || req.getParameter("PRIMER_PAIR_MAX_MISPRIMING").length() == 0) {
            message += "Please input 'Pair Max Mispriming'.";
        } else if (req.getParameter("PRIMER_MIN_SIZE") == null || req.getParameter("PRIMER_MIN_SIZE").length() == 0 || req.getParameter("PRIMER_OPT_SIZE") == null || req.getParameter("PRIMER_OPT_SIZE").length() == 0 || req.getParameter("PRIMER_MAX_SIZE") == null || req.getParameter("PRIMER_MAX_SIZE").length() == 0) {
            message += "Please input 'Primer Size'.";
        } else if (req.getParameter("PRIMER_MIN_TM") == null || req.getParameter("PRIMER_MIN_TM").length() == 0 || req.getParameter("PRIMER_OPT_TM") == null || req.getParameter("PRIMER_OPT_TM").length() == 0 || req.getParameter("PRIMER_MAX_TM") == null || req.getParameter("PRIMER_MAX_TM").length() == 0 || req.getParameter("PRIMER_MAX_DIFF_TM") == null || req.getParameter("PRIMER_MAX_DIFF_TM").length() == 0) {
            message += "Please input 'Primer Tm'.";
        } else if (req.getParameter("PRIMER_MIN_GC") == null || req.getParameter("PRIMER_MIN_GC").length() == 0 || req.getParameter("PRIMER_OPT_GC_PERCENT") == null || req.getParameter("PRIMER_OPT_GC_PERCENT").length() == 0 || req.getParameter("PRIMER_MAX_GC") == null || req.getParameter("PRIMER_MAX_GC").length() == 0) {
            message += "Please input 'Primer GC%'.";
        } else if (req.getParameter("PRIMER_SELF_ANY") == null || req.getParameter("PRIMER_SELF_ANY").length() == 0) {
            message += "Please input 'Max Self Complementarity'.";
        } else if (req.getParameter("PRIMER_SELF_END") == null || req.getParameter("PRIMER_SELF_END").length() == 0) {
            message += "Please input 'Max 3' Self Complementarity'.";
        } else if (req.getParameter("PRIMER_NUM_NS_ACCEPTED") == null || req.getParameter("PRIMER_NUM_NS_ACCEPTED").length() == 0) {
            message += "Please input 'Max #N's'.";
        } else if (req.getParameter("PRIMER_MAX_POLY_X") == null || req.getParameter("PRIMER_MAX_POLY_X").length() == 0) {
            message += "Please input 'Max Poly-X'.";
        } else if (req.getParameter("PRIMER_GC_CLAMP") == null || req.getParameter("PRIMER_GC_CLAMP").length() == 0) {
            message += "Please input 'CG Clamp'.";
        }
        if (message.length() > 0) {
        }
        Primer3Manager primer3Manager = new Primer3Manager();
        try {
            primer3Manager.setOverlapLength(Integer.parseInt(req.getParameter("OVERLAP_LENGTH")));
        } catch (NumberFormatException e) {
            message += "'Margin Size' is invalid.";
        }
        try {
            primer3Manager.setPrimerProductMinSize(Integer.parseInt(req.getParameter("MUST_XLATE_PRODUCT_MIN_SIZE")));
            primer3Manager.setPrimerProductMaxSize(Integer.parseInt(req.getParameter("MUST_XLATE_PRODUCT_MAX_SIZE")));
            primer3Manager.setPrimerProductOptSize(Integer.parseInt(req.getParameter("PRIMER_PRODUCT_OPT_SIZE")));
            if (primer3Manager.getPrimerProductMinSize() > primer3Manager.getPrimerProductOptSize() || primer3Manager.getPrimerProductOptSize() > primer3Manager.getPrimerProductMaxSize()) {
                throw new Exception();
            }
        } catch (Exception e) {
            message += "'Product Size' is invalid.";
        }
        try {
            primer3Manager.setPrimerMaxEndStability(Double.parseDouble(req.getParameter("PRIMER_MAX_END_STABILITY")));
        } catch (NumberFormatException e) {
            message += "'Max 3' Stability' is invalid.";
        }
        try {
            primer3Manager.setPrimerMaxMispriming(Double.parseDouble(req.getParameter("PRIMER_MAX_MISPRIMING")));
        } catch (NumberFormatException e) {
            message += "'Max Mispriming' is invalid.";
        }
        try {
            primer3Manager.setPrimerPairMaxMispriming(Double.parseDouble(req.getParameter("PRIMER_PAIR_MAX_MISPRIMING")));
        } catch (NumberFormatException e) {
            message += "Pair Max Mispriming' is invalid.";
        }
        try {
            primer3Manager.setPrimerMinSize(Integer.parseInt(req.getParameter("PRIMER_MIN_SIZE")));
            primer3Manager.setPrimerOptSize(Integer.parseInt(req.getParameter("PRIMER_OPT_SIZE")));
            primer3Manager.setPrimerMaxSize(Integer.parseInt(req.getParameter("PRIMER_MAX_SIZE")));
            if (primer3Manager.getPrimerMinSize() > primer3Manager.getPrimerOptSize() || primer3Manager.getPrimerOptSize() > primer3Manager.getPrimerMaxSize()) {
                throw new Exception();
            }
        } catch (Exception e) {
            message += "'Primer Size' is invalid.";
        }
        try {
            primer3Manager.setPrimerMinTm(Double.parseDouble(req.getParameter("PRIMER_MIN_TM")));
            primer3Manager.setPrimerOptTm(Double.parseDouble(req.getParameter("PRIMER_OPT_TM")));
            primer3Manager.setPrimerMaxTm(Double.parseDouble(req.getParameter("PRIMER_MAX_TM")));
            primer3Manager.setPrimerMaxDiffTm(Double.parseDouble(req.getParameter("PRIMER_MAX_DIFF_TM")));
            if (primer3Manager.getPrimerMinTm() > primer3Manager.getPrimerOptTm() || primer3Manager.getPrimerOptTm() > primer3Manager.getPrimerMaxTm()) {
                throw new Exception();
            }
        } catch (Exception e) {
            message += "'Primer Tm' is invalid.";
        }
        try {
            primer3Manager.setPrimerMinGc(Double.parseDouble(req.getParameter("PRIMER_MIN_GC")));
            primer3Manager.setPrimerOptGcPercent(Double.parseDouble(req.getParameter("PRIMER_OPT_GC_PERCENT")));
            primer3Manager.setPrimerMaxGc(Double.parseDouble(req.getParameter("PRIMER_MAX_GC")));
            if (primer3Manager.getPrimerMinGc() > primer3Manager.getPrimerOptGcPercent() || primer3Manager.getPrimerOptGcPercent() > primer3Manager.getPrimerMaxGc()) {
                throw new Exception();
            }
        } catch (Exception e) {
            message += "'Primer GC%' is invalid.";
        }
        try {
            primer3Manager.setPrimerSelfAny(Double.parseDouble(req.getParameter("PRIMER_SELF_ANY")));
        } catch (NumberFormatException e) {
            message += "'Max Self Complementarity' is invalid.";
        }
        try {
            primer3Manager.setPrimerSelfEnd(Double.parseDouble(req.getParameter("PRIMER_SELF_END")));
        } catch (NumberFormatException e) {
            message += "'Max 3' Self Complementarity' is invalid.";
        }
        try {
            primer3Manager.setPrimerNumNsAccepted(Integer.parseInt(req.getParameter("PRIMER_NUM_NS_ACCEPTED")));
        } catch (NumberFormatException e) {
            message += "'Max #N's' is invalid.";
        }
        try {
            primer3Manager.setPrimerMaxPolyX(Integer.parseInt(req.getParameter("PRIMER_MAX_POLY_X")));
        } catch (NumberFormatException e) {
            message += "'Max Poly-X' is invalid.";
        }
        try {
            primer3Manager.setPrimerGcClamp(Integer.parseInt(req.getParameter("PRIMER_GC_CLAMP")));
        } catch (NumberFormatException e) {
            message += "'CG Clamp' is invalid.";
        }
        if (message.length() > 0) {
            throw new Exception(message);
        } else {
            req.setAttribute("OVERLAP_LENGTH", String.valueOf(primer3Manager.getOverlapLength()));
            req.setAttribute("MUST_XLATE_PRODUCT_MIN_SIZE", String.valueOf(primer3Manager.getPrimerProductMinSize()));
            req.setAttribute("PRIMER_PRODUCT_OPT_SIZE", String.valueOf(primer3Manager.getPrimerProductOptSize()));
            req.setAttribute("MUST_XLATE_PRODUCT_MAX_SIZE", String.valueOf(primer3Manager.getPrimerProductMaxSize()));
            req.setAttribute("PRIMER_MAX_END_STABILITY", String.valueOf(primer3Manager.getPrimerMaxEndStability()));
            req.setAttribute("PRIMER_MAX_MISPRIMING", String.valueOf(primer3Manager.getPrimerMaxMispriming()));
            req.setAttribute("PRIMER_PAIR_MAX_MISPRIMING", String.valueOf(primer3Manager.getPrimerPairMaxMispriming()));
            req.setAttribute("PRIMER_MIN_SIZE", String.valueOf(primer3Manager.getPrimerMinSize()));
            req.setAttribute("PRIMER_OPT_SIZE", String.valueOf(primer3Manager.getPrimerOptSize()));
            req.setAttribute("PRIMER_MAX_SIZE", String.valueOf(primer3Manager.getPrimerMaxSize()));
            req.setAttribute("PRIMER_MIN_TM", String.valueOf(primer3Manager.getPrimerMinTm()));
            req.setAttribute("PRIMER_OPT_TM", String.valueOf(primer3Manager.getPrimerOptTm()));
            req.setAttribute("PRIMER_MAX_TM", String.valueOf(primer3Manager.getPrimerMaxTm()));
            req.setAttribute("PRIMER_MAX_DIFF_TM", String.valueOf(primer3Manager.getPrimerMaxDiffTm()));
            req.setAttribute("PRIMER_MIN_GC", String.valueOf(primer3Manager.getPrimerMinGc()));
            req.setAttribute("PRIMER_OPT_GC_PERCENT", String.valueOf(primer3Manager.getPrimerOptGcPercent()));
            req.setAttribute("PRIMER_MAX_GC", String.valueOf(primer3Manager.getPrimerMaxGc()));
            req.setAttribute("PRIMER_SELF_ANY", String.valueOf(primer3Manager.getPrimerSelfAny()));
            req.setAttribute("PRIMER_SELF_END", String.valueOf(primer3Manager.getPrimerSelfEnd()));
            req.setAttribute("PRIMER_NUM_NS_ACCEPTED", String.valueOf(primer3Manager.getPrimerNumNsAccepted()));
            req.setAttribute("PRIMER_MAX_POLY_X", String.valueOf(primer3Manager.getPrimerMaxPolyX()));
            req.setAttribute("PRIMER_GC_CLAMP", String.valueOf(primer3Manager.getPrimerGcClamp()));
        }
        return primer3Manager;
    }
}
