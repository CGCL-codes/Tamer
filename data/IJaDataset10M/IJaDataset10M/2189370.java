package org.okkam.core.match;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.Vector;
import java.util.Properties;
import javax.xml.bind.JAXBException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.okkam.core.match.query.*;
import org.okkam.core.match.query.dao.IOkkamDAO;
import org.okkam.core.match.query.dao.OkkamJDBCImpl;
import org.okkam.core.match.query.exceptions.NoQuerySpecifiedException;
import org.okkam.core.match.query.facets.FacetRegistry;
import org.okkam.core.match.ranking.RankableEntityURI;
import org.okkam.core.match.ranking.StringSimilarityRank;
import org.okkam.core.match.ranking.IEntityRank;

/**
 * The controller that runs all the steps of the matching pipeline. Implemented by hand, 
 * an approach via a workflow management architecture would have been possible but 
 * seems exaggerated.
 * 
 * @author stoermer
 * <p>
 * The steps required to run MatchingPipelineController are the following
 * <nl>
 * <li> retrieve the XML representation of the user query from somewhere
 * <li> load configuration from @UtilFactory
 * <li> create a new instance of this class,
 * <li> call {@link setAnnotatedQueryString(String)} to provide the user query data
 * <li> {@link run()} the pipeline, and retrieve the number of hits
 * <li> {@link getURIs()} and do what you need with them.
 * </nl>
 * <p>
 * You can find example code from {@link org.okkam.core.match.test.PipelineTest}, but it's not more than this:
 * <p>

<!-- ======================================================== -->
<!-- = Java Sourcecode to HTML automatically converted code = -->
<!-- =   Java2Html Converter 5.0 [2006-02-26] by Markus Gebhard  markus@jave.de   = -->
<!-- =     Further information: http://www.java2html.de     = -->
<div align="left" class="java">
<table border="0" cellpadding="3" cellspacing="0" bgcolor="#ffffff">
   <tr>
  <!-- start source code -->
   <td nowrap="nowrap" valign="top" align="left">
    <code>
<font color="#ffffff">&nbsp;&nbsp;&nbsp;&nbsp;</font><font color="#000000">MatchingPipelineController&nbsp;c&nbsp;=&nbsp;</font><font color="#7f0055"><b>new&nbsp;</b></font><font color="#000000">MatchingPipelineController</font><font color="#000000">()</font><font color="#000000">;</font><br />
<font color="#ffffff">&nbsp;&nbsp;&nbsp;&nbsp;</font><font color="#000000">c.setAnnotatedQueryString</font><font color="#000000">(</font><font color="#000000">annotatedQueryString</font><font color="#000000">)</font><font color="#000000">;</font><br />
<font color="#ffffff">&nbsp;&nbsp;&nbsp;&nbsp;</font><font color="#7f0055"><b>int&nbsp;</b></font><font color="#000000">count&nbsp;=&nbsp;c.run</font><font color="#000000">()</font><font color="#000000">;</font><br />
<font color="#ffffff">&nbsp;&nbsp;&nbsp;&nbsp;</font><font color="#000000">System.out.println</font><font color="#000000">(</font><font color="#2a00ff">&#34;Number&nbsp;of&nbsp;hits:&nbsp;&#34;&nbsp;</font><font color="#000000">+&nbsp;&nbsp;count</font><font color="#000000">)</font><font color="#000000">;</font><br />
<font color="#ffffff">&nbsp;&nbsp;&nbsp;&nbsp;</font><br />
<font color="#ffffff"></font><br />
<font color="#ffffff">&nbsp;&nbsp;&nbsp;&nbsp;</font><font color="#7f0055"><b>if</b></font><font color="#000000">(</font><font color="#000000">count&nbsp;&gt;&nbsp;</font><font color="#990000">0</font><font color="#000000">)</font><br />
<font color="#ffffff">&nbsp;&nbsp;&nbsp;&nbsp;</font><font color="#000000">{</font><br />
<font color="#ffffff">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font><font color="#000000">List&nbsp;&lt;String&gt;&nbsp;uris&nbsp;=&nbsp;c.getURIs</font><font color="#000000">()</font><font color="#000000">;</font><br />
<font color="#ffffff">&nbsp;&nbsp;&nbsp;&nbsp;</font><font color="#000000">}</font></code>
    
   </td>
  <!-- end source code -->
   </tr>
</table>
</div>

 */
