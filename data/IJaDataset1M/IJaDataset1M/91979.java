package com.kalessin.harvester;

/**
 * @author seretur Jorge Ramirez
 * @see Repositorysetter
 * 
 * this class knows specific data of SourceForge repository
 *
 */
public class SourceForgeSetter extends RepositorySetter {

    public SourceForgeSetter() {
        String baseAddress = "http://sourceforge.net/projects/";
        Site = "http://sourceforge.net";
        headers = "ID, Summary, Status, Opened, Assignee, Submitter, Resolution, Priority";
    }

    public String getProjectURL(String name) {
        String interm = "http://sourceforge.net/projects/" + name + "/develop";
        System.out.println(interm);
        return interm;
    }
}
