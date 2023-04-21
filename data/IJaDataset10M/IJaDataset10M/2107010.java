package jtwittconsolepackage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class PrettyPrinter is used for a nicer output on the console. It adds a
 * frame to the message, cuts the message after a defined number of chars into
 * several pieces and uses the user colors handled by the ColorManager to display
 * twitter messages.
 */
public class PrettyPrinter {

    private final int FIXEDSTRINGLENGTH = 73;

    private TwitterAppRepository twitterAppRepository;

    public PrettyPrinter() {
        twitterAppRepository = TwitterAppRepository.getInstance();
    }

    /**
     * Cuts a string to a length under FIXEDSTRINGLENTH-2.
     * The two chars are for the leading and finishing blank.
     * @param stringToCut This string should be cut.
     * @return All resulting strings.
     */
    private List<String> cutString(String stringToCut) {
        List<String> returnString = new LinkedList<String>();
        int lengthCounter = 0;
        int cutPosition = 0;
        boolean done = false;
        stringToCut = stringToCut.trim();
        while (!done) {
            while (lengthCounter < FIXEDSTRINGLENGTH - 2) {
                if (stringToCut.charAt(lengthCounter) == ' ') {
                    cutPosition = lengthCounter;
                }
                if (lengthCounter == stringToCut.length() - 1) {
                    cutPosition = lengthCounter;
                    done = true;
                    break;
                }
                lengthCounter++;
            }
            if (!done) {
                returnString.add(stringToCut.substring(0, cutPosition));
            } else {
                returnString.add(stringToCut.substring(0, cutPosition + 1));
            }
            stringToCut = stringToCut.substring(cutPosition + 1);
            lengthCounter = 0;
            cutPosition = 0;
        }
        return returnString;
    }

    /**
     * Extracts the name of the source program from a longer String.
     * The input String could be a long href. If it isn't a href the function returns the input.
     * @param possibleHrefString the source value of a tweet
     * @return only the name of the application that has been used to create the tweet
     */
    private String extractSourceAppNameFromString(String possibleHrefString) {
        String resultingString = possibleHrefString;
        Boolean linkDetected = false;
        Pattern pattern = Pattern.compile("]*href=\"?[^(>| )]*\"?[^>]*>");
        Matcher matcher = pattern.matcher(possibleHrefString);
        if (twitterAppRepository.contains(possibleHrefString)) {
            resultingString = twitterAppRepository.get(possibleHrefString);
        } else {
            while (matcher.find()) {
                linkDetected = true;
                resultingString = resultingString.substring(matcher.end(), resultingString.length() - 1);
            }
            if (linkDetected) {
                resultingString = resultingString.substring(0, resultingString.length() - 3);
                twitterAppRepository.put(possibleHrefString, resultingString);
            }
        }
        return resultingString;
    }

    /**
     * Extends a string to FIXEDSTRINGLENGTH
     * @param shortString The string which should be extended;
     * @return The extended string with a length of FIXEDSTRINGLENGTH
     */
    private String fillStringWithBlanks(String shortString) {
        shortString = shortString.trim();
        shortString = " ".concat(shortString);
        while (shortString.length() < FIXEDSTRINGLENGTH) {
            shortString = shortString.concat(" ");
        }
        return shortString;
    }

    /**
     * The method treats strings of all lengths. It calls methods to shorten or to
     * enlarge the given string to the needed size. Strings in the return list
     * are having a length of FIXEDSTRINGLENGTH.
     * @param stringToHandle The string which has be to cut or extended.
     * @return A string of an appropriate length.
     */
    private List<String> handleStringLength(String stringToHandle) {
        List<String> returnString = new LinkedList<String>();
        if (stringToHandle.length() >= (FIXEDSTRINGLENGTH - 2)) {
            List<String> tempStringList = cutString(stringToHandle);
            for (String s : tempStringList) {
                returnString.add(fillStringWithBlanks(s));
            }
        } else {
            returnString.add(fillStringWithBlanks(stringToHandle));
        }
        return returnString;
    }

