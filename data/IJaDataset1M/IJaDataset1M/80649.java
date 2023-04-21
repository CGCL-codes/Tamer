package com.organic.maynard.util.crawler;

import java.io.*;
import java.util.*;
import com.organic.maynard.io.*;
import com.organic.maynard.util.string.*;

public class SimpleFind {

    public SimpleFind(String args[]) {
        String startingPath = ConsoleTools.getNonEmptyInput("Enter starting path: ");
        String query = ConsoleTools.getNonEmptyInput("Enter string to find: ");
        String[] fileExtensions = ConsoleTools.getSeriesOfInputs("Enter file extension to match: ");
        while (fileExtensions.length <= 0) {
            fileExtensions = ConsoleTools.getSeriesOfInputs("Enter file extension to match: ");
        }
        System.out.println("");
        DirectoryCrawler crawler = new DirectoryCrawler();
        crawler.setFileHandler(new SimpleFindFileContentsHandler(query, FileTools.LINE_ENDING_WIN));
        crawler.setFileFilter(new FileExtensionFilter(fileExtensions));
        crawler.setVerbose(false);
        System.out.println("STARTING...");
        int status = crawler.crawl(startingPath);
        if (status == DirectoryCrawler.SUCCESS) {
            int totalMatches = ((SimpleFindFileContentsHandler) crawler.getFileHandler()).getTotalNumberOfMatches();
            System.out.println("Total Matches Found: " + totalMatches);
        }
        System.out.println("DONE");
    }

    public static void main(String args[]) {
        SimpleFind sf = new SimpleFind(args);
    }
}
