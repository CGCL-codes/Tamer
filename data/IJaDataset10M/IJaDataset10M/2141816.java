package org.expasy.jpl.tools.qm.main;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expasy.jpl.commons.base.io.StackTracePrinter;
import org.expasy.jpl.commons.collection.ExtraIterable.AbstractExtraIterator;
import org.expasy.jpl.core.ms.spectrum.editor.IntensityTransformer;
import org.expasy.jpl.core.ms.spectrum.filter.NPeaksPerWindowFilter;
import org.expasy.jpl.core.ms.spectrum.filter.SpectrumProcessorManager;
import org.expasy.jpl.core.ms.spectrum.peak.InvalidPeakException;
import org.expasy.jpl.io.AbstractExtraIterableReader;
import org.expasy.jpl.io.hadoop.HMapDeserializer;
import org.expasy.jpl.io.hadoop.HMapSerializer;
import org.expasy.jpl.io.hadoop.HMapSerializer.KeyingMode;
import org.expasy.jpl.io.hadoop.ms.HMapMSFileConverter.MS2HMSConvertException;
import org.expasy.jpl.io.ms.MSScan;
import org.expasy.jpl.tools.qm.charts.DeltaMassHistogramChartContainer;
import org.expasy.jpl.tools.qm.charts.FDRPlotContainer;
import org.expasy.jpl.tools.qm.dataloader.MSSmartLoader;
import org.expasy.jpl.tools.qm.export.ChartGraphicsExport;
import org.expasy.jpl.tools.qm.export.PepXMLExporter;
import org.expasy.jpl.tools.qm.export.QMFileHandling;
import org.expasy.jpl.tools.qm.export.SimpleExport;
import org.expasy.jpl.tools.qm.library.LibraryContainerException;
import org.expasy.jpl.tools.qm.library.LibraryContainerHms;
import org.expasy.jpl.tools.qm.library.SerializeLibrary;
import org.expasy.jpl.tools.qm.params.CommandLineOptions;
import org.expasy.jpl.tools.qm.params.SearchParams;
import org.expasy.jpl.tools.qm.params.SearchParamsException;
import org.expasy.jpl.tools.qm.params.SearchParamsLoaderException;
import org.expasy.jpl.tools.qm.results.FalseDiscoveryRate;
import org.expasy.jpl.tools.qm.results.LibrarySearchMatch;
import org.expasy.jpl.tools.qm.results.QueryMatchList;
import org.expasy.jpl.tools.qm.results.ScoreWeightException;
import org.expasy.jpl.tools.qm.search.LibrarySearch;

/**
 * @author eahrne
 * 
 */
public class RunQuickMod {

    @SuppressWarnings("unused")
    private static final String __VERSION__ = "1.04";

    Log log = LogFactory.getLog(this.getClass());

    private static AbstractExtraIterableReader<MSScan> QUERYREADER = HMapDeserializer.newSequentialAccessInstance();

    @SuppressWarnings("unchecked")
    private static final Transformer ITRANSFORM[] = { IntensityTransformer.BASE_PEAK, IntensityTransformer.RANK, IntensityTransformer.LOG, IntensityTransformer.SQRT };

    public RunQuickMod(String args[]) {
        CommandLineOptions clo;
        try {
            clo = new CommandLineOptions(args);
            if (clo.getLibraryFileName() != null) {
                SerializeLibrary.serialize(clo.getLibraryFileName(), clo.getBinaryBatchSize(), clo.getBinaryLibOutDir());
            } else if (clo.getParametersFile() != null) {
                runSearchTool(clo.getParametersFile());
            }
        } catch (Exception e) {
            StackTracePrinter stp = new StackTracePrinter("exception");
            log.error(stp.format(e));
        }
    }

