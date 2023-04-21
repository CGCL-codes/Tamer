package org.wakhok.grammar;

import org.wakhok.space.PrivateRequest;
import org.wakhok.space.Request;
import org.wakhok.space.SpaceSearch;
import org.wakhok.utils.FileSearch;

/**
 *
 * @author bishal acharya : bishalacharya@gmail.com
 */
public class Grammar extends Thread {

    String query;

    String[] syntax = { "SELECT * FROM channel WHERE channelName", "DELETE FROM channel WHERE channelName", "INSERT" };

    String[] splitString;

    String channelName, email, password;

    Integer id;

    Request result;

    PrivateRequest result1;

    int typeOfSearch;

    Thread t;

    public Grammar(String query) {
        this.query = query;
        queryAnalyzer(query);
    }

    public void privateSearch() {
        if (!FileSearch.browserModel.contains(result1.id + " :" + result1.inputName)) {
            FileSearch.browserModel.addElement(result1.id + " :" + result1.inputName);
        }
    }

    public void channelSearch() {
        if (!FileSearch.browserModel.contains(result.id + " :" + result.inputName)) {
            FileSearch.browserModel.addElement(result.id + " :" + result.inputName);
        }
    }

    public void queryAnalyzer(String query) {
        splitString = query.split("=");
        channelName = splitString[1];
        if (splitString[0].equalsIgnoreCase(syntax[0])) {
            typeOfSearch = 3;
            if (splitString[1].contains("AND email")) {
                email = splitString[2];
                channelName = splitString[1].toString().replaceAll("AND email", " ").trim();
                FileSearch.channelName = channelName;
                typeOfSearch = 1;
            }
            if (splitString[1].contains("AND id")) {
                id = Integer.parseInt(splitString[2]);
                channelName = splitString[1].toString().replaceAll("AND id", " ").trim();
                FileSearch.channelName = channelName;
                typeOfSearch = 2;
            }
            if (splitString[1].contains("AND password")) {
                password = splitString[2];
                channelName = splitString[1].toString().replaceAll("AND password", " ").trim();
                FileSearch.channelName = channelName;
                typeOfSearch = 4;
            }
        } else if (splitString[0].equalsIgnoreCase(syntax[1])) {
            if (splitString[1].contains("AND id")) {
                id = Integer.parseInt(splitString[2]);
                channelName = splitString[1].toString().replaceAll("AND id", " ").trim();
                SpaceSearch objSearch = new SpaceSearch();
                if (channelName.equals("privateChannel")) {
                    objSearch.takePrivateItem(channelName, id);
                } else {
                    objSearch.takeItem(channelName, id);
                }
            }
        }
    }

    public void run() {
        SpaceSearch objSearch = new SpaceSearch();
        int counter = 0;
        while (true) {
            if (typeOfSearch == 1) {
                result = objSearch.Search(channelName, email);
                channelSearch();
            }
            if (typeOfSearch == 2) {
                result = objSearch.Search(channelName, id);
                channelSearch();
            }
            if (typeOfSearch == 3) {
                result = objSearch.Search(channelName);
                channelSearch();
            }
            if (typeOfSearch == 4) {
                result1 = objSearch.privateSearch(channelName, password);
                privateSearch();
            }
            FileSearch.objCountLabel.setText("No Of Objects Searched : " + String.valueOf(FileSearch.browserModel.size()));
            counter++;
        }
    }
}
