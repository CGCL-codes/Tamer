package info.sharo;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * @author Svetoslav Batchovski
 * @mailto: sharan4o@gmail.com
 * 
 */
public class MovieInfoParser {

    /**
	 * HashMap to collect the movieinfos
	 */
    public static HashMap<String, MovieInfo> movieInfoMap = new HashMap<String, MovieInfo>();

    /**
	 * filling the HashMap
	 * @param file
	 * @return
	 */
    public static HashMap<String, MovieInfo> parseMovieInfoXml(File file) {
        System.out.println("Parsing " + file.getName());
        List<MovieInfo> movieInfoList = new ArrayList<MovieInfo>();
        SAXBuilder reader = new SAXBuilder();
        Document doc;
        try {
            doc = reader.build(file);
            Element root = doc.getRootElement();
            List<Element> movieElementsList = root.getChild("movies").getChildren("movie");
            MovieInfo movieInfo;
            for (Element movieElement : movieElementsList) {
                if (!Pattern.compile(Pattern.quote("sample"), Pattern.CASE_INSENSITIVE).matcher(getNiceInfo(movieElement, "baseFilename")).find()) {
                    movieInfo = new MovieInfo();
                    movieInfo.setBaseFilename(getNiceInfo(movieElement, "baseFilename"));
                    movieInfo.setTitle(getNiceInfo(movieElement, "title"));
                    movieInfo.setDirector(getNiceInfo(movieElement, "director"));
                    movieInfo.setCast(parseCast(movieElement));
                    movieInfo.setGenre(parseGenre(movieElement));
                    movieInfo.setYear(getNiceInfo(movieElement, "year"));
                    movieInfo.setRuntime(getNiceInfo(movieElement, "runtime"));
                    movieInfo.setRating(parseRating(movieElement));
                    movieInfo.setSynopsis(getNiceInfo(movieElement, "plot"));
                    movieInfo.setAudio(parseAudio(movieElement));
                    movieInfo.setResolution(getNiceInfo(movieElement, "resolution"));
                    movieInfo.setCodec(getNiceInfo(movieElement, "videoCodec"));
                    movieInfo.setDir(parseDirectory(movieElement));
                    movieInfoMap.put(movieInfo.getBaseFilename(), movieInfo);
                    movieInfoList.add(movieInfo);
                }
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movieInfoMap;
    }

    /**
	 * get "nice" info. Filters text. Directorinfo is sometimes trash.
	 * if exception thrown (missing child) return emptystring
	 * @param element
	 * @param child
	 * @return
	 */
    private static String getNiceInfo(Element element, String child) {
        String info = "";
        try {
            if (!element.getChildText(child).contains("href")) info = element.getChildText(child);
        } catch (Exception e) {
        }
        return info;
    }

    /**
	 * parse cast children to string with commas
	 * @param element
	 * @return
	 */
    private static String parseCast(Element element) {
        String cast = "";
        try {
            List<Element> list = element.getChild("cast").getChildren("actor");
            for (Element actor : list) {
                cast = cast.concat(actor.getText() + ", ");
            }
            cast = cast.substring(0, cast.length() - 2);
        } catch (Exception e) {
            cast = "NA";
        }
        return cast;
    }

    /**
	 * parse rating from int to double
	 * @param element
	 * @return
	 */
    private static String parseRating(Element element) {
        String rating = "NA";
        try {
            rating = element.getChildText("rating");
            rating = rating.charAt(0) + "." + rating.substring(1);
        } catch (Exception e) {
            rating = "NA";
        }
        return rating;
    }

    /**
	 * parse genres children to string with commas
	 * @param element
	 * @return
	 */
    private static String parseGenre(Element element) {
        String genre = "";
        try {
            List<Element> list = element.getChild("genres").getChildren("genre");
            for (Element actor : list) {
                genre = genre.concat(actor.getText() + ", ");
            }
            genre = genre.substring(0, genre.length() - 2);
        } catch (Exception e) {
            genre = "NA";
        }
        return genre;
    }

    /**
	 * parse folder of the film and decode to UTF8
	 * @param element
	 * @return
	 */
    private static String parseDirectory(Element element) {
        String dir = "";
        try {
            dir = element.getChild("files").getChild("file").getChildText("fileURL");
            dir = dir.substring(0, dir.lastIndexOf("/"));
            dir = dir.substring(dir.lastIndexOf("/") + 1);
            dir = URLDecoder.decode(dir, "UTF8");
        } catch (Exception e) {
            dir = "";
        }
        return dir;
    }

    /**
	 * parse the audio information to formated string
	 * @param element
	 * @return
	 */
    private static String parseAudio(Element element) {
        String audio = "";
        try {
            audio = getNiceInfo(element, "audioCodec") + " ";
            audio = audio.concat(getNiceInfo(element, "audioChannels") + "ch");
        } catch (Exception e) {
            audio = "";
        }
        return audio;
    }
}
