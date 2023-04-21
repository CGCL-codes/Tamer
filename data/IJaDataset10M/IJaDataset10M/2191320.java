package org.jcvi.elvira.coverage.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.hibernate.Session;
import org.jcvi.common.command.CommandLineOptionBuilder;
import org.jcvi.common.command.CommandLineUtils;
import org.jcvi.common.core.assembly.Contig;
import org.jcvi.common.core.assembly.PlacedRead;
import org.jcvi.common.core.assembly.ace.AbstractAceContigBuilder;
import org.jcvi.common.core.assembly.ace.AceContig;
import org.jcvi.common.core.assembly.ace.AceFileParser;
import org.jcvi.common.core.assembly.ace.AceFileVisitor;
import org.jcvi.common.core.assembly.ace.AcePlacedRead;
import org.jcvi.common.core.assembly.ctg.AbstractContigFileVisitorBuilder;
import org.jcvi.common.core.assembly.ctg.ContigFileParser;
import org.jcvi.common.core.assembly.ctg.ContigFileVisitor;
import org.jcvi.common.core.datastore.DataStoreException;
import org.jcvi.common.core.io.IOUtil;
import org.jcvi.common.core.util.iter.CloseableIterator;
import org.jcvi.common.io.fileServer.DirectoryFileServer;
import org.jcvi.common.io.fileServer.ReadWriteFileServer;
import org.jcvi.commonx.auth.tigr.ProjectDbAuthorizer;
import org.jcvi.commonx.auth.tigr.ProjectDbAuthorizerUtils;
import org.jcvi.glk.adapter.ContigAdapter;
import org.jcvi.glk.adapter.GLKAssemblyDataStore;
import org.jcvi.glk.adapter.GLKCurrentBacAssemblyDataStore;
import org.jcvi.glk.elvira.ElviraGLKSessionBuilder;
import org.jcvi.glk.helpers.GLKHelper;
import org.jcvi.glk.helpers.HibernateGLKHelper;

public class CreateCoverageMaps {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        Options options = new Options();
        options.addOption(CommandLineUtils.createHelpOption());
        OptionGroup assemblyGroup = new OptionGroup();
        assemblyGroup.setRequired(true);
        assemblyGroup.addOption(new CommandLineOptionBuilder("a", "path to ace file to examine").longName("ace").build());
        assemblyGroup.addOption(new CommandLineOptionBuilder("c", "path to contig file to examine").longName("contig").build());
        assemblyGroup.addOption(new CommandLineOptionBuilder("s", "bac id of sample to read from Project DB").longName("sample").build());
        options.addOptionGroup(assemblyGroup);
        ProjectDbAuthorizerUtils.addProjectDbLoginOptionsTo(options, false);
        options.addOption(new CommandLineOptionBuilder("o", "path to output directory of where to put png files").longName("out").isRequired(true).build());
        options.addOption(new CommandLineOptionBuilder("prefix", "file prefix of all output png files " + "the format of the files will be <prefix>.<contigId>.coverage.png").isRequired(true).build());
        if (CommandLineUtils.helpRequested(args)) {
            printHelp(options);
            System.exit(0);
        }
        try {
            CommandLine commandLine = CommandLineUtils.parseCommandLine(options, args);
            final String prefix = commandLine.getOptionValue("prefix");
            final ReadWriteFileServer outputDir = DirectoryFileServer.createReadWriteDirectoryFileServer(new File(commandLine.getOptionValue("o")));
            if (commandLine.hasOption("a")) {
                handleAceFile(commandLine, prefix, outputDir);
            } else if (commandLine.hasOption("c")) {
                handleContigFile(commandLine, prefix, outputDir);
            } else {
                handleProjectDbSample(commandLine, prefix, outputDir);
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printHelp(options);
            System.exit(1);
        }
    }

    private static void handleProjectDbSample(CommandLine commandLine, String prefix, ReadWriteFileServer outputDir) throws IOException {
        ProjectDbAuthorizer auth = ProjectDbAuthorizerUtils.getProjectDbAuthorizerFrom(commandLine);
        Session session = new ElviraGLKSessionBuilder(auth).build();
        GLKHelper glkHelper = new HibernateGLKHelper(session);
        int bacId = Integer.parseInt(commandLine.getOptionValue("s"));
        GLKAssemblyDataStore datastore = new GLKCurrentBacAssemblyDataStore(glkHelper, bacId);
        CloseableIterator<ContigAdapter> iter = null;
        try {
            iter = datastore.iterator();
            while (iter.hasNext()) {
                ContigAdapter contig = iter.next();
                String id = contig.getId();
                try {
                    File outputPng = outputDir.createNewFile(String.format("%s.%s.coverage.png", prefix, id));
                    SequenceCoverageWriter<PlacedRead> sequenceCoverageWriter = new SequenceCoverageWriter<PlacedRead>(outputPng, "ungapped Sequence Coverage of " + id);
                    sequenceCoverageWriter.write(contig);
                    sequenceCoverageWriter.close();
                } catch (IOException e) {
                    throw new IllegalStateException("error creating new output png file", e);
                }
            }
        } catch (DataStoreException e) {
            throw new IOException("error creating contig iterator", e);
        } finally {
            IOUtil.closeAndIgnoreErrors(iter, datastore);
            session.close();
        }
    }

    private static void handleAceFile(CommandLine commandLine, final String prefix, final ReadWriteFileServer outputDir) throws IOException {
        File aceFile = new File(commandLine.getOptionValue("a"));
        AceFileVisitor visitor = new AbstractAceContigBuilder() {

            @Override
            protected void visitContig(AceContig contig) {
                String id = contig.getId();
                try {
                    File outputPng = outputDir.createNewFile(String.format("%s.%s.coverage.png", prefix, id));
                    SequenceCoverageWriter<AcePlacedRead> sequenceCoverageWriter = new SequenceCoverageWriter<AcePlacedRead>(outputPng, "ungapped Sequence Coverage of " + id);
                    sequenceCoverageWriter.write(contig);
                    sequenceCoverageWriter.close();
                } catch (IOException e) {
                    throw new IllegalStateException("error creating new output png file", e);
                }
            }
        };
        AceFileParser.parseAceFile(aceFile, visitor);
    }

    private static void handleContigFile(CommandLine commandLine, final String prefix, final ReadWriteFileServer outputDir) throws IOException {
        File contigFile = new File(commandLine.getOptionValue("c"));
        ContigFileVisitor visitor = new AbstractContigFileVisitorBuilder() {

            @Override
            protected void addContig(Contig<PlacedRead> contig) {
                String id = contig.getId();
                try {
                    File outputPng = outputDir.createNewFile(String.format("%s.%s.coverage.png", prefix, id));
                    SequenceCoverageWriter<PlacedRead> sequenceCoverageWriter = new SequenceCoverageWriter<PlacedRead>(outputPng, "ungapped Sequence Coverage of " + id);
                    sequenceCoverageWriter.write(contig);
                    sequenceCoverageWriter.close();
                } catch (IOException e) {
                    throw new IllegalStateException("error creating new output png file", e);
                }
            }
        };
        ContigFileParser.parse(contigFile, visitor);
    }

    /**
     * @param options
     */
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("createCoverageMaps -a <ace file> -o <output dir> -p <prefix>", "read the given ace file, contig file or bac id and creates coverage map png files for each contig", options, "Created by Danny Katzel");
    }
}
