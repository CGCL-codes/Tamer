package uk.ac.ebi.rhea.util.apps.exp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.mapper.IRheaReader;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.rhea.mapper.db.RheaCompoundDbReader;
import uk.ac.ebi.rhea.mapper.db.RheaDbReader;
import uk.ac.ebi.rhea.mapper.util.IChebiHelper;
import uk.ac.ebi.rhea.mapper.util.db.ChebiDbHelper;
import uk.ac.ebi.rhea.stats.IRheaStatistics;
import uk.ac.ebi.rhea.stats.IRheaStatistics.Target;
import uk.ac.ebi.rhea.stats.db.RheaDbStatistics;
import uk.ac.ebi.rhea.util.BiopaxWriter;
import uk.ac.ebi.rhea.util.CTFileWriter;
import uk.ac.ebi.rhea.util.RheaWriter;

/**
 * Utility application to export Rhea in a number of formats.
 * @author rafalcan
 *
 */
public class RheaExporter {

    protected static final Logger LOGGER = Logger.getLogger(RheaExporter.class);

    protected static enum Format {

        biopax, rxn, rdSingle, rdAll, sdf, sitemap, ebinocle, keggCompound, keggReaction
    }

    private Connection rheaCon, chebiCon;

    protected RheaCompoundDbReader rheaCompoundReader;

    protected IChebiHelper chebiHelper;

    private IRheaReader rheaReader;

    private IRheaStatistics stats;

    public RheaExporter(String rheaDbConfig, String chebiDbConfig) throws Exception {
        rheaCon = OracleDatabaseInstance.getInstance(rheaDbConfig).getConnection();
        chebiCon = OracleDatabaseInstance.getInstance(chebiDbConfig).getConnection();
        rheaCompoundReader = new RheaCompoundDbReader(rheaCon);
        rheaReader = new RheaDbReader(rheaCompoundReader);
        chebiHelper = new ChebiDbHelper(chebiCon);
        stats = new RheaDbStatistics(rheaCon, Target.PUBLIC);
    }

    public Connection getChebiCon() {
        return chebiCon;
    }

    public void setChebiCon(Connection chebiCon) {
        this.chebiCon = chebiCon;
    }

    public Connection getRheaCon() {
        return rheaCon;
    }

    public void setRheaCon(Connection rheaCon) {
        this.rheaCon = rheaCon;
    }

