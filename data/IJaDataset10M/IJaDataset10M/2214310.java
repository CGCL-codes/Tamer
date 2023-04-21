package tr.edu.metu.srdc.utils.xpath;

public class PathParser {

    public PathParser() {
    }

    public static String pathParser(String path) {
        String newPath = path;
        while (newPath.contains(":")) {
            String lastPart = newPath.substring(newPath.indexOf(':') + 1);
            String firstPart = newPath.substring(0, newPath.indexOf(':'));
            String newFirstPart = firstPart.substring(0, firstPart.lastIndexOf('/') + 1);
            newPath = newFirstPart + lastPart;
        }
        return newPath;
    }
}
