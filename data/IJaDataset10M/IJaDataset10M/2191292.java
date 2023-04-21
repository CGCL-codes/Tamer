package org.dspace.app.mets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Bitstream;
import org.dspace.content.BitstreamFormat;
import org.dspace.content.Bundle;
import org.dspace.content.Collection;
import org.dspace.content.DCValue;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.content.ItemIterator;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.Utils;
import org.dspace.handle.HandleManager;
import org.dspace.app.webui.util.UIUtil;
import edu.harvard.hul.ois.mets.Agent;
import edu.harvard.hul.ois.mets.AmdSec;
import edu.harvard.hul.ois.mets.BinData;
import edu.harvard.hul.ois.mets.Checksumtype;
import edu.harvard.hul.ois.mets.Div;
import edu.harvard.hul.ois.mets.DmdSec;
import edu.harvard.hul.ois.mets.FLocat;
import edu.harvard.hul.ois.mets.FileGrp;
import edu.harvard.hul.ois.mets.FileSec;
import edu.harvard.hul.ois.mets.Loctype;
import edu.harvard.hul.ois.mets.MdWrap;
import edu.harvard.hul.ois.mets.Mdtype;
import edu.harvard.hul.ois.mets.Mets;
import edu.harvard.hul.ois.mets.MetsHdr;
import edu.harvard.hul.ois.mets.Name;
import edu.harvard.hul.ois.mets.RightsMD;
import edu.harvard.hul.ois.mets.Role;
import edu.harvard.hul.ois.mets.StructMap;
import edu.harvard.hul.ois.mets.Type;
import edu.harvard.hul.ois.mets.XmlData;
import edu.harvard.hul.ois.mets.helper.Base64;
import edu.harvard.hul.ois.mets.helper.MetsException;
import edu.harvard.hul.ois.mets.helper.MetsValidator;
import edu.harvard.hul.ois.mets.helper.MetsWriter;
import edu.harvard.hul.ois.mets.helper.PCData;
import edu.harvard.hul.ois.mets.helper.PreformedXML;

/**
 * Tool for exporting DSpace AIPs with the metadata serialised in METS format
 * 
 * @author Robert Tansley
 * @version $Revision: 1336 $
 */
public class METSExport {

    private static int licenseFormat = -1;

    private static Properties dcToMODS;

    public static void main(String[] args) throws Exception {
        Context context = new Context();
        init(context);
        CommandLineParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("c", "collection", true, "Handle of collection to export");
        options.addOption("i", "item", true, "Handle of item to export");
        options.addOption("a", "all", false, "Export all items in the archive");
        options.addOption("d", "destination", true, "Destination directory");
        options.addOption("h", "help", false, "Help");
        CommandLine line = parser.parse(options, args);
        if (line.hasOption('h')) {
            HelpFormatter myhelp = new HelpFormatter();
            myhelp.printHelp("metsexport", options);
            System.out.println("\nExport a collection:  metsexport -c hdl:123.456/789");
            System.out.println("Export an item:       metsexport -i hdl:123.456/890");
            System.out.println("Export everything:    metsexport -a");
            System.exit(0);
        }
        String dest = "";
        if (line.hasOption('d')) {
            dest = line.getOptionValue('d');
            if (!dest.endsWith(File.separator)) {
                dest = dest + File.separator;
            }
        }
        if (line.hasOption('i')) {
            String handle = getHandleArg(line.getOptionValue('i'));
            DSpaceObject o = HandleManager.resolveToObject(context, handle);
            if ((o != null) && o instanceof Item) {
                writeAIP(context, (Item) o, dest);
                System.exit(0);
            } else {
                System.err.println(line.getOptionValue('i') + " is not a valid item Handle");
                System.exit(1);
            }
        }
        ItemIterator items = null;
        if (line.hasOption('c')) {
            String handle = getHandleArg(line.getOptionValue('c'));
            DSpaceObject o = HandleManager.resolveToObject(context, handle);
            if ((o != null) && o instanceof Collection) {
                items = ((Collection) o).getItems();
            } else {
                System.err.println(line.getOptionValue('c') + " is not a valid collection Handle");
                System.exit(1);
            }
        }
        if (line.hasOption('a')) {
            items = Item.findAll(context);
        }
        if (items == null) {
            System.err.println("Nothing to export specified!");
            System.exit(1);
        }
        while (items.hasNext()) {
            writeAIP(context, items.next(), dest);
        }
        context.abort();
    }

