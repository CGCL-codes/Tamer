package org.pdfclown.samples.cli;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.pdfclown.documents.Document;
import org.pdfclown.documents.Page;
import org.pdfclown.documents.Pages;
import org.pdfclown.files.File;
import org.pdfclown.objects.PdfReference;
import org.pdfclown.tools.PageManager;

/**
  This sample demonstrates <b>how to manipulate the pages collection</b> within a PDF document,
  to perform page data size calculations, additions, movements, removals, extractions and
  splits of groups of pages.

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @version 0.1.2, 01/29/12
*/
public class PageManagementSample extends Sample {

    private enum ActionEnum {

        PageDataSizeCalculation, PageAddition, PageMovement, PageRemoval, PageExtraction, DocumentMerge, DocumentBurst, DocumentSplitByPageIndex, DocumentSplitOnMaximumFileSize;

        public String getDescription() {
            StringBuilder builder = new StringBuilder();
            for (char c : name().toCharArray()) {
                if (Character.isUpperCase(c) && builder.length() > 0) {
                    builder.append(" ");
                }
                builder.append(c);
            }
            return builder.toString();
        }
    }

    @Override
    public boolean run() {
        final ActionEnum action = promptAction();
        final String mainFilePath = promptPdfFileChoice("Please select a PDF file");
        final File mainFile;
        try {
            mainFile = new File(mainFilePath);
        } catch (Exception e) {
            throw new RuntimeException(mainFilePath + " file access error.", e);
        }
        final Document mainDocument = mainFile.getDocument();
        final Pages mainPages = mainDocument.getPages();
        final int mainPagesCount = mainPages.size();
        switch(action) {
            case PageDataSizeCalculation:
                {
                    System.out.println("\nThis algorithm calculates the data size (expressed in bytes) of the selected document's pages.");
                    System.out.println("Legend:");
                    System.out.println(" * full: page data size encompassing all its dependencies (like shared resources) -- this is the size of the page when extracted as a single-page document;");
                    System.out.println(" * differential: additional page data size -- this is the extra-content that's not shared with previous pages;");
                    System.out.println(" * incremental: data size of the page sublist encompassing all the previous pages and the current one.\n");
                    Set<PdfReference> visitedReferences = new HashSet<PdfReference>();
                    long incrementalDataSize = 0;
                    for (Page page : mainPages) {
                        long pageFullDataSize = PageManager.getSize(page);
                        long pageDifferentialDataSize = PageManager.getSize(page, visitedReferences);
                        incrementalDataSize += pageDifferentialDataSize;
                        System.out.println("Page " + (page.getIndex() + 1) + ": " + pageFullDataSize + " (full); " + pageDifferentialDataSize + " (differential); " + incrementalDataSize + " (incremental)");
                    }
                }
                break;
            case PageAddition:
                {
                    File sourceFile;
                    {
                        String sourceFilePath = promptPdfFileChoice("Select the source PDF file");
                        try {
                            sourceFile = new File(sourceFilePath);
                        } catch (Exception e) {
                            throw new RuntimeException(sourceFilePath + " file access error.", e);
                        }
                    }
                    Pages sourcePages = sourceFile.getDocument().getPages();
                    int sourcePagesCount = sourcePages.size();
                    int fromSourcePageIndex = promptPageChoice("Select the start source page to add", sourcePagesCount);
                    int toSourcePageIndex = promptPageChoice("Select the end source page to add", fromSourcePageIndex + 1, sourcePagesCount) + 1;
                    int targetPageIndex = promptPageChoice("Select the position where to insert the source pages", mainPagesCount + 1);
                    new PageManager(mainDocument).add(targetPageIndex, sourcePages.subList(fromSourcePageIndex, toSourcePageIndex));
                    serialize(mainFile, action);
                }
                break;
            case PageMovement:
                {
                    int fromSourcePageIndex = promptPageChoice("Select the start page to move", mainPagesCount);
                    int toSourcePageIndex = promptPageChoice("Select the end page to move", fromSourcePageIndex + 1, mainPagesCount) + 1;
                    int targetPageIndex = promptPageChoice("Select the position where to insert the pages", mainPagesCount + 1);
                    new PageManager(mainDocument).move(fromSourcePageIndex, toSourcePageIndex, targetPageIndex);
                    serialize(mainFile, action);
                }
                break;
            case PageRemoval:
                {
                    int fromPageIndex = promptPageChoice("Select the start page to remove", mainPagesCount);
                    int toPageIndex = promptPageChoice("Select the end page to remove", fromPageIndex + 1, mainPagesCount) + 1;
                    new PageManager(mainDocument).remove(fromPageIndex, toPageIndex);
                    serialize(mainFile, action);
                }
                break;
            case PageExtraction:
                {
                    int fromPageIndex = promptPageChoice("Select the start page", mainPagesCount);
                    int toPageIndex = promptPageChoice("Select the end page", fromPageIndex + 1, mainPagesCount) + 1;
                    Document targetDocument = new PageManager(mainDocument).extract(fromPageIndex, toPageIndex);
                    serialize(targetDocument.getFile(), action);
                }
                break;
            case DocumentMerge:
                {
                    File sourceFile;
                    {
                        String sourceFilePath = promptPdfFileChoice("Select the source PDF file");
                        try {
                            sourceFile = new File(sourceFilePath);
                        } catch (Exception e) {
                            throw new RuntimeException(sourceFilePath + " file access error.", e);
                        }
                    }
                    new PageManager(mainDocument).add(sourceFile.getDocument());
                    serialize(mainFile, action);
                }
                break;
            case DocumentBurst:
                {
                    List<Document> splitDocuments = new PageManager(mainDocument).split();
                    int index = 0;
                    for (Document splitDocument : splitDocuments) {
                        serialize(splitDocument.getFile(), action, ++index);
                    }
                }
                break;
            case DocumentSplitByPageIndex:
                {
                    int splitCount;
                    try {
                        splitCount = Integer.parseInt(promptChoice("Number of split positions: "));
                    } catch (Exception e) {
                        splitCount = 0;
                    }
                    int[] splitIndexes = new int[splitCount];
                    {
                        int prevSplitIndex = 0;
                        for (int index = 0; index < splitCount; index++) {
                            int splitIndex = promptPageChoice("Position " + (index + 1) + " of " + splitCount, prevSplitIndex + 1, mainPagesCount);
                            splitIndexes[index] = splitIndex;
                            prevSplitIndex = splitIndex;
                        }
                    }
                    List<Document> splitDocuments = new PageManager(mainDocument).split(splitIndexes);
                    int index = 0;
                    for (Document splitDocument : splitDocuments) {
                        serialize(splitDocument.getFile(), action, ++index);
                    }
                }
                break;
            case DocumentSplitOnMaximumFileSize:
                {
                    long maxDataSize;
                    {
                        long mainFileSize = new java.io.File(mainFilePath).length();
                        int kbMaxDataSize;
                        do {
                            try {
                                kbMaxDataSize = Integer.parseInt(promptChoice("Max file size (KB): "));
                            } catch (Exception e) {
                                kbMaxDataSize = 0;
                            }
                        } while (kbMaxDataSize == 0);
                        maxDataSize = kbMaxDataSize << 10;
                        if (maxDataSize > mainFileSize) {
                            maxDataSize = mainFileSize;
                        }
                    }
                    List<Document> splitDocuments = new PageManager(mainDocument).split(maxDataSize);
                    int index = 0;
                    for (Document splitDocument : splitDocuments) {
                        serialize(splitDocument.getFile(), action, ++index);
                    }
                }
                break;
        }
        return true;
    }

    private ActionEnum promptAction() {
        ActionEnum[] actions = ActionEnum.values();
        Map<String, String> options = new HashMap<String, String>();
        for (ActionEnum action : actions) {
            options.put(Integer.toString(action.ordinal()), action.getDescription());
        }
        try {
            return actions[Integer.parseInt(promptChoice(options))];
        } catch (Exception e) {
            return actions[0];
        }
    }

    /**
    Serializes the specified PDF file.

    @param file File to serialize.
    @param action Generator.
  */
    private void serialize(File file, ActionEnum action) {
        serialize(file, action, null);
    }

    /**
    Serializes the specified PDF file.

    @param file File to serialize.
    @param action Generator.
    @param index File index.
  */
    private void serialize(File file, ActionEnum action, Integer index) {
        serialize(file, getClass().getSimpleName() + "_" + action.name() + (index != null ? "." + index : ""), null, null);
    }
}
