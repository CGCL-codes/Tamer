package be.roam.drest.youtube;

import java.io.IOException;
import java.util.List;
import org.xml.sax.SAXException;
import be.roam.drest.service.youtube.YouTubeService;
import be.roam.drest.service.youtube.YouTubeUserProfile;
import be.roam.drest.service.youtube.YouTubeVideo;
import be.roam.util.CollectionsUtil;

public class YouTubeReader {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Arguments (at least): <developer id> <method>");
            System.out.println("Possible methods: ");
            System.out.println("\t- profile <user name>");
            System.out.println("\t- favs <user name>");
            System.out.println("\t- friends <user name>");
            System.out.println("\t- videodetails <video id>");
            System.out.println("\t- tag <tag> [page nr] [results per page]");
            System.out.println("\t- videos <user name>");
            System.out.println("\t- featured");
            return;
        }
        YouTubeService service = new YouTubeService(args[0]);
        if ("profile".equals(args[1])) {
            showProfile(service, boundCheckArguments(args, 2));
        } else if ("favs".equals(args[1])) {
            showFavorites(service, boundCheckArguments(args, 2));
        } else if ("friends".equals(args[1])) {
            showFriends(service, boundCheckArguments(args, 2));
        } else if ("videodetails".equals(args[1])) {
            showVideoDetails(service, boundCheckArguments(args, 2));
        } else if ("tag".equals(args[1])) {
            Integer pageNr = null;
            Integer resultsPerPage = null;
            if (args.length > 4) {
                pageNr = new Integer(args[3]);
                resultsPerPage = new Integer(args[4]);
            } else if (args.length > 3) {
                pageNr = new Integer(args[3]);
            }
            showVideosByTag(service, boundCheckArguments(args, 2), pageNr, resultsPerPage);
        } else if ("videos".equals(args[1])) {
            showVideosFromUser(service, boundCheckArguments(args, 2));
        } else if ("featured".equals(args[1])) {
            showFeaturedVideos(service);
        }
    }

    private static void showProfile(YouTubeService service, String userName) throws IOException, SAXException {
        YouTubeUserProfile profile = service.getUserProfile(userName);
        printProfile(profile);
    }

    private static void showFavorites(YouTubeService service, String userName) throws IOException, SAXException {
        List<YouTubeVideo> videoList = service.getFavoriteVideos(userName);
        if (CollectionsUtil.isNullOrEmpty(videoList)) {
            System.out.println("No favorites found for " + userName);
        } else {
            System.out.println("Favorites for " + userName);
            for (YouTubeVideo video : videoList) {
                printVideo(video);
            }
        }
    }

    private static void showFriends(YouTubeService service, String userName) throws IOException, SAXException {
        List<YouTubeUserProfile> friendList = service.getFriendList(userName);
        if (CollectionsUtil.isNullOrEmpty(friendList)) {
            System.out.println(userName + " has no friends :(");
        } else {
            System.out.println("Friends of " + userName);
            for (YouTubeUserProfile friend : friendList) {
                printProfile(friend);
            }
        }
    }

    private static void showVideoDetails(YouTubeService service, String videoId) throws IOException, SAXException {
        YouTubeVideo video = service.getVideoDetails(videoId);
        printVideo(video);
    }

    private static void showVideosByTag(YouTubeService service, String tag, Integer pageNr, Integer resultsPerPage) throws IOException, SAXException {
        List<YouTubeVideo> videoList = service.getVideoListByTag(tag, pageNr, resultsPerPage);
        if (CollectionsUtil.isNullOrEmpty(videoList)) {
            System.out.println("No videos found for " + tag);
        } else {
            System.out.println("Videos tagged with " + tag);
            for (YouTubeVideo video : videoList) {
                printVideo(video);
            }
        }
    }

    private static void showVideosFromUser(YouTubeService service, String userName) throws IOException, SAXException {
        List<YouTubeVideo> videoList = service.getVideosByUser(userName);
        if (CollectionsUtil.isNullOrEmpty(videoList)) {
            System.out.println("No videos found by " + userName);
        } else {
            System.out.println("Videos by " + userName);
            for (YouTubeVideo video : videoList) {
                printVideo(video);
            }
        }
    }

    private static void showFeaturedVideos(YouTubeService service) throws IOException, SAXException {
        List<YouTubeVideo> videoList = service.getFeaturedVideos();
        if (CollectionsUtil.isNullOrEmpty(videoList)) {
            System.out.println("No featured videos found");
        } else {
            System.out.println("Featured videos:");
            for (YouTubeVideo video : videoList) {
                printVideo(video);
            }
        }
    }

    private static String boundCheckArguments(String[] array, int index) {
        if (array.length - 1 < index) {
            System.out.println("Missing argument");
            System.exit(0);
        }
        return array[index];
    }

    /**
     * @param profile
     */
    private static void printProfile(YouTubeUserProfile profile) {
        if (profile == null) {
            System.out.println("No profile information found");
        } else {
            System.out.println("Profile information for user \"" + profile.getUserName() + "\"");
            System.out.println("        Real name: " + profile.getFirstName() + " " + profile.getLastName());
            System.out.println("              Age: " + profile.getAge());
            System.out.println("           Gender: " + profile.getGender());
            System.out.println(" # videos watched: " + profile.getNrVideosWatched());
            System.out.println("# videos uploaded: " + profile.getNrVideosUploaded());
        }
    }

    private static void printVideo(YouTubeVideo video) {
        if (video == null) {
            System.out.println("No video");
        } else {
            System.out.println("Video: " + video.getTitle() + " (length: " + video.getLengthInSeconds() + ")");
            System.out.println("\tURL: " + video.getThumbnailUrl());
            System.out.println("\tDescription: " + video.getDescription());
        }
    }
}