public class MatchingPipelineController {

    private String annotatedQueryString;

    private Query query = null;

    private Properties config;

    private IOkkamDAO dao;

    private List<String> entityURIs;

    private List<Float> confidences;

    private static final Log log = LogFactory.getLog(MatchingPipelineController.class);

    /**
	 * Constructor. Creates a  matching pipeline.
	 * @param config Properties containing the configuration.
	 */
    public MatchingPipelineController() {
        this.config = UtilFactory.getProperties();
        entityURIs = new Vector<String>();
    }

    /**
	 * returns the string representation of the XML-encoded Annotated Query
	 * @return
	 */
    public String getAnnotatedQueryString() {
        return annotatedQueryString;
    }

    /**
	 * set the string representation of the XML-encoded Annotated Query
	 * @param annotatedQueryString the XML representation of the query, conforming to the respective XML Schema.
	 * @throws NoQuerySpecifiedException 
	 * @throws JAXBException 
	 */
    public void setAnnotatedQueryString(String annotatedQueryString) throws NoQuerySpecifiedException, JAXBException {
        this.annotatedQueryString = annotatedQueryString;
        if (annotatedQueryString == null) {
            throw new NoQuerySpecifiedException("Input query string below minimum length");
        }
        entityURIs = new Vector<String>();
        query = new Query(annotatedQueryString);
    }

    /**
	 * Runs the matching pipeline.
	 * 
	 * @throws NoQuerySpecifiedException
	 * @return Number of hits found in the repository.
	 */
    public int run() throws NoQuerySpecifiedException, JAXBException, SQLException {
        log.debug("->run().");
        if (query == null) {
            log.error("now query specified. throwing exception.");
            throw new NoQuerySpecifiedException("No query was specified, maybe setAnnotatedQueryString() was not called beforehand.");
        }
        dao = new OkkamJDBCImpl();
        dao.init(query.getConditions());
        log.debug("estimating result count.");
        int count = dao.getResultCount();
        if (count < Integer.parseInt(config.getProperty("min_hits"))) {
            log.debug("less than minimum desired results. expanding.");
            query.expand();
            dao.init(query.getConditions());
            count = dao.getResultCount();
        }
        if (count < Integer.parseInt(config.getProperty("min_hits"))) {
            log.debug("less than minimum desired results, after expansion. giving up.");
            return 0;
        }
        Vector<String> unrankedUris = dao.getEntityUris();
        log.debug("Ranking.");
        IEntityRank er = new StringSimilarityRank();
        List<RankableEntityURI> rankedEntities = er.rank(query.getAnnotatedQuery(), unrankedUris);
        entityURIs = new Vector<String>();
        confidences = new Vector<Float>();
        int addcount = 0;
        for (Iterator<RankableEntityURI> i = rankedEntities.iterator(); i.hasNext() && addcount < query.getLimit(); ++addcount) {
            RankableEntityURI uri = i.next();
            confidences.add(uri.getSim());
            log.debug(uri.toString());
            entityURIs.add(uri.getOkkamuri());
        }
        return entityURIs.size();
    }

    /**
	 * Returns the list of entity URIs that were retrieved during the run() of the 
	 * pipeline.
	 * @return List of String containing entity URIs, empty list otherwise.
	 */
    public List<String> getURIs() {
        return entityURIs;
    }

    /**
	 * Returns the list of confidences corresponding to the list of URIs returned
	 * from {@link getURIs()}
	 * @return list of float containing the confidences.
	 */
    public List<Float> getConfidences() {
        return confidences;
    }
}