    /**
     * Initialise various variables, read in config etc.
     * 
     * @param context
     *            DSpace context
     */
    private static void init(Context context) throws SQLException, IOException {
        if (licenseFormat != -1) {
            return;
        }
        BitstreamFormat bf = BitstreamFormat.findByShortDescription(context, "License");
        licenseFormat = bf.getID();
        String configFile = ConfigurationManager.getProperty("dspace.dir") + File.separator + "config" + File.separator + "dc2mods.cfg";
        InputStream is = new FileInputStream(configFile);
        dcToMODS = new Properties();
        dcToMODS.load(is);
    }

    /**
     * Write out the AIP for the given item to the given directory. A new
     * directory will be created with the Handle (URL-encoded) as the directory
     * name, and inside, a mets.xml file written, together with the bitstreams.
     * 
     * @param context
     *            DSpace context to use
     * @param item
     *            Item to write
     * @param dest
     *            destination directory
     */
    public static void writeAIP(Context context, Item item, String dest) throws SQLException, IOException, AuthorizeException, MetsException {
        System.out.println("Exporting item hdl:" + item.getHandle());
        java.io.File aipDir = new java.io.File(dest + URLEncoder.encode("hdl:" + item.getHandle(), "UTF-8"));
        if (!aipDir.mkdir()) {
            throw new IOException("Couldn't create " + aipDir.toString());
        }
        FileOutputStream out = new FileOutputStream(aipDir.toString() + java.io.File.separator + "mets.xml");
        writeMETS(context, item, out, false);
        out.close();
        Bundle[] bundles = item.getBundles();
        for (int i = 0; i < bundles.length; i++) {
            Bitstream[] bitstreams = bundles[i].getBitstreams();
            for (int b = 0; b < bitstreams.length; b++) {
                if ((bitstreams[b].getFormat().getID() != licenseFormat) && AuthorizeManager.authorizeActionBoolean(context, bitstreams[b], Constants.READ)) {
                    out = new FileOutputStream(aipDir.toString() + java.io.File.separator + bitstreams[b].getName());
                    InputStream in = bitstreams[b].retrieve();
                    Utils.bufferedCopy(in, out);
                    out.close();
                    in.close();
                }
            }
        }
    }

