package com.byjyate.rssdreamwork;

public class RssTransformerBuilder {

    public static RssTransformer getRssTransformer(String command) {
        if (command.startsWith("fetchrss")) return new FetchRssTransformer(command.replace("fetchrss ", ""));
        if (command.startsWith("fetchpage")) return new FetchPageTransformer();
        if (command.startsWith("addbefore ")) return new AddBeforeTransformer(command.replace("addbefore ", ""));
        if (command.startsWith("addafter ")) return new AddAfterTransformer(command.replace("addafter ", ""));
        if (command.startsWith("removebeforeincself ")) return new RemoveBeforeIncSelfTransformer(command.replace("removebeforeincself ", ""));
        if (command.startsWith("removeafterincself ")) return new RemoveAfterIncSelfTransformer(command.replace("removeafterincself ", ""));
        if (command.startsWith("removebefore ")) return new RemoveBeforeTransformer(command.replace("removebefore ", ""));
        if (command.startsWith("removeafter ")) return new RemoveAfterTransformer(command.replace("removeafter ", ""));
        if (command.startsWith("itemfilter ")) return new ItemFilterTransformer(command.replace("itemfilter ", ""));
        if (command.startsWith("itemurlfilter ")) return new ItemUrlFilterTransformer(command.replace("itemurlfilter ", ""));
        if (command.startsWith("itemtimefilter ")) return new ItemTimeFilterTransformer(command.replace("itemtimefilter ", ""));
        if (command.startsWith("removeredundancy")) return new RemoveRedundancyTransformer();
        if (command.startsWith("replaceall ")) return new ReplaceAllTransformer(command.replace("replaceall ", ""));
        if (command.startsWith("replacefirst ")) return new ReplaceFirstTransformer(command.replace("replacefirst ", ""));
        if (command.startsWith("timezone ")) return new TimezoneTransformer(command.replace("timezone ", ""));
        if (command.startsWith("processphoenix")) return new ProcessPhoenixTransformer();
        if (command.startsWith("fetchcarnocrss")) return new FetchCarnocRss(command.replace("fetchcarnocrss ", ""));
        return null;
    }

    private RssTransformerBuilder() {
    }
}
