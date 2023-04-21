package net.sourceforge.seqware.queryengine.webservice.prototypes;

import java.io.File;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import net.sourceforge.seqware.queryengine.backend.factory.MismatchFactory;
import net.sourceforge.seqware.queryengine.backend.model.Mismatch;
import net.sourceforge.seqware.queryengine.backend.store.berkeleydb.MismatchStore;
import net.sourceforge.seqware.queryengine.backend.util.SeqWareException;
import net.sourceforge.seqware.queryengine.backend.util.SeqWareIterator;
import net.sourceforge.seqware.queryengine.backend.util.SeqWareSettings;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author boconnor
 *
 */
public class MismatchResource extends ServerResource {

    @Get
    public Representation represent() {
        this.getLogger().log(Level.SEVERE, "TESTING");
        String id = (String) getRequestAttributes().get("mismatchId");
        StringBuffer output = new StringBuffer();
        ArrayList<String> contigs = new ArrayList<String>();
        Form form = this.getRequest().getResourceRef().getQueryAsForm();
        String format = form.getFirstValue("format");
        String trackName = form.getFirstValue("track.name");
        String trackOptions = form.getFirstValue("track.options");
        HashMap<String, String> tagsMap = new HashMap<String, String>();
        getLogger().log(Level.SEVERE, "Number of form fields: " + form.getNames().size());
        if (form.getNames().size() == 0 || "help".equals(format)) {
            output.append("<html><body>" + " <h2>Real-time Analysis</h2>" + " <h3>Variant Module Documentation</h3>" + " This URI requires parameters to render." + "  <h4>Required</h4>" + "   <ul>" + "       <li>format=[bed|form|help]: the output format</li>" + "         <ul>" + "           <li>bed:  returns variant data in BED format suitable for loading in the <a href=\"http://genome.ucsc.edu\">UCSC genome browser</a></li>" + "           <li>tags: returns a non-redundant list of tags associated with these variants suitable for loading in the <a href=\"http://genome.ucsc.edu\">UCSC table browser</a></li>" + "           <li>form: returns an HTML form for constructing a query on this resource</li>" + "           <li>help: prints this help documentation</li>" + "         </ul>" + "   </ul>" + "  <h4>Optional</h4>" + "   <ul>" + "       <li>filter.contig=[contig_name|all]: one or more contigs must be specified, multiple contigs are given as separate params</li>" + "       <li>filter.tag[.and[.not]|.or]=tagstring: tag to filter by, multiple tags are given as separate params, append .and, .and.not, or .or for logical combo, defaults to and</li>" + "       <li>filter.size=int-int: size range for indels, inclusive</li>" + "       <li>filter.minCoverage=int: minimum coverage at a given position, default is 0</li>" + "       <li>filter.maxCoverage=int: maximum coverage at a given position, default is " + Integer.MAX_VALUE + "</li>" + "       <li>filter.minPhred=int: minimum phred score for the mismatch call, default is 0</li>" + "       <li>filter.minObservations=int: minimum number of times a mismatch must be seen at a given position, default is 0</li>" + "       <li>filter.minObservationsPerStrand=int: minimum number of times a mismatch must be seen at a given position on each strand, default is 0</li>" + "       <li>filter.minPercent=int: minimum percentage of time a mismatch must be seen at a given position, default is 0</li>" + "       <li>filter.includeSNVs=[true|false]: indicates whether to include single nucleotide variants, default is true</li>" + "       <li>filter.includeIndels=[true|false]: indicates whether to include small insertions/deletions, default is true</li>" + "       <li>track.name=string: the name for the track</li>" + "       <li>track.options=string: put options (key/values) for tracks in UCSC browser here, name can be placed here as name=<name> or in track.name field</li>" + "   </ul>" + "  <h4>Example</h4>" + "  <pre>http://{hostname}:{port}/mismatches/{id}?format=bed&filter.contig=chr22&filter.contig=chr10&filter.minObservations=4&filter.tag=early-termination</pre>" + "  This example queries a given mismatch resource, specified by id, and returns the indels and snvs on chr22 and chr10 in bed format with a min observations of 4 and resulting in a stop codon." + "</body></html>");
            StringRepresentation repOutput = new StringRepresentation(output.toString());
            repOutput.setMediaType(MediaType.TEXT_HTML);
            return (repOutput);
        }
        String requestContigs = form.getValues("filter.contig");
        if (requestContigs != null) {
            for (String requestContig : requestContigs.split(",")) {
                contigs.add(requestContig);
            }
        } else {
            contigs.add("all");
            getLogger().log(Level.SEVERE, "Contig is null!");
        }
        DecimalFormat df = new DecimalFormat("##0.0");
        boolean lookupByTags = false;
        ArrayList<String> tags = new ArrayList<String>();
        ArrayList<String> andNotTags = new ArrayList<String>();
        String requestTags = form.getValues("filter.tag");
        String requestTagsAnd = form.getValues("filter.tag.and");
        String requestTagsAndNot = form.getValues("filter.tag.and.not");
        if (requestTags != null) {
            lookupByTags = true;
            for (String requestTag : requestTags.split(",")) {
                getLogger().log(Level.SEVERE, "TAG: " + requestTag);
                tags.add(requestTag);
            }
        }
        if (requestTagsAnd != null) {
            lookupByTags = true;
            for (String requestTag : requestTagsAnd.split(",")) {
                getLogger().log(Level.SEVERE, "AND TAG: " + requestTag);
                tags.add(requestTag);
            }
        }
        if (requestTagsAndNot != null) {
            lookupByTags = true;
            for (String requestTag : requestTagsAndNot.split(",")) {
                getLogger().log(Level.SEVERE, "AND NOT TAG: " + requestTag);
                andNotTags.add(requestTag);
            }
        }
        boolean lookupByOrTags = false;
        ArrayList<String> orTags = new ArrayList<String>();
        String requestTagsOr = form.getValues("filter.tag.or");
        if (requestTagsOr != null) {
            lookupByOrTags = true;
            for (String requestTag : requestTagsOr.split(",")) {
                getLogger().log(Level.SEVERE, "OR TAG: " + requestTag);
                orTags.add(requestTag);
            }
        }
        int minCoverage = 1;
        String requestMinCov = form.getFirstValue("filter.minCoverage");
        if (requestMinCov != null) {
            minCoverage = Integer.parseInt(requestMinCov);
        }
        int maxCoverage = Integer.MAX_VALUE;
        String requestMaxCov = form.getFirstValue("filter.maxCoverage");
        if (requestMaxCov != null) {
            maxCoverage = Integer.parseInt(requestMaxCov);
        }
        String requestMinPhred = form.getFirstValue("filter.minPhred");
        int minPhred = 0;
        if (requestMinPhred != null) {
            minPhred = Integer.parseInt(requestMinPhred);
        }
        boolean includeSNVs = true;
        String requestIncludeSNVs = form.getFirstValue("filter.includeSNVs");
        if (requestIncludeSNVs != null) {
            includeSNVs = "true".equals(requestIncludeSNVs);
        }
        boolean includeIndels = true;
        String requestIncludeIndels = form.getFirstValue("filter.includeIndels");
        if (requestIncludeIndels != null) {
            includeIndels = "true".equals(requestIncludeIndels);
        }
        int minObservations = 0;
        String requestMinObs = form.getFirstValue("filter.minObservations");
        if (requestMinObs != null) {
            minObservations = Integer.parseInt(requestMinObs);
        }
        int sizeMin = -1;
        int sizeMax = -1;
        String sizeRange = form.getFirstValue("filter.size");
        if (sizeRange != null && sizeRange.matches("\\d+-\\d+")) {
            Pattern pat = Pattern.compile("(\\d+)-(\\d+)");
            Matcher m = pat.matcher(sizeRange);
            if (m.find()) {
                sizeMin = Integer.parseInt(m.group(1));
                sizeMax = Integer.parseInt(m.group(2));
            }
        }
        int minObservationsPerStrand = 0;
        String requestMinObsPerStrand = form.getFirstValue("filter.minObservationsPerStrand");
        if (requestMinObsPerStrand != null) {
            minObservationsPerStrand = Integer.parseInt(requestMinObsPerStrand);
        }
        int minPercent = 0;
        String requestMinPercent = form.getFirstValue("filter.minPercent");
        if (requestMinPercent != null) {
            minPercent = Integer.parseInt(requestMinPercent);
        }
        if ("form".equals(format)) {
            Configuration cfg = new Configuration();
            try {
                cfg.setDirectoryForTemplateLoading(new File("templates"));
                cfg.setObjectWrapper(new DefaultObjectWrapper());
                Map root = new HashMap();
                root.put("url", "");
                Template temp = cfg.getTemplate("mismatch_query.ftl");
                StringWriter sw = new StringWriter();
                temp.process(root, sw);
                StringRepresentation repOutput = new StringRepresentation(sw.toString());
                repOutput.setMediaType(MediaType.TEXT_HTML);
                return (repOutput);
            } catch (Exception e) {
                e.printStackTrace();
                StringRepresentation repOutput = new StringRepresentation(e.getMessage());
                repOutput.setMediaType(MediaType.TEXT_PLAIN);
                return (repOutput);
            }
        } else if ("bed".equals(format) || "tags".equals(format)) {
            MismatchFactory factory = new MismatchFactory();
            MismatchStore store = null;
            try {
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException cnfe) {
                    System.out.println("Couldn't find the driver!");
                    System.out.println("Let's print a stack trace, and exit.");
                    cnfe.printStackTrace();
                    System.exit(-1);
                }
                System.out.println("Registered the driver ok, so let's make a connection.");
                Connection c = null;
                try {
                    String server = System.getProperty("dbserver");
                    String user = System.getProperty("user");
                    String pass = System.getProperty("pass");
                    c = DriverManager.getConnection("jdbc:postgresql://" + server + "/seqware_meta_db", user, pass);
                } catch (SQLException se) {
                    System.out.println("Couldn't connect: print out a stack trace and exit.");
                    se.printStackTrace();
                    System.exit(1);
                }
                Statement s = null;
                try {
                    s = c.createStatement();
                } catch (SQLException se) {
                    System.out.println("We got an exception while creating a statement:" + "that probably means we're no longer connected.");
                    se.printStackTrace();
                    System.exit(1);
                }
                ResultSet rs = null;
                try {
                    rs = s.executeQuery("SELECT file_path, description, sw_accession, parameters, create_tstmp FROM processing WHERE meta_type = 'application/seqware-qe-vdb' AND sw_accession = " + id);
                } catch (SQLException se) {
                    System.out.println("We got an exception while executing our query:" + "that probably means our SQL is invalid");
                    se.printStackTrace();
                    System.exit(1);
                }
                long cache = 4294967296L;
                String filePath = null;
                long swid = 0L;
                try {
                    if (rs.next()) {
                        filePath = rs.getString(1);
                        swid = rs.getLong(3);
                        String params = rs.getString(4);
                        String[] paramsArray = params.split(",");
                        for (String param : paramsArray) {
                            String[] kv = param.split("=");
                            if ("cache_size".equals(kv[0])) {
                                cache = Long.parseLong(kv[1]);
                            }
                        }
                    }
                } catch (SQLException se) {
                    System.out.println("We got an exception while getting a result:this " + "shouldn't happen: we've done something really bad.");
                    se.printStackTrace();
                }
                try {
                    rs.close();
                    s.close();
                    c.close();
                } catch (SQLException se) {
                    System.out.println("We got an exception while trying to close connections ");
                    se.printStackTrace();
                }
                SeqWareSettings settings = new SeqWareSettings();
                settings.setStoreType("berkeleydb-mismatch-store");
                settings.setFilePath(filePath);
                settings.setCacheSize(cache);
                settings.setCreateMismatchDB(false);
                settings.setCreateConsequenceAnnotationDB(false);
                settings.setCreateDbSNPAnnotationDB(false);
                settings.setReadOnly(true);
                store = factory.getStore(settings);
                if (store == null) {
                    throw new Exception("Store is null");
                }
                if (store != null) {
                    Iterator<String> it = contigs.iterator();
                    while (it.hasNext()) {
                        String contig = (String) it.next();
                        getLogger().log(Level.SEVERE, "Processing Contig: " + contig);
                        SeqWareIterator matchIt = null;
                        if ("all".equals(contig)) {
                            if (lookupByTags && tags.size() > 0) {
                                matchIt = store.getMismatchesByTag(tags.get(0));
                            } else {
                                matchIt = store.getMismatches();
                            }
                        } else if (contig.matches("(\\S+):(\\d+)-(\\d+)")) {
                            String[] t = contig.split("[-:]");
                            contig = t[0];
                            matchIt = store.getMismatches(t[0], Integer.parseInt(t[1]), Integer.parseInt(t[2]));
                        } else {
                            matchIt = store.getMismatches(contig);
                        }
                        while (matchIt.hasNext()) {
                            Mismatch m = (Mismatch) matchIt.next();
                            if (m != null && m.getReadCount() >= minCoverage && m.getReadCount() <= maxCoverage && m.getConsensusCallQuality() >= minPhred) {
                                boolean passesSizeFilter = true;
                                if (sizeMin > -1 && sizeMax > -1 && (m.getType() == m.INSERTION || m.getType() == m.DELETION)) {
                                    if (m.getCalledBase().length() < sizeMin || m.getCalledBase().length() > sizeMax) {
                                        passesSizeFilter = false;
                                    }
                                }
                                boolean passesTagFilter = true;
                                int seen = 0;
                                HashMap<String, String> mTags = null;
                                if (tags.size() > 0) {
                                    if (mTags == null) {
                                        mTags = m.getTags();
                                    }
                                    for (String tag : tags) {
                                        if (mTags.containsKey(tag)) {
                                            seen++;
                                        }
                                    }
                                    if (seen == tags.size()) {
                                        passesTagFilter = true;
                                    } else {
                                        passesTagFilter = false;
                                    }
                                }
                                if (andNotTags.size() > 0) {
                                    seen = 0;
                                    if (mTags == null) {
                                        mTags = m.getTags();
                                    }
                                    for (String tag : andNotTags) {
                                        if (mTags.containsKey(tag)) {
                                            seen++;
                                        }
                                    }
                                    if (seen == 0 && passesTagFilter != false) {
                                        passesTagFilter = true;
                                    } else {
                                        passesTagFilter = false;
                                    }
                                }
                                if (orTags.size() > 0) {
                                    seen = 0;
                                    if (mTags == null) {
                                        mTags = m.getTags();
                                    }
                                    for (String tag : orTags) {
                                        if (mTags.containsKey(tag) && passesTagFilter != false) {
                                            passesTagFilter = true;
                                            seen = 1;
                                            break;
                                        }
                                    }
                                    if (seen == 0) {
                                        passesTagFilter = false;
                                    }
                                }
                                if (passesSizeFilter && passesTagFilter && m.getType() == Mismatch.SNV && includeSNVs && ("all".equals(contig) || m.getContig().equals(contig))) {
                                    double calledPercent = ((double) m.getCalledBaseCount() / (double) m.getReadCount()) * (double) 100.0;
                                    double calledFwdPercent = ((double) m.getCalledBaseCountForward() / (double) m.getReadCount()) * (double) 100.0;
                                    double calledRvsPercent = ((double) m.getCalledBaseCountReverse() / (double) m.getReadCount()) * (double) 100.0;
                                    String color = "80,175,175";
                                    String callStr = "heterozygous";
                                    if (m.getZygosity() == m.HOMOZYGOUS) {
                                        color = "0,50,180";
                                        callStr = "homozygous";
                                    }
                                    if (m.getCalledBaseCount() >= minObservations && m.getCalledBaseCountForward() >= minObservationsPerStrand && m.getCalledBaseCountReverse() >= minObservationsPerStrand && calledPercent >= minPercent) {
                                        int testTotal = m.getCalledBaseCountForward() + m.getCalledBaseCountReverse();
                                        output.append(m.getContig() + "\t" + m.getStartPosition() + "\t" + m.getStopPosition() + "\t" + m.getReferenceBase() + "->" + m.getCalledBase() + "(" + m.getReadCount() + ":" + m.getCalledBaseCount() + ":" + df.format(calledPercent) + "%[F:" + m.getCalledBaseCountForward() + ":" + df.format(calledFwdPercent) + "%|R:" + m.getCalledBaseCountReverse() + ":" + df.format(calledRvsPercent) + "%]" + "call=" + callStr + ":genome_phred=" + m.getReferenceCallQuality() + ":snp_phred=" + m.getConsensusCallQuality() + ":max_mapping_qual=" + m.getMaximumMappingQuality() + ":genome_max_qual=" + m.getReferenceMaxSeqQuality() + ":genome_ave_qual=" + df.format(m.getReferenceAveSeqQuality()) + ":snp_max_qual=" + m.getConsensusMaxSeqQuality() + ":snp_ave_qual=" + df.format(m.getConsensusAveSeqQuality()) + ":mismatch_id=" + m.getId());
                                        Iterator<String> tagIt = m.getTags().keySet().iterator();
                                        while (tagIt.hasNext()) {
                                            String tag = tagIt.next();
                                            String value = m.getTagValue(tag);
                                            output.append(":" + tag);
                                            if (value != null) {
                                                output.append("=" + value);
                                            }
                                            tagsMap.put(tag, value);
                                        }
                                        int blockSize = m.getStopPosition() - m.getStartPosition();
                                        output.append(")\t" + m.getConsensusCallQuality() + "\t+\t" + m.getStartPosition() + "\t" + m.getStopPosition() + "\t" + color + "\t1\t" + blockSize + "\t0\n");
                                    }
                                } else if (passesSizeFilter && passesTagFilter && (m.getType() == Mismatch.INSERTION || m.getType() == Mismatch.DELETION) && includeIndels && ("all".equals(contig) || m.getContig().equals(contig))) {
                                    double calledPercent = ((double) m.getCalledBaseCount() / (double) m.getReadCount()) * (double) 100.0;
                                    double calledFwdPercent = ((double) m.getCalledBaseCountForward() / (double) m.getReadCount()) * (double) 100.0;
                                    double calledRvsPercent = ((double) m.getCalledBaseCountReverse() / (double) m.getReadCount()) * (double) 100.0;
                                    String color = "80,175,175";
                                    String callStr = "heterozygous";
                                    if (m.getZygosity() == Mismatch.HOMOZYGOUS) {
                                        color = "0,50,180";
                                        callStr = "homozygous";
                                    }
                                    if (m.getCalledBaseCount() >= minObservations && m.getCalledBaseCountForward() >= minObservationsPerStrand && m.getCalledBaseCountReverse() >= minObservationsPerStrand && calledPercent >= minPercent) {
                                        String bedString = null;
                                        StringBuffer lengthString = new StringBuffer();
                                        int blockSize = 1;
                                        for (int i = 0; i < m.getCalledBase().length(); i++) {
                                            lengthString.append("-");
                                        }
                                        if (m.getType() == Mismatch.INSERTION) {
                                            bedString = "INS:" + lengthString + "->" + m.getCalledBase();
                                        } else if (m.getType() == Mismatch.DELETION) {
                                            bedString = "DEL:" + m.getCalledBase() + "->" + lengthString;
                                            blockSize = lengthString.length();
                                        } else {
                                            throw new Exception("What is type: " + m.getType());
                                        }
                                        int testTotal = m.getCalledBaseCountForward() + m.getCalledBaseCountReverse();
                                        if (testTotal != m.getCalledBaseCount()) {
                                            throw new Exception("Forward and reverse don't add to total\n");
                                        }
                                        if (m.getType() == Mismatch.INSERTION && m.getStartPosition() != m.getStopPosition() - 1) {
                                            int bugStop = m.getStartPosition() + 1;
                                            output.append(m.getContig() + "\t" + m.getStartPosition() + "\t" + bugStop + "\t" + bedString + "(" + m.getReadCount() + ":" + m.getCalledBaseCount() + ":" + df.format(calledPercent) + "%[F:" + m.getCalledBaseCountForward() + ":" + df.format(calledFwdPercent) + "%|R:" + m.getCalledBaseCountReverse() + ":" + df.format(calledRvsPercent) + "%]" + "call=" + callStr + ":genome_phred=" + m.getReferenceCallQuality() + ":snp_phred=" + m.getConsensusCallQuality() + ":max_mapping_qual=" + m.getMaximumMappingQuality() + ":mismatch_id=" + m.getId());
                                        } else {
                                            output.append(m.getContig() + "\t" + m.getStartPosition() + "\t" + m.getStopPosition() + "\t" + bedString + "(" + m.getReadCount() + ":" + m.getCalledBaseCount() + ":" + df.format(calledPercent) + "%[F:" + m.getCalledBaseCountForward() + ":" + df.format(calledFwdPercent) + "%|R:" + m.getCalledBaseCountReverse() + ":" + df.format(calledRvsPercent) + "%]" + "call=" + callStr + ":genome_phred=" + m.getReferenceCallQuality() + ":snp_phred=" + m.getConsensusCallQuality() + ":max_mapping_qual=" + m.getMaximumMappingQuality() + ":mismatch_id=" + m.getId());
                                        }
                                        Iterator<String> tagIt = m.getTags().keySet().iterator();
                                        while (tagIt.hasNext()) {
                                            String tag = tagIt.next();
                                            String value = m.getTagValue(tag);
                                            output.append(":" + tag);
                                            if (value != null) {
                                                output.append("=" + value);
                                            }
                                            tagsMap.put(tag, value);
                                        }
                                        if (m.getType() == Mismatch.INSERTION && m.getStartPosition() != m.getStopPosition() - 1) {
                                            int bugStop = m.getStartPosition() + 1;
                                            output.append(")\t" + m.getConsensusCallQuality() + "\t+\t" + m.getStartPosition() + "\t" + bugStop + "\t" + color + "\t1\t" + blockSize + "\t0\n");
                                        } else {
                                            output.append(")\t" + m.getConsensusCallQuality() + "\t+\t" + m.getStartPosition() + "\t" + m.getStopPosition() + "\t" + color + "\t1\t" + blockSize + "\t0\n");
                                        }
                                    }
                                }
                            }
                        }
                        matchIt.close();
                    }
                    store.close();
                }
            } catch (SeqWareException e) {
                e.printStackTrace();
                StringRepresentation repOutput = new StringRepresentation(e.getMessage());
                repOutput.setMediaType(MediaType.TEXT_PLAIN);
                return (repOutput);
            } catch (Exception e) {
                e.printStackTrace();
                StringRepresentation repOutput = new StringRepresentation(e.getMessage());
                repOutput.setMediaType(MediaType.TEXT_PLAIN);
                return (repOutput);
            }
            if (output.length() > 0 && "bed".equals(format)) {
                if (trackName == null) {
                    trackName = "SeqWare BED " + (new Date()).toString();
                }
                if (trackOptions == null) {
                    trackOptions = "";
                }
                StringRepresentation repOutput = new StringRepresentation("track name=\"" + trackName + "\" " + trackOptions + "\n" + output.toString());
                repOutput.setMediaType(MediaType.TEXT_PLAIN);
                return (repOutput);
            } else if ("tags".equals(format)) {
                output = new StringBuffer();
                for (String key : tagsMap.keySet()) {
                    output.append(key + "\n");
                }
                StringRepresentation repOutput = new StringRepresentation(output.toString());
                repOutput.setMediaType(MediaType.TEXT_PLAIN);
                return (repOutput);
            }
        }
        StringRepresentation repOutput = new StringRepresentation("# No Results!");
        repOutput.setMediaType(MediaType.TEXT_PLAIN);
        return (repOutput);
    }
}