    /**
     * Write METS metadata corresponding to the metadata for an item
     * 
     * @param context
     *            DSpace context
     * @param item
     *            DSpace item to create METS object for
     * @param os
     *            A stream to write METS package to (UTF-8 encoding will be used)
     * @param fullURL
     *            if <code>true</code>, the &lt;FLocat&gt; values for each
     *            bitstream will be the full URL for that bitstream. Otherwise,
     *            only the filename itself will be used.
     */
    public static void writeMETS(Context context, Item item, OutputStream os, boolean fullURL) throws SQLException, IOException, AuthorizeException {
        try {
            init(context);
            Mets mets = new Mets();
            mets.setOBJID("hdl:" + item.getHandle());
            mets.setLABEL("DSpace Item");
            mets.setSchema("mods", "http://www.loc.gov/mods/v3", "http://www.loc.gov/standards/mods/v3/mods-3-0.xsd");
            MetsHdr metsHdr = new MetsHdr();
            metsHdr.setCREATEDATE(new Date());
            Agent agent = new Agent();
            agent.setROLE(Role.CUSTODIAN);
            agent.setTYPE(Type.ORGANIZATION);
            Name name = new Name();
            name.getContent().add(new PCData(ConfigurationManager.getProperty("dspace.name")));
            agent.getContent().add(name);
            metsHdr.getContent().add(agent);
            mets.getContent().add(metsHdr);
            DmdSec dmdSec = new DmdSec();
            dmdSec.setID("DMD_hdl_" + item.getHandle());
            MdWrap mdWrap = new MdWrap();
            mdWrap.setMDTYPE(Mdtype.MODS);
            XmlData xmlData = new XmlData();
            createMODS(item, xmlData);
            mdWrap.getContent().add(xmlData);
            dmdSec.getContent().add(mdWrap);
            mets.getContent().add(dmdSec);
            AmdSec amdSec = new AmdSec();
            amdSec.setID("TMD_hdl_" + item.getHandle());
            InputStream licenseStream = findLicense(context, item);
            if (licenseStream != null) {
                RightsMD rightsMD = new RightsMD();
                MdWrap rightsMDWrap = new MdWrap();
                rightsMDWrap.setMIMETYPE("text/plain");
                rightsMDWrap.setMDTYPE(Mdtype.OTHER);
                rightsMDWrap.setOTHERMDTYPE("TEXT");
                BinData binData = new BinData();
                Base64 base64 = new Base64(licenseStream);
                binData.getContent().add(base64);
                rightsMDWrap.getContent().add(binData);
                rightsMD.getContent().add(rightsMDWrap);
                amdSec.getContent().add(rightsMD);
            }
            mets.getContent().add(amdSec);
            FileSec fileSec = new FileSec();
            boolean fileSecEmpty = true;
            Bundle[] bundles = item.getBundles();
            for (int i = 0; i < bundles.length; i++) {
                Bitstream[] bitstreams = bundles[i].getBitstreams();
                if (bitstreams[0].getFormat().getID() == licenseFormat) {
                    continue;
                }
                FileGrp fileGrp = new FileGrp();
                if ((bundles[i].getName() != null) && !bundles[i].getName().equals("")) {
                    fileGrp.setUSE(bundles[i].getName());
                }
                for (int bits = 0; bits < bitstreams.length; bits++) {
                    String bitstreamPID = ConfigurationManager.getProperty("dspace.url") + "/bitstream/" + item.getHandle() + "/" + bitstreams[bits].getSequenceID() + "/" + UIUtil.encodeBitstreamName(bitstreams[bits].getName(), "UTF-8");
                    edu.harvard.hul.ois.mets.File file = new edu.harvard.hul.ois.mets.File();
                    String xmlIDstart = item.getHandle().replaceAll("/", "_") + "_";
                    file.setID(xmlIDstart + bitstreams[bits].getSequenceID());
                    String groupID = "GROUP_" + xmlIDstart + bitstreams[bits].getSequenceID();
                    if ((bundles[i].getName() != null) && (bundles[i].getName().equals("THUMBNAIL") || bundles[i].getName().equals("TEXT"))) {
                        Bitstream original = findOriginalBitstream(item, bitstreams[bits]);
                        if (original != null) {
                            groupID = "GROUP_" + xmlIDstart + original.getSequenceID();
                        }
                    }
                    file.setGROUPID(groupID);
                    file.setOWNERID(bitstreamPID);
                    file.setMIMETYPE(bitstreams[bits].getFormat().getMIMEType());
                    file.setSIZE(bitstreams[bits].getSize());
                    file.setCHECKSUM(bitstreams[bits].getChecksum());
                    file.setCHECKSUMTYPE(Checksumtype.MD5);
                    FLocat flocat = new FLocat();
                    flocat.setLOCTYPE(Loctype.URL);
                    if (fullURL) {
                        flocat.setXlinkHref(bitstreamPID);
                    } else {
                        flocat.setXlinkHref(bitstreams[bits].getName());
                    }
                    file.getContent().add(flocat);
                    fileGrp.getContent().add(file);
                }
                fileSec.getContent().add(fileGrp);
                fileSecEmpty = false;
            }
            if (!fileSecEmpty) {
                mets.getContent().add(fileSec);
            }
            StructMap structMap = new StructMap();
            Div div = new Div();
            structMap.getContent().add(div);
            mets.getContent().add(structMap);
            mets.validate(new MetsValidator());
            mets.write(new MetsWriter(os));
        } catch (MetsException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Utility to find the license bitstream from an item
     * 
     * @param context
     *            DSpace context
     * @param item
     *            the item
     * @return the license as a string
     * 
     * @throws IOException
     *             if the license bitstream can't be read
     */
    private static InputStream findLicense(Context context, Item item) throws SQLException, IOException, AuthorizeException {
        Bundle[] bundles = item.getBundles();
        for (int i = 0; i < bundles.length; i++) {
            Bitstream[] bitstreams = bundles[i].getBitstreams();
            if (bitstreams[0].getFormat().getID() == licenseFormat) {
                return bitstreams[0].retrieve();
            }
        }
        return null;
    }

    /**
     * For a bitstream that's a thumbnail or extracted text, find the
     * corresponding bitstream in the ORIGINAL bundle
     * 
     * @param item
     *            the item we're dealing with
     * @param derived
     *            the derived bitstream
     * 
     * @return the corresponding original bitstream (or null)
     */
    private static Bitstream findOriginalBitstream(Item item, Bitstream derived) throws SQLException {
        Bundle[] bundles = item.getBundles();
        String originalFilename = derived.getName().substring(0, derived.getName().length() - 4);
        for (int i = 0; i < bundles.length; i++) {
            if ((bundles[i].getName() != null) && bundles[i].getName().equals("ORIGINAL")) {
                Bitstream[] bitstreams = bundles[i].getBitstreams();
                for (int bsnum = 0; bsnum < bitstreams.length; bsnum++) {
                    if (bitstreams[bsnum].getName().equals(originalFilename)) {
                        return bitstreams[bsnum];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Create MODS metadata from the DC in the item, and add to the given
     * XmlData METS object.
     * 
     * @param item
     *            the item
     * @param xmlData
     *            xmlData to add MODS to.
     */
    private static void createMODS(Item item, XmlData xmlData) {
        DCValue[] dc = item.getDC(Item.ANY, Item.ANY, Item.ANY);
        StringBuffer modsXML = new StringBuffer();
        for (int i = 0; i < dc.length; i++) {
            String propName = ((dc[i].qualifier == null) ? dc[i].element : (dc[i].element + "." + dc[i].qualifier));
            String modsMapping = dcToMODS.getProperty(propName);
            if (modsMapping == null) {
                System.err.println("WARNING: No MODS mapping for " + propName);
            } else {
                modsXML.append(modsMapping.replaceAll("%s", Utils.addEntities(dc[i].value)));
                modsXML.append("\n");
            }
        }
        PreformedXML pXML = new PreformedXML(modsXML.toString());
        xmlData.getContent().add(pXML);
    }

    /**
     * Get the handle from the command line in the form 123.456/789. Doesn't
     * matter if incoming handle has 'hdl:' or 'http://hdl....' before it.
     * 
     * @param original
     *            Handle as passed in by user
     * @return Handle as can be looked up in our table
     */
    private static String getHandleArg(String original) {
        if (original.startsWith("hdl:")) {
            return original.substring(4);
        }
        if (original.startsWith("http://hdl.handle.net/")) {
            return original.substring(22);
        }
        String localHandleUriPrefix = ConfigurationManager.getProperty("dspace.url") + "/handle/";
        if (original.startsWith(localHandleUriPrefix)) {
            return original.substring(localHandleUriPrefix.length());
        }
        return original;
    }
}