    /**
	 * Exports the public reactions in Rhea.
	 * @param args
	 * <ul>
	 * 	<li>-rheaDb &lt;config&gt;: database configuration file for Rhea
	 * 		(see constructor {@link #RheaExporter(String, String)})</li>
	 * 	<li>-chebiDb &lt;config&gt;: database configuration file for ChEBI
	 * 		(see constructor {@link #RheaExporter(String, String)}</li>
	 * 	<li>[-biopax &lt;file name&gt;]: export as one
	 * 		<a href="http://www.biopax.org">BioPAX</a> OWL file</li>
	 * 	<li>[-rxn]: export as RXN files (one per reaction)</li>
	 * 	<li>[-rdSingle]: export as RD files (one per reaction)</li>
	 * 	<li>[-rdAll]: export as one RD file</li>
	 * 	<li>[-o]: output directory for CT files (RXN, RD)</li>
	 * 	<li>[-sdf &lt;file name&gt;]: export as one SDF file with all of the
	 * 		compounds used by Rhea.</li>
	 * 	<li>[-keggCompound &lt;file name&gt;]: export as one KEGG compound file
     *      with all of the compounds used by Rhea.</li>
	 * 	<li>[-keggReaction &lt;file name&gt;]: export as one KEGG reaction file.
     *      </li>
	 * 	<li>[-sitemap &lt;file name&gt;]: export as
	 * 		<a href="http://www.sitemaps.org">Sitemap</a> XML file</li>
	 * 	<li>[-ebinocle &lt;file name&gt;]: export as
	 * 		<a href="http://www.ebi.ac.uk/es/documentation/doku.php?id=projects:ebinocle:index">EB-Eye</a>
	 * 		XML file</li>
	 * 	<li>[-reactionId &lt;id&gt;]: ID of the reaction to be exported.
	 * 		If not specified, all of the public reactions in Rhea are exported.</li>
	 * </ul>
	 * @throws Exception
	 */
    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption(OptionBuilder.isRequired().hasArg().withArgName("config").withDescription("Rhea database configuration").create("rheaDb"));
        options.addOption(OptionBuilder.isRequired().hasArg().withArgName("config").withDescription("ChEBI database configuration").create("chebiDb"));
        options.addOption(OptionBuilder.hasArg().withArgName("file name").withDescription("[optional] Export Rhea as one BioPAX OWL file").create(Format.biopax.name()));
        options.addOption(OptionBuilder.withDescription("[optional] Export as RXN files (one per reaction)").create(Format.rxn.name()));
        options.addOption(OptionBuilder.withDescription("[optional] Export as RD files (one per reaction)").create(Format.rdSingle.name()));
        options.addOption(OptionBuilder.withDescription("[optional] Export as one RD file").create(Format.rdAll.name()));
        options.addOption(OptionBuilder.hasArg().withArgName("file name").withDescription("[optional] Export as one SDF file").create(Format.sdf.name()));
        options.addOption(OptionBuilder.hasArg().withArgName("file name").withDescription("[optional] Export as one KEGG compound file").create(Format.keggCompound.name()));
        options.addOption(OptionBuilder.hasArg().withArgName("file name").withDescription("[optional] Export as one KEGG reaction file").create(Format.keggReaction.name()));
        options.addOption(OptionBuilder.hasArg().withArgName("dir name").withDescription("[required with -rxn, -rdSingle or -rdAll] output directory").create('o'));
        options.addOption(OptionBuilder.hasArg().withArgName("file name").withDescription("[optional] Export as Sitemap XML file").create(Format.sitemap.name()));
        options.addOption(OptionBuilder.hasArg().withArgName("file name").withDescription("[optional] Export as Ebinocle (EB-Eye) XML file").create(Format.ebinocle.name()));
        options.addOption(OptionBuilder.hasArg().withArgName("reaction ID").withDescription("[optional] ID of the reaction to be exported" + " (all if not specified)").create("reactionId"));
        CommandLine cl = null;
        try {
            cl = new GnuParser().parse(options, args);
        } catch (ParseException e) {
            new HelpFormatter().printHelp(RheaExporter.class.getName(), options);
            return;
        }
        if (!cl.hasOption('o') && (cl.hasOption(Format.rxn.name()) || cl.hasOption(Format.rdSingle.name()) || cl.hasOption(Format.rdAll.name()))) {
            new HelpFormatter().printHelp(RheaExporter.class.getName(), options);
            return;
        }
        RheaExporter exporter = null;
        try {
            String rheaDbConfig = cl.getOptionValue("rheaDb");
            String chebiDbConfig = cl.getOptionValue("chebiDb");
            exporter = new RheaExporter(rheaDbConfig, chebiDbConfig);
            Properties rheaProps = new Properties();
            rheaProps.setProperty("rhea.release.number", String.valueOf(exporter.stats.getReleaseNumber()));
            rheaProps.setProperty("rhea.release.date", new SimpleDateFormat("yyyy-MM-dd").format(exporter.stats.getReleaseDate()));
            final Collection<Reaction> exportedReactions = cl.hasOption("reactionId") ? exporter.getOneReaction(cl.getOptionValue("reactionId")) : exporter.getAllPublicReactions();
            final Collection<Compound> allCompounds;
            if (cl.hasOption(Format.sdf.name()) || cl.hasOption(Format.keggCompound.name())) {
                allCompounds = exporter.rheaCompoundReader.findAll();
            } else {
                allCompounds = null;
            }
            if (cl.hasOption(Format.biopax.name())) {
                String biopaxFile = cl.getOptionValue(Format.biopax.name());
                if (biopaxFile == null) {
                    LOGGER.error("Missing parameter: BioPAX OWL output file");
                } else try {
                    RheaWriter biopaxWriter = new BiopaxWriter();
                    LOGGER.info("Writing BioPAX file...");
                    biopaxWriter.write(exportedReactions, biopaxFile, rheaProps);
                    LOGGER.info("Written!");
                } catch (Exception e) {
                    LOGGER.error("Unable to write OWL file", e);
                }
            }
            if (cl.hasOption(Format.rxn.name()) || cl.hasOption(Format.rdSingle.name()) || cl.hasOption(Format.rdAll.name())) {
                String ctFilesOutDir = cl.getOptionValue('o');
                if (ctFilesOutDir == null) {
                    LOGGER.error("Missing parameter: CT files output directory");
                } else try {
                    exporter.checkWriteableDirectory(ctFilesOutDir);
                    RheaWriter ctWriter = new CTFileWriter(exporter.chebiHelper, cl.hasOption(Format.rxn.name()), cl.hasOption(Format.rdSingle.name()), cl.hasOption(Format.rdAll.name()));
                    LOGGER.info("Writing CT files...");
                    ctWriter.write(exportedReactions, ctFilesOutDir, rheaProps);
                    LOGGER.info("Written!");
                } catch (Exception e) {
                    LOGGER.error("Unable to write CT files", e);
                }
            }
            if (cl.hasOption(Format.sdf.name())) {
                String sdfFile = cl.getOptionValue(Format.sdf.name());
                if (sdfFile == null) {
                    LOGGER.error("Missing parameter: SDF output file");
                } else try {
                    File sdf = new File(sdfFile);
                    SDFWriter sdfWriter = new SDFWriter(exporter.chebiHelper);
                    LOGGER.info("Writing sitemap file...");
                    sdfWriter.write(allCompounds, sdf);
                    LOGGER.info("Written!");
                    makeGzip(sdf);
                    sdf.delete();
                } catch (Exception e) {
                    LOGGER.error("Unable to write SDF file", e);
                }
            }
            if (cl.hasOption(Format.keggCompound.name())) {
                String compoundFile = cl.getOptionValue(Format.keggCompound.name());
                if (compoundFile == null) {
                    LOGGER.error("Missing parameter: KEGG compound file");
                } else try {
                    File cf = new File(compoundFile);
                    KeggWriter keggWriter = new KeggWriter(exporter.rheaReader);
                    LOGGER.info("Writing KEGG compound file...");
                    keggWriter.writeCompound(allCompounds, new FileOutputStream(cf));
                    LOGGER.info("Written!");
                } catch (Exception e) {
                    LOGGER.error("Unable to write KEGG compound file", e);
                }
            }
            if (cl.hasOption(Format.keggReaction.name())) {
                String reactionFile = cl.getOptionValue(Format.keggReaction.name());
                if (reactionFile == null) {
                    LOGGER.error("Missing parameter: KEGG reaction file");
                } else try {
                    File rf = new File(reactionFile);
                    KeggWriter keggWriter = new KeggWriter(exporter.rheaReader);
                    LOGGER.info("Writing KEGG reaction file...");
                    keggWriter.writeReaction(exportedReactions, new FileOutputStream(rf));
                    LOGGER.info("Written!");
                } catch (Exception e) {
                    LOGGER.error("Unable to write KEGG reaction file", e);
                }
            }
            if (cl.hasOption(Format.sitemap.name())) {
                String sitemapFile = cl.getOptionValue(Format.sitemap.name());
                if (sitemapFile == null) {
                    LOGGER.error("Missing parameter: Sitemap output file");
                } else try {
                    File sitemap = new File(sitemapFile);
                    RheaWriter sitemapWriter = new SitemapWriter();
                    LOGGER.info("Writing sitemap file...");
                    sitemapWriter.write(exportedReactions, sitemapFile, rheaProps);
                    LOGGER.info("Written!");
                    makeGzip(sitemap);
                    sitemap.delete();
                } catch (Exception e) {
                    LOGGER.error("Unable to write sitemap file", e);
                }
            }
            if (cl.hasOption(Format.ebinocle.name())) {
                String ebinocleFile = cl.getOptionValue(Format.ebinocle.name());
                if (ebinocleFile == null) {
                    LOGGER.error("Missing parameter: EB-Eye output file");
                } else try {
                    RheaWriter ebinocleWriter = new EbinocleWriter();
                    LOGGER.info("Writing EB-Eye file...");
                    ebinocleWriter.write(exportedReactions, ebinocleFile, rheaProps);
                    LOGGER.info("Written!");
                } catch (Exception e) {
                    LOGGER.error("Unable to write EB-Eye file", e);
                }
            }
        } finally {
            if (exporter != null && exporter.rheaCon != null) exporter.rheaCon.close();
        }
    }

    protected void checkWriteableDirectory(String dirName) throws Exception {
        File dir = new File(dirName);
        if (!dir.exists()) dir.mkdirs();
        if (!dir.isDirectory()) {
            throw new Exception(dirName + " is not a directory");
        } else if (!dir.canWrite()) {
            throw new Exception(dirName + " is not writeable");
        }
    }

    /**
	 * Gets all of the public reactions from Rhea.
	 * @return a collection of {@link Reaction}s with public visibility.
	 * @throws Exception
	 * @see {@link IRheaReader#findAllPublic()}
	 */
    protected Collection<Reaction> getAllPublicReactions() throws Exception {
        Collection<Long> allPublicReactionIds = rheaReader.findAllPublic();
        Collection<Reaction> allPublicReactions = new HashSet<Reaction>();
        for (Long id : allPublicReactionIds) {
            Reaction r = rheaReader.findByReactionId(id);
            if (!r.isMapped()) {
                LOGGER.warn("Skippinig non-mapped reaction - RHEA:" + r.getId().toString());
                continue;
            }
            allPublicReactions.add(r);
            LOGGER.info("Added RHEA:" + r.getId().toString());
        }
        return allPublicReactions;
    }

    /**
	 * Gets one reaction from the ID provided as command-line option.
	 * @param reactionId the ID provided in the command-line
	 * @return a singleton with just one reaction.
	 * @throws MapperException
	 */
    protected Collection<Reaction> getOneReaction(String reactionId) throws MapperException {
        Long id = Long.valueOf(reactionId);
        Reaction r = rheaReader.findByReactionId(id);
        return Collections.singleton(r);
    }

    private static void makeGzip(File file) throws IOException {
        BufferedInputStream bin = null;
        GZIPOutputStream gzos = null;
        try {
            FileInputStream fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
            FileOutputStream fos = new FileOutputStream(file + ".gz");
            gzos = new GZIPOutputStream(fos);
            byte[] buf = new byte[1024];
            int len;
            while ((len = bin.read(buf)) > -1) {
                gzos.write(buf, 0, len);
            }
        } catch (IOException e) {
            LOGGER.fatal("Error while gzipping!", e);
        } finally {
            if (bin != null) bin.close();
            if (gzos != null) gzos.close();
        }
    }
}