    /**
     * This method is used for searches - they return tweets instead of status -> so no favorite info is available.
     * Prints a twitter message on the console.
     * @param author The name of the twitter user who has written the message.
     * @param authorColorCode The color code for the specified author.
     * @param message The message itself.
     * @param date The date the message has been written.
     * @param inReplyName Shows the user who has been replied.
     * @param sourceApp the app that had been used to right the message
     * @param id the tweet id
     */
    public void printWholeTwitterMessage(String author, String authorColorCode, String message, String date, String inReplyName, String sourceApp, Long id, TweetRepository tweetRepository) {
        printWholeTwitterMessage(author, authorColorCode, message, date, inReplyName, sourceApp, id, false, tweetRepository);
    }

    /**
     * Prints a twitter message on the console.
     * @param author The name of the twitter user who has written the message.
     * @param authorColorCode The color code for the specified author.
     * @param message The message itself.
     * @param date The date the message has been written.
     * @param inReplyName Shows the user who has been replied.
     * @param sourceApp the app that had been used to right the message
     * @param id the tweet id
     * @param isFavorited if the tweet is a favorite or not
     * @param tweetRepository the repository which is used to store and read the tweets
     */
    public void printWholeTwitterMessage(String author, String authorColorCode, String message, String date, String inReplyName, String sourceApp, Long id, boolean isFavorited, TweetRepository tweetRepository) {
        if (!tweetRepository.containsTweet(id)) {
            String favoriteStar = "";
            if (isFavorited) favoriteStar = " *";
            System.out.println("/------------------------------------------------------------------------\\");
            System.out.println("|" + authorColorCode + fillStringWithBlanks(author + favoriteStar) + "\033[0m" + "|");
            for (String s : handleStringLength(message)) {
                System.out.println("|" + s + "|");
            }
            System.out.println("|" + fillStringWithBlanks(date + " (via " + extractSourceAppNameFromString(sourceApp) + ")") + "|");
            if (inReplyName != null && !inReplyName.equals("")) {
                System.out.println("|" + fillStringWithBlanks("in reply to " + inReplyName) + "|");
            }
            System.out.println("|" + fillStringWithBlanks("ID: " + id.toString()) + "|");
            System.out.println("\\------------------------------------------------------------------------/");
            ArrayList<String> singlePartsOfTheTweet = new ArrayList<String>();
            singlePartsOfTheTweet.add("/------------------------------------------------------------------------\\");
            singlePartsOfTheTweet.add("|" + authorColorCode + fillStringWithBlanks(author + favoriteStar) + "\033[0m" + "|");
            for (String s : handleStringLength(message)) {
                singlePartsOfTheTweet.add("|" + s + "|");
            }
            singlePartsOfTheTweet.add("|" + fillStringWithBlanks(date + " (via " + extractSourceAppNameFromString(sourceApp) + ")") + "|");
            if (inReplyName != null && !inReplyName.equals("")) {
                singlePartsOfTheTweet.add("|" + fillStringWithBlanks("in reply to " + inReplyName) + "|");
            }
            singlePartsOfTheTweet.add("|" + fillStringWithBlanks("ID: " + id.toString()) + "|");
            singlePartsOfTheTweet.add("\\------------------------------------------------------------------------/");
            TweetReadyForPrint tweetReadyForPrint = new TweetReadyForPrint(singlePartsOfTheTweet, id);
            tweetRepository.addTweet(tweetReadyForPrint);
        } else {
            if (tweetRepository.storedTweets.get(id) != null) {
                for (String partOfTweet : tweetRepository.storedTweets.get(id).tweetSplittedForPrint) System.out.println(partOfTweet);
                tweetRepository.storedTweets.get(id).setRefreshedInLastRun();
            }
        }
    }
}
