package model.retrieval;

import java.io.*;
import java.net.*;
import java.util.*;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.dom.BibtexString;
import bibtex.parser.ParseException;
import model.storage.Artifact;
import model.storage.Author;

/**
 * Class handles parsing of BiBTeX formatted data into an Article object.
 * 
 * @author Wilan, jsashina
 */
public class ParserBibtex {

    /**
	 * Method which parses BiBTex formatted data into an Article object.
	 * 
	 * @param text A formatted BiBTex string to be parsed
	 * @return An Article object
	 */
    public static Artifact parse(String text) {
        Artifact ret = new Artifact();
        text = text.replaceAll("<br>", "\n");
        try {
            StringReader in = new StringReader(text);
            bibtex.parser.BibtexParser parser = new bibtex.parser.BibtexParser(false);
            BibtexFile file = new BibtexFile();
            parser.parse(file, in);
            for (Iterator it = file.getEntries().iterator(); it.hasNext(); ) {
                Object potentialEntry = it.next();
                if (!(potentialEntry instanceof BibtexEntry)) continue;
                BibtexEntry entry = (BibtexEntry) potentialEntry;
                BibtexString authorString = (BibtexString) entry.getFieldValue("author");
                if (authorString != null) {
                    ArrayList<String> list = new ArrayList<String>();
                    String content = authorString.getContent();
                    String tokens[] = content.split("\\s++");
                    String auth = new String();
                    for (int i = 0; i < tokens.length; i++) {
                        if (tokens[i].toLowerCase().equals("and")) {
                            list.add(auth);
                            auth = new String();
                            continue;
                        } else if (tokens[i].toLowerCase().equals("others")) {
                            auth = new String();
                        }
                        auth += (tokens[i] + " ");
                    }
                    if (auth.length() > 0) {
                        list.add(auth);
                    }
                    ArrayList<Author> authorSet = new ArrayList<Author>();
                    for (int i = 0; i < list.size(); i++) {
                        authorSet.add(new Author(list.get(i).trim()));
                    }
                    ret.setAuthors(authorSet);
                }
                BibtexString titleString = (BibtexString) entry.getFieldValue("title");
                if (titleString != null) {
                    String content = titleString.getContent();
                    ret.setTitle(content);
                }
                BibtexString journalString = (BibtexString) entry.getFieldValue("journal");
                if (journalString != null) {
                    ret.setJournal(journalString.getContent());
                }
                BibtexString abstractString = (BibtexString) entry.getFieldValue("abstract");
                if (abstractString != null) {
                    ret.setAbstract(abstractString.getContent());
                }
                BibtexString doiString = (BibtexString) entry.getFieldValue("doi");
                if (doiString != null) {
                    ret.setDOI(doiString.getContent());
                }
                BibtexString yearString = (BibtexString) entry.getFieldValue("year");
                if (yearString != null) {
                    String content = yearString.getContent();
                    String yearValue = new String();
                    for (int k = 0; k < content.length(); k++) {
                        if (Character.isDigit(content.charAt(k))) {
                            yearValue += content.charAt(k);
                        }
                    }
                    ret.setYear(Integer.parseInt(yearValue));
                }
                BibtexString numberString = (BibtexString) entry.getFieldValue("number");
                if (numberString != null) {
                    if (!numberString.getContent().isEmpty()) {
                        ret.setNumber(Integer.parseInt(numberString.getContent()));
                    }
                }
                BibtexString pageString = (BibtexString) entry.getFieldValue("pages");
                if (pageString != null) {
                    String pageData = pageString.getContent();
                    String modifiedPages = new String();
                    for (int i = 0; i < pageData.length(); i++) {
                        if (!Character.isDigit(pageData.charAt(i))) {
                            modifiedPages += " ";
                        } else {
                            modifiedPages += pageData.charAt(i);
                        }
                    }
                    modifiedPages = modifiedPages.trim();
                    String[] content = modifiedPages.split("[ ]+");
                    if (content.length == 2) {
                        ArrayList<Integer> pageSet = new ArrayList<Integer>();
                        for (int i = 0; i < content.length; i++) {
                            content[i] = content[i].trim();
                            if (!content[i].isEmpty()) {
                                pageSet.add(Integer.parseInt(content[i]));
                            }
                        }
                        ret.setPages(pageSet);
                    } else {
                        ret.setPages(new ArrayList<Integer>());
                    }
                }
                BibtexString publisherString = (BibtexString) entry.getFieldValue("publisher");
                if (publisherString != null) {
                    ret.setPublisher(publisherString.getContent());
                }
                BibtexString urlString = (BibtexString) entry.getFieldValue("url");
                if (urlString != null) {
                    ret.setArtifactPageURL(urlString.getContent());
                }
                BibtexString volumeString = (BibtexString) entry.getFieldValue("volume");
                if (volumeString != null) {
                    int volume = 0;
                    for (int i = 0; i < volumeString.getContent().length(); i++) {
                        if (Character.isDigit(volumeString.getContent().charAt(i))) {
                            volume = volume * 10 + (volumeString.getContent().charAt(i) - '0');
                        }
                    }
                    ret.setVolume(volume);
                }
            }
        } catch (IOException e) {
            System.err.println("IOException");
        } catch (ParseException e) {
            System.err.println("ParseException!");
        }
        return ret;
    }
}
