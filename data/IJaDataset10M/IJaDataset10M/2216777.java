package bot;

import java.util.*;
import java.util.regex.*;
import util.Logger;

public class HTML {

    private static final Pattern upgradeLink = Pattern.compile("dorf[12].php\\?a=\\d+&amp;c=\\w+");

    private static final Pattern resourceSection = Pattern.compile("(?s)<b>Costs</b>(.+?)</div>");

    public static ParseResults parseField(String field) {
        ParseResults results = parseResourceProduction(field);
        int constructBuildingIndex = field.indexOf("Construct a new building");
        if (constructBuildingIndex == -1) {
            results.add("EmptyField", false);
            Matcher upgradeLinkM = upgradeLink.matcher(field);
            if (upgradeLinkM.find()) {
                results.add("CanUpgrade", true);
                results.add("UpgradeLink", "/" + upgradeLinkM.group().replaceAll("&amp;", "&"));
            } else {
                results.add("CanUpgrade", false);
                Matcher resourceSectionM = resourceSection.matcher(field);
                resourceSectionM.find();
                results.merge(parseResourcesNeeded(resourceSectionM.group(1)));
            }
        } else {
            results.add("EmptyField", true);
        }
        return results;
    }

    public static ParseResults parseFieldToBuild(String field, int bId) {
        ParseResults results = parseField(field);
        if (!(Boolean) results.get("EmptyField")) {
            throw new IllegalArgumentException("Field is not empty.  Can't build on it.");
        }
        Pattern buildLink = Pattern.compile("dorf2.php\\?a=" + bId + "&amp;id=\\d+&amp;c=\\w+");
        Matcher buildLinkM = buildLink.matcher(field);
        if (buildLinkM.find()) {
            results.add("CanBuild", true);
            results.add("BuildLink", "/" + buildLinkM.group().replaceAll("&amp;", "&"));
        } else {
            results.add("CanBuild", false);
            Pattern buildResourceSection = Pattern.compile("(?s)g" + bId + "\"(.+?)\"duration\"");
            Matcher buildResourceSectionM = buildResourceSection.matcher(field);
            buildResourceSectionM.find();
            results.merge(parseResourcesNeeded(buildResourceSectionM.group(1)));
        }
        return results;
    }

    private static final Pattern resourceNeeded = Pattern.compile("<img class=\"r[1234]\".*?/>(?:<span class=\"little_res\">)?(\\d+)(?:</span>| \\|)");

    private static ParseResults parseResourcesNeeded(String resourceSection) {
        ParseResults results = new ParseResults();
        Matcher resourceNeededM = resourceNeeded.matcher(resourceSection);
        resourceNeededM.find();
        results.add("WoodNeeded", Integer.valueOf(resourceNeededM.group(1)));
        resourceNeededM.find();
        results.add("ClayNeeded", Integer.valueOf(resourceNeededM.group(1)));
        resourceNeededM.find();
        results.add("IronNeeded", Integer.valueOf(resourceNeededM.group(1)));
        resourceNeededM.find();
        results.add("WheatNeeded", Integer.valueOf(resourceNeededM.group(1)));
        return results;
    }

    private static final Pattern woodProduction = Pattern.compile("<td id=\"l4\" title=\"(\\d+)\">(\\d+)/");

    private static final Pattern clayProduction = Pattern.compile("<td id=\"l3\" title=\"(\\d+)\">(\\d+)/");

    private static final Pattern ironProduction = Pattern.compile("<td id=\"l2\" title=\"(\\d+)\">(\\d+)/");

    private static final Pattern wheatProduction = Pattern.compile("<td id=\"l1\" title=\"(\\d+)\">(\\d+)/");

    private static ParseResults parseResourceProduction(String field) {
        ParseResults results = new ParseResults();
        Matcher woodProductionM = woodProduction.matcher(field);
        woodProductionM.find();
        results.add("WoodProduction", Integer.valueOf(woodProductionM.group(1)));
        results.add("WoodStock", Integer.valueOf(woodProductionM.group(2)));
        Matcher clayProductionM = clayProduction.matcher(field);
        clayProductionM.find();
        results.add("ClayProduction", Integer.valueOf(clayProductionM.group(1)));
        results.add("ClayStock", Integer.valueOf(clayProductionM.group(2)));
        Matcher ironProductionM = ironProduction.matcher(field);
        ironProductionM.find();
        results.add("IronProduction", Integer.valueOf(ironProductionM.group(1)));
        results.add("IronStock", Integer.valueOf(ironProductionM.group(2)));
        Matcher wheatProductionM = wheatProduction.matcher(field);
        wheatProductionM.find();
        results.add("WheatProduction", Integer.valueOf(wheatProductionM.group(1)));
        results.add("WheatStock", Integer.valueOf(wheatProductionM.group(2)));
        return results;
    }

    public static class ParseResults {

        private final HashMap<String, Object> results = new HashMap<String, Object>();

        public Object get(String resultName) {
            return results.get(resultName);
        }

        public void add(String name, Object value) {
            results.put(name, value);
        }

        public void merge(ParseResults other) {
            results.putAll(other.results);
        }
    }
}
