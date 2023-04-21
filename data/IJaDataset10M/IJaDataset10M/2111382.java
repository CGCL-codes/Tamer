package dataLeaf;

import java.util.Date;
import java.text.*;
import java.sql.*;

class Interface {

    private Search currentSearch;

    private User theUser = User.getInstance();

    private DatabaseConnection connection;

    private Subject currentSubject;

    private Observation currentObservation;

    private int dbID;

    private Subject[] arrOfSubjects;

    private Observation[] arrOfObs;

    private Location loc;

    private prototype newGui;

    private String userpass;

    public Search getSearch() {
        return currentSearch;
    }

    public User getUser() {
        return theUser;
    }

    public DatabaseConnection getDBCon() {
        return connection;
    }

    public Subject getCurSubject() {
        return currentSubject;
    }

    public Observation getObs() {
        return currentObservation;
    }

    public Interface(prototype gui) {
        newGui = gui;
        theUser = User.getInstance();
        System.out.println("finishing constructor");
    }

    public void executeLogin() {
        theUser.setAlias((String) newGui.usernameTextBox.getText());
        theUser.setPassword(userpass = newGui.passwordTextBox.getText());
        connection.getInstance(dbID);
        connection = DatabaseConnection.getInstance(1);
        boolean result = theUser.login(connection);
        if (result) newGui.dateTextBox.setText("User: " + theUser.getAlias()); else newGui.dateTextBox.setText("no result");
    }

    public void executeLogout() {
        theUser.reset();
    }

    public void executeSearch() {
        System.out.println("Interface executeSearch");
        connection = DatabaseConnection.getInstance(1);
        currentSearch.executeSearch(connection);
        arrOfSubjects = currentSearch.getSubjectResults();
        String arrSpecies[] = new String[10];
        for (int i = 0; i < arrOfSubjects.length; i++) {
            arrSpecies[i] = arrOfSubjects[i].getSpecies();
        }
        newGui.speciesResultList.setListData(arrSpecies);
    }

    public void buildSearch() {
        System.out.println("Interface buildSearch");
        String spec = (String) newGui.speciesDropBox.getSelectedItem();
        String gen = (String) newGui.genusDropBox.getSelectedItem();
        String obs = (String) newGui.observerDropBox.getSelectedItem();
        String newDate = new String();
        int elev = 0;
        currentSearch = new Search(spec, gen, obs, newDate);
        System.out.println("Back in buildSearch after Search constructor");
    }

    private boolean validateLoginInput() {
        return true;
    }

    private boolean validateSearchInput() {
        return true;
    }

    private boolean verifyAccessLevel() {
        return true;
    }
}
