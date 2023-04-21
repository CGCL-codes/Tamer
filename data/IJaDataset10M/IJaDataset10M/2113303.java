package com.google.code.jspot;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *  This class provides access to the spotify metadata API
 */
public class Spotify {

    private Commander commander;

    public Spotify() throws IOException {
        commander = new Commander("spotify", "http://ws.spotify.com/", "");
        commander.setRetries(1);
        commander.setTimeout(10000);
        commander.setTraceSends(false);
    }

    /**
     * Searrch for an artist by name
     * @param query the name of the artist
     * @return a list of artists
     * @throws IOException
     */
    public Results<Artist> searchArtist(String query) throws IOException {
        return searchArtist(query, 1);
    }

    /**
     * Search for an artist by name
     * @param query the name of the artist
     * @param page the page number
     * @return a list of artists
     * @throws IOException
     */
    public Results<Artist> searchArtist(String query, int page) throws IOException {
        String encodedArtist = URLEncoder.encode(query, "UTF-8");
        Document doc = commander.sendCommand("search/1/artist?page=" + page + "&q=" + encodedArtist);
        Element docElement = doc.getDocumentElement();
        NodeList artists = docElement.getElementsByTagName("artist");
        int totalResults = XmlUtil.getDescendentTextAsInt(docElement, "opensearch:totalResults", -1);
        int startIndex = XmlUtil.getDescendentTextAsInt(docElement, "opensearch:startIndex", -1);
        int itemsPerPage = XmlUtil.getDescendentTextAsInt(docElement, "opensearch:itemsPerPage", -1);
        List<Artist> artistList = new ArrayList<Artist>();
        for (int i = 0; i < artists.getLength(); i++) {
            Element artistNode = (Element) artists.item(i);
            String id = artistNode.getAttribute("href");
            String name = XmlUtil.getDescendentText(artistNode, "name");
            float popularity = XmlUtil.getDescendentTextAsFloat(artistNode, "popularity", SpotifyItem.UNKNOWN_POPULARITY);
            Artist artist = new Artist(id, name, popularity);
            artistList.add(artist);
        }
        Results results = new Results(startIndex, totalResults, itemsPerPage, artistList);
        return results;
    }

    /**
     * search for albums
     * @param query the album name
     * @return results of the search
     * @throws IOException
     */
    public Results<Album> searchAlbum(String query) throws IOException {
        return searchAlbum(query, 1);
    }

    private Album parseAlbum(Element albumNode) throws IOException {
        String id = albumNode.getAttribute("href");
        String name = XmlUtil.getDescendentText(albumNode, "name");
        float popularity = XmlUtil.getDescendentTextAsFloat(albumNode, "popularity", SpotifyItem.UNKNOWN_POPULARITY);
        Album album = new Album(id, name, popularity);
        Element artistNode = (Element) XmlUtil.getDescendent(albumNode, "artist");
        if (artistNode != null) {
            String artistID = artistNode.getAttribute("href");
            String artistName = XmlUtil.getDescendentText(artistNode, "name");
            album.setArtistID(artistID);
            album.setArtistName(artistName);
        }
        Element availability = (Element) XmlUtil.getDescendent(albumNode, "availability");
        String territories = XmlUtil.getDescendentText(availability, "territories");
        String[] tfields = territories.split("\\s+");
        int released = XmlUtil.getDescendentTextAsInt(albumNode, "released", -1);
        NodeList idNodes = albumNode.getElementsByTagName("id");
        for (int j = 0; j < idNodes.getLength(); j++) {
            Element idNode = (Element) idNodes.item(j);
            String idType = idNode.getAttribute("type");
            String idVal = idNode.getTextContent();
            album.addID(idType, idVal);
        }
        album.setAvailableTerritories(tfields);
        album.setReleased(released);
        return album;
    }

    /**
     * search for an album
     * @param query the query
     * @param page the page of the results 
     * @return the search results
     * @throws IOException
     */
    public Results<Album> searchAlbum(String query, int page) throws IOException {
        String encodedAlbum = URLEncoder.encode(query, "UTF-8");
        Document doc = commander.sendCommand("search/1/album?page=" + page + "&q=" + encodedAlbum);
        Element docElement = doc.getDocumentElement();
        NodeList albums = docElement.getElementsByTagName("album");
        int totalResults = XmlUtil.getDescendentTextAsInt(docElement, "opensearch:totalResults", -1);
        int startIndex = XmlUtil.getDescendentTextAsInt(docElement, "opensearch:startIndex", -1);
        int itemsPerPage = XmlUtil.getDescendentTextAsInt(docElement, "opensearch:itemsPerPage", -1);
        List<Album> albumList = new ArrayList<Album>();
        for (int i = 0; i < albums.getLength(); i++) {
            Element albumNode = (Element) albums.item(i);
            Album album = parseAlbum(albumNode);
            albumList.add(album);
        }
        Results results = new Results(startIndex, totalResults, itemsPerPage, albumList);
        return results;
    }