    @SuppressWarnings("unchecked")
    private void runSearchTool(File paramsFile) {
        try {
            new SearchParams(paramsFile);
            SpectrumProcessorManager filterManager = new SpectrumProcessorManager();
            NPeaksPerWindowFilter filter = new NPeaksPerWindowFilter(SearchParams.PEAKSPERWINDOW, SearchParams.WINDOWSIZE);
            IntensityTransformer iTransformer = new IntensityTransformer(ITRANSFORM[SearchParams.INTENSITYTRANSFORMATION]);
            filterManager.add(filter);
            filterManager.add(iTransformer);
            if (SearchParams.INTENSITYTRANSFORMATION > 1) {
                IntensityTransformer iTransformer2 = new IntensityTransformer(ITRANSFORM[0]);
                filterManager.add(iTransformer2);
            }
            System.out.println("--------------------------------------SEARCH PARAMS--------------------------------------\n" + SearchParams.toText());
            System.out.println("-----------------------------------------------------------------------------------------\n");
            Calendar cal = new GregorianCalendar();
            System.out.println("Started at: " + cal.getTime());
            int minPrecCharge = SearchParams.MINPRECCHARGE;
            int maxPrecCharge = SearchParams.MAXPRECCHARGE;
            int canddatesPerSpectrum = SearchParams.CANDIDATESPERSPECTRUM;
            PepXMLExporter pepXMLExporter = new PepXMLExporter(new File(SearchParams.RESDIRECTORY + "/quickmod.pep.xml"));
            HMapSerializer<Serializable> resultsSerializer = HMapSerializer.newInstance(KeyingMode.LIST);
            resultsSerializer.enableAppendingMode(true);
            for (int expFileNb = 0; expFileNb < SearchParams.EXPFILES.length; expFileNb++) {
                String expFileName = SearchParams.EXPFILES[expFileNb];
                File expFile = new File(expFileName);
                HMapSerializer<Serializable> fdrSerializer = HMapSerializer.newInstance(KeyingMode.LIST);
                pepXMLExporter.addNewMSMSRunSummary(expFile);
                pepXMLExporter.addNewSearchSummaryList(expFile, Arrays.asList(SearchParams.LIBFILES));
                for (int libFileNb = 0; libFileNb < SearchParams.LIBFILES.length; libFileNb++) {
                    HashMap<Integer, FalseDiscoveryRate> fdrPerCharge = new HashMap<Integer, FalseDiscoveryRate>();
                    String libFileName = SearchParams.LIBFILES[libFileNb];
                    pepXMLExporter.setCurrentLibfilename(libFileName);
                    String outputFileBasePath = QMFileHandling.getOutPutFileName(SearchParams.RESDIRECTORY, expFileName, libFileName, SearchParams.RESULTSFILELABEL, "");
                    File libFile = new File(libFileName);
                    System.out.println("---Screening " + expFile.getName() + " vs. " + libFile.getName());
                    String hmsExpFileName = QMFileHandling.addFileSeparator(expFile.getParent()) + QMFileHandling.getHmsFileName(expFileName);
                    String hmsLibFileName = QMFileHandling.addFileSeparator(libFile.getParent()) + QMFileHandling.getHmsFileName(libFileName);
                    if (!(new File(hmsExpFileName)).exists()) {
                        hmsExpFileName = SearchParams.RESDIRECTORY + QMFileHandling.getHmsFileName(expFileName);
                        if (!(new File(hmsExpFileName)).exists()) {
                            System.out.println("------Serializing Query spectra to " + new File(hmsExpFileName).getName());
                            MSSmartLoader.enableMemoryTest(false);
                            MSSmartLoader.enableProgressBar(SearchParams.ENABLEPROGRESSBAR);
                            MSSmartLoader.createHMSExpFile(expFileName, hmsExpFileName, SearchParams.QUERYBATCHSIZE);
                        }
                    }
                    if (!(new File(hmsLibFileName)).exists()) {
                        hmsLibFileName = SearchParams.RESDIRECTORY + QMFileHandling.getHmsFileName(libFileName);
                        if (!(new File(hmsLibFileName)).exists()) {
                            System.out.println("\n------Serializing Library spectra to " + new File(hmsLibFileName).getName());
                            MSSmartLoader.enableMemoryTest(false);
                            MSSmartLoader.enableProgressBar(SearchParams.ENABLEPROGRESSBAR);
                            MSSmartLoader.createHMSLibFile(libFileName, hmsLibFileName, SearchParams.LIBRARYBATCHSIZE);
                        }
                    }
                    resultsSerializer.open(new File(outputFileBasePath + ".hser"));
                    fdrSerializer.open(new File(outputFileBasePath + ".fser"));
                    int selectedPrecCharge = minPrecCharge;
                    while (selectedPrecCharge <= maxPrecCharge) {
                        System.out.println("\n---------Analysing charge state: " + selectedPrecCharge);
                        FalseDiscoveryRate fdr = new FalseDiscoveryRate();
                        QUERYREADER.parse(new File(hmsExpFileName));
                        AbstractExtraIterator<MSScan> queryIt = QUERYREADER.iterator();
                        int queryBatch = 0;
                        LibraryContainerHms.setFILTERMANAGER(filterManager);
                        LibraryContainerHms lc = new LibraryContainerHms(hmsLibFileName, selectedPrecCharge, SearchParams.LIBRARYBATCHSIZE);
                        while (queryIt.hasNext() && lc.getLibraryBatch().size() > 0) {
                            List<QueryMatchList> resultList = new ArrayList<QueryMatchList>();
                            List<MSScan> querySpectra = MSSmartLoader.parseFile(queryIt, filterManager, selectedPrecCharge, SearchParams.QUERYBATCHSIZE, queryBatch);
                            QueryMatchList[] queryResults = new QueryMatchList[querySpectra.size()];
                            while (lc.searchBatch(queryBatch) && querySpectra.size() > 0) {
                                List<MSScan> libSpectra = lc.getLibraryBatch(queryBatch);
                                if (libSpectra.size() > 0) {
                                    Calendar cal3 = new GregorianCalendar();
                                    System.out.println("------------search start: " + cal3.getTime() + " queryBatch: " + queryBatch + " " + querySpectra.size() + " spectra, " + "libraryBatch: " + lc.getLibraryBatchNb() + " " + libSpectra.size() + " spectra.");
                                    LibrarySearch.setMatcher(SearchParams.FRAGMENTMZTOL);
                                    LibrarySearch.setENABLEPROGRESSBAR(SearchParams.ENABLEPROGRESSBAR);
                                    LibrarySearchMatch.setSCOREWEIGHTS(SearchParams.SCOREWEIGHTS);
                                    LibrarySearch libSearch = new LibrarySearch(querySpectra, libSpectra);
                                    QueryMatchList[] batchResults = libSearch.getQueryResults();
                                    Calendar cal4 = new GregorianCalendar();
                                    System.out.println("\n------------search end: " + cal4.getTime());
                                    queryResults = this.addBatchResults(queryResults, batchResults, lc.getLibraryBatchNb(), canddatesPerSpectrum);
                                }
                            }
                            System.out.println("------------Adding search results");
                            List<QueryMatchList> qmll = Arrays.asList(queryResults);
                            resultList.addAll(qmll);
                            if (SearchParams.PEPXMLEXPORT) {
                                for (QueryMatchList qml : qmll) {
                                    if (qml != null) {
                                        List<LibrarySearchMatch> qmlms = qml.getMatches();
                                        List<LibrarySearchMatch> qmlOms = qml.getOMSMatches();
                                        if (qmlms.size() > 0) {
                                            pepXMLExporter.addLibSearchMatches(qmlms);
                                        }
                                        if (qmlOms.size() > 0) {
                                            pepXMLExporter.addLibSearchMatches(qmlOms);
                                        }
                                        pepXMLExporter.flush();
                                    }
                                }
                            }
                            resultsSerializer.add((Serializable) resultList);
                            resultsSerializer.flush();
                            fdr.addResults(resultList);
                            System.out.println("------------DONE Adding search results");
                            queryBatch++;
                        }
                        fdrPerCharge.put(selectedPrecCharge, fdr);
                        selectedPrecCharge++;
                    }
                    resultsSerializer.close();
                    fdrSerializer.add((Serializable) fdrPerCharge);
                    fdrSerializer.flush();
                    fdrSerializer.close();
                    SimpleExport.writeFile(outputFileBasePath, false, fdrPerCharge);
                    if (SearchParams.OMS || SearchParams.TMS) {
                        SimpleExport.writeFile(outputFileBasePath, true, fdrPerCharge);
                    }
                    if (SearchParams.CREATEFDRPLOTS) {
                        System.out.println("---Creating FDR plots");
                        FDRPlotContainer fdrCont = new FDRPlotContainer(fdrPerCharge);
                        ChartGraphicsExport.exportChartListAsJPEG(fdrCont.getNoOmsFDRCharts(), outputFileBasePath, 640, 480);
                        ChartGraphicsExport.exportChartListAsJPEG(fdrCont.getNoOmsScoreDistribCharts(), outputFileBasePath, 640, 480);
                        if (SearchParams.OMS || SearchParams.TMS) {
                            ChartGraphicsExport.exportChartListAsJPEG(fdrCont.getOmsFDRCharts(), outputFileBasePath, 640, 480);
                            ChartGraphicsExport.exportChartListAsJPEG(fdrCont.getOmsScoreDistribCharts(), outputFileBasePath, 640, 480);
                            int bins = (int) Math.round((1 / SearchParams.PRECURSORMZTOL) * SearchParams.MODIFICATIONMASSTOL);
                            DeltaMassHistogramChartContainer.setBINS(bins);
                            DeltaMassHistogramChartContainer histChartCont = new DeltaMassHistogramChartContainer(outputFileBasePath + ".hser", fdrPerCharge, SearchParams.FDRCUTOFF);
                            ChartGraphicsExport.exportChartAsJPEG(histChartCont.getHistChart(), outputFileBasePath, 640, 480);
                        }
                    }
                }
            }
            if (SearchParams.PEPXMLEXPORT) {
                pepXMLExporter.close();
            }
        } catch (IOException e) {
            log.fatal(e.getMessage() + " : " + e.getClass());
        } catch (IllegalArgumentException e) {
            log.fatal(e.getMessage() + " : " + e.getClass());
        } catch (SearchParamsLoaderException e) {
            log.fatal(e.getMessage() + " : " + e.getClass());
        } catch (SearchParamsException e) {
            log.fatal(e.getMessage() + " : " + e.getClass());
        } catch (ParseException e) {
            log.fatal(e.getMessage() + " : " + e.getClass());
            e.printStackTrace();
        } catch (InvalidPeakException e) {
            log.fatal(e.getMessage() + " : " + e.getClass());
        } catch (LibraryContainerException e) {
            log.fatal(e.getMessage() + " : " + e.getClass());
        } catch (ClassNotFoundException e) {
            log.fatal(e.getMessage() + " : " + e.getClass());
        } catch (ScoreWeightException e) {
            log.fatal(e.getMessage() + " : " + e.getClass());
        } catch (MS2HMSConvertException e) {
            log.fatal(e.getMessage() + " : " + e.getClass());
        }
        Calendar cal2 = new GregorianCalendar();
        System.out.println("Finished at :" + cal2.getTime());
    }

    private QueryMatchList[] addBatchResults(QueryMatchList[] queryResults, QueryMatchList[] batchResults, int libraryBatch, int canddatesPerSpectrum) {
        if (libraryBatch > 0) {
            for (int k = 0; k < queryResults.length; k++) {
                if (queryResults[k] != null) {
                    if (batchResults[k] != null) {
                        queryResults[k].updateMatchList(batchResults[k].getMatches(), canddatesPerSpectrum);
                        queryResults[k].updateOMSMatchList(batchResults[k].getOMSMatches(), canddatesPerSpectrum);
                    }
                } else {
                    queryResults[k] = batchResults[k];
                }
            }
        } else {
            queryResults = batchResults;
        }
        return queryResults;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new RunQuickMod(args);
        System.exit(0);
    }
}
