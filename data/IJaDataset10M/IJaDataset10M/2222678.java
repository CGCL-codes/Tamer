package com.web.music.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import com.web.music.interfaces.HTTPClient;
import com.web.music.model.Album;
import com.web.music.model.Artist;
import com.web.music.model.Catalog;
import com.web.music.model.Genre;
import com.web.music.model.Person;

/**
 * Loads XML files from remote URL
 */
public class RemoteMusicProcessor extends BaseMusicProcessor {

    String personsUrl;

    String catalogUrl;

    private HTTPClient httpClient;

    public void execute() {
        super.execute();
        InputStream personsXml = getHttpClient().downloadToInputStream(personsUrl);
        Map<String, Person> persons = getXmlParser().extractPersons(personsXml);
        getPersistence().saveAll(new ArrayList<Person>(persons.values()));
        InputStream catalogXml = getHttpClient().downloadToInputStream(catalogUrl);
        Catalog catalog = getXmlParser().extractCatalog(catalogXml, persons);
        getPersistence().saveAll(new ArrayList<Genre>(catalog.getGenres()));
        getPersistence().saveAll(new ArrayList<Artist>(catalog.getArtists()));
        getPersistence().saveAll(new ArrayList<Album>(catalog.getAlbums()));
    }

    public String getPersonsUrl() {
        return personsUrl;
    }

    public void setPersonsUrl(String personsUrl) {
        this.personsUrl = personsUrl;
    }

    public String getCatalogUrl() {
        return catalogUrl;
    }

    public void setCatalogUrl(String catalogUrl) {
        this.catalogUrl = catalogUrl;
    }

    public HTTPClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }
}