    /**
     * Search for tracks 
     * @param artist the name of the artist
     * @param track the name of the track
     * @return results of the search
     * @throws IOException
     */
    public Results<Track> searchTrack(String artist, String track) throws IOException {
        String query = "artist:\"" + artist + "\"  track:\"" + track + "\"";
        return searchTrack(query, 1);
    }

    /**
     * Search for tracks
     * @param artist the name of the artist
     * @param track the name of the track
     * @param page the page number
     * @return results of the search
     * @throws IOException
     */
    public Results<Track> searchTrack(String artist, String track, int page) throws IOException {
        String query = "artist:\"" + artist + "\"  track:\"" + track + "\"";
        return searchTrack(query, page);
    }

    /**
     * Search for tracks
     * @param artist the artist
     * @param album the album
     * @param track the track
     * @return the results of the search
     * @throws IOException
     */
    public Results<Track> searchTrack(String artist, String album, String track) throws IOException {
        String query = "artist:\"" + artist + "\" album:\"" + album + "\"  track:\"" + track + "\"";
        return searchTrack(query, 1);
    }

    /**
     * search for tracks
     * @param query the searc query
     * @return the results of the search
     * @throws IOException
     */
    public Results<Track> searchTrack(String query) throws IOException {
        return searchTrack(query, 1);
    }

    /**
     * search for tracks
     * @param query the search query
     * @param page the page number
     * @return the search results
     * @throws IOException
     */
    public Results<Track> searchTrack(String query, int page) throws IOException {
        String encodedTrack = URLEncoder.encode(query, "UTF-8");
        Document doc = commander.sendCommand("search/1/track?page=" + page + "&q=" + encodedTrack);
        Element docElement = doc.getDocumentElement();
        NodeList tracks = docElement.getElementsByTagName("track");
        int totalResults = XmlUtil.getDescendentTextAsInt(docElement, "opensearch:totalResults", -1);
        int startIndex = XmlUtil.getDescendentTextAsInt(docElement, "opensearch:startIndex", -1);
        int itemsPerPage = XmlUtil.getDescendentTextAsInt(docElement, "opensearch:itemsPerPage", -1);
        List<Track> trackList = new ArrayList<Track>();
        for (int i = 0; i < tracks.getLength(); i++) {
            Element trackNode = (Element) tracks.item(i);
            String trackID = trackNode.getAttribute("href");
            String trackName = XmlUtil.getDescendentText(trackNode, "name");
            float trackPopularity = XmlUtil.getDescendentTextAsFloat(trackNode, "popularity", SpotifyItem.UNKNOWN_POPULARITY);
            Element artistNode = (Element) XmlUtil.getDescendent(trackNode, "artist");
            String artistID = artistNode.getAttribute("href");
            String artistName = XmlUtil.getDescendentText(artistNode, "name");
            Element albumNode = (Element) XmlUtil.getDescendent(trackNode, "album");
            Album album = parseAlbum(albumNode);
            album.setArtistID(artistID);
            album.setArtistName(artistName);
            int trackNumber = XmlUtil.getDescendentTextAsInt(trackNode, "track-number", -1);
            float trackLength = XmlUtil.getDescendentTextAsFloat(trackNode, "length", -1);
            Track track = new Track(trackID, trackName, trackPopularity);
            track.setAlbum(album);
            track.setArtistId(artistID);
            track.setArtistName(artistName);
            track.setTrackNumber(trackNumber);
            track.setLength(trackLength);
            NodeList idNodes = trackNode.getElementsByTagName("id");
            for (int j = 0; j < idNodes.getLength(); j++) {
                Element idNode = (Element) idNodes.item(j);
                String idType = idNode.getAttribute("type");
                String idVal = idNode.getTextContent();
                track.addID(idType, idVal);
            }
            trackList.add(track);
        }
        Results results = new Results(startIndex, totalResults, itemsPerPage, trackList);
        return results;
    }

    private static void test() throws IOException {
        Spotify spotify = new Spotify();
        System.out.println(spotify.searchArtist("weezer"));
        System.out.println(spotify.searchTrack("My Name Is Jonas"));
        System.out.println(spotify.searchTrack("miley cyrus", "goodbye"));
        System.out.println(spotify.searchTrack("The Nice", "America"));
        System.out.println(spotify.searchTrack("The Nice", "Elegy", "America"));
        Results<Track> results = null;
        int page = 1;
        int count = 1;
        do {
            results = spotify.searchTrack("artist:weezer", page);
            page = results.getNextPage();
            for (Track track : results.getItems()) {
                System.out.printf("%d %s\n", count++, track.getName());
            }
        } while (!results.isLast());
    }

    private static String concat(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String s : args) {
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: Spotify artist name");
        } else {
            String artist = concat(args);
            Spotify spotify = new Spotify();
            Results<Track> results = null;
            int page = 1;
            do {
                results = spotify.searchTrack("artist:" + artist, page);
                page = results.getNextPage();
                for (Track track : results.getItems()) {
                    System.out.printf("%.2f %s // %s // %s\n", track.getPopularity(), track.getArtistName(), track.getAlbum().getName(), track.getName());
                }
            } while (!results.isLast());
        }
    }
}
